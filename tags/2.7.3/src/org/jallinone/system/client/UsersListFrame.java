package org.jallinone.system.client;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.math.BigDecimal;
import org.openswing.swing.server.Action;
import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.java.ServerGridDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Internal frame that shows the list of connected users.</p>
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
public class UsersListFrame extends InternalFrame {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  GridControl grid = new GridControl();
  TextColumn colUsername = new TextColumn();
  TextColumn colName1 = new TextColumn();
  TextColumn colName2 = new TextColumn();
  ServerGridDataLocator dataLocator = new ServerGridDataLocator();


  public UsersListFrame() {
    try {
      jbInit();
      setSize(430,300);
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("connected users"));
    grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colName1.setPreferredWidth(150);
    colName2.setPreferredWidth(150);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    colUsername.setColumnName("attributeNameS0");
    colUsername.setHeaderColumnName("usernameSYS03");
    colName1.setColumnName("attributeNameS1");
    colName1.setHeaderColumnName("firstname");
    colName2.setColumnName("attributeNameS2");
    colName2.setHeaderColumnName("lastname");
    grid.setGridDataLocator(dataLocator);
    dataLocator.setServerMethodName("usersList");
    grid.getColumnContainer().add(colUsername, null);
    grid.getColumnContainer().add(colName1, null);
    grid.getColumnContainer().add(colName2, null);
  }

}
