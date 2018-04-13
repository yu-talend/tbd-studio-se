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
import org.talend.hadoop.distribution.condition.BasicExpression;
import org.talend.hadoop.distribution.condition.BooleanOperator;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.MultiComponentCondition;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.SparkBatchConstant;
import org.talend.hadoop.distribution.qubole.QuboleConstant;

public class QuboleSparkStreamingModuleGroup {

    private final static ComponentCondition nonSparkLocalCondition = new SimpleComponentCondition(new BasicExpression(
                                                              SparkBatchConstant.SPARK_LOCAL_MODE_PARAMETER, EqualityOperator.EQ,
                                                              "false")); //$NON-NLS-1$
    private final static ComponentCondition awsCondition = new SimpleComponentCondition(new BasicExpression(
            SparkBatchConstant.ALTUS_CLOUD_PROVIDER, EqualityOperator.EQ, "\"AWS\""));

    private final static ComponentCondition azureCondition = new SimpleComponentCondition(new BasicExpression(
            SparkBatchConstant.ALTUS_CLOUD_PROVIDER, EqualityOperator.EQ, "\"Azure\""));

    private final static ComponentCondition kinesisCondition = new MultiComponentCondition(nonSparkLocalCondition,
            BooleanOperator.AND, awsCondition);
    
    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(QuboleConstant.HDFS_MODULE_GROUP.getModuleName(), false, nonSparkLocalCondition));
        // hs.add(new DistributionModuleGroup(QuboleConstant.MAPREDUCE_MODULE_GROUP.getModuleName(), false, nonSparkLocalCondition));
     // hs.add(new DistributionModuleGroup(QuboleConstant.SPARK_MODULE_GROUP.getModuleName(), false, nonSparkLocalCondition));
        hs.add(new DistributionModuleGroup(QuboleConstant.BIGDATALAUNCHER_MODULE_GROUP.getModuleName(), true, nonSparkLocalCondition));
     // hs.add(new DistributionModuleGroup(QuboleConstant.SPARK_KINESIS_MRREQUIRED_MODULE_GROUP.getModuleName(), true,
     // kinesisCondition));
             // hs.add(new DistributionModuleGroup(QuboleConstant.SPARK_S3_MRREQUIRED_MODULE_GROUP.getModuleName(), true, awsCondition));
             // hs.add(new DistributionModuleGroup(QuboleConstant.SPARK_AZURE_MRREQUIRED_MODULE_GROUP.getModuleName(), true,
     //         azureCondition));
        return hs;
    }
}