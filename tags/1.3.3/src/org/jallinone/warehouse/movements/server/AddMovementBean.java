package org.jallinone.warehouse.movements.server;

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
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to add a new warehouse movement in WAR02 and consequently in WAR03.</p>
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
public class AddMovementBean {


  public AddMovementBean() {
  }


  /**
   * Add a new warehouse movement in WAR02 and consequently in WAR03.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if adding operation is correctly performed.
   */
  public final Response addWarehouseMovement(Connection conn,WarehouseMovementVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    String qtySign = null;
    String itemType = null;

    // retrieve warehouse motive info...
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "AddMovementBean.addWarehouseMovement",
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
      sql = "select QTY_SIGN,ITEM_TYPE from WAR04_WAREHOUSE_MOTIVES where WAREHOUSE_MOTIVE=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,vo.getWarehouseMotiveWar04WAR02());
      rset = pstmt.executeQuery();
      if (rset.next()) {
        qtySign = rset.getString(1);
        itemType = rset.getString(2);
      }
      else {
        // retrieve internationalization settings (Resources object)...
        ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
        Resources res = factory.getResources(userSessionPars.getLanguageId());
        return new ErrorResponse(res.getResource("the warehouse motive specified is not defined"));
      }
    }
    catch (Throwable ex){
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }

    // update WAR03: if it fails then insert into it...
    int rows = 0;
    try {
      sql = "update WAR03_ITEMS_AVAILABILITY set ";
      if (itemType.equals(ApplicationConsts.ITEM_GOOD))
        sql += "AVAILABLE_QTY=AVAILABLE_QTY";
      else if (itemType.equals(ApplicationConsts.ITEM_DAMAGED))
        sql += "DAMAGED_QTY=DAMAGED_QTY";
      sql += qtySign+vo.getDeltaQtyWAR02();
      sql +=
          " where COMPANY_CODE_SYS01=? and WAREHOUSE_CODE_WAR01=? and PROGRESSIVE_HIE01=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,vo.getCompanyCodeSys01WAR02());
      pstmt.setString(2,vo.getWarehouseCodeWar01WAR02());
      pstmt.setBigDecimal(3,vo.getProgressiveHie01WAR02());
      pstmt.setString(4,vo.getItemCodeItm01WAR02());

      pstmt.setString(5,vo.getVariantTypeItm06WAR02());
      pstmt.setString(6,vo.getVariantCodeItm11WAR02());
      pstmt.setString(7,vo.getVariantTypeItm07WAR02());
      pstmt.setString(8,vo.getVariantCodeItm12WAR02());
      pstmt.setString(9,vo.getVariantTypeItm08WAR02());
      pstmt.setString(10,vo.getVariantCodeItm13WAR02());
      pstmt.setString(11,vo.getVariantTypeItm09WAR02());
      pstmt.setString(12,vo.getVariantCodeItm14WAR02());
      pstmt.setString(13,vo.getVariantTypeItm10WAR02());
      pstmt.setString(14,vo.getVariantCodeItm15WAR02());

      rows = pstmt.executeUpdate();
    }
    catch (Throwable ex){
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }

    // update in WAR03 is failed: there will be inserted into it...
    if (rows==0)
      try {
        sql = "insert into WAR03_ITEMS_AVAILABILITY(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,PROGRESSIVE_HIE01,WAREHOUSE_CODE_WAR01,"+
              "AVAILABLE_QTY,DAMAGED_QTY,"+
              "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
              "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
              "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
              "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
              "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 "+
              ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,vo.getCompanyCodeSys01WAR02());
        pstmt.setString(2,vo.getItemCodeItm01WAR02());
        pstmt.setBigDecimal(3,vo.getProgressiveHie01WAR02());
        pstmt.setString(4,vo.getWarehouseCodeWar01WAR02());
        BigDecimal sign = null;
        if (qtySign.equals(ApplicationConsts.QTY_SIGN_PLUS))
          sign = new BigDecimal(1);
        else if (qtySign.equals(ApplicationConsts.QTY_SIGN_MINUS))
          sign = new BigDecimal(-1);

        if (itemType.equals(ApplicationConsts.ITEM_GOOD)) {
          pstmt.setBigDecimal(5,vo.getDeltaQtyWAR02().multiply(sign));
          pstmt.setBigDecimal(6,new BigDecimal(0));
        }
        else if (itemType.equals(ApplicationConsts.ITEM_DAMAGED)) {
          pstmt.setBigDecimal(5,new BigDecimal(0));
          pstmt.setBigDecimal(6,vo.getDeltaQtyWAR02().multiply(sign));
        }

        pstmt.setString(7,vo.getVariantTypeItm06WAR02());
        pstmt.setString(8,vo.getVariantCodeItm11WAR02());
        pstmt.setString(9,vo.getVariantTypeItm07WAR02());
        pstmt.setString(10,vo.getVariantCodeItm12WAR02());
        pstmt.setString(11,vo.getVariantTypeItm08WAR02());
        pstmt.setString(12,vo.getVariantCodeItm13WAR02());
        pstmt.setString(13,vo.getVariantTypeItm09WAR02());
        pstmt.setString(14,vo.getVariantCodeItm14WAR02());
        pstmt.setString(15,vo.getVariantTypeItm10WAR02());
        pstmt.setString(16,vo.getVariantCodeItm15WAR02());

        pstmt.execute();
      }
      catch (Throwable ex){
        return new ErrorResponse(ex.getMessage());
      }
      finally {
        try {
          rset.close();
        }
        catch (Exception ex1) {
        }
        try {
          pstmt.close();
        }
        catch (Exception ex1) {
        }
      }

    // insert movement in WAR02...
    try {
      sql = "insert into WAR02_WAREHOUSE_MOVEMENTS(PROGRESSIVE,COMPANY_CODE_SYS01,WAREHOUSE_CODE_WAR01,"+
            "ITEM_CODE_ITM01,DELTA_QTY,WAREHOUSE_MOTIVE_WAR04,PROGRESSIVE_HIE01,MOVEMENT_DATE,USERNAME,NOTE,"+
            "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
            "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
            "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
            "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
            "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 "+
            ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      pstmt = conn.prepareStatement(sql);
      BigDecimal progressiveWAR02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01WAR02(),"WAR02_WAREHOUSE_MOVEMENTS","PROGRESSIVE",conn);
      pstmt.setBigDecimal(1,progressiveWAR02);
      pstmt.setString(2,vo.getCompanyCodeSys01WAR02());
      pstmt.setString(3,vo.getWarehouseCodeWar01WAR02());
      pstmt.setString(4,vo.getItemCodeItm01WAR02());
      pstmt.setBigDecimal(5,vo.getDeltaQtyWAR02());
      pstmt.setString(6,vo.getWarehouseMotiveWar04WAR02());
      pstmt.setBigDecimal(7,vo.getProgressiveHie01WAR02());
      pstmt.setTimestamp(8,new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.setString(9,userSessionPars.getUsername());
      pstmt.setString(10,vo.getNoteWAR02());

      pstmt.setString(11,vo.getVariantTypeItm06WAR02());
      pstmt.setString(12,vo.getVariantCodeItm11WAR02());
      pstmt.setString(13,vo.getVariantTypeItm07WAR02());
      pstmt.setString(14,vo.getVariantCodeItm12WAR02());
      pstmt.setString(15,vo.getVariantTypeItm08WAR02());
      pstmt.setString(16,vo.getVariantCodeItm13WAR02());
      pstmt.setString(17,vo.getVariantTypeItm09WAR02());
      pstmt.setString(18,vo.getVariantCodeItm14WAR02());
      pstmt.setString(19,vo.getVariantTypeItm10WAR02());
      pstmt.setString(20,vo.getVariantCodeItm15WAR02());

      pstmt.execute();
    }
    catch (Throwable ex){
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }

    // insert serial numbers and bar codes in WAR05...
    try {
      if (qtySign.equals(ApplicationConsts.QTY_SIGN_PLUS)) {
        sql =
            "insert into WAR05_STORED_SERIAL_NUMBERS(SERIAL_NUMBER,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,PROGRESSIVE_HIE01,"+
            "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
            "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
            "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
            "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
            "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 "+
            ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pstmt = conn.prepareStatement(sql);
        for(int i=0;i<vo.getSerialNumbers().size();i++) {
          pstmt.setString(1,vo.getSerialNumbers().get(i).toString());
          pstmt.setString(2,vo.getCompanyCodeSys01WAR02());
          pstmt.setString(3,vo.getItemCodeItm01WAR02());
          pstmt.setBigDecimal(4,vo.getProgressiveHie01WAR02());

          pstmt.setString(5,vo.getVariantTypeItm06WAR02());
          pstmt.setString(6,vo.getVariantCodeItm11WAR02());
          pstmt.setString(7,vo.getVariantTypeItm07WAR02());
          pstmt.setString(8,vo.getVariantCodeItm12WAR02());
          pstmt.setString(9,vo.getVariantTypeItm08WAR02());
          pstmt.setString(10,vo.getVariantCodeItm13WAR02());
          pstmt.setString(11,vo.getVariantTypeItm09WAR02());
          pstmt.setString(12,vo.getVariantCodeItm14WAR02());
          pstmt.setString(13,vo.getVariantTypeItm10WAR02());
          pstmt.setString(14,vo.getVariantCodeItm15WAR02());

          pstmt.execute();
        }
      }
      else {
        sql = "delete from WAR05_STORED_SERIAL_NUMBERS where SERIAL_NUMBER=? ";
        pstmt = conn.prepareStatement(sql);
        for(int i=0;i<vo.getSerialNumbers().size();i++) {
          pstmt.setString(1,vo.getSerialNumbers().get(i).toString());
          pstmt.execute();
        }
      }
    }
    catch (Throwable ex){
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }


    Response answer = new VOResponse(Boolean.TRUE);

    try {
      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
          this,
          "AddMovementBean.addWarehouseMovement",
          GenericEvent.BEFORE_COMMIT,
          (JAIOUserSessionParameters) userSessionPars,
          request,
          response,
          userSession,
          context,
          conn,
          vo,
          answer
          ));
    }
    catch (EventsManagerException ex2) {
      return new ErrorResponse(ex2.getMessage());
    }

    return answer;
  }


}
