package org.jallinone.sqltool.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.openswing.swing.server.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing records, according to TableVO infos.</p>
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
public class UpdateTablesAction implements Action {

  public UpdateTablesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateTables";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  InsUpdDelTablesVO vo = (InsUpdDelTablesVO)inputPar;
	  try {
		  Response answer = updateTables(vo,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
    }
  }
  
  

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public RowVO getRow() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse updateTables(InsUpdDelTablesVO vo,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);
      TableVO tableVO = vo.getTableVO();

      String tableName = null;
      ColumnVO colVO = null;
      RowVO oldRowVO = null;
      RowVO newRowVO = null;
      Map attribute2dbField = null;
      HashSet pkAttrs = null;
      Response res = null;
      for(int i=0;i<tableVO.getMainTables().size();i++) {
        tableName = tableVO.getMainTables().get(i).toString();

        // define attributes-db field map for the current table name...
        attribute2dbField = new HashMap();
        pkAttrs = new HashSet();
        for(int j=0;j<tableVO.getColumns().size();j++) {
          colVO = (ColumnVO) tableVO.getColumns().get(j);
          if (colVO.getColumnName().startsWith(tableName)) {
            attribute2dbField.put(colVO.getAttributeName(),colVO.getColumnName().substring(tableName.length()+1));
            if (colVO.getPrimaryKey())
              pkAttrs.add(colVO.getAttributeName());
          }
        }

        if (pkAttrs.size()==0) {
          return new VOListResponse("no pk defined: update not allowed");
        }


        // create N update operations, for each updated record, based on the current table name...
        for(int k=0;k<vo.getOldUpdatedRows().size();k++) {
          oldRowVO = (RowVO)vo.getOldUpdatedRows().get(k);
          newRowVO = (RowVO)vo.getNewUpdatedRows().get(k);

          // execute update operation for the current RowVO object...
          res = QueryUtil.updateTable(
              conn,
              new UserSessionParameters(username),
              pkAttrs,
              oldRowVO,
              newRowVO,
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

      return new VOListResponse(vo.getNewUpdatedRows(),false,vo.getNewUpdatedRows().size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing records",ex);
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

