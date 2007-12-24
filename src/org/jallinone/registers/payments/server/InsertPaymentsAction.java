package org.jallinone.registers.payments.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.registers.payments.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new payments in REG10 table.</p>
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
public class InsertPaymentsAction implements Action {


  public InsertPaymentsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertPayments";
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
      PaymentVO vo = null;

      ArrayList list = (ArrayList)inputPar;
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeREG10","PAYMENT_CODE");
      attribute2dbField.put("progressiveSys10REG10","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG10","ENABLED");
      attribute2dbField.put("stepREG10","STEP");
      attribute2dbField.put("instalmentNumberREG10","INSTALMENT_NUMBER");
      attribute2dbField.put("startDayREG10","START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","PAYMENT_TYPE_CODE_REG11");
      attribute2dbField.put("firstInstalmentDaysREG10","FIRST_INSTALMENT_DAYS");

      pstmt = conn.prepareStatement(
        "insert into REG17_PAYMENT_INSTALMENTS(PAYMENT_CODE_REG10,RATE_NUMBER,PAYMENT_TYPE_CODE_REG11,PERCENTAGE,INSTALMENT_DAYS) "+
        "values(?,?,?,?,?)"
      );

      int days;
      BigDecimal total;
      BigDecimal ratePerc = null;

      for(int j=0;j<list.size();j++) {
        vo = (PaymentVO)list.get(j);
        vo.setEnabledREG10("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
        vo.setProgressiveSys10REG10(progressiveSYS10);

        // insert into REG10...
        Response res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "REG10_PAYMENTS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            new BigDecimal(212) // window identifier...
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

        // insert instalments into REG17...
        days = vo.getFirstInstalmentDaysREG10().intValue();
        total = new BigDecimal(0);
        ratePerc = null;
        for(int i=0;i<vo.getInstalmentNumberREG10().intValue();i++) {
          pstmt.setString(1,vo.getPaymentCodeREG10());
          pstmt.setInt(2,i+1);
          pstmt.setString(3,vo.getPaymentTypeCodeReg11REG10());
          if (i+1<vo.getInstalmentNumberREG10().intValue()) {
            ratePerc = new BigDecimal(100).divide(vo.getInstalmentNumberREG10(), 5,BigDecimal.ROUND_HALF_UP);
            total = total.add(ratePerc);
            pstmt.setBigDecimal(4,ratePerc);
          }
          else
            pstmt.setBigDecimal(4,new BigDecimal(100).subtract(total));
          pstmt.setInt(5,days);
          days += vo.getStepREG10().intValue();
          pstmt.execute();
        }
      }

      Response answer = new VOListResponse(list,false,list.size());

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
                   "executeCommand", "Error while inserting new payments", ex);
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

