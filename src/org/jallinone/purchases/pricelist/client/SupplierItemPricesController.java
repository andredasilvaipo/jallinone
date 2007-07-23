package org.jallinone.purchases.pricelist.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.purchases.pricelist.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.items.client.ItemsFrame;
import java.beans.PropertyVetoException;
import java.awt.Container;
import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.purchases.suppliers.client.SupplierDetailFrame;
import org.jallinone.items.java.DetailItemVO;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for suppliers item prices panel, for the specified item.</p>
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
public class SupplierItemPricesController extends CompanyGridController {

  /** supplier pricelist + supplier prices panel */
  private SupplierItemPricesPanel panel = null;


  public SupplierItemPricesController(SupplierItemPricesPanel panel) {
    this.panel = panel;
  }


  /**
   * Validate dates.
   */
  private Response validateDates(SupplierPriceVO vo) {
    if (vo.getStartDatePUR04().getTime()>vo.getEndDatePUR04().getTime())
      return new ErrorResponse("start date must be less than or equals to end date.");

    return new VOResponse(new Boolean(true));
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    SupplierPriceVO vo = (SupplierPriceVO)valueObject;
    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();
    vo.setCompanyCodeSys01PUR04(itemVO.getCompanyCodeSys01ITM01());
    vo.setItemCodeItm01PUR04(itemVO.getItemCodeITM01());
  }

  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SupplierPriceVO vo = null;
    Response response = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SupplierPriceVO)newValueObjects.get(i);
      response = validateDates(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("insertSupplierPrices",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response response = null;
    SupplierPriceVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (SupplierPriceVO)persistentObjects.get(i);
      response = validateDates(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("updateSupplierPrices",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteSupplierPrices",persistentObjects);
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    boolean ok = grid.isFieldEditable(row,attributeName);
    if (!ok)
      return ok;
    if (attributeName.equals("pricelistCodePur03PUR04") &&
        ((SupplierPriceVO)grid.getVOListTableModel().getObjectForRow(row)).getSupplierCodePUR01()==null)
      return false;

    return ok;
  }


}
