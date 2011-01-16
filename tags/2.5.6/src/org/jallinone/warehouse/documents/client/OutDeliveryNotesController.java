package org.jallinone.warehouse.documents.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.warehouse.documents.java.GridDeliveryNoteVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for in delivery notes grid.</p>
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
public class OutDeliveryNotesController extends CompanyGridController {

  /** purchase orders frame */
  private OutDeliveryNotesFrame frame = null;


  public OutDeliveryNotesController() {
    this.frame = new OutDeliveryNotesFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      // create purchase order detail frame in INSERT mode...
      new OutDeliveryNoteController(frame,null);
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    // create purchase order detail frame in READONLY mode...
    GridDeliveryNoteVO vo = (GridDeliveryNoteVO)persistentObject;
    DeliveryNotePK pk = new DeliveryNotePK(
        vo.getCompanyCodeSys01DOC08(),
        vo.getDocTypeDOC08(),
        vo.getDocYearDOC08(),
        vo.getDocNumberDOC08()
    );
    new OutDeliveryNoteController(frame,pk);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    DeliveryNotePK pk = null;
    GridDeliveryNoteVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridDeliveryNoteVO)persistentObjects.get(i);
      pk = new DeliveryNotePK(
          vo.getCompanyCodeSys01DOC08(),
          vo.getDocTypeDOC08(),
          vo.getDocYearDOC08(),
          vo.getDocNumberDOC08()
      );
      pks.add(pk);
    }
    return ClientUtils.getData("deleteDeliveryNotes",pks);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;

    GridDeliveryNoteVO vo = null;
    for(int i=0;i<frame.getGrid().getSelectedRows().length;i++) {
      vo = (GridDeliveryNoteVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRows()[i]);
      if (vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED))
        return false;
    }
    return true;
  }



}
