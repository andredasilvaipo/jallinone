package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.customizations.java.CustomFunctionVO;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.sqltool.java.TableVO;
import org.jallinone.sqltool.server.GetQueryInfoAction;
import org.jallinone.sqltool.java.ColumnVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.customizations.java.CustomColumnVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing custom function in SYS16 table.</p>
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
public class UpdateCustomFunctionAction implements Action {

  private GetQueryInfoAction query = new GetQueryInfoAction();


  public UpdateCustomFunctionAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCustomFunction";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);


      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));

      CustomFunctionVO oldVO = (CustomFunctionVO)((ValueObject[])inputPar)[0];
      CustomFunctionVO newVO = (CustomFunctionVO)((ValueObject[])inputPar)[1];

      // fetch query columns...
      TableVO tableVO = new TableVO(newVO.getSql(),new ArrayList(),true);
      Response res = query.executeCommand(
        tableVO,
        userSessionPars,
        request,
        response,
        userSession,
        context
      );

      if (res.isError()) {
        return res;
      }
      tableVO = (TableVO)((VOResponse)res).getVo();
      newVO.setSql(tableVO.getSql());

      if (!newVO.getSql().equals(oldVO.getSql())) {
        // delete all rows from SYS22...
        pstmt = conn.prepareStatement(
          "delete from SYS22_CUSTOM_COLUMNS where FUNCTION_CODE_SYS06=?"
        );
        pstmt.setString(1,newVO.getFunctionCodeSys06SYS16());
        pstmt.execute();

        // update description in SYS10...
        TranslationUtils.updateTranslation(
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveSys10SYS06(),
          serverLanguageId,
          conn
        );
      }

      // delete previous record in SYS18...
      pstmt = conn.prepareStatement(
        "delete from SYS18_FUNCTION_LINKS where FUNCTION_CODE_SYS06=? and PROGRESSIVE_HIE01=? "
      );
      pstmt.setString(1,oldVO.getFunctionCodeSys06SYS16());
      pstmt.setBigDecimal(2,oldVO.getProgressiveHie01SYS18());
      pstmt.execute();

      // insert new record in SYS18...
      pstmt = conn.prepareStatement(
        "insert into SYS18_FUNCTION_LINKS(FUNCTION_CODE_SYS06,PROGRESSIVE_HIE01,POS_ORDER) values(?,?,?)"
      );
      pstmt.setString(1,newVO.getFunctionCodeSys06SYS16());
      pstmt.setBigDecimal(2,newVO.getProgressiveHie01SYS18());
      pstmt.setInt(3,1);
      pstmt.execute();

      // update record in SYS16...
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

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("functionCodeSys06SYS16");

      res = QueryUtil.updateTable(
          conn,
          userSessionPars,
          pkAttrs,
          oldVO,
          newVO,
          "SYS16_CUSTOM_FUNCTIONS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }


      if (!newVO.getSql().equals(oldVO.getSql())) {
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
          columnVO.setFunctionCodeSys06SYS22(newVO.getFunctionCodeSys06SYS16());
          res = QueryUtil.insertTable(
              conn,
              userSessionPars,
              columnVO,
              "SYS22_CUSTOM_COLUMNS",
              attribute2dbField,
              "Y",
              "N",
              context,
              true
          );
          if (res.isError()) {
            conn.rollback();
            return res;
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
              columnVO.setFunctionCodeSys06SYS22(newVO.getFunctionCodeSys06SYS16());
              res = QueryUtil.insertTable(
                  conn,
                  userSessionPars,
                  columnVO,
                  "SYS22_CUSTOM_COLUMNS",
                  attribute2dbField,
                  "Y",
                  "N",
                  context,
                  true
              );
              if (res.isError()) {
                conn.rollback();
                return res;
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

      }


      Response answer = new VOResponse(newVO);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while updating an existing custom function", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}

