package org.jallinone.documents.client;

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
import java.math.BigDecimal;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import org.jallinone.documents.java.DocumentTypeVO;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the documents tree+grid frame.</p>
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
public class DocumentsFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  JSplitPane split = new JSplitPane();
  LabelControl labelDocumentType = new LabelControl();
  InsertButton insertButton1 = new InsertButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ComboBoxControl comboBoxControl1 = new ComboBoxControl();
  HierarTreePanel hierarTreePanel = new HierarTreePanel();
  GridControl grid = new GridControl();
  TextColumn colCod = new TextColumn();
  TextColumn colDescr = new TextColumn();
  TextColumn colFile = new TextColumn();
  TextColumn colCompany = new TextColumn();
  JSplitPane subsplit = new JSplitPane();
  DocFilterPanel filterPanel = new DocFilterPanel();
  private java.util.List docTypeList = null;

  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();


  public DocumentsFrame(final DocumentsController documentsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            documentsController.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      grid.setController(documentsController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadDocuments");
      hierarTreePanel.setFunctionId("DOC16");
      hierarTreePanel.setTreeController(documentsController);


//      grid.enableDrag(ApplicationConsts.ID_DOCUMENTS_GRID.toString());

      init();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public DocumentTypeVO getSelectedDocumentType() {
    if (comboBoxControl1.getSelectedIndex()==-1)
      return null;
    return (DocumentTypeVO)docTypeList.get(comboBoxControl1.getSelectedIndex());
  }


  /**
   * Retrieve document types and fill in the document types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadDocumentTypes",new GridParams());
    Domain d = new Domain("DOCUMENT_TYPES");
    if (!res.isError()) {
      DocumentTypeVO vo = null;
      docTypeList = ((VOListResponse)res).getRows();
      for(int i=0;i<docTypeList.size();i++) {
        vo = (DocumentTypeVO)docTypeList.get(i);
        d.addDomainPair(vo.getProgressiveHie02DOC16(),vo.getDescriptionSYS10());
      }
    }
    comboBoxControl1.setDomain(d);
    comboBoxControl1.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          hierarTreePanel.setProgressiveHIE02((BigDecimal)comboBoxControl1.getValue());
          hierarTreePanel.reloadTree();
          grid.clearData();
          filterPanel.clearData();
        }
      }
    });
    if (d.getDomainPairList().length>=1)
      comboBoxControl1.getComboBox().setSelectedIndex(0);
    else
      comboBoxControl1.getComboBox().setSelectedIndex(-1);
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("documents"));
    grid.setValueObjectClassName("org.jallinone.documents.java.GridDocumentVO");
    labelDocumentType.setText("document type");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colCod.setColumnFilterable(true);
    colCod.setColumnName("progressiveDOC14");
    colCod.setColumnSortable(true);
    colCod.setColumnVisible(false);
    colCod.setColumnSelectable(false);
    colCod.setSortingOrder(2);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionDOC14");
    colDescr.setColumnSortable(true);
    colDescr.setPreferredWidth(400);
    colDescr.setSortingOrder(0);
    colDescr.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setColumnFilterable(true);
    colCompany.setPreferredWidth(110);
    colCompany.setColumnName("companyCodeSys01DOC14");
    colCompany.setColumnSortable(true);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(1);

    colFile.setColumnFilterable(true);
    colFile.setPreferredWidth(150);
    colFile.setColumnName("filenameDOC14");
    colFile.setColumnSortable(true);

    grid.setAutoLoadData(false);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC14");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);
    buttonsPanel.add(labelDocumentType,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(insertButton1,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(reloadButton1,    new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(deleteButton1,    new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,    new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,    new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(comboBoxControl1,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    split.add(hierarTreePanel, JSplitPane.LEFT);
    split.add(subsplit, JSplitPane.RIGHT);

    subsplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
    subsplit.setDividerLocation(150);
    subsplit.add(filterPanel, JSplitPane.TOP);
    subsplit.add(grid, JSplitPane.BOTTOM);

    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colCod, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colFile, null);
    split.setDividerLocation(210);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }
  public HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }
  public DocFilterPanel getFilterPanel() {
    return filterPanel;
  }

}
