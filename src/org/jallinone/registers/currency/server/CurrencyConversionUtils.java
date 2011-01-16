package org.jallinone.registers.currency.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.registers.currency.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to execute a currency conversion between two currencies.</p>
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
public class CurrencyConversionUtils {


  /**
   * Converts a currency value from one currency to another one.
   * @param value value to convert
   * @param fromCurrencyCode currency code of the current value
   * @param toCurrencyCode currency code used to convert the value
   * @return new value, expressed in the new currency code
   * @throws java.lang.Exception if no conversion factor is defined between the two currencies
   * Note: connection is not released
   */
  public static final BigDecimal convertCurrencyToCurrency(BigDecimal value,String fromCurrencyCode,String toCurrencyCode,Connection conn) throws Exception {
    return convertCurrencyToCurrency(value,getConversionFactor(fromCurrencyCode,toCurrencyCode,conn));
  }


  /**
   * Retrieve the conversion factor between two currencies.
   * @param fromCurrencyCode starting currency code
   * @param toCurrencyCode final currency code
   * @return currency conversion factor (if defined)
   * @throws java.lang.Exception if no conversion factor is defined between the two currencies
   * Note: connection is not released
   */
  public static final BigDecimal getConversionFactor(String fromCurrencyCode,String toCurrencyCode,Connection conn) throws Exception {
    if (fromCurrencyCode.equals(toCurrencyCode))
      return new BigDecimal(1);

    Statement stmt = null;
    String errorMsg = null;
    boolean error = false;
    BigDecimal conv = null;
    try {
      String sql =
          "select REG06_CURRENCY_CONV.VALUE from REG06_CURRENCY_CONV where "+
          "REG06_CURRENCY_CONV.CURRENCY_CODE_REG03='"+fromCurrencyCode+"' and "+
          "REG06_CURRENCY_CONV.CURRENCY_CODE2_REG03='"+toCurrencyCode+"'";

      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      if(rset.next()) {
        conv = rset.getBigDecimal(1);
      }
      else {
        error = true;
        errorMsg = "no currency defined";
      }
      rset.close();

      if (conv==null) {
        error = true;
        errorMsg = "no currency conversion defined";
      }

    }
    catch (Throwable ex) {
      error = true;
      errorMsg = ex.toString();
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
    }
    if (error)
      throw new Exception(errorMsg);
    else
      return conv;
  }


  /**
   * Retrieve the conversion factor from the specified currency to the company currency.
   * @param fromCurrencyCode starting currency code
   * @param companyCode company code used to retrieve the final currency code
   * @return currency conversion factor (if defined)
   * @throws java.lang.Exception if no conversion factor is defined between the two currencies
   * Note: connection is not released
   */
  public static final BigDecimal getCompanyConversionFactor(String fromCurrencyCode,String companyCode,Connection conn) throws Exception {
    return getConversionFactor(fromCurrencyCode,getCompanyCurrencyCode(companyCode,conn),conn);
  }


  /**
   * Retrieve currency code related to the specified company.
   * @param companyCode company code used to retrieve the final currency code
   * @return currency code associated to the specified company
   * @throws java.lang.Exception if no company was found
   * Note: connection is not released
   */
  public static final String getCompanyCurrencyCode(String companyCode,Connection conn) throws Exception {
    Statement stmt = null;
    String errorMsg = null;
    boolean error = false;
    String toCurrencyCode = null;
    try {
      String sql = "select CURRENCY_CODE_REG03 from SYS01_COMPANIES where COMPANY_CODE='"+companyCode+"'";

      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      if(rset.next()) {
        toCurrencyCode = rset.getString(1);
      }
      else {
        error = true;
        errorMsg = "no company found";
      }
      rset.close();

    }
    catch (Throwable ex) {
      error = true;
      errorMsg = ex.toString();
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
    }
    if (error)
      throw new Exception(errorMsg);
    else
      return toCurrencyCode;
  }


  /**
   * Converts a currency value from one currency to another one.
   * @param value value to convert
   * @param conversionFactor conversion factor to use
   * @return new value, expressed in the new currency code, using the specified conversion factor
   */
  public static final BigDecimal convertCurrencyToCurrency(BigDecimal value,BigDecimal conversionFactor) {
    if (value==null)
      return null;
    return value.multiply(conversionFactor).setScale(value.scale(),BigDecimal.ROUND_HALF_UP);
  }



}
