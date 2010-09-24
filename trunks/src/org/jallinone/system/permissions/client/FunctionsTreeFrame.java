package org.jallinone.system.permissions.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.system.permissions.java.JAIOApplicationFunctionVO;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import java.math.BigDecimal;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
* <p>Title: JAllInOne</p>
* <p>Description: Tree frame that contains application functions.</p>
 * </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
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
public class FunctionsTreeFrame extends InternalFrame {

  /** tree panel */
  private TreePanel treePanel = new TreePanel();

  /** current dragged node */
  private DefaultMutableTreeNode dragNode = null;


  public FunctionsTreeFrame(FunctionController controller) {
    try {
      jbInit();
      setTitle(ClientSettings.getInstance().getResources().getResource("application functions"));
      setSize(600,550);
      treePanel.setTreeController(controller);
      treePanel.setTreeDataLocator(controller);
      treePanel.setLeavesImageName("functions.gif");
//      treePanel.setExpandAllNodes(true);
      treePanel.addPopupMenuItem("change description",'C',true,new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          JAIOApplicationFunctionVO vo = (JAIOApplicationFunctionVO)FunctionsTreeFrame.this.treePanel.getSelectedNode().getUserObject();
          String newValue = JOptionPane.showInputDialog(
            FunctionsTreeFrame.this,
            ClientSettings.getInstance().getResources().getResource("new description:"),
            vo.getDescription()
          );
          if (newValue!=null) {
            JAIOApplicationFunctionVO newVO = null;
            try {
              newVO = (JAIOApplicationFunctionVO)vo.clone();
              newVO.setDescription(newValue);
            }
            catch (Exception ex) {
              ex.printStackTrace();
              JOptionPane.showMessageDialog(
                 MDIFrame.getInstance(),
                 ClientSettings.getInstance().getResources().getResource(ex.getMessage()),
                 ClientSettings.getInstance().getResources().getResource("Error"),
                 JOptionPane.ERROR_MESSAGE
              );
              return;
            }
            Response res = ClientUtils.getData(
              "updateFunction",
              new JAIOApplicationFunctionVO[]{vo,newVO}
            );
            if (res.isError()) {
              JOptionPane.showMessageDialog(
                 MDIFrame.getInstance(),
                 ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
                 ClientSettings.getInstance().getResources().getResource("Error"),
                 JOptionPane.ERROR_MESSAGE
              );
              return;
            }

            vo.setDescription(newValue);
          }
        }

      });


      // enable drag 'n drop onto the treePanel...
      treePanel.enableDrag("TREE",new TreeDragNDropListener() {

        public boolean dragEnabled() {
          // drag operation has started...
          dragNode = (DefaultMutableTreeNode)treePanel.getSelectedNode();
          return true;
        }


        public boolean dropEnabled(DefaultMutableTreeNode node,String treeId) {
          // drop has terminated...

          int num = 0; // default operation: move
          if (!((JAIOApplicationFunctionVO) dragNode.getUserObject()).isFolder())
            // the node is a function: ask user which operation to do...
            num = JOptionPane.showOptionDialog(
              FunctionsTreeFrame.this,
              ClientSettings.getInstance().getResources().getResource("which operation?"),
              ClientSettings.getInstance().getResources().getResource("node dropped"),
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              new Object[]{
                ClientSettings.getInstance().getResources().getResource("move node"),
                ClientSettings.getInstance().getResources().getResource("copy node"),
                ClientSettings.getInstance().getResources().getResource("cancel")
              },
              null
            );
          if (num!=0 && num!=1)
            // user has pressed cancel button...
            return false;

          // user has pressed move or copy button...
          DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
          if (parentNode==null)
            // no drop allowed in root node...
            return false;


          // move or copy: prepare new v.o.
          JAIOApplicationFunctionVO vo = null;
          try {
            vo = (JAIOApplicationFunctionVO)((JAIOApplicationFunctionVO) dragNode.getUserObject()).clone();
          }
          catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
               MDIFrame.getInstance(),
               ClientSettings.getInstance().getResources().getResource(ex.getMessage()),
               ClientSettings.getInstance().getResources().getResource("Error"),
               JOptionPane.ERROR_MESSAGE
            );
            return false;
          }


          // prepare new node...
          JAIOApplicationFunctionVO sourceVO = (JAIOApplicationFunctionVO)dragNode.getUserObject();
          DefaultMutableTreeNode newDragNode = null;
          if (((JAIOApplicationFunctionVO) dragNode.getUserObject()).isFolder()) {
            return false;
          }
          else {
            // source node is a function...
            if (((JAIOApplicationFunctionVO)node.getUserObject()).isFolder()) {
              // destination node is a folder...
              vo.setProgressiveHie01SYS18(((JAIOApplicationFunctionVO)node.getUserObject()).getProgressiveHIE01());
              if (((JAIOApplicationFunctionVO)((DefaultMutableTreeNode)node.getLastChild()).getUserObject()).getPosOrderSYS18()!=null)
                vo.setPosOrderSYS18(
                  ((JAIOApplicationFunctionVO)((DefaultMutableTreeNode)node.getLastChild()).getUserObject()).getPosOrderSYS18().add(new BigDecimal(1))
                );
              else
                vo.setPosOrderSYS18(new BigDecimal(1));
            }
            else {
              // destination node is a leaf...
              vo.setProgressiveHie01SYS18(((JAIOApplicationFunctionVO)node.getUserObject()).getProgressiveHie01SYS18());
              vo.setPosOrderSYS18(new BigDecimal((
                ((JAIOApplicationFunctionVO)((DefaultMutableTreeNode)node).getUserObject()).getPosOrderSYS18().doubleValue()+
                ((JAIOApplicationFunctionVO)((DefaultMutableTreeNode)node.getNextSibling()).getUserObject()).getPosOrderSYS18().doubleValue()
              )/2));

                if (num==1)
                  vo.setCopyNode(true);
            }
            newDragNode = new OpenSwingTreeNode(vo);
          }


          // call server side...
          Response res = ClientUtils.getData(
            "updateFunction",
            new JAIOApplicationFunctionVO[]{
              sourceVO,
              vo
            }
          );
          if (res.isError()) {
            JOptionPane.showMessageDialog(
               MDIFrame.getInstance(),
               ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
               ClientSettings.getInstance().getResources().getResource("Error"),
               JOptionPane.ERROR_MESSAGE
            );
            return false;
          }

          if (num==0) {
            // move node...
            // remove previous node...
            ((DefaultMutableTreeNode)dragNode.getParent()).remove(dragNode);
          }

          if (num==0 || num==1) {
            if (((JAIOApplicationFunctionVO)node.getUserObject()).isFolder())
              // destination node is a folder...
              node.add(newDragNode);
            else
              // destination node is a leaf...
              parentNode.insert(newDragNode,parentNode.getIndex(node)+1);
            treePanel.repaintTree();
            treePanel.getTree().setSelectionPath(new TreePath(newDragNode.getPath()));
          }

          return true;
        }

      });


      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    this.getContentPane().add(treePanel, BorderLayout.CENTER);
  }


}

