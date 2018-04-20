// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.hadoop.distribution.qubole.modulegroup;

import java.util.HashSet;
import java.util.Set;

import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.qubole.QuboleConstant;

public class QuboleHiveModuleGroup {

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> moduleGroups = new HashSet<>();
        moduleGroups.add(new DistributionModuleGroup(QuboleConstant.HIVE_MODULE_GROUP.getModuleName()));
//        moduleGroups.add(new DistributionModuleGroup(QuboleConstant.HDFS_MODULE_GROUP.getModuleName()));
        //moduleGroups.add(new DistributionModuleGroup(QuboleConstant.MAPREDUCE_MODULE_GROUP.getModuleName()));
        return moduleGroups;

        // The following condition instance stands for:
        // (isShow[STORE_BY_HBASE] AND STORE_BY_HBASE=='true')
//        ComponentCondition hbaseLoaderCondition = new MultiComponentCondition(new SimpleComponentCondition(new BasicExpression(
//                HiveConstant.HIVE_CONFIGURATION_COMPONENT_HBASEPARAMETER)), //
//                BooleanOperator.AND, //
//                new SimpleComponentCondition(new ShowExpression(HiveConstant.HIVE_CONFIGURATION_COMPONENT_HBASEPARAMETER)));
        // The Hive components need to import some hbase libraries if the "Use HBase storage" is checked.
        //hs.add(new DistributionModuleGroup(QuboleConstant.HIVE_HBASE_MODULE_GROUP.getModuleName(), false, hbaseLoaderCondition));
    }
}
