package org.jallinone.system.importdata.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load ETL processes defined in SYS23 table.</p>
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
public class LoadETLProcessesAction implements Action {


  public LoadETLProcessesAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadETLProcesses";
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

      GridParams gridParams = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("IMPORT_DATA");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "SELECT SYS23_ETL_PROCESSES.FILE_FORMAT,SYS23_ETL_PROCESSES.CLASS_NAME,SYS23_ETL_PROCESSES.COMPANY_CODE_SYS01,"+
          "SYS23_ETL_PROCESSES.SCHEDULING_TYPE,SYS23_ETL_PROCESSES.START_TIME,SYS23_ETL_PROCESSES.FILENAME,"+
          "SYS23_ETL_PROCESSES.SUB_TYPE_VALUE,SYS23_ETL_PROCESSES.LEVELS_SEP,SYS23_ETL_PROCESSES.PROGRESSIVE_HIE02,"+
          "SYS23_ETL_PROCESSES.PROGRESSIVE,SYS23_ETL_PROCESSES.DESCRIPTION "+
          "FROM SYS23_ETL_PROCESSES WHERE SYS23_ETL_PROCESSES.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("fileFormatSYS23","SYS23_ETL_PROCESSES.FILE_FORMAT");
      attribute2dbField.put("classNameSYS23","SYS23_ETL_PROCESSES.CLASS_NAME");
      attribute2dbField.put("companyCodeSys01SYS23","SYS23_ETL_PROCESSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("schedulingTypeSYS23","SYS23_ETL_PROCESSES.SCHEDULING_TYPE");
      attribute2dbField.put("startTimeSYS23","SYS23_ETL_PROCESSES.START_TIME");
      attribute2dbField.put("filenameSYS23","SYS23_ETL_PROCESSES.FILENAME");
      attribute2dbField.put("subTypeValueSYS23","SYS23_ETL_PROCESSES.SUB_TYPE_VALUE");
      attribute2dbField.put("levelsSepSYS23","SYS23_ETL_PROCESSES.LEVELS_SEP");
      attribute2dbField.put("progressiveHIE02","SYS23_ETL_PROCESSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSYS23","SYS23_ETL_PROCESSES.PROGRESSIVE");
      attribute2dbField.put("descriptionSYS23","SYS23_ETL_PROCESSES.DESCRIPTION");

      ArrayList values = new ArrayList();


      // read from SYS23 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ETLProcessVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

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


      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching ETL processes list",ex);
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
