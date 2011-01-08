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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the call-outs tree+grid frame.</p>
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
public class CallOutsFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  JSplitPane split = new JSplitPane();
  LabelControl labelCallOutType = new LabelControl();
  InsertButton insertButton1 = new InsertButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ComboBoxControl comboBoxControl1 = new ComboBoxControl();
  HierarTreePanel hierarTreePanel = new HierarTreePanel();
  GridControl grid = new GridControl();
  TextColumn colCod = new TextColumn();
  TextColumn colDescr = new TextColumn();
  TextColumn colCompany = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();


  public CallOutsFrame(final CallOutsController callOutsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      grid.setController(callOutsController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadCallOuts");
      hierarTreePanel.setFunctionId("SCH11");
      hierarTreePanel.setTreeController(callOutsController);

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            callOutsController.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      init();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Retrieve call-out types and fill in the call-out types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadCallOutTypes",new GridParams());
    Domain d = new Domain("CALL_OUT_TYPES");
    if (!res.isError()) {
      CallOutTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (CallOutTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02SCH11(),vo.getDescriptionSYS10());
      }
    }
    comboBoxControl1.setDomain(d);
    comboBoxControl1.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          hierarTreePanel.setProgressiveHIE02((BigDecimal)comboBoxControl1.getValue());
          hierarTreePanel.reloadTree();
          grid.clearData();
        }
      }
    });
    if (d.getDomainPairList().length==1)
      comboBoxControl1.getComboBox().setSelectedIndex(0);
    else
      comboBoxControl1.getComboBox().setSelectedIndex(-1);
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("call-outs"));
    grid.setValueObjectClassName("org.jallinone.scheduler.callouts.java.CallOutVO");
    labelCallOutType.setText("call-out type");
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colCod.setColumnFilterable(true);
    colCod.setColumnName("callOutCodeSCH10");
    colCod.setColumnSortable(true);
    colCod.setPreferredWidth(120);
    colCod.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCod.setSortingOrder(2);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setPreferredWidth(250);
    colDescr.setSortingOrder(0);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01SCH10");
    colCompany.setColumnSortable(true);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(1);
    grid.setAutoLoadData(false);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("SCH10");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);
    buttonsPanel.add(labelCallOutType,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(insertButton1,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(reloadButton1,    new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(deleteButton1,    new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,    new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,    new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(comboBoxControl1,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    split.add(hierarTreePanel, JSplitPane.LEFT);
    split.add(grid, JSplitPane.RIGHT);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colCod, null);
    grid.getColumnContainer().add(colDescr, null);
    split.setDividerLocation(210);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }
  public HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }

}
