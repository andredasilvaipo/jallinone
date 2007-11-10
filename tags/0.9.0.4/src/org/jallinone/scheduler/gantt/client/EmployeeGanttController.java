package org.jallinone.scheduler.gantt.client;

import org.jallinone.scheduler.activities.java.ScheduledEmployeeVO;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.Date;
import org.openswing.swing.gantt.client.*;
import java.math.BigDecimal;
import org.openswing.swing.table.columns.client.CodLookupColumn;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientSettings;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to show/edit/delete activities for the specified employee.</p>
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
public class EmployeeGanttController implements AppointmentChangeListener {

  private EmployeeGanttFrame frame = null;
  private CodLookupColumn empColumn = null;

  public EmployeeGanttController() {
    this.empColumn = empColumn;
    frame = new EmployeeGanttFrame(this);
    MDIFrame.getInstance().add(frame);
  }



  /**
   * Method called by the gantt control when changing its content.
   */
  public void appointmentChange(AppointmentChangeEvent event) {
    if (event.getEventType()==event.APPOINTMENT_CHANGED) {
      ArrayList oldapps = new ArrayList();
      ArrayList newapps = new ArrayList();
      oldapps.add(event.getOldAppointment());
      newapps.add(event.getNewAppointment());
      Response res = ClientUtils.getData("updateScheduledEmployees",new ArrayList[]{oldapps,newapps});
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            MDIFrame.getInstance(),
            ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
    }
    else if (event.getEventType()==event.APPOINTMENT_DELETED) {
      ArrayList apps = new ArrayList();
      apps.add(event.getOldAppointment());
      Response res = ClientUtils.getData("deleteScheduledEmployees",apps);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            MDIFrame.getInstance(),
            ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }

}
