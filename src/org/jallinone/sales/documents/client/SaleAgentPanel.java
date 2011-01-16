package org.jallinone.sales.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.commons.java.ApplicationConsts;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header to show agent infos.</p>
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
public class SaleAgentPanel extends JPanel {

  TitledBorder titledBorder1;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelAgentCode = new LabelControl();
  CodLookupControl controlAgentCode = new CodLookupControl();
  TextControl controlName1 = new TextControl();
  LabelControl labelPercentage = new LabelControl();
  NumericControl controlPerc = new NumericControl();
  TextControl controlName2 = new TextControl();

  LookupController agentController = new LookupController();
  LookupServerDataLocator agentDataLocator = new LookupServerDataLocator();

  public SaleAgentPanel(Form form) {
    try {
      jbInit();


      // doc. ref. lookup...
      agentDataLocator.setGridMethodName("loadAgents");
      agentDataLocator.setValidationMethodName("validateAgentCode");

      controlAgentCode.setLookupController(agentController);
      controlAgentCode.setControllerMethodName("getAgentsList");
      agentController.setForm(form);
      agentController.setLookupDataLocator(agentDataLocator);
      agentController.setFrameTitle("agents");
      agentController.setLookupValueObjectClassName("org.jallinone.sales.agents.java.AgentVO");
      agentController.addLookup2ParentLink("agentCodeSAL10","agentCodeSal10DOC01");
      agentController.addLookup2ParentLink("percentageSAL10", "percentageDOC01");
      agentController.addLookup2ParentLink("name_1REG04", "name_1DOC01");
      agentController.addLookup2ParentLink("name_2REG04","name_2DOC01");
      agentController.setAllColumnVisible(false);
      agentController.setVisibleColumn("agentCodeSAL10", true);
      agentController.setVisibleColumn("name_1REG04", true);
      agentController.setVisibleColumn("name_2REG04", true);
      agentController.setPreferredWidthColumn("name_1REG04", 200);
      agentController.setPreferredWidthColumn("name_2REG04", 200);
      agentController.setHeaderColumnName("name_1REG04", "firstname");
      agentController.setHeaderColumnName("name_2REG04", "lastname");
      agentController.setFramePreferedSize(new Dimension(510,500));
      agentController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          DetailSaleDocVO vo = (DetailSaleDocVO)parentVO;
          agentDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
        }

        public void forceValidate() {}

      });


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    controlPerc.setAttributeName("percentageDOC01");
    controlAgentCode.setAttributeName("agentCodeSal10DOC01");
    controlName1.setAttributeName("name_1DOC01");
    controlName2.setAttributeName("name_2DOC01");
    titledBorder1 = new TitledBorder("");
    labelAgentCode.setText("agentCodeSAL10");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("agent"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelPercentage.setText("percentageDOC01");
    controlAgentCode.setLinkLabel(labelAgentCode);
    controlAgentCode.setMaxCharacters(20);
    controlName1.setEnabledOnInsert(false);
    controlName1.setEnabledOnEdit(false);
    controlPerc.setMaxValue(100.0);
    controlPerc.setMinValue(0.0);
    controlName2.setEnabledOnInsert(false);
    controlName2.setEnabledOnEdit(false);
    this.add(labelAgentCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlAgentCode,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlName1,     new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    this.add(labelPercentage,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPerc,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlName2,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
  }

}
