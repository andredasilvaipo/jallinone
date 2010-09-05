package org.jallinone.system.progressives.server;

import java.sql.*;
import java.math.BigDecimal;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Utility class used generate progressives, based on the specified COMPANY_CODE.</p>
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
public class CompanyProgressiveUtils {


  /**
   * Generate a visibile (consecutive) progressive, incremented by 1, beginning from 1, based on the specified tablename.columnname
   * @param tableName table name used to calculate the progressive
   * @param columnName column name used to calculate the progressive
   * @param conn database connection
   * @return progressive value
   */
  public static final BigDecimal getConsecutiveProgressive(String companyCode,String tableName,String columnName,Connection conn) throws Exception {
    Statement stmt = null;
    BigDecimal progressive = null;
    try {
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select VALUE from SYS20_COMPANY_PROGRESSIVES where "+
          "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"'");
      if (rset.next()) {
        // record found: it will be incremented by 10...
        progressive = rset.getBigDecimal(1);
        rset.close();
        int rows = stmt.executeUpdate(
            "update SYS20_COMPANY_PROGRESSIVES set VALUE=VALUE+1 where "+
            "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"' and VALUE="+progressive
        );
        if (rows==0)
          throw new Exception("Updating not performed: the record was previously updated.");

        progressive = new BigDecimal(progressive.intValue()+1);
      }
      else {
        // record not found: it will be inserted, beginning from 1...
        rset.close();
        stmt.execute(
            "insert into SYS20_COMPANY_PROGRESSIVES(COMPANY_CODE_SYS01,TABLE_NAME,COLUMN_NAME,VALUE) values("+
            "'"+companyCode+"','"+tableName+"','"+columnName+"',1)"
        );
        progressive = new BigDecimal(1);
      }
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
    }
    return progressive;
  }


  /**
   * Generate an internal (non consecutive) progressive, incremented by 10, beginning from 1, based on the specified tablename.columnname
   * @param tableName table name used to calculate the progressive
   * @param columnName column name used to calculate the progressive
   * @param conn database connection
   * @return progressive value
   */
  public static final BigDecimal getInternalProgressive(String companyCode,String tableName,String columnName,Connection conn) throws Exception {
    Statement stmt = null;
    BigDecimal progressive = null;
    try {
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select VALUE from SYS20_COMPANY_PROGRESSIVES where "+
          "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"'");
      if (rset.next()) {
        // record found: it will be incremented by 10...
        progressive = rset.getBigDecimal(1);
        rset.close();
        int rows = stmt.executeUpdate(
            "update SYS20_COMPANY_PROGRESSIVES set VALUE=VALUE+10 where "+
            "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"' and VALUE="+progressive
        );
        if (rows==0)
          throw new Exception("Updating not performed: the record was previously updated.");

        progressive = new BigDecimal(progressive.intValue()+10);
      }
      else {
        // record not found: it will be inserted, beginning from 1...
        rset.close();
        stmt.execute(
            "insert into SYS20_COMPANY_PROGRESSIVES(COMPANY_CODE_SYS01,TABLE_NAME,COLUMN_NAME,VALUE) values("+
            "'"+companyCode+"','"+tableName+"','"+columnName+"',1)"
        );
        progressive = new BigDecimal(1);
      }
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
    }
    return progressive;
  }




}
