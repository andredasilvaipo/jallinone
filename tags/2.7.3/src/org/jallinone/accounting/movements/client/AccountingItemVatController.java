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
import org.jallinone.accounting.movements.java.JournalHeaderVO;
import java.math.BigDecimal;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.accounting.movements.java.JournalRowWithVatVO;
import org.jallinone.accounting.movements.java.JournalHeaderWithVatVO;
import org.jallinone.accounting.movements.java.VatRowVO;
import java.util.Calendar;


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
public class AccountingItemVatController extends CompanyFormController {

  /** detail frame */
  private AccountingItemVatFrame detailFrame = null;


  public AccountingItemVatController() {
    detailFrame = new AccountingItemVatFrame(this);
    MDIFrame.add(detailFrame,true);

    detailFrame.getHeaderPanel().insert();
    detailFrame.getInsertButton1().setEnabled(true);
    detailFrame.getcontrolCS().getComboBox().setSelectedIndex(-1);
//    detailFrame.getGridDetailPanel().setMode(Consts.READONLY);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    VOListTableModel gridModel = detailFrame.getGrid().getVOListTableModel();
    if (gridModel.getRowCount()<1) {
      return new ErrorResponse("you must insert al least one row");
    }

    JournalHeaderWithVatVO jhVO = (JournalHeaderWithVatVO)newPersistentObject;
    jhVO.getJournalRows().clear();
    JournalRowVO jrVO = null;
    JournalRowWithVatVO rowVatVO = null;
    VatRowVO vatVO = null;
    BigDecimal vatValue = null;
    BigDecimal totalValue = new BigDecimal(0);
    for(int i=0;i<gridModel.getRowCount();i++) {
      // create 3 rows for each vat row value object...
      rowVatVO = (JournalRowWithVatVO)gridModel.getObjectForRow(i);

      // 1. add taxable income to the accounting item...
      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(rowVatVO.getAccountCodeAcc02ACC06());
      jrVO.setAccountCodeACC06(rowVatVO.getAccountCodeACC06());
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

      if (jhVO.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER))
        jrVO.setCreditAmountACC06(rowVatVO.getTaxableIncome().setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));
      else
        jrVO.setDebitAmountACC06(rowVatVO.getTaxableIncome().setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jrVO.setDescriptionACC06(rowVatVO.getDescriptionACC06());
      jhVO.addJournalRow(jrVO);

      totalValue = totalValue.add(rowVatVO.getTaxableIncome().setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));

      // 2. prepare vat row for the specified register...
      vatVO = new VatRowVO();
      vatVO.setCompanyCodeSys01ACC07(jhVO.getCompanyCodeSys01ACC05());
      vatVO.setRegisterCodeAcc04ACC07(jhVO.getRegisterCodeACC04());
      vatVO.setTaxableIncomeACC07(rowVatVO.getTaxableIncome().setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));
      vatVO.setVatCodeACC07(rowVatVO.getVatCodeREG01());
      vatVO.setVatDateACC07(new java.sql.Date(System.currentTimeMillis()));
      vatVO.setVatDescriptionACC07(rowVatVO.getVatDescriptionREG01());
      vatValue = rowVatVO.getTaxableIncome().multiply( new BigDecimal(rowVatVO.getValueREG01().doubleValue()/100d*(100d-rowVatVO.getDeductibleREG01().doubleValue())/100d) ).setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP);
      vatVO.setVatValueACC07(vatValue);
      vatVO.setVatYearACC07(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));
      jhVO.addVat(vatVO);

      totalValue = totalValue.add(vatValue);

      // 3. add total vat value to the accounting item...
      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(jhVO.getAccountCodeAcc02ACC04());
      jrVO.setAccountCodeACC06(jhVO.getAccountCodeAcc02ACC04());
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
      if (jhVO.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER))
        jrVO.setCreditAmountACC06(vatValue);
      else
        jrVO.setDebitAmountACC06(vatValue);
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jhVO.addJournalRow(jrVO);

      // 4. add total credit/debit value to the accounting item...
      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      if (jhVO.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER)) {
        jrVO.setAccountCodeAcc02ACC06(jhVO.getCreditAccountCodeAcc02SAL07());
        jrVO.setAccountCodeACC06(jhVO.getCustomerCodeSAL07());
        jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);
        jrVO.setDebitAmountACC06(rowVatVO.getTaxableIncome().add(vatValue).setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));
      }
      else {
        jrVO.setAccountCodeAcc02ACC06(jhVO.getDebitAccountCodeAcc02PUR01());
        jrVO.setAccountCodeACC06(jhVO.getSupplierCodePUR01());
        jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);
        jrVO.setCreditAmountACC06(rowVatVO.getTaxableIncome().add(vatValue).setScale(detailFrame.getCurrencyDecimals(),BigDecimal.ROUND_HALF_UP));
      }
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jhVO.addJournalRow(jrVO);
    }
    jhVO.setTotalValue(totalValue);

    Response response = ClientUtils.getData("insertJournalItem",jhVO);
    if (!response.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(detailFrame),
          ClientSettings.getInstance().getResources().getResource("item created"),
          ClientSettings.getInstance().getResources().getResource("new item with vat"),
          JOptionPane.INFORMATION_MESSAGE
      );
      detailFrame.closeFrame();
    }
    return response;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    JournalHeaderVO vo = (JournalHeaderVO)persistentObject;
    vo.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
    vo.setItemYearACC05(new BigDecimal(new java.util.Date().getYear()+1900));
  }



}
