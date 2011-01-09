package org.jallinone.sales.documents.invoices.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.documents.java.GridSaleDocVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.HashMap;
import org.jallinone.sales.documents.client.SaleDocument;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for sale credit note detail frame.</p>
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
public class SaleCreditDocController extends CompanyFormController {

  /** sale credit note frame */
  private SaleCreditDocFrame frame = null;

  /** parent frame */
  private SaleCreditDocsFrame parentFrame = null;

  /** sale invoice pk */
  private SaleDocPK pk = null;


  public SaleCreditDocController(SaleCreditDocsFrame parentFrame,SaleDocPK pk) {
    this.parentFrame = parentFrame;
    this.pk = pk;

    this.frame = new SaleCreditDocFrame(this);

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
      frame.getRowsPanel().getDetailPanel().getVOModel().setValueObject(new DetailSaleDocRowVO());
      frame.getRowsPanel().getDetailPanel().pull();
      frame.getDiscPanel().getGrid().clearData();
      frame.getChargePanel().getGrid().clearData();
      frame.getActPanel().getGrid().clearData();
      frame.setButtonsEnabled(false);
      frame.setTitle(ClientSettings.getInstance().getResources().getResource("credit note"));
    }
    return ok;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadSaleDoc",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response res = ClientUtils.getData("insertSaleDoc",newPersistentObject);
    if (!res.isError()) {
      DetailSaleDocVO vo = (DetailSaleDocVO)((VOResponse)res).getVo();
      pk = new SaleDocPK(
          vo.getCompanyCodeSys01DOC01(),
          vo.getDocTypeDOC01(),
          vo.getDocYearDOC01(),
          vo.getDocNumberDOC01()
      );

    }
    return res;
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    if (parentFrame!=null) {
      parentFrame.getGrid().reloadData();
    }

    DetailSaleDocVO vo = (DetailSaleDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    frame.getRowsPanel().setParentVO(vo);
    frame.getRowsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    frame.getRowsPanel().getGrid().reloadData();

    frame.getDiscPanel().setParentVO(vo);
    frame.getDiscPanel().getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    frame.getDiscPanel().getGrid().reloadData();

    frame.getChargePanel().setParentVO(vo);
    frame.getChargePanel().getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    frame.getChargePanel().getGrid().reloadData();

    frame.getActPanel().setParentVO(vo);
    frame.getActPanel().getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    frame.getActPanel().getGrid().reloadData();

    frame.setButtonsEnabled(true);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response res = ClientUtils.getData("updateSaleDoc",new ValueObject[]{oldPersistentObject,persistentObject});
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
    DetailSaleDocVO vo = (DetailSaleDocVO)persistentObject;

    pks.add(pk);
    Response res = ClientUtils.getData("deleteSaleDocs",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
      frame.getRowsPanel().getGrid().clearData();
      frame.getDiscPanel().getGrid().clearData();
      frame.getChargePanel().getGrid().clearData();
      frame.getActPanel().getGrid().clearData();
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailSaleDocVO vo = (DetailSaleDocVO)persistentObject;
    Calendar cal = Calendar.getInstance();
    vo.setDocYearDOC01(new BigDecimal(cal.get(cal.YEAR)));
    vo.setDocDateDOC01(new java.sql.Date(System.currentTimeMillis()));
    vo.setDocTypeDOC01(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE);
    vo.setDocStateDOC01(ApplicationConsts.OPENED);

    // pre-set warehouse code if previously defined (stored in SYS19 table...)
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    final ArrayList companiesList = bca.getCompaniesList("DOC01_CREDITNOTES");
    if (companiesList.size()>0) {
      HashMap map = new HashMap();
      // retrieve default warehouse...
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,companiesList.get(0));
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.WAREHOUSE_CODE);
      Response res = ClientUtils.getData("loadUserParam",map);
      if (!res.isError()) {
        String warCode = (String)((VOResponse)res).getVo();
        if (warCode!=null) {
          vo.setWarehouseCodeWar01DOC01(warCode);
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


  public SaleCreditDocsFrame getParentFrame() {
    return parentFrame;
  }


  public SaleDocPK getPk() {
    return pk;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    if (!super.beforeEditData(form))
      return false;
    DetailSaleDocVO vo = (DetailSaleDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    if (!super.beforeDeleteData(form))
      return false;
    DetailSaleDocVO vo = (DetailSaleDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      DetailSaleDocVO vo = (DetailSaleDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
      if (vo.getWarehouseCodeWar01DOC01()!=null)
        frame.getWarePanel().getWareController().forceValidate();
    }
  }


}
