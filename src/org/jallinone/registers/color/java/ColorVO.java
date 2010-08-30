package org.jallinone.registers.color.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store color info.</p>
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
public class ColorVO extends BaseValueObject {
  private String colorCodeREG13;
  private java.math.BigDecimal progressiveSys10REG13;
  private String enabledREG13;
  private String descriptionSYS10;


  public ColorVO() {
  }


  public String getColorCodeREG13() {
    return colorCodeREG13;
  }
  public void setColorCodeREG13(String colorCodeREG13) {
    this.colorCodeREG13 = colorCodeREG13;
  }
  public java.math.BigDecimal getProgressiveSys10REG13() {
    return progressiveSys10REG13;
  }
  public void setProgressiveSys10REG13(java.math.BigDecimal progressiveSys10REG13) {
    this.progressiveSys10REG13 = progressiveSys10REG13;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledREG13() {
    return enabledREG13;
  }
  public void setEnabledREG13(String enabledREG13) {
    this.enabledREG13 = enabledREG13;
  }

}