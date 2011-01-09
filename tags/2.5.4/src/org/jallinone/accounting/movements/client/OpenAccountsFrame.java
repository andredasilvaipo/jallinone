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
 * <p>Description: Frame used to open patrimonial accounts,
 * by transporting patrimonial settlements of the previous year to the current one.
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
public class OpenAccountsFrame extends InternalFrame {

  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelRegDate = new LabelControl();
  DateControl controlFromDate = new DateControl();
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();


  LookupController oAccountController = new LookupController();
  LookupServerDataLocator oAccountDataLocator = new LookupServerDataLocator();

  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  JPanel closePanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TextControl controlODescr = new TextControl();
  CodLookupControl controlOCode = new CodLookupControl();
  LabelControl labelOAccount = new LabelControl();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  SaveButton oButton = new SaveButton();
  LabelControl labelC = new LabelControl();


  public OpenAccountsFrame() {
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
            // pre-set the code...
            HashMap map = new HashMap();
            map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
            map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.OPENING_ACCOUNT);
            Response res = ClientUtils.getData("loadUserParam",map);
            if (!res.isError()) {
              controlOCode.setValue( ((VOResponse)res).getVo() );
              oAccountController.forceValidate();
            }
          }
        }
      });


      // opening patrim. accounts lookup...
      controlOCode.setLookupController(oAccountController);
      controlOCode.setControllerMethodName("getAccounts");
      oAccountController.setLookupDataLocator(oAccountDataLocator);
      oAccountDataLocator.setGridMethodName("loadAccounts");
      oAccountDataLocator.setValidationMethodName("validateAccountCode");
      oAccountController.setFrameTitle("accounts");
      oAccountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      oAccountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC02");
//      oAccountController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      oAccountController.setFramePreferedSize(new Dimension(400,400));
      oAccountController.setAllColumnVisible(false);
      oAccountController.setVisibleColumn("accountCodeACC02",true);
      oAccountController.setVisibleColumn("descriptionSYS10",true);
      oAccountController.setPreferredWidthColumn("descriptionSYS10",290);
      oAccountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)oAccountController.getLookupVO();
          controlOCode.setValue(vo.getAccountCodeACC02());
          controlODescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          oAccountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          oAccountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      controlCompaniesCombo.getComboBox().setSelectedIndex(0);

      setSize(750,230);
      setMinimumSize(new Dimension(750,230));
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    controlCompaniesCombo.setAttributeName("companyCodeSys01ACC02");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("open accounts"));
    this.getContentPane().setLayout(gridBagLayout4);
    labelRegDate.setText("item date");
    labelCompanyCode.setText("companyCodeSYS01");
    topPanel.setBorder(BorderFactory.createEtchedBorder());
    topPanel.setLayout(gridBagLayout5);
    closePanel.setBorder(BorderFactory.createEtchedBorder());
    closePanel.setLayout(gridBagLayout3);
    controlODescr.setEnabledOnEdit(false);
    controlODescr.setEnabledOnInsert(false);
    controlODescr.setEnabled(false);
    controlOCode.setMaxCharacters(20);
    labelOAccount.setText("opening account");
    oButton.addActionListener(new OpenAccountsFrame_oButton_actionAdapter(this));
    labelC.setText("endorse patrimonial accounts settlements of the previous year to " +
    "the current one");
    this.getContentPane().add(topPanel,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelRegDate,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlCompaniesCombo,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    topPanel.add(controlFromDate,    new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(closePanel,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    closePanel.add(controlOCode,         new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    closePanel.add(controlODescr,         new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    closePanel.add(labelOAccount,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    closePanel.add(oButton,     new GridBagConstraints(0, 2, 3, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    closePanel.add(labelC,  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));

    controlFromDate.setValue(new Date());
  }


  void oButton_actionPerformed(ActionEvent e) {
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
    if (controlOCode.getValue()==null || controlOCode.getValue().equals("")) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set the account code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.DATE_FILTER,controlFromDate.getValue());
    map.put(ApplicationConsts.OPENING_ACCOUNT,controlOCode.getValue());
    Response res = ClientUtils.getData("openPAccounts",map);
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
          ClientSettings.getInstance().getResources().getResource("open accounts"),
          JOptionPane.WARNING_MESSAGE
      );


  }


}

class OpenAccountsFrame_oButton_actionAdapter implements java.awt.event.ActionListener {
  OpenAccountsFrame adaptee;

  OpenAccountsFrame_oButton_actionAdapter(OpenAccountsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.oButton_actionPerformed(e);
  }
}
