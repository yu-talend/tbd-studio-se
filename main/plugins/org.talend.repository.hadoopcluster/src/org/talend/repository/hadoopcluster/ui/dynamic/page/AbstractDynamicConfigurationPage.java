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
import org.talend.hadoop.distribution.model.DistributionBean;

public abstract class AbstractDynamicConfigurationPage extends WizardPage {

    protected DistributionBean distributionBean;

    protected AbstractDynamicConfigurationPage(String pageName, DistributionBean distributionBean) {
        super(pageName);
        this.distributionBean = distributionBean;
    }

}
