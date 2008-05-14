package org.jallinone.purchases.documents.invoices.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.warehouse.java.WarehousePK;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.table.columns.client.*;
import java.awt.event.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.domains.java.Domain;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import java.util.HashSet;
import org.jallinone.purchases.documents.client.PurchaseDocument;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the list of in delivery notes linked to the purchase invoice.</p>
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
 * but WITHOUT ANY WARRANTY; within even the implied warranty of
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
public class InDeliveryNotesFromPurchaseDocPanel extends JPanel {

  GridBagLayout borderLayin2 = new GridBagLayout();
  GridBagLayout gridBagLayin1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TitledBorder titledBorder2;
  FlowLayout flowLayin1 = new FlowLayout();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  BorderLayout borderLayin1 = new BorderLayout();

  /** parent frame */
  private PurchaseDocument parentFrame = null;

  TextColumn colCompany = new TextColumn();
  IntegerColumn colDocYear = new IntegerColumn();
  IntegerColumn colDocSequence = new IntegerColumn();
  TextColumn colDestCode = new TextColumn();
  TextColumn colDesc = new TextColumn();
  DateColumn colDocDate = new DateColumn();
  CheckBoxColumn colSel = new CheckBoxColumn();


  public InDeliveryNotesFromPurchaseDocPanel(PurchaseDocument parentFrame) {
    this.parentFrame = parentFrame;
    try {
      jbInit();

      grid.setController(new InDeliveryNotesFromPurchaseDocController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadInDeliveryNotesForPurchaseDoc");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder2 = new TitledBorder("");
    this.setLayout(borderLayin1);
    grid.setAutoLoadData(false);
    grid.setFunctionId("DOC06_INV_FROM_DN");
    grid.setMaxSortedColumns(3);
    grid.setValueObjectClassName("org.jallinone.purchases.documents.invoices.java.InDeliveryNotesVO");
    grid.setVisibleStatusPanel(false);
    this.setBorder(titledBorder2);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("delivery notes"));
    titledBorder2.setTitleColor(Color.blue);
    flowLayin1.setAlignment(FlowLayout.LEFT);

    colCompany.setColumnName("companyCodeSys01DOC08");
    colDocYear.setColumnName("docYearDOC08");
    colDocYear.setPreferredWidth(80);
    colDocSequence.setColumnName("docSequenceDOC08");
    colDocSequence.setPreferredWidth(80);
    colDocDate.setColumnName("docDateDOC08");
    colDocDate.setPreferredWidth(80);
    colSel.setColumnName("selected");
    colSel.setShowDeSelectAllInPopupMenu(true);
    colSel.setColumnRequired(false);
    colSel.setEditableOnEdit(true);
    colSel.setPreferredWidth(50);
    colDestCode.setColumnName("destinationCodeReg18DOC08");
    colDestCode.setPreferredWidth(90);
    colDesc.setColumnName("descriptionDOC08");
    colDesc.setPreferredWidth(200);
    this.add(grid, BorderLayout.CENTER);

    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colDocSequence, null);
    grid.getColumnContainer().add(colDocDate, null);
    grid.getColumnContainer().add(colSel, null);
    grid.getColumnContainer().add(colDestCode, null);
    grid.getColumnContainer().add(colDesc, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  /**
   * Enable or disable this panel.
   */
  public final void setEnabled(boolean enabled) {
    if (!enabled) {
      grid.setMode(Consts.READONLY);
    }
    else {
      grid.setMode(Consts.EDIT);
    }
  }



}

