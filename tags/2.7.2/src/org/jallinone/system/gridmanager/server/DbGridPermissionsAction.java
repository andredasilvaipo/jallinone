package org.jallinone.system.gridmanager.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.system.gridmanager.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import org.openswing.swing.table.permissions.database.server.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid permissions manager: it manages the fetching of grid permissions.
 * This implementation is based on database tables: it stores and retrieves user roles from a table and after that
 * the permissions from a second table filtered by user roles and grid identifier.
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
public class DbGridPermissionsAction implements Action {


  private DbGridPermissionsManager getDbGridPermissionsManager() {
    return new DbGridPermissionsManager(
      new DbConnectionSource() {

        public Connection getConnection() {
          try {
            return ConnectionManager.getConnection(null);
          }
          catch (Exception ex) {
            return null;
          }
        }

        public void releaseConnection(Connection conn) {
          try {
            ConnectionManager.releaseConnection(conn,null);
          }
          catch (Exception ex) {
          }
        }

      },
      new JAIODbDigestDescriptor(),
      new JAIODbPermissionsDescriptor()
    );
  }


  public DbGridPermissionsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "dbGridPermissions";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
      DbGridPermissionsManager man = getDbGridPermissionsManager();
      man.setUsername(userSessionPars.getUsername());

      Object[] objs = (Object[])inputPar;
      String subaction = (String)objs[0];

      if ("getUserRoles".equals(subaction))
        return new VOResponse(man.getUserRoles());
      else if ("getUserGridPermissions".equals(subaction))
        return new VOResponse(man.getUserGridPermissions(
          (String)objs[1],
          (ArrayList)objs[2],
          (String[])objs[3],
          (boolean[])objs[4],
          (boolean[])objs[5],
          (boolean[])objs[6],
          (boolean[])objs[7]
        ));
      else if ("getLastGridDigest".equals(subaction))
        return new VOResponse(man.getLastGridDigest(
          (String)objs[1]
        ));
      else if ("getCurrentGridDigest".equals(subaction))
        return new VOResponse(man.getCurrentGridDigest(
          (String[])objs[1],
          (String)objs[2]
        ));
      else if ("storeGridPermissionsDefaults".equals(subaction)) {
        man.storeGridPermissionsDefaults(
            (String)objs[1],
            (String[])objs[2],
            (String[])objs[3],
            (boolean[])objs[4],
            (boolean[])objs[5],
            (boolean[])objs[6],
            (boolean[])objs[7]
        );
        return new VOResponse(Boolean.TRUE);
      }
      else if ("storeGridDigest".equals(subaction)) {
        man.storeGridDigest(
          (String)objs[1],
          (String)objs[2]
        );
        return new VOResponse(Boolean.TRUE);
      }
      else if ("deleteAllGridPermissionsPerFunctionId".equals(subaction)) {
        man.deleteAllGridPermissionsPerFunctionId(
          (String)objs[1]
        );
        return new VOResponse(Boolean.TRUE);
      }

      throw new UnsupportedOperationException(subaction);
    } catch (Throwable ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    }
  }





}
