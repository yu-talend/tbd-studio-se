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
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicDistributionsForm;
import org.talend.repository.preference.ProjectSettingPage;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionSettingPage extends ProjectSettingPage {

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Control createContents(Composite parent) {
        DynamicDistributionsForm existingConfigForm = new DynamicDistributionsForm(parent, SWT.NONE);
        return existingConfigForm;
    }

}
