package org.jallinone.sales.documents.client;

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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for delivery request detail frame.</p>
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
public class DeliveryRequestController extends CompanyFormController {

  /** sale delivery request  frame */
  private DeliveryRequestFrame frame = null;

  /** parent frame */
  private DeliveryRequestsFrame parentFrame = null;

  /** sale delivery request  pk */
  private SaleDocPK pk = null;


  public DeliveryRequestController(DeliveryRequestsFrame parentFrame,SaleDocPK pk) {
    this.parentFrame = parentFrame;
    this.pk = pk;
    this.frame = new DeliveryRequestFrame(this);
    MDIFrame.add(frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (pk!=null) {
      frame.getHeaderFormPanel().setMode(Consts.READONLY);
      frame.getHeaderFormPanel().executeReload();
    }
    else {
      frame.getHeaderFormPanel().insert();

      frame.getSaleCustomerHeadPanel1().getControlCompaniesCombo().addItemListener(new ItemListener() {

        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED &&
              frame.getHeaderFormPanel().getMode()==Consts.INSERT) {
            Object companyCodeSys01 = frame.getSaleCustomerHeadPanel1().getControlCompaniesCombo().getValue();
            DetailSaleDocVO vo = (DetailSaleDocVO)frame.getHeaderFormPanel().getVOModel().getValueObject();

            // retrieve default customer code...
            HashMap map = new HashMap();
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CUSTOMER_CODE);
            Response res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              String customerCode = (String)((VOResponse)res).getVo();
              if (customerCode!=null) {
                vo.setCustomerCodeSAL07(customerCode);
                vo.setCompanyCodeSys01DOC01(companyCodeSys01.toString());
                try {
                  frame.getSaleCustomerHeadPanel1().getControlCustomerCode().validateCode(customerCode);
                }
                catch (Exception ex) {
                }
              }
            }
            // retrieve default warehouse...
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.WAREHOUSE_CODE);
            res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              String warCode = (String)((VOResponse)res).getVo();
              if (warCode!=null) {
                vo.setWarehouseCodeWar01DOC01(warCode);
                try {
                  frame.getControlWarehouseCode().validateCode(warCode);
                }
                catch (Exception ex1) {
                }
              }
            }
          }
        }

      });

    }
  }



  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    // since this method could be invoked also when selecting another row on the linked grid,
    // the pk attribute must be recalculated from the grid...
    if (getParentFrame()!=null) {
      int row = parentFrame.getGrid().getSelectedRow();
      if (row!=-1) {
        GridSaleDocVO gridVO = (GridSaleDocVO)parentFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = new SaleDocPK(gridVO.getCompanyCodeSys01DOC01(),gridVO.getDocTypeDOC01(),gridVO.getDocYearDOC01(),gridVO.getDocNumberDOC01());
      }
    }

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
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }


  public DeliveryRequestsFrame getParentFrame() {
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
      if (vo.getCustomerCodeSAL07()!=null)
        frame.getSaleCustomerHeadPanel1().getCustomerController().forceValidate();
      if (vo.getWarehouseCodeWar01DOC01()!=null)
        frame.getWareController().forceValidate();

//      if (vo.getCustomerCodeSAL07()!=null && vo.getWarehouseCodeWar01DOC01()!=null) {
//        if (frame.getHeaderFormPanel().save()) {
//          frame.showRowsPanel();
//          frame.getRowsPanel().getDetailPanel().insert();
//        }
//      }
    }
  }



}
