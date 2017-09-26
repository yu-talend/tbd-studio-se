package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.common.AbstractHadoopForm;
import org.talend.repository.hadoopcluster.util.EHadoopClusterImage;
import org.talend.repository.model.hadoopcluster.HadoopClusterConnectionItem;

public class DynamicBuildConfigurationWizard extends Wizard {

    private AbstractHadoopForm parentForm;

    private HadoopClusterConnectionItem connectionItem;

    private String contextGroup;

    private boolean creation;

    private AbstractDynamicConfigurationPage optionPage;

    private String confJarName;

    public DynamicBuildConfigurationWizard(AbstractHadoopForm parentForm, HadoopClusterConnectionItem connectionItem,
            String contextGroup,
            boolean creation) {
        super();
        this.parentForm = parentForm;
        this.connectionItem = connectionItem;
        this.contextGroup = contextGroup;
        this.creation = creation;
        setNeedsProgressMonitor(true);
        setForcePreviousAndNextButtons(true);
    }

    @Override
    public String getWindowTitle() {
        return Messages.getString("HadoopImportConfsWizard.title"); //$NON-NLS-1$
    }

    @Override
    public void addPages() {
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EHadoopClusterImage.HADOOPCLUSTER_WIZ));
        optionPage = new DynamicChoicePage();
        addPage(optionPage);
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
