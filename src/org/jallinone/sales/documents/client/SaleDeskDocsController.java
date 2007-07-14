package org.jallinone.sales.documents.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.sales.documents.java.GridSaleDocVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for desk selling grid.</p>
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
public class SaleDeskDocsController extends CompanyGridController {

  /** desk selling frame */
  private SaleDeskDocsFrame frame = null;


  public SaleDeskDocsController() {
    this.frame = new SaleDeskDocsFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      // create desk selling detail frame in INSERT mode...
      new SaleDeskDocController(frame,null);
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    // create sale order detail frame in READONLY mode...
    GridSaleDocVO vo = (GridSaleDocVO)persistentObject;
    SaleDocPK pk = new SaleDocPK(
        vo.getCompanyCodeSys01DOC01(),
        vo.getDocTypeDOC01(),
        vo.getDocYearDOC01(),
        vo.getDocNumberDOC01()
    );
    new SaleDeskDocController(frame,pk);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    SaleDocPK pk = null;
    GridSaleDocVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridSaleDocVO)persistentObjects.get(i);
      pk = new SaleDocPK(
          vo.getCompanyCodeSys01DOC01(),
          vo.getDocTypeDOC01(),
          vo.getDocYearDOC01(),
          vo.getDocNumberDOC01()
      );
      pks.add(pk);
    }
    return ClientUtils.getData("deleteSaleDocs",pks);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;

    GridSaleDocVO vo = null;
    for(int i=0;i<frame.getGrid().getSelectedRows().length;i++) {
      vo = (GridSaleDocVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRows()[i]);
      if (vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED))
        return false;
    }
    return true;
  }



}
