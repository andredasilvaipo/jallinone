package org.jallinone.startup.client;

import javax.swing.*;
import java.awt.*;
import org.openswing.swing.util.client.ClientUtils;
import java.awt.event.*;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.startup.java.DbConnVO;
import org.openswing.swing.permissions.client.LoginDialog;
import org.jallinone.commons.client.ClientApplet;
import org.openswing.swing.util.client.ClientSettings;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Wizard frame used to setup the database connection.</p>
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
public class StartupFrame extends JFrame {
  JPanel northPanel = new JPanel();
  JPanel mainPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  JLabel titleLabel = new JLabel();
  JPanel introPanel = new JPanel();
  JPanel dbPanel = new JPanel();
  JPanel appSetupPanel = new JPanel();
  JPanel endPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel imagePanel = new JPanel() {

    private Image img = ClientUtils.getImage("setup.jpg");

    public void paint(Graphics g) {
      super.paint(g);
      g.drawImage(img,0,0,StartupFrame.this);
    }

  };
  JPanel textPanel = new JPanel();
  JLabel textLabel = new JLabel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel text2Label = new JLabel();
  JLabel text3Label = new JLabel();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton dxButton = new JButton();

  private int pos = 0;
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel dbTypeLabel = new JLabel();
  JComboBox dbTypeComboBox = new JComboBox();
  JPanel dbTypePanel = new JPanel();
  CardLayout cardLayout2 = new CardLayout();
  JPanel osmPanel = new JPanel();
  JPanel otherDbPanel = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JLabel hostLabel = new JLabel();
  JTextField hostTF = new JTextField();
  JLabel sidLabel = new JLabel();
  JTextField sidTF = new JTextField();
  JLabel portLabel = new JLabel();
  JTextField portTF = new JTextField();
  JLabel usernameLabel = new JLabel();
  JTextField usernameTF = new JTextField();
  JLabel passwdLabel = new JLabel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  JLabel driverLabel = new JLabel();
  JTextField driverTF = new JTextField();
  JLabel urlLabel = new JLabel();
  JTextField urlTF = new JTextField();
  JLabel otherUsernameLabel = new JLabel();
  JTextField otherUsernameTF = new JTextField();
  JLabel otherPasswdLabel = new JLabel();
  JPasswordField otherPasswdTF = new JPasswordField();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  JLabel companyCodeLabel = new JLabel();
  JLabel introLabel = new JLabel();
  JLabel intro2Label = new JLabel();
  JTextField companyCodeTF = new JTextField();
  JLabel maxCompanyCodeLabel = new JLabel();
  JLabel companyDescrLabel = new JLabel();
  JTextField companyDescrTF = new JTextField();
  JLabel maxCompanyDescrLabel = new JLabel();
  JLabel languageCodeLabel = new JLabel();
  JTextField languageCodeTF = new JTextField();
  JLabel maxLangCodeLabel = new JLabel();
  JLabel languageDescrLabel = new JLabel();
  JTextField languageDescrTF = new JTextField();
  JLabel maxLangDescrLabel = new JLabel();
  JLabel clientLangCodeLabel = new JLabel();
  JComboBox clientLanguageCodeComboBox = new JComboBox();

  private ClientApplet clientApplet = null;
  JButton exitButton = new JButton();
  JLabel paswdLabel = new JLabel();
  JPasswordField adminPasswdTF = new JPasswordField();
  JLabel maxAdminPasswdLabel = new JLabel();
  GridBagLayout gridBagLayout7 = new GridBagLayout();
  JLabel endLabel = new JLabel();
  JLabel passwordLabel = new JLabel();
  JPasswordField passwordTF = new JPasswordField();
  JButton sxButton = new JButton();
  JLabel currCodeLabel = new JLabel();
  JTextField currencyCodeTF = new JTextField();
  JLabel maxCurrCodeLabel = new JLabel();
  JLabel decSymLabel = new JLabel();
  JLabel thSymLabel = new JLabel();
  JLabel decLabel = new JLabel();
  JLabel symbLabel = new JLabel();
  JTextField decSymTF = new JTextField();
  JTextField thTF = new JTextField();
  JTextField decTF = new JTextField();
  JTextField symbTF = new JTextField();
  JLabel maxDecSymLabel = new JLabel();
  JLabel maxThLabel = new JLabel();
  JLabel maxCurrSymLabel = new JLabel();


  public StartupFrame(ClientApplet clientApplet) {
    setIconImage(ClientUtils.getImage(ClientSettings.ICON_FILENAME));
    this.clientApplet = clientApplet;
    try {
      jbInit();
      setSize(650,500);
      setLocation(
         (Toolkit.getDefaultToolkit().getScreenSize().width-getSize().width)/2,
         (Toolkit.getDefaultToolkit().getScreenSize().height-getSize().height)/2
      );

      dbTypeComboBox.addItem("Oracle");
      dbTypeComboBox.addItem("MS SqlServer");
      dbTypeComboBox.addItem("My SQL");
      dbTypeComboBox.addItem("Other Database");
      dbTypeComboBox.setSelectedIndex(0);

      clientLanguageCodeComboBox.addItem("English");
      clientLanguageCodeComboBox.addItem("Italiano");
      clientLanguageCodeComboBox.setSelectedIndex(0);

      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.setTitle("Application Setup");
    mainPanel.setLayout(cardLayout1);
    titleLabel.setFont(new java.awt.Font("Dialog", 1, 20));
    titleLabel.setText("J-All-in-one-Setup");
    introPanel.setLayout(gridBagLayout1);
    textLabel.setText("This wizard will help you to setup this application.");
    textPanel.setLayout(gridBagLayout2);
    text2Label.setText("Before to continue you have to prepare ");
    text3Label.setText("an empty database schema to host JAllInOne data.");
    imagePanel.setPreferredSize(new Dimension(180, 180));
    introPanel.setBorder(null);
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    dxButton.setText(">>");
    dxButton.addActionListener(new StartupFrame_dxButton_actionAdapter(this));
    dbPanel.setLayout(gridBagLayout3);
    dbTypeLabel.setText("Database Type");
    dbTypePanel.setLayout(cardLayout2);
    osmPanel.setLayout(gridBagLayout4);
    hostLabel.setText("Database Host");
    sidLabel.setText("Database Instance");
    portLabel.setText("Listener Port");
    usernameLabel.setText("Username");
    passwdLabel.setText("Password");
    hostTF.setText("");
    hostTF.setColumns(15);
    sidTF.setText("");
    sidTF.setColumns(15);
    portTF.setText("");
    portTF.setColumns(5);
    usernameTF.setText("");
    usernameTF.setColumns(15);
    dbTypeComboBox.addItemListener(new StartupFrame_dbTypeComboBox_itemAdapter(this));
    otherDbPanel.setLayout(gridBagLayout5);
    driverLabel.setText("JDBC Driver");
    driverTF.setText("");
    driverLabel.setText("URL");
    urlTF.setText("");
    driverLabel.setText("Username");
    otherUsernameTF.setText("");
    otherUsernameTF.setColumns(15);
    driverLabel.setRequestFocusEnabled(true);
    driverLabel.setText("JDBC Driver");
    otherPasswdTF.setColumns(17);
    urlLabel.setText("JDBC URL");
    otherUsernameLabel.setText("Username");
    otherPasswdLabel.setText("Password");
    appSetupPanel.setLayout(gridBagLayout6);
    companyCodeLabel.setText("Company Code");
    introLabel.setText("Now you can define one company and one language.");
    intro2Label.setText("When setup is finished, you can add other companies and other languages, " +
    "inside the application.");
    companyCodeTF.setText("");
    companyCodeTF.setColumns(15);
    companyCodeTF.addFocusListener(new StartupFrame_companyCodeTF_focusAdapter(this));
    maxCompanyCodeLabel.setText("(max 20 characters)");
    companyDescrLabel.setText("Company Description");
    companyDescrTF.setCaretPosition(0);
    companyDescrTF.setText("");
    companyDescrTF.setColumns(20);
    maxCompanyDescrLabel.setText("(max 255 characters)");
    languageCodeLabel.setText("Language Code");
    languageCodeTF.setText("EN");
    languageCodeTF.setColumns(15);
    maxLangCodeLabel.setText("(max 20 characters)");
    languageDescrLabel.setText("Language Description");
    languageDescrTF.setText("");
    maxLangDescrLabel.setText("(max 20 characters)");
    clientLangCodeLabel.setText("Client Language");
    exitButton.setText("Exit");
    exitButton.addActionListener(new StartupFrame_exitButton_actionAdapter(this));
    passwdLabel.setText("Admin Password");
    adminPasswdTF.setText("");
    adminPasswdTF.setColumns(17);
    maxAdminPasswdLabel.setText("(max 20 characters)");
    endPanel.setLayout(gridBagLayout7);
    endLabel.setText("Creating data structures: please wait...");
    passwordLabel.setText("Password");
    passwordTF.setColumns(17);
    sxButton.setEnabled(false);
    sxButton.setText("<<");
    sxButton.addActionListener(new StartupFrame_sxButton_actionAdapter(this));
    currCodeLabel.setText("Currency Code");
    currencyCodeTF.setText("EUR");
    currencyCodeTF.setColumns(10);
    currCodeLabel.setText("(max 20 characters)");
    decSymLabel.setText("Decimal Symbol");
    thSymLabel.setText("Thousand Symbol");
    decLabel.setText("Decimals");
    symbLabel.setText("Currency Symbol");
    decSymTF.setText(".");
    decSymTF.setColumns(10);
    currencyCodeTF.setColumns(10);
    thTF.setText(",");
    thTF.setColumns(10);
    decTF.setText("2");
    decTF.setColumns(10);
    symbTF.setText(new Character((char)8364).toString());
    symbTF.setColumns(10);
    maxCurrCodeLabel.setText("(20character)");
    maxDecSymLabel.setText("(1character)");
    maxThLabel.setText("(1character)");
    maxCurrSymLabel.setText("(3characters)");
    this.getContentPane().add(northPanel, BorderLayout.NORTH);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    northPanel.add(titleLabel, null);
    mainPanel.add(introPanel,   "INTRO");
    mainPanel.add(dbPanel,   "DB");
    dbPanel.add(dbTypeLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    dbPanel.add(dbTypeComboBox,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    dbPanel.add(dbTypePanel,  new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    dbTypePanel.add(osmPanel,  "OSM");
    osmPanel.add(hostLabel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(hostTF,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(portLabel,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(portTF,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(sidLabel,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(sidTF,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(usernameLabel,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(usernameTF,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(passwordLabel,  new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    osmPanel.add(passwordTF,  new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    dbTypePanel.add(otherDbPanel,  "OTHERDB");
    otherDbPanel.add(driverLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(driverTF,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(urlLabel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(urlTF,  new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(otherUsernameLabel,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(otherUsernameTF,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    otherDbPanel.add(otherPasswdLabel,     new GridBagConstraints(0, 3, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    otherDbPanel.add(otherPasswdTF,    new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    mainPanel.add(appSetupPanel,  "APPSETUP");
    appSetupPanel.add(companyCodeLabel,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    appSetupPanel.add(introLabel,       new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    appSetupPanel.add(intro2Label,      new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 20, 5), 0, 0));
    appSetupPanel.add(companyCodeTF,        new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 50), 0, 0));
    appSetupPanel.add(maxCompanyCodeLabel,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(companyDescrLabel,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(companyDescrTF,     new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxCompanyDescrLabel,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(languageCodeLabel,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(languageCodeTF,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxLangCodeLabel,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(languageDescrLabel,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(languageDescrTF,    new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxLangDescrLabel,    new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(clientLangCodeLabel,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    appSetupPanel.add(clientLanguageCodeComboBox,      new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 150, 0));
    appSetupPanel.add(passwdLabel,     new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(adminPasswdTF,      new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxAdminPasswdLabel,    new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(currCodeLabel,   new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(currencyCodeTF,   new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxCurrCodeLabel,  new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(decSymLabel,  new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(thSymLabel,  new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(decLabel,  new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(symbLabel,   new GridBagConstraints(0, 12, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(decSymTF,   new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    appSetupPanel.add(thTF,   new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    appSetupPanel.add(decTF,   new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    appSetupPanel.add(symbTF,        new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    appSetupPanel.add(maxDecSymLabel,  new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxThLabel,  new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    appSetupPanel.add(maxCurrSymLabel,  new GridBagConstraints(2, 12, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(endPanel,  "END");
    endPanel.add(endLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(exitButton, null);
    buttonsPanel.add(sxButton, null);
    buttonsPanel.add(dxButton, null);
    introPanel.add(imagePanel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    introPanel.add(textPanel,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 5), 0, 0));
    textPanel.add(textLabel,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    textPanel.add(text2Label,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    textPanel.add(text3Label,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    cardLayout1.show(mainPanel,"INTRO");
    cardLayout2.show(dbTypePanel,"OSM");
  }


  void dxButton_actionPerformed(ActionEvent e) {
    if (pos==0) {
      cardLayout1.show(mainPanel,"DB");
      pos++;
    }
    else if (pos==1) {
      sxButton.setEnabled(true);
      cardLayout1.show(mainPanel,"APPSETUP");
      pos++;
      sxButton.setEnabled(true);
    }
    else if (pos==2) {
      if (companyCodeTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a company code",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (companyDescrTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a company description (e.g. corporate name...)",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (languageCodeTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a language code (e.g. 'EN', 'IT', ...)",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (companyCodeTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a language description (e.g. 'English', 'Italiano', ...)",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (adminPasswdTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a password for administrator user",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }

      if (currencyCodeTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a currency code",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (symbTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a currency symbol",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (decSymTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a decimal symbol for the currency",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (thTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify a thousand symbol for the currency",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (decTF.getText().trim().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            "You must specify the number of decimal for the currency",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }

      pos++;
      cardLayout1.show(mainPanel,"END");
      dxButton.setEnabled(false);
      exitButton.setEnabled(false);
      sxButton.setEnabled(false);
      new Thread() {
        public void run() {
          if (!saveSettings()) {
            cardLayout1.show(mainPanel,"DB");
            pos = 1;
            dxButton.setEnabled(true);
            exitButton.setEnabled(true);
            return;
          }
          endLabel.setText("Database structure creation has successfully completed.");
          dxButton.setText("End");
          dxButton.setEnabled(true);
        }
      }.start();
    }
    else if (pos==3) {
      setVisible(false);
      // view the login window before viewing MDI frame...
      LoginDialog d = new LoginDialog(null,false,clientApplet);
      dispose();
    }
  }


  /**
   * Create config.xml file on server side and try to connect to it.
   */
  private DbConnVO createConn() {
    DbConnVO dbConnVO = new DbConnVO();
    if (dbTypeComboBox.getSelectedIndex()==0) {
      // Oracle database...
      dbConnVO.setDriverName("oracle.jdbc.driver.OracleDriver");
      dbConnVO.setPassword(passwordTF.getText().trim());
      dbConnVO.setUsername(usernameTF.getText().trim());
      dbConnVO.setUrl("jdbc:oracle:thin:@"+hostTF.getText().trim()+":"+portTF.getText().trim()+":"+sidTF.getText().trim());
    }
    else if (dbTypeComboBox.getSelectedIndex()==1) {
      // MS SqlServer database...
      dbConnVO.setDriverName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
      dbConnVO.setPassword(passwordTF.getText().trim());
      dbConnVO.setUsername(usernameTF.getText().trim());
      dbConnVO.setUrl("jdbc:microsoft:sqlserver://"+hostTF.getText().trim()+":"+portTF.getText().trim()+";DatabaseName="+sidTF.getText().trim()+";SelectMethod=cursor");
    }
    else if (dbTypeComboBox.getSelectedIndex()==2) {
      // MySQL database...
      dbConnVO.setDriverName("com.mysql.jdbc.Driver");
      dbConnVO.setPassword(passwordTF.getText().trim());
      dbConnVO.setUsername(usernameTF.getText().trim());
      dbConnVO.setUrl("jdbc:mysql://"+hostTF.getText().trim()+":"+portTF.getText().trim()+"/"+sidTF.getText().trim());
    }
    else if (dbTypeComboBox.getSelectedIndex()==3) {
      // other database...
      dbConnVO.setDriverName(driverTF.getText().trim());
      dbConnVO.setPassword(otherPasswdTF.getText().trim());
      dbConnVO.setUsername(otherUsernameTF.getText().trim());
      dbConnVO.setUrl(urlTF.getText().trim());
    }
    return dbConnVO;
  }


  /**
   * Create database structures and save initial data.
   */
  private boolean saveSettings() {
    DbConnVO dbConnVO = createConn();
    dbConnVO.setCompanyCode(companyCodeTF.getText().trim());
    dbConnVO.setCompanyDescription(companyDescrTF.getText().trim());
    dbConnVO.setLanguageCode(languageCodeTF.getText().trim());
    dbConnVO.setLanguageDescription(languageDescrTF.getText().trim());
    dbConnVO.setCurrencyCodeREG03(currencyCodeTF.getText().toUpperCase().trim());
    dbConnVO.setCurrencySymbolREG03(symbTF.getText().trim().length()>3?symbTF.getText().trim().substring(0,3):symbTF.getText().trim());
    dbConnVO.setDecimalsREG03(new BigDecimal(decTF.getText().trim()));
    dbConnVO.setDecimalSymbolREG03(decSymTF.getText().trim().substring(0,1));
    dbConnVO.setThousandSymbolREG03(thTF.getText().trim().substring(0,1));

    if (clientLanguageCodeComboBox.getSelectedIndex()==0)
      dbConnVO.setClientLanguageCode("EN");
    else
      dbConnVO.setClientLanguageCode("IT");
    dbConnVO.setAdminPassword(adminPasswdTF.getText().trim());
    Response response = ClientUtils.getData("createConfigFile",dbConnVO);
    if (response.isError()) {
      JOptionPane.showMessageDialog(
          this,
          response.getErrorMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
          );
      return false;
    }
    else
      return true;
  }


  void dbTypeComboBox_itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED) {
      if (dbTypeComboBox.getSelectedIndex()==0) {
        cardLayout2.show(dbTypePanel,"OSM");
        portTF.setText("1521");
      }
      else if (dbTypeComboBox.getSelectedIndex()==1) {
        cardLayout2.show(dbTypePanel,"OSM");
        portTF.setText("1434");
      }
      else if (dbTypeComboBox.getSelectedIndex()==2) {
        cardLayout2.show(dbTypePanel,"OSM");
        portTF.setText("3306");
      }
      else
        cardLayout2.show(dbTypePanel,"OTHERDB");
    }

  }


  void exitButton_actionPerformed(ActionEvent e) {
    clientApplet.stopApplication();
  }

  void sxButton_actionPerformed(ActionEvent e) {
    if (pos==2) {
      sxButton.setEnabled(false);
      cardLayout1.show(mainPanel,"DB");
      pos--;
    }
  }

  void companyCodeTF_focusLost(FocusEvent e) {
    companyCodeTF.setText(companyCodeTF.getText().toUpperCase().trim());
  }

}

class StartupFrame_dxButton_actionAdapter implements java.awt.event.ActionListener {
  StartupFrame adaptee;

  StartupFrame_dxButton_actionAdapter(StartupFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.dxButton_actionPerformed(e);
  }
}

class StartupFrame_dbTypeComboBox_itemAdapter implements java.awt.event.ItemListener {
  StartupFrame adaptee;

  StartupFrame_dbTypeComboBox_itemAdapter(StartupFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.dbTypeComboBox_itemStateChanged(e);
  }
}

class StartupFrame_exitButton_actionAdapter implements java.awt.event.ActionListener {
  StartupFrame adaptee;

  StartupFrame_exitButton_actionAdapter(StartupFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.exitButton_actionPerformed(e);
  }
}

class StartupFrame_sxButton_actionAdapter implements java.awt.event.ActionListener {
  StartupFrame adaptee;

  StartupFrame_sxButton_actionAdapter(StartupFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.sxButton_actionPerformed(e);
  }
}

class StartupFrame_companyCodeTF_focusAdapter extends java.awt.event.FocusAdapter {
  StartupFrame adaptee;

  StartupFrame_companyCodeTF_focusAdapter(StartupFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.companyCodeTF_focusLost(e);
  }
}
