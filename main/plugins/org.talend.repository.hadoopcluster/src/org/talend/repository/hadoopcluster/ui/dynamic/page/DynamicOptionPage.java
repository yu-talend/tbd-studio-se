package org.talend.repository.hadoopcluster.ui.dynamic.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicOptionForm;

public class DynamicOptionPage extends AbstractDynamicConfigurationPage {

    public DynamicOptionPage(IDynamicDistributionsGroup dynamicDistributionsGroup) {
        super(DynamicOptionPage.class.getSimpleName(), dynamicDistributionsGroup); // $NON-NLS-1$
        setTitle(Messages.getString("DynamicChoicePage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("DynamicChoicePage.description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {

        IDynamicMonitor monitor = new IDynamicMonitor() {

            @Override
            public void writeMessage(String message) {
                // nothing to do
            }
        };

        AbstractDynamicDistributionForm setupForm = new DynamicOptionForm(parent, SWT.NONE, getDynamicDistributionsGroup(),
                monitor);

        setControl(setupForm);
        setCurrentForm(setupForm);
        
        setPageComplete(false);
    }

    @Override
    public boolean isPageComplete() {
        return getCurrentForm().isComplete();
    }

}
