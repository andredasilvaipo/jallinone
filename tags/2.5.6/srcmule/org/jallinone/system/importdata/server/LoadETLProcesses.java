package org.jallinone.system.importdata.server;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import javax.jws.WebService;

import org.jallinone.system.importdata.java.ETLProcessVO;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.send.java.GridParams;

@WebService
public interface LoadETLProcesses {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public ETLProcessVO getETLProcess();

	public VOListResponse loadETLProcesses(GridParams gridParams,String username,ArrayList companiesList) throws Throwable;

	
}
