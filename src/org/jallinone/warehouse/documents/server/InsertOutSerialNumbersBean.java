package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to delete and re-insert serial numbers in DOC12 table.</p>
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
public class InsertOutSerialNumbersBean {

  public InsertOutSerialNumbersBean() {
  }


  /**
   * Delete and re-insert serial numbers in DOC11 table.
   * It does not comit or rollback the connection.
   */
  public final Response reinsertOutSerialNumbers(GridOutDeliveryNoteRowVO vo,Connection conn,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertOutSerialNumbersBean.reinsertOutSerialNumbers",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));
      ArrayList serialNums = vo.getSerialNumbers();

      // delete previous serial numbers from DOC11...
      pstmt = conn.prepareStatement(
       "delete from DOC12_OUT_SERIAL_NUMBERS where PROGRESSIVE_DOC10=?"
      );

      pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
      pstmt.execute();
      pstmt.close();

      pstmt = conn.prepareStatement(
        "insert into DOC12_OUT_SERIAL_NUMBERS(PROGRESSIVE_DOC10,SERIAL_NUMBER) values(?,?)"
      );

      for(int i=0;i<serialNums.size();i++) {
         pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
         pstmt.setString(2,(String)serialNums.get(i));
         pstmt.execute();
      }

      Response answer = new VOResponse(Boolean.TRUE);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertOutSerialNumbersBean.reinsertOutSerialNumbers",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"reinsertOutSerialNumbers","Error while creating serial numbers in DOC12 table.",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }




}
