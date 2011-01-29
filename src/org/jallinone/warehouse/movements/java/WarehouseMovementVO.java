package org.jallinone.warehouse.movements.java;

import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to execute  a warehouse movement.</p>
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
public class WarehouseMovementVO extends ValueObjectImpl {

	private java.math.BigDecimal progressiveHie01WAR02;
	private java.math.BigDecimal deltaQtyWAR02;
	private String companyCodeSys01WAR02;
	private String warehouseCodeWar01WAR02;
	private String itemCodeItm01WAR02;
	private String warehouseMotiveWar04WAR02;
	private String itemTypeWAR04;
	private String noteWAR02;
	private ArrayList serialNumbers;

	private String variantTypeItm06WAR02;
	private String variantCodeItm11WAR02;
	private String variantTypeItm07WAR02;
	private String variantCodeItm12WAR02;
	private String variantTypeItm08WAR02;
	private String variantCodeItm13WAR02;
	private String variantTypeItm09WAR02;
	private String variantCodeItm14WAR02;
	private String variantTypeItm10WAR02;
	private String variantCodeItm15WAR02;


	public WarehouseMovementVO() {}


	public WarehouseMovementVO(java.math.BigDecimal progressiveHie01WAR02,java.math.BigDecimal deltaQtyWAR02,String companyCodeSys01WAR02,
			String warehouseCodeWar01WAR02,String itemCodeItm01WAR02,String warehouseMotiveWar04WAR02,
			String itemTypeWAR04,String noteWAR02,ArrayList serialNumbers,
			String variantCodeItm11WAR02,
			String variantCodeItm12WAR02,
			String variantCodeItm13WAR02,
			String variantCodeItm14WAR02,
			String variantCodeItm15WAR02,
			String variantTypeItm06WAR02,
			String variantTypeItm07WAR02,
			String variantTypeItm08WAR02,
			String variantTypeItm09WAR02,
			String variantTypeItm10WAR02
	) {
		this.progressiveHie01WAR02 = progressiveHie01WAR02;
		this.deltaQtyWAR02 = deltaQtyWAR02;
		this.companyCodeSys01WAR02 = companyCodeSys01WAR02;
		this.warehouseCodeWar01WAR02 = warehouseCodeWar01WAR02;
		this.itemCodeItm01WAR02 = itemCodeItm01WAR02;
		this.warehouseMotiveWar04WAR02 = warehouseMotiveWar04WAR02;
		this.itemTypeWAR04 = itemTypeWAR04;
		this.noteWAR02 = noteWAR02;
		this.serialNumbers = serialNumbers;

		this.variantTypeItm10WAR02 = variantTypeItm10WAR02;
		this.variantTypeItm09WAR02 = variantTypeItm09WAR02;
		this.variantTypeItm08WAR02 = variantTypeItm08WAR02;
		this.variantTypeItm07WAR02 = variantTypeItm07WAR02;
		this.variantTypeItm06WAR02 = variantTypeItm06WAR02;
		this.variantCodeItm15WAR02 = variantCodeItm15WAR02;
		this.variantCodeItm14WAR02 = variantCodeItm14WAR02;
		this.variantCodeItm13WAR02 = variantCodeItm13WAR02;
		this.variantCodeItm12WAR02 = variantCodeItm12WAR02;
		this.variantCodeItm11WAR02 = variantCodeItm11WAR02;

	}



	public java.math.BigDecimal getProgressiveHie01WAR02() {
		return progressiveHie01WAR02;
	}
	public java.math.BigDecimal getDeltaQtyWAR02() {
		return deltaQtyWAR02;
	}
	public String getCompanyCodeSys01WAR02() {
		return companyCodeSys01WAR02;
	}
	public String getWarehouseCodeWar01WAR02() {
		return warehouseCodeWar01WAR02;
	}
	public String getItemCodeItm01WAR02() {
		return itemCodeItm01WAR02;
	}
	public String getWarehouseMotiveWar04WAR02() {
		return warehouseMotiveWar04WAR02;
	}
	public String getItemTypeWAR04() {
		return itemTypeWAR04;
	}
	public String getNoteWAR02() {
		return noteWAR02;
	}
	public ArrayList getSerialNumbers() {
		return serialNumbers;
	}
	public String getVariantCodeItm11WAR02() {
		return variantCodeItm11WAR02;
	}
	public String getVariantCodeItm12WAR02() {
		return variantCodeItm12WAR02;
	}
	public String getVariantCodeItm13WAR02() {
		return variantCodeItm13WAR02;
	}
	public String getVariantCodeItm14WAR02() {
		return variantCodeItm14WAR02;
	}
	public String getVariantCodeItm15WAR02() {
		return variantCodeItm15WAR02;
	}
	public String getVariantTypeItm06WAR02() {
		return variantTypeItm06WAR02;
	}
	public String getVariantTypeItm07WAR02() {
		return variantTypeItm07WAR02;
	}
	public String getVariantTypeItm08WAR02() {
		return variantTypeItm08WAR02;
	}
	public String getVariantTypeItm09WAR02() {
		return variantTypeItm09WAR02;
	}
	public String getVariantTypeItm10WAR02() {
		return variantTypeItm10WAR02;
	}


	public void setProgressiveHie01WAR02(java.math.BigDecimal progressiveHie01WAR02) {
		this.progressiveHie01WAR02 = progressiveHie01WAR02;
	}


	public void setDeltaQtyWAR02(java.math.BigDecimal deltaQtyWAR02) {
		this.deltaQtyWAR02 = deltaQtyWAR02;
	}


	public void setCompanyCodeSys01WAR02(String companyCodeSys01WAR02) {
		this.companyCodeSys01WAR02 = companyCodeSys01WAR02;
	}


	public void setWarehouseCodeWar01WAR02(String warehouseCodeWar01WAR02) {
		this.warehouseCodeWar01WAR02 = warehouseCodeWar01WAR02;
	}


	public void setItemCodeItm01WAR02(String itemCodeItm01WAR02) {
		this.itemCodeItm01WAR02 = itemCodeItm01WAR02;
	}


	public void setWarehouseMotiveWar04WAR02(String warehouseMotiveWar04WAR02) {
		this.warehouseMotiveWar04WAR02 = warehouseMotiveWar04WAR02;
	}


	public void setItemTypeWAR04(String itemTypeWAR04) {
		this.itemTypeWAR04 = itemTypeWAR04;
	}


	public void setNoteWAR02(String noteWAR02) {
		this.noteWAR02 = noteWAR02;
	}


	public void setSerialNumbers(ArrayList serialNumbers) {
		this.serialNumbers = serialNumbers;
	}


	public void setVariantTypeItm06WAR02(String variantTypeItm06WAR02) {
		this.variantTypeItm06WAR02 = variantTypeItm06WAR02;
	}


	public void setVariantCodeItm11WAR02(String variantCodeItm11WAR02) {
		this.variantCodeItm11WAR02 = variantCodeItm11WAR02;
	}


	public void setVariantTypeItm07WAR02(String variantTypeItm07WAR02) {
		this.variantTypeItm07WAR02 = variantTypeItm07WAR02;
	}


	public void setVariantCodeItm12WAR02(String variantCodeItm12WAR02) {
		this.variantCodeItm12WAR02 = variantCodeItm12WAR02;
	}


	public void setVariantTypeItm08WAR02(String variantTypeItm08WAR02) {
		this.variantTypeItm08WAR02 = variantTypeItm08WAR02;
	}


	public void setVariantCodeItm13WAR02(String variantCodeItm13WAR02) {
		this.variantCodeItm13WAR02 = variantCodeItm13WAR02;
	}


	public void setVariantTypeItm09WAR02(String variantTypeItm09WAR02) {
		this.variantTypeItm09WAR02 = variantTypeItm09WAR02;
	}


	public void setVariantCodeItm14WAR02(String variantCodeItm14WAR02) {
		this.variantCodeItm14WAR02 = variantCodeItm14WAR02;
	}


	public void setVariantTypeItm10WAR02(String variantTypeItm10WAR02) {
		this.variantTypeItm10WAR02 = variantTypeItm10WAR02;
	}


	public void setVariantCodeItm15WAR02(String variantCodeItm15WAR02) {
		this.variantCodeItm15WAR02 = variantCodeItm15WAR02;
	}



  
}
