package org.jallinone.system.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.openswing.swing.internationalization.java.Language;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.VOListResponse;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve languages defined for the application.</p>
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
public class LoadLanguagesBean  implements LoadLanguages {


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


  public LoadLanguagesBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public Language getLanguage() {
	  throw new UnsupportedOperationException();	  
  }
  


  /**
   * Business logic to execute.
   */
  public VOListResponse getLanguages() throws Throwable {
    PreparedStatement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.prepareStatement("select CLIENT_LANGUAGE_CODE,DESCRIPTION from SYS09_LANGUAGES where ENABLED='Y'");
      ResultSet rset = stmt.executeQuery();
      ArrayList list = new ArrayList();
      Language lang = null;
      while(rset.next()) {
        lang = new Language(rset.getString(1),rset.getString(2));
        list.add(lang);
      }
      
      return new VOListResponse(list,false,list.size());
    } catch (Exception ex) {
      Logger.error("NONAME",this.getClass().getName(),"executeCommand","Error while loading languages",ex);
      throw new Exception(ex.getMessage());
    } finally {
      try {
        stmt.close();
      }
      catch (Exception ex) {
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

