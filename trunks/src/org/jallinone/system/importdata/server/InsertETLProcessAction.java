package org.jallinone.system.importdata.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.registers.color.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantTypeVO;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.importdata.java.SelectableFieldVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new ETL process in SYS23 table.</p>
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
public class InsertETLProcessAction implements Action {

  private DeleteETLProcessesBean bean = new DeleteETLProcessesBean();


  public InsertETLProcessAction() {}



  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertETLProcess";
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

      Object[] objs = (Object[])inputPar;
      ETLProcessVO processVO = (ETLProcessVO)objs[0];
      java.util.List fieldsVO = (java.util.List)objs[1];

      // remove existing records...
      ArrayList vos = new ArrayList();
      vos.add(processVO);
      Response answer = bean.deleteETLProcesses(conn,vos,userSessionPars);
      if (answer.isError()) {
        conn.rollback();
        return answer;
      }

      BigDecimal progressive = CompanyProgressiveUtils.getInternalProgressive(
        ((JAIOUserSessionParameters)userSessionPars).getDefCompanyCodeSys01SYS03(),
        "SYS23_ETL_PROCESSES",
        "PROGRESSIVE",
        conn
      );
      processVO.setProgressiveSYS23(progressive);


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("fileFormatSYS23","FILE_FORMAT");
      attribute2dbField.put("classNameSYS23","CLASS_NAME");
      attribute2dbField.put("companyCodeSys01SYS23","COMPANY_CODE_SYS01");
      attribute2dbField.put("schedulingTypeSYS23","SCHEDULING_TYPE");
      attribute2dbField.put("startTimeSYS23","START_TIME");
      attribute2dbField.put("filenameSYS23","FILENAME");
      attribute2dbField.put("subTypeValueSYS23","SUB_TYPE_VALUE");
      attribute2dbField.put("levelsSepSYS23","LEVELS_SEP");
      attribute2dbField.put("progressiveHIE02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSYS23","PROGRESSIVE");
      attribute2dbField.put("descriptionSYS23","DESCRIPTION");

      // insert into SYS23...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          processVO,
          "SYS23_ETL_PROCESSES",
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
      processVO = (ETLProcessVO)((VOResponse)res).getVo();
      answer = res;


      attribute2dbField.clear();
      attribute2dbField.put("fieldNameSYS24","FIELD_NAME");
      attribute2dbField.put("languageCodeSYS24","LANGUAGE_CODE");
      attribute2dbField.put("startPosSYS24","START_POS");
      attribute2dbField.put("endPosSYS24","END_POS");
      attribute2dbField.put("dateFormatSYS24","DATE_FORMAT");
      attribute2dbField.put("posSYS24","POS");
      attribute2dbField.put("progressiveSys23SYS24","PROGRESSIVE_SYS23");
      attribute2dbField.put("progressiveSYS24","PROGRESSIVE");

      SelectableFieldVO field = null;
      progressive = null;
      for(int i=0;i<fieldsVO.size();i++) {
        field = (SelectableFieldVO)fieldsVO.get(i);
        if (field.isSelected()) {
          field.getField().setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

          progressive = CompanyProgressiveUtils.getInternalProgressive(
            ((JAIOUserSessionParameters)userSessionPars).getDefCompanyCodeSys01SYS03(),
            "SYS24_ETL_PROCESS_FIELDS",
            "PROGRESSIVE",
            conn
          );
          field.getField().setProgressiveSYS24(progressive);
          field.getField().setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

          // insert into SYS24...
          res = QueryUtil.insertTable(
              conn,
              userSessionPars,
              field.getField(),
              "SYS24_ETL_PROCESS_FIELDS",
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
      }

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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(), "executeCommand", "Error while inserting a new ETL process", ex);
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

