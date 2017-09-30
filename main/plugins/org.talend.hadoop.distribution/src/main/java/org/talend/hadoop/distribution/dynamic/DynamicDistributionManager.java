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
package org.talend.hadoop.distribution.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.talend.hadoop.distribution.dynamic.cdh.DynamicCDHDistributionsGroup;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionManager {

    private List<AbstractDynamicDistributionsGroup> dynamicDistributionsGroups;

    public List<AbstractDynamicDistributionsGroup> getDynamicDistributionsGroups() throws Exception {

        if (dynamicDistributionsGroups != null) {
            return dynamicDistributionsGroups;
        }

        dynamicDistributionsGroups = new ArrayList<>();

        dynamicDistributionsGroups.add(new DynamicCDHDistributionsGroup());

        return dynamicDistributionsGroups;

    }

}
