package org.jallinone.expirations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.server.*;
import java.math.*;
import org.jallinone.expirations.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.system.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing sale/purchase expirations.</p>
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
public class PayImmediatelyAction implements Action {

	public PayImmediatelyAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "payImmediately";
	}


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		try {
			Object[] pars = (Object[])inputPar;
			String companyCode = (String)pars[0];
			String docType = (String)pars[1];
			BigDecimal docYear = (BigDecimal)pars[2];
			BigDecimal docNumber = (BigDecimal)pars[3];
			BigDecimal docSequence = (BigDecimal)pars[4];

			// retrieve internationalization settings (Resources object)...
			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			Resources resources = factory.getResources(userSessionPars.getLanguageId());
			String t1 = resources.getResource("customer");
			String t2 = resources.getResource("supplier");

			UpdateExpirations bean = (UpdateExpirations)JAIOBeanFactory.getInstance().getBean(UpdateExpirations.class);
			Response answer = bean.payImmediately(companyCode,docType,docYear,docNumber,docSequence,t1,t2,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

