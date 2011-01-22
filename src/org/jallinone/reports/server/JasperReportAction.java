package org.jallinone.reports.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.server.Controller;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.util.java.Consts;
import org.jallinone.variants.java.VariantDescriptionsVO;

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


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
		  HashMap params = (HashMap)inputPar;


		  // retrieve internationalization settings (Resources object)...
		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources res = factory.getResources(userSessionPars.getLanguageId());
		  String t1 = res.getResource("jasper file not found: report generation is not possible.");
		  String langId = res.getLanguageId();
		  String reportDir = context.getRealPath("WEB-INF/classes/reports/") +"/";
		  String dateformat = res.getDateMask(Consts.TYPE_DATE);

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

		  Response answer = new VOResponse(getJasperReport(params,t1,langId,reportDir,dateformat,(JAIOUserSessionParameters)userSessionPars,userSessionPars.getUsername()));

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }




  /**
   * Business logic to execute.
   */
  public JasperPrint getJasperReport(HashMap params,String t1,String langId,String reportDir,String dateFormat,JAIOUserSessionParameters userSessionPars,String username) throws Throwable {
    PreparedStatement pstmt = null;
    String functionCode = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);

			String serverLanguageId = userSessionPars.getServerLanguageId();
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
        Logger.error(username,this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"'",null);
        new ErrorResponse(t1);
      }

      String path = this.getClass().getResource("/").getPath().replaceAll("%20"," ");
      String reportName = path+"reports/"+baseName.substring(0,baseName.lastIndexOf("."));

      File file = new File(reportName+".jasper");



      if (!file.exists()) {
        File jrxml = new File(reportName+".jrxml");
        if (!jrxml.exists()) {
//          String jasperjarfiledirectory = path+"../WEB-INF/lib";
//          System.setProperty("jasper.reports.compile.class.path",jasperjarfiledirectory);

          Logger.error(username,this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"' and jasper file '"+(reportName==null?"":reportName)+"'",null);
          new ErrorResponse(t1);
        }
        JasperCompileManager.compileReport(new FileInputStream(jrxml));
      }

      ResourceBundle resourceBundle = null;
      try {
        resourceBundle = new PropertyResourceBundle(
            new FileInputStream(
              reportName+"_" + langId + ".properties"
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
        reportDir

      );
      exportParams.put("LANGUAGE_CODE",serverLanguageId);
      exportParams.put("DATE_FORMAT",dateFormat);

			if (companyCode!=null) {
				VariantDescriptionsVO vo = (VariantDescriptionsVO)userSessionPars.getVariantDescriptionsVO().get(companyCode);
				exportParams.put("VAR1_DESCRS", vo.getVariant1Descriptions());
				exportParams.put("VAR2_DESCRS", vo.getVariant2Descriptions());
				exportParams.put("VAR3_DESCRS", vo.getVariant3Descriptions());
				exportParams.put("VAR4_DESCRS", vo.getVariant4Descriptions());
				exportParams.put("VAR5_DESCRS", vo.getVariant5Descriptions());
			}



      return JasperFillManager.fillReport(new FileInputStream(file),exportParams,conn);

    } catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while generating a jasper report for report '"+(functionCode==null?"":functionCode)+"'",ex);
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
    	  ConnectionManager.releaseConnection(conn, null);
      } catch (Exception e) {
      }
    }

  }



}

