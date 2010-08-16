package org.jallinone.contacts.client;

import java.math.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import org.jallinone.commons.client.*;
import org.jallinone.subjects.client.*;
import org.jallinone.subjects.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.Consts;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.scheduler.activities.client.ScheduledActivitiesPanel;
import org.jallinone.scheduler.activities.client.ScheduledActivityController;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import java.util.ArrayList;
import org.jallinone.scheduler.activities.java.GridScheduledActivityVO;
import java.util.HashSet;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame related to a contact (organization or person).</p>
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
public class ContactDetailFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  OrganizationPanel organizationPanel = new OrganizationPanel(true);
  PeoplePanel peoplePanel = new PeoplePanel();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel subjectPanel = new JPanel();
  JPanel cardPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  ComboBoxControl controlSubjectType = new ComboBoxControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelContactType = new LabelControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;

  private Form voidPanel = new Form();

  JPanel subjectTypePanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel refPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();

  ReferencesPanel referencesPanel = new ReferencesPanel();
  JPanel hierarPanel = new JPanel();
  SubjectHierarchyLevelsPanel hierarchiesPanel = new SubjectHierarchyLevelsPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  private PeopleContactsPanel contactsPanel = new PeopleContactsPanel(this);

  private ContactController controller = null;
  GenericButton convertButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));

  private ScheduledActivitiesPanel panel = new ScheduledActivitiesPanel(false);
  NavigatorBar navigatorBar = new NavigatorBar();


  public ContactDetailFrame(ContactController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750, 500));

      organizationPanel.setFunctionId("REG04_CONTACTS");
      peoplePanel.setFunctionId("REG04_CONTACTS");

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01REG04");
      pk.add("progressiveREG04");
      pk.add("name_1REG04");
      pk.add("name_2REG04");
      pk.add("subjectTypeREG04");
      voidPanel.linkGrid(controller.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);

      controlSubjectType.getComboBox().addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED)
            subjectChanged((String)controlSubjectType.getValue());
        }
      });

      controlSubjectType.getComboBox().setSelectedIndex(0);

//      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,customerPanel,new BigDecimal(282));



      panel.getGrid().setAutoLoadData(false);

      panel.setController(new CompanyGridController() {


            /**
             * Callback method invoked when the user has double clicked on the selected row of the grid.
             * @param rowNumber selected row index
             * @param persistentObject v.o. related to the selected row
             */
            public void doubleClick(int rowNumber,ValueObject persistentObject) {
              new ScheduledActivityController(
                  panel.getGrid(),
                  null,
                  new ScheduledActivityPK(
                    ((ScheduledActivityVO)persistentObject).getCompanyCodeSys01SCH06(),
                    ((ScheduledActivityVO)persistentObject).getProgressiveSCH06()
                  ),
                  true
              );
            }


            /**
             * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
             * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
             */
            public boolean beforeInsertGrid(GridControl grid) {
              if (super.beforeInsertGrid(grid)) {
                ScheduledActivityController c = new ScheduledActivityController(panel.getGrid(),null,null,true);
                c.getControlSubjectType().setValue(controlSubjectType.getValue());

                if (controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) {
                  OrganizationVO model = (OrganizationVO)getCurrentForm().getVOModel().getValueObject();
                  c.getControlName_1Subject().setValue(model.getName_1REG04());
                  c.getControlName_2Subject().setValue(model.getName_2REG04());
                  ScheduledActivityVO actVO = (ScheduledActivityVO)c.getVOModel().getValueObject();
                  actVO.setProgressiveReg04SubjectSCH06(model.getProgressiveREG04());
                  c.getDetailFrame().getMainForm().getForm().pull("progressiveReg04SubjectSCH06");
                }
                else {
                  PeopleVO model = (PeopleVO)getCurrentForm().getVOModel().getValueObject();
                  c.getControlName_1Subject().setValue(model.getName_1REG04());
                  c.getControlName_2Subject().setValue(model.getName_2REG04());
                  ScheduledActivityVO actVO = (ScheduledActivityVO)c.getVOModel().getValueObject();
                  actVO.setProgressiveReg04SubjectSCH06(model.getProgressiveREG04());
                  c.getDetailFrame().getMainForm().getForm().pull("progressiveReg04SubjectSCH06");
                }

              }
              return false;
            }


            /**
             * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
             * @param persistentObjects value objects to delete (related to the currently selected rows)
             * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
             */
            public Response deleteRecords(ArrayList persistentObjects) throws Exception {
              GridScheduledActivityVO vo = null;
              ScheduledActivityPK pk = null;
              ArrayList pks = new ArrayList();
              for(int i=0;i<persistentObjects.size();i++) {
                vo = (GridScheduledActivityVO)persistentObjects.get(i);
                pk = new ScheduledActivityPK(vo.getCompanyCodeSys01SCH06(),vo.getProgressiveSCH06());
                pks.add(pk);
              }
              Response response = ClientUtils.getData("deleteScheduledActivities",pks);
              return response;
            }


            /**
             * Method used to define the background color for each cell of the grid.
             * @param rowNumber selected row index
             * @param attributedName attribute name related to the column currently selected
             * @param value object contained in the selected cell
             * @return background color of the selected cell
             */
            public Color getBackgroundColor(int row,String attributedName,Object value) {
              GridScheduledActivityVO vo = (GridScheduledActivityVO)panel.getGrid().getVOListTableModel().getObjectForRow(row);
              if (attributedName.equals("activityStateSCH06")) {
                Color color = null;
                if (vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) ||
                    vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED))
                  return super.getBackgroundColor(row,attributedName,value);
                else return new Color(241,143,137);
              }
              else if (attributedName.equals("prioritySCH06")) {
                Color color = null;
                if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGHEST))
                  color = new Color(241,123,137);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGH))
                  color = new Color(248,176,181);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_NORMAL))
                  color = new Color(191,246,207);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_LOW))
                  color = new Color(191,226,207);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_TRIVIAL))
                  color = new Color(191,206,207);
                return color;
              }
              else
                return super.getBackgroundColor(row,attributedName,value);

            }

      }); // end activities grid controller


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    voidPanel.setFunctionId("REG04_CONTACTS");
    voidPanel.setFormController(controller);

    voidPanel.setInsertButton(insertButton);
    voidPanel.setEditButton(editButton);
    voidPanel.setDeleteButton(deleteButton);
    voidPanel.setReloadButton(reloadButton);
    voidPanel.setSaveButton(saveButton);

    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    controlSubjectType.setDomainId("CONTACT_TYPE");
    controlSubjectType.setLinkLabel(labelContactType);
    controlSubjectType.setAttributeName("subjectTypeREG04");
    controlSubjectType.setEnabledOnEdit(false);
    controlSubjectType.setCanCopy(true);
    subjectPanel.setLayout(gridBagLayout1);
    labelContactType.setText("contact type");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("contact"));
    titledBorder1.setTitleColor(Color.blue);
//    controlContactCode.setRequired(true);
    subjectTypePanel.setLayout(gridBagLayout3);
    cardPanel.setLayout(cardLayout1);
    refPanel.setLayout(borderLayout1);
    hierarPanel.setLayout(borderLayout4);
    convertButton.addActionListener(new ContactDetailFrame_convertButton_actionAdapter(this));
    subjectTypePanel.add(labelContactType,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectTypePanel.add(controlSubjectType,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 130, 0));
    cardPanel.add(organizationPanel,ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT);
    cardPanel.add(peoplePanel,ApplicationConsts.SUBJECT_PEOPLE_CONTACT);
    subjectPanel.add(cardPanel,      new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    if (controller.getOrganization()==null) {
      subjectPanel.add(subjectTypePanel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }

    subjectPanel.add(voidPanel,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.setTitle(ClientSettings.getInstance().getResources().getResource("contact detail"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setEnabled(false);
    saveButton.setText("saveButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");


    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);

    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(convertButton, null);
    buttonsPanel.add(navigatorBar, null);

    convertButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("convert contact to customer"));
    convertButton.setEnabled(false);

    tabbedPane.add(subjectPanel,   "generic data");
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

    tabbedPane.add(refPanel,   "references");
    refPanel.add(referencesPanel, BorderLayout.CENTER);
    tabbedPane.add(hierarPanel,  "hierarchies");
    hierarPanel.add(hierarchiesPanel,  BorderLayout.CENTER);

    tabbedPane.add(contactsPanel,"contacts");
    tabbedPane.add(panel,   "activities");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("generic data"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("references"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("hierarchies"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("contacts"));
    tabbedPane.setTitleAt(4,ClientSettings.getInstance().getResources().getResource("scheduled activities"));
  }


  public void subjectChanged(String subjectTypeREG04) {
    try {
      cardLayout1.show(cardPanel, subjectTypeREG04);
      if (controlSubjectType.getValue() == null ||
          !controlSubjectType.getValue().equals(subjectTypeREG04)) {
        controlSubjectType.setValue(subjectTypeREG04);

      }
      if (subjectTypeREG04.equals(ApplicationConsts.
                                  SUBJECT_ORGANIZATION_CONTACT)) {
        voidPanel.setVOClassName("org.jallinone.subjects.java.OrganizationVO");
//        voidPanel.getVOModel().setValueObject(new OrganizationVO());
        voidPanel.removeLinkedPanel(peoplePanel);
        voidPanel.addLinkedPanel(organizationPanel);
      }
      else {
        voidPanel.setVOClassName("org.jallinone.subjects.java.PeopleVO");
//        voidPanel.getVOModel().setValueObject(new PeopleVO());
        voidPanel.removeLinkedPanel(organizationPanel);
        voidPanel.addLinkedPanel(peoplePanel);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  public Form getCurrentForm() {
    return voidPanel;
  }


  public ComboBoxControl getControlSubjectType() {
    return controlSubjectType;
  }


  public ReferencesPanel getReferencesPanel() {
    return referencesPanel;
  }


  public SubjectHierarchyLevelsPanel getHierarchiesPanel() {
    return hierarchiesPanel;
  }



  public GridControl getActivitiesGrid() {
    return panel.getGrid();
  }


  public final void setButtonsEnabled(boolean enabled) {
    referencesPanel.setButtonsEnabled(enabled);
    hierarchiesPanel.setButtonsEnabled(enabled);
    contactsPanel.setButtonsEnabled(enabled);

    panel.setButtonsEnabled(enabled);

    if (!enabled)
      convertButton.setEnabled(false);
    else
      convertButton.setEnabled(true);
  }


  public PeopleContactsPanel getContactsPanel() {
    return contactsPanel;
  }
  public JTabbedPane getTabbedPane() {
    return tabbedPane;
  }
  public ContactController getController() {
    return controller;
  }


  void convertButton_actionPerformed(ActionEvent e) {
    Subject vo = (Subject)voidPanel.getVOModel().getValueObject();
    new Contact2CustomerController(vo,this,controller.getGridFrame());
  }


}

class ContactDetailFrame_convertButton_actionAdapter implements java.awt.event.ActionListener {
  ContactDetailFrame adaptee;

  ContactDetailFrame_convertButton_actionAdapter(ContactDetailFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.convertButton_actionPerformed(e);
  }
}
