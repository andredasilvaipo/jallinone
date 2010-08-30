package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;
import org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO;
import java.math.BigDecimal;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean called on closing a delivery note to update in qty in
 * a purchase order and warehouse available quantities.</p>
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
public class UpdateInQtysPurchaseOrderBean {

  LoadInDeliveryNoteRowsAction rowsAction = new LoadInDeliveryNoteRowsAction();
  AddMovementBean movBean = new AddMovementBean();


  public UpdateInQtysPurchaseOrderBean() {
  }


  /**
   * Update in qty in referred purchase orders when closing a delivery note.
   * It update warehouse available quantities too.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if qtys updating was correctly executed
   */
  public final Response updateInQuantities(Connection conn,DeliveryNotePK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateInQtysPurchaseOrderBean.updateInQuantities",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        null
      ));
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // retrieve all in delivery note rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
      Response res = rowsAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;

      ArrayList values = new ArrayList();
      String sql1 =
          "select QTY,IN_QTY,ORDER_QTY from DOC07_PURCHASE_ITEMS where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      String sql2 =
          "update DOC07_PURCHASE_ITEMS set IN_QTY=?,ORDER_QTY=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and IN_QTY=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      pstmt1 = conn.prepareStatement(sql1);
      pstmt2 = conn.prepareStatement(sql2);

      // for each item row it will be updated the related purchase order row and warehouse available quantities...
      GridInDeliveryNoteRowVO vo = null;
      ResultSet rset1 = null;
      BigDecimal qtyDOC07 = null;
      BigDecimal inQtyDOC07 = null;
      BigDecimal orderQtyDOC07 = null;
      BigDecimal qtyToAdd = null;
      Response innerResponse = null;
      for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
        vo = (GridInDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
        pstmt1.setString(1,vo.getCompanyCodeSys01DOC09());
        pstmt1.setString(2,vo.getDocTypeDoc06DOC09());
        pstmt1.setBigDecimal(3,vo.getDocYearDoc06DOC09());
        pstmt1.setBigDecimal(4,vo.getDocNumberDoc06DOC09());
        pstmt1.setString(5,vo.getItemCodeItm01DOC09());

        pstmt1.setString(6,vo.getVariantTypeItm06DOC09());
        pstmt1.setString(7,vo.getVariantCodeItm11DOC09());
        pstmt1.setString(8,vo.getVariantTypeItm07DOC09());
        pstmt1.setString(9,vo.getVariantCodeItm12DOC09());
        pstmt1.setString(10,vo.getVariantTypeItm08DOC09());
        pstmt1.setString(11,vo.getVariantCodeItm13DOC09());
        pstmt1.setString(12,vo.getVariantTypeItm09DOC09());
        pstmt1.setString(13,vo.getVariantCodeItm14DOC09());
        pstmt1.setString(14,vo.getVariantTypeItm10DOC09());
        pstmt1.setString(15,vo.getVariantCodeItm15DOC09());

        rset1 = pstmt1.executeQuery();
        if(rset1.next()) {
          qtyDOC07 = rset1.getBigDecimal(1);
          inQtyDOC07 = rset1.getBigDecimal(2);
          orderQtyDOC07 = rset1.getBigDecimal(3);
          rset1.close();

          // update in qty in the purchase order row...
          if (vo.getSupplierQtyDOC09().doubleValue()<qtyDOC07.subtract(inQtyDOC07).doubleValue())
            qtyToAdd = vo.getSupplierQtyDOC09();
          else
            qtyToAdd = qtyDOC07.subtract(inQtyDOC07);

          // update order qty in the purchase order row...
          orderQtyDOC07 = orderQtyDOC07.subtract(vo.getQtyDOC09()).setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP);
          if (orderQtyDOC07.doubleValue()<0)
            orderQtyDOC07 = new BigDecimal(0);

          pstmt2.setBigDecimal(1,inQtyDOC07.add(qtyToAdd).setScale(vo.getSupplierQtyDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          pstmt2.setBigDecimal(2,orderQtyDOC07);
          pstmt2.setString(3,vo.getCompanyCodeSys01DOC09());
          pstmt2.setString(4,vo.getDocTypeDoc06DOC09());
          pstmt2.setBigDecimal(5,vo.getDocYearDoc06DOC09());
          pstmt2.setBigDecimal(6,vo.getDocNumberDoc06DOC09());
          pstmt2.setString(7,vo.getItemCodeItm01DOC09());
          pstmt2.setBigDecimal(8,inQtyDOC07);

          pstmt2.setString(9,vo.getVariantTypeItm06DOC09());
          pstmt2.setString(10,vo.getVariantCodeItm11DOC09());
          pstmt2.setString(11,vo.getVariantTypeItm07DOC09());
          pstmt2.setString(12,vo.getVariantCodeItm12DOC09());
          pstmt2.setString(13,vo.getVariantTypeItm08DOC09());
          pstmt2.setString(14,vo.getVariantCodeItm13DOC09());
          pstmt2.setString(15,vo.getVariantTypeItm09DOC09());
          pstmt2.setString(16,vo.getVariantCodeItm14DOC09());
          pstmt2.setString(17,vo.getVariantTypeItm10DOC09());
          pstmt2.setString(18,vo.getVariantCodeItm15DOC09());

          if (pstmt2.executeUpdate()==0)
            return new ErrorResponse("Updating not performed: the record was previously updated.");
        }
        else
          rset1.close();

        // update warehouse available qty..
        WarehouseMovementVO movVO = new WarehouseMovementVO(
            vo.getProgressiveHie01DOC09(),
            vo.getQtyDOC09(),
            vo.getCompanyCodeSys01DOC09(),
            vo.getWarehouseCodeWar01DOC08(),
            vo.getItemCodeItm01DOC09(),
            ApplicationConsts.WAREHOUSE_MOTIVE_LOAD_BY_ORDER,
            ApplicationConsts.ITEM_GOOD,
            resources.getResource("load items from purchase order")+" "+vo.getDocNumberDoc06DOC09()+"/"+vo.getDocYearDoc06DOC09(),
            vo.getSerialNumbers(),

            vo.getVariantCodeItm11DOC09(),
            vo.getVariantCodeItm12DOC09(),
            vo.getVariantCodeItm13DOC09(),
            vo.getVariantCodeItm14DOC09(),
            vo.getVariantCodeItm15DOC09(),
            vo.getVariantTypeItm06DOC09(),
            vo.getVariantTypeItm07DOC09(),
            vo.getVariantTypeItm08DOC09(),
            vo.getVariantTypeItm09DOC09(),
            vo.getVariantTypeItm10DOC09()
        );
        innerResponse = movBean.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);

        if (innerResponse.isError())
          return innerResponse;
      }


      Response answer = new VOResponse(Boolean.TRUE);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateInQtysPurchaseOrderBean.updateInQuantities",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        answer
      ));


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"updateInQuantities","Error while updating in quantites in purchase orders",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt1.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
      }
    }

  }



}
