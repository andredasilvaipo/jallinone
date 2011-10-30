package org.jallinone.items.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantNameVO;
import org.openswing.swing.domains.java.*;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the brands grid frame.</p>
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
public class BrandsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colBrand = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();
  JPanel voidPanel = new JPanel();


  public BrandsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadBrands");
    try {
      init();
      jbInit();
      setSize(400,350);
      setMinimumSize(new Dimension(400,350));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    grid.setAutoLoadData(false);

    controlCompaniesCombo.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        Object companyCodeSys01 = controlCompaniesCombo.getValue();
        if (companyCodeSys01==null)
          companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();

        grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
				grid.reloadData();
      }

    });


  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.items.java.BrandVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("brands"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("ITM31");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colBrand.setMaxCharacters(120);
    colBrand.setTrimText(true);
    colBrand.setUpperCase(true);
    colBrand.setColumnFilterable(true);
    colBrand.setColumnName("brandCodeITM31");
		colBrand.setHeaderColumnName("brand code");
    colBrand.setColumnSortable(true);
    colBrand.setEditableOnInsert(true);
    colBrand.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colBrand.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
		colDescr.setColumnName("descriptionITM31");
    colDescr.setHeaderColumnName("descriptionSys10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(250);
    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout2);
    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colBrand, null);
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
    filterPanel.add(controlCompaniesCombo,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(voidPanel,    new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }


  public Object getCompanyCodeSys01() {
    Object companyCodeSys01 = controlCompaniesCombo.getValue();
    if (companyCodeSys01==null)
      companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();
    return companyCodeSys01;
  }
  public GridControl getGrid() {
    return grid;
  }

}
