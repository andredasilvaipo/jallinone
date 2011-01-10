package org.jallinone.subjects.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a subject hierarchy level + a list of subjects.</p>
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
public class HierarSubjectsVO extends ValueObjectImpl {


  private BigDecimal progressiveHie01REG16;
  private BigDecimal progressiveHie02REG16;
  private ArrayList subjects;

  
  public HierarSubjectsVO() {}
  

  public HierarSubjectsVO(BigDecimal progressiveHie01REG16,BigDecimal progressiveHie02REG16,ArrayList subjects) {
    this.progressiveHie01REG16 = progressiveHie01REG16;
    this.progressiveHie02REG16 = progressiveHie02REG16;
    this.subjects = subjects;
  }


  public java.math.BigDecimal getProgressiveHie01REG16() {
    return progressiveHie01REG16;
  }
  public java.math.BigDecimal getProgressiveHie02REG16() {
    return progressiveHie02REG16;
  }
  public java.util.ArrayList getSubjects() {
    return subjects;
  }


public void setProgressiveHie01REG16(BigDecimal progressiveHie01REG16) {
	this.progressiveHie01REG16 = progressiveHie01REG16;
}


public void setProgressiveHie02REG16(BigDecimal progressiveHie02REG16) {
	this.progressiveHie02REG16 = progressiveHie02REG16;
}


public void setSubjects(ArrayList subjects) {
	this.subjects = subjects;
}

  
  
  
}
