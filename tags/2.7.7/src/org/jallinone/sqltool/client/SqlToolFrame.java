package org.jallinone.sqltool.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Internal frame that contains SQL shell window and SQL entities list.</p>
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
public class SqlToolFrame extends InternalFrame {


  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  EntitiesListPanel tablesPanel = new EntitiesListPanel();
  SqlShellPanel sqlPanel = new SqlShellPanel();


  public SqlToolFrame() {
    try {
      jbInit();
      setSize(650,500);
      MDIFrame.add(this,true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    setTitle(ClientSettings.getInstance().getResources().getResource("sql tool"));
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(tabbedPane,  BorderLayout.CENTER);
    tabbedPane.add(tablesPanel,  "tables");
    tabbedPane.add(sqlPanel,   "sql");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("entities"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("sql"));
  }


}
