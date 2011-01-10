package org.jallinone.items.spareparts.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a spare part linked to an item (i.e. to a root level sheet)</p>
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
public class ItemSparePartVO extends ValueObjectImpl {

	private String companyCodeSys01ITM28;
	private String rootSheetCodeItm25ITM28;
	private String itemCodeItm01ITM28;
	private String descriptionSYS10;
	private BigDecimal progressiveHie02ITM01;


	public String getCompanyCodeSys01ITM28() {
		return companyCodeSys01ITM28;
	}
	public String getDescriptionSYS10() {
		return descriptionSYS10;
	}
	public String getItemCodeItm01ITM28() {
		return itemCodeItm01ITM28;
	}

	public void setItemCodeItm01ITM28(String itemCodeItm01ITM28) {
		this.itemCodeItm01ITM28 = itemCodeItm01ITM28;
	}
	public void setDescriptionSYS10(String descriptionSYS10) {
		this.descriptionSYS10 = descriptionSYS10;
	}
	public void setCompanyCodeSys01ITM28(String companyCodeSys01ITM28) {
		this.companyCodeSys01ITM28 = companyCodeSys01ITM28;
	}
  public String getRootSheetCodeItm25ITM28() {
    return rootSheetCodeItm25ITM28;
  }
  public void setRootSheetCodeItm25ITM28(String rootSheetCodeItm25ITM28) {
    this.rootSheetCodeItm25ITM28 = rootSheetCodeItm25ITM28;
  }
  public BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }



}
