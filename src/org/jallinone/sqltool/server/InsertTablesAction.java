package org.jallinone.sqltool.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;

import java.math.*;

import org.jallinone.system.translations.server.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.system.progressives.server.*;

import java.lang.reflect.*;

import org.jallinone.system.progressives.server.*;
import org.jallinone.system.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new records, according to TableVO infos.</p>
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
public class InsertTablesAction implements Action {

  public InsertTablesAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertTables";
  }


  public final Response executeCommand(Object inputPar,
		  UserSessionParameters userSessionPars,
		  HttpServletRequest request,
		  HttpServletResponse response,
		  HttpSession userSession,
		  ServletContext context) {
	  InsUpdDelTablesVO vo = (InsUpdDelTablesVO)inputPar;
	  try {
		  String defCompanyCodeSys01SYS03 = ((JAIOUserSessionParameters)userSessionPars).getDefCompanyCodeSys01SYS03();
		  Response answer = insertTables(vo,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),defCompanyCodeSys01SYS03);

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
  public VOListResponse insertTables(InsUpdDelTablesVO vo,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);
      TableVO tableVO = vo.getTableVO();

      String tableName = null;
      ColumnVO colVO = null;
      RowVO rowVO = null;
      BigDecimal progressive = null;
      Method m = null;
      Map attribute2dbField = null;
      Response res = null;
      for(int i=0;i<tableVO.getMainTables().size();i++) {
        tableName = tableVO.getMainTables().get(i).toString();

        // define attributes-db field map for the current table name...
        attribute2dbField = new HashMap();
        for(int j=0;j<tableVO.getColumns().size();j++) {
          colVO = (ColumnVO) tableVO.getColumns().get(j);
          if (colVO.getColumnName().startsWith(tableName)) {
            attribute2dbField.put(colVO.getAttributeName(),colVO.getColumnName().substring(tableName.length()+1));
          }
        }

        // create N insert operations, for each new record, based on the current table name...
        for(int k=0;k<vo.getInsertRows().size();k++) {
          rowVO = (RowVO)vo.getInsertRows().get(k);
          for(int j=0;j<tableVO.getColumns().size();j++) {
            colVO = (ColumnVO)tableVO.getColumns().get(j);
            // check if the current database field is a "calculated progressive"...
            if (colVO.getColumnName().startsWith(tableName) && colVO.getProgressive()) {
              progressive = CompanyProgressiveUtils.getInternalProgressive(defCompanyCodeSys01SYS03,tableName,colVO.getColumnName(),conn);
              m = rowVO.getClass().getMethod("set"+colVO.getAttributeName().substring(0,1).toUpperCase()+colVO.getAttributeName().substring(1),new Class[]{BigDecimal.class});
              m.invoke(rowVO,new Object[]{progressive});
            }
          }

          // execute insert operation for the current RowVO object...
          res = QueryUtil.insertTable(
              conn,
              new UserSessionParameters(username),
              rowVO,
              tableName,
              attribute2dbField,
              "Y",
              "N",
              null,
              true
          );
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }

        } // end loop in RowVO objects...

      } // end loop on table names...

      return new VOListResponse(vo.getInsertRows(),false,vo.getInsertRows().size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting new records", ex);
    	try {
   			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

    	throw new Exception(ex.getMessage());
    }
    finally {
    	try {
    		ConnectionManager.releaseConnection(conn, null);
    	} catch (Exception e) {
    	}

    }


  }



}

