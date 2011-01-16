package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to delete the selected customized columns of the specified window.</p>
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
public class DeleteWindowCustomizationsBean  implements DeleteWindowCustomizations {


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




  public DeleteWindowCustomizationsBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public WindowCustomizationVO getWindowCustomization() {
	  throw new UnsupportedOperationException();	  
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse deleteWindowCustomizations(ArrayList rows,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();
      WindowCustomizationVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (WindowCustomizationVO)rows.get(i);
        // delete from SYS12...
        stmt.execute(
            "delete from SYS12_WINDOW_CUSTOMIZATIONS where "+
            "PROGRESSIVE_SYS13="+vo.getProgressiveSys13SYS12()+" and "+
            "COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
        );

        // delete from SYS10...
        TranslationUtils.deleteTranslations(
            vo.getProgressiveSys10SYS12(),
            conn
        );
      }

      // maybe phisically delete the field from the table...
      ResultSet rset = stmt.executeQuery(
        "select COLUMN_NAME from SYS12_WINDOW_CUSTOMIZATIONS,SYS13_WINDOWS where "+
        "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13=SYS13_WINDOWS.PROGRESSIVE and "+
        "SYS13_WINDOWS.TABLE_NAME='"+vo.getTableNameSYS13()+"' and "+
        "SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
      );
      boolean columnAlreadyExist = false;
      if(rset.next())
        columnAlreadyExist = true;
      rset.close();
      if (!columnAlreadyExist)
        stmt.execute("alter table "+vo.getTableNameSYS13()+" drop column "+vo.getColumnNameSYS12());

      return new VOListResponse(rows,false,rows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting customized columns",ex);
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

