package org.jallinone.production.billsofmaterial.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.logger.server.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import java.math.BigDecimal;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.production.billsofmaterial.java.ComponentVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemPK;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve components of the the specified product.</p>
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
public class LoadComponentsAction implements Action {


  public LoadComponentsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadComponents";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ( (JAIOUserSessionParameters) userSessionPars).getServerLanguageId();

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


      String sql =
          "select ITM03_COMPONENTS.COMPANY_CODE_SYS01,ITM03_COMPONENTS.ITEM_CODE_ITM01,ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01,ITM03_COMPONENTS.ENABLED,"+
          "ITM03_COMPONENTS.SEQUENCE,ITM03_COMPONENTS.START_DATE,ITM03_COMPONENTS.END_DATE,"+
          "ITM03_COMPONENTS.VERSION,ITM03_COMPONENTS.REVISION,ITM03_COMPONENTS.QTY,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+
          "from ITM01_ITEMS,SYS10_TRANSLATIONS,ITM03_COMPONENTS "+
          "where "+
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01 = ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM03_COMPONENTS.ITEM_CODE_ITM01 = ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10 = SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and ITM03_COMPONENTS.ENABLED='Y' and "+
          "ITM03_COMPONENTS.COMPANY_CODE_SYS01=? and "+
          "ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM03","ITM03_COMPONENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveHIE02","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("itemCodeItm01ITM03","ITM03_COMPONENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("parentItemCodeItm01ITM03","ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01");
      attribute2dbField.put("sequenceITM03","ITM03_COMPONENTS.SEQUENCE");
      attribute2dbField.put("startDateITM03","ITM03_COMPONENTS.START_DATE");
      attribute2dbField.put("endDateITM03","ITM03_COMPONENTS.END_DATE");
      attribute2dbField.put("versionITM03","ITM03_COMPONENTS.VERSION");
      attribute2dbField.put("revisionITM03","ITM03_COMPONENTS.REVISION");
      attribute2dbField.put("qtyITM03","ITM03_COMPONENTS.QTY");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabledITM03","ITM03_COMPONENTS.ENABLED");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

      GridParams gridParams = (GridParams)inputPar;
      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

      ArrayList values = new ArrayList();
      values.add(langId);
      values.add(pk.getCompanyCodeSys01ITM01());
      values.add(pk.getItemCodeITM01());

      // read from ITM03 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ComponentVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing components",ex1);
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
