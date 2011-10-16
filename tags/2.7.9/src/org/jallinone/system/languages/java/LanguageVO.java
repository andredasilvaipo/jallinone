package org.jallinone.system.languages.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to store language info.</p>
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
public class LanguageVO extends ValueObjectImpl {


  private String languageCodeSYS09;
  private String descriptionSYS09;
  private String clientLanguageCodeSYS09;
  public LanguageVO() {
  }
  public String getLanguageCodeSYS09() {
    return languageCodeSYS09;
  }
  public void setLanguageCodeSYS09(String languageCodeSYS09) {
    this.languageCodeSYS09 = languageCodeSYS09;
  }
  public String getDescriptionSYS09() {
    return descriptionSYS09;
  }
  public void setDescriptionSYS09(String descriptionSYS09) {
    this.descriptionSYS09 = descriptionSYS09;
  }
  public String getClientLanguageCodeSYS09() {
    return clientLanguageCodeSYS09;
  }
  public void setClientLanguageCodeSYS09(String clientLanguageCodeSYS09) {
    this.clientLanguageCodeSYS09 = clientLanguageCodeSYS09;
  }

}
