package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.client.ClientUtils;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import java.util.HashMap;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.Response;
import java.util.Calendar;
import java.util.Date;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import java.io.File;
import java.util.Hashtable;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid Frame used to list all reports defined inside the application,
 * including custom functions too.</p>
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
public class ReportsFrame extends InternalFrame {

  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  JPanel mainPanel = new JPanel();
  GridControl grid = new GridControl();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel toolbarPanel = new JPanel();
  EditButton editButton1 = new EditButton();
  FlowLayout flowLayout1 = new FlowLayout();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  JPanel gridPanel = new JPanel();
  JPanel uploadPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JTabbedPane tabbed = new JTabbedPane();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colFunctionDescr = new TextColumn();
  CodLookupColumn colReportFileName = new CodLookupColumn();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelFilesToUpload = new LabelControl();
  JScrollPane filesScrollPane = new JScrollPane();

  DefaultListModel model = new DefaultListModel();
  JList filesList = new JList(model);
  JButton uploadButton = new JButton();
  JButton clearButton = new JButton();

  /** collection of pairs: file name + byte[] */
  private Hashtable bytes = new Hashtable();
  JButton selButton = new JButton();

  LookupController fileController = new LookupController();
  LookupServerDataLocator fileDataLocator = new LookupServerDataLocator();
  CheckBoxColumn colCustReport = new CheckBoxColumn();


  public ReportsFrame(ReportsController gridController) {
    try {
      jbInit();

      grid.setController(gridController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadCustomizedReports");

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SYS15");
      Domain domain = new Domain("DOMAIN_SYS15");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "SYS15",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED) {
            grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
            grid.reloadData();
          }
        }
      });


//      controlCompaniesCombo.getComboBox().setSelectedIndex(0);

      // file name lookup...
      fileDataLocator.setGridMethodName("loadReportFileNames");
      fileDataLocator.setValidationMethodName("");

      colReportFileName.setLookupController(fileController);
      fileController.setLookupDataLocator(fileDataLocator);
      fileController.setFrameTitle("files");
      fileController.setLookupValueObjectClassName("org.jallinone.system.customizations.java.ReportFileNameVO");
      fileController.addLookup2ParentLink("reportFileName","reportNameSYS15");
      fileController.setAllColumnVisible(false);
      fileController.setVisibleColumn("reportFileName", true);
      fileController.setFramePreferedSize(new Dimension(280,500));
      fileController.setPreferredWidthColumn("reportFileName",260);


      setSize(630,420);
      setMinimumSize(new Dimension(630,420));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    gridPanel.setLayout(borderLayout2);
    uploadPanel.setLayout(gridBagLayout2);
    colFunctionDescr.setColumnFilterable(true);
    colFunctionDescr.setColumnName("descriptionSYS10");
    colFunctionDescr.setColumnSortable(true);
    colFunctionDescr.setHeaderColumnName("functionDescription");
    colFunctionDescr.setPreferredWidth(310);
    colFunctionDescr.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colReportFileName.setColumnName("reportNameSYS15");
    colReportFileName.setEditableOnEdit(true);
    colReportFileName.setPreferredWidth(190);
    colReportFileName.setEnableCodBox(false);
    labelFilesToUpload.setText("files to upload");
    uploadButton.setToolTipText("");
    uploadButton.setText(ClientSettings.getInstance().getResources().getResource("upload files"));
    uploadButton.addActionListener(new ReportsFrame_uploadButton_actionAdapter(this));
    clearButton.setText(ClientSettings.getInstance().getResources().getResource("clear files"));
    clearButton.addActionListener(new ReportsFrame_clearButton_actionAdapter(this));
    selButton.setText(ClientSettings.getInstance().getResources().getResource("select files"));
    selButton.addActionListener(new ReportsFrame_selButton_actionAdapter(this));
    colCustReport.setColumnName("customFunction");
    colCustReport.setPreferredWidth(90);
    tabbed.add(gridPanel, "gridPanel");
    tabbed.add(uploadPanel, "uploadPanel");
    uploadPanel.add(labelFilesToUpload,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    uploadPanel.add(filesScrollPane,      new GridBagConstraints(1, 0, 2, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    tabbed.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("reports list"));
    tabbed.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("upload files"));

    controlCompaniesCombo.setAttributeName("companyCodeSys01SYS15");
    grid.setValueObjectClassName("org.jallinone.system.customizations.java.ReportVO");


    this.setTitle(ClientSettings.getInstance().getResources().getResource("customize reports"));
    topPanel.setLayout(gridBagLayout1);
    labelCompanyCode.setText("companyCodeSys01SYS15");
    mainPanel.setLayout(borderLayout1);
    grid.setAutoLoadData(false);
    grid.setEditButton(editButton1);
    grid.setFunctionId("SYS15");
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    toolbarPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    gridPanel.add(topPanel,BorderLayout.NORTH);
    gridPanel.add(mainPanel,BorderLayout.CENTER);


    this.getContentPane().add(tabbed, BorderLayout.CENTER);
    topPanel.add(labelCompanyCode,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    topPanel.add(controlCompaniesCombo,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colFunctionDescr, null);
    grid.getColumnContainer().add(colReportFileName, null);
    grid.getColumnContainer().add(colCustReport, null);
    topPanel.add(toolbarPanel,    new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    toolbarPanel.add(editButton1, null);
    toolbarPanel.add(saveButton1, null);
    toolbarPanel.add(reloadButton1, null);
    toolbarPanel.add(navigatorBar1, null);
    filesScrollPane.getViewport().add(filesList, null);
    uploadPanel.add(uploadButton,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    uploadPanel.add(clearButton,       new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    uploadPanel.add(selButton,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

  }


  public ComboBoxControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }


  void uploadButton_actionPerformed(ActionEvent e) {
    if (bytes.size()==0) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("you must select at least one file to upload"),
          ClientSettings.getInstance().getResources().getResource("files to upload"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    Response res = ClientUtils.getData("checkReportFiles",bytes);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("files to upload"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      Boolean allFilesAreNew = (Boolean)((VOResponse)res).getVo();
      if (!allFilesAreNew.booleanValue()) {
        if (JOptionPane.showConfirmDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("some uploaded files already exists. overwrite them?"),
            ClientSettings.getInstance().getResources().getResource("files to upload"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        )!=JOptionPane.YES_OPTION)
          return;
      }
      res = ClientUtils.getData("uploadReportFiles",bytes);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            res.getErrorMessage(),
            ClientSettings.getInstance().getResources().getResource("files to upload"),
            JOptionPane.ERROR_MESSAGE
        );
      }
      else {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("upload completed."),
            ClientSettings.getInstance().getResources().getResource("files to upload"),
            JOptionPane.INFORMATION_MESSAGE
        );
        clearButton_actionPerformed(null);
      }
    }

  }


  void clearButton_actionPerformed(ActionEvent e) {
    DefaultListModel model = new DefaultListModel();
    filesList.setModel(model);
    filesList.revalidate();
    filesList.repaint();
    bytes.clear();
  }


  void selButton_actionPerformed(ActionEvent e) {
    try {
      JFileChooser fc = new JFileChooser(".");
      fc.setDialogTitle(ClientSettings.getInstance().getResources().getResource(
          "files to upload"));
      fc.setMultiSelectionEnabled(true);
      int ret = fc.showDialog(MDIFrame.getInstance(),
                              ClientSettings.getInstance().getResources().
                              getResource("import files"));
      if (ret == fc.APPROVE_OPTION) {
        File[] files = fc.getSelectedFiles();
        byte[] b = null;
        FileInputStream in = null;
        for (int i = 0; i < files.length; i++) {
          if (files[i].isDirectory()) {
            continue;
          }
          if (bytes.containsKey(files[i].getName())) {
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                ClientSettings.getInstance().getResources().getResource(
                "there already exists a file with the same name in the list"),
                ClientSettings.getInstance().getResources().getResource(
                "files to upload"),
                JOptionPane.WARNING_MESSAGE
            );
            continue;
          }
          model.addElement(files[i].getName());
          b = new byte[ (int) files[i].length()];
          in = new FileInputStream(files[i]);
          in.read(b);
          in.close();
          bytes.put(files[i].getName(), b);
        }
        filesList.setModel(model);
        filesList.revalidate();
        filesList.repaint();
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ex.getMessage(),
          ClientSettings.getInstance().getResources().getResource(
          "files to upload"),
          JOptionPane.ERROR_MESSAGE
      );
    }

  }



}

class ReportsFrame_uploadButton_actionAdapter implements java.awt.event.ActionListener {
  ReportsFrame adaptee;

  ReportsFrame_uploadButton_actionAdapter(ReportsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.uploadButton_actionPerformed(e);
  }
}

class ReportsFrame_clearButton_actionAdapter implements java.awt.event.ActionListener {
  ReportsFrame adaptee;

  ReportsFrame_clearButton_actionAdapter(ReportsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.clearButton_actionPerformed(e);
  }
}

class ReportsFrame_selButton_actionAdapter implements java.awt.event.ActionListener {
  ReportsFrame adaptee;

  ReportsFrame_selButton_actionAdapter(ReportsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.selButton_actionPerformed(e);
  }
}

