package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.warehouse.availability.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.items.java.*;
import org.jallinone.warehouse.availability.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.companies.server.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.java.*;
import org.jallinone.system.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.commons.java.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to close a sale document, i.e.
 * - check if all items are available (for retail selling only)
 * - unload all items from the specified warehouse (for retail selling only)
 * - change doc state to close
 * - calculate document sequence
 * Requirements:
 * - position must be defined for each item row
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
@javax.jws.WebService
public interface CloseSaleDoc {

  public VOResponse closeSaleDoc(
		  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant1Descriptions,
		  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant2Descriptions,
		  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant3Descriptions,
		  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant4Descriptions,
		  @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant5Descriptions,
		  SaleDocPK pk,
		  String t1,String t2,String t3,String t4,String t5,
		  String t6,String t7,String t8,String t9,String t10,
		  String t11,String t12,String t13,String t14,String t15,String t16,
		  String serverLanguageId,String username,
		  ArrayList companiesList
  ) throws Throwable;

}

