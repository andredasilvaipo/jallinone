package org.jallinone.registers.measure.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.registers.measure.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class that retrieve conversion factor beetween two m.u. from REG05 table.</p>
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
public class MeasureConvBean {


  public MeasureConvBean() {
  }


  /**
   * Convert the specified quantity from the specified m.u. to the final m.u.
   * Throws and exception if no conversion factor is defined between the two m.u.
   */
  public final BigDecimal convertQty(String fromUMCode,String toUMCode,BigDecimal qty,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) throws Exception {
    if (qty==null)
      return null;
    if (fromUMCode.equals(toUMCode))
      return qty;
    try {
      return qty.multiply(getConversion(fromUMCode, toUMCode, userSessionPars,
                                        request, response, userSession, context));
    }
    catch (Exception ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"convertQty","Error while fetching measure conversion",ex);
      throw new Exception("no conversion factor is defined between m.u. '"+fromUMCode+"' to '"+toUMCode+"'");
    }
  }


  /**
   * @return conversion factor beetwen the specified m.u.; null if no conversion is defined beetween the two m.u.
   */
  public final BigDecimal getConversion(String fromUMCode,String toUMCode,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) throws Exception {
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      if (fromUMCode.equals(toUMCode))
        return new BigDecimal(1);

      String sql = "select VALUE from REG05_MEASURE_CONV where UM_CODE='"+fromUMCode+"' and UM_CODE_REG02='"+toUMCode+"'";
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      BigDecimal value = null;
      if(rset.next()) {
        value = rset.getBigDecimal(1);
      }
      rset.close();

      return value;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"getConversion","Error while fetching measure conversion",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
