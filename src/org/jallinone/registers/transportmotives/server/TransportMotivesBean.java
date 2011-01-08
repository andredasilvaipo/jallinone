package org.jallinone.registers.transportmotives.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.registers.transportmotives.java.TransportMotiveVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage transport motives.</p>
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
public class TransportMotivesBean  implements TransportMotives {


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




  public TransportMotivesBean() {
  }



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public TransportMotiveVO getTransportMotive() {
	  throw new UnsupportedOperationException();  
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse validateTransportMotiveCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE,REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,REG20_TRANSPORT_MOTIVES.ENABLED from REG20_TRANSPORT_MOTIVES,SYS10_TRANSLATIONS where "+
          "REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG20_TRANSPORT_MOTIVES.ENABLED='Y' and "+
          "REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("transportMotiveCodeREG20","REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG20","REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG20","REG20_TRANSPORT_MOTIVES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from REG20 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          TransportMotiveVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating transport motive code",ex);
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
  public VOListResponse loadTransportMotives(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE,REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,REG20_TRANSPORT_MOTIVES.ENABLED from REG20_TRANSPORT_MOTIVES,SYS10_TRANSLATIONS where "+
          "REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG20_TRANSPORT_MOTIVES.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("transportMotiveCodeREG20","REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG20","REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG20","REG20_TRANSPORT_MOTIVES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from REG20 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          TransportMotiveVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching transport motives list",ex);
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
  public VOListResponse insertTransportMotives(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      TransportMotiveVO vo = null;

      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("transportMotiveCodeREG20","TRANSPORT_MOTIVE_CODE");
      attribute2dbField.put("progressiveSys10REG20","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG20","ENABLED");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (TransportMotiveVO)list.get(i);
        vo.setEnabledREG20("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);
        vo.setProgressiveSys10REG20(progressiveSYS10);

        // insert into REG20...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG20_TRANSPORT_MOTIVES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new transport motives", ex);
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
  public VOListResponse updateTransportMotives(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      TransportMotiveVO oldVO = null;
      TransportMotiveVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (TransportMotiveVO)oldVOs.get(i);
        newVO = (TransportMotiveVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG20(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("transportMotiveCodeREG20");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("transportMotiveCodeREG20","TRANSPORT_MOTIVE_CODE");
        attr2dbFields.put("progressiveSys10REG20","PROGRESSIVE_SYS10");
        attr2dbFields.put("enabledREG20","ENABLED");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG20_TRANSPORT_MOTIVES",
            attr2dbFields,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing transport motives",ex);
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
  public VOResponse deleteTransportMotives(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      TransportMotiveVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG20...
        vo = (TransportMotiveVO)list.get(i);
        stmt.execute("update REG20_TRANSPORT_MOTIVES set ENABLED='N' where TRANSPORT_MOTIVE_CODE='"+vo.getTransportMotiveCodeREG20()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing transport motives",ex);
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

