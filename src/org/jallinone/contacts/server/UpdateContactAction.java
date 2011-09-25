package org.jallinone.contacts.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.Subject;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.Controller;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing contact.</p>
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
public class UpdateContactAction implements Action {

	public UpdateContactAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "updateContact";
	}


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		try {
			Subject oldVO = (Subject)((ValueObject[])inputPar)[0];
			Subject newVO = (Subject)((ValueObject[])inputPar)[1];

			// retrieve internationalization settings (Resources object)...
			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
			String t1 = factory.getResources(serverLanguageId).getResource("there is already another people with the same first and last name.");
			String t2 = factory.getResources(serverLanguageId).getResource("there is already another organization with the same corporate name.");

			Contacts bean = (Contacts)JAIOBeanFactory.getInstance().getBean(Contacts.class);
			Response answer = null;
			if (newVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) 
				answer = bean.updateOrganization((OrganizationVO)oldVO,(OrganizationVO)newVO,t1,t2,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());
			else
				answer = bean.updatePeople((PeopleVO)oldVO,(PeopleVO)newVO,t1,t2,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());


			return answer;
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}
}

