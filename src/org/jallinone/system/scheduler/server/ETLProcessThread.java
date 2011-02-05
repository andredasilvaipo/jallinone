package org.jallinone.system.scheduler.server;

import java.sql.Connection;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.importdata.server.ImportDataBean;
import org.jallinone.system.importdata.server.LoadETLProcessFieldsBean;
import org.jallinone.system.languages.server.LanguagesBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.server.ConnectionManager;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Thread used for a scheduled ETL process.</p>
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
public class ETLProcessThread extends Thread {

  /** flag used to interrupt the while loop */
  private boolean interruptProcess = false;

  /** ETL process descriptor */
  private ETLProcessVO processVO = null;

  private ImportDataBean bean = new ImportDataBean();
  private LoadETLProcessFieldsBean fieldsBean = new LoadETLProcessFieldsBean();
  private LanguagesBean langsBean = new LanguagesBean();


  public ETLProcessThread(ETLProcessVO processVO) {
    this.processVO = processVO;
    start();
  }


  public void interrupt() {
    interruptProcess = true;
    super.interrupt();
  }


  public void run() {
    while(!interruptProcess) {
      try {
        if (processVO.getSchedulingTypeSYS23().equals(ApplicationConsts.SCHEDULING_TYPE_EVERY_DAY)) {
          long time = processVO.getStartTimeSYS23().getTime();
          if (time<System.currentTimeMillis()) {
            time = (System.currentTimeMillis()-time)%86400000L;
          }
          else {
            time = time-System.currentTimeMillis();
          }
          sleep(time);

          // time has expired: process must be started...
          startProcess();
        }
        else if (processVO.getSchedulingTypeSYS23().equals(ApplicationConsts.SCHEDULING_TYPE_EVERY_WEEK)) {
          long time = processVO.getStartTimeSYS23().getTime();
          if (time<System.currentTimeMillis()) {
            time = (System.currentTimeMillis()-time)%(86400000L*7L);
          }
          else {
            time = time-System.currentTimeMillis();
          }
          sleep(time);

          // time has expired: process must be started...
          startProcess();
        }
        else
          break;
      }
      catch (Exception ex) {
        break;
      }
    }
  }


  private void startProcess() {
    Connection conn = null;
    try {
      //conn = ConnectionManager.getConnection(context);
      conn = ConnectionManager.getConnection(null);
      // retrieve supported languages...
      Response res = langsBean.loadLanguages(null,"");
      if (res.isError())
        return;
      java.util.List langsVO = ((VOListResponse)res).getRows();

      // read from SYS24 table...
      res = fieldsBean.loadETLProcessFields(processVO.getProgressiveSYS23(),null);
      if (res.isError())
        return;
      java.util.List fieldsVO = ((VOListResponse)res).getRows();

      Response answer = bean.importData(processVO,fieldsVO,langsVO,"",null);
      if (answer.isError()) {
        conn.rollback();
        return;
      }
      conn.commit();
    }
    catch (Throwable ex) {
      Logger.error(null,this.getClass().getName(),"startProcess","Error while importing data",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return;
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, null);
      }
      catch (Exception ex1) {
      }
    }
  }


}
