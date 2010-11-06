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
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new item in the journal (ACC05 and ACC06 tables).</p>
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
public class InsertJournalItemBean {


  public InsertJournalItemBean() {
  }


  /**
   * Insert a new accounting item in ACC05/ACC06 tables.
   * @return JournalHeaderVO object with the progressive attribute filled
   */
  public final Response insertItem(Connection conn,JournalHeaderVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "insertJournalItemBean.insertItem",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));

      // generate progressive for journal item number...
      vo.setProgressiveACC05(CompanyProgressiveUtils.getConsecutiveProgressive(
          vo.getCompanyCodeSys01ACC05(),
          "ACC05_JOURNAL_HEADER_YEAR="+vo.getItemYearACC05(),
          "PROGRESSIVE",
          conn
      ));


      // insert into ACC05...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ACC05","COMPANY_CODE_SYS01");
      attribute2dbField.put("accountingMotiveCodeAcc03ACC05","ACCOUNTING_MOTIVE_CODE_ACC03");
      attribute2dbField.put("progressiveACC05","PROGRESSIVE");
      attribute2dbField.put("itemYearACC05","ITEM_YEAR");
      attribute2dbField.put("itemDateACC05","ITEM_DATE");
      attribute2dbField.put("descriptionACC05","DESCRIPTION");
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "ACC05_JOURNAL_HEADER",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );
      if (res.isError())
        return res;


      // insert into ACC06...
      attribute2dbField.clear();

      attribute2dbField.put("companyCodeSys01ACC06","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveAcc05ACC06","PROGRESSIVE_ACC05");
      attribute2dbField.put("itemYearAcc05ACC06","ITEM_YEAR_ACC05");
      attribute2dbField.put("progressiveACC06","PROGRESSIVE");
      attribute2dbField.put("debitAmountACC06","DEBIT_AMOUNT");
      attribute2dbField.put("creditAmountACC06","CREDIT_AMOUNT");
      attribute2dbField.put("accountCodeTypeACC06","ACCOUNT_CODE_TYPE");
      attribute2dbField.put("accountCodeACC06","ACCOUNT_CODE");
      attribute2dbField.put("accountCodeAcc02ACC06","ACCOUNT_CODE_ACC02");
      attribute2dbField.put("descriptionACC06","DESCRIPTION");
      JournalRowVO rowVO = null;
      for(int i=0;i<vo.getJournalRows().size();i++) {
        rowVO = (JournalRowVO)vo.getJournalRows().get(i);
        rowVO.setProgressiveAcc05ACC06(vo.getProgressiveACC05());
        rowVO.setProgressiveACC06(CompanyProgressiveUtils.getConsecutiveProgressive(
            vo.getCompanyCodeSys01ACC05(),
            "ACC06_JOURNAL_ROWS",
            "PROGRESSIVE",
            conn
        ));
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            rowVO,
            "ACC06_JOURNAL_ROWS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError())
          return res;
      }

      Response answer = new VOResponse(vo);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "insertJournalItemBean.insertItem",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertItem","Error while inserting a new item in the journal",ex);
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
