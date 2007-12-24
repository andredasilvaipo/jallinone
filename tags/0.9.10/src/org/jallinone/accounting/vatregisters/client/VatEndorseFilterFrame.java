package org.jallinone.accounting.vatregisters.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Filter frame used to endorse vat from vat registers to tresury.</p>
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
public class VatEndorseFilterFrame extends InternalFrame {

  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelToDate = new LabelControl();
  DateControl controlToDate = new DateControl();
  GenericButton execButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();


  public VatEndorseFilterFrame() {
    try {
      jbInit();

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC04_VAT_ENDORSE");
      Domain domain = new Domain("DOMAIN_ACC04_VAT_ENDORSE");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC04_VAT_ENDORSE",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.getComboBox().setSelectedIndex(0);

      setSize(400,150);
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    execButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("vat endorsing"));
    execButton.addActionListener(new VatEndorseFilterFrame_execButton_actionAdapter(this));
    controlCompaniesCombo.setAttributeName("companyCodeSys01WAR01");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("vat endorsing"));
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setLayout(gridBagLayout1);
    labelToDate.setText("to date");
    labelCompanyCode.setText("companyCodeSys01WAR01");
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(execButton, null);
    mainPanel.add(labelCompanyCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelToDate,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCompaniesCombo,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlToDate,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    Calendar cal = Calendar.getInstance();
    cal.set(cal.DAY_OF_MONTH,1);
    cal.set(cal.MONTH,0);
    controlToDate.setValue(new Date());
  }


  void execButton_actionPerformed(ActionEvent e) {
    if (controlToDate.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("please set the date"),
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

    HashMap params = new HashMap();
    params.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    params.put(ApplicationConsts.DATE_FILTER,controlToDate.getValue());
    Response res = ClientUtils.getData("vatEndorse",params);
    if (!res.isError()) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        ClientSettings.getInstance().getResources().getResource("vat endorsing completed."),
        ClientSettings.getInstance().getResources().getResource("vat endorsing"),
        JOptionPane.INFORMATION_MESSAGE
      );
    } else
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("vat endorsing"),
        JOptionPane.ERROR_MESSAGE
      );
  }


}

class VatEndorseFilterFrame_execButton_actionAdapter implements java.awt.event.ActionListener {
  VatEndorseFilterFrame adaptee;

  VatEndorseFilterFrame_execButton_actionAdapter(VatEndorseFilterFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.execButton_actionPerformed(e);
  }
}


