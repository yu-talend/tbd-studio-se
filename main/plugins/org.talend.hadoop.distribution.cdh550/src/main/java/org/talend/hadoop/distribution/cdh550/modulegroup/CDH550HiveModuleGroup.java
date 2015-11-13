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
import org.talend.hadoop.distribution.condition.BasicExpression;
import org.talend.hadoop.distribution.condition.BooleanOperator;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.MultiComponentCondition;
import org.talend.hadoop.distribution.condition.ShowExpression;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.HiveConstant;

public class CDH550HiveModuleGroup {

    private static final String MODULE_GROUP_NAME = "HIVE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String MAPREDUCE_MODULE_GROUP_NAME = "MAPREDUCE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HDFS_MODULE_GROUP_NAME = "HDFS-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HIVE_HBASE_MODULE_GROUP_NAME = "HIVE-HBASE-LIB-CDH_5_5"; //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(HDFS_MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(MAPREDUCE_MODULE_GROUP_NAME));

        // The following condition instance stands for:
        // (isShow[STORE_BY_HBASE] AND STORE_BY_HBASE=='true')
        ComponentCondition hbaseLoaderCondition = new MultiComponentCondition(
                new SimpleComponentCondition(new BasicExpression(HiveConstant.HIVE_CONFIGURATION_COMPONENT_HBASEPARAMETER,
                        "true", EqualityOperator.EQ)), new SimpleComponentCondition(new ShowExpression(HiveConstant.HIVE_CONFIGURATION_COMPONENT_HBASEPARAMETER)), BooleanOperator.AND); //$NON-NLS-1$
        // The Hive components need to import some hbase libraries if the "Use HBase storage" is checked.
        hs.add(new DistributionModuleGroup(HIVE_HBASE_MODULE_GROUP_NAME, false, hbaseLoaderCondition));

        return hs;
    }
}
