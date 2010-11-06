package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.purchases.pricelist.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new supplier pricelists in PUR03 table.</p>
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
public class InsertSupplierPricelistsAction implements Action {


  public InsertSupplierPricelistsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertSupplierPricelists";
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
      java.util.List list = (ArrayList)inputPar;
      SupplierPricelistVO vo = null;
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR03","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePUR03","PRICELIST_CODE");
      attribute2dbField.put("progressiveReg04PUR03","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSys10PUR03","PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03PUR03","CURRENCY_CODE_REG03");
      Response res = null;

      pstmt = conn.prepareStatement(
        "insert into PUR04_SUPPLIER_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_PUR03,PROGRESSIVE_REG04,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE) "+
        "select ?,?,?,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?"
      );

      for (int i=0;i<list.size();i++) {
        vo = (SupplierPricelistVO)list.get(i);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PUR03(),conn);
        vo.setProgressiveSys10PUR03(progressiveSYS10);

        // insert into PUR03...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "PUR03_SUPPLIER_PRICELISTS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            ApplicationConsts.ID_SUPPLIER_PRICELIST_GRID // window identifier...
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

        // test if there will have to copy all prices from an existing pricelist...
        if (vo.getOldPricelistCodePur03PUR04()!=null) {
          pstmt.setString(1,vo.getCompanyCodeSys01PUR03());
          pstmt.setString(2,vo.getPricelistCodePUR03());
          pstmt.setBigDecimal(3,vo.getProgressiveReg04PUR03());
          pstmt.setString(4,vo.getCompanyCodeSys01PUR03());
          pstmt.setString(5,vo.getOldPricelistCodePur03PUR04());
          pstmt.setBigDecimal(6,vo.getProgressiveReg04PUR03());
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
                   "executeCommand", "Error while inserting new supplier pricelists", ex);
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

