package org.jallinone.employees.server;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.employees.java.EmployeeCalendarVO;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage employee calendar.</p>
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
public class InsertEmployeeCalendarsBean implements InsertEmployeeCalendars {




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
	  
	  
	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	   */
	  public EmployeeCalendarVO getEmployeeCalendar() {
		  throw new UnsupportedOperationException();
	  }

	
	  /**
	   * Business logic to execute.
	   */
	  public VOListResponse insertEmployeeCalendars(ArrayList list,String serverLanguageId,String username) throws Throwable {
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      EmployeeCalendarVO vo = null;


	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01SCH02","COMPANY_CODE_SYS01");
	      attribute2dbField.put("progressiveReg04SCH02","PROGRESSIVE_REG04");
	      attribute2dbField.put("dayOfWeekSCH02","DAY_OF_WEEK");
	      attribute2dbField.put("morningStartHourSCH02","MORNING_START_HOUR");
	      attribute2dbField.put("morningEndHourSCH02","MORNING_END_HOUR");
	      attribute2dbField.put("afternoonStartHourSCH02","AFTERNOON_START_HOUR");
	      attribute2dbField.put("afternoonEndHourSCH02","AFTERNOON_END_HOUR");
	      Response res = null;

	      for(int i=0;i<list.size();i++) {
	        vo = (EmployeeCalendarVO)list.get(i);

	        // insert into SCH02...
	        res = QueryUtil.insertTable(
	            conn,
	            new UserSessionParameters(username),
	            vo,
	            "SCH02_EMPLOYEE_CALENDAR",
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
	                   "executeCommand", "Error while inserting new days of week in the employee calendar", ex);
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
