package org.jallinone.commons.client;

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
import org.jallinone.startup.client.StartupFrame;
import org.jallinone.system.java.ApplicationParametersVO;
import java.awt.Color;
//import org.openswing.swing.util.client.HessianObjectSender;


public class AdminClientApplication extends ClientApplication {
  public AdminClientApplication() {
  }

  public String getParameter(String p) {
    if (p.equals("SERVERURL"))
      return "http://localhost:8080/jAllInOne/controller";
    else return p;
  }


  /**
   * Method that initialize the client side application.
   */
  protected void initApplication() {
//    ClientUtils.setObjectSender(new HessianObjectSender());

    // define domains...
    Domain colTypeSYS12Domain = new Domain("COLUMN_TYPE");
    colTypeSYS12Domain.addDomainPair("S","text");
    colTypeSYS12Domain.addDomainPair("D","date");
    colTypeSYS12Domain.addDomainPair("N","numeric");
    domains.put(
      colTypeSYS12Domain.getDomainId(),
      colTypeSYS12Domain
    );

    Domain languagesDomain = new Domain("LANGUAGES");
    languagesDomain.addDomainPair("EN","english");
    languagesDomain.addDomainPair("IT","italian");
    languagesDomain.addDomainPair("ES","espanol");
		languagesDomain.addDomainPair("PTBR","brazilian");
		languagesDomain.addDomainPair("DE","german");
    domains.put(
      languagesDomain.getDomainId(),
      languagesDomain
    );

    HashMap map = new HashMap();
    map.put("username","ADMIN");
    map.put("password","admin");
    try {
      authenticateUser(map);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called by Java Web Start to init the application.
   */
  public static void main(String[] argv) {
    System.setProperty("SERVERURL","http://localhost:8080/jAllInOne/controller");
    AdminClientApplication a = new AdminClientApplication();
    try {
      MDIFrame m = new MDIFrame(a);
      m.setVisible(true);
    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
  }


}
