package org.jallinone.system.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import javax.jws.soap.SOAPBinding;
import javax.swing.tree.*;
import javax.swing.tree.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openswing.swing.mdi.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.permissions.java.*;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;

import org.jallinone.commons.java.HashMapAdapter;
import org.jallinone.system.java.*;
import org.jallinone.system.java.*;
import org.jallinone.system.java.*;
import org.jallinone.system.permissions.java.RoleVO;
import org.jallinone.system.customizations.java.*;
import java.math.*;
import org.openswing.swing.tree.java.*;
import org.openswing.swing.table.permissions.java.*;
import org.jallinone.system.gridmanager.server.*;

import com.sun.xml.internal.ws.api.SOAPVersion;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve function authorizations and buttons authorizations,
 * according to the logged user.</p>
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
public interface UserAuthorizations {

	
	public ButtonAuthorization[] getButtonsAuthorizations(String serverLanguageId,String username) throws Throwable;
	
    public ButtonCompanyAuthorization[] getButtonCompanyAuthorizations(String serverLanguageId,String username) throws Throwable;
   
    @XmlJavaTypeAdapter(HashMapAdapter.class)
    public HashMap getUserRoles(String serverLanguageId,String username) throws Throwable;

    public int getCompaniesNumber() throws Throwable;    	
    
    public CustomizedWindows getWindowCustomizations(String langId) throws Throwable;
    
    @XmlJavaTypeAdapter(HashMapAdapter.class)
    public HashMap getApplicationPars() throws Throwable;
    
    @XmlJavaTypeAdapter(HashMapAdapter.class)
    public HashMap getLastGridPermissionsDigests() throws Throwable;
    
     
	
}

