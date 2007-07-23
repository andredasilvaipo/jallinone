package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch level properties from DOC21 table.</p>
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
public class LoadLevelPropertiesAction implements Action {


  public LoadLevelPropertiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadLevelProperties";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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

      HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
      if (vo!=null) {
        progressiveHIE01 = vo.getProgressiveHIE01();
        progressiveHIE02 = vo.getProgressiveHie02HIE01();
      }

      // retrieve companies list...
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC16");
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02,DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE "+
          " from DOC21_LEVEL_PROPERTIES,SYS10_TRANSLATIONS where "+
          "DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02=? and "+
          "DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01 in ("+companies+") ";

      if (pars.getOtherGridParams().get(ApplicationConsts.LOAD_ANCIENTS)!=null &&
          ((Boolean)pars.getOtherGridParams().get(ApplicationConsts.LOAD_ANCIENTS)).booleanValue() &&
          progressiveHIE02!=null &&
          progressiveHIE01!=null) {

        // retrieve all subnodes of the specified node...
        pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01 from HIE01_LEVELS "+
          "where HIE01_LEVELS.PROGRESSIVE<=? and PROGRESSIVE_HIE02=?"
        );
        pstmt.setBigDecimal(1,progressiveHIE01);
        pstmt.setBigDecimal(2,progressiveHIE02);
        ResultSet rset = pstmt.executeQuery();
        Hashtable parents = new Hashtable();
        BigDecimal parentProgressive = null;
        while(rset.next()) {
          parentProgressive = rset.getBigDecimal(2);
          if (parentProgressive!=null)
            parents.put(rset.getBigDecimal(1),parentProgressive);
        }
        rset.close();
        pstmt.close();

        String nodes = "";
        parentProgressive = progressiveHIE01;
        while(parentProgressive!=null) {
          nodes += parentProgressive+",";
          parentProgressive = (BigDecimal)parents.get(parentProgressive);
        }
        if (nodes.length()>0)
          nodes = nodes.substring(0,nodes.length()-1);
        sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01 in ("+nodes+")";

      }
      else if (progressiveHIE01!=null) {
        // retrieve all subnodes of the specified node...
        sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01="+progressiveHIE01;
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC21","DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSys10DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveHie01DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02");
      attribute2dbField.put("propertyTypeDOC21","DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(progressiveHIE02);

      // read from DOC21 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          LevelPropertyVO.class,
          "Y",
          "N",
          context,
          pars,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching level properties list",ex);
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
