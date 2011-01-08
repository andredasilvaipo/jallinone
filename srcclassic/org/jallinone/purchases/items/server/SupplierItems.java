package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.purchases.items.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage supplier items.</p>
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

public interface SupplierItems {


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SupplierItemVO getSupplierItem(HierarchyLevelVO pk);


	/**
	 * Insert new supplier items in PUR02 table. No commit or rollback are executed.
	 */
	public VOListResponse insertSupplierItems(ArrayList list,String langId,String username,ArrayList companiesList,ArrayList customizedFields) throws Exception;

	public VOListResponse loadSupplierItems(GridParams pars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOListResponse updateSupplierItems(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOListResponse validateSupplierItemCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable;

	public VOResponse importAllItemsToSupplier(SupplierItemVO vo,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteSupplierItems(ArrayList list,String serverLanguageId,String username) throws Throwable;

}

