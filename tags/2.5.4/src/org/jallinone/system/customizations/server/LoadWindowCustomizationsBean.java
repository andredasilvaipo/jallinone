package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.*;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.message.send.java.*;
import java.math.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch grid data for the custumizations window.</p>
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
public class LoadWindowCustomizationsBean  implements LoadWindowCustomizations {


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
  public WindowCustomizationVO getWindowCustomization() {
	  throw new UnsupportedOperationException();
  }


  public LoadWindowCustomizationsBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOListResponse loadWindowCustomizations(GridParams pars,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      if (pars.getOtherGridParams().size()==0)
        return new VOListResponse(new ArrayList(),false,0);

      BigDecimal progressiveSYS13 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS13);
      String tableNameSYS13 = (String)pars.getOtherGridParams().get(ApplicationConsts.TABLE_NAME_SYS13);



      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_TYPE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_SIZE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_DEC,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10,SYS12_WINDOW_CUSTOMIZATIONS.ATTRIBUTE_NAME,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS12_WINDOW_CUSTOMIZATIONS,SYS10_TRANSLATIONS where "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13="+progressiveSYS13+" order by SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10"
      );
      WindowCustomizationVO vo = null;
      ArrayList list = new ArrayList();

      while(rset.next()) {
        vo = new WindowCustomizationVO();
        vo.setProgressiveSys13SYS12(rset.getBigDecimal(1));
        vo.setColumnNameSYS12(rset.getString(2));
        vo.setColumnTypeSYS12(rset.getString(3));
        vo.setColumnSizeSYS12(rset.getBigDecimal(4));
        vo.setColumnDecSYS12(rset.getBigDecimal(5));
        vo.setProgressiveSys10SYS12(rset.getBigDecimal(6));
        vo.setAttributeNameSYS12(rset.getString(7));
        vo.setDescriptionSYS10(rset.getString(8));
        vo.setTableNameSYS13(tableNameSYS13);
        list.add(vo);
      }
      rset.close();

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching window customizations",ex);
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

