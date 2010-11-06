package org.jallinone.sales.charges.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.charges.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch charges from SAL06 table.</p>
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
public class ValidateChargeCodeAction implements Action {


  public ValidateChargeCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateChargeCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    PreparedStatement pstmt = null;
    try {


      LookupValidationParams validationPars = (LookupValidationParams)inputPar;

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

      String sql =
          "select SAL06_CHARGES.COMPANY_CODE_SYS01,SAL06_CHARGES.CHARGE_CODE,SAL06_CHARGES.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,SAL06_CHARGES.VALUE,SAL06_CHARGES.PERC,SAL06_CHARGES.VAT_CODE_REG01,"+
          "SAL06_CHARGES.CURRENCY_CODE_REG03,SAL06_CHARGES.ENABLED"+
          " from SAL06_CHARGES,SYS10_TRANSLATIONS where "+
          "SAL06_CHARGES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL06_CHARGES.ENABLED='Y' and "+
          "SAL06_CHARGES.COMPANY_CODE_SYS01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"' and "+
          "SAL06_CHARGES.CHARGE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL06","SAL06_CHARGES.COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSAL06","SAL06_CHARGES.CHARGE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL06","SAL06_CHARGES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL06","SAL06_CHARGES.VALUE");
      attribute2dbField.put("percSAL06","SAL06_CHARGES.PERC");
      attribute2dbField.put("vatCodeReg01SAL06","SAL06_CHARGES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL06","SAL06_CHARGES.CURRENCY_CODE_REG03");
      attribute2dbField.put("enabledSAL06","SAL06_CHARGES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from SAL06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ChargeVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true,
          new BigDecimal(292) // window identifier...
      );

      if (res.isError())
        return res;

      java.util.List list = ((VOListResponse)res).getRows();
      ChargeVO vo = null;
      sql =
          "select SYS10_TRANSLATIONS.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE "+
          "from SYS10_TRANSLATIONS,REG01_VATS where "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG01_VATS.VAT_CODE=?";
      pstmt = conn.prepareStatement(sql);
      ResultSet rset = null;
      for(int i=0;i<list.size();i++) {
        vo = (ChargeVO)list.get(i);
        if (vo.getVatCodeReg01SAL06()!=null) {
          // retrieve vat data from REG01...
          pstmt.setString(1,serverLanguageId);
          pstmt.setString(2,vo.getVatCodeReg01SAL06());
          rset = pstmt.executeQuery();
          if (rset.next()) {
            vo.setVatDescriptionSYS10(rset.getString(1));
            vo.setVatValueREG01(rset.getBigDecimal(2));
            vo.setVatDeductibleREG01(rset.getBigDecimal(3));
          }
          rset.close();
        }
      }

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating charge code",ex);
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
