package org.jallinone.variants.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a variant type.</p>
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
public class VariantTypeVO extends ValueObjectImpl {


  private String companyCodeSys01;
  private String variantType;
  private BigDecimal progressiveSys10;
  private String descriptionSys10;
  private String enabled;


  public VariantTypeVO() {}


  public String getCompanyCodeSys01() {
    return companyCodeSys01;
  }
  public String getDescriptionSys10() {
    return descriptionSys10;
  }
  public BigDecimal getProgressiveSys10() {
    return progressiveSys10;
  }
  public String getVariantType() {
    return variantType;
  }
  public void setVariantType(String variantType) {
    this.variantType = variantType;
  }
  public void setProgressiveSys10(BigDecimal progressiveSys10) {
    this.progressiveSys10 = progressiveSys10;
  }
  public void setDescriptionSys10(String descriptionSys10) {
    this.descriptionSys10 = descriptionSys10;
  }
  public void setCompanyCodeSys01(String companyCodeSys01) {
    this.companyCodeSys01 = companyCodeSys01;
  }
  public String getEnabled() {
    return enabled;
  }
  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }




}
