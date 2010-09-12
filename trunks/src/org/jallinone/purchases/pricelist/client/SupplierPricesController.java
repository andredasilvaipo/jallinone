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
import org.openswing.swing.client.SaveButton;
import java.awt.event.ActionListener;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.items.java.ItemPK;
import java.awt.event.ActionEvent;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for supplier item prices panel.</p>
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
public class SupplierPricesController extends CompanyGridController implements SupplierImportItems {

  /** supplier pricelist + supplier prices panel */
  private SupplierPricelistPanel panel = null;


  public SupplierPricesController(SupplierPricelistPanel panel) {
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
    vo.setStartDatePUR04(new java.sql.Date(System.currentTimeMillis()));
    vo.setEndDatePUR04(new java.sql.Date(System.currentTimeMillis()+86400000L*365));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SupplierPriceVO vo = null;
    SupplierPricelistVO pricelistVO = null;
    Response response = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SupplierPriceVO)newValueObjects.get(i);
      pricelistVO = (SupplierPricelistVO)panel.getGrid().getVOListTableModel().getObjectForRow(panel.getGrid().getSelectedRow());
      vo.setCompanyCodeSys01PUR04(pricelistVO.getCompanyCodeSys01PUR03());
      vo.setPricelistCodePur03PUR04(pricelistVO.getPricelistCodePUR03());
      vo.setProgressiveReg04PUR04(pricelistVO.getProgressiveReg04PUR03());
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
   * Save the imported supplier items with the specified validation dates and price.
   * @param items imported items
   * @param startDate start valid date
   * @param endDate end valid date
   * @param price price to set for each imte
   */
  public void saveItems(ArrayList items,Date startDate,Date endDate,BigDecimal price) {
    SupplierPricelistVO pricelistVO = (SupplierPricelistVO)panel.getGrid().getVOListTableModel().getObjectForRow(panel.getGrid().getSelectedRow());

    ArrayList persistentObjects = new ArrayList();
    SupplierItemVO vo = null;
    SupplierPriceVO priceVO = null;
    for(int i=0;i<items.size();i++) {
      vo = (SupplierItemVO)items.get(i);
      priceVO = new SupplierPriceVO();
      priceVO.setCompanyCodeSys01PUR04(vo.getCompanyCodeSys01PUR02());
      priceVO.setEndDatePUR04(new java.sql.Date(endDate.getTime()));
      priceVO.setItemCodeItm01PUR04(vo.getItemCodeItm01PUR02());
      priceVO.setItemDescriptionSYS10(vo.getDescriptionSYS10());
      priceVO.setPricelistCodePur03PUR04(pricelistVO.getPricelistCodePUR03());
      priceVO.setPricelistDescriptionSYS10(pricelistVO.getDescriptionSYS10());
      priceVO.setProgressiveHie02ITM01(vo.getProgressiveHie02PUR02());
      priceVO.setStartDatePUR04(new java.sql.Date(startDate.getTime()));
      priceVO.setProgressiveReg04PUR04(vo.getProgressiveReg04PUR02());
      priceVO.setValuePUR04(price);
      persistentObjects.add(priceVO);
    }
    Response res = ClientUtils.getData("insertSupplierPrices",persistentObjects);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("Error while saving")+"\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Saving Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else
      panel.getPricesGrid().reloadData();

    try {
      Container parent = panel.getParent();
      while(parent!=null && !(parent instanceof InternalFrame))
        parent = parent.getParent();
      if (parent!=null) {
        ((InternalFrame)parent).setSelected(true);
        ((InternalFrame)parent).toFront();
      }
    }
    catch (PropertyVetoException ex) {
    }

  }


  /**
   * Method called on firing a drop event onto the grid.
   * @param gridId identifier of the source grid (grid that generate a draf event)
   * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
   */
  public boolean dropEnabled(String gridId) {
    if (!beforeInsertGrid(panel.getPricesGrid()))
      return false;
    if (gridId.equals(ApplicationConsts.ID_SUPPLIER_ITEMS_GRID.toString()) &&
        panel.getGrid().getSelectedRow()!=-1) {

      SupplierDetailFrame f = (SupplierDetailFrame)MDIFrame.getSelectedFrame();
      int[] rows = f.getItemsGrid().getSelectedRows();
      SupplierItemVO vo = null;
      ArrayList list = new ArrayList();
      for(int i=0;i<rows.length;i++) {
        vo = (SupplierItemVO)f.getItemsGrid().getVOListTableModel().getObjectForRow(rows[i]);
        list.add( vo );
      }
      new SupplierImportItemsDialog(list,this);
      return true;
    }
    return false;
  }




  /**
   * Callback method invoked when the user has selected another row.
   * @param rowNumber selected row index
   */
  public void rowChanged(int rowNumber) {
    if (panel.getGrid().getMode()!=Consts.READONLY)
      return;
    if (panel.getPricesGrid().getMode()!=Consts.READONLY)
      return;

    if (rowNumber!=-1)
      reloadVariantsPrices(rowNumber);
    else
      panel.getVariantsPricesPanel().removeAll();
    panel.getVariantsPricesPanel().revalidate();
    panel.getVariantsPricesPanel().repaint();
  }


  private void reloadVariantsPrices(int rowNumber) {
    final SupplierPriceVO priceVO = (SupplierPriceVO)panel.getPricesGrid().getVOListTableModel().getObjectForRow(rowNumber);
    final ProductVariantsPanel variantsPricesPanel = panel.getVariantsPricesPanel();

    if(!Boolean.TRUE.equals(priceVO.getUseVariant1ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant2ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant3ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant4ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant5ITM01()))
      variantsPricesPanel.removeAll();
    else {
      variantsPricesPanel.removeAll();
      variantsPricesPanel.getOtherGridParams().put(ApplicationConsts.ITEM,new ItemPK(priceVO.getCompanyCodeSys01PUR04(),priceVO.getItemCodeItm01PUR04()));
      variantsPricesPanel.getOtherGridParams().put(ApplicationConsts.PRICELIST,priceVO.getPricelistCodePur03PUR04());
      variantsPricesPanel.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,priceVO.getProgressiveReg04PUR04());
      variantsPricesPanel.initGrid(priceVO);
      variantsPricesPanel.revalidate();
      variantsPricesPanel.repaint();

      SaveButton saveButton = new SaveButton();
      saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Response res = ClientUtils.getData("updateSupplierVariantsPrices",new Object[]{
             variantsPricesPanel.getVariantsMatrixVO(),
             variantsPricesPanel.getCells(),
             priceVO.getPricelistCodePur03PUR04(),
             priceVO.getStartDatePUR04(),
             priceVO.getEndDatePUR04(),
             priceVO.getProgressiveReg04PUR04()
          });
          if (res.isError()) {
            JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(panel),
              res.getErrorMessage(),
              ClientSettings.getInstance().getResources().getResource("Error while saving"),
              JOptionPane.ERROR_MESSAGE
            );
          }
          else
            JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(panel),
              ClientSettings.getInstance().getResources().getResource("prices saved"),
              ClientSettings.getInstance().getResources().getResource("Attention"),
              JOptionPane.INFORMATION_MESSAGE
            );

        }
      });
      variantsPricesPanel.getButtonsPanel().add(saveButton);
    }
    variantsPricesPanel.revalidate();
    variantsPricesPanel.repaint();
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    panel.getVariantsPricesPanel().removeAll();
    panel.getVariantsPricesPanel().revalidate();
    panel.getVariantsPricesPanel().repaint();
    int rowNumber = panel.getPricesGrid().getSelectedRow();
    if (rowNumber!=-1)
      reloadVariantsPrices(rowNumber);
    else
      panel.getVariantsPricesPanel().removeAll();
  }


}
