package org.jallinone.subjects.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.subjects.java.SubjectHierarchyVO;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.math.BigDecimal;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to manage the subject hierarchies grid.</p>
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
public class SubjectHierarchiesController extends CompanyGridController implements TreeController {

  /** subject type */
  private String subjectTypeREG08 = null;

  /** subject hierarchy grid frame */
  private SubjectHierarchiesFrame frame = null;

  /** function code */
  private String functionId = null;

  /** window id */
  private BigDecimal idGrid = null;



  public SubjectHierarchiesController(String subjectTypeREG08,String functionId,String title,BigDecimal idGrid,boolean loadCurrentLevel) {
    this.subjectTypeREG08 = subjectTypeREG08;
    this.functionId = functionId;
    this.idGrid = idGrid;
    this.frame = new SubjectHierarchiesFrame(this,functionId,title,subjectTypeREG08,loadCurrentLevel);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    doubleClick(grid.getSelectedRow(),grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow()));
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    int selRow = frame.getSubjectHierarchiesGrid().getSelectedRow();
    if (selRow!=-1)
      doubleClick(selRow,frame.getSubjectHierarchiesGrid().getVOListTableModel().getObjectForRow(selRow));
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    SubjectHierarchyVO vo = (SubjectHierarchyVO)persistentObject;
    frame.getTreePanel().setCompanyCode(vo.getCompanyCodeSys01REG08());
    frame.getTreePanel().setProgressiveHIE02(vo.getProgressiveHie02REG08());
    frame.getTreePanel().getTreeDataLocator().getTreeNodeParams().put(ApplicationConsts.SUBJECT_TYPE,vo.getSubjectTypeREG08());
    frame.getTreePanel().getTreeDataLocator().getTreeNodeParams().put(ApplicationConsts.FUCTION_CODE,functionId);
    frame.getTreePanel().reloadTree();
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ValueObject newValueObject = (ValueObject)newValueObjects.get(0);
    SubjectHierarchyVO vo = (SubjectHierarchyVO)newValueObject;
    vo.setSubjectTypeREG08(subjectTypeREG08);

    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList(functionId);
    if (vo.getCompanyCodeSys01REG08()==null)
      vo.setCompanyCodeSys01REG08( companiesList.get(0).toString() );

    Response res = ClientUtils.getData("insertSubjectHierarchy",vo);
    if (res.isError()) {
      return res;
    }
    else {
      ArrayList list = new ArrayList();
      list.add(((VOResponse)res).getVo());
      return new VOListResponse(list,false,list.size());
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateSubjectHierarchies",new ArrayList[] { oldPersistentObjects,persistentObjects });
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteSubjectHierarchy",persistentObjects);
    if (!res.isError()) {
      frame.getTreePanel().clearTree();
      frame.getSubjectsGrid().clearData();
    }
    return res;
  }






  /**
   * Callback method invoked when the user has clicked the left mouse button.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
    HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
    HierarchyLevelVO root = (HierarchyLevelVO)((DefaultMutableTreeNode)node.getRoot()).getUserObject();
    String subjectType = (String)frame.getTreePanel().getTreeDataLocator().getTreeNodeParams().get(ApplicationConsts.SUBJECT_TYPE);

    frame.getSubjectsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
    frame.getSubjectsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
    frame.getSubjectsGrid().getOtherGridParams().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());
    frame.getSubjectsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,frame.getTreePanel().getCompanyCode());
    frame.getSubjectsGrid().getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,subjectType);
    frame.getSubjectsGrid().reloadData();
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


  /**
   * @return window id
   */
  public BigDecimal getIdGrid() {
    return idGrid;
  }





}
