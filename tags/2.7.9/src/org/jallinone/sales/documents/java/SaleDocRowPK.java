package org.jallinone.sales.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale order row info, for a grid.</p>
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
 * Software Foundation, Inc., 775 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class SaleDocRowPK implements Serializable {

	private String companyCodeSys01DOC02;
	private String docTypeDOC02;
	private java.math.BigDecimal docYearDOC02;
	private java.math.BigDecimal docNumberDOC02;
	private String itemCodeItm01DOC02;

	private String variantTypeItm06DOC02;
	private String variantCodeItm11DOC02;
	private String variantTypeItm07DOC02;
	private String variantCodeItm12DOC02;
	private String variantTypeItm08DOC02;
	private String variantCodeItm13DOC02;
	private String variantTypeItm09DOC02;
	private String variantCodeItm14DOC02;
	private String variantTypeItm10DOC02;
	private String variantCodeItm15DOC02;


	public SaleDocRowPK(String companyCodeSys01DOC02,String docTypeDOC02,java.math.BigDecimal docYearDOC02,java.math.BigDecimal docNumberDOC02,String itemCodeItm01DOC02,
			String variantTypeItm06DOC02,
			String variantCodeItm11DOC02,
			String variantTypeItm07DOC02,
			String variantCodeItm12DOC02,
			String variantTypeItm08DOC02,
			String variantCodeItm13DOC02,
			String variantTypeItm09DOC02,
			String variantCodeItm14DOC02,
			String variantTypeItm10DOC02,
			String variantCodeItm15DOC02) {
		this.companyCodeSys01DOC02 = companyCodeSys01DOC02;
		this.docTypeDOC02 = docTypeDOC02;
		this.docYearDOC02 = docYearDOC02;
		this.docNumberDOC02 = docNumberDOC02;
		this.itemCodeItm01DOC02 = itemCodeItm01DOC02;

		this.variantTypeItm10DOC02 = variantTypeItm10DOC02;
		this.variantTypeItm09DOC02 = variantTypeItm09DOC02;
		this.variantTypeItm08DOC02 = variantTypeItm08DOC02;
		this.variantTypeItm07DOC02 = variantTypeItm07DOC02;
		this.variantTypeItm06DOC02 = variantTypeItm06DOC02;
		this.variantCodeItm15DOC02 = variantCodeItm15DOC02;
		this.variantCodeItm14DOC02 = variantCodeItm14DOC02;
		this.variantCodeItm13DOC02 = variantCodeItm13DOC02;
		this.variantCodeItm12DOC02 = variantCodeItm12DOC02;
		this.variantCodeItm11DOC02 = variantCodeItm11DOC02;
	}


	public SaleDocRowPK() {}


	public String getCompanyCodeSys01DOC02() {
		return companyCodeSys01DOC02;
	}
	public String getDocTypeDOC02() {
		return docTypeDOC02;
	}
	public java.math.BigDecimal getDocYearDOC02() {
		return docYearDOC02;
	}
	public java.math.BigDecimal getDocNumberDOC02() {
		return docNumberDOC02;
	}
	public String getItemCodeItm01DOC02() {
		return itemCodeItm01DOC02;
	}
	public String getVariantCodeItm11DOC02() {
		return variantCodeItm11DOC02;
	}
	public String getVariantCodeItm12DOC02() {
		return variantCodeItm12DOC02;
	}
	public String getVariantCodeItm13DOC02() {
		return variantCodeItm13DOC02;
	}
	public String getVariantCodeItm14DOC02() {
		return variantCodeItm14DOC02;
	}
	public String getVariantCodeItm15DOC02() {
		return variantCodeItm15DOC02;
	}
	public String getVariantTypeItm06DOC02() {
		return variantTypeItm06DOC02;
	}
	public String getVariantTypeItm07DOC02() {
		return variantTypeItm07DOC02;
	}
	public String getVariantTypeItm08DOC02() {
		return variantTypeItm08DOC02;
	}
	public String getVariantTypeItm09DOC02() {
		return variantTypeItm09DOC02;
	}
	public String getVariantTypeItm10DOC02() {
		return variantTypeItm10DOC02;
	}


	public void setCompanyCodeSys01DOC02(String companyCodeSys01DOC02) {
		this.companyCodeSys01DOC02 = companyCodeSys01DOC02;
	}


	public void setDocTypeDOC02(String docTypeDOC02) {
		this.docTypeDOC02 = docTypeDOC02;
	}


	public void setDocYearDOC02(java.math.BigDecimal docYearDOC02) {
		this.docYearDOC02 = docYearDOC02;
	}


	public void setDocNumberDOC02(java.math.BigDecimal docNumberDOC02) {
		this.docNumberDOC02 = docNumberDOC02;
	}


	public void setItemCodeItm01DOC02(String itemCodeItm01DOC02) {
		this.itemCodeItm01DOC02 = itemCodeItm01DOC02;
	}


	public void setVariantTypeItm06DOC02(String variantTypeItm06DOC02) {
		this.variantTypeItm06DOC02 = variantTypeItm06DOC02;
	}


	public void setVariantCodeItm11DOC02(String variantCodeItm11DOC02) {
		this.variantCodeItm11DOC02 = variantCodeItm11DOC02;
	}


	public void setVariantTypeItm07DOC02(String variantTypeItm07DOC02) {
		this.variantTypeItm07DOC02 = variantTypeItm07DOC02;
	}


	public void setVariantCodeItm12DOC02(String variantCodeItm12DOC02) {
		this.variantCodeItm12DOC02 = variantCodeItm12DOC02;
	}


	public void setVariantTypeItm08DOC02(String variantTypeItm08DOC02) {
		this.variantTypeItm08DOC02 = variantTypeItm08DOC02;
	}


	public void setVariantCodeItm13DOC02(String variantCodeItm13DOC02) {
		this.variantCodeItm13DOC02 = variantCodeItm13DOC02;
	}


	public void setVariantTypeItm09DOC02(String variantTypeItm09DOC02) {
		this.variantTypeItm09DOC02 = variantTypeItm09DOC02;
	}


	public void setVariantCodeItm14DOC02(String variantCodeItm14DOC02) {
		this.variantCodeItm14DOC02 = variantCodeItm14DOC02;
	}


	public void setVariantTypeItm10DOC02(String variantTypeItm10DOC02) {
		this.variantTypeItm10DOC02 = variantTypeItm10DOC02;
	}


	public void setVariantCodeItm15DOC02(String variantCodeItm15DOC02) {
		this.variantCodeItm15DOC02 = variantCodeItm15DOC02;
	}


  
  

}
