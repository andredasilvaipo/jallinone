package org.jallinone.warehouse.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.util.client.ClientUtils;
import java.awt.event.*;
import org.jallinone.warehouse.java.WarehouseVO;
import java.util.HashMap;
import org.openswing.swing.message.receive.java.Response;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouse inventory frame: it allows to view a report about
 * all company warehouses or about a specific warehouse.</p>
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
public class InventoryFrame extends InternalFrame {

  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  LabelControl labelCompanyCode = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  LabelControl labelWarehouseCode = new LabelControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();
  TextControl controlDescr = new TextControl();
  Form warehousePanel = new Form();


  public InventoryFrame() {
    try {
      jbInit();
      setSize(650,150);
      setMinimumSize(new Dimension(650,150));

      // warehouse lookup...
      wareDataLocator.setGridMethodName("loadWarehouses");
      wareDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(wareController);
      wareController.setForm(warehousePanel);
      wareController.setLookupDataLocator(wareDataLocator);
      wareController.setFrameTitle("warehouses");
      wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWAR01");
      wareController.addLookup2ParentLink("descriptionWAR01","descriptionWAR01");
			wareController.addLookup2ParentLink("progressiveHie02WAR01","progressiveHie02WAR01");
      wareController.setAllColumnVisible(false);
      wareController.setVisibleColumn("warehouseCodeWAR01", true);
      wareController.setVisibleColumn("descriptionWAR01", true);
      wareController.setVisibleColumn("addressWAR01", true);
      wareController.setVisibleColumn("cityWAR01", true);
      wareController.setVisibleColumn("zipWAR01", true);
      wareController.setVisibleColumn("provinceWAR01", true);
      wareController.setVisibleColumn("countryWAR01", true);
      wareController.setPreferredWidthColumn("descriptionWAR01",200);
      wareController.setFramePreferedSize(new Dimension(750,500));
      wareController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          WarehouseVO vo = (WarehouseVO)controlWarehouseCode.getLookupController().getLookupVO();
          controlDescr.setText(vo.getDescriptionWAR01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("WAR03_INVENTORY");
      Domain domain = new Domain("DOMAIN_WAR03_INVENTORY");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "WAR03_INVENTORY",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);


      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    warehousePanel.setVOClassName("org.jallinone.warehouse.java.WarehouseVO");
    warehousePanel.setFunctionId("WAR03_INVENTORY");

    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new InventoryFrame_printButton_actionAdapter(this));

    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse inventory"));
    labelWarehouseCode.setText("warehouseCodeWAR01");

    controlWarehouseCode.setAttributeName("warehouseCodeWAR01");
    controlWarehouseCode.setMaxCharacters(20);
    controlDescr.setAttributeName("descriptionWAR01");
    controlDescr.setEnabled(false);
    labelCompanyCode.setText("companyCodeSys01WAR01");
    controlCompaniesCombo.setAttributeName("companyCodeSys01WAR01");
    this.getContentPane().add(warehousePanel,  BorderLayout.CENTER);
    warehousePanel.setLayout(gridBagLayout1);
    warehousePanel.add(labelCompanyCode,               new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehousePanel.add(controlCompaniesCombo,                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
    warehousePanel.add(labelWarehouseCode,          new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehousePanel.add(controlWarehouseCode,               new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
    warehousePanel.add(controlDescr,            new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    warehousePanel.add(printButton,            new GridBagConstraints(0, 2, 3, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

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

    HashMap params = new HashMap();
    params.put("COMPANY_CODE",controlCompaniesCombo.getValue());
    if (controlWarehouseCode.getValue()!=null)
      params.put("WAREHOUSE_CODE"," and WAREHOUSE_CODE_WAR01='"+controlWarehouseCode.getValue()+"'");
    else
      params.remove("WAREHOUSE_CODE");

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
    map.put(ApplicationConsts.FUNCTION_CODE_SYS06,"WAR03_INVENTORY");
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
        ClientSettings.getInstance().getResources().getResource("print document"),
        JOptionPane.ERROR_MESSAGE
      );

  }




}

class InventoryFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  InventoryFrame adaptee;

  InventoryFrame_printButton_actionAdapter(InventoryFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
