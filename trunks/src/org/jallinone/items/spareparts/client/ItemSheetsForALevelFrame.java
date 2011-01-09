package org.jallinone.items.spareparts.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.items.spareparts.java.ItemSheetLevelVO;
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
import java.lang.reflect.Method;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define sheets for a specific level of the spare parts catalogue.</p>
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
public class ItemSheetsForALevelFrame extends InternalFrame {

	private ItemSheetLevelVO levelVO = null;
	ArrayList additionalCols = new ArrayList();

	GridControl grid = new GridControl();

	ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	ImagePanel imagePanel = new ImagePanel();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	TextColumn colSheetDescr = new TextColumn();
	TextColumn colSheetCode = new TextColumn();

	/** sheet code lookup data locator */
	LookupServerDataLocator sheetDataLocator = new LookupServerDataLocator();

	/** sheet code lookup controller */
	LookupController sheetController = new LookupController();
  FileColumn colFile = new FileColumn();
  JSplitPane split = new JSplitPane();
  JPanel gridPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  BorderLayout borderLayout2 = new BorderLayout();

	InsertButton insertButton = new InsertButton();
	EditButton editButton = new EditButton();
	ReloadButton reloadButton = new ReloadButton();
	DeleteButton deleteButton = new DeleteButton();
	SaveButton saveButton = new SaveButton();
  JPanel imgContainerPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();


	public ItemSheetsForALevelFrame(ItemSheetLevelVO levelVO) {
		this.levelVO = levelVO;
		grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,levelVO.getCompanyCodeSys01ITM29());
		grid.getOtherGridParams().put(ApplicationConsts.LEVEL,levelVO.getLevITM29());
		grid.setController(new ItemSheetsForALevelController(this));

		grid.setGridDataLocator(gridDataLocator);
		gridDataLocator.setServerMethodName("loadItemSheets");
		try {
			jbInit();
			init();
			setSize(750,600);
			MDIFrame.add(this,true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void init() {
		// define sheets grid additional columns..
		TextColumn tCol = null;
		DateColumn dCol = null;
		DecimalColumn nCol = null;
		for(int i=0;i<10;i++) {
			tCol = new TextColumn();
			tCol.setColumnName("sProp"+i+"ITM25");
			tCol.setColumnVisible(false);
			tCol.setColumnSelectable(false);
			tCol.setPreferredWidth(100);
			additionalCols.add(tCol);
			grid.getColumnContainer().add(tCol,null);

			dCol = new DateColumn();
			dCol.setColumnName("dProp"+i+"ITM25");
			dCol.setColumnVisible(false);
			dCol.setColumnSelectable(false);
			tCol.setPreferredWidth(80);
			additionalCols.add(dCol);
			grid.getColumnContainer().add(dCol,null);

			nCol = new DecimalColumn();
			nCol.setColumnName("nProp"+i+"ITM25");
			nCol.setColumnVisible(false);
			nCol.setColumnSelectable(false);
			nCol.setPreferredWidth(60);
			additionalCols.add(nCol);
			grid.getColumnContainer().add(nCol,null);
		}


		// define additional columns visibilities...
		try {
			if (levelVO!=null) {
				Method m = null;
				Method md = null;
				for(int i=0;i<10;i++) {

					m = ItemSheetLevelVO.class.getMethod("getSProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getSProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(levelVO,new Object[0])!=null) {
						((Column)additionalCols.get(i*3)).setColumnVisible(true);
						((Column)additionalCols.get(i*3)).setHeaderColumnName((String)md.invoke(levelVO,new Object[0]));
						((Column)additionalCols.get(i*3)).setEditableOnInsert(true);
						((Column)additionalCols.get(i*3)).setEditableOnEdit(true);
					}
					else
						((Column)additionalCols.get(i)).setColumnRequired(false);

					m = ItemSheetLevelVO.class.getMethod("getDProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getDProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(levelVO,new Object[0])!=null) {
						((Column)additionalCols.get(i*3+1)).setColumnVisible(true);
						((Column)additionalCols.get(i*3+1)).setHeaderColumnName((String)md.invoke(levelVO,new Object[0]));
						((Column)additionalCols.get(i*3+1)).setEditableOnInsert(true);
						((Column)additionalCols.get(i*3+1)).setEditableOnEdit(true);
					}
					else
						((Column)additionalCols.get(i+10)).setColumnRequired(false);

					m = ItemSheetLevelVO.class.getMethod("getNProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getNProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(levelVO,new Object[0])!=null) {
						((Column)additionalCols.get(i*3+2)).setColumnVisible(true);
						((Column)additionalCols.get(i*3+2)).setHeaderColumnName((String)md.invoke(levelVO,new Object[0]));
						((Column)additionalCols.get(i*3+2)).setEditableOnInsert(true);
						((Column)additionalCols.get(i*3+2)).setEditableOnEdit(true);
					}
					else
						((Column)additionalCols.get(i+20)).setColumnRequired(false);
				}
			}
			else {
				OptionPane.showMessageDialog(MDIFrame.getInstance(),
																		 "Level description not found!",
																		 "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
	   }
		 catch (Throwable t) {
			 OptionPane.showMessageDialog(MDIFrame.getInstance(), t.getMessage(),
																		"Error", JOptionPane.ERROR_MESSAGE);
			 return;
		}

	}



	private void jbInit() throws Exception {

		grid.setDeleteButton(deleteButton);
		grid.setInsertButton(insertButton);
		grid.setReloadButton(reloadButton);
		grid.setEditButton(editButton);
		grid.setSaveButton(saveButton);

		buttonsPanel.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);

		colSheetCode.setTrimText(true);
    colSheetCode.setUpperCase(true);
    imagePanel.setMinimumSize(new Dimension(1024, 1024));
    imagePanel.setPreferredSize(new Dimension(1024, 1024));
    imgContainerPanel.setLayout(borderLayout3);
		buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);

		grid.setMaxNumberOfRowsOnInsert(1);
		grid.setFunctionId("ITM29");
		grid.setAllowColumnsPermission(false);
		grid.setAllowColumnsProfile(false);

		this.setTitle(ClientSettings.getInstance().getResources().getResource("spare parts sheets for level")+" "+levelVO.getDescriptionSYS10());

		grid.setValueObjectClassName("org.jallinone.items.spareparts.java.ItemSheetVO");
		grid.setAllowColumnsPermission(false);
		grid.setAllowColumnsProfile(false);
		grid.setMaxSortedColumns(1);
		colSheetDescr.setColumnName("descriptionSYS10");
    colSheetDescr.setEditableOnEdit(true);
    colSheetDescr.setEditableOnInsert(true);
		colSheetDescr.setPreferredWidth(250);
		colSheetCode.setColumnName("sheetCodeITM25");
    colSheetCode.setEditableOnInsert(true);
    colSheetCode.setMinWidth(0);
    colSheetCode.setPreferredWidth(90);
    colSheetCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colSheetCode.setSortingOrder(1);

		this.getContentPane().setLayout(borderLayout2);
//		this.getContentPane().add(filterPanel, BorderLayout.NORTH);
		colFile.setColumnName("imageITM25");
    colFile.setFileNameAttributeName("imageNameITM25");
    colFile.setEditableOnEdit(true);
    colFile.setEditableOnInsert(true);
    colFile.setHeaderColumnName("image");
    colFile.setPreferredWidth(100);
		colFile.setMinWidth(100);
		colFile.setMaxWidth(100);
		 colFile.setShowDownloadButton(false);
		 colFile.setColumnRequired(false);

    gridPanel.setLayout(borderLayout1);
    grid.getColumnContainer().add(colSheetCode, null);
    grid.getColumnContainer().add(colSheetDescr, null);
    grid.getColumnContainer().add(colFile, null);
    gridPanel.add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);

		split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    split.add(imgContainerPanel, JSplitPane.BOTTOM);
    imgContainerPanel.add(imagePanel,  BorderLayout.CENTER);
    split.add(gridPanel, JSplitPane.TOP);
    gridPanel.add(grid, BorderLayout.CENTER);
    split.setDividerLocation(300);

	}


	public GridControl getGrid() {
		return grid;
	}
  public ItemSheetLevelVO getLevelVO() {
    return levelVO;
  }
  public ImagePanel getImagePanel() {
    return imagePanel;
  }



}
