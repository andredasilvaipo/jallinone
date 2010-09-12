package org.jallinone.warehouse.movements.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new manual warehouse movement.</p>
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
public class InsertManualMovementAction implements Action {

  private AddMovementBean movBean = new AddMovementBean();


  public InsertManualMovementAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertManualMovement";
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

      MovementVO vo = (MovementVO)inputPar;

      WarehouseMovementVO movVO = new WarehouseMovementVO(
          vo.getProgressiveHie01WAR02(),
          vo.getDeltaQtyWAR02(),
          vo.getCompanyCodeSys01WAR02(),
          vo.getWarehouseCodeWar01WAR02(),
          vo.getItemCodeItm01WAR02(),
          vo.getWarehouseMotiveWar04WAR02(),
          vo.getItemTypeWAR04(),
          vo.getNoteWAR02(),
          vo.getSerialNumbers(),

          vo.getVariantCodeItm11WAR02(),
          vo.getVariantCodeItm12WAR02(),
          vo.getVariantCodeItm13WAR02(),
          vo.getVariantCodeItm14WAR02(),
          vo.getVariantCodeItm15WAR02(),
          vo.getVariantTypeItm06WAR02(),
          vo.getVariantTypeItm07WAR02(),
          vo.getVariantTypeItm08WAR02(),
          vo.getVariantTypeItm09WAR02(),
          vo.getVariantTypeItm10WAR02()

      );

      Response res = movBean.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      Response answer = new VOResponse(vo);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new manual warehouse movement",ex);
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
