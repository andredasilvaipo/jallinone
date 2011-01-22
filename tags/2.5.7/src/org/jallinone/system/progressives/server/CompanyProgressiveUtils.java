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
          "select SYS20_COMPANY_PROGRESSIVES.VALUE,SYS11_APPLICATION_PARS.VALUE,SYS21_COMPANY_PARAMS.VALUE "+
          "from SYS11_APPLICATION_PARS,SYS21_COMPANY_PARAMS LEFT OUTER JOIN SYS20_COMPANY_PROGRESSIVES ON "+
          " SYS20_COMPANY_PROGRESSIVES.COMPANY_CODE_SYS01='"+companyCode+"' and "+
          " SYS20_COMPANY_PROGRESSIVES.TABLE_NAME='"+tableName+"' and "+
          " SYS20_COMPANY_PROGRESSIVES.COLUMN_NAME='"+columnName+"' "+
          "WHERE SYS11_APPLICATION_PARS.PARAM_CODE='INCREMENT_VALUE' AND "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01='"+companyCode+"' AND SYS21_COMPANY_PARAMS.PARAM_CODE='INITIAL_VALUE'"
      );
      rset.next();
      progressive = rset.getBigDecimal(1);
      long incrementValue = rset.getLong(2);
      long initialValue = rset.getLong(3);
      if (progressive!=null) {
        // progressive found: it will be incremented by "incrementValue"...
        progressive = rset.getBigDecimal(1);
        rset.close();
        int rows = stmt.executeUpdate(
            "update SYS20_COMPANY_PROGRESSIVES set VALUE=VALUE+"+incrementValue+" where "+
            "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"' and VALUE="+progressive
        );
        if (rows==0)
          throw new Exception("Updating not performed: the record was previously updated.");

        progressive = new BigDecimal(progressive.intValue()+1);
      }
      else {
        // record not found: it will be inserted, beginning from "initialValue"...
        rset.close();
        stmt.execute(
            "insert into SYS20_COMPANY_PROGRESSIVES(COMPANY_CODE_SYS01,TABLE_NAME,COLUMN_NAME,VALUE) values("+
            "'"+companyCode+"','"+tableName+"','"+columnName+"',"+initialValue+")"
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
    BigDecimal initialValue = null;
    BigDecimal incrementValue = null;
    try {
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select SYS20_COMPANY_PROGRESSIVES.VALUE,SYS21_COMPANY_PARAMS.VALUE,SYS11_APPLICATION_PARS.VALUE "+
          "from SYS21_COMPANY_PARAMS,SYS11_APPLICATION_PARS LEFT OUTER JOIN SYS20_COMPANY_PROGRESSIVES "+
          "ON SYS20_COMPANY_PROGRESSIVES.COMPANY_CODE_SYS01='"+companyCode+"' and "+
          "   SYS20_COMPANY_PROGRESSIVES.TABLE_NAME='"+tableName+"' and "+
          "   SYS20_COMPANY_PROGRESSIVES.COLUMN_NAME='"+columnName+"' "+
          "WHERE SYS11_APPLICATION_PARS.PARAM_CODE='INCREMENT_VALUE' and "+
          "      SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01='"+companyCode+"' and "+
          "      SYS21_COMPANY_PARAMS.PARAM_CODE='INITIAL_VALUE' ");

      rset.next();
      progressive = rset.getBigDecimal(1);
      initialValue = rset.getBigDecimal(2);
      incrementValue = rset.getBigDecimal(3);
      rset.close();
      if (progressive!=null) {
        // record found: it will be incremented by 10...
        int rows = stmt.executeUpdate(
            "update SYS20_COMPANY_PROGRESSIVES set VALUE=VALUE+"+incrementValue+" where "+
            "COMPANY_CODE_SYS01='"+companyCode+"' and TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnName+"' and VALUE="+progressive
        );
        if (rows==0)
          throw new Exception("Updating not performed: the record was previously updated.");

        progressive = new BigDecimal(progressive.intValue()+incrementValue.intValue());
      }
      else {
        // record not found: it will be inserted, beginning from "initialValue"...
        stmt.execute(
            "insert into SYS20_COMPANY_PROGRESSIVES(COMPANY_CODE_SYS01,TABLE_NAME,COLUMN_NAME,VALUE) values("+
            "'"+companyCode+"','"+tableName+"','"+columnName+"',"+initialValue+")"
        );
        progressive = initialValue;
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
