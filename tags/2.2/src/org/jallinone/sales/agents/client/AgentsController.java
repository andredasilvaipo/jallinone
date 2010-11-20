package org.jallinone.sales.agents.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.agents.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for agents grid frame.</p>
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
public class AgentsController extends CompanyGridController {

  /** grid frame */
  private AgentsGridFrame gridFrame = null;


  public AgentsController() {
    gridFrame = new AgentsGridFrame(this);
    MDIFrame.add(gridFrame);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertAgents",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateAgents",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteAgents",persistentObjects);
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
    if (attributeName.equals("companyCodeSys01SAL10") && newValue!=null) {
      gridFrame.getSubjectDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,newValue);
      gridFrame.getSubjectDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,newValue);
    }
    return true;
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    boolean ok = grid.isFieldEditable(row,attributeName);
    if (ok && attributeName.equals("progressiveReg04SAL10")) {
      AgentVO vo = (AgentVO)grid.getVOListTableModel().getObjectForRow(row);

      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SAL10");
      if (companiesList.size()==1)
        vo.setCompanyCodeSys01SAL10(companiesList.get(0).toString());

      if (vo.getCompanyCodeSys01SAL10()==null)
        return false;
    }
    return ok;
  }


}
