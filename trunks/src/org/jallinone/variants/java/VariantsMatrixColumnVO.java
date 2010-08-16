package org.jallinone.variants.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the column descriptor of the variants matrix for a specified item code.</p>
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
public class VariantsMatrixColumnVO extends ValueObjectImpl {

  private String variantTypeITM07;
  private String variantTypeITM08;
  private String variantTypeITM09;
  private String variantTypeITM10;

  private String variantCodeITM12;
  private String variantCodeITM13;
  private String variantCodeITM14;
  private String variantCodeITM15;

  private String columnDescription;


  public VariantsMatrixColumnVO() {}


  public String getColumnDescription() {
    return columnDescription;
  }
  public String getVariantCodeITM12() {
    return variantCodeITM12;
  }
  public String getVariantCodeITM13() {
    return variantCodeITM13;
  }
  public String getVariantCodeITM14() {
    return variantCodeITM14;
  }
  public String getVariantCodeITM15() {
    return variantCodeITM15;
  }
  public String getVariantTypeITM07() {
    return variantTypeITM07;
  }
  public String getVariantTypeITM08() {
    return variantTypeITM08;
  }
  public String getVariantTypeITM09() {
    return variantTypeITM09;
  }
  public String getVariantTypeITM10() {
    return variantTypeITM10;
  }
  public void setVariantTypeITM10(String variantTypeITM10) {
    this.variantTypeITM10 = variantTypeITM10;
  }
  public void setVariantTypeITM09(String variantTypeITM09) {
    this.variantTypeITM09 = variantTypeITM09;
  }
  public void setVariantTypeITM08(String variantTypeITM08) {
    this.variantTypeITM08 = variantTypeITM08;
  }
  public void setVariantTypeITM07(String variantTypeITM07) {
    this.variantTypeITM07 = variantTypeITM07;
  }
  public void setVariantCodeITM15(String variantCodeITM15) {
    this.variantCodeITM15 = variantCodeITM15;
  }
  public void setVariantCodeITM14(String variantCodeITM14) {
    this.variantCodeITM14 = variantCodeITM14;
  }
  public void setVariantCodeITM13(String variantCodeITM13) {
    this.variantCodeITM13 = variantCodeITM13;
  }
  public void setVariantCodeITM12(String variantCodeITM12) {
    this.variantCodeITM12 = variantCodeITM12;
  }
  public void setColumnDescription(String columnDescription) {
    this.columnDescription = columnDescription;
  }

}
