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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.dynamic.IDynamicConfiguration;
import org.talend.core.runtime.dynamic.IDynamicExtension;
import org.talend.core.runtime.dynamic.IDynamicPlugin;
import org.talend.core.runtime.dynamic.IDynamicPluginConfiguration;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicModuleGroupAdapter;
import org.talend.hadoop.distribution.dynamic.adapter.DynamicPluginAdapter;
import org.talend.hadoop.distribution.dynamic.bean.ModuleBean;
import org.talend.hadoop.distribution.dynamic.util.DynamicDistributionUtils;
import org.talend.repository.hadoopcluster.HadoopClusterPlugin;
import org.talend.repository.hadoopcluster.i18n.Messages;
import org.talend.repository.hadoopcluster.ui.dynamic.DynamicModuleGroupData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ModuleGroupDetailsForm extends AbstractModuleGroupDetailsForm {

    private Label groupTemplateIdLabel;

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

        groupTemplateIdLabel = new Label(container, SWT.NONE);
        FormData formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        groupTemplateIdLabel.setLayoutData(formData);

        groupDetailsViewer = new TableViewer(container, SWT.BORDER);
        TableViewerColumn groupColumn = new TableViewerColumn(groupDetailsViewer, SWT.LEFT);
        groupColumn.getColumn().setText(Messages.getString("ModuleGroupDetailsForm.groupDetails.column")); //$NON-NLS-1$
        groupColumn.getColumn().setWidth(500);
        groupColumn.setLabelProvider(new GroupDetailsColumnLabelProvider());
        groupColumn.setEditingSupport(new MavenUriEditingSupport(groupDetailsViewer));
        Table groupDetailsTable = groupDetailsViewer.getTable();
        groupDetailsTable.setHeaderVisible(true);
        groupDetailsTable.setLinesVisible(true);
        groupDetailsViewer.setContentProvider(ArrayContentProvider.getInstance());

        formData = new FormData();
        formData.left = new FormAttachment(groupTemplateIdLabel, 0, SWT.LEFT);
        formData.right = new FormAttachment(groupTemplateIdLabel, 0, SWT.RIGHT);
        formData.top = new FormAttachment(groupTemplateIdLabel, ALIGN_VERTICAL, SWT.BOTTOM);
        formData.bottom = new FormAttachment(100);
        groupDetailsTable.setLayoutData(formData);

    }

    protected void initData() {
        try {
            DynamicModuleGroupData moduleGroupData = getModuleGroupData();
            String groupTemplateId = moduleGroupData.getGroupTemplateId();

            groupTemplateIdLabel
                    .setText(Messages.getString("ModuleGroupDetailsForm.groupDetails.label.groupTemplateId", groupTemplateId)); //$NON-NLS-1$

            DynamicPluginAdapter pluginAdapter = moduleGroupData.getPluginAdapter();
            IDynamicConfiguration moduleGroup = pluginAdapter.getModuleGroupByTemplateId(groupTemplateId);
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
        return true;
    }

    @Override
    public boolean canFinish() {
        return true;
    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

    protected String getMavenUri(Object element) {
        if (element instanceof IDynamicConfiguration) {
            try {
                String id = (String) ((IDynamicConfiguration) element).getAttribute(DynamicModuleGroupAdapter.ATTR_LIBRARY_ID);
                DynamicModuleGroupData moduleGroupData = getModuleGroupData();
                DynamicPluginAdapter pluginAdapter = moduleGroupData.getPluginAdapter();
                IDynamicConfiguration moduleById = pluginAdapter.getModuleById(id);
                String mvnUri = null;
                if (moduleById == null) {
                    // should be an existing module in studio
                    mvnUri = id;
                    // ModuleNeeded moduleNeeded =
                    // HadoopDistributionPlugin.getInstance().getExistingModuleMap().get(id);
                    // mvnUri = moduleNeeded.getMavenUri();
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

    protected class GroupDetailsColumnLabelProvider extends ColumnLabelProvider {

        @Override
        public String getText(Object element) {
            return getMavenUri(element);
        }

    }

    protected class MavenUriEditingSupport extends EditingSupport {

        public MavenUriEditingSupport(ColumnViewer viewer) {
            super(viewer);
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new TextCellEditor((Composite) getViewer().getControl());
        }

        @Override
        protected boolean canEdit(Object element) {
            String id = (String) ((IDynamicConfiguration) element).getAttribute(DynamicModuleGroupAdapter.ATTR_LIBRARY_ID);
            String mavenUri = getMavenUri(element);
            if (StringUtils.equals(id, mavenUri)) {
                return false;
            }
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            return getMavenUri(element);
        }

        @Override
        protected void setValue(Object element, Object value) {

            try {
                String valueStr = (String) value;
                if (valueStr.endsWith("/")) { //$NON-NLS-1$
                    valueStr = valueStr.substring(0, valueStr.length() - 1);
                }
                DynamicModuleGroupData moduleGroupData = getModuleGroupData();
                Map<String, String> mavenUriIdMap = moduleGroupData.getMavenUriIdMap();
                String existId = mavenUriIdMap.get(value);
                if (StringUtils.isNotEmpty(existId)) {
                    ((IDynamicConfiguration) element).setAttribute(DynamicModuleGroupAdapter.ATTR_LIBRARY_ID, existId);
                } else {
                    MavenArtifact mavenArtifact = MavenUrlHelper.parseMvnUrl(valueStr, false);
                    if (mavenArtifact != null) {
                        DynamicPluginAdapter pluginAdapter = moduleGroupData.getPluginAdapter();
                        IDynamicPluginConfiguration pluginConfiguration = pluginAdapter.getPluginConfiguration();

                        String id = pluginConfiguration.getId();
                        String jarName = mavenArtifact.getFileName();
                        String moduleName = DynamicDistributionUtils
                                .formatId(jarName + "_" + DynamicDistributionUtils.generateTimestampId()); //$NON-NLS-1$
                        String runtimeId = DynamicDistributionUtils.getPluginKey("USER", "GENERATED", id, moduleName); //$NON-NLS-1$ //$NON-NLS-2$

                        ModuleBean moduleBean = new ModuleBean();
                        moduleBean.setContext("plugin:" + HadoopClusterPlugin.PLUGIN_ID); //$NON-NLS-1$
                        moduleBean.setExcludeDependencies(Boolean.TRUE.toString());
                        moduleBean.setId(runtimeId);
                        moduleBean.setMvnUri(valueStr);
                        moduleBean.setJarName(jarName);

                        IDynamicConfiguration libraryNeeded = DynamicModuleAdapter.createLibraryNeeded(moduleBean);
                        IDynamicPlugin dynamicPlugin = moduleGroupData.getDynamicPlugin();
                        IDynamicExtension libraryNeededExtension = DynamicPluginAdapter.getLibraryNeededExtension(dynamicPlugin);
                        libraryNeededExtension.addConfiguration(libraryNeeded);

                        ((IDynamicConfiguration) element).setAttribute(DynamicModuleGroupAdapter.ATTR_LIBRARY_ID, runtimeId);
                        mavenUriIdMap.put(valueStr, runtimeId);
                        pluginAdapter.buildIdMaps();
                    }
                }
                
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            getViewer().update(element, null);
        }

    }

}
