package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.customvo.java.CustomValueObject;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.purchases.pricelist.java.*;
import org.openswing.swing.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.ItemPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage supplier item prices.</p>
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

public interface SupplierPrices {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SupplierVariantsPriceVO getSupplierVariantsPrice(); 

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SupplierPriceVO getSupplierPrice(SupplierPricelistVO pk);
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public CustomValueObject getCustomValueObject();
	

	public VOResponse deleteSupplierPrices(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse importAllSupplierItems(SupplierPricelistChanges vo,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertSupplierPrices(ArrayList list,String serverLanguageId,String username,String imagePath,ArrayList companiesList,ArrayList customizedFields) throws Throwable;

	public VOListResponse loadSupplierPrices(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSupplierVariantsPrices(VariantsMatrixVO matrixVO,ItemPK itemPK,String priceListCode,BigDecimal progressiveReg04,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateSupplierPrices(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOResponse updateSupplierVariantsPrices(VariantsPrice variantsPrice,String serverLanguageId,String username) throws Throwable;

}

