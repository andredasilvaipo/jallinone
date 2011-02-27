package org.jallinone.system.importdata.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jallinone.commons.client.*;
import org.jallinone.commons.java.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is ETL processes grid frame.</p>
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
public class ETLProcessesGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  GridControl grid = new GridControl();


  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();


  public ETLProcessesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadETLProcesses");
    try {
      init();
      jbInit();
      setSize(450,350);
      setMinimumSize(new Dimension(450,350));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    controlCompaniesCombo.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        Object companyCodeSys01 = controlCompaniesCombo.getValue();
        if (companyCodeSys01==null)
          companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();
        grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
      }

    });

  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setValueObjectClassName("org.jallinone.system.importdata.java.ETLProcessVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("etl processes"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("IMPORT_DATA");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(null);
    grid.setReloadButton(reloadButton);
    colDescr.setMaxCharacters(120);
    colDescr.setTrimText(true);
    colDescr.setUpperCase(true);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS23");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("description");
    colDescr.setPreferredWidth(400);
    colDescr.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDescr.setSortingOrder(1);
    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout2);
    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");
    insertButton.addActionListener(new ETLProcessesGridFrame_insertButton_actionAdapter(this));
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colDescr, null);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);

    controlCompaniesCombo.setLinkLabel(companyLabel);
    controlCompaniesCombo.setFunctionCode(grid.getFunctionId());

    topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topPanel.add(filterPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(companyLabel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCompaniesCombo,       new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
  }


  public GridControl getGrid() {
    return grid;
  }

  void insertButton_actionPerformed(ActionEvent e) {
    new ETLProcessController(grid,null);
  }

}

class ETLProcessesGridFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessesGridFrame adaptee;

  ETLProcessesGridFrame_insertButton_actionAdapter(ETLProcessesGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}
