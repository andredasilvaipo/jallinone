package org.jallinone.purchases.suppliers.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.subjects.java.Subject;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.suppliers.java.DetailSupplierVO;
import org.openswing.swing.tree.client.TreeController;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for supplier detail frame.</p>
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
public class SupplierController extends CompanyFormController implements TreeController {

  /** pk */
  private SubjectPK pk = null;

  /** detail frame */
  private SupplierDetailFrame detailFrame = null;

  /** parent frame */
  private SuppliersGridFrame gridFrame = null;


  public SupplierController(SuppliersGridFrame gridFrame,SubjectPK pk) {
    this.gridFrame = gridFrame;
    this.pk = pk;

    detailFrame = new SupplierDetailFrame(this);
    MDIFrame.add(detailFrame,true);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (pk!=null) {
      detailFrame.getCurrentForm().setMode(Consts.READONLY);
      detailFrame.getCurrentForm().executeReload();
    }
    else {
      detailFrame.getCurrentForm().insert();
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
      Subject gridVO = (Subject)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
      pk = new SubjectPK(gridVO.getCompanyCodeSys01REG04(),gridVO.getProgressiveREG04());
    }


    return ClientUtils.getData("loadSupplier",pk);
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    if (!error) {
      DetailSupplierVO vo = (DetailSupplierVO)detailFrame.getCurrentForm().getVOModel().getValueObject();
      detailFrame.setTitle(ClientSettings.getInstance().getResources().getResource("supplier")+" "+vo.getSupplierCodePUR01()+" - "+vo.getName_1REG04());

      detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01REG04(),pk.getProgressiveREG04())
      );
      detailFrame.getReferencesPanel().getGrid().reloadData();
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01REG04(),pk.getProgressiveREG04())
      );
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_TYPE,
          ApplicationConsts.SUBJECT_SUPPLIER
      );
      detailFrame.getHierarchiesPanel().getGrid().reloadData();

      detailFrame.getSupplierPricelistPanel().init(vo);
      detailFrame.getSupplierPricelistPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
      detailFrame.getSupplierPricelistPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
      detailFrame.getSupplierPricelistPanel().getGrid().reloadData();

      detailFrame.getTreePanel().reloadTree();

      detailFrame.setButtonsEnabled(true);
    }
  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);
    if (ok) {
      detailFrame.getReferencesPanel().getGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
      detailFrame.getTreePanel().clearTree();
      detailFrame.getSupplierPricelistPanel().getGrid().clearData();
      detailFrame.getSupplierPricelistPanel().getPricesGrid().clearData();
      detailFrame.setButtonsEnabled(false);

      detailFrame.setTitle(ClientSettings.getInstance().getResources().getResource("supplier"));

    }
    return ok;
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response response = ClientUtils.getData("insertSupplier",newPersistentObject);
    if (!response.isError()) {
      DetailSupplierVO vo = (DetailSupplierVO)((VOResponse)response).getVo();
      pk = new SubjectPK(
        vo.getCompanyCodeSys01REG04(),
        vo.getProgressiveREG04()
      );
//      gridFrame.reloadData();
      detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01REG04(),pk.getProgressiveREG04())
      );
      detailFrame.getReferencesPanel().getGrid().reloadData();
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01REG04(),pk.getProgressiveREG04())
      );
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_TYPE,
          ApplicationConsts.SUBJECT_SUPPLIER
      );
      detailFrame.getHierarchiesPanel().getGrid().reloadData();

      detailFrame.getTreePanel().reloadTree();
      detailFrame.getSupplierPricelistPanel().init(vo);
      detailFrame.getSupplierPricelistPanel().getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
      detailFrame.getSupplierPricelistPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
      detailFrame.getSupplierPricelistPanel().getGrid().reloadData();

      detailFrame.setButtonsEnabled(true);
    }
    return response;
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response response = ClientUtils.getData("updateSupplier",new ValueObject[]{oldPersistentObject,persistentObject});
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
    ArrayList list = new ArrayList();
    DetailSupplierVO vo = (DetailSupplierVO)persistentObject;
    list.add(new SubjectPK(vo.getCompanyCodeSys01REG04(),vo.getProgressiveREG04()));
    Response response = ClientUtils.getData("deleteSuppliers",list);
    if (!response.isError()) {
      gridFrame.reloadData();
      detailFrame.getReferencesPanel().getGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
      detailFrame.getTreePanel().clearTree();
      detailFrame.getSupplierPricelistPanel().getGrid().clearData();
      detailFrame.getSupplierPricelistPanel().getPricesGrid().clearData();
    }
    return response;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject PersistentObject) throws Exception {
    DetailSupplierVO vo = (DetailSupplierVO)PersistentObject;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    if (applet.getAuthorizations().isOneCompany()) {
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("PUR01");
      vo.setCompanyCodeSys01REG04( companiesList.get(0).toString() );
    }

    // set default values...
    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.DEBITS_ACCOUNT);
    Response response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      vo.setDebitAccountCodeAcc02PUR01(code);
      detailFrame.getControlDebitsCode().setValue(code);
      detailFrame.getControlDebitsCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.COSTS_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      vo.setCostsAccountCodeAcc02PUR01(code);
      detailFrame.getControlCostsCode().setValue(code);
      detailFrame.getControlCostsCode().getLookupController().forceValidate();
    }

  }


  /**
   * Callback method invoked when the user has clicked the left mouse button.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
    try {
      HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
      HierarchyLevelVO root = (HierarchyLevelVO)((DefaultMutableTreeNode)node.getRoot()).getUserObject();
      detailFrame.getItemsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
      detailFrame.getItemsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
      detailFrame.getItemsGrid().getOtherGridParams().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());
      detailFrame.getItemsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,pk.getProgressiveREG04());
      detailFrame.getItemsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01REG04());


      detailFrame.getItemDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01REG04());
      detailFrame.getItemDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
      detailFrame.getItemDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
      detailFrame.getItemDataLocator().getLookupValidationParameters().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());
      detailFrame.getItemDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01REG04());
      detailFrame.getItemDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
      detailFrame.getItemDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHIE01());
      detailFrame.getItemDataLocator().getLookupFrameParams().put(ApplicationConsts.ROOT_PROGRESSIVE_HIE01,root.getProgressiveHIE01());


      detailFrame.getItemsGrid().reloadData();
    }
    catch (Exception ex) {
    }

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
  public SuppliersGridFrame getGridFrame() {
    return gridFrame;
  }




}
