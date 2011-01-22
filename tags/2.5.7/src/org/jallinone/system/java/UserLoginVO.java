package org.jallinone.system.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store user parameters, related to login task.</p>
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
public class UserLoginVO extends ValueObjectImpl {

	  /** client language identifier */
	  private String languageId;

	  /** server language identifier */
	  private String serverLanguageId;

	  /** employee identifier (optional) */
	  private java.math.BigDecimal progressiveReg04SYS03;

	  /** first name (optional) */
	  private String name_1;

	  /** last name (optional) */
	  private String name_2;

	  /** employee code (optional) */
	  private String employeeCode;

	  /** employee company code */
	  private String companyCodeSys01SYS03;

	  /** default company code */
	  private String defCompanyCodeSys01SYS03;

	  
	public String getServerLanguageId() {
		return serverLanguageId;
	}

	public void setServerLanguageId(String serverLanguageId) {
		this.serverLanguageId = serverLanguageId;
	}

	public java.math.BigDecimal getProgressiveReg04SYS03() {
		return progressiveReg04SYS03;
	}

	public void setProgressiveReg04SYS03(java.math.BigDecimal progressiveReg04SYS03) {
		this.progressiveReg04SYS03 = progressiveReg04SYS03;
	}

	public String getName_1() {
		return name_1;
	}

	public void setName_1(String name_1) {
		this.name_1 = name_1;
	}

	public String getName_2() {
		return name_2;
	}

	public void setName_2(String name_2) {
		this.name_2 = name_2;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getCompanyCodeSys01SYS03() {
		return companyCodeSys01SYS03;
	}

	public void setCompanyCodeSys01SYS03(String companyCodeSys01SYS03) {
		this.companyCodeSys01SYS03 = companyCodeSys01SYS03;
	}

	public String getDefCompanyCodeSys01SYS03() {
		return defCompanyCodeSys01SYS03;
	}

	public void setDefCompanyCodeSys01SYS03(String defCompanyCodeSys01SYS03) {
		this.defCompanyCodeSys01SYS03 = defCompanyCodeSys01SYS03;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	  
	  
	
	
}
