package org.jallinone.registers.transportmotives.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.registers.task.java.TaskVO;
import org.jallinone.registers.transportmotives.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage transport motives.</p>
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

public interface TransportMotives {

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public TransportMotiveVO getTransportMotive();

  public VOResponse deleteTransportMotives(ArrayList list,String serverLanguageId,String username) throws Throwable;

  public VOListResponse insertTransportMotives(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable;

  public VOListResponse loadTransportMotives(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

  public VOListResponse updateTransportMotives(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

  public VOListResponse validateTransportMotiveCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;
  
  
}

