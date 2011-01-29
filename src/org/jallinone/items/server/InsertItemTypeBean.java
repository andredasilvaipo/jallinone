package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.items.java.*;
import java.math.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert a new items in ITM02 table.</p>
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
public class InsertItemTypeBean  implements InsertItemType {


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




  public InsertItemTypeBean() {
  }


  /**
   * Business logic to execute.
   */
  public VOResponse insertItemType(ItemTypeVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setEnabledITM02("Y");

      String companyCode = companiesList.get(0).toString();
      if (vo.getCompanyCodeSys01ITM02()==null)
        vo.setCompanyCodeSys01ITM02(companyCode);

      // generate PROGRESSIVE_HIE02 value...
      stmt = conn.createStatement();
      BigDecimal progressiveHIE02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01ITM02(),"HIE02_HIERARCHIES","PROGRESSIVE",conn);
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ITM02(),conn);
      stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) VALUES("+progressiveHIE02+",'"+vo.getCompanyCodeSys01ITM02()+"','Y')");
      stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
      stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
      vo.setProgressiveHie02ITM02(progressiveHIE02);
      vo.setProgressiveHie01HIE02(progressiveHIE01);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM02","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveHie02ITM02","PROGRESSIVE_HIE02");
      attribute2dbField.put("enabledITM02","ENABLED");

      // insert into ITM02...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "ITM02_ITEM_TYPES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (!res.isError()) {
        stmt.execute("INSERT INTO SYS13_WINDOWS(TABLE_NAME,PROGRESSIVE,FUNCTION_CODE_SYS06) VALUES('ITM01_ITEMS',"+progressiveHIE01+",'ITM01')");
      }

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting a new items", ex);
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


