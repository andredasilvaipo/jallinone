package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.purchases.suppliers.server.*;
import org.jallinone.purchases.suppliers.java.*;
import org.jallinone.purchases.documents.invoices.java.*;
import org.jallinone.purchases.documents.invoices.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a purchase invoice from a set of in delivery notes.</p>
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

public interface CreateInvoiceFromInDelivNotes {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DetailPurchaseDocVO getDetailPurchaseDoc();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public InDeliveryNotesVO getInDeliveryNotes();

	public VOResponse createInvoiceFromInDelivNotes(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 PurchaseInvoiceFromDelivNotesVO invoiceVO,String companyCode,String serverLanguageId,String username) throws Throwable;

}

