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
 * * <p>Description: Bean used to retrieve a custom function from SYS16 table.</p>
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
public class LoadCustomFunctionBean  implements LoadCustomFunction {


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




  public LoadCustomFunctionBean() {
  }



  /**
   * Business logic to execute.
   */
  public CustomFunctionVO loadCustomFunction(String functionCode,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SYS16_CUSTOM_FUNCTIONS.FUNCTION_CODE_SYS06,SYS06_FUNCTIONS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SYS16_CUSTOM_FUNCTIONS.SQL1,SYS16_CUSTOM_FUNCTIONS.SQL2,SYS16_CUSTOM_FUNCTIONS.SQL3,SYS16_CUSTOM_FUNCTIONS.SQL4,"+
          "SYS16_CUSTOM_FUNCTIONS.SQL5,SYS16_CUSTOM_FUNCTIONS.NOTE,SYS16_CUSTOM_FUNCTIONS.AUTO_LOAD_DATA,SYS16_CUSTOM_FUNCTIONS.MAIN_TABLES,"+
          "SYS06_FUNCTIONS.USE_COMPANY_CODE,SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS10_B.DESCRIPTION "+
          "from SYS16_CUSTOM_FUNCTIONS,SYS06_FUNCTIONS,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS,SYS10_TRANSLATIONS SYS10_B where "+
          "SYS06_FUNCTIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS16_CUSTOM_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS16_CUSTOM_FUNCTIONS.FUNCTION_CODE_SYS06=? and "+
          "SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06=SYS16_CUSTOM_FUNCTIONS.FUNCTION_CODE_SYS06 and "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01=SYS10_B.PROGRESSIVE and "+
          "SYS10_B.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS16","SYS16_CUSTOM_FUNCTIONS.FUNCTION_CODE_SYS06");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SYS06","SYS06_FUNCTIONS.PROGRESSIVE_SYS10");
      attribute2dbField.put("sql1SYS16","SYS16_CUSTOM_FUNCTIONS.SQL1");
      attribute2dbField.put("sql2SYS16","SYS16_CUSTOM_FUNCTIONS.SQL2");
      attribute2dbField.put("sql3SYS16","SYS16_CUSTOM_FUNCTIONS.SQL3");
      attribute2dbField.put("sql4SYS16","SYS16_CUSTOM_FUNCTIONS.SQL4");
      attribute2dbField.put("sql5SYS16","SYS16_CUSTOM_FUNCTIONS.SQL5");
      attribute2dbField.put("noteSYS16","SYS16_CUSTOM_FUNCTIONS.NOTE");
      attribute2dbField.put("autoLoadDataSYS16","SYS16_CUSTOM_FUNCTIONS.AUTO_LOAD_DATA");
      attribute2dbField.put("mainTablesSYS16","SYS16_CUSTOM_FUNCTIONS.MAIN_TABLES");
      attribute2dbField.put("useCompanyCodeSYS06","SYS06_FUNCTIONS.USE_COMPANY_CODE");
      attribute2dbField.put("levelDescriptionSYS10","SYS10_B.DESCRIPTION");
      attribute2dbField.put("progressiveHie01SYS18","SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(functionCode);
      values.add(serverLanguageId);

      // read from SYS16 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CustomFunctionVO.class,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError())
        throw new Exception(res.getErrorMessage());

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (CustomFunctionVO)((VOResponse)answer).getVo();
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching a custom function",ex);
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

