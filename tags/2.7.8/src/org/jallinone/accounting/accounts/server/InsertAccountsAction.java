package org.jallinone.accounting.accounts.server;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.events.server.EventAction;
import org.jallinone.system.java.CustomizedWindows;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new accounts in ACC02 table.</p>
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
public class InsertAccountsAction implements EventAction {

	public InsertAccountsAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "insertAccounts";
	}




	/**
	 * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
	 */
	public Object getInputParClass() {
		return AccountVO.class;
	}


	/**
	 * @return value object class contained in the Response object
	 */
	public Class getValueObjectClass() {
		return AccountVO.class;
	}



	public final Response executeCommand(Object inputPar,
			UserSessionParameters userSessionPars,
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession userSession,
			ServletContext context) {
		ArrayList rows = (ArrayList)inputPar;
		try {
			CustomizedWindows cust = ((JAIOUserSessionParameters)userSessionPars).getCustomizedWindows();
			ArrayList customizedFields = cust.getCustomizedFields(ApplicationConsts.ID_ACCOUNTS);

			Accounts bean = (Accounts)JAIOBeanFactory.getInstance().getBean(Accounts.class);
			Response answer = bean.insertAccounts(rows,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),customizedFields);

			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

