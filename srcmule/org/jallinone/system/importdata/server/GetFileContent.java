package org.jallinone.system.importdata.server;

import java.io.File;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import javax.jws.WebService;

import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.receive.java.VOResponse;

@WebService
public interface GetFileContent {

	 /**
	   * Business logic to execute.
	   */
	  public byte[] getFileContent(String fileFormat,File file,String serverLanguageId,String username) throws Throwable;
	    
}
