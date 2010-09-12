package org.jallinone.warehouse.server;


import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import java.math.BigDecimal;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.jallinone.sales.documents.server.ValidatePriceItemCodeBean;
import org.jallinone.sales.documents.java.PriceItemVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate a barcode from WAR05 table.</p>
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
public class ValidateStoredSerialNumberAction implements Action {


  public ValidateStoredSerialNumberAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateStoredSerialNumber";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      LookupValidationParams pars = (LookupValidationParams)inputPar;
      String companyCodeSys01 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
      BigDecimal progressiveHie01 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE01);

      ArrayList params = new ArrayList();
      params.add(companyCodeSys01);
      String sql =
          "select WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01,WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER,"+
          "WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01,WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15 "+
          "from "+
          "WAR05_STORED_SERIAL_NUMBERS where "+
          "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=? ";
      if (progressiveHie01!=null) {
        sql += " and WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01=? ";
        params.add(progressiveHie01);
      }
      sql += " and WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER=? ";
      params.add(pars.getCode());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR05","WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01");
      attribute2dbField.put("serialNumberWAR05","WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER");
      attribute2dbField.put("itemCodeItm01WAR05","WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie01WAR05","WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01");
      attribute2dbField.put("variantTypeItm06WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15");


      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          params,
          attribute2dbField,
          StoredSerialNumberVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true
      );

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

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating serial number",ex);
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
