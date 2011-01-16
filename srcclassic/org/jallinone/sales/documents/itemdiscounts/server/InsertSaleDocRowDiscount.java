package org.jallinone.sales.documents.itemdiscounts.server;



import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOResponse;


public interface InsertSaleDocRowDiscount {

	
	public VOResponse insertSaleDocRowDiscount(SaleItemDiscountVO vo,String serverLanguageId,String username) throws Throwable;

	
}
