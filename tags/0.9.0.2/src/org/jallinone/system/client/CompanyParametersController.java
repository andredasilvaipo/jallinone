package org.jallinone.system.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form controller used to store company parameters, based on the specified company code.</p>
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


public class CompanyParametersController extends FormController {

  /** detail frame */
  private CompanyParametersFrame detailFrame = null;


  public CompanyParametersController() {
    detailFrame = new CompanyParametersFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.getAccountPanel().setMode(Consts.READONLY);
    detailFrame.getControlCompanies().getComboBox().setSelectedIndex(0);
//    detailFrame.getMainPanel().executeReload();
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    String companyCode = null;
    try {
      companyCode = (String) detailFrame.getControlCompanies().getValue();
    }
    catch (Exception ex) {
    }
    if (companyCode==null)
      companyCode = (String)detailFrame.getControlCompanies().getDomain().getDomainPairList()[0].getCode();
    return ClientUtils.getData("loadCompanyParams",companyCode);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    return ClientUtils.getData("updateCompanyParams",new ValueObject[]{oldPersistentObject,persistentObject});
  }





}
