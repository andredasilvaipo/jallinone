package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.customizations.java.ReportVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve customized reports from SYS15 table.</p>
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
public class LoadCustomizedReportsAction implements Action {


  public LoadCustomizedReportsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCustomizedReports";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
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

      String sql =
          "select SYS15_REPORT_CUSTOMIZATIONS.COMPANY_CODE_SYS01,SYS10_TRANSLATIONS.DESCRIPTION,SYS15_REPORT_CUSTOMIZATIONS.REPORT_NAME,FUNCTION_CODE_SYS06 "+
          "from SYS15_REPORT_CUSTOMIZATIONS,SYS06_FUNCTIONS,SYS10_TRANSLATIONS where "+
          "SYS06_FUNCTIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS15_REPORT_CUSTOMIZATIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS15_REPORT_CUSTOMIZATIONS.COMPANY_CODE_SYS01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SYS15","SYS15_REPORT_CUSTOMIZATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("reportNameSYS15","SYS15_REPORT_CUSTOMIZATIONS.REPORT_NAME");
      attribute2dbField.put("functionCodeSys06SYS15","FUNCTION_CODE_SYS06");

      GridParams gridParams = (GridParams)inputPar;

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));

      // read from SYS15 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ReportVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );
      if (res.isError())
        return res;


      java.util.List rows = ((VOListResponse)res).getRows();
      ReportVO vo = null;
      pstmt = conn.prepareStatement("select * from SYS16_CUSTOM_FUNCTIONS where FUNCTION_CODE_SYS06=?");
      ResultSet rset = null;
      for(int i=0;i<rows.size();i++) {
        vo = (ReportVO)rows.get(i);
        pstmt.setString(1,vo.getFunctionCodeSys06SYS15());
        rset = pstmt.executeQuery();
        if (rset.next())
          vo.setCustomFunction(new Boolean(true));
        else
          vo.setCustomFunction(new Boolean(false));
        rset.close();
      }

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching customized reports list",ex);
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
