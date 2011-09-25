package org.jallinone.items.server;

import java.math.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to prepare barcode labels related to the specified set of items
 * and fill in a temporary table (TMP02_BARCODES) with that data.</p>
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
public class CreateBarcodeLabelsDataAction implements Action {

  public CreateBarcodeLabelsDataAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
	  return "createBarcodeLabelsData";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  HashMap map = (HashMap)inputPar;
	  try {
		  CreateBarcodeLabelsData bean = (CreateBarcodeLabelsData)JAIOBeanFactory.getInstance().getBean(CreateBarcodeLabelsData.class);
		  String imagePath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);

		  ArrayList list = new ArrayList((List)map.get(ApplicationConsts.ITEMS));
		  ItemToPrintVO[] rows = (ItemToPrintVO[])list.toArray(new ItemToPrintVO[list.size()]);

			VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(rows[0].getCompanyCodeSys01());

		  Response answer = bean.createBarcodeLabelsData(
					vo.getVariant1Descriptions(),
					vo.getVariant2Descriptions(),
					vo.getVariant3Descriptions(),
					vo.getVariant4Descriptions(),
					vo.getVariant5Descriptions(),
					rows,
					( (JAIOUserSessionParameters) userSessionPars).getServerLanguageId(),
					userSessionPars.getUsername(), imagePath);

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
}

