package org.jallinone.items.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.model.client.VOModel;
import java.awt.event.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.documents.java.GridDocumentVO;
import java.util.HashSet;
import org.jallinone.documents.java.DocumentTypeVO;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemAttachedDocVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used inside the item detail frame to attach documents.</p>
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
public class ItemAttachedDocsPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  LookupServerDataLocator docDataLocator = new LookupServerDataLocator();
  ServerGridDataLocator docsGridDataLocator = new ServerGridDataLocator();


  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  ComboColumn colDocType = new ComboColumn();


  LookupController docController = new LookupController();
  SaveButton saveButton5 = new SaveButton();
  InsertButton insertButton5 = new InsertButton();
  DeleteButton deleteButton5 = new DeleteButton();
  ReloadButton reloadButton5 = new ReloadButton();
  NavigatorBar navigatorBar5 = new NavigatorBar();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel docsButtonsPanel1 = new JPanel();
  GridControl docsGrid = new GridControl();
  CodLookupColumn colDocProg = new CodLookupColumn();
  TextColumn collDocName = new TextColumn();

  private DetailItemVO itemVO = null;

  JPanel docPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();


  public ItemAttachedDocsPanel() {
    try {
      jbInit();

      docsGrid.setController(new ItemAttachedDocsController(docsGrid,this));
      docsGrid.setGridDataLocator(docsGridDataLocator);
      docsGridDataLocator.setServerMethodName("loadItemAttachedDocs");


      // documents lookup...
      docDataLocator.setGridMethodName("loadDocuments");
      docDataLocator.setValidationMethodName("");
      colDocProg.setLookupController(docController);
//      colDocProg.setControllerMethodName("getDocumentsList");

      docController.setCodeSelectionWindow(docController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      docDataLocator.setTreeDataLocator(treeLevelDataLocator);
      docDataLocator.setNodeNameAttribute("descriptionSYS10");


      docController.setLookupDataLocator(docDataLocator);
      docController.setFrameTitle("documents");
      docController.setLookupValueObjectClassName("org.jallinone.documents.java.GridDocumentVO");
      docController.addLookup2ParentLink("descriptionDOC14", "descriptionDOC14");
      docController.setAllColumnVisible(false);
      docController.setPreferredWidthColumn("descriptionDOC14",400);
      docController.setVisibleColumn("descriptionDOC14", true);
      docController.setFramePreferedSize(new Dimension(750,500));
      docController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridDocumentVO docVO = (GridDocumentVO)docController.getLookupVO();
          ItemAttachedDocVO vo = (ItemAttachedDocVO)parentVO;
          vo.setProgressiveDoc14ITM05(docVO.getProgressiveDOC14());
          vo.setProgressiveHie01ITM05(docVO.getProgressiveHie01DOC17());
          vo.setProgressiveHie02HIE01(docVO.getProgressiveHie02HIE01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          ItemAttachedDocVO vo = (ItemAttachedDocVO)parentVO;
          docDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
          docDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
        }

        public void forceValidate() {}

      });


      // set document types in doc.type column...
      Response res = ClientUtils.getData("loadDocumentTypes",new GridParams());
      Domain d = new Domain("DOC_TYPES_DOC16");
      if (!res.isError()) {
        DocumentTypeVO vo = null;
        java.util.List list = ((VOListResponse)res).getRows();
        for(int i=0;i<list.size();i++) {
          vo = (DocumentTypeVO)list.get(i);
          d.addDomainPair(vo.getProgressiveHie02DOC16(),vo.getDescriptionSYS10());
        }
      }
      colDocType.setDomain(d);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    docPanel.setLayout(gridBagLayout3);
    docsGrid.setMaxNumberOfRowsOnInsert(50);


    this.setLayout(borderLayout1);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    docsButtonsPanel1.setLayout(flowLayout5);
    docsGrid.setAutoLoadData(false);
    docsGrid.setDeleteButton(deleteButton5);
    //docsGrid.setFunctionId("ITM05");
    docsGrid.setInsertButton(insertButton5);
    docsGrid.setNavBar(navigatorBar5);
    docsGrid.setReloadButton(reloadButton5);
    docsGrid.setSaveButton(saveButton5);
    docsGrid.setValueObjectClassName("org.jallinone.items.java.ItemAttachedDocVO");
    docsGrid.setVisibleStatusPanel(false);
    colDocProg.setAllowOnlyNumbers(true);
    colDocProg.setColumnName("progressiveDoc14ITM05");
    colDocProg.setHideCodeBox(true);
    colDocProg.setEditableOnInsert(true);
    colDocProg.setMinWidth(0);
    colDocProg.setPreferredWidth(40);
    collDocName.setColumnName("descriptionDOC14");
    collDocName.setPreferredWidth(400);
    colDocType.setColumnFilterable(true);
    colDocType.setEditableOnInsert(true);
    colDocType.setPreferredWidth(200);
    colDocType.setPreferredWidth(200);
    colDocType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocType.setColumnName("progressiveHie02HIE01");
    colDocType.setColumnRequired(false);
    colDocType.setHeaderColumnName("docType");
    docPanel.add(docsButtonsPanel1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    docsButtonsPanel1.add(insertButton5, null);
    docsButtonsPanel1.add(saveButton5, null);
    docsButtonsPanel1.add(reloadButton5, null);
    docsButtonsPanel1.add(deleteButton5, null);
    docsButtonsPanel1.add(navigatorBar5, null);
    docPanel.add(docsGrid,      new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(docPanel, BorderLayout.CENTER);

    docsGrid.getColumnContainer().add(colDocType, null);
    docsGrid.getColumnContainer().add(colDocProg, null);
    docsGrid.getColumnContainer().add(collDocName, null);
  }


  public DetailItemVO getItemVO() {
    return itemVO;
  }
  public void setItemVO(DetailItemVO itemVO) {
    this.itemVO = itemVO;
  }
  public GridControl getDocsGrid() {
    return docsGrid;
  }


  public void setButtonsEnabled(boolean enabled) {
    insertButton5.setEnabled(enabled);
    reloadButton5.setEnabled(enabled);
    deleteButton5.setEnabled(enabled);
  }


}
