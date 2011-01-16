package org.jallinone.warehouse.documents.client;


import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;
import java.math.BigDecimal;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.variants.java.VariantsMatrixVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for in delivery note rows grid.</p>
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
public class InDeliveryNoteRowsController extends CompanyGridController {

  /** in delivery note rows panel */
  private InDeliveryNoteRowsGridPanel panel = null;


  public InDeliveryNoteRowsController(InDeliveryNoteRowsGridPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;
    if (panel.getParentVO().getDocStateDOC08().equals(ApplicationConsts.CLOSED))
      return false;

    return true;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)valueObject;
    vo.setCompanyCodeSys01DOC09(panel.getParentVO().getCompanyCodeSys01DOC08());
    vo.setDocTypeDOC09(panel.getParentVO().getDocTypeDOC08());
    vo.setDocYearDOC09(panel.getParentVO().getDocYearDOC08());
    vo.setDocNumberDOC09(panel.getParentVO().getDocNumberDOC08());
    vo.setDocTypeDoc06DOC09(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
    vo.setWarehouseCodeWar01DOC08(panel.getParentVO().getWarehouseCodeWar01DOC08());
    vo.setVariantCodeItm11DOC09("*");
    vo.setVariantCodeItm12DOC09("*");
    vo.setVariantCodeItm13DOC09("*");
    vo.setVariantCodeItm14DOC09("*");
    vo.setVariantCodeItm15DOC09("*");
    vo.setVariantTypeItm06DOC09("*");
    vo.setVariantTypeItm07DOC09("*");
    vo.setVariantTypeItm08DOC09("*");
    vo.setVariantTypeItm09DOC09("*");
    vo.setVariantTypeItm10DOC09("*");
    vo.setLocationDescriptionSYS10("");

    Response res = ClientUtils.getData("getRootLevel",panel.getParentVO().getProgressiveHie02WAR01());
    if (!res.isError()) {
      HierarchyLevelVO posVO = (HierarchyLevelVO)((VOResponse)res).getVo();
      vo.setProgressiveHie01DOC09(posVO.getProgressiveHIE01());
      vo.setLocationDescriptionSYS10(posVO.getDescriptionSYS10());
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ValueObject newValueObject = (ValueObject)newValueObjects.get(0);
    if (panel.isSerialNumberRequired() &&
        !promptSerialNumbers(null,null,(GridInDeliveryNoteRowVO)newValueObject)) {
      return new ErrorResponse("insert not allowed until serial numbers are not defined");
    }

    Response res = ClientUtils.getData("insertInDeliveryNoteRow",newValueObject);
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
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    panel.getHeaderPanel().setMode(Consts.READONLY);
    panel.getHeaderPanel().executeReload();
    panel.getOrders().reloadCurrentBlockOfData();
    panel.getFrame().enabledConfirmButton();
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    for(int i=0;i<persistentObjects.size();i++) {
      GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)persistentObjects.get(i);
      if (panel.isSerialNumberRequired() &&
          !promptSerialNumbers(null,null,vo)) {
        return new ErrorResponse("update not allowed until serial numbers are not defined");
      }
    }

    return ClientUtils.getData("updateInDeliveryNoteRows",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked after saving data when the grid was in EDIT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterEditGrid(GridControl grid) {
    panel.getGrid().reloadData();
    panel.getHeaderPanel().setMode(Consts.READONLY);
    panel.getHeaderPanel().executeReload();
    panel.getOrders().reloadCurrentBlockOfData();
  }


  /**
   * Show an input dialog to insert serial numbers.
   */
  private boolean promptSerialNumbers(Object[][] cells,VariantsMatrixVO matrixVO,GridInDeliveryNoteRowVO vo) {

    // define serial numbers list to the right size...
    ArrayList list = (ArrayList)vo.getSerialNumbers();
    if (list==null) {
      list = new ArrayList(vo.getQtyDOC09().intValue());
      for(int i=0;i<vo.getQtyDOC09().intValue();i++) {
        list.add(null);
      }
      vo.setSerialNumbers(list);
    }
    else {
      if (vo.getSerialNumbers().size()<vo.getQtyDOC09().intValue()) {
        for(int i=vo.getSerialNumbers().size();i<vo.getQtyDOC09().intValue();i++) {
          vo.getSerialNumbers().add(null);
        }
      }
      else if (vo.getSerialNumbers().size()>vo.getQtyDOC09().intValue()) {
        while(vo.getSerialNumbers().size()>vo.getQtyDOC09().intValue()) {
          vo.getSerialNumbers().remove(vo.getSerialNumbers().size()-1);
        }
      }
    }

    // show input dialog...
    new SerialNumberDialog(
      cells,
      matrixVO,
      vo.getSerialNumbers(),
      vo.getItemCodeItm01DOC09()+" - "+vo.getDescriptionSYS10()
    );

    return true;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    ArrayList pks = new ArrayList();
    InDeliveryNoteRowPK pk = null;
    GridInDeliveryNoteRowVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridInDeliveryNoteRowVO)persistentObjects.get(i);
      pk = new InDeliveryNoteRowPK(
          vo.getProgressiveDOC09(),
          vo.getCompanyCodeSys01DOC09(),
          vo.getDocTypeDOC09(),
          vo.getDocYearDOC09(),
          vo.getDocNumberDOC09(),
          vo.getDocTypeDoc06DOC09(),
          vo.getDocYearDoc06DOC09(),
          vo.getDocNumberDoc06DOC09(),
          vo.getRowNumberDOC09(),
          vo.getItemCodeItm01DOC09(),
          vo.getVariantTypeItm06DOC09(),
          vo.getVariantCodeItm11DOC09(),
          vo.getVariantTypeItm07DOC09(),
          vo.getVariantCodeItm12DOC09(),
          vo.getVariantTypeItm08DOC09(),
          vo.getVariantCodeItm13DOC09(),
          vo.getVariantTypeItm09DOC09(),
          vo.getVariantCodeItm14DOC09(),
          vo.getVariantTypeItm10DOC09(),
          vo.getVariantCodeItm15DOC09()

      );
      pks.add(pk);
    }
    Response res = ClientUtils.getData("deleteInDeliveryNoteRows",pks);
    if (!res.isError()) {
      panel.getGrid().reloadData();
      panel.getHeaderPanel().setMode(Consts.READONLY);
      panel.getHeaderPanel().executeReload();
      panel.getOrders().reloadCurrentBlockOfData();
    }
    return res;
  }


  /**
   * Callback method invoked on pressing EDIT button.
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    if (!super.beforeEditGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC08().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    if (!super.beforeDeleteGrid(grid))
      return false;
    return !panel.getParentVO().getDocStateDOC08().equals(ApplicationConsts.CLOSED);
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
    GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)panel.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
    if (attributeName.equals("supplierQtyDOC09")) {
      if (newValue==null)
        vo.setQtyDOC09(null);
      else if (vo.getValueREG05()!=null)
        vo.setQtyDOC09(((BigDecimal)newValue).divide(vo.getValueREG05(),vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
    }
    else if (attributeName.equals("qtyDOC09")) {
      if (newValue==null)
        vo.setSupplierQtyDOC09(null);
      else if (vo.getValueREG05()!=null)
        vo.setSupplierQtyDOC09(((BigDecimal)newValue).multiply(vo.getValueREG05()).setScale(vo.getSupplierQtyDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
    }
    return true;
  }


}
