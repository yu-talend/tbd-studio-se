// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkbatch;

import java.util.HashSet;
import java.util.Set;

import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.condition.common.SparkBatchLinkedNodeCondition;
import org.talend.hadoop.distribution.ibms210.IBMS210Constant;
import org.talend.hadoop.distribution.ibms210.IBMS210Distribution;

public class IBMS210SparkBatchParquetNodeModuleGroup {

	public static Set<DistributionModuleGroup> getModuleGroups() {
		Set<DistributionModuleGroup> hs = new HashSet<>();
		DistributionModuleGroup dmg = new DistributionModuleGroup(
				IBMS210Constant.SPARK_PARQUET_MRREQUIRED_MODULE_GROUP
						.getModuleName(),
				true, new SparkBatchLinkedNodeCondition(
						IBMS210Distribution.DISTRIBUTION_NAME,
						IBMS210Distribution.VERSION).getCondition());
		hs.add(dmg);
		return hs;
	}
}
