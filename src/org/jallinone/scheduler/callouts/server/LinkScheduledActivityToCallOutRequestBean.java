package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert a new scheduled activity and link it to the current call-out REQUEST.</p>
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
public class LinkScheduledActivityToCallOutRequestBean  implements LinkScheduledActivityToCallOutRequest {


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



  private ScheduledActivitiesBean insAct;

  public void setInsAct(ScheduledActivitiesBean insAct) {
    this.insAct = insAct;
  }

  private CallOutRequestsBean updReq;

  public void setUpdReq(CallOutRequestsBean updReq) {
    this.updReq = updReq;
  }



  public LinkScheduledActivityToCallOutRequestBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse linkScheduledActivityToCallOutRequest(
		  DetailCallOutRequestVO oldVO,
		  DetailCallOutRequestVO newVO,
		  ScheduledActivityVO actVO,
		  String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      insAct.setConn(conn); // use same transaction...
      updReq.setConn(conn); // use same transaction...

      Response res = insAct.insertActivity(actVO,serverLanguageId,username,new ArrayList());
      if (res.isError()) 
        throw new Exception(res.getErrorMessage());
      

      newVO.setProgressiveSch06SCH03(actVO.getProgressiveSCH06());
      res = updReq.updateCallOutRequest(oldVO,newVO,serverLanguageId,username,new ArrayList());
      if (res.isError()) 
          throw new Exception(res.getErrorMessage());

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting new scheduled activity and linking it to the specified call-out request",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw ex;
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
    	try {
    		insAct.setConn(null);
    		updReq.setConn(null);
    	} catch (Exception ex) {}
    }
  }



}

