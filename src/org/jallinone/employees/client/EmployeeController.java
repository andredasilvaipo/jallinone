package org.jallinone.employees.client;

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
import java.math.BigDecimal;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.employees.java.DetailEmployeeVO;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for employee detail frame.</p>
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
public class EmployeeController extends CompanyFormController {

  /** pks */
  private String companyCodeSys01SCH01 = null;
  private BigDecimal progressiveReg04SCH01 = null;

  /** detail frame */
  private EmployeeDetailFrame detailFrame = null;

  /** parent frame */
  private EmployeesGridFrame gridFrame = null;


  public EmployeeController(EmployeesGridFrame gridFrame,String companyCodeSys01SCH01,BigDecimal progressiveReg04SCH01) {
    this.gridFrame = gridFrame;
    this.companyCodeSys01SCH01 = companyCodeSys01SCH01;
    this.progressiveReg04SCH01 = progressiveReg04SCH01;

    detailFrame = new EmployeeDetailFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (companyCodeSys01SCH01!=null && progressiveReg04SCH01!=null) {
      detailFrame.getForm().setMode(Consts.READONLY);
      detailFrame.getForm().executeReload();
    }
    else {
      detailFrame.getForm().insert();
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadEmployee",new SubjectPK(companyCodeSys01SCH01,progressiveReg04SCH01));
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01SCH01);
    detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,progressiveReg04SCH01);
    detailFrame.getGrid().reloadData();

    DetailEmployeeVO vo = (DetailEmployeeVO)detailFrame.getForm().getVOModel().getValueObject();
    detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH01());
    detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH01());
    String actTypes = "'"+ApplicationConsts.ACT_ABSENCE+"','"+ApplicationConsts.ACT_HOLIDAY+"','"+ApplicationConsts.ACT_ILLNESS+"'";
    detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.ACTIVITY_TYPE,actTypes);
    detailFrame.getAbsGrid().reloadData();

    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        new SubjectPK(vo.getCompanyCodeSys01SCH01(),vo.getProgressiveReg04SCH01())
    );
    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_TYPE,
        ApplicationConsts.SUBJECT_EMPLOYEE
    );
    detailFrame.getHierarchiesPanel().getGrid().reloadData();

  }



  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response response = ClientUtils.getData("insertEmployee",newPersistentObject);
    if (!response.isError()) {
      DetailEmployeeVO vo = (DetailEmployeeVO)((VOResponse)response).getVo();
      companyCodeSys01SCH01 = vo.getCompanyCodeSys01REG04();
      progressiveReg04SCH01 = vo.getProgressiveREG04();
      gridFrame.reloadData();

      detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01SCH01);
      detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,progressiveReg04SCH01);
      detailFrame.getGrid().reloadData();

      detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01SCH01);
      detailFrame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,progressiveReg04SCH01);
      detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH01());
      detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH01());
      String actTypes = "'"+ApplicationConsts.ACT_ABSENCE+"','"+ApplicationConsts.ACT_HOLIDAY+"','"+ApplicationConsts.ACT_ILLNESS+"'";
      detailFrame.getAbsGrid().getOtherGridParams().put(ApplicationConsts.ACTIVITY_TYPE,actTypes);

      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(vo.getCompanyCodeSys01SCH01(),vo.getProgressiveReg04SCH01())
      );
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_TYPE,
          ApplicationConsts.SUBJECT_EMPLOYEE
      );
      detailFrame.getHierarchiesPanel().getGrid().reloadData();

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
    Response response = ClientUtils.getData("updateEmployee",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!response.isError()) {
      gridFrame.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    DetailEmployeeVO vo = (DetailEmployeeVO)persistentObject;
    Response response = ClientUtils.getData("deleteEmployee",new SubjectPK(vo.getCompanyCodeSys01REG04(),vo.getProgressiveReg04SCH01()));
    if (!response.isError()) {
      gridFrame.reloadData();
      detailFrame.getGrid().clearData();
      detailFrame.getAbsGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
    }
    return response;
  }


  /**
   * Callback method invoked on pressing INSERT button, after changing to insert mode.
   */
  public void afterInsertData(Form form) {
    detailFrame.getGrid().clearData();
    detailFrame.getAbsGrid().clearData();
    detailFrame.getHierarchiesPanel().getGrid().clearData();
  }




}
