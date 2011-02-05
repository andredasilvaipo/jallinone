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
  * <p>Description: Value object related to purchase prices import data for a specific supplier and pricelist.</p>
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
public class ImportSupplierPricesDescriptorVO extends ImportDescriptorVO {

  public ImportSupplierPricesDescriptorVO() {
    setTableNames(new String[]{"PUR04_SUPPLIER_PRICES"});
    setSubTypeField("PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03");
    setSubTypeField2("PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04");
    setSupportsCompanyCode(true);

    String[] labels = new String[] {
        "itemCodeITM01",
        "valuePUR04",
        "startDatePUR04",
        "endDatePUR04"
    };
    setLabels(labels);

    String[] fields = new String[] {
        "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01",
        "PUR04_SUPPLIER_PRICES.VALUE",
        "PUR04_SUPPLIER_PRICES.START_DATE",
        "PUR04_SUPPLIER_PRICES.END_DATE"
    };
    setFields(fields);

    Class[] fieldsType = new Class[] {
        String.class,
        BigDecimal.class,
        java.sql.Date.class,
        java.sql.Date.class
    };
    setFieldsType(fieldsType);

    LinkedHashSet pkFields = new LinkedHashSet();
    pkFields.add("PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01");
    setPkFields(pkFields);

    HashSet requiredFields = new HashSet();
    requiredFields.add("PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01");
    requiredFields.add("PUR04_SUPPLIER_PRICES.VALUE");
    requiredFields.add("PUR04_SUPPLIER_PRICES.START_DATE");
    requiredFields.add("PUR04_SUPPLIER_PRICES.END_DATE");
    setRequiredFields(requiredFields);


  }
}
