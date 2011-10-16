package org.jallinone.items.server;

import java.math.*;
import java.sql.*;
import java.util.*;



import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to prepare barcode labels related to the specified set of items
 * and fill in a temporary table (TMP02_BARCODES) with that data.</p>
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

public interface CreateBarcodeLabelsData {

  public VOResponse createBarcodeLabelsData(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		ItemToPrintVO[] rows, String serverLanguageId, String username,
		String imagePath) throws Throwable;

}

