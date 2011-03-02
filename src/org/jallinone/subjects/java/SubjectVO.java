package org.jallinone.subjects.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a generic subject.</p>
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
public class SubjectVO extends ValueObjectImpl implements Subject {

  private String companyCodeSys01REG04;
  private java.math.BigDecimal progressiveREG04;
  private String name_1REG04;
  private String name_2REG04;
  private String subjectTypeREG04;


  public SubjectVO() {}

  public SubjectVO(String companyCodeSys01REG04,BigDecimal progressiveREG04,String name_1REG04,String name_2REG04,String subjectTypeREG04) {
    this.companyCodeSys01REG04 = companyCodeSys01REG04;
    this.progressiveREG04 = progressiveREG04;
    this.name_1REG04 = name_1REG04;
    this.name_2REG04 = name_2REG04;
    this.subjectTypeREG04 = subjectTypeREG04;
  }


  public String getCompanyCodeSys01REG04() {
    return companyCodeSys01REG04;
  }
  public void setCompanyCodeSys01REG04(String companyCodeSys01REG04) {
    this.companyCodeSys01REG04 = companyCodeSys01REG04;
  }
  public java.math.BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public void setProgressiveREG04(java.math.BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public String getSubjectTypeREG04() {
    return subjectTypeREG04;
  }
  public void setSubjectTypeREG04(String subjectTypeREG04) {
    this.subjectTypeREG04 = subjectTypeREG04;
  }


}
