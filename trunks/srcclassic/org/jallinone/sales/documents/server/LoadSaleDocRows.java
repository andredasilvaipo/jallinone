package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.activities.java.SaleDocActivityVO;
import org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO;
import org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixUtils;
import org.jallinone.variants.java.VariantsMatrixVO;

import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sales order rows.</p>
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

public interface LoadSaleDocRows {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public GridSaleDocRowVO getGridSaleDocRow(SaleDocPK pk);

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SaleDocActivityVO getSaleDocActivity(SaleDocPK pk);

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SaleDocChargeVO getSaleDocCharge(SaleDocPK pk);

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SaleDocDiscountVO getSaleDocDiscount(SaleDocPK pk);


	public VOListResponse loadSaleDocRows(
              HashMap variant1Descriptions,
              HashMap variant2Descriptions,
              HashMap variant3Descriptions,
              HashMap variant4Descriptions,
              HashMap variant5Descriptions,
              GridParams pars,
              String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSaleDocActivities(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSaleDocCharges(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSaleDocDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable;



}
