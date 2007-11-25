package org.jallinone.sales.documents.activities.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch sale activities applied to a sale document from DOC13 table.</p>
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
public class LoadSaleDocActivitiesBean {


  public LoadSaleDocActivitiesBean() {
  }


  /**
   * Retrive all activities defined for the specified sale document.
   * No commit or rollback are executed. No connection is created or released.
   */
  public final Response loadSaleDocActivities(Connection conn,GridParams gridParams,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocActivitiesBean.loadSaleDocActivities",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        gridParams,
        null
      ));
      String sql =
          "select DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01,DOC13_SELLING_ACTIVITIES.ACTIVITY_CODE_SAL09,"+
          "DOC13_SELLING_ACTIVITIES.VALUE,DOC13_SELLING_ACTIVITIES.VALUE_SAL09,DOC13_SELLING_ACTIVITIES.ACTIVITY_DESCRIPTION,"+
          "DOC13_SELLING_ACTIVITIES.DOC_TYPE,DOC13_SELLING_ACTIVITIES.DOC_YEAR,DOC13_SELLING_ACTIVITIES.DOC_NUMBER, "+
          "DOC13_SELLING_ACTIVITIES.VAT_CODE_SAL09,DOC13_SELLING_ACTIVITIES.VAT_DESCRIPTION,DOC13_SELLING_ACTIVITIES.VAT_VALUE,DOC13_SELLING_ACTIVITIES.VAT_DEDUCTIBLE, "+
          "DOC13_SELLING_ACTIVITIES.DURATION,DOC13_SELLING_ACTIVITIES.PROGRESSIVE_SCH06, "+
          "REG03_CURRENCIES.CURRENCY_SYMBOL,DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03,DOC13_SELLING_ACTIVITIES.INVOICED_VALUE, "+
          "DOC13_SELLING_ACTIVITIES.TAXABLE_INCOME,DOC13_SELLING_ACTIVITIES.VALUE_REG01 "+
          "from DOC13_SELLING_ACTIVITIES,REG03_CURRENCIES where "+
          "DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_TYPE=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_YEAR=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC13","DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSal09DOC13","DOC13_SELLING_ACTIVITIES.ACTIVITY_CODE_SAL09");
      attribute2dbField.put("valueSal09DOC13","DOC13_SELLING_ACTIVITIES.VALUE_SAL09");
      attribute2dbField.put("activityDescriptionDOC13","DOC13_SELLING_ACTIVITIES.ACTIVITY_DESCRIPTION");
      attribute2dbField.put("valueDOC13","DOC13_SELLING_ACTIVITIES.VALUE");
      attribute2dbField.put("docTypeDOC13","DOC13_SELLING_ACTIVITIES.DOC_TYPE");
      attribute2dbField.put("docYearDOC13","DOC13_SELLING_ACTIVITIES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC13","DOC13_SELLING_ACTIVITIES.DOC_NUMBER");
      attribute2dbField.put("vatCodeSal09DOC13","DOC13_SELLING_ACTIVITIES.VAT_CODE_SAL09");
      attribute2dbField.put("vatDescriptionDOC13","DOC13_SELLING_ACTIVITIES.VAT_DESCRIPTION");
      attribute2dbField.put("vatValueDOC13","DOC13_SELLING_ACTIVITIES.VAT_VALUE");
      attribute2dbField.put("vatDeductibleDOC13","DOC13_SELLING_ACTIVITIES.VAT_DEDUCTIBLE");
      attribute2dbField.put("durationDOC13","DOC13_SELLING_ACTIVITIES.DURATION");
      attribute2dbField.put("progressiveSch06DOC13","DOC13_SELLING_ACTIVITIES.PROGRESSIVE_SCH06");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("currencyCodeReg03DOC13","DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03");
      attribute2dbField.put("invoicedValueDOC13","DOC13_SELLING_ACTIVITIES.INVOICED_VALUE");
      attribute2dbField.put("valueReg01DOC13","DOC13_SELLING_ACTIVITIES.VALUE_REG01");
      attribute2dbField.put("taxableIncomeDOC13","DOC13_SELLING_ACTIVITIES.TAXABLE_INCOME");

      ArrayList values = new ArrayList();
      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
      values.add( pk.getCompanyCodeSys01DOC01() );
      values.add( pk.getDocTypeDOC01() );
      values.add( pk.getDocYearDOC01() );
      values.add( pk.getDocNumberDOC01() );

      // read from DOC13 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SaleDocActivityVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );
      if (res.isError())
        return res;

      ArrayList list = ((VOListResponse)res).getRows();
      SaleDocActivityVO vo = null;
      sql =
         "select SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION "+
         "from SCH06_SCHEDULED_ACTIVITIES,REG01_VATS where "+
         "SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01=? and "+
         "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE=?";
      pstmt = conn.prepareStatement(sql);
      ResultSet rset = null;
      for(int i=0;i<list.size();i++) {
        vo = (SaleDocActivityVO)list.get(i);
        if (vo.getProgressiveSch06DOC13()!=null) {
          // retrieve scheduled activity description from SCH06...
          pstmt.setString(1,vo.getCompanyCodeSys01DOC13());
          pstmt.setBigDecimal(2,vo.getProgressiveSch06DOC13());
          rset = pstmt.executeQuery();
          if (rset.next()) {
            vo.setDescriptionSCH06(rset.getString(1));
          }
          rset.close();
        }
      }

      Response answer = res;

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocActivitiesBean.loadSaleDocActivities",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        gridParams,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadSaleDocActivities","Error while fetching sale activities list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }



}
