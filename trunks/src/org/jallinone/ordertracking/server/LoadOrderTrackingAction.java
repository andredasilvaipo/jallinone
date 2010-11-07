package org.jallinone.ordertracking.server;


import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.ordertracking.java.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.internationalization.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used by the order tracking frame.</p>
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
public class LoadOrderTrackingAction implements Action {

  public LoadOrderTrackingAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadOrderTracking";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
		  GridParams gridParams = (GridParams)inputPar;

		  ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
		  Resources res = factory.getResources(userSessionPars.getLanguageId());

		  
		  HashMap mapDOC06 = new HashMap();
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE,
				  res.getResource("purchase invoice")
		  );
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE,
				  res.getResource("purchase invoice from delivery notes")
		  );
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE,
				  res.getResource("purchase invoice from purchase document")
		  );
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE,
				  res.getResource("debiting note")
		  );
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_GENERIC_INVOICE,
				  res.getResource("purchase generic document")
		  );
		  mapDOC06.put(
				  ApplicationConsts.PURCHASE_ORDER_DOC_TYPE,
				  res.getResource("purchase order")
		  );
		  
		  
		  HashMap mapDOC01 = new HashMap();
		  mapDOC01.put(
				  ApplicationConsts.SALE_INVOICE_DOC_TYPE,
				  res.getResource("sale invoice")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE,
				  res.getResource("sale invoice from delivery notes")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE,
				  res.getResource("sale invoice from sale document")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE,
				  res.getResource("credit note")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_GENERIC_INVOICE,
				  res.getResource("sale generic document")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_ORDER_DOC_TYPE,
				  res.getResource("sale order")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_CONTRACT_DOC_TYPE,
				  res.getResource("sale contract")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_DESK_DOC_TYPE,
				  res.getResource("desk selling")
		  );
		  mapDOC01.put(
				  ApplicationConsts.SALE_ESTIMATE_DOC_TYPE,
				  res.getResource("sale estimate")
		  );
		  mapDOC01.put(
				  ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE,
				  res.getResource("delivery request")
		  );
	  

		  HashMap mapDOC08 = new HashMap();
		  mapDOC08.put(
				  ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE,
				  res.getResource("in delivery note")
		  );
		  mapDOC08.put(
				  ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE,
				  res.getResource("out delivery note")
		  );
		  
		  
		  LoadOrderTracking bean = (LoadOrderTracking)JAIOBeanFactory.getInstance().getBean(LoadOrderTracking.class);
		  Response answer = bean.loadOrderTracking(gridParams,mapDOC06,mapDOC01,mapDOC08,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
  

  
  
}

