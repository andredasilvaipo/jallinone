package org.jallinone.purchases.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the purchase header to show purchase identifier: doc number, doc year, etc.</p>
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
public class PurchaseIdHeadPanel extends JPanel {
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDocNum = new LabelControl();
  NumericControl controlDocNumber = new NumericControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelDocRif = new LabelControl();
  CodLookupControl controlDocRifLookup = new CodLookupControl();
  NumericControl controlDocYearRef = new NumericControl();
  LabelControl labelDocDate = new LabelControl();
  DateControl controlDocDate = new DateControl();
  LabelControl labelDocState = new LabelControl();
  ComboBoxControl controlDocState = new ComboBoxControl();

  LookupController docRefController = new LookupController();
  LookupServerDataLocator docRefDataLocator = new LookupServerDataLocator();

  private boolean showDocRefLookup;



  public PurchaseIdHeadPanel(final Form form,boolean showDocRefLookup,boolean docNumberEditable) {
    this.showDocRefLookup = showDocRefLookup;
    try {
      jbInit();


      // doc. ref. lookup...
//      docRefDataLocator.setGridMethodName("loadPurchaseDocuments");
//      docRefDataLocator.setValidationMethodName("");
//
//      controlDocRifLookup.setLookupController(docRefController);
//      docRefController.setForm(form);
//      docRefController.setLookupDataLocator(docRefDataLocator);
//      docRefController.setFrameTitle("documents");
//      docRefController.setLookupValueObjectClassName("org.jallinone.purchases.documents.java.GridPurchaseDocVO");
//      docRefController.addLookup2ParentLink("companyCodeSys01DOC06", "companyCodeSys01Doc06DOC06");
//      docRefController.addLookup2ParentLink("docTypeDOC06","docTypeDoc6DOC06");
//      docRefController.addLookup2ParentLink("docYearDOC06", "docYearDoc06DOC06");
//      docRefController.addLookup2ParentLink("docNumberDOC06","docNumberDoc06DOC06");
//      docRefController.addLookup2ParentLink("docSequenceDOC06","docSequenceDoc06DOC06");
//      docRefController.setAllColumnVisible(false);
//      docRefController.setVisibleColumn("companyCodeSys01DOC06", true);
//      docRefController.setVisibleColumn("docTypeDOC06", true);
//      docRefController.setVisibleColumn("docYearDOC06", true);
//      docRefController.setVisibleColumn("docSequenceDOC06", true);
//      docRefController.setVisibleColumn("name_1REG04", true);
//      docRefController.setVisibleColumn("docDateDOC06", true);
//      docRefController.setVisibleColumn("docStateDOC06", true);
//      docRefController.setHeaderColumnName("name_1REG04", "corporateName1");
//      docRefController.setFramePreferedSize(new Dimension(600,500));

      // doc. ref. lookup...
      docRefDataLocator.setGridMethodName("loadPurchaseDocs");
      docRefDataLocator.setValidationMethodName("validatePurchaseDocNumber");

      controlDocRifLookup.setLookupController(docRefController);
      controlDocRifLookup.setControllerMethodName("getPurchaseOrdersList");
      docRefController.setForm(form);
      docRefController.setLookupDataLocator(docRefDataLocator);
      docRefController.setFrameTitle("documents");
      docRefController.setLookupValueObjectClassName("org.jallinone.purchases.documents.java.GridPurchaseDocVO");
      docRefController.addLookup2ParentLink("companyCodeSys01DOC06", "companyCodeSys01Doc06DOC06");
      docRefController.addLookup2ParentLink("docTypeDOC06","docTypeDoc06DOC06");
      docRefController.addLookup2ParentLink("docYearDOC06", "docYearDoc06DOC06");
      docRefController.addLookup2ParentLink("docNumberDOC06","docNumberDoc06DOC06");
      docRefController.addLookup2ParentLink("docSequenceDOC06","docSequenceDoc06DOC06");
      docRefController.setAllColumnVisible(false);
      docRefController.setPreferredWidthColumn("docStateDOC06",60);
      docRefController.setPreferredWidthColumn("docYearDOC06",60);
      docRefController.setPreferredWidthColumn("docSequenceDOC06",80);
      docRefController.setPreferredWidthColumn("name_1REG04",150);
      docRefController.setPreferredWidthColumn("name_2REG04",140);
      docRefController.setPreferredWidthColumn("docDateDOC06",80);

      docRefController.setVisibleColumn("companyCodeSys01DOC06", true);
      docRefController.setVisibleColumn("docYearDOC06", true);
      docRefController.setVisibleColumn("docSequenceDOC06", true);
      docRefController.setVisibleColumn("name_1REG04", true);
      docRefController.setVisibleColumn("name_2REG04", true);
      docRefController.setVisibleColumn("docDateDOC06", true);
      docRefController.setFramePreferedSize(new Dimension(700,500));

      docRefController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_TYPE,ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
          docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_TYPE,ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
          docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
          docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);

        }

        public void forceValidate() {}

      });

			if (docNumberEditable) {
				controlDocNumber.setRequired(true);
				controlDocNumber.setEnabledOnInsert(true);
				controlDocNumber.setEnabledOnEdit(true);
				controlDocNumber.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
				    form.getVOModel().setValue("docSequenceDOC06",controlDocNumber.getValue());
					}
				});
			}
			else {
				controlDocNumber.setRequired(false);
				controlDocNumber.setEnabledOnInsert(false);
				controlDocNumber.setEnabledOnEdit(false);
			}

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    labelDocNum.setText("docNumber");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("document identification"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelDocYear.setText("docYear");
    labelDocRif.setText("docRif");
    controlDocRifLookup.setAttributeName("docSequenceDoc06DOC06");
    controlDocRifLookup.setAllowOnlyNumbers(true);
    controlDocRifLookup.setCanCopy(true);
    controlDocRifLookup.setCodBoxVisible(true);
    controlDocRifLookup.setLinkLabel(labelDocRif);
    controlDocRifLookup.setMaxCharacters(20);
    labelDocDate.setText("docDate");
    labelDocState.setText("docState");
    controlDocNumber.setLinkLabel(labelDocNum);
    controlDocNumber.setMaxCharacters(255);
    controlDocNumber.setAttributeName("docSequenceDOC06");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDOC06");

    if (showDocRefLookup) {
      this.add(labelDocRif,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      this.add(new JPanel(),  new GridBagConstraints(8, 0, 1, 1, 1.0, 0.0
          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      this.add(controlDocRifLookup,  new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
      this.add(controlDocYearRef, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    controlDocYearRef.setEnabledOnInsert(false);
    controlDocYearRef.setEnabledOnEdit(false);
    controlDocYearRef.setAttributeName("docYearDoc06DOC06");
    controlDocDate.setCanCopy(true);
    controlDocDate.setLinkLabel(labelDocDate);
    controlDocDate.setRequired(true);
    controlDocDate.setAttributeName("docDateDOC06");
    controlDocState.setCanCopy(false);
    controlDocState.setLinkLabel(labelDocDate);
    controlDocState.setRequired(true);
    controlDocState.setEnabledOnInsert(false);
    controlDocState.setEnabledOnEdit(false);
    controlDocState.setAttributeName("docStateDOC06");
    controlDocState.setDomainId("DOC06_STATES");
    this.add(labelDocNum,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocNumber,              new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
//    this.add(labelDocRif,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//    this.add(controlDocRifLookup,          new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
//    this.add(controlDocNumRef,        new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
//    this.add(controlDocYearRef,         new GridBagConstraints(4, 1, 2, 1, 0.0, 0.0
//            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    this.add(controlDocYear,          new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
    this.add(labelDocYear,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//    this.add(controlDocTypeRef,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 40, 0));
    this.add(labelDocDate,    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocDate,        new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelDocState,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocState,      new GridBagConstraints(7, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 10, 0));
  }


  public CodLookupControl getControlDocRifLookup() {
    return controlDocRifLookup;
  }


}
