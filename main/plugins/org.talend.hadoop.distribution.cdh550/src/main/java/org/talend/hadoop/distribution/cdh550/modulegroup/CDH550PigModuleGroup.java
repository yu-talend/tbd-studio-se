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
import org.talend.hadoop.distribution.constants.PigConstant;

public class CDH550PigModuleGroup {

    private static final String MODULE_GROUP_NAME = "PIG-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String MAPREDUCE_MODULE_GROUP_NAME = "MAPREDUCE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HDFS_MODULE_GROUP_NAME = "HDFS-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_HCAT_MODULE_GROUP_NAME = "PIG-HCATALOG-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HBASE_MODULE_GROUP_NAME = "HBASE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_PARQUET_MODULE_GROUP_NAME = "PIG-PARQUET-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_HBASE_MODULE_GROUP_NAME = "PIG-HBASE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_AVRO_MODULE_GROUP_NAME = "PIG-AVRO-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_RCFILE_MODULE_GROUP_NAME = "PIG-RCFILE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_SEQUENCEFILE_MODULE_GROUP_NAME = "PIG-SEQUENCEFILE-LIB-CDH_5_5"; //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        ComponentCondition hbaseLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
                PigConstant.HBASE_LOADER_VALUE, EqualityOperator.EQ));
        ComponentCondition parquetLoaderCondition = new SimpleComponentCondition(new BasicExpression(
                PigConstant.LOADER_PARAMETER, PigConstant.PARQUET_LOADER_VALUE, EqualityOperator.EQ));
        ComponentCondition hcatLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
                PigConstant.HCAT_LOADER_VALUE, EqualityOperator.EQ));
        ComponentCondition avroLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
                PigConstant.AVRO_LOADER_VALUE, EqualityOperator.EQ));
        ComponentCondition rcfileLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
                PigConstant.RCFILE_LOADER_VALUE, EqualityOperator.EQ));
        ComponentCondition sequencefileLoaderCondition = new SimpleComponentCondition(new BasicExpression(
                PigConstant.LOADER_PARAMETER, PigConstant.SEQUENCEFILE_LOADER_VALUE, EqualityOperator.EQ));

        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(HDFS_MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(MAPREDUCE_MODULE_GROUP_NAME));
        hs.add(new DistributionModuleGroup(PIG_HCAT_MODULE_GROUP_NAME, false, hcatLoaderCondition));
        hs.add(new DistributionModuleGroup(HBASE_MODULE_GROUP_NAME, false, hbaseLoaderCondition));
        hs.add(new DistributionModuleGroup(PIG_HBASE_MODULE_GROUP_NAME, false, hbaseLoaderCondition));
        hs.add(new DistributionModuleGroup(PIG_PARQUET_MODULE_GROUP_NAME, false, parquetLoaderCondition));
        hs.add(new DistributionModuleGroup(PIG_AVRO_MODULE_GROUP_NAME, false, avroLoaderCondition));
        hs.add(new DistributionModuleGroup(PIG_RCFILE_MODULE_GROUP_NAME, false, rcfileLoaderCondition));
        hs.add(new DistributionModuleGroup(PIG_SEQUENCEFILE_MODULE_GROUP_NAME, false, sequencefileLoaderCondition));
        return hs;
    }

}
