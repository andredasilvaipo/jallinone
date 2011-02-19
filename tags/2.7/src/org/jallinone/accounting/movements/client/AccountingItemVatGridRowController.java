package org.jallinone.accounting.movements.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.contacts.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.subjects.java.Subject;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import java.math.BigDecimal;
import java.awt.Color;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used to insert a new accounting item, with vat usage.</p>
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
public class AccountingItemVatGridRowController extends CompanyFormController {

  /** detail frame */
  private AccountingItemVatFrame frame = null;

  /** grid content */
  private ArrayList vos = null;


  public AccountingItemVatGridRowController(AccountingItemVatFrame frame,ArrayList vos) {
    this.frame = frame;
    this.vos = vos;
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    vos.add(newPersistentObject);
    frame.getGrid().reloadData();

    return new VOResponse(newPersistentObject);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    frame.getGrid().reloadData();

    return new VOResponse(persistentObject);
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {

  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    JournalRowVO vo = (JournalRowVO)persistentObject;
    vo.setItemYearAcc05ACC06(new BigDecimal(new java.util.Date().getYear()+1900));
  }


  public void afterInsertData() {
    frame.getInsertButton1().setEnabled(true);
  }


}
