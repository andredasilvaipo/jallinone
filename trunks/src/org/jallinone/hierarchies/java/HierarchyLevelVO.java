package org.jallinone.hierarchies.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a hierachy level of HIE01 table.</p>
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
public class HierarchyLevelVO extends ValueObjectImpl {


  private java.math.BigDecimal progressiveHIE01;
  private java.math.BigDecimal progressiveHie02HIE01;
  private java.math.BigDecimal progressiveHie01HIE01;
  private java.math.BigDecimal levelHIE01;
  private String descriptionSYS10;
  private String enabledHIE01;
  private java.math.BigDecimal progressiveHie01HIE02;


  public HierarchyLevelVO() {
  }


  public java.math.BigDecimal getProgressiveHIE01() {
    return progressiveHIE01;
  }
  public void setProgressiveHIE01(java.math.BigDecimal progressiveHIE01) {
    this.progressiveHIE01 = progressiveHIE01;
  }
  public java.math.BigDecimal getProgressiveHie02HIE01() {
    return progressiveHie02HIE01;
  }
  public void setProgressiveHie02HIE01(java.math.BigDecimal progressiveHie02HIE01) {
    this.progressiveHie02HIE01 = progressiveHie02HIE01;
  }
  public java.math.BigDecimal getProgressiveHie01HIE01() {
    return progressiveHie01HIE01;
  }
  public void setProgressiveHie01HIE01(java.math.BigDecimal progressiveHie01HIE01) {
    this.progressiveHie01HIE01 = progressiveHie01HIE01;
  }
  public java.math.BigDecimal getLevelHIE01() {
    return levelHIE01;
  }
  public void setLevelHIE01(java.math.BigDecimal levelHIE01) {
    this.levelHIE01 = levelHIE01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledHIE01() {
    return enabledHIE01;
  }
  public void setEnabledHIE01(String enabledHIE01) {
    this.enabledHIE01 = enabledHIE01;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }

}
