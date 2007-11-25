package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.pricelist.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch supplier item prices from PUR04 table.</p>
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
public class LoadSupplierPricesAction implements Action {


  public LoadSupplierPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSupplierPrices";
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

      SupplierPricelistVO vo = (SupplierPricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
      if (vo!=null)
        companyCodeSYS01 = vo.getCompanyCodeSys01PUR03();
      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
      if (pk!=null)
        companyCodeSYS01 = pk.getCompanyCodeSys01ITM01();


      sql =
          "select PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01,PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03,PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04,PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01,PUR04_SUPPLIER_PRICES.VALUE,PUR04_SUPPLIER_PRICES.START_DATE,PUR04_SUPPLIER_PRICES.END_DATE,A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,PUR01_SUPPLIERS.SUPPLIER_CODE,B.DESCRIPTION "+
          " from PUR04_SUPPLIER_PRICES,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,ITM01_ITEMS,REG04_SUBJECTS,PUR01_SUPPLIERS,PUR03_SUPPLIER_PRICELISTS where "+
          "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 and "+
          "PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE and "+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? and "+
          "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' ";

      if (vo!=null) {
        sql +=
            " and PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03='" +vo.getPricelistCodePUR03() + "' "+
            " and PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03();
      }
      if (pk!=null)
        sql += " and ITM01_ITEMS.ITEM_CODE='"+pk.getItemCodeITM01()+"' ";

      java.sql.Date filterDate = null;
      if (gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
        filterDate = new java.sql.Date( ((java.util.Date)gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)).getTime() );
        sql += " and START_DATE<=? and END_DATE>=?";
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR04","PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePur03PUR04","PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03");
      attribute2dbField.put("progressiveReg04PUR04","PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04");
      attribute2dbField.put("itemCodeItm01PUR04","PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR04","PUR04_SUPPLIER_PRICES.VALUE");
      attribute2dbField.put("startDatePUR04","PUR04_SUPPLIER_PRICES.START_DATE");
      attribute2dbField.put("endDatePUR04","PUR04_SUPPLIER_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      if (filterDate!=null) {
        values.add(filterDate);
        values.add(filterDate);
      }

      // read from PUR04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SupplierPriceVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching supplier item prices list",ex);
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
