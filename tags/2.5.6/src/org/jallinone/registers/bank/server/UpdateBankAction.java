package org.jallinone.registers.bank.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.java.CustomizedWindows;
import org.jallinone.system.server.*;
import org.jallinone.registers.bank.java.*;
import org.openswing.swing.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.*;
import java.math.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing banks.</p>
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
public class UpdateBankAction implements Action {

  public UpdateBankAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateBank";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
      BankVO oldVO = (BankVO)((ValueObject[])inputPar)[0];
      BankVO newVO = (BankVO)((ValueObject[])inputPar)[1];
    try {
		CustomizedWindows cust = ((JAIOUserSessionParameters)userSessionPars).getCustomizedWindows();
		ArrayList customizedFields = cust.getCustomizedFields(new BigDecimal(232));

      Banks bean = (Banks)JAIOBeanFactory.getInstance().getBean(Banks.class);
      Response answer = bean.updateBank(oldVO,newVO,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),customizedFields);

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
}

