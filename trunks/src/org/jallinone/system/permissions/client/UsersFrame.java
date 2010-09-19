package org.jallinone.system.permissions.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.system.companies.java.CompanyVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame that shows/edit users and roles associated to a specified user.</p>
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
public class UsersFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  JPanel rolesPanel = new JPanel();
  JPanel usersPanel = new JPanel();
  GridControl usersGridControl = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  CopyButton copyButton = new CopyButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();
  ReloadButton reloadButton = new ReloadButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel rolesButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  GridControl userRolesGridControl = new GridControl();
  TextColumn colUsername = new TextColumn();
  TextColumn colPaswd = new TextColumn();
  DateColumn colDateExp = new DateColumn();
  TextColumn colFirstName = new TextColumn();
  TextColumn colLastName = new TextColumn();
  CodLookupColumn colLangCode = new CodLookupColumn();
  TextColumn colRoleDescr = new TextColumn();
  CheckBoxColumn colSel = new CheckBoxColumn();

  /** users grid data locator */
  private ServerGridDataLocator usersGridDataLocator = new ServerGridDataLocator();

  /** user roles grid data locator */
  private ServerGridDataLocator userRolesGridDataLocator = new ServerGridDataLocator();

  /** languages lookup controller */
  private LookupController langLookupController = new LookupController();

  /** languages lookup data locator */
  private LookupServerDataLocator langDataLocator = new LookupServerDataLocator();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  CodLookupColumn codEmpCode = new CodLookupColumn();

  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();
  DecimalColumn colProgressiveREG04 = new DecimalColumn();
  TextColumn colCompanyCode = new TextColumn();
  ComboColumn colDefCompany = new ComboColumn();


  public UsersFrame(GridController usersController) {
    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750,500));

      // fill in companies combo...
      Domain domain = new Domain("COMPANIES");
      Response res = ClientUtils.getData("loadCompanies",null);
      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        CompanyVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (CompanyVO)rows.get(i);
          domain.addDomainPair(vo.getCompanyCodeSYS01(),vo.getName_1REG04());
        }
      }
      colDefCompany.setDomain(domain);


      usersGridControl.setController(usersController);
      usersGridControl.setGridDataLocator(usersGridDataLocator);
      usersGridDataLocator.setServerMethodName("loadUsers");

      userRolesGridControl.setController(new UserRolesController());
      userRolesGridControl.setGridDataLocator(userRolesGridDataLocator);
      userRolesGridDataLocator.setServerMethodName("loadUserRoles");

      colLangCode.setLookupController(langLookupController);
      colLangCode.setControllerMethodName("getLanguagesList");
      langLookupController.setLookupDataLocator(langDataLocator);
      langDataLocator.setGridMethodName("loadLanguages");
      langDataLocator.setValidationMethodName("validateLanguageCode");
      langLookupController.setAllColumnVisible(true);
      langLookupController.setFrameTitle("languages list");
      langLookupController.setLookupValueObjectClassName("org.jallinone.system.languages.java.LanguageVO");
      langLookupController.addLookup2ParentLink("languageCodeSYS09","languageCodeSys09SYS03");
      langLookupController.setAllColumnVisible(false);
      langLookupController.setVisibleColumn("languageCodeSYS09",true);
      langLookupController.setVisibleColumn("descriptionSYS10",true);

      // employees lookup...
      empDataLocator.setGridMethodName("loadEmployees");
      empDataLocator.setValidationMethodName("validateEmployeeCode");
      codEmpCode.setLookupController(empController);
      codEmpCode.setControllerMethodName("getEmployeesList");
      empController.setLookupDataLocator(empDataLocator);
      empController.setFrameTitle("employees");
      empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      empController.addLookup2ParentLink("employeeCodeSCH01", "employeeCodeSCH01");
      empController.addLookup2ParentLink("companyCodeSys01SCH01", "companyCodeSys01SYS03");
      empController.addLookup2ParentLink("name_1REG04", "firstNameSYS03");
      empController.addLookup2ParentLink("name_2REG04", "lastNameSYS03");
      empController.addLookup2ParentLink("progressiveReg04SCH01", "progressiveReg04SYS03");
      empController.setAllColumnVisible(false);
      empController.setVisibleColumn("employeeCodeSCH01", true);
      empController.setVisibleColumn("name_1REG04", true);
      empController.setVisibleColumn("name_2REG04", true);
      empController.setPreferredWidthColumn("name_1REG04",150);
      empController.setPreferredWidthColumn("name_2REG04",150);
      empController.setFramePreferedSize(new Dimension(430,400));


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    colDefCompany.setColumnName("defCompanyCodeSys01SYS03");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    usersGridControl.setValueObjectClassName("org.jallinone.system.permissions.java.UserVO");
    userRolesGridControl.setValueObjectClassName("org.jallinone.system.permissions.java.UserRoleVO");
    usersGridControl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    usersPanel.setLayout(borderLayout2);
    this.setTitle(ClientSettings.getInstance().getResources().getResource("users"));
    this.getContentPane().setLayout(gridBagLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    usersGridControl.setCopyButton(copyButton);
    usersGridControl.setDeleteButton(deleteButton);
    usersGridControl.setEditButton(editButton);
    usersGridControl.setExportButton(exportButton);
    usersGridControl.setFunctionId("SYS03");
    usersGridControl.setInsertButton(insertButton);
    usersGridControl.setNavBar(navigatorBar);
    usersGridControl.setReloadButton(reloadButton);
    usersGridControl.setResizingAllowed(true);
    usersGridControl.setSaveButton(saveButton);
    rolesPanel.setLayout(borderLayout1);
    rolesButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    userRolesGridControl.setAutoLoadData(false);
    userRolesGridControl.setEditButton(editButton1);
    userRolesGridControl.setReloadButton(reloadButton1);
    userRolesGridControl.setSaveButton(saveButton1);
    colUsername.setMaxCharacters(20);
    colUsername.setTrimText(true);
    colUsername.setUpperCase(true);
    colUsername.setColumnFilterable(true);
    colUsername.setColumnName("usernameSYS03");
    colUsername.setColumnSortable(true);
    colUsername.setEditableOnEdit(false);
    colUsername.setEditableOnInsert(true);
    colUsername.setPreferredWidth(90);
    colUsername.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colUsername.setSortingOrder(1);
    colPaswd.setMaxCharacters(20);
    colPaswd.setColumnName("passwdSYS03");
    colPaswd.setEditableOnEdit(true);
    colPaswd.setEncryptText(true);
    colPaswd.setEditableOnInsert(true);
    colPaswd.setPreferredWidth(90);
    colDateExp.setColumnDuplicable(true);
    colDateExp.setColumnFilterable(true);
    colDateExp.setColumnName("passwdExpirationSYS03");
    colDateExp.setColumnRequired(false);
    colDateExp.setColumnSortable(true);
    colDateExp.setEditableOnEdit(true);
    colDateExp.setEditableOnInsert(true);
    colLangCode.setColumnDuplicable(true);
    colLangCode.setColumnFilterable(true);
    colLangCode.setColumnName("languageCodeSys09SYS03");
    colLangCode.setColumnSortable(true);
    colLangCode.setEditableOnEdit(true);
    colLangCode.setEditableOnInsert(true);
    colLangCode.setPreferredWidth(80);
    colLangCode.setMaxCharacters(20);
    colFirstName.setMaxCharacters(20);
    colFirstName.setColumnDuplicable(true);
    colFirstName.setColumnFilterable(true);
    colFirstName.setColumnName("firstNameSYS03");
    colFirstName.setColumnRequired(false);
    colFirstName.setColumnSortable(true);
    colFirstName.setEditableOnEdit(true);
    colFirstName.setEditableOnInsert(true);
    colFirstName.setPreferredWidth(120);
    colLastName.setMaxCharacters(20);
    colLastName.setColumnDuplicable(true);
    colLastName.setColumnFilterable(true);
    colLastName.setColumnName("lastNameSYS03");
    colLastName.setColumnRequired(false);
    colLastName.setColumnSortable(true);
    colLastName.setEditableOnEdit(true);
    colLastName.setEditableOnInsert(true);
    colLastName.setPreferredWidth(120);
    colRoleDescr.setColumnName("descriptionSYS10");
    colRoleDescr.setHeaderColumnName("roleDescription");
    colRoleDescr.setPreferredWidth(300);
    colSel.setColumnName("selected");
    colSel.setShowDeSelectAllInPopupMenu(true);
    colSel.setEditableOnEdit(true);
    usersPanel.setBorder(titledBorder2);
    rolesPanel.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("user roles"));
    titledBorder1.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("users"));
    titledBorder2.setTitleColor(Color.blue);
    codEmpCode.setColumnRequired(false);
    codEmpCode.setEditableOnEdit(true);
    codEmpCode.setEditableOnInsert(true);
    codEmpCode.setPreferredWidth(90);
    codEmpCode.setColumnName("employeeCodeSCH01");
    colProgressiveREG04.setColumnName("progressiveReg04SYS03");
    colProgressiveREG04.setColumnSelectable(false);
    colProgressiveREG04.setColumnSortable(false);
    colProgressiveREG04.setColumnVisible(false);
    colCompanyCode.setColumnName("companyCodeSys01SYS03");
    colCompanyCode.setColumnSelectable(false);
    colCompanyCode.setColumnVisible(false);
    colDefCompany.setHeaderColumnName("default company");
    usersPanel.add(buttonsPanel,BorderLayout.NORTH);
    usersPanel.add(usersGridControl,BorderLayout.CENTER);
    this.getContentPane().add(usersPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(rolesPanel,     new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 100));
    rolesPanel.add(rolesButtonsPanel, BorderLayout.NORTH);
    usersGridControl.getColumnContainer().add(colUsername, null);
    usersGridControl.getColumnContainer().add(colPaswd, null);
    usersGridControl.getColumnContainer().add(colDateExp, null);
    usersGridControl.getColumnContainer().add(colLangCode, null);
    usersGridControl.getColumnContainer().add(colDefCompany, null);
    usersGridControl.getColumnContainer().add(codEmpCode, null);
    usersGridControl.getColumnContainer().add(colProgressiveREG04, null);
    usersGridControl.getColumnContainer().add(colCompanyCode, null);
    usersGridControl.getColumnContainer().add(colFirstName, null);
    usersGridControl.getColumnContainer().add(colLastName, null);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(navigatorBar, null);
    buttonsPanel.add(exportButton, null);
    rolesButtonsPanel.add(editButton1, null);
    rolesButtonsPanel.add(saveButton1, null);
    rolesButtonsPanel.add(reloadButton1, null);
    rolesPanel.add(userRolesGridControl, BorderLayout.CENTER);
    userRolesGridControl.getColumnContainer().add(colRoleDescr, null);
    userRolesGridControl.getColumnContainer().add(colSel, null);
  }


  public GridControl getUserRolesGridControl() {
    return userRolesGridControl;
  }


  public GridControl getUsersGridControl() {
    return usersGridControl;
  }

}
