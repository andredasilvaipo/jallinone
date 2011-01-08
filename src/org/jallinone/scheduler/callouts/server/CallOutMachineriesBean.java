package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage call-out machineries.</p>
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
public class CallOutMachineriesBean  implements CallOutMachineries {


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




  public CallOutMachineriesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public CallOutMachineryVO getCallOutMachinery(CallOutPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadCallOutMachineries(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03,SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01,SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SCH13_CALL_OUT_MACHINERIES,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
          "SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=? and "+
          "SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH13","SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH13","SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("machineryCodePro03SCH13","SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03");

      CallOutPK pk = (CallOutPK)gridParams.getOtherGridParams().get(ApplicationConsts.CALL_OUT_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());


      // read from SCH13 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CallOutMachineryVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching call-out machineries list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
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




  /**
   * Business logic to execute.
   */
  public VOListResponse insertCallOutMachineries(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CallOutMachineryVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("callOutCodeSch10SCH13","CALL_OUT_CODE_SCH10");
      attribute2dbField.put("companyCodeSys01SCH13","COMPANY_CODE_SYS01");
      attribute2dbField.put("machineryCodePro03SCH13","MACHINERY_CODE_PRO03");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (CallOutMachineryVO)list.get(i);

        // insert into SCH13...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH13_CALL_OUT_MACHINERIES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
    		  "executeCommand", "Error while inserting new call-out machineries", ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }

      throw new Exception(ex.getMessage());
    }
    finally {
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



  /**
   * Business logic to execute.
   */
  public VOResponse deleteCallOutMachineries(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "delete from SCH13_CALL_OUT_MACHINERIES where COMPANY_CODE_SYS01=? and CALL_OUT_CODE_SCH10=? and MACHINERY_CODE_PRO03=?"
      );

      CallOutMachineryVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH13...
        vo = (CallOutMachineryVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH13());
        pstmt.setString(2,vo.getCallOutCodeSch10SCH13());
        pstmt.setString(3,vo.getMachineryCodePro03SCH13());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing call-out machineries",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

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

