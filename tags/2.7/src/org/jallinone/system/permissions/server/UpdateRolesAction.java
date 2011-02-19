package org.jallinone.system.permissions.server;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update the modified roles descriptions.</p>
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
public class UpdateRolesAction implements Action {

	public UpdateRolesAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "updateRoles";
	}


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		ArrayList oldVOs = ((ArrayList[])inputPar)[0];
		ArrayList newVOs = ((ArrayList[])inputPar)[1];
		try {

			Roles bean = (Roles)JAIOBeanFactory.getInstance().getBean(Roles.class);
			Response answer = bean.updateRoles(((ArrayList[])inputPar)[0],((ArrayList[])inputPar)[1],((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

