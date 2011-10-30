package org.jallinone.sales.documents.itemdiscounts.server;

import java.util.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import javax.jws.WebService;

import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.openswing.swing.message.receive.java.VOListResponse;

@WebService
public interface InsertSaleDocRowDiscounts {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SaleItemDiscountVO getSaleItemDiscount();


	public VOListResponse insertSaleDocRowDiscounts(
		  	  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant1Descriptions,
		      @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant2Descriptions,
		      @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant3Descriptions,
		      @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant4Descriptions,
		      @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant5Descriptions,
		      ArrayList list,String serverLanguageId,String username) throws Throwable;

}
