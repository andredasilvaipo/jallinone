package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage attached documents.</p>
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
public class SchedulerAttachedDocsBean  implements SchedulerAttachedDocs {


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
  public ActAttachedDocVO getActAttachedDoc(ScheduledActivityPK pk) {
	  throw new UnsupportedOperationException();
  }


  public SchedulerAttachedDocsBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse insertAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ActAttachedDocVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH08","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSch06SCH08","PROGRESSIVE_SCH06");
      attribute2dbField.put("progressiveDoc14SCH08","PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveHie01SCH08","PROGRESSIVE_HIE01");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (ActAttachedDocVO)list.get(i);

        // insert into SCH08...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH08_ACT_ATTACHED_DOCS",
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
                   "executeCommand", "Error while inserting new attached documents to the scheduled activity", ex);
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
  public VOListResponse loadAttachedDocs(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH08_ACT_ATTACHED_DOCS.COMPANY_CODE_SYS01,SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_DOC14,"+
          "SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_SCH06,DOC14_DOCUMENTS.DESCRIPTION,SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_HIE01, "+
          "HIE01_LEVELS.PROGRESSIVE_HIE02 "+
          "from SCH08_ACT_ATTACHED_DOCS,DOC14_DOCUMENTS,HIE01_LEVELS where "+
          "SCH08_ACT_ATTACHED_DOCS.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 and "+
          "SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_DOC14=DOC14_DOCUMENTS.PROGRESSIVE and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_HIE01 and "+
          "SCH08_ACT_ATTACHED_DOCS.COMPANY_CODE_SYS01=? and "+
          "SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH08","SCH08_ACT_ATTACHED_DOCS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
      attribute2dbField.put("progressiveSch06SCH08","SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_SCH06");
      attribute2dbField.put("progressiveDoc14SCH08","SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveHie01SCH08","SCH08_ACT_ATTACHED_DOCS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");

      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());

      // read from SCH08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ActAttachedDocVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching attached documents list",ex);
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
  public VOResponse deleteAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "delete from SCH08_ACT_ATTACHED_DOCS where COMPANY_CODE_SYS01=? and PROGRESSIVE_SCH06=? and PROGRESSIVE_DOC14=? and PROGRESSIVE_HIE01=?"
      );

      ActAttachedDocVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH08...
        vo = (ActAttachedDocVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH08());
        pstmt.setBigDecimal(2,vo.getProgressiveSch06SCH08());
        pstmt.setBigDecimal(3,vo.getProgressiveDoc14SCH08());
        pstmt.setBigDecimal(4,vo.getProgressiveHie01SCH08());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing attached documents",ex);
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

