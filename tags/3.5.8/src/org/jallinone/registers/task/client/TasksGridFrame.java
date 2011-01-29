package org.jallinone.registers.task.client;

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
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.registers.task.java.TaskVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the tasks grid frame.</p>
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
public class TasksGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  TextColumn colTask = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  CheckBoxColumn colFiniteCap = new CheckBoxColumn();

  CodLookupColumn colActCode = new CodLookupColumn();
  TextColumn colActDescr = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  LookupController actController = new LookupController();
  LookupServerDataLocator actDataLocator = new LookupServerDataLocator();
  ComboColumn colCompany = new ComboColumn();


  public TasksGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadTasks");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));


      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(192),grid);

      // fill in the company combo...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("REG07");
      Domain domain = new Domain("DOMAIN_REG07");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "REG07",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      colCompany.setDomain(domain);
//      colCompany.addItemListener(new ItemListener() {
//        public void itemStateChanged(ItemEvent e) {
//          if (e.getStateChange()==e.SELECTED) {
//            DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
//            vo.setCompanyCodeSys01SCH03((String)controlCompaniesCombo.getValue());
//          }
//        }
//      });

      // sale activity lookup...
      actDataLocator.setGridMethodName("loadSaleActivities");
      actDataLocator.setValidationMethodName("validateSaleActivityCode");
      colActCode.setLookupController(actController);
      colActCode.setControllerMethodName("getSalesActivitiesList");
      actController.setLookupDataLocator(actDataLocator);
      actController.setFrameTitle("sale activities");
      actController.setLookupValueObjectClassName("org.jallinone.sales.activities.java.SaleActivityVO");
      actController.addLookup2ParentLink("activityCodeSAL09", "activityCodeSal09REG07");
      actController.addLookup2ParentLink("descriptionSYS10", "activityDescriptionREG07");
      actController.setAllColumnVisible(false);
      actController.setVisibleColumn("activityCodeSAL09", true);
      actController.setVisibleColumn("descriptionSYS10", true);
      actController.setPreferredWidthColumn("descriptionSYS10", 200);
      actController.setFramePreferedSize(new Dimension(340,400));
      actController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          TaskVO vo = (TaskVO)parentVO;
          actDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG07());
          actDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG07());
        }

        public void forceValidate() {}

      });


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("tasks"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG07");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colTask.setMaxCharacters(20);
    colTask.setTrimText(true);
    colTask.setUpperCase(true);
    colTask.setColumnFilterable(true);
    colTask.setColumnName("taskCodeREG07");
    colTask.setColumnSortable(true);
    colTask.setEditableOnInsert(true);
    colTask.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colTask.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("taskDescription");
    colDescr.setPreferredWidth(240);

    colActCode.setColumnDuplicable(true);
    colActCode.setColumnFilterable(true);
    colActCode.setColumnRequired(false);
    colActCode.setColumnName("activityCodeSal09REG07");
    colActCode.setColumnSortable(true);
    colActCode.setEditableOnEdit(true);
    colActCode.setEditableOnInsert(true);
    colActCode.setMaxCharacters(20);

    colActDescr.setColumnDuplicable(true);
    colActDescr.setColumnFilterable(true);
    colActDescr.setColumnRequired(false);
    colActDescr.setColumnName("activityDescriptionREG07");
    colActDescr.setColumnSortable(true);
    colActDescr.setEditableOnEdit(false);
    colActDescr.setEditableOnInsert(false);
    colActDescr.setPreferredWidth(200);

    colCompany.setColumnDuplicable(true);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01REG07");
    colCompany.setColumnSortable(true);
    colCompany.setEditableOnEdit(false);
    colCompany.setEditableOnInsert(true);
    colCompany.setColumnRequired(true);

    colFiniteCap.setColumnFilterable(true);
    colFiniteCap.setColumnName("finiteCapacityREG07");
    colFiniteCap.setColumnRequired(false);
    colFiniteCap.setColumnSortable(true);
    colFiniteCap.setEditableOnEdit(true);
    colFiniteCap.setEditableOnInsert(true);
    colFiniteCap.setPreferredWidth(60);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colTask, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colActCode, null);
    grid.getColumnContainer().add(colActDescr, null);
    grid.getColumnContainer().add(colFiniteCap, null);
  }
  public GridControl getGrid() {
    return grid;
  }

}
