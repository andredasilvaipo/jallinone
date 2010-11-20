package org.jallinone.hierarchies.client;

import org.openswing.swing.tree.client.TreePanel;
import java.math.BigDecimal;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.util.client.*;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.VOResponse;
import java.beans.Beans;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Tree panel used to view a tree whose nodes are retrieved from HIE01 table, based on the specified hierarchy identifier.</p>
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
public class HierarTreePanel extends TreePanel implements TreeController {

  /** hierarchy identifier */
  private BigDecimal progressiveHIE02 = null;

  /** list of HierarTreeListener objects */
  private ArrayList hierarTreeListeners = new ArrayList();


  /** tree data locator */
  private TreeServerDataLocator treeDataLocator = new TreeServerDataLocator() {

    /**
     * Callback method invoked when the data loading is completed.
     * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
     */
    public void loadDataCompleted(boolean error) {
      for(int i=0;i<hierarTreeListeners.size();i++)
        ((HierarTreeListener)hierarTreeListeners.get(i)).loadDataCompleted(error);
    }

  };

  /** flag use din addNotify method */
  private boolean firstTime = true;

  /** function code */
  private String functionId;

  /** company code (may be null if no company code is related to the current hierarchy */
  private String companyCode;


  public HierarTreePanel() {
    treeDataLocator.setServerMethodName("loadHierarchy");
    treeDataLocator.setNodeNameAttribute("descriptionSYS10");

    super.setTreeDataLocator(treeDataLocator);
    super.setTreeController(this);
    super.setLoadWhenVisibile(false);
  }


  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
  }


  /**
   * Method called when the panel is set to visibile.
   */
  public void addNotify() {
    super.addNotify();

    if (Beans.isDesignTime())
      return;

    if (firstTime) {
      firstTime = false;

      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();

      if (functionId==null ||
          (companyCode==null && applet.getAuthorizations().getBa().isInsertEnabled(functionId)) ||
          (companyCode!=null && applet.getAuthorizations().getCompanyBa().isInsertEnabled(functionId,companyCode))
      )
        super.addPopupMenuItem(
          "newlevel.text",
          ClientSettings.getInstance().getResources().getResource("newlevel.mnemonic").charAt(0),
          true,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              DefaultMutableTreeNode selNode = getSelectedNode();
              if (selNode != null) {
                String value = JOptionPane.showInputDialog(
                  ClientUtils.getParentFrame(HierarTreePanel.this),
                  ClientSettings.getInstance().getResources().getResource("level description: "),
                  ClientSettings.getInstance().getResources().getResource("new level"),
                  JOptionPane.QUESTION_MESSAGE
                );
                if (value!=null) {
                  try {
                    HierarchyLevelVO newVO = (HierarchyLevelVO)((HierarchyLevelVO)selNode.getUserObject()).clone();
                    newVO.setProgressiveHie01HIE01(newVO.getProgressiveHIE01());
                    newVO.setLevelHIE01(newVO.getLevelHIE01().add(new BigDecimal(1)));
                    newVO.setDescriptionSYS10(value);
                    DefaultMutableTreeNode newNode = new OpenSwingTreeNode(newVO);
                    Response response = ClientUtils.getData("insertLevel",newVO);
                    if (!response.isError()) {
                      newNode.setUserObject(((VOResponse)response).getVo());
                      selNode.add(newNode);
                      repaintTree();
                   }
                   else
                     JOptionPane.showMessageDialog(
                       ClientUtils.getParentFrame(HierarTreePanel.this),
                       response.getErrorMessage(),
                       ClientSettings.getInstance().getResources().getResource("Error"),
                       JOptionPane.ERROR_MESSAGE
                     );
                  } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                  }
                }
                else
                  JOptionPane.showMessageDialog(
                    ClientUtils.getParentFrame(HierarTreePanel.this),
                    ClientSettings.getInstance().getResources().getResource("You must specify a description for the new level."),
                    ClientSettings.getInstance().getResources().getResource("new level"),
                    JOptionPane.WARNING_MESSAGE
                  );
              }
            }
          }
        );

      if (functionId==null ||
          (companyCode==null && applet.getAuthorizations().getBa().isEditEnabled(functionId)) ||
          (companyCode!=null && applet.getAuthorizations().getCompanyBa().isEditEnabled(functionId,companyCode))
      )
        super.addPopupMenuItem(
          "updatelevel.text",
          ClientSettings.getInstance().getResources().getResource("updatelevel.mnemonic").charAt(0),
          true,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              DefaultMutableTreeNode selNode = getSelectedNode();
              if (selNode != null) {
                HierarchyLevelVO newVO = (HierarchyLevelVO)selNode.getUserObject();
                String value = JOptionPane.showInputDialog(
                  ClientUtils.getParentFrame(HierarTreePanel.this),
                  ClientSettings.getInstance().getResources().getResource("level description: "),
                  newVO.getDescriptionSYS10()
                );
                if (value!=null) {
                  try {
                    HierarchyLevelVO oldVO = (HierarchyLevelVO)((HierarchyLevelVO)selNode.getUserObject()).clone();
                    newVO.setDescriptionSYS10(value);
                    Response response = ClientUtils.getData("updateLevel",new HierarchyLevelVO[] {oldVO,newVO});
                    if (!response.isError()) {
                      selNode.setUserObject(newVO);
                      repaintTree();
                   }
                   else
                     JOptionPane.showMessageDialog(
                       ClientUtils.getParentFrame(HierarTreePanel.this),
                       response.getErrorMessage(),
                       ClientSettings.getInstance().getResources().getResource("Error"),
                       JOptionPane.ERROR_MESSAGE
                     );
                  } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                  }
                }
                else
                  JOptionPane.showMessageDialog(
                    ClientUtils.getParentFrame(HierarTreePanel.this),
                    ClientSettings.getInstance().getResources().getResource("You must specify a description for the level."),
                    ClientSettings.getInstance().getResources().getResource("change level description"),
                    JOptionPane.WARNING_MESSAGE
                  );
              }
            }
          }
        );

      if (functionId==null ||
          (companyCode==null && applet.getAuthorizations().getBa().isDeleteEnabled(functionId)) ||
          (companyCode!=null && applet.getAuthorizations().getCompanyBa().isDeleteEnabled(functionId,companyCode))
      )
        super.addPopupMenuItem(
          "removelevel.text",
          ClientSettings.getInstance().getResources().getResource("removelevel.mnemonic").charAt(0),
          true,
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              DefaultMutableTreeNode selNode = getSelectedNode();
              if (selNode != null && !selNode.isRoot()) {
                if (JOptionPane.showConfirmDialog(
                  ClientUtils.getParentFrame(HierarTreePanel.this),
                  ClientSettings.getInstance().getResources().getResource("this will remove the current level and all sub-levels.Are you sure?"),
                  ClientSettings.getInstance().getResources().getResource("remove level"),
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE
                )==JOptionPane.YES_OPTION) {
                  Response response = ClientUtils.getData("deleteLevel",selNode.getUserObject());
                  if (!response.isError()) {
                    ((DefaultMutableTreeNode)selNode.getParent()).remove(selNode);
                    repaintTree();
                 }
                 else
                   JOptionPane.showMessageDialog(
                     ClientUtils.getParentFrame(HierarTreePanel.this),
                     response.getErrorMessage(),
                     ClientSettings.getInstance().getResources().getResource("Error"),
                     JOptionPane.ERROR_MESSAGE
                   );
                }
              }

            }
          }
        );
    }
  }


  /**
   * @return hierarchy identifier
   */
  public final BigDecimal getProgressiveHIE02() {
    return progressiveHIE02;
  }


  /**
   * Set the hierarchy identifier.
   * @param progressiveHIE02 hierarchy identifier
   */
  public final void setProgressiveHIE02(BigDecimal progressiveHIE02) {
    this.progressiveHIE02 = progressiveHIE02;
    treeDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,progressiveHIE02);
  }


  /**
   * Callback method invoked when the user has clicked the left mouse button a tree node.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
  }


  /**
   * Callback method invoked when the user has clicked the right mouse button a tree node.
   * @param node selected node
   */
  public boolean rightClick(DefaultMutableTreeNode node) {
    return true;
  }


  /**
   * Callback method invoked when the user has doubled clicked a tree node.
   * @param node selected node
   */
  public void doubleClick(DefaultMutableTreeNode node) {

  }


  /**
   * @return function code
   */
  public final String getFunctionId() {
    return functionId;
  }


  /**
   * Set function code.
   * @param functionId function code
   */
  public final void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  /**
   * @return company code
   */
  public final String getCompanyCode() {
    return companyCode;
  }


  /**
   * Set the company code.
   * @param companyCode company code
   */
  public final void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }


  /**
   * Add a HierarTreeListener object.
   */
  public final void addHierarTreeListener(HierarTreeListener listener) {
    hierarTreeListeners.add(listener);
  }

}
