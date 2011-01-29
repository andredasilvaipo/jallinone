package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.*;
import org.jallinone.items.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch attached documents to an item from ITM05 table.</p>
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
public class LoadItemAttachedDocsBean  implements LoadItemAttachedDocs {


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




  public LoadItemAttachedDocsBean() {
  }


  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ItemAttachedDocVO getItemAttachedDoc(ItemPK pk) {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadItemAttachedDocs(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01,ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14,"+
          "ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01,DOC14_DOCUMENTS.DESCRIPTION,ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01, "+
          "HIE01_LEVELS.PROGRESSIVE_HIE02 "+
          "from ITM05_ITEM_ATTACHED_DOCS,DOC14_DOCUMENTS,HIE01_LEVELS where "+
          "ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 and "+
          "ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14=DOC14_DOCUMENTS.PROGRESSIVE and "+
          "HIE01_LEVELS.PROGRESSIVE=ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01 and "+
          "ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01=? and "+
          "ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM05","ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
      attribute2dbField.put("itemCodeItm01ITM05","ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveDoc14ITM05","ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveHie01ITM05","ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");

      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01ITM01());
      values.add(pk.getItemCodeITM01());

      // read from ITM05 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ItemAttachedDocVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item attached documents list",ex);
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

