package org.jallinone.scheduler.gantt.client;

import org.jallinone.scheduler.activities.java.ScheduledEmployeeVO;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.Date;
import org.openswing.swing.gantt.client.*;
import java.math.BigDecimal;
import org.openswing.swing.table.columns.client.CodLookupColumn;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to allocate employees for the specified scheduled activity.</p>
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
public class ActivityGanttController implements AppointmentChangeListener {

  private ScheduledEmployeeVO empVO = null;
  private ActivityGanttFrame frame = null;
  private CodLookupColumn empColumn = null;

  public ActivityGanttController(CodLookupColumn empColumn,boolean canInsert,Date startDate,Date endDate,ScheduledEmployeeVO empVO) {
    this.empColumn = empColumn;
    this.empVO = empVO;
    frame = new ActivityGanttFrame(canInsert,startDate,endDate,this);
    MDIFrame.getInstance().add(frame);
  }


  public ScheduledEmployeeVO getEmpVO() {
    return empVO;
  }


  /**
   * Method called by the gantt control when changing its content.
   */
  public void appointmentChange(AppointmentChangeEvent event) {
    try {
      if (event.getEventType() == event.NEW_APPOINTMENT) {
        empVO.setEmployeeCodeSCH01( (String) frame.getGantt().getValueAt(event.
            getRowNumber(), 0));
        empColumn.forceValidate();
        empVO.setProgressiveReg04SCH07( (BigDecimal) frame.getGantt().
                                       getValueAt(event.getRowNumber(), 1));
        empVO.setStartDateSCH07( ( (ScheduledEmployeeVO) event.
                                  getOldAppointment()).getStartDateSCH07());
        empVO.setEndDateSCH07( ( (ScheduledEmployeeVO) event.getOldAppointment()).
                              getEndDateSCH07());
        empVO.setDurationSCH07( ( (ScheduledEmployeeVO) event.getOldAppointment()).
                               getDurationSCH07());
        frame.closeFrame();
      }
      else if (event.getEventType() == event.APPOINTMENT_CHANGED) {
        empVO.setStartDateSCH07( ( (ScheduledEmployeeVO) event.
                                  getNewAppointment()).getStartDateSCH07());
        empVO.setEndDateSCH07( ( (ScheduledEmployeeVO) event.getNewAppointment()).
                              getEndDateSCH07());
        empVO.setDurationSCH07( ( (ScheduledEmployeeVO) event.getNewAppointment()).
                               getDurationSCH07());
        frame.closeFrame();
      }
    }
    catch (Exception ex) {
    }
  }

}
