package org.jallinone.system.customizations.client;

import org.openswing.swing.table.client.GridController;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.customizations.java.CustomFunctionVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used to manage custom functions.</p>
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
public class CustomFunctionsController extends GridController {

  private CustomFunctionsGridFrame frame = null;


  public CustomFunctionsController() {
    frame = new CustomFunctionsGridFrame(this);
    MDIFrame.add(frame);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      new CustomFunctionController(frame,null);
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    CustomFunctionVO vo = (CustomFunctionVO)persistentObject;
    new CustomFunctionController(frame,vo);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    CustomFunctionVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (CustomFunctionVO)persistentObjects.get(i);
      pks.add(vo.getFunctionCodeSys06SYS16());
    }
    return ClientUtils.getData("deleteCustomFunctions",pks);
  }





}
