package org.jallinone.system.java;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.tree.DefaultTreeModel;

import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.ValueObjectImpl;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object containing all application parameters,
 * like user authorizations (functions, buttons), parameters, et.al.</p>
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
public class ApplicationParametersVO extends ValueObjectImpl {


  /** buttons authorizations, filtered by the logged user */
  private ButtonsAuthorizations ba = null;

  /** application tree menu, filtered by the logged user */
  private DefaultTreeModel appMenu = null;

  /** buttons authorizations per company code */
  private ButtonCompanyAuthorizations companyBa = null;

  /** language used */
  private String languageId = null;

  /** <code>true</code> if there is only one company defined, <code>false</code> if there are more than one company */
  private boolean oneCompany;

  /** customized windows */
  private CustomizedWindows customizedWindows;

  /** application parameters, stored in SYS11 table */
  private HashMap applicationPars;

  /** collection of roles associated to the user (role identifier, role description) */
  private HashMap userRoles = new HashMap();

  /** employee identifier (optional) */
  private BigDecimal progressiveReg04SYS03;

  /** first name (optional) */
  private String name_1;

  /** last name (optional) */
  private String name_2;

  /** employee code (optional) */
  private String employeeCode;

  /** company employee (optional) */
  private String companyCodeSys01SYS03;

  /** collection of pairs <functionId of grid,associated digest> */
  private HashMap lastGridPermissionsDigests = new HashMap();

  /** collection of pairs <functionId of grid,GridPermissions partial content> */
  private HashMap gridPermissions = new HashMap();


  public ApplicationParametersVO() {}


  /**
   * Costructor,
   * @param languageId language used
   * @param appMenu application tree menu, filtered by the logged user
   * @param ba buttons authorizations
   * @param companyBa buttons authorizations per company code
   * @param oneCompany <code>true</code> if there is only one company defined, <code>false</code> if there are more than one company
   * @param customizedWindows customized windows
   * @param application parameters, stored in SYS11 table
   * @param userRoles collection of roles associated to the user
   * @param progressiveReg04SYS03 employee identifier (optional)
   * @param name_1 first name (optional)
   * @param name_2 last name (optional)
   * @param employeeCode employee code (optional)
   * @param company employee (optional)
   */
  public ApplicationParametersVO(
      String languageId,
      DefaultTreeModel appMenu,
      ButtonsAuthorizations ba,
      ButtonCompanyAuthorizations companyBa,
      boolean oneCompany,
      CustomizedWindows customizedWindows,
      HashMap applicationPars,
      HashMap userRoles,
      BigDecimal progressiveReg04SYS03,
      String name_1,
      String name_2,
      String employeeCode,
      String companyCodeSys01SYS03,
      HashMap lastGridPermissionsDigests,
      HashMap gridPermissions
  ) {
    this.languageId = languageId;
    this.appMenu = appMenu;
    this.ba = ba;
    this.companyBa = companyBa;
    this.oneCompany = oneCompany;
    this.customizedWindows = customizedWindows;
    this.applicationPars = applicationPars;
    this.userRoles = userRoles;
    this.progressiveReg04SYS03 = progressiveReg04SYS03;
    this.name_1 = name_1;
    this.name_2 = name_2;
    this.employeeCode = employeeCode;
    this.companyCodeSys01SYS03 = companyCodeSys01SYS03;
    this.lastGridPermissionsDigests = lastGridPermissionsDigests;
    this.gridPermissions = gridPermissions;
  }


  /**
   * @return application tree menu, filtered by the logged user
   */
  public final DefaultTreeModel getAppMenu() {
    return appMenu;
  }


  /**
   * @return buttons authorizations, filtered by the logged user
   */
  public final ButtonsAuthorizations getBa() {
    return ba;
  }


  /**
   * @return buttons authorizations per company code
   */
  public final ButtonCompanyAuthorizations getCompanyBa() {
    return companyBa;
  }


  /**
   * @return language used
   */
  public final String getLanguageId() {
    return languageId;
  }


  /**
   * @return <code>true</code> if there is only one company defined, <code>false</code> if there are more than one company
   */
  public final boolean isOneCompany() {
    return oneCompany;
  }


  /**
   * @return customized windows
   */
  public final CustomizedWindows getCustomizedWindows() {
    return customizedWindows;
  }


  /**
   * @return application parameters, stored in SYS11 table
   */
  public final HashMap getApplicationPars() {
    return applicationPars;
  }


  /**
   * @return collection of roles associated to the user
   */
  public final HashMap getUserRoles() {
    return userRoles;
  }


  /**
   * Set image repository path.
   * @param imagePath image repository path
   */
  public final void setImagePath(String imagePath) {
    applicationPars.put(ApplicationConsts.IMAGE_PATH,imagePath);
  }


  /**
   * @erturn image repository path
   */
  public final String getImagePath() {
    return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.IMAGE_PATH);
  }


  /**
   * Set document repository path.
   * @param documentPath document repository path
   */
  public final void setDocumentPath(String imagePath) {
    applicationPars.put(ApplicationConsts.DOC_PATH,imagePath);
  }


  /**
   * @return document repository path
   */
  public final String getDocumentPath() {
    return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.DOC_PATH);
  }


	/**
	 * @return flag used to manually insert doc num in sale docs
	 */
	 public final Boolean getManualDocNumInSaleDocs() {
		if (applicationPars==null)
			return null;
		String value = (String)applicationPars.get(ApplicationConsts.DOC_NUM_IN_SALE_DOCS);
		return new Boolean("Y".equals(value));
	 }


  /**
   * @return employee identifier (optional)
   */
  public final BigDecimal getProgressiveReg04SYS03() {
    return progressiveReg04SYS03;
  }


  /**
   * @return first name
   */
  public final String getName_1() {
    return name_1;
  }


  /**
   * @return last name
   */
  public final String getName_2() {
    return name_2;
  }


  /**
   * @return String employee code (optional)
   */
  public final String getEmployeeCode() {
    return employeeCode;
  }


  /**
   * @return company employee (optional)
   */
  public final String getCompanyCodeSys01SYS03() {
    return companyCodeSys01SYS03;
  }



  /**
   * Set increment value for progressives
   * @param imagePath increment value for progressives
   */
  public final void setIncrementValue(BigDecimal incrementValue) {
    applicationPars.put(ApplicationConsts.INCREMENT_VALUE,incrementValue.toString());
  }


  /**
   * @erturn increment value for progressives
   */
  public final BigDecimal getIncrementValue() {
	  String value = applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.INCREMENT_VALUE);
	  return value==null?null:new BigDecimal(value);
  }


  public HashMap getLastGridPermissionsDigests() {
	  return lastGridPermissionsDigests;
  }


  public void setLastGridPermissionsDigests(HashMap lastGridPermissionsDigests) {
	  this.lastGridPermissionsDigests = lastGridPermissionsDigests;
  }
  public HashMap getGridPermissions() {
	  return gridPermissions;
  }


  public void setBa(ButtonsAuthorizations ba) {
	  this.ba = ba;
  }


  public void setAppMenu(DefaultTreeModel appMenu) {
	  this.appMenu = appMenu;
  }


  public void setCompanyBa(ButtonCompanyAuthorizations companyBa) {
	  this.companyBa = companyBa;
  }


  public void setLanguageId(String languageId) {
	  this.languageId = languageId;
  }


  public void setOneCompany(boolean oneCompany) {
	  this.oneCompany = oneCompany;
  }


  public void setCustomizedWindows(CustomizedWindows customizedWindows) {
	  this.customizedWindows = customizedWindows;
  }


  public void setApplicationPars(HashMap applicationPars) {
	  this.applicationPars = applicationPars;
  }


  public void setUserRoles(HashMap userRoles) {
	  this.userRoles = userRoles;
  }


  public void setProgressiveReg04SYS03(BigDecimal progressiveReg04SYS03) {
	  this.progressiveReg04SYS03 = progressiveReg04SYS03;
  }


  public void setName_1(String name_1) {
	  this.name_1 = name_1;
  }


  public void setName_2(String name_2) {
	  this.name_2 = name_2;
  }


  public void setEmployeeCode(String employeeCode) {
	  this.employeeCode = employeeCode;
  }


  public void setCompanyCodeSys01SYS03(String companyCodeSys01SYS03) {
	  this.companyCodeSys01SYS03 = companyCodeSys01SYS03;
  }


  public void setGridPermissions(HashMap gridPermissions) {
	  this.gridPermissions = gridPermissions;
  }













		public final String getInvNegCorrForGoodItemsValue() {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.NEG_INVCORR_GOOD_ITM);
		}

		public final void setInvNegCorrForGoodItemsValue(String value) {
			applicationPars.put(ApplicationConsts.NEG_INVCORR_GOOD_ITM,value);
		}


		public final String getInvPosCorrForGoodItemsValue() {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.POS_INVCORR_GOOD_ITM);
		}

		public final void setInvPosCorrForGoodItemsValue(String value) {
			applicationPars.put(ApplicationConsts.POS_INVCORR_GOOD_ITM,value);
		}


		public final String getInvNegCorrForDamagedItemsValue() {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.NEG_INVCORR_DAMG_ITM);
		}

		public final void setInvNegCorrForDamagedItemsValue(String value) {
			applicationPars.put(ApplicationConsts.NEG_INVCORR_DAMG_ITM,value);
		}


		public final String getInvPosCorrForDamagedItemsValue() {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.POS_INVCORR_DAMG_ITM);
		}

		public final void setInvPosCorrForDamagedItemsValue(String value) {
			applicationPars.put(ApplicationConsts.POS_INVCORR_DAMG_ITM,value);
		}

		 public final void setManualDocNumInSaleDocs(Boolean value) {
			applicationPars.put(ApplicationConsts.DOC_NUM_IN_SALE_DOCS,value.booleanValue()?"Y":"N");
		 }


}
