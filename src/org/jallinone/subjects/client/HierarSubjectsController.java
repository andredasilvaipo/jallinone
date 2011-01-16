package org.jallinone.subjects.client;

import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.customers.client.CustomersGridFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.subjects.java.Subject;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.subjects.java.HierarSubjectsVO;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientSettings;
import java.beans.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to manage the hierarchy subjects grid.</p>
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
public class HierarSubjectsController extends CompanyGridController {

  private SubjectHierarchiesFrame frame = null;


  public HierarSubjectsController(SubjectHierarchiesFrame frame) {
    this.frame = frame;
  }



  /**
   * Method called on firing a drop event onto the grid.
   * @param gridId identifier of the source grid (grid that generate a draf event)
   * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
   */
  public boolean dropEnabled(String gridId) {
    if (!beforeInsertGrid(frame.getSubjectsGrid()))
      return false;
    if (gridId.equals(frame.getController().getIdGrid().toString()) &&
        frame.getTreePanel().getSelectedNode()!=null) {
      if (!frame.getTreePanel().getSelectedNode().isLeaf()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select a leaf node in the tree"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.WARNING_MESSAGE
        );
        return false;
      }
      HierarchyLevelVO levelVO = (HierarchyLevelVO)frame.getTreePanel().getSelectedNode().getUserObject();
      SubjectFrame f = (SubjectFrame)MDIFrame.getSelectedFrame();
      int[] rows = f.getGrid().getSelectedRows();
      Subject vo = null;
      ArrayList list = new ArrayList();
      for(int i=0;i<rows.length;i++) {
        vo = (Subject)f.getGrid().getVOListTableModel().getObjectForRow(rows[i]);
        list.add( vo );
      }
      ClientUtils.getData("insertSubjectsLinks",new HierarSubjectsVO(
        levelVO.getProgressiveHIE01(),
        levelVO.getProgressiveHie02HIE01(),
        list
      ));
      frame.getSubjectsGrid().reloadData();
      try {
        frame.setSelected(true);
      }
      catch (PropertyVetoException ex) {
      }
      frame.toFront();
      return true;
    }
    return false;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    HierarchyLevelVO levelVO = (HierarchyLevelVO)frame.getTreePanel().getSelectedNode().getUserObject();
    return ClientUtils.getData("deleteSubjectsLinks",new HierarSubjectsVO(
        levelVO.getProgressiveHIE01(),
        levelVO.getProgressiveHie02HIE01(),
        persistentObjects
    ));
  }


}
