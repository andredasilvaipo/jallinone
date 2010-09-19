package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.customizations.java.WindowCustomizationVO;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new customized fields in the specified window.</p>
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
public class InsertWindowCustomizationsAction implements Action {


  public InsertWindowCustomizationsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertWindowCustomizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    // retrieve internationalization settings (Resources object)...
    ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
    Resources res = factory.getResources(userSessionPars.getLanguageId());


    try {
      WindowCustomizationVO vo = null;

      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));


      String sql = null;
      java.util.List list = (ArrayList)inputPar;
      stmt = conn.createStatement();
      boolean columnAlreadyExist = false;
      String attrName = null;
      HashSet attrNamesAlreadyUsed = new HashSet();
      BigDecimal progressiveSys10 = null;

      for(int j=0;j<list.size();j++) {
        vo = (WindowCustomizationVO)list.get(j);

        // find out if the field name already exixts...
        ResultSet rset = stmt.executeQuery(
          "select COLUMN_TYPE from SYS12_WINDOW_CUSTOMIZATIONS,SYS13_WINDOWS where "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13=SYS13_WINDOWS.PROGRESSIVE and "+
          "SYS13_WINDOWS.TABLE_NAME='"+vo.getTableNameSYS13()+"' and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
        );
        columnAlreadyExist = false;
        if(rset.next()) {
          columnAlreadyExist = true;
          String colType = rset.getString(1);
          rset.close();
          if (!colType.equals(vo.getColumnTypeSYS12())) {
            // column already exixts AND it's of a different type: insert not allowed!
            Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","The field name spacified '"+vo.getColumnNameSYS12()+"' already exists in '"+vo.getTableNameSYS13()+"'.",null);
            conn.rollback();
            return new ErrorResponse(res.getResource("The specified field name already exixts."));
          }
        }
        else
          rset.close();

        // define attributeName...
        attrName = null;
        rset = stmt.executeQuery(
          "select ATTRIBUTE_NAME from SYS12_WINDOW_CUSTOMIZATIONS where PROGRESSIVE_SYS13="+vo.getProgressiveSys13SYS12()+" and COLUMN_TYPE='"+vo.getColumnTypeSYS12()+"'"
        );
        attrNamesAlreadyUsed.clear();
        while(rset.next()) {
          attrNamesAlreadyUsed.add( rset.getString(1) );
        }
        rset.close();
        boolean attrFound = false;
        for(int i=0;i<10;i++)
          if (!attrNamesAlreadyUsed.contains("attributeName"+vo.getColumnTypeSYS12()+i)) {
            attrFound = true;
            vo.setAttributeNameSYS12("attributeName"+vo.getColumnTypeSYS12()+i);
            break;
          }
        if (!attrFound) {
          // there are not other available attributes...
          Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Maximum number of customized columns reached.",null);
          conn.rollback();
          return new ErrorResponse(res.getResource("Maximum number of customized columns reached."));
        }

        // insert record in SYS10...
        progressiveSys10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),((JAIOUserSessionParameters)userSessionPars).getDefCompanyCodeSys01SYS03(),conn);

        // insert record in SYS12...
        stmt.execute(
            "insert into SYS12_WINDOW_CUSTOMIZATIONS(PROGRESSIVE_SYS13,COLUMN_NAME,COLUMN_TYPE,COLUMN_SIZE,COLUMN_DEC,PROGRESSIVE_SYS10,ATTRIBUTE_NAME) values("+
            vo.getProgressiveSys13SYS12()+",'"+vo.getColumnNameSYS12()+"','"+vo.getColumnTypeSYS12()+"',"+vo.getColumnSizeSYS12()+","+vo.getColumnDecSYS12()+","+progressiveSys10+",'"+vo.getAttributeNameSYS12()+"')"
        );

        if (!columnAlreadyExist) {
          // add the column to the table...
          sql = "alter table "+vo.getTableNameSYS13()+" add column "+vo.getColumnNameSYS12()+" ";
          if (vo.getColumnTypeSYS12().equals("S")) {
            if (conn.getMetaData().getDriverName().equals("oracle.jdbc.driver.OracleDriver"))
              sql += "varchar2";
            else
              sql += "varchar";
            sql += "("+vo.getColumnSizeSYS12()+")";
          }
          else if (vo.getColumnTypeSYS12().equals("D")) {
            sql += "date";
          }
          else if (vo.getColumnTypeSYS12().equals("N")) {
            sql += "decimal("+vo.getColumnSizeSYS12()+vo.getColumnDecSYS12()+","+vo.getColumnDecSYS12()+")";
          }
          stmt.execute(sql);
        }
      }


      Response answer = new VOListResponse(list,false,list.size());

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting new customized fields",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
