package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update existing custom columns in SYS22 table.</p>
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
public class UpdateCustomColumnsBean  implements UpdateCustomColumns {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }




  public UpdateCustomColumnsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public CustomColumnVO getCustomColumn() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse updateCustomColumns(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CustomColumnVO oldVO = null;
      CustomColumnVO newVO = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS22","FUNCTION_CODE_SYS06");
      attribute2dbField.put("columnNameSYS22","COLUMN_NAME");
      attribute2dbField.put("columnTypeSYS22","COLUMN_TYPE");
      attribute2dbField.put("constraintValuesSYS22","CONSTRAINT_VALUES");
      attribute2dbField.put("columnVisibleSYS22","COLUMN_VISIBLE");
      attribute2dbField.put("defaultValueTextSYS22","DEFAULT_VALUE_TEXT");
      attribute2dbField.put("defaultValueDateSYS22","DEFAULT_VALUE_DATE");
      attribute2dbField.put("defaultValueNumSYS22","DEFAULT_VALUE_NUM");
      attribute2dbField.put("isParamSYS22","IS_PARAM");
      attribute2dbField.put("isParamRequiredSYS22","IS_PARAM_REQUIRED");

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("functionCodeSys06SYS22");
      pkAttrs.add("columnNameSYS22");

      // update SYS22 table...
      Response res = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CustomColumnVO)oldVOs.get(i);
        newVO = (CustomColumnVO)newVOs.get(i);
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SYS22_CUSTOM_COLUMNS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing custom columns",ex);
    	throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



}

