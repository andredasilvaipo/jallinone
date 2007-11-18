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
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import java.math.BigDecimal;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean called on closing a delivery note to update out qty in
 * a sale document and warehouse available quantities.</p>
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
public class UpdateOutQtysSaleDocBean {

  LoadOutDeliveryNoteRowsAction rowsAction = new LoadOutDeliveryNoteRowsAction();
  AddMovementBean movBean = new AddMovementBean();


  public UpdateOutQtysSaleDocBean() {
  }


  /**
   * Update out qty in referred sale documents when closing an out delivery note.
   * It update warehouse available quantities too.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if qtys updating was correctly executed
   */
  public final Response updateOutQuantities(Connection conn,DeliveryNotePK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateOutQtysSaleDocBean.updateOutQuantities",
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

      // retrieve all out delivery note rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
      Response res = rowsAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;

      ArrayList values = new ArrayList();
      String sql1 =
          "select QTY,OUT_QTY from DOC02_SELLING_ITEMS where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?";
      String sql2 =
          "update DOC02_SELLING_ITEMS set OUT_QTY=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and OUT_QTY=?";
      pstmt1 = conn.prepareStatement(sql1);
      pstmt2 = conn.prepareStatement(sql2);

      // for each item row it will be updated the related sale document row and warehouse available quantities...
      GridOutDeliveryNoteRowVO vo = null;
      ResultSet rset1 = null;
      BigDecimal qtyDOC02 = null;
      BigDecimal outQtyDOC02 = null;
      BigDecimal qtyToAdd = null;
      Response innerResponse = null;
      for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
        vo = (GridOutDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
        pstmt1.setString(1,vo.getCompanyCodeSys01DOC10());
        pstmt1.setString(2,vo.getDocTypeDoc01DOC10());
        pstmt1.setBigDecimal(3,vo.getDocYearDoc01DOC10());
        pstmt1.setBigDecimal(4,vo.getDocNumberDoc01DOC10());
        pstmt1.setString(5,vo.getItemCodeItm01DOC10());
        rset1 = pstmt1.executeQuery();
        if(rset1.next()) {
          qtyDOC02 = rset1.getBigDecimal(1);
          outQtyDOC02 = rset1.getBigDecimal(2);
          rset1.close();

          // update out qty in the sale document row...
          if (vo.getQtyDOC10().doubleValue()<qtyDOC02.subtract(outQtyDOC02).doubleValue())
            qtyToAdd = vo.getQtyDOC10();
          else
            qtyToAdd = qtyDOC02.subtract(outQtyDOC02);
          pstmt2.setBigDecimal(1,outQtyDOC02.add(qtyToAdd).setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          pstmt2.setString(2,vo.getCompanyCodeSys01DOC10());
          pstmt2.setString(3,vo.getDocTypeDoc01DOC10());
          pstmt2.setBigDecimal(4,vo.getDocYearDoc01DOC10());
          pstmt2.setBigDecimal(5,vo.getDocNumberDoc01DOC10());
          pstmt2.setString(6,vo.getItemCodeItm01DOC10());
          pstmt2.setBigDecimal(7,outQtyDOC02);


          if (pstmt2.executeUpdate()==0)
            return new ErrorResponse("Updating not performed: the record was previously updated.");
        }
        else
          rset1.close();

        String motive = null;
        if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE))
          motive = ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_ORDER;
        else if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE))
          motive = ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_CONTRACT;

        // update warehouse available qty..
        WarehouseMovementVO movVO = new WarehouseMovementVO(
            vo.getProgressiveHie01DOC10(),
            vo.getQtyDOC10(),
            vo.getCompanyCodeSys01DOC10(),
            vo.getWarehouseCodeWar01DOC08(),
            vo.getItemCodeItm01DOC10(),
            motive,
            ApplicationConsts.ITEM_GOOD,
            resources.getResource("unload items from sale document")+" "+vo.getDocTypeDoc01DOC10()+"/"+vo.getDocNumberDoc01DOC10()+"/"+vo.getDocYearDoc01DOC10(),
            vo.getSerialNumbers(),
            vo.getBarCodes()
        );
        innerResponse = movBean.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);

        if (innerResponse.isError())
          return innerResponse;
      }


      Response answer = new VOResponse(Boolean.TRUE);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateOutQtysSaleDocBean.updateOutQuantities",
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"updateOutQuantities","Error while updating out quantites in sale documents",ex);
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
