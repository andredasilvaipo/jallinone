package org.jallinone.warehouse.documents.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO;
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import org.jallinone.warehouse.documents.java.InDeliveryNoteRowPK;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.movements.server.ManualMovementsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage in delivery notes.</p>
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

public interface WarehouseUtils {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DeliveryNotePK getDeliveryNote();

	public VOListResponse loadInDeliveryNoteRows(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams pars, String serverLanguageId, String username) throws Throwable;

	public VOListResponse loadOutDeliveryNoteRows(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams pars, String serverLanguageId, String username) throws Throwable;

	public VOResponse reinsertInSerialNumbers(GridInDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable;

	public VOResponse reinsertOutSerialNumbers(GridOutDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable;



}
