package org.jallinone.contacts.client;

import java.beans.PropertyVetoException;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.customers.java.*;
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
import java.util.HashMap;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.openswing.swing.logger.client.Logger;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used to convert a contact to a customer.</p>
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
public class Contact2CustomerController extends CompanyFormController {

  /** detail frame */
  private Contact2CustomerDetailFrame detailFrame = null;

  /** v.o. of the contact */
  private Subject contactVO = null;

  /** contact detail frame */
  private ContactDetailFrame contactFrame = null;

  /** contacts grid frame */
  private ContactsGridFrame contactsFrame = null;


  public Contact2CustomerController(Subject contactVO,ContactDetailFrame contactFrame,ContactsGridFrame contactsFrame) {
    this.contactVO = contactVO;
    this.contactFrame = contactFrame;
    this.contactsFrame = contactsFrame;

    detailFrame = new Contact2CustomerDetailFrame(this);
    MDIFrame.add(detailFrame);

    contactFrame.pushFrame(detailFrame);
    detailFrame.setParentFrame(contactFrame);

    if (contactVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT))
      detailFrame.setSubjectType(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
    else
      detailFrame.setSubjectType(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
    detailFrame.getCurrentForm().insert();

  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData(
        "loadCustomer",
        new CustomerPK(contactVO.getCompanyCodeSys01REG04(),contactVO.getProgressiveREG04(),contactVO.getSubjectTypeREG04())
    );
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Subject subjectVO = (Subject)newPersistentObject;
    if (subjectVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
      OrganizationVO vo = (OrganizationVO)subjectVO;
      OrganizationVO contact1VO = (OrganizationVO)this.contactVO;
      vo.setAddressREG04(contact1VO.getAddressREG04());
      vo.setCityREG04(contact1VO.getCityREG04());
      vo.setCompanyCodeSys01REG04(contact1VO.getCompanyCodeSys01REG04());
      vo.setCompanyCodeSys01Reg04REG04(contact1VO.getCompanyCodeSys01Reg04REG04());
      vo.setCountryREG04(contact1VO.getCountryREG04());
      vo.setEmailAddressREG04(contact1VO.getEmailAddressREG04());
      vo.setFaxNumberREG04(contact1VO.getFaxNumberREG04());
      vo.setLawfulSiteREG04(contact1VO.getLawfulSiteREG04());
      vo.setName_1REG04(contact1VO.getName_1REG04());
      vo.setName_2REG04(contact1VO.getName_2REG04());
      vo.setNoteREG04(contact1VO.getNoteREG04());
      vo.setPhoneNumberREG04(contact1VO.getPhoneNumberREG04());
      vo.setProgressiveREG04(contact1VO.getProgressiveREG04());
      vo.setProgressiveReg04REG04(contact1VO.getProgressiveReg04REG04());
      vo.setProvinceREG04(contact1VO.getProvinceREG04());
      vo.setTaxCodeREG04(contact1VO.getTaxCodeREG04());
      vo.setWebSiteREG04(contact1VO.getWebSiteREG04());
      vo.setZipREG04(contact1VO.getZipREG04());
    }
    else {
      PeopleVO vo = (PeopleVO)subjectVO;
      PeopleVO contact1VO = (PeopleVO)this.contactVO;
      vo.setAddressREG04(contact1VO.getAddressREG04());
      vo.setBirthdayREG04(contact1VO.getBirthdayREG04());
      vo.setBirthplaceREG04(contact1VO.getBirthplaceREG04());
      vo.setCityREG04(contact1VO.getCityREG04());
      vo.setCompanyCodeSys01REG04(contact1VO.getCompanyCodeSys01REG04());
      vo.setCompanyCodeSys01Reg04REG04(contact1VO.getCompanyCodeSys01Reg04REG04());
      vo.setCountryREG04(contact1VO.getCountryREG04());
      vo.setEmailAddressREG04(contact1VO.getEmailAddressREG04());
      vo.setFaxNumberREG04(contact1VO.getFaxNumberREG04());
      vo.setMaritalStatusREG04(contact1VO.getMaritalStatusREG04());
      vo.setMobileNumberREG04(contact1VO.getMobileNumberREG04());
      vo.setName_1REG04(contact1VO.getName_1REG04());
      vo.setName_2REG04(contact1VO.getName_2REG04());
      vo.setNationalityREG04(contact1VO.getNationalityREG04());
      vo.setNoteREG04(contact1VO.getNoteREG04());
      vo.setPhoneNumberREG04(contact1VO.getPhoneNumberREG04());
      vo.setProgressiveREG04(contact1VO.getProgressiveREG04());
      vo.setProgressiveReg04REG04(contact1VO.getProgressiveReg04REG04());
      vo.setProvinceREG04(contact1VO.getProvinceREG04());
      vo.setSexREG04(contact1VO.getSexREG04());
      vo.setTaxCodeREG04(contact1VO.getTaxCodeREG04());
      vo.setWebSiteREG04(contact1VO.getWebSiteREG04());
      vo.setZipREG04(contact1VO.getZipREG04());
    }

    Response response = ClientUtils.getData("insertCustomer",subjectVO);
    if (response.isError()) {
      throw new RuntimeException(response.getErrorMessage());
    }
    return response;
  }


  public void afterInsertData() {
    super.afterInsertData();
    contactsFrame.reloadData();
    try {
      contactFrame.closeFrame();
    } catch (PropertyVetoException ex) {
      Logger.error(this.getClass().getName(),"afterInsertData",ex.getMessage(),ex);
    }
  }



  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject PersistentObject) throws Exception {
    ((Subject) PersistentObject).setCompanyCodeSys01REG04(contactVO.getCompanyCodeSys01REG04());
    if (contactVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) {
      ((Subject) PersistentObject).setSubjectTypeREG04(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
    } else {
      ((Subject) PersistentObject).setSubjectTypeREG04(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
    }

    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getClientFacade()).getMainClass();
    String companyCode = null;
    if (applet.getAuthorizations().isOneCompany()) {
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SAL07");
      companyCode = companiesList.get(0).toString();
    }
    // set default values...
    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CREDITS_ACCOUNT);
    Response response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlCreditsCode().setValue(code);
      detailFrame.getControlCreditsCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ITEMS_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlItemsCode() .setValue(code);
      detailFrame.getControlItemsCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ACTIVITIES_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlActCode() .setValue(code);
      detailFrame.getControlActCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CHARGES_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlChargesCode().setValue(code);
      detailFrame.getControlChargesCode().getLookupController().forceValidate();
    }

  }




}
