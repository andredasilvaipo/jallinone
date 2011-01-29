package org.jallinone.scheduler.callouts.client;

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
import org.jallinone.scheduler.callouts.java.*;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import org.jallinone.registers.vat.java.VatVO;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.warehouse.availability.client.*;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.subjects.client.*;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.scheduler.activities.client.*;
import java.util.HashSet;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocController;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.subjects.java.Subject;
import java.text.SimpleDateFormat;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Call-Out Request detail frame.</p>
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
public class CallOutRequestFrame extends InternalFrame implements CloseActivity {

  JPanel buttonsPanel = new JPanel();
  JTabbedPane tab = new JTabbedPane();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel detailPanel = new JPanel();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  LabelControl labelCompanyCode = new LabelControl();
  LabelControl labelProg = new LabelControl();
  ComboBoxControl controlCallOutType = new ComboBoxControl();

  BorderLayout borderLayout1 = new BorderLayout();
  FlowLayout flowLayout2 = new FlowLayout();

  JPanel subjectPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  Form calloutPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel subjectToolbarPanel = new JPanel();
  JPanel cardPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel subjectButtonsPanel = new JPanel();
  JPanel subjectTypePanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  InsertButton insertButton5 = new InsertButton();
  EditButton editButton5 = new EditButton();
  SaveButton saveButton5 = new SaveButton();
  ReloadButton reloadButton5 = new ReloadButton();
  LabelControl labelSubjectType = new LabelControl();
  ComboBoxControl controlSubjectType = new ComboBoxControl();
  OrganizationPanel organizationPanel = new OrganizationPanel(false);
  PeoplePanel peoplePanel = new PeoplePanel();
  CardLayout cardLayout1 = new CardLayout();
  CodLookupControl filterPeopleButton = new CodLookupControl();
  CodLookupControl filterOrgButton = new CodLookupControl();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  LookupController peopleController = new LookupController();
  LookupServerDataLocator peopleDataLocator = new LookupServerDataLocator();
  LookupController orgController = new LookupController();
  LookupServerDataLocator orgDataLocator = new LookupServerDataLocator();

  private CallOutSubjectController callOutSubjectController = new CallOutSubjectController(this);
  NumericControl controlProg = new NumericControl();
  LabelControl labelYear = new LabelControl();
  NumericControl controlYear = new NumericControl();
  LabelControl labelDate = new LabelControl();
  DateControl controlDate = new DateControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelPriority = new LabelControl();
  ComboBoxControl controlPriority = new ComboBoxControl();
  LabelControl labelCallOutType = new LabelControl();
  CodLookupControl controlCallOutCode = new CodLookupControl();
  TextControl controlCallOutDescr = new TextControl();
  LabelControl labelState = new LabelControl();
  TextControl controlUsername = new TextControl();
  TextAreaControl controlNote = new TextAreaControl();
  LabelControl labelNote = new LabelControl();
  LabelControl labelUsername = new LabelControl();
  ComboBoxControl controlState = new ComboBoxControl();
  LookupController callOutController = new LookupController();
  LookupServerDataLocator callOutDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator callOutTreeLevelDataLocator = new TreeServerDataLocator();
  ScheduledActivityPanel actPanel = new ScheduledActivityPanel(false,this);
  ScheduledResourcesPanel resourcesPanel = new ScheduledResourcesPanel(actPanel);
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  private CallOutRequestsFrame gridFrame = null;
  GenericButton invoiceButton = new GenericButton(new ImageIcon(ClientUtils.getImage("doc3.gif")));
  GenericButton viewInvoiceButton = new GenericButton(new ImageIcon(ClientUtils.getImage("docs.gif")));
  LabelControl labelItemType = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();
  CodLookupControl controlItemCode = new CodLookupControl();
  TextControl controlItemDescr = new TextControl();
  CheckBoxControl controlFilterItems = new CheckBoxControl();

	/** item code lookup data locator */
	LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

	/** item code lookup controller */
	LookupController itemController = new LookupController();

	LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
	TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

	private	java.util.List itemTypes = null;


  public CallOutRequestFrame(CallOutRequestController controller,CallOutRequestsFrame gridFrame) {
    this.gridFrame = gridFrame;
    try {
      jbInit();
      setSize(750,640);
      setMinimumSize(new Dimension(750,640));

      organizationPanel.setFunctionId("REG04_CONTACTS");
      organizationPanel.setVOClassName("org.jallinone.subjects.java.OrganizationVO");
      organizationPanel.getVOModel().setValueObject(new OrganizationVO());
      organizationPanel.setFormController(callOutSubjectController);
      organizationPanel.setInsertButton(insertButton5);
      organizationPanel.setEditButton(editButton5);
      organizationPanel.setSaveButton(saveButton5);
      organizationPanel.setReloadButton(reloadButton5);

      peoplePanel.setFunctionId("REG04_CONTACTS");
      peoplePanel.setVOClassName("org.jallinone.subjects.java.PeopleVO");
      peoplePanel.getVOModel().setValueObject(new PeopleVO());
      peoplePanel.setFormController(callOutSubjectController);
      peoplePanel.setInsertButton(insertButton5);
      peoplePanel.setEditButton(editButton5);
      peoplePanel.setSaveButton(saveButton5);
      peoplePanel.setReloadButton(reloadButton5);


      calloutPanel.setFormController(controller);

      CustomizedControls customizedControls = new CustomizedControls(tab,calloutPanel,ApplicationConsts.ID_CALL_OUTS);

      // people lookup...
      peopleDataLocator.setGridMethodName("loadSubjectPerName");
      peopleDataLocator.setValidationMethodName("");
      filterPeopleButton.setLookupController(peopleController);
      peopleController.setLookupDataLocator(peopleDataLocator);
      peopleController.setFrameTitle("people");
      peopleController.setLookupValueObjectClassName("org.jallinone.subjects.java.PeopleVO");
      peopleController.setAllColumnVisible(false);
      peopleController.setVisibleColumn("name_1REG04", true);
      peopleController.setVisibleColumn("name_2REG04", true);
      peopleController.setVisibleColumn("addressREG04", true);
      peopleController.setVisibleColumn("cityREG04", true);
      peopleController.setVisibleColumn("provinceREG04", true);
      peopleController.setVisibleColumn("countryREG04", true);
      peopleController.setVisibleColumn("zipREG04", true);
      peopleController.setHeaderColumnName("addressREG04", "address");
      peopleController.setHeaderColumnName("cityREG04", "city");
      peopleController.setHeaderColumnName("provinceREG04", "prov");
      peopleController.setHeaderColumnName("countryREG04", "country");
      peopleController.setHeaderColumnName("zipREG04", "zip");
      peopleController.setPreferredWidthColumn("name_1REG04", 120);
      peopleController.setPreferredWidthColumn("name_2REG04", 120);
      peopleController.setPreferredWidthColumn("addressREG04", 200);
      peopleController.setPreferredWidthColumn("provinceREG04", 50);
      peopleController.setPreferredWidthColumn("countryREG04", 70);
      peopleController.setPreferredWidthColumn("zipREG04", 50);
      peopleController.setFramePreferedSize(new Dimension(740,500));
      peopleController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          PeopleVO vo = (PeopleVO)peopleController.getLookupVO();
          peoplePanel.getVOModel().setValueObject(vo);
          peoplePanel.setMode(Consts.READONLY);
          peoplePanel.pull();
          tab.setEnabledAt(1,true);
          controlCompaniesCombo.setEnabled(false);
          controlSubjectType.setEnabled(false);
        }

        public void beforeLookupAction(ValueObject parentVO) {
          peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE);
        }

        public void forceValidate() {}

      });


      // organization lookup...
      orgDataLocator.setGridMethodName("loadSubjectPerName");
      orgDataLocator.setValidationMethodName("");
      filterOrgButton.setLookupController(orgController);
      orgController.setLookupDataLocator(orgDataLocator);
      orgController.setFrameTitle("organizations");
      orgController.setLookupValueObjectClassName("org.jallinone.subjects.java.OrganizationVO");
      orgController.setAllColumnVisible(false);
      orgController.setVisibleColumn("name_1REG04", true);
      orgController.setVisibleColumn("addressREG04", true);
      orgController.setVisibleColumn("cityREG04", true);
      orgController.setVisibleColumn("provinceREG04", true);
      orgController.setVisibleColumn("countryREG04", true);
      orgController.setVisibleColumn("zipREG04", true);
      orgController.setHeaderColumnName("addressREG04", "address");
      orgController.setHeaderColumnName("cityREG04", "city");
      orgController.setHeaderColumnName("provinceREG04", "prov");
      orgController.setHeaderColumnName("countryREG04", "country");
      orgController.setHeaderColumnName("zipREG04", "zip");
      orgController.setPreferredWidthColumn("name_1REG04", 150);
      orgController.setPreferredWidthColumn("addressREG04", 200);
      orgController.setPreferredWidthColumn("provinceREG04", 50);
      orgController.setPreferredWidthColumn("zipREG04", 50);
      orgController.setFramePreferedSize(new Dimension(740,500));
      orgController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          OrganizationVO vo = (OrganizationVO)orgController.getLookupVO();
          organizationPanel.getVOModel().setValueObject(vo);
          organizationPanel.setMode(Consts.READONLY);
          organizationPanel.pull();
          tab.setEnabledAt(1,true);
          controlCompaniesCombo.setEnabled(false);
          controlSubjectType.setEnabled(false);
        }

        public void beforeLookupAction(ValueObject parentVO) {
          orgDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          orgDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION);
        }

        public void forceValidate() {}

      });


      // call-out code lookup...
      callOutDataLocator.setGridMethodName("loadCallOuts");
      callOutDataLocator.setValidationMethodName("validateCallOutCode");
      controlCallOutCode.setLookupController(callOutController);
      controlCallOutCode.setControllerMethodName("getCallOuts");
      callOutController.setCodeSelectionWindow(callOutController.TREE_GRID_FRAME);
      callOutController.setLookupDataLocator(callOutDataLocator);
      callOutTreeLevelDataLocator.setServerMethodName("loadHierarchy");
      callOutDataLocator.setTreeDataLocator(callOutTreeLevelDataLocator);
      callOutController.setFrameTitle("call-outs");
      callOutController.setLookupValueObjectClassName("org.jallinone.scheduler.callouts.java.CallOutVO");
      callOutController.addLookup2ParentLink("callOutCodeSCH10", "callOutCodeSch10SCH03");
      callOutController.addLookup2ParentLink("descriptionSYS10", "callOutDescriptionSYS10");
      callOutController.setAllColumnVisible(false);
      callOutController.setVisibleColumn("callOutCodeSCH10", true);
      callOutController.setVisibleColumn("descriptionSYS10", true);
      callOutController.setPreferredWidthColumn("descriptionSYS10",250);
      callOutController.setFramePreferedSize(new Dimension(600,500));
      callOutDataLocator.setNodeNameAttribute("descriptionSYS10");
      callOutController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
          vo.setDescriptionSCH03(vo.getCallOutDescriptionSYS10());
          calloutPanel.pull("descriptionSCH03");
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
          if (vo.getCompanyCodeSys01SCH03()==null)
            vo.setCompanyCodeSys01SCH03((String)controlCompaniesCombo.getValue());
          callOutTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02SCH10());
          callOutTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
          callOutDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02SCH10());
          callOutDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02SCH10());
          callOutDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
          callOutDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
        }

        public void forceValidate() {}

      });

      actPanel.setSubjectVisible(false);
      actPanel.getControlActType().setDomainId("ACTIVITY_TYPE");
      actPanel.getControlActType().setEnabledOnInsert(false);
      actPanel.getControlActType().setEnabledOnEdit(false);
      actPanel.setFormController(new CallOutActivityController(this));
      actPanel.setFunctionId("SCH03");
      actPanel.setMode(Consts.READONLY);
      actPanel.setInsertButtonVisible(false);

      init();

      for(int i=1;i<getTab().getTabCount();i++)
        getTab().setEnabledAt(i,false);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  public Form getSubjectForm() {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
    if (vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION)) {
      return organizationPanel;
    }
    else {
      return peoplePanel;
    }
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public final void loadDataCompleted(boolean error,CallOutRequestPK pk) {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();

    tab.setEnabledAt(1,true);
    tab.setEnabledAt(2,true);

    if (vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION) ||
        vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ||
        vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) {
      organizationPanel.setMode(Consts.READONLY);
      organizationPanel.reload();
    }
    else {
      peoplePanel.setMode(Consts.READONLY);
      peoplePanel.reload();
    }

    if (vo.getProgressiveSch06SCH03()!=null) {
      if (actPanel.getMode()!=Consts.READONLY)
        actPanel.setMode(Consts.READONLY);
      actPanel.reload();
    }
    else {
      if (actPanel.getMode()!=Consts.READONLY)
        actPanel.setMode(Consts.READONLY);
      actPanel.insert();
    }

    controlCompaniesCombo.setEnabled(false);
    controlSubjectType.setEnabled(false);
    if (vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
        vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ||
        vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION))
      controlSubjectType.setValue(ApplicationConsts.SUBJECT_ORGANIZATION);
    else
      controlSubjectType.setValue(ApplicationConsts.SUBJECT_PEOPLE);
    controlCompaniesCombo.setValue(vo.getCompanyCodeSys01SCH03());

    if (vo.getCallOutStateSCH03().equals(ApplicationConsts.CLOSED) ||
        vo.getCallOutStateSCH03().equals(ApplicationConsts.INVOICED)) {

      editButton1.setEnabled(false);
      deleteButton1.setEnabled(false);
      reloadButton1.setEnabled(false);
      if (peoplePanel.getEditButton()!=null)
        peoplePanel.getEditButton().setEnabled(false);
      if (peoplePanel.getReloadButton()!=null)
          peoplePanel.getReloadButton().setEnabled(false);
      if (peoplePanel.getDeleteButton()!=null)
        peoplePanel.getDeleteButton().setEnabled(false);
      if (organizationPanel.getEditButton()!=null)
        organizationPanel.getEditButton().setEnabled(false);
      if (organizationPanel.getReloadButton()!=null)
        organizationPanel.getReloadButton().setEnabled(false);
      if (organizationPanel.getDeleteButton()!=null)
        organizationPanel.getDeleteButton().setEnabled(false);

      confirmButton.setEnabled(false);
      if (vo.getDocNumberDoc01SCH03()==null && vo.getProgressiveSch06SCH03()!=null &&
          !vo.getCallOutStateSCH03().equals(ApplicationConsts.INVOICED))
        invoiceButton.setEnabled(true);
      else
        invoiceButton.setEnabled(false);

      if (vo.getCallOutStateSCH03().equals(ApplicationConsts.INVOICED))
        viewInvoiceButton.setEnabled(true);
      else
        viewInvoiceButton.setEnabled(false);
    }
    else {
      confirmButton.setEnabled(true);
      invoiceButton.setEnabled(false);
      viewInvoiceButton.setEnabled(false);
    }

		if (vo.getItemCodeItm01SCH03()==null) {
			resourcesPanel.setItem(null,null,null);
		}
		else {
			resourcesPanel.setItem(vo.getProgressiveHie02ITM01(),vo.getCompanyCodeSys01SCH03(),vo.getItemCodeItm01SCH03());
		}

  }


  /**
   * This method is called by ScheduledActivityPanel when user clicks on "Close Activity" button.
   */
  public void closeActivity() {
    editButton1.setEnabled(false);
    deleteButton1.setEnabled(false);
    reloadButton1.setEnabled(false);
    if (peoplePanel.getEditButton()!=null)
      peoplePanel.getEditButton().setEnabled(false);
    if (peoplePanel.getReloadButton()!=null)
        peoplePanel.getReloadButton().setEnabled(false);
    if (peoplePanel.getDeleteButton()!=null)
      peoplePanel.getDeleteButton().setEnabled(false);
    if (organizationPanel.getEditButton()!=null)
      organizationPanel.getEditButton().setEnabled(false);
    if (organizationPanel.getReloadButton()!=null)
      organizationPanel.getReloadButton().setEnabled(false);
    if (organizationPanel.getDeleteButton()!=null)
      organizationPanel.getDeleteButton().setEnabled(false);

    controlState.setValue(ApplicationConsts.CLOSED);

    confirmButton.setEnabled(false);
    invoiceButton.setEnabled(true);
    viewInvoiceButton.setEnabled(false);

    resourcesPanel.setButtonsEnabled(false);
    if (gridFrame!=null)
      gridFrame.getGrid().reloadCurrentBlockOfData();

  }



  /**
   * Setting-up some input controls.
   */
  private void init() {
    // fill in the companies combo-box...
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList("SCH03");
    Domain domain = new Domain("DOMAIN_SCH03");
    for (int i = 0; i < companiesList.size(); i++) {
      if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
          "SCH03",companiesList.get(i).toString()
      ))
        domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
    }
    controlCompaniesCombo.setDomain(domain);
    controlCompaniesCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
          vo.setCompanyCodeSys01SCH03((String)controlCompaniesCombo.getValue());
        }
      }
    });

    // set call-out types in call-out type input control...
    Response res = ClientUtils.getData("loadCallOutTypes",new GridParams());
    Domain d = new Domain("CALL_OUT_TYPES");
    if (!res.isError()) {
      CallOutTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (CallOutTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02SCH11(),vo.getDescriptionSYS10());
      }
    }
    controlCallOutType.setDomain(d);
    controlCallOutType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          controlCallOutCode.getCodBox().setText(null);
          controlCallOutDescr.setText("");
          callOutTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, controlCallOutType.getValue());
        }
      }
    });
    if (d.getDomainPairList().length==1)
      controlCallOutType.getComboBox().setSelectedIndex(0);
    else
      controlCallOutType.getComboBox().setSelectedIndex(-1);

    // add listener in subject type input control...
    controlSubjectType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED)
          subjectChanged((String)controlSubjectType.getValue());
      }
    });


		// retrieve item types..
		res = ClientUtils.getData("loadItemTypes",new GridParams());
		final Domain dd = new Domain("ITEM_TYPES");
		if (!res.isError()) {
			ItemTypeVO vo = null;
			itemTypes = ((VOListResponse)res).getRows();
			for(int i=0;i<itemTypes.size();i++) {
				vo = (ItemTypeVO)itemTypes.get(i);
				dd.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
			}
		}
		controlItemType.setDomain(dd);
		controlItemType.getComboBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (calloutPanel.getMode()!=Consts.READONLY && e.getStateChange()==e.SELECTED) {
					try {
						int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
						Object selValue = dd.getDomainPairList()[selIndex].getCode();
						ItemTypeVO vo = (ItemTypeVO)itemTypes.get(selIndex);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());


						controlItemCode.setValue(null);
						controlItemCode.validateCode(null);
					}
					catch (Exception ex) {
					}

				}
			}
		});

		// item code lookup...
		itemDataLocator.setGridMethodName("loadItems");
		itemDataLocator.setValidationMethodName("validateItemCode");

		controlItemCode.setLookupController(itemController);
		itemController.setLookupDataLocator(itemDataLocator);
		itemController.setFrameTitle("items");

		itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
		treeLevelDataLocator.setServerMethodName("loadHierarchy");
		itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
		itemDataLocator.setNodeNameAttribute("descriptionSYS10");

		itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");

		itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01SCH03");
		itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
		itemController.setAllColumnVisible(false);
		itemController.setVisibleColumn("companyCodeSys01ITM01", true);
		itemController.setVisibleColumn("itemCodeITM01", true);
		itemController.setVisibleColumn("descriptionSYS10", true);
		itemController.setVisibleColumn("docDateDOC01", true);
		itemController.setPreferredWidthColumn("descriptionSYS10", 200);
		itemController.setFramePreferedSize(new Dimension(730,500));
		itemController.setShowErrorMessage(false);
		itemController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				GridItemVO vo = (GridItemVO)itemController.getLookupVO();
				if (vo==null || vo.getItemCodeITM01()==null) {
					resourcesPanel.setItem(null,null,null);
				}
				else {
					SimpleDateFormat sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
					controlNote.setText(
					  ClientSettings.getInstance().getResources().getResource("item sold on")+" "+
						sdf.format(vo.getDocDateDOC01())
					);
				  resourcesPanel.setItem(vo.getProgressiveHie02ITM01(),vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01());
				}
			}

			public void beforeLookupAction(ValueObject gridVO) {
				DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
				Subject subVO = (Subject)getSubjectForm().getVOModel().getValueObject();
				vo.setCompanyCodeSys01SCH03(subVO.getCompanyCodeSys01REG04());
				vo.setProgressiveReg04SCH03(subVO.getProgressiveREG04());
				itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_PURCHASED_ITEMS,new Boolean(controlFilterItems.isSelected()));
				itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_PURCHASED_ITEMS,new Boolean(controlFilterItems.isSelected()));
				if (vo.getProgressiveReg04SCH03()!=null) {
					itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH03());
					itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH03());
				}
				else {
					itemDataLocator.getLookupFrameParams().remove(ApplicationConsts.PROGRESSIVE_REG04);
					itemDataLocator.getLookupValidationParameters().remove(ApplicationConsts.PROGRESSIVE_REG04);
				}
			}

			public void forceValidate() {}

		});



  }


  /**
   * Toggle between the two subject panels, according to the subject type.
   */
  public final void subjectChanged(String subjectTypeREG04) {
    cardLayout1.show(cardPanel,subjectTypeREG04);
    if (controlSubjectType.getValue()==null ||
        !controlSubjectType.getValue().equals(subjectTypeREG04)) {
      controlSubjectType.setValue(subjectTypeREG04);

      if (ApplicationConsts.SUBJECT_ORGANIZATION.equals(subjectTypeREG04)) {
        organizationPanel.setInsertButton(insertButton5);
        organizationPanel.setEditButton(editButton5);
        organizationPanel.setSaveButton(saveButton5);
        organizationPanel.setReloadButton(reloadButton5);
        peoplePanel.setInsertButton(null);
        peoplePanel.setEditButton(null);
        peoplePanel.setSaveButton(null);
        peoplePanel.setReloadButton(null);
      }
      else {
        organizationPanel.setInsertButton(null);
        organizationPanel.setEditButton(null);
        organizationPanel.setSaveButton(null);
        organizationPanel.setReloadButton(null);
        peoplePanel.setInsertButton(insertButton5);
        peoplePanel.setEditButton(editButton5);
        peoplePanel.setSaveButton(saveButton5);
        peoplePanel.setReloadButton(reloadButton5);
      }
    }

    if (controlSubjectType.getValue()!=null) {
      DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
      vo.setSubjectTypeReg04SCH03(subjectTypeREG04);
    }

  }


  /**
   * Retrieve people: if it exists, then load it in the panel and set the panel in detail mode and enabled the other panes in the tabbed pane.
   * @param name_1 first name
   * @param name_2 last name
   */
  private void retrievePeople(String name_1,String name_2) {
    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    gridParams.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE);
    gridParams.getOtherGridParams().put(ApplicationConsts.NAME_1,name_1);
    gridParams.getOtherGridParams().put(ApplicationConsts.NAME_2,name_2);
    Response res = ClientUtils.getData("loadSubjectPerName",gridParams);
    if (!res.isError()) {
      java.util.List vos = ((VOListResponse)res).getRows();
      if (vos.size()==1) {
        // person found...
        PeopleVO vo = (PeopleVO)vos.get(0);
        peoplePanel.getVOModel().setValueObject(vo);
        peoplePanel.setMode(Consts.READONLY);
        peoplePanel.pull();
        tab.setEnabledAt(1,true);
        controlCompaniesCombo.setEnabled(false);
        controlSubjectType.setEnabled(false);
      }
    }
  }


  /**
   * Retrieve organization: if it exists, then load it in the panel and set the panel in detail mode and enabled the other panes in the tabbed pane.
   * @param corporateName corporate name
   */
  private void retrieveOrganization(String corporateName) {
    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    gridParams.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION);
    gridParams.getOtherGridParams().put(ApplicationConsts.NAME_1,corporateName);
    Response res = ClientUtils.getData("loadSubjectPerName",gridParams);
    if (!res.isError()) {
      java.util.List vos = ((VOListResponse)res).getRows();
      if (vos.size()==1) {
        // organization found...
        OrganizationVO vo = (OrganizationVO)vos.get(0);
        organizationPanel.getVOModel().setValueObject(vo);
        organizationPanel.setMode(Consts.READONLY);
        organizationPanel.pull();
        tab.setEnabledAt(1,true);
        controlCompaniesCombo.setEnabled(false);
        controlSubjectType.setEnabled(false);
      }
    }
  }


  private void jbInit() throws Exception {
    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("close activity"));
    confirmButton.setEnabled(false);
    confirmButton.addActionListener(new CallOutRequestFrame_confirmButton_actionAdapter(this));

    invoiceButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("create invoice"));
    invoiceButton.setEnabled(false);
    invoiceButton.addActionListener(new CallOutRequestFrame_invoiceButton_actionAdapter(this));

    viewInvoiceButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("view invoice"));
    viewInvoiceButton.setEnabled(false);
    viewInvoiceButton.addActionListener(new CallOutRequestFrame_viewInvoiceButton_actionAdapter(this));

    filterPeopleButton.setAllowOnlyNumbers(true);
    filterPeopleButton.setCodBoxVisible(false);
    filterPeopleButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("retrieve people type subjects"));
    filterPeopleButton.addActionListener(new CallOutRequestFrame_filterPeopleButton_actionAdapter(this));
    filterPeopleButton.setMinimumSize(new Dimension(30,20));
    filterPeopleButton.setPreferredSize(new Dimension(30,20));
    filterPeopleButton.setSize(new Dimension(30,20));
    filterOrgButton.setAllowOnlyNumbers(true);
    filterOrgButton.setCodBoxVisible(false);
    filterOrgButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("retrieve organization type subjects"));
    filterOrgButton.addActionListener(new CallOutRequestFrame_filterOrgButton_actionAdapter(this));
    filterOrgButton.setMinimumSize(new Dimension(30,20));
    filterOrgButton.setPreferredSize(new Dimension(30,20));
    filterOrgButton.setSize(new Dimension(30,20));

    labelYear.setRequestFocusEnabled(true);
    labelYear.setToolTipText("");
    labelYear.setText("requestYearSCH03");
    labelDate.setText("requestDateSCH03");
    labelDescr.setText("descriptionSCH03");
    labelPriority.setText("prioritySCH03");
    labelCallOutType.setText("callOutType");
    labelState.setText("callOutStateSCH03");
    labelNote.setText("noteSCH03");
    labelUsername.setText("usernameSys03SCH03");
    controlProg.setAttributeName("progressiveSCH03");
    controlProg.setLinkLabel(labelProg);
    controlProg.setRequired(false);
    controlProg.setEnabledOnInsert(false);
    controlProg.setEnabledOnEdit(false);
    controlYear.setAttributeName("requestYearSCH03");
    controlYear.setLinkLabel(labelYear);
    controlYear.setEnabledOnInsert(false);
    controlYear.setEnabledOnEdit(false);
    controlDate.setLinkLabel(labelDate);
    controlDate.setRequired(true);
    controlDescr.setAttributeName("descriptionSCH03");
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    controlState.setAttributeName("callOutStateSCH03");
    controlState.setDomainId("CALL_OUT_STATE");
    controlState.setLinkLabel(labelState);
    controlState.setEnabledOnInsert(false);
    controlState.setEnabledOnEdit(false);
    controlPriority.setDomainId("ACTIVITY_PRIORITY");
    controlPriority.setLinkLabel(labelPriority);
    controlPriority.setRequired(true);
    controlUsername.setLinkLabel(labelUsername);
    controlUsername.setRequired(true);
    controlUsername.setEnabledOnInsert(false);
    controlUsername.setEnabledOnEdit(false);
    controlCallOutType.setEnabledOnEdit(false);
    controlCallOutCode.setAttributeName("callOutCodeSch10SCH03");
    controlCallOutCode.setLinkLabel(labelCallOutType);
    controlCallOutCode.setMaxCharacters(20);
    controlCallOutCode.setRequired(true);
    controlCallOutDescr.setAttributeName("callOutDescriptionSYS10");
    controlCallOutDescr.setEnabledOnInsert(false);
    controlCallOutDescr.setEnabledOnEdit(false);
    labelItemType.setText("itemType");
    controlItemCode.setAttributeName("itemCodeItm01SCH03");
    controlItemCode.setColumns(5);
    controlItemCode.setEnabledOnEdit(false);
    controlItemCode.setMaxCharacters(20);
    controlItemDescr.setAttributeName("descriptionSYS10");
    controlItemDescr.setEnabledOnInsert(false);
    controlItemDescr.setEnabledOnEdit(false);
    controlItemType.setAttributeName("progressiveHie02ITM01");
    controlItemType.setEnabledOnEdit(false);
    controlFilterItems.setText("show purchased items");
    controlFilterItems.addItemListener(new CallOutRequestFrame_controlFilterItems_itemAdapter(this));
    peoplePanel.add(filterPeopleButton,    new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    organizationPanel.add(filterOrgButton,    new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    peoplePanel.getControlCorpName1().addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (peoplePanel.getControlCorpName1().getValue()!=null &&
            !peoplePanel.getControlCorpName1().getValue().equals("") &&
            peoplePanel.getControlCorpName2().getValue()!=null &&
            !peoplePanel.getControlCorpName2().getValue().equals(""))
          retrievePeople(peoplePanel.getControlCorpName1().getValue().toString(),peoplePanel.getControlCorpName2().getValue().toString());
      }
    });
    peoplePanel.getControlCorpName2().addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (peoplePanel.getMode()==Consts.INSERT &&
            peoplePanel.getControlCorpName1().getValue()!=null &&
            !peoplePanel.getControlCorpName1().getValue().equals("") &&
            peoplePanel.getControlCorpName2().getValue()!=null &&
            !peoplePanel.getControlCorpName2().getValue().equals(""))
          retrievePeople(peoplePanel.getControlCorpName1().getValue().toString(),peoplePanel.getControlCorpName2().getValue().toString());
      }
    });
    organizationPanel.getControlCorpName1().addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (organizationPanel.getMode()==Consts.INSERT &&
            organizationPanel.getControlCorpName1().getValue()!=null &&
            !organizationPanel.getControlCorpName1().getValue().equals(""))
          retrieveOrganization(organizationPanel.getControlCorpName1().getValue().toString());
      }
    });

    cardPanel.setLayout(cardLayout1);
    cardPanel.add(organizationPanel,ApplicationConsts.SUBJECT_ORGANIZATION);
    cardPanel.add(peoplePanel,ApplicationConsts.SUBJECT_PEOPLE);

    controlSubjectType.setDomainId("SUBJECT_TYPE_2");
    controlSubjectType.setLinkLabel(labelSubjectType);
    controlSubjectType.setAttributeName("subjectTypeREG04");
    controlSubjectType.setEnabledOnEdit(false);
//    controlSubjectType.setCanCopy(true);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("call-out request detail"));
    calloutPanel.setVOClassName("org.jallinone.scheduler.callouts.java.DetailCallOutRequestVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    detailPanel.setLayout(borderLayout2);
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    calloutPanel.setEditButton(editButton1);
    calloutPanel.setReloadButton(reloadButton1);
    calloutPanel.setDeleteButton(deleteButton1);
    calloutPanel.setSaveButton(saveButton1);
    calloutPanel.setFunctionId("SCH03");
    labelCompanyCode.setText("companyCode");
    labelProg.setText("call-out number");
    controlCompaniesCombo.setAttributeName("companyCodeSys01SCH10");
    controlCompaniesCombo.setCanCopy(true);
    controlCompaniesCombo.setLinkLabel(labelCompanyCode);
    controlCompaniesCombo.setRequired(true);
    controlCompaniesCombo.setEnabledOnEdit(false);
    controlCallOutType.setAttributeName("progressiveHie02SCH10");
    controlCallOutType.setCanCopy(true);
    controlCallOutType.setLinkLabel(labelCallOutType);
    controlCallOutType.setRequired(true);




    flowLayout2.setAlignment(FlowLayout.LEFT);
    calloutPanel.setLayout(gridBagLayout1);
    subjectPanel.setLayout(borderLayout3);
    subjectToolbarPanel.setLayout(borderLayout4);
    subjectButtonsPanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    insertButton5.setText("insertButton5");
    editButton5.setText("editButton2");
    saveButton5.setText("saveButton5");
    reloadButton5.setText("reloadButton5");
    labelSubjectType.setText("subject type");
    subjectTypePanel.setLayout(gridBagLayout3);
    detailPanel.add(buttonsPanel,  BorderLayout.NORTH);
    this.getContentPane().add(tab, BorderLayout.CENTER);
    tab.add(subjectPanel,  "subjectPanel");
    subjectPanel.add(subjectToolbarPanel, BorderLayout.NORTH);
    subjectPanel.add(cardPanel,  BorderLayout.CENTER);
    tab.add(detailPanel,   "detailPanel");
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(confirmButton, null);
    buttonsPanel.add(invoiceButton, null);
    buttonsPanel.add(viewInvoiceButton, null);

    tab.add(actPanel,  "activityPanel");
    tab.add(resourcesPanel,  "resourcesPanel");


    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("subject identification"));
    tab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("call-out detail"));
    tab.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("scheduled activity"));
    tab.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("activity resources"));


    controlNote.setAttributeName("noteSCH03");
    controlDate.setAttributeName("requestDateSCH03");
    controlDate.setDateType(Consts.TYPE_DATE_TIME);
    controlUsername.setAttributeName("usernameSys03SCH03");
    controlPriority.setAttributeName("prioritySCH03");

    detailPanel.add(calloutPanel,  BorderLayout.CENTER);
    calloutPanel.add(controlProg,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelProg,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectTypePanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectTypePanel.add(controlCompaniesCombo,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 20), 60, 0));

    subjectToolbarPanel.add(subjectButtonsPanel,  BorderLayout.NORTH);
    subjectToolbarPanel.add(subjectTypePanel,  BorderLayout.SOUTH);
    subjectButtonsPanel.add(insertButton5, null);
    subjectButtonsPanel.add(editButton5, null);
    subjectButtonsPanel.add(saveButton5, null);
    subjectButtonsPanel.add(reloadButton5, null);
    subjectTypePanel.add(labelSubjectType,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectTypePanel.add(controlSubjectType,     new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    calloutPanel.add(labelYear,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlYear,       new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelDate,        new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlDate,         new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelDescr,       new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlDescr,       new GridBagConstraints(1, 2, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelPriority,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlPriority,       new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelState,      new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    calloutPanel.add(controlNote,       new GridBagConstraints(1, 5, 5, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelNote,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    calloutPanel.add(labelUsername,      new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlUsername,     new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlState,      new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelCallOutType,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlCallOutType,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlCallOutCode,   new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlCallOutDescr,     new GridBagConstraints(4, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(labelItemType,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlItemType,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlItemCode, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlItemDescr,  new GridBagConstraints(3, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    calloutPanel.add(controlFilterItems,  new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));


  }


  public Form getCalloutPanel() {
    return calloutPanel;
  }



  public final void setButtonsEnabled(boolean enabled) {
  }


  public JPanel getDetailPanel() {
    return detailPanel;
  }


  void filterOrgButton_actionPerformed(ActionEvent e) {

  }

  void filterPeopleButton_actionPerformed(ActionEvent e) {


  }
  public ComboBoxControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }
  public JTabbedPane getTab() {
    return tab;
  }
  public ComboBoxControl getControlSubjectType() {
    return controlSubjectType;
  }
  public ComboBoxControl getControlCallOutType() {
    return controlCallOutType;
  }
  public ScheduledActivityPanel getActPanel() {
    return actPanel;
  }
  public ScheduledResourcesPanel getResourcesPanel() {
    return resourcesPanel;
  }


  public PeoplePanel getPeoplePanel() {
    return peoplePanel;
  }
  public OrganizationPanel getOrganizationPanel() {
    return organizationPanel;
  }
  public GenericButton getConfirmButton() {
    return confirmButton;
  }
  public GenericButton getInvoiceButton() {
    return invoiceButton;
  }
  public GenericButton getViewInvoiceButton() {
    return viewInvoiceButton;
  }


  void invoiceButton_actionPerformed(ActionEvent e) {
    new CreateInvoiceDialog(this);
  }


  void viewInvoiceButton_actionPerformed(ActionEvent e) {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
    new SaleInvoiceDocController(null,new SaleDocPK(
      vo.getCompanyCodeSys01SCH03(),
      vo.getDocTypeDoc01SCH03(),
      vo.getDocYearDoc01SCH03(),
      vo.getDocNumberDoc01SCH03()
    ));
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("confirm call-out request closing?"),
                                  ClientSettings.getInstance().getResources().getResource("call-out request closing"),
                                  JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
     return;

  try {
    DetailCallOutRequestVO oldVO = (DetailCallOutRequestVO)calloutPanel.getVOModel().getValueObject();
    DetailCallOutRequestVO newVO = (DetailCallOutRequestVO)oldVO.clone();
    newVO.setCallOutStateSCH03(ApplicationConsts.CLOSED);
    Response res = ClientUtils.getData("updateCallOutRequest", new ValueObject[] {oldVO, newVO});
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      tab.setEnabledAt(2,false);
      tab.setEnabledAt(3,false);
      confirmButton.setEnabled(false);
      closeActivity();
    }
  }
  catch (Exception ex) {
    ex.printStackTrace();
  }

  }
  public CallOutRequestsFrame getGridFrame() {
    return gridFrame;
  }

  void controlFilterItems_itemStateChanged(ItemEvent e) {

  }



}

class CallOutRequestFrame_filterOrgButton_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_filterOrgButton_actionAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.filterOrgButton_actionPerformed(e);
  }
}

class CallOutRequestFrame_filterPeopleButton_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_filterPeopleButton_actionAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.filterPeopleButton_actionPerformed(e);
  }
}

class CallOutRequestFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_confirmButton_actionAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class CallOutRequestFrame_invoiceButton_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_invoiceButton_actionAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.invoiceButton_actionPerformed(e);
  }
}

class CallOutRequestFrame_viewInvoiceButton_actionAdapter implements java.awt.event.ActionListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_viewInvoiceButton_actionAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.viewInvoiceButton_actionPerformed(e);
  }
}

class CallOutRequestFrame_controlFilterItems_itemAdapter implements java.awt.event.ItemListener {
  CallOutRequestFrame adaptee;

  CallOutRequestFrame_controlFilterItems_itemAdapter(CallOutRequestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlFilterItems_itemStateChanged(e);
  }
}
