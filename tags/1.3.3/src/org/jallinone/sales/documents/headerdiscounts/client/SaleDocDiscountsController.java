package org.jallinone.sales.documents.headerdiscounts.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.CompanyGridController;
import java.util.ArrayList;
import org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO;
import org.openswing.swing.client.GridControl;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid Controller used for customer discounts defined in a sale document.</p>
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
public class SaleDocDiscountsController extends CompanyGridController {


  /** item discounts panel */
  private SaleDocDiscountsPanel panel = null;


  public SaleDocDiscountsController(SaleDocDiscountsPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    SaleDocDiscountVO vo = (SaleDocDiscountVO)valueObject;
    vo.setCompanyCodeSys01DOC05(panel.getParentVO().getCompanyCodeSys01DOC01());
    vo.setDocTypeDOC05(panel.getParentVO().getDocTypeDOC01());
    vo.setDocYearDOC05(panel.getParentVO().getDocYearDOC01());
    vo.setDocNumberDOC05(panel.getParentVO().getDocNumberDOC01());
  }



  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    SaleDocDiscountVO vo = (SaleDocDiscountVO)panel.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
    if (attributeName.equals("valueDOC05") && newValue!=null) {
      if (vo.getPercDOC05() != null)
        vo.setPercDOC05(null);
      if (((BigDecimal)newValue).doubleValue()<vo.getMinValueDOC05().doubleValue())
        return false;
      else if (((BigDecimal)newValue).doubleValue()>vo.getMaxValueDOC05().doubleValue())
        return false;
    }
    else if (attributeName.equals("percDOC05") && newValue!=null) {
      if (vo.getValueDOC05() != null)
        vo.setValueDOC05(null);
      if (((BigDecimal)newValue).doubleValue()<vo.getMinPercDOC05().doubleValue())
        return false;
      else if (((BigDecimal)newValue).doubleValue()>vo.getMaxPercDOC05().doubleValue())
        return false;
    }
    return true;
  }


  /**
   * Check if a discount has been defined (as a value or as a percentage)
   */
  private Response checkDiscount(SaleDocDiscountVO vo) {
    if (vo.getValueDOC05()==null && vo.getPercDOC05()==null) {
      return new ErrorResponse("you must define a discount as value or as a percentage");
    }
    return new VOResponse(Boolean.TRUE);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SaleDocDiscountVO vo = null;
    Response res = null;

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SaleDocDiscountVO)newValueObjects.get(i);
      res = checkDiscount(vo);
      if (res.isError())
        return res;
    }
    return ClientUtils.getData("insertSaleDocDiscounts",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    SaleDocDiscountVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (SaleDocDiscountVO)persistentObjects.get(i);
      Response res = checkDiscount(vo);
      if (res.isError())
        return res;
    }

    return ClientUtils.getData("updateSaleDocDiscounts",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteSaleDocDiscounts",persistentObjects);
  }



  /**
   * Callback method invoked after saving data when the grid was in EDIT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterEditGrid(GridControl grid) {
    panel.getParentFrame().getHeaderFormPanel().setMode(Consts.READONLY);
    panel.getParentFrame().getHeaderFormPanel().executeReload();
  }


  /**
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    panel.getParentFrame().getHeaderFormPanel().setMode(Consts.READONLY);
    panel.getParentFrame().getHeaderFormPanel().executeReload();
  }


  /**
   * Callback method invoked after deleting data when the grid was in READONLY mode (on pressing delete button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterDeleteGrid() {
    panel.getParentFrame().getHeaderFormPanel().setMode(Consts.READONLY);
    panel.getParentFrame().getHeaderFormPanel().executeReload();
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    SaleDocDiscountVO vo = (SaleDocDiscountVO)grid.getVOListTableModel().getObjectForRow(row);
    if (attributeName.equals("valueDOC05") && vo.getMinValueDOC05()==null) {
      return false;
    }
    else if (attributeName.equals("percDOC05") && vo.getMinPercDOC05()==null) {
      return false;
    }
    return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Callback method invoked on pressing INSERT button.
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED);
  }


  /**
   * Callback method invoked on pressing EDIT button.
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    if (!super.beforeEditGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC01().equals(ApplicationConsts.CONFIRMED);
  }





}
