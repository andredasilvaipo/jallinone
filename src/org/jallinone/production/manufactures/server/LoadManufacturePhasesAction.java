package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch manufacture phases from PRO02 table.</p>
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
public class LoadManufacturePhasesAction implements Action {


  public LoadManufacturePhasesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadManufacturePhases";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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
      ManufactureVO vo = (ManufactureVO)gridParams.getOtherGridParams().get(ApplicationConsts.MANUFACTURE_VO);

      String sql =
          "select PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01,PRO02_MANUFACTURE_PHASES.PROGRESSIVE,SYS10_TRANSLATIONS.DESCRIPTION,PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01,"+
          "PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10,PRO02_MANUFACTURE_PHASES.QTY,PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07,"+
          "PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03,PRO02_MANUFACTURE_PHASES.PHASE_NUMBER,PRO02_MANUFACTURE_PHASES.MANUFACTURE_TYPE,"+
          "PRO02_MANUFACTURE_PHASES.DURATION,PRO02_MANUFACTURE_PHASES.VALUE,PRO02_MANUFACTURE_PHASES.COMPLETION_PERC,PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04,"+
          "PRO02_MANUFACTURE_PHASES.NOTE,PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04,PRO04_ALIAS.DESCRIPTION,TASKS.DESCRIPTION,PRO03.DESCRIPTION "+
          "from SYS10_TRANSLATIONS,PRO02_MANUFACTURE_PHASES "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO04_OPERATIONS.OPERATION_CODE,PRO04_OPERATIONS.COMPANY_CODE_SYS01 "+
          "from PRO04_OPERATIONS,SYS10_TRANSLATIONS where "+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO04_ALIAS ON "+
          "PRO04_ALIAS.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "PRO04_ALIAS.OPERATION_CODE=PRO02_MANUFACTURE_PHASES.SUBST_OPERATION_CODE_PRO04 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE "+
          "from REG07_TASKS,SYS10_TRANSLATIONS where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) TASKS ON "+
          "TASKS.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "TASKS.TASK_CODE=PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO03 ON "+
          "PRO03.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "PRO03.MACHINERY_CODE=PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03 "+
          "where "+
          "PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01=? and "+
          "PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO02","PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("manufactureCodePro01PRO02","PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("progressivePRO02","PRO02_MANUFACTURE_PHASES.PROGRESSIVE");
      attribute2dbField.put("progressiveSys10PRO02","PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO02","PRO02_MANUFACTURE_PHASES.QTY");
      attribute2dbField.put("taskCodeReg07PRO02","PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO02","PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03");
      attribute2dbField.put("phaseNumberPRO02","PRO02_MANUFACTURE_PHASES.PHASE_NUMBER");
      attribute2dbField.put("manufactureTypePRO02","PRO02_MANUFACTURE_PHASES.MANUFACTURE_TYPE");
      attribute2dbField.put("durationPRO02","PRO02_MANUFACTURE_PHASES.DURATION");
      attribute2dbField.put("valuePRO02","PRO02_MANUFACTURE_PHASES.VALUE");
      attribute2dbField.put("completionPercPRO02","PRO02_MANUFACTURE_PHASES.COMPLETION_PERC");
      attribute2dbField.put("substOperationCodePro04PRO02","PRO02_MANUFACTURE_PHASES.SUBST_OPERATION_CODE_PRO04");
      attribute2dbField.put("notePRO02","PRO02_MANUFACTURE_PHASES.NOTE");
      attribute2dbField.put("description2","PRO04_ALIAS.DESCRIPTION");
      attribute2dbField.put("taskDescriptionSYS10","TASKS.DESCRIPTION");
      attribute2dbField.put("machineryDescriptionSYS10","PRO03.DESCRIPTION");
      attribute2dbField.put("operationCodePro04PRO02","PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(vo.getCompanyCodeSys01PRO01());
      values.add(vo.getManufactureCodePRO01());

      // read from PRO02 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ManufacturePhaseVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true,
          ApplicationConsts.ID_MANUFACTURE_PHASE // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching manufacture phases",ex);
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
