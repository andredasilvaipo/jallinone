package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import java.math.BigDecimal;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.permissions.java.RoleFunctionCompanyVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve companies related to the specified role-function.</p>
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
public class LoadRoleFunctionCompaniesAction implements Action {


  public LoadRoleFunctionCompaniesAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadRoleFunctionCompanies";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;
    Connection conn = null;
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
      GridParams params = (GridParams)inputPar;
      BigDecimal progressiveSYS04 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      String functionCodeSYS06 = (String)params.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06);

      // retrieve all companies...
      RoleFunctionCompanyVO vo = null;
      ArrayList list = new ArrayList();
      pstmt = conn.prepareStatement(
          "select COMPANY_CODE,NAME_1 from SYS01_COMPANIES,REG04_SUBJECTS where "+
          "SYS01_COMPANIES.COMPANY_CODE=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and SYS01_COMPANIES.ENABLED='Y'"
      );
      ResultSet rset = pstmt.executeQuery();
      Hashtable companies = new Hashtable();
      while(rset.next()) {
        vo = new RoleFunctionCompanyVO();
        vo.setCompanyCodeSys01SYS02(rset.getString(1));
        vo.setName_1REG04(rset.getString(2));
        vo.setCanDelSYS02(Boolean.FALSE);
        vo.setCanUpdSYS02(Boolean.FALSE);
        vo.setCanInsSYS02(Boolean.FALSE);
        vo.setCanView(Boolean.FALSE);
        vo.setProgressiveSys04SYS02(progressiveSYS04);
        vo.setFunctionCodeSys06SYS02(functionCodeSYS06);
        list.add(vo);
        companies.put(rset.getString(1),new Integer(list.size()-1));
      }
      rset.close();
      pstmt.close();


      // retrieve all USER companies, associated to the specified role-function...
      pstmt = conn.prepareStatement(
          "select SYS02_COMPANIES_ACCESS.COMPANY_CODE_SYS01,SYS02_COMPANIES_ACCESS.CAN_INS,SYS02_COMPANIES_ACCESS.CAN_UPD,SYS02_COMPANIES_ACCESS.CAN_DEL from "+
          "SYS02_COMPANIES_ACCESS where "+
          "SYS02_COMPANIES_ACCESS.PROGRESSIVE_SYS04=? and "+
          "SYS02_COMPANIES_ACCESS.FUNCTION_CODE_SYS06=?"
      );
      pstmt.setBigDecimal(1,progressiveSYS04);
      pstmt.setString(2,functionCodeSYS06);

      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (RoleFunctionCompanyVO)list.get(((Integer)companies.get(rset.getString(1))).intValue());
        vo.setFunctionCodeSys06SYS02(functionCodeSYS06);
        vo.setProgressiveSys04SYS02(progressiveSYS04);
        vo.setCanInsSYS02(Boolean.valueOf(rset.getString(2).equals("Y")));
        vo.setCanUpdSYS02(Boolean.valueOf(rset.getString(3).equals("Y")));
        vo.setCanDelSYS02(Boolean.valueOf(rset.getString(4).equals("Y")));
        vo.setCanView(Boolean.TRUE);
      }
      rset.close();

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

      return answer;
    } catch (Exception ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }

  }

}
