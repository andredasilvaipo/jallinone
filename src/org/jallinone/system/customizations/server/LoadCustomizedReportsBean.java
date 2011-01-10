package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve customized reports from SYS15 table.</p>
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
public class LoadCustomizedReportsBean  implements LoadCustomizedReports {


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


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ReportVO getReport() {
	  throw new UnsupportedOperationException();
  }
  


  public LoadCustomizedReportsBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadCustomizedReports(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


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


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));

      // read from SYS15 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ReportVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (res.isError())
        throw new Exception(res.getErrorMessage());


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
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching customized reports list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



}

