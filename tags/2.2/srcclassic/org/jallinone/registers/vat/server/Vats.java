package org.jallinone.registers.vat.server;

import java.util.ArrayList;

import org.jallinone.registers.transportmotives.java.TransportMotiveVO;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage transport vat.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */

public interface Vats {

	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	   */
	  public VatVO getVat();

  
  public VOResponse deleteVats(ArrayList list,String serverLanguageId,String username) throws Throwable;

  public VOListResponse insertVats(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable;

  public VOListResponse loadVats(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

  public VOListResponse updateVats(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

  public VOListResponse validateVatCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;
  
  
}

