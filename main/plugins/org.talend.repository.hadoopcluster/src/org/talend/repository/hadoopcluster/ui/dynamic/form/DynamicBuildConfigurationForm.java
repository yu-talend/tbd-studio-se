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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.repository.hadoopcluster.i18n.Messages;

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

    public DynamicBuildConfigurationForm(Composite parent, int style) {
        super(parent, style);
        createControl();
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

}
