package org.jallinone.system.companies.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.system.companies.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.subjects.java.SubjectPK;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for companies grid frame.</p>
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
public class CompaniesController extends GridController {

  /** grid frame */
  private CompaniesGridFrame gridFrame = null;


  public CompaniesController() {
    gridFrame = new CompaniesGridFrame(this);
    MDIFrame.add(gridFrame);
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    new CompanyController(
        gridFrame,
        ((CompanyVO)persistentObject).getCompanyCodeSYS01(),
        ((CompanyVO)persistentObject).getProgressiveREG04()
    );
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    new CompanyController(gridFrame,null,null);
    return false;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    CompanyVO vo = (CompanyVO)persistentObjects.get(0);
    SubjectPK pk = new SubjectPK(vo.getCompanyCodeSYS01(),vo.getProgressiveREG04());

    Response response = ClientUtils.getData("deleteCompany",pk);
    return response;
  }




}