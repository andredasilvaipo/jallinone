package org.jallinone.warehouse.movements.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantNameVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new manual warehouse movement.</p>
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
public class InsertManualMovementsAction implements Action {

  private AddMovementBean movBean = new AddMovementBean();


  public InsertManualMovementsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertManualMovements";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
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

      Object[] pars = (Object[])inputPar;
      MovementVO voTemplate = (MovementVO)pars[0];
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)pars[1];
      Object[][] cells = (Object[][])pars[2];

      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      MovementVO vo = null;
      Response res = null;
      int pos = 0;
      ArrayList sn = new ArrayList();

      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);


        if (matrixVO.getColumnDescriptors().size()==0) {

          if (cells[i][0]!=null) {
            vo = (MovementVO)voTemplate.clone();

            if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
              // e.g. color but not no size...
              vo.setVariantCodeItm11WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm06WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
              vo.setVariantCodeItm12WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm07WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm12WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm07WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
              vo.setVariantCodeItm13WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm08WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm13WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm08WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
              vo.setVariantCodeItm14WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm09WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm14WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm09WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
              vo.setVariantCodeItm15WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm10WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm15WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm10WAR02(rowVO.getVariantTypeITM06());
            }


            //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());
/*
            vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());

            vo.setVariantCodeItm12WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm13WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm14WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm15WAR02(ApplicationConsts.JOLLY);

            vo.setVariantTypeItm07WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm08WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm09WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm10WAR02(ApplicationConsts.JOLLY);
*/
            vo.setDeltaQtyWAR02((BigDecimal)cells[i][0]);
            sn.clear();
            if (vo.getDeltaQtyWAR02().intValue()>0) {
              sn.addAll(voTemplate.getSerialNumbers().subList(pos,pos+vo.getDeltaQtyWAR02().intValue()));
            }
            vo.setSerialNumbers(sn);
            pos += vo.getDeltaQtyWAR02().intValue();
          }

          WarehouseMovementVO movVO = new WarehouseMovementVO(
              vo.getProgressiveHie01WAR02(),
              vo.getDeltaQtyWAR02(),
              vo.getCompanyCodeSys01WAR02(),
              vo.getWarehouseCodeWar01WAR02(),
              vo.getItemCodeItm01WAR02(),
              vo.getWarehouseMotiveWar04WAR02(),
              vo.getItemTypeWAR04(),
              vo.getNoteWAR02(),
              vo.getSerialNumbers(),

              vo.getVariantCodeItm11WAR02(),
              vo.getVariantCodeItm12WAR02(),
              vo.getVariantCodeItm13WAR02(),
              vo.getVariantCodeItm14WAR02(),
              vo.getVariantCodeItm15WAR02(),
              vo.getVariantTypeItm06WAR02(),
              vo.getVariantTypeItm07WAR02(),
              vo.getVariantTypeItm08WAR02(),
              vo.getVariantTypeItm09WAR02(),
              vo.getVariantTypeItm10WAR02()

          );

          res = movBean.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().size();k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(k);
            if (cells[i][k]!=null) {
              vo = (MovementVO)voTemplate.clone();

              vo.setDeltaQtyWAR02((BigDecimal)cells[i][k]);
              sn.clear();
              if (vo.getDeltaQtyWAR02().intValue()>0) {
                sn.addAll(voTemplate.getSerialNumbers().subList(pos,pos+vo.getDeltaQtyWAR02().intValue()));
              }
              vo.setSerialNumbers(sn);
              pos += vo.getDeltaQtyWAR02().intValue();

              //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

              vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());

              vo.setVariantCodeItm12WAR02(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13WAR02(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14WAR02(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15WAR02(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              vo.setVariantTypeItm07WAR02(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08WAR02(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09WAR02(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10WAR02(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              WarehouseMovementVO movVO = new WarehouseMovementVO(
                  vo.getProgressiveHie01WAR02(),
                  vo.getDeltaQtyWAR02(),
                  vo.getCompanyCodeSys01WAR02(),
                  vo.getWarehouseCodeWar01WAR02(),
                  vo.getItemCodeItm01WAR02(),
                  vo.getWarehouseMotiveWar04WAR02(),
                  vo.getItemTypeWAR04(),
                  vo.getNoteWAR02(),
                  vo.getSerialNumbers(),

                  vo.getVariantCodeItm11WAR02(),
                  vo.getVariantCodeItm12WAR02(),
                  vo.getVariantCodeItm13WAR02(),
                  vo.getVariantCodeItm14WAR02(),
                  vo.getVariantCodeItm15WAR02(),
                  vo.getVariantTypeItm06WAR02(),
                  vo.getVariantTypeItm07WAR02(),
                  vo.getVariantTypeItm08WAR02(),
                  vo.getVariantTypeItm09WAR02(),
                  vo.getVariantTypeItm10WAR02()

              );

              res = movBean.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);
              if (res.isError()) {
                conn.rollback();
                return res;
              }

            } // end if not null
          } // end inner for
      } // end outer for

      Response answer = new VOResponse(voTemplate);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new manual warehouse movement",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().size();i++)
      if (((VariantNameVO)vo.getManagedVariants().get(i)).getTableName().equals(tableName))
        return true;
    return false;
  }


}
