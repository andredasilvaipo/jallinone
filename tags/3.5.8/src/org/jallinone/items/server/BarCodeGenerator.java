package org.jallinone.items.server;

import java.sql.*;

import org.jallinone.items.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to generate a unique barcode for the specified item or item+variants combination.</p>
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
public abstract class BarCodeGenerator {


  /**
   * Method invoked by InsertItemAction or UpdateItemAction in order to fill in barCodeITM01 attribute
   * within DetailItemVO object, in case of an item without variants.
   * @param conn database connection
   * @param itemVO DetailItemVO object to fill in
   */
  public abstract void calculateBarCode(Connection conn,DetailItemVO itemVO) throws Exception;


  /**
   * Method invoked by UpdateItemVariantsAction in order to fill in barCodeITM22 attribute
   * within VariantBarcodeVO object, in case of an item having variants, for a specified combination of
   * supported variants.
   * @param conn database connection
   * @param vo VariantBarcodeVO object to fill in
   */
  public abstract void calculateBarCode(Connection conn,VariantBarcodeVO vo) throws Exception;


}
