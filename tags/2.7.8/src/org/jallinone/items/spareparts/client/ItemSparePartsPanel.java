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
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.form.model.client.ValueChangeListener;
import org.openswing.swing.form.model.client.ValueChangeEvent;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is a panel within item detail frame, used to
 * - select a root sheet to link to the item
 * - show the list of item's spare parts
 * - shoe the spare parts catalogue, filtered by the current root sheet
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
public class ItemSparePartsPanel extends JPanel {

	GridControl gridItemSpareParts = new GridControl();

  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
	 JTabbedPane tabpanel = new JTabbedPane();
  TextColumn colItemDescr = new TextColumn();
  TextColumn colItemCode = new TextColumn();
  LabelControl labelRootSheet = new LabelControl();
  CodLookupControl controlRootSheet = new CodLookupControl();
  TextControl controlDescr = new TextControl();

	LookupController rootSheetController = new LookupController();
	LookupServerDataLocator rootSheetDataLocator = new LookupServerDataLocator();

	private DetailItemVO itemVO = null;
	private ItemSheetsGridPanel sheets = new ItemSheetsGridPanel(true,null);
//	private SheetCodeChangeListener listener = null;
	private Form itemFrameForm = null;

  public ItemSparePartsPanel(Form itemFrameForm) {
		this.itemFrameForm = itemFrameForm;
		gridItemSpareParts.setAutoLoadData(false);
    gridItemSpareParts.setController(new GridController() {

			public void doubleClick(int rowNumber,ValueObject persistentObject) {
				ItemSparePartVO vo = (ItemSparePartVO)persistentObject;
				new ItemController(null,new ItemPK(vo.getCompanyCodeSys01ITM28(),vo.getItemCodeItm01ITM28()),false);
			}

		});
    gridItemSpareParts.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadItemSpareParts");
    try {
			init();
      jbInit();
			itemFrameForm.bind(controlRootSheet);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


	private void init() {
		// root sheets lookup...
		rootSheetDataLocator.setGridMethodName("loadItemSheets");
		rootSheetDataLocator.setValidationMethodName("validateSheetCode");

		controlRootSheet.setLookupController(rootSheetController);
		rootSheetController.setLookupDataLocator(rootSheetDataLocator);
		rootSheetController.setFrameTitle("item models");
		rootSheetController.setLookupValueObjectClassName("org.jallinone.items.spareparts.java.ItemSheetVO");
		rootSheetController.setAllColumnVisible(false);
		rootSheetController.setVisibleColumn("sheetCodeITM25", true);
		rootSheetController.setVisibleColumn("descriptionSYS10", true);
		rootSheetController.setSortedColumn("sheetCodeITM25","ASC");
		rootSheetController.setPreferredWidthColumn("descriptionSYS10",250);
		rootSheetController.setFramePreferedSize(new Dimension(370,500));
		rootSheetController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				ItemSheetVO vo = (ItemSheetVO)rootSheetController.getLookupVO();
				sheets.clearData();
				if (vo.getSheetCodeITM25()==null) {
					controlRootSheet.setValue(null);
					controlDescr.setValue(null);
					gridItemSpareParts.clearData();
				}
				else {
					controlRootSheet.setValue(vo.getSheetCodeITM25());
					controlDescr.setValue(vo.getDescriptionSYS10());
					gridItemSpareParts.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(itemVO.getCompanyCodeSys01(),itemVO.getItemCodeITM01()));
					gridItemSpareParts.reloadData();

         	sheets.addLink(vo);
				}
			}

			public void beforeLookupAction(ValueObject parentVO) {
				if (itemVO!=null) {
					rootSheetDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
					rootSheetDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
					rootSheetDataLocator.getLookupFrameParams().put(ApplicationConsts.LEVEL,new BigDecimal(1));
					rootSheetDataLocator.getLookupValidationParameters().put(ApplicationConsts.LEVEL,new BigDecimal(1));
				}
			}

			public void forceValidate() {}

		});



	}


  public void init(DetailItemVO itemVO,boolean enabled) {
		this.itemVO = itemVO;
		controlRootSheet.setEnabled(enabled);

//	  if (listener==null) {
//			listener = new SheetCodeChangeListener();
//			itemFrameForm.getVOModel().addValueChangeListener(listener);

			if (itemVO!=null) {
				try {
					controlRootSheet.validateCode(itemVO.getSheetCodeItm25ITM01());
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}

//		}
  }


  private void jbInit() throws Exception {
   	controlRootSheet.setAttributeName("sheetCodeItm25ITM01");
		gridItemSpareParts.setValueObjectClassName("org.jallinone.items.spareparts.java.ItemSparePartVO");
		gridItemSpareParts.setAllowColumnsPermission(false);
		gridItemSpareParts.setAllowColumnsProfile(false);
		gridItemSpareParts.setMaxSortedColumns(1);
    filterPanel.setLayout(gridBagLayout2);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setPreferredWidth(350);
    colItemCode.setColumnName("itemCodeItm01ITM28");
		colItemCode.setPreferredWidth(110);
    colItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colItemCode.setSortingOrder(1);

	  this.setLayout(new BorderLayout());
		labelRootSheet.setLabel("item model");
    controlDescr.setEnabled(false);
    //controlRootSheet.setEnabled(false);
    controlRootSheet.setMaxCharacters(20);
    this.add(filterPanel, BorderLayout.NORTH);
    filterPanel.add(labelRootSheet,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(tabpanel, BorderLayout.CENTER);
		tabpanel.add(ClientSettings.getInstance().getResources().getResource("item spare parts"), gridItemSpareParts);
		tabpanel.add(ClientSettings.getInstance().getResources().getResource("sheets"), sheets);

    gridItemSpareParts.getColumnContainer().add(colItemCode, null);
    gridItemSpareParts.getColumnContainer().add(colItemDescr, null);
    filterPanel.add(controlRootSheet,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDescr,   new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }



//	 class SheetCodeChangeListener implements ValueChangeListener {
//
//		public void valueChanged(ValueChangeEvent e) {
//			if (e.getAttributeName().equals("sheetCodeItm25ITM01") &&
//					itemVO!=null &&
//					itemVO.getSheetCodeItm25ITM01()!=null) {
//				try {
//					controlRootSheet.validateCode(itemVO.getSheetCodeItm25ITM01());
//				}
//				catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//		}
//
//	};



}
