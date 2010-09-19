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
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new in delivery note row in DOC09 table.</p>
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
public class InsertInDeliveryNoteRowAction implements Action {

  private CompanyProgressiveUtils progBean = new CompanyProgressiveUtils();
  private InsertInSerialNumbersBean serialNumBean = new InsertInSerialNumbersBean();


  public InsertInDeliveryNoteRowAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertInDeliveryNoteRow";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)inputPar;
      vo.setInvoiceQtyDOC09(new BigDecimal(0));

      // check if note in DOC08 must be updated with delivery note defined in DOC01/DOC06...

      String note = "";
      pstmt = conn.prepareStatement(
        "select NOTE FROM DOC06_PURCHASE WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? AND "+
        "NOT EXISTS(SELECT * FROM DOC09_IN_DELIVERY_NOTE_ITEMS WHERE "+
        "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? AND "+
        "DOC_TYPE_DOC06=? and DOC_YEAR_DOC06=? and DOC_NUMBER_DOC06=? )"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(2,vo.getDocTypeDoc06DOC09());
      pstmt.setBigDecimal(3,vo.getDocYearDoc06DOC09());
      pstmt.setBigDecimal(4,vo.getDocNumberDoc06DOC09());
      pstmt.setString(5,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(6,vo.getDocTypeDOC09());
      pstmt.setBigDecimal(7,vo.getDocYearDOC09());
      pstmt.setBigDecimal(8,vo.getDocNumberDOC09());
      pstmt.setString(9,vo.getDocTypeDoc06DOC09());
      pstmt.setString(10,vo.getDocTypeDoc06DOC09());
      pstmt.setBigDecimal(11,vo.getDocNumberDoc06DOC09());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        note = rset.getString(1);
      }
      rset.close();
      pstmt.close();
      if (note!=null && !note.equals("")) {
        // retrieve current note...
        pstmt = conn.prepareStatement(
          "SELECT NOTE FROM DOC08_DELIVERY_NOTES "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,vo.getCompanyCodeSys01DOC09());
        pstmt.setString(2,vo.getDocTypeDOC09());
        pstmt.setBigDecimal(3,vo.getDocYearDOC09());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC09());
        rset = pstmt.executeQuery();
        String currentNote = null;
        if (rset.next()) {
          currentNote = rset.getString(1);
        }
        rset.close();
        pstmt.close();
        if (currentNote==null)
          currentNote = note;
        else
          currentNote = currentNote+"\n"+note;

        // update note...
        pstmt = conn.prepareStatement(
          "UPDATE DOC08_DELIVERY_NOTES SET NOTE=? "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,currentNote);
        pstmt.setString(2,vo.getCompanyCodeSys01DOC09());
        pstmt.setString(3,vo.getDocTypeDOC09());
        pstmt.setBigDecimal(4,vo.getDocYearDOC09());
        pstmt.setBigDecimal(5,vo.getDocNumberDOC09());
        pstmt.execute();
        pstmt.close();

      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC09","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC09","DOC_TYPE");
      attribute2dbField.put("docYearDOC09","DOC_YEAR");
      attribute2dbField.put("docNumberDOC09","DOC_NUMBER");
      attribute2dbField.put("docTypeDoc06DOC09","DOC_TYPE_DOC06");
      attribute2dbField.put("docYearDoc06DOC09","DOC_YEAR_DOC06");
      attribute2dbField.put("docNumberDoc06DOC09","DOC_NUMBER_DOC06");
      attribute2dbField.put("rowNumberDOC09","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC09","ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC09","QTY");
      attribute2dbField.put("supplierQtyDOC09","SUPPLIER_QTY");
      attribute2dbField.put("progressiveHie02DOC09","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01DOC09","PROGRESSIVE_HIE01");
      attribute2dbField.put("docSequenceDoc06DOC09","DOC_SEQUENCE_DOC06");
      attribute2dbField.put("invoiceQtyDOC09","INVOICE_QTY");

      attribute2dbField.put("progressiveDOC09","PROGRESSIVE");
      attribute2dbField.put("variantTypeItm06DOC09","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC09","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC09","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC09","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC09","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC09","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC09","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC09","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC09","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC09","VARIANT_CODE_ITM15");

      vo.setProgressiveDOC09( progBean.getInternalProgressive(vo.getCompanyCodeSys01DOC09(),"DOC09_IN_DELIVERY_NOTE_ITEMS","PROGRESSIVE",conn) );
      vo.setRowNumberDOC09( progBean.getInternalProgressive(vo.getCompanyCodeSys01DOC09(),"DOC09_IN_DELIVERY_NOTE_ITEMS","ROW_NUMBER",conn) );
/*
      vo.setVariantCodeItm11DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm06DOC09(ApplicationConsts.JOLLY);

      vo.setVariantCodeItm12DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm13DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm14DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm15DOC09(ApplicationConsts.JOLLY);

      vo.setVariantTypeItm07DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm08DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm09DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm10DOC09(ApplicationConsts.JOLLY);
*/
      // insert into DOC09...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC09_IN_DELIVERY_NOTE_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

      if (res.isError()) {
        conn.rollback();
        return res;
      }

      // update delivery note state...
      pstmt = conn.prepareStatement("update DOC08_DELIVERY_NOTES set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(3,vo.getDocTypeDOC09());
      pstmt.setBigDecimal(4,vo.getDocYearDOC09());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC09());
      pstmt.execute();

      // insert serial numbers...
      if (vo.getSerialNumbers().size()>0) {
        res = serialNumBean.reinsertInSerialNumbers(vo,conn,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new in delivery note row",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
