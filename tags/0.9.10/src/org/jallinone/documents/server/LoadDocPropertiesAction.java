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
 * <p>Description: Action class used to fetch document properties from DOC20/DOC21 tables.</p>
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
public class LoadDocPropertiesAction implements Action {

  private LoadDocumentLinksAction links = new LoadDocumentLinksAction();


  public LoadDocPropertiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadDocProperties";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    String sql = null;

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


      DocumentPK pk = (DocumentPK)inputPar;

      // retrieve document links...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DOCUMENT_PK,pk);
      Response res = links.executeCommand(pars,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;

      // for each document link retrieve ancient progressiveHIE01s...
      ArrayList linkVOs = ((VOListResponse)res).getRows();
      DocumentLinkVO linkVO = null;
      pstmt = conn.prepareStatement(
        "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01 from HIE01_LEVELS "+
        "where HIE01_LEVELS.PROGRESSIVE<=? and PROGRESSIVE_HIE02=?"
      );
      Hashtable parents = new Hashtable();
      HashSet progressiveHIE01s = new HashSet();
      BigDecimal progressiveHIE01 = null;
      for(int i=0;i<linkVOs.size();i++) {
        linkVO = (DocumentLinkVO)linkVOs.get(i);
        parents.clear();
        pstmt.setBigDecimal(1,linkVO.getProgressiveHie01DOC17());
        pstmt.setBigDecimal(2,linkVO.getProgressiveHIE02());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          progressiveHIE01 = rset.getBigDecimal(2);
          if (progressiveHIE01!=null)
            parents.put(rset.getBigDecimal(1),progressiveHIE01);
        }
        rset.close();

        // fill in the hashset "progressiveHIE01s"...
        progressiveHIE01 = linkVO.getProgressiveHie01DOC17();
        while(progressiveHIE01!=null) {
          progressiveHIE01s.add(progressiveHIE01);
          progressiveHIE01 = (BigDecimal)parents.get(progressiveHIE01);
        }
      }
      pstmt.close();

      sql =
          "select DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02,DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE, "+
          "DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14,DOC20_DOC_PROPERTIES.TEXT_VALUE,DOC20_DOC_PROPERTIES.NUM_VALUE,DOC20_DOC_PROPERTIES.DATE_VALUE "+
          " from SYS10_TRANSLATIONS,DOC21_LEVEL_PROPERTIES LEFT OUTER JOIN "+
          "(select DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01,DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14,DOC20_DOC_PROPERTIES.TEXT_VALUE,"+
          "DOC20_DOC_PROPERTIES.NUM_VALUE,DOC20_DOC_PROPERTIES.DATE_VALUE,DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10 "+
          "from DOC20_DOC_PROPERTIES where DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14=?) DOC20_DOC_PROPERTIES ON "+
          "DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01=DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01 and "+
          "DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10=DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10 "+
          " where "+
          "DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01=? ";

      // append to SQL the filter on progressiveHIE0xs...
      sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01 in ";
      Iterator it = progressiveHIE01s.iterator();
      String where = "";
      while(it.hasNext())
        where += it.next()+",";
      if (progressiveHIE01s.size()>0)
        where = sql.substring(0,sql.length()-1);
      if (where.length()>0)
        sql += "("+where+")";
      else
        sql += "(-1)";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC20","DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("propertyTypeDOC21","DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE");
      attribute2dbField.put("progressiveSys10DOC20","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("progressiveHie01DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveDoc14DOC20","DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14");
      attribute2dbField.put("textValueDOC20","DOC20_DOC_PROPERTIES.TEXT_VALUE");
      attribute2dbField.put("numValueDOC20","DOC20_DOC_PROPERTIES.NUM_VALUE");
      attribute2dbField.put("dateValueDOC20","DOC20_DOC_PROPERTIES.DATE_VALUE");


      ArrayList values = new ArrayList();
      values.add(pk.getProgressiveDOC14());
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC14());

      // read from DOC20 table...
      res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DocPropertyVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true
      );

      if (!res.isError()) {
        ArrayList rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++)
          ((DocPropertyVO)rows.get(i)).setProgressiveDoc14DOC20(pk.getProgressiveDOC14());
      }

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching document properties list",ex);
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
