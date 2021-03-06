package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jallinone.commons.java.*;
import org.jallinone.purchases.documents.java.PurchaseDocPK;


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
@javax.jws.WebService
public interface InDeliveryNotes {

  public VOResponse deleteInDeliveryNoteRows(ArrayList list,String serverLanguageId,String username) throws Throwable;

  public VOResponse insertInDeliveryNote(DetailDeliveryNoteVO vo,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

  public VOResponse insertInDeliveryNoteRow(GridInDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable;

  public VOResponse loadInDeliveryNote(DeliveryNotePK pk,String serverLanguageId,String username) throws Throwable;

  public VOListResponse loadInDeliveryNotes(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

  public VOResponse updateInDeliveryNote(DetailDeliveryNoteVO oldVO,DetailDeliveryNoteVO newVO,String serverLanguageId,String username) throws Throwable;

  public VOListResponse updateInDeliveryNoteRows(ArrayList oldRows,ArrayList newRows,String serverLanguageId,String username) throws Throwable;
	
  public VOResponse updateInQuantities(
  	            @XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant1Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant2Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant3Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant4Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant5Descriptions,
				DeliveryNotePK pk,String t1,String t2,String serverLanguageId,String username) throws Throwable;

  public GridInDeliveryNoteRowVO validateCode(
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant1Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant2Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant3Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant4Descriptions,
				@XmlJavaTypeAdapter(HashMapAdapter.class) HashMap variant5Descriptions,
				PurchaseDocPK pk,
				String warehouseCode,
				BigDecimal progressiveREG04,
				BigDecimal progressiveHie01DOC09,
				BigDecimal progressiveHie02DOC09,			
				BigDecimal delivNoteDocNumber,
				String codeType,
				String code,
				String serverLanguageId,String username) throws Throwable;

  
}

