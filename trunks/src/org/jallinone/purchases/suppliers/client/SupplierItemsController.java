package org.jallinone.purchases.suppliers.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.subjects.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.purchases.items.java.SupplierItemPK;
import org.openswing.swing.form.client.Form;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.suppliers.java.DetailSupplierVO;
import org.jallinone.items.client.ItemsFrame;
import org.jallinone.items.java.GridItemVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller for supplier items list.</p>
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
public class SupplierItemsController extends CompanyGridController {


  /** supplier detail frame */
  private SupplierDetailFrame frame = null;


  public SupplierItemsController(SupplierDetailFrame frame) {
    this.frame = frame;
  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean ok = super.beforeInsertGrid(grid);
    if (!ok)
      return false;

    if (frame.getTreePanel().getSelectedNode()==null || !frame.getTreePanel().getSelectedNode().isLeaf()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(grid),
          ClientSettings.getInstance().getResources().getResource("you are allowed to insert data only on a leaf node."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return false;
    }

    return ok;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    SupplierItemVO vo = (SupplierItemVO)valueObject;
    vo.setMinPurchaseQtyPUR02(new BigDecimal(1));
    vo.setMultipleQtyPUR02(new BigDecimal(1));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SupplierItemVO vo = null;
    DefaultMutableTreeNode selNode = frame.getTreePanel().getSelectedNode();
    HierarchyLevelVO levelVO = (HierarchyLevelVO)selNode.getUserObject();
    DetailSupplierVO supplierVO = (DetailSupplierVO)frame.getCurrentForm().getVOModel().getValueObject();

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SupplierItemVO)newValueObjects.get(i);
      vo.setProgressiveHie02PUR02(levelVO.getProgressiveHie02HIE01());
      vo.setProgressiveHie01PUR02(levelVO.getProgressiveHIE01());
      vo.setProgressiveReg04PUR02(supplierVO.getProgressiveREG04());
    }

    return ClientUtils.getData("insertSupplierItems",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateSupplierItems",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    SupplierItemVO vo = null;
    ArrayList pks = new ArrayList();
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (SupplierItemVO)persistentObjects.get(i);
      pks.add(new SupplierItemPK(vo.getCompanyCodeSys01PUR02(),vo.getItemCodeItm01PUR02(),vo.getProgressiveReg04PUR02()));
    }
    Response response = ClientUtils.getData("deleteSupplierItems",pks);
    return response;
  }



  /**
   * Method called on firing a drop event onto the grid.
   * @param gridId identifier of the source grid (grid that generate a draf event)
   * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
   */
  public boolean dropEnabled(String gridId) {
    if (!beforeInsertGrid(frame.getItemsGrid()))
      return false;
    if (gridId.equals(ApplicationConsts.ID_ITEMS_GRID.toString()) &&
        frame.getCurrentForm().getVOModel().getValueObject()!=null &&
        ((DetailSupplierVO)frame.getCurrentForm().getVOModel().getValueObject()).getProgressiveREG04()!=null
    ) {

      ItemsFrame f = (ItemsFrame)MDIFrame.getSelectedFrame();
      int[] rows = f.getGrid().getSelectedRows();
      GridItemVO vo = null;
      SupplierItemVO suppItemVO = null;
      ArrayList list = new ArrayList();
      DetailSupplierVO supplierVO = (DetailSupplierVO)frame.getCurrentForm().getVOModel().getValueObject();
      for(int i=0;i<rows.length;i++) {
        vo = (GridItemVO)f.getGrid().getVOListTableModel().getObjectForRow(rows[i]);
        suppItemVO = new SupplierItemVO();
        suppItemVO.setCompanyCodeSys01PUR02(vo.getCompanyCodeSys01ITM01());
        suppItemVO.setDescriptionSYS10(vo.getDescriptionSYS10());
        suppItemVO.setItemCodeItm01PUR02(vo.getItemCodeITM01());
        suppItemVO.setMinPurchaseQtyPUR02(new BigDecimal(1));
        suppItemVO.setMultipleQtyPUR02(new BigDecimal(1));
        suppItemVO.setProgressiveHie01PUR02(vo.getProgressiveHie01ITM01());
        suppItemVO.setProgressiveHie02PUR02(vo.getProgressiveHie02ITM01());
        suppItemVO.setProgressiveReg04PUR02(supplierVO.getProgressiveREG04());
        suppItemVO.setSupplierItemCodePUR02(vo.getItemCodeITM01());
        suppItemVO.setUmCodeReg02PUR02(vo.getMinSellingQtyUmCodeReg02ITM01());

        list.add( suppItemVO );
      }
      Response res = ClientUtils.getData("insertSupplierItems",list);
      frame.getItemsGrid().reloadData();

      return true;
    }
    return false;
  }


}
