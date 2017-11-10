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
package org.talend.hadoop.distribution.preference.initializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.talend.hadoop.distribution.dynamic.cdh.DynamicCDHDistributionPreference;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionSettingsInitializer extends AbstractPreferenceInitializer {

    public DynamicDistributionSettingsInitializer() {
        super();
    }

    @Override
    public void initializeDefaultPreferences() {
        DynamicCDHDistributionPreference.getInstance().initDefaultPreference();
    }

    public Collection<String> getPreferencePaths() {
        Set<String> paths = new HashSet<>();
        String cdhPath = DynamicCDHDistributionPreference.getInstance().getPreferencePath();
        paths.add(cdhPath);
        return paths;
    }

}
