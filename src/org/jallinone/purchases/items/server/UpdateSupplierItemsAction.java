package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing supplier items.</p>
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
public class UpdateSupplierItemsAction implements Action {


  public UpdateSupplierItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSupplierItems";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];

      SupplierItemVO oldVO = null;
      SupplierItemVO newVO = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","ENABLED");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01PUR02");
      pkAttributes.add("itemCodeItm01PUR02");
      pkAttributes.add("progressiveReg04PUR02");

      Response res = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SupplierItemVO)oldVOs.get(i);
        newVO = (SupplierItemVO)newVOs.get(i);

        // update PUR02 table...
        res = CustomizeQueryUtil.updateTable(
            conn,
            userSessionPars,
            pkAttributes,
            oldVO,
            newVO,
            "PUR02_SUPPLIER_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            ApplicationConsts.ID_SUPPLIER_ITEMS_GRID // window identifier...
        );

        if (res.isError()) {
          conn.rollback();
          return res;
        }

      }


      Response answer =  new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing supplier items",ex);
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
