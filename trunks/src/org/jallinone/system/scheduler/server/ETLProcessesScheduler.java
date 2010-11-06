package org.jallinone.system.scheduler.server;

import java.util.ArrayList;
import org.jallinone.system.importdata.server.LoadETLProcessesBean;
import java.sql.Connection;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.commons.java.ApplicationConsts;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Singleton class used to scheduling ETL processes.</p>
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
public class ETLProcessesScheduler {

  /** unique instance of this class */
  private static ETLProcessesScheduler instance = null;

  /** list of sscheduled ETL processes */
  private ArrayList scheduledProcesses = new ArrayList();

  private LoadETLProcessesBean bean = new LoadETLProcessesBean();


  public static ETLProcessesScheduler getInstance() {
    if (instance==null)
      instance = new ETLProcessesScheduler();
    return instance;
  }

  private ETLProcessesScheduler() { }


  /**
   * Restart all ETL processes.
   */
  public void restartProcesses() {
    synchronized(scheduledProcesses) {
      removeAllProcesses();

      Connection conn = null;
      try {
        conn = ConnectionManager.getConnection(null);
        bean.setConn(conn);

        GridParams gridParams = new GridParams();
        Response answer = bean.loadETLProcesses(gridParams,"",null);
        if (!answer.isError()) {
          java.util.List list = ((VOListResponse)answer).getRows();
          ETLProcessVO vo = null;
          for(int i=0;i<list.size();i++) {
            vo = (ETLProcessVO)list.get(i);
            if (!vo.getSchedulingTypeSYS23().equals(ApplicationConsts.SCHEDULING_TYPE_NO_SCHED)) {
              scheduledProcesses.add(new ETLProcessThread(vo));
            }
          }
        }
      }
      catch (Throwable ex) {
        Logger.error(null,this.getClass().getName(),"restartProcesses","Error while fetching ETL processes list",ex);
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


  /**
   * Stop all ETL processes.
   */
  public void stopProcesses() {
    synchronized(scheduledProcesses) {
      removeAllProcesses();
    }
  }


  /**
   * Remove all ETL processes.
   */
  private void removeAllProcesses() {
    ETLProcessThread t = null;
    while(scheduledProcesses.size()>0) {
      t = (ETLProcessThread )scheduledProcesses.remove(0);
      t.interrupt();
    }
  }


}
