package org.jallinone.commons.server;

import org.openswing.swing.internationalization.java.XMLResourcesFactory;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import java.util.Hashtable;
import org.openswing.swing.internationalization.java.Resources;
import javax.servlet.ServletContext;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Collection of resources, one for each language supported, used server side.</p>
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
public class XMLResources extends ServerResourcesFactory {

  private XMLResourcesFactory factory = null;

  public XMLResources() {
  }


  /**
   * Method called by the server controller (Controller object) to initialize the factory.
   * @param context
   */
  public void init(ServletContext context) {
    Hashtable xmlFiles = new Hashtable();
    xmlFiles.put("EN",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_en.xml");
    xmlFiles.put("IT",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_it.xml");
    xmlFiles.put("ES",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_es.xml");
		xmlFiles.put("PTBR",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_PTBR.xml");
		xmlFiles.put("DE",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_de.xml");
		xmlFiles.put("HR",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_hr.xml");
		xmlFiles.put("RU",this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"Resources_ru.xml");
    factory = new XMLResourcesFactory(xmlFiles,false);
  }


  /**
   * Load dictionary, according to the specified language id.
   * @param langId language id identifier
   */
  public final void setLanguage(String langId) throws UnsupportedOperationException {
    factory.setLanguage(langId);
  }


  /**
   * @return internationalization settings, according with the current language
   */
  public final Resources getResources() {
    return factory.getResources();
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    return factory.getResources(langId);
  }


}
