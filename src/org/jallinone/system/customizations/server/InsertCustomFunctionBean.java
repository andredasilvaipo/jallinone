package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.server.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert a new custom function in SYS16 table.</p>
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
public class InsertCustomFunctionBean  implements InsertCustomFunction {


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


  public InsertCustomFunctionBean() {
  }


  /**
   * Business logic to execute.
   */
  public VOResponse insertCustomFunction(CustomFunctionVO vo,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // fetch query columns...
      TableVO tableVO = new TableVO(vo.getSql(),new ArrayList(),true);
      Response res = GetQueryInfoUtil.getQueryInfo(
        tableVO,
        serverLanguageId,
        username
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      tableVO = (TableVO)((VOResponse)res).getVo();
      vo.setSql(tableVO.getSql());

      // insert new record in SYS10...
      vo.setProgressiveSys10SYS06( TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn) );

      // insert new record in SYS06...
      pstmt = conn.prepareStatement(
        "insert into SYS06_FUNCTIONS(FUNCTION_CODE,PROGRESSIVE_SYS10,IMAGE_NAME,METHOD_NAME,USE_COMPANY_CODE,CAN_DEL) values(?,?,?,?,?,?)"
      );
      pstmt.setString(1,vo.getFunctionCodeSys06SYS16());
      pstmt.setBigDecimal(2,vo.getProgressiveSys10SYS06());
      pstmt.setString(3,"page.gif");
      pstmt.setString(4,"executeCustomFunction");
      pstmt.setString(5,vo.isUseCompanyCodeSYS06()?"Y":"N");
      pstmt.setString(6,"Y");
      pstmt.execute();

      // insert new record in SYS18...
      pstmt = conn.prepareStatement(
        "insert into SYS18_FUNCTION_LINKS(FUNCTION_CODE_SYS06,PROGRESSIVE_HIE01,POS_ORDER) values(?,?,?)"
      );
      pstmt.setString(1,vo.getFunctionCodeSys06SYS16());
      pstmt.setBigDecimal(2,vo.getProgressiveHie01SYS18());
      pstmt.setInt(3,1);
      pstmt.execute();

      // insert new record in SYS07...
      pstmt = conn.prepareStatement(
        "insert into SYS07_ROLE_FUNCTIONS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) values(?,?,?,?,?)"
      );
      pstmt.setInt(1,2); // administrator role...
      pstmt.setString(2,vo.getFunctionCodeSys06SYS16());
      pstmt.setString(3,"Y");
      pstmt.setString(4,"Y");
      pstmt.setString(5,"Y");
      pstmt.execute();

      if (vo.isUseCompanyCodeSYS06()) {
        // insert records into SYS02...
        pstmt = conn.prepareStatement(
          "insert into SYS02_COMPANIES_ACCESS(COMPANY_CODE_SYS01,PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) "+
          "select COMPANY_CODE,2,?,'Y','Y',Y' from SYS01_COMPANIES where ENABLED='Y'"
        );
        pstmt.setString(1,vo.getFunctionCodeSys06SYS16());
        pstmt.execute();
      }

      // insert new record in SYS16...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS16","FUNCTION_CODE_SYS06");
      attribute2dbField.put("sql1SYS16","SQL1");
      attribute2dbField.put("sql2SYS16","SQL2");
      attribute2dbField.put("sql3SYS16","SQL3");
      attribute2dbField.put("sql4SYS16","SQL4");
      attribute2dbField.put("sql5SYS16","SQL5");
      attribute2dbField.put("noteSYS16","NOTE");
      attribute2dbField.put("autoLoadDataSYS16","AUTO_LOAD_DATA");
      attribute2dbField.put("mainTablesSYS16","MAIN_TABLES");
      res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SYS16_CUSTOM_FUNCTIONS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // insert records in SYS22...
      attribute2dbField = new HashMap();
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

      ColumnVO colVO = null;
      CustomColumnVO columnVO = new CustomColumnVO();
      for(int i=0;i<tableVO.getColumns().size();i++) {
        colVO = (ColumnVO)tableVO.getColumns().get(i);
        columnVO.setColumnNameSYS22(colVO.getColumnName());
        if (colVO.getColumnSqlType()==Types.DATE ||
            colVO.getColumnSqlType()==Types.TIMESTAMP ||
            colVO.getColumnSqlType()==Types.TIME)
          columnVO.setColumnTypeSYS22(ApplicationConsts.TYPE_DATE);
        else if (colVO.getColumnSqlType()==Types.BIGINT ||
                 colVO.getColumnSqlType()==Types.INTEGER ||
                 colVO.getColumnSqlType()==Types.NUMERIC ||
                 colVO.getColumnSqlType()==Types.SMALLINT ||
                 colVO.getColumnSqlType()==Types.DECIMAL ||
                 colVO.getColumnSqlType()==Types.DOUBLE ||
                 colVO.getColumnSqlType()==Types.FLOAT ||
                 colVO.getColumnSqlType()==Types.REAL)
          columnVO.setColumnTypeSYS22(ApplicationConsts.TYPE_NUM);
        else
          columnVO.setColumnTypeSYS22(ApplicationConsts.TYPE_TEXT);
        columnVO.setColumnVisibleSYS22(true);
        columnVO.setFunctionCodeSys06SYS22(vo.getFunctionCodeSys06SYS16());
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            columnVO,
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


      // check if there exists "?" parameters in where clause...
      String sql = tableVO.getSql().replace('\n',' ').replace('\t',' ');
      String sql2 = sql.toLowerCase();
      if (sql2.indexOf(" where ")!=-1 &&
          sql2.indexOf("?",sql2.indexOf(" where "))!=-1) {
        // for each "?" retrieve its column name...
        int i = sql.indexOf("?");
        int j = i;
        int k = -1;
        while(j>0) {
          // find out operator..
          if (sql2.substring(j).startsWith("<") ||
              sql2.substring(j).startsWith(">") ||
              sql2.substring(j).startsWith(" in ") ||
              sql2.substring(j).startsWith(" in(") ||
              sql2.substring(j).startsWith("=") ||
              sql2.substring(j).startsWith(" like "))
            k = j;
          if (k==-1) {
            j--;
            continue;
          }

          // find out column name...
          if (sql2.substring(j).startsWith(" and ") ||
              sql2.substring(j).startsWith(" or ") ||
              sql2.substring(j).startsWith("(") ||
              sql2.substring(j).startsWith(" where ")) {
            // column name found...
            if (sql2.substring(j).startsWith(" and "))
              columnVO.setColumnNameSYS22(sql.substring(j+5,k));
            else if (sql2.substring(j).startsWith(" or "))
              columnVO.setColumnNameSYS22(sql.substring(j+4,k));
            else if (sql2.substring(j).startsWith("("))
              columnVO.setColumnNameSYS22(sql.substring(j+1,k));
            else if (sql2.substring(j).startsWith(" where "))
              columnVO.setColumnNameSYS22(sql.substring(j+7,k));

            // trim column name + use alias only if defined...
            columnVO.setColumnNameSYS22( columnVO.getColumnNameSYS22().trim() );
            if (columnVO.getColumnNameSYS22().indexOf(" ")!=-1)
              columnVO.setColumnNameSYS22( columnVO.getColumnNameSYS22().substring(columnVO.getColumnNameSYS22().indexOf(" ")+1) );

            columnVO.setColumnTypeSYS22(ApplicationConsts.TYPE_WHERE);

            columnVO.setColumnVisibleSYS22(false);
            columnVO.setIsParamSYS22(true);
            columnVO.setIsParamRequiredSYS22(true);
            columnVO.setFunctionCodeSys06SYS22(vo.getFunctionCodeSys06SYS16());
            res = QueryUtil.insertTable(
                conn,
                new UserSessionParameters(username),
                columnVO,
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


            // reset indexes and search again...
            i = sql.indexOf("?",i+1);
            j = i;
            k = -1;
          }
          else
            j--;
        }


      }

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new custom function", ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
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


