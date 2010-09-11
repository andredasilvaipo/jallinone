package org.jallinone.purchases.items.server;

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
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.purchases.items.java.CreateABCFilterVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to analyze items for the point of view of their invoice and unsold stock
 * and create an ABC classification.</p>
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
public class CreateABCAction implements Action {

  private DeleteABCAction action = new DeleteABCAction();


  public CreateABCAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createABC";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
    ResultSet rset = null;
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


      CreateABCFilterVO filterVO = (CreateABCFilterVO)inputPar;
      if (filterVO.getReportId()!=null)
        action.executeCommand(filterVO.getReportId(),userSessionPars,request,response,userSession,context);

      BigDecimal reportId = ProgressiveUtils.getInternalProgressive("TMP03_ABC","REPORT_ID",conn);
      filterVO.setReportId(reportId);


      // insert into TMP03 the good qtys...
      String sql =
        "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
        "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
        "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
        "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
        "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
        "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
        "QTY_TYPE,QTY) "+
        "SELECT "+reportId+",WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15,"+
        "'"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"',"+
        "SUM(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY) "+
        "FROM WAR03_ITEMS_AVAILABILITY,ITM01_ITEMS WHERE "+
        "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? AND "+
        "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? AND "+
        "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
        "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
        "ITM01_ITEMS.PROGRESSIVE_HIE02=? "+
        "GROUP BY WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setString(2,filterVO.getWarehouseCode());
      pstmt.setBigDecimal(3,filterVO.getProgressiveHie02ITM01());
      pstmt.execute();
      pstmt.close();


      // insert into TMP03 the sold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_SOLD_QTY+"',"+
       "SUM(DOC02_SELLING_ITEMS.QTY) "+
       "FROM DOC02_SELLING_ITEMS,DOC01_SELLING WHERE "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
       "DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=? AND "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
       "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
       "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
       "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
       "DOC01_SELLING.WAREHOUSE_CODE_WAR01=? AND "+
       "DOC01_SELLING.CURRENCY_CODE_REG03=? AND "+
       "DOC01_SELLING.DOC_TYPE IN (?,?,?,?) AND "+
       "DOC01_SELLING.DOC_STATE=? AND "+
       "DOC01_SELLING.DOC_DATE>=? AND "+
       "DOC01_SELLING.DOC_DATE<=? "+
       "GROUP BY DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setBigDecimal(2,filterVO.getProgressiveHie02ITM01());
      pstmt.setString(3,filterVO.getWarehouseCode());
      pstmt.setString(4,filterVO.getCurrencyCodeREG03());
      pstmt.setString(5,ApplicationConsts.SALE_INVOICE_DOC_TYPE);
      pstmt.setString(6,ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE);
      pstmt.setString(7,ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
      pstmt.setString(8,ApplicationConsts.SALE_DESK_DOC_TYPE);
      pstmt.setString(9,ApplicationConsts.CLOSED); // ???
      pstmt.setDate(10,filterVO.getStartDate());
      pstmt.setDate(11,filterVO.getEndDate());
      int rows = pstmt.executeUpdate();
      pstmt.close();


      // insert into TMP03 the pawned qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"',"+
       "SUM(DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY) "+
       "FROM DOC02_SELLING_ITEMS,DOC01_SELLING WHERE "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
       "DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=? AND "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
       "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
       "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
       "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
       "DOC01_SELLING.WAREHOUSE_CODE_WAR01=? AND "+
       "DOC01_SELLING.CURRENCY_CODE_REG03=? AND "+
       "DOC01_SELLING.DOC_TYPE IN (?,?) AND "+
       "DOC01_SELLING.DOC_STATE=? AND "+
       "DOC01_SELLING.DOC_DATE>=? AND "+
       "DOC01_SELLING.DOC_DATE<=? "+
       "GROUP BY DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setBigDecimal(2,filterVO.getProgressiveHie02ITM01());
      pstmt.setString(3,filterVO.getWarehouseCode());
      pstmt.setString(4,filterVO.getCurrencyCodeREG03());
      pstmt.setString(5,ApplicationConsts.SALE_ORDER_DOC_TYPE);
      pstmt.setString(6,ApplicationConsts.SALE_CONTRACT_DOC_TYPE);
      pstmt.setString(7,ApplicationConsts.CONFIRMED);
      pstmt.setDate(8,filterVO.getStartDate());
      pstmt.setDate(9,filterVO.getEndDate());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // CASE A: insert into TMP03 the unsold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
       "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
       "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
       "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
       "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
       "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"',"+
       "T1.QTY-T2.QTY "+
       "FROM TMP03_ABC T1,TMP03_ABC T2 WHERE "+
       "T1.REPORT_ID=? AND "+
       "T2.REPORT_ID=? AND "+
       "T1.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"' AND "+
       "T2.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"' AND "+
       "T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
       "T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
       "T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
       "T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
       "T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
       "T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
       "T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
       "T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
       "T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
       "T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
       "T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
       "T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 AND "+
       "T1.QTY-T2.QTY>0 ";

      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setBigDecimal(2,filterVO.getReportId());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // CASE B: insert into TMP03 the unsold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
       "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
       "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
       "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
       "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
       "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"',"+
       "T1.QTY "+
       "FROM TMP03_ABC T1 WHERE "+
       "T1.REPORT_ID=? AND "+
       "T1.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"' AND "+
       "NOT EXISTS(SELECT * FROM TMP03_ABC T2 WHERE "+
       " T2.REPORT_ID=? AND "+
       " T2.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"' AND "+
       " T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
       " T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
       " T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
       " T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
       " T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
       " T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
       " T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
       " T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
       " T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
       " T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
       " T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
       " T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 "+
       ")";

      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setBigDecimal(2,filterVO.getReportId());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // retrieve total unsold...
      sql = "SELECT SUM(QTY) FROM TMP03_ABC WHERE REPORT_ID=? AND QTY_TYPE='"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"'";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      rset = pstmt.executeQuery();
      BigDecimal totalUnsold = new BigDecimal(0);
      if (rset.next())
        totalUnsold = rset.getBigDecimal(1);
      rset.close();
      pstmt.close();


      // retrieve total invoiced...
      sql = "SELECT SUM(QTY) FROM TMP03_ABC WHERE REPORT_ID=? AND QTY_TYPE=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      rset = pstmt.executeQuery();
      BigDecimal totalSold = new BigDecimal(0);
      if (rset.next())
        totalSold = rset.getBigDecimal(1);
      rset.close();
      pstmt.close();


      // grade unsold items...
      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='A' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?>? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.setBigDecimal(3,totalUnsold);
      pstmt.setBigDecimal(4,filterVO.getPerc1Unsold().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='C' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?<? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.setBigDecimal(3,totalUnsold);
      pstmt.setBigDecimal(4,filterVO.getPerc2Unsold().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='B' WHERE REPORT_ID=? AND QTY_TYPE=? AND UNSOLD_GRADE IS NULL ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.execute();
      pstmt.close();


      // grade invoiced items...
      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='A' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?>? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.setBigDecimal(3,totalSold);
      pstmt.setBigDecimal(4,filterVO.getPerc1Invoiced().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='C' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?<? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.setBigDecimal(3,totalSold);
      pstmt.setBigDecimal(4,filterVO.getPerc2Invoiced().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='B' WHERE REPORT_ID=? AND QTY_TYPE=? AND INVOICED_GRADE IS NULL ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.execute();
      pstmt.close();


      Response answer = new VOResponse(reportId);

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

      return answer;
    }
    catch (Throwable ex) {
      try {
        conn.rollback();
      }
      catch (Exception ex2) {
      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while creating ABC classification",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        rset.close();
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
