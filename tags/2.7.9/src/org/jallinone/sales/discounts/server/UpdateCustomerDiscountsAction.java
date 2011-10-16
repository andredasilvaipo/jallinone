package org.jallinone.sales.discounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.java.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.customers.java.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.subjects.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.sales.discounts.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update customer discounts in SAL03/SAL8 tables.</p>
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
public class UpdateCustomerDiscountsAction implements Action {

  public UpdateCustomerDiscountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCustomerDiscounts";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
      ArrayList oldVOs = (ArrayList)((ArrayList[])inputPar)[0];
      ArrayList newVOs = (ArrayList)((ArrayList[])inputPar)[1];
    try {

    	CustomerDiscounts bean = (CustomerDiscounts)JAIOBeanFactory.getInstance().getBean(CustomerDiscounts.class);
      Response answer = bean.updateCustomerDiscounts(((ArrayList[])inputPar)[0],((ArrayList[])inputPar)[1],((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
}

