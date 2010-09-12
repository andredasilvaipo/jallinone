package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch prices from SAL02 table.</p>
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
public class LoadPricesAction implements Action {


  public LoadPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPrices";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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

      GridParams gridParams = (GridParams)inputPar;

      String companyCodeSYS01 = null;
      String sql = null;


      if (gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST)!=null) {
        PricelistVO vo = (PricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
        companyCodeSYS01 = vo.getCompanyCodeSys01SAL01();

        sql =
            "select SAL02_PRICES.COMPANY_CODE_SYS01,SAL02_PRICES.PRICELIST_CODE_SAL01,SAL02_PRICES.ITEM_CODE_ITM01,SAL02_PRICES.VALUE,SAL02_PRICES.START_DATE,SAL02_PRICES.END_DATE,A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5 "+
            " from SAL02_PRICES,SYS10_TRANSLATIONS A,ITM01_ITEMS where "+
            "SAL02_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL02_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
            "A.LANGUAGE_CODE=? and SAL02_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' and "+
            "SAL02_PRICES.PRICELIST_CODE_SAL01='"+vo.getPricelistCodeSAL01()+"'";
      }
      else {
        DetailItemVO vo = (DetailItemVO)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM);
        companyCodeSYS01 = vo.getCompanyCodeSys01ITM01();

        sql =
            "select SAL02_PRICES.COMPANY_CODE_SYS01,SAL02_PRICES.PRICELIST_CODE_SAL01,SAL02_PRICES.ITEM_CODE_ITM01,SAL02_PRICES.VALUE,SAL02_PRICES.START_DATE,SAL02_PRICES.END_DATE,B.DESCRIPTION,"+
            "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5 "+
            " from SAL02_PRICES,SYS10_TRANSLATIONS B,SAL01_PRICELISTS,ITM01_ITEMS where "+
            "SAL02_PRICES.COMPANY_CODE_SYS01=SAL01_PRICELISTS.COMPANY_CODE_SYS01 and "+
            "SAL02_PRICES.PRICELIST_CODE_SAL01=SAL01_PRICELISTS.PRICELIST_CODE and "+
            "SAL01_PRICELISTS.PROGRESSIVE_SYS10=B.PROGRESSIVE and "+
            "B.LANGUAGE_CODE=? and SAL02_PRICES.COMPANY_CODE_SYS01=? and "+
            "SAL02_PRICES.ITEM_CODE_ITM01='"+vo.getItemCodeITM01()+"' and "+
            "SAL02_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL02_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE";
      }

      java.sql.Date filterDate = null;
      if (gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
        filterDate = new java.sql.Date( ((java.util.Date)gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)).getTime() );
        sql += " and START_DATE<=? and END_DATE>=?";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL02","SAL02_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL02","SAL02_PRICES.PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL02","SAL02_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL02","SAL02_PRICES.VALUE");
      attribute2dbField.put("startDateSAL02","SAL02_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL02","SAL02_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      if (filterDate!=null) {
        values.add(filterDate);
        values.add(filterDate);
      }

      // read from SAL02 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          PriceVO.class,
          "Y",
          "N",
          context,
          gridParams,
          50,
          true
      );

      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        PriceVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (PriceVO)rows.get(i);
          if (gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST)!=null) {
            PricelistVO parentVO = (PricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
            vo.setPricelistDescriptionSYS10(parentVO.getDescriptionSYS10());
          }
          else {
            DetailItemVO parentVO = (DetailItemVO)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM);
            vo.setItemDescriptionSYS10(parentVO.getDescriptionSYS10());
            vo.setProgressiveHie02ITM01(parentVO.getProgressiveHie02ITM01());
          }
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching prices list",ex);
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
