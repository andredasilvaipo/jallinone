package org.jallinone.items.spareparts.client;

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
import javax.swing.border.*;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.spareparts.java.ItemSparePartVO;
import org.jallinone.items.client.ItemFrame;
import org.jallinone.items.client.ItemController;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.items.spareparts.java.ItemSheetVO;
import org.jallinone.items.java.DetailItemVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import java.util.HashMap;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.lookup.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: spare parts catalogoue frame, used to
 * - select an items
 * - show the list of item's spare parts
 * - show the spare parts catalogue, filtered by the current root sheet (related to selected item)
 * </p>
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
public class SparePartsCatalogueFrame extends InternalFrame {

	GridControl gridItemSpareParts = new GridControl();

	ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel filterPanel = new JPanel();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	 JTabbedPane tabpanel = new JTabbedPane();
	TextColumn colItemDescr = new TextColumn();
	TextColumn colItemCode = new TextColumn();
	LabelControl labelItemCode = new LabelControl();
	CodLookupControl controlItemCode = new CodLookupControl();
	TextControl controlDescr = new TextControl();

	ComboBoxControl controlItemType = new ComboBoxControl();

	/** item code lookup data locator */
	LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

	/** item code lookup controller */
	LookupController itemController = new LookupController();

	LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
	TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

	private ItemSheetsGridPanel sheets;

	private	java.util.List itemTypes = null;

	private SparePartsCatalogueCallbacks callbacks = null;


	public SparePartsCatalogueFrame(boolean readOnly) {
		sheets = new ItemSheetsGridPanel(readOnly,gridItemSpareParts);
		gridItemSpareParts.setAutoLoadData(false);
		gridItemSpareParts.setController(new GridController() {

			public void doubleClick(int rowNumber,ValueObject persistentObject) {
				ItemSparePartVO vo = (ItemSparePartVO)persistentObject;
				if (callbacks!=null)
					callbacks.sparePartDoubleClick(vo.getProgressiveHie02ITM01(),vo.getCompanyCodeSys01ITM28(),vo.getItemCodeItm01ITM28(),vo.getDescriptionSYS10());
				else
					new ItemController(null,new ItemPK(vo.getCompanyCodeSys01ITM28(),vo.getItemCodeItm01ITM28()),false);
			}

		});
		gridItemSpareParts.setGridDataLocator(gridDataLocator);
		gridDataLocator.setServerMethodName("loadItemSpareParts");
		try {
			init();
			jbInit();

	    setSize(750,600);
			MDIFrame.add(this,true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	public final void setItem(BigDecimal progressiveHie02ITM01,String companyCode,String itemCode) {
		try {
			controlItemType.setValue(progressiveHie02ITM01);
			controlItemCode.setValue(itemCode);
			controlItemCode.getLookupController().getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
			controlItemCode.getLookupController().getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
			controlItemCode.validateCode(itemCode);
		}
		catch (Exception ex) {
		}
	}


	private void init() {
		Response res = ClientUtils.getData("loadItemTypes",new GridParams());
		final Domain d = new Domain("ITEM_TYPES");
		if (!res.isError()) {
			ItemTypeVO vo = null;
			itemTypes = ((VOListResponse)res).getRows();
			for(int i=0;i<itemTypes.size();i++) {
				vo = (ItemTypeVO)itemTypes.get(i);
				d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
			}
		}
		controlItemType.setDomain(d);
		controlItemType.getComboBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==e.SELECTED) {
					try {
						int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
						Object selValue = d.getDomainPairList()[selIndex].getCode();
						ItemTypeVO vo = (ItemTypeVO)itemTypes.get(selIndex);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());


						controlItemCode.setValue(null);
						controlItemCode.validateCode(null);
					}
					catch (Exception ex) {
					}

				}
			}
		});

		// item code lookup...
		itemDataLocator.setGridMethodName("loadItems");
		itemDataLocator.setValidationMethodName("validateItemCode");

		controlItemCode.setLookupController(itemController);
		itemController.setLookupDataLocator(itemDataLocator);
		itemController.setFrameTitle("items");

		itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
		treeLevelDataLocator.setServerMethodName("loadHierarchy");
		itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
		itemDataLocator.setNodeNameAttribute("descriptionSYS10");

		itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");

		itemController.setAllColumnVisible(false);
		itemController.setVisibleColumn("companyCodeSys01ITM01", true);
		itemController.setVisibleColumn("itemCodeITM01", true);
		itemController.setVisibleColumn("descriptionSYS10", true);
		itemController.setPreferredWidthColumn("descriptionSYS10", 200);
		itemController.setFramePreferedSize(new Dimension(650,500));
		itemController.setShowErrorMessage(false);
		itemController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				GridItemVO vo = (GridItemVO)itemController.getLookupVO();
				if (vo==null) {
					controlItemCode.setValue(null);
					controlDescr.setValue(null);
					gridItemSpareParts.clearData();
					sheets.clearData();
				}
				else {
					controlItemCode.setValue(vo.getItemCodeItm01());
					controlDescr.setValue(vo.getDescriptionSYS10());
					gridItemSpareParts.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01(),vo.getItemCodeITM01()));
					gridItemSpareParts.reloadData();
					sheets.clearData();
					tabpanel.setSelectedIndex(0);
					tabpanel.setEnabledAt(1,false);

					String sheetCode = vo.getSheetCodeItm25ITM01();
					if (sheetCode!=null) {
						LookupValidationParams pars = new LookupValidationParams(sheetCode,new HashMap());
						pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01());
						Response res = ClientUtils.getData("validateSheetCode",pars);

						if (res.isError()) {
							OptionPane.showMessageDialog(MDIFrame.getInstance(),
																					 res.getErrorMessage(),
																					 "Error", JOptionPane.ERROR_MESSAGE);
						}
						else {
							tabpanel.setEnabledAt(1,true);
							java.util.List rows = ((VOListResponse)res).getRows();
							if (rows.size()==1) {
								// specified item has a model associated...
								ItemSheetVO sheetVO = (ItemSheetVO)rows.get(0);
								sheets.addLink(sheetVO);
							}
						}
					}
				}
			}

			public void beforeLookupAction(ValueObject gridVO) {
//				itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
//				itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
			}

			public void forceValidate() {}

		});
	}



	private void jbInit() throws Exception {

		controlItemType.setCanCopy(true);
		controlItemType.setEnabledOnEdit(false);

		gridItemSpareParts.setValueObjectClassName("org.jallinone.items.spareparts.java.ItemSparePartVO");
		gridItemSpareParts.setAllowColumnsPermission(false);
		gridItemSpareParts.setAllowColumnsProfile(false);
		gridItemSpareParts.setMaxSortedColumns(1);
		filterPanel.setLayout(gridBagLayout2);
		colItemDescr.setColumnName("descriptionSYS10");
		colItemDescr.setPreferredWidth(250);
		colItemCode.setColumnName("itemCodeItm01ITM28");

		this.setTitle(ClientSettings.getInstance().getResources().getResource("spare parts catalogue"));

		this.getContentPane().setLayout(new BorderLayout());
		labelItemCode.setLabel("itemCodeITM01");
		controlDescr.setEnabled(false);
		controlItemCode.setMaxCharacters(20);
		this.getContentPane().add(filterPanel, BorderLayout.NORTH);
		this.getContentPane().add(tabpanel, BorderLayout.CENTER);
		tabpanel.add(ClientSettings.getInstance().getResources().getResource("item spare parts"), gridItemSpareParts);
		tabpanel.add(ClientSettings.getInstance().getResources().getResource("sheets"), sheets);
		tabpanel.setEnabledAt(1,false);

		gridItemSpareParts.getColumnContainer().add(colItemCode, null);
		gridItemSpareParts.getColumnContainer().add(colItemDescr, null);

		filterPanel.add(labelItemCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(controlItemType,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		filterPanel.add(controlItemCode,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(controlDescr,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	}


  public void setCallbacks(SparePartsCatalogueCallbacks callbacks) {
    this.callbacks = callbacks;
		sheets.setCallbacks(callbacks);
  }



}
