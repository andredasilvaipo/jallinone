package org.jallinone.accounting.movements.server;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.events.server.EventAction;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.server.Controller;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to endorse economic accounts to loss/profit economic account and
 * to loss/profit patrimonial account.</p>
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
public class EndorseEAccountsAction implements EventAction {

	public EndorseEAccountsAction() {
	}


	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "endorseEAccounts";
	}




	/**
	 * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
	 */
	public Object getInputParClass() {
		HashMap map = new HashMap();
		map.put(ApplicationConsts.COMPANY_CODE_SYS01,String.class);
		map.put(ApplicationConsts.DATE_FILTER,java.sql.Date.class);
		map.put(ApplicationConsts.LOSSPROFIT_E_ACCOUNT,String.class);
		map.put(ApplicationConsts.LOSSPROFIT_P_ACCOUNT,String.class);
		return map;
	}


	/**
	 * @return value object class contained in the Response object
	 */
	public Class getValueObjectClass() {
		return JournalHeaderVO.class;
	}

	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		HashMap map = (HashMap)inputPar;
		try {
			// retrieve internationalization settings (Resources object)...
			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			Resources resources = factory.getResources(userSessionPars.getLanguageId());
			String t1 = resources.getResource("endorse loss/profit to econ. account");
			String t2 = resources.getResource("endorse profit to patrimonial account");
			String t3 = resources.getResource("endorse loss to patrimonial account");

			AccountingMovements bean = (AccountingMovements)JAIOBeanFactory.getInstance().getBean(AccountingMovements.class);

			Response answer = bean.endorseEAccounts(map,t1,t2,t3,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

