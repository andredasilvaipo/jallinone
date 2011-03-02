package org.jallinone.system.importdata.server;

import java.io.File;



import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.receive.java.VOResponse;


public interface GetFileContent {

	 /**
	   * Business logic to execute.
	   */
	  public byte[] getFileContent(String fileFormat,File file,String serverLanguageId,String username) throws Throwable;
	    
}
