package org.jallinone.commons.client;

import javax.swing.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.beans.Beans;
import java.util.ArrayList;
import org.openswing.swing.table.columns.client.ComboColumn;
import org.openswing.swing.domains.java.Domain;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Container;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.domains.java.DomainPair;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Combo column that contains the list of company codes allowed for the specified application function.</p>
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
public class CompaniesComboColumn extends ComboColumn {

  /** function identifier */
  private String functionCode = null;


  public CompaniesComboColumn() {
    setEditableOnEdit(false);
  }


  /**
   * @return function identifier
   */
  public final String getFunctionCode() {
    return functionCode;
  }


  /**
   * Set the function identifier.
   * @param functionCode function identifier
   */
  public final void setFunctionCode(String functionCode) {
    this.functionCode = functionCode;
    if (Beans.isDesignTime())
      return;

    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList(functionCode);
    final Domain domain = new Domain("DOMAIN_" + functionCode);
    for (int i = 0; i < companiesList.size(); i++) {
      domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
    }
    super.setDomain(domain);
    super.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED &&
            ((JComboBox)e.getSource()).getSelectedIndex() != -1
        ) {
          ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
          if (!applet.getAuthorizations().getCompanyBa().isInsertEnabled(
              CompaniesComboColumn.this.functionCode,
              getCode(domain,e.getItem().toString())
          )) {
            ((JComboBox)e.getSource()).setSelectedIndex( -1);
          }
        }
      }
    });

    if (applet.getAuthorizations().isOneCompany() && this.getParent()!=null) {
      this.getParent().remove(this);
    }
  }


  /**
   * @return code related to the specified combo item description
   */
  private String getCode(Domain domain,String desc) {
    DomainPair[] list = domain.getDomainPairList();
    for(int i=0;i<list.length;i++)
      if (list[i].getDescription().equals(desc))
        return list[i].getCode().toString();
    return "";
  }




}
