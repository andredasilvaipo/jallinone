package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import java.sql.Date;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.sales.documents.java.SaleDocRowPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to confirm a sale document:
 * it change the document state and calculate the doc. sequence that returns to calling class.</p>
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
public class ConfirmSaleDocAction implements Action {

  private LoadSaleDocBean bean = new LoadSaleDocBean();
  private LoadSaleDocRowsBean rowsBean = new LoadSaleDocRowsBean();
  private InsertSaleDocBean insBean = new InsertSaleDocBean();
  private InsertSaleDocRowBean insRowBean = new InsertSaleDocRowBean();
  private LoadSaleDocRowBean rowBean = new LoadSaleDocRowBean();


  public ConfirmSaleDocAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "confirmSaleDoc";
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

      SaleDocPK pk = (SaleDocPK)inputPar;

      // generate progressive for doc. sequence...
      pstmt = conn.prepareStatement(
        "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
      );
      pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(2,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(3,pk.getDocYearDOC01());
      ResultSet rset = pstmt.executeQuery();
      int docSequenceDOC01 = 1;
      if (rset.next())
        docSequenceDOC01 = rset.getInt(1)+1;
      rset.close();
      pstmt.close();


      // create delivery requests from sale orders/contracts...
      if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
          pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE)) {

        // retrieve doc header...
        Response res = bean.loadSaleDoc(
          conn,
          pk,
          userSessionPars,
          request,
          response,
          userSession,
          context
        );
        if (res.isError())
          return res;
        DetailSaleDocVO originalVO = (DetailSaleDocVO)((VOResponse)res).getVo();

        // retrieve doc rows...
        GridParams gridParams = new GridParams();
        gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
        res = rowsBean.loadSaleDocRows(
          conn,
          gridParams,
          userSessionPars,
          request,
          response,
          userSession,
          context
        );
        if (res.isError())
          return res;
        java.util.List rows = ((VOListResponse)res).getRows();

        // group rows per delivery date...
        GridSaleDocRowVO gridRowVO = null;
        HashMap map = new HashMap(); // collection of couples <delivery date,list of DetailSaleDocRowVO objects having that date>
        ArrayList dates = new ArrayList(); // list of delivery dates (to sort...)
        ArrayList aux = null;
        for(int i=0;i<rows.size();i++) {
          gridRowVO = (GridSaleDocRowVO)rows.get(i);
          aux = (ArrayList)map.get(gridRowVO.getDeliveryDateDOC02());
          if (aux==null) {
            aux = new ArrayList();
            map.put(gridRowVO.getDeliveryDateDOC02(),aux);
            dates.add(gridRowVO.getDeliveryDateDOC02());
          }
          aux.add(gridRowVO);
        }
        Date[] datesToSort = (Date[])dates.toArray(new Date[dates.size()]);
        Arrays.sort(datesToSort);

        // create delivery requests for each delivery date, ordered by date...
        DetailSaleDocRowVO rowVO = null;
        DetailSaleDocVO vo = null;
        for(int i=0;i<datesToSort.length;i++) {
          vo = (DetailSaleDocVO)originalVO.clone();
          vo.setDocTypeDoc01DOC01(vo.getDocTypeDOC01());
          vo.setDocYearDoc01DOC01(vo.getDocYearDOC01());
          vo.setDocNumberDoc01DOC01(vo.getDocNumberDOC01());
          vo.setDocSequenceDoc01DOC01(new BigDecimal(docSequenceDOC01));
          vo.setDocRefNumberDOC01(docSequenceDOC01+"/"+vo.getDocYearDOC01());
          vo.setDocTypeDOC01(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE);
          vo.setDeliveryDateDOC01(datesToSort[i]);
          vo.setDocStateDOC01(ApplicationConsts.CONFIRMED);

          // generate progressive for doc. sequence...
          pstmt = conn.prepareStatement(
            "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
          );
          pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
          pstmt.setString(2,vo.getDocTypeDOC01());
          pstmt.setBigDecimal(3,pk.getDocYearDOC01());
          rset = pstmt.executeQuery();
          int docSequence = 1;
          if (rset.next())
            docSequence = rset.getInt(1)+1;
          rset.close();
          pstmt.close();

          vo.setDocSequenceDOC01(new BigDecimal(docSequence));

          res = insBean.insertSaleDoc(
            conn,
            vo,
            userSessionPars,
            request,
            response,
            userSession,
            context
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }


          // insert rows related to the current delivery date...
          aux = (ArrayList)map.get(datesToSort[i]);
          for(int k=0;k<aux.size();k++) {
            gridRowVO = (GridSaleDocRowVO)aux.get(k);

            // retrieve detail...
            res = rowBean.loadSaleDocRow(
                conn,
                new SaleDocRowPK(
                  gridRowVO.getCompanyCodeSys01DOC02(),
                  gridRowVO.getDocTypeDOC02(),
                  gridRowVO.getDocYearDOC02(),
                  gridRowVO.getDocNumberDOC02(),
                  gridRowVO.getItemCodeItm01DOC02(),
                  gridRowVO.getVariantTypeItm06DOC02(),
                  gridRowVO.getVariantCodeItm11DOC02(),
                  gridRowVO.getVariantTypeItm07DOC02(),
                  gridRowVO.getVariantCodeItm12DOC02(),
                  gridRowVO.getVariantTypeItm08DOC02(),
                  gridRowVO.getVariantCodeItm13DOC02(),
                  gridRowVO.getVariantTypeItm09DOC02(),
                  gridRowVO.getVariantCodeItm14DOC02(),
                  gridRowVO.getVariantTypeItm10DOC02(),
                  gridRowVO.getVariantCodeItm15DOC02()
                ),
                userSessionPars,
                request,
                response,
                userSession,
                context
            );
            if (res.isError()) {
              conn.rollback();
              return res;
            }
            rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
            rowVO.setDocTypeDOC02(vo.getDocTypeDOC01());
            rowVO.setDocNumberDOC02(vo.getDocNumberDOC01());

            insRowBean.insertSaleItem(
                conn,
                rowVO,
                userSessionPars,
                request,
                response,
                userSession,
                context
            );
            if (res.isError()) {
              conn.rollback();
              return res;
            }

          } // end for on rows for a specified deliv.date

        } // end for on delivery dates...

      } // end del.req. creation for sale orders/contracts...


      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.CONFIRMED);
      pstmt.setInt(2,docSequenceDOC01);
      pstmt.setString(3,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(4,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(5,pk.getDocYearDOC01());
      pstmt.setBigDecimal(6,pk.getDocNumberDOC01());
      pstmt.execute();

      Response answer = new VOResponse(new BigDecimal(docSequenceDOC01));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while confirming a sale order",ex);
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
