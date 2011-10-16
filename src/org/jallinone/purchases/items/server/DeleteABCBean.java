package org.jallinone.purchases.items.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.VOResponse;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage ABC classification.</p>
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
public class DeleteABCBean implements DeleteABC {


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
	   * Business logic to execute.
	   */
	  public VOResponse deleteABC(BigDecimal reportId,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

	      // delete from TMP03...
	      String sql = "DELETE FROM TMP03_ABC WHERE REPORT_ID=?";
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setBigDecimal(1,reportId);
	      pstmt.execute();
	      return new VOResponse(Boolean.TRUE);
	    }
	    catch (Throwable ex) {
	    	try {
	    		if (this.conn==null && conn!=null)
	    			// rollback only local connection
	    			conn.rollback();
	    	}
	    	catch (Exception ex3) {
	    	}
	    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while removing ABC classification",ex);
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
