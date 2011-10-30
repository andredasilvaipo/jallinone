package org.jallinone.accounting.movements.java;

import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to create an item in the journal.</p>
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
public class JournalHeaderVO extends ValueObjectImpl {


  private String companyCodeSys01ACC05;
  private String accountingMotiveCodeAcc03ACC05;
  private java.math.BigDecimal progressiveACC05;
  private java.math.BigDecimal itemYearACC05;
  private java.sql.Date itemDateACC05;
  private ArrayList journalRows = new ArrayList();
  private String descriptionACC05;
  private String motiveDescrACC05;


  public JournalHeaderVO() {
  }


  public String getCompanyCodeSys01ACC05() {
    return companyCodeSys01ACC05;
  }
  public void setCompanyCodeSys01ACC05(String companyCodeSys01ACC05) {
    this.companyCodeSys01ACC05 = companyCodeSys01ACC05;
  }
  public String getAccountingMotiveCodeAcc03ACC05() {
    return accountingMotiveCodeAcc03ACC05;
  }
  public void setAccountingMotiveCodeAcc03ACC05(String accountingMotiveCodeAcc03ACC05) {
    this.accountingMotiveCodeAcc03ACC05 = accountingMotiveCodeAcc03ACC05;
  }
  public java.sql.Date getItemDateACC05() {
    return itemDateACC05;
  }
  public void setItemDateACC05(java.sql.Date itemDateACC05) {
    this.itemDateACC05 = itemDateACC05;
  }
  public java.math.BigDecimal getProgressiveACC05() {
    return progressiveACC05;
  }
  public void setProgressiveACC05(java.math.BigDecimal progressiveACC05) {
    this.progressiveACC05 = progressiveACC05;
  }
  public java.math.BigDecimal getItemYearACC05() {
    return itemYearACC05;
  }
  public void setItemYearACC05(java.math.BigDecimal itemYearACC05) {
    this.itemYearACC05 = itemYearACC05;
  }


  public void addJournalRow(JournalRowVO row) {
    journalRows.add(row);
  }

  public ArrayList getJournalRows() {
    return journalRows;
  }
  public String getDescriptionACC05() {
    return descriptionACC05;
  }
  public void setDescriptionACC05(String descriptionACC05) {
    this.descriptionACC05 = descriptionACC05;
  }
  public String getMotiveDescrACC05() {
    return motiveDescrACC05;
  }
  public void setMotiveDescrACC05(String motiveDescrACC05) {
    this.motiveDescrACC05 = motiveDescrACC05;
  }


}
