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
import org.jallinone.purchases.items.java.CreateABCFilterVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.purchases.items.java.ABCVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load ABC classification previously created.</p>
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
public class LoadABCAction implements Action {



  public LoadABCAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadABC";
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
      ArrayList mask = (ArrayList)gridParams.getOtherGridParams().get(ApplicationConsts.FILTER_VO);
      BigDecimal reportId = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.REPORT_ID);

      if (mask.size()==0 || reportId==null)
        return new VOListResponse(new ArrayList(),false,0);


      // load from TMP03...
      String sql =
        "SELECT T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
        "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
        "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
        "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
        "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
        "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
        "T.DESCRIPTION,"+
        "T1.QTY,T2.QTY,"+
        "T1.UNSOLD_GRADE,T2.INVOICED_GRADE,ITM23.MIN_STOCK "+
        "FROM SYS10_TRANSLATIONS T,ITM01_ITEMS I,TMP03_ABC T1 LEFT OUTER JOIN ("+
        " SELECT QTY,INVOICED_GRADE,REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
        " VARIANT_TYPE_ITM06,"+
        " VARIANT_CODE_ITM11,"+
        " VARIANT_TYPE_ITM07,"+
        " VARIANT_CODE_ITM12,"+
        " VARIANT_TYPE_ITM08,"+
        " VARIANT_CODE_ITM13,"+
        " VARIANT_TYPE_ITM09,"+
        " VARIANT_CODE_ITM14,"+
        " VARIANT_TYPE_ITM10,"+
        " VARIANT_CODE_ITM15 "+
        " FROM TMP03_ABC WHERE TMP03_ABC.QTY_TYPE=? "+
        ") T2 ON "+
        "T1.REPORT_ID=T2.REPORT_ID AND "+
        "T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
        "T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
        "T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
        "T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
        "T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
        "T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
        "T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
        "T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
        "T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
        "T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
        "T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
        "T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 "+
        "LEFT OUTER JOIN ITM23_VARIANT_MIN_STOCKS ITM23 ON "+
        " T1.COMPANY_CODE_SYS01=ITM23.COMPANY_CODE_SYS01 AND "+
        " T1.ITEM_CODE_ITM01=ITM23.ITEM_CODE_ITM01 AND "+
        " T1.VARIANT_TYPE_ITM06=ITM23.VARIANT_TYPE_ITM06 AND "+
        " T1.VARIANT_CODE_ITM11=ITM23.VARIANT_CODE_ITM11 AND "+
        " T1.VARIANT_TYPE_ITM07=ITM23.VARIANT_TYPE_ITM07 AND "+
        " T1.VARIANT_CODE_ITM12=ITM23.VARIANT_CODE_ITM12 AND "+
        " T1.VARIANT_TYPE_ITM08=ITM23.VARIANT_TYPE_ITM08 AND "+
        " T1.VARIANT_CODE_ITM13=ITM23.VARIANT_CODE_ITM13 AND "+
        " T1.VARIANT_TYPE_ITM09=ITM23.VARIANT_TYPE_ITM09 AND "+
        " T1.VARIANT_CODE_ITM14=ITM23.VARIANT_CODE_ITM14 AND "+
        " T1.VARIANT_TYPE_ITM10=ITM23.VARIANT_TYPE_ITM10 AND "+
        " T1.VARIANT_CODE_ITM15=ITM23.VARIANT_CODE_ITM15 "+
        "WHERE T1.REPORT_ID=? AND "+
        "T1.QTY_TYPE=? AND "+
        "T1.COMPANY_CODE_SYS01=I.COMPANY_CODE_SYS01 AND "+
        "T1.ITEM_CODE_ITM01=I.ITEM_CODE AND "+
        "I.PROGRESSIVE_SYS10=T.PROGRESSIVE AND "+
        "T.LANGUAGE_CODE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01","T1.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCode","T1.ITEM_CODE_ITM01");
      attribute2dbField.put("itemDescription","T.DESCRIPTION");
//      attribute2dbField.put("minStockITM23","SYS23_VARIANT_MIN_STOCKS.MIN_STOCK");
      attribute2dbField.put("unsold","T1.QTY");
      attribute2dbField.put("sold","T2.QTY");
      attribute2dbField.put("unsoldGrade","T1.UNSOLD_GRADE");
      attribute2dbField.put("invoicedGrade","T2.INVOICED_GRADE");

      attribute2dbField.put("variantTypeITM06","T1.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeITM11","T1.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeITM07","T1.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeITM12","T1.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeITM08","T1.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeITM13","T1.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeITM09","T1.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeITM14","T1.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeITM10","T1.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeITM15","T1.VARIANT_CODE_ITM15");

      attribute2dbField.put("minStockITM23","ITM23.MIN_STOCK");

      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.ABC_TYPE_SOLD_QTY);
      values.add(reportId);
      values.add(ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      values.add(serverLanguageId);

      if (mask.size()<9) {
        sql += " AND (";

        String pattern = null;
        for(int i=0;i<mask.size();i++) {
          pattern = mask.get(i).toString();
          if (pattern.substring(1).equals("C"))
            sql += "T1.UNSOLD_GRADE=? AND (T2.INVOICED_GRADE=? OR T2.INVOICED_GRADE is null) OR ";
          else
            sql += "T1.UNSOLD_GRADE=? AND T2.INVOICED_GRADE=? OR ";

          values.add(pattern.substring(0,1));
          values.add(pattern.substring(1));
        }
        sql = sql.substring(0,sql.length()-3)+")";
      }

      // read from PUR02 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ABCVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
        res
      ));

      return res;
    }
    catch (Throwable ex) {
      try {
        conn.rollback();
      }
      catch (Exception ex2) {
      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while loading ABC classification",ex);
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
