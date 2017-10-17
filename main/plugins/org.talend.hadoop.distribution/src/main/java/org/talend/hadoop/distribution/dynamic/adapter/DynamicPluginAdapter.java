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
package org.talend.hadoop.distribution.dynamic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.core.runtime.dynamic.IDynamicExtension;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicPluginAdapter {

    private IDynamicPlugin plugin;

    private IDynamicPluginConfiguration pluginConfiguration;

    private Map<String, IDynamicConfiguration> moduleGroupTemplateMap;
    
    private Map<String, IDynamicConfiguration> moduleMap;

    public DynamicPluginAdapter(IDynamicPlugin plugin) {
        this.plugin = plugin;
        this.pluginConfiguration = this.plugin.getPluginConfiguration();
        moduleGroupTemplateMap = new HashMap<>();
        moduleMap = new HashMap<>();
    }

    public IDynamicPlugin getPlugin() {
        return plugin;
    }

    public IDynamicPluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    /**
     * Build related informations, and remove attributes not needed
     * 
     * @throws Exception
     */
    public void adapt() throws Exception {
        List<IDynamicExtension> allExtensions = plugin.getAllExtensions();
        IDynamicExtension libNeededExtension = null;
        for (IDynamicExtension extension : allExtensions) {
            if (DynamicLibraryNeededExtensionAdaper.ATTR_POINT.equals(extension.getExtensionPoint())) {
                libNeededExtension = extension;
                break;
            }
        }
        if (libNeededExtension == null) {
            throw new Exception("Can't find extension: " + DynamicLibraryNeededExtensionAdaper.ATTR_POINT);
        }
        List<IDynamicConfiguration> configurations = libNeededExtension.getConfigurations();
        if (configurations == null || configurations.isEmpty()) {
            throw new Exception("No libraryModuelGroup configured");
        }
        for (IDynamicConfiguration configuration : configurations) {
            if (DynamicModuleGroupAdapter.TAG_NAME.equals(configuration.getTagName())) {
                String templateId = (String) configuration.getAttribute(DynamicModuleGroupAdapter.ATTR_GROUP_TEMPLATE_ID);
                if (StringUtils.isEmpty(templateId)) {
                    throw new Exception("Template id is not configured!");
                }
                moduleGroupTemplateMap.put(templateId, configuration);
                configuration.removeAttribute(DynamicModuleGroupAdapter.ATTR_GROUP_TEMPLATE_ID);
            } else if (DynamicModuleAdapter.TAG_NAME.equals(configuration.getTagName())) {
                String moduleId = (String) configuration.getAttribute(DynamicModuleAdapter.ATTR_ID);
                if (StringUtils.isEmpty(moduleId)) {
                    throw new Exception("Module id is empty!");
                }
                moduleMap.put(moduleId, configuration);
            }
        }
        // plugin.setPluginConfiguration(null);
    }

    public void buildIdMaps() throws Exception {
        IDynamicExtension libNeededExtension = getLibraryNeededExtension(plugin);
        if (libNeededExtension == null) {
            throw new Exception("Can't find extension: " + DynamicLibraryNeededExtensionAdaper.ATTR_POINT);
        }
        List<IDynamicConfiguration> configurations = libNeededExtension.getConfigurations();
        if (configurations == null || configurations.isEmpty()) {
            throw new Exception("No libraryModuelGroup configured");
        }
        for (IDynamicConfiguration configuration : configurations) {
            if (DynamicModuleGroupAdapter.TAG_NAME.equals(configuration.getTagName())) {
                String templateId = (String) configuration.getAttribute(DynamicModuleGroupAdapter.ATTR_GROUP_TEMPLATE_ID);
                if (StringUtils.isEmpty(templateId)) {
                    throw new Exception("Template id is not configured!");
                }
                moduleGroupTemplateMap.put(templateId, configuration);
            } else if (DynamicModuleAdapter.TAG_NAME.equals(configuration.getTagName())) {
                String moduleId = (String) configuration.getAttribute(DynamicModuleAdapter.ATTR_ID);
                if (StringUtils.isEmpty(moduleId)) {
                    throw new Exception("Module id is empty!");
                }
                moduleMap.put(moduleId, configuration);
            }
        }
    }

    public static IDynamicExtension getLibraryNeededExtension(IDynamicPlugin dynamicPlugin) {
        List<IDynamicExtension> allExtensions = dynamicPlugin.getAllExtensions();
        IDynamicExtension libNeededExtension = null;
        for (IDynamicExtension extension : allExtensions) {
            if (DynamicLibraryNeededExtensionAdaper.ATTR_POINT.equals(extension.getExtensionPoint())) {
                libNeededExtension = extension;
                break;
            }
        }
        return libNeededExtension;
    }

    public Set<String> getAllModuleIds() {
        return moduleMap.keySet();
    }

    public IDynamicConfiguration getModuleById(String id) {
        return moduleMap.get(id);
    }

    public IDynamicConfiguration getModuleGroupByTemplateId(String templateId) {
        return moduleGroupTemplateMap.get(templateId);
    }

    public String getRuntimeModuleGroupIdByTemplateId(String templateId) {
        IDynamicConfiguration moduleGroup = getModuleGroupByTemplateId(templateId);
        if (moduleGroup == null) {
            return null;
        } else {
            return (String) moduleGroup.getAttribute(DynamicModuleGroupAdapter.ATTR_ID);
        }
    }

}
