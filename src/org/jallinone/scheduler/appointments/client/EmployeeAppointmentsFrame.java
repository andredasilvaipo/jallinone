package org.jallinone.scheduler.appointments.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import com.toedter.calendar.JCalendar;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.Locale;
import org.openswing.swing.util.client.ClientSettings;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.employees.java.GridEmployeeVO;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.subjects.java.SubjectPK;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.employees.java.DetailEmployeeVO;
import java.util.Calendar;
import javax.swing.border.*;
import java.awt.event.*;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import java.util.ArrayList;
import org.jallinone.scheduler.activities.java.EmployeeActivityVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to view employee day appointments</p>
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
public class EmployeeAppointmentsFrame extends InternalFrame {
  JPanel filterPanel = new JPanel();
  JPanel dayPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane dayScrollPane = new JScrollPane();
  Day day = new Day();
  JCalendar calendar = new JCalendar();
  LabelControl labelEmp = new LabelControl();
  CodLookupControl controlEmpCode = new CodLookupControl();
  TextControl controlName1 = new TextControl();
  TextControl controlName2 = new TextControl();
  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();
  private DetailEmployeeVO empVO = null;
  JPanel findPanel = new JPanel();
  TitledBorder titledBorder1;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelSubjectType = new LabelControl();
  CodLookupControl filterPeopleButton = new CodLookupControl();
  CodLookupControl filterOrgButton = new CodLookupControl();
  TextControl controlName_1 = new TextControl();
  TextControl controlName_2 = new TextControl();
  LabelControl labelActType = new LabelControl();
  ComboBoxControl controlActType = new ComboBoxControl();
  GenericButton findButton = new GenericButton(new ImageIcon(ClientUtils.getImage("filter.gif")));
  LabelControl labelFromDate = new LabelControl();
  DateControl controlFromDate = new DateControl();
  ComboBoxControl controlSubjectType = new ComboBoxControl();

  LookupController peopleController = new LookupController();
  LookupServerDataLocator peopleDataLocator = new LookupServerDataLocator();
  LookupController orgController = new LookupController();
  LookupServerDataLocator orgDataLocator = new LookupServerDataLocator();
  CardLayout cardLayout2 = new CardLayout();
  JPanel cardPanel2 = new JPanel();


  public EmployeeAppointmentsFrame(BigDecimal progressiveReg04SYS03,String companyCodeSys01SYS03,String empCode,String name_1,String name_2) {
    try {
      jbInit();
      init();

      // initialize calendar...
      calendar.setLocale(new Locale(ClientSettings.getInstance().getResources().getLanguageId()));
      calendar.getDayChooser().addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
          Calendar cal = Calendar.getInstance();
          cal.setTime(calendar.getCalendar().getTime());
          cal.set(cal.DAY_OF_MONTH,cal.get(cal.DAY_OF_MONTH)+1);
          cal.set(cal.HOUR_OF_DAY,0);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          controlFromDate.setValue(cal.getTime());

          day.setCurrentDay(calendar.getCalendar().getTime());
        }
      });


      // employees lookup...
      empDataLocator.setGridMethodName("loadEmployees");
      empDataLocator.setValidationMethodName("validateEmployeeCode");
      controlEmpCode.setLookupController(empController);
      controlEmpCode.setControllerMethodName("getEmployeesList");
      empController.setLookupDataLocator(empDataLocator);
      empController.setFrameTitle("employees");
      empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      empController.addLookup2ParentLink("employeeCodeSCH01", "employeeCodeSCH01");
      empController.addLookup2ParentLink("companyCodeSys01SCH01", "companyCodeSys01SYS03");
      empController.addLookup2ParentLink("name_1REG04", "firstNameSYS03");
      empController.addLookup2ParentLink("name_2REG04", "lastNameSYS03");
      empController.addLookup2ParentLink("progressiveReg04SCH01", "progressiveReg04SYS03");
      empController.setAllColumnVisible(false);
      empController.setVisibleColumn("employeeCodeSCH01", true);
      empController.setVisibleColumn("name_1REG04", true);
      empController.setVisibleColumn("name_2REG04", true);
      empController.setPreferredWidthColumn("name_1REG04",150);
      empController.setPreferredWidthColumn("name_2REG04",150);
      empController.setFramePreferedSize(new Dimension(430,400));
      empController.addLookupListener(new LookupListener() {

        /**
         * Method called when lookup code is validated and when code is selected on lookup grid frame.
         * @param validated <code>true</code> if lookup code is correclty validated, <code>false</code> otherwise
         */
        public void codeValidated(boolean validated) {}


        /**
         * Method called when lookup code is changed (also when is set to "" or null)
         * @param parentVO lookup container v.o.
         * @param parentChangedAttributes lookup container v.o. attributes
         */
        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridEmployeeVO vo = (GridEmployeeVO)empController.getLookupVO();
          controlName1.setValue(vo.getName_1REG04());
          controlName2.setValue(vo.getName_2REG04());
          loadEmployeeDetail(vo.getCompanyCodeSys01SCH01(),vo.getProgressiveReg04SCH01());
        }


        /**
         * Method called before code validation and on lookup button click.
         */
        public void beforeLookupAction(ValueObject parentVO) {}


        /**
         * Validation is forced.
         */
        public void forceValidate() {}

      });

      // fill in the employee control...
      controlEmpCode.setValue(empCode);
      controlName1.setValue(name_1);
      controlName2.setValue(name_2);

      controlFromDate.setValue(new java.sql.Date(System.currentTimeMillis()));

      setSize(750,600);
      MDIFrame.getInstance().add(this,true);

      loadEmployeeDetail(companyCodeSys01SYS03,progressiveReg04SYS03);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
//    controlActType.getComboBox().setSelectedIndex(-1);

      // people lookup...
    peopleDataLocator.setGridMethodName("loadSubjectPerName");
    peopleDataLocator.setValidationMethodName("");
    filterPeopleButton.setLookupController(peopleController);
    peopleController.setLookupDataLocator(peopleDataLocator);
    peopleController.setFrameTitle("people");
    peopleController.setLookupValueObjectClassName("org.jallinone.subjects.java.PeopleVO");
    peopleController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
    peopleController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
    peopleController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
    peopleController.setAllColumnVisible(false);
    peopleController.setVisibleColumn("name_1REG04", true);
    peopleController.setVisibleColumn("name_2REG04", true);
    peopleController.setVisibleColumn("addressREG04", true);
    peopleController.setVisibleColumn("cityREG04", true);
    peopleController.setVisibleColumn("provinceREG04", true);
    peopleController.setVisibleColumn("countryREG04", true);
    peopleController.setVisibleColumn("zipREG04", true);
    peopleController.setHeaderColumnName("addressREG04", "address");
    peopleController.setHeaderColumnName("cityREG04", "city");
    peopleController.setHeaderColumnName("provinceREG04", "prov");
    peopleController.setHeaderColumnName("countryREG04", "country");
    peopleController.setHeaderColumnName("zipREG04", "zip");
    peopleController.setPreferredWidthColumn("name_1REG04", 120);
    peopleController.setPreferredWidthColumn("name_2REG04", 120);
    peopleController.setPreferredWidthColumn("addressREG04", 200);
    peopleController.setPreferredWidthColumn("provinceREG04", 50);
    peopleController.setPreferredWidthColumn("countryREG04", 70);
    peopleController.setPreferredWidthColumn("zipREG04", 50);
    peopleController.setFramePreferedSize(new Dimension(740,500));
    peopleController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        PeopleVO vo = (PeopleVO)peopleController.getLookupVO();
        controlName_1.setValue(vo.getName_1REG04());
        controlName_2.setValue(vo.getName_2REG04());
        filterPeopleButton.setValue(vo.getProgressiveREG04());
      }

      public void beforeLookupAction(ValueObject parentVO) {
        peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,empVO.getCompanyCodeSys01REG04());
        peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE);
      }

      public void forceValidate() {}

    });


    // organization lookup...
    orgDataLocator.setGridMethodName("loadSubjectPerName");
    orgDataLocator.setValidationMethodName("");
    filterOrgButton.setLookupController(orgController);
    orgController.setLookupDataLocator(orgDataLocator);
    orgController.setFrameTitle("organizations");
    orgController.setLookupValueObjectClassName("org.jallinone.subjects.java.OrganizationVO");
    orgController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
    orgController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
    orgController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
    orgController.setAllColumnVisible(false);
    orgController.setVisibleColumn("name_1REG04", true);
    orgController.setVisibleColumn("addressREG04", true);
    orgController.setVisibleColumn("cityREG04", true);
    orgController.setVisibleColumn("provinceREG04", true);
    orgController.setVisibleColumn("countryREG04", true);
    orgController.setVisibleColumn("zipREG04", true);
    orgController.setHeaderColumnName("addressREG04", "address");
    orgController.setHeaderColumnName("cityREG04", "city");
    orgController.setHeaderColumnName("provinceREG04", "prov");
    orgController.setHeaderColumnName("countryREG04", "country");
    orgController.setHeaderColumnName("zipREG04", "zip");
    orgController.setPreferredWidthColumn("name_1REG04", 150);
    orgController.setPreferredWidthColumn("addressREG04", 200);
    orgController.setPreferredWidthColumn("provinceREG04", 50);
    orgController.setPreferredWidthColumn("zipREG04", 50);
    orgController.setFramePreferedSize(new Dimension(740,500));
    orgController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        OrganizationVO vo = (OrganizationVO)orgController.getLookupVO();
        controlName_1.setValue(vo.getName_1REG04());
        controlName_2.setValue(vo.getName_2REG04());
        filterOrgButton.setValue(vo.getProgressiveREG04());
      }

      public void beforeLookupAction(ValueObject parentVO) {
        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,empVO.getCompanyCodeSys01REG04());
        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION);
      }

      public void forceValidate() {}

    });

    controlSubjectType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (controlSubjectType.getComboBox().getSelectedIndex()==-1) {
          controlName_1.setValue("");
          controlName_2.setValue("");
          filterOrgButton.setValue(null);
          filterPeopleButton.setValue(null);
        }
        else if (e.getStateChange()==e.SELECTED) {
          cardLayout2.show(cardPanel2, (String) controlSubjectType.getValue());
        }
      }
    });

  }




  /**
   * Retrieve employee detail and employee working hours.
   */
  private void loadEmployeeDetail(String companyCodeSys01SYS03,BigDecimal progressiveReg04SYS03) {
    // retrieve employee detail...
    Response res = ClientUtils.getData("loadEmployee",new SubjectPK(companyCodeSys01SYS03,progressiveReg04SYS03));
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      empVO = (DetailEmployeeVO)((VOResponse)res).getVo();
    }

    // retrieve employee working hours...
    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01SYS03);
    gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,progressiveReg04SYS03);
    res = ClientUtils.getData("loadEmployeeCalendar",gridParams);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      day.setEmpCalendar( new ArrayList(((VOListResponse)res).getRows()) );
    }

    day.setEmployeeVO(empVO);
    if (day.getEmpCalVO()!=null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(day.getEmpCalVO().getMorningStartHourSCH02());
      int startH = cal.get(cal.HOUR_OF_DAY);
      int startM = cal.get(cal.MINUTE);
      dayScrollPane.getVerticalScrollBar().setValue(startH*day.hourH+startM*day.hourH/60);
    }
  }


  private void jbInit() throws Exception {
    cardPanel2.setLayout(cardLayout2);
    cardPanel2.add(filterOrgButton,ApplicationConsts.SUBJECT_ORGANIZATION);
    cardPanel2.add(filterPeopleButton,ApplicationConsts.SUBJECT_PEOPLE);

    findButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("find next appointment"));

    titledBorder1 = new TitledBorder("");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("day appointments"));
    filterPanel.setBorder(BorderFactory.createEtchedBorder());
    filterPanel.setLayout(gridBagLayout1);
    dayPanel.setDebugGraphicsOptions(0);
    dayPanel.setLayout(borderLayout1);
    labelEmp.setText("employeeCodeSCH01");
    controlName2.setColumns(15);
    controlName2.setEnabled(false);
    controlName1.setColumns(15);
    controlName1.setEnabled(false);
    controlName1.setEnabledOnEdit(true);
    controlEmpCode.setMaxCharacters(20);
    calendar.setBorder(BorderFactory.createLoweredBevelBorder());
    findPanel.setBorder(titledBorder1);
    findPanel.setLayout(gridBagLayout2);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("find appointment"));
    labelSubjectType.setText("subject type");
    labelActType.setText("activityTypeSCH06");
    findButton.addActionListener(new EmployeeAppointmentsFrame_findButton_actionAdapter(this));
    filterOrgButton.setAllowOnlyNumbers(true);
    filterOrgButton.setCodBoxVisible(false);
    filterPeopleButton.setAllowOnlyNumbers(true);
    filterPeopleButton.setCodBoxVisible(false);
    controlName_1.setEnabled(false);
    controlName_2.setEnabled(false);
    labelFromDate.setText("from date");
    controlSubjectType.setDomainId("SUBJECT_TYPE_2");
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    filterPanel.add(calendar,   new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelEmp,    new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlEmpCode,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlName1,    new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlName2,     new GridBagConstraints(4, 0, 1, 2, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(findPanel,  new GridBagConstraints(1, 1, 4, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(dayPanel,  BorderLayout.CENTER);
    dayPanel.add(dayScrollPane, BorderLayout.CENTER);
    dayScrollPane.getViewport().add(day, null);
    findPanel.add(labelSubjectType,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    findPanel.add(cardPanel2,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    findPanel.add(controlName_1,     new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    findPanel.add(controlName_2,     new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    findPanel.add(labelActType,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    findPanel.add(findButton,        new GridBagConstraints(4, 1, 1, 2, 0.0, 1.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    findPanel.add(labelFromDate,    new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    findPanel.add(controlFromDate,     new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    findPanel.add(controlSubjectType,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    findPanel.add(controlActType,  new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    controlActType.setDomainId("ACTIVITY_TYPE");
  }


  void findButton_actionPerformed(ActionEvent e) {
    GridParams gridPars = new GridParams();
    gridPars.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,empVO.getCompanyCodeSys01SCH01());
    gridPars.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,empVO.getProgressiveReg04SCH01());
    if (controlFromDate.getValue()!=null)
      gridPars.getOtherGridParams().put(ApplicationConsts.START_DATE,controlFromDate.getValue());
    if (controlSubjectType.getValue()!=null && controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_ORGANIZATION))
      gridPars.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,filterOrgButton.getValue());
    else if (controlSubjectType.getValue()!=null && controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_PEOPLE))
      gridPars.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,filterPeopleButton.getValue());
    if (controlActType.getValue()!=null)
      gridPars.getOtherGridParams().put(ApplicationConsts.ACTIVITY_TYPE,"'"+controlActType.getValue()+"'");

    Response res = ClientUtils.getData("loadEmployeeActivities",gridPars);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
      );
      return;
    }

    java.util.List rows = ((VOListResponse)res).getRows();
    if (rows.size()>0) {
      EmployeeActivityVO vo = (EmployeeActivityVO)rows.get(0);
      Calendar cal = Calendar.getInstance();
      cal.setTime(vo.getStartDateSCH07());
      calendar.setCalendar(cal);
      cal.setTime(vo.getStartDateSCH07());
      cal.set(cal.DAY_OF_MONTH,cal.get(cal.DAY_OF_MONTH)+1);
      cal.set(cal.HOUR_OF_DAY,0);
      cal.set(cal.MINUTE,0);
      cal.set(cal.SECOND,0);
      cal.set(cal.MILLISECOND,0);
      controlFromDate.setValue(cal.getTime());
      day.setCurrentDay(vo.getStartDateSCH07());
    }
    else {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("no appointment found"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.ERROR_MESSAGE
      );
    }

  }

}

class EmployeeAppointmentsFrame_findButton_actionAdapter implements java.awt.event.ActionListener {
  EmployeeAppointmentsFrame adaptee;

  EmployeeAppointmentsFrame_findButton_actionAdapter(EmployeeAppointmentsFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.findButton_actionPerformed(e);
  }
}
