package org.jallinone.subjects.client;

import org.openswing.swing.form.client.Form;
import java.awt.*;
import javax.swing.border.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.jallinone.commons.client.CompaniesComboControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel related to a person (table REG04)
 * with SUBJECT_TYPE='P' (Contacts-people) or 'E' (Employee) or 'R' (Customers-private).</p>
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
public class PeoplePanel extends Form {


  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  TextControl controlCorpName1 = new TextControl();
  TextControl controlCorpName2 = new TextControl();
  TextControl controlAddress = new TextControl();
  TextControl controlCity = new TextControl();
  LabelControl labelZip = new LabelControl();
  TextControl controlZip = new TextControl();
  LabelControl labelProv = new LabelControl();
  TextControl controlProv = new TextControl();
  LabelControl labelCountry = new LabelControl();
  TextControl controlCountry = new TextControl();
  LabelControl labelVat = new LabelControl();
  TextControl controlVat = new TextControl();
  LabelControl labelCorpName1 = new LabelControl();
  LabelControl labelCorpName2 = new LabelControl();
  LabelControl labelAddress = new LabelControl();
  LabelControl labelCity = new LabelControl();
  LabelControl labelPhone = new LabelControl();
  LabelControl labelFax = new LabelControl();
  TextControl controlPhone = new TextControl();
  TextControl controlFax = new TextControl();
  LabelControl labelEmail = new LabelControl();
  TextControl controlEmail = new TextControl();
  TextControl controlWebSite = new TextControl();
  LabelControl labelWebSite = new LabelControl();
  LabelControl labelMobile = new LabelControl();
  TextControl controlMobile = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();
  LabelControl labelSex = new LabelControl();
  ComboBoxControl controlSex = new ComboBoxControl();
  LabelControl labelMaritalStatus = new LabelControl();
  TextControl controlMaritalStatus = new TextControl();
  LabelControl labelBirthplace = new LabelControl();
  LabelControl labelBirthday = new LabelControl();
  LabelControl labelNationality = new LabelControl();
  TextControl controlBirthplace = new TextControl();
  TextControl controlNationality = new TextControl();
  DateControl controlBirthday = new DateControl();

  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl companiesComboControl = new CompaniesComboControl();


  public PeoplePanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");

    companiesComboControl.setLinkLabel(companyLabel);
    companiesComboControl.setFunctionCode(this.getFunctionId());

    this.setVOClassName("org.jallinone.subjects.java.PeopleVO");
    titledBorder1 = new TitledBorder("");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("personal data"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setMinimumSize(new Dimension(700, 350));
    this.setLayout(gridBagLayout1);
    labelZip.setText("zip");
    labelProv.setText("prov");
    labelCountry.setText("country");
    labelVat.setText("socialsecnum");
    labelCorpName1.setText("firstname");
    labelCorpName2.setText("lastname");
    labelAddress.setText("address");
    labelCity.setText("city");
    labelPhone.setText("phone");
    labelFax.setText("fax");
    labelEmail.setText("email");
    labelWebSite.setText("webSite");
    labelMobile.setText("mobile");
    labelNote.setText("note");
    controlCorpName1.setAttributeName("name_1REG04");
    controlCorpName1.setLinkLabel(labelCorpName1);
    controlCorpName1.setRequired(true);
    controlCorpName2.setAttributeName("name_2REG04");
    controlCorpName2.setLinkLabel(labelCorpName2);
    controlCorpName2.setRequired(true);
    controlMobile.setAttributeName("mobileNumberREG04");
    controlMobile.setLinkLabel(labelMobile);
    controlAddress.setAttributeName("addressREG04");
    controlAddress.setLinkLabel(labelAddress);
    controlCity.setAttributeName("cityREG04");
    controlCity.setLinkLabel(labelCity);
    controlZip.setAttributeName("zipREG04");
    controlZip.setLinkLabel(labelZip);
    controlZip.setMaxCharacters(20);
    controlProv.setAttributeName("provinceREG04");
    controlProv.setLinkLabel(labelProv);
    controlProv.setMaxCharacters(20);
    controlProv.setTrimText(true);
    controlProv.setUpperCase(true);
    controlCountry.setAttributeName("countryREG04");
    controlCountry.setLinkLabel(labelCountry);
    controlCountry.setMaxCharacters(20);
    controlCountry.setTrimText(true);
    controlCountry.setUpperCase(true);
    controlVat.setAttributeName("taxCodeREG04");
    controlVat.setLinkLabel(labelVat);
    controlPhone.setAttributeName("phoneNumberREG04");
    controlPhone.setLinkLabel(labelPhone);
    controlFax.setAttributeName("faxNumberREG04");
    controlFax.setLinkLabel(labelFax);
    controlEmail.setAttributeName("emailAddressREG04");
    controlEmail.setLinkLabel(labelEmail);
    controlWebSite.setAttributeName("webSiteREG04");
    controlWebSite.setLinkLabel(labelWebSite);
    controlNote.setAttributeName("noteREG04");
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    labelSex.setText("sex");
    controlSex.setAttributeName("sexREG04");
    controlSex.setCanCopy(true);
    controlSex.setDomainId("SEX");
    controlSex.setLinkLabel(labelSex);
    controlSex.setRequired(true);
    labelMaritalStatus.setText("marital status");
    controlMaritalStatus.setAttributeName("maritalStatusREG04");
    controlMaritalStatus.setCanCopy(true);
    controlMaritalStatus.setLinkLabel(labelMaritalStatus);
    controlMaritalStatus.setMaxCharacters(20);
    labelBirthplace.setText("birthplace");
    labelBirthday.setText("birthday");
    labelNationality.setText("nationality");
    controlNationality.setAttributeName("nationalityREG04");
    controlNationality.setCanCopy(true);
    controlNationality.setLinkLabel(labelNationality);
    controlNationality.setRpadding(false);
    controlNationality.setTrimText(true);
    controlNationality.setUpperCase(true);
    controlBirthplace.setAttributeName("birthplaceREG04");
    controlBirthplace.setCanCopy(true);
    controlBirthplace.setLinkLabel(labelBirthplace);
    controlBirthday.setAttributeName("birthdayREG04");
    controlBirthday.setCanCopy(false);
    controlBirthday.setLinkLabel(labelBirthday);

    companiesComboControl.setAttributeName("companyCodeSys01REG04");
    companiesComboControl.setRequired(true);

    this.add(companyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(companiesComboControl, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    this.add(controlCorpName1,            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCorpName2,                new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlAddress,            new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCity,             new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlZip,            new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelProv,           new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlProv,             new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    this.add(labelCountry,           new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCountry,             new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    this.add(labelVat,            new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlVat,           new GridBagConstraints(3, 4, 3, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCorpName1,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelAddress,           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCity,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelPhone,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPhone,             new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelEmail,           new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlEmail,             new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlWebSite,             new GridBagConstraints(3, 6, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelNote,             new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlNote,            new GridBagConstraints(1, 9, 5, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelZip,        new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    this.add(controlFax,         new GridBagConstraints(3, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelWebSite,       new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelFax,       new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCorpName2,      new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelMobile,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlMobile,    new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelSex,     new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlSex,     new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    this.add(labelMaritalStatus,     new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlMaritalStatus,     new GridBagConstraints(5, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelBirthplace,    new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelBirthday,    new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelNationality,    new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlBirthplace,   new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlNationality,    new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlBirthday,      new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }
  public TextControl getControlCorpName1() {
    return controlCorpName1;
  }
  public TextControl getControlCorpName2() {
    return controlCorpName2;
  }

}
