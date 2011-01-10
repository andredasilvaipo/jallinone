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
  * <p>Description: Value object related to items import data for a specific table.</p>
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
public class ImportItemsDescriptorVO extends ImportDescriptorVO {

  public ImportItemsDescriptorVO() {
    setTableNames(new String[]{"ITM01_ITEMS"});
    setSubTypeField("ITM01_ITEMS.PROGRESSIVE_HIE02");
    setSupportsCompanyCode(true);

    String[] labels = new String[] {
        "itemCodeITM01",
        "minSellingQtyITM01",
        "minSellingUmCodeReg02ITM01",
        "vatCodeREG01",
        "grossWeightITM01",
        "grossWeightUmCodeReg02ITM01",
        "netWeightITM01",
        "netWeightUmCodeReg02ITM01",
        "widthITM01",
        "widthUmCodeReg02ITM01",
        "heightITM01",
        "heightUmCodeReg02ITM01",
        "note",
        "serial number required",
        "versionITM01",
        "revisionITM01",
        "startDateITM01",
        "barcode type",
        "barcode",
        "useVariant1ITM01",
        "useVariant2ITM01",
        "useVariant3ITM01",
        "useVariant4ITM01",
        "useVariant5ITM01"
    };
    setLabels(labels);

    String[] fields = new String[] {
        "ITM01_ITEMS.ITEM_CODE",
        "ITM01_ITEMS.MIN_SELLING_QTY",
        "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02",
        "ITM01_ITEMS.VAT_CODE_REG01",
        "ITM01_ITEMS.GROSS_WEIGHT",
        "ITM01_ITEMS.GROSS_WEIGHT_UM_CODE_REG02",
        "ITM01_ITEMS.NET_WEIGHT",
        "ITM01_ITEMS.NET_WEIGHT_UM_CODE_REG02",
        "ITM01_ITEMS.WIDTH",
        "ITM01_ITEMS.WIDTH_UM_CODE_REG02",
        "ITM01_ITEMS.HEIGHT",
        "ITM01_ITEMS.HEIGHT_UM_CODE_REG02",
        "ITM01_ITEMS.NOTE",
        "ITM01_ITEMS.SERIAL_NUMBER_REQUIRED",
        "ITM01_ITEMS.VERSION",
        "ITM01_ITEMS.REVISION",
        "ITM01_ITEMS.START_DATE",
        "ITM01_ITEMS.BARCODE_TYPE",
        "ITM01_ITEMS.BAR_CODE",
        "ITM01_ITEMS.USE_VARIANT_1",
        "ITM01_ITEMS.USE_VARIANT_2",
        "ITM01_ITEMS.USE_VARIANT_3",
        "ITM01_ITEMS.USE_VARIANT_4",
        "ITM01_ITEMS.USE_VARIANT_5"
    };
    setFields(fields);

    Class[] fieldsType = new Class[] {
        String.class,
        BigDecimal.class,
        String.class,
        String.class,
        BigDecimal.class,
        String.class,
        BigDecimal.class,
        String.class,
        BigDecimal.class,
        String.class,
        BigDecimal.class,
        String.class,
        String.class,
        String.class,
        BigDecimal.class,
        BigDecimal.class,
        java.sql.Date.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class
    };
    setFieldsType(fieldsType);

    LinkedHashSet pkFields = new LinkedHashSet();
    pkFields.add("ITM01_ITEMS.ITEM_CODE");
    setPkFields(pkFields);

    HashSet requiredFields = new HashSet();
    requiredFields.add("ITM01_ITEMS.ITEM_CODE");
    requiredFields.add("ITM01_ITEMS.MIN_SELLING_QTY");
    requiredFields.add("ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
    requiredFields.add("ITM01_ITEMS.VAT_CODE_REG01");
    requiredFields.add("ITM01_ITEMS.USE_VARIANT_1");
    requiredFields.add("ITM01_ITEMS.USE_VARIANT_2");
    requiredFields.add("ITM01_ITEMS.USE_VARIANT_3");
    requiredFields.add("ITM01_ITEMS.USE_VARIANT_4");
    requiredFields.add("ITM01_ITEMS.USE_VARIANT_5");
    requiredFields.add("ITM01_ITEMS.BARCODE_TYPE");
    requiredFields.add("ITM01_ITEMS.PROGRESSIVE_SYS10");
    requiredFields.add("ITM01_ITEMS.PROGRESSIVE_HIE01");
    requiredFields.add("ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
    setRequiredFields(requiredFields);

    LinkedHashMap progressiveSys10Fields = new LinkedHashMap();
    progressiveSys10Fields.put("ITM01_ITEMS.PROGRESSIVE_SYS10","description");
    progressiveSys10Fields.put("ITM01_ITEMS.ADD_PROGRESSIVE_SYS10","addDescr");
    setProgressiveSys10Fields(progressiveSys10Fields);

    LinkedHashMap defaultFields = new LinkedHashMap();
    defaultFields.put("ITM01_ITEMS.ENABLED","Y");
    setDefaultFields(defaultFields);

    setHierarchyField("ITM01_ITEMS.PROGRESSIVE_HIE01");


  }
}
