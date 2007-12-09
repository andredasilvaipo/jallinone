package org.jallinone.commons.client;

import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import java.util.ArrayList;
import org.openswing.swing.util.java.Consts;
import java.lang.reflect.Method;
import java.lang.reflect.*;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.form.client.Form;
import java.awt.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form controller used with forms that support more companies.</p>
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
public class CompanyFormController extends FormController {


  public CompanyFormController() {
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    try {
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      Method[] m = Class.forName(form.getVOClassName()).getMethods();
      for (int i = 0; i < m.length; i++) {
        if (m[i].getName().startsWith("getCompanyCode")) {
          try {
            String companyCode = m[i].invoke(form.getVOModel().getValueObject(), new Object[0]).toString();
            boolean canEdit = applet.getAuthorizations().getCompanyBa().isEditEnabled(
                form.getFunctionId(),
                companyCode
            );
            if (!canEdit)
              JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(form),
                ClientSettings.getInstance().getResources().getResource(
                "You are not allowed to edit data."),
                ClientSettings.getInstance().getResources().getResource(
                "Attention"),
                JOptionPane.WARNING_MESSAGE
              );
            return canEdit;

          }
          catch (Throwable ex) {
            return false;
          }
        }
      }
      return false;
    }
    catch (Throwable ex) {
      return false;
    }
  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    ClientApplet applet = ((ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
    ArrayList companyCodes = applet.getAuthorizations().getCompanyBa().getCompaniesList(form.getFunctionId());
    for(int i=0;i<companyCodes.size();i++)
      if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
        form.getFunctionId(),
        companyCodes.get(i).toString()
      ))
        return true;

    JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(form),
        ClientSettings.getInstance().getResources().getResource("You are not allowed to insert data."),
        ClientSettings.getInstance().getResources().getResource("Attention"),
        JOptionPane.WARNING_MESSAGE
    );
    return false;
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    try {
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      Method[] m = Class.forName(form.getVOClassName()).getMethods();
      for (int i = 0; i < m.length; i++) {
        if (m[i].getName().startsWith("getCompanyCode")) {
          try {
            String companyCode = m[i].invoke(form.getVOModel().getValueObject(), new Object[0]).toString();
            boolean canDelete = applet.getAuthorizations().getCompanyBa().isDeleteEnabled(
                form.getFunctionId(),
                companyCode
            );
            if (!canDelete)
              JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(form),
                ClientSettings.getInstance().getResources().getResource(
                "You are not allowed to delete data."),
                ClientSettings.getInstance().getResources().getResource(
                "Attention"),
                JOptionPane.WARNING_MESSAGE
              );
            return canDelete;
          }
          catch (Throwable ex) {
            return false;
          }
        }
      }
      return false;
    }
    catch (Throwable ex) {
      return false;
    }
  }



}
