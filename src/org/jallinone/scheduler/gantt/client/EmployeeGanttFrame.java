package org.jallinone.scheduler.gantt.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
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
import java.util.Calendar;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to show/edit/delete activities for the specified employee.</p>
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
public class EmployeeGanttFrame extends InternalFrame {
  GanttControl gantt = new GanttControl();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelFrom = new LabelControl();
  DateControl controlFrom = new DateControl();
  LabelControl labelTo = new LabelControl();
  DateControl controlTo = new DateControl();
  private EmployeeGanttController controller = null;
  private ServerGanttDataLocator ganttDataLocator = new ServerGanttDataLocator();
  GenericButton viewButton = new GenericButton(new ImageIcon(ClientUtils.getImage("gantt.gif")));
  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();
  LabelControl labelEmp = new LabelControl();
  CodLookupControl controlEmpCode = new CodLookupControl();
  TextControl controlName_1 = new TextControl();
  TextControl controlName_2 = new TextControl();


  public EmployeeGanttFrame(EmployeeGanttController controller) {
    this.controller = controller;
    try {
      jbInit();

      gantt.init(new String[]{
        ClientSettings.getInstance().getResources().getResource("scheduled activities")
      });
      gantt.setGridEnabled(false);
      gantt.setShowDescription(false);
      gantt.setEnableInsert(false);
      gantt.setEnableEdit(true);
      gantt.setEnableDelete(true);
//      gantt.setShowDescription(false);
      gantt.setGanttDataLocator(ganttDataLocator);
      ganttDataLocator.setServerMethodName("loadEmployeeActivitiesOnGantt");
      gantt.addAppointmentChangeListener(controller);

      // show appointments for a week...
      Calendar cal = Calendar.getInstance();
      cal.set(cal.HOUR_OF_DAY,0);
      cal.set(cal.MINUTE,0);
      cal.set(cal.SECOND,0);
      cal.set(cal.MILLISECOND,0);
      controlFrom.setValue(cal.getTime());
      gantt.setStartDate(cal.getTime());
      cal.set(cal.DAY_OF_MONTH,cal.get(cal.DAY_OF_MONTH)+6);
      controlTo.setValue(cal.getTime());
      gantt.setEndDate(cal.getTime());
      gantt.setAutoLoadData(false);

      // employees lookup...
      empDataLocator.setGridMethodName("loadEmployees");
      empDataLocator.setValidationMethodName("validateEmployeeCode");
      controlEmpCode.setLookupController(empController);
      controlEmpCode.setControllerMethodName("getEmployeesList");
      empController.setLookupDataLocator(empDataLocator);
      empController.setFrameTitle("employees");
      empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      empController.addLookup2ParentLink("employeeCodeSCH01", "employeeCodeSCH01");
//      empController.addLookup2ParentLink("name_1REG04", "name_1REG04");
//      empController.addLookup2ParentLink("name_2REG04", "name_2REG04");
      empController.setAllColumnVisible(false);
      empController.setVisibleColumn("employeeCodeSCH01", true);
      empController.setVisibleColumn("name_1REG04", true);
      empController.setVisibleColumn("name_2REG04", true);
      empController.setPreferredWidthColumn("name_1REG04",150);
      empController.setPreferredWidthColumn("name_2REG04",150);
      empController.setFramePreferedSize(new Dimension(430,400));
      empController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridEmployeeVO empVO = (GridEmployeeVO)empController.getLookupVO();
          gantt.getGanttParameters().put(ApplicationConsts.SCHEDULED_EMPLOYEE,empVO);
          controlEmpCode.setValue(empVO.getEmployeeCodeSCH01());
          controlName_1.setValue(empVO.getName_1REG04());
          controlName_2.setValue(empVO.getName_2REG04());
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() { }

      });



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
    viewButton.addActionListener(new EmployeeGanttFrame_viewButton_actionAdapter(this));

    filterPanel.setLayout(gridBagLayout1);
    labelFrom.setText("from date");
    labelTo.setText("to date");
    labelEmp.setText("employeeCodeSCH01");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("gantt diagram"));
    controlFrom.addFocusListener(new EmployeeGanttFrame_controlFrom_focusAdapter(this));
    controlTo.addFocusListener(new EmployeeGanttFrame_controlTo_focusAdapter(this));
    controlEmpCode.setMaxCharacters(20);
    this.getContentPane().add(gantt, BorderLayout.CENTER);
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    filterPanel.add(labelFrom,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlFrom,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelTo,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlTo,   new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(viewButton,    new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelEmp,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlEmpCode,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlName_1,   new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlName_2,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    controlName_1.setEnabled(false);
    controlName_2.setEnabled(false);
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
    if (controlEmpCode.getValue()==null || controlEmpCode.getValue()==null) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("you must specify employee code"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.ERROR_MESSAGE
      );
      return;
    }
    gantt.reloadData();
  }


}

class EmployeeGanttFrame_controlFrom_focusAdapter extends java.awt.event.FocusAdapter {
  EmployeeGanttFrame adaptee;

  EmployeeGanttFrame_controlFrom_focusAdapter(EmployeeGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlFrom_focusLost(e);
  }
}

class EmployeeGanttFrame_controlTo_focusAdapter extends java.awt.event.FocusAdapter {
  EmployeeGanttFrame adaptee;

  EmployeeGanttFrame_controlTo_focusAdapter(EmployeeGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlTo_focusLost(e);
  }
}

class EmployeeGanttFrame_viewButton_actionAdapter implements java.awt.event.ActionListener {
  EmployeeGanttFrame adaptee;

  EmployeeGanttFrame_viewButton_actionAdapter(EmployeeGanttFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.viewButton_actionPerformed(e);
  }
}
