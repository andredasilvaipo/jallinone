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
 * * <p>Description: Bean used to retrieve custom columns from SYS22 table.</p>
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
public class LoadCustomColumnsBean  implements LoadCustomColumns {


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




  public LoadCustomColumnsBean() {
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
  public VOListResponse loadCustomColumns(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06,SYS22_CUSTOM_COLUMNS.COLUMN_NAME,SYS22_CUSTOM_COLUMNS.COLUMN_TYPE,"+
          "SYS22_CUSTOM_COLUMNS.CONSTRAINT_VALUES,SYS22_CUSTOM_COLUMNS.COLUMN_VISIBLE,SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_TEXT,"+
          "SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_DATE,SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_NUM,SYS22_CUSTOM_COLUMNS.IS_PARAM,"+
          "SYS22_CUSTOM_COLUMNS.IS_PARAM_REQUIRED from SYS22_CUSTOM_COLUMNS where "+
          "SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS22","SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06");
      attribute2dbField.put("columnNameSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_NAME");
      attribute2dbField.put("columnTypeSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_TYPE");
      attribute2dbField.put("constraintValuesSYS22","SYS22_CUSTOM_COLUMNS.CONSTRAINT_VALUES");
      attribute2dbField.put("columnVisibleSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_VISIBLE");
      attribute2dbField.put("defaultValueTextSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_TEXT");
      attribute2dbField.put("defaultValueDateSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_DATE");
      attribute2dbField.put("defaultValueNumSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_NUM");
      attribute2dbField.put("isParamSYS22","SYS22_CUSTOM_COLUMNS.IS_PARAM");
      attribute2dbField.put("isParamRequiredSYS22","SYS22_CUSTOM_COLUMNS.IS_PARAM_REQUIRED");


      ArrayList values = new ArrayList();
      values.add(gridParams.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06));

      // read from SYS22 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CustomColumnVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (res.isError())
        throw new Exception(res.getErrorMessage());

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching custom columns list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
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

