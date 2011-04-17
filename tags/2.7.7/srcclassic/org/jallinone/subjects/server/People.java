package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.subjects.java.PeopleVO;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to insert/update/delete people in/from REG04 table.</p>
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

public interface People {

  public void insert(boolean generateProgressive,PeopleVO vo,String t1,String serverLanguageId,String username) throws Throwable ;

  public VOResponse update(PeopleVO oldVO,PeopleVO newVO,String t1,String serverLanguageId,String username) throws Throwable  ;

}

