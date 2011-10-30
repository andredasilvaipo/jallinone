package org.jallinone.system.importdata.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.importdata.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to read the content of a file system folder.</p>
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
public class GetFolderContentBean  implements GetFolderContent {


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




  public GetFolderContentBean() {}




  /**
   * Business logic to execute.
   */
  public VOListResponse getFolderContent(final String fileFormat,File file,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      if (file==null) {
        List vos = Arrays.asList(File.listRoots());
        return new VOListResponse(vos,false,vos.size());
      }

      List vos = Arrays.asList(file.listFiles(new FileFilter() {

    	  public boolean accept(File f) {
    		  return
    		  f.isDirectory() ||
    		  f.getName().toLowerCase().endsWith(".xls") && fileFormat.equals("XLS") ||
    		  f.getName().toLowerCase().endsWith(".csv") && fileFormat.equals("CSV1") ||
    		  f.getName().toLowerCase().endsWith(".csv") && fileFormat.equals("CSV2") ||
    		  f.getName().toLowerCase().endsWith(".txt") && fileFormat.equals("TXT");
    	  }

      }));
      vos = new ArrayList(vos);
      if (file.getParentFile()!=null) {
    	  vos.add(0,file.getParentFile());
      }
      else {
    	  vos.addAll(0,Arrays.asList(File.listRoots()));
      }
      return new VOListResponse(vos,false,vos.size());


    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(), "executeCommand", "Error while reading a folder", ex);
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



}


