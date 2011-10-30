package org.jallinone.contacts.java;

import java.math.BigDecimal;

import org.jallinone.subjects.java.Subject;
import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to store an contact (organization or person) limited info.</p>
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
public class GridContactVO extends ValueObjectImpl implements Subject {

  private String companyCodeSys01REG04;
  private String subjectTypeREG04;
  private String name_1REG04;
  private String name_2REG04;
  private String phoneNumberREG04;
  private String cityREG04;
  private String provinceREG04;
  private String countryREG04;
  private BigDecimal progressiveREG04;
  private BigDecimal progressiveReg04REG04;
  private String organizationName_1REG04;

  public GridContactVO() {
  }


  public String getCityREG04() {
    return cityREG04;
  }
  public String getCountryREG04() {
    return countryREG04;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }

  public String getPhoneNumberREG04() {
    return phoneNumberREG04;
  }
  public String getProvinceREG04() {
    return provinceREG04;
  }
  public String getSubjectTypeREG04() {
    return subjectTypeREG04;
  }
  public void setSubjectTypeREG04(String subjectTypeREG04) {
    this.subjectTypeREG04 = subjectTypeREG04;
  }
  public void setProvinceREG04(String provinceREG04) {
    this.provinceREG04 = provinceREG04;
  }
  public void setPhoneNumberREG04(String phoneNumberREG04) {
    this.phoneNumberREG04 = phoneNumberREG04;
  }

  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public void setCountryREG04(String countryREG04) {
    this.countryREG04 = countryREG04;
  }
  public void setCityREG04(String cityREG04) {
    this.cityREG04 = cityREG04;
  }
  public String getCompanyCodeSys01REG04() {
    return companyCodeSys01REG04;
  }
  public void setCompanyCodeSys01REG04(String companyCodeSys01REG04) {
    this.companyCodeSys01REG04 = companyCodeSys01REG04;
  }
  public BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public void setProgressiveREG04(BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public String getOrganizationName_1REG04() {
    return organizationName_1REG04;
  }
  public void setOrganizationName_1REG04(String organizationName_1REG04) {
    this.organizationName_1REG04 = organizationName_1REG04;
  }
  public BigDecimal getProgressiveReg04REG04() {
    return progressiveReg04REG04;
  }
  public void setProgressiveReg04REG04(BigDecimal progressiveReg04REG04) {
    this.progressiveReg04REG04 = progressiveReg04REG04;
  }
}
