// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.hadoop.distribution.cdh550.modulegroup;

import java.util.HashSet;
import java.util.Set;

import org.talend.hadoop.distribution.DistributionModuleGroup;

public class CDH550ImpalaModuleGroup {

    private static final String MODULE_GROUP_NAME = "HIVE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String MAPREDUCE_MODULE_GROUP_NAME = "MAPREDUCE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HDFS_MODULE_GROUP_NAME = "HDFS-LIB-CDH_5_5"; //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(HDFS_MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(MAPREDUCE_MODULE_GROUP_NAME));

        return hs;
    }
}
