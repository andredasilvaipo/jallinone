package org.jallinone.variants.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the item+variants combination.</p>
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
public class VariantItemPK implements Serializable {

	private String companyCodeSys01;
	private String itemCodeItm01;

	private String variantTypeItm06;
	private String variantCodeItm11;
	private String variantTypeItm07;
	private String variantCodeItm12;
	private String variantTypeItm08;
	private String variantCodeItm13;
	private String variantTypeItm09;
	private String variantCodeItm14;
	private String variantTypeItm10;
	private String variantCodeItm15;

	public VariantItemPK() {}
	

	public VariantItemPK(String companyCodeSys01,String itemCodeItm01,
			String variantTypeItm06,String variantCodeItm11,
			String variantTypeItm07,String variantCodeItm12,
			String variantTypeItm08,String variantCodeItm13,
			String variantTypeItm09,String variantCodeItm14,
			String variantTypeItm10,String variantCodeItm15) {
		this.companyCodeSys01 = companyCodeSys01;
		this.itemCodeItm01 = itemCodeItm01;

		this.variantTypeItm10 = variantTypeItm10;
		this.variantTypeItm09 = variantTypeItm09;
		this.variantTypeItm08 = variantTypeItm08;
		this.variantTypeItm07 = variantTypeItm07;
		this.variantTypeItm06 = variantTypeItm06;
		this.variantCodeItm15 = variantCodeItm15;
		this.variantCodeItm14 = variantCodeItm14;
		this.variantCodeItm13 = variantCodeItm13;
		this.variantCodeItm12 = variantCodeItm12;
		this.variantCodeItm11 = variantCodeItm11;
	}



	public String getCompanyCodeSys01() {
		return companyCodeSys01;
	}
	public String getItemCodeItm01() {
		return itemCodeItm01;
	}
	public String getVariantCodeItm11() {
		return variantCodeItm11;
	}
	public String getVariantCodeItm12() {
		return variantCodeItm12;
	}
	public String getVariantCodeItm13() {
		return variantCodeItm13;
	}
	public String getVariantCodeItm14() {
		return variantCodeItm14;
	}
	public String getVariantCodeItm15() {
		return variantCodeItm15;
	}
	public String getVariantTypeItm06() {
		return variantTypeItm06;
	}
	public String getVariantTypeItm07() {
		return variantTypeItm07;
	}
	public String getVariantTypeItm08() {
		return variantTypeItm08;
	}
	public String getVariantTypeItm09() {
		return variantTypeItm09;
	}
	public String getVariantTypeItm10() {
		return variantTypeItm10;
	}


	public boolean equals(Object o) {
		if (o==null || !(o instanceof VariantItemPK))
			return false;
		VariantItemPK obj = (VariantItemPK)o;
		return
		companyCodeSys01.equals(obj.companyCodeSys01) &&
		itemCodeItm01.equals(obj.itemCodeItm01) &&
		variantTypeItm06.equals(obj.variantTypeItm06) &&
		variantCodeItm11.equals(obj.variantCodeItm11) &&
		variantTypeItm07.equals(obj.variantTypeItm07) &&
		variantCodeItm12.equals(obj.variantCodeItm12) &&
		variantTypeItm08.equals(obj.variantTypeItm08) &&
		variantCodeItm13.equals(obj.variantCodeItm13) &&
		variantTypeItm09.equals(obj.variantTypeItm09) &&
		variantCodeItm14.equals(obj.variantCodeItm14) &&
		variantTypeItm10.equals(obj.variantTypeItm10) &&
		variantCodeItm15.equals(obj.variantCodeItm15);
	}


	public int hashCode() {
		return
		(companyCodeSys01+
				itemCodeItm01+
				variantTypeItm06+
				variantCodeItm11+
				variantTypeItm07+
				variantCodeItm12+
				variantTypeItm08+
				variantCodeItm13+
				variantTypeItm09+
				variantCodeItm14+
				variantTypeItm10+
				variantCodeItm15).hashCode();
	}


	public void setCompanyCodeSys01(String companyCodeSys01) {
		this.companyCodeSys01 = companyCodeSys01;
	}


	public void setItemCodeItm01(String itemCodeItm01) {
		this.itemCodeItm01 = itemCodeItm01;
	}


	public void setVariantTypeItm06(String variantTypeItm06) {
		this.variantTypeItm06 = variantTypeItm06;
	}


	public void setVariantCodeItm11(String variantCodeItm11) {
		this.variantCodeItm11 = variantCodeItm11;
	}


	public void setVariantTypeItm07(String variantTypeItm07) {
		this.variantTypeItm07 = variantTypeItm07;
	}


	public void setVariantCodeItm12(String variantCodeItm12) {
		this.variantCodeItm12 = variantCodeItm12;
	}


	public void setVariantTypeItm08(String variantTypeItm08) {
		this.variantTypeItm08 = variantTypeItm08;
	}


	public void setVariantCodeItm13(String variantCodeItm13) {
		this.variantCodeItm13 = variantCodeItm13;
	}


	public void setVariantTypeItm09(String variantTypeItm09) {
		this.variantTypeItm09 = variantTypeItm09;
	}


	public void setVariantCodeItm14(String variantCodeItm14) {
		this.variantCodeItm14 = variantCodeItm14;
	}


	public void setVariantTypeItm10(String variantTypeItm10) {
		this.variantTypeItm10 = variantTypeItm10;
	}


	public void setVariantCodeItm15(String variantCodeItm15) {
		this.variantCodeItm15 = variantCodeItm15;
	}



  
}
