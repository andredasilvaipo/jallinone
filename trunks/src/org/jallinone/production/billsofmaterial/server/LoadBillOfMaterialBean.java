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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to retrieve the bills of material for the specified item code,
 * expressed as a tree model.</p>
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
public class LoadBillOfMaterialBean {

  private LoadCompanyCurrencyAction companyCurr = new LoadCompanyCurrencyAction();


  public LoadBillOfMaterialBean() {
  }


  /**
   * Retrieve a DefaultTreeModel object that contains the bill of materials (MaterialVO objects) of the specified product.
   */
  public final Response getBillOfMaterials(Connection conn,ItemPK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ( (JAIOUserSessionParameters) userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadBillOfMaterialBean.getBillOfMaterials",
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


      String sql1 =
          "select ITM03_COMPONENTS.COMPANY_CODE_SYS01,ITM03_COMPONENTS.ITEM_CODE_ITM01,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02," +
          "ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01,ITM03_COMPONENTS.QTY,SYS10_TRANSLATIONS.DESCRIPTION " +
          "from ITM03_COMPONENTS,ITM01_ITEMS,SYS10_TRANSLATIONS " +
          "where " +
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01 = ITM01_ITEMS.COMPANY_CODE_SYS01 and " +
          "ITM03_COMPONENTS.ITEM_CODE_ITM01 = ITM01_ITEMS.ITEM_CODE and " +
          "ITM01_ITEMS.PROGRESSIVE_SYS10 = SYS10_TRANSLATIONS.PROGRESSIVE and " +
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and ITM03_COMPONENTS.ENABLED='Y' and " +
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01=? and " +
          "ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01 in (";
      String sql3 =
          ") order by ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01,ITM03_COMPONENTS.SEQUENCE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM03","ITM03_COMPONENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01ITM03","ITM03_COMPONENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("parentItemCodeItm01ITM03","ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("qtyITM03","ITM03_COMPONENTS.QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

      ArrayList values = new ArrayList();
      values.add(langId);
      values.add(pk.getCompanyCodeSys01ITM01());

      // retrieve the whole tree...
      MaterialVO compVO = new MaterialVO();
      compVO.setCompanyCodeSys01ITM03(pk.getCompanyCodeSys01ITM01());
      compVO.setDescriptionSYS10("");
      compVO.setItemCodeItm01ITM03(pk.getItemCodeITM01());
      compVO.setQtyITM03(new BigDecimal(1));
      DefaultMutableTreeNode node = new OpenSwingTreeNode(compVO);
      DefaultTreeModel model = new DefaultTreeModel(node);

      String sql2 = "'" + pk.getItemCodeITM01() + "'";
      ArrayList nodes = new ArrayList();
      ArrayList nodes2 = new ArrayList(); // current nodes...
      Hashtable auxnodes = new Hashtable();
      nodes.add(node);

      // read from ITM03 table...
      Response res = null;
      ArrayList rows = null;
      ArrayList auxlist = null;
      DefaultMutableTreeNode auxnode,auxnode2 = null;
      String currentItemCode = null;

      do {
        res = QueryUtil.getQuery(
            conn,
            userSessionPars,
            sql1+sql2+sql3,
            values,
            attribute2dbField,
            MaterialVO.class,
            "Y",
            "N",
            context,
            new GridParams(),
            true
        );
        if (!res.isError()) {
          rows = ((VOListResponse)res).getRows();
          auxnodes.clear();
          nodes2 = new ArrayList();
          sql2 = "";

          // group items per item code parent...
          for(int i=0;i<rows.size();i++) {
            compVO = (MaterialVO)rows.get(i);
            sql2 += "'"+compVO.getItemCodeItm01ITM03()+"',";
            auxnode = new OpenSwingTreeNode(compVO);
            auxlist = (ArrayList)auxnodes.get(compVO.getParentItemCodeItm01ITM03());
            if (auxlist==null) {
              auxlist = new ArrayList();
              auxnodes.put(compVO.getParentItemCodeItm01ITM03(),auxlist);
            }
            auxlist.add(auxnode);
            nodes2.add(auxnode);
          }

          if (sql2.length()>0)
            sql2 = sql2.substring(0,sql2.length()-1);


          // add items to the corresponding parent...
          for(int i=0;i<nodes.size();i++) {
            auxnode = (DefaultMutableTreeNode)nodes.get(i);
            compVO = (MaterialVO)auxnode.getUserObject();
            auxlist = (ArrayList)auxnodes.get(compVO.getItemCodeItm01ITM03());
            if (auxlist!=null)
              for(int j=0;j<auxlist.size();j++) {
                auxnode.add( (DefaultMutableTreeNode) auxlist.get(j));
                ((MaterialVO)((DefaultMutableTreeNode)auxlist.get(j)).getUserObject()).setQtyITM03(
                  ((MaterialVO)((DefaultMutableTreeNode)auxlist.get(j)).getUserObject()).getQtyITM03().multiply(
                  ((MaterialVO)auxnode.getUserObject()).getQtyITM03()).setScale(5,BigDecimal.ROUND_HALF_UP)
                );
              }
          }

          nodes = nodes2;
         }

      }
      while (!res.isError() && rows.size()>0);
      if (res.isError())
        return res;

      res = companyCurr.executeCommand(pk.getCompanyCodeSys01ITM01(),userSessionPars,request, response,userSession,context);
      if (res.isError())
        return res;
      CurrencyVO currVO = (CurrencyVO)((VOResponse)res).getVo();

      // navigate through the tree and fetch item prices...
      pstmt = conn.prepareStatement(
        "select sum(PUR04_SUPPLIER_PRICES.VALUE),count(PUR04_SUPPLIER_PRICES.VALUE),PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03 "+
        "from PUR04_SUPPLIER_PRICES,PUR03_SUPPLIER_PRICELISTS "+
        "where "+
        "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 and "+
        "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 and "+
        "PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE and "+
        "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=? and "+
        "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01=? and "+
        "PUR04_SUPPLIER_PRICES.START_DATE<=? and "+
        "PUR04_SUPPLIER_PRICES.END_DATE>=? "+
        "group by PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03"
      );
      pstmt2 = conn.prepareStatement(
        "select sum(PRO02_MANUFACTURE_PHASES.VALUE) "+
        "from PRO02_MANUFACTURE_PHASES,ITM01_ITEMS "+
        "where "+
        "PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
        "PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01=ITM01_ITEMS.MANUFACTURE_CODE_PRO01 and "+
        "ITM01_ITEMS.COMPANY_CODE_SYS01=? and "+
        "ITM01_ITEMS.ITEM_CODE=?"
      );
      res = expandNode(conn,currVO,pstmt,pstmt2,(DefaultMutableTreeNode)model.getRoot());
      if (res.isError())
        return res;

      Response answer = new VOResponse(model);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadBillOfMaterialBean.getBillOfMaterials",
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
    } catch (Exception ex1) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"getBillOfMaterials","Error while fetching bill of materials",ex1);
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * Navigate tree and fetch item purchase price and production costs.
   * @param node current analyzed node
   */
  private Response expandNode(Connection conn,CurrencyVO currVO,PreparedStatement pstmt,PreparedStatement pstmt2,DefaultMutableTreeNode node) {
    ResultSet rset = null;
    try {
      MaterialVO vo = (MaterialVO)node.getUserObject();
      if (node.getChildCount()==0) {
        // the current node is a leaf, so it cannot be constructed: it will be purchased...
        pstmt.setString(1,vo.getCompanyCodeSys01ITM03());
        pstmt.setString(2,vo.getItemCodeItm01ITM03());
        pstmt.setDate(3,new java.sql.Date(System.currentTimeMillis()));
        pstmt.setDate(4,new java.sql.Date(System.currentTimeMillis()+86400*1000-1));
        rset = pstmt.executeQuery();
        BigDecimal totPrice = new BigDecimal(0);
        BigDecimal totNum = new BigDecimal(0);
        BigDecimal price = null;
        BigDecimal num = null;
        String currencyCode = null;
        while(rset.next()) {
          price = rset.getBigDecimal(1);
          num = rset.getBigDecimal(2);
          currencyCode = rset.getString(3);
          price = CurrencyConversionUtils.convertCurrencyToCurrency(price,currencyCode,currVO.getCurrencyCodeREG03(),conn);
          if (price!=null && num!=null) {
            totPrice = totPrice.add(price);
            totNum = totNum.add(num);
          }
        }
        rset.close();
        if (totPrice.doubleValue()>0) {
          vo.setValuePUR04(vo.getQtyITM03().multiply(totPrice).divide(num, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_HALF_UP));
          vo.setTotalPrices(vo.getValuePUR04());
        }
      }
      else {
        // the current node is not a leaf, so it will be constructed: now it will be calculated production costs...
        pstmt2.setString(1,vo.getCompanyCodeSys01ITM03());
        pstmt2.setString(2,vo.getItemCodeItm01ITM03());
        rset = pstmt2.executeQuery();
        BigDecimal cost = null;
        if(rset.next()) {
          cost = rset.getBigDecimal(1);
        }
        rset.close();
        if (cost!=null && cost.doubleValue()>0) {
          vo.setValuePRO02(vo.getQtyITM03().multiply(cost).setScale(5, BigDecimal.ROUND_HALF_UP));
        }

        // navigate through children nodes...
        Response res = null;
        for(int i=0;i<node.getChildCount();i++) {
          res = expandNode(conn,currVO,pstmt,pstmt2,(DefaultMutableTreeNode)node.getChildAt(i));
          if (res.isError())
            return res;
        }
        if (res.isError())
          return res;

        // calculate total costs + total prices...
        MaterialVO childVO = null;
        BigDecimal totalPrices = new BigDecimal(0);
        BigDecimal totalCosts = vo.getValuePRO02();
        for(int i=0;i<node.getChildCount();i++) {
          childVO = (MaterialVO)((DefaultMutableTreeNode)node.getChildAt(i)).getUserObject();
          if (childVO.getTotalPrices()!=null && totalPrices!=null)
//            totalPrices = totalPrices.add(childVO.getTotalPrices().multiply(childVO.getQtyITM03()));
            totalPrices = totalPrices.add(childVO.getTotalPrices());
          else
            totalPrices = null;
          if (childVO.getTotalCosts()!=null && totalCosts!=null)
//            totalCosts = totalCosts.add(childVO.getTotalCosts().multiply(childVO.getQtyITM03()));
            totalCosts = totalCosts.add(childVO.getTotalCosts());
        }
        if (totalCosts!=null)
          vo.setTotalCosts(totalCosts.setScale(5,BigDecimal.ROUND_HALF_UP));
        if (totalPrices!=null && totalPrices!=null)
          vo.setTotalPrices(totalPrices.setScale(5,BigDecimal.ROUND_HALF_UP));
      }

      return new VOResponse(Boolean.TRUE);

    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex1) {
      }
    }
  }



}
