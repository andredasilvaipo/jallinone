package org.jallinone.registers.bank.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.bank.java.*;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for bank detail.</p>
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
public class BankController extends FormController {

  /** pk */
  private BankVO bank = null;


  /** frame */
  private BanksFrame frame = null;


  public BankController(BanksFrame frame) {
    this.frame = frame;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return new VOResponse(bank);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response response = ClientUtils.getData("insertBank",newPersistentObject);
    if (!response.isError()) {
      bank = (BankVO)((VOResponse)response).getVo();
      frame.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response response = ClientUtils.getData("updateBank",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!response.isError()) {
      frame.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ArrayList vos = new ArrayList();
    vos.add(persistentObject);
    Response response = ClientUtils.getData("deleteBanks",vos);
    if (!response.isError()) {
      frame.reloadData();
    }
    return response;
  }


  /**
   * Set the primary key.
   * @param bank bank v.o.
   */
  public void setBank(BankVO bank) {
    this.bank = bank;
  }





}
