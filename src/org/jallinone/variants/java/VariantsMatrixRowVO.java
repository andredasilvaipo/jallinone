package org.jallinone.variants.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a row description of the variants matrix for a specified item code.</p>
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
public class VariantsMatrixRowVO extends ValueObjectImpl {

  private String variantTypeITM06;
  private String variantCodeITM11;
  private String rowDescription;


  public VariantsMatrixRowVO() {}


  public String getVariantCodeITM11() {
    return variantCodeITM11;
  }
  public String getVariantTypeITM06() {
    return variantTypeITM06;
  }
  public void setVariantTypeITM06(String variantTypeITM06) {
    this.variantTypeITM06 = variantTypeITM06;
  }
  public void setVariantCodeITM11(String variantCodeITM11) {
    this.variantCodeITM11 = variantCodeITM11;
  }
  public void setRowDescription(String rowDescription) {
    this.rowDescription = rowDescription;
  }
  public String getRowDescription() {
    return rowDescription;
  }

}
