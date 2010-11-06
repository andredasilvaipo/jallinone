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
 * <p>Description: Frame used to close the accounts.
 * It allows to:
 * 1) determine loss & profit for all economic accounts
 * 2) move loss/profit to the patrimonial account
 * 3) close all patrimonial accounts
 * </p>
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
public class CloseAccountsFrame extends InternalFrame {

  JPanel endorsePanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelRegDate = new LabelControl();
  DateControl controlFromDate = new DateControl();
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();

  LabelControl labelEAccount = new LabelControl();
  CodLookupControl controlECode = new CodLookupControl();
  TextControl controlEDescr = new TextControl();
  LookupController eAccountController = new LookupController();
  LookupServerDataLocator eAccountDataLocator = new LookupServerDataLocator();
  LookupController pAccountController = new LookupController();
  LookupServerDataLocator pAccountDataLocator = new LookupServerDataLocator();

  LookupController cAccountController = new LookupController();
  LookupServerDataLocator cAccountDataLocator = new LookupServerDataLocator();

  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  LabelControl labelPAccount = new LabelControl();
  CodLookupControl controlPCode = new CodLookupControl();
  TextControl controlPDescr = new TextControl();
  JPanel closePanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TextControl controlCDescr = new TextControl();
  CodLookupControl controlCCode = new CodLookupControl();
  LabelControl labelCAccount = new LabelControl();
  SaveButton epButton = new SaveButton();
  LabelControl labelEP = new LabelControl();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  SaveButton cButton = new SaveButton();
  LabelControl labelC = new LabelControl();


  public CloseAccountsFrame() {
    try {
      jbInit();

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC06_CLOSE_ACCOUNT");
      Domain domain = new Domain("DOMAIN_ACC06_CLOSE_ACCOUNT");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC06_CLOSE_ACCOUNT",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED) {
            // pre-set the three codes...
            HashMap map = new HashMap();
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
            Response res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              controlECode.setValue( ((VOResponse)res).getVo() );
              eAccountController.forceValidate();
            }
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.LOSSPROFIT_P_ACCOUNT);
            res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              controlPCode.setValue( ((VOResponse)res).getVo() );
              pAccountController.forceValidate();
            }
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CLOSING_ACCOUNT);
            res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              controlCCode.setValue( ((VOResponse)res).getVo() );
              cAccountController.forceValidate();
            }
          }
        }
      });

      // loss/profit to econ. endorsing account code lookup...
      controlECode.setLookupController(eAccountController);
      controlECode.setControllerMethodName("getAccounts");
      eAccountController.setLookupDataLocator(eAccountDataLocator);
      eAccountDataLocator.setGridMethodName("loadAccounts");
      eAccountDataLocator.setValidationMethodName("validateAccountCode");
      eAccountController.setFrameTitle("accounts");
      eAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      eAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
//      eAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      eAccountController.setFramePreferedSize(new Dimension(400,400));
      eAccountController.setAllColumnVisible(false);
      eAccountController.setVisibleColumn("accountCodeACC02",true);
      eAccountController.setVisibleColumn("descriptionSYS10",true);
      eAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      eAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)eAccountController.getLookupVO();
          controlECode.setValue(vo.getAccountCodeACC02());
          controlEDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          eAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          eAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // loss/profit to patrim. endorsing account code lookup...
      controlPCode.setLookupController(pAccountController);
      controlPCode.setControllerMethodName("getAccounts");
      pAccountController.setLookupDataLocator(pAccountDataLocator);
      pAccountDataLocator.setGridMethodName("loadAccounts");
      pAccountDataLocator.setValidationMethodName("validateAccountCode");
      pAccountController.setFrameTitle("accounts");
      pAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      pAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
//      pAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      pAccountController.setFramePreferedSize(new Dimension(400,400));
      pAccountController.setAllColumnVisible(false);
      pAccountController.setVisibleColumn("accountCodeACC02",true);
      pAccountController.setVisibleColumn("descriptionSYS10",true);
      pAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      pAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)pAccountController.getLookupVO();
          controlPCode.setValue(vo.getAccountCodeACC02());
          controlPDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          pAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          pAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // closing patrim. accounts lookup...
      controlCCode.setLookupController(cAccountController);
      controlCCode.setControllerMethodName("getAccounts");
      cAccountController.setLookupDataLocator(cAccountDataLocator);
      cAccountDataLocator.setGridMethodName("loadAccounts");
      cAccountDataLocator.setValidationMethodName("validateAccountCode");
      cAccountController.setFrameTitle("accounts");
      cAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      cAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
//      cAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      cAccountController.setFramePreferedSize(new Dimension(400,400));
      cAccountController.setAllColumnVisible(false);
      cAccountController.setVisibleColumn("accountCodeACC02",true);
      cAccountController.setVisibleColumn("descriptionSYS10",true);
      cAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      cAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)cAccountController.getLookupVO();
          controlCCode.setValue(vo.getAccountCodeACC02());
          controlCDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          cAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          cAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });



      controlCompaniesCombo.getComboBox().setSelectedIndex(0);

      setSize(750,350);
      setMinimumSize(new Dimension(750,350));
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

//    controlECode.setAttributeName("accountCodeACC02");
    controlECode.setMaxCharacters(20);
//    controlEDescr.setAttributeName("descriptionSYS10");
    controlEDescr.setEnabledOnEdit(false);
    controlEDescr.setEnabledOnInsert(false);
    controlEDescr.setEnabled(false);

//    controlCCode.setAttributeName("accountCodeACC02");
//    controlCDescr.setAttributeName("descriptionSYS10");

    controlCompaniesCombo.setAttributeName("companyCodeSys01ACC02");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("close accounts"));
    this.getContentPane().setLayout(gridBagLayout4);
    endorsePanel.setLayout(gridBagLayout1);
    labelRegDate.setText("item date");
    labelCompanyCode.setText("companyCodeSYS01");
    labelEAccount.setText("loss/profit econ. account");
    endorsePanel.setBorder(BorderFactory.createEtchedBorder());
    topPanel.setBorder(BorderFactory.createEtchedBorder());
    topPanel.setLayout(gridBagLayout5);
    labelPAccount.setText("loss/profit patrim. account");
    controlPDescr.setEnabled(false);
    controlPDescr.setEnabledOnInsert(false);
    controlPDescr.setEnabledOnEdit(false);
    controlPCode.setMaxCharacters(20);
    closePanel.setBorder(BorderFactory.createEtchedBorder());
    closePanel.setLayout(gridBagLayout3);
    controlCDescr.setEnabledOnEdit(false);
    controlCDescr.setEnabledOnInsert(false);
    controlCDescr.setEnabled(false);
    controlCCode.setMaxCharacters(20);
    labelCAccount.setText("closing account");
    epButton.addActionListener(new CloseAccountsFrame_epButton_actionAdapter(this));
    labelEP.setText("endorse economic accounts to loss/profit economic account and to " +
    "loss/profit patrimonial account");
    cButton.addActionListener(new CloseAccountsFrame_cButton_actionAdapter(this));
    labelC.setText("close patrimonial accounts");
    this.getContentPane().add(endorsePanel,    new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelRegDate,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    endorsePanel.add(labelEAccount,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    topPanel.add(controlCompaniesCombo,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    topPanel.add(controlFromDate,       new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 80, 0));
    this.getContentPane().add(closePanel,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    endorsePanel.add(controlECode,         new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    endorsePanel.add(controlEDescr,          new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    endorsePanel.add(labelPAccount,            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    endorsePanel.add(controlPCode,        new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    endorsePanel.add(controlPDescr,      new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    endorsePanel.add(epButton,   new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    closePanel.add(controlCCode,         new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    closePanel.add(controlCDescr,         new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    closePanel.add(labelCAccount,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    closePanel.add(cButton,     new GridBagConstraints(0, 2, 3, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    closePanel.add(labelC,  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
    endorsePanel.add(labelEP,   new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));

    controlFromDate.setValue(new Date());
  }



  void epButton_actionPerformed(ActionEvent e) {
    if (controlFromDate.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set a date"),
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
    if (controlPCode.getValue()==null || controlPCode.getValue().equals("")) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set the patrimonial account code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlECode.getValue()==null || controlECode.getValue().equals("")) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set the economic account code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.DATE_FILTER,controlFromDate.getValue());
    map.put(ApplicationConsts.LOSSPROFIT_E_ACCOUNT,controlECode.getValue());
    map.put(ApplicationConsts.LOSSPROFIT_P_ACCOUNT,controlPCode.getValue());
    Response res = ClientUtils.getData("endorseEAccounts",map);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("operation completed"),
          ClientSettings.getInstance().getResources().getResource("endorse loss/profit to patrim. account"),
          JOptionPane.WARNING_MESSAGE
      );
  }


  void cButton_actionPerformed(ActionEvent e) {
    if (controlFromDate.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set a date"),
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
    if (controlCCode.getValue()==null || controlCCode.getValue().equals("")) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set the account code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (JOptionPane.showConfirmDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("after closing accounts you will not be able to move settlements to new year. Continue?"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
    )==JOptionPane.NO_OPTION) {
      return;
    }

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.DATE_FILTER,controlFromDate.getValue());
    map.put(ApplicationConsts.CLOSING_ACCOUNT,controlCCode.getValue());
    Response res = ClientUtils.getData("closePAccounts",map);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("operation completed"),
          ClientSettings.getInstance().getResources().getResource("close patrim. accounts"),
          JOptionPane.WARNING_MESSAGE
      );


  }


}


class CloseAccountsFrame_epButton_actionAdapter implements java.awt.event.ActionListener {
  CloseAccountsFrame adaptee;

  CloseAccountsFrame_epButton_actionAdapter(CloseAccountsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.epButton_actionPerformed(e);
  }
}

class CloseAccountsFrame_cButton_actionAdapter implements java.awt.event.ActionListener {
  CloseAccountsFrame adaptee;

  CloseAccountsFrame_cButton_actionAdapter(CloseAccountsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cButton_actionPerformed(e);
  }
}
