package org.jallinone.scheduler.appointments.client;

import java.math.BigDecimal;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import org.jallinone.scheduler.activities.java.EmployeeActivityVO;
import java.util.Hashtable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import org.openswing.swing.util.java.Consts;
import org.jallinone.scheduler.activities.client.ScheduledActivityController;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import java.awt.event.MouseMotionAdapter;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.scheduler.activities.java.ScheduledEmployeeVO;
import org.jallinone.employees.java.GridEmployeeVO;
import org.jallinone.employees.java.EmployeeCalendarVO;
import org.jallinone.employees.java.DetailEmployeeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used to view employee day appointments.</p>
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
public class Day extends JPanel {

  public final int hourH = 50;
  private final int hourW = 40;
  private final int appW = 400;

  private Date currentDay = Calendar.getInstance().getTime();

  /** list of appointments; objects of type EmployeeActivityVO */
  private ArrayList rows = new ArrayList();

  /** collection of pairs: int[x1,y1,x2,y2], EmployeeActivityVO */
  private Hashtable appointments = new Hashtable();

  /** current selected appointment */
  private int[] selectedApp = null;

  /** new appointment */
  private int[] newApp = null;

  /** employee v.o. */
  private DetailEmployeeVO empVO = null;

  /** employee working hours */
  private ArrayList empCalendar = new ArrayList();

  /** working hours */
  private EmployeeCalendarVO empCalVO = null;


  public Day() {
    setSize(4000,24*hourH);
    setPreferredSize(new Dimension(4000,24*hourH));
    setMinimumSize(new Dimension(4000,24*hourH));
    addMouseMotionListener(new MouseMotionAdapter() {

      public void mouseDragged(MouseEvent e) {
        if (newApp!=null) {
          newApp[2] = e.getX();
          newApp[3] = e.getY();
        }
        Day.this.repaint();
      }

    });
    addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        if (newApp==null)
          newApp = new int[]{e.getX(),e.getY(),e.getX(),e.getY()};
      }


      public void mouseReleased(MouseEvent e) {
        if (newApp==null || newApp[1]>=newApp[3]) {
          newApp = null;
          return;
        }

        final ScheduledActivityController c = new ScheduledActivityController(null,null,null,false) {

          /**
           * Method called by the Form panel to insert new data.
           * @param newValueObject value object to save
           * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
           */
          public Response insertRecord(ValueObject newPersistentObject) throws Exception {
            Response response = super.insertRecord(newPersistentObject);
            if (!response.isError()) {
              ScheduledActivityVO vo = (ScheduledActivityVO)((VOResponse)response).getVo();
              ScheduledEmployeeVO schEmpVO = new ScheduledEmployeeVO();
              schEmpVO.setCompanyCodeSys01SCH07(vo.getCompanyCodeSys01SCH06());
              schEmpVO.setDescriptionSYS10(empVO.getTaskDescriptionSYS10());
              schEmpVO.setDurationSCH07(vo.getEstimatedDurationSCH06());
              schEmpVO.setEmployeeCodeSCH01(empVO.getEmployeeCodeSCH01());
              schEmpVO.setEndDateSCH07(vo.getEstimatedEndDateSCH06());
              schEmpVO.setProgressiveReg04SCH07(empVO.getProgressiveReg04SCH01());
              schEmpVO.setProgressiveSch06SCH07(vo.getProgressiveSCH06());
              schEmpVO.setStartDateSCH07(vo.getStartDateSCH06());
              schEmpVO.setTaskCodeREG07(empVO.getTaskCodeReg07SCH01());
              ArrayList list = new ArrayList();
              list.add(schEmpVO);
              Response res = ClientUtils.getData("insertScheduledEmployees",list);
              if (res.isError())
                return res;
              else
                setEmployeeVO(empVO);
            }
            return response;
          }

        };
        ScheduledActivityVO vo = (ScheduledActivityVO)c.getDetailFrame().getMainForm().getVOModel().getValueObject();

        int h = newApp[1]/hourH;
        int m = (newApp[1]-h*hourH)*69/hourH;
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDay);
        cal.set(cal.HOUR_OF_DAY,h);
        cal.set(cal.MINUTE,m);
        vo.setStartDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()));

        h = newApp[3]/hourH;
        m = (newApp[3]-h*hourH)*60/hourH;
        cal.setTime(currentDay);
        cal.set(cal.HOUR_OF_DAY,h);
        cal.set(cal.MINUTE,m);
        vo.setEstimatedEndDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()));
        vo.setEstimatedDurationSCH06(new BigDecimal((vo.getEstimatedEndDateSCH06().getTime()-vo.getStartDateSCH06().getTime())/60000));

        c.getDetailFrame().getMainForm().getForm().pull();

        newApp = null;
        repaint();
      }


      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) &&
            (e.getClickCount()==1 || e.getClickCount()==2)) {
          Enumeration en = appointments.keys();
          int[] app = null;
          selectedApp = null;
          while(en.hasMoreElements()) {
            app = (int[])en.nextElement();
            if (e.getX()>=app[0] && e.getX()<=app[2] && e.getY()>=app[1] && e.getY()<=app[3]) {
              selectedApp = app;
              break;
            }
          }
          Day.this.repaint();
          if (selectedApp!=null && e.getClickCount()==2) {
            EmployeeActivityVO vo = (EmployeeActivityVO)appointments.get(selectedApp);
            ScheduledActivityController c = new ScheduledActivityController(null,null,new ScheduledActivityPK(
              vo.getCompanyCodeSys01SCH06(),
              vo.getProgressiveSCH06()
            ),false) {
              public void afterInsertData() {
                super.afterInsertData();
                setEmployeeVO(empVO);
              }
              public void afterEditData() {
                super.afterEditData();
                setEmployeeVO(empVO);
              }
              public void afterDeleteData() {
                super.afterDeleteData();
                setEmployeeVO(empVO);
              }
            };
          }
        }
      }
    });
  }


  /**
   * Reload appointments assigned to the current employee.
   * @param progressiveReg04SYS03 employee identifier
   */
  public final void setEmployeeVO(DetailEmployeeVO empVO) {
    this.empVO = empVO;
    new Thread() {
      public void run() {
        GridParams gridParams = new GridParams();
        gridParams.getCurrentSortedColumns().add("startDateSCH07");
        gridParams.getCurrentSortedVersusColumns().add("ASC");
        gridParams.getOtherGridParams().put(ApplicationConsts.DATE_FILTER,currentDay);
        gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,Day.this.empVO.getProgressiveReg04SCH01());
        Response res = ClientUtils.getData("loadEmployeeActivities",gridParams);
        if (res.isError()) {
          JOptionPane.showMessageDialog(
              MDIFrame.getInstance(),
              ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
        else {
          // reset the panel content
          selectedApp = null;
          rows = new ArrayList(((VOListResponse)res).getRows());
        }
        repaint();
      }
    }.start();
  }


  public void paint(Graphics g) {
    super.paint(g);
   SimpleDateFormat sdf = null;
    try {
      sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_TIME));
    }
    catch (Exception ex) {
    }


    int startH = 0;
    int endH = 0;
    int startM = 0;
    int endM = 0;
    Calendar cal = Calendar.getInstance();

    g.setColor(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
    g.fillRect(hourW,0,getWidth()-hourW,getHeight());

    // fill in working hours...
    if (empCalVO!=null) {
      g.setColor(Color.lightGray);
      cal.setTime(
        empCalVO.getMorningStartHourSCH02()==null?
        empCalVO.getAfternoonStartHourSCH02():
        empCalVO.getMorningStartHourSCH02()
      );
      startH = cal.get(cal.HOUR_OF_DAY);
      startM = cal.get(cal.MINUTE);
      g.fillRect(hourW,0,getWidth()-hourW,startH*hourH+startM*hourH/60);

      cal.setTime(
        empCalVO.getMorningEndHourSCH02()==null?
        empCalVO.getAfternoonStartHourSCH02():
        empCalVO.getMorningEndHourSCH02()
      );
      startH = cal.get(cal.HOUR_OF_DAY);
      startM = cal.get(cal.MINUTE);
      cal.setTime(
        empCalVO.getAfternoonStartHourSCH02()==null?
        empCalVO.getMorningEndHourSCH02():
        empCalVO.getAfternoonStartHourSCH02()
      );
      endH = cal.get(cal.HOUR_OF_DAY);
      endM = cal.get(cal.MINUTE);
      g.fillRect(hourW,startH*hourH+startM*hourH/60,getWidth()-hourW,(endH-startH)*hourH+(endM-startM)*hourH/60);

      g.setColor(Color.lightGray);
      cal.setTime(
        empCalVO.getAfternoonEndHourSCH02()==null?
        empCalVO.getMorningEndHourSCH02():
        empCalVO.getAfternoonEndHourSCH02()
      );
      startH = cal.get(cal.HOUR_OF_DAY);
      startM = cal.get(cal.MINUTE);
      g.fillRect(hourW,startH*hourH+startM*hourH/60,getWidth()-hourW,getHeight()-(startH*hourH+startM*hourH/60));
    }

    // create hour rectangles...
    for(int i=0;i<24;i++) {
      g.setColor(Color.lightGray);
      g.fill3DRect(0,i*hourH,hourW,hourH,true);
      g.setColor(Color.gray);
      g.drawRect(hourW,i*hourH,this.getWidth()-hourW,hourH);
      g.setColor(Color.black);
      g.drawString(String.valueOf(i)+":00",2,i*hourH+12);
    }

    // fill in all appointments that are absences...
    EmployeeActivityVO vo = null;
    int startW = 0;
    Calendar day = Calendar.getInstance();
    day.setTime(currentDay);
    appointments.clear();
    for(int i=0;i<rows.size();i++) {
      vo = (EmployeeActivityVO)rows.get(i);
      if (!vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ABSENCE) &&
          !vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_HOLIDAY) &&
          !vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ILLNESS))
        continue;
      if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_CALL_OUT))
        g.setColor(new Color(240,200,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_APPOINTMENT))
        g.setColor(new Color(250,210,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_PHONE_CALL))
        g.setColor(new Color(240,210,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_MEETING))
        g.setColor(new Color(250,220,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_SEND_EMAIL))
        g.setColor(new Color(240,220,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_SEND_FAX))
        g.setColor(new Color(250,230,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ABSENCE))
        g.setColor(new Color(230,230,230));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_HOLIDAY))
        g.setColor(new Color(220,220,220));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ILLNESS))
        g.setColor(new Color(225,225,225));
      else
        g.setColor(new Color(240,230,200));

      cal.setTime(vo.getStartDateSCH07());
      startH = 0;
      startM = 0;
      if (cal.get(cal.DAY_OF_MONTH)==day.get(day.DAY_OF_MONTH)) {
        startH = cal.get(cal.HOUR_OF_DAY);
        startM = cal.get(cal.MINUTE);
      }

      cal.setTime(vo.getEndDateSCH07());
      endH = 24;
      endM = 60;
      if (cal.get(cal.DAY_OF_MONTH)==day.get(day.DAY_OF_MONTH)) {
        endH = cal.get(cal.HOUR_OF_DAY);
        endM = cal.get(cal.MINUTE);
      }

      g.fillRect(
        hourW,
        startH*hourH+startM*hourH/60,
        getWidth()-hourW,
        (endH-startH)*hourH-startM*hourH/60+endM*hourH/60
      );
      g.setColor(Color.gray);
      g.drawRect(
        hourW,
        startH*hourH+startM*hourH/60,
        getWidth()-hourW,
        (endH-startH)*hourH-startM*hourH/60+endM*hourH/60
      );
      g.setColor(Color.black);
      g.drawString(
        sdf.format(vo.getStartDateSCH07())+" - "+vo.getDescriptionSCH06(),
        hourW+3,
        startH*hourH+startM*hourH/60+14
      );
      appointments.put(new int[]{
        hourW,
        startH*hourH+startM*hourH/60,
        getWidth()-hourW,
        endH*hourH+endM*hourH/60
      },vo);
    }

    // fill in all appointments that are not absences...
    for(int i=0;i<rows.size();i++) {
      vo = (EmployeeActivityVO)rows.get(i);
      if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ABSENCE) ||
          vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_HOLIDAY) ||
          vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ILLNESS))
        continue;
      if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_CALL_OUT))
        g.setColor(new Color(240,200,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_APPOINTMENT))
        g.setColor(new Color(250,210,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_PHONE_CALL))
        g.setColor(new Color(240,210,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_MEETING))
        g.setColor(new Color(250,220,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_SEND_EMAIL))
        g.setColor(new Color(240,220,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_SEND_FAX))
        g.setColor(new Color(250,230,200));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ABSENCE))
        g.setColor(new Color(230,230,230));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_HOLIDAY))
        g.setColor(new Color(220,220,220));
      else if (vo.getActivityTypeSCH06().equals(ApplicationConsts.ACT_ILLNESS))
        g.setColor(new Color(225,225,225));
      else
        g.setColor(new Color(240,230,200));

      cal.setTime(vo.getStartDateSCH07());
      startH = 0;
      startM = 0;
      if (cal.get(cal.DAY_OF_MONTH)==day.get(day.DAY_OF_MONTH)) {
        startH = cal.get(cal.HOUR_OF_DAY);
        startM = cal.get(cal.MINUTE);
      }

      cal.setTime(vo.getEndDateSCH07());
      endH = 24;
      endM = 60;
      if (cal.get(cal.DAY_OF_MONTH)==day.get(day.DAY_OF_MONTH)) {
        endH = cal.get(cal.HOUR_OF_DAY);
        endM = cal.get(cal.MINUTE);
      }

      startW = getNextW(startW,startH*hourH+startM*hourH/60,endH*hourH+endM*hourH/60);

      g.fillRect(
        hourW+startW,
        startH*hourH+startM*hourH/60,
        appW,
        (endH-startH)*hourH-startM*hourH/60+endM*hourH/60
      );
      g.setColor(Color.gray);
      g.drawRect(
        hourW+startW,
        startH*hourH+startM*hourH/60,
        appW,
        (endH-startH)*hourH-startM*hourH/60+endM*hourH/60
      );
      g.setColor(Color.black);
      g.drawString(
        sdf.format(vo.getStartDateSCH07())+" - "+vo.getDescriptionSCH06(),
        hourW+startW+3,
        startH*hourH+startM*hourH/60+14
      );
      appointments.put(new int[]{
        hourW+startW,
        startH*hourH+startM*hourH/60,
        hourW+startW+appW,
        endH*hourH+endM*hourH/60
      },vo);
    }

    if (selectedApp!=null) {
      g.setColor(Color.black);
      g.drawRect(selectedApp[0],selectedApp[1],selectedApp[2]-selectedApp[0],selectedApp[3]-selectedApp[1]);
      g.drawRect(selectedApp[0]+1,selectedApp[1]+1,selectedApp[2]-selectedApp[0]-2,selectedApp[3]-selectedApp[1]-2);
    }
    if (newApp!=null) {
      g.setColor(Color.gray);
      g.drawRect(newApp[0],newApp[1],newApp[2]-newApp[0],newApp[3]-newApp[1]);
    }

  }


  /**
   * @param start starting pixel (in height)
   * @param end ending pixel (in height)
   * @return int starting pixel (in width)
   */
  private int getNextW(int startW,int start,int end) {
    Enumeration en = appointments.keys();
    int[] app = null;
    boolean found = false;
    while(en.hasMoreElements()) {
      app = (int[])en.nextElement();
      if (start>=app[1] && start<=app[3] ||
          end>=app[1] && end<=app[3]) {
        startW += appW+2;
        found = true;
        break;
      }
    }
    if (found)
      return startW;
    else
      return 0;
  }


  /**
   * Set day to view
   * @param currentDay day to view
   */
  public void setCurrentDay(Date currentDay) {
    this.currentDay = currentDay;
    EmployeeCalendarVO vo = null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(currentDay);
    for(int i=0;i<empCalendar.size();i++) {
      vo = (EmployeeCalendarVO)empCalendar.get(i);
      if (vo.getDayOfWeekSCH02().equals(String.valueOf(cal.get(cal.DAY_OF_WEEK)))) {
        empCalVO = vo;
        break;
      }
    }
    if (empVO!=null)
      setEmployeeVO(empVO);
  }


  /**
   * Set the employee working hours.
   * @param empCalendar employee working hours
   */
  public void setEmpCalendar(ArrayList empCalendar) {
    this.empCalendar = empCalendar;
    EmployeeCalendarVO vo = null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(currentDay);
    for(int i=0;i<empCalendar.size();i++) {
      vo = (EmployeeCalendarVO)empCalendar.get(i);
      if (vo.getDayOfWeekSCH02().equals(String.valueOf(cal.get(cal.DAY_OF_WEEK)))) {
        empCalVO = vo;
        break;
      }
    }
  }
  public EmployeeCalendarVO getEmpCalVO() {
    return empCalVO;
  }


}
