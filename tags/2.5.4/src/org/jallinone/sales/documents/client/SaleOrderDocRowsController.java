package org.jallinone.sales.documents.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for sale order rows grid.</p>
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
public class SaleOrderDocRowsController extends CompanyGridController {

  /** sale order rows panel */
  private SaleOrderDocRowsGridPanel panel = null;


  public SaleOrderDocRowsController(SaleOrderDocRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;
    if (panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED))
      return false;

    panel.getDetailPanel().insert();
    return false;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (panel.getGrid().getVOListTableModel().getRowCount()>0) {
      doubleClick(0,panel.getGrid().getVOListTableModel().getObjectForRow(0));
    }
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    GridSaleDocRowVO vo = (GridSaleDocRowVO)persistentObject;
    SaleDocRowPK pk = new SaleDocRowPK(
        vo.getCompanyCodeSys01DOC02(),
        vo.getDocTypeDOC02(),
        vo.getDocYearDOC02(),
        vo.getDocNumberDOC02(),
        vo.getItemCodeItm01DOC02(),
        vo.getVariantTypeItm06DOC02(),
        vo.getVariantCodeItm11DOC02(),
        vo.getVariantTypeItm07DOC02(),
        vo.getVariantCodeItm12DOC02(),
        vo.getVariantTypeItm08DOC02(),
        vo.getVariantCodeItm13DOC02(),
        vo.getVariantTypeItm09DOC02(),
        vo.getVariantCodeItm14DOC02(),
        vo.getVariantTypeItm10DOC02(),
        vo.getVariantCodeItm15DOC02()

    );
    ((SaleOrderDocRowController)panel.getDetailPanel().getFormController()).setPk(pk);
    panel.getDetailPanel().setMode(Consts.READONLY);
    panel.getDetailPanel().executeReload();
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    SaleDocRowPK pk = null;
    GridSaleDocRowVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridSaleDocRowVO)persistentObjects.get(i);
      pk = new SaleDocRowPK(
          vo.getCompanyCodeSys01DOC02(),
          vo.getDocTypeDOC02(),
          vo.getDocYearDOC02(),
          vo.getDocNumberDOC02(),
          vo.getItemCodeItm01DOC02(),
          vo.getVariantTypeItm06DOC02(),
          vo.getVariantCodeItm11DOC02(),
          vo.getVariantTypeItm07DOC02(),
          vo.getVariantCodeItm12DOC02(),
          vo.getVariantTypeItm08DOC02(),
          vo.getVariantCodeItm13DOC02(),
          vo.getVariantTypeItm09DOC02(),
          vo.getVariantCodeItm14DOC02(),
          vo.getVariantTypeItm10DOC02(),
          vo.getVariantCodeItm15DOC02()

      );
      pks.add(pk);
    }
    Response res = ClientUtils.getData("deleteSaleDocRows",pks);
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getOrders().reloadCurrentBlockOfData();
			panel.getDiscountsPanel().getGrid().reloadCurrentBlockOfData();
    }
    return res;
  }


  /**
   * Callback method invoked on pressing EDIT button.
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    if (!super.beforeEditGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED);
  }



}
