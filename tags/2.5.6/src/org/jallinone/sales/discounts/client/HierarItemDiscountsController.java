package org.jallinone.sales.discounts.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.subjects.java.*;
import org.jallinone.sales.customers.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import org.jallinone.sales.discounts.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.tree.client.TreeController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Tree + Grid controller for hierarchy item discounts.</p>
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
public class HierarItemDiscountsController extends CompanyGridController implements TreeController {

  /** hierarchy item discounts frame */
  private HierarItemDiscountsFrame frame = null;


  public HierarItemDiscountsController() {
    this.frame = new HierarItemDiscountsFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    HierarItemDiscountVO vo = (HierarItemDiscountVO)valueObject;
    vo.setMinQtySAL03(new BigDecimal(1));
    vo.setMultipleQtySAL03(Boolean.FALSE);
  }


  /**
   * Validate min/max values/percentages and dates.
   */
  private Response validateDiscount(HierarItemDiscountVO vo) {
    if (vo.getMinValueSAL03()!=null && vo.getMaxValueSAL03()==null ||
        vo.getMinValueSAL03()==null && vo.getMaxValueSAL03()!=null) {
      return new ErrorResponse("max and min values must be both setted.");
    }
    if (vo.getMinPercSAL03()!=null && vo.getMaxPercSAL03()==null ||
        vo.getMinPercSAL03()==null && vo.getMaxPercSAL03()!=null) {
      return new ErrorResponse("max and min percentages must be both setted.");
    }
    if (vo.getMinValueSAL03()==null && vo.getMaxValueSAL03()==null &&
        vo.getMinPercSAL03()==null && vo.getMaxPercSAL03()==null) {
      return new ErrorResponse("you must define max and min percentages or max and min values.");
    }
    if (vo.getMinValueSAL03()!=null && vo.getMaxValueSAL03()!=null) {
      if (vo.getMinPercSAL03()!=null || vo.getMaxPercSAL03()!=null)
        return new ErrorResponse("you must define max and min percentages otherwise max and min values.");
      if (vo.getMinValueSAL03().doubleValue()>vo.getMaxValueSAL03().doubleValue())
        return new ErrorResponse("min value must be less than or equals to max value.");
    }
    if (vo.getMinPercSAL03()!=null && vo.getMaxPercSAL03()!=null) {
      if (vo.getMinValueSAL03()!=null || vo.getMaxValueSAL03()!=null)
        return new ErrorResponse("you must define max and min percentages otherwise max and min values.");
      if (vo.getMinPercSAL03().doubleValue()>vo.getMaxPercSAL03().doubleValue())
        return new ErrorResponse("min percentage must be less than or equals to max percentage.");
    }

    if (vo.getStartDateSAL03().getTime()>vo.getEndDateSAL03().getTime())
      return new ErrorResponse("start date must be less than or equals to end date.");

    return new VOResponse(new Boolean(true));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    HierarItemDiscountVO vo = null;
    DefaultMutableTreeNode node = frame.getHierarTreePanel().getSelectedNode();
    HierarchyLevelVO levelVO = (HierarchyLevelVO)node.getUserObject();
    HierarchyLevelVO root = (HierarchyLevelVO)((DefaultMutableTreeNode)node.getRoot()).getUserObject();
    Response response = null;

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (HierarItemDiscountVO)newValueObjects.get(i);
      vo.setProgressiveHie01SAL05(levelVO.getProgressiveHIE01());
      response = validateDiscount(vo);
      if (response.isError())
        return response;
    }

    response = ClientUtils.getData("insertHierarItemDiscounts",newValueObjects);
    return response;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response response = null;
    HierarItemDiscountVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (HierarItemDiscountVO)persistentObjects.get(i);
      response = validateDiscount(vo);
      if (response.isError())
        return response;
    }

    response = ClientUtils.getData("updateHierarItemDiscounts",new ArrayList[]{oldPersistentObjects,persistentObjects});
    return response;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteHierarItemDiscounts",persistentObjects);
    return response;
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
