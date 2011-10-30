package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.items.java.BrandVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.items.java.ItemVariantImageVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage item variant images.</p>
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
public interface ItemVariantImages {



	public VOResponse deleteItemVariantImage(ItemVariantImageVO vo,String username) throws Throwable;

	public VOResponse insertItemVariantImage(ItemVariantImageVO vo,String imagePath,String username) throws Throwable;

	public VOListResponse loadItemVariantImages(
			HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
			String serverLanguageId,
			GridParams gridParams,String imagePath,String username) throws Throwable;

	public VOResponse updateItemVariantImage(ItemVariantImageVO oldVO,ItemVariantImageVO newVO,String imagePath,String username) throws Throwable;




}

