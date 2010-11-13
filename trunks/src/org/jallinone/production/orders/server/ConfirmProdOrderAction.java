package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.production.orders.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.production.billsofmaterial.server.*;
import org.jallinone.items.java.*;
import javax.swing.tree.*;
import javax.swing.tree.*;
import org.jallinone.production.billsofmaterial.java.*;
import org.jallinone.warehouse.availability.server.*;
import org.jallinone.warehouse.availability.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.items.server.*;
import org.jallinone.items.java.*;
import org.jallinone.production.manufactures.server.*;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.registers.measure.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to confirm a production order.
 * Confirmation activity involves the following actions:
 * - fill in DOC24 table
 * - check if all components are available in the specified warehouse
 * - remove all required components from the specified warehouse
 * - calculate doc sequence
 * </p>
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
public class ConfirmProdOrderAction implements Action {

  public ConfirmProdOrderAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "confirmProdOrder";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  DetailProdOrderVO voProd = (DetailProdOrderVO)inputPar;
	  try {
		  // retrieve internationalization settings (Resources object)...
		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources resources = factory.getResources(userSessionPars.getLanguageId());
                  String t1 = resources.getResource("component is not available in the specified warehouse");
                  String t2 = resources.getResource("found");
                  String t3 = resources.getResource("required");
                  String t4 = resources.getResource("unload items from production order");
                  String t5 = resources.getResource("the warehouse motive specified is not defined");
		  String imagePath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
		  ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01");

                  VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(voProd.getCompanyCodeSys01DOC22());
		  ProdOrders bean = (ProdOrders)JAIOBeanFactory.getInstance().getBean(ProdOrders.class);
		  Response answer = bean.confirmProdOrder(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),voProd,t1,t2,t3,t4,t5,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),imagePath,companiesList);

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

