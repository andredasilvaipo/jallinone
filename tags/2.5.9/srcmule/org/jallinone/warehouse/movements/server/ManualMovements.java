package org.jallinone.warehouse.movements.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;

import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to manage warehouse manual movements in WAR02 and consequently in WAR03.</p>
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
@javax.jws.WebService
public interface ManualMovements {
		

  public VOResponse insertManualMovement(MovementVO vo,String t1,String serverLanguageId,String username) throws Throwable;

  public VOResponse insertManualMovements(MovementVO voTemplate,VariantsMatrixVO matrixVO,Object[][] cells,String t1,String serverLanguageId,String username) throws Throwable;

  
}

