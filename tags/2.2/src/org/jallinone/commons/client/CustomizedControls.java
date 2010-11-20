package org.jallinone.commons.client;

import java.beans.*;
import java.math.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.jallinone.system.customizations.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains a set of customized controls,
 * read from SYS12 table and based on the specified function code.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class CustomizedControls extends JScrollPane {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel innerPanel = new JPanel();

  public CustomizedControls(Container parent,Form form,BigDecimal progressiveSYS13) {
    try {
      jbInit();

      if (Beans.isDesignTime()) {
        return;
      }

      // retrieve the panel dimension...
      Dimension dim = parent.getPreferredSize();
      int w = dim.width;
      int h = dim.height;
      int maxCols = 2;
      if (w > 0 && h > 0) {
        maxCols = w / 100;
      }

      // retrieve customized input controls list...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ArrayList inputControls = applet.getAuthorizations().getCustomizedWindows().getCustomizedFields(progressiveSYS13);
      if (inputControls.size() > 0) {

        parent.add(this,ClientSettings.getInstance().getResources().getResource("custom data"));
//        this.setPreferredSize(new Dimension(parent.getPreferredSize()));
//        this.setSize(new Dimension(parent.getPreferredSize()));
//        this.getViewport().setPreferredSize(new Dimension(parent.getPreferredSize()));
//        this.getViewport().setSize(new Dimension(parent.getPreferredSize()));
//        innerPanel.setPreferredSize(new Dimension(parent.getPreferredSize()));
//        innerPanel.setSize(new Dimension(parent.getPreferredSize()));

        WindowCustomizationVO inputControlInfo = null;
        int row = 0;
        int col = 0;
        LabelControl labelControl = null;
        TextControl textControl = null;
        DateControl dateControl = null;
        NumericControl numericControl = null;
        for (int i = 0; i < inputControls.size(); i++) {
          inputControlInfo = (WindowCustomizationVO) inputControls.get(i);
          labelControl = new LabelControl();
          labelControl.setText(inputControlInfo.getDescriptionSYS10());
          innerPanel.add(labelControl,
                         new GridBagConstraints(col++, row, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 5, 5, 5), 0, 0));

          if (inputControlInfo.getColumnTypeSYS12().equals("S")) {
            textControl = new TextControl();
            textControl.setAttributeName(inputControlInfo.
                                         getAttributeNameSYS12());
            textControl.setMaxCharacters(inputControlInfo.getColumnSizeSYS12().
                                         intValue());
            textControl.setLinkLabel(labelControl);
            innerPanel.add(textControl,
                           new GridBagConstraints(col, row, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
          }
          else if (inputControlInfo.getColumnTypeSYS12().equals("D")) {
            dateControl = new DateControl();
            dateControl.setAttributeName(inputControlInfo.
                                         getAttributeNameSYS12());
            dateControl.setLinkLabel(labelControl);
            innerPanel.add(dateControl,
                           new GridBagConstraints(col, row, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
          }
          else if (inputControlInfo.getColumnTypeSYS12().equals("N")) {
            numericControl = new NumericControl();
            numericControl.setAttributeName(inputControlInfo.
                                            getAttributeNameSYS12());
            if (inputControlInfo.getColumnDecSYS12() != null) {
              numericControl.setDecimals(inputControlInfo.getColumnDecSYS12().
                                         intValue());
            }
            numericControl.setMaxValue(Math.pow(10d,
                                                inputControlInfo.getColumnSizeSYS12().
                                                doubleValue()) - 1);
            numericControl.setMaxCharacters(
                inputControlInfo.getColumnSizeSYS12().intValue() +
                inputControlInfo.getColumnDecSYS12().intValue() +
                1
                );
            numericControl.setLinkLabel(labelControl);
            innerPanel.add(numericControl,
                           new GridBagConstraints(col, row, 1, 1, 0.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
          }

          col++;
          if (col >= maxCols) {
            innerPanel.add(new JPanel(),
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
            row++;
            col = 0;
          }

        }
        row++;
        innerPanel.add(new JPanel(),
                       new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));

        innerPanel.add(new JPanel(),
                       new GridBagConstraints(0, row, 1, 1, 0.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.VERTICAL,
                                              new Insets(5, 5, 5, 5), 0, 0));
        this.getViewport().revalidate();
//        if (parent instanceof JTabbedPane)
//          ((JTabbedPane)parent).revalidate();
//        else
//          parent.validate();
//        parent.repaint();
        form.addLinkedPanel(innerPanel);
//        innerPanel.revalidate();
//        innerPanel.repaint();
//        JInternalFrame frame = ClientUtils.getParentInternalFrame(innerPanel);
//        frame.revalidate();
//        frame.repaint();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void setContent() {
    this.getViewport().removeAll();
    this.getViewport().add(innerPanel,null);
    this.getViewport().revalidate();
  }


  private void jbInit() throws Exception {
    this.setBorder(BorderFactory.createEmptyBorder());
    this.getViewport().add(innerPanel,null);
    this.setAutoscrolls(true);
    innerPanel.setLayout(gridBagLayout1);
  }






}
