package org.jallinone.documents.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.util.ArrayList;
import java.util.HashMap;

import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import java.awt.event.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.documents.java.*;
import javax.swing.border.*;
import java.beans.Beans;
import org.openswing.swing.form.client.FormController;
import java.sql.Timestamp;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Hashtable;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used to filter documents.</p>
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
public class DocFilterPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel innerPanel = new Form();
  JScrollPane sp = new JScrollPane();
  TitledBorder titledBorder1;

  /** filter to apply to the documents grid */
  private HashMap filters = new HashMap();


  public DocFilterPanel() {
    try {
      jbInit();
    }
    catch (Exception ex) {
    }
  }


  /**
   * Remove all panel content.
   */
  public final void clearData() {
    innerPanel.removeAll();
    innerPanel.revalidate();
    innerPanel.repaint();
    filters.clear();
  }


  /**
   * Load data from server and construct panel content.
   */
  public final void reloadData(DocumentsFrame frame) {
    try {
      // remove all panel content...
      clearData();
      frame.getGrid().getOtherGridParams().put(ApplicationConsts.PROPERTIES_FILTER,filters);


      // retrieve customized input controls list...
      GridParams gridParams = new GridParams();
      DefaultMutableTreeNode node = frame.getHierarTreePanel().getSelectedNode();
      if (node!=null) {
        HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
        HierarchyLevelVO root = (HierarchyLevelVO)((DefaultMutableTreeNode)node.getRoot()).getUserObject();
        gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
        gridParams.getOtherGridParams().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());
      }
      else
        gridParams.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_HIE01);
      gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,frame.getHierarTreePanel().getProgressiveHIE02());
      gridParams.getOtherGridParams().put(ApplicationConsts.LOAD_ANCIENTS,Boolean.TRUE);
      Response res = ClientUtils.getData("loadLevelProperties",gridParams);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
            ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      java.util.List rows = ((VOListResponse)res).getRows();
      if (rows.size() > 0) {
        // adding customized input controls...
        LevelPropertyVO inputControlInfo = null;
        int row = 0;
        int col = 0;
        LabelControl labelControl = null;
        TextControl textControl = null;
        DateControl dateControl = null;
        NumericControl numericControl = null;
        for (int i = 0; i < rows.size(); i++) {
          inputControlInfo = (LevelPropertyVO) rows.get(i);
          labelControl = new LabelControl();
          labelControl.setText(inputControlInfo.getDescriptionSYS10());
          innerPanel.add(labelControl,
                         new GridBagConstraints(col++, row, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 5, 5, 5), 0, 0));

          if (inputControlInfo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_TEXT)) {
            textControl = new TextControl();
            textControl.setMaxCharacters(255);
            textControl.setLinkLabel(labelControl);
            innerPanel.add(textControl,
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 15), 0, 0));
            textControl.addFocusListener(new ControlFocusListener(inputControlInfo,textControl));
          }
          else if (inputControlInfo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_DATE)) {
            dateControl = new DateControl();
            dateControl.setLinkLabel(labelControl);
            innerPanel.add(dateControl,
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
            dateControl.addFocusListener(new ControlFocusListener(inputControlInfo,dateControl));
          }
          else if (inputControlInfo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_NUM)) {
            numericControl = new NumericControl();
            numericControl.setDecimals(5);
            numericControl.setLinkLabel(labelControl);
            innerPanel.add(numericControl,
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 15), 0, 0));
            numericControl.addFocusListener(new ControlFocusListener(inputControlInfo,numericControl));
          }

          col++;
          if (col >= 2) {
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
                       new GridBagConstraints(col, row, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 5), 0, 0));

        innerPanel.add(new JPanel(),
                       new GridBagConstraints(0, row, 1, 1, 0.0, 1.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.VERTICAL,
                                              new Insets(5, 5, 5, 5), 0, 0));
        this.revalidate();
        this.repaint();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    this.setLayout(borderLayout1);
    this.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("documents filter"));
    this.add(sp,BorderLayout.CENTER);
    sp.setBorder(BorderFactory.createEmptyBorder());
    sp.getViewport().add(innerPanel,null);
    sp.setAutoscrolls(true);
    innerPanel.setLayout(gridBagLayout1);
  }


  /**
   * <p>Description: Focus listener for the specified input control.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ControlFocusListener extends FocusAdapter {

    private LevelPropertyVO inputControlInfo;
    private BaseInputControl control;

    public ControlFocusListener(LevelPropertyVO inputControlInfo,BaseInputControl control) {
      this.inputControlInfo = inputControlInfo;
      this.control = control;
    }

    public void focusLost(FocusEvent e) {
      if (control.getValue()==null || control.getValue().toString().equals(""))
        filters.remove(inputControlInfo.getProgressiveSys10DOC21());
      else
        filters.put(inputControlInfo.getProgressiveSys10DOC21(),control.getValue());
    }

  }


}

