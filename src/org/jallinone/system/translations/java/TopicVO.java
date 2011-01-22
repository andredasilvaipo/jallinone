package org.jallinone.system.translations.java;

import java.io.Serializable;

import org.jallinone.system.languages.java.LanguageVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Descriptor used for translations management.</p>
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
public class TopicVO implements Serializable {

  private String tableName;
  private String columnName;
  private boolean useCompanyCode;
  private boolean useEnabled;
  private LanguageVO[] langs = new LanguageVO[0];

  
  public TopicVO() {}
  

  public TopicVO(LanguageVO[] langs,String tableName,String columnName,boolean useCompanyCode,boolean useEnabled) {
    this.langs = langs;
    this.tableName = tableName;
    this.columnName = columnName;
    this.useCompanyCode = useCompanyCode;
    this.useEnabled = useEnabled;
  }
  public String getColumnName() {
    return columnName;
  }
  public String getTableName() {
    return tableName;
  }
  public boolean isUseCompanyCode() {
    return useCompanyCode;
  }
  public boolean isUseEnabled() {
	  return useEnabled;
  }




  public void setTableName(String tableName) {
	  this.tableName = tableName;
  }


  public void setColumnName(String columnName) {
	  this.columnName = columnName;
  }


  public void setUseCompanyCode(boolean useCompanyCode) {
	  this.useCompanyCode = useCompanyCode;
  }


  public void setUseEnabled(boolean useEnabled) {
	  this.useEnabled = useEnabled;
  }


  public void setLangs(LanguageVO[] langs) {
	  this.langs = langs;
  }


  public boolean uquals(Object obj) {
	  if (obj==null || !(obj instanceof TopicVO))
		  return false;

	  TopicVO o2 = (TopicVO)obj;
	  return
	  tableName.equals(o2.tableName) &&
	  columnName.equals(o2.columnName) &&
	  useCompanyCode==o2.useCompanyCode &&
	  useEnabled==o2.useEnabled;
  }
  
  
  public LanguageVO[] getLangs() {
	  return langs;
  }


}
