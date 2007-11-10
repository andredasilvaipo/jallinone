package org.jallinone.scheduler.activities.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;
import org.jallinone.scheduler.activities.java.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import java.awt.event.*;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.employees.java.GridEmployeeVO;
import java.util.Calendar;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Scheduled activities grid frame.</p>
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
public class ScheduledActivitiesGridFrame extends InternalFrame {

  private ScheduledActivitiesPanel panel = new ScheduledActivitiesPanel(true);


  public ScheduledActivitiesGridFrame(ScheduledActivitiesController controller) {
    panel.setController(controller);
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Reload grid data.
   */
  public final void reloadData() {
    panel.reloadData();
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("scheduled activities"));
    this.getContentPane().add(panel, BorderLayout.CENTER);
  }


  public GridControl getGrid() {
    return panel.getGrid();
  }



}

