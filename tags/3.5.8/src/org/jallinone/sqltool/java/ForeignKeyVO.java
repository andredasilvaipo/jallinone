package org.jallinone.sqltool.java;

import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define a foreign key.</p>
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
public class ForeignKeyVO extends ValueObjectImpl {

  /** primary key table name */
  private String pkTableName = null;

  /** foreign key table name */
  private String fkTableName = null;

  /** primary key field names */
  private ArrayList pkFieldNames = new ArrayList();

  /** foreign key field names */
  private ArrayList fkFieldNames = new ArrayList();


  public ForeignKeyVO() {}
  
  
  public ForeignKeyVO(String fkTableName,String pkTableName) {
    this.fkTableName = fkTableName;
    this.pkTableName = pkTableName;
  }


  public void addFieldName(String fkColumnName,String pkColumnName) {
    fkFieldNames.add(fkColumnName);
    pkFieldNames.add(pkColumnName);
  }

  public ArrayList getFkFieldNames() {
    return fkFieldNames;
  }
  public String getFkTableName() {
    return fkTableName;
  }
  public ArrayList getPkFieldNames() {
    return pkFieldNames;
  }
  public String getPkTableName() {
    return pkTableName;
  }


  public void setPkTableName(String pkTableName) {
	  this.pkTableName = pkTableName;
  }


  public void setFkTableName(String fkTableName) {
	  this.fkTableName = fkTableName;
  }


  public void setPkFieldNames(ArrayList pkFieldNames) {
	  this.pkFieldNames = pkFieldNames;
  }


  public void setFkFieldNames(ArrayList fkFieldNames) {
	  this.fkFieldNames = fkFieldNames;
  }


}
