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
  * <p>Description: Value object related to customers import data for a specific table.</p>
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
public class ImportCustomersDescriptorVO extends ImportDescriptorVO {

  public ImportCustomersDescriptorVO() {
    setTableNames(new String[]{"REG04_SUBJECTS","SAL07_CUSTOMERS"});
    setSupportsCompanyCode(true);
    setProgressiveFieldName("REG04_SUBJECTS.PROGRESSIVE");

    HashMap fieldsToCopy = new HashMap();
    fieldsToCopy.put("REG04_SUBJECTS.PROGRESSIVE","SAL07_CUSTOMERS.PROGRESSIVE_REG04");
    setFieldsToCopy(fieldsToCopy);

    String[] labels = new String[] {
        "subjectTypeREG04",
        "name_1REG04",
        "name_2REG04",
        "address",
        "city",
        "zip",
        "province",
        "country",
        "sex",
        "taxCode",
        "phone",
        "fax",
        "mobile",
        "email",
        "webSite",
        "lawfulSite",
        "birthday",
        "birthplace",
        "maritalStatus",
        "nationality",
        "note",
        "customerCodeSAL07",
        "paymentCodeREG10",
        "pricelistCodeSAL01",
        "bankCodeREG12",
        "vatCodeREG01",
        "trustAmountSAL07",
        "credits account",
        "selling items account",
        "selling activities account",
        "selling charges account"
    };
    setLabels(labels);

    String[] fields = new String[] {
        "REG04_SUBJECTS.SUBJECT_TYPE",
        "REG04_SUBJECTS.NAME_1",
        "REG04_SUBJECTS.NAME_2",
        "REG04_SUBJECTS.ADDRESS",
        "REG04_SUBJECTS.CITY",
        "REG04_SUBJECTS.ZIP",
        "REG04_SUBJECTS.PROVINCE",
        "REG04_SUBJECTS.COUNTRY",
        "REG04_SUBJECTS.SEX",
        "REG04_SUBJECTS.TAX_CODE",
        "REG04_SUBJECTS.PHONE_NUMBER",
        "REG04_SUBJECTS.FAX_NUMBER",
        "REG04_SUBJECTS.MOBILE_NUMBER",
        "REG04_SUBJECTS.EMAIL_ADDRESS",
        "REG04_SUBJECTS.WEB_SITE",
        "REG04_SUBJECTS.LAWFUL_SITE",
        "REG04_SUBJECTS.BIRTHDAY",
        "REG04_SUBJECTS.BIRTHDAY_PLACE",
        "REG04_SUBJECTS.MARITAL_STATUS",
        "REG04_SUBJECTS.NATIONALITY",
        "REG04_SUBJECTS.NOTE",
        "SAL07_CUSTOMERS.CUSTOMER_CODE",
        "SAL07_CUSTOMERS.PAYMENT_CODE_REG10",
        "SAL07_CUSTOMERS.PRICELIST_CODE_SAL01",
        "SAL07_CUSTOMERS.BANK_CODE_REG12",
        "SAL07_CUSTOMERS.VAT_CODE_REG01",
        "SAL07_CUSTOMERS.TRUST_AMOUNT",
        "SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02",
        "SAL07_CUSTOMERS.ITEMS_ACCOUNT_CODE_ACC02",
        "SAL07_CUSTOMERS.ACTIVITIES_ACCOUNT_CODE_ACC02",
        "SAL07_CUSTOMERS.CHARGES_ACCOUNT_CODE_ACC02"
    };
    setFields(fields);

    Class[] fieldsType = new Class[] {
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        java.sql.Date.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        String.class,
        BigDecimal.class,
        String.class,
        String.class,
        String.class,
        String.class
    };
    setFieldsType(fieldsType);

    LinkedHashSet pkFields = new LinkedHashSet();
    pkFields.add("REG04_SUBJECTS.NAME_1");
    pkFields.add("REG04_SUBJECTS.NAME_2");
    pkFields.add("SAL07_CUSTOMERS.CUSTOMER_CODE");
    setPkFields(pkFields);

    HashSet requiredFields = new HashSet();
    requiredFields.add("REG04_SUBJECTS.SUBJECT_TYPE");
    requiredFields.add("REG04_SUBJECTS.NAME_1");
    requiredFields.add("REG04_SUBJECTS.NAME_2");
    requiredFields.add("SAL07_CUSTOMERS.CUSTOMER_CODE");
    requiredFields.add("SAL07_CUSTOMERS.PAYMENT_CODE_REG10");
    requiredFields.add("SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02");
    requiredFields.add("SAL07_CUSTOMERS.ITEMS_ACCOUNT_CODE_ACC02");
    requiredFields.add("SAL07_CUSTOMERS.ACTIVITIES_ACCOUNT_CODE_ACC02");
    requiredFields.add("SAL07_CUSTOMERS.CHARGES_ACCOUNT_CODE_ACC02");
    setRequiredFields(requiredFields);

    LinkedHashMap defaultFields = new LinkedHashMap();
    defaultFields.put("REG04_SUBJECTS.ENABLED","Y");
    defaultFields.put("SAL07_CUSTOMERS.ENABLED","Y");
    setDefaultFields(defaultFields);


  }
}
