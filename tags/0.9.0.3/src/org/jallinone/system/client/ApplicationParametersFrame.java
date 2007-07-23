package org.jallinone.system.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.system.companies.java.CompanyVO;
import java.util.ArrayList;
import org.jallinone.system.java.ApplicationParametersVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;
import java.awt.event.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define application parameters.</p>
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

public class ApplicationParametersFrame extends InternalFrame {


  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel topPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  Form mainPanel = new Form();
  BorderLayout borderLayout2 = new BorderLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  TextControl controlImagePath = new TextControl();
  LabelControl labelImagePath = new LabelControl();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  LabelControl labelDocPath = new LabelControl();
  TextControl controlDocPath = new TextControl();



  public ApplicationParametersFrame(ApplicationParametersController controller) {
    try {
      jbInit();

      mainPanel.setFormController(controller);


      setSize(450,180);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setVOClassName("org.jallinone.system.java.ApplicationParametersVO");
    mainPanel.setFunctionId("SYS11");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("application parameters"));
    this.getContentPane().setLayout(gridBagLayout3);
    topPanel.setLayout(gridBagLayout2);
    labelImagePath.setText("image repository path");


    labelDocPath.setText("document repository path");
    topPanel.add(buttonsPanel,         new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


    mainPanel.setEditButton(editButton1);
    mainPanel.setReloadButton(reloadButton1);
    mainPanel.setSaveButton(saveButton1);
    this.getContentPane().add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(editButton1,null);
    buttonsPanel.add(saveButton1,null);
    buttonsPanel.add(reloadButton1,null);
    this.getContentPane().add(mainPanel,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.setLayout(gridBagLayout1);


    controlImagePath.setAttributeName("imagePath");
    controlDocPath.setAttributeName("documentPath");

    mainPanel.add(controlImagePath,          new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelImagePath,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    mainPanel.add(labelDocPath,      new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDocPath,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));




  }


  public Form getMainPanel() {
    return mainPanel;
  }




}
