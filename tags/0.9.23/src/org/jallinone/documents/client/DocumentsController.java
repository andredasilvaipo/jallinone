package org.jallinone.documents.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.documents.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for documents tree+grid.</p>
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
public class DocumentsController extends CompanyGridController implements TreeController {

  /** documents frame */
  private DocumentsFrame frame = null;


  public DocumentsController() {
    this.frame = new DocumentsFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean canIns = super.beforeInsertGrid(grid);
    if (canIns) {
      // create document detail frame in INSERT mode...
      DefaultMutableTreeNode node = frame.getHierarTreePanel().getSelectedNode();
      if (node==null) {
        // no tree level node selected: insert not allowed!
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select a tree level before inserting new document"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return false;
      }

      HierarchyLevelVO levelVO = (HierarchyLevelVO)node.getUserObject();
      new DocumentController(frame,null, frame.getSelectedDocumentType().getCompanyCodeSys01DOC16(),levelVO.getProgressiveHIE01());
    }
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    // create document detail frame in READONLY mode...
    GridDocumentVO vo = (GridDocumentVO)persistentObject;
    DocumentPK pk = new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14());
    new DocumentController(frame,pk,null,null); /*@todo... */
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    DocumentPK pk = null;
    GridDocumentVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridDocumentVO)persistentObjects.get(i);
      pk = new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14());
      pks.add(pk);
    }
    return ClientUtils.getData("deleteDocuments",pks);
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
    frame.getFilterPanel().reloadData(frame);
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
