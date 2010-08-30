package org.jallinone.variants.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.items.java.ItemPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the variants matrix for a specified item code.</p>
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
public class VariantsMatrixVO extends ValueObjectImpl {

  /** pk for the item */
  private ItemPK itemPK;
  /** list of VariantsMatrixRowVO objects */
  private ArrayList rowDescriptors = new ArrayList();
  /** list of VariantsMatrixColumnVO objects */
  private ArrayList columnDescriptors = new ArrayList();
  /** number of decimals for item qty in order */
  private int decimals;
  /** list of VariantNameVO objects */
  private java.util.ArrayList managedVariants;


  public VariantsMatrixVO() { }


  public ArrayList getColumnDescriptors() {
    return columnDescriptors;
  }
  public ItemPK getItemPK() {
    return itemPK;
  }
  public ArrayList getRowDescriptors() {
    return rowDescriptors;
  }
  public void setRowDescriptors(ArrayList rowDescriptors) {
    this.rowDescriptors = rowDescriptors;
  }
  public void setItemPK(ItemPK itemPK) {
    this.itemPK = itemPK;
  }
  public void setColumnDescriptors(ArrayList columnDescriptors) {
    this.columnDescriptors = columnDescriptors;
  }
  public int getDecimals() {
    return decimals;
  }
  public void setDecimals(int decimals) {
    this.decimals = decimals;
  }
  public java.util.ArrayList getManagedVariants() {
    return managedVariants;
  }
  public void setManagedVariants(java.util.ArrayList managedVariants) {
    this.managedVariants = managedVariants;
  }


}
