package org.jallinone.hierarchies.server;

import java.math.*;
import java.sql.*;

import org.jallinone.hierarchies.java.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to insert a new level in the specified hierarchy.</p>
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
public class InsertLevelBean {


  public InsertLevelBean() {
  }


  /**
   * Business logic to execute.
   */
  public final Response insertLevel(Connection conn,HierarchyLevelVO vo,UserSessionParameters userSessionPars) {
    PreparedStatement pstmt = null;
    try {
      vo.setEnabledHIE01("Y");

      // insert record in SYS10...
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),((JAIOUserSessionParameters)userSessionPars).getDefCompanyCodeSys01SYS03(),conn);
      vo.setProgressiveHIE01(progressiveHIE01);

      // insert record in HIE01...
      pstmt = conn.prepareStatement(
          "insert into HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE01,PROGRESSIVE_HIE02,LEV,ENABLED) values(?,?,?,?,?)"
      );
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie01HIE01());
      pstmt.setBigDecimal(3,vo.getProgressiveHie02HIE01());
      pstmt.setBigDecimal(4,vo.getLevelHIE01());
      pstmt.setString(5,vo.getEnabledHIE01());
      pstmt.execute();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertLevel","Error while inserting new hierarchy level",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }



}
