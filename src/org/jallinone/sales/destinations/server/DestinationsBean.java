package org.jallinone.sales.destinations.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.system.server.*;
import org.jallinone.sales.destinations.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage destinations.</p>
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
public class DestinationsBean  implements Destinations {


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




  public DestinationsBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public DestinationVO getDestination(SubjectPK pk) {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadDestinations(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG18_DESTINATIONS.COMPANY_CODE_SYS01,REG18_DESTINATIONS.PROGRESSIVE_REG04,REG18_DESTINATIONS.DESTINATION_CODE,REG18_DESTINATIONS.DESCRIPTION,REG18_DESTINATIONS.ADDRESS,REG18_DESTINATIONS.CITY,REG18_DESTINATIONS.ZIP,REG18_DESTINATIONS.PROVINCE,REG18_DESTINATIONS.COUNTRY from REG18_DESTINATIONS where "+
          "REG18_DESTINATIONS.COMPANY_CODE_SYS01=? and REG18_DESTINATIONS.PROGRESSIVE_REG04=?";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG18","REG18_DESTINATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04REG18","REG18_DESTINATIONS.PROGRESSIVE_REG04");
      attribute2dbField.put("addressREG18","REG18_DESTINATIONS.ADDRESS");
      attribute2dbField.put("cityREG18","REG18_DESTINATIONS.CITY");
      attribute2dbField.put("zipREG18","REG18_DESTINATIONS.ZIP");
      attribute2dbField.put("provinceREG18","REG18_DESTINATIONS.PROVINCE");
      attribute2dbField.put("countryREG18","REG18_DESTINATIONS.COUNTRY");
      attribute2dbField.put("destinationCodeREG18","REG18_DESTINATIONS.DESTINATION_CODE");
      attribute2dbField.put("descriptionREG18","REG18_DESTINATIONS.DESCRIPTION");

      ArrayList values = new ArrayList();

      SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);

      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());


      // read from REG18 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          DestinationVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching destinations list",ex);
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
  public VOListResponse updateDestinations(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      DestinationVO oldVO = null;
      DestinationVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (DestinationVO)oldVOs.get(i);
        newVO = (DestinationVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01REG18");
        pkAttrs.add("progressiveReg04REG18");
        pkAttrs.add("destinationCodeREG18");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01REG18","COMPANY_CODE_SYS01");
        attribute2dbField.put("progressiveReg04REG18","PROGRESSIVE_REG04");
        attribute2dbField.put("addressREG18","ADDRESS");
        attribute2dbField.put("cityREG18","CITY");
        attribute2dbField.put("zipREG18","ZIP");
        attribute2dbField.put("provinceREG18","PROVINCE");
        attribute2dbField.put("countryREG18","COUNTRY");
        attribute2dbField.put("destinationCodeREG18","DESTINATION_CODE");
        attribute2dbField.put("descriptionREG18","DESCRIPTION");

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG18_DESTINATIONS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing destinations",ex);
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
  public VOListResponse validateDestinationCode(LookupValidationParams validationPars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG18_DESTINATIONS.COMPANY_CODE_SYS01,REG18_DESTINATIONS.PROGRESSIVE_REG04,REG18_DESTINATIONS.DESTINATION_CODE,REG18_DESTINATIONS.DESCRIPTION,REG18_DESTINATIONS.ADDRESS,REG18_DESTINATIONS.CITY,REG18_DESTINATIONS.ZIP,REG18_DESTINATIONS.PROVINCE,REG18_DESTINATIONS.COUNTRY from REG18_DESTINATIONS where "+
          "REG18_DESTINATIONS.COMPANY_CODE_SYS01=? and REG18_DESTINATIONS.PROGRESSIVE_REG04=? and "+
          "REG18_DESTINATIONS.DESTINATION_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG18","REG18_DESTINATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04REG18","REG18_DESTINATIONS.PROGRESSIVE_REG04");
      attribute2dbField.put("addressREG18","REG18_DESTINATIONS.ADDRESS");
      attribute2dbField.put("cityREG18","REG18_DESTINATIONS.CITY");
      attribute2dbField.put("zipREG18","REG18_DESTINATIONS.ZIP");
      attribute2dbField.put("provinceREG18","REG18_DESTINATIONS.PROVINCE");
      attribute2dbField.put("countryREG18","REG18_DESTINATIONS.COUNTRY");
      attribute2dbField.put("destinationCodeREG18","REG18_DESTINATIONS.DESTINATION_CODE");
      attribute2dbField.put("descriptionREG18","REG18_DESTINATIONS.DESCRIPTION");

      ArrayList values = new ArrayList();
      SubjectPK pk = (SubjectPK)validationPars.getLookupValidationParameters().get(ApplicationConsts.SUBJECT_PK);
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());

      GridParams gridParams = new GridParams();

      // read from REG18 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          DestinationVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating destination code",ex);
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
  public VOListResponse insertDestinations(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      DestinationVO vo = null;
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG18","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04REG18","PROGRESSIVE_REG04");
      attribute2dbField.put("addressREG18","ADDRESS");
      attribute2dbField.put("cityREG18","CITY");
      attribute2dbField.put("zipREG18","ZIP");
      attribute2dbField.put("provinceREG18","PROVINCE");
      attribute2dbField.put("countryREG18","COUNTRY");
      attribute2dbField.put("destinationCodeREG18","DESTINATION_CODE");
      attribute2dbField.put("descriptionREG18","DESCRIPTION");
      Response res = null;

      for(int i=0;i<list.size();i++) {
        vo = (DestinationVO)list.get(i);

        // insert into REG18...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG18_DESTINATIONS",
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
                   "executeCommand", "Error while inserting new destinations", ex);
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
  public VOResponse deleteDestinations(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();
      DestinationVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in REG18...
        vo = (DestinationVO)list.get(i);
        stmt.execute("delete from REG18_DESTINATIONS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG18()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04REG18()+" and DESTINATION_CODE='"+vo.getDestinationCodeREG18()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing destinations",ex);
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

