package org.jallinone.warehouse.documents.server;

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
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch out delivery note rows from DOC10 table.</p>
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
public class LoadOutDeliveryNoteRowsAction implements Action {


  public LoadOutDeliveryNoteRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadOutDeliveryNoteRows";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    PreparedStatement pstmt = null;
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
      DeliveryNotePK pk = (DeliveryNotePK)pars.getOtherGridParams().get(ApplicationConsts.DELIVERY_NOTE_PK);

      String sql =
          "select DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE,DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.ROW_NUMBER,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01,SYS10_LOC.DESCRIPTION,DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED, "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.INVOICE_QTY, "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15 "+
          " from DOC08_DELIVERY_NOTES,DOC10_OUT_DELIVERY_NOTE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,REG02_MEASURE_UNITS REG02_ALIAS1,SYS10_TRANSLATIONS SYS10_LOC where "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
          "SYS10_LOC.LANGUAGE_CODE=? and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=DOC08_DELIVERY_NOTES.DOC_TYPE and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=DOC08_DELIVERY_NOTES.DOC_YEAR and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=DOC08_DELIVERY_NOTES.DOC_NUMBER and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=? and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=? and "+
          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER");
      attribute2dbField.put("docTypeDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01");
      attribute2dbField.put("docYearDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01");
      attribute2dbField.put("docNumberDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01");
      attribute2dbField.put("rowNumberDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("qtyDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY");
      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("progressiveHie02DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("docSequenceDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC01");
      attribute2dbField.put("invoiceQtyDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.INVOICE_QTY");

      attribute2dbField.put("variantTypeItm06DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC08());
      values.add(pk.getDocTypeDOC08());
      values.add(pk.getDocYearDOC08());
      values.add(pk.getDocNumberDOC08());

      // read from DOC10 table...
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
       ArrayList serialNums = null;
       java.util.List rows = ((VOListResponse)res).getRows();
       GridOutDeliveryNoteRowVO vo = null;

       pstmt = conn.prepareStatement(
         "select SERIAL_NUMBER from DOC12_OUT_SERIAL_NUMBERS where "+
         "PROGRESSIVE_DOC10=? "
        );
        for(int i=0;i<rows.size();i++) {
          vo = (GridOutDeliveryNoteRowVO)rows.get(i);

          // retrieve serial numbers...
          serialNums = new ArrayList();
          vo.setSerialNumbers(serialNums);
          pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
          ResultSet rset = null;
          try {
            rset = pstmt.executeQuery();
            while(rset.next()) {
              serialNums.add(rset.getString(1));
            }
          }
          catch (Exception ex3) {
            throw ex3;
          }
          finally {
            rset.close();
         }


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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching out delivery note rows list",ex);
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
