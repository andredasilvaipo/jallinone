package org.jallinone.warehouse.tables.motives.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.warehouse.tables.motives.java.MotiveVO;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage warehouse motives.</p>
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
public class WarehouseMotivesBean  implements WarehouseMotives {


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




  public WarehouseMotivesBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadWarehouseMotives(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE,WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,WAR04_WAREHOUSE_MOTIVES.ENABLED,WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE,WAR04_WAREHOUSE_MOTIVES.QTY_SIGN from WAR04_WAREHOUSE_MOTIVES,SYS10_TRANSLATIONS where "+
          "WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "WAR04_WAREHOUSE_MOTIVES.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("warehouseMotiveWAR04","WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10WAR04","WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledWAR04","WAR04_WAREHOUSE_MOTIVES.ENABLED");
      attribute2dbField.put("itemTypeWAR04","WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE");
      attribute2dbField.put("qtySignWAR04","WAR04_WAREHOUSE_MOTIVES.QTY_SIGN");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from WAR04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MotiveVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching warehouse motives list",ex);
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
  public VOListResponse updateWarehouseMotives(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      MotiveVO oldVO = null;
      MotiveVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (MotiveVO)oldVOs.get(i);
        newVO = (MotiveVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10WAR04(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("warehouseMotiveWAR04");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("warehouseMotiveWAR04","WAREHOUSE_MOTIVE");
        attribute2dbField.put("progressiveSys10WAR04","PROGRESSIVE_SYS10");
        attribute2dbField.put("enabledWAR04","ENABLED");
        attribute2dbField.put("itemTypeWAR04","ITEM_TYPE");
        attribute2dbField.put("qtySignWAR04","QTY_SIGN");

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "WAR04_WAREHOUSE_MOTIVES",
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

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing warehouse motives",ex);
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
  public VOListResponse validateWarehouseMotiveCode(LookupValidationParams validationPars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE,WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,WAR04_WAREHOUSE_MOTIVES.ENABLED,WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE,WAR04_WAREHOUSE_MOTIVES.QTY_SIGN  from WAR04_WAREHOUSE_MOTIVES,SYS10_TRANSLATIONS where "+
          "WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "WAR04_WAREHOUSE_MOTIVES.ENABLED='Y' and "+
          "WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("warehouseMotiveWAR04","WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10WAR04","WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledWAR04","WAR04_WAREHOUSE_MOTIVES.ENABLED");
      attribute2dbField.put("itemTypeWAR04","WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE");
      attribute2dbField.put("qtySignWAR04","WAR04_WAREHOUSE_MOTIVES.QTY_SIGN");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from WAR04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MotiveVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
      
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating warehouse motive code",ex);
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
  public VOResponse insertWarehouseMotive(MotiveVO vo,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setEnabledWAR04("Y");

      // insert record in SYS10...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);
      vo.setProgressiveSys10WAR04(progressiveSYS10);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("warehouseMotiveWAR04","WAREHOUSE_MOTIVE");
      attribute2dbField.put("progressiveSys10WAR04","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledWAR04","ENABLED");
      attribute2dbField.put("itemTypeWAR04","ITEM_TYPE");
      attribute2dbField.put("qtySignWAR04","QTY_SIGN");

      // insert into WAR04...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "WAR04_WAREHOUSE_MOTIVES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );


      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new warehouse motive", ex);
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
  public VOListResponse insertWarehouseMotives(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      MotiveVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("warehouseMotiveWAR04","WAREHOUSE_MOTIVE");
      attribute2dbField.put("progressiveSys10WAR04","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledWAR04","ENABLED");
      attribute2dbField.put("itemTypeWAR04","ITEM_TYPE");
      attribute2dbField.put("qtySignWAR04","QTY_SIGN");

      BigDecimal progressiveSYS10 = null;
      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (MotiveVO)list.get(i);
        vo.setEnabledWAR04("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);
        vo.setProgressiveSys10WAR04(progressiveSYS10);

        // insert into WAR04...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "WAR04_WAREHOUSE_MOTIVES",
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
                   "executeCommand", "Error while inserting new warehouse motives", ex);
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
  public VOResponse deleteWarehouseMotives(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      MotiveVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in WAR04...
        vo = (MotiveVO)list.get(i);
        stmt.execute("update WAR04_WAREHOUSE_MOTIVES set ENABLED='N' where WAREHOUSE_MOTIVE='"+vo.getWarehouseMotiveWAR04()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing warehouse motives",ex);
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

