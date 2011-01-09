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
import org.jallinone.warehouse.java.InventoryVO;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.message.send.java.GridParams;
import javax.swing.border.*;
import org.jallinone.warehouse.java.InventoryItemVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouse inventory items frame: it allows to define the list of items to check and update qtys.</p>
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
public class InventoryItemsFrame extends InternalFrame {

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Form filterPanel = new Form();
	GridControl grid = new GridControl();
	GenericButton buttonImp = new GenericButton(new ImageIcon(ClientUtils.getImage("import.gif")));
	GenericButton buttonConfirm = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
	GenericButton buttonClose = new GenericButton(new ImageIcon(ClientUtils.getImage("lock.gif")));
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
	CodLookupColumn colItemCode = new CodLookupColumn();

	private java.util.List itemTypesList = null;

	/** grid data locator */
	private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	private InventoryVO inventoryVO = null;

	LabelControl labelItemType = new LabelControl();
	ComboBoxControl controlLOB = new ComboBoxControl();

	LookupController levelController = new LookupController();
	LookupServerDataLocator filterLevelDataLocator = new LookupServerDataLocator();
	TreeServerDataLocator filterTreeLevelDataLocator = new TreeServerDataLocator();

	CodLookupControl controlItemLevel = new CodLookupControl();
	TextControl controlDescr = new TextControl();
  ComboColumn colItemType = new ComboColumn();
  ComboColumn colProg = new ComboColumn();
  ComboColumn colRealProg = new ComboColumn();
  IntegerColumn colRealQty = new IntegerColumn();
  IntegerColumn colQty = new IntegerColumn();
  IntegerColumn colDelta = new IntegerColumn();
  JPanel toolbar = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;

	/** item code lookup data locator */
	LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

	/** item code lookup controller */
	LookupController itemController = new LookupController();

	LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
	TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

	private BigDecimal progressiveHIE01 = null;
	private InventoryListFrame parentFrame = null;


	public InventoryItemsFrame(InventoryListFrame parentFrame,InventoryVO inventoryVO) {
		this.parentFrame = parentFrame;
		this.inventoryVO = inventoryVO;
		try {
			jbInit();
			init();

	    grid.setController(new InventoryItemsController(this));
			grid.setGridDataLocator(gridDataLocator);
			gridDataLocator.setServerMethodName("loadInventoryItems");
			grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,inventoryVO.getCompanyCodeSys01WAR06());
			grid.getOtherGridParams().put(ApplicationConsts.ID,inventoryVO.getProgressiveWAR06());
			grid.setFunctionId("INVENTORY_LIST");

			setSize(750,550);
			setMinimumSize(new Dimension(750,550));



			Domain domainState = new Domain("DOMAIN_INVENTORY_STATE");
			domainState.addDomainPair(ApplicationConsts.OPENED,"opened");
			domainState.addDomainPair(ApplicationConsts.CONFIRMED,"items confirmed");
			domainState.addDomainPair(ApplicationConsts.CLOSED,"closed");
			domainState.addDomainPair(ApplicationConsts.IN_PROGRESS,"in progress");
			colState.setDomain(domainState);


			MDIFrame.add(this,true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Retrieve item types and fill in the item types combo box.
	 */
	private void init() {
		Response res = ClientUtils.getData("loadItemTypes",new GridParams());
		Domain d = new Domain("ITEM_TYPES");
		if (!res.isError()) {
			ItemTypeVO vo = null;
			itemTypesList = ((VOListResponse)res).getRows();
			for(int i=0;i<itemTypesList.size();i++) {
				vo = (ItemTypeVO)itemTypesList.get(i);
				d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
			}
		}
		controlLOB.setDomain(d);
		controlLOB.getComboBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
					progressiveHIE01 = null;
					controlDescr.setText(null);
			}
		});
		if (d.getDomainPairList().length>=1)
			controlLOB.getComboBox().setSelectedIndex(0);
		else
			controlLOB.getComboBox().setSelectedIndex(-1);


		// col item type...
  	colItemType.setDomain(d);
		colItemType.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==e.SELECTED) {
					InventoryItemVO vo = (InventoryItemVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
					vo.setItemCodeItm01WAR07(null);
					vo.setDescriptionSYS10(null);
					itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
					itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
					treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
					treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
					itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
					itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
				}
			}

		});


		// items level lookup...
		filterLevelDataLocator.setGridMethodName("loadHierarchy");
		filterLevelDataLocator.setValidationMethodName("");
		filterTreeLevelDataLocator.setServerMethodName("loadHierarchy");
		filterTreeLevelDataLocator.setNodeNameAttribute("descriptionSYS10");
		filterLevelDataLocator.setTreeDataLocator(filterTreeLevelDataLocator);
		filterLevelDataLocator.setNodeNameAttribute("descriptionSYS10");

		controlItemLevel.setLookupController(levelController);
		levelController.setCodeSelectionWindow(levelController.TREE_FRAME);
		levelController.setAllowTreeLeafSelectionOnly(false);
		levelController.setForm(filterPanel);
		levelController.setLookupDataLocator(filterLevelDataLocator);
		levelController.setFrameTitle("level");
		levelController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
		levelController.setAllColumnVisible(false);
		levelController.setVisibleColumn("descriptionSYS10", true);
		levelController.setPreferredWidthColumn("descriptionSYS10",200);
		levelController.setFramePreferedSize(new Dimension(400,400));
		levelController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				HierarchyLevelVO vo = (HierarchyLevelVO)controlItemLevel.getLookupController().getLookupVO();
				controlDescr.setText(vo==null?null:vo.getDescriptionSYS10());
				progressiveHIE01 = vo==null?null:vo.getProgressiveHIE01();
			}

			public void beforeLookupAction(ValueObject parentVO) {
				filterTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlLOB.getValue());
			}

			public void forceValidate() {}

		});



		// item code lookup...
		itemDataLocator.setGridMethodName("loadItems");
		itemDataLocator.setValidationMethodName("validateItemCode");
		itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
		itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);

		colItemCode.setLookupController(itemController);
		itemController.setLookupDataLocator(itemDataLocator);
		itemController.setFrameTitle("items");
		itemController.setShowErrorMessage(false);

		itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
		treeLevelDataLocator.setServerMethodName("loadHierarchy");
		itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
		itemDataLocator.setNodeNameAttribute("descriptionSYS10");

		itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
		itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01WAR07");
		itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01WAR07");
		itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

		itemController.setAllColumnVisible(false);
		itemController.setVisibleColumn("companyCodeSys01ITM01", true);
		itemController.setVisibleColumn("itemCodeITM01", true);
		itemController.setVisibleColumn("descriptionSYS10", true);
		itemController.setPreferredWidthColumn("descriptionSYS10", 200);
		itemController.setFramePreferedSize(new Dimension(650,500));
		itemController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

			public void beforeLookupAction(ValueObject parentVO) {
				InventoryItemVO vo = (InventoryItemVO)parentVO;
				vo.setCompanyCodeSys01WAR07(inventoryVO.getCompanyCodeSys01WAR06());
				itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR07());
				treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
				itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
				itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR07());
				itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);
				itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);
			}

			public void forceValidate() {}

		});



		// domain for locations...
		HashMap map = new HashMap();
		map.put(ApplicationConsts.PROGRESSIVE_HIE02,inventoryVO.getProgressiveHie02WAR01());
		res = ClientUtils.getData("loadLeaves",map);
		d = new Domain("WAR_LOCATIONS");
		if (!res.isError()) {
			java.util.List rows = null;
			HierarchyLevelVO vo = null;
			rows = ((VOListResponse)res).getRows();
			for(int i=0;i<rows.size();i++) {
				vo = (HierarchyLevelVO)rows.get(i);
				d.addDomainPair(vo.getProgressiveHIE01(),vo.getDescriptionSYS10());
			}
		}
		colProg.setDomain(d);
		colRealProg.setDomain(d);



	}


	public GridControl getGrid() {
		return grid;
	}


	public InventoryVO getInventory() {
		return inventoryVO;
	}


	private void jbInit() throws Exception {
		titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    filterPanel.setVOClassName("org.jallinone.items.java.GridItemVO");
		filterPanel.setFunctionId("INVENTORY_LIST");

		labelItemType.setText("item type");

		this.setTitle(ClientSettings.getInstance().getResources().getResource("inventory")+" - "+inventoryVO.getDescriptionWAR06());

		grid.setDeleteButton(deleteButton1);
		grid.setEditButton(editButton1);
    grid.setMaxSortedColumns(3);
		grid.setInsertButton(insertButton1);
		grid.setReloadButton(reloadButton1);
		grid.setSaveButton(saveButton1);
		grid.setValueObjectClassName("org.jallinone.warehouse.java.InventoryItemVO");
		buttonImp.setHorizontalTextPosition(SwingConstants.LEADING);
		buttonImp.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonImp.setText("import items");
		buttonImp.setToolTipText(ClientSettings.getInstance().getResources().getResource("import items that match the filtering conditions"));
		buttonImp.addActionListener(new InventoryItemsFrame_buttonImp_actionAdapter(this));

		buttonConfirm.setHorizontalTextPosition(SwingConstants.LEADING);
		buttonClose.setHorizontalTextPosition(SwingConstants.LEADING);


		buttonConfirm.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonConfirm.setText("confirm inventory");
		buttonConfirm.addActionListener(new InventoryItemsFrame_buttonConfirm_actionAdapter(this));

		buttonClose.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonClose.setText("close inventory");
		buttonClose.addActionListener(new InventoryItemsFrame_buttonClose_actionAdapter(this));


		gridPanel.setLayout(borderLayout1);
		buttonsPanel.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);
		colState.setColumnName("stateWAR07");
		colState.setPreferredWidth(80);
		colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnFilterable(true);
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(false);
    colDescr.setEditableOnInsert(false);
		colDescr.setPreferredWidth(200);
		colItemCode.setColumnName("itemCodeItm01WAR07");
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnSortable(true);
    colItemCode.setEditableOnEdit(false);
		colItemCode.setEditableOnInsert(true);
    colItemCode.setMinWidth(0);
    colItemCode.setPreferredWidth(70);
    controlItemLevel.setCodBoxVisible(false);
    colItemType.setColumnName("progressiveHie02WAR07");
    colItemType.setColumnFilterable(true);
    colItemType.setColumnSortable(true);
    colItemType.setEditableOnInsert(true);
    colItemType.setPreferredWidth(90);
    colProg.setColumnName("progressiveHie01WAR07");
    colProg.setColumnFilterable(true);
    colProg.setColumnSortable(true);
    colProg.setEditableOnInsert(true);
    colProg.setPreferredWidth(190);
    colRealProg.setColumnName("realProgressiveHie01WAR07");
    colRealProg.setColumnRequired(false);
    colRealProg.setEditableOnEdit(true);
    colRealProg.setEditableOnInsert(true);
    colRealProg.setPreferredWidth(190);
    colQty.setColumnName("qtyWAR07");
    colQty.setColumnFilterable(true);
    colQty.setColumnSortable(true);
    colQty.setEditableOnEdit(false);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(40);
    colRealQty.setColumnName("realQtyWAR07");
    colRealQty.setColumnFilterable(false);
    colRealQty.setColumnRequired(false);
    colRealQty.setEditableOnEdit(true);
    colRealQty.setEditableOnInsert(true);
    colRealQty.setPreferredWidth(60);
    colDelta.setColumnName("deltaQtyWAR07");
    colDelta.setColumnRequired(false);
    colDelta.setPreferredWidth(60);
    toolbar.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    gridPanel.setBorder(titledBorder2);
    titledBorder2.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("items to include in inventory"));
    controlDescr.setEnabled(false);
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    this.getContentPane().add(filterPanel,  BorderLayout.NORTH);
		filterPanel.setLayout(gridBagLayout1);
		this.getContentPane().add(gridPanel,  BorderLayout.CENTER);

    filterPanel.add(controlDescr,          new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemLevel,          new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlLOB,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    filterPanel.add(labelItemType,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(toolbar,     new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		if (inventoryVO.getStateWAR06().equals(ApplicationConsts.OPENED))
			toolbar.add(buttonImp, null);
		if (inventoryVO.getStateWAR06().equals(ApplicationConsts.OPENED))
			toolbar.add(buttonConfirm, null);

	  if (inventoryVO.getStateWAR06().equals(ApplicationConsts.CONFIRMED))
			toolbar.add(buttonClose, null);

		gridPanel.add(buttonsPanel, BorderLayout.NORTH);
		gridPanel.add(grid,  BorderLayout.CENTER);

		if (!inventoryVO.getStateWAR06().equals(ApplicationConsts.CLOSED)) {
			buttonsPanel.add(insertButton1, null);
			buttonsPanel.add(editButton1, null);
			buttonsPanel.add(reloadButton1, null);
			buttonsPanel.add(saveButton1, null);
			buttonsPanel.add(deleteButton1, null);
		}

    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colProg, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colRealProg, null);
    grid.getColumnContainer().add(colRealQty, null);
    grid.getColumnContainer().add(colDelta, null);
    grid.getColumnContainer().add(colState, null);

	}



	void buttonImp_actionPerformed(ActionEvent e) {
		new Thread() {
			public void run() {
				try {
					Response res = ClientUtils.getData("importInventoryItems",new Object[]{
				    getInventory(),
						controlLOB.getValue(),
						progressiveHIE01
	        });
					grid.reloadData();

	        if (res.isError())
						OptionPane.showMessageDialog(InventoryItemsFrame.this,res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					else
						OptionPane.showMessageDialog(InventoryItemsFrame.this,"items import completed","Attention",JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}


	void buttonConfirm_actionPerformed(ActionEvent e) {
		try {
			Response res = ClientUtils.getData("confirmInventory",new Object[]{
					 getInventory().getCompanyCodeSys01WAR06(),
					 getInventory().getWarehouseCodeWar01WAR06(),
					 getInventory().getProgressiveWAR06()
			});
			grid.reloadData();
			parentFrame.getGrid().reloadCurrentBlockOfData();
			if (res.isError())
				OptionPane.showMessageDialog(InventoryItemsFrame.this,res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			else {
				inventoryVO.setStateWAR06(ApplicationConsts.CONFIRMED);
				toolbar.remove(buttonClose);
				toolbar.add(buttonClose, null);
				toolbar.revalidate();
				toolbar.repaint();

				OptionPane.showMessageDialog(InventoryItemsFrame.this,"items confirmation completed","Attention",JOptionPane.INFORMATION_MESSAGE);

			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	void buttonClose_actionPerformed(ActionEvent e) {
		new Thread() {
			public void run() {
				try {
					Response res = ClientUtils.getData("closeInventory",new Object[]{
						getInventory().getCompanyCodeSys01WAR06(),
						getInventory().getWarehouseCodeWar01WAR06(),
						getInventory().getProgressiveWAR06(),
						getInventory().getItemTypeWAR06(),
						getInventory().getDescriptionWAR06()
					});
					grid.reloadData();
					parentFrame.getGrid().reloadCurrentBlockOfData();
					if (res.isError())
						OptionPane.showMessageDialog(InventoryItemsFrame.this,res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					else {
						OptionPane.showMessageDialog(InventoryItemsFrame.this,"inventory closed","Attention",JOptionPane.INFORMATION_MESSAGE);
						InventoryItemsFrame.this.closeFrame();
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}
  public GenericButton getButtonConfirm() {
    return buttonConfirm;
  }



}

class InventoryItemsFrame_buttonImp_actionAdapter implements java.awt.event.ActionListener {
	InventoryItemsFrame adaptee;

	InventoryItemsFrame_buttonImp_actionAdapter(InventoryItemsFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonImp_actionPerformed(e);
	}
}


class InventoryItemsFrame_buttonConfirm_actionAdapter implements java.awt.event.ActionListener {
	InventoryItemsFrame adaptee;

	InventoryItemsFrame_buttonConfirm_actionAdapter(InventoryItemsFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonConfirm_actionPerformed(e);
	}
}


class InventoryItemsFrame_buttonClose_actionAdapter implements java.awt.event.ActionListener {
	InventoryItemsFrame adaptee;

	InventoryItemsFrame_buttonClose_actionAdapter(InventoryItemsFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonClose_actionPerformed(e);
	}
}
