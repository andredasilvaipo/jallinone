package org.jallinone.sales.discounts.server;

import java.sql.*;
import org.jallinone.sales.discounts.java.DiscountVO;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.translations.server.TranslationUtils;
import java.util.ArrayList;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import javax.servlet.ServletContext;
import java.util.HashMap;
import org.openswing.swing.message.send.java.GridParams;
import java.util.HashSet;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to select/insert/update/delete a sale discount.</p>
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

public interface Discount {
	
	
  public Response getDiscountsList(String companyCodeSYS01,ArrayList discountCodes,String langId,GridParams gridParams,String username,Class discountVOClass) throws Exception ;

  public void insertDiscount(DiscountVO vo) throws Exception ;

  public Response updateDiscount(DiscountVO oldVO,DiscountVO newVO,String langId,String username) throws Exception ;

  public void deleteDiscount(DiscountVO vo) throws Exception ;

}

