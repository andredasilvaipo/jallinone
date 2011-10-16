package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.warehouse.availability.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.items.java.*;
import org.jallinone.warehouse.availability.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.system.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to close a purchase document, i.e.
 * - check if all items are available (for retail selling only)
 * - load all items into the specified warehouse (for retail selling only)
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
public class ClosePurchaseDocAction implements Action {

  public ClosePurchaseDocAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "closePurchaseDoc";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
		  PurchaseDocPK pk = (PurchaseDocPK)inputPar;

		  // retrieve internationalization settings (Resources object)...
		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources resources = factory.getResources(userSessionPars.getLanguageId());
      String t1 = resources.getResource("debiting note");
      String t2 = resources.getResource("valueREG01");
      String t3 = resources.getResource("rateNumberREG17");
      String t4 = resources.getResource("purchase invoice");

      String t5 = resources.getResource("noteNumber");
      String t6 = resources.getResource("supplier");
      String t7 = resources.getResource("invoiceNumber");
			VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(pk.getCompanyCodeSys01DOC06());

		  ClosePurchaseDoc bean = (ClosePurchaseDoc)JAIOBeanFactory.getInstance().getBean(ClosePurchaseDoc.class);
		  Response answer = bean.closePurchaseDoc(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),pk,t1,t2,t3,t4,t5,t6,t7,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

