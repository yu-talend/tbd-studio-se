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
import java.util.List;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.cdh.DynamicCDHDistributionsGroup;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionManager {

    private static DynamicDistributionManager instance;

    private DynamicDistributionManager() {
        // nothing to do
    }

    public static DynamicDistributionManager getInstance() {
        if (instance == null) {
            instance = new DynamicDistributionManager();
        }
        return instance;
    }

    private List<IDynamicDistributionsGroup> dynamicDistributionsGroups;

    public List<IDynamicDistributionsGroup> getDynamicDistributionsGroups() throws Exception {

        if (dynamicDistributionsGroups != null) {
            return dynamicDistributionsGroups;
        }

        dynamicDistributionsGroups = new ArrayList<>();

        dynamicDistributionsGroups.add(new DynamicCDHDistributionsGroup());

        return dynamicDistributionsGroups;

    }

    public void registAll(IDynamicMonitor monitor) throws Exception {
        registAllBuildin(monitor, false);
        registAllUsers(monitor, false);
        cleanCache();
    }

    public void registAllBuildin(IDynamicMonitor monitor, boolean cleanCache) throws Exception {

        if (cleanCache) {
            cleanCache();
        }
    }

    public void registAllUsers(IDynamicMonitor monitor, boolean cleanCache) throws Exception {
        if (cleanCache) {
            cleanCache();
        }
    }

    public void unregistAll(IDynamicMonitor monitor) throws Exception {
        unregistAllUsers(monitor, false);
        unregistAllBuildin(monitor, false);
        cleanCache();
    }

    public void unregistAllBuildin(IDynamicMonitor monitor, boolean cleanCache) throws Exception {
        if (cleanCache) {
            cleanCache();
        }
    }

    public void unregistAllUsers(IDynamicMonitor monitor, boolean cleanCache) throws Exception {
        if (cleanCache) {
            cleanCache();
        }
    }

    public void cleanCache() throws Exception {

        // 1. reset modulesNeeded cache
        getLibrariesService().resetModulesNeeded();

    }

    private static ILibrariesService getLibrariesService() {
        return (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
    }
}
