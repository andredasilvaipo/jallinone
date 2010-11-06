package org.jallinone.system.importdata.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.registers.color.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantTypeVO;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.importdata.java.SelectableFieldVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to read a file system folder.</p>
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
public class GetFolderContentAction implements Action {


  public GetFolderContentAction() {}



  /**
   * @return request name
   */
  public final String getRequestName() {
    return "getFolderContent";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        null,
        inputPar,
        null
      ));

      Object[] objs = (Object[])inputPar;
      final String fileFormat = (String)objs[0];
      File file = (File)objs[1];
      if (file==null) {
        List vos = Arrays.asList(File.listRoots());
        return new VOListResponse(vos,false,vos.size());
      }

      if (file.isDirectory()) {
        List vos = Arrays.asList(file.listFiles(new FileFilter() {

          public boolean accept(File f) {
            return
                f.isDirectory() ||
                f.getName().toLowerCase().endsWith(".xls") && fileFormat.equals("XLS") ||
                f.getName().toLowerCase().endsWith(".csv") && fileFormat.equals("CSV1") ||
                f.getName().toLowerCase().endsWith(".csv") && fileFormat.equals("CSV2") ||
                f.getName().toLowerCase().endsWith(".txt") && fileFormat.equals("TXT");
          }

        }));
        vos = new ArrayList(vos);
        if (file.getParentFile()!=null) {
          vos.add(0,file.getParentFile());
        }
        else {
          vos.addAll(0,Arrays.asList(File.listRoots()));
        }
        return new VOListResponse(vos,false,vos.size());
      }
      else {
        if (!file.exists())
          return new ErrorResponse("File not exists");

        BufferedInputStream in = null;
        byte[] bytes = new byte[0];
        try {
          in = new BufferedInputStream(new FileInputStream(file));
          byte[] aux = null;
          byte[] bb = new byte[10000];
          int len = 0;
          while((len=in.read(bb))>0) {
            aux = bytes;
            bytes = new byte[aux.length+len];
            System.arraycopy(aux,0,bytes,0,aux.length);
            System.arraycopy(bb,0,bytes,aux.length,len);
          }
          in.close();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        finally {
          try {
            in.close();
          }
          catch (Exception ex1) {
          }
        }
        return new VOResponse(bytes);
      }
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(), "executeCommand", "Error while reading a folder", ex);
      return new ErrorResponse(ex.getMessage());
    }
  }



}

