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
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.SparkBatchConstant;

public class CDH550SparkBatchModuleGroup {

    private static final String MODULE_GROUP_NAME = "SPARK-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String MRREQUIRED_MODULE_GROUP_NAME = "SPARK-LIB-MRREQUIRED-CDH_5_5"; //$NON-NLS-1$

    private static final String HDFS_MODULE_GROUP_NAME = "HDFS-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String MAPREDUCE_MODULE_GROUP_NAME = "MAPREDUCE-LIB-CDH_5_5"; //$NON-NLS-1$

    private final static ComponentCondition condition = new SimpleComponentCondition(new BasicExpression(
            SparkBatchConstant.SPARK_LOCAL_MODE_PARAMETER, "false", EqualityOperator.EQ)); //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(MODULE_GROUP_NAME, false, condition));
        hs.add(new DistributionModuleGroup(MRREQUIRED_MODULE_GROUP_NAME, true, condition));
        hs.add(new DistributionModuleGroup(HDFS_MODULE_GROUP_NAME, false, condition));
        hs.add(new DistributionModuleGroup(MAPREDUCE_MODULE_GROUP_NAME, false, condition));
        return hs;
    }

}
