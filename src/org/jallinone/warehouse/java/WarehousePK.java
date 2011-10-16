package org.jallinone.warehouse.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouse primary key.</p>
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
public class WarehousePK implements Serializable {

  private String companyCodeSys01WAR01;
  private String warehouseCodeWAR01;


  public WarehousePK(String companyCodeSys01WAR01,String warehouseCodeWAR01) {
    this.companyCodeSys01WAR01 = companyCodeSys01WAR01;
    this.warehouseCodeWAR01 = warehouseCodeWAR01;
  }

  
  public WarehousePK() {}
  

  public String getCompanyCodeSys01WAR01() {
    return companyCodeSys01WAR01;
  }
  public String getWarehouseCodeWAR01() {
    return warehouseCodeWAR01;
  }


  public void setCompanyCodeSys01WAR01(String companyCodeSys01WAR01) {
	  this.companyCodeSys01WAR01 = companyCodeSys01WAR01;
  }


  public void setWarehouseCodeWAR01(String warehouseCodeWAR01) {
	  this.warehouseCodeWAR01 = warehouseCodeWAR01;
  }

  
  
  

}