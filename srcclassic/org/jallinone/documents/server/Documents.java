package org.jallinone.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage documents, links, levels and properties.</p>
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

public interface Documents {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public GridDocumentVO getGridDocument(HierarchyLevelVO pk);

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DocPropertyVO getDocProperty();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public LevelPropertyVO getLevelProperty();


	public VOResponse deleteDocumentLinks(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteDocuments(ArrayList list,String serverLanguageId,String username,String docPath) throws Throwable;

	public VOResponse deleteDocumentVersions(ArrayList documentVersionVOs,String serverLanguageId,String username,String docPath) throws Throwable;

	public VOResponse deleteLevelProperties(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertDocument(DetailDocumentVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields,String docPath) throws Throwable;


	public VOListResponse insertDocumentLinks(ArrayList list,String serverLanguageId,String username) throws Throwable;


	public VOListResponse insertLevelProperties(ArrayList list,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public VOListResponse loadDocProperties(DocumentPK pk,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadDocuments(GridParams pars,HashMap filters,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public Document loadDocumentVersion(DocumentVersionVO vo,String serverLanguageId,String username,String docPath) throws Throwable;

	public VOListResponse loadLevelProperties(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public VOListResponse updateDocProperties(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOResponse updateDocument(DetailDocumentVO oldVO,DetailDocumentVO newVO,String serverLanguageId,String username,ArrayList customizedFields,String docPath) throws Throwable;

	public VOListResponse updateLevelProperties(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

}

