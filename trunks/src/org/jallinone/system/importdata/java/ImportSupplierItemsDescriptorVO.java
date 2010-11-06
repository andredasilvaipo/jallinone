package org.jallinone.system.importdata.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import org.jallinone.system.languages.java.LanguageVO;
import java.math.BigDecimal;
import java.util.*;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object related to purchase items import data for a specific supplier.</p>
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
public class ImportSupplierItemsDescriptorVO extends ImportDescriptorVO {

  public ImportSupplierItemsDescriptorVO() {
    setTableNames(new String[]{"PUR02_SUPPLIER_ITEMS"});
    setSubTypeField("PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04");
    setSubTypeField2("PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02");
    setSupportsCompanyCode(true);

    String[] labels = new String[] {
        "itemCodeITM01",
        "supplierItemCodePUR02",
        "umCodeREG02",
        "minPurchaseQtyPUR02",
        "multipleQtyPUR02"
    };
    setLabels(labels);

    String[] fields = new String[] {
        "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01",
        "PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE",
        "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02",
        "PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY",
        "PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY"
    };
    setFields(fields);

    Class[] fieldsType = new Class[] {
        String.class,
        String.class,
        String.class,
        BigDecimal.class,
        BigDecimal.class
    };
    setFieldsType(fieldsType);

    LinkedHashSet pkFields = new LinkedHashSet();
    pkFields.add("PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01");
    setPkFields(pkFields);

    HashSet requiredFields = new HashSet();
    requiredFields.add("PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01");
    requiredFields.add("PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE");
    requiredFields.add("PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
    requiredFields.add("PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY");
    requiredFields.add("PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY");
    requiredFields.add("PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01");
    setRequiredFields(requiredFields);

    LinkedHashMap defaultFields = new LinkedHashMap();
    defaultFields.put("PUR02_SUPPLIER_ITEMS.ENABLED","Y");
    setDefaultFields(defaultFields);

    setHierarchyField("PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01");


  }
}
