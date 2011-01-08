package org.jallinone.sqltool.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;

import java.lang.reflect.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete existing records, according to TableVO infos.</p>
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
public class DeleteTablesAction implements Action {

  public DeleteTablesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteTables";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  InsUpdDelTablesVO vo = (InsUpdDelTablesVO)inputPar;
	  try {
		  Response answer = deleteTables(vo,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
  
  

  /**
   * Business logic to execute.
   */
  public VOResponse deleteTables(InsUpdDelTablesVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);
      TableVO tableVO = vo.getTableVO();

      String tableName = null;
      ColumnVO colVO = null;
      RowVO rowVO = null;
      Response res = null;
      String sql = null;
      boolean pkOK = false;
      ArrayList pkMethods = new ArrayList();
      for(int i=0;i<tableVO.getMainTables().size();i++) {
        tableName = tableVO.getMainTables().get(i).toString();
        pkMethods.clear();

        // define delete sql statement...
        sql = "delete from "+tableName+" where ";
        pkOK = false;
        for(int j=0;j<tableVO.getColumns().size();j++) {
          colVO = (ColumnVO) tableVO.getColumns().get(j);
          if (colVO.getColumnName().startsWith(tableName) && colVO.getPrimaryKey()) {
            sql += colVO.getColumnName().substring(colVO.getColumnName().indexOf(tableName.length())+1)+"=? and ";
            pkOK = true;
            pkMethods.add(
              vo.getRowsToDelete().get(0).getClass().getMethod("get"+colVO.getAttributeName().substring(0,1).toUpperCase()+colVO.getAttributeName().substring(1),new Class[0])
            );
          }
        }
        if (!pkOK) {
          throw new Exception("no pk defined: delete not allowed");
        }
        sql = sql.substring(0,sql.length()-4);
        pstmt = conn.prepareStatement(sql);

        // create N delete operations, for each record to delete, based on the current table name...
        for(int k=0;k<vo.getRowsToDelete().size();k++) {
          rowVO = (RowVO)vo.getRowsToDelete().get(k);

          // set where values...
          for(int j=0;j<pkMethods.size();j++)
            pstmt.setObject(j+1,((Method)pkMethods.get(j)).invoke(rowVO,new Object[0]));

          // esecute delete...
          pstmt.execute();

        } // end loop in RowVO objects...
        pstmt.close();
      } // end loop on table names...


      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing carriers",ex);
      try {
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
    		ConnectionManager.releaseConnection(conn, null);
    	} catch (Exception e) {
    	}

    }


  }


}

