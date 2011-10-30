package org.jallinone.items.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store brands.</p>
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
public class BrandVO extends BaseValueObject {



  private String companyCodeSys01ITM31;
	private String brandCodeITM31;
  private String descriptionITM31;
	private String enabledITM31;


  public BrandVO() {
  }
  public String getBrandCodeITM31() {
    return brandCodeITM31;
  }
  public String getCompanyCodeSys01ITM31() {
    return companyCodeSys01ITM31;
  }
  public String getDescriptionITM31() {
    return descriptionITM31;
  }
  public String getEnabledITM31() {
    return enabledITM31;
  }
  public void setEnabledITM31(String enabledITM31) {
    this.enabledITM31 = enabledITM31;
  }
  public void setBrandCodeITM31(String brandCodeITM31) {
    this.brandCodeITM31 = brandCodeITM31;
  }
  public void setDescriptionITM31(String descriptionITM31) {
    this.descriptionITM31 = descriptionITM31;
  }
  public void setCompanyCodeSys01ITM31(String companyCodeSys01ITM31) {
    this.companyCodeSys01ITM31 = companyCodeSys01ITM31;
  }


}
