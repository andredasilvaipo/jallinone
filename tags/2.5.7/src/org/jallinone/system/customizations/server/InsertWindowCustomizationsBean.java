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
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert new customized fields in the specified window.</p>
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
public class InsertWindowCustomizationsBean  implements InsertWindowCustomizations {


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




  public InsertWindowCustomizationsBean() {
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
  public VOListResponse insertWindowCustomizations(ArrayList list,String t1,String t2,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      WindowCustomizationVO vo = null;
      String sql = null;
      stmt = conn.createStatement();
      boolean columnAlreadyExist = false;
      String attrName = null;
      HashSet attrNamesAlreadyUsed = new HashSet();
      BigDecimal progressiveSys10 = null;

      for(int j=0;j<list.size();j++) {
        vo = (WindowCustomizationVO)list.get(j);

        // find out if the field name already exixts...
        ResultSet rset = stmt.executeQuery(
          "select COLUMN_TYPE from SYS12_WINDOW_CUSTOMIZATIONS,SYS13_WINDOWS where "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13=SYS13_WINDOWS.PROGRESSIVE and "+
          "SYS13_WINDOWS.TABLE_NAME='"+vo.getTableNameSYS13()+"' and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
        );
        columnAlreadyExist = false;
        if(rset.next()) {
          columnAlreadyExist = true;
          String colType = rset.getString(1);
          rset.close();
          if (!colType.equals(vo.getColumnTypeSYS12())) {
            // column already exixts AND it's of a different type: insert not allowed!
            Logger.error(username,this.getClass().getName(),"executeCommand","The field name spacified '"+vo.getColumnNameSYS12()+"' already exists in '"+vo.getTableNameSYS13()+"'.",null);
            return new VOListResponse(t1);
          }
        }
        else
          rset.close();

        // define attributeName...
        attrName = null;
        rset = stmt.executeQuery(
          "select ATTRIBUTE_NAME from SYS12_WINDOW_CUSTOMIZATIONS where PROGRESSIVE_SYS13="+vo.getProgressiveSys13SYS12()+" and COLUMN_TYPE='"+vo.getColumnTypeSYS12()+"'"
        );
        attrNamesAlreadyUsed.clear();
        while(rset.next()) {
          attrNamesAlreadyUsed.add( rset.getString(1) );
        }
        rset.close();
        boolean attrFound = false;
        for(int i=0;i<10;i++)
          if (!attrNamesAlreadyUsed.contains("attributeName"+vo.getColumnTypeSYS12()+i)) {
            attrFound = true;
            vo.setAttributeNameSYS12("attributeName"+vo.getColumnTypeSYS12()+i);
            break;
          }
        if (!attrFound) {
          // there are not other available attributes...
          Logger.error(username,this.getClass().getName(),"executeCommand","Maximum number of customized columns reached.",null);
          return new VOListResponse(t2);
        }

        // insert record in SYS10...
        progressiveSys10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);

        // insert record in SYS12...
        stmt.execute(
            "insert into SYS12_WINDOW_CUSTOMIZATIONS(PROGRESSIVE_SYS13,COLUMN_NAME,COLUMN_TYPE,COLUMN_SIZE,COLUMN_DEC,PROGRESSIVE_SYS10,ATTRIBUTE_NAME) values("+
            vo.getProgressiveSys13SYS12()+",'"+vo.getColumnNameSYS12()+"','"+vo.getColumnTypeSYS12()+"',"+vo.getColumnSizeSYS12()+","+vo.getColumnDecSYS12()+","+progressiveSys10+",'"+vo.getAttributeNameSYS12()+"')"
        );

        if (!columnAlreadyExist) {
          // add the column to the table...
          sql = "alter table "+vo.getTableNameSYS13()+" add column "+vo.getColumnNameSYS12()+" ";
          if (vo.getColumnTypeSYS12().equals("S")) {
            if (conn.getMetaData().getDriverName().equals("oracle.jdbc.driver.OracleDriver"))
              sql += "varchar2";
            else
              sql += "varchar";
            sql += "("+vo.getColumnSizeSYS12()+")";
          }
          else if (vo.getColumnTypeSYS12().equals("D")) {
            sql += "date";
          }
          else if (vo.getColumnTypeSYS12().equals("N")) {
            sql += "decimal("+vo.getColumnSizeSYS12()+vo.getColumnDecSYS12()+","+vo.getColumnDecSYS12()+")";
          }
          stmt.execute(sql);
        }
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting new customized fields",ex);
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

