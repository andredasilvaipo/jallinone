package org.jallinone.sales.documents.server;

import java.util.*;

import org.jallinone.sales.documents.java.*;
import org.openswing.swing.message.receive.java.*;


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

public interface CloseSaleDoc {

  public VOResponse closeSaleDoc(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocPK pk,
      String t1,String t2,String t3,String t4,String t5,
      String t6,String t7,String t8,String t9,String t10,
      String t11,String t12,String t13,String t14,String t15,String t16,String t17,
      String serverLanguageId,String username,
      ArrayList companiesList
  ) throws Throwable;

}

