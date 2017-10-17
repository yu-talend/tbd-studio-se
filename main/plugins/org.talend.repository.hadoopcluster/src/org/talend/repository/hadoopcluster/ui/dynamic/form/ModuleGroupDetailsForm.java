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

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.hadoop.distribution.HadoopDistributionPlugin;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleGroupAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicModuleGroupData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ModuleGroupDetailsForm extends AbstractModuleGroupDetailsForm {

    private TableViewer groupDetailsViewer;

    public ModuleGroupDetailsForm(Composite parent, int style, DynamicModuleGroupData moduleGroupData) {
        super(parent, style, moduleGroupData);
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

        groupDetailsViewer = new TableViewer(container, SWT.BORDER);
        TableViewerColumn groupColumn = new TableViewerColumn(groupDetailsViewer, SWT.LEFT);
        groupColumn.getColumn().setText(Messages.getString("ModuleGroupDetailsForm.groupDetails.column")); //$NON-NLS-1$
        groupColumn.getColumn().setWidth(500);
        groupColumn.setLabelProvider(new GroupDetailsColumnLabelProvider());
        Table groupDetailsTable = groupDetailsViewer.getTable();
        groupDetailsTable.setHeaderVisible(true);
        groupDetailsTable.setLinesVisible(true);
        groupDetailsViewer.setContentProvider(ArrayContentProvider.getInstance());

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.top = new FormAttachment(0);
        formData.bottom = new FormAttachment(100);
        groupDetailsTable.setLayoutData(formData);

    }

    protected void initData() {
        try {
            DynamicModuleGroupData moduleGroupData = getModuleGroupData();
            DynamicPluginAdapter pluginAdapter = moduleGroupData.getPluginAdapter();
            IDynamicConfiguration moduleGroup = pluginAdapter.getModuleGroupByTemplateId(moduleGroupData.getGroupTemplateId());
            List<IDynamicConfiguration> childConfigurations = moduleGroup.getChildConfigurations();
            groupDetailsViewer.setInput(childConfigurations);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    protected void addListeners() {

    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean canFinish() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canFlipToNextPage() {
        // TODO Auto-generated method stub
        return false;
    }

    protected class GroupDetailsColumnLabelProvider extends ColumnLabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof IDynamicConfiguration) {
                try {
                    String id = (String) ((IDynamicConfiguration) element)
                            .getAttribute(DynamicModuleGroupAdapter.ATTR_LIBRARY_ID);
                    DynamicModuleGroupData moduleGroupData = getModuleGroupData();
                    DynamicPluginAdapter pluginAdapter = moduleGroupData.getPluginAdapter();
                    IDynamicConfiguration moduleById = pluginAdapter.getModuleById(id);
                    String mvnUri = null;
                    if (moduleById == null) {
                        // should be an existing module in studio
                        ModuleNeeded moduleNeeded = HadoopDistributionPlugin.getInstance().getExistingModuleMap().get(id);
                        mvnUri = moduleNeeded.getMavenUri();
                    } else {
                        mvnUri = (String) moduleById.getAttribute(DynamicModuleAdapter.ATTR_MVN_URI);
                    }
                    return mvnUri;
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            return element == null ? "" : element.toString();//$NON-NLS-1$
        }

    }

}
