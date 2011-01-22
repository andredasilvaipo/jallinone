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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import org.jallinone.variants.java.VariantsMatrixUtils;

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

  private boolean showButtons;

  /** server grid method name (optional); used for instance when loading variants prices */
  private String serverGridMethodName = null;

  private HashMap otherGridParams = new HashMap();

  private JPanel buttonsPanel = new JPanel();

  /** optional panel's controller */
  private ProductVariantsPanelController variantsPanelController;

  /** flag used to avoid infinitive loops */
  private boolean onValidating = false;


  public ProductVariantsPanel(
      ProductVariantsController controller,
      Form form,
      CodLookupControl controlItemCode,
      LookupController lookupController,
      String serverMatrixMethodName,
      NumericControl controlQty,
      JSplitPane splitPane,
      int initialDiv,
      boolean showButtons
  ) {
    try {
      this.controller = controller;
      this.form = form;
      this.controlItemCode = controlItemCode;
      this.lookupController = lookupController;
      this.serverMatrixMethodName = serverMatrixMethodName;
      this.controlQty = controlQty;
      this.splitPane = splitPane;
      this.initialDiv = initialDiv;
      this.showButtons = showButtons;
      this.formHeight = form.getPreferredSize().height;
      if (lookupController!=null)
        lookupController.addLookupListener(this);
      jbInit();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public Form getForm() {
    return form;
  }


  public LookupController getLookupController() {
    return lookupController;
  }


  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
  }


  public final JPanel getButtonsPanel() {
    return buttonsPanel;
  }


  public final void setServerGridMethodName(String serverGridMethodName) {
    this.serverGridMethodName = serverGridMethodName;
  }


  public final void initGrid(VariantsItemDescriptor lookupVO) {

    Response res = ClientUtils.getData(serverMatrixMethodName,lookupVO);
    if (!res.isError()) {
      // retrieve grid descriptor...
      VariantsMatrixVO vo = (VariantsMatrixVO)((VOResponse)res).getVo();

      // create grid...
      grid = new GridControl();
      grid.getOtherGridParams().putAll(otherGridParams);
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
          else {
            grid.setMode(Consts.EDIT);
          }

          if (variantsPanelController!=null)
            variantsPanelController.loadDataCompleted(error);

          grid.revalidate();
          grid.repaint();
        }

        public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
          try {
            onValidating = true;

            if (variantsPanelController!=null)
              if (!variantsPanelController.validateCell(rowNumber,grid.getTable().getGrid().getColumnIndex(attributeName),attributeName,(Number)oldValue,(Number)newValue))
                return false;

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
          catch (Exception ex) {
            ex.printStackTrace();
            return false;
          }
          finally {
            onValidating = false;
          }
        }

      });


      // set grid's data locator...
      if (serverGridMethodName!=null && !serverGridMethodName.equals("")) {
        ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
        gridDataLocator.setServerMethodName(serverGridMethodName);
        grid.setGridDataLocator(gridDataLocator);
      }
      else
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
            for(int i=0;i<matrixVO.getRowDescriptors().length;i++) {
              rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];

              vo = new CustomValueObject();
              vo.setAttributeNameS0(rowVO.getRowDescription());
              rows.add(vo);
            }
            return new VOListResponse(rows,false,rows.size());
          }

        });

      grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");
      grid.getOtherGridParams().put(ApplicationConsts.VARIANTS_MATRIX_VO,vo);
      grid.setReorderingAllowed(false);
      grid.setVisibleStatusPanel(false);
//      if (vo.getManagedVariants().length>1)
//        grid.setHeaderHeight(grid.getHeaderHeight()*(vo.getManagedVariants().length-1));

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
        col.setColumnName("attributeNameN"+i);
        col.setColumnRequired(false);
        col.setDecimals(vo.getDecimals());
        col.setMinWidth(50);

        col.setPreferredWidth(20+this.getFontMetrics(this.getFont()).stringWidth(col.getHeaderColumnName()));

        grid.getColumnContainer().add(col,null);
      }
      if (vo.getColumnDescriptors().length==0) {
        col = new DecimalColumn();
        col.setColumnSelectable(false);
        col.setEditableOnEdit(true);
        col.setHeaderColumnName(" ");
        col.setColumnName("attributeNameN0");
        col.setDecimals(vo.getDecimals());
        //col.setPreferredWidth(110*(vo.getManagedVariants().length-1));

        col.setPreferredWidth(50);

        grid.getColumnContainer().add(col,null);
      }

      grid.addFocusListener(new FocusAdapter() {
        public void focusLost(FocusEvent e) {
          if (!onValidating)
            grid.getTable().getGrid().stopCellEditing();
        }
      });


      buttonsPanel.removeAll();
      final VariantNameVO varVO = (VariantNameVO)getVariantsMatrixVO().getManagedVariants()[0];
      buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
      if (showButtons && vo.getManagedVariants().length>1) {
        // create a buttons panel used to copy the same qty for all cells matching the same variant value...
        for(int i=0;i<vo.getManagedVariants().length;i++) {
          GenericButton btn = new GenericButton(new ImageIcon(ClientUtils.getImage("copy.gif")));
          final VariantNameVO vnVO = (VariantNameVO)vo.getManagedVariants()[i];
          btn.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
          btn.setHorizontalTextPosition(SwingConstants.LEADING);
          btn.setVerticalTextPosition(SwingConstants.CENTER);
          btn.setText(vnVO.getDescriptionSYS10());
          btn.setToolTipText(ClientSettings.getInstance().getResources().getResource("set the same value when matching the same")+" "+vnVO.getDescriptionSYS10());
          btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             if (!onValidating)
               grid.getTable().getGrid().stopCellEditing();
            try {
               int row = grid.getSelectedRow();
               int col = grid.getTable().getGrid().getSelectedColumn();
               if (row!=-1 && col!=-1 && grid.getTable().getGrid().getValueAt(row,col)!=null) {
                 BigDecimal qty = (BigDecimal)grid.getTable().getGrid().getValueAt(row,col);
                 VariantsMatrixRowVO currentRowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors()[row];
                 VariantsMatrixColumnVO currentColVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors()[col-1];
                 String rowtype = null,rowcode = null;
                 String coltype = null,colcode = null;
                 String rowtypename = null,rowcodename = null;
                 String coltypename = null,colcodename = null;


                 if (getVariantsMatrixVO().getColumnDescriptors().length==0 ||
                     varVO.getTableName().equals(vnVO.getTableName())) {
                   rowtype = VariantsMatrixUtils.getVariantType(getVariantsMatrixVO(),currentRowVO);
                   rowcode = VariantsMatrixUtils.getVariantCode(getVariantsMatrixVO(),currentRowVO);

                   if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
                     rowtypename = "getVariantTypeITM06";
                     rowcodename = "getVariantCodeITM11";
                   }
                   else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
                     rowtypename = "getVariantTypeITM07";
                     rowcodename = "getVariantCodeITM12";
                   }
                   else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
                     rowtypename = "getVariantTypeITM08";
                     rowcodename = "getVariantCodeITM13";
                   }
                   else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
                     rowtypename = "getVariantTypeITM09";
                     rowcodename = "getVariantCodeITM14";
                   }
                   else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
                     rowtypename = "getVariantTypeITM10";
                     rowcodename = "getVariantCodeITM15";
                   }

                 }
                 else {
                   if (vnVO.getTableName().equals("ITM11_VARIANTS_1")) {
                     rowtype = currentRowVO.getVariantTypeITM06();
                     rowcode = currentRowVO.getVariantCodeITM11();
                     rowtypename = "getVariantTypeITM06";
                     rowcodename = "getVariantCodeITM11";
                   }
                   else if (vnVO.getTableName().equals("ITM12_VARIANTS_2")) {
                     coltype = currentColVO.getVariantTypeITM07();
                     colcode = currentColVO.getVariantCodeITM12();
                     coltypename = "getVariantTypeITM07";
                     colcodename = "getVariantCodeITM12";
                   }
                   else if (vnVO.getTableName().equals("ITM13_VARIANTS_3")) {
                     coltype = currentColVO.getVariantTypeITM08();
                     colcode = currentColVO.getVariantCodeITM13();
                     coltypename = "getVariantTypeITM08";
                     colcodename = "getVariantCodeITM13";
                   }
                   else if (vnVO.getTableName().equals("ITM14_VARIANTS_4")) {
                     coltype = currentColVO.getVariantTypeITM09();
                     colcode = currentColVO.getVariantCodeITM14();
                     coltypename = "getVariantTypeITM09";
                     colcodename = "getVariantCodeITM14";
                   }
                   else if (vnVO.getTableName().equals("ITM15_VARIANTS_5")) {
                     coltype = currentColVO.getVariantTypeITM10();
                     colcode = currentColVO.getVariantCodeITM15();
                     coltypename = "getVariantTypeITM10";
                     colcodename = "getVariantCodeITM15";
                   }
                 }


                 VariantsMatrixRowVO rowVO = null;
                 VariantsMatrixColumnVO colVO = null;
                 for(int r=0;r<grid.getVOListTableModel().getRowCount();r++) {
                   rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors()[r];
                   for (int c = 0;c < getVariantsMatrixVO().getColumnDescriptors().length;c++)
                     if (! (r == row && c + 1 == col)) {
                       colVO = (VariantsMatrixColumnVO) getVariantsMatrixVO().getColumnDescriptors()[c];
                      try {
                        if (rowtype!=null && rowcode!=null) {
                          if (rowtype.equals(rowVO.getClass().getMethod(rowtypename,new Class[0]).invoke(rowVO,new Object[0])) &&
                              rowcode.equals(rowVO.getClass().getMethod(rowcodename,new Class[0]).invoke(rowVO,new Object[0])))
                            grid.getVOListTableModel().setValueAt(qty, r, c + 1);
                        }
                        else if (coltype!=null && colcode!=null) {
                          if (coltype.equals(colVO.getClass().getMethod(coltypename,new Class[0]).invoke(colVO,new Object[0])) &&
                              colcode.equals(colVO.getClass().getMethod(colcodename,new Class[0]).invoke(colVO,new Object[0])))
                           grid.getVOListTableModel().setValueAt(qty, r, c + 1);
                        }
                      }
                      catch (Exception ex) {
                        ex.printStackTrace();
                      }
                     }
                 }
                 grid.repaint();
               }

              }
              catch (Throwable t) {
                t.printStackTrace();
              }
            } // end actionPerformed
          });
          buttonsPanel.add(btn);
        }
      }
      this.add(buttonsPanel,BorderLayout.NORTH);


      // add grid to panel...
      this.add(grid,BorderLayout.CENTER);
      //grid.setSize(new Dimension(form.getWidth(),(splitPane==null?20:0)+40+vo.getRowDescriptors().length*20));
      this.setMinimumSize(new Dimension(form.getWidth(),(splitPane==null?20:0)+30+vo.getRowDescriptors().length*20));
      //form.setSize(new Dimension(form.getWidth(),form.getHeight()+(splitPane==null?20:0)+30+vo.getRowDescriptors().length*20));
      if (splitPane!=null)
        splitPane.setDividerLocation(Math.max(100,initialDiv-grid.getHeaderHeight()-30-vo.getRowDescriptors().length*20));
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
   * Lookup code has been changed.
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
    if (!onValidating)
      grid.getTable().getGrid().stopCellEditing();

    VariantsMatrixRowVO rowVO = null;
    VariantsMatrixColumnVO colVO = null;
    CustomValueObject vo = null;
    int cols = getVariantsMatrixVO().getColumnDescriptors().length==0?1:getVariantsMatrixVO().getColumnDescriptors().length;
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors()[i];
      vo = (CustomValueObject)grid.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getVariantTypeITM06().equals(snVO.getVariantTypeItm06WAR05()) &&
          rowVO.getVariantCodeITM11().equals(snVO.getVariantCodeItm11WAR05())) {
        if (getVariantsMatrixVO().getColumnDescriptors().length==0)
          vo.setAttributeNameN0(new BigDecimal(1));
        else
          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors()[j];
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
    if (!onValidating)
      grid.getTable().getGrid().stopCellEditing();

    VariantsMatrixRowVO rowVO = null;
    VariantsMatrixColumnVO colVO = null;
    CustomValueObject vo = null;
    int cols = getVariantsMatrixVO().getColumnDescriptors().length==0?1:getVariantsMatrixVO().getColumnDescriptors().length;
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      rowVO = (VariantsMatrixRowVO)getVariantsMatrixVO().getRowDescriptors()[i];
      vo = (CustomValueObject)grid.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getVariantTypeITM06().equals(barcodeVO.getVariantTypeItm06ITM22()) &&
          rowVO.getVariantCodeITM11().equals(barcodeVO.getVariantCodeItm11ITM22())) {
        if (getVariantsMatrixVO().getColumnDescriptors().length==0)
          vo.setAttributeNameN0(new BigDecimal(1));
        else
          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)getVariantsMatrixVO().getColumnDescriptors()[j];
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
    if (!onValidating && grid!=null && grid.getTable()!=null && grid.getTable().getGrid()!=null)
      grid.getTable().getGrid().stopCellEditing();

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


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
  for(int i=0;i<vo.getManagedVariants().length;i++)
    if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
      return true;
  return false;
}
  public HashMap getOtherGridParams() {
    return otherGridParams;
  }
  public ProductVariantsPanelController getVariantsPanelController() {
    return variantsPanelController;
  }
  public void setVariantsPanelController(ProductVariantsPanelController variantsPanelController) {
    this.variantsPanelController = variantsPanelController;
  }



}
