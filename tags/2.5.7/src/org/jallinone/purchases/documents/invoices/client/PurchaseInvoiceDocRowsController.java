package org.jallinone.purchases.documents.invoices.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.purchases.documents.java.PurchaseDocRowPK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for purchase invoice rows grid.</p>
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
 * but WITHOUT ANY WARRANTY; within even the implied warranty of
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
public class PurchaseInvoiceDocRowsController extends CompanyGridController {

  /** purchase invoice rows panel */
  private PurchaseInvoiceDocRowsGridPanel panel = null;


  public PurchaseInvoiceDocRowsController(PurchaseInvoiceDocRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;
    if (panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CLOSED))
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
    GridPurchaseDocRowVO vo = (GridPurchaseDocRowVO)persistentObject;
    PurchaseDocRowPK pk = new PurchaseDocRowPK(
        vo.getCompanyCodeSys01DOC07(),
        vo.getDocTypeDOC07(),
        vo.getDocYearDOC07(),
        vo.getDocNumberDOC07(),
        vo.getItemCodeItm01DOC07(),
        vo.getVariantTypeItm06DOC07(),
        vo.getVariantCodeItm11DOC07(),
        vo.getVariantTypeItm07DOC07(),
        vo.getVariantCodeItm12DOC07(),
        vo.getVariantTypeItm08DOC07(),
        vo.getVariantCodeItm13DOC07(),
        vo.getVariantTypeItm09DOC07(),
        vo.getVariantCodeItm14DOC07(),
        vo.getVariantTypeItm10DOC07(),
        vo.getVariantCodeItm15DOC07()
    );
    ((PurchaseInvoiceDocRowController)panel.getDetailPanel().getFormController()).setPk(pk);
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
    PurchaseDocRowPK pk = null;
    GridPurchaseDocRowVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridPurchaseDocRowVO)persistentObjects.get(i);
      pk = new PurchaseDocRowPK(
          vo.getCompanyCodeSys01DOC07(),
          vo.getDocTypeDOC07(),
          vo.getDocYearDOC07(),
          vo.getDocNumberDOC07(),
          vo.getItemCodeItm01DOC07(),
          vo.getVariantTypeItm06DOC07(),
          vo.getVariantCodeItm11DOC07(),
          vo.getVariantTypeItm07DOC07(),
          vo.getVariantCodeItm12DOC07(),
          vo.getVariantTypeItm08DOC07(),
          vo.getVariantCodeItm13DOC07(),
          vo.getVariantTypeItm09DOC07(),
          vo.getVariantCodeItm14DOC07(),
          vo.getVariantTypeItm10DOC07(),
          vo.getVariantCodeItm15DOC07()
      );
      pks.add(pk);
    }
    Response res = ClientUtils.getData("deletePurchaseDocRows",pks);
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      if (panel.getInvoices()!=null)
        panel.getInvoices().reloadCurrentBlockOfData();
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
    return !panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC06().equals(ApplicationConsts.CLOSED);
  }



}
