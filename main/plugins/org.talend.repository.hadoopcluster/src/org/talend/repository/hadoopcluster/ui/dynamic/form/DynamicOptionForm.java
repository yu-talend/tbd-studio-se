// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.hadoopcluster.ui.dynamic.form;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.DynamicServiceUtil;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.designer.maven.aether.AbsDynamicProgressMonitor;
import org.talend.designer.maven.aether.DummyDynamicMonitor;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.DynamicConfiguration;
import org.talend.hadoop.distribution.dynamic.DynamicDistributionManager;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicDistriConfigAdapter;
import org.talend.hadoop.distribution.dynamic.comparator.DynamicPluginComparator;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData.ActionType;
import org.talend.repository.ui.login.LoginDialogV2;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class DynamicOptionForm extends AbstractDynamicDistributionForm {

    private Button newConfigBtn;

    private Button editExistingConfigBtn;

    private Button importConfigBtn;

    private Button deleteExistingConfigBtn;

    private Text configNameText;

    private ComboViewer existingConfigsComboViewer;

    private Text importConfigText;

    private Button importConfigBrowseBtn;

    private Text descriptionText;

    private Composite newConfigGroup;

    private Composite editExistingGroup;

    private Composite importConfigGroup;

    private Set<String> existingConfigurationNames;

    private Map<String, IDynamicPlugin> existingConfigurationIdMap;

    private String userInputDescription = ""; //$NON-NLS-1$

    private IDynamicPlugin importedDynamicPlugin;

    private DynamicConfiguration dynamicConfiguration;

    private List<IDynamicPlugin> allBuildinDynamicPlugins;

    private List<IDynamicPlugin> allCurrentUsersDynamicPlugins;

    public DynamicOptionForm(Composite parent, int style, DynamicBuildConfigurationData configData, IDynamicMonitor monitor) {
        super(parent, style, configData);
        createControl();
        initData(monitor);

        newConfigBtn.setSelection(true);
        onNewConfigSelected(true);
        onEditExistingSelected(false);
        onImportConfigSelected(false);

        addListeners();
    }

    protected void createControl() {
        Composite parent = this;

        Composite container = createFormContainer(parent);

        int ALIGN_VERTICAL = getAlignVertical();
        int ALIGN_VERTICAL_INNER = getAlignVerticalInner();
        int ALIGN_HORIZON = getAlignHorizon();
        int HORZON_WIDTH = getHorizonWidth();

        newConfigBtn = new Button(container, SWT.RADIO);
        newConfigBtn.setText(Messages.getString("DynamicOptionForm.form.newConfigBtn")); //$NON-NLS-1$
        FormData formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        newConfigBtn.setLayoutData(formData);

        newConfigGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(newConfigBtn, ALIGN_VERTICAL_INNER, SWT.BOTTOM);
        formData.left = new FormAttachment(newConfigBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(newConfigBtn, 0, SWT.RIGHT);
        newConfigGroup.setLayoutData(formData);
        newConfigGroup.setLayout(new FormLayout());

        configNameText = new Text(newConfigGroup, SWT.BORDER);
        formData = new FormData();
        formData.top = new FormAttachment();
        formData.left = new FormAttachment(0, HORZON_WIDTH);
        formData.right = new FormAttachment(100);
        configNameText.setLayoutData(formData);

        Label nameLabel = new Label(newConfigGroup, SWT.NONE);
        nameLabel.setText(Messages.getString("DynamicOptionForm.form.nameLabel")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(configNameText, 0, SWT.CENTER);
        formData.right = new FormAttachment(configNameText, -1 * ALIGN_HORIZON, SWT.LEFT);
        nameLabel.setLayoutData(formData);

        editExistingConfigBtn = new Button(container, SWT.RADIO);
        editExistingConfigBtn.setText(Messages.getString("DynamicOptionForm.form.editExistingConfigBtn")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(newConfigGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(newConfigGroup, 0, SWT.LEFT);
        formData.right = new FormAttachment(newConfigGroup, 0, SWT.RIGHT);
        editExistingConfigBtn.setLayoutData(formData);

        editExistingGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(editExistingConfigBtn, ALIGN_VERTICAL_INNER, SWT.BOTTOM);
        formData.left = new FormAttachment(editExistingConfigBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(editExistingConfigBtn, 0, SWT.RIGHT);
        editExistingGroup.setLayoutData(formData);
        editExistingGroup.setLayout(new FormLayout());

        existingConfigsComboViewer = new ComboViewer(editExistingGroup, SWT.READ_ONLY);
        existingConfigsComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        existingConfigsComboViewer.setLabelProvider(new ExistingConfigsLabelProvider());

        deleteExistingConfigBtn = new Button(editExistingGroup, SWT.PUSH);
        deleteExistingConfigBtn.setText(Messages.getString("DynamicOptionForm.form.deleteExistingConfigBtn")); //$NON-NLS-1$

        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0, HORZON_WIDTH);
        formData.right = new FormAttachment(deleteExistingConfigBtn, -1 * ALIGN_HORIZON, SWT.LEFT);
        existingConfigsComboViewer.getControl().setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(existingConfigsComboViewer.getControl(), 0, SWT.CENTER);
        // formData.bottom = new FormAttachment(existingConfigsComboViewer.getControl(), 0, SWT.BOTTOM);
        formData.right = new FormAttachment(100);
        formData.width = getNewButtonSize(deleteExistingConfigBtn).x;
        deleteExistingConfigBtn.setLayoutData(formData);

        importConfigBtn = new Button(container, SWT.RADIO);
        importConfigBtn.setText(Messages.getString("DynamicOptionForm.form.importConfigBtn")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(editExistingGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(editExistingGroup, 0, SWT.LEFT);
        formData.right = new FormAttachment(editExistingGroup, 0, SWT.RIGHT);
        importConfigBtn.setLayoutData(formData);

        importConfigGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(importConfigBtn, ALIGN_VERTICAL_INNER, SWT.BOTTOM);
        formData.left = new FormAttachment(importConfigBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(importConfigBtn, 0, SWT.RIGHT);
        importConfigGroup.setLayoutData(formData);
        importConfigGroup.setLayout(new FormLayout());

        importConfigText = new Text(importConfigGroup, SWT.BORDER);
        importConfigText.setEditable(false);

        importConfigBrowseBtn = new Button(importConfigGroup, SWT.PUSH);
        importConfigBrowseBtn.setText(Messages.getString("DynamicOptionForm.form.importConfig.browse")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(100, -1 * getNewButtonSize(importConfigBrowseBtn).x);
        formData.right = new FormAttachment(100);
        importConfigBrowseBtn.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(importConfigBrowseBtn, 0, SWT.CENTER);
        formData.left = new FormAttachment(0, HORZON_WIDTH);
        formData.right = new FormAttachment(importConfigBrowseBtn, -1 * ALIGN_HORIZON, SWT.LEFT);
        importConfigText.setLayoutData(formData);

        descriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        formData = new FormData();
        formData.top = new FormAttachment(importConfigGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(importConfigGroup, 0, SWT.LEFT);
        formData.right = new FormAttachment(importConfigGroup, 0, SWT.RIGHT);
        formData.bottom = new FormAttachment(100);
        descriptionText.setLayoutData(formData);

    }

    protected void addListeners() {

        newConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onNewConfigSelected(newConfigBtn.getSelection());
                updateButtons();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                onNewConfigSelected(newConfigBtn.getSelection());
                updateButtons();
            }

        });

        editExistingConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onEditExistingSelected(editExistingConfigBtn.getSelection());
                updateButtons();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                onEditExistingSelected(editExistingConfigBtn.getSelection());
                updateButtons();
            }

        });

        deleteExistingConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onDeleteExistingSelected();
                updateButtons();
            }

        });

        importConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onImportConfigSelected(importConfigBtn.getSelection());
                updateButtons();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                onImportConfigSelected(importConfigBtn.getSelection());
                updateButtons();
            }

        });

        configNameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                updateButtons();
            }

        });

        existingConfigsComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                updateButtons();
            }

        });

        importConfigBrowseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    onImportConfigBrowseBtnSelected();
                    updateButtons();
                } catch (Exception ex) {
                    importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                    importConfigText.setToolTipText(ex.getMessage());
                    showMessage(ex.getMessage(), WizardPage.ERROR);
                    ExceptionHandler.process(ex);
                }
            }

        });

        descriptionText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                String description = descriptionText.getText();
                descriptionText.setToolTipText(description);
                dynamicConfiguration.setDescription(description);
            }
        });

    }

    private void onNewConfigSelected(boolean selected) {
        if (selected) {
            getDynamicBuildConfigurationData().setActionType(ActionType.NewConfig);
            getDynamicBuildConfigurationData().setReadonly(false);
        }

        // newConfigGroup.setEnabled(selected);
        configNameText.setEditable(selected);
        descriptionText.setEditable(selected);
        if (selected) {
            configNameText.selectAll();
            configNameText.forceFocus();
            descriptionText.setText(userInputDescription);
        } else {
            userInputDescription = descriptionText.getText();
        }
    }

    private void onEditExistingSelected(boolean selected) {
        if (selected) {
            getDynamicBuildConfigurationData().setActionType(ActionType.EditExisting);
            getDynamicBuildConfigurationData().setReadonly(true);
        }

        // editExistingGroup.setEnabled(selected);
        existingConfigsComboViewer.getControl().setEnabled(selected);
        deleteExistingConfigBtn.setEnabled(selected);
    }

    private void onDeleteExistingSelected() {
        boolean agree = MessageDialog.openConfirm(getShell(),
                Messages.getString("DynamicOptionForm.form.deleteExistingConfig.confirm.dialog.title"), //$NON-NLS-1$
                Messages.getString("DynamicOptionForm.form.deleteExistingConfig.confirm.dialog.message")); //$NON-NLS-1$
        if (agree) {
            try {
                IStructuredSelection selection = (IStructuredSelection) existingConfigsComboViewer.getSelection();
                final IDynamicPlugin dynamicPlugin = (IDynamicPlugin) selection.getFirstElement();
                doDelete(dynamicPlugin);
                IDynamicMonitor monitor = new DummyDynamicMonitor();
                refreshExistingConfigsCombo(monitor, getDynamicBuildConfigurationData().getDynamicDistributionsGroup());
            } catch (Throwable e) {
                ExceptionHandler.process(e);
                String message = e.getMessage();
                if (StringUtils.isEmpty(message)) {
                    message = Messages.getString("ExceptionDialog.message.empty"); //$NON-NLS-1$
                }
                ExceptionMessageDialog.openError(getShell(), Messages.getString("ExceptionDialog.title"), message, e); //$NON-NLS-1$
            }
        }
    }

    private void doDelete(IDynamicPlugin dynamicPlugin) throws Throwable {
        final Throwable throwable[] = new Throwable[1];
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
        progressDialog.run(true, false, new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor pMonitor) throws InvocationTargetException, InterruptedException {

                ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(new RepositoryWorkUnit<Boolean>(
                        Messages.getString("DynamicOptionForm.form.deleteExistingConfig.workunit.title")) { //$NON-NLS-1$

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        IDynamicMonitor monitor = new AbsDynamicProgressMonitor(pMonitor) {

                            @Override
                            public void writeMessage(String message) {
                                // nothing to do
                            }
                        };
                        try {
                            monitor.beginTask(Messages.getString("DynamicBuildConfigurationForm.delete.progress.unregist"), //$NON-NLS-1$
                                    IDynamicMonitor.UNKNOWN);
                            IDynamicPluginConfiguration pluginConfiguration = dynamicPlugin.getPluginConfiguration();
                            String distribution = pluginConfiguration.getDistribution();
                            IDynamicDistributionsGroup dynamicDistributionGroup = DynamicDistributionManager.getInstance()
                                    .getDynamicDistributionGroup(distribution);
                            dynamicDistributionGroup.unregist(dynamicPlugin, monitor);

                            monitor.setTaskName(Messages.getString("DynamicBuildConfigurationForm.delete.progress.deleteFile")); //$NON-NLS-1$
                            String filePath = (String) pluginConfiguration
                                    .getAttribute(DynamicDistriConfigAdapter.ATTR_FILE_PATH);
                            File file = new File(filePath);
                            file.delete();

                            monitor.setTaskName(Messages.getString("DynamicBuildConfigurationForm.delete.progress.resetCache")); //$NON-NLS-1$
                            DynamicDistributionManager.getInstance().resetSystemCache();
                        } catch (Throwable e) {
                            throwable[0] = e;
                        }
                    }

                });
            }
        });
        if (throwable[0] != null) {
            throw throwable[0];
        }

    }

    private void onImportConfigSelected(boolean selected) {
        if (selected) {
            getDynamicBuildConfigurationData().setActionType(ActionType.Import);
            getDynamicBuildConfigurationData().setReadonly(false);
        }

        // importConfigGroup.setEnabled(selected);
        // importConfigText.setEnabled(selected);
        importConfigBrowseBtn.setEnabled(selected);
        if (selected) {
            descriptionText.setText(""); //$NON-NLS-1$
        }
    }

    private void onImportConfigBrowseBtnSelected() throws Exception {
        FileDialog fileDialog = new FileDialog(getShell());
        fileDialog.setFilterExtensions(new String[] { "*." + DynamicDistributionManager.DISTRIBUTION_FILE_EXTENSION }); //$NON-NLS-1$
        String filePath = fileDialog.open();
        if (StringUtils.isNotEmpty(filePath)) {

            importedDynamicPlugin = null;
            importConfigText.setText(""); //$NON-NLS-1$

            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception(Messages.getString("DynamicDistributionsForm.importConfigText.check.fileNotExist")); //$NON-NLS-1$
            }
            String jsonContent = DynamicServiceUtil.readFile(file);
            importedDynamicPlugin = DynamicFactory.getInstance().createPluginFromJson(jsonContent);
            importConfigText.setText(filePath);
        }
    }

    private void updateDistributionDescription(IDynamicPlugin dynamicPlugin) {
        IDynamicPluginConfiguration pluginConfiguration = dynamicPlugin.getPluginConfiguration();
        String description = pluginConfiguration.getDescription();
        if (description == null) {
            description = ""; //$NON-NLS-1$
        }
        if (!description.equals(descriptionText.getText())) {
            descriptionText.setText(description);
        }
    }

    private void initData(IDynamicMonitor monitor) {
        DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
        IDynamicDistributionsGroup dynamicDistributionsGroup = dynConfigData.getDynamicDistributionsGroup();

        dynamicConfiguration = new DynamicConfiguration();
        dynamicConfiguration.setDistribution(dynamicDistributionsGroup.getDistribution());

        dynConfigData.setNewDistrConfigration(dynamicConfiguration);

        try {
            refreshExistingConfigsCombo(monitor, dynamicDistributionsGroup);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void refreshExistingConfigsCombo(IDynamicMonitor monitor, IDynamicDistributionsGroup dynamicDistributionsGroup)
            throws Exception {
        List<IDynamicPlugin> distriDynamicPlugins = copyAllUsersDynamicPlugins(monitor, dynamicDistributionsGroup);

        existingConfigsComboViewer.setInput(distriDynamicPlugins);
        if (0 < distriDynamicPlugins.size()) {
            existingConfigsComboViewer.setSelection(new StructuredSelection(distriDynamicPlugins.get(0)));
        }
    }

    private List<IDynamicPlugin> copyAllUsersDynamicPlugins(IDynamicMonitor monitor,
            IDynamicDistributionsGroup dynamicDistributionsGroup) throws Exception {
        List<IDynamicPlugin> distriDynamicPlugins = new LinkedList<>();

        /**
         * Can't edit buildin plugins
         */
        allBuildinDynamicPlugins = dynamicDistributionsGroup.getAllBuildinDynamicPlugins(monitor);
        if (allBuildinDynamicPlugins != null && !allBuildinDynamicPlugins.isEmpty()) {
            distriDynamicPlugins.addAll(allBuildinDynamicPlugins);
        }

        List<IDynamicPlugin> allUsersDynamicPlugins = DynamicDistributionManager.getInstance()
                .getAllUsersDynamicPluginsForProject(ProjectManager.getInstance().getCurrentProject(), monitor);
        if (allUsersDynamicPlugins != null && !allUsersDynamicPlugins.isEmpty()) {
            List<IDynamicPlugin> filterDynamicPlugins = dynamicDistributionsGroup.filterDynamicPlugins(allUsersDynamicPlugins,
                    monitor);
            if (filterDynamicPlugins != null && !filterDynamicPlugins.isEmpty()) {
                for (IDynamicPlugin dynPlugin : filterDynamicPlugins) {
                    IDynamicPlugin clonedPlugin = DynamicFactory.getInstance()
                            .createPluginFromJson(dynPlugin.toXmlJson().toString());
                    distriDynamicPlugins.add(clonedPlugin);
                }
            }
        }
        allCurrentUsersDynamicPlugins = allUsersDynamicPlugins;

        Collections.sort(distriDynamicPlugins, Collections.reverseOrder(new DynamicPluginComparator()));
        return distriDynamicPlugins;
    }

    private boolean checkNewConfigNameValid() {
        if (!newConfigBtn.getSelection()) {
            return true;
        }
        String configName = configNameText.getText().trim();
        if (configName.isEmpty()) {
            String errorMessage = Messages.getString("DynamicDistributionsForm.newConfigName.check.empty"); //$NON-NLS-1$
            showMessage(errorMessage, WizardPage.ERROR);
            configNameText.setBackground(LoginDialogV2.RED_COLOR);
            configNameText.setToolTipText(errorMessage);
            return false;
        }
        try {
            if (isConfigurationNameExist(configName)) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.newConfigName.check.exist", configName); //$NON-NLS-1$
                showMessage(errorMessage, WizardPage.ERROR);
                configNameText.setBackground(LoginDialogV2.RED_COLOR);
                configNameText.setToolTipText(errorMessage);
                return false;
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            showMessage(errorMessage, WizardPage.ERROR);
            configNameText.setBackground(LoginDialogV2.RED_COLOR);
            configNameText.setToolTipText(errorMessage);
            return false;
        }
        configNameText.setBackground(null);
        configNameText.setToolTipText(configNameText.getText());

        dynamicConfiguration.setName(configName);

        return true;
    }

    private boolean isConfigurationNameExist(String name) throws Exception {
        if (existingConfigurationNames == null) {
            initExistingConfigurationInfos();
        }
        return existingConfigurationNames.contains(name);
    }

    private IDynamicPlugin getDynamicPluginById(String id) throws Exception {
        if (existingConfigurationIdMap == null) {
            initExistingConfigurationInfos();
        }
        return existingConfigurationIdMap.get(id);
    }

    private void initExistingConfigurationInfos() throws Exception {
        existingConfigurationNames = new HashSet<>();
        existingConfigurationIdMap = new HashMap<>();
        IDynamicMonitor monitor = new DummyDynamicMonitor();
        DynamicDistributionManager dynDistrManager = DynamicDistributionManager.getInstance();
        List<IDynamicPlugin> allDynamicPlugins = new LinkedList<>();
        List<IDynamicPlugin> allBuildinDynamicPlugins = dynDistrManager.getAllBuildinDynamicPlugins(monitor);
        if (allBuildinDynamicPlugins != null && !allBuildinDynamicPlugins.isEmpty()) {
            allDynamicPlugins.addAll(allBuildinDynamicPlugins);
        }
        List<IDynamicPlugin> allUsesDynamicPlugins = dynDistrManager.getAllUsersDynamicPlugins(monitor);
        if (allUsesDynamicPlugins != null && !allUsesDynamicPlugins.isEmpty()) {
            allDynamicPlugins.addAll(allUsesDynamicPlugins);
        }
        if (allDynamicPlugins != null && !allDynamicPlugins.isEmpty()) {
            Iterator<IDynamicPlugin> iter = allDynamicPlugins.iterator();
            while (iter.hasNext()) {
                IDynamicPlugin dynPlugin = iter.next();
                IDynamicPluginConfiguration pluginConfiguration = dynPlugin.getPluginConfiguration();
                existingConfigurationNames.add(pluginConfiguration.getName());
                existingConfigurationIdMap.put(pluginConfiguration.getId(), dynPlugin);
            }
        }
    }

    private boolean checkImportConfigText() {
        try {
            if (!importConfigBtn.getSelection()) {
                return true;
            }
            StringBuffer messageBuffer = new StringBuffer();

            importConfigText.setBackground(null);
            importConfigText.setToolTipText(importConfigText.getText());
            descriptionText.setText(""); //$NON-NLS-1$
            String importConfig = importConfigText.getText();
            if (StringUtils.isEmpty(importConfig)) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.empty"); //$NON-NLS-1$
                showMessage(errorMessage, WizardPage.ERROR);
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                return false;
            }

            if (importedDynamicPlugin == null) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.empty"); //$NON-NLS-1$
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }

            // 1. check plugin configuration
            IDynamicPluginConfiguration pluginConfiguration = importedDynamicPlugin.getPluginConfiguration();
            if (pluginConfiguration == null) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.noConfiguration"); //$NON-NLS-1$
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }
            DynamicBuildConfigurationData dynamicBuildConfigurationData = getDynamicBuildConfigurationData();

            // 2. check distribution
            IDynamicDistributionsGroup dynamicDistributionsGroup = dynamicBuildConfigurationData.getDynamicDistributionsGroup();
            if (!dynamicDistributionsGroup.getDistribution().equalsIgnoreCase(pluginConfiguration.getDistribution())) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.wrongDistribution", //$NON-NLS-1$
                        pluginConfiguration.getDistribution(), dynamicDistributionsGroup.getDistribution());
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }

            // 3. check id
            String id = pluginConfiguration.getId();
            if (StringUtils.isEmpty(id)) {
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.badId.empty"); //$NON-NLS-1$
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }
            if (id.contains(".")) { //$NON-NLS-1$
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.badId.invalid", id); //$NON-NLS-1$
                importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                importConfigText.setToolTipText(errorMessage);
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }
            IDynamicPlugin existingDynamicPlugin = getDynamicPluginById(id);
            if (existingDynamicPlugin != null) {
                IDynamicPluginConfiguration existingPluginConfig = existingDynamicPlugin.getPluginConfiguration();
                String distribution = existingPluginConfig.getDistribution();
                if (!dynamicDistributionsGroup.getDistribution().equalsIgnoreCase(distribution)) {
                    String errorMessage = Messages.getString(
                            "DynamicDistributionsForm.importConfigText.check.badId.exist.diffDistribution", id, //$NON-NLS-1$
                            dynamicDistributionsGroup.getDistribution(), distribution); // $NON-NLS-1$
                    importConfigText.setBackground(LoginDialogV2.RED_COLOR);
                    importConfigText.setToolTipText(errorMessage);
                    showMessage(errorMessage, WizardPage.ERROR);
                    return false;
                }
                String errorMessage = Messages.getString(
                        "DynamicDistributionsForm.importConfigText.check.badId.exist.sameDistribution", //$NON-NLS-1$
                        id);
                messageBuffer.append(errorMessage).append("\n"); //$NON-NLS-1$
            }

            // 4. check name
            String name = pluginConfiguration.getName();
            if (isConfigurationNameExist(name)) {
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String newName = name + Messages.getString("DynamicDistributionsForm.importConfigText.check.badName.exist.desc", //$NON-NLS-1$
                        date.toString());
                pluginConfiguration.setName(newName);
                String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.badName.exist", //$NON-NLS-1$
                        name, newName);
                messageBuffer.append(errorMessage).append("\n"); //$NON-NLS-1$
            }

            if (0 < messageBuffer.length()) {
                String warnMessage = messageBuffer.toString();
                importConfigText.setBackground(LoginDialogV2.YELLOW_COLOR);
                importConfigText.setToolTipText(warnMessage);
                showMessage(warnMessage, WizardPage.WARNING);
            }

            descriptionText.setText(pluginConfiguration.getDescription());

            getDynamicBuildConfigurationData().setDynamicPlugin(importedDynamicPlugin);
            return true;
        } catch (Exception e) {
            importConfigText.setBackground(LoginDialogV2.RED_COLOR);
            importConfigText.setToolTipText(e.getMessage());
            return false;
        }
    }

    private boolean checkSelectExistingConfig() {
        if (!editExistingConfigBtn.getSelection()) {
            return true;
        }
        deleteExistingConfigBtn.setEnabled(false);
        IStructuredSelection selection = (IStructuredSelection) existingConfigsComboViewer.getSelection();
        if (selection == null) {
            String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.existingConfig.empty"); //$NON-NLS-1$
            showMessage(errorMessage, WizardPage.ERROR);
            return false;
        }
        Object firstElement = selection.getFirstElement();
        if (firstElement == null) {
            String errorMessage = Messages.getString("DynamicDistributionsForm.importConfigText.check.existingConfig.empty"); //$NON-NLS-1$
            showMessage(errorMessage, WizardPage.ERROR);
            return false;
        }

        IDynamicPlugin dynamicPlugin = (IDynamicPlugin) firstElement;
        existingConfigsComboViewer.getControl().setToolTipText(existingConfigsComboViewer.getCombo().getText());
        updateDistributionDescription(dynamicPlugin);

        getDynamicBuildConfigurationData().setDynamicPlugin(dynamicPlugin);
        boolean isBuildin = isBuildinDynamicConfiguration(dynamicPlugin);
        getDynamicBuildConfigurationData().setReadonly(isBuildin);

        deleteExistingConfigBtn.setEnabled(!isBuildin);

        return true;
    }

    @Override
    public boolean isComplete() {
        try {
            clearErrorStatus();
            if (!checkNewConfigNameValid()) {
                return false;
            }
            if (!checkImportConfigText()) {
                return false;
            }
            if (!checkSelectExistingConfig()) {
                return false;
            }
            return true;
        } finally {
            String description = descriptionText.getText();
            descriptionText.setToolTipText(description);
            dynamicConfiguration.setDescription(description);
        }
    }

    private void clearErrorStatus() {
        showMessage(null, WizardPage.NONE);

        configNameText.setBackground(null);
        configNameText.setToolTipText(""); //$NON-NLS-1$

        importConfigText.setBackground(null);
        importConfigText.setToolTipText(""); //$NON-NLS-1$
    }

    private boolean isBuildinDynamicConfiguration(IDynamicPlugin dynamicPlugin) {
        boolean isBuildin = true;

        if (allBuildinDynamicPlugins != null) {
            isBuildin = allBuildinDynamicPlugins.contains(dynamicPlugin);
        }

        return isBuildin;
    }

    @Override
    public boolean canFlipToNextPage() {
        if (!isComplete()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canFinish() {
        return false;
    }

    private DynamicConfiguration buildDynamicConfiguration(IDynamicPlugin plugin) {
        if (plugin == null) {
            return null;
        }
        IDynamicPluginConfiguration pluginConfiguration = plugin.getPluginConfiguration();
        DynamicConfiguration config = new DynamicConfiguration();
        config.setDescription(pluginConfiguration.getDescription());
        config.setDistribution(pluginConfiguration.getDistribution());
        config.setId(pluginConfiguration.getId());
        config.setName(pluginConfiguration.getName());
        config.setVersion(pluginConfiguration.getVersion());
        return config;
    }

    private class ExistingConfigsLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof IDynamicPlugin) {
                IDynamicPluginConfiguration pluginConfiguration = ((IDynamicPlugin) element).getPluginConfiguration();
                String name = pluginConfiguration.getName();
                return name;
            } else {
                return element == null ? "" : element.toString();//$NON-NLS-1$
            }
        }

    }

}
