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
 * <p>Description: Action class used to fetch documents from DOC14 table.</p>
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
public class LoadDocumentsAction implements Action {


  public LoadDocumentsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadDocuments";
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
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC14");
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select DOC14_DOCUMENTS.COMPANY_CODE_SYS01,DOC14_DOCUMENTS.PROGRESSIVE,DOC14_DOCUMENTS.DESCRIPTION,"+
          "HIE01_LEVELS.PROGRESSIVE_HIE02,DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01"+
          " from DOC14_DOCUMENTS,DOC17_DOCUMENT_LINKS,HIE01_LEVELS where "+
          "DOC14_DOCUMENTS.COMPANY_CODE_SYS01=DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01 and "+
          "DOC14_DOCUMENTS.PROGRESSIVE=DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14 and "+
          "DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01=HIE01_LEVELS.PROGRESSIVE and "+
          "HIE01_LEVELS.PROGRESSIVE_HIE02=? and "+
          "DOC14_DOCUMENTS.COMPANY_CODE_SYS01 in ("+companies+") ";


      if (rootProgressiveHIE01==null || !rootProgressiveHIE01.equals(progressiveHIE01)) {
        // retrieve all subnodes of the specified node...
        pstmt = conn.prepareStatement(
            "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEVEL from HIE01_LEVELS "+
            "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
            "order by LEVEL,PROGRESSIVE_HIE01,PROGRESSIVE"
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
        sql += " and DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01 in ("+nodes+")";
      }

      ArrayList values = new ArrayList();
      values.add(progressiveHIE02);

      if (pars.getOtherGridParams().get(ApplicationConsts.PROPERTIES_FILTER)!=null) {
        Hashtable filters = (Hashtable)pars.getOtherGridParams().get(ApplicationConsts.PROPERTIES_FILTER);
        if (filters.size()>0) {
          // apply doc. property filters...
          sql +=
              "and DOC14_DOCUMENTS.PROGRESSIVE in (select DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14 "+
              "from DOC20_DOC_PROPERTIES where ";
//              "DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 ";
          Enumeration en = filters.keys();
          BigDecimal progressive = null;
          Object value = null;
          while(en.hasMoreElements()) {
            progressive = (BigDecimal)en.nextElement();
            value = filters.get(progressive);
            sql += "DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10="+progressive+" and ";
            if (value instanceof String)
              sql += "DOC20_DOC_PROPERTIES.TEXT_VALUE='"+value+"' or ";
            else if (value instanceof Timestamp) {
              sql += "DOC20_DOC_PROPERTIES.DATE_VALUE=? or ";
              values.add(value);
            } else if (value instanceof Number)
              sql += "DOC20_DOC_PROPERTIES.NUM_VALUE="+value+" or ";
          }
          sql = sql.substring(0,sql.length()-3); // remove the last "or"...
          sql += ")";
        }
      }



      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC14","DOC14_DOCUMENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDOC14","DOC14_DOCUMENTS.PROGRESSIVE");
      attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
      attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01");

      // read from DOC14 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridDocumentVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching documents list",ex);
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
