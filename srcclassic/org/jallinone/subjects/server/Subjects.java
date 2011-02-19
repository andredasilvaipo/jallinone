package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.subjects.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage subjects.</p>
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

public interface Subjects {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public ReferenceVO getReferenceVO(SubjectPK pk);

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SubjectHierarchyLevelVO getSubjectHierarchyLevel(SubjectVO vo);
	
	
	

	public VOResponse deleteReferences(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteSubjectHierarchy(ArrayList list,String serverLanguageId,String username) throws Throwable;
	public VOResponse deleteSubjectsLinks(HierarSubjectsVO hsVO,String serverLanguageId,String username) throws Throwable;
	public VOListResponse insertReferences(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertPeople(PeopleVO inputPar,String t1,String t2,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertOrganization(OrganizationVO inputPar,String t1,String t2,String serverLanguageId,String username) throws Throwable;
	public VOResponse insertSubjectHierarchy(SubjectHierarchyVO vo,String serverLanguageId,String username) throws Throwable;
	public VOResponse insertSubjectsLinks(HierarSubjectsVO hsVO,String serverLanguageId,String username) throws Throwable;
	public VOListResponse loadHierarSubjects(GridParams pars,String serverLanguageId,String username) throws Throwable;
	public VOListResponse loadReferences(GridParams gridParams,String serverLanguageId,String username) throws Throwable;
	public VOListResponse loadSubjectHierarchies(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;
	public VOListResponse loadSubjectHierarchyLevels(GridParams gridParams,String serverLanguageId,String username) throws Throwable;
	public VOListResponse loadSubjectPerName(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public VOListResponse updateReferences(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public void updateOrganization(OrganizationVO oldVO,OrganizationVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable;

	public void updatePeople(PeopleVO oldVO,PeopleVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable;
	public VOListResponse updateSubjectHierarchies(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;
	public VOListResponse updateSubjectHierarchyLevels(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

}

