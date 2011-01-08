package org.jallinone.accounting.ledger.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.accounting.ledger.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;

import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (logically) delete existing ledger.</p>
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
public class DeleteLedgerAction implements EventAction {

  public DeleteLedgerAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteLedger";
  }

  


  /**
   * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
   */
  public Object getInputParClass() {
    return LedgerVO.class;
  }


  /**
   * @return value object class contained in the Response object
   */
  public Class getValueObjectClass() {
    return Boolean.class;
  }



  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
      java.util.ArrayList list = (ArrayList)inputPar;
    try {

      LedgerServices bean = (LedgerServices)JAIOBeanFactory.getInstance().getBean(LedgerServices.class);
      Response answer = bean.deleteLedger(list,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
}

