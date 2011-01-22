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
 * <p>Description: Form Controller used to insert a new accounting item, without vat usage.</p>
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
public class AccountingItemNoVatGridRowController extends CompanyFormController {

  /** detail frame */
  private AccountingItemNoVatFrame frame = null;

  /** grid content */
  private ArrayList vos = null;


  public AccountingItemNoVatGridRowController(AccountingItemNoVatFrame frame,ArrayList vos) {
    this.frame = frame;
    this.vos = vos;
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    JournalRowVO vo = (JournalRowVO)newPersistentObject;
    if (vo.getAccountCodeACC06()==null)
      return new ErrorResponse("you must specify a code");
    if (vo.getDebitAmountACC06()==null && vo.getCreditAmountACC06()==null)
      return new ErrorResponse("you must specify a debit or credit amount");


    vos.add(newPersistentObject);
    frame.getGrid().reloadData();
    updateTotals();

    return new VOResponse(newPersistentObject);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    JournalRowVO vo = (JournalRowVO)persistentObject;
    if (vo.getAccountCodeACC06()==null)
      return new ErrorResponse("you must specify a code");
    if (vo.getDebitAmountACC06()==null && vo.getCreditAmountACC06()==null)
      return new ErrorResponse("you must specify a debit or credit amount");

    frame.getGrid().reloadData();
    updateTotals();

    return new VOResponse(persistentObject);
  }


  /**
   * Update totals in frame.
   */
  private void updateTotals() {
    JournalRowVO vo = null;
    BigDecimal d = new BigDecimal(0);
    BigDecimal c = new BigDecimal(0);
    for(int i=0;i<vos.size();i++) {
      vo = (JournalRowVO)vos.get(i);
      if (vo.getDebitAmountACC06()!=null)
        d = d.add(vo.getDebitAmountACC06());
      if (vo.getCreditAmountACC06()!=null)
        c = c.add(vo.getCreditAmountACC06());
    }
    frame.getControlTotDebit().setValue(d);
    frame.getControlTotCredit().setValue(c);
    frame.getControlSbil().setValue(d.subtract(c));
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      frame.getControlCSA().getComboBox().setSelectedIndex(-1);
    }
    else if (currentMode==Consts.EDIT) {
      frame.updateCodes();
    }

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
