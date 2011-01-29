package org.jallinone.commons.client;

import javax.swing.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.beans.Beans;
import java.util.ArrayList;
import org.openswing.swing.client.ComboBoxControl;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.client.LabelControl;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Container;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Combo control that contains the list of company codes allowed for the specified application function.</p>
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
public class CompaniesComboControl extends ComboBoxControl {

  /** flag used inside addNotify method */
  private boolean firstTime = true;

  /** fucntion identifier */
  private String functionCode = null;


  public CompaniesComboControl() {
    this.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED &&
            getComboBox().getSelectedIndex()!=-1
            ) {
          ClientApplet applet = ((ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
          if (!applet.getAuthorizations().getCompanyBa().isInsertEnabled(
              functionCode,
              getValue().toString()
          )) {
            getComboBox().setSelectedIndex( -1);
          }
        }
      }
    });
  }


  /**
   * Method called when the window containing this component is set to visible.
   */
  public void addNotify() {
    try {
      super.addNotify();
      if (firstTime) {
        firstTime = false;

        // retrieve function identifier...
        Container c = this.getParent();
        while (c != null && ! (c instanceof Form)) {
          c = c.getParent();
        }
        if (c != null) {
          functionCode = ( (Form) c).getFunctionId();
        }
        final Form form = (Form)c;

        // set domain in combo box...
        ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
        ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
        final ArrayList companiesList = bca.getCompaniesList(functionCode);
        Domain domain = new Domain("DOMAIN_" + functionCode);
        for (int i = 0; i < companiesList.size(); i++) {
          if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
              functionCode,companiesList.get(i).toString()
          ))
            domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
        }
        super.setDomain(domain);

        if (applet.getAuthorizations().isOneCompany()) {
          new Thread() {
            public void run() {
              yield();

              CompaniesComboControl.this.getParent().remove(CompaniesComboControl.this);
              if (getLinkLabel() != null) {
                getLinkLabel().getParent().remove(getLinkLabel());
              }

              if (form!=null) {
                try {
                  if (form.getVOModel().getValueObject() == null) {
                    form.getVOModel().setValueObject((ValueObject)form.getVOModel().getValueObjectType().newInstance());
                  }
                }
                catch (Throwable ex1) {
                  ex1.printStackTrace();
                }
								if (companiesList.size()>0)
									form.getVOModel().setValue(getAttributeName(),companiesList.get(0));
                try {
                  form.unbind(CompaniesComboControl.this);
                }
                catch (Exception ex) {
                  ex.printStackTrace();
                }
              }
              else
                setValue(companiesList.get(0));

            }
          }.start();
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * This method is optional: usually this control is inserted in a Form panel and hinerits its function identifier
   * @param functionCode function identifier used by this control; this method must be called when this control is not inside a Form panel
   */
  public final void setFunctionCode(String functionCode) {
    this.functionCode = functionCode;
  }


  /**
   * @return function identifier used by this control
   */
  public final String getFunctionCode() {
    return functionCode;
  }





}
