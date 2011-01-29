package org.jallinone.documents.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import java.awt.event.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.documents.java.*;
import javax.swing.border.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Document detail frame.</p>
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
public class DocumentFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  JTabbedPane tab = new JTabbedPane();
  JTabbedPane subtab = new JTabbedPane();
  FlowLayout flowLayout1 = new FlowLayout();
  Form formPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  InsertButton insertButton1 = new InsertButton();
  CopyButton copyButton1 = new CopyButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();



  private ServerGridDataLocator linksGridDataLocator = new ServerGridDataLocator();
  private ServerGridDataLocator versGridDataLocator = new ServerGridDataLocator();




  SaveButton saveButton2 = new SaveButton();
  DeleteButton deleteButton2 = new DeleteButton();
  JPanel linksButtonsPanel = new JPanel();
  JPanel linksPanel = new JPanel();
  ReloadButton reloadButton2 = new ReloadButton();
  FlowLayout flowLayout2 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel versionsPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel buttons2Panel = new JPanel();
  GridControl vgrid = new GridControl();
  FlowLayout flowLayout3 = new FlowLayout();
  DeleteButton deleteButton3 = new DeleteButton();
  GridControl linksgrid = new GridControl();
  JPanel propsPanel = new JPanel();
  LabelControl labelDoc = new LabelControl();
  JButton uploadButton = new JButton();
  TextControl controlDocSize = new TextControl();
  TextColumn colLevelDescr = new TextColumn();
  ComboColumn colDocType = new ComboColumn();
  CodLookupColumn colLevelCode = new CodLookupColumn();

  LookupController levelController = new LookupController();
  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  IntegerColumn colVersion = new IntegerColumn();
  TextColumn colUsername = new TextColumn();
  DateTimeColumn colDate = new DateTimeColumn();
  TextControl controlFilename = new TextControl();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel propsButtonsPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  EditButton editButton3 = new EditButton();
  SaveButton saveButton3 = new SaveButton();
  ReloadButton reloadButton3 = new ReloadButton();
  DocPropertiesPanel propPanel = new DocPropertiesPanel();


  public DocumentFrame(DocumentController controller) {
    try {

      jbInit();
      setSize(750,450);
      setMinimumSize(new Dimension(750,450));

      colLevelCode.setLookupController(levelController);
      levelController.setLookupDataLocator(levelDataLocator);
      levelController.setCodeSelectionWindow(levelController.TREE_FRAME);
      levelController.setFrameTitle("hierarchy");
      levelController.setAllowTreeLeafSelectionOnly(true);
      levelController.getLookupDataLocator().setNodeNameAttribute("descriptionSYS10");
      levelController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      levelController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC17");
      levelController.addLookup2ParentLink("descriptionSYS10", "levelDescriptionSYS10");
      levelDataLocator.setTreeDataLocator(treeLevelDataLocator);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");


      init();

      formPanel.setFormController(controller);

      CustomizedControls customizedControls = new CustomizedControls(tab,formPanel,ApplicationConsts.ID_DOCUMENT);


      linksgrid.setController(new DocumentLinksController(this));
      linksgrid.setGridDataLocator(linksGridDataLocator);
      linksGridDataLocator.setServerMethodName("loadDocumentLinks");

      vgrid.setController(new DocumentVersionsController(this));
      vgrid.setGridDataLocator(versGridDataLocator);
      versGridDataLocator.setServerMethodName("loadDocumentVersions");



    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public final void loadDataCompleted(boolean error,DocumentPK pk) {
    DetailDocumentVO vo = (DetailDocumentVO)formPanel.getVOModel().getValueObject();

    getControlDocSize().setText("");

    linksgrid.getOtherGridParams().put(
      ApplicationConsts.DOCUMENT_PK,
      pk
    );
    linksgrid.reloadData();

    vgrid.getOtherGridParams().put(
      ApplicationConsts.DOCUMENT_PK,
      pk
    );
    vgrid.reloadData();

    getPropPanel().reloadData(this);

    setButtonsEnabled(true);

  }


  /**
   * Retrieve document types and fill in the document types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadDocumentTypes",new GridParams());
    Domain d = new Domain("DOC_TYPE_DOC16");
    if (!res.isError()) {
      DocumentTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (DocumentTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02DOC16(),vo.getDescriptionSYS10());
      }
    }
    colDocType.setDomain(d);
    colDocType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          DocumentLinkVO vo = (DocumentLinkVO)linksgrid.getVOListTableModel().getObjectForRow(linksgrid.getSelectedRow());
          vo.setProgressiveHie01DOC17(null);
          vo.setLevelDescriptionSYS10("");
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHIE02());
        }
      }
    });
  }


  private void jbInit() throws Exception {
    linksgrid.setMaxNumberOfRowsOnInsert(50);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("document detail"));
    formPanel.setVOClassName("org.jallinone.documents.java.DetailDocumentVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    formPanel.setLayout(gridBagLayout1);
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    formPanel.setInsertButton(insertButton1);
    formPanel.setCopyButton(copyButton1);
    formPanel.setEditButton(editButton1);
    formPanel.setReloadButton(reloadButton1);
    formPanel.setDeleteButton(deleteButton1);
    formPanel.setSaveButton(saveButton1);
    formPanel.setFunctionId("DOC14");
    labelDescr.setText("descriptionDOC14");
    controlDescr.setAttributeName("descriptionDOC14");
    controlDescr.setCanCopy(true);
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    saveButton2.setText("saveButton2");
    deleteButton2.setText("deleteButton2");
    linksButtonsPanel.setLayout(flowLayout2);
    linksPanel.setLayout(borderLayout1);
    reloadButton2.setText("reloadButton2");
    flowLayout2.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    linksPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("document links"));
    versionsPanel.setLayout(borderLayout2);
    vgrid.setAutoLoadData(false);
    vgrid.setDeleteButton(deleteButton3);
    vgrid.setFunctionId("DOC14");
    vgrid.setValueObjectClassName("org.jallinone.documents.java.DocumentVersionVO");
    buttons2Panel.setDebugGraphicsOptions(0);
    buttons2Panel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    deleteButton3.setText("deleteButton3");
    linksgrid.setAutoLoadData(false);
    linksgrid.setDeleteButton(deleteButton2);
    linksgrid.setFunctionId("DOC14");
    linksgrid.setInsertButton(insertButton2);
    linksgrid.setReloadButton(reloadButton2);
    linksgrid.setSaveButton(saveButton2);
    linksgrid.setValueObjectClassName("org.jallinone.documents.java.DocumentLinkVO");
    labelDoc.setText("uploaded document");
    uploadButton.setMaximumSize(new Dimension(22, 20));
    uploadButton.setMinimumSize(new Dimension(22, 20));
    uploadButton.setPreferredSize(new Dimension(22, 20));
    uploadButton.setText("...");
    uploadButton.addActionListener(new DocumentFrame_uploadButton_actionAdapter(this));
    controlDocSize.setEnabled(false);
    controlDocSize.setEnabledOnInsert(false);
    controlDocSize.setEnabledOnEdit(false);
    colLevelDescr.setColumnName("levelDescriptionSYS10");
    colLevelDescr.setPreferredWidth(250);
    colDocType.setDomainId("DOC_TYPE_DOC16");
    colDocType.setColumnFilterable(true);
    colDocType.setColumnName("progressiveHIE02");
    colDocType.setColumnSortable(true);
    colDocType.setEditableOnInsert(true);
    colDocType.setHeaderColumnName("documentType");
    colDocType.setPreferredWidth(250);
    colDocType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocType.setSortingOrder(1);
    colLevelCode.setColumnFilterable(false);
    colLevelCode.setColumnName("progressiveHie01DOC17");
    colLevelCode.setEditableOnInsert(true);
    colLevelCode.setMaxWidth(40);
    colLevelCode.setMinWidth(40);
    colLevelCode.setPreferredWidth(40);
    colLevelCode.setAllowOnlyNumbers(true);
    colLevelCode.setHideCodeBox(true);
    colUsername.setColumnName("createUsernameDOC15");
    colUsername.setPreferredWidth(120);
    colDate.setPreferredWidth(180);
    colDate.setColumnName("createDateDOC15");
    controlFilename.setEnabledOnInsert(false);
    controlFilename.setEnabledOnEdit(false);
    controlFilename.setAttributeName("filenameDOC14");
    vgrid.setToolTipText("double click to open the document");
    propsPanel.setLayout(borderLayout3);
    propsButtonsPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    editButton3.setText("editButton2");
    saveButton3.setText("saveButton3");
    reloadButton3.setText("reloadButton3");
    propPanel.setEditButton(editButton3);
    propPanel.setReloadButton(reloadButton3);
    propPanel.setSaveButton(saveButton3);
    propPanel.setVOClassName("org.jallinone.documents.java.DocPropertyVO");
    colVersion.setSortingOrder(1);
    colVersion.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    this.getContentPane().add(tab, BorderLayout.CENTER);
    tab.add(formPanel,   "detailPanel");
    formPanel.add(labelDescr,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlDescr,        new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(subtab,        new GridBagConstraints(0, 3, 4, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    linksPanel.add(linksButtonsPanel, BorderLayout.NORTH);
    linksButtonsPanel.add(insertButton2, null);
    linksButtonsPanel.add(saveButton2, null);
    linksButtonsPanel.add(reloadButton2, null);
    linksButtonsPanel.add(deleteButton2, null);
    subtab.add(versionsPanel,  "versionsPanel");
    subtab.add(linksPanel,  "linksPanel");
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    versionsPanel.add(buttons2Panel, BorderLayout.NORTH);
    versionsPanel.add(vgrid,  BorderLayout.CENTER);
    tab.add(propsPanel,  "propsPanel");
    propsPanel.add(propsButtonsPanel, BorderLayout.NORTH);
    buttons2Panel.add(deleteButton3, null);
    linksPanel.add(linksgrid, BorderLayout.CENTER);
    linksgrid.getColumnContainer().add(colDocType, null);
    linksgrid.getColumnContainer().add(colLevelCode, null);
    linksgrid.getColumnContainer().add(colLevelDescr, null);
    formPanel.add(labelDoc,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(uploadButton,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    formPanel.add(controlDocSize,       new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    formPanel.add(controlFilename,    new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    colVersion.setColumnName("versionDOC15");
    vgrid.getColumnContainer().add(colVersion, null);
    vgrid.getColumnContainer().add(colUsername, null);
    vgrid.getColumnContainer().add(colDate, null);
    propsButtonsPanel.add(editButton3, null);
    propsButtonsPanel.add(saveButton3, null);
    propsButtonsPanel.add(reloadButton3, null);
    propsPanel.add(propPanel, BorderLayout.CENTER);


    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("document detail"));
    tab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("document properties"));

    subtab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("document versions"));
    subtab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("document links"));
  }


  public Form getFormPanel() {
    return formPanel;
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton2.setEnabled(enabled);
    editButton3.setEnabled(enabled);
    if (!enabled) {
      deleteButton2.setEnabled(enabled);
      reloadButton2.setEnabled(enabled);
      saveButton2.setEnabled(enabled);

      reloadButton3.setEnabled(enabled);
      saveButton3.setEnabled(enabled);
    }

    deleteButton3.setEnabled(enabled);
  }

  void uploadButton_actionPerformed(ActionEvent e) {
    byte[] bytes = loadFile();
    if (bytes!=null) {
      DetailDocumentVO vo = (DetailDocumentVO)formPanel.getVOModel().getValueObject();
      vo.setDocument(bytes);
      controlDocSize.setText((bytes.length/1024+(bytes.length/1024==0?1:0))+" Kbytes");
    }

  }


  public byte[] loadFile() {
    JFileChooser f = new JFileChooser();
    f.setFileFilter(new FileFilter() {

      /**
       * Whether the given file is accepted by this filter.
       */
      public boolean accept(File f) {
        return true;
      }

      /**
       * The description of this filter. For example: "JPG and GIF Images"
       * @see FileView#getName
       */
      public String getDescription() {
        return "*.* files";
      }

    });
    int res = f.showOpenDialog(ClientUtils.getParentFrame(this));
    byte[] bytes = null;
    if (res==f.APPROVE_OPTION) {
      try {
        bytes = new byte[ (int) f.getSelectedFile().length()];
        FileInputStream in = new FileInputStream(f.getSelectedFile());
        in.read(bytes);
        in.close();
        controlFilename.setValue(f.getSelectedFile().getName());
      }
      catch (Throwable ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ex.getMessage(),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.WARNING_MESSAGE
        );
      }
    }
    return bytes;
  }


  public GridControl getLinksgrid() {
    return linksgrid;
  }
  public ServerGridDataLocator getLinksGridDataLocator() {
    return linksGridDataLocator;
  }
  public GridControl getVgrid() {
    return vgrid;
  }
  public ServerGridDataLocator getVersGridDataLocator() {
    return versGridDataLocator;
  }
  public TextControl getControlDocSize() {
    return controlDocSize;
  }
  public DocPropertiesPanel getPropPanel() {
    return propPanel;
  }


}

class DocumentFrame_uploadButton_actionAdapter implements java.awt.event.ActionListener {
  DocumentFrame adaptee;

  DocumentFrame_uploadButton_actionAdapter(DocumentFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.uploadButton_actionPerformed(e);
  }
}
