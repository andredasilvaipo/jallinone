package org.jallinone.accounting.movements.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert vat values in the specified vat register, for each vat code (ACC07 table).</p>
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
public class InsertVatRegisterBean {


  public InsertVatRegisterBean() {
  }


  /**
   * Business logic to execute.
   */
  public final Response insertVatRows(Connection conn,ArrayList vatRows,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertVatRegisterBean.insertVatRows",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vatRows,
        null
      ));

      // generate progressive for record number...
      VatRowVO rowVO = (VatRowVO)vatRows.get(0);
      BigDecimal recordNumber = CompanyProgressiveUtils.getConsecutiveProgressive(
          rowVO.getCompanyCodeSys01ACC07(),
          "ACC07_VAT_ROWS_RC="+rowVO.getRegisterCodeAcc04ACC07()+"_VAT_YEAR="+rowVO.getVatYearACC07(),
          "RECORD_NUMBER",
          conn
      );

      // insert into ACC07...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ACC07","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveACC07","PROGRESSIVE");
      attribute2dbField.put("vatYearACC07","VAT_YEAR");
      attribute2dbField.put("recordNumberACC07","RECORD_NUMBER");
      attribute2dbField.put("registerCodeAcc04ACC07","REGISTER_CODE_ACC04");
      attribute2dbField.put("vatDateACC07","VAT_DATE");
      attribute2dbField.put("vatValueACC07","VAT_VALUE");
      attribute2dbField.put("vatCodeACC07","VAT_CODE");
      attribute2dbField.put("vatDescriptionACC07","VAT_DESCRIPTION");
      attribute2dbField.put("taxableIncomeACC07","TAXABLE_INCOME");
      Response res = null;
      for(int i=0;i<vatRows.size();i++) {
        rowVO = (VatRowVO)vatRows.get(i);
        rowVO.setRecordNumberACC07(recordNumber);
        rowVO.setProgressiveACC07(CompanyProgressiveUtils.getInternalProgressive(
            rowVO.getCompanyCodeSys01ACC07(),
            "ACC07_VAT_ROWS",
            "PROGRESSIVE",
            conn
        ));
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            rowVO,
            "ACC07_VAT_ROWS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError())
          return res;
      }

      Response answer = new VOListResponse(vatRows,false,vatRows.size());

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertVatRegisterBean.insertVatRows",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vatRows,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertVatRows","Error while inserting a new vat rows in vat register",ex);
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
