package org.jallinone.documents.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.documents.java.DetailDocumentVO;
import org.jallinone.documents.java.DocPropertyVO;
import org.jallinone.documents.java.DocumentLinkVO;
import org.jallinone.documents.java.DocumentPK;
import org.jallinone.documents.java.DocumentVersionVO;
import org.jallinone.documents.java.GridDocumentVO;
import org.jallinone.documents.java.LevelPropertyVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage document links.</p>
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

public interface LoadDocumentLinks {

	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public DocumentLinkVO getDocumentLink(DocumentPK pk);
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public DocumentVersionVO getDocumentVersion();
	
	public VOListResponse loadDocumentLinks(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadDocumentVersions(DocumentPK pk,GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public DetailDocumentVO loadDocument(DocumentPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;



	
}
