package org.jallinone.purchases.documents.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.purchases.documents.java.PurchaseDocRowPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.purchases.documents.java.DetailPurchaseDocRowVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.math.BigDecimal;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for purchase order row detail panel.</p>
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
public class PurchaseDocRowController extends CompanyFormController {

  /** purchase order rows panel */
  private PurchaseDocRowsGridPanel panel = null;

  /** order row pk */
  private PurchaseDocRowPK pk = null;


  public PurchaseDocRowController(PurchaseDocRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    if (pk==null)
      return new VOResponse(new DetailPurchaseDocRowVO());
    else
      return ClientUtils.getData("loadPurchaseDocRow",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
		panel.controlPriceUnit_focusLost(null);
    DetailPurchaseDocRowVO vo = (DetailPurchaseDocRowVO)newPersistentObject;
    Response res = null;

    if (panel.getVariantsPanel().getVariantsMatrixVO()==null) {
      // no variants...
      res = ClientUtils.getData("insertPurchaseDocRow",vo);
    }
    else {
      // the item has variants...
      res = ClientUtils.getData(
        "insertPurchaseDocRows",
        new Object[]{
          vo,
          panel.getVariantsPanel().getVariantsMatrixVO(),
          panel.getVariantsPanel().getCells(),
          panel.getParentVO().getDecimalsREG03()
        }
      );
    }


    if (!res.isError()) {
      vo = (DetailPurchaseDocRowVO)((VOResponse)res).getVo();
      pk = new PurchaseDocRowPK(
          vo.getCompanyCodeSys01DOC07(),
          vo.getDocTypeDOC07(),
          vo.getDocYearDOC07(),
          vo.getDocNumberDOC07(),
          vo.getItemCodeItm01DOC07(),
          vo.getVariantTypeItm06DOC07(),
          vo.getVariantCodeItm11DOC07(),
          vo.getVariantTypeItm07DOC07(),
          vo.getVariantCodeItm12DOC07(),
          vo.getVariantTypeItm08DOC07(),
          vo.getVariantCodeItm13DOC07(),
          vo.getVariantTypeItm09DOC07(),
          vo.getVariantCodeItm14DOC07(),
          vo.getVariantTypeItm10DOC07(),
          vo.getVariantCodeItm15DOC07()
      );
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getOrders().reloadCurrentBlockOfData();
      panel.getFrame().enabledConfirmButton();
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
    panel.controlPriceUnit_focusLost(null);
    Response res = ClientUtils.getData("updatePurchaseDocRow",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getOrders().reloadCurrentBlockOfData();
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
    DetailPurchaseDocRowVO vo = (DetailPurchaseDocRowVO)persistentObject;

    pks.add(pk);
    Response res = ClientUtils.getData("deletePurchaseDocRows",pks);
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getOrders().reloadCurrentBlockOfData();
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailPurchaseDocRowVO vo = (DetailPurchaseDocRowVO)persistentObject;
    DetailPurchaseDocVO parentVO = panel.getParentVO();
    Calendar cal = Calendar.getInstance();
    vo.setCompanyCodeSys01DOC07(parentVO.getCompanyCodeSys01DOC06());
    vo.setDocYearDOC07(parentVO.getDocYearDOC06());
    vo.setDocTypeDOC07(parentVO.getDocTypeDOC06());
    vo.setDocNumberDOC07(parentVO.getDocNumberDOC06());
    vo.setDeliveryDateDOC07(new java.sql.Date(System.currentTimeMillis()));
		vo.setVariantCodeItm11DOC07("*");
		vo.setVariantCodeItm12DOC07("*");
		vo.setVariantCodeItm13DOC07("*");
		vo.setVariantCodeItm14DOC07("*");
		vo.setVariantCodeItm15DOC07("*");
		vo.setVariantTypeItm06DOC07("*");
		vo.setVariantTypeItm07DOC07("*");
		vo.setVariantTypeItm08DOC07("*");
		vo.setVariantTypeItm09DOC07("*");
		vo.setVariantTypeItm10DOC07("*");
  }


  public void setPk(PurchaseDocRowPK pk) {
    this.pk = pk;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    if (!super.beforeEditData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CONFIRMED);
  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    if (!super.beforeInsertData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CONFIRMED);
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    if (!super.beforeDeleteData(form))
      return false;
    return !panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CONFIRMED);
  }


  public void modeChanged(int mode) {
    if (mode!=Consts.INSERT) {
      panel.getVariantsPanel().codeChanged(null,null);
    }
  }

}
