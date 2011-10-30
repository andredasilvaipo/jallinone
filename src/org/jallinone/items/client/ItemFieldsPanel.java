package org.jallinone.items.client;

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
import java.sql.Types;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel containing the grid for item fields.</p>
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
public class ItemFieldsPanel extends JPanel {

	JPanel buttonsPanel = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	ReloadButton reloadButton = new ReloadButton();
	GridControl grid = new GridControl();

	EditButton editButton = new EditButton();
	SaveButton saveButton = new SaveButton();


	/** grid data locator */
	private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	TextColumn colItemField = new TextColumn();
	JPanel topPanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridController controller = new ItemFieldsController(this);
  CheckBoxColumn colSel = new CheckBoxColumn();
  ComboColumn colType = new ComboColumn();
  IntegerColumn colPos = new IntegerColumn();
  TextColumn colName = new TextColumn();


	public ItemFieldsPanel() {
  	grid.setController(controller);
		grid.setGridDataLocator(gridDataLocator);
		gridDataLocator.setServerMethodName("loadItemFields");
		try {
			init();
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}



	private void init() {
		Domain d = new Domain("SQL_TYPES");
		d.addDomainPair(new Integer(Types.VARCHAR),"text");
		d.addDomainPair(new Integer(Types.INTEGER),"numeric");
		d.addDomainPair(new Integer(Types.TIMESTAMP),"datetime");
		d.addDomainPair(new Integer(Types.DATE),"date");
		d.addDomainPair(new Integer(Types.DECIMAL),"decimal");
		colType.setDomain(d);
	}


	private void jbInit() throws Exception {
		grid.setAutoLoadData(false);
		this.setLayout(new BorderLayout());
		grid.setMaxNumberOfRowsOnInsert(50);
		grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		saveButton.setExecuteAsThread(true);
		grid.setValueObjectClassName("org.jallinone.items.java.ItemFieldVO");
		buttonsPanel.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);
		grid.setEditButton(editButton);
		grid.setMaxSortedColumns(3);
		grid.setReloadButton(reloadButton);
		grid.setSaveButton(saveButton);
		colItemField.setMaxCharacters(120);
		colItemField.setTrimText(true);
		colItemField.setUpperCase(true);
    colItemField.setAdditionalHeaderColumnName("field name");
		colItemField.setColumnFilterable(true);
		colItemField.setColumnName("fieldNameITM32");
		colItemField.setHeaderColumnName("ItemField code");
    colItemField.setPreferredWidth(150);
		colItemField.setColumnSortable(true);
		colItemField.setEditableOnInsert(true);
		colItemField.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
		colItemField.setSortingOrder(1);
		topPanel.setLayout(gridBagLayout1);
		colSel.setColumnName("selectedITM32");
    colSel.setAdditionalHeaderColumnName("sel");
    colSel.setEditableOnEdit(true);
    colSel.setPreferredWidth(70);
		colSel.setShowDeSelectAllInPopupMenu(true);
    colType.setColumnName("fieldTypeITM32");
    colType.setPreferredWidth(130);
    colPos.setColumnName("posTM32");
    colPos.setEditableOnEdit(true);
    colPos.setPreferredWidth(50);
		colName.setColumnName("descriptionSYS10");
    colName.setEditableOnEdit(true);
    colName.setEditableOnInsert(false);
    colName.setPreferredWidth(150);
    buttonsPanel.add(editButton, null);
		buttonsPanel.add(saveButton, null);
		buttonsPanel.add(reloadButton, null);
		this.add(grid, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);

		topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    grid.getColumnContainer().add(colSel, null);
    grid.getColumnContainer().add(colItemField, null);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colType, null);
    grid.getColumnContainer().add(colPos, null);
	}

	public GridControl getGrid() {
		return grid;
	}



}
