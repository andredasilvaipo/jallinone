package org.jallinone.expirations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.expirations.java.*;
import org.jallinone.system.server.*;
import org.jallinone.accounting.accountingmotives.java.AccountingMotiveVO;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to expirations from DOC19 table,
 * filtered by sale or purchase type document, or by customer/supplier code, or by interval of dates.</p>
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

public interface LoadExpirations {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ExpirationVO getExpiration();

	public VOListResponse loadExpirations(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable;

	public VOListResponse loadPayments(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable;

	public VOListResponse loadPaymentDistributions(String companyCode,BigDecimal progressiveDOC27,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;


}

