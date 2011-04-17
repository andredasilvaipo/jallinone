package org.jallinone.system.importdata.java;

import java.util.*;

import org.openswing.swing.message.receive.java.*;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object related to import data for a specific table.</p>
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
public class ImportDescriptorVO extends ValueObjectImpl {

  private String[] labels;

  private String[] fields;

  private Class[] fieldsType;

  private LinkedHashSet pkFields = new LinkedHashSet();

  private HashSet requiredFields = new HashSet();

  /** e.g. an item type */
  private String subTypeField;

  /** e.g. a pricelist code */
  private String subTypeField2;

  private boolean supportsCompanyCode;

  private LinkedHashMap progressiveSys10Fields = new LinkedHashMap();

  private String hierarchyField;

  /** collection of pairs <field,default value for field> */
  private LinkedHashMap defaultFields = new LinkedHashMap();

  /** table names where inserting/updating data, reported in the order to use when inserting data */
  private String[] tableNames;

  /** field name related to a progressive that must be calculated at run time, in insert ops */
  private String progressiveFieldName;

  /** collection of couples <orginal field name,target field name> */
  private HashMap fieldsToCopy = new HashMap();


  public ImportDescriptorVO() {}


  public String[] getFields() {
    return fields;
  }
  public LinkedHashSet getPkFields() {
    return pkFields;
  }
  public HashSet getRequiredFields() {
    return requiredFields;
  }
  public void setRequiredFields(HashSet requiredFields) {
    this.requiredFields = requiredFields;
  }
  public void setPkFields(LinkedHashSet pkFields) {
    this.pkFields = pkFields;
  }
  public void setFields(String[] fields) {
    this.fields = fields;
  }
  public void setSupportsCompanyCode(boolean supportsCompanyCode) {
    this.supportsCompanyCode = supportsCompanyCode;
  }
  public boolean isSupportsCompanyCode() {
    return supportsCompanyCode;
  }
  public Class[] getFieldsType() {
    return fieldsType;
  }
  public void setFieldsType(Class[] fieldsType) {
    this.fieldsType = fieldsType;
  }
  public String[] getLabels() {
    return labels;
  }
  public void setLabels(String[] labels) {
    this.labels = labels;
  }
  public void setProgressiveSys10Fields(LinkedHashMap progressiveSys10Fields) {
    this.progressiveSys10Fields = progressiveSys10Fields;
  }
  public LinkedHashMap getProgressiveSys10Fields() {
    return progressiveSys10Fields;
  }
  public LinkedHashMap getDefaultFields() {
    return defaultFields;
  }
  public void setDefaultFields(LinkedHashMap defaultFields) {
    this.defaultFields = defaultFields;
  }
  public String getSubTypeField() {
    return subTypeField;
  }
  public void setSubTypeField(String subTypeField) {
    this.subTypeField = subTypeField;
  }

  public void setHierarchyField(String hierarchyField) {
    this.hierarchyField = hierarchyField;
  }
  public String getHierarchyField() {
    return hierarchyField;
  }
  public String[] getTableNames() {
    return tableNames;
  }
  public void setTableNames(String[] tableNames) {
    this.tableNames = tableNames;
  }
  public String getProgressiveFieldName() {
    return progressiveFieldName;
  }
  public void setProgressiveFieldName(String progressiveFieldName) {
    this.progressiveFieldName = progressiveFieldName;
  }
  public HashMap getFieldsToCopy() {
    return fieldsToCopy;
  }
  public void setFieldsToCopy(HashMap fieldsToCopy) {
    this.fieldsToCopy = fieldsToCopy;
  }
  public String getSubTypeField2() {
    return subTypeField2;
  }
  public void setSubTypeField2(String subTypeField2) {
    this.subTypeField2 = subTypeField2;
  }



}
