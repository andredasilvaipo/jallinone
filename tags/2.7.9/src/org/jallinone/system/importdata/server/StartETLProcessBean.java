package org.jallinone.system.importdata.server;

import java.sql.Connection;

import javax.sql.DataSource;

import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.languages.server.LanguagesBean;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used start an ETL process.</p>
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
public class StartETLProcessBean  implements StartETLProcess {


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



  private ImportDataBean bean;

  public void setBean(ImportDataBean bean) {
    this.bean = bean;
  }

  private LoadETLProcessFieldsBean fieldsBean;

  public void setFieldsBean(LoadETLProcessFieldsBean fieldsBean) {
    this.fieldsBean = fieldsBean;
  }

  private LanguagesBean langsBean;

  public void setLangsBean(LanguagesBean langsBean) {
    this.langsBean = langsBean;
  }



  public StartETLProcessBean() {}




  /**
   * Business logic to execute.
   */
  public Response startETLProcess(ETLProcessVO processVO,String serverLanguageId,String username,String defaultCompanyCode) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...
      fieldsBean.setConn(conn); // use same transaction...
      langsBean.setConn(conn); // use same transaction...

      // retrieve supported languages...
      Response res = langsBean.loadLanguages(serverLanguageId,username);
      if (res.isError())
        return res;
      java.util.List langsVO = ((VOListResponse)res).getRows();

      // read from SYS24 table...
      res = fieldsBean.loadETLProcessFields(processVO.getProgressiveSYS23(),username);
      if (res.isError())
        return res;
      java.util.List fieldsVO = ((VOListResponse)res).getRows();

      Response answer = bean.importData(processVO,fieldsVO,langsVO,username,defaultCompanyCode);
      if (answer.isError()) {
        return answer;
      }


      return answer;
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while importing data",ex);
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
        fieldsBean.setConn(null);
        langsBean.setConn(null);
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

