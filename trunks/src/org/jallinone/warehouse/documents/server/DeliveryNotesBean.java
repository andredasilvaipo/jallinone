package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage a delivery note.</p>
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
public class DeliveryNotesBean  implements DeliveryNotes {


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



  private InDeliveryNotesBean inQtyBean;

  public void setInQtyBean(InDeliveryNotesBean inQtyBean) {
    this.inQtyBean = inQtyBean;
  }

  private OutDeliveryNotesBean outQtyBean;

  public void setOutQtyBean(OutDeliveryNotesBean outQtyBean) {
    this.outQtyBean = outQtyBean;
  }



  public DeliveryNotesBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse closeDeliveryNote(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		DetailDeliveryNoteVO vo, String t1, String t2, String t3, String t4,
		String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      inQtyBean.setConn(conn); // use same transaction...
      outQtyBean.setConn(conn); // use same transaction...

      // generate progressive for doc. sequence...
      pstmt = conn.prepareStatement(
        "select max(DOC_SEQUENCE) from DOC08_DELIVERY_NOTES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC08());
      pstmt.setString(2,vo.getDocTypeDOC08());
      pstmt.setBigDecimal(3,vo.getDocYearDOC08());
      ResultSet rset = pstmt.executeQuery();
      int docSequenceDOC08 = 1;
      if (rset.next())
        docSequenceDOC08 = rset.getInt(1)+1;
      rset.close();


      // update delivery note state and note...
      pstmt = conn.prepareStatement("update DOC08_DELIVERY_NOTES set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and DOC_STATE=?");
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setInt(2,docSequenceDOC08);
      pstmt.setString(3,vo.getCompanyCodeSys01DOC08());
      pstmt.setString(4,vo.getDocTypeDOC08());
      pstmt.setBigDecimal(5,vo.getDocYearDOC08());
      pstmt.setBigDecimal(6,vo.getDocNumberDOC08());
      pstmt.setString(7,ApplicationConsts.HEADER_BLOCKED);
      if (pstmt.executeUpdate()==1) {
        // call in/out quantities updating routine...
        Response res = null;
        DeliveryNotePK pk = new DeliveryNotePK(
            vo.getCompanyCodeSys01DOC08(),
            vo.getDocTypeDOC08(),
            vo.getDocYearDOC08(),
            vo.getDocNumberDOC08()
        );
        if (vo.getDocTypeDOC08().equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE))
          res = inQtyBean.updateInQuantities(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pk,t3,t4,serverLanguageId,username);
        else if (vo.getDocTypeDOC08().equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
          res = outQtyBean.updateOutQtysSaleDoc(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pk,t2,t4,serverLanguageId,username);

        if (res.isError()) {
          Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing a delivery note:\n"+res.getErrorMessage(),null);
          if (res.isError())
        	  throw new Exception(res.getErrorMessage());
        }
        Response answer = new VOResponse(new BigDecimal(docSequenceDOC08));
        return (VOResponse)answer;
      }
      else {
        // retrieve internationalization settings (Resources object)...
        throw new Exception(t1);
      }
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing a delivery note",ex);
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

      try {
        inQtyBean.setConn(null);
        outQtyBean.setConn(null);
      } catch (Exception ex) {}
    }
  }


  /**
   * Business logic to execute.
   */
  public VOResponse deleteDeliveryNotes(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      DeliveryNotePK pk = null;

      pstmt = conn.prepareStatement(
          "update DOC08_DELIVERY_NOTES set ENABLED='N' where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
      );

      for(int i=0;i<list.size();i++) {
        pk = (DeliveryNotePK)list.get(i);

        // logically delete the record in DOC08...
        pstmt.setString(1,pk.getCompanyCodeSys01DOC08());
        pstmt.setString(2,pk.getDocTypeDOC08());
        pstmt.setBigDecimal(3,pk.getDocYearDOC08());
        pstmt.setBigDecimal(4,pk.getDocNumberDOC08());
        pstmt.execute();
      }


      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing delivery notes",ex);
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

