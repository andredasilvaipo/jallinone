package org.jallinone.scheduler.callouts.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import java.math.BigDecimal;
import javax.swing.border.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import org.jallinone.scheduler.callouts.java.CallOutTypeVO;
import org.jallinone.commons.java.ApplicationConsts;
import java.awt.event.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the call-out requests grid frame.</p>
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
public class CallOutRequestsFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  InsertButton insertButton1 = new InsertButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TextColumn colDescription = new TextColumn();
  TextColumn colName_1 = new TextColumn();
  TextColumn colCompany = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  IntegerColumn colProg = new IntegerColumn();
  IntegerColumn colReqYear = new IntegerColumn();
  TextColumn colName_2 = new TextColumn();
  TextColumn colCallOutCode = new TextColumn();
  TextColumn colCallOutDescr = new TextColumn();
  ComboColumn colPriority = new ComboColumn();
  ComboColumn colState = new ComboColumn();
  DateTimeColumn colReqDate = new DateTimeColumn();
  TextColumn colUsername = new TextColumn();
	GenericButton buttonGraph = new GenericButton(new ImageIcon(ClientUtils.getImage("graph.gif")));


  public CallOutRequestsFrame(CallOutRequestsController callOutRequestsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      grid.setController(callOutRequestsController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadCallOutRequests");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("call-out requests"));
    grid.setValueObjectClassName("org.jallinone.scheduler.callouts.java.GridCallOutRequestVO");
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colDescription.setColumnFilterable(true);
    colDescription.setColumnName("descriptionSCH03");
    colDescription.setColumnSortable(true);
    colDescription.setPreferredWidth(250);
    colDescription.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colName_1.setColumnFilterable(true);
    colName_1.setColumnName("name_1REG04");
    colName_1.setColumnSortable(true);
    colName_1.setPreferredWidth(150);
    colName_1.setSortingOrder(0);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01SCH03");
    colCompany.setColumnSortable(true);
    colCompany.setPreferredWidth(90);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(1);
    grid.setAutoLoadData(true);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("SCH03");
    grid.setLockedColumns(3);
    grid.setMaxSortedColumns(5);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    colProg.setColumnFilterable(true);
    colProg.setColumnName("progressiveSCH03");
    colProg.setColumnSortable(true);
    colProg.setPreferredWidth(70);
    colProg.setSortingOrder(3);
    colProg.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colReqYear.setColumnFilterable(true);
    colReqYear.setColumnName("requestYearSCH03");
    colReqYear.setColumnSortable(true);
    colReqYear.setPreferredWidth(80);
    colReqYear.setSortingOrder(2);
    colReqYear.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colName_2.setColumnName("name_2REG04");
    colName_2.setPreferredWidth(120);
    colCallOutDescr.setColumnName("callOutDescriptionSYS10");
    colCallOutDescr.setPreferredWidth(200);
    colCallOutCode.setColumnName("callOutCodeSch10SCH03");
    colCallOutCode.setPreferredWidth(80);
    colPriority.setColumnName("prioritySCH03");
    colPriority.setDomainId("ACTIVITY_PRIORITY");
    colPriority.setPreferredWidth(50);
    colState.setDomainId("CALL_OUT_STATE");
    colState.setColumnName("callOutStateSCH03");
    colState.setPreferredWidth(70);
    colReqDate.setColumnFilterable(true);
    colReqDate.setColumnName("requestDateSCH03");
    colReqDate.setColumnSortable(true);
    colReqDate.setPreferredWidth(120);
    colUsername.setColumnFilterable(true);
    colUsername.setColumnName("usernameSys03SCH03");
    colUsername.setColumnSortable(true);
    buttonGraph.addActionListener(new CallOutRequestsFrame_buttonGraph_actionAdapter(this));
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    buttonsPanel.add(insertButton1,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(reloadButton1,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(deleteButton1,      new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,      new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,          new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		buttonsPanel.add(buttonGraph,          new GridBagConstraints(8, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));


    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colReqYear, null);
    grid.getColumnContainer().add(colProg, null);
    grid.getColumnContainer().add(colReqDate, null);
    grid.getColumnContainer().add(colState, null);
    grid.getColumnContainer().add(colDescription, null);
    grid.getColumnContainer().add(colName_1, null);
    grid.getColumnContainer().add(colName_2, null);
    grid.getColumnContainer().add(colCallOutCode, null);
    grid.getColumnContainer().add(colCallOutDescr, null);
    grid.getColumnContainer().add(colPriority, null);
    grid.getColumnContainer().add(colUsername, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }

  void buttonGraph_actionPerformed(ActionEvent e) {
		new CallOutRequestsReportFrame();
  }


}

class CallOutRequestsFrame_buttonGraph_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestsFrame adaptee;

  CallOutRequestsFrame_buttonGraph_actionAdapter(CallOutRequestsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonGraph_actionPerformed(e);
  }
}
