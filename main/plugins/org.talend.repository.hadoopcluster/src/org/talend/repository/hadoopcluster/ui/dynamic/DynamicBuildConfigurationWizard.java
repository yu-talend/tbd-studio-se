package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.DynamicDistributionManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData.ActionType;
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
        final IDynamicMonitor monitor = new IDynamicMonitor() {

            @Override
            public void writeMessage(String message) {
                // TODO Auto-generated method stub

            }
        };

        try {
            final IDynamicPlugin dynamicPlugin = configData.getDynamicPlugin();
            if (dynamicPlugin != null) {
                ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(new RepositoryWorkUnit<Boolean>(
                        Messages.getString("DynamicBuildConfigurationWizard.repositoryWorkUnit.title")) { //$NON-NLS-1$

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        result = false;
                        try {
                            DynamicDistributionManager.getInstance().saveUsersDynamicPlugin(dynamicPlugin, monitor);
                        } catch (Exception e) {
                            throw new PersistenceException(e);
                        }
                        result = true;
                    }

                });
                ActionType actionType = configData.getActionType();
                if (ActionType.EditExisting.equals(actionType)) {
                    configData.getDynamicDistributionsGroup().unregist(dynamicPlugin, monitor);
                }
                configData.getDynamicDistributionsGroup().regist(dynamicPlugin, monitor);
                DynamicDistributionManager.getInstance().cleanSystemCache();
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return true;
    }

}
