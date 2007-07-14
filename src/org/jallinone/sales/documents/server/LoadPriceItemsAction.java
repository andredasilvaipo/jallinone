package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.sales.documents.java.PriceItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch items + price from SAL01/SAL02 tables.</p>
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
public class LoadPriceItemsAction implements Action {


  public LoadPriceItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPriceItems";
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
      String companyCodeSYS01 = (String)pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String pricelistCodeSAL01 = (String)pars.getOtherGridParams().get(ApplicationConsts.PRICELIST);

      HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
      if (vo!=null) {
        progressiveHIE01 = vo.getProgressiveHIE01();
        progressiveHIE02 = vo.getProgressiveHie02HIE01();
      }

      String sql =
          "select SAL02_PRICES.COMPANY_CODE_SYS01,SAL02_PRICES.ITEM_CODE_ITM01,"+
          "ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.PROGRESSIVE_HIE01,ITM01_ITEMS.MIN_SELLING_QTY,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,REG02_MEASURE_UNITS.DECIMALS,"+
          "ITM01_ITEMS.VAT_CODE_REG01,SYS10_VAT.DESCRIPTION,REG01_VATS.DEDUCTIBLE,REG01_VATS.VALUE,"+
          "SAL02_PRICES.VALUE,SAL02_PRICES.START_DATE,SAL02_PRICES.END_DATE,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED "+
          " from SAL02_PRICES,SYS10_TRANSLATIONS,ITM01_ITEMS,REG02_MEASURE_UNITS,SYS10_TRANSLATIONS SYS10_VAT,REG01_VATS where "+
          "ITM01_ITEMS.PROGRESSIVE_HIE02=? and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_MEASURE_UNITS.UM_CODE and "+
          "SAL02_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "SAL02_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL02_PRICES.COMPANY_CODE_SYS01 = ? and "+
          "ITM01_ITEMS.VAT_CODE_REG01=REG01_VATS.VAT_CODE and "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_VAT.PROGRESSIVE and "+
          "SYS10_VAT.LANGUAGE_CODE=? and "+
          "SAL02_PRICES.PRICELIST_CODE_SAL01=? and "+
          "SAL02_PRICES.START_DATE<=? and "+
          "SAL02_PRICES.END_DATE>? ";

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
          nodes = nodes.substring(0, nodes.length() - 1);
        if (rootProgressiveHIE01!=null || nodes.length()>0)
          sql += " and ITM01_ITEMS.PROGRESSIVE_HIE01 in (" + nodes +")";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL02","SAL02_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01SAL02","SAL02_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("minSellingQtyITM01","ITM01_ITEMS.MIN_SELLING_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("decimalsREG02","REG02_MEASURE_UNITS.DECIMALS");

      attribute2dbField.put("vatCodeReg01ITM01","ITM01_ITEMS.VAT_CODE_REG01");
      attribute2dbField.put("vatDescriptionSYS10","SYS10_VAT.DESCRIPTION");
      attribute2dbField.put("deductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("valueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("valueSAL02","SAL02_PRICES.VALUE");
      attribute2dbField.put("startDateSAL02","SAL02_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL02","SAL02_PRICES.END_DATE");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");

      ArrayList values = new ArrayList();
      values.add(progressiveHIE02);
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      values.add(serverLanguageId);
      values.add(pricelistCodeSAL01);
      values.add(new java.sql.Date(System.currentTimeMillis()));
      values.add(new java.sql.Date(System.currentTimeMillis()));

      // read from SAL02 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          PriceItemVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching items+prices list",ex);
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
