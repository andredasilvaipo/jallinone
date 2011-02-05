package org.jallinone.system.importdata.server;

import java.math.BigDecimal;
import java.sql.Connection;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import javax.jws.WebService;

import org.jallinone.system.importdata.java.ETLProcessVO;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.UserSessionParameters;

@WebService
public interface LoadETLProcess {

	  public ETLProcessVO loadETLProcess(BigDecimal progressiveSYS23,String username) throws Throwable;

	
}
