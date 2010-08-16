package org.jallinone.variants.client;

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
import org.jallinone.items.java.VariantBarcodeVO;

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
public class ProductVariantsPanel extends JPanel implements LookupListener {

  ///** grid data locator */
  //private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  /** item lookup */
  private CodLookupControl controlItemCode = null;
  /** item lookup controller */
  private LookupController lookupController = null;
  /** form panel that contains the item lookup */
  private Form form = null;
  /** grid server method name */
  private String serverGridMethodName = null;
  /** matrix server method name */
  private String serverMatrixMethodName = null;
  /** qty control */
  private NumericControl controlQty = null;
  /** split pane */
  private JSplitPane splitPane = null;
  /** initial divider location */
  private int initialDiv = 0;
  /** grid */
  private GridControl grid = null;
  /** panel's controller */
  private ProductVariantsController controller = null;
  /** Form height */
  private int formHeight = 0;

  /** serial number (optional) */
  private StoredSerialNumberVO snVO = null;

  /** variants barcode (optional) */
  private VariantBarcodeVO barcodeVO = null;


  public ProductVariantsPanel(
      ProductVariantsController controller,
      Form form,
      CodLookupControl controlItemCode,
      LookupController lookupController,
      String serverMatrixMethodName,
      //String serverGridMethodName,
      NumericControl controlQty,
      JSplitPane splitPane,
      int initialDiv
  ) {
    try {
      this.controller = controller;
      this.form = form;
      this.controlItemCode = controlItemCode;
      this.lookupController = lookupController;
      this.serverMatrixMethodName = serverMatrixMethodName;
      //this.serverGridMethodName = serverGridMethodName;
      this.controlQty = controlQty;
      this.splitPane = splitPane;
      this.initialDiv = initialDiv;
      this.formHeight = form.getPreferredSize().height;
      lookupController.addLookupListener(this);
      jbInit();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
  }


  private void initGrid(VariantsItemDescriptor lookupVO) {

    Response res = ClientUtils.getData(serverMatrixMethodName,lookupVO);
    if (!res.isError()) {
      // retrieve grid descriptor...
      VariantsMatrixVO vo = (VariantsMatrixVO)((VOResponse)res).getVo();

      // create grid...
      grid = new GridControl();
      grid.setController(new GridController() {

        /**
         * @param grid grid
         * @param row selected row index
         * @param attributeName attribute name that identifies the selected grid column
         * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
         */
        public boolean isCellEditable(GridControl grid,int row,String attributeName) {
          int col = grid.getTable().getGrid().getColumnIndex(attributeName);
          if (col<1)
            return false;
          if (barcodeVO!=null) {
            return getCells()[row][col-1]!=null;
          }
          return grid.isFieldEditable(row,attributeName);
        }


        /**
         * Callback method invoked when the data loading is completed.
         * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
         */
        public void loadDataCompleted(boolean error) {
          if (barcodeVO!=null) {
            updateMatrixWithBarcode();
            grid.setMode(Consts.EDIT);
          }
          else if (snVO!=null)
            updateMatrixWithSN();
          else
            grid.setMode(Consts.EDIT);
        }

        public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
          if (newValue!=null && ((Number)newValue).doubleValue()<=0)
            return false;

          if (newValue!=null) {
              BigDecimal correctValue = controller.validateQty((BigDecimal)newValue);
              if (!correctValue.equals(newValue))
                // in case of multiple qty not correct...
                return false;
          }
          SwingUtilities.invokeLater(new Runnable() {

            public void run() {
              BigDecimal tot = new BigDecimal(0);
              for(int i=0;i<grid.getVOListTableModel().getRowCount();i++)
                for(int k=1;k<grid.getTable().getGrid().getColumnCount();k++)
                  if (grid.getVOListTableModel().getValueAt(i,k)!=null)
                    tot = tot.add((BigDecimal)grid.getVOListTableModel().getValueAt(i,k));
              if (tot.doubleValue()==0)
                controlQty.setText("");
              else {
                controlQty.setEnabled(true);
                controlQty.setValue(tot);

                FocusListener[] ll = controlQty.getBindingComponent().getFocusListeners();
                for(int i=0;i<ll.length;i++)
                  ll[i].focusLost(new FocusEvent(controlQty,FocusEvent.FOCUS_LOST));

                controller.qtyUpdated(tot);
                controlQty.setEnabled(false);
              }

            }

          });

          return true;
        }

      });
      grid.setGridDataLocator(new GridDataLocator() {

        public Response loadData(int action, int startIndex,
                                 Map filteredColumns,
                                 ArrayList currentSortedColumns,
                                 ArrayList currentSortedVersusColumns,
                                 Class valueObjectType, Map otherGridParams) {
          VariantsMatrixVO matrixVO = (VariantsMatrixVO)otherGridParams.get(ApplicationConsts.VARIANTS_MATRIX_VO);
          ArrayList rows = new ArrayList();
          CustomValueObject vo = null;
          VariantsMatrixRowVO rowVO = null;
          for(int i=0;i<matrixVO.getRowDescriptors().size();i++) {
            rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

            vo = new CustomValueObject();
            vo.setAttributeNameS0(rowVO.getRowDescription());
            rows.add(vo);
          }
          return new VOListResponse(rows,false,rows.size());
        }

      });
      //gridDataLocator.setServerMethodName(serverGridMethodName);
      grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");
      grid.getOtherGridParams().put(ApplicationConsts.VARIANTS_MATRIX_VO,vo);
      grid.setReorderingAllowed(false);
      grid.setVisibleStatusPanel(false);
//      if (vo.getManagedVariants().size()>1)
//        grid.setHeaderHeight(grid.getHeaderHeight()*(vo.getManagedVariants().size()-1));

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
      for(int i=0;i<vo.getColumnDescriptors().size();i++) {
        colVO = (VariantsMatrixColumnVO)vo.getColumnDescriptors().get(i);
        col = new DecimalColumn();
        col.setColumnSelectable(false);
        col.setEditableOnEdit(true);
        col.setHeaderColumnName(colVO.getColumnDescription());
        col.setColumnName("attributeNameN"+i);
        col.setColumnRequired(false);
        col.setDecimals(vo.getDecimals());
        //col.setPreferredWidth(110*(vo.getManagedVariants().size()-1));

        col.setPreferredWidth(20+this.getFontMetrics(this.getFont()).stringWidth(col.getHeaderColumnName()));

        grid.getColumnContainer().add(col,null);
      }
      if (vo.getColumnDescriptors().size()==0) {
        col = new DecimalColumn();
        col.setColumnSelectable(false);
        col.setEditableOnEdit(true);
        col.setHeaderColumnName(" ");
        col.setColumnName("attributeNameN0");
        col.setDecimals(vo.getDecimals());
        //col.setPreferredWidth(110*(vo.getManagedVariants().size()-1));

        col.setPreferredWidth(50);

        grid.getColumnContainer().add(col,null);
      }

      // add grid to panel...
      this.add(grid,BorderLayout.CENTER);
      //grid.setSize(new Dimension(form.getWidth(),(splitPane==null?20:0)+40+vo.getRowDescriptors().size()*20));
      this.setMinimumSize(new Dimension(form.getWidth(),(splitPane==null?20:0)+30+vo.getRowDescriptors().size()*20));
      //form.setSize(new Dimension(form.getWidth(),form.getHeight()+(splitPane==null?20:0)+30+vo.getRowDescriptors().size()*20));
      if (splitPane!=null)
        splitPane.setDividerLocation(Math.max(100,initialDiv-grid.getHeaderHeight()-30-vo.getRowDescriptors().size()*20));
      this.revalidate();
      this.repaint();
      form.revalidate();
      form.repaint();
    }
  }


  /**
   * beforeLookupAction
   *
   * @param parentVO ValueObject
   */
  public void beforeLookupAction(ValueObject parentVO) {
  }


  /**
   * codeChanged
   *
   * @param parentVO ValueObject
   * @param parentChangedAttributes Collection
   */
  public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
    VariantsItemDescriptor lookupVO = (VariantsItemDescriptor)lookupController.getLookupVO();

    if (form.getMode()!=Consts.INSERT ||
        controlItemCode.getValue()==null ||
        controlItemCode.getValue().equals("") ||
        (lookupVO!=null &&
         !Boolean.TRUE.equals(lookupVO.getUseVariant1ITM01()) &&
         !Boolean.TRUE.equals(lookupVO.getUseVariant2ITM01()) &&
         !Boolean.TRUE.equals(lookupVO.getUseVariant3ITM01()) &&
         !Boolean.TRUE.equals(lookupVO.getUseVariant4ITM01()) &&
         !Boolean.TRUE.equals(lookupVO.getUseVariant5ITM01())
    ) ) {
      if (form.getMode()==Consts.INSERT)
        controlQty.setEnabled(true);
      if (splitPane!=null)
        splitPane.setDividerLocation(initialDiv);
      grid = null;
      this.removeAll();
      this.revalidate();
      this.setMinimumSize(new Dimension(0,0));
      this.setPreferredSize(new Dimension(0,0));
      this.repaint();
      form.revalidate();
      form.repaint();
    }
    else {
      this.removeAll();

      SwingUtilities.invokeLater(new Runnable() {

        public void run() {
          controlQty.setEnabled(false);
          //controlQty.setText("");
          controller.qtyUpdated(null);
        }

      });

      initGrid(lookupVO);
    }
  }

  /**
   * codeValidated
   *
   * @param validated boolean
   */
  public void codeValidated(boolean validated) {
  }

  /**
   * forceValidate
   */
  public void forceValidate() {
  }


  public final void setSN(StoredSerialNumberVO snVO) {
    this.snVO = snVO;
  }


  public final void setVariantsBarcode(VariantBarcodeVO barcodeVO) {
    this.barcodeVO = barcodeVO;
  }


  private void updateMatrixWithSN() {
    VariantsMatrixRowVO rowVO = null;
    VariantsMatrixColumnVO colVO = null;
    CustomValueObject vo = null;
    int cols = getVariantsMatrixVO().getColumnDescriptors().size()==0?1:getVariantsMatrixVO().getColumnDescriptors().size();
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors().get(i);
      vo = (CustomValueObject)grid.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getVariantTypeITM06().equals(snVO.getVariantTypeItm06WAR05()) &&
          rowVO.getVariantCodeITM11().equals(snVO.getVariantCodeItm11WAR05())) {
        if (getVariantsMatrixVO().getColumnDescriptors().size()==0)
          vo.setAttributeNameN0(new BigDecimal(1));
        else
          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors().get(j);
            if (colVO.getVariantCodeITM12().equals(snVO.getVariantCodeItm12WAR05()) &&
                colVO.getVariantCodeITM13().equals(snVO.getVariantCodeItm13WAR05()) &&
                colVO.getVariantCodeITM14().equals(snVO.getVariantCodeItm14WAR05()) &&
                colVO.getVariantCodeITM15().equals(snVO.getVariantCodeItm15WAR05()) &&
                colVO.getVariantTypeITM07().equals(snVO.getVariantTypeItm07WAR05()) &&
                colVO.getVariantTypeITM08().equals(snVO.getVariantTypeItm08WAR05()) &&
                colVO.getVariantTypeITM09().equals(snVO.getVariantTypeItm09WAR05()) &&
                colVO.getVariantTypeITM10().equals(snVO.getVariantTypeItm10WAR05())) {
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


  private void updateMatrixWithBarcode() {
    VariantsMatrixRowVO rowVO = null;
    VariantsMatrixColumnVO colVO = null;
    CustomValueObject vo = null;
    int cols = getVariantsMatrixVO().getColumnDescriptors().size()==0?1:getVariantsMatrixVO().getColumnDescriptors().size();
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors().get(i);
      vo = (CustomValueObject)grid.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getVariantTypeITM06().equals(barcodeVO.getVariantTypeItm06ITM22()) &&
          rowVO.getVariantCodeITM11().equals(barcodeVO.getVariantCodeItm11ITM22())) {
        if (getVariantsMatrixVO().getColumnDescriptors().size()==0)
          vo.setAttributeNameN0(new BigDecimal(1));
        else
          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors().get(j);
            if ((colVO.getVariantCodeITM12()==null && barcodeVO.getVariantCodeItm12ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM12().equals(barcodeVO.getVariantCodeItm12ITM22())) &&
                (colVO.getVariantCodeITM13()==null && barcodeVO.getVariantCodeItm13ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM13().equals(barcodeVO.getVariantCodeItm13ITM22())) &&
                (colVO.getVariantCodeITM14()==null && barcodeVO.getVariantCodeItm14ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM14().equals(barcodeVO.getVariantCodeItm14ITM22())) &&
                (colVO.getVariantCodeITM15()==null && barcodeVO.getVariantCodeItm15ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM15().equals(barcodeVO.getVariantCodeItm15ITM22())) &&
                (colVO.getVariantTypeITM07()==null && barcodeVO.getVariantTypeItm07ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM07().equals(barcodeVO.getVariantTypeItm07ITM22())) &&
                (colVO.getVariantTypeITM08()==null && barcodeVO.getVariantTypeItm08ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM08().equals(barcodeVO.getVariantTypeItm08ITM22())) &&
                (colVO.getVariantTypeITM09()==null && barcodeVO.getVariantTypeItm09ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM09().equals(barcodeVO.getVariantTypeItm09ITM22())) &&
                (colVO.getVariantTypeITM10()==null && barcodeVO.getVariantTypeItm10ITM22().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM10().equals(barcodeVO.getVariantTypeItm10ITM22()))) {
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


  /**
   * @return list of objects stored withing the grid
   */
  public Object[][] getCells() {
    if (grid==null)
      return null;

    Object[][] cells = new Object[grid.getVOListTableModel().getRowCount()][getVariantsMatrixVO().getColumnDescriptors().size()==0?1:getVariantsMatrixVO().getColumnDescriptors().size()];
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      for(int k=0;k<getVariantsMatrixVO().getColumnDescriptors().size();k++)
        if (grid.getVOListTableModel().getValueAt(i,k+1)!=null)
          cells[i][k] = grid.getVOListTableModel().getValueAt(i,k+1);
      if (getVariantsMatrixVO().getColumnDescriptors().size() == 0)
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
