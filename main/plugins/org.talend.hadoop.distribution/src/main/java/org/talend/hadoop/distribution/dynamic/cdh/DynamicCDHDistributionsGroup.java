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
package org.talend.hadoop.distribution.dynamic.cdh;

import java.util.List;

import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.constants.cdh.IClouderaDistribution;
import org.talend.hadoop.distribution.dynamic.AbstractDynamicDistributionsGroup;
import org.talend.hadoop.distribution.dynamic.DynamicConfiguration;
import org.talend.hadoop.distribution.dynamic.IDynamicDistribution;
import org.talend.hadoop.distribution.dynamic.bean.TemplateBean;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicCDHDistributionsGroup extends AbstractDynamicDistributionsGroup {

    @Override
    public List<String> getCompatibleVersions(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    @Override
    public List<TemplateBean> getAllTemplates(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    @Override
    public IDynamicPlugin buildDynamicPlugin(IDynamicMonitor monitor, DynamicConfiguration configuration) throws Exception {
        return null;
    }

    @Override
    public List<IDynamicPlugin> getAllUsersDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    @Override
    public List<IDynamicPlugin> getAllBuildinDynamicPlugins(IDynamicMonitor monitor) throws Exception {
        return null;
    }

    @Override
    public String getDistribution() {
        return IClouderaDistribution.DISTRIBUTION_NAME;
    }

    @Override
    public String getDistributionDisplay() {
        return IClouderaDistribution.DISTRIBUTION_DISPLAY_NAME;
    }

    @Override
    protected Class<? extends IDynamicDistribution> getDynamicDistributionClass() {
        return IDynamicCDHDistribution.class;
    }

}
