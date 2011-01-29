package org.jallinone.sales.documents.invoices.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for sale invoice row detail panel.</p>
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
public class SaleInvoiceDocRowController extends CompanyFormController {

  /** sale invoice rows panel */
  private SaleInvoiceDocRowsGridPanel panel = null;

  /** invoice row pk */
  private SaleDocRowPK pk = null;


  public SaleInvoiceDocRowController(SaleInvoiceDocRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadSaleDocRow",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response res = null;

    if (panel.getVariantsPanel().getVariantsMatrixVO()==null) {
      // no variants...
      res = ClientUtils.getData("insertSaleDocRow",newPersistentObject);
    }
    else {
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
    if (panel.getInvoices()!=null)
      panel.getInvoices().reloadCurrentBlockOfData();
    ((InvoiceDocument)panel.getFrame()).enabledConfirmButton();

    panel.getDiscountsPanel().setEnabled(true);
    panel.getDiscountsPanel().setParentVO(panel.getParentVO(),(DetailSaleDocRowVO)panel.getDetailPanel().getVOModel().getValueObject());
    panel.getDiscountsPanel().getGrid().reloadData();
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    if (error)
      return;

    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)panel.getDetailPanel().getVOModel().getValueObject();

    panel.getDiscountsPanel().setEnabled(true);
    panel.getDiscountsPanel().setParentVO(panel.getParentVO(),vo);
    panel.getDiscountsPanel().getGrid().reloadData();

    panel.getBookedItemsPanel().getControlItemType().setValue(vo.getProgressiveHie02DOC02());
    panel.getBookedItemsPanel().getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
    panel.getBookedItemsPanel().getControlItemCode().getLookupController().forceValidate();
    panel.getBookedItemsPanel().getGrid().reloadData();
    panel.getOrderedItemsPanel().getControlItemType().setValue(vo.getProgressiveHie02DOC02());
    panel.getOrderedItemsPanel().getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
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
    Response res = ClientUtils.getData("updateSaleDocRow",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      if (panel.getInvoices()!=null)
        panel.getInvoices().reloadCurrentBlockOfData();
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
      if (panel.getInvoices()!=null)
        panel.getInvoices().reloadCurrentBlockOfData();

      panel.getDiscountsPanel().setEnabled(false);
      panel.getDiscountsPanel().getGrid().clearData();
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

    panel.getDiscountsPanel().setEnabled(false);
    panel.getDiscountsPanel().getGrid().clearData();
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


  public void modeChanged(int mode) {
    if (mode!=Consts.INSERT) {
      panel.getVariantsPanel().codeChanged(null,null);
    }
  }


}
