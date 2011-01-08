package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.customvo.java.CustomValueObject;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.VariantBarcodeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sale prices.</p>
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

public interface SalePrices {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DetailItemVO getDetailItem();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public PricelistVO getPricelist();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public CustomValueObject getCustomValueObject();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public PriceVO getPrice();

	public VOListResponse insertPrices(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadPrices(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadVariantsPrice(VariantBarcodeVO barcodeVO,String priceListCode,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadVariantsPrices(GridParams params,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updatePrices(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOResponse updateVariantsPrices(VariantsPrice variantsPrice,String serverLanguageId,String username) throws Throwable;

	public VOResponse deletePrices(ArrayList list,String serverLanguageId,String username) throws Throwable;

}

