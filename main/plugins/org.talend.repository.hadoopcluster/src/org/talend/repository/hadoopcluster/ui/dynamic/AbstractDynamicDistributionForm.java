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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class AbstractDynamicDistributionForm extends Composite {

    public AbstractDynamicDistributionForm(Composite parent, int style) {
        super(parent, style);
    }

    public static Point getNewButtonSize(Button btn) {
        return getNewButtonSize(btn, 6);
    }

    public static Point getNewButtonSize(Button btn, int hPadding) {
        Point btnSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        btnSize.x += hPadding * 2;
        return btnSize;
    }

}
