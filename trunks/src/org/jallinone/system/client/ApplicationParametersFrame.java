package org.jallinone.system.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.system.companies.java.CompanyVO;
import java.util.ArrayList;
import org.jallinone.system.java.ApplicationParametersVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;
import java.awt.event.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.warehouse.tables.motives.java.MotiveVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define application parameters.</p>
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

public class ApplicationParametersFrame extends InternalFrame {


	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel topPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	Form mainPanel = new Form();
	BorderLayout borderLayout2 = new BorderLayout();
	EditButton editButton1 = new EditButton();
	SaveButton saveButton1 = new SaveButton();
	ReloadButton reloadButton1 = new ReloadButton();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	TextControl controlImagePath = new TextControl();
	LabelControl labelImagePath = new LabelControl();
	GridBagLayout gridBagLayout3 = new GridBagLayout();
	LabelControl labelDocPath = new LabelControl();
	TextControl controlDocPath = new TextControl();
	LabelControl labelIncrementVal = new LabelControl();
	NumericControl controlIncremVal = new NumericControl();
	LabelControl labelNegDamg = new LabelControl();
	LabelControl labelPosDamg = new LabelControl();
	LabelControl labelNegGood = new LabelControl();
	LabelControl labelPosGood = new LabelControl();
	ComboBoxControl controlPosGood = new ComboBoxControl();
	ComboBoxControl controlNegGood = new ComboBoxControl();
	ComboBoxControl controlPosDamaged = new ComboBoxControl();
	ComboBoxControl controlNegDamg = new ComboBoxControl();
	LabelControl labelManualDocNumInSaleDocs = new LabelControl();
	CheckBoxControl controlManualDocNumInSaleDocs = new CheckBoxControl();



	public ApplicationParametersFrame(ApplicationParametersController controller) {
		try {
			jbInit();

			mainPanel.setFormController(controller);

			Response res = ClientUtils.getData("loadWarehouseMotives",new GridParams());
			Domain d = new Domain("WAR_MOTIVES");
			if (!res.isError()) {
				MotiveVO vo = null;
				java.util.List rows = null;
				rows = ((VOListResponse)res).getRows();
				for(int i=0;i<rows.size();i++) {
					vo = (MotiveVO)rows.get(i);
					d.addDomainPair(vo.getWarehouseMotiveWAR04(),vo.getDescriptionSYS10());
				}
			}
			controlPosDamaged.setDomain(d);
			controlNegDamg.setDomain(d);
			controlPosGood.setDomain(d);
			controlNegGood.setDomain(d);


			setSize(600,350);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void jbInit() throws Exception {
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.setVOClassName("org.jallinone.system.java.ApplicationParametersVO");
		mainPanel.setFunctionId("SYS11");

		this.setTitle(ClientSettings.getInstance().getResources().getResource("application parameters"));
		this.getContentPane().setLayout(gridBagLayout3);
		topPanel.setLayout(gridBagLayout2);
		labelImagePath.setText("image repository path");


		labelDocPath.setText("document repository path");
		labelIncrementVal.setText("increment value in progr");
		controlIncremVal.setMinValue(1.0);
		controlIncremVal.setRequired(true);
		labelNegDamg.setText("negdamaged");
		labelPosDamg.setText("posdamaged");
		labelNegGood.setText("neggood");
		labelPosGood.setText("posgood");
		controlPosGood.setRequired(true);
		controlNegDamg.setRequired(true);
		controlNegGood.setRequired(true);
		controlPosDamaged.setRequired(true);
		controlImagePath.setRequired(true);
		controlDocPath.setRequired(true);
		labelManualDocNumInSaleDocs.setText("manual doc num in sale docs");
		controlManualDocNumInSaleDocs.setAttributeName("manualDocNumInSaleDocs");
		controlManualDocNumInSaleDocs.setText("");
		topPanel.add(buttonsPanel,         new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


		mainPanel.setEditButton(editButton1);
		mainPanel.setReloadButton(reloadButton1);
		mainPanel.setSaveButton(saveButton1);
		this.getContentPane().add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		buttonsPanel.add(editButton1,null);
		buttonsPanel.add(saveButton1,null);
		buttonsPanel.add(reloadButton1,null);
		this.getContentPane().add(mainPanel,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
						,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.setLayout(gridBagLayout1);


		controlImagePath.setAttributeName("imagePath");
		controlDocPath.setAttributeName("documentPath");
		controlIncremVal.setAttributeName("incrementValue");

		controlPosDamaged.setAttributeName("invPosCorrForDamagedItemsValue");
		controlNegDamg.setAttributeName("invNegCorrForDamagedItemsValue");
		controlPosGood.setAttributeName("invPosCorrForGoodItemsValue");
		controlNegGood.setAttributeName("invNegCorrForGoodItemsValue");


		mainPanel.add(controlImagePath,                new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelImagePath,                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(labelDocPath,             new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlDocPath,             new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelIncrementVal,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlIncremVal,        new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelNegDamg,       new GridBagConstraints(0, 7, 1, 1, 0.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelPosDamg,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelNegGood,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelPosGood,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlNegGood,  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlPosDamaged,   new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlNegDamg,     new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(labelManualDocNumInSaleDocs,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlPosGood, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(controlManualDocNumInSaleDocs,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));




	}


	public Form getMainPanel() {
		return mainPanel;
	}




}
