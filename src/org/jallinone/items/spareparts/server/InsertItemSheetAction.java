package org.jallinone.items.spareparts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.system.progressives.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.items.spareparts.java.ItemSheetVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert a sheet in ITM25 table.</p>
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

public class InsertItemSheetAction implements Action {

		public InsertItemSheetAction() {
		}

		/**
		 * @return request name
		 */
		public final String getRequestName() {
			return "insertItemSheet";
		}


		public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
			try {
				ItemSheetVO vo = (ItemSheetVO)inputPar;
				String imagePath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);

				ItemSheets bean = (ItemSheets)JAIOBeanFactory.getInstance().getBean(ItemSheets.class);
				Response answer = bean.insertItemSheet(vo,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername(),imagePath);

				return answer;
			}
			catch (Throwable ex) {
				Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
				return new ErrorResponse(ex.getMessage());
			}
		}

}

