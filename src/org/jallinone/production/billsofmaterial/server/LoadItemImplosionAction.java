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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve all products that use the specified item code,
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
public class LoadItemImplosionAction implements Action {
  public LoadItemImplosionAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadItemImplosion";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ( (JAIOUserSessionParameters) userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
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

      Map map = (Map)inputPar;

      ItemPK pk = (ItemPK)map.get(ApplicationConsts.ITEM_PK);

      String sql1 =
          "select ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02," +
          "ITM03_COMPONENTS.ITEM_CODE_ITM01,ITM03_COMPONENTS.QTY,SYS10_TRANSLATIONS.DESCRIPTION " +
          "from ITM03_COMPONENTS,ITM01_ITEMS,SYS10_TRANSLATIONS " +
          "where " +
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01 = ITM01_ITEMS.COMPANY_CODE_SYS01 and " +
          "ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01 = ITM01_ITEMS.ITEM_CODE and " +
          "ITM01_ITEMS.PROGRESSIVE_SYS10 = SYS10_TRANSLATIONS.PROGRESSIVE and " +
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and ITM03_COMPONENTS.ENABLED='Y' and " +
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01=? and " +
          "ITM03_COMPONENTS.ITEM_CODE_ITM01 in (";
      String sql3 =
          ") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM03","ITM01_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01ITM03","ITM03_COMPONENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("parentItemCodeItm01ITM03","ITM01_ITEMS.ITEM_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("qtyITM03","ITM03_COMPONENTS.QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");


      ArrayList values = new ArrayList();
      values.add(langId);
      values.add(pk.getCompanyCodeSys01ITM01());

      // retrieve the whole tree...
      MaterialVO rootVO = new MaterialVO();
      rootVO.setCompanyCodeSys01ITM03(pk.getCompanyCodeSys01ITM01());
      rootVO.setDescriptionSYS10("");
      rootVO.setParentItemCodeItm01ITM03("");
      rootVO.setItemCodeItm01ITM03("");
      DefaultMutableTreeNode root = new OpenSwingTreeNode(rootVO);
      DefaultTreeModel model = new DefaultTreeModel(root);

      String sql2 = "'" + pk.getItemCodeITM01() + "'";

      // read from ITM03 table...
      Response res = null;
      java.util.List rows = null;
      DefaultMutableTreeNode auxnode,auxnode2;
      MaterialVO auxVO = null;
      Hashtable nodes = new Hashtable(); // collections of <item component code,related node>
      Hashtable auxnodes = new Hashtable(); // collections of <item component code,related node>
      Enumeration en = null;

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

          sql2 = "";

          for(int i=0;i<rows.size();i++) {
            auxVO = (MaterialVO)rows.get(i);
            sql2 += "'"+auxVO.getParentItemCodeItm01ITM03()+"',";
            auxnode = new OpenSwingTreeNode(auxVO);
            auxnode2 = (DefaultMutableTreeNode)nodes.get(auxVO.getItemCodeItm01ITM03());
            if (auxnode2!=null)
              auxnode.add(auxnode2);
            nodes.remove(auxVO.getItemCodeItm01ITM03());
            auxnodes.put(auxVO.getParentItemCodeItm01ITM03(),auxnode);
          }

          en = nodes.keys();
          while(en.hasMoreElements()) {
            root.add((DefaultMutableTreeNode)nodes.get(en.nextElement()));
          }

          nodes = auxnodes;
          if (sql2.length()>0)
            sql2 = sql2.substring(0,sql2.length()-1);
         }
      }
      while (!res.isError() && rows.size()>0);
      if (res.isError())
        return res;


      Response answer = new VOResponse(model);

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
      return answer;
    } catch (Exception ex1) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching products that use the specified component",ex1);
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }
  }




}
