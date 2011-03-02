package org.jallinone.production.orders.server;

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
import org.jallinone.production.orders.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.production.billsofmaterial.server.*;
import org.jallinone.items.java.*;
import javax.swing.tree.*;
import javax.swing.tree.*;
import org.jallinone.production.billsofmaterial.java.*;
import org.jallinone.warehouse.availability.server.*;
import org.jallinone.warehouse.availability.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage production orders.
 * </p>
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

public interface ProdOrders {

	public VOResponse confirmProdOrder(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             DetailProdOrderVO vo, String t1, String t2, String t3, String t4,
             String t5, String serverLanguageId, String username, String imagePath,
             ArrayList companiesList) throws Throwable;

	public VOResponse deleteProdOrders(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertProdOrder(DetailProdOrderVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable;

	public VOListResponse loadProdOrders(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public VOResponse loadProdOrder(ProdOrderPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOResponse updateProdOrder(DetailProdOrderVO oldVO,DetailProdOrderVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable;

	public VOResponse closeProdOrder(DetailProdOrderVO vo,String t1,String t2,String serverLanguageId,String username) throws Throwable;

}

