// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.hadoop.distribution.dynamic.cdh;

import org.talend.hadoop.distribution.constants.cdh.IClouderaDistribution;
import org.talend.hadoop.distribution.dynamic.AbstractDynamicDistributionsGroup;
import org.talend.hadoop.distribution.dynamic.IDynamicDistribution;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionPreference;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicCDHDistributionsGroup extends AbstractDynamicDistributionsGroup implements IDynamicCDHDistributionsGroup {

    @Override
    public String getDistribution() {
        return IClouderaDistribution.DISTRIBUTION_NAME;
    }

    @Override
    public String getDistributionDisplay() {
        return IClouderaDistribution.DISTRIBUTION_DISPLAY_NAME;
    }

    @Override
    protected Class<? extends IDynamicDistribution> getDynamicDistributionClass() {
        return IDynamicCDHDistribution.class;
    }

    @Override
    public IDynamicDistributionPreference getDynamicDistributionPreference() {
        return DynamicCDHDistributionPreference.getInstance();
    }

}
