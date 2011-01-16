package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage scheduled items.</p>
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
public class ScheduledItemsBean  implements ScheduledItems {


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




  public ScheduledItemsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ScheduledItemVO getScheduledItem(ScheduledActivityPK pk) {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadScheduledItems(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01,SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06,SYS10_TRANSLATIONS.DESCRIPTION,SCH15_SCHEDULED_ITEMS.QTY "+
          "from SCH15_SCHEDULED_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01=? and "+
          "SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH15","SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSch06SCH15","SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06");
      attribute2dbField.put("itemCodeItm01SCH15","SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("qtySCH15","SCH15_SCHEDULED_ITEMS.QTY");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());


      // read from SCH15 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ScheduledItemVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (!res.isError()) {
        HashSet itemCodes = new HashSet();
        ScheduledItemVO vo = null;
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          vo = (ScheduledItemVO)rows.get(i);
          itemCodes.add(vo.getItemCodeItm01SCH15());
        }

        // retrieve tasks defined in the call-out...
        sql =
            "select SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
            "from SCH14_CALL_OUT_ITEMS,SCH03_CALL_OUT_REQUESTS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01 and "+
            "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH14_CALL_OUT_ITEMS.CALL_OUT_CODE_SCH10 and "+
            "SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
            "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,serverLanguageId);
        pstmt.setString(2,pk.getCompanyCodeSys01SCH06());
        pstmt.setBigDecimal(3,pk.getProgressiveSCH06());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          vo = new ScheduledItemVO();
          vo.setCompanyCodeSys01SCH15(pk.getCompanyCodeSys01SCH06());
          vo.setProgressiveSch06SCH15(pk.getProgressiveSCH06());
          vo.setItemCodeItm01SCH15(rset.getString(1));
          vo.setDescriptionSYS10(rset.getString(2));
          vo.setProgressiveHie02ITM01(rset.getBigDecimal(3));
          if (!itemCodes.contains(vo.getItemCodeItm01SCH15()))
            rows.add(vo);
        }
        rset.close();
      }


      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled items list",ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse updateScheduledItems(ArrayList oldVos,ArrayList newVos,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ScheduledItemVO oldVO = null;
      ScheduledItemVO newVO = null;

      HashSet pk = new HashSet();
      pk.add("progressiveSch06SCH15");
      pk.add("companyCodeSys01SCH15");
      pk.add("itemCodeItm01SCH15");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveSch06SCH15","PROGRESSIVE_SCH06");
      attribute2dbField.put("companyCodeSys01SCH15","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01SCH15","ITEM_CODE_ITM01");
      attribute2dbField.put("qtySCH15","QTY");

      Response res = null;
      for(int i=0;i<oldVos.size();i++) {
        oldVO = (ScheduledItemVO)oldVos.get(i);
        newVO = (ScheduledItemVO)newVos.get(i);

        // update record in SCH15...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pk,
            oldVO,
            newVO,
            "SCH15_SCHEDULED_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVos,false,newVos.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while updating scheduled items", ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse insertScheduledItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ScheduledItemVO vo = null;


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveSch06SCH15","PROGRESSIVE_SCH06");
      attribute2dbField.put("companyCodeSys01SCH15","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01SCH15","ITEM_CODE_ITM01");
      attribute2dbField.put("qtySCH15","QTY");

      Response res = null;

      for(int i=0;i<list.size();i++) {
        vo = (ScheduledItemVO)list.get(i);

        // insert into SCH15...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH15_SCHEDULED_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new scheduled items", ex);
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




  /**
   * Business logic to execute.
   */
  public VOResponse deleteScheduledItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "delete from SCH15_SCHEDULED_ITEMS where COMPANY_CODE_SYS01=? and PROGRESSIVE_SCH06=? and ITEM_CODE_ITM01=?"
      );

      ScheduledItemVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH15...
        vo = (ScheduledItemVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH15());
        pstmt.setBigDecimal(2,vo.getProgressiveSch06SCH15());
        pstmt.setString(3,vo.getItemCodeItm01SCH15());
        pstmt.execute();
      }

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing scheduled items",ex);
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

