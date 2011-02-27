package org.jallinone.system.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.math.BigDecimal;
import org.jallinone.system.customizations.java.WindowCustomizationVO;
import java.util.Hashtable;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: contains additional db fields (WindowCustomizationVO objects), for each function identifier.</p>
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
public class CustomizedWindows implements Serializable {

  /** collection of pairs: progressiveSYS13,WindowCustomizationVO object */
  private Hashtable cust = new Hashtable();


  public CustomizedWindows() {
  }


  /**
   * Add a customized field to the collection.
   * @param vo customized field
   */
  public final void addWindowCustomization(WindowCustomizationVO vo) {
    java.util.List list = (ArrayList)cust.get(vo.getProgressiveSys13SYS12());
    if (list==null) {
      list = new ArrayList();
      cust.put(vo.getProgressiveSys13SYS12(),list);
    }
    list.add(vo);
  }


  /**
   * @param progressiveSYS13 window identifier
   * @return list of customized fields associated to the specified window identifier
   */
  public final ArrayList getCustomizedFields(BigDecimal progressiveSYS13) {
    ArrayList list = (ArrayList)cust.get(progressiveSYS13);
    if (list==null)
      return new ArrayList();
    else
      return list;
  }


  public Hashtable getCust() {
	  return cust;
  }


  public void setCust(Hashtable cust) {
	  this.cust = cust;
  }


}
