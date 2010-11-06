package org.jallinone.sales.pos.client;

import javax.swing.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.*;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CompaniesComboControl;
import org.jallinone.sales.pos.client.PosFrame;
import java.util.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.system.client.UserParametersController;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.sales.customers.client.CustomersController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Dialog used to select a company for the POS.</p>
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
public class SaleDeskCompanySelDialog extends JDialog {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GenericButton buttonOk = new GenericButton();
  GenericButton buttonCancel = new GenericButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();


  public SaleDeskCompanySelDialog() {
    super(MDIFrame.getInstance(),ClientSettings.getInstance().getResources().getResource("company selection"),true);
    super.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
    setSize(300,160);
    try {
      jbInit();

      controlCompaniesCombo.setFunctionCode("DOC01_POS");

      // check if there exists exactly one company binded to current user...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList(controlCompaniesCombo.getFunctionCode());
      if (companiesList.size()==1) {
        checkValues((String)companiesList.get(0));
        this.dispose();
        return;
      }

      ClientUtils.centerDialog(MDIFrame.getInstance(),this);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonOk.setButtonBehavior(Consts.BUTTON_TEXT_ONLY);
    buttonOk.setText("ok");
    buttonOk.addActionListener(new SaleDeskCompanySelDialog_buttonOk_actionAdapter(this));
    buttonCancel.setButtonBehavior(Consts.BUTTON_TEXT_ONLY);
    buttonCancel.setText("cancel");
    buttonCancel.addActionListener(new SaleDeskCompanySelDialog_buttonCancel_actionAdapter(this));
    mainPanel.setLayout(gridBagLayout1);
    companyLabel.setLabel("companyCodeSYS01");
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(buttonOk, null);
    buttonsPanel.add(buttonCancel, null);
    mainPanel.add(companyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    mainPanel.add(controlCompaniesCombo, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 60, 0));
  }


  void buttonOk_actionPerformed(ActionEvent e) {
    if (controlCompaniesCombo.getValue()!=null) {
      String companyCodeSys01 = controlCompaniesCombo.getValue().toString();
      checkValues(companyCodeSys01);
    }
  }


  private void checkValues(String companyCodeSys01) {
    String customerCode = null;
    String warehouseCode = null;

    // retrieve default customer code...
    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CUSTOMER_CODE);
    Response res = ClientUtils.getData("loadUserParam",map);
    if (!res.isError()) {
      customerCode = (String)((VOResponse)res).getVo();
      if (customerCode==null) {
        OptionPane.showMessageDialog(MDIFrame.getInstance(),"you need to specifify the default customer for retail sale","Attention",JOptionPane.OK_OPTION);
        new UserParametersController();
        return;
      }
    }
    else
      return;

    // retrieve default warehouse...
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.WAREHOUSE_CODE);
    res = ClientUtils.getData("loadUserParam",map);
    if (!res.isError()) {
      warehouseCode = (String)((VOResponse)res).getVo();
      if (warehouseCode==null) {
        OptionPane.showMessageDialog(MDIFrame.getInstance(),"you need to specifify the default warehouse for retail sale","Attention",JOptionPane.OK_OPTION);
        new CustomersController();
        return;
      }
    }
    else
      return;


    PosFrame p = new PosFrame(companyCodeSys01,customerCode,warehouseCode);
    setVisible(false);
  }


  void buttonCancel_actionPerformed(ActionEvent e) {
    setVisible(false);
  }




}

class SaleDeskCompanySelDialog_buttonOk_actionAdapter implements java.awt.event.ActionListener {
  SaleDeskCompanySelDialog adaptee;

  SaleDeskCompanySelDialog_buttonOk_actionAdapter(SaleDeskCompanySelDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonOk_actionPerformed(e);
  }
}

class SaleDeskCompanySelDialog_buttonCancel_actionAdapter implements java.awt.event.ActionListener {
  SaleDeskCompanySelDialog adaptee;

  SaleDeskCompanySelDialog_buttonCancel_actionAdapter(SaleDeskCompanySelDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCancel_actionPerformed(e);
  }
}
