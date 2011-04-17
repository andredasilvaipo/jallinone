package org.jallinone.production.billsofmaterial.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import javax.swing.tree.*;
import javax.swing.tree.*;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import java.math.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.production.billsofmaterial.java.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.system.progressives.server.*;
import javax.swing.tree.*;


import java.text.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage bill of materials.</p>
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

public interface BillOfMaterials {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ComponentVO getComponent();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public AltComponentVO getAltComponent();

	public VOResponse createBillOfMaterialsData(ItemPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOResponse deleteAltComponents(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteBillOfMaterialsData(String reportId,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteComponents(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertAltComponents(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertComponents(ArrayList list,String serverLanguageId,String username,String t1) throws Throwable;

	public VOListResponse loadAltComponents(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadComponents(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateComponents(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

}

