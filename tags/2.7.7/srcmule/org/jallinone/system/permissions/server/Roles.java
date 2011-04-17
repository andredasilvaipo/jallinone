package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.permissions.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.commons.java.HashMapAdapter;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage roles.</p>
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
@javax.jws.WebService
public interface Roles {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public UserRoleVO getUserRole();
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public GridPermissionsPerRoleVO getGridPermissionsPerRole();
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public RoleFunctionCompanyVO getRoleFunctionCompany();
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public RoleFunctionVO getRoleFunction();
	

	public VOResponse deleteRole(RoleVO vo,String t1,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertRoles(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable;

	public VOListResponse loadGridPermissionsPerRole(Properties p,GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadRoleFunctionCompanies(GridParams params,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadRoleFunctions(GridParams params,String serverLanguageId,String username,@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap userRoles) throws Throwable;

	public VOListResponse loadRoles(String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadUserRoles(GridParams params,String serverLanguageId,String username,@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap userRoles) throws Throwable;

	public VOListResponse updateGridPermissionsPerRole(String functionCodeSYS06,BigDecimal progressiveSYS04,ArrayList vos,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateRoleFunctionCompanies(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateRoleFunctions(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateRoles(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateUserRoles(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

}

