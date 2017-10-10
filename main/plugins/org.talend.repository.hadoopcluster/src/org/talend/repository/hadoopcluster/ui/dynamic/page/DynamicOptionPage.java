package org.talend.repository.hadoopcluster.ui.dynamic.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicOptionForm;

public class DynamicOptionPage extends AbstractDynamicConfigurationPage {

    public DynamicOptionPage() {
        super(DynamicOptionPage.class.getSimpleName()); // $NON-NLS-1$
        setTitle(Messages.getString("DynamicChoicePage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("DynamicChoicePage.description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {

        AbstractDynamicDistributionForm setupForm = new DynamicOptionForm(parent, SWT.NONE);

        setControl(setupForm);
        setCurrentForm(setupForm);
        
        setPageComplete(false);
    }

    @Override
    public boolean isPageComplete() {
        return getCurrentForm().isComplete();
    }

}
