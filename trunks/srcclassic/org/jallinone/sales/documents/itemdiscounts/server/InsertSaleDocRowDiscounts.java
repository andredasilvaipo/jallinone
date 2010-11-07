package org.jallinone.sales.documents.itemdiscounts.server;

import java.util.ArrayList;



import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOListResponse;


public interface InsertSaleDocRowDiscounts {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SaleItemDiscountVO getSaleItemDiscount();


	public VOListResponse insertSaleDocRowDiscounts(ArrayList list,String serverLanguageId,String username) throws Throwable;

}
