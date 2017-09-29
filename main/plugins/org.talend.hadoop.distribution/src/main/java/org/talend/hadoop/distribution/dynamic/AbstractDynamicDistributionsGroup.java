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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.bean.TemplateBean;
import org.talend.hadoop.distribution.dynamic.resolver.DependencyResolverFactory;
import org.talend.hadoop.distribution.dynamic.resolver.IDependencyResolver;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractDynamicDistributionsGroup {

    private Map<IDynamicDistribution, List<String>> compatibleDistribuionVersionMap;

    abstract protected String getDistribution();

    abstract protected Class<? extends IDynamicDistribution> getDynamicDistributionClass();

    public List<String> getCompatibleVersions(IDynamicMonitor monitor) throws Exception {
        Set<String> compatibleVersions = new HashSet<>();
        compatibleDistribuionVersionMap = new HashMap<>();
        List<IDynamicDistribution> allRegistedDynamicDistributions = getAllRegistedDynamicDistributions(monitor);
        if (allRegistedDynamicDistributions != null) {
            for (IDynamicDistribution dynamicDistribution : allRegistedDynamicDistributions) {
                List<String> curCompatibleVersions = dynamicDistribution.getCompatibleVersions(monitor);
                if (curCompatibleVersions != null && !curCompatibleVersions.isEmpty()) {
                    compatibleDistribuionVersionMap.put(dynamicDistribution, curCompatibleVersions);
                    compatibleVersions.addAll(curCompatibleVersions);
                }
            }
        }
        List<String> compatibleVersionList = new ArrayList<>(compatibleVersions);
        compatibleVersionList.sort(null);
        return compatibleVersionList;
    }

    public List<TemplateBean> getAllTemplates(IDynamicMonitor monitor) throws Exception {
        List<TemplateBean> templateBeans = new ArrayList<>();
        List<IDynamicDistribution> allRegistedDynamicDistributions = getAllRegistedDynamicDistributions(monitor);
        if (allRegistedDynamicDistributions != null) {
            for (IDynamicDistribution dynamicDistribution : allRegistedDynamicDistributions) {
                List<TemplateBean> templates = dynamicDistribution.getTemplates(monitor);
                templateBeans.addAll(templates);
            }
        }
        return templateBeans;
    }

    public IDynamicPlugin buildDynamicPlugin(IDynamicMonitor monitor, DynamicConfiguration configuration) throws Exception {
        return null;
    }

    public List<IDynamicPlugin> getAllUsersDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    public List<IDynamicPlugin> getAllBuildinDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    public List<String> getAllVersions(IDynamicMonitor monitor) throws Exception {
        DynamicConfiguration configuration = new DynamicConfiguration();
        configuration.setDistribution(getDistribution());
        IDependencyResolver resolver = DependencyResolverFactory.getInstance().getDependencyResolver(configuration);
        return resolver.listHadoopVersions(null, null, monitor);
    }

    public List<IDynamicDistribution> getAllRegistedDynamicDistributions(IDynamicMonitor monitor) throws Exception {
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

}
