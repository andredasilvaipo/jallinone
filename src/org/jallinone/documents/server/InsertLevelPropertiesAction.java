package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.documents.java.DetailDocumentVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.documents.java.LevelPropertyVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new level properties for the specified doc. hierarchy level.</p>
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
public class InsertLevelPropertiesAction implements Action {


  public InsertLevelPropertiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertLevelProperties";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
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

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC16").get(0).toString();

      LevelPropertyVO vo = null;


      java.util.List list = (ArrayList)inputPar;
      BigDecimal progressiveSys10DOC21 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC21","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSys10DOC21","PROGRESSIVE_SYS10");
      attribute2dbField.put("progressiveHie01DOC21","PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02DOC21","PROGRESSIVE_HIE02");
      attribute2dbField.put("propertyTypeDOC21","PROPERTY_TYPE");

      Response res = null;

      for(int i=0;i<list.size();i++) {
        vo = (LevelPropertyVO)list.get(i);
        if (vo.getCompanyCodeSys01DOC21() == null)
          vo.setCompanyCodeSys01DOC21(companyCode);

        // insert record in SYS10 and generate progressive for property description...
        progressiveSys10DOC21 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01DOC21(),conn);
        vo.setProgressiveSys10DOC21(progressiveSys10DOC21);

        // insert into DOC21...
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "DOC21_LEVEL_PROPERTIES",
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting new level properties",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }
  }



}
