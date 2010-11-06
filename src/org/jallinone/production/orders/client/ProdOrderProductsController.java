package org.jallinone.production.orders.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.production.orders.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for production order products grid.</p>
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
public class ProdOrderProductsController extends CompanyGridController {

  /** production order detail frame */
  private ProdOrderFrame frame = null;


  public ProdOrderProductsController(ProdOrderFrame frame) {
    this.frame = frame;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    DetailProdOrderVO vo = (DetailProdOrderVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    if (vo.getDocStateDOC22().equals(ApplicationConsts.HEADER_BLOCKED)) {
      frame.getCompsGridDataLocator().setServerMethodName("checkComponentsAvailability");
      ArrayList products = new ArrayList();
      for(int i=0;i<frame.getProductsGrid().getVOListTableModel().getRowCount();i++)
        products.add(frame.getProductsGrid().getVOListTableModel().getObjectForRow(i));
      frame.getCompsGrid().getOtherGridParams().put(ApplicationConsts.PRODUCTS,products);
      frame.getCompsGrid().reloadData();
    }
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;
    DetailProdOrderVO vo = (DetailProdOrderVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED))
      return false;
    return true;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    DetailProdOrderVO vo = (DetailProdOrderVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    ProdOrderProductVO prodVO = (ProdOrderProductVO)valueObject;
    prodVO.setCompanyCodeSys01DOC23(vo.getCompanyCodeSys01DOC22());
    prodVO.setDocYearDOC23(vo.getDocYearDOC22());
    prodVO.setDocNumberDOC23(vo.getDocNumberDOC22());
    prodVO.setQtyDOC23(new BigDecimal(1));
    if (frame.getProductsGrid().getVOListTableModel().getRowCount()>1) {
      ProdOrderProductVO pVO= (ProdOrderProductVO)frame.getProductsGrid().getVOListTableModel().getObjectForRow(0);
      prodVO.setProgressiveHie01DOC23( pVO.getProgressiveHie01DOC23() );
      prodVO.setProgressiveHie02DOC23( pVO.getProgressiveHie02DOC23() );
      prodVO.setLocationDescriptionSYS10( pVO.getLocationDescriptionSYS10() );
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    Response res = ClientUtils.getData("insertProdOrderProducts",newValueObjects);
    if (!res.isError()) {
      if (frame.getProductsGrid().getVOListTableModel().getRowCount()==1 ||
          frame.getProductsGrid().getVOListTableModel().getRowCount()==newValueObjects.size()) {
        // refresh detail header and orders list, because order state was updated...
        frame.getHeaderFormPanel().setMode(Consts.READONLY);
        frame.getHeaderFormPanel().executeReload();
        frame.getOrders().reloadCurrentBlockOfData();
      }

    }
    return res;
  }


  /**
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    frame.getCompsGridDataLocator().setServerMethodName("checkComponentsAvailability");
    ArrayList products = new ArrayList();
    for(int i=0;i<frame.getProductsGrid().getVOListTableModel().getRowCount();i++)
      products.add(frame.getProductsGrid().getVOListTableModel().getObjectForRow(i));
    frame.getCompsGrid().getOtherGridParams().put(ApplicationConsts.PRODUCTS,products);
    frame.getCompsGrid().reloadData();
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("updateProdOrderProducts",new ArrayList[]{oldPersistentObjects,persistentObjects});
    if (!res.isError()) {
    }
    return res;
  }


  /**
   * Callback method invoked after saving data when the grid was in EDIT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterEditGrid(GridControl grid) {
    frame.getCompsGridDataLocator().setServerMethodName("checkComponentsAvailability");
    ArrayList products = new ArrayList();
    for(int i=0;i<frame.getProductsGrid().getVOListTableModel().getRowCount();i++)
      products.add(frame.getProductsGrid().getVOListTableModel().getObjectForRow(i));
    frame.getCompsGrid().getOtherGridParams().put(ApplicationConsts.PRODUCTS,products);
    frame.getCompsGrid().reloadData();
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteProdOrderProducts",persistentObjects);
    if (!res.isError()) {
    }
    return res;
  }


  /**
   * Callback method invoked after deleting data when the grid was in READONLY mode (on pressing delete button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterDeleteGrid() {
    frame.getCompsGridDataLocator().setServerMethodName("checkComponentsAvailability");
    ArrayList products = new ArrayList();
    for(int i=0;i<frame.getProductsGrid().getVOListTableModel().getRowCount();i++)
      products.add(frame.getProductsGrid().getVOListTableModel().getObjectForRow(i));
    frame.getCompsGrid().getOtherGridParams().put(ApplicationConsts.PRODUCTS,products);
    frame.getCompsGrid().reloadData();
  }


  /**
   * Callback method invoked on pressing EDIT button.
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    if (!super.beforeEditGrid(grid))
      return false;
    DetailProdOrderVO vo = (DetailProdOrderVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED) &&
           !vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;
    DetailProdOrderVO vo = (DetailProdOrderVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED) &&
           !vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED);
  }



}
