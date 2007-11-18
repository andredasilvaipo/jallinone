package org.jallinone.expirations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.expirations.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.currency.server.LoadCurrenciesAction;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to expirations from DOC19 table,
 * filtered by sale or purchase type document, or by customer/supplier code, or by interval of dates.</p>
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
public class LoadExpirationsAction implements Action {

  private LoadCurrenciesAction currAction = new LoadCurrenciesAction();


  public LoadExpirationsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadExpirations";
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


      GridParams gridPars = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC19");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select DOC19_EXPIRATIONS.COMPANY_CODE_SYS01,DOC19_EXPIRATIONS.DOC_TYPE,DOC19_EXPIRATIONS.DOC_NUMBER,DOC19_EXPIRATIONS.PROGRESSIVE,"+
          "DOC19_EXPIRATIONS.DOC_YEAR,DOC19_EXPIRATIONS.DOC_SEQUENCE,DOC19_EXPIRATIONS.NAME_1,DOC19_EXPIRATIONS.NAME_2,"+
          "DOC19_EXPIRATIONS.DESCRIPTION,DOC19_EXPIRATIONS.VALUE,DOC19_EXPIRATIONS.PAYED,DOC19_EXPIRATIONS.DOC_DATE,"+
          "DOC19_EXPIRATIONS.CURRENCY_CODE_REG03,"+
          "DOC19_EXPIRATIONS.EXPIRATION_DATE,DOC19_EXPIRATIONS.PROGRESSIVE_REG04,DOC19_EXPIRATIONS.CUSTOMER_SUPPLIER_CODE "+
          " from DOC19_EXPIRATIONS where DOC19_EXPIRATIONS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveDOC19","DOC19_EXPIRATIONS.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01DOC19","DOC19_EXPIRATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC19","DOC19_EXPIRATIONS.DOC_TYPE");
      attribute2dbField.put("docNumberDOC19","DOC19_EXPIRATIONS.DOC_NUMBER");
      attribute2dbField.put("docYearDOC19","DOC19_EXPIRATIONS.DOC_YEAR");
      attribute2dbField.put("docSequenceDOC19","DOC19_EXPIRATIONS.DOC_SEQUENCE");
      attribute2dbField.put("name_1DOC19","DOC19_EXPIRATIONS.NAME_1");
      attribute2dbField.put("name_2DOC19","DOC19_EXPIRATIONS.NAME_2");
      attribute2dbField.put("descriptionDOC19","DOC19_EXPIRATIONS.DESCRIPTION");
      attribute2dbField.put("valueDOC19","DOC19_EXPIRATIONS.VALUE");
      attribute2dbField.put("payedDOC19","DOC19_EXPIRATIONS.PAYED");
      attribute2dbField.put("docDateDOC19","DOC19_EXPIRATIONS.DOC_DATE");
      attribute2dbField.put("expirationDateDOC19","DOC19_EXPIRATIONS.EXPIRATION_DATE");
      attribute2dbField.put("progressiveReg04DOC19","DOC19_EXPIRATIONS.PROGRESSIVE_REG04");
      attribute2dbField.put("customerSupplierCodeDOC19","DOC19_EXPIRATIONS.CUSTOMER_SUPPLIER_CODE");
      attribute2dbField.put("currencyCodeReg03DOC19","DOC19_EXPIRATIONS.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();

      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql += " and DOC19_EXPIRATIONS.PROGRESSIVE_REG04=?";
        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04) );
      }

//      if (gridPars.getOtherGridParams().get(ApplicationConsts.CUSTOMER_CODE)!=null) {
//        sql += " and DOC19_EXPIRATIONS.CUSTOMER_SUPPLIER_CODE=?";
//        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.CUSTOMER_CODE) );
//      }
//
//      if (gridPars.getOtherGridParams().get(ApplicationConsts.SUPPLIER_CODE)!=null) {
//        sql += " and DOC19_EXPIRATIONS.CUSTOMER_SUPPLIER_CODE=?";
//        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.SUPPLIER_CODE) );
//      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE)!=null) {
        sql += " and DOC19_EXPIRATIONS.EXPIRATION_DATE>=?";
        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE)!=null) {
        sql += " and DOC19_EXPIRATIONS.EXPIRATION_DATE<=?";
        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.PAYED)!=null) {
        sql += " and DOC19_EXPIRATIONS.PAYED=?";
        values.add( gridPars.getOtherGridParams().get(ApplicationConsts.PAYED) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE)!=null) {
        if (gridPars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE).equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
          // sale doc...
          sql += " and DOC19_EXPIRATIONS.DOC_TYPE in (?,?,?,?,?)";
          values.add(ApplicationConsts.SALE_INVOICE_DOC_TYPE);
          values.add(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE);
          values.add(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
          values.add(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE);
          values.add(ApplicationConsts.SALE_GENERIC_INVOICE);
        }
        else if (gridPars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE).equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE)) {
          // purchase doc...
          sql += " and DOC19_EXPIRATIONS.DOC_TYPE in (?,?,?,?,?)";
          values.add(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE);
          values.add(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE);
          values.add(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE);
          values.add(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE);
          values.add(ApplicationConsts.PURCHASE_GENERIC_INVOICE);
        }
        else {
          sql += " and DOC19_EXPIRATIONS.DOC_TYPE=?";
          values.add(gridPars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE));
        }

      }


      // read from DOC19 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ExpirationVO.class,
          "Y",
          "N",
          context,
          gridPars,
          50,
          true
      );

      if (!res.isError()) {
        // retrieve all currencies...
        Response currRes = currAction.executeCommand(new GridParams(),userSessionPars,request,response,userSession,context);
        if (currRes.isError())
          return res;
        ArrayList currList = ((VOListResponse)currRes).getRows();
        Hashtable currencies = new Hashtable(currList.size());
        CurrencyVO currVO = null;
        for(int i=0;i<currList.size();i++) {
          currVO = (CurrencyVO)currList.get(i);
          currencies.put(currVO.getCurrencyCodeREG03(),currVO);
        }

        ArrayList rows = ((VOListResponse)res).getRows();
        ExpirationVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (ExpirationVO)rows.get(i);
          currVO = (CurrencyVO)currencies.get(vo.getCurrencyCodeReg03DOC19());
          vo.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
          vo.setDecimalsREG03(currVO.getDecimalsREG03());
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching expirations list",ex);
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
