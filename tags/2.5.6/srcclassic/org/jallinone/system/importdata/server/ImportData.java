package org.jallinone.system.importdata.server;

import java.util.List;

import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.receive.java.VOResponse;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Bean used to import data in a specific table.</p>
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

public interface ImportData {

	  public VOResponse importData(
		      ETLProcessVO processVO,
		      List fieldsVO,
		      List langsVO,
		      String username,
		      String defaultCOmpanyCode
		  ) throws Throwable;

}

