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
 * * <p>Description: Bean used to manage call-out items.</p>
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
public class CallOutItemsBean  implements CallOutItems {


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


  public CallOutItemsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public CallOutItemVO getCallOutItem(CallOutPK pk) {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadCallOutItems(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01,SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01,"+
          "SCH14_CALL_OUT_ITEMS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
          "from SCH14_CALL_OUT_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01=? and "+
          "SCH14_CALL_OUT_ITEMS.CALL_OUT_CODE_SCH10=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH14","SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH14","SCH14_CALL_OUT_ITEMS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("itemCodeItm01SCH14","SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      CallOutPK pk = (CallOutPK)gridParams.getOtherGridParams().get(ApplicationConsts.CALL_OUT_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());


      // read from SCH14 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CallOutItemVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching call-out items list",ex);
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
  public VOListResponse insertCallOutItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CallOutItemVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("callOutCodeSch10SCH14","CALL_OUT_CODE_SCH10");
      attribute2dbField.put("companyCodeSys01SCH14","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01SCH14","ITEM_CODE_ITM01");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (CallOutItemVO)list.get(i);

        // insert into SCH14...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH14_CALL_OUT_ITEMS",
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
                   "executeCommand", "Error while inserting new call-out items", ex);
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
  public VOResponse deleteCallOutItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      pstmt = conn.prepareStatement(
        "delete from SCH14_CALL_OUT_ITEMS where COMPANY_CODE_SYS01=? and CALL_OUT_CODE_SCH10=? and ITEM_CODE_ITM01=?"
      );

      CallOutItemVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH14...
        vo = (CallOutItemVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH14());
        pstmt.setString(2,vo.getCallOutCodeSch10SCH14());
        pstmt.setString(3,vo.getItemCodeItm01SCH14());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing call-out items",ex);
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

