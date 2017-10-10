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
package org.talend.hadoop.distribution.cdh5x;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.DynamicServiceUtil;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.component.HBaseComponent;
import org.talend.hadoop.distribution.component.HCatalogComponent;
import org.talend.hadoop.distribution.component.HDFSComponent;
import org.talend.hadoop.distribution.component.HadoopComponent;
import org.talend.hadoop.distribution.component.HiveComponent;
import org.talend.hadoop.distribution.component.HiveOnSparkComponent;
import org.talend.hadoop.distribution.component.ImpalaComponent;
import org.talend.hadoop.distribution.component.MRComponent;
import org.talend.hadoop.distribution.component.PigComponent;
import org.talend.hadoop.distribution.component.SparkBatchComponent;
import org.talend.hadoop.distribution.component.SparkStreamingComponent;
import org.talend.hadoop.distribution.component.SqoopComponent;
import org.talend.hadoop.distribution.dynamic.AbstractDynamicDistribution;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionTemplate;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;
import org.talend.hadoop.distribution.dynamic.cdh.IDynamicCDHDistribution;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicCDH5xDistribution extends AbstractDynamicDistribution implements IDynamicCDHDistribution {

    public static final String TEMPLATE_FOLDER_PATH = "resources/template/cdh5x/"; //$NON-NLS-1$

    public static final String BUILD_IN_FOLDER_PATH = "resources/buildin/cdh5x/"; //$NON-NLS-1$

    private CDH5xDistributionTemplate cdhService;

    private IDynamicPlugin runtimePlugin;

    private ServiceRegistration osgiService;

    public void regist(IDynamicMonitor monitor) throws Exception {
        if (true) {
            return;
        }
        CDH5xPlugin cdh5xPlugin = CDH5xPlugin.getInstance();

        Bundle bundle = cdh5xPlugin.getBundle();

        URL resourceURL = bundle.getEntry("resources/cdh5x.json");
        String cdh5xPath = FileLocator.toFileURL(resourceURL).getPath();
        String xmlJsonString = DynamicServiceUtil.readFile(new File(cdh5xPath));
        runtimePlugin = DynamicFactory.getInstance().createPluginFromJson(xmlJsonString);
        IDynamicPluginConfiguration pluginConfiguration = runtimePlugin.getPluginConfiguration();

        String id = pluginConfiguration.getId();
        String displayName = pluginConfiguration.getName();

        cdhService = new CDH5xDistributionTemplate(null) {
        };
        // AbstractDynamicAdapter adapter = DynamicAdapterFactory.getInstance().create(runtimePlugin.getTagName(),
        // runtimePlugin,
        // id);
        // adapter.adapt();

        DynamicServiceUtil.addContribution(bundle, runtimePlugin);

        BundleContext context = bundle.getBundleContext();
        osgiService = DynamicServiceUtil.registOSGiService(context,
                new String[] { HadoopComponent.class.getName(), HDFSComponent.class.getName(), HBaseComponent.class.getName(),
                        HCatalogComponent.class.getName(), HiveComponent.class.getName(), HiveOnSparkComponent.class.getName(),
                        ImpalaComponent.class.getName(), MRComponent.class.getName(), PigComponent.class.getName(),
                        SqoopComponent.class.getName(), SparkBatchComponent.class.getName(),
                        SparkStreamingComponent.class.getName() },
                cdhService, null);

    }

    public void unregist(IDynamicMonitor monitor) throws Exception {
        DynamicServiceUtil.unregistOSGiService(osgiService);
        DynamicServiceUtil.removeContribution(runtimePlugin);
    }

    @Override
    protected IDynamicDistributionTemplate initTemplate(DynamicPluginAdapter pluginAdapter, IDynamicMonitor monitor)
            throws Exception {
        IDynamicDistributionTemplate dynamicDistributionTemplate = null;
        IDynamicPluginConfiguration pluginConfiguration = pluginAdapter.getPluginConfiguration();
        String templateId = pluginConfiguration.getTemplateId();
        switch (templateId) {
        case CDH5xDistributionTemplate.TEMPLATE_ID:
            dynamicDistributionTemplate = new CDH5xDistributionTemplate(pluginAdapter);
            break;
        default:
            throw new Exception("Unknown templateId: " + templateId);
        }
        return dynamicDistributionTemplate;
    }

    @Override
    public List<String> getSupportedTemplateIds(IDynamicMonitor monitor) throws Exception {
        List<String> templateIds = new ArrayList<>();

        templateIds.add(CDH5xDistributionTemplate.TEMPLATE_ID);

        return templateIds;
    }

    @Override
    protected String getTemplateFolderPath() {
        return TEMPLATE_FOLDER_PATH;
    }

    @Override
    protected Bundle getBundle() {
        CDH5xPlugin cdh5xPlugin = CDH5xPlugin.getInstance();
        return cdh5xPlugin.getBundle();
    }

    @Override
    protected String getBuildinFolderPath() {
        return BUILD_IN_FOLDER_PATH;
    }

    @Override
    public String getDistributionName() {
        return IDynamicCDHDistribution.DISTRIBUTION;
    }

}
