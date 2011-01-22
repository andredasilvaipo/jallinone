package org.jallinone.scheduler.gantt.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.model.client.VOModel;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import java.util.HashSet;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.employees.java.GridEmployeeVO;
import org.openswing.swing.gantt.client.*;
import org.openswing.swing.gantt.client.GanttDataLocator;
import java.util.Date;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to allocate employees for the specified scheduled activity.</p>
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
public class ActivityGanttFrame extends InternalFrame {
  GanttControl gantt = new GanttControl();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelFrom = new LabelControl();
  DateControl controlFrom = new DateControl();
  LabelControl labelTo = new LabelControl();
  DateControl controlTo = new DateControl();
  private ActivityGanttController controller = null;
  private ServerGanttDataLocator ganttDataLocator = new ServerGanttDataLocator();
  GenericButton viewButton = new GenericButton(new ImageIcon(ClientUtils.getImage("gantt.gif")));


  public ActivityGanttFrame(boolean canInsert,Date startDate,Date endDate,ActivityGanttController controller) {
    this.controller = controller;
    try {
      jbInit();

      gantt.init(new String[]{
        "",
        "",
        ClientSettings.getInstance().getResources().getResource("employees")
      });
      gantt.setGridColumnVisible(0,false);
      gantt.setGridColumnVisible(0,false);
      gantt.setGridEnabled(false);
      gantt.setEnableInsert(canInsert);
      gantt.setEnableEdit(!canInsert);
      gantt.setEnableDelete(false);
//      gantt.setShowDescription(false);
      gantt.setGanttDataLocator(ganttDataLocator);
      ganttDataLocator.setServerMethodName("loadScheduledEmployeesOnGantt");
      gantt.addAppointmentChangeListener(controller);

      controlFrom.setValue(startDate);
      controlTo.setValue(endDate);
      gantt.setStartDate(startDate);
      gantt.setEndDate(endDate);

      gantt.getGanttParameters().put(ApplicationConsts.SCHEDULED_EMPLOYEE,controller.getEmpVO());
      gantt.getGanttParameters().put(ApplicationConsts.START_DATE,controlFrom.getValue());
      gantt.getGanttParameters().put(ApplicationConsts.END_DATE,controlTo.getValue());

      setSize(750,500);
      setMinimumSize(new Dimension(750,500));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    viewButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("reload data"));
    viewButton.addActionListener(new ActivityGanttFrame_viewButton_actionAdapter(this));

    filterPanel.setLayout(gridBagLayout1);
    labelFrom.setText("from date");
    labelTo.setText("to date");
    this.setTitle(
      ClientSettings.getInstance().getResources().getResource("gantt diagram")+
      " - "+
      ClientSettings.getInstance().getResources().getResource("employees having task")+
      " '"+
      controller.getEmpVO().getDescriptionSYS10()+
      "'"
    );
    controlFrom.addFocusListener(new ActivityGanttFrame_controlFrom_focusAdapter(this));
    controlTo.addFocusListener(new ActivityGanttFrame_controlTo_focusAdapter(this));
    this.getContentPane().add(gantt, BorderLayout.CENTER);
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    filterPanel.add(labelFrom,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlFrom,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelTo,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlTo,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(viewButton,    new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  void controlFrom_focusLost(FocusEvent e) {
    if (controlFrom.getValue()==null || controlFrom.getValue().equals(gantt.getStartDate()))
      return;
    gantt.setStartDate((java.util.Date)controlFrom.getValue());
  }


  void controlTo_focusLost(FocusEvent e) {
    if (controlTo.getValue()==null || controlTo.getValue().equals(gantt.getEndDate()))
      return;
    gantt.setEndDate((java.util.Date)controlTo.getValue());
  }


  public GanttControl getGantt() {
    return gantt;
  }


  void viewButton_actionPerformed(ActionEvent e) {
    if (controlFrom.getValue()==null || controlTo.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("you must specify both dates"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.ERROR_MESSAGE
      );
      return;
    }
    gantt.reloadData();
  }


}

class ActivityGanttFrame_controlFrom_focusAdapter extends java.awt.event.FocusAdapter {
  ActivityGanttFrame adaptee;

  ActivityGanttFrame_controlFrom_focusAdapter(ActivityGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlFrom_focusLost(e);
  }
}

class ActivityGanttFrame_controlTo_focusAdapter extends java.awt.event.FocusAdapter {
  ActivityGanttFrame adaptee;

  ActivityGanttFrame_controlTo_focusAdapter(ActivityGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlTo_focusLost(e);
  }
}

class ActivityGanttFrame_viewButton_actionAdapter implements java.awt.event.ActionListener {
  ActivityGanttFrame adaptee;

  ActivityGanttFrame_viewButton_actionAdapter(ActivityGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.viewButton_actionPerformed(e);
  }
}
