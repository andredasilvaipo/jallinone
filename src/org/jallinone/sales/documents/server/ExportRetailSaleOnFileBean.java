package org.jallinone.sales.documents.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.system.companies.server.CompaniesBean;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.server.ParamsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;


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
public class ExportRetailSaleOnFileBean implements ExportRetailSaleOnFile {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }



  private ParamsBean userParAction;

  public void setUserParAction(ParamsBean userParAction) {
    this.userParAction = userParAction;
  }

  private CompaniesBean companyAction;

  public void setCompanyAction(CompaniesBean companyAction) {
    this.companyAction = companyAction;
  }



  public ExportRetailSaleOnFileBean() {
  }


  /**
   * Export to a text file the retail selling.
   */
  public VOResponse exportToFile(String t1,String t2,String t3,String t4,SaleDocPK pk,DetailSaleDocVO docVO,ArrayList rows,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      userParAction.setConn(conn); // use same transaction...
      companyAction.setConn(conn); // use same transaction...


      // retrieve receipt path user parameter...
      HashMap params = new HashMap();
      params.put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01DOC01());
      params.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.RECEIPT_PATH);
      Response userParRes = userParAction.loadUserParam(params,serverLanguageId,username);
      if (userParRes.isError()){
    	  VOResponse res = new VOResponse();
    	  res.setErrorMessage(userParRes.getErrorMessage());
    	  return res;
      }
      String programPath = (String)((VOResponse)userParRes).getVo();
      if (programPath==null) {
        throw new Exception(t1);
      }

      // for demo only...
      if (programPath.equals("NOPRINT")) {
        return new VOResponse(Boolean.TRUE);
      }

      programPath = programPath.replace('\\','/');
      String path = programPath.substring(0,programPath.lastIndexOf("/"));

      // retrieve progressiveREG04 of current company...
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
        throw new Exception(t2);
      }

      // retrieve company data...
      //SubjectPK subjectPK = new SubjectPK(pk.getCompanyCodeSys01DOC01(),progressiveREG04);
      Response companyRes = companyAction.loadCompany(pk.getCompanyCodeSys01DOC01(),serverLanguageId,username);
      if (companyRes.isError())
        throw new Exception(companyRes.getErrorMessage());
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

      SimpleDateFormat sdf = new SimpleDateFormat(t3);
      pw.println(sdf.format(new java.util.Date()));
      pw.println();

      pw.println(docVO.getDocSequenceDOC01());

      pw.close();

      // "commit" file...
      String file = path+"/receipt_"+docVO.getDocYearDOC01()+"_"+docVO.getDocSequenceDOC01()+".txt";
      new File(tmpFile).renameTo(new File(file));

      // call external application that manages this text file...
      Process p = Runtime.getRuntime().exec(programPath+" "+file);
      LogProcessMessage log = new LogProcessMessage(username,p.getInputStream());
      LogErrorProcessMessage errorLog = new LogErrorProcessMessage(username,p.getErrorStream());
      int returnCode = p.waitFor();
      if (returnCode!=0) {
        throw new Exception(t4+" "+returnCode);
      }

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while exporting a retail sale to file",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
    	  if (this.conn==null && conn!=null) {
    		  // close only local connection
    		  conn.commit();
    		  conn.close();
    	  }

      }
      catch (Exception exx) {}      
      try {
          userParAction.setConn(null);
          companyAction.setConn(null);
      } catch (Exception ex) {}
    }

  }


  /**
   * <p>Description: Inner class used to fetch external application log messages.</p>
   */
  class LogProcessMessage extends Thread {

  private BufferedReader br;

  public void setBr(BufferedReader br) {
    this.br = br;
  }

  private String username;

  public void setUsername(String username) {
    this.username = username;
  }


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

  private BufferedReader brError;

  public void setBrError(BufferedReader brError) {
    this.brError = brError;
  }

  private String username;

  public void setUsername(String username) {
    this.username = username;
  }


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
      try {
    	  brError.close();
		} catch (Exception e) {
	  }
    }

  }



}


