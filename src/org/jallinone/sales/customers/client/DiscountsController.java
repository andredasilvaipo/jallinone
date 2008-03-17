package org.jallinone.sales.customers.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.subjects.java.*;
import org.jallinone.sales.customers.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import org.jallinone.sales.discounts.java.CustomerDiscountVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller customer discounts grid panel.</p>
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
public class DiscountsController extends CompanyGridController {

  /** customer detail frame */
  private CustomerDetailFrame frame = null;


  public DiscountsController(CustomerDetailFrame frame) {
    this.frame = frame;
  }


  /**
   * Validate min/max values/percentages and dates.
   */
  private Response validateDiscount(CustomerDiscountVO vo) {
    if (vo.getMinValueSAL03()!=null && vo.getMaxValueSAL03()==null ||
        vo.getMinValueSAL03()==null && vo.getMaxValueSAL03()!=null) {
      return new ErrorResponse("max and min values must be both setted.");
    }
    if (vo.getMinPercSAL03()!=null && vo.getMaxPercSAL03()==null ||
        vo.getMinPercSAL03()==null && vo.getMaxPercSAL03()!=null) {
      return new ErrorResponse("max and min percentages must be both setted.");
    }
    if (vo.getMinValueSAL03()==null && vo.getMaxValueSAL03()==null &&
        vo.getMinPercSAL03()==null && vo.getMaxPercSAL03()==null) {
      return new ErrorResponse("you must define max and min percentages or max and min values.");
    }
    if (vo.getMinValueSAL03()!=null && vo.getMaxValueSAL03()!=null) {
      if (vo.getMinPercSAL03()!=null || vo.getMaxPercSAL03()!=null)
        return new ErrorResponse("you must define max and min percentages otherwise max and min values.");
      if (vo.getMinValueSAL03().doubleValue()>vo.getMaxValueSAL03().doubleValue())
        return new ErrorResponse("min value must be less than or equals to max value.");
    }
    if (vo.getMinPercSAL03()!=null && vo.getMaxPercSAL03()!=null) {
      if (vo.getMinValueSAL03()!=null || vo.getMaxValueSAL03()!=null)
        return new ErrorResponse("you must define max and min percentages otherwise max and min values.");
      if (vo.getMinPercSAL03().doubleValue()>vo.getMaxPercSAL03().doubleValue())
        return new ErrorResponse("min percentage must be less than or equals to max percentage.");
    }

    if (vo.getStartDateSAL03().getTime()>vo.getEndDateSAL03().getTime())
      return new ErrorResponse("start date must be less than or equals to end date.");

    return new VOResponse(new Boolean(true));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    CustomerDiscountVO vo = null;
    Response response = null;
    Subject customerVO = (Subject)frame.getCurrentForm().getVOModel().getValueObject();

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (CustomerDiscountVO) newValueObjects.get(i);
      response = validateDiscount(vo);
      if (response.isError())
        return response;
      vo.setCompanyCodeSys01SAL03(customerVO.getCompanyCodeSys01REG04());
      vo.setProgressiveReg04SAL08(customerVO.getProgressiveREG04());
    }

    response = ClientUtils.getData("insertCustomerDiscounts",newValueObjects);
    return response;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response response = null;
    CustomerDiscountVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (CustomerDiscountVO)persistentObjects.get(i);
      response = validateDiscount(vo);
      if (response.isError())
        return response;
    }

    response = ClientUtils.getData("updateCustomerDiscounts",new ArrayList[]{oldPersistentObjects,persistentObjects});
    return response;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteCustomerDiscounts",persistentObjects);
    return response;
  }



}
