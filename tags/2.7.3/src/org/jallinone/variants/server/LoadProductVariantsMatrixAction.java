package org.jallinone.variants.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.events.server.*;
import org.jallinone.sales.documents.java.PriceItemVO;
import org.jallinone.sales.pricelist.java.PriceVO;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.purchases.documents.java.SupplierPriceItemVO;
import org.jallinone.purchases.pricelist.java.SupplierPriceVO;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.items.java.ItemPK;
import java.util.ArrayList;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.items.server.LoadItemVariantsAction;
import org.jallinone.items.java.ItemVariantVO;
import java.math.BigDecimal;
import org.jallinone.variants.java.VariantsItemDescriptor;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load variants matrix for the specified item code.</p>
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
public class LoadProductVariantsMatrixAction implements Action {

  public LoadProductVariantsMatrixAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadProductVariantsMatrix";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {

      // create VariantsMatrixVO v.o.
      VariantsItemDescriptor itemVO = (VariantsItemDescriptor)inputPar;
      LoadProductVariantsMatrix bean = (LoadProductVariantsMatrix)JAIOBeanFactory.getInstance().getBean(LoadProductVariantsMatrix.class);      

      if (itemVO instanceof DetailItemVO)
        return new VOResponse( bean.getDetailItemVariantsMatrix(
	        (DetailItemVO)itemVO,
	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
        ));
      else if (itemVO instanceof GridItemVO)
          return new VOResponse( bean.getGridItemVariantsMatrix(
  	        (GridItemVO)itemVO,
  	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
          ));
      else if (itemVO instanceof SupplierPriceItemVO)
          return new VOResponse( bean.getSupplierPriceItemVariantsMatrix(
  	        (SupplierPriceItemVO)itemVO,
  	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
          ));
      else if (itemVO instanceof SupplierPriceVO)
          return new VOResponse( bean.getSupplierPriceVariantsMatrix(
  	        (SupplierPriceVO)itemVO,
  	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
          ));
      else if (itemVO instanceof PriceVO)
          return new VOResponse( bean.getPriceVariantsMatrix(
  	        (PriceVO)itemVO,
  	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
          ));
      else if (itemVO instanceof PriceItemVO)
          return new VOResponse( bean.getPriceItemVariantsMatrix(
  	        (PriceItemVO)itemVO,
  	        ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername()
          ));
      else
    	  return new ErrorResponse("Unsupported class type: "+itemVO.getClass().getName());
   

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching the variants matrix",ex);
      return new ErrorResponse(ex.getMessage());
    }

  }



}


