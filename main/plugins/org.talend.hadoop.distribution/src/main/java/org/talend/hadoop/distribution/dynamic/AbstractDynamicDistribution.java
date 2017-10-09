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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.DynamicServiceUtil;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.designer.maven.aether.util.DynamicDistributionAetherUtils;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicTemplateAdapter;
import org.talend.hadoop.distribution.dynamic.bean.TemplateBean;
import org.talend.hadoop.distribution.dynamic.resolver.DependencyResolverFactory;
import org.talend.hadoop.distribution.dynamic.resolver.IDependencyResolver;
import org.talend.hadoop.distribution.helper.HadoopDistributionsHelper;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractDynamicDistribution implements IDynamicDistribution {

    private List<IDynamicPlugin> buildinPluginsCache;

    private List<IDynamicPlugin> usersPluginsCache;

    private String usersPluginsCacheVersion = ""; //$NON-NLS-1$

    private List<TemplateBean> templateBeansCache;

    private Map<TemplateBean, List<String>> templateBeanVersionMap;

    abstract protected Bundle getBundle();

    abstract protected String getTemplateFolderPath();

    abstract protected String getBuildinFolderPath();

    @Override
    public List<TemplateBean> getTemplates(IDynamicMonitor monitor) throws Exception {
        if (templateBeansCache != null) {
            return templateBeansCache;
        }

        List<TemplateBean> templates = new ArrayList<>();

        Bundle bundle = getBundle();

        Enumeration<URL> entries = bundle.findEntries(getTemplateFolderPath(), null, true);

        if (entries != null) {
            while (entries.hasMoreElements()) {
                try {
                    ObjectMapper om = new ObjectMapper();
                    URL curUrl = entries.nextElement();
                    if (curUrl != null) {
                        String templateFilePath = FileLocator.toFileURL(curUrl).getPath();
                        TemplateBean bean = om.readValue(new File(templateFilePath), TemplateBean.class);
                        templates.add(bean);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        templateBeansCache = templates;

        return templateBeansCache;
    }

    @Override
    public List<IDynamicPlugin> getAllBuildinDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        if (buildinPluginsCache != null) {
            return buildinPluginsCache;
        }

        List<IDynamicPlugin> dynamicPlugins = new ArrayList<>();

        Bundle bundle = getBundle();

        Enumeration<URL> entries = bundle.findEntries(getBuildinFolderPath(), null, true);

        if (entries != null) {
            while (entries.hasMoreElements()) {
                try {
                    URL curUrl = entries.nextElement();
                    if (curUrl != null) {
                        String buildinDistributionPath = FileLocator.toFileURL(curUrl).getPath();
                        String jsonContent = DynamicServiceUtil.readFile(new File(buildinDistributionPath));
                        IDynamicPlugin dynamicPlugin = DynamicFactory.getInstance().createPluginFromJson(jsonContent);
                        dynamicPlugins.add(dynamicPlugin);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        buildinPluginsCache = dynamicPlugins;

        return buildinPluginsCache;
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

        List<IDynamicPlugin> dynamicPlugins = new ArrayList<>();

        Bundle bundle = getBundle();

        Enumeration<URL> entries = bundle.findEntries(getBuildinFolderPath(), null, true);

        if (entries != null) {
            while (entries.hasMoreElements()) {
                try {
                    URL curUrl = entries.nextElement();
                    if (curUrl != null) {
                        String buildinDistributionPath = FileLocator.toFileURL(curUrl).getPath();
                        String jsonContent = DynamicServiceUtil.readFile(new File(buildinDistributionPath));
                        IDynamicPlugin dynamicPlugin = DynamicFactory.getInstance().createPluginFromJson(jsonContent);
                        dynamicPlugins.add(dynamicPlugin);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return usersPluginsCache;
    }

    @Override
    public List<String> getCompatibleVersions(IDynamicMonitor monitor) throws Exception {
        Set<String> allCompatibleVersion = new HashSet<>();
        List<TemplateBean> templates = getTemplates(monitor);
        if (templates != null) {
            templateBeanVersionMap = new HashMap<>();
            DynamicConfiguration dynamicConfiguration = new DynamicConfiguration();
            dynamicConfiguration.setDistribution(getDistributionName());
            IDependencyResolver dependencyResolver = DependencyResolverFactory.getInstance()
                    .getDependencyResolver(dynamicConfiguration);
            List<String> allHadoopVersions = dependencyResolver.listHadoopVersions(null, null, monitor);
            if (allHadoopVersions != null) {
                for (TemplateBean templateBean : templates) {
                    String baseVersion = templateBean.getBaseVersion();
                    String topVersion = templateBean.getTopVersion();
                    String versionRange = "["; //$NON-NLS-1$
                    if (StringUtils.isEmpty(baseVersion)) {
                        versionRange = versionRange + "0"; //$NON-NLS-1$
                    } else {
                        versionRange = versionRange + baseVersion;
                    }
                    versionRange = versionRange + ","; //$NON-NLS-1$
                    if (StringUtils.isNotEmpty(topVersion)) {
                        versionRange = versionRange + topVersion;
                    }
                    versionRange = versionRange + ")"; //$NON-NLS-1$
                    List<String> filteredVersions = DynamicDistributionAetherUtils.filterVersions(allHadoopVersions,
                            versionRange);
                    if (filteredVersions != null && !filteredVersions.isEmpty()) {
                        allCompatibleVersion.addAll(filteredVersions);
                        templateBeanVersionMap.put(templateBean, filteredVersions);
                    }
                }
            }
        }
        List<String> compatibleVersionList = new LinkedList<>(allCompatibleVersion);
        Collections.reverse(compatibleVersionList);
        return compatibleVersionList;
    }

    @Override
    public IDynamicPlugin buildDynamicPlugin(IDynamicMonitor monitor, DynamicConfiguration configuration) throws Exception {
        String distribution = configuration.getDistribution();
        if (!StringUtils.equals(getDistributionName(), distribution)) {
            throw new Exception(
                    "only support to build dynamic plugin of " + getDistributionName() + " instead of " + distribution);
        }
        String version = configuration.getVersion();
        Set<Entry<TemplateBean, List<String>>> entrySet = templateBeanVersionMap.entrySet();
        TemplateBean bestTemplateBean = null;
        int distance = -1;
        for (Entry<TemplateBean, List<String>> entry : entrySet) {
            List<String> list = entry.getValue();
            Collections.reverse(list);
            int size = list.size();
            int index = list.indexOf(version);
            int curDistance = size - index;
            if (distance < curDistance) {
                curDistance = distance;
                bestTemplateBean = entry.getKey();
            }
        }

        DynamicTemplateAdapter templateAdapter = new DynamicTemplateAdapter(bestTemplateBean, configuration);
        templateAdapter.adapt(monitor);
        IDynamicPlugin dynamicPlugin = templateAdapter.getDynamicPlugin();

        return dynamicPlugin;
    }

    public List<IDynamicPlugin> getBuildinPluginsCache() {
        return this.buildinPluginsCache;
    }

    public void setBuildinPluginsCache(List<IDynamicPlugin> buildinPluginsCache) {
        this.buildinPluginsCache = buildinPluginsCache;
    }

    public List<TemplateBean> getTemplateBeansCache() {
        return this.templateBeansCache;
    }

    public void setTemplateBeansCache(List<TemplateBean> templateBeansCache) {
        this.templateBeansCache = templateBeansCache;
    }

}
