package org.jallinone.system.importdata.server;

import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.events.server.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load ETL processes defined in SYS23 table.</p>
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
public class LoadETLProcessesAction implements Action {

  public LoadETLProcessesAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadETLProcesses";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("IMPORT_DATA");
      GridParams gridParams = (GridParams)inputPar;
      
      gridParams.getOtherGridParams().clear();
      
      LoadETLProcesses bean = (LoadETLProcesses)JAIOBeanFactory.getInstance().getBean(LoadETLProcesses.class);
      Response answer = bean.loadETLProcesses(gridParams,userSessionPars.getUsername(),companiesList);
      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching ETL processes list",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }



}
