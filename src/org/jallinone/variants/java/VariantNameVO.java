package org.jallinone.variants.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a variant name.</p>
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
public class VariantNameVO extends ValueObjectImpl {


  private String companyCodeSys01ITM21;
  private String tableName;
  private BigDecimal progressiveSys10ITM21;
  private String descriptionSYS10;
  private Boolean useVariantTypeITM21;




  public BigDecimal getProgressiveSys10ITM21() {
    return progressiveSys10ITM21;
  }
  public void setProgressiveSys10ITM21(BigDecimal progressiveSys10ITM21) {
    this.progressiveSys10ITM21 = progressiveSys10ITM21;
  }
  public String getCompanyCodeSys01ITM21() {
    return companyCodeSys01ITM21;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setCompanyCodeSys01ITM21(String companyCodeSys01ITM21) {
    this.companyCodeSys01ITM21 = companyCodeSys01ITM21;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public Boolean getUseVariantTypeITM21() {
    return useVariantTypeITM21;
  }
  public void setUseVariantTypeITM21(Boolean useVariantTypeITM21) {
    this.useVariantTypeITM21 = useVariantTypeITM21;
  }



}
