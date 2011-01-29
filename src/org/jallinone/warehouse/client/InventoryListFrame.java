package org.jallinone.warehouse.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.util.client.ClientUtils;
import java.awt.event.*;
import org.jallinone.warehouse.java.WarehouseVO;
import java.util.HashMap;
import org.openswing.swing.message.receive.java.Response;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouse inventory list frame: it allows to define a list of inventories about warehouses.</p>
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
public class InventoryListFrame extends InternalFrame {

	LabelControl labelCompanyCode = new LabelControl();
	ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
	LabelControl labelWarehouseCode = new LabelControl();
	CodLookupControl controlWarehouseCode = new CodLookupControl();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	LookupController wareController = new LookupController();
	LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();
	TextControl controlDescr = new TextControl();
	Form warehousePanel = new Form();
  GridControl grid = new GridControl();
  GenericButton buttonSearch = new GenericButton(new ImageIcon(ClientUtils.getImage("filter.gif")));
  JPanel gridPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  ReloadButton reloadButton1 = new ReloadButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ComboColumn colState = new ComboColumn();
  TextColumn colDescr = new TextColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();

	/** grid data locator */
	private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  LabelControl labelItemType = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();


	public InventoryListFrame() {
		try {
			jbInit();

			grid.setController(new InventoryListController(this));
			grid.setGridDataLocator(gridDataLocator);
			gridDataLocator.setServerMethodName("loadInventories");
			grid.setFunctionId("INVENTORY_LIST");

			setSize(750,450);
			setMinimumSize(new Dimension(650,150));

			// warehouse lookup...
			wareDataLocator.setGridMethodName("loadWarehouses");
			wareDataLocator.setValidationMethodName("validateWarehouseCode");

			controlWarehouseCode.setLookupController(wareController);
			wareController.setForm(warehousePanel);
			wareController.setLookupDataLocator(wareDataLocator);
			wareController.setFrameTitle("warehouses");
			wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
			wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01WAR06");
			wareController.addLookup2ParentLink("descriptionWAR01","descriptionWAR01");
			wareController.setAllColumnVisible(false);
			wareController.setVisibleColumn("warehouseCodeWAR01", true);
			wareController.setVisibleColumn("descriptionWAR01", true);
			wareController.setVisibleColumn("addressWAR01", true);
			wareController.setVisibleColumn("cityWAR01", true);
			wareController.setVisibleColumn("zipWAR01", true);
			wareController.setVisibleColumn("provinceWAR01", true);
			wareController.setVisibleColumn("countryWAR01", true);
			wareController.setPreferredWidthColumn("descriptionWAR01",200);
			wareController.setFramePreferedSize(new Dimension(750,500));
			wareController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					WarehouseVO vo = (WarehouseVO)controlWarehouseCode.getLookupController().getLookupVO();
					controlDescr.setText(vo.getDescriptionWAR01());
					grid.clearData();

					if (getCompanyCodeSys01()!=null ||
							getWarehouseCodeWar01()!=null ||
							getItemType()!=null) {
						grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
						grid.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());

						grid.reloadData();
					}

				}

				public void beforeLookupAction(ValueObject parentVO) {
					wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
					wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
				}

				public void forceValidate() {}

			});

			// set domain in combo box...
			ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
			ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
			ArrayList companiesList = bca.getCompaniesList("INVENTORY_LIST");
			Domain domain = new Domain("DOMAIN_INVENTORY_LIST");
			for (int i = 0; i < companiesList.size(); i++) {
				if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
						"INVENTORY_LIST",companiesList.get(i).toString()
				))
					domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
			}
			controlCompaniesCombo.setDomain(domain);


			Domain domainState = new Domain("DOMAIN_INVENTORY_STATE");
			domainState.addDomainPair(ApplicationConsts.OPENED,"opened");
			domainState.addDomainPair(ApplicationConsts.CONFIRMED,"items confirmed");
			domainState.addDomainPair(ApplicationConsts.CLOSED,"closed");
			domainState.addDomainPair(ApplicationConsts.IN_PROGRESS,"in progress");
			colState.setDomain(domainState);


			MDIFrame.add(this);

			controlItemType.setSelectedIndex(0);
			if (controlCompaniesCombo.getComboBox().getModel().getSize()>0)
				controlCompaniesCombo.setSelectedIndex(0);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	public GridControl getGrid() {
		return grid;
	}



	private void jbInit() throws Exception {
		warehousePanel.setVOClassName("org.jallinone.warehouse.java.InventoryVO");
		warehousePanel.setFunctionId("INVENTORY_LIST");

		// define item type domain...
		Domain d = new Domain("ITEM_TYPE_WAR06");
		d.addDomainPair(ApplicationConsts.ITEM_GOOD,"good item");
		d.addDomainPair(ApplicationConsts.ITEM_DAMAGED,"damaged item");


		this.setTitle(ClientSettings.getInstance().getResources().getResource("inventories list"));
		labelWarehouseCode.setText("warehouseCodeWAR01");

		controlWarehouseCode.setAttributeName("warehouseCodeWar01WAR06");
		controlWarehouseCode.setMaxCharacters(20);
		controlDescr.setAttributeName("descriptionWAR01");
		controlDescr.setEnabled(false);
		labelCompanyCode.setText("companyCodeSys01WAR01");
		controlCompaniesCombo.setAttributeName("companyCodeSys01WAR06");
		grid.setAutoLoadData(false);

    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setInsertButton(insertButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("org.jallinone.warehouse.java.InventoryVO");
    buttonSearch.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonSearch.setHorizontalTextPosition(SwingConstants.LEADING);
		buttonSearch.setText("search");
    buttonSearch.addActionListener(new InventoryListFrame_buttonSearch_actionAdapter(this));
    gridPanel.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colState.setColumnName("stateWAR06");
    colState.setPreferredWidth(120);
    colDescr.setColumnName("descriptionWAR06");
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(250);
    colStartDate.setColumnName("startDateWAR06");
    colEndDate.setColumnName("endDateWAR06");
    colEndDate.setColumnRequired(false);
    labelItemType.setText("item type");
    controlItemType.setAttributeName("itemTypeWAR06");
    controlItemType.setDomain(d);
    this.getContentPane().add(warehousePanel,  BorderLayout.NORTH);
		warehousePanel.setLayout(gridBagLayout1);
		warehousePanel.add(labelCompanyCode,                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		warehousePanel.add(controlCompaniesCombo,                 new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
		warehousePanel.add(labelWarehouseCode,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		warehousePanel.add(controlWarehouseCode,                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
		warehousePanel.add(controlDescr,             new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    this.getContentPane().add(gridPanel,  BorderLayout.CENTER);
    warehousePanel.add(buttonSearch,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehousePanel.add(labelItemType,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    gridPanel.add(buttonsPanel, BorderLayout.NORTH);
    gridPanel.add(grid,  BorderLayout.CENTER);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(deleteButton1, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colState, null);
    grid.getColumnContainer().add(colStartDate, null);
    grid.getColumnContainer().add(colEndDate, null);
    warehousePanel.add(controlItemType,  new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

	}



  void buttonSearch_actionPerformed(ActionEvent e) {
		grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,wareDataLocator.getLookupFrameParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
		grid.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,controlWarehouseCode.getValue());
		grid.reloadData();
  }


	public String getCompanyCodeSys01() {
		return (String)wareDataLocator.getLookupFrameParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
  }


	public String getWarehouseCodeWar01() {
		return (String)controlWarehouseCode.getValue();
	}


	public String getItemType() {
		return (String)controlItemType.getValue();
	}


}

class InventoryListFrame_buttonSearch_actionAdapter implements java.awt.event.ActionListener {
  InventoryListFrame adaptee;

  InventoryListFrame_buttonSearch_actionAdapter(InventoryListFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSearch_actionPerformed(e);
  }
}
