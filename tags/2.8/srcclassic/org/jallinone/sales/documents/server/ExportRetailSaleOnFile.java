package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.server.LoadUserParamAction;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.companies.server.LoadCompanyAction;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.openswing.swing.util.java.Consts;
import java.text.SimpleDateFormat;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to export (a closed) retail sale data to a text file and to call an external application to process it.</p>
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

public interface ExportRetailSaleOnFile {
	

	public VOResponse exportToFile(String t1,String t2,String t3,String t4,SaleDocPK pk,DetailSaleDocVO docVO,ArrayList rows,String serverLanguageId,String username) throws Throwable;



}

