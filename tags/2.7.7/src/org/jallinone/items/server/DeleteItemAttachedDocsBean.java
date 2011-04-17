package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.items.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to (phisically) delete existing item attached documents.</p>
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
public class DeleteItemAttachedDocsBean  implements DeleteItemAttachedDocs {


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




  public DeleteItemAttachedDocsBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse deleteItemAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      pstmt = conn.prepareStatement(
        "delete from ITM05_ITEM_ATTACHED_DOCS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and PROGRESSIVE_DOC14=? and PROGRESSIVE_HIE01=?"
      );


      ItemAttachedDocVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in ITM05...
        vo = (ItemAttachedDocVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01ITM05());
        pstmt.setString(2,vo.getItemCodeItm01ITM05());
        pstmt.setBigDecimal(3,vo.getProgressiveDoc14ITM05());
        pstmt.setBigDecimal(4,vo.getProgressiveHie01ITM05());
        pstmt.execute();
      }

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing item attached documents",ex);
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

