package org.jallinone.system.server;

import java.util.HashMap;
import java.util.HashSet;

import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.system.java.CustomizedWindows;
import org.openswing.swing.server.UserSessionParameters;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class to store client session info.
 * It contains also the server language code (used by SYS09...)</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
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
public class JAIOUserSessionParameters extends UserSessionParameters {

  /** server language identifier */
  private String serverLanguageId;

  /** buttons authorizations per company code */
  private ButtonCompanyAuthorizations companyBa = null;

  /** roles associated to the user (role identifier, role description) */
  private HashMap userRoles;

  /** customized windows */
  private CustomizedWindows customizedWindows;

  /** application parameters */
  private HashMap appParams;

  /** employee identifier (optional) */
  private java.math.BigDecimal progressiveReg04SYS03;

  /** first name (optional) */
  private String name_1;

  /** last name (optional) */
  private String name_2;

  /** employee code (optional) */
  private String employeeCode;

  /** employee company code */
  private String companyCodeSys01SYS03;

  /** default company code */
  private String defCompanyCodeSys01SYS03;

  /** collection of FUNCTION_CODEs having a permission in SYS02 */
  private HashSet functionCodesBasedOnCompany = new HashSet();

  /** cache of VariantDescriptionsVO object, one for each company code */
  private HashMap variantDescriptionsVO = new HashMap();


  public JAIOUserSessionParameters() { }


  /**
   * @return server language identifier
   */
  public final String getServerLanguageId() {
    return serverLanguageId;
  }


  /**
   * Set the server language identifier.
   * @param serverLanguageId server language identifier
   */
  public final void setServerLanguageId(String serverLanguageId) {
    this.serverLanguageId = serverLanguageId;
  }


  /**
   * @return roles associated to thr user
   */
  public final HashMap getUserRoles() {
    return userRoles;
  }


  /**
   * Set the roles associated to thr user.
   * @param userRoles roles associated to thr user
   */
  public final void setUserRoles(HashMap userRoles) {
    this.userRoles = userRoles;
  }


  /**
   * @return buttons authorizations per company code
   */
  public final ButtonCompanyAuthorizations getCompanyBa() {
    return companyBa;
  }


  /**
   * Set the buttons authorizations per company code.
   * @param companyBa buttons authorizations per company code
   */
  public final void setCompanyBa(ButtonCompanyAuthorizations companyBa) {
    this.companyBa = companyBa;
  }


  /**
   * @return customized windows
   */
  public final CustomizedWindows getCustomizedWindows() {
    return customizedWindows;
  }


  /**
   * Set the customized windows.
   * @param customizedWindows customized windows
   */
  public final void setCustomizedWindows(CustomizedWindows customizedWindows) {
    this.customizedWindows = customizedWindows;
  }


  /**
   * @return application parameters
   */
  public final HashMap getAppParams() {
    return appParams;
  }


  /**
   * Set application parameters.
   * @param appParams application parameters
   */
  public final void setAppParams(HashMap appParams) {
    this.appParams = appParams;
  }


  /**
   * @return employee identifier (optional)
   */
  public final java.math.BigDecimal getProgressiveReg04SYS03() {
    return progressiveReg04SYS03;
  }


  /**
   * Set the employee identifier (optional).
   * @param progressiveReg04SYS03 employee identifier (optional)
   */
  public final void setProgressiveReg04SYS03(java.math.BigDecimal progressiveReg04SYS03) {
    this.progressiveReg04SYS03 = progressiveReg04SYS03;
  }


  /**
   * @return first name
   */
  public final String getName_1() {
    return name_1;
  }


  /**
   * Set the first name.
   * @param name_1 first name
   */
  public final void setName_1(String name_1) {
    this.name_1 = name_1;
  }


  /**
   * @return last name
   */
  public final String getName_2() {
    return name_2;
  }


  /**
   * Set the last name.
   * @param name_2 last name
   */
  public final void setName_2(String name_2) {
    this.name_2 = name_2;
  }


  /**
   * @return String employee code (optional)
   */
  public final String getEmployeeCode() {
    return employeeCode;
  }


  /**
   * Set the employee code (optional).
   * @param employeeCode employee code (optional)
   */
  public final void setEmployeeCode(String employeeCode) {
    this.employeeCode = employeeCode;
  }


  /**
   * @return employee company code
   */
  public final String getCompanyCodeSys01SYS03() {
    return companyCodeSys01SYS03;
  }


  /**
   * Set employee company code
   * @param companyCodeSys01SYS03 employee company code
   */
  public final void setCompanyCodeSys01SYS03(String companyCodeSys01SYS03) {
    this.companyCodeSys01SYS03 = companyCodeSys01SYS03;
  }


  /**
   * @return default company code
   */
  public final String getDefCompanyCodeSys01SYS03() {
    return defCompanyCodeSys01SYS03;
  }


  /**
   * Set the default company code.
   * @param defCompanyCodeSys01SYS03 default company code
   */
  public final void setDefCompanyCodeSys01SYS03(String defCompanyCodeSys01SYS03) {
    this.defCompanyCodeSys01SYS03 = defCompanyCodeSys01SYS03;
  }


  /**
   * @return collection of FUNCTION_CODEs having a permission in SYS02
   */
  public final HashSet getFunctionCodesBasedOnCompany() {
    return functionCodesBasedOnCompany;
  }


  /**
   * Set the collection of FUNCTION_CODEs having a permission in SYS02.
   * @param functionCodesBasedOnCompany collection of FUNCTION_CODEs having a permission in SYS02
   */
  public final void setFunctionCodesBasedOnCompany(HashSet functionCodesBasedOnCompany) {
    this.functionCodesBasedOnCompany = functionCodesBasedOnCompany;
  }
  public HashMap getVariantDescriptionsVO() {
    return variantDescriptionsVO;
  }
  public void setVariantDescriptionsVO(HashMap variantDescriptionsVO) {
    this.variantDescriptionsVO = variantDescriptionsVO;
  }


}
