package org.jallinone.system.java;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: According to the authorizations defined for the button container,
 * this class defines a button abilitation, based on the COMPANY_CODE value.
 * .</p>
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
public class ButtonCompanyAuthorizations implements Serializable {

  /** collection of pairs: functionId, ButtonAuthorization object */
  private Hashtable authorizations = new Hashtable();


  public ButtonCompanyAuthorizations() { }


  public final void addButtonAuthorization(String functionId,String companyCode,boolean isInsertEnabled,boolean isEditEnabled,boolean isDeleteEnabled) {
    Hashtable companies = (Hashtable )authorizations.get(functionId);
    if (companies==null) {
      companies = new Hashtable();
      authorizations.put(functionId,companies);
      companies.put(companyCode,new ButtonAuthorization(isInsertEnabled,isEditEnabled,isDeleteEnabled));
    }
    else {
      ButtonAuthorization ba = (ButtonAuthorization)companies.get(companyCode);
      if (ba==null) {
        companies.put(companyCode,new ButtonAuthorization(isInsertEnabled,isEditEnabled,isDeleteEnabled));
      }
      else
        companies.put(companyCode,new ButtonAuthorization(
            ba.isInsertEnabled() || isInsertEnabled,
            ba.isEditEnabled() || isEditEnabled,
            ba.isDeleteEnabled() || isDeleteEnabled
        ));
    }
  }


  /**
   * @param functionId identifier of the function
   * @return list of companies code allowed to view
   */
  public final ArrayList getCompaniesList(String functionId) {
    ArrayList list = new ArrayList();
    Hashtable companies = (Hashtable)authorizations.get(functionId);
    if (companies!=null) {
      Enumeration keys = companies.keys();
      while(keys.hasMoreElements())
        list.add(keys.nextElement());
    }

    return list;
  }


  /**
   * @param functionId identifier of the function
   * @param companyCode company code
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isInsertEnabled(String functionId,String companyCode) {
    if (functionId==null)
      // if no functionId is defined, then button is false
      return false;
    Hashtable companies = (Hashtable)authorizations.get(functionId);
    if (companies==null)
      return false;

    ButtonAuthorization auth = (ButtonAuthorization)companies.get(companyCode);
    if (auth==null)
      // button is disabled if no authorization was found...
      return false;
    else
      return auth.isInsertEnabled();
  }


  /**
   * @param functionId identifier of the function
   * @param companyCode company code
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isEditEnabled(String functionId,String companyCode) {
    if (functionId==null)
      // if no functionId is defined, then button is false
      return false;
    Hashtable companies = (Hashtable)authorizations.get(functionId);
    if (companies==null)
      return false;

    ButtonAuthorization auth = (ButtonAuthorization)companies.get(companyCode);
    if (auth==null)
      // button is disabled if no authorization was found...
      return false;
    else
      return auth.isEditEnabled();
  }


  /**
   * @param functionId identifier of the function
   * @param companyCode company code
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isDeleteEnabled(String functionId,String companyCode) {
    if (functionId==null)
      // if no functionId is defined, then button is false
      return false;
    Hashtable companies = (Hashtable)authorizations.get(functionId);
    if (companies==null)
      return false;

    ButtonAuthorization auth = (ButtonAuthorization)companies.get(companyCode);
    if (auth==null)
      // button is disabled if no authorization was found...
      return false;
    else
      return auth.isDeleteEnabled();
  }


  /**
   * <p>Description: Inner class used to store authorizations for a single functionId+companyCode.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ButtonAuthorization implements Serializable {

    private boolean isInsertEnabled; // copy = insert...
    private boolean isEditEnabled;
    private boolean isDeleteEnabled;

    public ButtonAuthorization(boolean isInsertEnabled,boolean isEditEnabled,boolean isDeleteEnabled) {
      this.isInsertEnabled = isInsertEnabled;
      this.isEditEnabled = isEditEnabled;
      this.isDeleteEnabled = isDeleteEnabled;
    }


    public final boolean isInsertEnabled() {
      return isInsertEnabled;
    }

    public final boolean isEditEnabled() {
      return isEditEnabled;
    }

    public final boolean isDeleteEnabled() {
      return isDeleteEnabled;
    }

  }



}
