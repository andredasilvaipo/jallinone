package org.jallinone.hierarchies.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a hierachy level of HIE03 table.</p>
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


  private java.math.BigDecimal progressiveHIE03;
  private java.math.BigDecimal progressiveHie04HIE03;
  private java.math.BigDecimal progressiveHie03HIE03;
  private java.math.BigDecimal levelHIE03;
  private String descriptionSYS10;
  private String enabledHIE03;
  private java.math.BigDecimal progressiveHie03HIE04;


  public HierarchyLevelVO() {
  }


  public java.math.BigDecimal getProgressiveHIE03() {
    return progressiveHIE03;
  }
  public void setProgressiveHIE03(java.math.BigDecimal progressiveHIE03) {
    this.progressiveHIE03 = progressiveHIE03;
  }
  public java.math.BigDecimal getProgressiveHie04HIE03() {
    return progressiveHie04HIE03;
  }
  public void setProgressiveHie04HIE03(java.math.BigDecimal progressiveHie04HIE03) {
    this.progressiveHie04HIE03 = progressiveHie04HIE03;
  }
  public java.math.BigDecimal getProgressiveHie03HIE03() {
    return progressiveHie03HIE03;
  }
  public void setProgressiveHie03HIE03(java.math.BigDecimal progressiveHie03HIE03) {
    this.progressiveHie03HIE03 = progressiveHie03HIE03;
  }
  public java.math.BigDecimal getLevelHIE03() {
    return levelHIE03;
  }
  public void setLevelHIE03(java.math.BigDecimal levelHIE03) {
    this.levelHIE03 = levelHIE03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledHIE03() {
    return enabledHIE03;
  }
  public void setEnabledHIE03(String enabledHIE03) {
    this.enabledHIE03 = enabledHIE03;
  }
  public void setProgressiveHie03HIE04(java.math.BigDecimal progressiveHie03HIE04) {
    this.progressiveHie03HIE04 = progressiveHie03HIE04;
  }
  public java.math.BigDecimal getProgressiveHie03HIE04() {
    return progressiveHie03HIE04;
  }


}
