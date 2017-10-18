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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.dynamic.DynamicFactory;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.core.runtime.dynamic.IDynamicExtension;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.designer.maven.aether.comparator.VersionStringComparator;
import org.talend.hadoop.distribution.dynamic.DynamicConfiguration;
import org.talend.hadoop.distribution.dynamic.DynamicDistributionManager;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicDistriConfigAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicLibraryNeededExtensionAdaper;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleGroupAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;
import org.talend.hadoop.distribution.dynamic.comparator.DynamicAttributeComparator;
import org.talend.hadoop.distribution.dynamic.util.DynamicDistributionUtils;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData.ActionType;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicModuleGroupData;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicModuleGroupWizard;
import org.talend.repository.ui.login.LoginDialogV2;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicBuildConfigurationForm extends AbstractDynamicDistributionForm {

    private Button fetchVersionBtn;

    private Button retrieveBaseJarsBtn;

    private Button showOnlyCompatibleVersionBtn;

    private Button exportConfigBtn;

    private ComboViewer hadoopVersionCombo;

    private TableViewer baseJarsTable;

    private Composite fetchGroup;

    private Map<String, IDynamicPlugin> dynamicPluginMap;

    private IDynamicPlugin curDynamicPlugin;

    private ActionType curActionType;

    public DynamicBuildConfigurationForm(Composite parent, int style, DynamicBuildConfigurationData configData,
            IDynamicMonitor monitor) {
        super(parent, style, configData);
        dynamicPluginMap = new HashMap<>();
        createControl();
        initData();
        addListeners();
    }

    protected void createControl() {

        Composite parent = this;

        Composite container = createFormContainer(parent);

        int ALIGN_VERTICAL = getAlignVertical();
        int ALIGN_VERTICAL_INNER = getAlignVerticalInner();
        int ALIGN_HORIZON = getAlignHorizon();

        fetchVersionBtn = new Button(container, SWT.PUSH);
        fetchVersionBtn.setText(Messages.getString("DynamicBuildConfigurationForm.fetchBtn")); //$NON-NLS-1$

        retrieveBaseJarsBtn = new Button(container, SWT.PUSH);
        retrieveBaseJarsBtn.setText(Messages.getString("DynamicBuildConfigurationForm.retrieveBtn")); //$NON-NLS-1$

        Point fetchVersionBtnSize = getNewButtonSize(fetchVersionBtn, 12);
        Point retrieveBaseJarsBtnSize = getNewButtonSize(retrieveBaseJarsBtn, 12);
        int buttonSize = retrieveBaseJarsBtnSize.x;
        if (buttonSize < fetchVersionBtnSize.x) {
            buttonSize = fetchVersionBtnSize.x;
        }

        FormData formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(0, buttonSize);
        fetchVersionBtn.setLayoutData(formData);

        fetchGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(fetchVersionBtn, 0, SWT.TOP);
        formData.left = new FormAttachment(fetchVersionBtn, ALIGN_HORIZON, SWT.RIGHT);
        formData.right = new FormAttachment(100);
        fetchGroup.setLayoutData(formData);
        fetchGroup.setLayout(new FormLayout());

        Label hadoopLabel = new Label(fetchGroup, SWT.NONE);
        hadoopVersionCombo = new ComboViewer(fetchGroup, SWT.READ_ONLY);
        hadoopVersionCombo.setContentProvider(ArrayContentProvider.getInstance());
        hadoopVersionCombo.setLabelProvider(new LabelProvider());

        hadoopLabel.setText(Messages.getString("DynamicBuildConfigurationForm.label.hadoop")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(hadoopVersionCombo.getCombo(), 0, SWT.CENTER);
        formData.left = new FormAttachment(0);
        hadoopLabel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(hadoopLabel, ALIGN_HORIZON, SWT.RIGHT);
        formData.right = new FormAttachment(100);
        hadoopVersionCombo.getCombo().setLayoutData(formData);

        showOnlyCompatibleVersionBtn = new Button(fetchGroup, SWT.CHECK);
        showOnlyCompatibleVersionBtn.setText(Messages.getString("DynamicBuildConfigurationForm.showOnlyCompatibleVersionBtn")); //$NON-NLS-1$
        showOnlyCompatibleVersionBtn.setSelection(true);
        formData = new FormData();
        formData.top = new FormAttachment(hadoopVersionCombo.getCombo(), ALIGN_VERTICAL_INNER, SWT.BOTTOM);
        formData.right = new FormAttachment(hadoopVersionCombo.getCombo(), 0, SWT.RIGHT);
        showOnlyCompatibleVersionBtn.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(fetchGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(fetchVersionBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(fetchVersionBtn, 0, SWT.RIGHT);
        retrieveBaseJarsBtn.setLayoutData(formData);

        baseJarsTable = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        TableViewerColumn indexColumn = new TableViewerColumn(baseJarsTable, SWT.RIGHT);
        indexColumn.getColumn().setText(Messages.getString("DynamicBuildConfigurationForm.baseJars.table.index")); //$NON-NLS-1$
        indexColumn.getColumn().setWidth(50);
        indexColumn.setLabelProvider(new RowNumberLabelProvider());
        TableViewerColumn groupNameColumn = new TableViewerColumn(baseJarsTable, SWT.LEFT);
        groupNameColumn.getColumn().setText(Messages.getString("DynamicBuildConfigurationForm.baseJars.table.groupName")); //$NON-NLS-1$
        groupNameColumn.getColumn().setWidth(200);
        groupNameColumn.setLabelProvider(new BaseJarTableGroupLabelProvider());
        TableViewerColumn groupDetailsColumn = new TableViewerColumn(baseJarsTable, SWT.LEFT);
        groupDetailsColumn.getColumn().setText(Messages.getString("DynamicBuildConfigurationForm.baseJars.table.groupDetails")); //$NON-NLS-1$
        groupDetailsColumn.getColumn().setWidth(300);
        BaseJarTableDetailLabelProvider detailLabelProvider = new BaseJarTableDetailLabelProvider();
        groupDetailsColumn.setLabelProvider(detailLabelProvider);
        Table table = baseJarsTable.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        baseJarsTable.setContentProvider(new BaseJarTableContentProvider(detailLabelProvider));
        // baseJarsTable.setLabelProvider(new BaseJarTableLabelProvider());

        exportConfigBtn = new Button(container, SWT.PUSH);

        formData = new FormData();
        formData.top = new FormAttachment(retrieveBaseJarsBtn, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(exportConfigBtn, -1 * ALIGN_VERTICAL, SWT.TOP);
        baseJarsTable.getTable().setLayoutData(formData);

        exportConfigBtn.setText(Messages.getString("DynamicBuildConfigurationForm.exportConfigBtn")); //$NON-NLS-1$
        formData = new FormData();
        // formData.top = new FormAttachment(baseJarsTable.getTable(), ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(baseJarsTable.getTable(), 0, SWT.LEFT);
        formData.right = new FormAttachment(baseJarsTable.getTable(), getNewButtonSize(exportConfigBtn, 10).x, SWT.LEFT);
        formData.bottom = new FormAttachment(100);
        exportConfigBtn.setLayoutData(formData);

    }

    private void initData() {

        DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
        ActionType actionType = dynConfigData.getActionType();

        boolean actionChanged = !actionType.equals(curActionType);
        if (actionChanged) {
            curDynamicPlugin = null;
            dynamicPluginMap.clear();
            if (ActionType.Import.equals(actionType) || ActionType.EditExisting.equals(actionType)) {
                List<String> versionList = new ArrayList<>();
                IDynamicPlugin dynamicPlugin = dynConfigData.getDynamicPlugin();
                curDynamicPlugin = dynamicPlugin;
                IDynamicPluginConfiguration pluginConfiguration = dynamicPlugin.getPluginConfiguration();
                String version = pluginConfiguration.getVersion();
                versionList.add(version);
                dynamicPluginMap.put(version, dynamicPlugin);
                hadoopVersionCombo.setInput(versionList);
                hadoopVersionCombo.setSelection(new StructuredSelection(version));
            } else if (ActionType.NewConfig.equals(actionType)) {
                // nothing to do
            }
        } else {
            boolean dataChanged = false;
            String selectedVersion = getSelectedVersion();
            if (ActionType.Import.equals(actionType) || ActionType.EditExisting.equals(actionType)) {
                IDynamicPlugin dynamicPlugin = dynConfigData.getDynamicPlugin();

                IDynamicPluginConfiguration pluginConfiguration = dynamicPlugin.getPluginConfiguration();
                String version = pluginConfiguration.getVersion();

                if (dynamicPlugin != curDynamicPlugin) {
                    dataChanged = true;
                    curDynamicPlugin = dynamicPlugin;
                    selectedVersion = version;
                    List<String> versionList = new ArrayList<>();
                    versionList.add(version);
                    hadoopVersionCombo.setInput(versionList);
                    hadoopVersionCombo.setSelection(new StructuredSelection(version));
                }
            } else if (ActionType.NewConfig.equals(actionType)) {
                if (curDynamicPlugin != null) {
                    DynamicConfiguration newDistrConfigration = dynConfigData.getNewDistrConfigration();
                    String name = newDistrConfigration.getName();
                    String desc = newDistrConfigration.getDescription();
                    IDynamicPluginConfiguration pluginConfiguration = curDynamicPlugin.getPluginConfiguration();
                    pluginConfiguration.setName(name);
                    pluginConfiguration.setDescription(desc);
                }
            }

            if (dataChanged) {
                dynamicPluginMap.clear();
                dynamicPluginMap.put(selectedVersion, curDynamicPlugin);
            }
        }
        curActionType = actionType;
    }

    private void addListeners() {
        fetchVersionBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onFetchVersionBtnSelected();
                updateButtons();
            }

        });

        showOnlyCompatibleVersionBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onFetchVersionBtnSelected();
                updateButtons();
            }

        });

        retrieveBaseJarsBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onRetrieveBaseJarsSelected();
                updateButtons();
            }

        });

        exportConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onExportConfigurationSelected();
                // updateButtons();
            }

        });

        hadoopVersionCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateButtons();
            }

        });

    }

    private void onFetchVersionBtnSelected() {
        try {
            String selectedVersion = getSelectedVersion();
            List<String> versionList = getVersionList();
            if (versionList != null && !versionList.isEmpty()) {
                Collections.sort(versionList, Collections.reverseOrder(new VersionStringComparator()));
                hadoopVersionCombo.setInput(versionList);
                if (StringUtils.isEmpty(selectedVersion)) {
                    selectedVersion = versionList.get(0);
                }
                hadoopVersionCombo.setSelection(new StructuredSelection(selectedVersion));
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    private void onRetrieveBaseJarsSelected() {
        DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
        IDynamicDistributionsGroup dynDistrGroup = dynConfigData.getDynamicDistributionsGroup();
        try {
            DynamicConfiguration newDistrConfigration = null;
            String version = getSelectedVersion();
            ActionType actionType = dynConfigData.getActionType();
            if (ActionType.NewConfig.equals(actionType)) {
                newDistrConfigration = dynConfigData.getNewDistrConfigration();
            } else if (ActionType.Import.equals(actionType) || ActionType.EditExisting.equals(actionType)) {
                newDistrConfigration = new DynamicConfiguration();
                IDynamicPlugin dynPlugin = dynConfigData.getDynamicPlugin();
                IDynamicPluginConfiguration pluginConfiguration = dynPlugin.getPluginConfiguration();
                String name = pluginConfiguration.getName();
                String distribution = pluginConfiguration.getDistribution();
                String description = pluginConfiguration.getDescription();
                String id = pluginConfiguration.getId();

                newDistrConfigration.setDescription(description);
                newDistrConfigration.setDistribution(distribution);
                newDistrConfigration.setId(id);
                newDistrConfigration.setName(name);
            }

            newDistrConfigration.setVersion(version);
            dynamicPluginMap.remove(version);
            IDynamicMonitor monitor = new IDynamicMonitor() {

                @Override
                public void writeMessage(String message) {
                    System.out.println(message);
                }
            };
            IDynamicPlugin newDynamicPlugin = dynDistrGroup.buildDynamicPlugin(monitor, newDistrConfigration);
            if (newDynamicPlugin != null) {
                if (ActionType.Import.equals(actionType) || ActionType.EditExisting.equals(actionType)) {
                    IDynamicPlugin dynPlugin = dynConfigData.getDynamicPlugin();

                    IDynamicPluginConfiguration newConfig = newDynamicPlugin.getPluginConfiguration();
                    IDynamicPluginConfiguration curConfig = dynPlugin.getPluginConfiguration();

                    newConfig.setAttribute(DynamicDistriConfigAdapter.ATTR_FILE_PATH,
                            curConfig.getAttribute(DynamicDistriConfigAdapter.ATTR_FILE_PATH));
                }
                curDynamicPlugin = newDynamicPlugin;
                dynamicPluginMap.put(version, curDynamicPlugin);
                dynConfigData.setDynamicPlugin(curDynamicPlugin);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void onExportConfigurationSelected() {
        DirectoryDialog dirDialog = new DirectoryDialog(getShell());
        String folderPath = dirDialog.open();
        if (StringUtils.isNotEmpty(folderPath)) {
            try {
                IDynamicPluginConfiguration pluginConfiguration = curDynamicPlugin.getPluginConfiguration();
                String id = pluginConfiguration.getId();
                String fileName = id + "." + DynamicDistributionManager.DISTRIBUTION_FILE_EXTENSION; //$NON-NLS-1$
                String filePath = folderPath + "/" + fileName; //$NON-NLS-1$

                File file = new File(filePath);
                if (file.exists()) {
                    boolean agree = MessageDialog.openQuestion(getShell(),
                            Messages.getString("DynamicBuildConfigurationForm.exportConfig.dialog.fileExist.title"), //$NON-NLS-1$
                            Messages.getString("DynamicBuildConfigurationForm.exportConfig.dialog.fileExist.message", //$NON-NLS-1$
                                    file.getCanonicalPath()));
                    if (!agree) {
                        return;
                    }
                }

                IDynamicMonitor monitor = new IDynamicMonitor() {

                    @Override
                    public void writeMessage(String message) {
                        // TODO Auto-generated method stub

                    }
                };
                DynamicDistributionManager.getInstance().saveUsersDynamicPlugin(curDynamicPlugin, filePath, monitor);
                MessageDialog.openInformation(getShell(),
                        Messages.getString("DynamicBuildConfigurationForm.exportConfig.dialog.title"), //$NON-NLS-1$
                        Messages.getString("DynamicBuildConfigurationForm.exportConfig.dialog.message", //$NON-NLS-1$
                                new File(filePath).getCanonicalPath()));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private List<String> getVersionList() throws Exception {
        List<String> versionList = null;
        try {
            DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
            IDynamicDistributionsGroup dynDistrGroup = dynConfigData.getDynamicDistributionsGroup();
            IDynamicMonitor monitor = new IDynamicMonitor() {

                @Override
                public void writeMessage(String message) {
                    // TODO Auto-generated method stub

                }
            };

            if (showOnlyCompatibleVersionBtn.getSelection()) {
                versionList = dynDistrGroup.getCompatibleVersions(monitor);
            } else {
                versionList = dynDistrGroup.getAllVersions(monitor);
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
        return versionList;
    }

    private String getSelectedVersion() {
        String selectedVersion = null;
        IStructuredSelection selection = (IStructuredSelection) hadoopVersionCombo.getSelection();
        if (selection != null) {
            selectedVersion = (String) selection.getFirstElement();
        }
        return selectedVersion;
    }

    private void enableVersionCombo(boolean enable) {
        hadoopVersionCombo.getControl().setEnabled(enable);
    }

    private void enableJarsTable(boolean enable) {
        baseJarsTable.getControl().setEnabled(enable);
    }

    private void enableRetrieveBaseJarBtn(boolean enable) {
        retrieveBaseJarsBtn.setEnabled(enable);
        if (!enable) {
            enableJarsTable(enable);
        }
    }

    @Override
    public boolean isComplete() {
        try {
            initData();
            showMessage(null, WizardPage.INFORMATION);
            if (!checkVersionList()) {
                return false;
            }
            if (!checkBaseJars()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        } finally {
            if (curDynamicPlugin != null) {
                exportConfigBtn.setEnabled(true);
            } else {
                exportConfigBtn.setEnabled(false);
            }
        }
    }

    private boolean checkVersionList() {
        DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
        ActionType actionType = dynConfigData.getActionType();
        enableVersionCombo(false);
        enableRetrieveBaseJarBtn(false);
        String selectedVersion = getSelectedVersion();
        if (StringUtils.isEmpty(selectedVersion)) {
            String errorMessage = Messages.getString("DynamicBuildConfigurationForm.check.versionList.empty", //$NON-NLS-1$
                    fetchVersionBtn.getText());
            fetchVersionBtn.getShell().setDefaultButton(fetchVersionBtn);
            showMessage(errorMessage, WizardPage.ERROR);
            return false;
        }
        if (ActionType.NewConfig.equals(actionType)) {
            DynamicConfiguration newDistrConfigration = dynConfigData.getNewDistrConfigration();
            newDistrConfigration.setVersion(selectedVersion);
            newDistrConfigration.setId(generateId(newDistrConfigration.getDistribution(), selectedVersion));
            hadoopVersionCombo.getControl().setToolTipText(selectedVersion);
        }
        curDynamicPlugin = dynamicPluginMap.get(selectedVersion);
        enableVersionCombo(true);
        enableRetrieveBaseJarBtn(true);
        return true;
    }

    private boolean checkBaseJars() {
        if (!retrieveBaseJarsBtn.isEnabled()) {
            return true;
        }

        IDynamicPlugin generatedDynamicPlugin = null;
        String version = getSelectedVersion();
        if (StringUtils.isNotEmpty(version)) {
            generatedDynamicPlugin = dynamicPluginMap.get(version);
        }
        if (generatedDynamicPlugin == null) {
            initTableViewData(null);
            String errorMessage = Messages.getString("DynamicBuildConfigurationForm.check.baseJars.empty", //$NON-NLS-1$
                    retrieveBaseJarsBtn.getText());
            retrieveBaseJarsBtn.getShell().setDefaultButton(retrieveBaseJarsBtn);
            showMessage(errorMessage, WizardPage.ERROR);
            return false;
        }

        getDynamicBuildConfigurationData().setDynamicPlugin(generatedDynamicPlugin);

        initTableViewData(generatedDynamicPlugin);
        enableJarsTable(true);

        return true;
    }

    private void initTableViewData(IDynamicPlugin dynamicPlugin) {
        if (dynamicPlugin == null) {
            baseJarsTable.setInput(null);
        } else {
            List<IDynamicExtension> allExtensions = dynamicPlugin.getAllExtensions();
            IDynamicExtension libNeededExtension = null;
            for (IDynamicExtension extension : allExtensions) {
                if (DynamicLibraryNeededExtensionAdaper.ATTR_POINT.equals(extension.getExtensionPoint())) {
                    libNeededExtension = extension;
                    break;
                }
            }
            List<IDynamicConfiguration> configurations = libNeededExtension.getConfigurations();
            Iterator<IDynamicConfiguration> iter = configurations.iterator();
            List<IDynamicConfiguration> moduleGroups = new ArrayList<>();
            while (iter.hasNext()) {
                IDynamicConfiguration dynConfig = iter.next();
                if (DynamicModuleGroupAdapter.TAG_NAME.equals(dynConfig.getTagName())) {
                    moduleGroups.add(dynConfig);
                }
            }
            Collections.sort(moduleGroups, new DynamicAttributeComparator());
            baseJarsTable.setInput(moduleGroups);
        }
    }

    private String generateId(String distribution, String version) {
        String versionStr = DynamicDistributionUtils.formatId(version);
        String timestamp = DynamicDistributionUtils.generateTimestampId();
        // String id = distribution.toUpperCase() + "_" + versionStr.toUpperCase() + "_" + timestamp; //$NON-NLS-1$
        // //$NON-NLS-2$
        String id = DynamicDistributionUtils.formatId(distribution.toUpperCase() + "_" + timestamp); //$NON-NLS-1$
        return id;
    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

    @Override
    public boolean canFinish() {
        if (isComplete()) {
            return true;
        }
        return false;
    }

    protected class BaseJarTableContentProvider extends ArrayContentProvider {

        private BaseJarTableDetailLabelProvider detailLabelProvider;

        public BaseJarTableContentProvider(BaseJarTableDetailLabelProvider detailLabelProvider) {
            this.detailLabelProvider = detailLabelProvider;
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.detailLabelProvider.clearBtns();
            this.detailLabelProvider.setDynamicPlugin(curDynamicPlugin);
        }

    }

    protected static class BaseJarTableGroupLabelProvider extends ColumnLabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof IDynamicConfiguration) {
                return (String) ((IDynamicConfiguration) element).getAttribute(DynamicModuleGroupAdapter.ATTR_GROUP_TEMPLATE_ID);
            }
            return element == null ? "" : element.toString();//$NON-NLS-1$
        }

    }

    protected static class BaseJarTableDetailLabelProvider extends ColumnLabelProvider {

        private Map<Object, Composite> compositeMap = new HashMap<Object, Composite>();

        private Map<String, String> mavenUriMap = new HashMap<>();

        private IDynamicPlugin dynamicPlugin;

        private IDynamicPlugin tempDynamicPlugin;

        private DynamicPluginAdapter pluginAdapter;

        @Override
        public String getText(Object element) {
            if (element instanceof IDynamicConfiguration) {
                return getModuleGroupRuntimeId(element);
            }
            return element == null ? "" : element.toString();//$NON-NLS-1$
        }

        @Override
        public void update(ViewerCell cell) {

            TableItem item = (TableItem) cell.getItem();
            Composite composite = null;
            Object element = cell.getElement();
            if (compositeMap.containsKey(cell.getElement())) {
                composite = compositeMap.get(cell.getElement());
            } else {
                composite = new Composite((Composite) cell.getViewerRow().getControl(), SWT.NONE);
                composite.setLayout(new FormLayout());
                // composite.setBackground(getBackground(element));
                composite.setBackground(LoginDialogV2.WHITE_COLOR);
                composite.setForeground(getForeground(element));

                String text = getText(element);
                CLabel label = new CLabel(composite, SWT.NONE);
                label.setBackground(LoginDialogV2.WHITE_COLOR);
                label.setText(text);
                Button button = new Button(composite, SWT.PUSH);
                button.setText(Messages.getString("DynamicBuildConfigurationForm.baseJars.table.groupDetails.btn")); //$NON-NLS-1$
                addDetailBtnListener(button, getModuleGroupTemplateId(element));

                FormData formData = new FormData();
                formData.top = new FormAttachment(0);
                formData.bottom = new FormAttachment(100);
                formData.left = new FormAttachment(0);
                formData.right = new FormAttachment(button, -10, SWT.LEFT);
                label.setLayoutData(formData);

                formData = new FormData();
                formData.top = new FormAttachment(0);
                formData.bottom = new FormAttachment(100);
                formData.right = new FormAttachment(100);
                button.setLayoutData(formData);

                compositeMap.put(cell.getElement(), composite);

            }

            // cell.setBackground(getBackground(element));
            cell.setBackground(LoginDialogV2.WHITE_COLOR);
            cell.setForeground(getForeground(element));
            cell.setFont(getFont(element));

            TableEditor editor = new TableEditor(item.getParent());
            editor.grabHorizontal = true;
            editor.grabVertical = true;
            editor.setEditor(composite, item, cell.getColumnIndex());
            editor.layout();
        }

        private String getModuleGroupTemplateId(Object element) {
            return (String) ((IDynamicConfiguration) element).getAttribute(DynamicModuleGroupAdapter.ATTR_GROUP_TEMPLATE_ID);
        }

        private String getModuleGroupRuntimeId(Object element) {
            return (String) ((IDynamicConfiguration) element).getAttribute(DynamicModuleGroupAdapter.ATTR_ID);
        }

        @Override
        public void dispose() {
            super.dispose();
            clearBtns();
        }

        public void clearBtns() {
            Collection<Composite> values = compositeMap.values();
            if (values != null) {
                for (Composite composite : values) {
                    composite.dispose();
                }
            }
            compositeMap.clear();
            mavenUriMap.clear();
        }

        public void setDynamicPlugin(IDynamicPlugin dynPlugin) {

            if (dynPlugin == null) {
                mavenUriMap.clear();
                dynamicPlugin = null;
                tempDynamicPlugin = null;
                pluginAdapter = null;
                return;
            }

            try {
                this.dynamicPlugin = dynPlugin;
                this.tempDynamicPlugin = DynamicFactory.getInstance()
                        .createPluginFromJson(this.dynamicPlugin.toXmlJson().toString());

                pluginAdapter = new DynamicPluginAdapter(tempDynamicPlugin);
                pluginAdapter.buildIdMaps();
                Set<String> allModuleIds = pluginAdapter.getAllModuleIds();
                Iterator<String> iter = allModuleIds.iterator();
                while (iter.hasNext()) {
                    String id = iter.next();
                    IDynamicConfiguration module = pluginAdapter.getModuleById(id);
                    String mavenUri = (String) module.getAttribute(DynamicModuleAdapter.ATTR_MVN_URI);
                    mavenUriMap.put(mavenUri, id);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        private void addDetailBtnListener(Button btn, final String groupTemplateId) {
            btn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    DynamicModuleGroupData groupData = new DynamicModuleGroupData();
                    groupData.setDynamicPlugin(tempDynamicPlugin);
                    groupData.setGroupTemplateId(groupTemplateId);
                    groupData.setPluginAdapter(pluginAdapter);
                    groupData.setMavenUriIdMap(new HashMap<>(mavenUriMap));
                    DynamicModuleGroupWizard wizard = new DynamicModuleGroupWizard(groupData);
                    WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            wizard);
                    wizardDialog.create();
                    int result = wizardDialog.open();
                    if (result == IDialogConstants.OK_ID) {
                        IDynamicExtension oldLibNeeded = DynamicPluginAdapter.getLibraryNeededExtension(dynamicPlugin);
                        int index = dynamicPlugin.getChildIndex(oldLibNeeded);
                        if (index < 0) {
                            index = 1;
                        }
                        dynamicPlugin.removeExtensions(DynamicLibraryNeededExtensionAdaper.ATTR_POINT);
                        dynamicPlugin.addExtension(index, DynamicPluginAdapter.getLibraryNeededExtension(tempDynamicPlugin));
                    }
                    setDynamicPlugin(dynamicPlugin);
                }

            });
        }

    }

}
