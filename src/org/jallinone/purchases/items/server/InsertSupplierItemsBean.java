package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert new supplier items in PUR02 table,
* based on the Connection passed as argument. No commit or rollback are executed.</p>
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
public class InsertSupplierItemsBean {


  public InsertSupplierItemsBean() {
  }


  /**
   * Insert new supplier items in PUR02 table. No commit or rollback are executed.
   */
  public final Response insertSupplierItems(Connection conn,ArrayList list,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSupplierItemsBean.insertSupplierItems",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        list,
        null
      ));
      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("PUR01").get(0).toString();
      SupplierItemVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","ENABLED");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (SupplierItemVO)list.get(i);
        vo.setEnabledPUR02("Y");

        if (vo.getCompanyCodeSys01PUR02()==null)
          vo.setCompanyCodeSys01PUR02(companyCode);

        // insert into PUR02...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "PUR02_SUPPLIER_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            ApplicationConsts.ID_SUPPLIER_ITEMS_GRID // window identifier...
        );

        if (res.isError() && list.size()==1) {
          return res;
        }

      }
      Response answer = new VOListResponse(list,false,list.size());

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSupplierItemsBean.insertSupplierItems",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        list,
        answer
      ));


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertSupplierItems","Error while inserting new supplier items",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }



}
