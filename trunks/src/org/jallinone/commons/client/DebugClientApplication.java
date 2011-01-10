package org.jallinone.commons.client;

import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.client.*;
import java.awt.Image;
import javax.swing.*;
import org.openswing.swing.internationalization.java.Language;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.internationalization.java.XMLResourcesFactory;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.message.receive.java.UserAuthorizationsResponse;
import netscape.javascript.JSObject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.startup.client.StartupFrame;
import java.awt.Color;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Application main class: this is the first class called.</p>
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
public class DebugClientApplication extends ClientApplet {


  /**
   * Method called by Java Web Start to init the application.
   */
  public static void main(String[] argv) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new DebugClientApplication();
      }
    });

  }


  public DebugClientApplication() {
    calledAsApplet = false;
    System.setProperty("SERVERURL","http://localhost:8081/jallinone/controller");
    initApplication();
  }


  /**
   * Method that initialize the client side application.
   */
  protected void initApplication() {
//    ClientUtils.setObjectSender(new HessianObjectSender());

    try {
			String lookAndFeelClassName = System.getProperty("LOOK_AND_FEEL_CLASS_NAME");
			if (lookAndFeelClassName!=null)
				ClientSettings.LOOK_AND_FEEL_CLASS_NAME = lookAndFeelClassName; // "com.jtattoo.plaf.aero.McWinLookAndFeel";
      Properties props = new Properties();
      props.put("logoString", "JAllInOne");
      props.put("backgroundColor", "232 232 232");
      String color = "220 220 220";
      props.put("disabledBackgroundColor", color);
      props.put("systemTextFont", "Arial PLAIN 11");
      props.put("controlTextFont", "Arial PLAIN 11");
      props.put("menuTextFont", "Arial PLAIN 11");
      props.put("userTextFont", "Arial PLAIN 11");
      props.put("subTextFont", "Arial PLAIN 11");
			if (lookAndFeelClassName!=null) {
				Class.forName(ClientSettings.LOOK_AND_FEEL_CLASS_NAME).getMethod(
						"setCurrentTheme", new Class[] {Properties.class}).invoke(null,
						new Object[] {props});
				UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
			}
    }
    catch (Throwable ex1) {
      ex1.printStackTrace();
    }

    loadDomains();

    // currently these are the supported languages...
    Hashtable xmlFiles = new Hashtable();
    xmlFiles.put("EN","Resources_en.xml");
    xmlFiles.put("IT","Resources_it.xml");
    xmlFiles.put("ES","Resources_es.xml");
    xmlFiles.put("PTBR","Resources_PTBR.xml");
    xmlFiles.put("DE","Resources_de.xml");
    xmlFiles.put("HR","Resources_hr.xml");
		xmlFiles.put("RU","Resources_ru.xml");

    // initialize internationalization settings, according to user language identifier...
    ClientSettings clientSettings = new ClientSettings(
        new XMLResourcesFactory(xmlFiles,false),
        domains,
        new ButtonsAuthorizations(),
        true
    );
    clientSettings.setLanguage("EN");

    // test if database is already created...
    VOResponse response = (VOResponse)ClientUtils.getData("databaseAlreadyExixts",new Object[0]);
    if (((Boolean)response.getVo()).booleanValue()) {

      Map loginInfo = new HashMap();
      loginInfo.put("username","ADMIN");
      loginInfo.put("password","admin");
      try {
        authenticateUser(loginInfo);
        loginSuccessful(loginInfo);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

    }
    else {
      // startup wizard will be showed...
      new StartupFrame(this);
    }
  }



  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {

    try {
//      UIManager.setLookAndFeel(new com.stefankrause.xplookandfeel.XPLookAndFeel());
//      UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());
//      UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticLookAndFeel());
//      UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
//      UIManager.setLookAndFeel(new net.infonode.gui.laf.InfoNodeLookAndFeel());
//      UIManager.setLookAndFeel(new com.birosoft.liquid.LiquidLookAndFeel());
//      UIManager.setLookAndFeel(new com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel());
//      UIManager.setLookAndFeel(new ch.randelshofer.quaqua.QuaquaLookAndFeel());

//      UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
//      UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
//      UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }


    // add user roles domain...
    Domain rolesDomain = new Domain("USERROLES");
    Iterator it = authorizations.getUserRoles().keySet().iterator();
    Object progressiveSYS04 = null;
    while(it.hasNext()) {
      progressiveSYS04 = new BigDecimal(it.next().toString());
      rolesDomain.addDomainPair(progressiveSYS04,authorizations.getUserRoles().get(progressiveSYS04).toString());
    }
    domains.put(
      rolesDomain.getDomainId(),
      rolesDomain
    );

    // add username panel to the status panel...
    GenericStatusPanel userPanel = new GenericStatusPanel();
    userPanel.setColumns(12);
    MDIFrame.addStatusComponent(userPanel);

    // add the clock panel to the status panel...
    userPanel.setText(username);
    MDIFrame.addStatusComponent(new Clock());
    frame.setSize(1280,1024);
  }


  /**
   * @see JFrame getExtendedState method
   */
  public int getExtendedState() {
    return JFrame.NORMAL;
  }


}
