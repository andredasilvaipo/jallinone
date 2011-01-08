package org.jallinone.variants.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.HashMap;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store all descriptions for all variants, related to a specific company code.</p>
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
 * Software Foundation, Inc., 775 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class VariantDescriptionsVO extends ValueObjectImpl {

  /** collection of pairs <VARIANT_TYPE||'_'||VARIANT_CODE,variant description> */
  HashMap variant1Descriptions = new HashMap();

  /** collection of pairs <VARIANT_TYPE||'_'||VARIANT_CODE,variant description> */
  HashMap variant2Descriptions = new HashMap();

  /** collection of pairs <VARIANT_TYPE||'_'||VARIANT_CODE,variant description> */
  HashMap variant3Descriptions = new HashMap();

  /** collection of pairs <VARIANT_TYPE||'_'||VARIANT_CODE,variant description> */
  HashMap variant4Descriptions = new HashMap();

  /** collection of pairs <VARIANT_TYPE||'_'||VARIANT_CODE,variant description> */
  HashMap variant5Descriptions = new HashMap();


  public VariantDescriptionsVO() {
  }


  public HashMap getVariant1Descriptions() {
    return variant1Descriptions;
  }
  public HashMap getVariant2Descriptions() {
    return variant2Descriptions;
  }
  public HashMap getVariant3Descriptions() {
    return variant3Descriptions;
  }
  public HashMap getVariant4Descriptions() {
    return variant4Descriptions;
  }
  public HashMap getVariant5Descriptions() {
    return variant5Descriptions;
  }
  public void setVariant5Descriptions(HashMap variant5Descriptions) {
    this.variant5Descriptions = variant5Descriptions;
  }
  public void setVariant4Descriptions(HashMap variant4Descriptions) {
    this.variant4Descriptions = variant4Descriptions;
  }
  public void setVariant3Descriptions(HashMap variant3Descriptions) {
    this.variant3Descriptions = variant3Descriptions;
  }
  public void setVariant2Descriptions(HashMap variant2Descriptions) {
    this.variant2Descriptions = variant2Descriptions;
  }
  public void setVariant1Descriptions(HashMap variant1Descriptions) {
    this.variant1Descriptions = variant1Descriptions;
  }




}
