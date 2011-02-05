package org.jallinone.system.importdata.server;

import java.math.BigDecimal;



import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.server.UserSessionParameters;


public interface LoadETLProcessFields {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public ETLProcessFieldVO getETLProcessField();

	public VOListResponse loadETLProcessFields(BigDecimal progressiveSYS23,String username) throws Throwable;


}
