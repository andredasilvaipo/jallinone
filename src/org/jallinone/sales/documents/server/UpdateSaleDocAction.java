package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.*;

import java.math.*;

import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.registers.payments.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing sale header document.</p>
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
public class UpdateSaleDocAction implements Action {

  public UpdateSaleDocAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
	  return "updateSaleDoc";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  DetailSaleDocVO oldVO = (DetailSaleDocVO)((ValueObject[])inputPar)[0];
	  DetailSaleDocVO newVO = (DetailSaleDocVO)((ValueObject[])inputPar)[1];
	  try {
		  CustomizedWindows cust = ((JAIOUserSessionParameters)userSessionPars).getCustomizedWindows();
		  ArrayList customizedFields = cust.getCustomizedFields(ApplicationConsts.ID_SALE_ORDER);

                  VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(oldVO.getCompanyCodeSys01DOC01());

		  SaleDocs bean = (SaleDocs)JAIOBeanFactory.getInstance().getBean(SaleDocs.class);
		  Response answer = bean.updateSaleDoc(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),oldVO,newVO,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),customizedFields);

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

