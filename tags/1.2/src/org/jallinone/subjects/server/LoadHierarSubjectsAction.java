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
 * <p>Description: Action class used to fetch subjects of a specific hierarchy level from REG16 table.</p>
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
public class LoadHierarSubjectsAction implements Action {


  public LoadHierarSubjectsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadHierarSubjects";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {

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

      GridParams pars = (GridParams)inputPar;
      BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);
      String companyCodeSYS01 = (String)pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String subjectType = (String)pars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);
      Boolean loadOnlyCurrentLevel = (Boolean)pars.getOtherGridParams().get(ApplicationConsts.LOAD_ONLY_CURRENT_LEVEL);

      String subjectTypes = null;
      if (subjectType.equals(ApplicationConsts.SUBJECT_EMPLOYEE) ||
          subjectType.equals(ApplicationConsts.SUBJECT_SUPPLIER))
        subjectTypes = "'"+subjectType+"'";
      else if (subjectType.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
          subjectType.equals(ApplicationConsts.SUBJECT_PEOPLE_CONTACT))
        subjectTypes = "'"+ApplicationConsts.SUBJECT_PEOPLE_CONTACT+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT+"'";
      else if (subjectType.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ||
          subjectType.equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER))
        subjectTypes = "'"+ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER+"'";


      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS,REG16_SUBJECTS_LINKS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ("+subjectTypes+") and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=REG16_SUBJECTS_LINKS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=REG16_SUBJECTS_LINKS.PROGRESSIVE_REG04 and "+
          "REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE02=? and "+
          "REG04_SUBJECTS.ENABLED='Y'";


      if (!rootProgressiveHIE01.equals(progressiveHIE01)) {
        // retrieve all subnodes of the specified node...

        if (loadOnlyCurrentLevel!=null && loadOnlyCurrentLevel.booleanValue())
          pstmt = conn.prepareStatement(
              "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
              "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE=? "+
              "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
          );
        else
          pstmt = conn.prepareStatement(
              "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
              "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
              "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
          );
        pstmt.setBigDecimal(1,progressiveHIE02);
        pstmt.setBigDecimal(2,progressiveHIE01);
        ResultSet rset = pstmt.executeQuery();

        HashSet currentLevelNodes = new HashSet();
        HashSet newLevelNodes = new HashSet();
        String nodes = "";
        int currentLevel = -1;
        while(rset.next()) {
          if (currentLevel!=rset.getInt(3)) {
            // next level...
            currentLevel = rset.getInt(3);
            currentLevelNodes = newLevelNodes;
            newLevelNodes = new HashSet();
          }
          if (rset.getBigDecimal(1).equals(progressiveHIE01)) {
            newLevelNodes.add(rset.getBigDecimal(1));
            nodes += rset.getBigDecimal(1)+",";
          }
          else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
            newLevelNodes.add(rset.getBigDecimal(1));
            nodes += rset.getBigDecimal(1)+",";
          }
        }
        rset.close();
        pstmt.close();
        if (nodes.length()>0)
          nodes = nodes.substring(0,nodes.length()-1);
        sql += " and PROGRESSIVE_HIE01 in ("+nodes+")";
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");

      ArrayList values = new ArrayList();
      values.add(companyCodeSYS01);
      values.add(progressiveHIE02);

      // read from REG04,REG16 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SubjectVO.class,
          "Y",
          "N",
          context,
          pars,
          50,
          true
      );


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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching subjects list",ex);
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
