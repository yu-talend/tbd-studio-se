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
package org.talend.hadoop.distribution.dynamic.cdh;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionPreference;
import org.talend.utils.security.CryptoHelper;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicCDHDistributionPreference implements IDynamicDistributionPreference {

    private static final String PREF_OVERRIDE_DEFAULT_SETUP = "distribution.dynamic.repository.cdh.overrideDefaultSetup"; //$NON-NLS-1$

    private static final boolean PREF_OVERRIDE_DEFAULT_SETUP_DEFAULT = false;

    private static final String PREF_REPOSITORY = "distribution.dynamic.repository.cdh.repository"; //$NON-NLS-1$

    private static final String PREF_REPOSITORY_DEFAULT = "https://talend-update.talend.com/nexus/content/repositories/cloudera-repos/"; //$NON-NLS-1$

    private static final String PREF_ANONYMOUS = "distribution.dynamic.repository.cdh.isAnonymous"; //$NON-NLS-1$

    private static final boolean PREF_ANONYMOUS_DEFAULT = true;

    private static final String PREF_USERNAME = "distribution.dynamic.repository.cdh.username"; //$NON-NLS-1$

    private static final String PREF_USERNAME_DEFAULT = ""; //$NON-NLS-1$

    private static final String PREF_PASSWORD = "distribution.dynamic.repository.cdh.password"; //$NON-NLS-1$

    private static final String PREF_PASSWORD_DEFAULT = ""; //$NON-NLS-1$

    private IPreferenceStore prefStore;

    private CryptoHelper cryptoHelper;

    private static DynamicCDHDistributionPreference instance;

    public static DynamicCDHDistributionPreference getInstance() {
        if (instance == null) {
            instance = new DynamicCDHDistributionPreference();
        }
        return instance;
    }

    private DynamicCDHDistributionPreference() {
        prefStore = CoreRuntimePlugin.getInstance().getProjectPreferenceManager().getPreferenceStore();
        cryptoHelper = CryptoHelper.getDefault();
    }

    @Override
    public void initDefaultPreference() {
        prefStore.setDefault(PREF_OVERRIDE_DEFAULT_SETUP, getDefaultOverrideDefaultSetup());
        prefStore.setDefault(PREF_REPOSITORY, getDefaultRepository());
        prefStore.setDefault(PREF_ANONYMOUS, getDefaultIsAnonymous());
        prefStore.setDefault(PREF_USERNAME, getDefaultUsername());
        prefStore.setDefault(PREF_PASSWORD, cryptoHelper.encrypt(getDefaultPassword()));
    }

    @Override
    public boolean overrideDefaultSetup() {
        return prefStore.getBoolean(PREF_OVERRIDE_DEFAULT_SETUP);
    }

    @Override
    public boolean getDefaultOverrideDefaultSetup() {
        return PREF_OVERRIDE_DEFAULT_SETUP_DEFAULT;
    }

    @Override
    public void setOverrideDefaultSetup(boolean override) {
        prefStore.setValue(PREF_OVERRIDE_DEFAULT_SETUP, override);
    }

    @Override
    public String getRepository() {
        return prefStore.getString(PREF_REPOSITORY);
    }

    @Override
    public String getDefaultRepository() {
        return PREF_REPOSITORY_DEFAULT;
    }

    @Override
    public void setRepository(String repository) {
        prefStore.setValue(PREF_REPOSITORY, repository);
    }

    @Override
    public boolean isAnonymous() {
        return prefStore.getBoolean(PREF_ANONYMOUS);
    }

    @Override
    public boolean getDefaultIsAnonymous() {
        return PREF_ANONYMOUS_DEFAULT;
    }

    @Override
    public void setAnonymous(boolean anonymous) {
        prefStore.setValue(PREF_ANONYMOUS, anonymous);
    }

    @Override
    public String getUsername() {
        return prefStore.getString(PREF_USERNAME);
    }

    @Override
    public String getDefaultUsername() {
        return PREF_USERNAME_DEFAULT;
    }

    @Override
    public void setUsername(String username) {
        prefStore.setValue(PREF_USERNAME, username);
    }

    @Override
    public String getPassword() {
        String password = prefStore.getString(PREF_PASSWORD);
        if (StringUtils.isNotEmpty(password)) {
            password = cryptoHelper.decrypt(password);
        }
        return password;
    }

    @Override
    public String getDefaultPassword() {
        return PREF_PASSWORD_DEFAULT;
    }

    @Override
    public void setPassword(String password) {
        if (password == null) {
            password = ""; //$NON-NLS-1$
        }
        prefStore.setValue(PREF_PASSWORD, cryptoHelper.encrypt(password));
    }

    @Override
    public void save() throws Exception {
        ((IPersistentPreferenceStore) prefStore).save();
    }

}
