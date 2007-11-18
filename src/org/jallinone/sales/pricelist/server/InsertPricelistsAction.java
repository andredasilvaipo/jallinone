package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.pricelist.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new pricelists in SAL01 table.</p>
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
public class InsertPricelistsAction implements Action {


  public InsertPricelistsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertPricelists";
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
     PricelistVO vo = null;

      ArrayList list = (ArrayList)inputPar;
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL01","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSAL01","PRICELIST_CODE");
      attribute2dbField.put("progressiveSys10SAL01","PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL01","CURRENCY_CODE_REG03");

      Response res = null;
      BigDecimal progressiveSYS10 = null;
      pstmt = conn.prepareStatement(
        "insert into SAL02_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_SAL01,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE) "+
        "select ?,?,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE from SAL02_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=?"
      );
      for(int i=0;i<list.size();i++) {
        vo = (PricelistVO)list.get(i);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
        vo.setProgressiveSys10SAL01(progressiveSYS10);

        // insert into SAL01...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "SAL01_PRICELISTS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            new BigDecimal(302) // window identifier...
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

        // test if there will have to copy all prices from an existing pricelist...
        if (vo.getOldPricelistCodeSal01SAL02()!=null) {
          pstmt.setString(1,vo.getCompanyCodeSys01SAL01());
          pstmt.setString(2,vo.getPricelistCodeSAL01());
          pstmt.setString(3,vo.getCompanyCodeSys01SAL01());
          pstmt.setString(4,vo.getOldPricelistCodeSal01SAL02());
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
                   "executeCommand", "Error while inserting new pricelists", ex);
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

