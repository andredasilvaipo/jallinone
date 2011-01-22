package org.jallinone.registers.measure.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.registers.measure.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage measures.</p>
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
public class MeasuresBean implements Measures {


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




  public MeasuresBean() {
  }


  public MeasureVO getMeasure() {
	  throw new UnsupportedOperationException();
  }
  
  public MeasureConvVO getMeasureConv() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Convert the specified quantity from the specified m.u. to the final m.u.
   * Throws and exception if no conversion factor is defined between the two m.u.
   */
  public BigDecimal convertQty(String fromUMCode,String toUMCode,BigDecimal qty,String serverLanguageId,String username) throws Exception {
    if (qty==null)
      return null;
    if (fromUMCode.equals(toUMCode))
      return qty;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      return qty.multiply(getConversion(fromUMCode, toUMCode, serverLanguageId, username));
    }
    catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"convertQty","Error while fetching measure conversion",ex);
      throw new Exception("no conversion factor is defined between m.u. '"+fromUMCode+"' to '"+toUMCode+"'");
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
   * @return conversion factor beetwen the specified m.u.; null if no conversion is defined beetween the two m.u.
   */
  public BigDecimal getConversion(String fromUMCode,String toUMCode,String serverLanguageId,String username) throws Exception {
    Connection conn = null;
    Statement stmt = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      
      if (fromUMCode.equals(toUMCode))
        return new BigDecimal(1);

      String sql = "select VALUE from REG05_MEASURE_CONV where UM_CODE='"+fromUMCode+"' and UM_CODE_REG02='"+toUMCode+"'";
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      BigDecimal value = null;
      if(rset.next()) {
        value = rset.getBigDecimal(1);
      }
      rset.close();

      return value;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"getConversion","Error while fetching measure conversion",ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse loadMeasures(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG02_MEASURE_UNITS.UM_CODE,REG02_MEASURE_UNITS.DECIMALS,REG02_MEASURE_UNITS.ENABLED from REG02_MEASURE_UNITS where "+
          "REG02_MEASURE_UNITS.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("umCodeREG02","REG02_MEASURE_UNITS.UM_CODE");
      attribute2dbField.put("decimalsREG02","REG02_MEASURE_UNITS.DECIMALS");
      attribute2dbField.put("enabledREG02","REG02_MEASURE_UNITS.ENABLED");

      ArrayList values = new ArrayList();


      // read from REG02 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MeasureVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching measures list",ex);
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
  public VOListResponse loadMeasureConvs(GridParams pars,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String umCode = (String)pars.getOtherGridParams().get(ApplicationConsts.UM_CODE_REG02);


      String sql =
          "select REG05_MEASURE_CONV.UM_CODE,REG05_MEASURE_CONV.UM_CODE_REG02,REG05_MEASURE_CONV.VALUE from REG05_MEASURE_CONV where "+
          "REG05_MEASURE_CONV.UM_CODE='"+umCode+"'";

      MeasureConvVO vo = null;
      ArrayList list = new ArrayList();
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(sql);
      while(rset.next()) {
        vo = new MeasureConvVO();
        vo.setUmCodeREG05(rset.getString(1));
        vo.setUmCodeReg02REG05(rset.getString(2));
        vo.setValueREG05(rset.getBigDecimal(3));
        list.add(vo);
      }
      rset.close();

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching measure conversions list",ex);
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

  

  /**
   * Business logic to execute.
   */
  public VOListResponse validateMeasureCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG02_MEASURE_UNITS.UM_CODE,REG02_MEASURE_UNITS.DECIMALS,REG02_MEASURE_UNITS.ENABLED from REG02_MEASURE_UNITS where "+
          "REG02_MEASURE_UNITS.ENABLED='Y' and "+
          "REG02_MEASURE_UNITS.UM_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("umCodeREG02","REG02_MEASURE_UNITS.UM_CODE");
      attribute2dbField.put("decimalsREG02","REG02_MEASURE_UNITS.DECIMALS");
      attribute2dbField.put("enabledREG02","REG02_MEASURE_UNITS.ENABLED");

      ArrayList values = new ArrayList();
      GridParams gridParams = new GridParams();

      // read from REG03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MeasureVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating measure code",ex);
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
  public VOListResponse insertMeasures(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      MeasureVO vo = null;


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("umCodeREG02","UM_CODE");
      attribute2dbField.put("decimalsREG02","DECIMALS");
      attribute2dbField.put("enabledREG02","ENABLED");

      Response res = null;
      stmt = conn.createStatement();
      for(int i=0;i<list.size();i++) {
        vo = (MeasureVO)list.get(i);
        vo.setEnabledREG02("Y");

        // insert into REG03...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG02_MEASURE_UNITS",
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

        // insert records in REG05...
        stmt.execute(
            "insert into REG05_MEASURE_CONV(UM_CODE,UM_CODE_REG02) "+
            "select '"+vo.getUmCodeREG02()+"',UM_CODE from REG02_MEASURE_UNITS where UM_CODE <> '"+vo.getUmCodeREG02()+"' and ENABLED='Y'"
        );
        stmt.execute(
            "insert into REG05_MEASURE_CONV(UM_CODE_REG02,UM_CODE) "+
            "select '"+vo.getUmCodeREG02()+"',UM_CODE from REG02_MEASURE_UNITS where UM_CODE <> '"+vo.getUmCodeREG02()+"' and ENABLED='Y'"
        );
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new measures", ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse updateMeasures(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      MeasureVO oldVO = null;
      MeasureVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (MeasureVO)oldVOs.get(i);
        newVO = (MeasureVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("umCodeREG02");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("umCodeREG02","UM_CODE");
        attr2dbFields.put("decimalsREG02","DECIMALS");
        attr2dbFields.put("enabledREG02","ENABLED");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG02_MEASURE_UNITS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing measures",ex);
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
  public VOListResponse updateMeasureConvs(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      MeasureConvVO oldVO = null;
      MeasureConvVO newVO = null;
      Response res = null;

      String sql = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (MeasureConvVO)oldVOs.get(i);
        newVO = (MeasureConvVO)newVOs.get(i);

        sql =
            "update REG05_MEASURE_CONV set VALUE="+
            (newVO.getValueREG05()==null?"null":newVO.getValueREG05().toString())+" where "+
            "UM_CODE='"+newVO.getUmCodeREG05()+"' and "+
            "UM_CODE_REG02='"+newVO.getUmCodeReg02REG05()+"' and "+
            "VALUE"+(oldVO.getValueREG05()==null?" is null":"="+oldVO.getValueREG05());


        stmt.execute(sql);
        String convStr = "null";
        if (newVO.getValueREG05()!=null) {
            BigDecimal conv = new BigDecimal(1).divide(newVO.getValueREG05(),5,BigDecimal.ROUND_HALF_UP);
            convStr = conv.toString();
        }
        sql =
            "update REG05_MEASURE_CONV set VALUE="+
            convStr+" where "+
            "UM_CODE='"+newVO.getUmCodeReg02REG05()+"' and "+
            "UM_CODE_REG02='"+newVO.getUmCodeREG05()+"'";


        stmt.execute(sql);

      }


      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing measure conversions",ex);
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




  /**
   * Business logic to execute.
   */
  public VOResponse deleteMeasures(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      MeasureVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG02...
        vo = (MeasureVO)list.get(i);
        stmt.execute("update REG02_MEASURE_UNITS set ENABLED='N' where UM_CODE='"+vo.getUmCodeREG02()+"'");

        // delete records from REG05...
        stmt.execute(
            "delete from REG05_MEASURE_CONV where UM_CODE = '"+vo.getUmCodeREG02()+"' or UM_CODE_REG02 = '"+vo.getUmCodeREG02()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing measures",ex);
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

