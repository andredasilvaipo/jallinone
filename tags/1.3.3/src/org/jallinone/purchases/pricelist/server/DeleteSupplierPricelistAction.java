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
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete an existing supplier pricelist.</p>
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
public class DeleteSupplierPricelistAction implements Action {


  public DeleteSupplierPricelistAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteSupplierPricelist";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
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
      stmt = conn.createStatement();

      java.util.List list =  (ArrayList)inputPar;
      SupplierPricelistVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete records from PUR04...
        vo = (SupplierPricelistVO)list.get(i);

        stmt.execute("delete from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PUR03()+"' and PRICELIST_CODE_PUR03='"+vo.getPricelistCodePUR03()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03());

        // phisically delete record from SYS10...
        TranslationUtils.deleteTranslations(vo.getProgressiveSys10PUR03(),conn);

        // phisically delete the record in PUR03...
        vo = (SupplierPricelistVO)list.get(i);
        stmt.execute("delete from PUR03_SUPPLIER_PRICELISTS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PUR03()+"' and PRICELIST_CODE='"+vo.getPricelistCodePUR03()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03());
      }

      Response answer = new VOResponse(new Boolean(true));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting an existing supplier pricelist",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
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