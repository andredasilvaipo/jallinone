package org.jallinone.warehouse.availability.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.availability.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch item availabilities for a specific warehouse and position (optionally) from WAR03 table.</p>
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
public class LoadItemAvailabilitiesAction implements Action {


  public LoadItemAvailabilitiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadItemAvailabilities";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;

    Connection conn = null;
    Statement stmt = null;
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

      GridParams gridPars = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,SYS10_LOC.DESCRIPTION,"+
          "SYS10_ITM01.DESCRIPTION,WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY,WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY,WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01, "+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 "+
          "from "+
          "WAR03_ITEMS_AVAILABILITY,SYS10_TRANSLATIONS SYS10_ITM01,SYS10_TRANSLATIONS SYS10_LOC,ITM01_ITEMS where "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? and "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
          "SYS10_LOC.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR03","WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01WAR03","WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01");
      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
      attribute2dbField.put("descriptionSYS10","SYS10_ITM01.DESCRIPTION");
      attribute2dbField.put("availableQtyWAR03","WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY");
      attribute2dbField.put("damagedQtyWAR03","WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY");
      attribute2dbField.put("progressiveHie01WAR03","WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01");

      attribute2dbField.put("variantTypeItm06WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15");


      ArrayList pars = new ArrayList();
      pars.add( serverLanguageId );
      pars.add( serverLanguageId );

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? ";

        pars.add( gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK)!=null) {
        ItemPK pk = (ItemPK)gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
        sql += " and WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? "+
               " and WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=? ";

        pars.add( pk.getCompanyCodeSys01ITM01() );
        pars.add( pk.getItemCodeITM01() );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01)!=null &&
          gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02)!=null) {

        // retrieve all subnodes of the specified node...
        pstmt = conn.prepareStatement(
            "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
            "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
            "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
        );

        BigDecimal progressiveHIE01 = (BigDecimal)gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
        BigDecimal progressiveHIE02 = (BigDecimal)gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

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

        sql += " and WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01 in ("+nodes+")";

      }

      Response answer = null;
      if (gridPars.getOtherGridParams().get(ApplicationConsts.LOAD_ALL)!=null &&
          ((Boolean)gridPars.getOtherGridParams().get(ApplicationConsts.LOAD_ALL)).booleanValue())
        answer = QueryUtil.getQuery(
            conn,
            userSessionPars,
            sql,
            pars,
            attribute2dbField,
            ItemAvailabilityVO.class,
            "Y",
            "N",
            context,
            gridPars,
            true
        );
      else
        answer = QueryUtil.getQuery(
            conn,
            userSessionPars,
            sql,
            pars,
            attribute2dbField,
            ItemAvailabilityVO.class,
            "Y",
            "N",
            context,
            gridPars,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching item availabilities",ex);
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
