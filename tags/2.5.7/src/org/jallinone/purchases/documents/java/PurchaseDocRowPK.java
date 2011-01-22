package org.jallinone.purchases.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase order row info, for a grid.</p>
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
public class PurchaseDocRowPK implements Serializable {

	private String companyCodeSys01DOC07;
	private String docTypeDOC07;
	private java.math.BigDecimal docYearDOC07;
	private java.math.BigDecimal docNumberDOC07;
	private String itemCodeItm01DOC07;

	private String variantTypeItm06DOC07;
	private String variantCodeItm11DOC07;
	private String variantTypeItm07DOC07;
	private String variantCodeItm12DOC07;
	private String variantTypeItm08DOC07;
	private String variantCodeItm13DOC07;
	private String variantTypeItm09DOC07;
	private String variantCodeItm14DOC07;
	private String variantTypeItm10DOC07;
	private String variantCodeItm15DOC07;


	public PurchaseDocRowPK(String companyCodeSys01DOC07,String docTypeDOC07,java.math.BigDecimal docYearDOC07,java.math.BigDecimal docNumberDOC07,String itemCodeItm01DOC07,
			String variantTypeItm06DOC07,String variantCodeItm11DOC07,
			String variantTypeItm07DOC07,String variantCodeItm12DOC07,
			String variantTypeItm08DOC07,String variantCodeItm13DOC07,
			String variantTypeItm09DOC07,String variantCodeItm14DOC07,
			String variantTypeItm10DOC07,String variantCodeItm15DOC07) {
		this.companyCodeSys01DOC07 = companyCodeSys01DOC07;
		this.docTypeDOC07 = docTypeDOC07;
		this.docYearDOC07 = docYearDOC07;
		this.docNumberDOC07 = docNumberDOC07;
		this.itemCodeItm01DOC07 = itemCodeItm01DOC07;

		this.variantTypeItm10DOC07 = variantTypeItm10DOC07;
		this.variantTypeItm09DOC07 = variantTypeItm09DOC07;
		this.variantTypeItm08DOC07 = variantTypeItm08DOC07;
		this.variantTypeItm07DOC07 = variantTypeItm07DOC07;
		this.variantTypeItm06DOC07 = variantTypeItm06DOC07;
		this.variantCodeItm15DOC07 = variantCodeItm15DOC07;
		this.variantCodeItm14DOC07 = variantCodeItm14DOC07;
		this.variantCodeItm13DOC07 = variantCodeItm13DOC07;
		this.variantCodeItm12DOC07 = variantCodeItm12DOC07;
		this.variantCodeItm11DOC07 = variantCodeItm11DOC07;
	}


	public PurchaseDocRowPK() {}



	public String getCompanyCodeSys01DOC07() {
		return companyCodeSys01DOC07;
	}
	public String getDocTypeDOC07() {
		return docTypeDOC07;
	}
	public java.math.BigDecimal getDocYearDOC07() {
		return docYearDOC07;
	}
	public java.math.BigDecimal getDocNumberDOC07() {
		return docNumberDOC07;
	}
	public String getItemCodeItm01DOC07() {
		return itemCodeItm01DOC07;
	}
	public String getVariantCodeItm11DOC07() {
		return variantCodeItm11DOC07;
	}
	public String getVariantCodeItm12DOC07() {
		return variantCodeItm12DOC07;
	}
	public String getVariantCodeItm13DOC07() {
		return variantCodeItm13DOC07;
	}
	public String getVariantCodeItm14DOC07() {
		return variantCodeItm14DOC07;
	}
	public String getVariantCodeItm15DOC07() {
		return variantCodeItm15DOC07;
	}
	public String getVariantTypeItm06DOC07() {
		return variantTypeItm06DOC07;
	}
	public String getVariantTypeItm07DOC07() {
		return variantTypeItm07DOC07;
	}
	public String getVariantTypeItm08DOC07() {
		return variantTypeItm08DOC07;
	}
	public String getVariantTypeItm09DOC07() {
		return variantTypeItm09DOC07;
	}
	public String getVariantTypeItm10DOC07() {
		return variantTypeItm10DOC07;
	}


	public void setCompanyCodeSys01DOC07(String companyCodeSys01DOC07) {
		this.companyCodeSys01DOC07 = companyCodeSys01DOC07;
	}


	public void setDocTypeDOC07(String docTypeDOC07) {
		this.docTypeDOC07 = docTypeDOC07;
	}


	public void setDocYearDOC07(java.math.BigDecimal docYearDOC07) {
		this.docYearDOC07 = docYearDOC07;
	}


	public void setDocNumberDOC07(java.math.BigDecimal docNumberDOC07) {
		this.docNumberDOC07 = docNumberDOC07;
	}


	public void setItemCodeItm01DOC07(String itemCodeItm01DOC07) {
		this.itemCodeItm01DOC07 = itemCodeItm01DOC07;
	}


	public void setVariantTypeItm06DOC07(String variantTypeItm06DOC07) {
		this.variantTypeItm06DOC07 = variantTypeItm06DOC07;
	}


	public void setVariantCodeItm11DOC07(String variantCodeItm11DOC07) {
		this.variantCodeItm11DOC07 = variantCodeItm11DOC07;
	}


	public void setVariantTypeItm07DOC07(String variantTypeItm07DOC07) {
		this.variantTypeItm07DOC07 = variantTypeItm07DOC07;
	}


	public void setVariantCodeItm12DOC07(String variantCodeItm12DOC07) {
		this.variantCodeItm12DOC07 = variantCodeItm12DOC07;
	}


	public void setVariantTypeItm08DOC07(String variantTypeItm08DOC07) {
		this.variantTypeItm08DOC07 = variantTypeItm08DOC07;
	}


	public void setVariantCodeItm13DOC07(String variantCodeItm13DOC07) {
		this.variantCodeItm13DOC07 = variantCodeItm13DOC07;
	}


	public void setVariantTypeItm09DOC07(String variantTypeItm09DOC07) {
		this.variantTypeItm09DOC07 = variantTypeItm09DOC07;
	}


	public void setVariantCodeItm14DOC07(String variantCodeItm14DOC07) {
		this.variantCodeItm14DOC07 = variantCodeItm14DOC07;
	}


	public void setVariantTypeItm10DOC07(String variantTypeItm10DOC07) {
		this.variantTypeItm10DOC07 = variantTypeItm10DOC07;
	}


	public void setVariantCodeItm15DOC07(String variantCodeItm15DOC07) {
		this.variantCodeItm15DOC07 = variantCodeItm15DOC07;
	}



}
