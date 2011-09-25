package org.jallinone.registers.measure.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store measure info.</p>
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
public class MeasureVO extends BaseValueObject {

  private String umCodeREG02;
  private String enabledREG02;
  private java.math.BigDecimal decimalsREG02;


  public MeasureVO() {
  }


  public String getUmCodeREG02() {
    return umCodeREG02;
  }
  public void setUmCodeREG02(String umCodeREG02) {
    this.umCodeREG02 = umCodeREG02;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public String getEnabledREG02() {
    return enabledREG02;
  }
  public void setEnabledREG02(String enabledREG02) {
    this.enabledREG02 = enabledREG02;
  }

}