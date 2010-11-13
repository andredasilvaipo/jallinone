package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.documents.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.registers.task.server.*;
import org.jallinone.registers.task.java.*;
import org.jallinone.sales.activities.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.sales.activities.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to create a sale invoice from a closed scheduled activity
 * linked to a call-out request.</p>
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
public class CreateInvoiceFromScheduledActivityAction implements Action {

	public CreateInvoiceFromScheduledActivityAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "createInvoiceFromScheduledActivity";
	}


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		try {
			InvoiceFromCallOutRequestVO invVO = (InvoiceFromCallOutRequestVO)inputPar;

			// retrieve internationalization settings (Resources object)...
			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			Resources resources = factory.getResources(userSessionPars.getLanguageId());
			String t1 = resources.getResource("no price defined for the scheduled item");

                        VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(invVO.getCallOutRequest().getCompanyCodeSys01SCH03());

			CreateInvoiceFromScheduledActivity bean = (CreateInvoiceFromScheduledActivity)JAIOBeanFactory.getInstance().getBean(CreateInvoiceFromScheduledActivity.class);
			Response answer = bean.createInvoiceFromScheduledActivity(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),invVO,t1,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

