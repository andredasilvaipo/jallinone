package org.jallinone.warehouse.documents.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.form.client.Form;
import java.awt.event.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.documents.java.*;
import java.math.BigDecimal;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.registers.measure.java.*;
import java.util.ArrayList;
import java.beans.Beans;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.table.client.GridController;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import java.util.HashSet;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the out delivery note rows grid + import sale doc rows panel.</p>
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
public class OutDeliveryNoteRowsGridPanel extends JPanel implements GenericButtonController {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  JSplitPane splitPane = new JSplitPane();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  CopyButton copyButton1 = new CopyButton();
  GridControl grid = new GridControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ExportButton exportButton1 = new ExportButton();
  DecimalColumn colRowNum = new DecimalColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colQty = new DecimalColumn();
  TextColumn colUmCode = new TextColumn();

  /** header v.o. */
  private DetailDeliveryNoteVO parentVO = null;

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  /** header panel */
  private Form headerPanel = null;

  /** detail frame */
  private OutDeliveryNoteFrame frame = null;

  private Form detailPanel = new Form();

  IntegerColumn colYear = new IntegerColumn();
  CodLookupColumn colDocNumLookup = new CodLookupColumn();
  CodLookupColumn colItemCodeLookup = new CodLookupColumn();
  ComboColumn colItemType = new ComboColumn();

  LookupController docRefController = new LookupController();
  LookupServerDataLocator docRefDataLocator = new LookupServerDataLocator();

  CodLookupColumn colPositionLookup = new CodLookupColumn();

  /** warehouse position lookup controller */
  LookupController posController = new LookupController();
  LookupServerDataLocator posDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosDataLocator = new TreeServerDataLocator();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder2;
  LabelControl labelSaleDocNr = new LabelControl();
  CodLookupControl controlSaleDocNr = new CodLookupControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelGrid = new LabelControl();
  GridControl orderRows = new GridControl();
  GenericButton importButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));

  LookupController saleDocController = new LookupController();
  LookupServerDataLocator saleDocDataLocator = new LookupServerDataLocator();

  /** order rows grid data locator */
  private ServerGridDataLocator orderRowsDataLocator = new ServerGridDataLocator();

  TextColumn colSaleItemCode = new TextColumn();
  TextColumn colSaleItemDescr = new TextColumn();
  DecimalColumn colSaleQty = new DecimalColumn();
  DecimalColumn colSaleOutQty = new DecimalColumn();
  DecimalColumn colOutQty = new DecimalColumn();

  private OutDeliveryNoteRowsController gridController = new OutDeliveryNoteRowsController(this);

  CodLookupColumn colSalePositionLookup = new CodLookupColumn();
  LookupController posSaleController = new LookupController();
  LookupServerDataLocator posSaleDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosSaleDataLocator = new TreeServerDataLocator();

  private boolean serialNumberRequired = false;

  ComboColumn colDocType = new ComboColumn();
  ComboBoxControl controlDocType = new ComboBoxControl();



  public OutDeliveryNoteRowsGridPanel(OutDeliveryNoteFrame frame,Form headerPanel) {
    this.frame = frame;
    this.headerPanel = headerPanel;
    try {
      jbInit();

      if (Beans.isDesignTime())
        return;

      init();

      grid.setController(gridController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadOutDeliveryNoteRows");


      // doc. ref. lookup...
      docRefDataLocator.setGridMethodName("loadSaleDocs");
      docRefDataLocator.setValidationMethodName("validateSaleDocNumber");
      docRefController.setLookupDataLocator(docRefDataLocator);
      docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
      docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);

      colDocNumLookup.setAllowOnlyNumbers(true);
      colDocNumLookup.setLookupController(docRefController);
      docRefController.setFrameTitle("confirmed sale documents");
      docRefController.setLookupValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocVO");
      docRefController.addLookup2ParentLink("docTypeDOC01","docTypeDoc01DOC10");
      docRefController.addLookup2ParentLink("docYearDOC01", "docYearDoc01DOC10");
      docRefController.addLookup2ParentLink("docNumberDOC01","docNumberDoc01DOC10");
      docRefController.addLookup2ParentLink("docSequenceDOC01","docSequenceDoc01DOC10");
      docRefController.setAllColumnVisible(false);
      docRefController.setVisibleColumn("companyCodeSys01DOC01", true);
//      docRefController.setVisibleColumn("docTypeDOC01", true);
      docRefController.setVisibleColumn("docYearDOC01", true);
      docRefController.setVisibleColumn("docSequenceDOC01", true);
      docRefController.setVisibleColumn("name_1REG04", true);
      docRefController.setVisibleColumn("name_2REG04", true);
      docRefController.setVisibleColumn("docDateDOC01", true);
//      docRefController.setVisibleColumn("docStateDOC01", true);
      docRefController.setPreferredWidthColumn("name_1REG04",200);
      docRefController.setPreferredWidthColumn("name_2REG04",150);
      docRefController.setFramePreferedSize(new Dimension(700,500));
      docRefController.setGroupingEnabledColumn("docYearDOC01", false);
      docRefController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_TYPE,vo.getDocTypeDoc01DOC10());
          docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_TYPE,vo.getDocTypeDoc01DOC10());
        }

        public void forceValidate() {}

      });


      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");

      itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);
      itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);

      colItemCodeLookup.setLookupController(itemController);
      colItemCodeLookup.setControllerMethodName("getItemsList");
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");
      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");
      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01DOC10");
      itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      itemController.addLookup2ParentLink("minSellingQtyUmCodeReg02ITM01","umCodeREG02");
      itemController.addLookup2ParentLink("decimalsREG02","decimalsREG02");
      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 200);
      itemController.setFramePreferedSize(new Dimension(650,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {

          // fill in the grid v.o., according to the selected item settings...
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          if (vo.getItemCodeItm01DOC10()==null || vo.getItemCodeItm01DOC10().equals("")) {
            vo.setDescriptionSYS10(null);
            vo.setUmCodeREG02(null);
            vo.setQtyDOC10(null);
            vo.setQtyDOC10(null);
            serialNumberRequired = false;
          }
          else {
            grid.repaint();
            serialNumberRequired = ((GridItemVO)itemController.getLookupVO()).getSerialNumberRequiredITM01().booleanValue();
          }

        }

        public void beforeLookupAction(ValueObject parentVO) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC10());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC10());
        }

        public void forceValidate() {}

      });

      // warehouse position code lookup...
      colPositionLookup.setLookupController(posController);
      posController.setLookupDataLocator(posDataLocator);
      posController.setFrameTitle("warehouse positions");

      posController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosDataLocator.setServerMethodName("loadHierarchy");
      posDataLocator.setTreeDataLocator(treeLevelPosDataLocator);
      posDataLocator.setNodeNameAttribute("descriptionSYS10");
      posController.setAllowTreeLeafSelectionOnly(false);
      posController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC10");
      posController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posController.setFramePreferedSize(new Dimension(400,400));
      posController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)parentVO;
          vo.setProgressiveHie02DOC10( OutDeliveryNoteRowsGridPanel.this.parentVO.getProgressiveHie02WAR01() );
          treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC10());
        }

        public void forceValidate() {}

      });



      // sale document lookup...
      saleDocDataLocator.setGridMethodName("loadSaleDocs");
      saleDocDataLocator.setValidationMethodName("validateSaleDocNumber");
      saleDocController.setLookupDataLocator(saleDocDataLocator);
      saleDocDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
      saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);

      controlSaleDocNr.setLookupController(saleDocController);
      controlSaleDocNr.setAllowOnlyNumbers(true);
      saleDocController.setFrameTitle("confirmed sale documents");
      saleDocController.setLookupValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocVO");
      saleDocController.addLookup2ParentLink("docTypeDOC01","docTypeDoc01DOC10");
      saleDocController.addLookup2ParentLink("docYearDOC01", "docYearDoc01DOC10");
      saleDocController.addLookup2ParentLink("docNumberDOC01","docNumberDoc01DOC10");
      saleDocController.addLookup2ParentLink("docSequenceDOC01","docSequenceDoc01DOC10");
      saleDocController.setAllColumnVisible(false);
      saleDocController.setVisibleColumn("companyCodeSys01DOC01", true);
//      saleDocController.setVisibleColumn("docTypeDOC01", true);
      saleDocController.setVisibleColumn("docYearDOC01", true);
      saleDocController.setVisibleColumn("docSequenceDOC01", true);
      saleDocController.setVisibleColumn("name_1REG04", true);
      saleDocController.setVisibleColumn("docDateDOC01", true);
//      saleDocController.setVisibleColumn("docStateDOC01", true);
      saleDocController.setHeaderColumnName("name_1REG04", "corporateName1");
      saleDocController.setPreferredWidthColumn("name_1REG04",200);
      saleDocController.setFramePreferedSize(new Dimension(700,500));
      saleDocController.setGroupingEnabledColumn("docYearDOC01", false);
//      saleDocController.setDomainColumn("docTypeDOC01","DOC_TYPE");
      saleDocController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected item settings...
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)detailPanel.getVOModel().getValueObject();
          if (vo.getDocNumberDoc01DOC10()==null) {
            orderRows.setMode(Consts.READONLY);
            orderRows.clearData();
            importButton.setEnabled(false);
          }
          else {
            SaleDocPK pk = new SaleDocPK(
                OutDeliveryNoteRowsGridPanel.this.parentVO.getCompanyCodeSys01DOC08(),
                vo.getDocTypeDoc01DOC10(),
                vo.getDocYearDoc01DOC10(),
                vo.getDocNumberDoc01DOC10()
            );
            orderRows.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
            orderRows.reloadData();
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)detailPanel.getVOModel().getValueObject();
          saleDocDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_TYPE,vo.getDocTypeDoc01DOC10());
          saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_TYPE,vo.getDocTypeDoc01DOC10());
        }

        public void forceValidate() {}

      });

      orderRows.setGridDataLocator(orderRowsDataLocator);
      orderRowsDataLocator.setServerMethodName("loadSaleDocAndDelivNoteRows");
      orderRows.setController(new GridController() {

        public void loadDataCompleted(boolean error) {
          if (error || orderRows.getVOListTableModel().getRowCount()==0)
            return;
          orderRows.setMode(Consts.EDIT);

          Response res = ClientUtils.getData("getRootLevel",parentVO.getProgressiveHie02WAR01());
          if (!res.isError()) {
            HierarchyLevelVO posVO = (HierarchyLevelVO)((VOResponse)res).getVo();
            GridOutDeliveryNoteRowVO rowVO = null;
            for(int i=0;i<orderRows.getVOListTableModel().getRowCount();i++) {
              rowVO = (GridOutDeliveryNoteRowVO)orderRows.getVOListTableModel().getObjectForRow(i);
              rowVO.setProgressiveHie01DOC10(posVO.getProgressiveHIE01());
              rowVO.setLocationDescriptionSYS10(posVO.getDescriptionSYS10());
              rowVO.setQtyDOC10(rowVO.getQtyDOC02().subtract(rowVO.getOutQtyDOC02()).setScale(rowVO.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
            }
          }
          orderRows.repaint();

          importButton.setEnabled(true);
        }

      });




      // warehouse position code in sale doc rows grid lookup...
      colSalePositionLookup.setLookupController(posSaleController);
      posSaleController.setLookupDataLocator(posSaleDataLocator);
      posSaleController.setFrameTitle("warehouse positions");

      posSaleController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosSaleDataLocator.setServerMethodName("loadHierarchy");
      posSaleDataLocator.setTreeDataLocator(treeLevelPosSaleDataLocator);
      posSaleDataLocator.setNodeNameAttribute("descriptionSYS10");
      posSaleController.setAllowTreeLeafSelectionOnly(false);
      posSaleController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posSaleController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC10");
      posSaleController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posSaleController.setFramePreferedSize(new Dimension(400,400));
      posSaleController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)parentVO;

          vo.setProgressiveHie02DOC10( OutDeliveryNoteRowsGridPanel.this.parentVO.getProgressiveHie02WAR01() );
          treeLevelPosSaleDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC10());
        }

        public void forceValidate() {}

      });


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    if (parentVO!=null && parentVO.getDocStateDOC08()!=null && parentVO.getDocStateDOC08().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
  }


  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
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

    colItemType.setDomain(d);
    colItemType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange()==e.SELECTED && grid.getMode()!=Consts.READONLY) {
          GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          vo.setItemCodeItm01DOC10(null);
          vo.setDescriptionSYS10(null);
          vo.setUmCodeREG02(null);
          vo.setQtyDOC10(null);
          vo.setQtyDOC10(null);

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          Object selValue = d.getDomainPairList()[selIndex].getCode();
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
        }

      }

    });


    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(insertButton1);
    buttonsToDisable.add(copyButton1);
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    grid.addButtonsNotEnabled(buttonsToDisable,this);

  }



  private void jbInit() throws Exception {
    controlDocYear.setEnabled(false);
    titledBorder2 = new TitledBorder("");
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("unload items from sale document"));
    titledBorder2.setTitleColor(Color.blue);
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setAutoLoadData(false);
    grid.setCopyButton(copyButton1);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC08_OUT");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    detailPanel.setVOClassName("org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO");
    grid.setValueObjectClassName("org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO");
    colRowNum.setColumnFilterable(false);
    colRowNum.setColumnName("rowNumberDOC10");
    colRowNum.setColumnRequired(false);
    colRowNum.setColumnVisible(false);
    colRowNum.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colRowNum.setColumnSelectable(false);

    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(false);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(200);
    colQty.setDecimals(5);
    colQty.setGrouping(false);
    colQty.setColumnName("qtyDOC10");
    colQty.setColumnSortable(false);
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(50);

    colUmCode.setColumnName("umCodeREG02");
    colUmCode.setHeaderColumnName("um");
    colUmCode.setColumnSortable(false);
    colUmCode.setPreferredWidth(40);

    insertButton1.setEnabled(false);
    copyButton1.setEnabled(false);
    saveButton1.setEnabled(false);
    deleteButton1.setEnabled(false);
    exportButton1.setEnabled(false);
    editButton1.setEnabled(false);

    colDocType.setDomainId("SALE_DOC_TYPE");
    colDocType.setColumnDuplicable(true);
    colDocType.setColumnFilterable(true);
    colDocType.setColumnName("docTypeDoc01DOC10");
    colDocType.setColumnSortable(true);
    colDocType.setEditableOnInsert(true);
    colDocType.setEditableOnEdit(false);
    colDocType.setHeaderColumnName("docType");
    colDocType.setPreferredWidth(85);

    colYear.setColumnDuplicable(true);
    colYear.setColumnFilterable(true);
    colYear.setColumnName("docYearDoc01DOC10");
    colYear.setColumnSortable(true);
    colYear.setEditableOnEdit(false);
    colYear.setHeaderColumnName("docYearDoc01DOC10");
    colYear.setPreferredWidth(70);
    colDocNumLookup.setColumnDuplicable(true);
    colDocNumLookup.setColumnFilterable(true);
    colDocNumLookup.setColumnName("docSequenceDoc01DOC10");
    colDocNumLookup.setColumnSortable(true);
    colDocNumLookup.setEditableOnEdit(false);
    colDocNumLookup.setEditableOnInsert(true);
    colDocNumLookup.setHeaderColumnName("docSequenceDoc01DOC10");
    colDocNumLookup.setPreferredWidth(70);
    colDocNumLookup.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colDocNumLookup.setAllowOnlyNumbers(true);
    colItemCodeLookup.setColumnName("itemCodeItm01DOC10");
    colItemCodeLookup.setEditableOnEdit(false);
    colItemCodeLookup.setEditableOnInsert(true);
    colItemCodeLookup.setPreferredWidth(90);
    colItemCodeLookup.setMaxCharacters(20);
    colItemType.setColumnName("progressiveHie02DOC10");
    colItemType.setEditableOnInsert(true);
    colItemType.setPreferredWidth(80);
    colPositionLookup.setColumnDuplicable(true);
    colPositionLookup.setColumnFilterable(true);
    colPositionLookup.setColumnName("locationDescriptionSYS10");
    colPositionLookup.setColumnSortable(true);
    colPositionLookup.setEditableOnEdit(true);
    colPositionLookup.setEditableOnInsert(true);
    colPositionLookup.setHeaderColumnName("locationDescriptionSYS10");
    colPositionLookup.setPreferredWidth(200);
    colPositionLookup.setEnableCodBox(false);
    detailPanel.setLayout(gridBagLayout2);
    detailPanel.setBorder(titledBorder2);
    labelSaleDocNr.setText("docSequenceDoc01DOC10");
    labelDocYear.setText("docYearDoc01DOC10");
    labelGrid.setText("sale document rows");
    importButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("import sale document rows"));
    importButton.setText(ClientSettings.getInstance().getResources().getResource("import sale document rows"));
    importButton.setEnabled(false);
    importButton.addActionListener(new OutDeliveryNoteRowsGridPanel_importButton_actionAdapter(this));
    controlSaleDocNr.setEnabledOnEdit(true);
    controlSaleDocNr.setLinkLabel(labelSaleDocNr);
    controlSaleDocNr.setMaxCharacters(20);
    controlSaleDocNr.setAttributeName("docSequenceDoc01DOC10");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDoc01DOC10");
    orderRows.setAutoLoadData(false);
    orderRows.setVisibleStatusPanel(false);
    colSaleOutQty.setEditableOnEdit(false);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(splitPane,  BorderLayout.CENTER);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    splitPane.add(grid, JSplitPane.TOP);
    grid.getColumnContainer().add(colRowNum, null);
    grid.getColumnContainer().add(colDocType, null);
    grid.getColumnContainer().add(colYear, null);
    grid.getColumnContainer().add(colDocNumLookup, null);
    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colItemCodeLookup, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colUmCode, null);
    splitPane.add(detailPanel, JSplitPane.BOTTOM);

    controlDocType.setDomainId("SALE_DOC_TYPE");
    controlDocType.setAttributeName("docTypeDoc01DOC10");

    detailPanel.add(labelSaleDocNr,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    detailPanel.add(controlDocType,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    detailPanel.add(controlSaleDocNr,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 70, 0));
    detailPanel.add(labelDocYear,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    detailPanel.add(controlDocYear,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 70, 0));
    detailPanel.add(labelGrid,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(orderRows,   new GridBagConstraints(0, 2, 5, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(importButton,    new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    splitPane.setDividerLocation(220);
    grid.getColumnContainer().add(colPositionLookup, null);

    colSaleItemCode.setColumnFilterable(false);
    colSaleItemCode.setColumnName("itemCodeItm01DOC10");
    colSaleItemCode.setColumnSortable(false);
    colSaleItemCode.setPreferredWidth(80);
    colSaleItemDescr.setColumnName("descriptionSYS10");
    colSaleItemDescr.setColumnSortable(false);
    colSaleItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colSaleItemDescr.setPreferredWidth(200);
    colSaleQty.setDecimals(5);
    colSaleQty.setGrouping(false);
    colSaleQty.setColumnName("qtyDOC02");
    colSaleQty.setHeaderColumnName("saleQtyDOC02");
    colSaleQty.setColumnSortable(false);
    colSaleQty.setPreferredWidth(80);
    colSaleOutQty.setDecimals(5);
    colSaleOutQty.setGrouping(false);
    colSaleOutQty.setColumnName("outQtyDOC02");
    colSaleOutQty.setHeaderColumnName("saleOutQtyDOC02");
    colSaleOutQty.setColumnSortable(false);
    colSaleOutQty.setPreferredWidth(80);

    colOutQty.setDecimals(5);
    colOutQty.setGrouping(false);
    colOutQty.setColumnName("qtyDOC10");
    colOutQty.setColumnSortable(false);
    colOutQty.setPreferredWidth(80);
    colOutQty.setEditableOnEdit(true);

    colSalePositionLookup.setColumnDuplicable(true);
    colSalePositionLookup.setColumnFilterable(true);
    colSalePositionLookup.setColumnName("locationDescriptionSYS10");
    colSalePositionLookup.setColumnSortable(true);
    colSalePositionLookup.setEditableOnEdit(true);
    colSalePositionLookup.setHeaderColumnName("locationDescriptionSYS10");
    colSalePositionLookup.setPreferredWidth(200);
    colSalePositionLookup.setEnableCodBox(false);

    orderRows.setValueObjectClassName("org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO");
    orderRows.getColumnContainer().add(colSaleItemCode, null);
    orderRows.getColumnContainer().add(colSaleItemDescr, null);
    orderRows.getColumnContainer().add(colSaleQty, null);
    orderRows.getColumnContainer().add(colSaleOutQty, null);
    orderRows.getColumnContainer().add(colOutQty, null);
    orderRows.getColumnContainer().add(colSalePositionLookup, null);
  }


  public void setButtonsEnabled(boolean enabled) {
    if (enabled) {
      insertButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
      copyButton1.setEnabled(enabled);
    }
    else {
      copyButton1.setEnabled(enabled);
      insertButton1.setEnabled(enabled);
      editButton1.setEnabled(enabled);
      saveButton1.setEnabled(enabled);
      deleteButton1.setEnabled(enabled);
      exportButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
      importButton.setEnabled(enabled);
    }
  }


  public void setParentVO(DetailDeliveryNoteVO parentVO) {
    this.parentVO = parentVO;

    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());

    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());

    saleDocDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    saleDocDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    saleDocDataLocator.getLookupFrameParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());
    saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());

    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_YEAR,parentVO.getDocYearDOC08());
    saleDocDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_YEAR,parentVO.getDocYearDOC08());

    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());


    setButtonsEnabled(true);

    if (parentVO.getDocStateDOC08().equals(ApplicationConsts.CLOSED)) {
      controlSaleDocNr.setEnabled(false);
      controlDocType.setEnabled(false);
    }
    else {
      controlSaleDocNr.setEnabled(true);
      controlDocType.setEnabled(true);
      controlDocType.getComboBox().setSelectedIndex(0);
    }
  }


  public GridControl getGrid() {
    return grid;
  }


  public DetailDeliveryNoteVO getParentVO() {
    return parentVO;
  }


  public Form getHeaderPanel() {
    return headerPanel;
  }


  public GridControl getOrders() {
    return frame.getOrders();
  }
  public OutDeliveryNoteFrame getFrame() {
    return frame;
  }


  /**
   * <p>Description: Inner class used to define qty column settings for each grid row.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class QtyColumnDynamicSettings implements DecimalColumnSettings {

    public double getMaxValue(int row) {
      return colQty.getMaxValue();
    }

    public double getMinValue(int row) {
      return colQty.getMinValue();
    }

    public boolean isGrouping(int row) {
      return colQty.isGrouping();
    }

    public int getDecimals(int row) {
      GridOutDeliveryNoteRowVO vo = (GridOutDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(row);
      return vo.getDecimalsREG02().intValue();
    }
  }


  void importButton_actionPerformed(ActionEvent e) {

    orderRows.setMode(Consts.READONLY);

    GridOutDeliveryNoteRowVO rowVO = null;
    Response res = null;
    ArrayList list = new ArrayList();
    for(int i=0;i<orderRows.getVOListTableModel().getRowCount();i++) {
      // scroll over the sale document rows and insert specified item quantities...
      rowVO = (GridOutDeliveryNoteRowVO)orderRows.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getQtyDOC10()!=null &&
          rowVO.getQtyDOC10().doubleValue()>0 &&
          rowVO.getProgressiveHie01DOC10()!=null) {
        try {
          rowVO.setWarehouseCodeWar01DOC08(parentVO.getWarehouseCodeWar01DOC08());
          rowVO.setDocNumberDOC10(parentVO.getDocNumberDOC08());
          rowVO.setDocTypeDOC10(parentVO.getDocTypeDOC08());
          rowVO.setDocYearDOC10(parentVO.getDocYearDOC08());

          serialNumberRequired = rowVO.getSerialNumberRequiredITM01().booleanValue();

          list.clear();
          list.add(rowVO);
          res = gridController.insertRecords(new int[]{0}, list);
          if (res.isError()) {
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                res.getErrorMessage(),
                ClientSettings.getInstance().getResources().getResource("Error"),
                JOptionPane.ERROR_MESSAGE
            );
            break;
          }
          else {
            getFrame().enabledConfirmButton();
            frame.getHeaderFormPanel().reload();
          }

        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    grid.reloadData();
    orderRows.clearData();
    orderRows.revalidate();
    orderRows.repaint();
    importButton.setEnabled(false);
    rowVO = (GridOutDeliveryNoteRowVO)detailPanel.getVOModel().getValueObject();
    rowVO.setDocYearDoc01DOC10(null);
    rowVO.setDocNumberDoc01DOC10(null);
    detailPanel.pull();

  }


  /**
   * Method called by item row saving algorithm to prompt the list of serial numbers (if required).
   */
  public final boolean isSerialNumberRequired() {
    return serialNumberRequired;
  }


}

class OutDeliveryNoteRowsGridPanel_importButton_actionAdapter implements java.awt.event.ActionListener {
  OutDeliveryNoteRowsGridPanel adaptee;

  OutDeliveryNoteRowsGridPanel_importButton_actionAdapter(OutDeliveryNoteRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.importButton_actionPerformed(e);
  }
}

