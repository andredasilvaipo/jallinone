package org.jallinone.production.orders.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.jallinone.production.orders.java.ProdOrderProductVO;
import org.openswing.swing.message.receive.java.VOListResponse;


public interface CheckComponentsAvailability {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ProdOrderProductVO getProdOrderProduct();


	/**
	 * Check if components required by specified products are available.
	 * @param products list of ProdOrderProductVO objects
	 * @params compAltComps collection of <component item code,HashSet of alternative component item codes>; filled by this method (and given back by reference)
	 * @return VOListResponse of ProdOrderComponentVO objects
	 */
	public VOListResponse checkComponentsAvailability(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             HashMap compAltComps, ArrayList products, String serverLanguageId,
             String username, ArrayList companiesList) throws Throwable;


}
