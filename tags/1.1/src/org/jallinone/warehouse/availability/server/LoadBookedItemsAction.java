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
 * <p>Description: Action class used to fetch item availabilities and book items, eventually for a specific warehouse.</p>
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
public class LoadBookedItemsAction implements Action {


  public LoadBookedItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadBookedItems";
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
          "select WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,sum(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY),WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 "+
          " from WAR03_ITEMS_AVAILABILITY,ITM01_ITEMS,SYS10_TRANSLATIONS,WAR01_WAREHOUSES "+
          " where "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("availableQtyWAR03","sum(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY)");
      attribute2dbField.put("itemCodeItm01DOC02","WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("companyCodeSys01WAR03","WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWar01WAR03","WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("variantTypeItm06DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15");

      ArrayList pars = new ArrayList();
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

      sql += " group by WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
             "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 ";

      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          pars,
          attribute2dbField,
          BookedItemQtyVO.class,
          "Y",
          "N",
          context,
          gridPars,
          50,
          true
      );

      if (res.isError())
        return res;

      // retrieve booked item quantities, for each item found...
      sql =
          "select sum(DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY) "+
          " from DOC02_SELLING_ITEMS,DOC01_SELLING where "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER and "+
          "DOC01_SELLING.DOC_STATE=? ";

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC01_SELLING.WAREHOUSE_CODE_WAR01=? ";
      }

      sql += " group by DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01 ";

      BookedItemQtyVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      pstmt = conn.prepareStatement(sql);
      ResultSet rset = null;
      for(int i=0;i<list.size();i++) {
        vo = (BookedItemQtyVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01WAR03());
        pstmt.setString(2,vo.getItemCodeItm01DOC02());
        pstmt.setString(3,ApplicationConsts.CONFIRMED);

        if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
          pstmt.setString(4,gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE).toString());
        }

        rset = pstmt.executeQuery();
        if (rset.next())
          vo.setBookQtyDOC02(rset.getBigDecimal(1));
        else
          vo.setBookQtyDOC02(new BigDecimal(0));
        rset.close();
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching item availabilities and book items",ex);
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
