package org.jallinone.sales.pricelist.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.pricelist.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.items.client.ItemsFrame;
import java.beans.PropertyVetoException;
import org.openswing.swing.client.SaveButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for item prices panel.</p>
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
public class PricesController extends CompanyGridController implements ImportItems {

  /** pricelist + prices frame */
  private PricelistFrame frame = null;


  public PricesController(PricelistFrame frame) {
    this.frame = frame;
  }


  /**
   * Validate dates.
   */
  private Response validateDates(PriceVO vo) {
    if (vo.getStartDateSAL02().getTime()>vo.getEndDateSAL02().getTime())
      return new ErrorResponse("start date must be less than or equals to end date.");

    return new VOResponse(new Boolean(true));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    PriceVO vo = null;
    Response response = null;

    PricelistVO pricelistVO = (PricelistVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRow());

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (PriceVO)newValueObjects.get(i);
      if (vo.getCompanyCodeSys01SAL02()==null)
        vo.setCompanyCodeSys01SAL02(pricelistVO.getCompanyCodeSys01SAL01());
      if (vo.getPricelistCodeSal01SAL02()==null)
        vo.setPricelistCodeSal01SAL02(pricelistVO.getPricelistCodeSAL01());
      response = validateDates(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("insertPrices",newValueObjects);
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
    PriceVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (PriceVO)persistentObjects.get(i);
      response = validateDates(vo);
      if (response.isError())
        return response;
    }

    return ClientUtils.getData("updatePrices",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deletePrices",persistentObjects);
  }


  /**
   * Save the imported items with the specified validation dates and price.
   * @param items imported items
   * @param startDate start valid date
   * @param endDate end valid date
   * @param price price to set for each imte
   */
  public void saveItems(ArrayList items,Date startDate,Date endDate,BigDecimal price) {
    PricelistVO pricelistVO = (PricelistVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRow());

    ArrayList persistentObjects = new ArrayList();
    GridItemVO vo = null;
    PriceVO priceVO = null;
    for(int i=0;i<items.size();i++) {
      vo = (GridItemVO)items.get(i);
      priceVO = new PriceVO();
      priceVO.setCompanyCodeSys01SAL02(vo.getCompanyCodeSys01ITM01());
      priceVO.setEndDateSAL02(new java.sql.Date(endDate.getTime()));
      priceVO.setItemCodeItm01SAL02(vo.getItemCodeITM01());
      priceVO.setItemDescriptionSYS10(vo.getDescriptionSYS10());
      priceVO.setPricelistCodeSal01SAL02(pricelistVO.getPricelistCodeSAL01());
      priceVO.setPricelistDescriptionSYS10(pricelistVO.getDescriptionSYS10());
      priceVO.setProgressiveHie02ITM01(vo.getProgressiveHie02ITM01());
      priceVO.setStartDateSAL02(new java.sql.Date(startDate.getTime()));
      priceVO.setValueSAL02(price);
      persistentObjects.add(priceVO);
    }
    Response res = ClientUtils.getData("insertPrices",persistentObjects);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("Error while saving")+"\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Saving Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else
      frame.getPricesGrid().reloadData();

    try {
      frame.setSelected(true);
    }
    catch (PropertyVetoException ex) {
    }
    frame.toFront();

  }


  /**
   * Method called on firing a drop event onto the grid.
   * @param gridId identifier of the source grid (grid that generate a draf event)
   * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
   */
  public boolean dropEnabled(String gridId) {
    if (!beforeInsertGrid(frame.getPricesGrid()))
      return false;
    if (gridId.equals(ApplicationConsts.ID_ITEMS_GRID.toString()) &&
        frame.getGrid().getSelectedRow()!=-1) {

      ItemsFrame f = (ItemsFrame)MDIFrame.getSelectedFrame();
      int[] rows = f.getGrid().getSelectedRows();
      GridItemVO vo = null;
      ArrayList list = new ArrayList();
      for(int i=0;i<rows.length;i++) {
        vo = (GridItemVO)f.getGrid().getVOListTableModel().getObjectForRow(rows[i]);
        list.add( vo );
      }
      new ImportItemsDialog(list,this);
      return true;
    }
    return false;
  }


  /**
   * Callback method invoked when the user has selected another row.
   * @param rowNumber selected row index
   */
  public void rowChanged(int rowNumber) {
    if (frame.getPricesGrid().getMode()!=Consts.READONLY)
      return;

    if (rowNumber!=-1)
      reloadVariantsPrices(rowNumber);
    else
      frame.getVariantsPricesPanel().removeAll();
    frame.getVariantsPricesPanel().revalidate();
    frame.getVariantsPricesPanel().repaint();
  }


  private void reloadVariantsPrices(int rowNumber) {
    final PriceVO priceVO = (PriceVO)frame.getPricesGrid().getVOListTableModel().getObjectForRow(rowNumber);
    final ProductVariantsPanel variantsPricesPanel = frame.getVariantsPricesPanel();

    if(!Boolean.TRUE.equals(priceVO.getUseVariant1ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant2ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant3ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant4ITM01()) &&
       !Boolean.TRUE.equals(priceVO.getUseVariant5ITM01()))
      variantsPricesPanel.removeAll();
    else {
      variantsPricesPanel.removeAll();
      variantsPricesPanel.getOtherGridParams().put(ApplicationConsts.ITEM,new ItemPK(priceVO.getCompanyCodeSys01SAL02(),priceVO.getItemCodeItm01SAL02()));
      variantsPricesPanel.getOtherGridParams().put(ApplicationConsts.PRICELIST,priceVO.getPricelistCodeSal01SAL02());
      variantsPricesPanel.initGrid(priceVO);
      variantsPricesPanel.revalidate();
      variantsPricesPanel.repaint();

      SaveButton saveButton = new SaveButton();
      saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Response res = ClientUtils.getData("updateVariantsPrices",new Object[]{
             variantsPricesPanel.getVariantsMatrixVO(),
             variantsPricesPanel.getCells(),
             priceVO.getPricelistCodeSal01SAL02(),
             priceVO.getStartDateSAL02(),
             priceVO.getEndDateSAL02()
          });
          if (res.isError()) {
            JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(frame),
              res.getErrorMessage(),
              ClientSettings.getInstance().getResources().getResource("Error while saving"),
              JOptionPane.ERROR_MESSAGE
            );
          }
          else
            JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(frame),
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
    frame.getVariantsPricesPanel().removeAll();
    frame.getVariantsPricesPanel().revalidate();
    frame.getVariantsPricesPanel().repaint();
    int rowNumber = frame.getPricesGrid().getSelectedRow();
    if (rowNumber!=-1)
      reloadVariantsPrices(rowNumber);
    else
      frame.getVariantsPricesPanel().removeAll();
  }



}
