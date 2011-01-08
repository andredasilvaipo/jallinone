package org.jallinone.system.permissions.java;

import java.math.BigDecimal;
import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: application function.
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
public class JAIOApplicationFunctionVO extends ValueObjectImpl {

  /** function identifier */
  private String functionId;

  /** image name  */
  private String iconName;

  /** method name in ClientFacade to execute */
  private String methodName;

  /** this node is a folder */
  private boolean isFolder;

  /** node description */
  private String description;

  /** folder identifier on which the function is linked */
  private BigDecimal progressiveHie01SYS18;

  /** function position inside the folder */
  private BigDecimal posOrderSYS18;

  /** folder identifier */
  private BigDecimal progressiveHIE01;

  /** progressive in SYS10 related to function description */
  private BigDecimal progressiveSys10SYS06;

  /** parent folder identifier */
  private BigDecimal progressiveHie01HIE01;


  public JAIOApplicationFunctionVO() {}

  /** flag used on updating node to indicate that the node must be duplicated */
  private boolean copyNode;


  /**
   * Constructor: a folder
   * @param nodeName description (already translated) to view in the tree node/menu item
   * @param iconName image name
   */
  public JAIOApplicationFunctionVO(String nodeName,String iconName,BigDecimal progressiveHIE01,BigDecimal progressiveHie01HIE01) {
    this.description = nodeName;
    this.iconName = iconName;
    isFolder = true;
    this.progressiveHIE01 = progressiveHIE01;
    this.progressiveHie01HIE01 = progressiveHie01HIE01;
  }


  /**
   * Constructor: a node function
   * @param nodeName description (already translated) to view in the tree node/menu item
   * @param functionId function identifier
   * @param iconName image name
   * @param methodName method name in ClientFacade to execute
   */
  public JAIOApplicationFunctionVO(String nodeName,String functionId,String iconName,String methodName,
                                 BigDecimal progressiveHie01SYS18,BigDecimal posOrderSYS18,BigDecimal progressiveSys10SYS06) {
    this.description = nodeName;
    this.functionId = functionId;
    this.iconName = iconName;
    this.methodName = methodName;
    isFolder = false;
    this.progressiveHie01SYS18 = progressiveHie01SYS18;
    this.posOrderSYS18 = posOrderSYS18;
    this.progressiveSys10SYS06 = progressiveSys10SYS06;
  }


  /**
   * @return folder identifier on which the function is linked
   */
  public final BigDecimal getProgressiveHie01SYS18() {
    return progressiveHie01SYS18;
  }


  /**
   * @return function position inside the folder
   */
  public final BigDecimal getPosOrderSYS18() {
    return posOrderSYS18;
  }


  /**
   * @return folder identifier
   */
  public final BigDecimal getProgressiveHIE01() {
    return progressiveHIE01;
  }


  /**
   * @return parent folder identifier
   */
  public final BigDecimal getProgressiveHie01HIE01() {
    return progressiveHie01HIE01;
  }


  /**
   * @return progressive in SYS10 related to function description
   */
  public final BigDecimal getProgressiveSys10SYS06() {
    return progressiveSys10SYS06;
  }
  public String getDescription() {
    return description;
  }
  public String getFunctionId() {
    return functionId;
  }
  public String getIconName() {
    return iconName;
  }
  public boolean isFolder() {
    return isFolder;
  }
  public String getMethodName() {
    return methodName;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setProgressiveHIE01(BigDecimal progressiveHIE01) {
    this.progressiveHIE01 = progressiveHIE01;
  }
  public void setProgressiveHie01HIE01(BigDecimal progressiveHie01HIE01) {
    this.progressiveHie01HIE01 = progressiveHie01HIE01;
  }
  public void setProgressiveHie01SYS18(BigDecimal progressiveHie01SYS18) {
    this.progressiveHie01SYS18 = progressiveHie01SYS18;
  }
  public void setProgressiveSys10SYS06(BigDecimal progressiveSys10SYS06) {
    this.progressiveSys10SYS06 = progressiveSys10SYS06;
  }
  public void setPosOrderSYS18(BigDecimal posOrderSYS18) {
    this.posOrderSYS18 = posOrderSYS18;
  }


  /**
   * @return used on updating node to indicate that the node must be duplicated
   */
  public boolean isCopyNode() {
    return copyNode;
  }


  /**
   * @param copyNode flag used on updating node to indicate that the node must be duplicated
   */
  public final void setCopyNode(boolean copyNode) {
    this.copyNode = copyNode;
  }


}
