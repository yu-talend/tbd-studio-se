package org.talend.repository.hadoopcluster.ui.dynamic;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.designer.maven.aether.AbsDynamicProgressMonitor;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.DynamicDistributionManager;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;
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

        try {
            Throwable throwable[] = new Throwable[1];

            ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());

            progressDialog.run(true, true, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        final IDynamicMonitor dMonitor = new AbsDynamicProgressMonitor(monitor) {

                            @Override
                            public void writeMessage(String message) {
                                if (CorePlugin.getDefault().isDebugging()) {
                                    System.out.print(message);
                                }
                            }
                        };
                        IDynamicPlugin dynamicPlugin = configData.getDynamicPlugin();
                        if (dynamicPlugin != null) {
                            dMonitor.beginTask(Messages.getString("DynamicBuildConfigurationWizard.finish.progress.save"), //$NON-NLS-1$
                                    IDynamicMonitor.UNKNOWN);
                            DynamicPluginAdapter pluginAdapter = new DynamicPluginAdapter(dynamicPlugin);
                            pluginAdapter.cleanUnusedAndRefresh();
                            IDynamicPlugin fDynPlugin = pluginAdapter.getPlugin();
                            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(new RepositoryWorkUnit<Boolean>(
                                    Messages.getString("DynamicBuildConfigurationWizard.repositoryWorkUnit.title")) { //$NON-NLS-1$

                                @Override
                                protected void run() throws LoginException, PersistenceException {
                                    result = false;
                                    try {
                                        DynamicDistributionManager.getInstance().saveUsersDynamicPlugin(fDynPlugin, dMonitor);
                                    } catch (Exception e) {
                                        throw new PersistenceException(e);
                                    }
                                    result = true;
                                }

                            });

                            dMonitor.setTaskName(Messages.getString("DynamicBuildConfigurationWizard.finish.progress.regist")); //$NON-NLS-1$
                            ActionType actionType = configData.getActionType();
                            if (ActionType.EditExisting.equals(actionType)) {
                                configData.getDynamicDistributionsGroup().unregist(fDynPlugin, dMonitor);
                            }
                            configData.getDynamicDistributionsGroup().regist(fDynPlugin, dMonitor);

                            dMonitor.setTaskName(
                                    Messages.getString("DynamicBuildConfigurationWizard.finish.progress.resetCache")); //$NON-NLS-1$
                            DynamicDistributionManager.getInstance().resetSystemCache();
                        }
                    } catch (Throwable ex) {
                        throwable[0] = ex;
                    }
                }
            });
            if (throwable[0] != null) {
                throw throwable[0];
            }
        } catch (Throwable e) {
            ExceptionHandler.process(e);
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = Messages.getString("ExceptionDialog.message.empty"); //$NON-NLS-1$
            }
            ExceptionMessageDialog.openError(getShell(), Messages.getString("ExceptionDialog.title"), message, e); //$NON-NLS-1$
            return false;
        }
        return true;
    }

}
