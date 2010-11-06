package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.server.LoadUserParamAction;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.companies.server.LoadCompanyAction;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.openswing.swing.util.java.Consts;
import java.text.SimpleDateFormat;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to export (a closed) retail sale data to a text file and to call an external application to process it.</p>
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
public class ExportRetailSaleOnFileBean  {

  private LoadUserParamAction userParAction = new LoadUserParamAction();
  private LoadCompanyAction companyAction = new LoadCompanyAction();


  public ExportRetailSaleOnFileBean() {
  }


  /**
   * Export to a text file the retail selling.
   */
  public final Response exportToFile(SaleDocPK pk,DetailSaleDocVO docVO,ArrayList rows,UserSessionParameters userSessionPars,HttpServletRequest request,HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "ExportRetailSaleOnFileBean.exportToFile",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        docVO,
        null
      ));

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // retrieve receipt path user parameter...
      Map params = new HashMap();
      params.put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01DOC01());
      params.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.RECEIPT_PATH);
      Response userParRes = userParAction.executeCommand(params,userSessionPars,request,response,userSession,context);
      if (userParRes.isError())
        return userParRes;
      String programPath = (String)((VOResponse)userParRes).getVo();
      if (programPath==null) {
        return new ErrorResponse(resources.getResource("retail sale export non allowed: receipt path has not been defined as user parameter."));
      }

      // for demo only...
      if (programPath.equals("NOPRINT")) {
        return new VOResponse(Boolean.TRUE);
      }

      programPath = programPath.replace('\\','/');
      String path = programPath.substring(0,programPath.lastIndexOf("/"));

      // retrieve progressiveREG04 of current company...
      conn = ConnectionManager.getConnection(context);
      pstmt = conn.prepareStatement(
          "select PROGRESSIVE from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and SUBJECT_TYPE=?"
      );
      pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(2,ApplicationConsts.SUBJECT_MY_COMPANY);
      ResultSet rset = pstmt.executeQuery();
      BigDecimal progressiveREG04 = null;
      if(rset.next()) {
        progressiveREG04 = rset.getBigDecimal(1);
      }
      rset.close();
      if (progressiveREG04==null) {
        return new ErrorResponse(resources.getResource("company not found."));
      }

      // retrieve company data...
      //SubjectPK subjectPK = new SubjectPK(pk.getCompanyCodeSys01DOC01(),progressiveREG04);
      Response companyRes = companyAction.executeCommand(pk.getCompanyCodeSys01DOC01(),userSessionPars,request,response,userSession,context);
      if (companyRes.isError())
        return companyRes;
      OrganizationVO companyVO = (OrganizationVO)((VOResponse)companyRes).getVo();

      // prepare text file...
      GridSaleDocRowVO docRowVO = null;
      String tmpFile = path+"/receipt_"+docVO.getDocYearDOC01()+"_"+docVO.getDocSequenceDOC01()+".tmp";
      PrintWriter pw = new PrintWriter(new FileOutputStream(tmpFile));
      pw.println(companyVO.getName_1REG04());
      pw.println(companyVO.getAddressREG04()==null?"":companyVO.getAddressREG04());
      pw.println(companyVO.getCityREG04()==null?"":companyVO.getCityREG04());
      pw.println(companyVO.getProvinceREG04()==null?"":companyVO.getProvinceREG04());
      pw.println(companyVO.getTaxCodeREG04()==null?"":companyVO.getTaxCodeREG04());
      pw.println();

      for(int i=0;i<rows.size();i++) {
        docRowVO = (GridSaleDocRowVO)rows.get(i);
        pw.println(
            docRowVO.getItemCodeItm01DOC02()+"\t"+
            docRowVO.getDescriptionSYS10()+"\t"+
            docVO.getCurrencyCodeReg03DOC01()+"\t"+
            docRowVO.getValueDOC02()
        );
      }
      pw.println();

      if (docVO.getDiscountPercDOC01()!=null)
        pw.println(
            "%"+"\t"+
            docVO.getDiscountPercDOC01()
        );
      if (docVO.getDiscountValueDOC01()!=null)
        pw.println(
            docVO.getCurrencyCodeReg03DOC01()+"\t"+
            docVO.getDiscountValueDOC01()
        );
      pw.println();

      pw.println(
          docVO.getCurrencyCodeReg03DOC01()+"\t"+
          docVO.getTotalDOC01()
      );
      pw.println();

      SimpleDateFormat sdf = new SimpleDateFormat(resources.getDateMask(Consts.TYPE_DATE_TIME));
      pw.println(sdf.format(new java.util.Date()));
      pw.println();

      pw.println(docVO.getDocSequenceDOC01());

      pw.close();

      // "commit" file...
      String file = path+"/receipt_"+docVO.getDocYearDOC01()+"_"+docVO.getDocSequenceDOC01()+".txt";
      new File(tmpFile).renameTo(new File(file));

      // call external application that manages this text file...
      Process p = Runtime.getRuntime().exec(programPath+" "+file);
      LogProcessMessage log = new LogProcessMessage(userSessionPars.getUsername(),p.getInputStream());
      LogErrorProcessMessage errorLog = new LogErrorProcessMessage(userSessionPars.getUsername(),p.getErrorStream());
      int returnCode = p.waitFor();
      if (returnCode!=0) {
        return new ErrorResponse(resources.getResource("error while executing external application; return code:")+" "+returnCode);
      }

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while exporting a retail sale to file",ex);
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


  /**
   * <p>Description: Inner class used to fetch external application log messages.</p>
   */
  class LogProcessMessage extends Thread {

    private BufferedReader br = null;
    private String username = null;

    public LogProcessMessage(String username,InputStream in) {
      this.username = username;
      br = new BufferedReader(new InputStreamReader(in));
      start();
    }

    public void run() {
      String line = null;
      try {
        while((line=br.readLine())!=null) {
          Logger.debug(
            username,
            "org.jallinone.sales.documents.server.LogProcessMessage",
            "run",
            line
          );
        }
      }
      catch (IOException ex) {
      }
    }

  }


  /**
   * <p>Description: Inner class used to fetch external application error log messages.</p>
   */
  class LogErrorProcessMessage extends Thread {

    private BufferedReader brError = null;
    private String username = null;

    public LogErrorProcessMessage(String username,InputStream in) {
      this.username = username;
      brError = new BufferedReader(new InputStreamReader(in));
      start();
    }

    public void run() {
      String line = null;
      try {
        while((line=brError.readLine())!=null) {
          Logger.error(
            username,
            "org.jallinone.sales.documents.server.LogErrorProcessMessage",
            "run",
            line,
            null
          );
        }
      }
      catch (IOException ex) {
      }
    }

  }



}

