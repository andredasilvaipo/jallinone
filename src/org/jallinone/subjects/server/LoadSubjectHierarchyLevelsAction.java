package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.subjects.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch subject hierarchies + levels from REG16/REG08 table.</p>
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
public class LoadSubjectHierarchyLevelsAction implements Action {


  public LoadSubjectHierarchyLevelsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSubjectHierarchyLevels";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      GridParams gridParams = (GridParams)inputPar;
      SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);
      String subjectType = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);

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
      String sql =
          "select REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION from REG08_SUBJECT_HIERARCHIES,SYS10_TRANSLATIONS where "+
          "REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01=? and SUBJECT_TYPE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG16","REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveHie02REG16","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02");
      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(subjectType);

      // read from REG08 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SubjectHierarchyLevelVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );
      if (res.isError())
        return res;

      ArrayList rows = ((VOListResponse)res).getRows();
      Hashtable hash = new Hashtable();
      SubjectHierarchyLevelVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (SubjectHierarchyLevelVO)rows.get(i);
        vo.setProgressiveReg04REG16(pk.getProgressiveREG04());
        hash.put(
            vo.getProgressiveHie02REG16(),
            vo
        );
      }

      sql =
          "select SYS10_TRANSLATIONS.DESCRIPTION,REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE02,REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE01 "+
          "from REG16_SUBJECTS_LINKS,SYS10_TRANSLATIONS "+
          "where "+
          "REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE01=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG16_SUBJECTS_LINKS.COMPANY_CODE_SYS01=? and REG16_SUBJECTS_LINKS.PROGRESSIVE_REG04=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,serverLanguageId);
      pstmt.setString(2,pk.getCompanyCodeSys01REG04());
      pstmt.setBigDecimal(3,pk.getProgressiveREG04());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (SubjectHierarchyLevelVO)hash.get(rset.getBigDecimal(2));
        if (vo!=null) {
            vo.setLevelDescriptionSYS10(rset.getString(1));
            vo.setProgressiveHie01REG16(rset.getBigDecimal(3));
        }
      }
      rset.close();

      Response answer = res;

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
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching subject hierarchy levels list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
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
