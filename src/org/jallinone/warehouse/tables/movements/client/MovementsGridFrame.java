package org.jallinone.warehouse.tables.movements.client;

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
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.form.client.Form;
import java.awt.FlowLayout;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.warehouse.java.WarehousePK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the warehouse movements grid frame.</p>
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
public class MovementsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();


  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colWarehouseCode = new TextColumn();
  TextColumn colWarehouseDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  ComboColumn colItemType = new ComboColumn();
  ComboColumn colQtySign = new ComboColumn();
  DateTimeColumn colMovDate = new DateTimeColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colDeltaQty = new DecimalColumn();
  TextColumn colPosition = new TextColumn();
  TextColumn colUsername = new TextColumn();
  TextColumn colNote = new TextColumn();
  TextColumn colMotiveCode = new TextColumn();
  TextColumn colMotiveDescr = new TextColumn();

	JPanel northPanel = new JPanel();

	Form warehousePanel = new Form();
	LabelControl labelWarehouse = new LabelControl();
	CodLookupControl controlWarehouseCod = new CodLookupControl();
	TextControl controlWarehouseDescr = new TextControl();
	FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);

	/** warehouse code lookup data locator */
	LookupServerDataLocator warDataLocator = new LookupServerDataLocator();

	/** warehouse code lookup controller */
	LookupController warController = new LookupController();



  public MovementsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadWarehouseMovements");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));


			// warehouse code lookup...
			warDataLocator.setGridMethodName("loadWarehouses");
			warDataLocator.setValidationMethodName("validateWarehouseCode");

			controlWarehouseCod.setLookupController(warController);
			warController.setLookupDataLocator(warDataLocator);
			warController.setFrameTitle("warehouses");

			warController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
			warController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCodeSys01WAR01");
			warController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWAR01");
			warController.addLookup2ParentLink("progressiveHie02WAR01", "progressiveHie02WAR01");
			warController.addLookup2ParentLink("descriptionWAR01", "descriptionWAR01");

			warController.setAllColumnVisible(false);
			warController.setVisibleColumn("companyCodeSys01WAR01", true);
			warController.setVisibleColumn("warehouseCodeWAR01", true);
			warController.setVisibleColumn("descriptionWAR01", true);
			warController.setPreferredWidthColumn("descriptionWAR01", 250);
			warController.setFramePreferedSize(new Dimension(460,500));
			warController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					// fill in the grid v.o., according to the selected war settings...
					WarehouseVO vo = (WarehouseVO)warehousePanel.getVOModel().getValueObject();
					if (vo.getWarehouseCodeWAR01()==null || vo.getWarehouseCodeWAR01().equals("")) {
						grid.clearData();
					}
					else {
						grid.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,new WarehousePK(vo.getCompanyCodeSys01WAR01(),vo.getWarehouseCodeWAR01()));
						grid.reloadData();
					}
				}

				public void beforeLookupAction(ValueObject parentVO) { }

				public void forceValidate() {}

			});


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
	  northPanel.setLayout(new BorderLayout());
		grid.setAutoLoadData(false);

    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setValueObjectClassName("org.jallinone.warehouse.tables.movements.java.MovementVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse movements"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setExportButton(exportButton);
    grid.setFunctionId("WAR02");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colWarehouseCode.setMaxCharacters(20);
    colWarehouseCode.setTrimText(true);
    colWarehouseCode.setUpperCase(true);
    colWarehouseCode.setColumnFilterable(true);
    colWarehouseCode.setColumnName("warehouseCodeWar01WAR02");
    colWarehouseCode.setColumnSortable(true);
    colWarehouseCode.setEditableOnInsert(true);
    colWarehouseCode.setHeaderColumnName("warehouseCodeWar01DOC08");
    colWarehouseCode.setPreferredWidth(70);
    colWarehouseCode.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colWarehouseCode.setSortingOrder(0);
    colWarehouseDescr.setColumnFilterable(false);
    colWarehouseDescr.setColumnName("descriptionWAR01");
    colWarehouseDescr.setColumnSortable(false);
    colWarehouseDescr.setEditableOnEdit(true);
    colWarehouseDescr.setEditableOnInsert(true);
    colWarehouseDescr.setHeaderColumnName("descriptionWAR01");
    colWarehouseDescr.setPreferredWidth(150);
    colItemType.setDomainId("WAR_ITEM_TYPE");
    colItemType.setColumnDuplicable(true);
    colItemType.setColumnFilterable(true);
    colItemType.setColumnName("itemTypeWAR04");
    colItemType.setColumnSortable(true);
    colItemType.setEditableOnEdit(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setHeaderColumnName("type");
    colItemType.setPreferredWidth(70);
    colQtySign.setDomainId("WAR_QTY_SIGN");
    colQtySign.setColumnFilterable(true);
    colQtySign.setColumnName("qtySignWAR04");
    colQtySign.setColumnSortable(true);
    colQtySign.setEditableOnInsert(true);
    colQtySign.setHeaderColumnName("sign");
    colQtySign.setPreferredWidth(60);
    colMovDate.setColumnFilterable(true);
    colMovDate.setColumnName("movementDateWAR02");
    colMovDate.setColumnSortable(true);
    colMovDate.setPreferredWidth(120);
    colMovDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01WAR02");
    colItemCode.setColumnSortable(true);
    colItemCode.setPreferredWidth(70);
    colItemDescr.setColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(200);
    colDeltaQty.setColumnName("deltaQtyWAR02");
    colDeltaQty.setPreferredWidth(50);
    colPosition.setColumnFilterable(true);
    colPosition.setColumnName("locationDescriptionSYS10");
    colPosition.setColumnSortable(true);
    colPosition.setPreferredWidth(100);
    colUsername.setColumnName("usernameWAR02");
    colUsername.setPreferredWidth(80);
    colNote.setColumnFilterable(true);
    colNote.setColumnName("noteWAR02");
    colNote.setColumnSortable(true);
    colNote.setPreferredWidth(300);
    colMotiveCode.setColumnFilterable(true);
    colMotiveCode.setColumnName("warehouseMotiveWar04WAR02");
    colMotiveCode.setColumnSortable(true);
    colMotiveCode.setHeaderColumnName("warehouseMotiveWar04WAR02");
    colMotiveCode.setPreferredWidth(60);
    colMotiveDescr.setColumnName("motiveDescriptionSYS10");
    colMotiveDescr.setPreferredWidth(200);
    this.getContentPane().add(northPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colMovDate, null);
    grid.getColumnContainer().add(colWarehouseCode, null);
    grid.getColumnContainer().add(colWarehouseDescr, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colDeltaQty, null);
    grid.getColumnContainer().add(colQtySign, null);
    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colPosition, null);
    grid.getColumnContainer().add(colUsername, null);
    grid.getColumnContainer().add(colMotiveCode, null);
    grid.getColumnContainer().add(colMotiveDescr, null);
    grid.getColumnContainer().add(colNote, null);


		warehousePanel.setVOClassName("org.jallinone.warehouse.java.WarehouseVO");
		labelWarehouse.setText("warehouse");
		controlWarehouseCod.setEnabled(true);
		controlWarehouseCod.setMaxCharacters(20);
		controlWarehouseCod.setAttributeName("warehouseCodeWAR01");
		controlWarehouseDescr.setEnabled(false);
		controlWarehouseDescr.setAttributeName("descriptionWAR01");
		warehousePanel.setLayout(flowLayout2);
		warehousePanel.add(labelWarehouse, null);
		warehousePanel.add(controlWarehouseCod, null);
		northPanel.add(warehousePanel, BorderLayout.NORTH);
		northPanel.add(buttonsPanel, BorderLayout.SOUTH);
		warehousePanel.add(controlWarehouseDescr, null);


  }

}
