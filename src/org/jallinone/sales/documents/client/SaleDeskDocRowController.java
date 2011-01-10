package org.jallinone.sales.documents.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.warehouse.documents.client.SerialNumberDialog;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.items.java.ItemPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for desk selling row detail panel.</p>
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
public class SaleDeskDocRowController extends CompanyFormController {

  /** desk selling rows panel */
  private SaleDeskDocRowsGridPanel panel = null;

  /** desk selling row pk */
  private SaleDocRowPK pk = null;


  public SaleDeskDocRowController(SaleDeskDocRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    if (pk==null)
      return new VOResponse(new DetailSaleDocRowVO());
    else
      return ClientUtils.getData("loadSaleDocRow",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    if (panel.isSerialNumberRequired() &&
        !promptSerialNumbers(panel.getVariantsPanel().getCells(),panel.getVariantsPanel().getVariantsMatrixVO(),(DetailSaleDocRowVO)newPersistentObject)) {
      return new ErrorResponse("insert not allowed until serial numbers are not defined");
    }

    Response res = null;

    if (panel.getVariantsPanel().getVariantsMatrixVO()==null) {
      // no variants...
      res = ClientUtils.getData("insertSaleDocRow",newPersistentObject);
    }
    else {
      // check for already existing item for qty 1 and serial num enabled...
      if (panel.isSerialNumberRequired()) {
        DetailSaleDocRowVO itemVO = (DetailSaleDocRowVO)newPersistentObject;

        for(int i=0;i<panel.getGrid().getVOListTableModel().getRowCount();i++) {
          GridSaleDocRowVO vo =(GridSaleDocRowVO)panel.getGrid().getVOListTableModel().getObjectForRow(i);
          if (vo.getItemCodeItm01DOC02().equals(itemVO.getItemCodeItm01DOC02()) &&
              vo.getVariantCodeItm11DOC02().equals(itemVO.getVariantCodeItm11DOC02()) &&
              vo.getVariantCodeItm12DOC02().equals(itemVO.getVariantCodeItm12DOC02()) &&
              vo.getVariantCodeItm13DOC02().equals(itemVO.getVariantCodeItm13DOC02()) &&
              vo.getVariantCodeItm14DOC02().equals(itemVO.getVariantCodeItm14DOC02()) &&
              vo.getVariantCodeItm15DOC02().equals(itemVO.getVariantCodeItm15DOC02()) &&
              vo.getVariantTypeItm06DOC02().equals(itemVO.getVariantTypeItm06DOC02()) &&
              vo.getVariantTypeItm07DOC02().equals(itemVO.getVariantTypeItm07DOC02()) &&
              vo.getVariantTypeItm08DOC02().equals(itemVO.getVariantTypeItm08DOC02()) &&
              vo.getVariantTypeItm09DOC02().equals(itemVO.getVariantTypeItm09DOC02()) &&
              vo.getVariantTypeItm10DOC02().equals(itemVO.getVariantTypeItm10DOC02())) {

            // load old row..
            SaleDocRowPK pk = new SaleDocRowPK(
              vo.getCompanyCodeSys01DOC02(),
              vo.getDocTypeDOC02(),vo.getDocYearDOC02(),
              vo.getDocNumberDOC02(),
              vo.getItemCodeItm01DOC02(),
              vo.getVariantTypeItm06DOC02(),
              vo.getVariantCodeItm11DOC02(),
              vo.getVariantTypeItm07DOC02(),
              vo.getVariantCodeItm12DOC02(),
              vo.getVariantTypeItm08DOC02(),
              vo.getVariantCodeItm13DOC02(),
              vo.getVariantTypeItm09DOC02(),
              vo.getVariantCodeItm14DOC02(),
              vo.getVariantTypeItm10DOC02(),
              vo.getVariantCodeItm15DOC02()
            );
            res = ClientUtils.getData("loadSaleDocRow",pk);
            if (res.isError())
              return res;
            DetailSaleDocRowVO oldVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
            DetailSaleDocRowVO newVO = (DetailSaleDocRowVO)oldVO.clone();
            newVO.setQtyDOC02(oldVO.getQtyDOC02().add(new BigDecimal(1)));
            newVO.setSerialNumbers(itemVO.getSerialNumbers());
            newVO.getSerialNumbers().addAll(oldVO.getSerialNumbers());

            // update qty...
            res = ClientUtils.getData(
              "updateSaleDocRow",
              new ValueObject[]{
                oldVO,
                newVO
              }
            );
            return res;
          }
        }
      }


      // the item has variants...
      res = ClientUtils.getData(
        "insertSaleDocRows",
        new Object[]{
          newPersistentObject,
          panel.getVariantsPanel().getVariantsMatrixVO(),
          panel.getVariantsPanel().getCells(),
          panel.getParentVO().getDecimalsREG03()
        }
      );
    }


    //Response res = ClientUtils.getData("insertSaleDocRow",newPersistentObject);
    if (!res.isError()) {
      DetailSaleDocRowVO vo = (DetailSaleDocRowVO)((VOResponse)res).getVo();
      pk = new SaleDocRowPK(
          vo.getCompanyCodeSys01DOC02(),
          vo.getDocTypeDOC02(),
          vo.getDocYearDOC02(),
          vo.getDocNumberDOC02(),
          vo.getItemCodeItm01DOC02(),
          vo.getVariantTypeItm06DOC02(),
          vo.getVariantCodeItm11DOC02(),
          vo.getVariantTypeItm07DOC02(),
          vo.getVariantCodeItm12DOC02(),
          vo.getVariantTypeItm08DOC02(),
          vo.getVariantCodeItm13DOC02(),
          vo.getVariantTypeItm09DOC02(),
          vo.getVariantCodeItm14DOC02(),
          vo.getVariantTypeItm10DOC02(),
          vo.getVariantCodeItm15DOC02()

      );
    }
    return res;
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    panel.getGrid().reloadData();
    panel.getHeaderPanel().setMode(Consts.READONLY);
    panel.getHeaderPanel().executeReload();
    panel.getDesks().reloadCurrentBlockOfData();
    panel.getFrame().enabledConfirmButton();
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    if (error)
      return;

    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)panel.getDetailPanel().getVOModel().getValueObject();

    panel.getControlBarCode().setValue(null);
    panel.getControlSN().setValue(null);

    if (vo==null || vo.getCompanyCodeSys01DOC02()==null)
      return;

    panel.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01DOC02(),vo.getItemCodeItm01DOC02()));
    panel.getBookedItemsPanel().getControlItemType().setValue(vo.getProgressiveHie02DOC02());
    panel.getBookedItemsPanel().getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
    panel.getBookedItemsPanel().getControlItemCode().getLookupController().getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC02());
    panel.getBookedItemsPanel().getControlItemCode().getLookupController().getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC02());
    panel.getBookedItemsPanel().getControlItemCode().getLookupController().forceValidate();
    panel.getBookedItemsPanel().getGrid().reloadData();

    panel.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01DOC02(),vo.getItemCodeItm01DOC02()));
    panel.getOrderedItemsPanel().getControlItemType().setValue(vo.getProgressiveHie02DOC02());
    panel.getOrderedItemsPanel().getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
    panel.getOrderedItemsPanel().getControlItemCode().getLookupController().getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC02());
    panel.getOrderedItemsPanel().getControlItemCode().getLookupController().getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC02());
    panel.getOrderedItemsPanel().getControlItemCode().getLookupController().forceValidate();
    panel.getOrderedItemsPanel().getGrid().reloadData();

  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    if (panel.isSerialNumberRequired() &&
        !promptSerialNumbers(panel.getVariantsPanel().getCells(),panel.getVariantsPanel().getVariantsMatrixVO(),(DetailSaleDocRowVO)persistentObject)) {
      return new ErrorResponse("update not allowed until serial numbers are not defined");
    }

    Response res = ClientUtils.getData("updateSaleDocRow",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getDesks().reloadCurrentBlockOfData();
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
    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)persistentObject;

    pks.add(pk);
    Response res = ClientUtils.getData("deleteSaleDocRows",pks);
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getDesks().reloadCurrentBlockOfData();

    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)persistentObject;
    DetailSaleDocVO parentVO = panel.getParentVO();
    Calendar cal = Calendar.getInstance();
    vo.setCompanyCodeSys01DOC02(parentVO.getCompanyCodeSys01DOC01());
    vo.setDocYearDOC02(parentVO.getDocYearDOC01());
    vo.setDocTypeDOC02(parentVO.getDocTypeDOC01());
    vo.setDocNumberDOC02(parentVO.getDocNumberDOC01());
    vo.setDeliveryDateDOC02(new java.sql.Date(System.currentTimeMillis()));
    vo.setCurrencyCodeReg03DOC01(panel.getParentVO().getCurrencyCodeReg03DOC01());
    vo.setProgressiveHie01DOC02(panel.getParentVO().getProgressiveHie01HIE02());
		vo.setVariantCodeItm11DOC02("*");
		vo.setVariantCodeItm12DOC02("*");
		vo.setVariantCodeItm13DOC02("*");
		vo.setVariantCodeItm14DOC02("*");
		vo.setVariantCodeItm15DOC02("*");
		vo.setVariantTypeItm06DOC02("*");
		vo.setVariantTypeItm07DOC02("*");
		vo.setVariantTypeItm08DOC02("*");
		vo.setVariantTypeItm09DOC02("*");
		vo.setVariantTypeItm10DOC02("*");
  }


  public void setPk(SaleDocRowPK pk) {
    this.pk = pk;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    if (!super.beforeEditData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    panel.getControlBarCode().setValue(null);
    panel.getControlSN().setValue(null);

    if (!super.beforeInsertData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    if (!super.beforeDeleteData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Show an input dialog to insert serial numbers.
   */
  private boolean promptSerialNumbers(Object[][] cells,VariantsMatrixVO matrixVO,DetailSaleDocRowVO vo) {

    // define serial numbers and bar codes list to the right size...
    ArrayList list = (ArrayList)vo.getSerialNumbers();
    if (list==null) {
      list = new ArrayList(vo.getQtyDOC02().intValue());
      for(int i=0;i<vo.getQtyDOC02().intValue();i++) {
        list.add(null);
      }
      vo.setSerialNumbers(list);
    }
    else {
      if (vo.getSerialNumbers().size()<vo.getQtyDOC02().intValue()) {
        for(int i=vo.getSerialNumbers().size();i<vo.getQtyDOC02().intValue();i++) {
          vo.getSerialNumbers().add(null);
        }
      }
      else if (vo.getSerialNumbers().size()>vo.getQtyDOC02().intValue()) {
        while(vo.getSerialNumbers().size()>vo.getQtyDOC02().intValue()) {
          vo.getSerialNumbers().remove(vo.getSerialNumbers().size()-1);
        }
      }
    }

    boolean open = false;
    for(int i=0;i<vo.getSerialNumbers().size();i++)
      if (vo.getSerialNumbers().get(i)==null) {
        open = true;
        break;
      }
    if (!open)
      return true;

    // show input dialog...
    new SerialNumberDialog(
      cells,
      matrixVO,
      vo.getSerialNumbers(),
      vo.getItemCodeItm01DOC02()+" - "+vo.getDescriptionSYS10(
    ));

    return true;
  }


  public void modeChanged(int mode) {
    if (mode!=Consts.INSERT) {
      panel.getVariantsPanel().codeChanged(null,null);
    }
  }


}
