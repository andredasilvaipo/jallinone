package org.jallinone.subjects.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.subjects.java.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.openswing.swing.internationalization.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to insert/update/delete people in/from REG04 table.</p>
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
public class PeopleBean implements People {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }

  
  private CheckSubjectExistBean bean;
  
  public void setBean(CheckSubjectExistBean bean) {
	  this.bean = bean;
  }



  public PeopleBean() {
  }


  /**
   * (Optionally) generate progressive and insert record.
   */
  public final void insert(boolean generateProgressive,PeopleVO vo,String t1,String serverLanguageId,String username) throws Throwable {
	  Connection conn = null;
	  PreparedStatement pstmt = null;
	  try {
		  if (this.conn==null) conn = getConn(); else conn = this.conn;
		  bean.setConn(conn);
		  
		  if (vo.getProgressiveREG04()==null)
			  bean.checkPeopleExist(vo,t1,serverLanguageId,username);


		  // check if there already exists a progressive (a contact...)
		  if (vo.getProgressiveREG04()!=null) {
			  // update subject type in REG04...
			  pstmt = conn.prepareStatement(
					  "update REG04_SUBJECTS set SUBJECT_TYPE=? where COMPANY_CODE_SYS01=? and PROGRESSIVE=? "
			  );
			  pstmt.setString(1,vo.getSubjectTypeREG04());
			  pstmt.setString(2,vo.getCompanyCodeSys01REG04());
			  pstmt.setBigDecimal(3,vo.getProgressiveREG04());
			  pstmt.execute();
			  return;
		  }

		  if (generateProgressive) {
			  vo.setProgressiveREG04( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01REG04(),"REG04_SUBJECTS","PROGRESSIVE",conn) );
		  }

		  // insert record in REG04...
		  pstmt = conn.prepareStatement(
				  "insert into REG04_SUBJECTS(COMPANY_CODE_SYS01,NAME_1,NAME_2,ADDRESS,CITY,ZIP,PROVINCE,COUNTRY,TAX_CODE,PHONE_NUMBER,"+
				  "FAX_NUMBER,EMAIL_ADDRESS,WEB_SITE,MOBILE_NUMBER,NOTE,ENABLED,SUBJECT_TYPE,PROGRESSIVE,SEX,MARITAL_STATUS,BIRTHDAY,"+
				  "BIRTHPLACE,NATIONALITY,COMPANY_CODE_SYS01_REG04,PROGRESSIVE_REG04) VALUES("+
				  "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?,?,?)"
		  );
		  pstmt.setString(1,vo.getCompanyCodeSys01REG04());
		  pstmt.setString(2,vo.getName_1REG04());
		  pstmt.setString(3,vo.getName_2REG04());
		  pstmt.setString(4,vo.getAddressREG04());
		  pstmt.setString(5,vo.getCityREG04());
		  pstmt.setString(6,vo.getZipREG04());
		  pstmt.setString(7,vo.getProvinceREG04());
		  pstmt.setString(8,vo.getCountryREG04());
		  pstmt.setString(9,vo.getTaxCodeREG04());
		  pstmt.setString(10,vo.getPhoneNumberREG04());
		  pstmt.setString(11,vo.getFaxNumberREG04());
		  pstmt.setString(12,vo.getEmailAddressREG04());
		  pstmt.setString(13,vo.getWebSiteREG04());
		  pstmt.setString(14,vo.getMobileNumberREG04());
		  pstmt.setString(15,vo.getNoteREG04());
		  pstmt.setString(16,vo.getSubjectTypeREG04());
		  pstmt.setBigDecimal(17,vo.getProgressiveREG04());

		  pstmt.setString(18,vo.getSexREG04());
		  pstmt.setString(19,vo.getMaritalStatusREG04());
		  pstmt.setDate(20,vo.getBirthdayREG04());
		  pstmt.setString(21,vo.getBirthplaceREG04());
		  pstmt.setString(22,vo.getNationalityREG04());
		  pstmt.setString(23,vo.getCompanyCodeSys01Reg04REG04());
		  pstmt.setBigDecimal(24,vo.getProgressiveReg04REG04());

		  pstmt.execute();
	  }
	  catch (Exception ex) {
		  try {
			  if (this.conn==null && conn!=null)
				  // rollback only local connection
				  conn.rollback();
		  }
		  catch (Exception ex3) {
		  }
		  throw ex;
	  }	  
	  finally {
		  try {
			  pstmt.close();
		  }
		  catch (Exception ex2) {
		  }
		  try {
			  bean.setConn(null);
		  } catch (Exception e) {
		  }

		  try {
			  if (this.conn==null && conn!=null) {
				  // close only local connection
				  conn.commit();
				  conn.close();
			  }

		  }
		  catch (Exception exx) {}
	  }
  }


  /**
   * Update record.
   */
  public VOResponse update(PeopleVO oldVO,PeopleVO newVO,String t1,String serverLanguageId,String username) throws Throwable  {
	  Connection conn = null;
	  try {
		  if (this.conn==null) conn = getConn(); else conn = this.conn;
		  bean.setConn(conn);
		  
		  bean.checkPeopleExist(newVO,t1,serverLanguageId,username);

		  HashSet pkAttrs = new HashSet();
		  pkAttrs.add("companyCodeSys01REG04");
		  pkAttrs.add("progressiveREG04");

		  HashMap attr2dbFields = new HashMap();
		  attr2dbFields.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
		  attr2dbFields.put("progressiveREG04","PROGRESSIVE");
		  attr2dbFields.put("name_1REG04","NAME_1");
		  attr2dbFields.put("name_2REG04","NAME_2");
		  attr2dbFields.put("addressREG04","ADDRESS");
		  attr2dbFields.put("cityREG04","CITY");
		  attr2dbFields.put("zipREG04","ZIP");
		  attr2dbFields.put("provinceREG04","PROVINCE");
		  attr2dbFields.put("countryREG04","COUNTRY");
		  attr2dbFields.put("taxCodeREG04","TAX_CODE");
		  attr2dbFields.put("phoneNumberREG04","PHONE_NUMBER");
		  attr2dbFields.put("faxNumberREG04","FAX_NUMBER");
		  attr2dbFields.put("emailAddressREG04","EMAIL_ADDRESS");
		  attr2dbFields.put("webSiteREG04","WEB_SITE");
		  attr2dbFields.put("mobileNumberREG04","MOBILE_NUMBER");
		  attr2dbFields.put("noteREG04","NOTE");
		  attr2dbFields.put("sexREG04","SEX");
		  attr2dbFields.put("maritalStatusREG04","MARITAL_STATUS");
		  attr2dbFields.put("birthdayREG04","BIRTHDAY");
		  attr2dbFields.put("birthplaceREG04","BIRTHPLACE");
		  attr2dbFields.put("nationalityREG04","NATIONALITY");
		  attr2dbFields.put("companyCodeSys01Reg04REG04","COMPANY_CODE_SYS01_REG04");
		  attr2dbFields.put("progressiveReg04REG04","PROGRESSIVE_REG04");

		  Response res = new QueryUtil().updateTable(
				  conn,
				  new UserSessionParameters(username),
				  pkAttrs,
				  oldVO,
				  newVO,
				  "REG04_SUBJECTS",
				  attr2dbFields,
				  "Y",
				  "N",
				  null,
				  true
		  );
		  if (res.isError())
			  throw new Exception(res.getErrorMessage());
		  return (VOResponse)res;

	  }
	  catch (Exception ex) {
		  try {
			  if (this.conn==null && conn!=null)
				  // rollback only local connection
				  conn.rollback();
		  }
		  catch (Exception ex3) {
		  }
		  throw ex;
	  }
	  finally {
		  try {
			  bean.setConn(null);
		  } catch (Exception e) {
		  }
		  try {
			  if (this.conn==null && conn!=null) {
				  // close only local connection
				  conn.commit();
				  conn.close();
			  }

		  }
		  catch (Exception exx) {}      
	  }    
  }





}

