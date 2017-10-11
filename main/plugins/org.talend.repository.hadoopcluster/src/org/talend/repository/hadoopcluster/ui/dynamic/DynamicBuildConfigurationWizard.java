package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.page.DynamicOptionPage;
import org.talend.repository.hadoopcluster.ui.dynamic.page.DynamicRetrievePage;
import org.talend.repository.hadoopcluster.util.EHadoopClusterImage;

public class DynamicBuildConfigurationWizard extends Wizard {

    private DynamicOptionPage optionPage;

    private DynamicRetrievePage retrivePage;

    private IDynamicDistributionsGroup dynamicDistributionsGroup;

    public DynamicBuildConfigurationWizard(IDynamicDistributionsGroup dynamicDistributionsGroup) {
        super();
        this.dynamicDistributionsGroup = dynamicDistributionsGroup;
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
        optionPage = new DynamicOptionPage(this.dynamicDistributionsGroup);
        retrivePage = new DynamicRetrievePage(this.dynamicDistributionsGroup);
        addPage(optionPage);
        addPage(retrivePage);
    }

    @Override
    public boolean canFinish() {
        return super.canFinish();
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
