package org.jallinone.registers.payments.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.registers.carrier.java.CarrierVO;
import org.jallinone.registers.payments.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage payments.</p>
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

public interface Payments {


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public PaymentVO getPayment();


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public PaymentInstalmentVO getPaymentInstalment();


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public PaymentTypeVO getPaymentType();


	public VOResponse deletePayments(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse deletePaymentTypes(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertPayments(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable;

	public VOListResponse insertPaymentTypes(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable;

	public VOListResponse loadPaymentInstalments(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadPayments(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable;

	public VOListResponse loadPaymentTypes(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable;

	public VOListResponse updatePaymentInstalments(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable;

	public VOListResponse updatePayments(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOListResponse updatePaymentTypes(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOListResponse validatePaymentCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable;

	public VOListResponse validatePaymentTypeCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable;


}

