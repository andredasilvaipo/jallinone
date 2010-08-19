package org.jallinone.items.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for items grid.</p>
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
public class ItemsController extends CompanyGridController implements TreeController {

  /** items frame */
  private ItemsFrame frame = null;
  private boolean productsOnly;


  public ItemsController(boolean productsOnly) {
    this.productsOnly = productsOnly;
    this.frame = new ItemsFrame(this,productsOnly);
    MDIFrame.add(frame);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      // create item detail frame in INSERT mode...
      new ItemController(frame,null,productsOnly);
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    // create item detail frame in READONLY mode...
    GridItemVO vo = (GridItemVO)persistentObject;
    ItemPK pk = new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01());
    new ItemController(frame,pk,productsOnly);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    ItemPK pk = null;
    GridItemVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridItemVO)persistentObjects.get(i);
      pk = new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01());
      pks.add(pk);
    }
    return ClientUtils.getData("deleteItems",pks);
  }


  /**
   * Callback method invoked when the user has clicked the left mouse button.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
    HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
    HierarchyLevelVO root = (HierarchyLevelVO)((DefaultMutableTreeNode)node.getRoot()).getUserObject();
    frame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
    frame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
    frame.getGrid().getOtherGridParams().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());
    frame.getGrid().reloadData();
  }


  /**
   * Callback method invoked when the user has clicked the right mouse button.
   * @param node selected node
   */
  public boolean rightClick(DefaultMutableTreeNode node) {
    return true;
  }


  /**
   * Callback method invoked when the user has doubled clicked.
   * @param node selected node
   */
  public void doubleClick(DefaultMutableTreeNode node) {
  }



}
