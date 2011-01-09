package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
* <p>Description: Value Object used to store grid permissions.</p>
 * </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
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
public class GridPermissionsPerRoleVO extends ValueObjectImpl {

  private String columnName;
  private boolean visible;
  private boolean editableInIns;
  private boolean editableInEdit;
  private boolean required;
  private String description;

  private boolean defaultVisible;
  private boolean defaultEditableInIns;
  private boolean defaultEditableInEdit;
  private boolean defaultRequired;


  public GridPermissionsPerRoleVO() {
  }


  public boolean isVisible() {
    return visible;
  }
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public boolean isEditableInIns() {
    return editableInIns;
  }
  public void setEditableInIns(boolean editableInIns) {
    this.editableInIns = editableInIns;
  }
  public boolean isEditableInEdit() {
    return editableInEdit;
  }
  public void setEditableInEdit(boolean editableInEdit) {
    this.editableInEdit = editableInEdit;
  }
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public boolean isDefaultEditableInEdit() {
    return defaultEditableInEdit;
  }
  public boolean isDefaultEditableInIns() {
    return defaultEditableInIns;
  }
  public boolean isDefaultRequired() {
    return defaultRequired;
  }
  public boolean isDefaultVisible() {
    return defaultVisible;
  }
  public void setDefaultVisible(boolean defaultVisible) {
    this.defaultVisible = defaultVisible;
  }
  public void setDefaultRequired(boolean defaultRequired) {
    this.defaultRequired = defaultRequired;
  }
  public void setDefaultEditableInIns(boolean defaultEditableInIns) {
    this.defaultEditableInIns = defaultEditableInIns;
  }
  public void setDefaultEditableInEdit(boolean defaultEditableInEdit) {
    this.defaultEditableInEdit = defaultEditableInEdit;
  }




}
