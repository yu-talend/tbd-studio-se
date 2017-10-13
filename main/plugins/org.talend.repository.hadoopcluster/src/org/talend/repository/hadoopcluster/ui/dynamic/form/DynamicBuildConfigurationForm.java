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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationData.ActionType;

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

    public DynamicBuildConfigurationForm(Composite parent, int style, DynamicBuildConfigurationData configData,
            IDynamicMonitor monitor) {
        super(parent, style, configData);
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

        baseJarsTable = new TableViewer(container, SWT.BORDER);
        baseJarsTable.setContentProvider(ArrayContentProvider.getInstance());
        baseJarsTable.setLabelProvider(new LabelProvider());
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
        List<String> versionList = new ArrayList<>();
        ActionType actionType = dynConfigData.getActionType();
        if (actionType == ActionType.Import || actionType == ActionType.EditExisting) {
            IDynamicPlugin dynamicPlugin = dynConfigData.getDynamicPlugin();
            IDynamicPluginConfiguration pluginConfiguration = dynamicPlugin.getPluginConfiguration();
            String version = pluginConfiguration.getVersion();
            versionList.add(version);
        }

    }

    private void addListeners() {
        fetchVersionBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onFetchVersionBtnSelected();
                updateButtons();
            }

        });
    }

    private void onFetchVersionBtnSelected() {
        try {
            List<String> versionList = getVersionList();
            if (versionList != null && !versionList.isEmpty()) {
                Collections.sort(versionList, Collections.reverseOrder());
                hadoopVersionCombo.setInput(versionList);
                hadoopVersionCombo.setSelection(new StructuredSelection(versionList.get(0)));
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
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

    private void enableVersionCombo(boolean enable) {
        hadoopVersionCombo.getControl().setEnabled(enable);
    }

    private void enableJarsTable(boolean enable) {
        baseJarsTable.getControl().setEnabled(enable);
    }

    private void enableRetrieveBaseJarBtn(boolean enable) {
        retrieveBaseJarsBtn.setEnabled(enable);
        if (!enable) {
            baseJarsTable.getControl().setEnabled(enable);
        }
    }

    @Override
    public boolean isComplete() {
        showMessage(null, WizardPage.INFORMATION);
        if (!checkVersionList()) {
            return false;
        }
        return true;
    }

    private boolean checkVersionList() {
        DynamicBuildConfigurationData dynConfigData = getDynamicBuildConfigurationData();
        ActionType actionType = dynConfigData.getActionType();
        enableVersionCombo(false);
        enableRetrieveBaseJarBtn(false);
        if (actionType == ActionType.NewConfig) {
            String selectedVersion = null;
            IStructuredSelection selection = (IStructuredSelection) hadoopVersionCombo.getSelection();
            if (selection != null) {
                selectedVersion = (String) selection.getFirstElement();
            }
            if (StringUtils.isEmpty(selectedVersion)) {
                String errorMessage = Messages.getString("DynamicBuildConfigurationForm.check.versionList.empty", //$NON-NLS-1$
                        Messages.getString("DynamicBuildConfigurationForm.fetchBtn")); //$NON-NLS-1$
                showMessage(errorMessage, WizardPage.ERROR);
                return false;
            }
        }
        enableVersionCombo(true);
        enableRetrieveBaseJarBtn(true);
        return true;
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

}
