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
package org.talend.hadoop.distribution.hdinsight360.modulegroup;

import java.util.HashSet;
import java.util.Set;

import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.condition.BasicExpression;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.PigOutputConstant;
import org.talend.hadoop.distribution.hdinsight360.HDInsight36Constant;

public class HDInsight36PigOutputModuleGroup {

    public static Set<DistributionModuleGroup> getModuleGroups() {

        ComponentCondition rcfileStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, EqualityOperator.EQ, PigOutputConstant.RCFILE_STORER_VALUE));
        ComponentCondition sequencefileStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, EqualityOperator.EQ, PigOutputConstant.SEQUENCEFILE_STORER_VALUE));

        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(HDInsight36Constant.BIGDATALAUNCHER_MODULE_GROUP.getModuleName(), true, null));
        hs.add(new DistributionModuleGroup(HDInsight36Constant.HDINSIGHT36COMMON_MODULE_GROUP.getModuleName()));
        hs.add(new DistributionModuleGroup(HDInsight36Constant.PIG_MODULE_GROUP.getModuleName(), true, null));
        hs.add(new DistributionModuleGroup(HDInsight36Constant.PIG_PARQUET_MODULE_GROUP.getModuleName(), true, null));
        hs.add(new DistributionModuleGroup(HDInsight36Constant.PIG_SEQUENCEFILE_MODULE_GROUP.getModuleName(), true,
                sequencefileStorerCondition));
        hs.add(new DistributionModuleGroup(HDInsight36Constant.PIG_RCFILE_MODULE_GROUP.getModuleName(), true,
                rcfileStorerCondition));
        return hs;

    }
}
