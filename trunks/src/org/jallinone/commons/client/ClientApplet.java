package org.jallinone.commons.client;

import java.math.BigDecimal;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.jallinone.commons.java.*;
import org.jallinone.startup.client.*;
import org.jallinone.system.gridmanager.client.*;
import org.jallinone.system.java.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.permissions.client.*;
import org.openswing.swing.permissions.java.*;
import org.openswing.swing.table.profiles.client.*;
import org.openswing.swing.util.client.*;
import netscape.javascript.*;
//import org.openswing.swing.util.client.HessianObjectSender;


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
public class ClientApplet extends ClientUtils implements MDIController,LoginController {

  /** application client facade */
  private ApplicationClientFacade clientFacade = new ApplicationClientFacade(this);

  /** collection of application domains, i.e. collection of enumeration of constant values (e.g. yes/no, male/female, ...) */
  protected Hashtable domains = new Hashtable();

  /** username */
  protected String username = null;

  /** flag used in stopApplicationMethod to correctly close the application */
  protected boolean calledAsApplet = true;

  /** user authorizations */
  protected ApplicationParametersVO authorizations = null;

  /** login dialog */
  private LoginDialog loginDialog = null;


  /**
   * Method called by the browser to init the application, when it's executed as an applet.
   */
  public void start() {
    calledAsApplet = true;
    initApplication();
  }


  /**
   * Method that initialize the client side application.
   */
  protected void initApplication() {
//    ClientUtils.setObjectSender(new HessianObjectSender());

//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel";

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
//        false
        true
    );
    clientSettings.setLanguage("EN");


    // test if database is already created...
    VOResponse response = null;
    try {
      response = (VOResponse) ClientUtils.getData("databaseAlreadyExixts",new Object[0]);
    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
    if (response.isError()) {
      JOptionPane.showMessageDialog(null,response.getErrorMessage(),"Error on checking database",JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (((Boolean)response.getVo()).booleanValue()) {
      // view the login window before viewing MDI frame...
      //loginDialog = new LoginDialog(null,false,this);
      viewLoginDialog(null,false);
    }
    else {
      // startup wizard will be showed...
      new StartupFrame(this);
    }
  }


  protected void loadDomains() {
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
    languagesDomain.addDomainPair("ES","spanish");
    languagesDomain.addDomainPair("PTBR","brazilian");
		languagesDomain.addDomainPair("DE","german");
		languagesDomain.addDomainPair("HR","croatian");
		languagesDomain.addDomainPair("RU","russian");
    domains.put(
      languagesDomain.getDomainId(),
      languagesDomain
    );

    Domain subjectTypeDomain = new Domain("SUBJECT_TYPE");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_MY_COMPANY,"organization");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT,"organization contact");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER,"organization customer");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_PEOPLE_CONTACT,"private contact");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER,"private customer");
    subjectTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_SUPPLIER,"supplier");
    domains.put(
      subjectTypeDomain.getDomainId(),
      subjectTypeDomain
    );

    Domain customerTypeDomain = new Domain("CUSTOMER_TYPE");
    customerTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER,"organization");
    customerTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER,"private");
    domains.put(
      customerTypeDomain.getDomainId(),
      customerTypeDomain
    );

    Domain contactTypeDomain = new Domain("CONTACT_TYPE");
    contactTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT,"organization");
    contactTypeDomain.addDomainPair(ApplicationConsts.SUBJECT_PEOPLE_CONTACT,"private");
    domains.put(
      contactTypeDomain.getDomainId(),
      contactTypeDomain
    );

    Domain sexDomain = new Domain("SEX");
    sexDomain.addDomainPair("M","male");
    sexDomain.addDomainPair("F","female");
    domains.put(
      sexDomain.getDomainId(),
      sexDomain
    );


    Domain startDaysDomain = new Domain("START_DAY");
    startDaysDomain.addDomainPair(ApplicationConsts.START_DAY_END_MONTH,"end month");
    startDaysDomain.addDomainPair(ApplicationConsts.START_DAY_INVOICE_DATE,"invoice date");
    domains.put(
      startDaysDomain.getDomainId(),
      startDaysDomain
    );

    Domain refDomain = new Domain("REFERENCE_TYPE");
    refDomain.addDomainPair("P","phone");
    refDomain.addDomainPair("M","mobile");
    refDomain.addDomainPair("F","fax");
    refDomain.addDomainPair("W","webSite");
    refDomain.addDomainPair("E","email");
    refDomain.addDomainPair("I","officeNr");
    refDomain.addDomainPair("O","otherRef");
    domains.put(
      refDomain.getDomainId(),
      refDomain
    );

    Domain purchaseDocStateDomain = new Domain("DOC06_STATES");
    purchaseDocStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    purchaseDocStateDomain.addDomainPair(ApplicationConsts.HEADER_BLOCKED,"header blocked");
    purchaseDocStateDomain.addDomainPair(ApplicationConsts.CONFIRMED,"confirmed");
    purchaseDocStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    domains.put(
      purchaseDocStateDomain.getDomainId(),
      purchaseDocStateDomain
    );

    Domain deliveryNotesStateDomain = new Domain("DOC08_STATES");
    deliveryNotesStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    deliveryNotesStateDomain.addDomainPair(ApplicationConsts.HEADER_BLOCKED,"header blocked");
    deliveryNotesStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    domains.put(
      deliveryNotesStateDomain.getDomainId(),
      deliveryNotesStateDomain
    );

    Domain docTypeDomain = new Domain("DOC_TYPE");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE,"purchase invoice");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE,"purchase invoice from delivery notes");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE,"purchase invoice from purchase document");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE,"debiting note");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_GENERIC_INVOICE,"purchase generic document");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_DOC_TYPE,"sale invoice");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE,"sale invoice from delivery notes");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE,"sale invoice from sale document");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE,"credit note");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_GENERIC_INVOICE,"sale generic document");
    domains.put(
      docTypeDomain.getDomainId(),
      docTypeDomain
    );

    Domain warItemTypeDomain = new Domain("WAR_ITEM_TYPE");
    warItemTypeDomain.addDomainPair(ApplicationConsts.ITEM_GOOD,"good item");
    warItemTypeDomain.addDomainPair(ApplicationConsts.ITEM_DAMAGED,"damaged item");
    domains.put(
      warItemTypeDomain.getDomainId(),
      warItemTypeDomain
    );

    Domain warQtySignDomain = new Domain("WAR_QTY_SIGN");
    warQtySignDomain.addDomainPair(ApplicationConsts.QTY_SIGN_PLUS,"add");
    warQtySignDomain.addDomainPair(ApplicationConsts.QTY_SIGN_MINUS,"substract");
    domains.put(
      warQtySignDomain.getDomainId(),
      warQtySignDomain
    );

    Domain saleDocStateDomain = new Domain("DOC01_STATES");
    saleDocStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    saleDocStateDomain.addDomainPair(ApplicationConsts.HEADER_BLOCKED,"header blocked");
    saleDocStateDomain.addDomainPair(ApplicationConsts.CONFIRMED,"confirmed");
    saleDocStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    domains.put(
      saleDocStateDomain.getDomainId(),
      saleDocStateDomain
    );

    Domain saleDocTypeDomain = new Domain("SALE_DOC_TYPE");
    saleDocTypeDomain.addDomainPair(ApplicationConsts.SALE_ORDER_DOC_TYPE,"sale order");
    saleDocTypeDomain.addDomainPair(ApplicationConsts.SALE_CONTRACT_DOC_TYPE,"sale contract");
    saleDocTypeDomain.addDomainPair(ApplicationConsts.SALE_DESK_DOC_TYPE,"desk selling");
    domains.put(
      saleDocTypeDomain.getDomainId(),
      saleDocTypeDomain
    );

    Domain invoiceDocTypeDomain = new Domain("INVOICE_TYPE");
    invoiceDocTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_DOC_TYPE,"manually created");
    invoiceDocTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE,"from deliv.note");
    invoiceDocTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE,"from sale doc.");
    domains.put(
      invoiceDocTypeDomain.getDomainId(),
      invoiceDocTypeDomain
    );

    Domain purInvoiceDocTypeDomain = new Domain("PURCHASE_INVOICE_TYPE");
    purInvoiceDocTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE,"manually created");
    purInvoiceDocTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE,"from deliv.note");
    purInvoiceDocTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE,"from purchase doc.");
    domains.put(
      purInvoiceDocTypeDomain.getDomainId(),
      purInvoiceDocTypeDomain
    );

    Domain accountTypeACC01Domain = new Domain("ACCOUNT_TYPE_ACC01");
    accountTypeACC01Domain.addDomainPair(ApplicationConsts.PATRIMONIAL_ACCOUNT,"patrimonial");
    accountTypeACC01Domain.addDomainPair(ApplicationConsts.ECONOMIC_ACCOUNT,"economic");
    domains.put(
      accountTypeACC01Domain.getDomainId(),
      accountTypeACC01Domain
    );

    Domain regTypeACC04Domain = new Domain("REGISTER_TYPE_ACC04");
    regTypeACC04Domain.addDomainPair(ApplicationConsts.VAT_REGISTER_SELLING,"selling");
    regTypeACC04Domain.addDomainPair(ApplicationConsts.VAT_REGISTER_RETAIL,"retail");
    regTypeACC04Domain.addDomainPair(ApplicationConsts.VAT_REGISTER_PURCHASE,"purchase");
    regTypeACC04Domain.addDomainPair(ApplicationConsts.VAT_REGISTER_INTRA_SELLING,"intracom. on selling");
    regTypeACC04Domain.addDomainPair(ApplicationConsts.VAT_REGISTER_INTRA_PURCHASE,"intracom. on purchase");
    domains.put(
      regTypeACC04Domain.getDomainId(),
      regTypeACC04Domain
    );

    Domain accountTypeACC02Domain = new Domain("ACCOUNT_TYPE_ACC02");
    accountTypeACC02Domain.addDomainPair(ApplicationConsts.DEBIT_ACCOUNT,"debits/proceedings");
    accountTypeACC02Domain.addDomainPair(ApplicationConsts.CREDIT_ACCOUNT,"credits/costs");
    domains.put(
      accountTypeACC02Domain.getDomainId(),
      accountTypeACC02Domain
    );


    Domain accountCodeTypeACC05Domain = new Domain("ACCOUNT_CODE_TYPE_ACC05");
    accountCodeTypeACC05Domain.addDomainPair(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT,"account");
    accountCodeTypeACC05Domain.addDomainPair(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER,"customer");
    accountCodeTypeACC05Domain.addDomainPair(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER,"supplier");
    domains.put(
      accountCodeTypeACC05Domain.getDomainId(),
      accountCodeTypeACC05Domain
    );

    Domain accountCodeTypeCSACC05Domain = new Domain("ACCOUNT_CODE_TYPE_CS_ACC05");
    accountCodeTypeCSACC05Domain.addDomainPair(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER,"customer");
    accountCodeTypeCSACC05Domain.addDomainPair(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER,"supplier");
    domains.put(
      accountCodeTypeCSACC05Domain.getDomainId(),
      accountCodeTypeCSACC05Domain
    );

    Domain dayOfWeekDomain = new Domain("DAY_OF_WEEK");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.SUNDAY),"sunday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.MONDAY),"monday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.TUESDAY),"tuesday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.WEDNESDAY),"wednesday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.THURSDAY),"thursday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.FRIDAY),"friday");
    dayOfWeekDomain.addDomainPair(String.valueOf(java.util.Calendar.SATURDAY),"saturday");
    domains.put(
      dayOfWeekDomain.getDomainId(),
      dayOfWeekDomain
    );

    Domain callOutStateDomain = new Domain("CALL_OUT_STATE");
    callOutStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    callOutStateDomain.addDomainPair(ApplicationConsts.HEADER_BLOCKED,"header blocked");
    callOutStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    callOutStateDomain.addDomainPair(ApplicationConsts.INVOICED,"invoiced");
    domains.put(
      callOutStateDomain.getDomainId(),
      callOutStateDomain
    );

    Domain subjectType2Domain = new Domain("SUBJECT_TYPE_2");
    subjectType2Domain.addDomainPair(ApplicationConsts.SUBJECT_ORGANIZATION,"organization");
    subjectType2Domain.addDomainPair(ApplicationConsts.SUBJECT_PEOPLE,"private");
    domains.put(
      subjectType2Domain.getDomainId(),
      subjectType2Domain
    );

    Domain actPriorityDomain = new Domain("ACTIVITY_PRIORITY");
    actPriorityDomain.addDomainPair(ApplicationConsts.PRIORITY_HIGHEST,"highest");
    actPriorityDomain.addDomainPair(ApplicationConsts.PRIORITY_HIGH,"high");
    actPriorityDomain.addDomainPair(ApplicationConsts.PRIORITY_NORMAL,"normal");
    actPriorityDomain.addDomainPair(ApplicationConsts.PRIORITY_LOW,"low");
    actPriorityDomain.addDomainPair(ApplicationConsts.PRIORITY_TRIVIAL,"trivial");
    domains.put(
      actPriorityDomain.getDomainId(),
      actPriorityDomain
    );

    Domain actTypeDomain = new Domain("ACTIVITY_TYPE");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_GENERIC_TASK,"generic task");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_APPOINTMENT,"appointment");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_CALL_OUT,"call-out");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_SEND_EMAIL,"send email");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_SEND_FAX,"send fax");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_MEETING,"meeting");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_PHONE_CALL,"telephone call");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_ABSENCE,"absence");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_HOLIDAY,"holiday");
    actTypeDomain.addDomainPair(ApplicationConsts.ACT_ILLNESS,"illness");
    domains.put(
      actTypeDomain.getDomainId(),
      actTypeDomain
    );

    Domain actTypeDomainNoCallOut = new Domain("ACTIVITY_TYPE_NO_CALL_OUT");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_GENERIC_TASK,"generic task");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_APPOINTMENT,"appointment");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_SEND_EMAIL,"send email");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_SEND_FAX,"send fax");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_MEETING,"meeting");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_PHONE_CALL,"telephone call");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_ABSENCE,"absence");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_HOLIDAY,"holiday");
    actTypeDomainNoCallOut.addDomainPair(ApplicationConsts.ACT_ILLNESS,"illness");
    domains.put(
      actTypeDomainNoCallOut.getDomainId(),
      actTypeDomainNoCallOut
    );

    Domain actStateDomain = new Domain("ACTIVITY_STATE");
    actStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    actStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    actStateDomain.addDomainPair(ApplicationConsts.INVOICED,"invoiced");
    domains.put(
      actStateDomain.getDomainId(),
      actStateDomain
    );

    Domain absenceTypeDomain = new Domain("ABSENCE_TYPE");
    absenceTypeDomain.addDomainPair(ApplicationConsts.ACT_ABSENCE,"absence");
    absenceTypeDomain.addDomainPair(ApplicationConsts.ACT_HOLIDAY,"holiday");
    absenceTypeDomain.addDomainPair(ApplicationConsts.ACT_ILLNESS,"illness");
    domains.put(
      absenceTypeDomain.getDomainId(),
      absenceTypeDomain
    );

    Domain propTypeDOC21Domain = new Domain("PROPERTY_TYPE_DOC21");
    propTypeDOC21Domain.addDomainPair(ApplicationConsts.TYPE_TEXT,"text");
    propTypeDOC21Domain.addDomainPair(ApplicationConsts.TYPE_NUM,"numeric");
    propTypeDOC21Domain.addDomainPair(ApplicationConsts.TYPE_DATE,"date");
    domains.put(
      propTypeDOC21Domain.getDomainId(),
      propTypeDOC21Domain
    );

    Domain manufactureTypeDomain = new Domain("MANUFACTURE_TYPE");
    manufactureTypeDomain.addDomainPair(ApplicationConsts.INTERNAL_MANUFACTURE,"internal");
    manufactureTypeDomain.addDomainPair(ApplicationConsts.EXTERNAL_MANUFACTURE,"external");
    domains.put(
      manufactureTypeDomain.getDomainId(),
      manufactureTypeDomain
    );

    Domain prodOrderStateDomain = new Domain("DOC22_STATES");
    prodOrderStateDomain.addDomainPair(ApplicationConsts.OPENED,"opened");
    prodOrderStateDomain.addDomainPair(ApplicationConsts.HEADER_BLOCKED,"header blocked");
    prodOrderStateDomain.addDomainPair(ApplicationConsts.CONFIRMED,"confirmed");
    prodOrderStateDomain.addDomainPair(ApplicationConsts.CLOSED,"closed");
    domains.put(
      prodOrderStateDomain.getDomainId(),
      prodOrderStateDomain
    );

    Domain colTypeDomain = new Domain("SYS22_COL_TYPES");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_NUM,"numeric");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_TEXT,"text");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_DATE,"date");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_ENUM,"enumeration");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_PROG,"progressive");
    colTypeDomain.addDomainPair(ApplicationConsts.TYPE_WHERE,"where param");
    domains.put(
      colTypeDomain.getDomainId(),
      colTypeDomain
    );


    Domain barcodeTypeDomain = new Domain("BARCODE_TYPES");
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_EAN13,ApplicationConsts.BAR_CODE_EAN13);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_EAN128,ApplicationConsts.BAR_CODE_EAN128);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_128,ApplicationConsts.BAR_CODE_128);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_128A,ApplicationConsts.BAR_CODE_128A);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_128B,ApplicationConsts.BAR_CODE_128B);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_128C,ApplicationConsts.BAR_CODE_128C);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_39E,ApplicationConsts.BAR_CODE_39E);
    barcodeTypeDomain.addDomainPair(ApplicationConsts.BAR_CODE_PDF417,ApplicationConsts.BAR_CODE_PDF417);
    domains.put(
      barcodeTypeDomain.getDomainId(),
      barcodeTypeDomain
    );

  }


  /**
   * @return client facade, invoked by the MDI Frame tree/menu
   */
  public ClientFacade getClientFacade() {
    return clientFacade;
  }


  /**
   * Method used to destroy application.
   */
  public void stopApplication() {
    ClientUtils.getData("closeApplication",Boolean.TRUE);

    if (calledAsApplet) {
      destroy();
      try {
        JSObject js = JSObject.getWindow(this);
        js.call("closeFrame", new Object[] {});
      }
      catch (Throwable ex) {
      }
    }
    else
      System.exit(0);
  }


  /**
   * Defines if application functions must be viewed inside a tree panel of MDI Frame.
   * @return <code>true</code> if application functions must be viewed inside a tree panel of MDI Frame, <code>false</code> no tree is viewed
   */
  public boolean viewFunctionsInTreePanel() {
    return true;
  }


  /**
   * Defines if application functions must be viewed in the menubar of MDI Frame.
   * @return <code>true</code> if application functions must be viewed in the menubar of MDI Frame, <code>false</code> otherwise
   */
  public boolean viewFunctionsInMenuBar() {
    return true;
  }


  /**
   * @return <code>true</code> if the MDI frame must show a login menu in the menubar, <code>false</code> no login menu item will be added
   */
  public boolean viewLoginInMenuBar() {
    return true;
  }


  /**
   * @return application title
   */
  public String getMDIFrameTitle() {
    return "JAllInOne";
  }


  /**
   * @return text to view in the about dialog window
   */
  public String getAboutText() {
    return
        "JAllInOne ERP/CRM Application ver. 2.5.8\n"+
        "\n"+
        "Copyright: Copyright (C) 2010 Mauro Carniel\n"+
        "Author: Mauro Carniel\n"+
        "Database release: "+ApplicationConsts.DB_VERSION+"\n\n"+
        "This application is free software; you can redistribute it and/or\n"+
        "modify it under the terms of the (LGPL) Lesser General Public\n"+
        "License as published by the Free Software Foundation;\n\n"+
        "                GNU LESSER GENERAL PUBLIC LICENSE\n"+
        "                   Version 2.1, February 1999\n\n"+
        "This application is distributed in the hope that it will be useful,\n"+
        "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n"+
        "Library General Public License for more details.\n";
  }


  /**
   * @return image name to view in the about dialog window
   */
  public String getAboutImage() {
    return "jAllInOne.jpg";
  }


  /**
   * @param parentFrame parent frame
   * @return a dialog window to logon the application; the method can return null if viewLoginInMenuBar returns false
   */
  public JDialog viewLoginDialog(JFrame parentFrame) {
    return viewLoginDialog(parentFrame,true);
  }


  /**
   * @param parentFrame parent frame
   * @return a dialog window to logon the application; the method can return null if viewLoginInMenuBar returns false
   */
  public JDialog viewLoginDialog(JFrame parentFrame,boolean changeLogin) {
    Properties supportedLanguageIds = new Properties();
    supportedLanguageIds.setProperty("EN","english");
    supportedLanguageIds.setProperty("IT","italian");
    supportedLanguageIds.setProperty("ES","spanish");
    supportedLanguageIds.setProperty("PTBR","brazilian");
		supportedLanguageIds.setProperty("DE","german");
		supportedLanguageIds.setProperty("HR","croatian");
		supportedLanguageIds.setProperty("RU","russian");
    String currentLanguageIdentifier = "EN";
    Locale locale = Locale.getDefault();
    if (supportedLanguageIds.containsKey(locale.getLanguage().toUpperCase()))
      currentLanguageIdentifier = locale.getLanguage().toUpperCase();

    loginDialog = new LoginDialog(
      parentFrame,
      changeLogin,
      this,
      "Logon",
      "Login",
      'L',
      "Exit",
      'E',
      "Store account",
      "JALLINONE",
      null,
      supportedLanguageIds,
      currentLanguageIdentifier
    );
    return loginDialog;
  }


  /**
   * @return maximum number of failed login
   */
  public int getMaxAttempts() {
    return 3;
  }


  /**
   * Method called by MDI Frame to authenticate the user.
   * @param loginInfo login information, like username, password, ...
   * @return <code>true</code> if user is correcly authenticated, <code>false</code> otherwise
   */
  public boolean authenticateUser(Map loginInfo) throws Exception {
    username = (String) loginInfo.get("username");
    String password = (String) loginInfo.get("password");
    if (username == null || password == null)
      return false;

    username = username.toUpperCase();
    loginInfo.put("username",username);
    loginInfo.put("password",password);

    Response response = ClientUtils.getData("login",new String[]{username,password});
    if (response.isError())
      throw new Exception(response.getErrorMessage());

    // user correcly authenticated: retrieve from the server response the language identifier...
    String languageId = ((TextResponse)response).getMessage();

    if (ClientSettings.getInstance().getResources()!=null) {
      // not first login: change user
      String currentLanguageId = ClientSettings.getInstance().getResources().getLanguageId();
      if (currentLanguageId!=null && !currentLanguageId.equals(languageId)) {
        ClientUtils.getData("changeLanguage", currentLanguageId);
        languageId = currentLanguageId;
      }
    }

    // retrieve user authorizations...
    response = ClientUtils.getData("getUserAuthorizations",new Object[0]);
    if (response.isError())
      throw new Exception(response.getErrorMessage());
    authorizations = (ApplicationParametersVO)((VOResponse)response).getVo();

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
        authorizations.getBa(),
//        false
        true
    );

    ClientSettings.GRID_ACTIVE_CELL_BACKGROUND = new Color(205,239,255);
    ClientSettings.GRID_SELECTION_BACKGROUND = new Color(195,229,254);
    ClientSettings.PERC_TREE_FOLDER = "folder3.gif";
    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.TREE_BACK = "treeback2.jpg";
//    ClientSettings.ICON_FILENAME = "appicon2.gif";
    ClientSettings.ICON_FILENAME = "appicon1.gif";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.LOCK_OFF="unlock.gif";
    ClientSettings.LOCK_ON="lock.gif";
    ClientSettings.FILTER_PANEL_ON_GRID = false;

//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";

    ClientSettings.GRID_PROFILE_MANAGER = new FileGridProfileManager();
    ClientSettings.ON_INVALID_CODE = LookupController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE;
    ClientSettings.FORCE_FOCUS_ON_LOOKUP_CONTROL = true;
    ClientSettings.ASK_BEFORE_CLOSE = true;
    ClientSettings.SHOW_FILTERING_CONDITIONS_IN_EXPORT = true;
    ClientSettings.SHOW_SORTING_ORDER = true;
    ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME = 1500;
    ClientSettings.SHOW_FRAME_TITLE_IN_EXPORT = true;
    ClientSettings.AS_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
    //ClientSettings.SHOW_TREE_MENU_ROOT = false;

    ClientSettings.GRID_PERMISSION_MANAGER = new JAIODbGridPermissions();
    ClientSettings.GRID_PERMISSION_MANAGER.setUsername(username.toUpperCase());

    ClientSettings.getInstance().setLanguage(languageId);

    return true;
  }


  /**
   * @see JFrame getExtendedState method
   */
  public int getExtendedState() {
    return JFrame.MAXIMIZED_BOTH;
  }


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {
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
  }


  /**
   * Method called by LoginDialog to notify the sucessful login.
   * @param loginInfo login information, like username, password, ...
   */
  public void loginSuccessful(Map loginInfo) {
    MDIFrame mdi = new MDIFrame(this);
  }


  /**
   * @return <code>true</code> if the MDI frame must show a change language menu in the menubar, <code>false</code> no change language menu item will be added
   */
  public boolean viewChangeLanguageInMenuBar() {
    return true;
  }


  /**
   * @return list of languages supported by the application
   */
  public ArrayList getLanguages() {
    Response response = ClientUtils.getData("getLanguages",new Object[0]);
    if (response.isError()) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          response.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
      return new ArrayList();
    }
    return new ArrayList(((VOListResponse)response).getRows()); // the list is composed of Language objects...
  }


  /**
   * @return application functions (ApplicationFunction objects), organized as a tree
   */
  public DefaultTreeModel getApplicationFunctions() {
    if (!authorizations.getLanguageId().equals(ClientSettings.getInstance().getResources().getLanguageId())) {
      // retrieve user authorizations...
      Response response = ClientUtils.getData("getUserAuthorizations",new Object[0]);
      if (response.isError()) {
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          response.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
        );
      }
      else
        authorizations = (ApplicationParametersVO)((VOResponse)response).getVo();
    }
    return authorizations.getAppMenu();
  }


  /**
   * @return user authorizations
   */
  public final ApplicationParametersVO getAuthorizations() {
    return authorizations;
  }


  /**
   * @return <code>true</code> if the MDI frame must show a panel in the bottom, containing last opened window icons, <code>false</code> no panel is showed
   */
  public boolean viewOpenedWindowIcons() {
    return true;
  }


  /**
   * @return username
   */
  public final String getUsername() {
    return username;
  }


  /**
   * @return <code>true</code> if the MDI frame must show the "File" menu in the menubar of the frame, <code>false</code> to hide it
   */
  public final boolean viewFileMenu() {
    return true;
  }


}
