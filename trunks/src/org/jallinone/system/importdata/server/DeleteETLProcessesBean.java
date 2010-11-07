package org.jallinone.system.importdata.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.importdata.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to (phisically) delete existing ETL processes from SYS23/SYS24 tables.</p>
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
public class DeleteETLProcessesBean implements DeleteETLProcesses {


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




  public DeleteETLProcessesBean() {}

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ETLProcessVO getETLProcess() {
	  throw new UnsupportedOperationException();	  
  }
  

  /**
   * Business logic to execute.
   */
  public VOResponse deleteETLProcesses(ArrayList vos,String serverLanguageId,String username)  throws Throwable{
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      stmt = conn.createStatement();
      ETLProcessVO vo = null;
      for(int i=0;i<vos.size();i++) {
        vo = (ETLProcessVO)vos.get(i);

        // phisically delete records from SYS24...
        stmt.execute(
          "delete from SYS24_ETL_PROCESS_FIELDS WHERE PROGRESSIVE_SYS23="+vo.getProgressiveSYS23()
        );

        // phisically delete the record from SYS23...
        stmt.execute(
          "delete from SYS23_ETL_PROCESSES WHERE PROGRESSIVE="+vo.getProgressiveSYS23()
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"deleteETLProcesses","Error while deleting existing ETL processes",ex);
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
            stmt.close();
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

