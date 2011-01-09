package org.jallinone.items.spareparts.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantNameVO;
import org.openswing.swing.domains.java.*;
import java.util.ArrayList;
import javax.swing.border.*;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.spareparts.java.ItemSparePartVO;
import org.jallinone.items.client.ItemFrame;
import org.jallinone.items.client.ItemController;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.items.spareparts.java.ItemSheetVO;
import org.jallinone.items.java.DetailItemVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.jallinone.items.spareparts.java.ItemSheetLevelVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define sheets levels and names and define sheets for spare parts catalogue.</p>
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
public class SheetLevelsFrame extends InternalFrame {

	GridControl gridLevels = new GridControl();

	ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel filterPanel = new JPanel();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	IntegerColumn colLevel = new IntegerColumn();
	TextColumn colLevDescr = new TextColumn();

	LabelControl companyLabel = new LabelControl();
	CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();
  ButtonColumn colButton = new ButtonColumn();

	JPanel buttonsPanel = new JPanel();

	InsertButton insertButton = new InsertButton();
	EditButton editButton = new EditButton();
	ReloadButton reloadButton = new ReloadButton();
	DeleteButton deleteButton = new DeleteButton();
	SaveButton saveButton = new SaveButton();
  FlowLayout flowLayout1 = new FlowLayout();


	public SheetLevelsFrame() {
		gridLevels.setController(new SheetLevelsController(this));
		gridLevels.setGridDataLocator(gridDataLocator);
		gridLevels.setInsertRowsOnTop(false);
		gridDataLocator.setServerMethodName("loadItemSheetLevels");
		try {
			jbInit();
			init();
			setSize(750,300);
			MDIFrame.add(this);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void init() {
		// define sheets grid additional columns..
		TextColumn tCol = null;
		TextColumn dCol = null;
		TextColumn nCol = null;
		for(int i=0;i<10;i++) {
			tCol = new TextColumn();
			tCol.setColumnName("sProp"+i+"DescriptionSys10ITM29");
			tCol.setHeaderColumnName(ClientSettings.getInstance().getResources().getResource("text")+(i+1));
			tCol.setEditableOnInsert(true);
			tCol.setEditableOnEdit(true);
			tCol.setPreferredWidth(70);
			tCol.setColumnRequired(false);
			gridLevels.getColumnContainer().add(tCol,null);

			dCol = new TextColumn();
			dCol.setColumnName("dProp"+i+"DescriptionSys10ITM29");
			dCol.setHeaderColumnName(ClientSettings.getInstance().getResources().getResource("date")+(i+1));
			dCol.setEditableOnInsert(true);
			dCol.setEditableOnEdit(true);
			dCol.setPreferredWidth(70);
			dCol.setColumnRequired(false);
			gridLevels.getColumnContainer().add(dCol,null);

			nCol = new TextColumn();
			nCol.setColumnName("nProp"+i+"DescriptionSys10ITM29");
			nCol.setHeaderColumnName(ClientSettings.getInstance().getResources().getResource("numeric")+(i+1));
			nCol.setPreferredWidth(70);
			nCol.setEditableOnInsert(true);
			nCol.setEditableOnEdit(true);
			nCol.setColumnRequired(false);
			gridLevels.getColumnContainer().add(nCol,null);
		}
	}


	private void jbInit() throws Exception {
		gridLevels.setFunctionId("ITM29");

		gridLevels.setDeleteButton(deleteButton);
		gridLevels.setInsertButton(insertButton);
		gridLevels.setReloadButton(reloadButton);
		gridLevels.setEditButton(editButton);
		gridLevels.setSaveButton(saveButton);

		buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    buttonsPanel.add(insertButton, null);
		buttonsPanel.add(editButton, null);
		buttonsPanel.add(saveButton, null);
		buttonsPanel.add(reloadButton, null);
		buttonsPanel.add(deleteButton, null);

		companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
		companyLabel.setLabel("companyCodeSYS01");

		controlCompaniesCombo.setLinkLabel(companyLabel);
		controlCompaniesCombo.setFunctionCode(gridLevels.getFunctionId());

		this.setTitle(ClientSettings.getInstance().getResources().getResource("spare parts sheets levels"));

		gridLevels.setValueObjectClassName("org.jallinone.items.spareparts.java.ItemSheetLevelVO");
		gridLevels.setAllowColumnsPermission(false);
		gridLevels.setAllowColumnsProfile(false);
		gridLevels.setMaxSortedColumns(1);
		gridLevels.setMaxNumberOfRowsOnInsert(1000);
		filterPanel.setLayout(gridBagLayout2);

		this.getContentPane().setLayout(new BorderLayout());
		colLevDescr.setEditableOnEdit(true);
    colLevDescr.setEditableOnInsert(true);
    colLevDescr.setPreferredWidth(150);
    colLevel.setPreferredWidth(70);
    colLevel.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colLevel.setSortingOrder(1);
    colButton.setColumnName("companyCodeSys01ITM29");
    colButton.setEnableInReadOnlyMode(true);
    colButton.setHeaderColumnName("sheets");
    colButton.setPreferredWidth(50);
    colButton.setText("...");
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
		this.getContentPane().add(gridLevels, BorderLayout.CENTER);

		colLevel.setColumnName("levITM29");
		colLevDescr.setColumnName("descriptionSYS10");

		gridLevels.getColumnContainer().add(colLevel, null);
		gridLevels.getColumnContainer().add(colLevDescr, null);
    gridLevels.getColumnContainer().add(colButton, null);

		filterPanel.add(companyLabel,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(controlCompaniesCombo,       new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonsPanel,   new GridBagConstraints(0, 1, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		colButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ItemSheetLevelVO vo = (ItemSheetLevelVO)gridLevels.getVOListTableModel().getObjectForRow(gridLevels.getSelectedRow());
				new ItemSheetsForALevelFrame(vo);
			}

		});

	}



		public Object getCompanyCodeSys01() {
			Object companyCodeSys01 = controlCompaniesCombo.getValue();
			if (companyCodeSys01==null)
				companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();
			return companyCodeSys01;
		}

  public GridControl getGridLevels() {
    return gridLevels;
  }



}
