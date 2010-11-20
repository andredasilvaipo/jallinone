package org.jallinone.system.companies.client;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.mdi.client.MDIFrame;
import java.math.BigDecimal;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.subjects.java.SubjectPK;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.subjects.java.OrganizationVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller related to the detail frame of a company.</p>
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
public class CompanyController extends FormController {

  /** company detail frame */
  private CompanyDetailFrame detailFrame = null;

  /** parent frame */
  private CompaniesGridFrame gridFrame = null;

  /** pk */
  private String companyCodeSys01REG04 = null;
  private BigDecimal progressiveREG04 = null;


  public CompanyController(
      CompaniesGridFrame gridFrame,
      String companyCodeSys01REG04,
      BigDecimal progressiveREG04
  ) {
    this.gridFrame = gridFrame;
    this.companyCodeSys01REG04 = companyCodeSys01REG04;
    this.progressiveREG04 = progressiveREG04;

    detailFrame = new CompanyDetailFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (companyCodeSys01REG04!=null && progressiveREG04!=null) {
      detailFrame.getOrganizationPanel().setMode(Consts.READONLY);
//      detailFrame.getOrganizationPanel().reload();
      detailFrame.getOrganizationPanel().executeReload();
    }
    else {
      detailFrame.getOrganizationPanel().insert();
//      detailFrame.getOrganizationPanel().setMode(Consts.INSERT);
//      detailFrame.getSaveButton().setEnabled(true);
//      detailFrame.getDeleteButton().setEnabled(false);
//      detailFrame.getEditButton().setEnabled(false);
//      detailFrame.getInsertButton().setEnabled(false);
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadCompany",companyCodeSys01REG04);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response response = ClientUtils.getData("insertCompany",newPersistentObject);
    if (!response.isError()) {
      companyCodeSys01REG04 = ((OrganizationVO)newPersistentObject).getCompanyCodeSys01REG04();
      progressiveREG04 = ((OrganizationVO)newPersistentObject).getProgressiveREG04();
      gridFrame.reloadData();
    }
    return response;
  }


		/**
		 * Callback method called after saving SUCCESSFULLY data in INSERT mode.
		 */
		public void afterInsertData() {
			detailFrame.getOrganizationPanel().reload();
		}


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response response = ClientUtils.getData("updateCompany",new ValueObject[]{oldPersistentObject,persistentObject});
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
    OrganizationVO vo = (OrganizationVO)persistentObject;
    SubjectPK pk = new SubjectPK(vo.getCompanyCodeSys01REG04(),vo.getProgressiveREG04());
    Response response = ClientUtils.getData("deleteCompany",pk);
    if (!response.isError()) {
      gridFrame.reloadData();
    }
    return response;
  }









}
