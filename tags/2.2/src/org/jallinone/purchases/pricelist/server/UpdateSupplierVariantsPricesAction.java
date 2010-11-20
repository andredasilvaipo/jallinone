package org.jallinone.purchases.pricelist.server;

import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import org.jallinone.purchases.pricelist.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.progressives.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to remove existing records ands re-insert one or more supplier prices in PUR05 table.</p>
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
public class UpdateSupplierVariantsPricesAction implements Action {

  public UpdateSupplierVariantsPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSupplierVariantsPrices";
  }


  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
      Object[] objs = (Object[])inputPar;
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)objs[0];
      Object[][] cells = (Object[][])objs[1];
      String priceListCode = (String)objs[2];
      java.sql.Date startDate = (java.sql.Date)objs[3];
      java.sql.Date endDate = (java.sql.Date)objs[4];
      BigDecimal progressiveReg04 = (BigDecimal)objs[5];
    try {
      VariantsPrice variantsPrice = new VariantsPrice();
      variantsPrice.setMatrixVO(matrixVO);
      variantsPrice.setCells(cells);
      variantsPrice.setPriceListCode(priceListCode);
      variantsPrice.setStartDate(startDate);
      variantsPrice.setEndDate(endDate);
      variantsPrice.setProgressiveReg04(progressiveReg04);
      
      SupplierPrices bean = (SupplierPrices)JAIOBeanFactory.getInstance().getBean(SupplierPrices.class);
      Response answer = bean.updateSupplierVariantsPrices(variantsPrice,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
}

