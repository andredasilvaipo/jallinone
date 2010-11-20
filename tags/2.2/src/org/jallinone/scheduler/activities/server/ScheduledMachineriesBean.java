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
 * * <p>Description: Bean used to manage scheduled machineries.</p>
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
public class ScheduledMachineriesBean  implements ScheduledMachineries {


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




  public ScheduledMachineriesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ScheduledMachineriesVO getScheduledMachineries(ScheduledActivityPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadScheduledMachineries(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03,SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01,SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,SCH09_SCHEDULED_MACHINERIES.START_DATE,SCH09_SCHEDULED_MACHINERIES.END_DATE,SCH09_SCHEDULED_MACHINERIES.DURATION,"+
          "SCH09_SCHEDULED_MACHINERIES.NOTE "+
          "from SCH09_SCHEDULED_MACHINERIES,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
          "SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01=? and "+
          "SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH09","SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSch06SCH09","SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06");
      attribute2dbField.put("machineryCodePro03SCH09","SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03");
      attribute2dbField.put("startDateSCH09","SCH09_SCHEDULED_MACHINERIES.START_DATE");
      attribute2dbField.put("endDateSCH09","SCH09_SCHEDULED_MACHINERIES.END_DATE");
      attribute2dbField.put("durationSCH09","SCH09_SCHEDULED_MACHINERIES.DURATION");
      attribute2dbField.put("noteSCH09","SCH09_SCHEDULED_MACHINERIES.NOTE");

      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());


      // read from SCH09 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ScheduledMachineriesVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (!res.isError()) {
        HashSet macCodes = new HashSet();
        ScheduledMachineriesVO vo = null;
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          vo = (ScheduledMachineriesVO)rows.get(i);
          macCodes.add(vo.getMachineryCodePro03SCH09());
        }

        // retrieve tasks defined in the call-out...
        sql =
            "select SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03,SYS10_TRANSLATIONS.DESCRIPTION "+
            "from SCH13_CALL_OUT_MACHINERIES,SCH03_CALL_OUT_REQUESTS,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01 and "+
            "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10 and "+
            "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
            "SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
            "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
            "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,serverLanguageId);
        pstmt.setString(2,pk.getCompanyCodeSys01SCH06());
        pstmt.setBigDecimal(3,pk.getProgressiveSCH06());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          vo = new ScheduledMachineriesVO();
          vo.setCompanyCodeSys01SCH09(pk.getCompanyCodeSys01SCH06());
          vo.setProgressiveSch06SCH09(pk.getProgressiveSCH06());
          vo.setMachineryCodePro03SCH09(rset.getString(1));
          vo.setDescriptionSYS10(rset.getString(2));
          if (!macCodes.contains(vo.getMachineryCodePro03SCH09()))
            rows.add(vo);
        }
        rset.close();
      }


      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled machineries list",ex);
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
  public VOListResponse updateScheduledMachineries(ArrayList oldVos,ArrayList newVos,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ScheduledMachineriesVO oldVO = null;
      ScheduledMachineriesVO newVO = null;

      HashSet pk = new HashSet();
      pk.add("companyCodeSys01SCH09");
      pk.add("progressiveSch06SCH09");
      pk.add("machineryCodePro03SCH09");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveSch06SCH09","PROGRESSIVE_SCH06");
      attribute2dbField.put("companyCodeSys01SCH09","COMPANY_CODE_SYS01");
      attribute2dbField.put("machineryCodePro03SCH09","MACHINERY_CODE_PRO03");
      attribute2dbField.put("startDateSCH09","START_DATE");
      attribute2dbField.put("endDateSCH09","END_DATE");
      attribute2dbField.put("durationSCH09","DURATION");
      attribute2dbField.put("noteSCH09","NOTE");

      Response res = null;
      for(int i=0;i<oldVos.size();i++) {
        oldVO = (ScheduledMachineriesVO)oldVos.get(i);
        newVO = (ScheduledMachineriesVO)newVos.get(i);

        // update record in SCH09...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pk,
            oldVO,
            newVO,
            "SCH09_SCHEDULED_MACHINERIES",
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
                   "executeCommand", "Error while updating scheduled machineries", ex);
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
  public VOListResponse insertScheduledMachinery(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ScheduledMachineriesVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveSch06SCH09","PROGRESSIVE_SCH06");
      attribute2dbField.put("companyCodeSys01SCH09","COMPANY_CODE_SYS01");
      attribute2dbField.put("machineryCodePro03SCH09","MACHINERY_CODE_PRO03");
      attribute2dbField.put("startDateSCH09","START_DATE");
      attribute2dbField.put("endDateSCH09","END_DATE");
      attribute2dbField.put("durationSCH09","DURATION");
      attribute2dbField.put("noteSCH09","NOTE");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (ScheduledMachineriesVO)list.get(i);

        // insert into SCH09...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH09_SCHEDULED_MACHINERIES",
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
                   "executeCommand", "Error while inserting new scheduled machineries", ex);
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
  public VOResponse deleteScheduledMachineries(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "delete from SCH09_SCHEDULED_MACHINERIES where COMPANY_CODE_SYS01=? and PROGRESSIVE_SCH06=? and MACHINERY_CODE_PRO03=?"
      );

      ScheduledMachineriesVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH09...
        vo = (ScheduledMachineriesVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH09());
        pstmt.setBigDecimal(2,vo.getProgressiveSch06SCH09());
        pstmt.setString(3,vo.getMachineryCodePro03SCH09());
        pstmt.execute();
      }

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing scheduled machineries",ex);
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

