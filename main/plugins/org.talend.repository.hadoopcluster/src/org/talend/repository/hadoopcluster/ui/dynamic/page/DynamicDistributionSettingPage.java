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
import org.eclipse.swt.widgets.Control;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicDistributionsForm;
import org.talend.repository.preference.ProjectSettingPage;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionSettingPage extends ProjectSettingPage {

    private AbstractDynamicDistributionForm distributionForm;

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Control createContents(Composite parent) {
        IDynamicMonitor monitor = new IDynamicMonitor() {

            @Override
            public void writeMessage(String message) {
                // nothing to do
            }
        };
        DynamicDistributionsForm existingConfigForm = new DynamicDistributionsForm(parent, SWT.NONE, monitor);
        setCurrentForm(existingConfigForm);

        return existingConfigForm;
    }

    private void setCurrentForm(AbstractDynamicDistributionForm distributionForm) {
        this.distributionForm = distributionForm;
    }

    private AbstractDynamicDistributionForm getCurrentForm() {
        return this.distributionForm;
    }

}
