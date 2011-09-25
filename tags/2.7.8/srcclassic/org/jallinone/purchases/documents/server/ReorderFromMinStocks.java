package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.purchases.documents.java.*;
import java.math.*;
import java.util.*;
import org.jallinone.variants.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve the list of variants/items having a min stock declared in the item detail.</p>
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

public interface ReorderFromMinStocks {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ReorderFromMinStockVO getReorderFromMinStock(ReorderFromMinStockFilterVO pk);

	public VOListResponse reorderFromMinStocks(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams gridPars, String serverLanguageId, String username) throws Throwable;

}

