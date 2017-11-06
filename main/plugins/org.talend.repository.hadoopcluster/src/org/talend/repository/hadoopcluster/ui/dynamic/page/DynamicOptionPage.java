package org.talend.repository.hadoopcluster.ui.dynamic.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.designer.maven.aether.DummyDynamicMonitor;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm;
import org.talend.repository.hadoopcluster.ui.dynamic.form.AbstractDynamicDistributionForm.ICheckListener;
import org.talend.repository.hadoopcluster.ui.dynamic.form.DynamicOptionForm;

public class DynamicOptionPage extends AbstractDynamicConfigurationPage {

    public DynamicOptionPage(DynamicBuildConfigurationData configData) {
        super(DynamicOptionPage.class.getSimpleName(), configData); // $NON-NLS-1$
        setTitle(Messages.getString("DynamicChoicePage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("DynamicChoicePage.description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent) {

        IDynamicMonitor monitor = new DummyDynamicMonitor();

        AbstractDynamicDistributionForm.ICheckListener checkListener = new ICheckListener() {

            @Override
            public void showMessage(String message, int level) {
                setMessage(message, level);
            }

            @Override
            public void updateButtons() {
                getContainer().updateButtons();
            }

            @Override
            public String getMessage() {
                return DynamicOptionPage.this.getMessage();
            }

        };

        AbstractDynamicDistributionForm setupForm = new DynamicOptionForm(parent, SWT.NONE, getDynamicBuildConfigurationData(),
                monitor);
        setupForm.setCheckListener(checkListener);

        setControl(setupForm);
        setCurrentForm(setupForm);
        
        setPageComplete(false);
    }

    @Override
    public boolean isPageComplete() {
        return getCurrentForm().isComplete();
    }

}
