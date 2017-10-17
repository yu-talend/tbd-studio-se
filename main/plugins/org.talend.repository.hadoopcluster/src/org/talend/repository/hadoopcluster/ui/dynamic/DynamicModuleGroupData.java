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
package org.talend.repository.hadoopcluster.ui.dynamic;

import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicModuleGroupData {

    private IDynamicPlugin dynamicPlugin;

    private DynamicPluginAdapter pluginAdapter;

    private String groupTemplateId;

    public IDynamicPlugin getDynamicPlugin() {
        return this.dynamicPlugin;
    }

    public void setDynamicPlugin(IDynamicPlugin dynamicPlugin) {
        this.dynamicPlugin = dynamicPlugin;
    }

    public String getGroupTemplateId() {
        return this.groupTemplateId;
    }

    public void setGroupTemplateId(String groupTemplateId) {
        this.groupTemplateId = groupTemplateId;
    }

    public DynamicPluginAdapter getPluginAdapter() {
        return this.pluginAdapter;
    }

    public void setPluginAdapter(DynamicPluginAdapter pluginAdapter) {
        this.pluginAdapter = pluginAdapter;
    }

}
