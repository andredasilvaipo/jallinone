package org.jallinone.subjects.server;

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
import org.jallinone.subjects.java.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.openswing.swing.internationalization.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to insert/update/delete organizations in/from REG04 table.</p>
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
public class CheckSubjectExistBean implements CheckSubjectExist {

	

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
	   * Check if there exist a person with the same first nane + last name
	   */
	  public final void checkPeopleExist(PeopleVO vo,String t1,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    ResultSet rset = null;
	    
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;


//	      // retrieve internationalization settings (Resources object)...
//	      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
//	      

	      String sql = "select NAME_1,NAME_2 from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and UPPER(NAME_1)=? and UPPER(NAME_2)=? and ENABLED='Y' ";
	      if (vo.getProgressiveREG04()!=null)
	        sql += " and not PROGRESSIVE="+vo.getProgressiveREG04();

	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
	      pstmt.setString(2,vo.getName_1REG04()==null?null:vo.getName_1REG04().toUpperCase());
	      pstmt.setString(3,vo.getName_2REG04()==null?null:vo.getName_2REG04().toUpperCase());
	      rset = pstmt.executeQuery();
	      if (rset.next())
	        throw new Exception(t1);
	    }
	    finally {
	      try {
	        rset.close();
	      }
	      catch (Exception ex) {
	      }
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
	    }

	  }


	  

	  /**
	   * Check if there exist an organization with the same corporate name
	   */
	  public void checkOrganizationExist(OrganizationVO vo,String t1,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    ResultSet rset = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;


//	      // retrieve internationalization settings (Resources object)...
//	      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
//	      

	      String sql = "select NAME_1 from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and UPPER(NAME_1)=? and ENABLED='Y' ";
	      if (vo.getProgressiveREG04()!=null)
	        sql += " and not PROGRESSIVE="+vo.getProgressiveREG04();

	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
	      pstmt.setString(2,vo.getName_1REG04()==null?null:vo.getName_1REG04().toUpperCase());
	      rset = pstmt.executeQuery();
	      if (rset.next())
	        throw new Exception(t1);
	    }
	    finally {
	      try {
	        rset.close();
	      }
	      catch (Exception ex) {
	      }
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
	    }

	  }


}
