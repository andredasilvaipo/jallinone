package org.jallinone.purchases.documents.invoices.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.purchases.documents.java.GridPurchaseDocVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.math.BigDecimal;
import org.jallinone.purchases.documents.java.DetailPurchaseDocRowVO;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.HashMap;
import org.jallinone.purchases.documents.client.PurchaseDocument;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for purchase debiting note detail frame.</p>
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
 * but WITHOUT ANY WARRANTY; within even the implied warranty of
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
public class PurchaseDebitingDocController extends CompanyFormController {

  /** purchase debiting note frame */
  private PurchaseDebitingDocFrame frame = null;

  /** parent frame */
  private PurchaseDebitingDocsFrame parentFrame = null;

  /** purchase debiting note pk */
  private PurchaseDocPK pk = null;


  public PurchaseDebitingDocController(PurchaseDebitingDocsFrame parentFrame,PurchaseDocPK pk) {
    this.parentFrame = parentFrame;
    this.pk = pk;

    this.frame = new PurchaseDebitingDocFrame(this);

    MDIFrame.add((InternalFrame)frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame((InternalFrame)frame);
    }

    if (pk!=null) {
      frame.getHeaderFormPanel().setMode(Consts.READONLY);
      frame.getHeaderFormPanel().executeReload();
    }
    else {
      frame.getHeaderFormPanel().insert();
    }
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);

    if (ok) {
      frame.getRowsPanel().getGrid().clearData();
      frame.getRowsPanel().getDetailPanel().getVOModel().setValueObject(new DetailPurchaseDocRowVO());
      frame.getRowsPanel().getDetailPanel().pull();
      frame.setButtonsEnabled(false);
      frame.setTitle(ClientSettings.getInstance().getResources().getResource("debiting note"));
    }
    return ok;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadPurchaseDoc",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response res = ClientUtils.getData("insertPurchaseDoc",newPersistentObject);
    if (!res.isError()) {
      DetailPurchaseDocVO vo = (DetailPurchaseDocVO)((VOResponse)res).getVo();
      pk = new PurchaseDocPK(
          vo.getCompanyCodeSys01DOC06(),
          vo.getDocTypeDOC06(),
          vo.getDocYearDOC06(),
          vo.getDocNumberDOC06()
      );

    }
    return res;
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    if (parentFrame!=null) {
      parentFrame.getGrid().reloadCurrentBlockOfData();
    }

    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    frame.getRowsPanel().setParentVO(vo);
    frame.getRowsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
    frame.getRowsPanel().getGrid().reloadData();

    frame.setButtonsEnabled(true);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response res = ClientUtils.getData("updatePurchaseDoc",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
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
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)persistentObject;

    pks.add(pk);
    Response res = ClientUtils.getData("deletePurchaseDocs",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
      frame.getRowsPanel().getGrid().clearData();
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)persistentObject;
    Calendar cal = Calendar.getInstance();
    vo.setDocYearDOC06(new BigDecimal(cal.get(cal.YEAR)));
    vo.setDocDateDOC06(new java.sql.Date(System.currentTimeMillis()));
    vo.setDocTypeDOC06(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE);
    vo.setDocStateDOC06(ApplicationConsts.OPENED);

    // pre-set warehouse code if previously defined (stored in SYS19 table...)
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    final ArrayList companiesList = bca.getCompaniesList("DOC06_DEBITINGNOTES");
    if (companiesList.size()>0) {
      HashMap map = new HashMap();
      // retrieve default warehouse...
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,companiesList.get(0));
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.WAREHOUSE_CODE);
      Response res = ClientUtils.getData("loadUserParam",map);
      if (!res.isError()) {
        String warCode = (String)((VOResponse)res).getVo();
        if (warCode!=null) {
          vo.setWarehouseCodeWar01DOC06(warCode);
        }
      }
    }
  }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }


  public PurchaseDebitingDocsFrame getParentFrame() {
    return parentFrame;
  }


  public PurchaseDocPK getPk() {
    return pk;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    if (!super.beforeEditData(form))
      return false;
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    if (!super.beforeDeleteData(form))
      return false;
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      DetailPurchaseDocVO vo = (DetailPurchaseDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
      if (vo.getWarehouseCodeWar01DOC06()!=null)
        frame.getWarePanel().getWareController().forceValidate();
    }
  }


}
