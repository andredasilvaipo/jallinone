package org.jallinone.items.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Item primary key.</p>
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
public class ItemPK implements Serializable {

  private String companyCodeSys01ITM01;
  private String itemCodeITM01;


  public ItemPK(String companyCodeSys01ITM01,String itemCodeITM01) {
    this.companyCodeSys01ITM01 = companyCodeSys01ITM01;
    this.itemCodeITM01 = itemCodeITM01;
  }

  
  public ItemPK() {}


  public String getCompanyCodeSys01ITM01() {
    return companyCodeSys01ITM01;
  }
  public String getItemCodeITM01() {
    return itemCodeITM01;
  }


public void setCompanyCodeSys01ITM01(String companyCodeSys01ITM01) {
	this.companyCodeSys01ITM01 = companyCodeSys01ITM01;
}


public void setItemCodeITM01(String itemCodeITM01) {
	this.itemCodeITM01 = itemCodeITM01;
}
  
  
  

}