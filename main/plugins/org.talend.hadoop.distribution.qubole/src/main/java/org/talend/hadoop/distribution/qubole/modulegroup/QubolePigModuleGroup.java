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
import org.talend.hadoop.distribution.condition.BasicExpression;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.PigConstant;

public class QubolePigModuleGroup {

    public static Set<DistributionModuleGroup> getModuleGroups() {

        Set<DistributionModuleGroup> moduleGroups = new HashSet<>();
        moduleGroups.add(new DistributionModuleGroup(QuboleConstant.PIG_MODULE_GROUP.getModuleName()));
        return moduleGroups;

//        ComponentCondition hbaseLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
//                EqualityOperator.EQ, PigConstant.HBASE_LOADER_VALUE));
//        ComponentCondition parquetLoaderCondition = new SimpleComponentCondition(new BasicExpression(
//                PigConstant.LOADER_PARAMETER, EqualityOperator.EQ, PigConstant.PARQUET_LOADER_VALUE));
//        ComponentCondition hcatLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
//                EqualityOperator.EQ, PigConstant.HCAT_LOADER_VALUE));
//        ComponentCondition avroLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
//                EqualityOperator.EQ, PigConstant.AVRO_LOADER_VALUE));
//        ComponentCondition rcfileLoaderCondition = new SimpleComponentCondition(new BasicExpression(PigConstant.LOADER_PARAMETER,
//                EqualityOperator.EQ, PigConstant.RCFILE_LOADER_VALUE));
//        ComponentCondition sequencefileLoaderCondition = new SimpleComponentCondition(new BasicExpression(
//                PigConstant.LOADER_PARAMETER, EqualityOperator.EQ, PigConstant.SEQUENCEFILE_LOADER_VALUE));
//
//        ComponentCondition s3condition = new SimpleComponentCondition(new BasicExpression(PigConstant.PIGLOAD_S3_LOCATION_LOAD));

//        hs.add(new DistributionModuleGroup(QuboleConstant.HDFS_MODULE_GROUP.getModuleName()));
        //hs.add(new DistributionModuleGroup(CDH580Constant.MAPREDUCE_MODULE_GROUP.getModuleName()));
        //hs.add(new DistributionModuleGroup(CDH580Constant.PIG_HCATALOG_MODULE_GROUP.getModuleName(), false, hcatLoaderCondition));
        //hs.add(new DistributionModuleGroup(CDH580Constant.HBASE_MODULE_GROUP.getModuleName(), false, hbaseLoaderCondition));
        //hs.add(new DistributionModuleGroup(CDH580Constant.PIG_HBASE_MODULE_GROUP.getModuleName(), false, hbaseLoaderCondition));
//        hs.add(new DistributionModuleGroup(QuboleConstant.PIG_PARQUET_MODULE_GROUP.getModuleName(), false, parquetLoaderCondition));
//        hs.add(new DistributionModuleGroup(QuboleConstant.PIG_AVRO_MODULE_GROUP.getModuleName(), false, avroLoaderCondition));
//        hs.add(new DistributionModuleGroup(QuboleConstant.PIG_RCFILE_MODULE_GROUP.getModuleName(), false, rcfileLoaderCondition));
//        hs.add(new DistributionModuleGroup(QuboleConstant.PIG_SEQUENCEFILE_MODULE_GROUP.getModuleName(), false,
//                sequencefileLoaderCondition));
//        hs.add(new DistributionModuleGroup(QuboleConstant.PIG_S3_MODULE_GROUP.getModuleName(), false, s3condition));
    }

}
