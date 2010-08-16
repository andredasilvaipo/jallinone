package org.jallinone.purchases.items.server;

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
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch supplier items from PUR02 table.</p>
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
public class LoadSupplierItemsAction implements Action {

  private MeasureConvBean convBean = new  MeasureConvBean();


  public LoadSupplierItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSupplierItems";
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
      BigDecimal progressiveREG04 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04);
      String companyCodeSYS01 = (String)pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);

      HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
      if (vo!=null) {
        progressiveHIE01 = vo.getProgressiveHIE01();
        progressiveHIE02 = vo.getProgressiveHie02HIE01();
      }

      String sql =
          "select PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01,PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01,PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04,"+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01,PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY,PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY,"+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02,PUR02_SUPPLIER_ITEMS.ENABLED,SYS10_TRANSLATIONS.DESCRIPTION,REG02_ALIAS1.DECIMALS,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS2.DECIMALS,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED "+
          " from PUR02_SUPPLIER_ITEMS,SYS10_TRANSLATIONS,ITM01_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2 where "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02=? and "+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 = ? and "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=? and "+
          "PUR02_SUPPLIER_ITEMS.ENABLED='Y' and "+
          "ITM01_ITEMS.MIN_SELlING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE ";
      if (Boolean.TRUE.equals(pars.getOtherGridParams().get(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS)))
          sql +=
            " and ITM01_ITEMS.USE_VARIANT_1='N' "+
            " and ITM01_ITEMS.USE_VARIANT_2='N' "+
            " and ITM01_ITEMS.USE_VARIANT_3='N' "+
            " and ITM01_ITEMS.USE_VARIANT_4='N' "+
            " and ITM01_ITEMS.USE_VARIANT_5='N' ";

      if (rootProgressiveHIE01==null || !rootProgressiveHIE01.equals(progressiveHIE01)) {
        // retrieve all subnodes of the specified node...
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
        sql += " and PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01 in ("+nodes+")";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","PUR02_SUPPLIER_ITEMS.ENABLED");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("minSellingQtyDecimalsReg02ITM01","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");

      ArrayList values = new ArrayList();
      values.add(progressiveHIE02);
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      values.add(progressiveREG04);

      // read from PUR02 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SupplierItemVO.class,
          "Y",
          "N",
          context,
          pars,
          50,
          true,
          ApplicationConsts.ID_SUPPLIER_ITEMS_GRID // window identifier...
      );


      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        SupplierItemVO supplierVO = null;
        for(int i=0;i<rows.size();i++) {
          supplierVO = (SupplierItemVO)rows.get(i);
          supplierVO.setValueREG05(convBean.getConversion(
            supplierVO.getMinSellingQtyUmCodeReg02ITM01(),
            supplierVO.getUmCodeReg02PUR02(),
            userSessionPars,
            request,
            response,
            userSession,
            context
          ));
        }
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching supplier items list",ex);
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
