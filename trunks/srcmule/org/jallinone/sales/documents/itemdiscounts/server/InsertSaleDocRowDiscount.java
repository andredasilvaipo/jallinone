package org.jallinone.sales.documents.itemdiscounts.server;

import javax.jws.WebService;

import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOResponse;

@WebService
public interface InsertSaleDocRowDiscount {

	
	public VOResponse insertSaleDocRowDiscount(SaleItemDiscountVO vo,String serverLanguageId,String username) throws Throwable;

	
}
