package org.jallinone.sqltool.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.*;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: JPanel that contains SQL shell.</p>
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
public class SqlShellPanel extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel topPanel = new JPanel();
  JPanel statusPanel = new JPanel();
  JLabel statusLabel = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  JSplitPane splitPane = new JSplitPane();
  JScrollPane sqlScrollPane = new JScrollPane();
  JEditorPane sql = new JEditorPane();
  JPanel resultsPanel = new JPanel();
  SaveButton commitButton = new SaveButton();
  FlowLayout flowLayout1 = new FlowLayout();


  public SqlShellPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    statusPanel.setLayout(borderLayout2);
    statusLabel.setText(" ");
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    sql.setText("");
    topPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    commitButton.addActionListener(new SqlShellPanel_commitButton_actionAdapter(this));
    this.add(topPanel, BorderLayout.NORTH);
    this.add(statusPanel,  BorderLayout.SOUTH);
    statusPanel.add(statusLabel, BorderLayout.CENTER);
    this.add(splitPane, BorderLayout.CENTER);
    splitPane.add(sqlScrollPane, JSplitPane.TOP);
    splitPane.add(resultsPanel, JSplitPane.BOTTOM);
    sqlScrollPane.getViewport().add(sql, null);
    topPanel.add(commitButton, null);
    splitPane.setDividerLocation(250);
    resultsPanel.setLayout(new BorderLayout());
  }


  void commitButton_actionPerformed(ActionEvent e) {
    if (sql.getText()==null || sql.getText().trim().length()==0) {
      JOptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        ClientSettings.getInstance().getResources().getResource("you must define a sql statement!"),
        ClientSettings.getInstance().getResources().getResource("Error"),
        JOptionPane.WARNING_MESSAGE
      );
    }
    else {
      final String sqlScript = sql.getText().trim().replace('\n',' ').replace('\t',' ');
      if (sql.getText().trim().toLowerCase().startsWith("select")) {
        // execute a query statement...
        resultsPanel.removeAll();
        resultsPanel.add(new TablePanel(sqlScript,new ArrayList(),false,true,true),BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
      }
      else {
        // execute a DML/DDL statement...
        new Thread() {
          public void run() {
            Response res = ClientUtils.getData("executeStatement", sqlScript);
            if (res.isError())
              JOptionPane.showMessageDialog(
                MDIFrame.getInstance(),
                ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
                ClientSettings.getInstance().getResources().getResource("Error"),
                JOptionPane.ERROR_MESSAGE
              );
            else {
              // set structure content...
              Object text = ((VOResponse)res).getVo();
              statusLabel.setText(text.toString()+" "+ClientSettings.getInstance().getResources().getResource("rows affected."));
            }
          }
        }.start();
      }
    }
  }


}

class SqlShellPanel_commitButton_actionAdapter implements java.awt.event.ActionListener {
  SqlShellPanel adaptee;

  SqlShellPanel_commitButton_actionAdapter(SqlShellPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.commitButton_actionPerformed(e);
  }
}
