package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import org.openswing.swing.util.java.Consts;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.warehouse.availability.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.items.java.*;
import org.jallinone.warehouse.availability.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.companies.server.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.java.*;
import org.jallinone.system.server.*;
import org.jallinone.system.progressives.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to close a sale document, i.e.
 * - check if all items are available (for retail selling only)
 * - unload all items from the specified warehouse (for retail selling only)
 * - change doc state to close
 * - calculate document sequence
 * Requirements:
 * - position must be defined for each item row
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
public class CloseSaleDocAction implements Action {

  public CloseSaleDocAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "closeSaleDoc";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  SaleDocPK pk = (SaleDocPK)inputPar;
	  try {
		  // retrieve internationalization settings (Resources object)...
		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources resources = factory.getResources(userSessionPars.getLanguageId());
		  String t1 = resources.getResource("document closing not allowed: item not available in warehouse");
		  String t2 = resources.getResource("unload items from sale document");
		  String t3 = resources.getResource("retail");
		  String t4 = resources.getResource("noteNumber");
		  String t5 = resources.getResource("customer");
		  String t6 = resources.getResource("invoiceNumber");

		  String t7 = resources.getResource("credit note");
		  String t8 = resources.getResource("valueREG01");
		  String t9 = resources.getResource("rateNumberREG17");
		  String t10 = resources.getResource("sale invoice");

		  String t11 = resources.getResource("retail sale export non allowed: receipt path has not been defined as user parameter.");
		  String t12 = resources.getResource("company not found.");
		  String t13 = resources.getDateMask(Consts.TYPE_DATE_TIME);
		  String t14 = resources.getResource("error while executing external application; return code:");

		  String t15 = resources.getResource("the warehouse motive specified is not defined");
			String t16 = resources.getResource("total amount must not be zero");
			String t17 = resources.getResource("only an immediate payment is allowed");

		  ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01");

     VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(pk.getCompanyCodeSys01DOC01());

		  CloseSaleDoc bean = (CloseSaleDoc)JAIOBeanFactory.getInstance().getBean(CloseSaleDoc.class);
		  Response answer = bean.closeSaleDoc(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),pk,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),companiesList);

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

