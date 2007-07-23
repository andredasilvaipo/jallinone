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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for manufacture phases grid frame.</p>
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
public class ManufacturePhasesController extends CompanyGridController {

  /** grid frame */
  private ManufacturesGridFrame gridFrame = null;


  public ManufacturePhasesController(ManufacturesGridFrame gridFrame) {
    this.gridFrame = gridFrame;
  }


  private Response validatePhase(ManufacturePhaseVO vo) {
    if (vo.getManufactureTypePRO02().equals(ApplicationConsts.INTERNAL_MANUFACTURE)) {
      if (vo.getMachineryCodePro03PRO02() == null && vo.getTaskCodeReg07PRO02() == null)
        return new ErrorResponse("you must define either a machinery or a duty");
      if (vo.getTaskCodeReg07PRO02()!=null && vo.getQtyPRO02()==null)
        return new ErrorResponse("you must define the resources number");
      if (vo.getCompletionPercPRO02()!=null && vo.getCompletionPercPRO02().intValue()>100)
        return new ErrorResponse("completion % must be between 0 and 100");
      if (vo.getCompletionPercPRO02()!=null && vo.getCompletionPercPRO02().intValue()<0)
        return new ErrorResponse("completion % must be between 0 and 100");
    }
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
    ManufacturePhaseVO vo = null;
    Response response = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (ManufacturePhaseVO)persistentObjects.get(i);
      response = validatePhase(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("updateManufacturePhases",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    ManufactureVO manVO = (ManufactureVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(gridFrame.getGrid().getSelectedRow());
    ManufacturePhaseVO vo = (ManufacturePhaseVO)valueObject;
    vo.setCompanyCodeSys01PRO02(manVO.getCompanyCodeSys01PRO01());
    vo.setManufactureCodePro01PRO02(manVO.getManufactureCodePRO01());
    vo.setCompletionPercPRO02(new BigDecimal(100));
    vo.setQtyPRO02(new BigDecimal(1));
    vo.setPhaseNumberPRO02(new BigDecimal(gridFrame.getPhasesGridControl().getVOListTableModel().getRowCount()));
  }



  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ManufacturePhaseVO vo = null;
    Response response = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (ManufacturePhaseVO)newValueObjects.get(i);
      response = validatePhase(vo);
      if (response.isError())
        return response;
    }
    return ClientUtils.getData("insertManufacturePhases",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteManufacturePhases",persistentObjects);
    return response;
  }




}
