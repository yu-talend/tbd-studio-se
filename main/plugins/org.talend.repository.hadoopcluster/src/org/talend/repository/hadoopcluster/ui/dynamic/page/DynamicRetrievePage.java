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
package org.talend.repository.hadoopcluster.ui.dynamic.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.maven.aether.DummyDynamicMonitor;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm.ICheckListener;
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicBuildConfigurationForm;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicRetrievePage extends AbstractDynamicConfigurationPage {

    public DynamicRetrievePage(DynamicBuildConfigurationData configData) {
        super(DynamicRetrievePage.class.getSimpleName(), configData);
        setTitle(Messages.getString("DynamicRetrievePage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("DynamicRetrievePage.description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {

        IDynamicMonitor monitor = new DummyDynamicMonitor();

        AbstractDynamicDistributionForm.ICheckListener checkListener = new ICheckListener() {

            @Override
            public void showMessage(String message, int level) {
                setMessage(message, level);
                setErrorMessage(message);
            }

            @Override
            public void updateButtons() {
                getContainer().updateButtons();
            }

        };

        AbstractDynamicDistributionForm setupForm = new DynamicBuildConfigurationForm(parent, SWT.NONE,
                getDynamicBuildConfigurationData(), monitor);
        setupForm.setCheckListener(checkListener);

        setControl(setupForm);
        setCurrentForm(setupForm);

        setPageComplete(false);
    }

    @Override
    public boolean isPageComplete() {
        if (!isCurrentPage()) {
            return false;
        }
        return getCurrentForm().isComplete();
    }

}
