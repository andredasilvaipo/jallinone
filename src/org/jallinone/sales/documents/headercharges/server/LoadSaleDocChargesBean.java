package org.jallinone.sales.documents.headercharges.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch charges applied to a sale document from DOC03 table.</p>
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
public class LoadSaleDocChargesBean {


  public LoadSaleDocChargesBean() {
  }


  /**
   * Retrieve all charges defined for a specified sale document.
   * No commit or rollback are executed. No connection is created or released.
   */
  public final Response loadSaleDocCharges(Connection conn,GridParams gridParams,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocChargesBean.loadSaleDocCharges",
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
          "select DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01,DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06,"+
          "DOC03_SELLING_CHARGES.VALUE,DOC03_SELLING_CHARGES.VALUE_SAL06,DOC03_SELLING_CHARGES.PERC,"+
          "DOC03_SELLING_CHARGES.PERC_SAL06,DOC03_SELLING_CHARGES.CHARGE_DESCRIPTION,"+
          "DOC03_SELLING_CHARGES.DOC_TYPE,DOC03_SELLING_CHARGES.DOC_YEAR,DOC03_SELLING_CHARGES.DOC_NUMBER, "+
          "DOC03_SELLING_CHARGES.VAT_CODE_SAL06,DOC03_SELLING_CHARGES.VAT_DESCRIPTION,DOC03_SELLING_CHARGES.VAT_VALUE,"+
          "DOC03_SELLING_CHARGES.VAT_DEDUCTIBLE,DOC03_SELLING_CHARGES.INVOICED_VALUE, "+
          "DOC03_SELLING_CHARGES.VALUE_REG01,DOC03_SELLING_CHARGES.TAXABLE_INCOME "+
          "from DOC03_SELLING_CHARGES where "+
          "DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01=? and "+
          "DOC03_SELLING_CHARGES.DOC_TYPE=? and "+
          "DOC03_SELLING_CHARGES.DOC_YEAR=? and "+
          "DOC03_SELLING_CHARGES.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC03","DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSal06DOC03","DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06");
      attribute2dbField.put("valueSal06DOC03","DOC03_SELLING_CHARGES.VALUE_SAL06");
      attribute2dbField.put("percSal06DOC03","DOC03_SELLING_CHARGES.PERC_SAL06");
      attribute2dbField.put("chargeDescriptionDOC03","DOC03_SELLING_CHARGES.CHARGE_DESCRIPTION");
      attribute2dbField.put("valueDOC03","DOC03_SELLING_CHARGES.VALUE");
      attribute2dbField.put("percDOC03","DOC03_SELLING_CHARGES.PERC");
      attribute2dbField.put("docTypeDOC03","DOC03_SELLING_CHARGES.DOC_TYPE");
      attribute2dbField.put("docYearDOC03","DOC03_SELLING_CHARGES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC03","DOC03_SELLING_CHARGES.DOC_NUMBER");
      attribute2dbField.put("vatCodeSal06DOC03","DOC03_SELLING_CHARGES.VAT_CODE_SAL06");
      attribute2dbField.put("vatDescriptionDOC03","DOC03_SELLING_CHARGES.VAT_DESCRIPTION");
      attribute2dbField.put("vatValueDOC03","DOC03_SELLING_CHARGES.VAT_VALUE");
      attribute2dbField.put("vatDeductibleDOC03","DOC03_SELLING_CHARGES.VAT_DEDUCTIBLE");
      attribute2dbField.put("invoicedValueDOC03","DOC03_SELLING_CHARGES.INVOICED_VALUE");
      attribute2dbField.put("valueReg01DOC03","DOC03_SELLING_CHARGES.VALUE_REG01");
      attribute2dbField.put("taxableIncomeDOC03","DOC03_SELLING_CHARGES.TAXABLE_INCOME");

      ArrayList values = new ArrayList();
      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
      values.add( pk.getCompanyCodeSys01DOC01() );
      values.add( pk.getDocTypeDOC01() );
      values.add( pk.getDocYearDOC01() );
      values.add( pk.getDocNumberDOC01() );

      // read from DOC03 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SaleDocChargeVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );


      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocChargesBean.loadSaleDocCharges",
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadSaleDocCharges","Error while fetching sale charges list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}
