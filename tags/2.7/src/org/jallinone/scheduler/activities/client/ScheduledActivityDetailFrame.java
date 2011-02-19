package org.jallinone.scheduler.activities.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Scheduled activity detail frame.</p>
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
public class ScheduledActivityDetailFrame extends InternalFrame implements CloseActivity {

  JTabbedPane tabbedPane = new JTabbedPane();
  ScheduledActivityPanel mainForm = new ScheduledActivityPanel(true,this);
  ScheduledResourcesPanel resPanel = new ScheduledResourcesPanel(mainForm);
  private ScheduledActivitiesGridFrame gridFrame = null;


  public ScheduledActivityDetailFrame(ScheduledActivityController controller,ScheduledActivitiesGridFrame gridFrame,boolean crm) {
    this.gridFrame = gridFrame;
    try {
      jbInit();
      setSize(750,640);
      setMinimumSize(new Dimension(750,640));

      mainForm.setFormController(controller);

      if (crm) {
        mainForm.setManagerVisible(true);
        mainForm.setSubjectVisible(true);
      }

      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,mainForm.getForm(),ApplicationConsts.ID_SCHEDULED_ACTIVITIES);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("scheduled activity"));
    this.getContentPane().add(tabbedPane,  BorderLayout.CENTER);
    tabbedPane.add(mainForm,  "activityPanel");
    tabbedPane.add(resPanel,  "resPanel");
    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("scheduled activity"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("scheduled resources"));

  }


  /**
   * This method is called by ScheduledActivityPanel when user clicks on "Close Activity" button.
   */
  public void closeActivity() {
    resPanel.setButtonsEnabled(false);
    if (gridFrame!=null)
      gridFrame.getGrid().reloadData();
  }



  public ScheduledActivityPanel getMainForm() {
    return mainForm;
  }


  public ScheduledResourcesPanel getResPanel() {
    return resPanel;
  }


  public JTabbedPane getTabbedPane() {
    return tabbedPane;
  }


  public TextControl getControlName_1Subject() {
    return mainForm.getControlName_1Subject();
  }
  public TextControl getControlName_2Subject() {
    return mainForm.getControlName_2Subject();
  }
  public ComboBoxControl getControlSubjectType() {
    return mainForm.getControlSubjectType();
  }


}
