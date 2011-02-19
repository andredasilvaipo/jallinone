package org.jallinone.items.spareparts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store items model.</p>
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
public class ItemSheetVO extends ValueObjectImpl {

	private String companyCodeSys01ITM25;
	private String sheetCodeITM25;
	private String descriptionSYS10;
	private java.math.BigDecimal levITM25;
	private java.math.BigDecimal progressiveSys10ITM25;
	private String imageNameITM25;
	private byte[] imageITM25;

	private String enabledITM25;

	private String sProp0ITM25;
	private String sProp1ITM25;
	private String sProp2ITM25;
	private String sProp3ITM25;
	private String sProp4ITM25;
	private String sProp5ITM25;
	private String sProp6ITM25;
	private String sProp7ITM25;
	private String sProp8ITM25;
	private String sProp9ITM25;

	private java.sql.Date dProp0ITM25;
	private java.sql.Date dProp1ITM25;
	private java.sql.Date dProp2ITM25;
	private java.sql.Date dProp3ITM25;
	private java.sql.Date dProp4ITM25;
	private java.sql.Date dProp5ITM25;
	private java.sql.Date dProp6ITM25;
	private java.sql.Date dProp7ITM25;
	private java.sql.Date dProp8ITM25;
	private java.sql.Date dProp9ITM25;

	private java.math.BigDecimal nProp0ITM25;
	private java.math.BigDecimal nProp1ITM25;
	private java.math.BigDecimal nProp2ITM25;
	private java.math.BigDecimal nProp3ITM25;
	private java.math.BigDecimal nProp4ITM25;
	private java.math.BigDecimal nProp5ITM25;
	private java.math.BigDecimal nProp6ITM25;
	private java.math.BigDecimal nProp7ITM25;
	private java.math.BigDecimal nProp8ITM25;
	private java.math.BigDecimal nProp9ITM25;

  private SubsheetVO subsheet;


	public ItemSheetVO() {
	}


	public String getCompanyCodeSys01ITM25() {
		return companyCodeSys01ITM25;
	}
	public void setCompanyCodeSys01ITM25(String companyCodeSys01ITM25) {
		this.companyCodeSys01ITM25 = companyCodeSys01ITM25;
	}
	public String getDescriptionSYS10() {
		return descriptionSYS10;
	}
	public void setDescriptionSYS10(String descriptionSYS10) {
		this.descriptionSYS10 = descriptionSYS10;
	}
  public java.sql.Date getDProp0ITM25() {
    return dProp0ITM25;
  }
  public java.sql.Date getDProp1ITM25() {
    return dProp1ITM25;
  }
  public java.sql.Date getDProp2ITM25() {
    return dProp2ITM25;
  }
  public java.sql.Date getDProp3ITM25() {
    return dProp3ITM25;
  }
  public java.sql.Date getDProp4ITM25() {
    return dProp4ITM25;
  }
  public java.sql.Date getDProp5ITM25() {
    return dProp5ITM25;
  }
  public java.sql.Date getDProp6ITM25() {
    return dProp6ITM25;
  }
  public java.sql.Date getDProp7ITM25() {
    return dProp7ITM25;
  }
  public java.sql.Date getDProp8ITM25() {
    return dProp8ITM25;
  }
  public java.sql.Date getDProp9ITM25() {
    return dProp9ITM25;
  }
  public byte[] getImageITM25() {
    return imageITM25;
  }
  public String getImageNameITM25() {
    return imageNameITM25;
  }
  public java.math.BigDecimal getLevITM25() {
    return levITM25;
  }
  public java.math.BigDecimal getNProp0ITM25() {
    return nProp0ITM25;
  }
  public java.math.BigDecimal getNProp1ITM25() {
    return nProp1ITM25;
  }
  public java.math.BigDecimal getNProp2ITM25() {
    return nProp2ITM25;
  }
  public java.math.BigDecimal getNProp3ITM25() {
    return nProp3ITM25;
  }
  public java.math.BigDecimal getNProp4ITM25() {
    return nProp4ITM25;
  }
  public java.math.BigDecimal getNProp5ITM25() {
    return nProp5ITM25;
  }
  public java.math.BigDecimal getNProp6ITM25() {
    return nProp6ITM25;
  }
  public java.math.BigDecimal getNProp7ITM25() {
    return nProp7ITM25;
  }
  public java.math.BigDecimal getNProp8ITM25() {
    return nProp8ITM25;
  }
  public java.math.BigDecimal getNProp9ITM25() {
    return nProp9ITM25;
  }

  public java.math.BigDecimal getProgressiveSys10ITM25() {
    return progressiveSys10ITM25;
  }
  public String getSheetCodeITM25() {
    return sheetCodeITM25;
  }
  public String getSProp0ITM25() {
    return sProp0ITM25;
  }
  public String getSProp1ITM25() {
    return sProp1ITM25;
  }
  public String getSProp2ITM25() {
    return sProp2ITM25;
  }
  public String getSProp3ITM25() {
    return sProp3ITM25;
  }
  public String getSProp4ITM25() {
    return sProp4ITM25;
  }
  public String getSProp5ITM25() {
    return sProp5ITM25;
  }
  public String getSProp6ITM25() {
    return sProp6ITM25;
  }
  public String getSProp7ITM25() {
    return sProp7ITM25;
  }
  public String getSProp8ITM25() {
    return sProp8ITM25;
  }
  public String getSProp9ITM25() {
    return sProp9ITM25;
  }
  public void setSProp9ITM25(String sProp9ITM25) {
    this.sProp9ITM25 = sProp9ITM25;
  }
  public void setSProp8ITM25(String sProp8ITM25) {
    this.sProp8ITM25 = sProp8ITM25;
  }
  public void setSProp7ITM25(String sProp7ITM25) {
    this.sProp7ITM25 = sProp7ITM25;
  }
  public void setSProp6ITM25(String sProp6ITM25) {
    this.sProp6ITM25 = sProp6ITM25;
  }
  public void setSProp5ITM25(String sProp5ITM25) {
    this.sProp5ITM25 = sProp5ITM25;
  }
  public void setSProp4ITM25(String sProp4ITM25) {
    this.sProp4ITM25 = sProp4ITM25;
  }
  public void setSProp3ITM25(String sProp3ITM25) {
    this.sProp3ITM25 = sProp3ITM25;
  }
  public void setSProp2ITM25(String sProp2ITM25) {
    this.sProp2ITM25 = sProp2ITM25;
  }
  public void setSProp1ITM25(String sProp1ITM25) {
    this.sProp1ITM25 = sProp1ITM25;
  }
  public void setDProp0ITM25(java.sql.Date DProp0ITM25) {
    this.dProp0ITM25 = DProp0ITM25;
  }
  public void setDProp1ITM25(java.sql.Date DProp1ITM25) {
    this.dProp1ITM25 = DProp1ITM25;
  }
  public void setDProp2ITM25(java.sql.Date DProp2ITM25) {
    this.dProp2ITM25 = DProp2ITM25;
  }
  public void setDProp3ITM25(java.sql.Date DProp3ITM25) {
    this.dProp3ITM25 = DProp3ITM25;
  }
  public void setDProp4ITM25(java.sql.Date DProp4ITM25) {
    this.dProp4ITM25 = DProp4ITM25;
  }
  public void setDProp5ITM25(java.sql.Date DProp5ITM25) {
    this.dProp5ITM25 = DProp5ITM25;
  }
  public void setDProp6ITM25(java.sql.Date DProp6ITM25) {
    this.dProp6ITM25 = DProp6ITM25;
  }
  public void setDProp7ITM25(java.sql.Date DProp7ITM25) {
    this.dProp7ITM25 = DProp7ITM25;
  }
  public void setDProp8ITM25(java.sql.Date DProp8ITM25) {
    this.dProp8ITM25 = DProp8ITM25;
  }
  public void setDProp9ITM25(java.sql.Date DProp9ITM25) {
    this.dProp9ITM25 = DProp9ITM25;
  }
  public void setImageITM25(byte[] imageITM25) {
    this.imageITM25 = imageITM25;
  }
  public void setImageNameITM25(String imageNameITM25) {
    this.imageNameITM25 = imageNameITM25;
  }
  public void setLevITM25(java.math.BigDecimal levITM25) {
    this.levITM25 = levITM25;
  }
  public void setNProp0ITM25(java.math.BigDecimal NProp0ITM25) {
    this.nProp0ITM25 = NProp0ITM25;
  }
  public void setNProp1ITM25(java.math.BigDecimal NProp1ITM25) {
    this.nProp1ITM25 = NProp1ITM25;
  }
  public void setNProp2ITM25(java.math.BigDecimal NProp2ITM25) {
    this.nProp2ITM25 = NProp2ITM25;
  }
  public void setNProp3ITM25(java.math.BigDecimal NProp3ITM25) {
    this.nProp3ITM25 = NProp3ITM25;
  }
  public void setNProp4ITM25(java.math.BigDecimal NProp4ITM25) {
    this.nProp4ITM25 = NProp4ITM25;
  }
  public void setNProp5ITM25(java.math.BigDecimal NProp5ITM25) {
    this.nProp5ITM25 = NProp5ITM25;
  }
  public void setNProp6ITM25(java.math.BigDecimal NProp6ITM25) {
    this.nProp6ITM25 = NProp6ITM25;
  }
  public void setNProp7ITM25(java.math.BigDecimal NProp7ITM25) {
    this.nProp7ITM25 = NProp7ITM25;
  }
  public void setNProp8ITM25(java.math.BigDecimal NProp8ITM25) {
    this.nProp8ITM25 = NProp8ITM25;
  }
  public void setNProp9ITM25(java.math.BigDecimal NProp9ITM25) {
    this.nProp9ITM25 = NProp9ITM25;
  }
  public void setProgressiveSys10ITM25(java.math.BigDecimal progressiveSys10ITM25) {
    this.progressiveSys10ITM25 = progressiveSys10ITM25;
  }
  public void setSheetCodeITM25(String sheetCodeITM25) {
    this.sheetCodeITM25 = sheetCodeITM25;
  }
  public void setSProp0ITM25(String SProp0ITM25) {
    this.sProp0ITM25 = SProp0ITM25;
  }

		public void setEnabledITM25(String enabledITM25) {
			this.enabledITM25 = enabledITM25;
		}

		public String getEnabledITM25() {
			return this.enabledITM25;
		}


		public SubsheetVO getSubsheet() {
			return this.subsheet;
		}

		public void setSubsheet(SubsheetVO subsheet) {
			this.subsheet = subsheet;
		}



		public final int hashCode() {
			return companyCodeSys01ITM25.hashCode()+sheetCodeITM25.hashCode();
		}


		public final boolean equals(Object o) {
			if (!(o instanceof ItemSheetVO))
				return false;
			ItemSheetVO oo = (ItemSheetVO)o;
			return
				companyCodeSys01ITM25.equals(oo.getCompanyCodeSys01ITM25()) &&
				sheetCodeITM25.equals(oo.getSheetCodeITM25());
		}

}
