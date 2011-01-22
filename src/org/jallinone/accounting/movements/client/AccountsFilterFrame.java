package org.jallinone.accounting.movements.client;

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
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.client.Form;
import org.jallinone.accounting.accounts.java.AccountVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Filter frame used to view the accounts filter frame.</p>
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
public class AccountsFilterFrame extends InternalFrame {

  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  LabelControl labelFromDate = new LabelControl();
  LabelControl labelToDate = new LabelControl();
  DateControl controlFromDate = new DateControl();
  DateControl controlToDate = new DateControl();
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();

  LabelControl labelStartAccount = new LabelControl();
  CodLookupControl controlStartAccount = new CodLookupControl();
  TextControl controlStartAccountDescr = new TextControl();
  LookupController startAccountController = new LookupController();
  LookupServerDataLocator startAccountDataLocator = new LookupServerDataLocator();

  LabelControl labelEndAccount = new LabelControl();
  CodLookupControl controlEndAccount = new CodLookupControl();
  TextControl controlEndAccountDescr = new TextControl();
  LookupController endAccountController = new LookupController();
  LookupServerDataLocator endAccountDataLocator = new LookupServerDataLocator();

  Form startPanel = new Form();
  Form endPanel = new Form();


  public AccountsFilterFrame() {
    try {
      jbInit();

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC06_ACCOUNT");
      Domain domain = new Domain("DOMAIN_ACC06_ACCOUNT");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC06_ACCOUNT",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.getComboBox().setSelectedIndex(0);


      // start account code lookup...
      controlStartAccount.setLookupController(startAccountController);
      controlStartAccount.setControllerMethodName("getAccounts");
      startAccountController.setLookupDataLocator(startAccountDataLocator);
      startAccountDataLocator.setGridMethodName("loadAccounts");
      startAccountDataLocator.setValidationMethodName("validateAccountCode");
      startAccountController.setFrameTitle("accounts");
      startAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      startAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
      startAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      startAccountController.setFramePreferedSize(new Dimension(400,400));
      startAccountController.setAllColumnVisible(false);
      startAccountController.setVisibleColumn("accountCodeACC02",true);
      startAccountController.setVisibleColumn("descriptionSYS10",true);
      startAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      startAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
//          AccountVO vo = (AccountVO)startAccountController.getLookupVO();
//          controlStartAccount.setValue(vo.getAccountCodeACC02());
//          controlStartAccountDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          startAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          startAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // end account code lookup...
      controlEndAccount.setLookupController(endAccountController);
      controlEndAccount.setControllerMethodName("getAccounts");
      endAccountController.setLookupDataLocator(endAccountDataLocator);
      endAccountDataLocator.setGridMethodName("loadAccounts");
      endAccountDataLocator.setValidationMethodName("validateAccountCode");
      endAccountController.setFrameTitle("accounts");
      endAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      endAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
      endAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      endAccountController.setFramePreferedSize(new Dimension(400,400));
      endAccountController.setAllColumnVisible(false);
      endAccountController.setVisibleColumn("accountCodeACC02",true);
      endAccountController.setVisibleColumn("descriptionSYS10",true);
      endAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      endAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
//          AccountVO vo = (AccountVO)endAccountController.getLookupVO();
//          controlEndAccount.setValue(vo.getAccountCodeACC02());
//          controlEndAccountDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          endAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          endAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });




      setSize(550,260);
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    controlStartAccount.setAttributeName("accountCodeACC02");
    controlStartAccount.setMaxCharacters(20);
    controlStartAccountDescr.setAttributeName("descriptionSYS10");
    controlStartAccountDescr.setEnabledOnEdit(false);
    controlStartAccountDescr.setEnabledOnInsert(false);
    controlStartAccountDescr.setEnabled(false);

    controlEndAccount.setAttributeName("accountCodeACC02");
    controlEndAccount.setMaxCharacters(20);
    controlEndAccountDescr.setAttributeName("descriptionSYS10");
    controlEndAccountDescr.setEnabledOnEdit(false);
    controlEndAccountDescr.setEnabledOnInsert(false);
    controlEndAccountDescr.setEnabled(false);

    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print accounts movements"));
    printButton.addActionListener(new AccountsFilterFrame_printButton_actionAdapter(this));
    controlCompaniesCombo.setAttributeName("companyCodeSys01ACC02");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("print accounts movements"));
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setLayout(gridBagLayout1);
    startPanel.setLayout(gridBagLayout2);
    endPanel.setLayout(gridBagLayout3);
    labelFromDate.setText("from date");
    labelToDate.setText("to date");
    labelCompanyCode.setText("companyCodeSYS01");
    labelStartAccount.setText("from account");
    labelEndAccount.setText("to account");
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(printButton, null);
    mainPanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFromDate,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelToDate,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    startPanel.add(labelStartAccount,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    endPanel.add(labelEndAccount,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCompaniesCombo,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlFromDate,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlToDate,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    startPanel.add(controlStartAccount,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    endPanel.add(controlEndAccount,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    startPanel.add(controlStartAccountDescr,     new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    endPanel.add(controlEndAccountDescr,     new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    mainPanel.add(startPanel,     new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    mainPanel.add(endPanel,       new GridBagConstraints(0, 4, 3, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    startPanel.setFunctionId("ACC06_ACCOUNT");
    startPanel.setVOClassName("org.jallinone.accounting.accounts.java.AccountVO");
    endPanel.setFunctionId("ACC06_ACCOUNT");
    endPanel.setVOClassName("org.jallinone.accounting.accounts.java.AccountVO");
    Calendar cal = Calendar.getInstance();
    cal.set(cal.DAY_OF_MONTH,1);
    cal.set(cal.MONTH,0);
    controlFromDate.setValue(cal.getTime());
    controlToDate.setValue(new Date());
  }


  void printButton_actionPerformed(ActionEvent e) {
    if (controlFromDate.getValue()==null || controlToDate.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set a from/to date"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlCompaniesCombo.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please select a company"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlStartAccount.getValue()==null || controlEndAccount.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set a from/to account code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    HashMap params = new HashMap();
    params.put("COMPANY_CODE_SYS01",controlCompaniesCombo.getValue());
    params.put("START_DATE",controlFromDate.getValue());
    params.put("END_DATE",controlToDate.getValue());
    params.put("START_ACCOUNT_CODE",controlStartAccount.getValue());
    params.put("END_ACCOUNT_CODE",controlEndAccount.getValue());

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.FUNCTION_CODE_SYS06,"ACC06_ACCOUNT");
    map.put(ApplicationConsts.EXPORT_PARAMS,params);
    Response res = ClientUtils.getData("getJasperReport",map);
    if (!res.isError()) {
      JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
      JRViewer viewer = new JRViewer(print);
      JFrame frame = new JFrame();
      frame.setSize(MDIFrame.getInstance().getSize());
      frame.setContentPane(viewer);
      frame.setTitle(this.getTitle());
      frame.setIconImage(MDIFrame.getInstance().getIconImage());
      frame.setVisible(true);
    } else JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print accounts movements"),
        JOptionPane.ERROR_MESSAGE
      );
  }


}


class AccountsFilterFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  AccountsFilterFrame adaptee;

  AccountsFilterFrame_printButton_actionAdapter(AccountsFilterFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}


