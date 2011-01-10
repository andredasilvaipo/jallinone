package org.jallinone.scheduler.callouts.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.scheduler.callouts.java.*;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import java.awt.Color;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for call-out requests grid.</p>
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
public class CallOutRequestsController extends CompanyGridController {

  /** call-out requests frame */
  private CallOutRequestsFrame frame = null;


  public CallOutRequestsController() {
    this.frame = new CallOutRequestsFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      // create call-out request detail frame in INSERT mode...
      new CallOutRequestController(frame,null);
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    // create call-out detail frame in READONLY mode...
    GridCallOutRequestVO vo = (GridCallOutRequestVO)persistentObject;
    CallOutRequestPK pk = new CallOutRequestPK(vo.getCompanyCodeSys01SCH03(),vo.getRequestYearSCH03(),vo.getProgressiveSCH03());
    new CallOutRequestController(frame,pk);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    CallOutRequestPK pk = null;
    GridCallOutRequestVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridCallOutRequestVO)persistentObjects.get(i);
      pk = new CallOutRequestPK(vo.getCompanyCodeSys01SCH03(),vo.getRequestYearSCH03(),vo.getProgressiveSCH03());
      pks.add(pk);
    }
    return ClientUtils.getData("deleteCallOutRequests",pks);
  }


  /**
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributedName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributedName,Object value) {
    GridCallOutRequestVO vo = (GridCallOutRequestVO)frame.getGrid().getVOListTableModel().getObjectForRow(row);
    if (attributedName.equals("callOutStateSCH03")) {
      Color color = null;
      if (vo.getCallOutStateSCH03().equals(ApplicationConsts.CLOSED) ||
          vo.getCallOutStateSCH03().equals(ApplicationConsts.INVOICED))
        return super.getBackgroundColor(row,attributedName,value);
      else return new Color(241,143,137);
    }
    else if (attributedName.equals("prioritySCH03")) {
      Color color = null;
      if (vo.getPrioritySCH03().equals(ApplicationConsts.PRIORITY_HIGHEST))
        color = new Color(241,123,137);
      else if (vo.getPrioritySCH03().equals(ApplicationConsts.PRIORITY_HIGH))
        color = new Color(248,176,181);
      else if (vo.getPrioritySCH03().equals(ApplicationConsts.PRIORITY_NORMAL))
        color = new Color(191,246,207);
      else if (vo.getPrioritySCH03().equals(ApplicationConsts.PRIORITY_LOW))
        color = new Color(191,226,207);
      else if (vo.getPrioritySCH03().equals(ApplicationConsts.PRIORITY_TRIVIAL))
        color = new Color(191,206,207);
      return color;
    }
    else
      return super.getBackgroundColor(row,attributedName,value);
  }


}
