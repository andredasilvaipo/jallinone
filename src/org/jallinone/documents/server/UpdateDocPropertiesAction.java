package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.documents.java.*;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update property value for the specified document.</p>
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
public class UpdateDocPropertiesAction implements Action {


  public UpdateDocPropertiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateDocProperties";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC20","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDoc14DOC20","PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveSys10DOC20","PROGRESSIVE_SYS10");
      attribute2dbField.put("textValueDOC20","TEXT_VALUE");
      attribute2dbField.put("numValueDOC20","NUM_VALUE");
      attribute2dbField.put("dateValueDOC20","DATE_VALUE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC20");
      pkAttributes.add("progressiveDoc14DOC20");
      pkAttributes.add("progressiveSys10DOC20");

      Response res = null;
      DocPropertyVO oldVO = null;
      DocPropertyVO newVO = null;

      pstmt = conn.prepareStatement(
        "select PROGRESSIVE_DOC14 from DOC20_DOC_PROPERTIES where "+
        "COMPANY_CODE_SYS01=? and PROGRESSIVE_DOC14=? and PROGRESSIVE_SYS10=?"
      );
      ResultSet rset = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (DocPropertyVO)oldVOs.get(i);
        newVO = (DocPropertyVO)newVOs.get(i);

        // check if the record already exists: if it does not exist, then insert it...
        pstmt.setString(1,newVO.getCompanyCodeSys01DOC20());
        pstmt.setBigDecimal(2,newVO.getProgressiveDoc14DOC20());
        pstmt.setBigDecimal(3,newVO.getProgressiveSys10DOC20());
        rset = pstmt.executeQuery();
        if(rset.next()) {
          // the record exixts: it will be updated...
          res = QueryUtil.updateTable(
              conn,
              userSessionPars,
              pkAttributes,
              oldVO,
              newVO,
              "DOC20_DOC_PROPERTIES",
              attribute2dbField,
              "Y",
              "N",
              context,
              true
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
        else {
          // the record does not exixt: it will be inserted...
          res = QueryUtil.insertTable(
              conn,
              userSessionPars,
              newVO,
              "DOC20_DOC_PROPERTIES",
              attribute2dbField,
              "Y",
              "N",
              context,
              true
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
        rset.close();


      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating property values for the specified document",ex);
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
