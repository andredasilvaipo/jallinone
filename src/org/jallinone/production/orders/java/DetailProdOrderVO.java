package org.jallinone.production.orders.java;

import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a production order header detail (table DOC22).</p>
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
public class DetailProdOrderVO extends ProdOrderVO {

  private String noteDOC22;
  private BigDecimal progressiveHie02WAR01; // hierarchy of destination warehouse


  public DetailProdOrderVO() {
  }


  public String getNoteDOC22() {
    return noteDOC22;
  }
  public void setNoteDOC22(String noteDOC22) {
    this.noteDOC22 = noteDOC22;
  }
  public BigDecimal getProgressiveHie02WAR01() {
    return progressiveHie02WAR01;
  }
  public void setProgressiveHie02WAR01(BigDecimal progressiveHie02WAR01) {
    this.progressiveHie02WAR01 = progressiveHie02WAR01;
  }

}
