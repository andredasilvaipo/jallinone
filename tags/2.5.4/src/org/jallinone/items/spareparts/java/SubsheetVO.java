package org.jallinone.items.spareparts.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a link between a sheet and its child sheet (table ITM30).</p>
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
public class SubsheetVO extends ValueObjectImpl {

	private String companyCodeSys01ITM30;
	private String parentSheetCodeItm25ITM30;
	private String sheetCodeItm25ITM30;
	private String polygonITM30;


	public String getCompanyCodeSys01ITM30() {
		return companyCodeSys01ITM30;
	}
	public String getSheetCodeItm25ITM30() {
		return sheetCodeItm25ITM30;
	}
	public void setSheetCodeItm25ITM30(String sheetCodeItm25ITM30) {
		this.sheetCodeItm25ITM30 = sheetCodeItm25ITM30;
	}

	public void setCompanyCodeSys01ITM30(String companyCodeSys01ITM30) {
		this.companyCodeSys01ITM30 = companyCodeSys01ITM30;
	}
  public String getParentSheetCodeItm25ITM30() {
    return parentSheetCodeItm25ITM30;
  }
  public String getPolygonITM30() {
    return polygonITM30;
  }
  public void setParentSheetCodeItm25ITM30(String parentSheetCodeItm25ITM30) {
    this.parentSheetCodeItm25ITM30 = parentSheetCodeItm25ITM30;
  }
  public void setPolygonITM30(String polygonITM30) {
    this.polygonITM30 = polygonITM30;
  }




}
