package org.jallinone.sales.agents.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an agent info.</p>
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
public class AgentVO extends BaseValueObject {

  private java.math.BigDecimal progressiveSys10SAL10;
  private String agentCodeSAL10;
  private String descriptionSYS10;
  private String companyCodeSys01SAL10;
  private java.math.BigDecimal percentageSAL10;
  private java.math.BigDecimal progressiveReg04SAL10;
  private String name_1REG04;
  private String name_2REG04;


  public AgentVO() {
  }


  public java.math.BigDecimal getProgressiveSys10SAL10() {
    return progressiveSys10SAL10;
  }
  public void setProgressiveSys10SAL10(java.math.BigDecimal progressiveSys10SAL10) {
    this.progressiveSys10SAL10 = progressiveSys10SAL10;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getAgentCodeSAL10() {
    return agentCodeSAL10;
  }
  public void setAgentCodeSAL10(String agentCodeSAL10) {
    this.agentCodeSAL10 = agentCodeSAL10;
  }
  public String getCompanyCodeSys01SAL10() {
    return companyCodeSys01SAL10;
  }
  public void setCompanyCodeSys01SAL10(String companyCodeSys01SAL10) {
    this.companyCodeSys01SAL10 = companyCodeSys01SAL10;
  }
  public java.math.BigDecimal getPercentageSAL10() {
    return percentageSAL10;
  }
  public void setPercentageSAL10(java.math.BigDecimal percentageSAL10) {
    this.percentageSAL10 = percentageSAL10;
  }
  public java.math.BigDecimal getProgressiveReg04SAL10() {
    return progressiveReg04SAL10;
  }
  public void setProgressiveReg04SAL10(java.math.BigDecimal progressiveReg04SAL10) {
    this.progressiveReg04SAL10 = progressiveReg04SAL10;
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

}