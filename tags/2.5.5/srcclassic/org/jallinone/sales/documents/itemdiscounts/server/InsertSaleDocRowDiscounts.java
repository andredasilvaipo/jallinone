package org.jallinone.sales.documents.itemdiscounts.server;

import java.util.ArrayList;



import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOListResponse;
import java.util.HashMap;


public interface InsertSaleDocRowDiscounts {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SaleItemDiscountVO getSaleItemDiscount();


	public VOListResponse insertSaleDocRowDiscounts(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             ArrayList list, String serverLanguageId, String username) throws Throwable;

}
