package org.jallinone.sqltool.server;

import java.math.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.sqltool.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Utility class used to execute a query statement and retrieve column info.</p>
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
public class GetQueryInfoUtil  {



  /**
   * Business logic to execute.
   */
  public static VOResponse getQueryInfo(TableVO tableVO,String serverLanguageId,String username) throws Throwable {
    ResultSet rset = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);
      String sql = tableVO.getSql();
      stmt = conn.createStatement();

      String sql2 = sql;
      if (sql2.toLowerCase().indexOf(" where ")!=-1 &&
          sql2.indexOf("?",sql2.toLowerCase().indexOf(" where "))!=-1) {
        // replace each "?" with null
        sql2 = sql2.replaceAll("\\?","null");
      }

      rset = stmt.executeQuery(sql2);

      // create a list of ColumnVO objects...
      ColumnVO vo = null;
//      ArrayList colNames = new ArrayList();
//      String selectClause = sql.substring(6,sql.toLowerCase().indexOf(" from "));
//      if (selectClause.trim().toLowerCase().indexOf(" distinct ")!=-1)
//        selectClause = selectClause.substring(selectClause.trim().toLowerCase().indexOf(" distinct ")+9);
//      if (selectClause.indexOf("*")==-1) {
//        String[] tokens = selectClause.split(",");
//        for(int i=0;i<tokens.length;i++)
//          if (tokens[i].indexOf(" ")!=-1)
//            colNames.add(tokens[i].substring(tokens[i].indexOf(" ")));
//          else
//            colNames.add(tokens[i]);
//      }
      int t;
      int scount = 0;
      int dcount = 0;
      int ncount = 0;
      StringBuffer sb = null;
      ResultSetMetaData meta = rset.getMetaData();
//      if (colNames.size()>0 && meta.getColumnCount()!=colNames.size()) {
//        return new ErrorResponse("select has not been correctly parsed");
//      }
      Hashtable tables = new Hashtable();
      String aux = null;
      String select = "select ";
			String tName = null;
      for(int i=0;i<meta.getColumnCount();i++) {
        vo = new ColumnVO(tableVO);
        t = meta.getColumnType(i+1);
        vo.setColumnRequired(meta.isNullable(i+1)==meta.columnNoNulls);
        vo.setColumnSize(new BigDecimal(meta.getPrecision(i+1)));
        vo.setColumnVisible(true);
        vo.setColumnValues(new ArrayList());
        vo.setColumnSqlType(t);

        if (t==Types.DATE || t==Types.TIMESTAMP || t==Types.TIME) {
          vo.setAttributeName("attributeNameD"+dcount);
          dcount++;
          vo.setColumnDec(new BigDecimal(0));
          vo.setColumnType(meta.getColumnTypeName(i+1));
        }
        else if (t==Types.BIGINT || t==Types.INTEGER || t==Types.NUMERIC || t==Types.SMALLINT) {
          vo.setAttributeName("attributeNameN"+ncount);
          ncount++;
          vo.setColumnDec(new BigDecimal(0));
          vo.setColumnType(meta.getColumnTypeName(i+1)+"("+vo.getColumnSize().intValue()+")");
        }
        else if (t==Types.DECIMAL || t==Types.DOUBLE || t==Types.FLOAT || t==Types.REAL) {
          vo.setAttributeName("attributeNameN"+ncount);
          ncount++;
          vo.setColumnDec(new BigDecimal(meta.getScale(i+1)));
          if (vo.getColumnDec().equals(new BigDecimal(0)))
            vo.setColumnType(meta.getColumnTypeName(i+1)+"("+vo.getColumnSize().intValue()+")");
          else
            vo.setColumnType(meta.getColumnTypeName(i+1)+"("+vo.getColumnSize().intValue()+","+vo.getColumnDec().intValue()+")");
        }
        else {
          vo.setAttributeName("attributeNameS"+scount);
          scount++;
          vo.setColumnDec(new BigDecimal(0));
          vo.setColumnType(meta.getColumnTypeName(i+1)+"("+vo.getColumnSize().intValue()+")");
        }

    		tName = meta.getTableName(i+1);
        tables.put(tName,new ArrayList());
        vo.setColumnName((tName==null || tName.equals("")?"":(tName+"."))+meta.getColumnName(i+1));
        select += vo.getColumnName()+",";
//        else
//          vo.setColumnName(colNames.get(i).toString());
        if (tableVO.isConvertColumnHeaders()) {
          sb = new StringBuffer(vo.getColumnName().replace('_',' ').toLowerCase());
          if (sb.indexOf(".")!=-1)
            sb = sb.delete(0,sb.indexOf(".")+1);
          for(int j=0;j<sb.length();j++)
            if (j==0 || sb.charAt(j-1)==' ')
              sb = sb.replace(j,j+1,String.valueOf(sb.charAt(j)).toUpperCase());
          vo.setColumnHeaderName(sb.toString());
        }
        else
          vo.setColumnHeaderName(vo.getColumnName().substring(vo.getColumnName().indexOf(".")+1));
        tableVO.addColumn(vo);
      }

      // check if sql is in the format: "select [... distinct] * from ...";
      select = select.substring(0,select.length()-1);
      String selectClause = sql.substring(6,sql.toLowerCase().indexOf(" from "));
      if (selectClause.trim().toLowerCase().indexOf(" distinct ")!=-1)
        selectClause = selectClause.substring(selectClause.trim().toLowerCase().indexOf(" distinct ")+9);
      if (selectClause.indexOf("*")!=-1) {
        sql = select+sql.substring(sql.indexOf(" from "));
        tableVO.setSql(sql);
      }


      // retrieve primary keys for each referred table...
      Enumeration en = tables.keys();
      String tableName = null;
      ArrayList pks = null;
      while(en.hasMoreElements()) {
        rset.close();
        stmt.close();

        tableName = en.nextElement().toString();
        pks = (ArrayList)tables.get(tableName);
        rset = conn.getMetaData().getPrimaryKeys(null,null,tableName.equals("")?tableVO.getMainTables().get(0).toString():tableName);
        stmt = rset.getStatement();
        while(rset.next()) {
          pks.add((tableName.equals("")?"":(tableName+"."))+rset.getString(4));
        }
        tableVO.addPrimaryKeys(tableName,pks);
      }


      // retrieve foreign keys (that will become lookups...)
      try {
        try {
          rset.close();
          stmt.close();
        }
        catch (Exception ex4) {
        }
        rset = conn.getMetaData().getImportedKeys(null, null, tableName);
        stmt = rset.getStatement();
        Hashtable fks = new Hashtable(); // collection of pairs <FKEntryVO,ForeignKeyVO>
        FKEntryVO entry = null;
        ForeignKeyVO fk = null;
        while(rset.next()) {
          entry = new FKEntryVO(rset.getString(7),rset.getString(3));
          fk = (ForeignKeyVO)fks.get(entry);
          if (fk==null) {
            fk = new ForeignKeyVO(entry.getFkTableName(),entry.getPkTableName());
            fks.put(entry,fk);
          }
          fk.addFieldName(rset.getString(8),rset.getString(4));
        }
        tableVO.setForeingKeys(fks);
      }
      catch (Exception ex2) {
        // maybe the database does not support this feature...
      }



      return new VOResponse(tableVO);
    }
    catch (Throwable ex) {
      Logger.error(username,GetQueryInfoUtil.class.getName(),"executeCommand","Error while retrieving query info",ex);
      try {
   		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex3) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex3) {
      }
  	try {
		ConnectionManager.releaseConnection(conn, null);
	} catch (Exception e) {
	}

    }

  }


}

