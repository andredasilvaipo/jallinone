package org.jallinone.sales.charges.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.charges.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.CompanyGridController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for charges grid frame.</p>
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
public class ChargesController extends CompanyGridController {

  /** grid frame */
  private ChargesGridFrame gridFrame = null;


  public ChargesController() {
    gridFrame = new ChargesGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Validate value and percentage.
   */
  private Response validateCharge(ChargeVO vo) {
    if (vo.getPercSAL06()==null && vo.getValueSAL06()==null)
      return new ErrorResponse("you must define a value or a percentage charge.");
    if (vo.getPercSAL06()!=null && vo.getValueSAL06()!=null)
      return new ErrorResponse("a charge must be define as a value or as a percentage.");

    // vat is required for charge expressed as a percentage...
    if (vo.getValueSAL06()!=null && vo.getVatCodeReg01SAL06()==null)
      return new ErrorResponse("vat code is required for a charge expressed as a value.");
    return new VOResponse(Boolean.TRUE);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ChargeVO vo = null;
    Response res = null;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList("SAL06");

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (ChargeVO)newValueObjects.get(i);
      res = validateCharge(vo);
      if (res.isError())
        return res;

      if (vo.getCompanyCodeSys01SAL06()==null) {
        vo.setCompanyCodeSys01SAL06( companiesList.get(0).toString() );
      }
    }

    return ClientUtils.getData("insertCharges",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    ChargeVO vo = null;
    Response res = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (ChargeVO)persistentObjects.get(i);
      res = validateCharge(vo);
      if (res.isError())
        return res;
    }

    return ClientUtils.getData("updateCharges",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteCharges",persistentObjects);
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    ChargeVO vo = (ChargeVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);

    // set vat data to null if the user has define a charge expressed as a value...
    if (attributeName.equals("percSAL06") && newValue!=null) {
      vo.setVatCodeReg01SAL06(null);
      vo.setVatDeductibleREG01(null);
      vo.setVatDescriptionSYS10(null);
      vo.setVatValueREG01(null);
    }

    return true;
  }



}
