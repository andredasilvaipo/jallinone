package org.jallinone.warehouse.movements.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;

import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.warehouse.tables.movements.java.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.variants.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new manual warehouse movement.</p>
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
public class InsertManualMovementsAction implements Action {

  public InsertManualMovementsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertManualMovements";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
		  Object[] pars = (Object[])inputPar;
		  MovementVO voTemplate = (MovementVO)pars[0];
		  VariantsMatrixVO matrixVO = (VariantsMatrixVO)pars[1];
		  Object[][] cells = (Object[][])pars[2];

		  // retrieve internationalization settings (Resources object)...
		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources res = factory.getResources(userSessionPars.getLanguageId());
		  String t1 = res.getResource("the warehouse motive specified is not defined");

		  ManualMovements bean = (ManualMovements)JAIOBeanFactory.getInstance().getBean(ManualMovements.class);
		  Response answer = bean.insertManualMovements(voTemplate,matrixVO,cells,t1,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

