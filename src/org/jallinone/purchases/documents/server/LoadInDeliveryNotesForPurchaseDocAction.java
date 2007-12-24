package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.purchases.documents.invoices.java.InDeliveryNotesVO;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch in delivery notes from DOC08 table,
 * filtered by the specified purchase document.</p>
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
public class LoadInDeliveryNotesForPurchaseDocAction implements Action {


  public LoadInDeliveryNotesForPurchaseDocAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadInDeliveryNotesForPurchaseDoc";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      // retrieve companies list...
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC08_IN");
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.DESTINATION_CODE_REG18,DOC08_DELIVERY_NOTES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE "+
          " from DOC08_DELIVERY_NOTES where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
          "DOC08_DELIVERY_NOTES.DOC_STATE=? and "+
          "(DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER) "+
          " in (select DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
          " from DOC09_IN_DELIVERY_NOTE_ITEMS where "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06=? ";

      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)pars.getOtherGridParams().get(ApplicationConsts.PURCHASE_DOC_VO); // invoice document...
      if (docVO.getDocNumberDOC06()==null)
        sql += " and DOC09_IN_DELIVERY_NOTE_ITEMS.QTY-DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY>0)";
      else
        sql += ")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("destinationCodeReg18DOC08","DOC08_DELIVERY_NOTES.DESTINATION_CODE_REG18");
      attribute2dbField.put("descriptionDOC08","DOC08_DELIVERY_NOTES.DESCRIPTION");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");


      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
      values.add(ApplicationConsts.CLOSED);
      values.add(docVO.getCompanyCodeSys01DOC06());
      values.add(docVO.getDocTypeDoc06DOC06());
      values.add(docVO.getDocYearDoc06DOC06());
      values.add(docVO.getDocNumberDoc06DOC06());


      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          InDeliveryNotesVO.class,
          "Y",
          "N",
          context,
          pars,
          true
      );

      if (res.isError())
        return res;

      // check if the invoice document has been already created and there exists delivery notes linked to it...
      if (docVO.getDocNumberDOC06()!=null) {
        pstmt = conn.prepareStatement(
            "select DOC08_DELIVERY_NOTES.DOC_NUMBER from DOC08_DELIVERY_NOTES where "+
            "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
            "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
            "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
            "DOC08_DELIVERY_NOTES.DOC_STATE=? and "+
            "(DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER) "+
            " in (select DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
            " from DOC09_IN_DELIVERY_NOTE_ITEMS,DOC07_PURCHASE_ITEMS where "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01 and "+
            " DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
            " DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
            " DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01 and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.QTY=DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY"+
            ")"
        );

        pstmt.setString(1,ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
        pstmt.setString(2,ApplicationConsts.CLOSED);
        pstmt.setString(3,docVO.getCompanyCodeSys01DOC06());
        pstmt.setString(4,docVO.getDocTypeDoc06DOC06());
        pstmt.setBigDecimal(5,docVO.getDocYearDoc06DOC06());
        pstmt.setBigDecimal(6,docVO.getDocNumberDoc06DOC06());
        pstmt.setString(7,docVO.getDocTypeDOC06());
        pstmt.setBigDecimal(8,docVO.getDocYearDOC06());
        pstmt.setBigDecimal(9,docVO.getDocNumberDOC06());

        HashSet docNumberDOC08s = new HashSet();
        ResultSet rset = pstmt.executeQuery();
        while(rset.next())
          docNumberDOC08s.add(rset.getBigDecimal(1));
        rset.close();

        ArrayList rows = ((VOListResponse)res).getRows();
        InDeliveryNotesVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (InDeliveryNotesVO)rows.get(i);
          if (docNumberDOC08s.contains(vo.getDocNumberDOC08()))
            vo.setSelected(Boolean.TRUE);
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching in delivery notes list, related to the specified purchase document",ex);
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
