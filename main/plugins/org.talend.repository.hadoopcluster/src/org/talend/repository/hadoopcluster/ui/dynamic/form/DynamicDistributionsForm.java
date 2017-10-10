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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.designer.maven.aether.IDynamicMonitor;
import org.talend.hadoop.distribution.dynamic.DynamicDistributionManager;
import org.talend.hadoop.distribution.dynamic.IDynamicDistributionsGroup;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicBuildConfigurationWizard;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicDistributionsForm extends AbstractDynamicDistributionForm {

    private ComboViewer distributionCombo;

    private ComboViewer versionCombo;

    private Button buildConfigBtn;

    private Map<String, IDynamicDistributionsGroup> dynDistriGroupMap = new HashMap<>();

    public DynamicDistributionsForm(Composite parent, int style, IDynamicMonitor monitor) {
        super(parent, style);
        createControl();
        loadData(monitor);
        addListeners();
    }

    private void createControl() {
        Composite parent = this;

        Composite container = createFormContainer(parent);
        int ALIGN_HORIZON = getAlignHorizon();
        int ALIGN_VERTICAL_INNER = getAlignVerticalInner();

        Group group = new Group(container, SWT.NONE);
        group.setText(Messages.getString("DynamicDistributionsForm.group.existing")); //$NON-NLS-1$
        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        group.setLayoutData(formData);
        FormLayout formLayout = new FormLayout();
        formLayout.marginTop = 5;
        formLayout.marginBottom = 5;
        formLayout.marginLeft = 5;
        formLayout.marginRight = 5;
        group.setLayout(formLayout);

        Label distributionLabel = new Label(group, SWT.NONE);
        distributionLabel.setText(Messages.getString("DynamicDistributionsForm.label.existing.distribution")); //$NON-NLS-1$

        distributionCombo = new ComboViewer(group, SWT.READ_ONLY);
        distributionCombo.setContentProvider(ArrayContentProvider.getInstance());
        distributionCombo.setLabelProvider(new LabelProvider());
        formData = new FormData();
        formData.top = new FormAttachment(0);
        int distriAlignHorizon = ALIGN_HORIZON;
        formData.left = new FormAttachment(distributionLabel, distriAlignHorizon, SWT.RIGHT);
        formData.right = new FormAttachment(distributionLabel, distriAlignHorizon + 180, SWT.RIGHT);
        distributionCombo.getControl().setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(distributionCombo.getControl(), 0, SWT.CENTER);
        formData.left = new FormAttachment(0);
        distributionLabel.setLayoutData(formData);

        Label versionLabel = new Label(group, SWT.NONE);
        versionLabel.setText(Messages.getString("DynamicDistributionsForm.label.existing.version")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(distributionCombo.getControl(), 0, SWT.CENTER);
        formData.left = new FormAttachment(distributionCombo.getControl(), ALIGN_HORIZON * 2, SWT.RIGHT);
        versionLabel.setLayoutData(formData);

        versionCombo = new ComboViewer(group, SWT.READ_ONLY);
        versionCombo.setContentProvider(ArrayContentProvider.getInstance());
        versionCombo.setLabelProvider(new LabelProvider());
        formData = new FormData();
        formData.top = new FormAttachment(versionLabel, 0, SWT.CENTER);
        formData.left = new FormAttachment(versionLabel, ALIGN_HORIZON, SWT.RIGHT);
        formData.right = new FormAttachment(100);
        versionCombo.getControl().setLayoutData(formData);

        buildConfigBtn = new Button(group, SWT.PUSH);
        buildConfigBtn.setText(Messages.getString("DynamicDistributionsForm.button.existing.buildConfig")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(versionCombo.getControl(), ALIGN_VERTICAL_INNER, SWT.BOTTOM);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(0, getNewButtonSize(buildConfigBtn).x);
        buildConfigBtn.setLayoutData(formData);

    }

    private void addListeners() {
        buildConfigBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) distributionCombo.getSelection();
                if (selection == null) {

                }
                Object distribution = selection.getFirstElement();
                if (distribution == null) {

                }
                IDynamicDistributionsGroup dynamicDistributionsGroup = dynDistriGroupMap.get(distribution);
                DynamicBuildConfigurationWizard wizard = new DynamicBuildConfigurationWizard(dynamicDistributionsGroup);
                WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        wizard);
                wizardDialog.create();
                if (wizardDialog.open() == IDialogConstants.OK_ID) {
                    IDynamicMonitor monitor = new IDynamicMonitor() {

                        @Override
                        public void writeMessage(String message) {
                            // nothing to do
                        }
                    };
                    refreshVersionList(monitor);
                }
            }

        });
    }

    private void loadData(IDynamicMonitor monitor) {
        try {
            dynDistriGroupMap.clear();
            DynamicDistributionManager dynDistriManager = DynamicDistributionManager.getInstance();
            List<IDynamicDistributionsGroup> dynDistriGroups = dynDistriManager.getDynamicDistributionsGroups();
            if (dynDistriGroups != null && !dynDistriGroups.isEmpty()) {
                for (IDynamicDistributionsGroup dynDistriGroup : dynDistriGroups) {
                    String displayName = dynDistriGroup.getDistributionDisplay();
                    dynDistriGroupMap.put(displayName, dynDistriGroup);
                }
                List<String> distributionDisplayNames = new LinkedList<>(dynDistriGroupMap.keySet());
                Collections.sort(distributionDisplayNames);
                distributionCombo.setInput(distributionDisplayNames);
                if (0 < distributionDisplayNames.size()) {
                    distributionCombo.setSelection(new StructuredSelection(distributionDisplayNames.get(0)));
                    refreshVersionList(monitor);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void refreshVersionList(IDynamicMonitor monitor) {
        try {
            IStructuredSelection selection = (IStructuredSelection) distributionCombo.getSelection();
            if (selection != null) {
                Object selectedObject = selection.getFirstElement();
                if (selectedObject != null) {
                    IDynamicDistributionsGroup dynDistriGroup = dynDistriGroupMap.get(selectedObject);
                    if (dynDistriGroup == null) {
                        throw new Exception(Messages.getString("DynamicDistributionsForm.exception.noDistributionGroupFound", //$NON-NLS-1$
                                dynDistriGroup));
                    }
                    List<IDynamicPlugin> dynamicPlugins = new LinkedList<>();
                    List<IDynamicPlugin> allBuildinDynamicPlugins = dynDistriGroup.getAllBuildinDynamicPlugins(monitor);
                    if (allBuildinDynamicPlugins != null && !allBuildinDynamicPlugins.isEmpty()) {
                        dynamicPlugins.addAll(allBuildinDynamicPlugins);
                    }
                    List<IDynamicPlugin> allUsersDynamicPlugins = DynamicDistributionManager.getInstance()
                            .getAllUsersDynamicPlugins(monitor);
                    if (allUsersDynamicPlugins != null && !allUsersDynamicPlugins.isEmpty()) {
                        List<IDynamicPlugin> tempDynamicPlugins = dynDistriGroup.filterDynamicPlugins(allUsersDynamicPlugins,
                                monitor);
                        if (tempDynamicPlugins != null && !tempDynamicPlugins.isEmpty()) {
                            dynamicPlugins.addAll(tempDynamicPlugins);
                        }
                    }
                    List<String> versions = new LinkedList<>();
                    Iterator<IDynamicPlugin> iter = dynamicPlugins.iterator();
                    while (iter.hasNext()) {
                        try {
                            IDynamicPlugin plugin = iter.next();
                            IDynamicPluginConfiguration pluginConfiguration = plugin.getPluginConfiguration();
                            String name = pluginConfiguration.getName();
                            versions.add(name);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                    Collections.reverse(versions);
                    versionCombo.setInput(versions);
                    if (0 < versions.size()) {
                        versionCombo.setSelection(new StructuredSelection(versions.get(0)));
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public List<String> checkErrors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

}
