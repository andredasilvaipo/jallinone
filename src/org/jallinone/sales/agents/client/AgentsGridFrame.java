package org.jallinone.sales.agents.client;

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
import org.jallinone.commons.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.agents.java.AgentTypeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the agents grid frame.</p>
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
public class AgentsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  ExportButton exportButton = new ExportButton();
  CompaniesComboColumn colCompany = new CompaniesComboColumn();
  TextColumn colAgentCode = new TextColumn();
  PercentageColumn colPerc = new PercentageColumn();
  ComboColumn colAgentType = new ComboColumn();
  CodLookupColumn colSubject = new CodLookupColumn();
  TextColumn colFirstName = new TextColumn();
  TextColumn colLastName = new TextColumn();

  LookupController subjectController = new LookupController();
  LookupServerDataLocator subjectDataLocator = new LookupServerDataLocator();



  public AgentsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadAgents");
    try {
      jbInit();
      init();

      setSize(750,500);
      setMinimumSize(new Dimension(750,500));

      // employee lookup...
      subjectDataLocator.setGridMethodName("loadEmployees");
      subjectDataLocator.setValidationMethodName("");
      colSubject.setLookupController(subjectController);
      colSubject.setHideCodeBox(true);
      subjectController.setLookupDataLocator(subjectDataLocator);
      subjectController.setFrameTitle("employees");
      subjectController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      subjectController.addLookup2ParentLink("progressiveReg04SCH01", "progressiveReg04SAL10");
      subjectController.addLookup2ParentLink("name_1REG04","name_1REG04");
      subjectController.addLookup2ParentLink("name_2REG04","name_2REG04");
      subjectController.setAllColumnVisible(false);
      subjectController.setVisibleColumn("companyCodeSys01SCH01", true);
      subjectController.setVisibleColumn("name_1REG04", true);
      subjectController.setVisibleColumn("name_2REG04", true);
      subjectController.setHeaderColumnName("name_1REG04","firstname");
      subjectController.setHeaderColumnName("name_2REG04","lastname");
      subjectController.setVisibleColumn("descriptionSYS10", true);
      subjectController.setVisibleColumn("officeSCH01", true);
      subjectController.setVisibleColumn("phoneNumberSCH01", true);
      subjectController.setHeaderColumnName("name_1REG04","firstname");
      subjectController.setHeaderColumnName("name_2REG04","lastname");
      subjectController.setHeaderColumnName("companyCodeSys01SCH01","companyCodeSYS01");
      subjectController.setHeaderColumnName("descriptionSYS10","taskDescription");
      subjectController.setFramePreferedSize(new Dimension(650,400));


      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(342),grid);

      colCompany.setFunctionCode("SAL10");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadAgentTypes",new GridParams());
    Domain d = new Domain("AGENT_TYPES");
    if (!res.isError()) {
      AgentTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (AgentTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveSys10REG19(),vo.getDescriptionSYS10());
      }
    }
    colAgentType.setDomain(d);
  }



  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.sales.agents.java.AgentVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("agents"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SAL10");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colCompany.setColumnDuplicable(true);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01SAL10");
    colCompany.setColumnSortable(true);
    colCompany.setEditableOnInsert(true);
    colCompany.setHeaderColumnName("companyCodeSYS01");
    colCompany.setPreferredWidth(100);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colAgentCode.setMaxCharacters(20);
    colAgentCode.setTrimText(true);
    colAgentCode.setUpperCase(true);
    colAgentCode.setColumnFilterable(true);
    colAgentCode.setColumnName("agentCodeSAL10");
    colAgentCode.setColumnSortable(true);
    colAgentCode.setEditableOnInsert(true);
    colAgentCode.setPreferredWidth(90);
    colAgentCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colAgentCode.setSortingOrder(1);
    colPerc.setColumnDuplicable(true);
    colPerc.setColumnFilterable(true);
    colPerc.setColumnName("percentageSAL10");
    colPerc.setColumnSortable(true);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setPreferredWidth(90);
    colAgentType.setColumnDuplicable(true);
    colAgentType.setColumnFilterable(true);
    colAgentType.setColumnName("progressiveSys10SAL10");
    colAgentType.setColumnSortable(true);
    colAgentType.setEditableOnEdit(true);
    colAgentType.setEditableOnInsert(true);
    colAgentType.setMaxWidth(500);
    colAgentType.setPreferredWidth(170);
    colSubject.setColumnDuplicable(true);
    colSubject.setColumnFilterable(true);
    colSubject.setColumnName("progressiveReg04SAL10");
    colSubject.setColumnSortable(true);
    colSubject.setEditableOnInsert(true);
    colSubject.setPreferredWidth(40);
    colFirstName.setColumnDuplicable(true);
    colFirstName.setColumnFilterable(true);
    colFirstName.setColumnName("name_1REG04");
    colFirstName.setColumnSortable(true);
    colFirstName.setHeaderColumnName("firstname");
    colFirstName.setPreferredWidth(110);
    colLastName.setColumnDuplicable(true);
    colLastName.setColumnFilterable(true);
    colLastName.setColumnName("name_2REG04");
    colLastName.setColumnSortable(true);
    colLastName.setHeaderColumnName("lastname");
    colLastName.setPreferredWidth(120);
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
    grid.getColumnContainer().add(colAgentCode, null);
    grid.getColumnContainer().add(colPerc, null);
    grid.getColumnContainer().add(colAgentType, null);
    grid.getColumnContainer().add(colSubject, null);
    grid.getColumnContainer().add(colFirstName, null);
    grid.getColumnContainer().add(colLastName, null);
  }
  public LookupServerDataLocator getSubjectDataLocator() {
    return subjectDataLocator;
  }

}
