package org.jallinone.sales.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import javax.swing.SwingConstants;
import org.jallinone.commons.client.ClientApplet;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ApplicationParametersVO;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header to show sale identifier: doc number, doc year, etc.</p>
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
public class SaleIdHeadPanel extends JPanel {
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDocNum = new LabelControl();
  NumericControl controlDocNumber = new NumericControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelDocRif = new LabelControl();
  CodLookupControl controlDocRifLookup = new CodLookupControl();
  ComboBoxControl controlDocTypeRef = new ComboBoxControl();
  NumericControl controlDocYearRef = new NumericControl();
  LabelControl labelDocDate = new LabelControl();
  DateControl controlDocDate = new DateControl();
  LabelControl labelDocState = new LabelControl();
  ComboBoxControl controlDocState = new ComboBoxControl();

  LookupController docRefController = new LookupController();
  LookupServerDataLocator docRefDataLocator = new LookupServerDataLocator();
  LabelControl labelDocRefNr = new LabelControl();
  TextControl controlDofRef = new TextControl();
  private boolean showDocRefLookup;
  private boolean showSectional;
  TextControl controlSectional = new TextControl();



  /**
   * Constructor called by sale doc header frames.
   * @param form sale doc header form
   * @param showDocRefLookup <code>true</code> to show combo+lookup used to select a linked document (e.g. a sale document in an invoice document); <code>false</code> to hide combo+lookup and show doc. ref. nr. input control
   * @param showSectional <code>true</code> to show  the sectional field (for invoice documents); <code>false</code> to hide that field
   */
  public SaleIdHeadPanel(Form form,boolean showDocRefLookup,boolean showSectional) {
    this.showDocRefLookup = showDocRefLookup;
    this.showSectional = showSectional;
    try {
      jbInit();

      // doc. ref. lookup...
      docRefDataLocator.setGridMethodName("loadSaleDocs");
      docRefDataLocator.setValidationMethodName("validateSaleDocNumber");

      controlDocRifLookup.setLookupController(docRefController);
      docRefController.setForm(form);
      docRefController.setLookupDataLocator(docRefDataLocator);
      docRefController.setFrameTitle("documents");
      docRefController.setLookupValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocVO");
      docRefController.addLookup2ParentLink("companyCodeSys01DOC01", "companyCodeSys01Doc01DOC01");
      docRefController.addLookup2ParentLink("docTypeDOC01","docTypeDoc01DOC01");
      docRefController.addLookup2ParentLink("docYearDOC01", "docYearDoc01DOC01");
      docRefController.addLookup2ParentLink("docNumberDOC01","docNumberDoc01DOC01");
      docRefController.addLookup2ParentLink("docSequenceDOC01","docSequenceDoc01DOC01");
      docRefController.setAllColumnVisible(false);
      docRefController.setDomainColumn("docTypeDOC01","SALE_DOC_TYPE");
      docRefController.setPreferredWidthColumn("docStateDOC01",60);
      docRefController.setPreferredWidthColumn("docTypeDOC01",60);
      docRefController.setPreferredWidthColumn("docYearDOC01",60);
      docRefController.setPreferredWidthColumn("docSequenceDOC01",90);
      docRefController.setPreferredWidthColumn("name_1REG04",150);
      docRefController.setPreferredWidthColumn("name_2REG04",140);
      docRefController.setPreferredWidthColumn("docDateDOC01",80);

	     docRefController.setSortedColumn("companyCodeSys01DOC01","ASC",1);
			 docRefController.setSortedColumn("docTypeDOC01","ASC",2);
			 docRefController.setSortedColumn("docYearDOC01","ASC",3);
			 docRefController.setSortedColumn("docSequenceDOC01","ASC",4);

      docRefController.setVisibleColumn("companyCodeSys01DOC01", true);
      docRefController.setVisibleColumn("docTypeDOC01", true);
      docRefController.setVisibleColumn("docYearDOC01", true);
      docRefController.setVisibleColumn("docSequenceDOC01", true);
      docRefController.setVisibleColumn("name_1REG04", true);
      docRefController.setVisibleColumn("name_2REG04", true);
      docRefController.setVisibleColumn("docDateDOC01", true);
      docRefController.setFramePreferedSize(new Dimension(700,500));

      docRefController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_TYPE,controlDocTypeRef.getValue());
          docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_TYPE,controlDocTypeRef.getValue());
					// filter sale documents having state confirmed, i.e. not yet invoiced, except for sale desk docs...
					if (ApplicationConsts.SALE_DESK_DOC_TYPE.equals(controlDocTypeRef.getValue())) {
						docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CLOSED);
						docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CLOSED);
					}
					else {
						docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
						docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
					}
        }

        public void forceValidate() {}

      });


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    controlDofRef.setAttributeName("docRefNumberDOC01");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    labelDocNum.setText("docNumber");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("document identification"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelDocYear.setText("docYear");
    labelDocRif.setText("docRif");
    controlDocRifLookup.setCanCopy(true);
    controlDocRifLookup.setAllowOnlyNumbers(true);
    controlDocRifLookup.setLinkLabel(labelDocRif);
    controlDocRifLookup.setMaxCharacters(20);
    controlDocRifLookup.setEnabledOnEdit(false);
    controlDocRifLookup.setAttributeName("docSequenceDoc01DOC01");
    labelDocDate.setText("docDate");
    labelDocState.setText("docState");

		ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
		ApplicationParametersVO appVO = applet.getAuthorizations();
		if (!appVO.getManualDocNumInSaleDocs().booleanValue()) {
			controlDocNumber.setEnabledOnInsert(false);
			controlDocNumber.setEnabledOnEdit(false);
		}

    controlDocNumber.setLinkLabel(labelDocNum);
    controlDocNumber.setMaxCharacters(255);
    controlDocNumber.setRequired(false);
    controlDocNumber.setAttributeName("docSequenceDOC01");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDOC01");
    controlDocTypeRef.setCanCopy(true);
    controlDocTypeRef.setAttributeName("docTypeDoc01DOC01");
    controlDocTypeRef.setLinkLabel(labelDocRif);
    controlDocTypeRef.setEnabledOnInsert(true);
    controlDocTypeRef.setEnabledOnEdit(false);
    controlDocTypeRef.setDomainId("SALE_DOC_TYPE");
    controlDocYearRef.setEnabledOnInsert(false);
    controlDocYearRef.setEnabledOnEdit(false);
    controlDocYearRef.setAttributeName("docYearDoc01DOC01");
    controlDocDate.setCanCopy(true);
    controlDocDate.setLinkLabel(labelDocDate);
    controlDocDate.setRequired(true);
    controlDocDate.setEnabledOnEdit(false);
    controlDocDate.setAttributeName("docDateDOC01");
    controlDocState.setCanCopy(false);
    controlDocState.setLinkLabel(labelDocDate);
    controlDocState.setRequired(true);
    controlDocState.setEnabledOnInsert(false);
    controlDocState.setEnabledOnEdit(false);
    controlDocState.setAttributeName("docStateDOC01");
    controlDocState.setDomainId("DOC01_STATES");
    labelDocRefNr.setText("docRef");
    controlSectional.setAttributeName("sectionalDOC01");
    controlSectional.setEnabledOnInsert(false);
    controlSectional.setEnabledOnEdit(false);
    controlSectional.setTextAlignment(SwingConstants.RIGHT);
    this.add(labelDocNum,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocNumber,                  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

    if (showDocRefLookup) {
      this.add(labelDocRif,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      this.add(controlDocTypeRef,     new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
      this.add(controlDocRifLookup,          new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
      this.add(controlDocYearRef,         new GridBagConstraints(5, 1, 3, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 40, 0));
      this.add(new JPanel(),  new GridBagConstraints(9, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }
    else {
      this.add(labelDocRefNr,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      this.add(controlDofRef,  new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    }

    this.add(controlDocYear,              new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    this.add(labelDocYear,       new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));


    this.add(labelDocDate,      new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocDate,             new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelDocState,     new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocState,          new GridBagConstraints(8, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    if (showSectional)
      this.add(controlSectional,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }


  public CodLookupControl getControlDocRifLookup() {
    return controlDocRifLookup;
  }

}
