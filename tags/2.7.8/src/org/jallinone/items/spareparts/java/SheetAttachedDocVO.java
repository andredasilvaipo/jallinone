package org.jallinone.items.spareparts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an attached document, linked to a sheet (table ITM26).</p>
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
public class SheetAttachedDocVO extends ValueObjectImpl {

	private java.math.BigDecimal progressiveDoc14ITM26;
	private String companyCodeSys01ITM26;
	private String descriptionDOC14;
	private java.math.BigDecimal progressiveHie02HIE01;
	private java.math.BigDecimal progressiveHie01ITM26;
	private String sheetCodeItm25ITM26;


	public SheetAttachedDocVO() {
	}


	public String getSheetCodeItm25ITM26() {
    return sheetCodeItm25ITM26;
	}
	public void setSheetCodeItm25ITM26(String sheetCodeItm25ITM26) {
    this.sheetCodeItm25ITM26 = sheetCodeItm25ITM26;
	}
	public java.math.BigDecimal getProgressiveDoc14ITM26() {
		return progressiveDoc14ITM26;
	}
	public void setProgressiveDoc14ITM26(java.math.BigDecimal progressiveDoc14ITM26) {
		this.progressiveDoc14ITM26 = progressiveDoc14ITM26;
	}
	public String getCompanyCodeSys01ITM26() {
		return companyCodeSys01ITM26;
	}
	public void setCompanyCodeSys01ITM26(String companyCodeSys01ITM26) {
		this.companyCodeSys01ITM26 = companyCodeSys01ITM26;
	}
	public String getDescriptionDOC14() {
		return descriptionDOC14;
	}
	public void setDescriptionDOC14(String descriptionDOC14) {
		this.descriptionDOC14 = descriptionDOC14;
	}
	public java.math.BigDecimal getProgressiveHie02HIE01() {
		return progressiveHie02HIE01;
	}
	public void setProgressiveHie02HIE01(java.math.BigDecimal progressiveHie02HIE01) {
		this.progressiveHie02HIE01 = progressiveHie02HIE01;
	}
	public java.math.BigDecimal getProgressiveHie01ITM26() {
		return progressiveHie01ITM26;
	}
	public void setProgressiveHie01ITM26(java.math.BigDecimal progressiveHie01ITM26) {
		this.progressiveHie01ITM26 = progressiveHie01ITM26;
	}


}
