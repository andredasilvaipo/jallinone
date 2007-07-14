package org.jallinone.purchases.pricelist.client;

import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Interface used between ImportItemsDialog and SupplierPricelistPanel to retrieve validations dates and price for imported dragged supplier items.</p>
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
public interface SupplierImportItems {

  /**
   * Save the imported items with the specified validation dates and price.
   * @param items imported items
   * @param startDate start valid date
   * @param endDate end valid date
   * @param price price to set for each imte
   */
  public void saveItems(ArrayList items,Date startDate,Date endDate,BigDecimal price);


}