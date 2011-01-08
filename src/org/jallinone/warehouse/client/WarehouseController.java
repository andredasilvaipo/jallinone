package org.jallinone.warehouse.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.warehouse.java.WarehousePK;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for warehouse detail frame.</p>
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
public class WarehouseController extends CompanyFormController {

  /** pks */
  private String companyCodeSys01WAR01 = null;
  private String warehouseCodeWAR01 = null;

  /** detail frame */
  private WarehouseDetailFrame detailFrame = null;

  /** parent frame */
  private WarehousesGridFrame gridFrame = null;


  public WarehouseController(WarehousesGridFrame gridFrame,String companyCodeSys01WAR01,String warehouseCodeWAR01) {
    this.gridFrame = gridFrame;
    this.companyCodeSys01WAR01 = companyCodeSys01WAR01;
    this.warehouseCodeWAR01 = warehouseCodeWAR01;

    detailFrame = new WarehouseDetailFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (companyCodeSys01WAR01!=null && warehouseCodeWAR01!=null) {
      detailFrame.getWarehouseForm().setMode(Consts.READONLY);
//      detailFrame.getWarehouseForm().reload();
      detailFrame.getWarehouseForm().executeReload();
    }
    else {
      detailFrame.getWarehouseForm().insert();
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    // since this method could be invoked also when selecting another row on the linked grid,
   // the pk attribute must be recalculated from the grid...
   int row = gridFrame.getGrid().getSelectedRow();
   if (row!=-1) {
     WarehouseVO gridVO = (WarehouseVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
     companyCodeSys01WAR01 =gridVO.getCompanyCodeSys01WAR01();
     warehouseCodeWAR01 =gridVO.getWarehouseCodeWAR01();

   }

    return ClientUtils.getData("loadWarehouse",new WarehousePK(companyCodeSys01WAR01,warehouseCodeWAR01));
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response response = ClientUtils.getData("insertWarehouse",newPersistentObject);
    if (!response.isError()) {
      companyCodeSys01WAR01 = ((WarehouseVO)newPersistentObject).getCompanyCodeSys01WAR01();
      warehouseCodeWAR01 = ((WarehouseVO)newPersistentObject).getWarehouseCodeWAR01();
 //     gridFrame.reloadData();
    }
    return response;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    boolean canEdit = super.beforeEditData(form);
    if (!canEdit)
      return false;

    // warehouse data are editable ONLY if the user has the "warehouse role" (progressiveSys04WAR01)...
    WarehouseVO vo = (WarehouseVO)form.getVOModel().getValueObject();
    if (vo.getProgressiveSys04WAR01()==null) {
      detailFrame.getHierarTreePanel().setEnabled(true);
      return true;
    }
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    if (!applet.getAuthorizations().getUserRoles().containsKey(vo.getProgressiveSys04WAR01())) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(form),
        ClientSettings.getInstance().getResources().getResource(
        "You are not allowed to edit data."),
        ClientSettings.getInstance().getResources().getResource(
        "Attention"),
        JOptionPane.WARNING_MESSAGE
      );
      return false;
    }

    detailFrame.getHierarTreePanel().setEnabled(true);
    return true;
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    boolean canDelete = super.beforeDeleteData(form);
    if (!canDelete)
      return false;

    // warehouse data are erasable ONLY if the user has the "warehouse role" (progressiveSys04WAR01)...
    WarehouseVO vo = (WarehouseVO)form.getVOModel().getValueObject();
    if (vo.getProgressiveSys04WAR01()==null)
      return true;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    if (!applet.getAuthorizations().getUserRoles().containsKey(vo.getProgressiveSys04WAR01())) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(form),
        ClientSettings.getInstance().getResources().getResource(
        "You are not allowed to delete data."),
        ClientSettings.getInstance().getResources().getResource(
        "Attention"),
        JOptionPane.WARNING_MESSAGE
      );
      return false;
    }
    return true;
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response response = ClientUtils.getData("updateWarehouse",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!response.isError()) {
//      gridFrame.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    WarehouseVO vo = (WarehouseVO)persistentObject;
    WarehousePK pk = new WarehousePK(vo.getCompanyCodeSys01WAR01(),vo.getWarehouseCodeWAR01());
    Response response = ClientUtils.getData("deleteWarehouse",pk);
    if (!response.isError()) {
      gridFrame.reloadData();
    }
    return response;
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    WarehouseVO vo = (WarehouseVO)detailFrame.getWarehouseForm().getVOModel().getValueObject();

    detailFrame.getHierarTreePanel().setEnabled(false);
    detailFrame.getHierarTreePanel().setCompanyCode(vo.getCompanyCodeSys01WAR01());
    detailFrame.getHierarTreePanel().setProgressiveHIE02(vo.getProgressiveHie02WAR01());
    detailFrame.getHierarTreePanel().reloadTree();

    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR01());
    if (detailFrame.getHierarTreePanel().getSelectedNode()!=null) {
      HierarchyLevelVO levelVO = (HierarchyLevelVO)detailFrame.getHierarTreePanel().getSelectedNode().getUserObject();
      detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,levelVO.getProgressiveHIE01());
    }
    detailFrame.getAvailPanel().setEnabled(true);
    detailFrame.getAvailPanel().getGrid().reloadData();

    detailFrame.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getBookedItemsPanel().setEnabled(true);
    detailFrame.getBookedItemsPanel().getGrid().reloadData();

    detailFrame.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getOrderedItemsPanel().setEnabled(true);
  }


  /**
   * Callback method called when reloading data is SUCCESSFULLY completed.
   */
  public void afterReloadData() {
  }


  /**
   * Callback method called when inserting data is SUCCESSFULLY completed.
   */
  public void afterInsertData() {
    WarehouseVO vo = (WarehouseVO)detailFrame.getWarehouseForm().getVOModel().getValueObject();
    detailFrame.getHierarTreePanel().setCompanyCode(vo.getCompanyCodeSys01WAR01());
    detailFrame.getHierarTreePanel().setProgressiveHIE02(vo.getProgressiveHie02WAR01());
    detailFrame.getHierarTreePanel().reloadTree();

    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR01());
    detailFrame.getAvailPanel().setEnabled(true);

    detailFrame.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getBookedItemsPanel().setEnabled(true);

    detailFrame.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
    detailFrame.getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
    detailFrame.getBookedItemsPanel().setEnabled(true);
  }




  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);
    if (ok) {
      detailFrame.getHierarTreePanel().clearTree();
      detailFrame.getHierarTreePanel().setEnabled(false);

      detailFrame.getAvailPanel().setEnabled(false);
      detailFrame.getAvailPanel().getGrid().clearData();

      detailFrame.getBookedItemsPanel().setEnabled(false);
      detailFrame.getBookedItemsPanel().getGrid().clearData();

      detailFrame.getOrderedItemsPanel().setEnabled(false);
      detailFrame.getOrderedItemsPanel().getGrid().clearData();

    } return ok;
  }
  public WarehousesGridFrame getGridFrame() {
    return gridFrame;
  }





}
