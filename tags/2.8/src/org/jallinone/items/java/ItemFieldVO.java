package org.jallinone.items.java;

import java.math.BigDecimal;
import org.jallinone.system.customizations.java.BaseValueObject;
import org.openswing.swing.message.receive.java.ValueObjectImpl;

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
public class ItemFieldVO extends ValueObjectImpl {

	private String companyCodeSys01ITM32;
	private BigDecimal progressiveHie02ITM32;
	private String fieldNameITM32;
	private String descriptionSYS10;
	private BigDecimal progressiveSys10ITM32;
	private BigDecimal fieldTypeITM32;
	private BigDecimal posTM32;
	private Boolean selectedITM32;


	public ItemFieldVO() {
	}


  public String getCompanyCodeSys01ITM32() {
    return companyCodeSys01ITM32;
  }
  public String getFieldNameITM32() {
    return fieldNameITM32;
  }
  public BigDecimal getFieldTypeITM32() {
    return fieldTypeITM32;
  }
  public BigDecimal getPosTM32() {
    return posTM32;
  }
  public BigDecimal getProgressiveHie02ITM32() {
    return progressiveHie02ITM32;
  }
  public void setProgressiveHie02ITM32(BigDecimal progressiveHie02ITM32) {
    this.progressiveHie02ITM32 = progressiveHie02ITM32;
  }
  public void setPosTM32(BigDecimal posTM32) {
    this.posTM32 = posTM32;
  }
  public void setFieldTypeITM32(BigDecimal fieldTypeITM32) {
    this.fieldTypeITM32 = fieldTypeITM32;
  }
  public void setFieldNameITM32(String fieldNameITM32) {
    this.fieldNameITM32 = fieldNameITM32;
  }
  public void setCompanyCodeSys01ITM32(String companyCodeSys01ITM32) {
    this.companyCodeSys01ITM32 = companyCodeSys01ITM32;
  }
  public Boolean getSelectedITM32() {
    return selectedITM32;
  }
  public void setSelectedITM32(Boolean selectedITM32) {
    this.selectedITM32 = selectedITM32;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public BigDecimal getProgressiveSys10ITM32() {
    return progressiveSys10ITM32;
  }
  public void setProgressiveSys10ITM32(BigDecimal progressiveSys10ITM32) {
    this.progressiveSys10ITM32 = progressiveSys10ITM32;
  }


}

