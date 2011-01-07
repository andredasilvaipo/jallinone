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
public class InDeliveryNoteRowPK implements Serializable {

	private String companyCodeSys01DOC09;
	private String docTypeDOC09;
	private java.math.BigDecimal docYearDOC09;
	private java.math.BigDecimal docNumberDOC09;
	private java.math.BigDecimal docNumberDoc06DOC09;
	private java.math.BigDecimal docYearDoc06DOC09;
	private String docTypeDoc06DOC09;
	private java.math.BigDecimal rowNumberDOC09;
	private String itemCodeItm01DOC09;

	private java.math.BigDecimal progressiveDOC09;
	private String variantTypeItm06DOC09;
	private String variantCodeItm11DOC09;
	private String variantTypeItm07DOC09;
	private String variantCodeItm12DOC09;
	private String variantTypeItm08DOC09;
	private String variantCodeItm13DOC09;
	private String variantTypeItm09DOC09;
	private String variantCodeItm14DOC09;
	private String variantTypeItm10DOC09;
	private String variantCodeItm15DOC09;

	
	
	public InDeliveryNoteRowPK(
			java.math.BigDecimal progressiveDOC09,
			String companyCodeSys01DOC09,String docTypeDOC09,java.math.BigDecimal docYearDOC09,java.math.BigDecimal docNumberDOC09,
			String docTypeDoc06DOC09,java.math.BigDecimal docYearDoc06DOC09,java.math.BigDecimal docNumberDoc06DOC09,
			java.math.BigDecimal rowNumberDOC09,String itemCodeItm01DOC09,
			String variantTypeItm06DOC09,
			String variantCodeItm11DOC09,
			String variantTypeItm07DOC09,
			String variantCodeItm12DOC09,
			String variantTypeItm08DOC09,
			String variantCodeItm13DOC09,
			String variantTypeItm09DOC09,
			String variantCodeItm14DOC09,
			String variantTypeItm10DOC09,
			String variantCodeItm15DOC09
	) {
		this.progressiveDOC09 = progressiveDOC09;
		this.companyCodeSys01DOC09 = companyCodeSys01DOC09;
		this.docTypeDOC09 = docTypeDOC09;
		this.docYearDOC09 = docYearDOC09;
		this.docNumberDOC09 = docNumberDOC09;
		this.docTypeDoc06DOC09 = docTypeDoc06DOC09;
		this.docYearDoc06DOC09 = docYearDoc06DOC09;
		this.docNumberDoc06DOC09 = docNumberDoc06DOC09;
		this.rowNumberDOC09 = rowNumberDOC09;
		this.itemCodeItm01DOC09 = itemCodeItm01DOC09;

		this.variantTypeItm10DOC09 = variantTypeItm10DOC09;
		this.variantTypeItm09DOC09 = variantTypeItm09DOC09;
		this.variantTypeItm08DOC09 = variantTypeItm08DOC09;
		this.variantTypeItm07DOC09 = variantTypeItm07DOC09;
		this.variantTypeItm06DOC09 = variantTypeItm06DOC09;
		this.variantCodeItm15DOC09 = variantCodeItm15DOC09;
		this.variantCodeItm14DOC09 = variantCodeItm14DOC09;
		this.variantCodeItm13DOC09 = variantCodeItm13DOC09;
		this.variantCodeItm12DOC09 = variantCodeItm12DOC09;
		this.variantCodeItm11DOC09 = variantCodeItm11DOC09;
	}


	public InDeliveryNoteRowPK() {}


	public String getCompanyCodeSys01DOC09() {
		return companyCodeSys01DOC09;
	}
	public String getDocTypeDOC09() {
		return docTypeDOC09;
	}
	public java.math.BigDecimal getDocYearDOC09() {
		return docYearDOC09;
	}
	public java.math.BigDecimal getDocNumberDOC09() {
		return docNumberDOC09;
	}
	public java.math.BigDecimal getDocNumberDoc06DOC09() {
		return docNumberDoc06DOC09;
	}
	public java.math.BigDecimal getDocYearDoc06DOC09() {
		return docYearDoc06DOC09;
	}
	public String getDocTypeDoc06DOC09() {
		return docTypeDoc06DOC09;
	}
	public String getItemCodeItm01DOC09() {
		return itemCodeItm01DOC09;
	}
	public java.math.BigDecimal getRowNumberDOC09() {
		return rowNumberDOC09;
	}
	public String getVariantCodeItm11DOC09() {
		return variantCodeItm11DOC09;
	}
	public String getVariantCodeItm12DOC09() {
		return variantCodeItm12DOC09;
	}
	public String getVariantCodeItm13DOC09() {
		return variantCodeItm13DOC09;
	}
	public String getVariantCodeItm14DOC09() {
		return variantCodeItm14DOC09;
	}
	public String getVariantCodeItm15DOC09() {
		return variantCodeItm15DOC09;
	}
	public String getVariantTypeItm06DOC09() {
		return variantTypeItm06DOC09;
	}
	public String getVariantTypeItm07DOC09() {
		return variantTypeItm07DOC09;
	}
	public String getVariantTypeItm08DOC09() {
		return variantTypeItm08DOC09;
	}
	public String getVariantTypeItm09DOC09() {
		return variantTypeItm09DOC09;
	}
	public String getVariantTypeItm10DOC09() {
		return variantTypeItm10DOC09;
	}
	public java.math.BigDecimal getProgressiveDOC09() {
		return progressiveDOC09;
	}
	public void setProgressiveDOC09(java.math.BigDecimal progressiveDOC09) {
		this.progressiveDOC09 = progressiveDOC09;
	}


	public void setCompanyCodeSys01DOC09(String companyCodeSys01DOC09) {
		this.companyCodeSys01DOC09 = companyCodeSys01DOC09;
	}


	public void setDocTypeDOC09(String docTypeDOC09) {
		this.docTypeDOC09 = docTypeDOC09;
	}


	public void setDocYearDOC09(java.math.BigDecimal docYearDOC09) {
		this.docYearDOC09 = docYearDOC09;
	}


	public void setDocNumberDOC09(java.math.BigDecimal docNumberDOC09) {
		this.docNumberDOC09 = docNumberDOC09;
	}


	public void setDocNumberDoc06DOC09(java.math.BigDecimal docNumberDoc06DOC09) {
		this.docNumberDoc06DOC09 = docNumberDoc06DOC09;
	}


	public void setDocYearDoc06DOC09(java.math.BigDecimal docYearDoc06DOC09) {
		this.docYearDoc06DOC09 = docYearDoc06DOC09;
	}


	public void setDocTypeDoc06DOC09(String docTypeDoc06DOC09) {
		this.docTypeDoc06DOC09 = docTypeDoc06DOC09;
	}


	public void setRowNumberDOC09(java.math.BigDecimal rowNumberDOC09) {
		this.rowNumberDOC09 = rowNumberDOC09;
	}


	public void setItemCodeItm01DOC09(String itemCodeItm01DOC09) {
		this.itemCodeItm01DOC09 = itemCodeItm01DOC09;
	}


	public void setVariantTypeItm06DOC09(String variantTypeItm06DOC09) {
		this.variantTypeItm06DOC09 = variantTypeItm06DOC09;
	}


	public void setVariantCodeItm11DOC09(String variantCodeItm11DOC09) {
		this.variantCodeItm11DOC09 = variantCodeItm11DOC09;
	}


	public void setVariantTypeItm07DOC09(String variantTypeItm07DOC09) {
		this.variantTypeItm07DOC09 = variantTypeItm07DOC09;
	}


	public void setVariantCodeItm12DOC09(String variantCodeItm12DOC09) {
		this.variantCodeItm12DOC09 = variantCodeItm12DOC09;
	}


	public void setVariantTypeItm08DOC09(String variantTypeItm08DOC09) {
		this.variantTypeItm08DOC09 = variantTypeItm08DOC09;
	}


	public void setVariantCodeItm13DOC09(String variantCodeItm13DOC09) {
		this.variantCodeItm13DOC09 = variantCodeItm13DOC09;
	}


	public void setVariantTypeItm09DOC09(String variantTypeItm09DOC09) {
		this.variantTypeItm09DOC09 = variantTypeItm09DOC09;
	}


	public void setVariantCodeItm14DOC09(String variantCodeItm14DOC09) {
		this.variantCodeItm14DOC09 = variantCodeItm14DOC09;
	}


	public void setVariantTypeItm10DOC09(String variantTypeItm10DOC09) {
		this.variantTypeItm10DOC09 = variantTypeItm10DOC09;
	}


	public void setVariantCodeItm15DOC09(String variantCodeItm15DOC09) {
		this.variantCodeItm15DOC09 = variantCodeItm15DOC09;
	}


  
  
}
