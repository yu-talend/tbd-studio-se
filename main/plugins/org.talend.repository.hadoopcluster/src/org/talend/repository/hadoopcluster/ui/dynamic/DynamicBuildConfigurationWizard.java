package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.page.AbstractDynamicConfigurationPage;
import org.talend.repository.hadoopcluster.ui.dynamic.page.DynamicOptionPage;
import org.talend.repository.hadoopcluster.ui.dynamic.page.DynamicRetrievePage;
import org.talend.repository.hadoopcluster.util.EHadoopClusterImage;

public class DynamicBuildConfigurationWizard extends Wizard {

    private DynamicOptionPage optionPage;

    private DynamicRetrievePage retrivePage;

    private DynamicBuildConfigurationData configData;

    public DynamicBuildConfigurationWizard(DynamicBuildConfigurationData configData) {
        super();
        this.configData = configData;
        setNeedsProgressMonitor(true);
        setForcePreviousAndNextButtons(true);
    }

    @Override
    public String getWindowTitle() {
        return Messages.getString("DynamicBuildConfigurationWizard.title"); //$NON-NLS-1$
    }

    @Override
    public void addPages() {
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EHadoopClusterImage.HADOOPCLUSTER_WIZ));
        optionPage = new DynamicOptionPage(configData);
        retrivePage = new DynamicRetrievePage(configData);
        addPage(optionPage);
        addPage(retrivePage);
    }

    @Override
    public boolean canFinish() {
        IWizardPage currentPage = getContainer().getCurrentPage();
        if (currentPage instanceof AbstractDynamicConfigurationPage) {
            return ((AbstractDynamicConfigurationPage) currentPage).canFinish();
        } else {
            return super.canFinish();
        }
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
