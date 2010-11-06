package org.jallinone.warehouse.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store in delivery note row info.</p>
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
public class OutDeliveryNoteRowPK implements Serializable {

	private String companyCodeSys01DOC10;
	private String docTypeDOC10;
	private java.math.BigDecimal docYearDOC10;
	private java.math.BigDecimal docNumberDOC10;
	private java.math.BigDecimal docNumberDoc01DOC10;
	private java.math.BigDecimal docYearDoc01DOC10;
	private String docTypeDoc01DOC10;
	private java.math.BigDecimal rowNumberDOC10;
	private String itemCodeItm01DOC10;

	private java.math.BigDecimal progressiveDOC10;
	private String variantTypeItm06DOC10;
	private String variantCodeItm11DOC10;
	private String variantTypeItm07DOC10;
	private String variantCodeItm12DOC10;
	private String variantTypeItm08DOC10;
	private String variantCodeItm13DOC10;
	private String variantTypeItm09DOC10;
	private String variantCodeItm14DOC10;
	private String variantTypeItm10DOC10;
	private String variantCodeItm15DOC10;


	public OutDeliveryNoteRowPK(
			java.math.BigDecimal progressiveDOC10,
			String companyCodeSys01DOC10,String docTypeDOC10,java.math.BigDecimal docYearDOC10,java.math.BigDecimal docNumberDOC10,
			String docTypeDoc01DOC10,java.math.BigDecimal docYearDoc01DOC10,java.math.BigDecimal docNumberDoc01DOC10,
			java.math.BigDecimal rowNumberDOC10,String itemCodeItm01DOC10,
			String variantTypeItm06DOC10,
			String variantCodeItm11DOC10,
			String variantTypeItm07DOC10,
			String variantCodeItm12DOC10,
			String variantTypeItm08DOC10,
			String variantCodeItm13DOC10,
			String variantTypeItm09DOC10,
			String variantCodeItm14DOC10,
			String variantTypeItm10DOC10,
			String variantCodeItm15DOC10
	) {
		this.progressiveDOC10 = progressiveDOC10;
		this.companyCodeSys01DOC10 = companyCodeSys01DOC10;
		this.docTypeDOC10 = docTypeDOC10;
		this.docYearDOC10 = docYearDOC10;
		this.docNumberDOC10 = docNumberDOC10;
		this.docTypeDoc01DOC10 = docTypeDoc01DOC10;
		this.docYearDoc01DOC10 = docYearDoc01DOC10;
		this.docNumberDoc01DOC10 = docNumberDoc01DOC10;
		this.rowNumberDOC10 = rowNumberDOC10;
		this.itemCodeItm01DOC10 = itemCodeItm01DOC10;

		this.variantTypeItm10DOC10 = variantTypeItm10DOC10;
		this.variantTypeItm09DOC10 = variantTypeItm09DOC10;
		this.variantTypeItm08DOC10 = variantTypeItm08DOC10;
		this.variantTypeItm07DOC10 = variantTypeItm07DOC10;
		this.variantTypeItm06DOC10 = variantTypeItm06DOC10;
		this.variantCodeItm15DOC10 = variantCodeItm15DOC10;
		this.variantCodeItm14DOC10 = variantCodeItm14DOC10;
		this.variantCodeItm13DOC10 = variantCodeItm13DOC10;
		this.variantCodeItm12DOC10 = variantCodeItm12DOC10;
		this.variantCodeItm11DOC10 = variantCodeItm11DOC10;

	}

	public OutDeliveryNoteRowPK() {}


	public String getCompanyCodeSys01DOC10() {
		return companyCodeSys01DOC10;
	}
	public String getDocTypeDOC10() {
		return docTypeDOC10;
	}
	public java.math.BigDecimal getDocYearDOC10() {
		return docYearDOC10;
	}
	public java.math.BigDecimal getDocNumberDOC10() {
		return docNumberDOC10;
	}
	public java.math.BigDecimal getDocNumberDoc01DOC10() {
		return docNumberDoc01DOC10;
	}
	public java.math.BigDecimal getDocYearDoc01DOC10() {
		return docYearDoc01DOC10;
	}
	public String getDocTypeDoc01DOC10() {
		return docTypeDoc01DOC10;
	}
	public String getItemCodeItm01DOC10() {
		return itemCodeItm01DOC10;
	}
	public java.math.BigDecimal getRowNumberDOC10() {
		return rowNumberDOC10;
	}
	public java.math.BigDecimal getProgressiveDOC10() {
		return progressiveDOC10;
	}
	public String getVariantCodeItm11DOC10() {
		return variantCodeItm11DOC10;
	}
	public String getVariantCodeItm13DOC10() {
		return variantCodeItm13DOC10;
	}
	public String getVariantCodeItm12DOC10() {
		return variantCodeItm12DOC10;
	}
	public String getVariantCodeItm15DOC10() {
		return variantCodeItm15DOC10;
	}
	public String getVariantCodeItm14DOC10() {
		return variantCodeItm14DOC10;
	}
	public String getVariantTypeItm06DOC10() {
		return variantTypeItm06DOC10;
	}
	public String getVariantTypeItm07DOC10() {
		return variantTypeItm07DOC10;
	}
	public String getVariantTypeItm08DOC10() {
		return variantTypeItm08DOC10;
	}
	public String getVariantTypeItm09DOC10() {
		return variantTypeItm09DOC10;
	}
	public String getVariantTypeItm10DOC10() {
		return variantTypeItm10DOC10;
	}

	public void setCompanyCodeSys01DOC10(String companyCodeSys01DOC10) {
		this.companyCodeSys01DOC10 = companyCodeSys01DOC10;
	}

	public void setDocTypeDOC10(String docTypeDOC10) {
		this.docTypeDOC10 = docTypeDOC10;
	}

	public void setDocYearDOC10(java.math.BigDecimal docYearDOC10) {
		this.docYearDOC10 = docYearDOC10;
	}

	public void setDocNumberDOC10(java.math.BigDecimal docNumberDOC10) {
		this.docNumberDOC10 = docNumberDOC10;
	}

	public void setDocNumberDoc01DOC10(java.math.BigDecimal docNumberDoc01DOC10) {
		this.docNumberDoc01DOC10 = docNumberDoc01DOC10;
	}

	public void setDocYearDoc01DOC10(java.math.BigDecimal docYearDoc01DOC10) {
		this.docYearDoc01DOC10 = docYearDoc01DOC10;
	}

	public void setDocTypeDoc01DOC10(String docTypeDoc01DOC10) {
		this.docTypeDoc01DOC10 = docTypeDoc01DOC10;
	}

	public void setRowNumberDOC10(java.math.BigDecimal rowNumberDOC10) {
		this.rowNumberDOC10 = rowNumberDOC10;
	}

	public void setItemCodeItm01DOC10(String itemCodeItm01DOC10) {
		this.itemCodeItm01DOC10 = itemCodeItm01DOC10;
	}

	public void setProgressiveDOC10(java.math.BigDecimal progressiveDOC10) {
		this.progressiveDOC10 = progressiveDOC10;
	}

	public void setVariantTypeItm06DOC10(String variantTypeItm06DOC10) {
		this.variantTypeItm06DOC10 = variantTypeItm06DOC10;
	}

	public void setVariantCodeItm11DOC10(String variantCodeItm11DOC10) {
		this.variantCodeItm11DOC10 = variantCodeItm11DOC10;
	}

	public void setVariantTypeItm07DOC10(String variantTypeItm07DOC10) {
		this.variantTypeItm07DOC10 = variantTypeItm07DOC10;
	}

	public void setVariantCodeItm12DOC10(String variantCodeItm12DOC10) {
		this.variantCodeItm12DOC10 = variantCodeItm12DOC10;
	}

	public void setVariantTypeItm08DOC10(String variantTypeItm08DOC10) {
		this.variantTypeItm08DOC10 = variantTypeItm08DOC10;
	}

	public void setVariantCodeItm13DOC10(String variantCodeItm13DOC10) {
		this.variantCodeItm13DOC10 = variantCodeItm13DOC10;
	}

	public void setVariantTypeItm09DOC10(String variantTypeItm09DOC10) {
		this.variantTypeItm09DOC10 = variantTypeItm09DOC10;
	}

	public void setVariantCodeItm14DOC10(String variantCodeItm14DOC10) {
		this.variantCodeItm14DOC10 = variantCodeItm14DOC10;
	}

	public void setVariantTypeItm10DOC10(String variantTypeItm10DOC10) {
		this.variantTypeItm10DOC10 = variantTypeItm10DOC10;
	}

	public void setVariantCodeItm15DOC10(String variantCodeItm15DOC10) {
		this.variantCodeItm15DOC10 = variantCodeItm15DOC10;
	}



  
}
