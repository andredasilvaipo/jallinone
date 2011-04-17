package org.jallinone.warehouse.server;

import org.jallinone.warehouse.java.*;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import java.math.BigDecimal;
import java.util.HashMap;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage the inventory.</p>
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

public interface Inventories {


	public InventoryVO insertInventory(InventoryVO vo,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadInventories(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updateInventories(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteInventories(ArrayList list,String serverLanguageId,String username) throws Throwable;


	public VOResponse importInventoryItems(
		 InventoryVO vo,
		 BigDecimal progressiveHIE02,
		 BigDecimal progressiveHIE01,
		 String serverLanguageId,
		 String username
	) throws Throwable;


	public InventoryItemVO insertInventoryItem(InventoryItemVO vo,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadInventoryItems(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams gridParams, String serverLanguageId, String username) throws Throwable;

	public VOListResponse updateInventoryItems(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteInventoryItems(ArrayList list,String serverLanguageId,String username) throws Throwable;


	public VOResponse closeInventory(
		 String companyCodeSys01,
		 String warehouseCodeWAR01,
		 BigDecimal progressiveWAR06,
		 String itemType,
		 String note,
		 String serverLanguageId,
		 String username
	) throws Throwable;



	public VOResponse confirmInventory(
		 String companyCodeSys01,
		 String warehouseCodeWAR01,
		 BigDecimal progressiveWAR06,
		 String serverLanguageId,
		 String username
	) throws Throwable;



 public VOResponse updateInventoryItem(
		String companyCodeSys01,
		String warehouseCodeWAR01,
		BigDecimal progressiveWAR06,
	  BigDecimal progressiveHIE02,
		String itemCodeItm01WAR07,
		BigDecimal realProgressiveHie01WAR07, // warehouse's real location
		BigDecimal realQtyWAR07, // warehouse's real location
		String variantTypeItm06,
		String variantCodeItm11,
		String variantTypeItm07,
		String variantCodeItm12,
		String variantTypeItm08,
		String variantCodeItm13,
		String variantTypeItm09,
		String variantCodeItm14,
		String variantTypeItm10,
		String variantCodeItm15,
		ArrayList serialNumbers,
		String serverLanguageId,
		String username
 ) throws Throwable;


}

