package org.jallinone.items.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jallinone.commons.client.*;
import org.jallinone.commons.java.*;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.variants.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;
import org.jallinone.items.java.ItemToPrintVO;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.openswing.swing.form.client.Form;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.variants.client.ProductVariantsController;
import java.math.BigDecimal;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.lookup.client.LookupListener;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.form.client.FormController;
import org.jallinone.variants.java.VariantsMatrixColumnVO;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to print barcode labels for a set of items.</p>
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
public class PrintBarcodeLabelsFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  GridControl grid = new GridControl();

  private ArrayList rows = new ArrayList();

  /** grid data locator */
  private GridDataLocator gridDataLocator = new GridDataLocator() {

    public Response loadData(int action, int startIndex, Map filteredColumns,
                             ArrayList currentSortedColumns,
                             ArrayList currentSortedVersusColumns,
                             Class valueObjectType, Map otherGridParams) {
      return new VOListResponse(rows,false,rows.size());
    }
  };
  TextColumn coItemCode = new TextColumn();
  TextColumn colDescr = new TextColumn();
  IntegerColumn colQty = new IntegerColumn();
  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();
  HashMap variantsNames = new HashMap();
  GenericButton printLabelsButton = new GenericButton(new ImageIcon(ClientUtils.getImage("barcode.gif")));
  Form itemPanel = new Form();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  LabelControl labelItem = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();
  CodLookupControl controlItemCode = new CodLookupControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelQty = new LabelControl();
  NumericControl controlQty = new NumericControl();
  JSplitPane splitPane = new JSplitPane();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  private int splitDiv = 350;

  private ProductVariantsPanel variantsPanel = new ProductVariantsPanel(
      new ProductVariantsController() {

        public BigDecimal validateQty(BigDecimal qty) {
          return qty;
        }

        public void qtyUpdated(BigDecimal qty) {
        }

      },
      itemPanel,
      controlItemCode,
      itemController,
      "loadProductVariantsMatrix",
      controlQty,
      splitPane,
      splitDiv,
      true
  );
  DeleteButton deleteButton = new DeleteButton();
  InsertButton insertButton = new InsertButton();


  public PrintBarcodeLabelsFrame() {
    grid.setController(new GridController() {

      /**
       * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
       * @param persistentObjects value objects to delete (related to the currently selected rows)
       * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
       */
      public Response deleteRecords(ArrayList persistentObjects) throws Exception {
        rows.remove( grid.getSelectedRow() );
        return new VOResponse(Boolean.TRUE);
      }

    });
    grid.setGridDataLocator(gridDataLocator);
    try {
      jbInit();
      init();

      itemPanel.setMode(Consts.INSERT);
      controlQty.setValue(new BigDecimal(1));

      setSize(620,550);
      setMinimumSize(new Dimension(600,550));
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    this.setAskBeforeClose(false);
    itemPanel.setFormController(new FormController() {
    });

    // item code lookup...
    itemDataLocator.setGridMethodName("loadItems");
    itemDataLocator.setValidationMethodName("validateItemCode");

    controlItemCode.setLookupController(itemController);
    itemController.setForm(itemPanel);
    itemController.setLookupDataLocator(itemDataLocator);
    itemController.setFrameTitle("items");

    itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
    treeLevelDataLocator.setServerMethodName("loadHierarchy");
    itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
    itemDataLocator.setNodeNameAttribute("descriptionSYS10");

    itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
    itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01");
    itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01");
    itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

    itemController.setAllColumnVisible(false);
    itemController.setVisibleColumn("itemCodeITM01", true);
    itemController.setVisibleColumn("descriptionSYS10", true);
    itemController.setPreferredWidthColumn("descriptionSYS10", 200);
    itemController.setFramePreferedSize(new Dimension(550,500));
    itemController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        // fill in the detail form, according to the selected item settings...
        ItemToPrintVO vo = (ItemToPrintVO)itemPanel.getVOModel().getValueObject();
        if (vo.getItemCodeItm01()==null || vo.getItemCodeItm01().equals("")) {
          vo.setItemCodeItm01(null);
          vo.setDescriptionSYS10(null);
          vo.setQty(null);
        }
      }

      public void beforeLookupAction(ValueObject parentVO) {
        Object companyCodeSys01 = controlCompaniesCombo.getValue();
        if (companyCodeSys01==null) {
        	if (controlCompaniesCombo.getDomain()!=null && controlCompaniesCombo.getDomain().getDomainPairList().length>0)
        		companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();
        	else {
                ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
                ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
                ArrayList companiesList = bca.getCompaniesList(controlCompaniesCombo.getFunctionCode());
                companyCodeSys01 = companiesList.get(0).toString();
        	}
        }

        itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
        itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
      }

      public void forceValidate() {}

    });


    // retrieve item types and fill in the item types combo box and set buttons disabilitation...
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
    final Domain d = new Domain("ITEM_TYPES");
    if (!res.isError()) {
      ItemTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (ItemTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
      }
    }
    controlItemType.setDomain(d);
    controlItemType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          ItemToPrintVO vo = (ItemToPrintVO)itemPanel.getVOModel().getValueObject();
          vo.setItemCodeItm01(null);
          vo.setDescriptionSYS10(null);
          vo.setQty(null);

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          Object selValue = d.getDomainPairList()[selIndex].getCode();
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);

          try {
            itemPanel.pull(controlItemCode.getAttributeName());
            controlItemCode.validateCode(null);
          }
          catch (Exception ex) {
          }

        }
      }
    });


  }


  private void jbInit() throws Exception {

    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setValueObjectClassName("org.jallinone.items.java.ItemToPrintVO");
    grid.setVisibleStatusPanel(false);
    printLabelsButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print barcode labels"));
    this.setTitle(ClientSettings.getInstance().getResources().getResource("print barcode labels"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setFunctionId("BARCODE_LABELS");
    coItemCode.setColumnName("itemCodeItm01");
    coItemCode.setColumnSortable(true);
    coItemCode.setHeaderColumnName("itemCodeITM01");
    coItemCode.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setMaxCharacters(255);
    colDescr.setPreferredWidth(400);

    itemPanel.setVOClassName("org.jallinone.items.java.ItemToPrintVO");

    colQty.setColumnName("qty");
    colQty.setPreferredWidth(60);

    controlItemCode.setAttributeName("itemCodeItm01");
    controlItemCode.setMaxCharacters(20);

    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout2);
    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");
    printLabelsButton.addActionListener(new PrintBarcodeLabelsFrame_buttonPrintLabels_actionAdapter(this));
    itemPanel.setLayout(gridBagLayout3);
    labelItem.setLabel("itemCodeITM01");
    controlDescr.setEnabled(false);
    controlDescr.setAttributeName("descriptionSYS10");
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    labelItem.setLabel("item");
    controlQty.setDecimals(5);
    controlQty.setAttributeName("qty");
    insertButton.addActionListener(new PrintBarcodeLabelsFrame_insertButton_actionAdapter(this));
    labelQty.setLabel("qty");
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(printLabelsButton, null);

    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    this.getContentPane().add(splitPane,  BorderLayout.CENTER);
    splitPane.add(grid, JSplitPane.TOP);
    splitPane.add(itemPanel, JSplitPane.BOTTOM);
    splitPane.setDividerLocation(350);

    grid.getColumnContainer().add(coItemCode, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colQty, null);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    itemPanel.add(labelItem,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    itemPanel.add(controlItemType,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    itemPanel.add(controlItemCode,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    itemPanel.add(controlDescr,    new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    itemPanel.add(labelItem,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    itemPanel.add(labelQty,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    itemPanel.add(controlQty,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    itemPanel.add(insertButton,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    itemPanel.add(variantsPanel,          new GridBagConstraints(0, 3, 4, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    controlCompaniesCombo.setLinkLabel(companyLabel);
    controlCompaniesCombo.setFunctionCode(grid.getFunctionId());

    topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topPanel.add(filterPanel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    filterPanel.add(companyLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCompaniesCombo,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


  }


  void buttonPrintLabels_actionPerformed(ActionEvent e) {
    if (grid.getVOListTableModel().getRowCount()==0)
      return;

    HashMap map = new HashMap();
    map.put(
      ApplicationConsts.ITEMS,
      grid.getVOListTableModel().getDataVector()
    );
    Response res = ClientUtils.getData("createBarcodeLabelsData",map);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print barcode labels"),
        JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      Object reportId = ((VOResponse)res).getVo();


      HashMap params = new HashMap();
      params.put("REPORT_ID",reportId);

      ItemToPrintVO vo = (ItemToPrintVO)grid.getVOListTableModel().getObjectForRow(0);

      map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01());
      map.put(ApplicationConsts.FUNCTION_CODE_SYS06,"BARCODE_LABELS");
      map.put(ApplicationConsts.EXPORT_PARAMS,params);
      res = ClientUtils.getData("getJasperReport",map);
      if (!res.isError()) {
        JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
        JRViewer viewer = new JRViewer(print);
        JFrame frame = new JFrame();
        frame.setSize(MDIFrame.getInstance().getSize());
        frame.setContentPane(viewer);
        frame.setTitle(this.getTitle());
        frame.setIconImage(MDIFrame.getInstance().getIconImage());
        frame.setVisible(true);
        res = ClientUtils.getData("deleteBarcodeLabelsData",reportId);
      } else
        JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("print barcode labels"),
          JOptionPane.ERROR_MESSAGE
        );
    }

  }

  void insertButton_actionPerformed(ActionEvent e) {
    try {
      if (controlItemCode.getValue()!=null &&
          controlQty.getValue()!=null) {
        ItemToPrintVO vo = (ItemToPrintVO)itemPanel.getVOModel().getValueObject();
        Object[][] cells = variantsPanel.getCells();
        if (cells==null) {
          try {
            vo = (ItemToPrintVO) vo.clone();
          }
          catch (CloneNotSupportedException ex) {
          }
          vo.setVariantTypeItm06(ApplicationConsts.JOLLY);
          vo.setVariantTypeItm07(ApplicationConsts.JOLLY);
          vo.setVariantTypeItm08(ApplicationConsts.JOLLY);
          vo.setVariantTypeItm09(ApplicationConsts.JOLLY);
          vo.setVariantTypeItm10(ApplicationConsts.JOLLY);
          vo.setVariantCodeItm11(ApplicationConsts.JOLLY);
          vo.setVariantCodeItm12(ApplicationConsts.JOLLY);
          vo.setVariantCodeItm13(ApplicationConsts.JOLLY);
          vo.setVariantCodeItm14(ApplicationConsts.JOLLY);
          vo.setVariantCodeItm15(ApplicationConsts.JOLLY);
          rows.add(vo);
          grid.reloadData();
        }
        else {
          Object[] row = null;
          VariantsMatrixRowVO rowVO = null;
          VariantsMatrixColumnVO colMatrixVO = null;
          VariantsMatrixVO matrixVO = variantsPanel.getVariantsMatrixVO();
					String descr = vo.getDescriptionSYS10();
          for(int i=0;i<cells.length;i++) {
            row = cells[i];
            for(int j=0;j<row.length;j++) {
              if (cells[i][j]!=null) {
								BigDecimal qty = null;
								try {
									qty = (BigDecimal) cells[i][j];
								}
								catch (Exception ex2) {
									qty = new BigDecimal(0);
								}
                if (qty.intValue()>0) {
                  try {
                    vo = (ItemToPrintVO) vo.clone();
                  }
                  catch (CloneNotSupportedException ex) {
                  }
                  vo.setQty(qty);
                  rowVO = (VariantsMatrixRowVO) matrixVO.getRowDescriptors()[i];
									vo.setDescriptionSYS10(descr+" "+rowVO.getRowDescription());

                  if (variantsPanel.getVariantsMatrixVO().getColumnDescriptors().length==0) {
                    VariantsMatrixUtils.setVariantTypesAndCodes(vo,"",variantsPanel.getVariantsMatrixVO(),rowVO,null);
                  }
                  else {
                    colMatrixVO = (VariantsMatrixColumnVO )variantsPanel.getVariantsMatrixVO().getColumnDescriptors()[j];
                    VariantsMatrixUtils.setVariantTypesAndCodes(vo,"",variantsPanel.getVariantsMatrixVO(),rowVO,colMatrixVO);
										vo.setDescriptionSYS10(vo.getDescriptionSYS10()+" "+colMatrixVO.getColumnDescription());
                  }


                  rows.add(vo);
                } // end if on qty >0
              } // end if cell is not null
            } // end inner for
          } // end outer for

          grid.reloadData();

        } // end else
      }
    }
    catch (Throwable ex1) {
      ex1.printStackTrace();
    }
  }



  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }



}

class PrintBarcodeLabelsFrame_buttonPrintLabels_actionAdapter implements java.awt.event.ActionListener {
  PrintBarcodeLabelsFrame adaptee;

  PrintBarcodeLabelsFrame_buttonPrintLabels_actionAdapter(PrintBarcodeLabelsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonPrintLabels_actionPerformed(e);
  }
}

class PrintBarcodeLabelsFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  PrintBarcodeLabelsFrame adaptee;

  PrintBarcodeLabelsFrame_insertButton_actionAdapter(PrintBarcodeLabelsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}

