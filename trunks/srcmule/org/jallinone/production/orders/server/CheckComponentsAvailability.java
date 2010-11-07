package org.jallinone.production.orders.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jallinone.commons.java.HashMapAdapter;
import org.jallinone.production.orders.java.ProdOrderProductVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.server.UserSessionParameters;

@WebService
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
	public VOListResponse checkComponentsAvailability(@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap compAltComps,ArrayList products,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;


}
