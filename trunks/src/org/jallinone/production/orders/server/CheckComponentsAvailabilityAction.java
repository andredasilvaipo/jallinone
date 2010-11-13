package org.jallinone.production.orders.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.Controller;
import org.openswing.swing.server.UserSessionParameters;
import org.jallinone.variants.java.VariantDescriptionsVO;
import org.jallinone.production.orders.java.ProdOrderProductVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to check components availability for a production order.</p>
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
public class CheckComponentsAvailabilityAction implements Action {


  public CheckComponentsAvailabilityAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "checkComponentsAvailability";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {

    	// retrieve internationalization settings (Resources object)...
    	ServerResourcesFactory factory = (ServerResourcesFactory) context.getAttribute(Controller.RESOURCES_FACTORY);
    	Resources resources = factory.getResources(userSessionPars.getLanguageId());

    	ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01");


    	CheckComponentsAvailability bean = (CheckComponentsAvailability)JAIOBeanFactory.getInstance().getBean(CheckComponentsAvailability.class);
    	GridParams gridParams = (GridParams)inputPar;;
    	ArrayList products = (ArrayList)gridParams.getOtherGridParams().get(ApplicationConsts.PRODUCTS);
        ProdOrderProductVO prodVO = (ProdOrderProductVO)products.get(0);
        VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(prodVO.getCompanyCodeSys01DOC23());
    	Response answer = bean.checkComponentsAvailability(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),new HashMap(),products,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),companiesList);

    	return answer;
    }
    catch (Throwable ex) {
    	Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while checking components availability for a production order",ex);
    	return new ErrorResponse(ex.getMessage());
    }

  }



}
