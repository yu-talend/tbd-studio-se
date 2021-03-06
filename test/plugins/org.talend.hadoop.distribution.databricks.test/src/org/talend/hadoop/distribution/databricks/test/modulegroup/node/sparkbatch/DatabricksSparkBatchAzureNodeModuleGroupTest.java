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
package org.talend.hadoop.distribution.databricks.test.modulegroup.node.sparkbatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.databricks.DatabricksConstant;
import org.talend.hadoop.distribution.databricks.DatabricksDistribution;
import org.talend.hadoop.distribution.databricks.modulegroup.node.sparkbatch.DatabricksSparkBatchAzureNodeModuleGroup;

public class DatabricksSparkBatchAzureNodeModuleGroupTest {

    @Test
    public void testModuleGroups() throws Exception {
        Map<String, String> results = new HashMap<>();
        results
                .put(DatabricksConstant.SPARK_AZURE_MRREQUIRED_MODULE_GROUP.getModuleName(),
                        "((#LINK@NODE.STORAGE_CONFIGURATION.DISTRIBUTION=='DATABRICKS') AND (#LINK@NODE.STORAGE_CONFIGURATION.SPARK_VERSION=='Databricks_3_5')) AND (#LINK@NODE.STORAGE_CONFIGURATION.SPARK_LOCAL_MODE=='false')"); //$NON-NLS-1$

        Set<DistributionModuleGroup> moduleGroups =
                DatabricksSparkBatchAzureNodeModuleGroup.getModuleGroups(DatabricksDistribution.DISTRIBUTION_NAME,
                        DatabricksDistribution.VERSION);
        assertEquals(results.size(), moduleGroups.size());

        for (DistributionModuleGroup module : moduleGroups) {
            assertTrue("Should contain module " + module.getModuleName(), results.containsKey(module.getModuleName())); //$NON-NLS-1$
            if (results.get(module.getModuleName()) == null) {
                assertTrue("The condition of the module " + module.getModuleName() + " is not null.", //$NON-NLS-1$ //$NON-NLS-2$
                        results.get(module.getModuleName()) == null);
            } else {
                assertTrue("The condition of the module " + module.getModuleName() + " is null, but it should be " //$NON-NLS-1$ //$NON-NLS-2$
                        + results.get(module.getModuleName()) + ".", results.get(module.getModuleName()) != null); //$NON-NLS-1$
                assertEquals(results.get(module.getModuleName()), module.getRequiredIf().getConditionString());
            }
        }
    }
}
