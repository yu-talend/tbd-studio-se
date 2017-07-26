// ============================================================================
package org.talend.hadoop.distribution.ibms210.modulegroup.node.sparkbatch;

import java.util.HashSet;
import java.util.Set;

import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.condition.common.SparkBatchLinkedNodeCondition;
import org.talend.hadoop.distribution.constants.SparkBatchConstant;
import org.talend.hadoop.distribution.ibms210.IBMS210Constant;
import org.talend.hadoop.distribution.ibms210.IBMS210Distribution;

public class IBMS210GraphFramesNodeModuleGroup {

    public static Set<DistributionModuleGroup> getModuleGroups() {
        Set<DistributionModuleGroup> hs = new HashSet<>();
        DistributionModuleGroup dmg = new DistributionModuleGroup(
        		IBMS210Constant.GRAPHFRAMES_MRREQUIRED_MODULE_GROUP.getModuleName(), true, new SparkBatchLinkedNodeCondition(
        				IBMS210Distribution.DISTRIBUTION_NAME, IBMS210Distribution.VERSION,
                        SparkBatchConstant.SPARK_BATCH_SPARKCONFIGURATION_LINKEDPARAMETER).getCondition());
        hs.add(dmg);
        return hs;
    }
}
