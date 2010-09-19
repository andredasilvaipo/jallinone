package org.jallinone.production.billsofmaterial.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import java.math.BigDecimal;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.ItemPK;
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.registers.currency.server.LoadCompanyCurrencyAction;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import javax.swing.tree.TreeModel;
import java.text.DecimalFormat;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve the bill of materials for the specified item code,
 * expressed as a tree model, and fill in a temporary table (TMP01_BILL_OF_MATERIALS) with that tree model.</p>
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
public class CreateBillOfMaterialsDataAction implements Action {

  private LoadBillOfMaterialBean bean = new LoadBillOfMaterialBean();
  private LoadCompanyCurrencyAction compCurr = new LoadCompanyCurrencyAction();


  public CreateBillOfMaterialsDataAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createBillOfMaterialsData";
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

      Map map = (Map)inputPar;
      ItemPK pk = (ItemPK)map.get(ApplicationConsts.ITEM_PK);

      Response res = compCurr.executeCommand(pk.getCompanyCodeSys01ITM01(),userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
      CurrencyVO currVO = (CurrencyVO)((VOResponse)res).getVo();
      String pattern = currVO.getCurrencySymbolREG03()+" #"+currVO.getThousandSymbolREG03()+"##0"+currVO.getDecimalSymbolREG03();
      if (currVO.getDecimalsREG03().intValue()>0)
        for(int i=0;i<currVO.getDecimalsREG03().intValue();i++)
          pattern += "0";
      else
        pattern = "#";
      DecimalFormat format = new DecimalFormat(pattern);
      DecimalFormat qtyFormat = new DecimalFormat("###,##");

      res = bean.getBillOfMaterials(conn,pk,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;

      BigDecimal reportId = CompanyProgressiveUtils.getInternalProgressive(pk.getCompanyCodeSys01ITM01(),"TMP01_BILL_OF_MATERIALS","REPORT_ID",conn);
      TreeModel model = (TreeModel)((VOResponse)res).getVo();
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

      pstmt = conn.prepareStatement(
        "insert into TMP01_BILL_OF_MATERIALS(REPORT_ID,PROGRESSIVE,COMPANY_CODE,PAD,ITEM_CODE,DESCRIPTION,LEV,QTY,UM,PRICE,TOTAL_PRICE,COST,TOTAL_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?)"
      );
      expandNode(0,"",0,null,format,qtyFormat,reportId,pstmt,root);

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
    } catch (Exception ex1) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching bill of materials and fill in TMP01 table",ex1);
      try {
        conn.rollback();
      }
      catch (Exception ex) {
      }
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }
  }


  /**
   * Expand the current node and fill in TMP01 table.
   */
  private int expandNode(int level,String lastPad,int progressive,DefaultMutableTreeNode parentNode,DecimalFormat format,DecimalFormat qtyFormat,BigDecimal reportId,PreparedStatement pstmt,DefaultMutableTreeNode node) throws Exception {
    MaterialVO vo = (MaterialVO)node.getUserObject();

    String pad = "";

    if (level>0) {
//      pad = "|";
//      int spaces = level*2-1;
//      for(int i=0;i<spaces;i++)
//        pad += " ";
//      if (level>1)
//        pad += "|";

      pad = lastPad+"| ";

      pstmt.setBigDecimal(1,reportId);
      pstmt.setInt(2,progressive);
      pstmt.setString(3,vo.getCompanyCodeSys01ITM03());
      pstmt.setString(4,pad);
      pstmt.setString(5,null);
      pstmt.setString(6,null);
      pstmt.setBigDecimal(7,null);
      pstmt.setString(8,null);
      pstmt.setString(9,null);
      pstmt.setString(10,null);
      pstmt.setString(11,null);
      pstmt.setString(12,null);
      pstmt.setString(13,null);
      pstmt.execute();

      progressive++;
    }

    if (level>0) {
//      if (level==1)
//        pad = "+-";
//      else {
//        pad = "|";
//        int spaces = level*2-1;
//        for(int i=0;i<spaces;i++)
//          pad += " ";
//        pad += "+-";
//      }
      pad = lastPad+"+-";
    }
    else
      pad = "";

    pstmt.setBigDecimal(1,reportId);
    pstmt.setInt(2,progressive);
    pstmt.setString(3,vo.getCompanyCodeSys01ITM03());
    pstmt.setString(4,pad);
    pstmt.setString(5,vo.getItemCodeItm01ITM03());
    pstmt.setString(6,vo.getDescriptionSYS10());
    pstmt.setInt(7,level);
    pstmt.setString(8,qtyFormat.format(vo.getQtyITM03()));
    pstmt.setString(9,vo.getMinSellingQtyUmCodeReg02ITM01());
    pstmt.setString(10,vo.getValuePUR04()==null?null:format.format(vo.getValuePUR04().doubleValue()));
    pstmt.setString(11,vo.getTotalPrices()==null?null:format.format(vo.getTotalPrices().doubleValue()));
    pstmt.setString(12,vo.getValuePRO02()==null?null:format.format(vo.getValuePRO02().doubleValue()));
    pstmt.setString(13,vo.getTotalCosts()==null?null:format.format(vo.getTotalCosts().doubleValue()));
    pstmt.execute();

    progressive++;


//    pad = "|";
//    if (level>0) {
//      int spaces = level*2-1;
//      for(int i=0;i<spaces;i++)
//        pad += " ";
//      if (level>1)
//        pad += "|";
//    }
//
//    pstmt.setBigDecimal(1,reportId);
//    pstmt.setInt(2,progressive);
//    pstmt.setString(3,pad);
//    pstmt.setString(4,null);
//    pstmt.setBigDecimal(5,null);
//    pstmt.setString(6,null);
//    pstmt.setString(7,null);
//    pstmt.setString(8,null);
//    pstmt.setString(9,null);
//    pstmt.setString(10,null);
//    pstmt.setString(11,null);
//    pstmt.execute();
//
//    progressive++;

    for(int j=0;j<node.getChildCount();j++) {
      progressive = expandNode(level+1,lastPad+(node.getNextLeaf()!=null?"| ":""),progressive,node,format,qtyFormat,reportId,pstmt,(DefaultMutableTreeNode)node.getChildAt(j));
    }
    return progressive;
  }



}
