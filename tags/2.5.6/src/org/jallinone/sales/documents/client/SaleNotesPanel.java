package org.jallinone.sales.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header/footer to show header + footer notes and delivery notes.</p>
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
public class SaleNotesPanel extends JPanel {

  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelHeader = new LabelControl();
  TextAreaControl controlHeaderNote = new TextAreaControl();
  LabelControl labelFooterNote = new LabelControl();
  TextAreaControl controlFooterNote = new TextAreaControl();
  LabelControl labelDelNote = new LabelControl();
  TextAreaControl controlDelNote = new TextAreaControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();


  public SaleNotesPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder2 = new TitledBorder("");
    labelHeader.setText("header note");
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("notes"));
    titledBorder2.setTitleColor(Color.blue);
    this.setBorder(titledBorder2);
    this.setLayout(gridBagLayout1);
    labelFooterNote.setText("footer note");
    labelDelNote.setText("delivery note");
    controlNote.setAttributeName("noteDOC01");
    controlHeaderNote.setAttributeName("headingNoteDOC01");
    controlFooterNote.setAttributeName("footerNoteDOC01");
    controlDelNote.setAttributeName("deliveryNoteDOC01");
    labelNote.setText("note");
    this.add(labelHeader,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.add(controlHeaderNote,     new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.add(labelFooterNote,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.add(controlFooterNote,     new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.add(labelDelNote,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.add(controlDelNote,      new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.add(labelNote,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    this.add(controlNote,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
  }

}