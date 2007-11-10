package org.jallinone.reports.server;

import org.openswing.swing.logger.server.*;
import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.internationalization.java.Language;
import org.jallinone.commons.java.ApplicationConsts;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;
import java.util.*;
import java.io.*;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.util.java.Consts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to execute a .jasper file with Jasper Report and
 * export it with the specified document format.</p>
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
public class JasperReportAction implements Action {

  public JasperReportAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "getJasperReport";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;
    Connection conn = null;
    String functionCode = null;
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

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources res = factory.getResources(userSessionPars.getLanguageId());

      Map params = (Map)inputPar;
      String companyCode = (String)params.get(ApplicationConsts.COMPANY_CODE_SYS01);
      functionCode = (String)params.get(ApplicationConsts.FUNCTION_CODE_SYS06);
      String exportFormat = (String)params.get(ApplicationConsts.EXPORT_FORMAT);
      Map exportParams = (Map)params.get(ApplicationConsts.EXPORT_PARAMS);

      pstmt = conn.prepareStatement("select REPORT_NAME from SYS15_REPORT_CUSTOMIZATIONS where COMPANY_CODE_SYS01=? and FUNCTION_CODE_SYS06=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,functionCode);
      ResultSet rset = pstmt.executeQuery();
      String baseName = null;
      while(rset.next()) {
        baseName = rset.getString(1);
      }
      if (baseName==null) {
        Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"'",null);
        new ErrorResponse(res.getResource("jasper file not found: report generation is not possible."));
      }

      String path = this.getClass().getResource("/").getPath();
      String reportName = path+"reports/"+baseName.substring(0,baseName.lastIndexOf("."));

      File file = new File(reportName+".jasper");

      try {
        System.setProperty(
            "jasper.reports.compile.class.path",
            context.getRealPath("WEB-INF/lib/jasperreports-1.2.7.jar") +
            System.getProperty("path.separator") +
            context.getRealPath("/WEB-INF/classes/")
            );
      }
      catch (Throwable ex3) {
      }

      if (!file.exists()) {
        File jrxml = new File(reportName+".jrxml");
        if (!jrxml.exists()) {
//          String jasperjarfiledirectory = path+"../WEB-INF/lib";
//          System.setProperty("jasper.reports.compile.class.path",jasperjarfiledirectory);

          Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"' and jasper file '"+(reportName==null?"":reportName)+"'",null);
          new ErrorResponse(res.getResource("jasper file not found: report generation is not possible."));
        }
        JasperCompileManager.compileReport(new FileInputStream(jrxml));
      }

      ResourceBundle resourceBundle = null;
      try {
        resourceBundle = new PropertyResourceBundle(
            new FileInputStream(
              reportName+"_" + res.getLanguageId() + ".properties"
            )
        );
      }
      catch (IOException ex1) {
        resourceBundle = new PropertyResourceBundle(
            new FileInputStream(
              reportName+".properties"
            )
        );
      }

      exportParams.put(JRParameter.REPORT_RESOURCE_BUNDLE,resourceBundle);
      exportParams.put(
        "SUBREPORT_DIR",
        context.getRealPath("WEB-INF/classes/reports/") +"/"

      );
      exportParams.put("LANGUAGE_CODE",serverLanguageId);
      exportParams.put("DATE_FORMAT",res.getDateMask(Consts.TYPE_DATE));

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
        null
      ));

      JasperPrint print = JasperFillManager.fillReport(new FileInputStream(file),exportParams,conn);

      conn.rollback();

      return new VOResponse(print);

    } catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"'",ex);
      return new ErrorResponse(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }

  }


}
