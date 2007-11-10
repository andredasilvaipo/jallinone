package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.permissions.java.RoleFunctionVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update role functions.</p>
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
public class UpdateRoleFunctionsAction implements Action {


  public UpdateRoleFunctionsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateRoleFunctions";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
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

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      RoleFunctionVO oldVO = null;
      RoleFunctionVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (RoleFunctionVO)oldVOs.get(i);
        newVO = (RoleFunctionVO)newVOs.get(i);

        if (!oldVO.getCanView().booleanValue()) {
          // no record in SYS07 yet...
          if (newVO.getCanView().booleanValue()) {
            pstmt = conn.prepareStatement(
              "insert into SYS07_ROLE_FUNCTIONS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) values(?,?,?,?,?)"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
            pstmt.setString(3,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
            pstmt.setString(4,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
            pstmt.setString(5,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
            pstmt.execute();

            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // insert also records in SYS02...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "insert into SYS02_COMPANIES_ACCESS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01) "+
                "select ?,?,?,?,?,COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y'"
              );
              pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
              pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
              pstmt.setString(3,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
              pstmt.setString(4,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
              pstmt.setString(5,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
              pstmt.execute();
            }

          }
        }
        else {
          // record already exists in SYS07...
          if (newVO.getCanView().booleanValue()) {
            // record in SYS07 will be updated...
            pstmt = conn.prepareStatement(
              "update SYS07_ROLE_FUNCTIONS set CAN_INS=?,CAN_UPD=?,CAN_DEL=? where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
            );
            pstmt.setString(1,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
            pstmt.setString(2,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
            pstmt.setString(3,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
            pstmt.setBigDecimal(4,newVO.getProgressiveSys04SYS07());
            pstmt.setString(5,newVO.getFunctionCodeSys06SYS07());
            pstmt.execute();

            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // update records in SYS02 too...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "update SYS02_COMPANIES_ACCESS set CAN_INS=?,CAN_UPD=?,CAN_DEL=? where "+
                "PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=? and COMPANY_CODE_SYS01 in "+
                "(select COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y')"
              );
              pstmt.setString(1,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
              pstmt.setString(2,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
              pstmt.setString(3,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
              pstmt.setBigDecimal(4,newVO.getProgressiveSys04SYS07());
              pstmt.setString(5,newVO.getFunctionCodeSys06SYS07());
              pstmt.execute();
            }

          }
          else {
            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // delete records from SYS02...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "delete from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
              );
              pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
              pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
              pstmt.execute();
            }

            // record will be deleted from SYS07...
            pstmt = conn.prepareStatement(
              "delete from SYS07_ROLE_FUNCTIONS where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
            pstmt.execute();
          }
        }
      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating role-functions settings",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
