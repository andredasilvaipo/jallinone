package org.jallinone.items.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.items.java.DetailItemVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.math.BigDecimal;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for item form.</p>
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
public class ItemController extends CompanyFormController {

  /** items frame */
  private ItemFrame frame = null;

  /** parent frame */
  private ItemsFrame parentFrame = null;

  /** item pk */
  private ItemPK pk = null;


  public ItemController(ItemsFrame parentFrame,ItemPK pk,boolean productsOnly) {
    this.parentFrame = parentFrame;
    this.pk = pk;
    this.frame = new ItemFrame(this,productsOnly);
    MDIFrame.add(frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (pk!=null) {
      frame.getFormPanel().setMode(Consts.READONLY);
      frame.getFormPanel().executeReload();
    }
    else {
      frame.getFormPanel().insert();
    }
  }


  public ItemPK getPK() {
    return pk;
  }


  /**
   * Callback method invoked before going in INSERT mode.
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);
    if (ok) {
      frame.getSmallImage().setImage(null);
      frame.getLargeImage().setImage(null);
      frame.getDiscountsGrid().clearData();
      frame.getPricesGrid().clearData();

      frame.getBookedItemsPanel().setEnabled(false);
      frame.getOrderedItemsPanel().setEnabled(false);
      frame.getBookedItemsPanel().getGrid().clearData();
      frame.getOrderedItemsPanel().getGrid().clearData();

      frame.getBomTabbedPane().getComponentsGrid().clearData();
      frame.getBomTabbedPane().getAltCompsGrid().clearData();
      frame.getBomTabbedPane().setCompButtonsEnabled(false);
      frame.getBomTabbedPane().setAltButtonsEnabled(false);
      frame.getBomTabbedPane().getExplosionPanel().clearTree();
      frame.getBomTabbedPane().getImplosionPanel().clearTree();

			frame.getItemSparePartsPanel().init(null,true);

      frame.getSupplierPrices().getPricesGrid().clearData();
      frame.getSupplierPrices().setButtonsEnabled(false);

      frame.setButtonsEnabled(false);

      frame.getItemVariantsPanel().clearData();
      frame.getVariantBarcodesPanel().clearData();
      frame.getVariantMinStockPanel().clearData();
      frame.setTitle(ClientSettings.getInstance().getResources().getResource("item detail"));

    }
    return ok;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    // since this method could be invoked also when selecting another row on the linked grid,
   // the pk attribute must be recalculated from the grid...
   int row = -1;
	 if (parentFrame!=null)
		 parentFrame.getGrid().getSelectedRow();
   if (row!=-1) {
     GridItemVO gridVO = (GridItemVO)parentFrame.getGrid().getVOListTableModel().getObjectForRow(row);
     pk = new ItemPK(gridVO.getCompanyCodeSys01ITM01(),gridVO.getItemCodeITM01());
   }

    return ClientUtils.getData("loadItem",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    DetailItemVO vo = (DetailItemVO)newPersistentObject;
    if (vo.getSerialNumberRequiredITM01()!=null &&
        vo.getSerialNumberRequiredITM01().booleanValue() &&
        vo.getManufactureCodePro01ITM01()!=null) {
      return new ErrorResponse("a product cannot require serial number definition");
    }
    Response res = ClientUtils.getData("insertItem",vo);
    if (!res.isError()) {
      vo = (DetailItemVO)((VOResponse)res).getVo();
      pk = new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01());
      if (parentFrame!=null) {
//        parentFrame.getGrid().reloadData();
      }

      frame.getDiscountsGrid().getOtherGridParams().put(
          ApplicationConsts.ITEM_PK,
          pk
      );
      frame.getDiscountsGrid().reloadData();
      frame.getPricesGrid().getOtherGridParams().put(ApplicationConsts.ITEM,vo);
      frame.getPricesGrid().reloadData();
      frame.setButtonsEnabled(true);

      frame.getSupplierPrices().getPricesGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);
      frame.getSupplierPrices().setButtonsEnabled(true);
      frame.getSupplierPrices().getPricesGrid().reloadData();

			frame.getItemSparePartsPanel().init(vo,false);

      frame.getBookedItemsPanel().setEnabled(true);
      frame.getOrderedItemsPanel().setEnabled(true);
      frame.getBookedItemsPanel().getGrid().reloadData();
      frame.getOrderedItemsPanel().getGrid().reloadData();
      frame.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01()));
      frame.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01()));
      frame.getDocsPanel().setItemVO(vo);
//      frame.getItemVariantsPanel().setContent(vo,frame.getVariantsNames());


      frame.getBomTabbedPane().getComponentsGrid().getOtherGridParams().put(
          ApplicationConsts.ITEM_PK,
          pk
      );
      frame.getBomTabbedPane().getExplosionPanel().getTreeDataLocator().getTreeNodeParams().put(
          ApplicationConsts.ITEM_PK,
          pk
      );
      frame.getBomTabbedPane().getImplosionPanel().getTreeDataLocator().getTreeNodeParams().put(
          ApplicationConsts.ITEM_PK,
          pk
      );
      frame.getBomTabbedPane().getAltCompsGrid().getOtherGridParams().put(
          ApplicationConsts.ITEM_PK,
          pk
      );
      frame.getBomTabbedPane().getComponentsGrid().reloadData();
      frame.getBomTabbedPane().getAltCompsGrid().reloadData();
      frame.getBomTabbedPane().setCompButtonsEnabled(true);
      frame.getBomTabbedPane().setAltButtonsEnabled(true);

    }
    return res;
  }

  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    DetailItemVO vo = (DetailItemVO)persistentObject;
    if (vo.getSerialNumberRequiredITM01()!=null &&
        vo.getSerialNumberRequiredITM01().booleanValue() &&
        vo.getManufactureCodePro01ITM01()!=null) {
      return new ErrorResponse("a product cannot require serial number definition");
    }

    if (frame.getBomTabbedPane().getAltCompsGrid().getVOListTableModel().getRowCount()>0 &&
        vo.getManufactureCodePro01ITM01()!=null) {
      return new ErrorResponse("you cannot define this item as a product because there are alternative components defined.");
    }

    Response res = ClientUtils.getData("updateItem",new ValueObject[]{oldPersistentObject,vo});
    if (!res.isError()) {
      if (parentFrame!=null) {
 //       parentFrame.getGrid().reloadData();
      }
    }
    return res;
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ArrayList pks = new ArrayList();
    DetailItemVO vo = (DetailItemVO)persistentObject;
    ItemPK pk = new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01());
    pks.add(pk);
    Response res = ClientUtils.getData("deleteItems",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
      frame.getDiscountsGrid().clearData();
      frame.getPricesGrid().clearData();

      frame.getBookedItemsPanel().setEnabled(false);
      frame.getOrderedItemsPanel().setEnabled(false);
      frame.getBookedItemsPanel().getGrid().clearData();
      frame.getOrderedItemsPanel().getGrid().clearData();
      frame.getDocsPanel().getDocsGrid().clearData();
      frame.getItemVariantsPanel().clearData();
      frame.getVariantBarcodesPanel().clearData();
      frame.getVariantMinStockPanel().clearData();
			frame.getItemSparePartsPanel().init(null,false);

      frame.getSupplierPrices().setButtonsEnabled(false);
      frame.getSupplierPrices().getPricesGrid().clearData();

      frame.getBomTabbedPane().getComponentsGrid().clearData();
      frame.getBomTabbedPane().getAltCompsGrid().clearData();
      frame.getBomTabbedPane().getExplosionPanel().clearTree();
      frame.getBomTabbedPane().getImplosionPanel().clearTree();
    }
    return res;
  }


  public boolean beforeEditData(Form form) {
    boolean ok = super.beforeEditData(form);
		if (ok) {
			DetailItemVO vo = (DetailItemVO) form.getVOModel().getValueObject();
			if (vo.getSheetCodeItm25ITM01()==null)
				frame.getItemSparePartsPanel().init(vo,true);
		}
		return ok;
	}



  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailItemVO vo = (DetailItemVO)persistentObject;
    vo.setStartDateITM01(new java.sql.Date(System.currentTimeMillis()));
    vo.setVersionITM01(new BigDecimal(1));
    vo.setRevisionITM01(new BigDecimal(1));

		if (parentFrame != null) {
			HierarchyLevelVO levelVO = (HierarchyLevelVO)parentFrame.getHierarTreePanel().getSelectedNode().getUserObject();
			vo.setProgressiveHie01ITM01(levelVO.getProgressiveHIE01());
			vo.setProgressiveHie02ITM01(levelVO.getProgressiveHie02HIE01());
			vo.setLevelDescriptionSYS10(levelVO.getDescriptionSYS10());
			vo.setProgressiveHie01HIE02(levelVO.getProgressiveHie01HIE02());
			vo.setCompanyCodeSys01ITM01(parentFrame.getSelectedItemType().getCompanyCodeSys01ITM02());
		}


    frame.setVariants(vo.getCompanyCodeSys01ITM01());

  }


  /**
   * Callback method called when inserting data is SUCCESSFULLY completed.
   */
  public void afterInsertData() {
    frame.getDocsPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK, pk);
    frame.getDocsPanel().getDocsGrid().reloadData();
  }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }
  public ItemsFrame getParentFrame() {
    return parentFrame;
  }


}
