package org.jallinone.items.client;


import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantNameVO;
import org.openswing.swing.domains.java.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.jallinone.variants.java.VariantTypeVO;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.util.java.Consts;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import org.openswing.swing.table.java.GridDataLocator;
import java.util.Map;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsItemDescriptor;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import java.lang.reflect.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is a panel that shows a matrix of all allowed combinations of an item and its variants/variant types.</p>
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
public class VariantMinStocksPanel extends JPanel {

  private LabelControl labelbc = new LabelControl();
  private TextControl controlbc = new TextControl();
  private GridControl grid = null;


  public VariantMinStocksPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    controlbc.setColumns(20);
    labelbc.setLabel("min stock");
    this.setLayout(new BorderLayout());
  }


  private void initGrid(VariantsItemDescriptor itemVO) {

    Response res = ClientUtils.getData("loadProductVariantsMatrix",itemVO);
    if (!res.isError()) {
      // retrieve grid descriptor...
      final VariantsMatrixVO vo = (VariantsMatrixVO)((VOResponse)res).getVo();

      // create grid...
      grid = new GridControl();
      grid.setController(new GridController() {


        /**
         * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
         * @param rowNumbers row indexes related to the changed/new rows
         * @param oldPersistentObjects old value objects, previous the changes; it can contains null objects, in case of new inserted rows
         * @param persistentObjects value objects related to the changed/new rows
         * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
         */
        public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
          Response res = ClientUtils.getData("updateVariantMinStocks",new Object[]{vo,getCells()});
          if (res.isError())
            return res;
          return new VOListResponse(persistentObjects,false,persistentObjects.size());
        }

      });
      grid.setGridDataLocator(new GridDataLocator() {

        public Response loadData(int action, int startIndex,
                                 Map filteredColumns,
                                 ArrayList currentSortedColumns,
                                 ArrayList currentSortedVersusColumns,
                                 Class valueObjectType, Map otherGridParams) {
          VariantsMatrixVO matrixVO = (VariantsMatrixVO)otherGridParams.get(ApplicationConsts.VARIANTS_MATRIX_VO);
          return ClientUtils.getData("loadVariantMinStocks",matrixVO);
        }

      });
      //gridDataLocator.setServerMethodName(serverGridMethodName);
      grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");
      grid.getOtherGridParams().put(ApplicationConsts.VARIANTS_MATRIX_VO,vo);
      grid.setReorderingAllowed(false);
      grid.setVisibleStatusPanel(false);

      // create grid columns...
      ButtonColumn var1Col = new ButtonColumn();
      var1Col.setColumnName("attributeNameS0");
      var1Col.setEnableInReadOnlyMode(true);
      var1Col.setShowAttributeValue(true);
      var1Col.setHeaderColumnName(" ");
      var1Col.setColumnSelectable(false);
      grid.getColumnContainer().add(var1Col,null);

      VariantsMatrixColumnVO colVO = null;
      DecimalColumn col = null;
      for(int i=0;i<vo.getColumnDescriptors().length;i++) {
        colVO = (VariantsMatrixColumnVO)vo.getColumnDescriptors()[i];
        col = new DecimalColumn();
        col.setColumnSelectable(false);
        col.setEditableOnEdit(true);
        col.setHeaderColumnName(colVO.getColumnDescription());
        col.setColumnName("attributeNameN"+(i+1));
        col.setColumnRequired(false);
        col.setMinWidth(90);

        col.setPreferredWidth(20+this.getFontMetrics(this.getFont()).stringWidth(col.getHeaderColumnName()));

        grid.getColumnContainer().add(col,null);
      }
      if (vo.getColumnDescriptors().length==0) {
        col = new DecimalColumn();
        col.setColumnSelectable(false);
        col.setColumnRequired(false);
        col.setEditableOnEdit(true);
        col.setHeaderColumnName(" ");
        col.setColumnName("attributeNameN0");
        col.setPreferredWidth(50);

        grid.getColumnContainer().add(col,null);
      }

      EditButton editButton = new EditButton();
      SaveButton saveButton = new SaveButton();
      ReloadButton reloadButton = new ReloadButton();
      grid.setEditButton(editButton);
      grid.setSaveButton(saveButton);
      grid.setReloadButton(reloadButton);

      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
      buttonsPanel.add(editButton,null);
      buttonsPanel.add(saveButton,null);
      buttonsPanel.add(reloadButton,null);
      this.add(buttonsPanel,BorderLayout.NORTH);
      buttonsPanel.revalidate();
      buttonsPanel.repaint();

      // add grid to panel...
      this.add(grid,BorderLayout.CENTER);
      this.revalidate();
      this.repaint();
    }
  }


  public final void clearData() {
    grid = null;
    this.removeAll();
    this.revalidate();
    this.repaint();
  }


  public final void setItem(Form form) {
    VariantsItemDescriptor itemVO = (VariantsItemDescriptor)form.getVOModel().getValueObject();

    if (form.getMode()==Consts.INSERT ||
        itemVO.getItemCodeItm01()==null ||
        itemVO.getItemCodeItm01().equals("") ||
        (!Boolean.TRUE.equals(itemVO.getUseVariant1ITM01()) &&
         !Boolean.TRUE.equals(itemVO.getUseVariant2ITM01()) &&
         !Boolean.TRUE.equals(itemVO.getUseVariant3ITM01()) &&
         !Boolean.TRUE.equals(itemVO.getUseVariant4ITM01()) &&
         !Boolean.TRUE.equals(itemVO.getUseVariant5ITM01()))
    ) {
      clearData();
    }
    else {
      this.removeAll();
      initGrid(itemVO);
    }
  }


/*
  private void updateMatrix() {
    VariantsMatrixRowVO rowVO = null;
    VariantsMatrixColumnVO colVO = null;
    CustomValueObject vo = null;
    int cols = getVariantsMatrixVO().getColumnDescriptors().length==0?1:getVariantsMatrixVO().getColumnDescriptors().length;
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors().get(i);
      vo = (CustomValueObject)grid.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getVariantTypeITM06().equals(snVO.getVariantTypeItm06WAR05()) &&
          rowVO.getVariantCodeITM11().equals(snVO.getVariantCodeItm11WAR05())) {
        if (getVariantsMatrixVO().getColumnDescriptors().length==0)
          vo.setAttributeNameN0(new BigDecimal(1));
        else
          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors()[j];
            if (colVO.getVariantCodeITM12().equals(snVO.getVariantCodeItm12WAR05()) &&
                colVO.getVariantCodeITM12().equals(snVO.getVariantCodeItm12WAR05()) &&
                colVO.getVariantCodeITM12().equals(snVO.getVariantCodeItm12WAR05()) &&
                colVO.getVariantCodeITM12().equals(snVO.getVariantCodeItm12WAR05()) &&
                colVO.getVariantTypeITM07().equals(snVO.getVariantTypeItm07WAR05()) &&
                colVO.getVariantTypeITM07().equals(snVO.getVariantTypeItm07WAR05()) &&
                colVO.getVariantTypeITM07().equals(snVO.getVariantTypeItm07WAR05()) &&
                colVO.getVariantTypeITM07().equals(snVO.getVariantTypeItm07WAR05())) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" + j,new Class[] {BigDecimal.class}).invoke(vo, new Object[] {new BigDecimal(1)});
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
              break;
            }
          }
        break;
      }
    }
  }
*/


  /**
   * @return list of objects stored withing the grid
   */
  public Object[][] getCells() {
    if (grid==null)
      return null;

    Object[][] cells = new Object[grid.getVOListTableModel().getRowCount()][getVariantsMatrixVO().getColumnDescriptors().length==0?1:getVariantsMatrixVO().getColumnDescriptors().length];
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      for(int k=0;k<getVariantsMatrixVO().getColumnDescriptors().length;k++)
        if (grid.getVOListTableModel().getValueAt(i,k+1)!=null)
          cells[i][k] = grid.getVOListTableModel().getValueAt(i,k+1);
      if (getVariantsMatrixVO().getColumnDescriptors().length == 0)
        if (grid.getVOListTableModel().getValueAt(i, 1) != null)
          cells[i][0] = grid.getVOListTableModel().getValueAt(i, 1);
    }

     return cells;
  }


  /**
   * @return list of CustomValueObject objects
   */
  public VariantsMatrixVO getVariantsMatrixVO() {
    return grid==null?null:(VariantsMatrixVO)grid.getOtherGridParams().get(ApplicationConsts.VARIANTS_MATRIX_VO);

  }


}
