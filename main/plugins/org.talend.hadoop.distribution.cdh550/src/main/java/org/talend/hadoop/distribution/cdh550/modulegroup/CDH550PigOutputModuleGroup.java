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
import org.talend.hadoop.distribution.constants.PigOutputConstant;

public class CDH550PigOutputModuleGroup {

    private static final String PIG_HCAT_MODULE_GROUP_NAME = "PIG-HCATALOG-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String HBASE_MODULE_GROUP_NAME = "HBASE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_HBASE_MODULE_GROUP_NAME = "PIG-HBASE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_AVRO_MODULE_GROUP_NAME = "PIG-AVRO-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_RCFILE_MODULE_GROUP_NAME = "PIG-RCFILE-LIB-CDH_5_5"; //$NON-NLS-1$

    private static final String PIG_SEQUENCEFILE_MODULE_GROUP_NAME = "PIG-SEQUENCEFILE-LIB-CDH_5_5"; //$NON-NLS-1$

    public static Set<DistributionModuleGroup> getModuleGroups() {
        ComponentCondition hbaseStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, PigOutputConstant.HBASE_STORER_VALUE, EqualityOperator.EQ));
        ComponentCondition hcatStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, PigOutputConstant.HCAT_STORER_VALUE, EqualityOperator.EQ));
        ComponentCondition avroStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, PigOutputConstant.AVRO_STORER_VALUE, EqualityOperator.EQ));
        ComponentCondition rcfileStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, PigOutputConstant.RCFILE_STORER_VALUE, EqualityOperator.EQ));
        ComponentCondition sequencefileStorerCondition = new SimpleComponentCondition(new BasicExpression(
                PigOutputConstant.STORER_PARAMETER, PigOutputConstant.SEQUENCEFILE_STORER_VALUE, EqualityOperator.EQ));

        Set<DistributionModuleGroup> hs = new HashSet<>();
        hs.add(new DistributionModuleGroup(PIG_HCAT_MODULE_GROUP_NAME, false, hcatStorerCondition));
        hs.add(new DistributionModuleGroup(HBASE_MODULE_GROUP_NAME, false, hbaseStorerCondition));
        hs.add(new DistributionModuleGroup(PIG_HBASE_MODULE_GROUP_NAME, false, hbaseStorerCondition));
        hs.add(new DistributionModuleGroup(PIG_AVRO_MODULE_GROUP_NAME, false, avroStorerCondition));
        hs.add(new DistributionModuleGroup(PIG_RCFILE_MODULE_GROUP_NAME, false, rcfileStorerCondition));
        hs.add(new DistributionModuleGroup(PIG_SEQUENCEFILE_MODULE_GROUP_NAME, false, sequencefileStorerCondition));
        return hs;
    }

}
