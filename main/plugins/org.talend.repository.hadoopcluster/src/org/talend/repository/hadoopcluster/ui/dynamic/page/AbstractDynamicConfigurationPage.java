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
package org.talend.repository.hadoopcluster.ui.dynamic.page;

import org.eclipse.jface.wizard.WizardPage;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;

public abstract class AbstractDynamicConfigurationPage extends WizardPage {

    private AbstractDynamicDistributionForm currentForm;

    private DynamicBuildConfigurationData configData;

    protected AbstractDynamicConfigurationPage(String pageName, DynamicBuildConfigurationData configData) {
        super(pageName);
        this.configData = configData;
    }

    protected void setCurrentForm(AbstractDynamicDistributionForm form) {
        this.currentForm = form;
    }

    protected AbstractDynamicDistributionForm getCurrentForm() {
        return this.currentForm;
    }

    protected DynamicBuildConfigurationData getDynamicBuildConfigurationData() {
        return this.configData;
    }

    public boolean canFinish() {
        return getCurrentForm().canFinish();
    }

    @Override
    public boolean canFlipToNextPage() {
        return getCurrentForm().canFlipToNextPage();
    }

}
