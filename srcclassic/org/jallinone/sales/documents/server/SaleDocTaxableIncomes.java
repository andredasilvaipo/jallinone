package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.registers.vat.server.ValidateVatCodeAction;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.accounting.movements.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to calculate taxable incomes of a sale document, grouped by vat code,
 * based on document rows (items, item discounts, activities) and header discounts/charges.
 * It does not update the database, it only return an updated v.o.</p>
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

public interface SaleDocTaxableIncomes {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public TaxableIncomeVO getTaxableIncome();

	public VOListResponse calcTaxableIncomes(DetailSaleDocVO vo,String serverLanguageId,String username) throws Throwable;

}

