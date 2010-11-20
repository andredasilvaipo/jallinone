package org.jallinone.purchases.documents.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.events.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import java.math.*;
import org.jallinone.commons.java.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.warehouse.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.items.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to create a set of purchase orders, starting from items specified in the reorder frame.</p>
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
public class CreatePurchaseOrdersAction implements Action {

  public CreatePurchaseOrdersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createPurchaseOrders";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
		  Object[] objs = (Object[])inputPar;
		  CurrencyVO currVO = (CurrencyVO)objs[0];
		  WarehouseVO warehouseVO = (WarehouseVO)objs[1];
		  ItemTypeVO itemTypeVO = (ItemTypeVO)objs[2];
		  ArrayList vos = (ArrayList)objs[3];


		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources resources = factory.getResources(userSessionPars.getLanguageId());
		  String t1 = resources.getResource("order created using reorder functionality");

		  CreatePurchaseOrders bean = (CreatePurchaseOrders)JAIOBeanFactory.getInstance().getBean(CreatePurchaseOrders.class);
		  Response answer = bean.createPurchaseOrders(t1,currVO,warehouseVO,itemTypeVO,vos,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

