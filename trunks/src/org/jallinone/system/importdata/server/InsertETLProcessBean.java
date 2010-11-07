package org.jallinone.system.importdata.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.scheduler.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert a new ETL process in SYS23 table.</p>
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
public class InsertETLProcessBean  implements InsertETLProcess {


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



  private DeleteETLProcessesBean bean;

  public void setBean(DeleteETLProcessesBean bean) {
    this.bean = bean;
  }



  public InsertETLProcessBean() {}




  /**
   * Business logic to execute.
   */
  public VOResponse insertETLProcess(ETLProcessVO processVO,java.util.List fieldsVO,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...

      // remove existing records...
      ArrayList vos = new ArrayList();
      vos.add(processVO);
      VOResponse answer = bean.deleteETLProcesses(vos,serverLanguageId,username);
      if (answer.isError()) {
        throw new Exception(answer.getErrorMessage()); 
      }

      BigDecimal progressive = CompanyProgressiveUtils.getInternalProgressive(
        defCompanyCodeSys01SYS03,
        "SYS23_ETL_PROCESSES",
        "PROGRESSIVE",
        conn
      );
      processVO.setProgressiveSYS23(progressive);


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("fileFormatSYS23","FILE_FORMAT");
      attribute2dbField.put("classNameSYS23","CLASS_NAME");
      attribute2dbField.put("companyCodeSys01SYS23","COMPANY_CODE_SYS01");
      attribute2dbField.put("schedulingTypeSYS23","SCHEDULING_TYPE");
      attribute2dbField.put("startTimeSYS23","START_TIME");
      attribute2dbField.put("filenameSYS23","FILENAME");
      attribute2dbField.put("subTypeValueSYS23","SUB_TYPE_VALUE");
      attribute2dbField.put("subTypeValue2SYS23","SUB_TYPE_VALUE2");
      attribute2dbField.put("levelsSepSYS23","LEVELS_SEP");
      attribute2dbField.put("progressiveHIE02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSYS23","PROGRESSIVE");
      attribute2dbField.put("descriptionSYS23","DESCRIPTION");

      // insert into SYS23...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          processVO,
          "SYS23_ETL_PROCESSES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      processVO = (ETLProcessVO)((VOResponse)res).getVo();
      answer = (VOResponse)res;


      attribute2dbField.clear();
      attribute2dbField.put("fieldNameSYS24","FIELD_NAME");
      attribute2dbField.put("languageCodeSYS24","LANGUAGE_CODE");
      attribute2dbField.put("startPosSYS24","START_POS");
      attribute2dbField.put("endPosSYS24","END_POS");
      attribute2dbField.put("dateFormatSYS24","DATE_FORMAT");
      attribute2dbField.put("posSYS24","POS");
      attribute2dbField.put("progressiveSys23SYS24","PROGRESSIVE_SYS23");
      attribute2dbField.put("progressiveSYS24","PROGRESSIVE");

      SelectableFieldVO field = null;
      progressive = null;
      for(int i=0;i<fieldsVO.size();i++) {
        field = (SelectableFieldVO)fieldsVO.get(i);
        if (field.isSelected()) {
          field.getField().setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

          progressive = CompanyProgressiveUtils.getInternalProgressive(
            defCompanyCodeSys01SYS03,
            "SYS24_ETL_PROCESS_FIELDS",
            "PROGRESSIVE",
            conn
          );
          field.getField().setProgressiveSYS24(progressive);
          field.getField().setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

          // insert into SYS24...
          res = QueryUtil.insertTable(
              conn,
              new UserSessionParameters(username),
              field.getField(),
              "SYS24_ETL_PROCESS_FIELDS",
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
      }


      ETLProcessesScheduler.getInstance().restartProcesses();
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(), "executeCommand", "Error while inserting a new ETL process", ex);
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
        bean.setConn(null);
      } catch (Exception ex) {}
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


