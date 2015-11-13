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

import org.talend.core.hadoop.version.EHadoopDistributions;
import org.talend.hadoop.distribution.ComponentType;
import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.cdh550.CDH550Distribution;
import org.talend.hadoop.distribution.condition.BooleanOperator;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.LinkedNodeExpression;
import org.talend.hadoop.distribution.condition.MultiComponentCondition;
import org.talend.hadoop.distribution.condition.NestedComponentCondition;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.SparkStreamingConstant;

public class CDH550SparkStreamingParquetNodeModuleGroup {

    private static final String SPARK_PARQUET_MODULE_GROUP_NAME = "SPARK-PARQUET-LIB-MRREQUIRED-CDH_5_5"; //$NON-NLS-1$

    // This condition stands for:
    // (#LINK@NODE.STORAGE_CONFIGURATION.DISTRIBUTION=='CLOUDERA' AND
    // #LINK@NODE.STORAGE_CONFIGURATION.SPARK_VERSION=='Cloudera_CDH5_5')
    private static final ComponentCondition condition = new NestedComponentCondition(new MultiComponentCondition(
            new SimpleComponentCondition(new LinkedNodeExpression(
                    SparkStreamingConstant.SPARK_STREAMING_SPARKCONFIGURATION_LINKEDPARAMETER,
                    ComponentType.SPARKSTREAMING.getDistributionParameter(), EHadoopDistributions.CLOUDERA.getName(),
                    EqualityOperator.EQ)), new SimpleComponentCondition(new LinkedNodeExpression(
                    SparkStreamingConstant.SPARK_STREAMING_SPARKCONFIGURATION_LINKEDPARAMETER,
                    ComponentType.SPARKSTREAMING.getVersionParameter(), CDH550Distribution.VERSION, EqualityOperator.EQ)),
            BooleanOperator.AND));

    private static final ComponentCondition isNotLocal = new SimpleComponentCondition(new LinkedNodeExpression(
            SparkStreamingConstant.SPARK_STREAMING_SPARKCONFIGURATION_LINKEDPARAMETER,
            SparkStreamingConstant.SPARKCONFIGURATION_IS_LOCAL_MODE_PARAMETER, "false", EqualityOperator.EQ)); //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        DistributionModuleGroup dmg = new DistributionModuleGroup(SPARK_PARQUET_MODULE_GROUP_NAME, false,
                new MultiComponentCondition(isNotLocal, condition, BooleanOperator.AND));
        hs.add(dmg);
        return hs;
    }
}
