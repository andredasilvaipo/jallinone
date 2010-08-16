package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch sale document rows from DOC02 table and let null in quantity + war. position defined in a delivery note row.</p>
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
public class LoadSaleDocAndDelivNoteRowsAction implements Action {

  private MeasureConvBean convBean = new  MeasureConvBean();


  public LoadSaleDocAndDelivNoteRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSaleDocAndDelivNoteRows";
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

      GridParams pars = (GridParams)inputPar;
      SaleDocPK pk = (SaleDocPK)pars.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);

      String sql =
          "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,"+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,"+
          "DOC02_SELLING_ITEMS.QTY,SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.OUT_QTY,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,DOC01_SELLING.DOC_SEQUENCE, "+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 "+
          " from DOC01_SELLING,REG02_MEASURE_UNITS REG02_ALIAS1,DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC01_SELLING.DOC_STATE=? and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
          "DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY>0 and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS1.UM_CODE ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC10","DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDoc01DOC10","DOC02_SELLING_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDoc01DOC10","DOC02_SELLING_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDoc01DOC10","DOC02_SELLING_ITEMS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC10","DOC02_SELLING_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC02","DOC02_SELLING_ITEMS.QTY");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("customerQtyDecimalsREG02","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("progressiveHie02DOC10","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("docSequenceDoc01DOC10","DOC01_SELLING.DOC_SEQUENCE");

      attribute2dbField.put("variantTypeItm06DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC01());
      values.add(ApplicationConsts.CONFIRMED);
      values.add(pk.getDocYearDOC01());
      values.add(pk.getDocNumberDOC01());

      // read from DOC02 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridOutDeliveryNoteRowVO.class,
          "Y",
          "N",
          context,
          pars,
          true
      );

      if (!res.isError()) {
        GridOutDeliveryNoteRowVO vo = null;
        for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
          vo = (GridOutDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
          vo.setQtyDOC10(vo.getQtyDOC02().subtract(vo.getOutQtyDOC02()));
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching sale document + delivery note rows proposal list",ex);
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
