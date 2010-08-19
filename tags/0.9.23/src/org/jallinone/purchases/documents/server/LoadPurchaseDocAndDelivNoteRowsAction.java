package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch purchase order rows from DOC07 table and let null in quantity + war. position defined in a delivery note row.</p>
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
public class LoadPurchaseDocAndDelivNoteRowsAction implements Action {

  private MeasureConvBean convBean = new  MeasureConvBean();


  public LoadPurchaseDocAndDelivNoteRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPurchaseDocAndDelivNoteRows";
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
      PurchaseDocPK pk = (PurchaseDocPK)pars.getOtherGridParams().get(ApplicationConsts.PURCHASE_DOC_PK);

      String sql =
          "select DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01,DOC07_PURCHASE_ITEMS.DOC_TYPE,DOC07_PURCHASE_ITEMS.DOC_YEAR,DOC07_PURCHASE_ITEMS.DOC_NUMBER,"+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,"+
          "DOC07_PURCHASE_ITEMS.QTY,SYS10_TRANSLATIONS.DESCRIPTION,DOC07_PURCHASE_ITEMS.IN_QTY,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,REG02_ALIAS2.DECIMALS,PUR02_SUPPLIER_ITEMS.UM_CODE_REG02, "+
          "DOC06_PURCHASE.DOC_SEQUENCE, "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15 "+
          " from DOC06_PURCHASE,PUR02_SUPPLIER_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2,DOC07_PURCHASE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
          "DOC06_PURCHASE.DOC_STATE=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
          "DOC07_PURCHASE_ITEMS.QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=DOC06_PURCHASE.PROGRESSIVE_REG04 and "+
          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01 and "+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC09","DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDoc06DOC09","DOC07_PURCHASE_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDoc06DOC09","DOC07_PURCHASE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDoc06DOC09","DOC07_PURCHASE_ITEMS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC09","DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePur02DOC09","DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02");
      attribute2dbField.put("qtyDOC07","DOC07_PURCHASE_ITEMS.QTY");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("inQtyDOC07","DOC07_PURCHASE_ITEMS.IN_QTY");
      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("supplierQtyDecimalsREG02","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("umCodeReg02PUR02","PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
      attribute2dbField.put("progressiveHie02DOC09","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("docSequenceDoc06DOC09","DOC06_PURCHASE.DOC_SEQUENCE");

      attribute2dbField.put("variantTypeItm06DOC09","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC09","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC09","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC09","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC09","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC09","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC09","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC09","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC09","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC09","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC06());
      values.add(pk.getDocTypeDOC06());
      values.add(pk.getDocYearDOC06());
      values.add(ApplicationConsts.CONFIRMED);
      values.add(pk.getDocNumberDOC06());

      // read from DOC07 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridInDeliveryNoteRowVO.class,
          "Y",
          "N",
          context,
          pars,
          true
      );

      if (!res.isError()) {
        GridInDeliveryNoteRowVO vo = null;
        for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
          vo = (GridInDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
          vo.setValueREG05(convBean.getConversion(
            vo.getUmCodeREG02(),
            vo.getUmCodeReg02PUR02(),
            userSessionPars,
            request,
            response,
            userSession,
            context
          ));
          vo.setSupplierQtyDOC09(vo.getQtyDOC07().subtract(vo.getInQtyDOC07()));
          vo.setQtyDOC09(vo.getSupplierQtyDOC09().divide(vo.getValueREG05(),vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching purchase order + delivery note rows proposal list",ex);
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
