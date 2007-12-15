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
import org.jallinone.sales.customers.java.GridCustomerVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Filter frame used to view the statement of account filter frame.</p>
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
public class StatementOfAccountFilterFrame extends InternalFrame {

  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();

  LabelControl labelCustomer = new LabelControl();
  CodLookupControl controlCustomerCode = new CodLookupControl();
  TextControl controlName1 = new TextControl();
  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  Form custPanel = new Form();


  public StatementOfAccountFilterFrame() {
    try {
      jbInit();

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC06_CUST_ACCOUNT");
      Domain domain = new Domain("DOMAIN_ACC06_CUST_ACCOUNT");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC06_CUST_ACCOUNT",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.getComboBox().setSelectedIndex(0);


      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCustomerCode.setLookupController(customerController);
      controlCustomerCode.setControllerMethodName("getCustomersList");
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01REG04");
      customerController.addLookup2ParentLink("customerCodeSAL07","customerCodeSAL07");
      customerController.addLookup2ParentLink("progressiveREG04","progressiveREG04");
      customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      customerController.setAllColumnVisible(false);
      customerController.setVisibleColumn("companyCodeSys01REG04", true);
      customerController.setVisibleColumn("customerCodeSAL07", true);
      customerController.setVisibleColumn("name_1REG04", true);
      customerController.setVisibleColumn("name_2REG04", true);
      customerController.setVisibleColumn("cityREG04", true);
      customerController.setVisibleColumn("provinceREG04", true);
      customerController.setVisibleColumn("countryREG04", true);
      customerController.setVisibleColumn("taxCodeREG04", true);
      customerController.setHeaderColumnName("cityREG04", "city");
      customerController.setHeaderColumnName("provinceREG04", "prov");
      customerController.setHeaderColumnName("countryREG04", "country");
      customerController.setHeaderColumnName("taxCodeREG04", "taxCode");
      customerController.setPreferredWidthColumn("name_1REG04", 200);
      customerController.setPreferredWidthColumn("name_2REG04", 150);
      customerController.setFramePreferedSize(new Dimension(750,500));
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          customerController.setHeaderColumnName("name_1REG04", "corporateName1");
          customerController.setHeaderColumnName("name_2REG04", "corporateName2");
        }

        public void forceValidate() {}

      });


      setSize(500,140);
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    controlCustomerCode.setAttributeName("customerCodeSAL07");
    controlCustomerCode.setMaxCharacters(20);
    controlName1.setAttributeName("name_1REG04");
    controlName1.setEnabledOnEdit(false);
    controlName1.setEnabledOnInsert(false);
    controlName1.setEnabled(false);

    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print statement of account"));
    printButton.addActionListener(new StatementOfAccountFilterFrame_printButton_actionAdapter(this));
    controlCompaniesCombo.setAttributeName("companyCodeSys01REG04");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("print statement of account"));
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setLayout(gridBagLayout1);
    custPanel.setLayout(gridBagLayout2);
    labelCompanyCode.setText("companyCodeSYS01");
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(printButton, null);
    mainPanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    custPanel.add(labelCustomer,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCompaniesCombo,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    custPanel.add(controlCustomerCode,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    custPanel.add(controlName1,     new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(custPanel,      new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    custPanel.setFunctionId("ACC06_CUST_ACCOUNT");
    custPanel.setVOClassName("org.jallinone.sales.customers.java.GridCustomerVO");
  }


  void printButton_actionPerformed(ActionEvent e) {
    if (controlCompaniesCombo.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please select a company"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlCustomerCode.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set a customer code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    HashMap params = new HashMap();

    GridCustomerVO vo = (GridCustomerVO)custPanel.getVOModel().getValueObject();
    params.put("COMPANY_CODE",controlCompaniesCombo.getValue());
    params.put("PROGRESSIVE_REG04",vo.getProgressiveREG04());
    params.put("CUSTOMER_CODE",vo.getCustomerCodeSAL07());

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.FUNCTION_CODE_SYS06,"ACC06_CUST_ACCOUNT");
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
        ClientSettings.getInstance().getResources().getResource("print statement of account"),
        JOptionPane.ERROR_MESSAGE
      );
  }


}


class StatementOfAccountFilterFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  StatementOfAccountFilterFrame adaptee;

  StatementOfAccountFilterFrame_printButton_actionAdapter(StatementOfAccountFilterFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}


