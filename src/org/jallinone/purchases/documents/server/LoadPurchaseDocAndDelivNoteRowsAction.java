package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.registers.measure.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.variants.java.VariantDescriptionsVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch purchase order rows from DOC07 table and let null in quantity + war. position defined in a delivery note row.</p>
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
public class LoadPurchaseDocAndDelivNoteRowsAction implements Action {

  public LoadPurchaseDocAndDelivNoteRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPurchaseDocAndDelivNoteRows";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    GridParams pars = (GridParams)inputPar;
    try {
      PurchaseDocPK pk = (PurchaseDocPK)pars.getOtherGridParams().get(ApplicationConsts.PURCHASE_DOC_PK);
			VariantDescriptionsVO vo = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(pk.getCompanyCodeSys01DOC06());

      LoadPurchaseDocAndDelivNoteRows bean = (LoadPurchaseDocAndDelivNoteRows)JAIOBeanFactory.getInstance().getBean(LoadPurchaseDocAndDelivNoteRows.class);
      Response answer = bean.loadPurchaseDocAndDelivNoteRows(vo.getVariant1Descriptions(),vo.getVariant2Descriptions(),vo.getVariant3Descriptions(),vo.getVariant4Descriptions(),vo.getVariant5Descriptions(),pars,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
}

