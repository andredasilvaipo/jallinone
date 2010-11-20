package org.jallinone.production.manufactures.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.production.manufactures.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.registers.currency.java.CurrencyVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for operations grid frame used to define manufacture phases.</p>
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
public class OperationsController extends CompanyGridController {

  /** grid frame */
  private OperationsGridFrame gridFrame = null;


  public OperationsController() {
    gridFrame = new OperationsGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  private Response validatePhase(OperationVO vo) {
    if (vo.getMachineryCodePro03PRO04() == null && vo.getTaskCodeReg07PRO04() == null)
      return new ErrorResponse("you must define either a machinery or a duty");
    if (vo.getTaskCodeReg07PRO04()!=null && vo.getQtyPRO04()==null)
      return new ErrorResponse("you must define the resources number");
    return new VOResponse(Boolean.TRUE);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    OperationVO vo = null;
    Response response = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (OperationVO)persistentObjects.get(i);
      response = validatePhase(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("updateOperations",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    OperationVO vo = (OperationVO)valueObject;

    // set company code if not yet setted...
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList("PRO04");
    if (companiesList.size()>0 && vo.getCompanyCodeSys01PRO04()==null) {
      vo.setCompanyCodeSys01PRO04((String)companiesList.get(0));
    }

    Response res = ClientUtils.getData("loadCompanyCurrency",vo.getCompanyCodeSys01PRO04());
    if (res.isError())
      throw new Exception(res.getErrorMessage());
    gridFrame.setCurrencyVO((CurrencyVO)((VOResponse)res).getVo());


  }



  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    OperationVO vo = null;
    Response response = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (OperationVO)newValueObjects.get(i);
      response = validatePhase(vo);
      if (response.isError())
        return response;
    }
    return ClientUtils.getData("insertOperations",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteOperations",persistentObjects);
    return response;
  }




}
