package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.repository.hadoopcluster.i18n.Messages;

public class DynamicChoicePage extends AbstractDynamicConfigurationPage {

    public DynamicChoicePage() {
        super(DynamicChoicePage.class.getSimpleName(), null); // $NON-NLS-1$
        setTitle(Messages.getString("DynamicChoicePage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("DynamicChoicePage.description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {

        AbstractDynamicDistributionForm setupForm = new DynamicBuildConfigurationFrom(parent, SWT.NONE);

        setControl(setupForm);
        
        setPageComplete(false);
    }

    @Override
    public IWizardPage getNextPage() {
        IWizardPage nextPage = super.getNextPage();
        return nextPage;
    }

}
