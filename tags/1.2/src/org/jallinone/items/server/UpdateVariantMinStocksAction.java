package org.jallinone.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.items.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update variants min stocks.</p>
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
public class UpdateVariantMinStocksAction implements Action {


  public UpdateVariantMinStocksAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateVariantMinStocks";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      Object[] objs = (Object[])inputPar;
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)objs[0];
      Object[][] cells = (Object[][])objs[1];

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "updateVariantMinStocks",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        new Object[]{matrixVO,cells},
        null
      ));

      String sql =
          "delete from ITM23_VARIANT_MIN_STOCKS where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());
      pstmt.execute();
      pstmt.close();

      sql =
          "insert into ITM23_VARIANT_MIN_STOCKS("+
          "COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "MIN_STOCK) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      pstmt = conn.prepareStatement(sql);

      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      Object[] row = null;
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      VariantMinStockVO vo = new VariantMinStockVO();
      vo.setCompanyCodeSys01ITM23(matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      vo.setItemCodeItm01ITM23(matrixVO.getItemPK().getItemCodeITM01());

      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);
        row = cells[i];

        if (matrixVO.getColumnDescriptors().size()==0) {
            // e.g. color but not no size...

          if (cells[0]==null)
            continue;

          VariantsMatrixUtils.setVariantTypesAndCodes(vo,"ITM23",matrixVO,rowVO,null);

          pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
          pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

          pstmt.setString(3,vo.getVariantTypeItm06ITM23());
          pstmt.setString(4,vo.getVariantTypeItm07ITM23());
          pstmt.setString(5,vo.getVariantTypeItm08ITM23());
          pstmt.setString(6,vo.getVariantTypeItm09ITM23());
          pstmt.setString(7,vo.getVariantTypeItm10ITM23());

          pstmt.setString(8, vo.getVariantCodeItm11ITM23());
          pstmt.setString(9, vo.getVariantCodeItm12ITM23());
          pstmt.setString(10,vo.getVariantCodeItm13ITM23());
          pstmt.setString(11,vo.getVariantCodeItm14ITM23());
          pstmt.setString(12,vo.getVariantCodeItm15ITM23());

          pstmt.setBigDecimal(13,(BigDecimal)row[0]);

          pstmt.execute();

        }
        else {
          // e.g. color and size...
          for(int j=0;j<row.length;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(j);
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"ITM23",matrixVO,rowVO,colVO);

            if (row[j]==null)
              continue;

            pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
            pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

            pstmt.setString(3,vo.getVariantTypeItm06ITM23());
            pstmt.setString(4,vo.getVariantTypeItm07ITM23());
            pstmt.setString(5,vo.getVariantTypeItm08ITM23());
            pstmt.setString(6,vo.getVariantTypeItm09ITM23());
            pstmt.setString(7,vo.getVariantTypeItm10ITM23());

            pstmt.setString(8, vo.getVariantCodeItm11ITM23());
            pstmt.setString(9, vo.getVariantCodeItm12ITM23());
            pstmt.setString(10,vo.getVariantCodeItm13ITM23());
            pstmt.setString(11,vo.getVariantCodeItm14ITM23());
            pstmt.setString(12,vo.getVariantCodeItm15ITM23());

            pstmt.setBigDecimal(13,(BigDecimal)row[j]);

            pstmt.execute();

          } // end inner for
        }
      } // end outer for

      VOListResponse res = new VOListResponse(new ArrayList(),false,0);

      if (!res.isError())
        conn.commit();
      else
        conn.rollback();

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
        res
      ));
      return res;

    }
    catch (Throwable ex) {
      try {
        conn.rollback();
      }
      catch (Exception ex1) {
      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating min stocks",ex);
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
