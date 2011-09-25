package org.jallinone.sales.activities.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.activities.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.jallinone.subjects.java.OrganizationVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sale activities grid frame.</p>
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
public class SaleActivitiesController extends CompanyGridController {

  /** grid frame */
  private SaleActivitiesGridFrame gridFrame = null;


  public SaleActivitiesController() {
    gridFrame = new SaleActivitiesGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SaleActivityVO vo = null;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList("SAL09");

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SaleActivityVO)newValueObjects.get(i);
      if (vo.getCompanyCodeSys01SAL09()==null) {
        vo.setCompanyCodeSys01SAL09( companiesList.get(0).toString() );
      }
    }

    return ClientUtils.getData("insertSaleActivities",newValueObjects);
  }

  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateSaleActivities",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteSaleActivities",persistentObjects);
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    SaleActivityVO vo = (SaleActivityVO)grid.getVOListTableModel().getObjectForRow(row);
    if ((vo.getCurrencyCodeReg03SAL09()==null || vo.getCurrencyCodeReg03SAL09().equals("")) &&
         attributeName.equals("valueSAL09")) {
      return false;
    }
    return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    SaleActivityVO vo = (SaleActivityVO)valueObject;
    if (vo.getCompanyCodeSys01SAL09()==null) {
      // retrieve company code...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList(gridFrame.getGrid().getFunctionId());
      if (companiesList.size()==1) {
        vo.setCompanyCodeSys01SAL09(companiesList.get(0).toString());
      }

      if (vo.getCompanyCodeSys01SAL09()!=null) {
        // set default currency code...
        Response res = ClientUtils.getData("loadCompany",vo.getCompanyCodeSys01SAL09());
        if (!res.isError()) {
          OrganizationVO compVO = (OrganizationVO)((VOResponse)res).getVo();
          vo.setCurrencyCodeReg03SAL09(compVO.getCurrencyCodeReg03());
        }
      }
    }

  }


}
