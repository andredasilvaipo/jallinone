package org.jallinone.sales.documents.itemdiscounts.server;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import javax.jws.WebService;
import java.util.*;
import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOResponse;

@WebService
public interface InsertSaleDocRowDiscount {

	
	public VOResponse insertSaleDocRowDiscount(
	  SaleItemDiscountVO vo,String serverLanguageId,String username) throws Throwable;

	
}
