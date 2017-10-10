// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.hadoop.distribution.dynamic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.DynamicServiceUtil;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.bean.TemplateBean;
import org.talend.hadoop.distribution.dynamic.resolver.DependencyResolverFactory;
import org.talend.hadoop.distribution.dynamic.resolver.IDependencyResolver;
import org.talend.hadoop.distribution.helper.HadoopDistributionsHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractDynamicDistributionsGroup implements IDynamicDistributionsGroup {

    private Map<IDynamicDistribution, List<String>> compatibleDistribuionVersionMap;

    private List<IDynamicPlugin> usersPluginsCache;

    private String usersPluginsCacheVersion;

    abstract protected Class<? extends IDynamicDistribution> getDynamicDistributionClass();

    @Override
    public List<String> getCompatibleVersions(IDynamicMonitor monitor) throws Exception {
        Set<String> compatibleVersions = new HashSet<>();
        compatibleDistribuionVersionMap = new HashMap<>();
        List<IDynamicDistribution> allRegistedDynamicDistributions = getAllRegistedDynamicDistributions(monitor);
        if (allRegistedDynamicDistributions != null) {
            for (IDynamicDistribution dynamicDistribution : allRegistedDynamicDistributions) {
                try {
                    List<String> curCompatibleVersions = dynamicDistribution.getCompatibleVersions(monitor);
                    if (curCompatibleVersions != null && !curCompatibleVersions.isEmpty()) {
                        compatibleDistribuionVersionMap.put(dynamicDistribution, curCompatibleVersions);
                        compatibleVersions.addAll(curCompatibleVersions);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        List<String> compatibleVersionList = new ArrayList<>(compatibleVersions);
        Collections.reverse(compatibleVersionList);
        return compatibleVersionList;
    }

    @Override
    public List<TemplateBean> getAllTemplates(IDynamicMonitor monitor) throws Exception {
        List<TemplateBean> templateBeans = new ArrayList<>();
        List<IDynamicDistribution> allRegistedDynamicDistributions = getAllRegistedDynamicDistributions(monitor);
        if (allRegistedDynamicDistributions != null) {
            for (IDynamicDistribution dynamicDistribution : allRegistedDynamicDistributions) {
                try {
                    List<TemplateBean> templates = dynamicDistribution.getTemplates(monitor);
                    templateBeans.addAll(templates);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return templateBeans;
    }

    @Override
    public IDynamicPlugin buildDynamicPlugin(IDynamicMonitor monitor, DynamicConfiguration configuration) throws Exception {
        String distribution = configuration.getDistribution();
        if (!StringUtils.equals(getDistribution(), distribution)) {
            throw new Exception("only support to build dynamic plugin of " + getDistribution() + " instead of " + distribution);
        }
        String version = configuration.getVersion();
        Set<Entry<IDynamicDistribution, List<String>>> entrySet = compatibleDistribuionVersionMap.entrySet();
        IDynamicDistribution bestDistribution = null;
        int distance = -1;
        for (Entry<IDynamicDistribution, List<String>> entry : entrySet) {
            List<String> list = entry.getValue();
            Collections.reverse(list);
            int size = list.size();
            int index = list.indexOf(version);
            int curDistance = size - index;
            if (distance < curDistance) {
                curDistance = distance;
                bestDistribution = entry.getKey();
            }
        }
        return bestDistribution.buildDynamicPlugin(monitor, configuration);
    }

    @Override
    public List<IDynamicPlugin> getAllUsersDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        if (usersPluginsCache != null) {
            String systemCacheVersion = HadoopDistributionsHelper.getCacheVersion();
            if (StringUtils.equals(systemCacheVersion, usersPluginsCacheVersion)) {
                return usersPluginsCache;
            }
        }
        usersPluginsCacheVersion = HadoopDistributionsHelper.getCacheVersion();

        List<IDynamicPlugin> dynamicPlugins = new LinkedList<>();

        ProjectManager projectManager = ProjectManager.getInstance();

        List<IDynamicPlugin> tempDynPlugins = getAllUsersDynamicPluginsForProject(projectManager.getCurrentProject(), monitor);
        if (tempDynPlugins != null && 0 < tempDynPlugins.size()) {
            dynamicPlugins.addAll(tempDynPlugins);
        }

        List<Project> allRefProjects = ProjectManager.getInstance().getAllReferencedProjects();
        if (allRefProjects != null && 0 < allRefProjects.size()) {
            for (Project refProject : allRefProjects) {
                try {
                    tempDynPlugins = getAllUsersDynamicPluginsForProject(refProject, monitor);
                    if (tempDynPlugins != null && 0 < tempDynPlugins.size()) {
                        dynamicPlugins.addAll(tempDynPlugins);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        usersPluginsCache = dynamicPlugins;

        return usersPluginsCache;
    }

    protected List<IDynamicPlugin> getAllUsersDynamicPluginsForProject(Project project, IDynamicMonitor monitor)
            throws Exception {
        IProject eProject = ResourceUtils.getProject(project);
        IFolder usersFolder = ResourceUtils.getFolder(eProject,
                RepositoryConstants.SETTING_DIRECTORY + "/" + getUsersFolderPath(), //$NON-NLS-1$
                false);
        if (usersFolder == null || !usersFolder.exists()) {
            return null;
        }

        List<IDynamicPlugin> dynamicPlugins = new ArrayList<>();

        IResource[] members = usersFolder.members();

        if (members != null && 0 < members.length) {
            for (int i = 0; i < members.length; ++i) {
                try {
                    String absolutePath = members[i].getLocation().toPortableString();
                    String jsonContent = DynamicServiceUtil.readFile(new File(absolutePath));
                    IDynamicPlugin dynamicPlugin = DynamicFactory.getInstance().createPluginFromJson(jsonContent);
                    dynamicPlugins.add(dynamicPlugin);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return dynamicPlugins;
    }

    @Override
    public List<IDynamicPlugin> getAllBuildinDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        List<IDynamicPlugin> dynamicPlugins = new ArrayList<>();
        List<IDynamicDistribution> allRegistedDynamicDistributions = getAllRegistedDynamicDistributions(monitor);
        if (allRegistedDynamicDistributions != null) {
            for (IDynamicDistribution dynamicDistribution : allRegistedDynamicDistributions) {
                try {
                    List<IDynamicPlugin> allBuildinDynamicPlugins = dynamicDistribution.getAllBuildinDynamicPlugins(monitor);
                    if (allBuildinDynamicPlugins != null) {
                        dynamicPlugins.addAll(allBuildinDynamicPlugins);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return dynamicPlugins;
    }

    @Override
    public List<String> getAllVersions(IDynamicMonitor monitor) throws Exception {
        DynamicConfiguration configuration = new DynamicConfiguration();
        configuration.setDistribution(getDistribution());
        IDependencyResolver resolver = DependencyResolverFactory.getInstance().getDependencyResolver(configuration);
        List<String> allVersions = resolver.listHadoopVersions(null, null, monitor);
        Collections.reverse(allVersions);
        return allVersions;
    }

    protected List<IDynamicDistribution> getAllRegistedDynamicDistributions(IDynamicMonitor monitor) throws Exception {
        BundleContext bc = getBundleContext();

        List<IDynamicDistribution> registedDynamicDistributions = new ArrayList<>();

        Class<? extends IDynamicDistribution> distributionClass = getDynamicDistributionClass();
        Collection<?> serviceReferences = bc.getServiceReferences(distributionClass, null);
        if (serviceReferences != null && !serviceReferences.isEmpty()) {
            for (Object obj : serviceReferences) {
                ServiceReference<IDynamicDistribution> sr = (ServiceReference<IDynamicDistribution>) obj;
                IDynamicDistribution service = bc.getService(sr);
                registedDynamicDistributions.add(service);
            }
        }

        return registedDynamicDistributions;
    }

    protected static BundleContext getBundleContext() {
        return FrameworkUtil.getBundle(AbstractDynamicDistributionsGroup.class).getBundleContext();
    }

    abstract protected String getUsersFolderPath();

}
