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
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import java.awt.event.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.documents.java.*;
import javax.swing.border.*;
import java.beans.Beans;
import org.openswing.swing.form.client.FormController;
import java.sql.Timestamp;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used to show document properties.</p>
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
public class DocPropertiesPanel extends Form {

  BorderLayout borderLayout1 = new BorderLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel innerPanel = new Form();
  JScrollPane sp = new JScrollPane();

  /** last row v.o. */
  private ArrayList oldRows = new ArrayList();

  /** input controls */
  private ArrayList inputControls = new ArrayList();


  public DocPropertiesPanel() {
    try {
      this.setFunctionId("DOC14");
      jbInit();
      this.setFormController(new FormController() {

        /**
         * Method called by the Form panel to update existing data.
         * @param oldPersistentObject original value object, previous to the changes
         * @param persistentObject value object to save
         * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
         */
        public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
          BaseInputControl control = null;
          ArrayList newRows = new ArrayList();
          DocPropertyVO vo = null;
          for(int i=0;i<inputControls.size();i++) {
            control = (BaseInputControl)inputControls.get(i);
            vo = (DocPropertyVO)((DocPropertyVO)oldRows.get(i)).clone();
            if (vo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_TEXT))
              vo.setTextValueDOC20((String)control.getValue());
            else if (vo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_NUM))
              vo.setNumValueDOC20((BigDecimal)control.getValue());
            else if (vo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_DATE))
              vo.setDateValueDOC20((Timestamp)control.getValue());
            newRows.add(vo);
          }
          ArrayList[] list = new ArrayList[]{oldRows,newRows};
          Response res = ClientUtils.getData("updateDocProperties",list);
          if (!res.isError()) {
            oldRows = newRows;
            return new VOResponse(persistentObject);
          }
          return res;
        }


      });
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
  }


  /**
   * Load data from server and construct panel content.
   */
  public final void reloadData(DocumentFrame frame) {
    try {
      // remove all panel content...
      this.removeLinkedPanel(innerPanel);
      clearData();
      oldRows.clear();

      // retrieve customized input controls list...
      DetailDocumentVO vo = (DetailDocumentVO)frame.getFormPanel().getVOModel().getValueObject();
      Response res = ClientUtils.getData("loadDocProperties",new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14()));
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
            ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      ArrayList rows = new ArrayList(((VOListResponse)res).getRows());
      if (rows.size() > 0) {
        // adding customized input controls...
        DocPropertyVO inputControlInfo = null;
        int row = 0;
        int col = 0;
        LabelControl labelControl = null;
        TextControl textControl = null;
        DateControl dateControl = null;
        NumericControl numericControl = null;
        for (int i = 0; i < rows.size(); i++) {
          inputControlInfo = (DocPropertyVO) rows.get(i);
          oldRows.add(inputControlInfo);
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
            textControl.setValue(inputControlInfo.getTextValueDOC20());
            inputControls.add(textControl);
          }
          else if (inputControlInfo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_DATE)) {
            dateControl = new DateControl();
            dateControl.setLinkLabel(labelControl);
            innerPanel.add(dateControl,
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 15), 0, 0));
            dateControl.setValue(inputControlInfo.getDateValueDOC20());
            inputControls.add(dateControl);
          }
          else if (inputControlInfo.getPropertyTypeDOC21().equals(ApplicationConsts.TYPE_NUM)) {
            numericControl = new NumericControl();
            numericControl.setDecimals(5);
            numericControl.setLinkLabel(labelControl);
            innerPanel.add(numericControl,
                           new GridBagConstraints(col, row, 1, 1, 1.0, 0.0
                , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 15), 0, 0));
            numericControl.setValue(inputControlInfo.getNumValueDOC20());
            inputControls.add(numericControl);
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
        this.addLinkedPanel(innerPanel);
        this.setMode(Consts.READONLY);

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    this.add(sp,BorderLayout.CENTER);
    sp.setBorder(BorderFactory.createEmptyBorder());
    sp.getViewport().add(innerPanel,null);
    sp.setAutoscrolls(true);
    innerPanel.setLayout(gridBagLayout1);
  }


}
