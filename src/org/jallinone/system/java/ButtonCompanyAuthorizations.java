package org.jallinone.system.java;

import java.io.Serializable;
import java.util.*;


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

  /** collection of pairs: functionId_companyCode, ButtonCompanyAuthorization object */
  private Hashtable authorizations = new Hashtable();


  public ButtonCompanyAuthorizations() { }

  
  public Hashtable getAuthorizations() {
	  return authorizations;
  }

  public void setAuthorizations(Hashtable authorizations) {
	  this.authorizations = authorizations;
  }


  public final void addButtonAuthorization(String functionId,String companyCode,boolean isInsertEnabled,boolean isEditEnabled,boolean isDeleteEnabled) {
	ButtonCompanyAuthorization auth = (ButtonCompanyAuthorization)authorizations.get(functionId+"_"+companyCode);
    if (auth==null) {
      auth = new ButtonCompanyAuthorization(functionId,companyCode,isInsertEnabled,isEditEnabled,isDeleteEnabled);
      authorizations.put(functionId+"_"+companyCode,auth);
    }
    else {
    	authorizations.put(functionId+"_"+companyCode,new ButtonCompanyAuthorization(
        	functionId,
        	companyCode,
        	auth.isInsertEnabled() || isInsertEnabled,
        	auth.isEditEnabled() || isEditEnabled,
        	auth.isDeleteEnabled() || isDeleteEnabled
        ));
    }
  }


  /**
   * @param functionId identifier of the function
   * @return list of companies code allowed to view
   */
  public final ArrayList getCompaniesList(String functionId) {
    ArrayList list = new ArrayList();
    Iterator it = authorizations.keySet().iterator();
    String f_c = null;
    while(it.hasNext()) {
    	f_c = it.next().toString();
    	if (f_c.startsWith(functionId+"_"))
    		list.add(f_c.substring(f_c.lastIndexOf("_")+1));
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
    ButtonCompanyAuthorization auth = (ButtonCompanyAuthorization)authorizations.get(functionId+"_"+companyCode);
    if (auth==null)
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
    ButtonCompanyAuthorization auth = (ButtonCompanyAuthorization)authorizations.get(functionId+"_"+companyCode);
    if (auth==null)
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
	  ButtonCompanyAuthorization auth = (ButtonCompanyAuthorization)authorizations.get(functionId+"_"+companyCode);
	  if (auth==null)
		  return false;
	  else
		  return auth.isDeleteEnabled();
  }



}
