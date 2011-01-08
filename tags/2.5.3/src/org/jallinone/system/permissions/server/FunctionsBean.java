package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import javax.swing.tree.*;
import javax.swing.tree.*;

import java.math.BigDecimal;
import java.sql.*;

import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.permissions.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.tree.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage application functions.</p>
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
public class FunctionsBean  implements Functions {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }



  public FunctionsBean() {
  }


  /**
   * Business logic to execute.
   */
  public VOResponse updateFunction(JAIOApplicationFunctionVO oldVO,JAIOApplicationFunctionVO newVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      Response res = null;

      if (!newVO.getDescription().equals(oldVO.getDescription())) {
        // description is changed...
        if (!newVO.isFolder())
          TranslationUtils.updateTranslation(oldVO.getDescription(),newVO.getDescription(),newVO.getProgressiveSys10SYS06(),serverLanguageId,conn);
        else
          TranslationUtils.updateTranslation(oldVO.getDescription(),newVO.getDescription(),newVO.getProgressiveHIE01(),serverLanguageId,conn);
      }


      if (newVO.isCopyNode()) {
        // the node must be duplicated...
        // the node must be moved...
        if (!newVO.isFolder() &&
            (!oldVO.getPosOrderSYS18().equals(newVO.getPosOrderSYS18()) ||
             !oldVO.getProgressiveHie01SYS18().equals(newVO.getProgressiveHie01SYS18()))) {
          // update function position...
          pstmt = conn.prepareStatement(
            "insert into SYS18_FUNCTION_LINKS(PROGRESSIVE_HIE01,POS_ORDER,FUNCTION_CODE_SYS06) values(?,?,?)"
          );
          pstmt.setBigDecimal(1,newVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(2,newVO.getPosOrderSYS18());
          pstmt.setString(3,newVO.getFunctionId());
          pstmt.executeUpdate();
        }
      }
      else {
        // the node must be moved...
        if (!newVO.isFolder() &&
            (!oldVO.getPosOrderSYS18().equals(newVO.getPosOrderSYS18()) ||
             !oldVO.getProgressiveHie01SYS18().equals(newVO.getProgressiveHie01SYS18()))) {
          // update function position...
          pstmt = conn.prepareStatement(
            "update SYS18_FUNCTION_LINKS set PROGRESSIVE_HIE01=?,POS_ORDER=? where FUNCTION_CODE_SYS06=? and PROGRESSIVE_HIE01=? and POS_ORDER=?"
          );
          pstmt.setBigDecimal(1,newVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(2,newVO.getPosOrderSYS18());
          pstmt.setString(3,newVO.getFunctionId());
          pstmt.setBigDecimal(4,oldVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(5,oldVO.getPosOrderSYS18());
          int num = pstmt.executeUpdate();
          if (num==0) {
            throw new Exception("Updating not performed: the record was previously updated.");
          }
        }

      }

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an application function",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      throw new Exception(ex.getMessage()); 
    }

  }



}

