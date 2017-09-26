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
package org.talend.repository.hadoopcluster.ui.dynamic;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.repository.hadoopcluster.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class DynamicSetupConfigurationForm extends AbstractDynamicDistributionForm {

    private Button newConfigBtn;

    private Button editExistingConfigBtn;

    private Button importConfigBtn;

    private Text configNameText;

    private ComboViewer existingConfigsComboViewer;

    private Text importConfigText;

    private Button importConfigBrowseBtn;

    private Text descriptionText;

    private Composite newConfigGroup;

    private Composite editExistingGroup;

    private Composite importConfigGroup;

    public DynamicSetupConfigurationForm(Composite parent, int style) {
        super(parent, style);
        this.setLayout(new FillLayout());
        createControl();
    }

    protected void createControl() {
        Composite parent = this;

        Composite container = new Composite(parent, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        formLayout.marginTop = 10;
        formLayout.marginBottom = 10;
        formLayout.marginLeft = 10;
        formLayout.marginRight = 10;
        container.setLayout(formLayout);

        int ALIGN_VERTICAL = 12;
        int ALIGN_GROUP_VERTICAL = 7;
        int ALIGN_HORIZON = 3;
        int HORZON_WIDTH = 100;

        newConfigBtn = new Button(container, SWT.RADIO);
        newConfigBtn.setText(Messages.getString("DynamicChoicePage.form.newConfigBtn")); //$NON-NLS-1$
        FormData formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        newConfigBtn.setLayoutData(formData);

        newConfigGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(newConfigBtn, ALIGN_GROUP_VERTICAL, SWT.BOTTOM);
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
        nameLabel.setText(Messages.getString("DynamicChoicePage.form.nameLabel")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(configNameText, 0, SWT.CENTER);
        formData.right = new FormAttachment(configNameText, -1 * ALIGN_HORIZON, SWT.LEFT);
        nameLabel.setLayoutData(formData);

        editExistingConfigBtn = new Button(container, SWT.RADIO);
        editExistingConfigBtn.setText(Messages.getString("DynamicChoicePage.form.editExistingConfigBtn")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(newConfigGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(newConfigGroup, 0, SWT.LEFT);
        formData.right = new FormAttachment(newConfigGroup, 0, SWT.RIGHT);
        editExistingConfigBtn.setLayoutData(formData);

        editExistingGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(editExistingConfigBtn, ALIGN_GROUP_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(editExistingConfigBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(editExistingConfigBtn, 0, SWT.RIGHT);
        editExistingGroup.setLayoutData(formData);
        editExistingGroup.setLayout(new FormLayout());

        existingConfigsComboViewer = new ComboViewer(editExistingGroup, SWT.READ_ONLY);
        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0, HORZON_WIDTH);
        formData.right = new FormAttachment(100);
        existingConfigsComboViewer.getCombo().setLayoutData(formData);

        importConfigBtn = new Button(container, SWT.RADIO);
        importConfigBtn.setText(Messages.getString("DynamicChoicePage.form.importConfigBtn")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(editExistingGroup, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(editExistingGroup, 0, SWT.LEFT);
        formData.right = new FormAttachment(editExistingGroup, 0, SWT.RIGHT);
        importConfigBtn.setLayoutData(formData);

        importConfigGroup = new Composite(container, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(importConfigBtn, ALIGN_GROUP_VERTICAL, SWT.BOTTOM);
        formData.left = new FormAttachment(importConfigBtn, 0, SWT.LEFT);
        formData.right = new FormAttachment(importConfigBtn, 0, SWT.RIGHT);
        importConfigGroup.setLayoutData(formData);
        importConfigGroup.setLayout(new FormLayout());

        importConfigBrowseBtn = new Button(importConfigGroup, SWT.PUSH);
        importConfigBrowseBtn.setText(Messages.getString("DynamicChoicePage.form.importConfig.browse")); //$NON-NLS-1$
        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(100, -1 * getNewButtonSize(importConfigBrowseBtn).x);
        formData.right = new FormAttachment(100);
        importConfigBrowseBtn.setLayoutData(formData);

        importConfigText = new Text(importConfigGroup, SWT.BORDER);
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

        importConfigBrowseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

            }

        });

    }

}
