package org.jallinone.items.spareparts.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import org.jallinone.commons.java.*;
import org.jallinone.items.spareparts.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.math.BigDecimal;
import java.lang.reflect.Method;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.table.columns.client.DateColumn;
import org.openswing.swing.table.columns.client.DecimalColumn;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the item models grid frame.</p>
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
public class ItemSheetsGridPanel	extends JPanel {

	JPanel buttonsPanel2 = new JPanel();
	JPanel buttonsPanel3 = new JPanel();
	JPanel bottomPanel = new JPanel();
	FlowLayout flowLayout3 = new FlowLayout();
	FlowLayout flowLayout4 = new FlowLayout();

	GridControl gridSheets = new GridControl();
	GridControl gridSpareParts = new GridControl();

	InsertButton insertButton3 = new InsertButton();
	ReloadButton reloadButton3 = new ReloadButton();
	DeleteButton deleteButton3 = new DeleteButton();
	SaveButton saveButton3 = new SaveButton();

	InsertButton insertButton2 = new InsertButton();
	ReloadButton reloadButton2 = new ReloadButton();
	DeleteButton deleteButton2 = new DeleteButton();
	SaveButton saveButton2 = new SaveButton();

	private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	private ServerGridDataLocator gridDataLocator3 = new ServerGridDataLocator();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JSplitPane split = new JSplitPane();
	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	TitledBorder titledBorder3;
	TextColumn colSheetDescr = new TextColumn();
	CodLookupColumn colSheetCode = new CodLookupColumn();
	JPanel rightPanel = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JSplitPane leftPanel = new JSplitPane();
	SheetAttachedDocsPanel docsPanel;
	JPanel sparePartsPanel = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	BorderLayout borderLayout3 = new BorderLayout();
	JPanel navigationPanel = new JPanel();
	FlowLayout flowLayout2 = new FlowLayout();
	LabelControl labelNav = new LabelControl();
	JSplitPane topPanel = new JSplitPane();
	JPanel sheetsPanel = new JPanel();
	BorderLayout borderLayout4 = new BorderLayout();
	JScrollPane imgScrollPane = new JScrollPane();
	SheetImage imgPanel = new SheetImage();

	private ArrayList sheetVOs = new ArrayList();
	private ItemSheetVO parentVO = null;
	private ArrayList points = new ArrayList();
	private ArrayList pols = new ArrayList();
	private byte[] bytes = null;
	JPopupMenu popup = new JPopupMenu();
	JMenuItem menuStart = new JMenuItem(ClientSettings.getInstance().getResources().
																			getResource("start region"));
	JMenuItem menuClear = new JMenuItem(ClientSettings.getInstance().getResources().
																			getResource("clear region"));
	JMenuItem menuSave = new JMenuItem(ClientSettings.getInstance().getResources().
																		 getResource("save region"));

	LookupController sheetController = new LookupController();
	LookupServerDataLocator sheetDataLocator = new LookupServerDataLocator();

	/** item code lookup data locator */
	LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

	/** item code lookup controller */
	LookupController itemController = new LookupController();

	LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
	TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  CodLookupColumn colItemCode = new CodLookupColumn();
  TextColumn colItemDescr = new TextColumn();

	ArrayList additionalCols = new ArrayList();
	HashMap levelsPerCompanyCode = new HashMap(); // collection of pairs <companyCode_level,ItemSheetLevelVO>
	private boolean readOnly = false;
  BorderLayout borderLayout5 = new BorderLayout();
	private	java.util.List itemTypes = null;
	ComboColumn colItemType = new ComboColumn();
	private GridControl itemSparePartsGrid = null;
	private SparePartsCatalogueCallbacks callbacks = null;


	public ItemSheetsGridPanel() {
		this(true,null);
	}


	public ItemSheetsGridPanel(boolean readOnly,GridControl itemSparePartsGrid) {
		this.readOnly = readOnly;
		this.itemSparePartsGrid = itemSparePartsGrid;
		try {
			docsPanel = new SheetAttachedDocsPanel(readOnly);
			jbInit();
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void init() {
		// define combo on item types...
		Response res = ClientUtils.getData("loadItemTypes",new GridParams());
		final Domain d = new Domain("ITEM_TYPES");
		if (!res.isError()) {
			ItemTypeVO vo = null;
			itemTypes = ((VOListResponse)res).getRows();
			for(int i=0;i<itemTypes.size();i++) {
				vo = (ItemTypeVO)itemTypes.get(i);
				d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
			}
		}
		colItemType.setDomain(d);
		colItemType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==e.SELECTED) {
					try {
						int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
						Object selValue = d.getDomainPairList()[selIndex].getCode();
						ItemTypeVO vo = (ItemTypeVO)itemTypes.get(selIndex);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
						treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());
						itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM02());

//						controlItemCode.setValue(null);
//						controlItemCode.validateCode(null);
					}
					catch (Exception ex) {
					}

				}
			}
		});

		// define grids...
		gridSheets.setAutoLoadData(false);
		docsPanel.getDocsGrid().setAutoLoadData(false);

		gridSheets.setController(new ItemSheetsGridPanelController(this));
		gridSheets.setGridDataLocator(gridDataLocator);
		gridDataLocator.setServerMethodName("loadItemSheets");

		gridSpareParts.setController(new SheetSparePartsController(this));
		gridSpareParts.setGridDataLocator(gridDataLocator3);
		gridDataLocator3.setServerMethodName("loadSheetSpareParts");

		// define popup menu...
		menuStart.setEnabled(false);
		menuSave.setEnabled(false);
		popup.add(menuStart);
		popup.add(menuClear);
		popup.add(menuSave);
		menuStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				menuStart.setEnabled(false);
			}

		});
		menuClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				points.clear();
				imgPanel.repaint();
				menuStart.setEnabled(true);
			}

		});
		menuSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String polStr = "";
				for (int i = 0; i < points.size(); i++) {
					polStr += points.get(i) + "\n";

				}
				ItemSheetVO sheetVO = (ItemSheetVO) gridSheets.getVOListTableModel().getObjectForRow(gridSheets.getSelectedRow());
				SubsheetVO vo = new SubsheetVO();
				vo.setCompanyCodeSys01ITM30(sheetVO.getCompanyCodeSys01ITM25());
				vo.setParentSheetCodeItm25ITM30(parentVO.getSheetCodeITM25());
				vo.setSheetCodeItm25ITM30(sheetVO.getSheetCodeITM25());
				vo.setPolygonITM30(polStr);

				Response res = ClientUtils.getData("updateSubsheet", vo);
				points.clear();
				imgPanel.repaint();
				menuStart.setEnabled(true);
				if (res.isError()) {
					OptionPane.showMessageDialog(MDIFrame.getInstance(),
																			 res.getErrorMessage(), "Error",
																			 JOptionPane.ERROR_MESSAGE);
				}
				else {
					Polygon pol = getPolygon(vo);
					pols.set(gridSheets.getSelectedRow(), pol);
					imgPanel.repaint();
				}
			}

		});

    // define image panel management...
		imgPanel.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				try {
					// check if user is over a matching region...
					Polygon pol = null;
					for (int i = 0; i < pols.size(); i++) {
						pol = (Polygon) pols.get(i);
						if (pol != null && pol.contains(e.getX(), e.getY())) {
							imgPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
							return;
						}
					}
					imgPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
		imgPanel.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
				try {
					imgPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				catch (Exception ex) {
				}
			}


			public void mouseClicked(MouseEvent e) {
				try {
					if (e.getClickCount() == 1 && parentVO != null && bytes != null &&
							gridSheets.getSelectedRow() != -1) {
						if (SwingUtilities.isRightMouseButton(e) && !readOnly) {
							popup.show(imgPanel, e.getX(), e.getY());
						}
						else if (SwingUtilities.isLeftMouseButton(e) && !menuStart.isEnabled()) {
							// user has clicked on the image, in order to define a region...
							points.add(e.getX() + "," + e.getY());
							imgPanel.repaint();
						}
						else if (SwingUtilities.isLeftMouseButton(e) && menuStart.isEnabled()) {
							// user has clicked on the image: check for a matching region...
							Polygon pol = null;
							for (int i = 0; i < pols.size(); i++) {
								pol = (Polygon) pols.get(i);
								if (pol != null && pol.contains(e.getX(), e.getY())) {
									ItemSheetVO sheetVO = (ItemSheetVO) gridSheets.getVOListTableModel().getObjectForRow(i);
									addLink(sheetVO);
									break;
								}
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

		});

	  // define sheets lookup...
		sheetDataLocator.setGridMethodName("loadItemSheets");
		sheetDataLocator.setValidationMethodName("validateSheetCode");

		colSheetCode.setLookupController(sheetController);
		sheetController.setLookupDataLocator(sheetDataLocator);
		sheetController.setFrameTitle("sheets");
		sheetController.setLookupValueObjectClassName("org.jallinone.items.spareparts.java.ItemSheetVO");
		Method[] mm = ItemSheetVO.class.getMethods();
		String attr = null;
		for(int i=0;i<mm.length;i++) {
			if (mm[i].getName().startsWith("get") && mm[i].getName().endsWith("ITM25")) {
				attr = mm[i].getName().substring(3);
				attr = attr.substring(0,1).toLowerCase()+attr.substring(1);
				sheetController.addLookup2ParentLink(attr,attr);
			}
		}
		sheetController.setAllColumnVisible(false);
		sheetController.setVisibleColumn("sheetCodeITM25", true);
		sheetController.setVisibleColumn("descriptionSYS10", true);
		sheetController.setPreferredWidthColumn("descriptionSYS10",250);
		sheetController.setFramePreferedSize(new Dimension(370,500));
		sheetController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				ItemSheetVO vo = (ItemSheetVO)sheetController.getLookupVO();
				gridSheets.getVOListTableModel().updateObjectAt(vo,gridSheets.getSelectedRow());
			}

			public void beforeLookupAction(ValueObject gridVO) {
				sheetDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
				sheetDataLocator.getLookupFrameParams().put(ApplicationConsts.ID,parentVO.getSheetCodeITM25());
				sheetDataLocator.getLookupFrameParams().put(ApplicationConsts.LEVEL,parentVO.getLevITM25().add(new BigDecimal(1)));

				sheetDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
				sheetDataLocator.getLookupValidationParameters().put(ApplicationConsts.ID,parentVO.getSheetCodeITM25());
				sheetDataLocator.getLookupValidationParameters().put(ApplicationConsts.LEVEL,parentVO.getLevITM25().add(new BigDecimal(1)));
			}

			public void forceValidate() {}

		});

	  // define spare parts lookup...
		itemDataLocator.setGridMethodName("loadItems");
		itemDataLocator.setValidationMethodName("validateItemCode");

		colItemCode.setLookupController(itemController);
		itemController.setLookupDataLocator(itemDataLocator);
		itemController.setFrameTitle("items");

		itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
		treeLevelDataLocator.setServerMethodName("loadHierarchy");
		itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
		itemDataLocator.setNodeNameAttribute("descriptionSYS10");

		itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
		itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01ITM27");
		itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01ITM27");
		itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

		itemController.setAllColumnVisible(false);
		itemController.setVisibleColumn("companyCodeSys01ITM01", true);
		itemController.setVisibleColumn("itemCodeITM01", true);
		itemController.setVisibleColumn("descriptionSYS10", true);
		itemController.setPreferredWidthColumn("descriptionSYS10", 200);
		itemController.setFramePreferedSize(new Dimension(650,500));
		itemController.setShowErrorMessage(false);
		itemController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

			public void beforeLookupAction(ValueObject gridVO) {
				itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
				itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
			}

			public void forceValidate() {}

		});

		// define sheets grid additional columns..
		TextColumn tCol = null;
		DateColumn dCol = null;
		DecimalColumn nCol = null;
		for(int i=0;i<10;i++) {
			tCol = new TextColumn();
			tCol.setColumnName("sProp"+i+"ITM25");
			tCol.setColumnVisible(false);
			tCol.setColumnRequired(false);
			tCol.setPreferredWidth(80);
	    additionalCols.add(tCol);
			gridSheets.getColumnContainer().add(tCol,null);

			dCol = new DateColumn();
			dCol.setColumnName("dProp"+i+"ITM25");
			dCol.setColumnVisible(false);
			dCol.setColumnRequired(false);
			dCol.setPreferredWidth(80);
			additionalCols.add(dCol);
			gridSheets.getColumnContainer().add(dCol,null);

			nCol = new DecimalColumn();
			nCol.setColumnName("nProp"+i+"ITM25");
			nCol.setColumnVisible(false);
			nCol.setColumnRequired(false);
			nCol.setPreferredWidth(60);
			additionalCols.add(nCol);
			gridSheets.getColumnContainer().add(nCol,null);
		}

		// retrieve levels info...
		res = ClientUtils.getData("loadItemSheetLevels",null);
		if (res.isError()) {
			OptionPane.showMessageDialog(MDIFrame.getInstance(), res.getErrorMessage(),
																	 "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			java.util.List vos = ((VOListResponse)res).getRows();
			ItemSheetLevelVO vo = null;
			for(int i=0;i<vos.size();i++) {
				vo = (ItemSheetLevelVO)vos.get(i);
				levelsPerCompanyCode.put(vo.getCompanyCodeSys01ITM29()+"_"+vo.getLevITM29(),vo);
			}
		}
	}


	public final void loadSheetsCompleted() {
		ItemSheetVO vo = null;
		Polygon pol = null;
		pols.clear();
		for (int i = 0; i < gridSheets.getVOListTableModel().getRowCount(); i++) {
			vo = (ItemSheetVO) gridSheets.getVOListTableModel().getObjectForRow(i);
			pol = getPolygon(vo.getSubsheet());
			pols.add(pol);
		}
		imgPanel.repaint();
	}


	public final void loadSparePartsCompleted() {
//		if (gridSpareParts.getVOListTableModel().getRowCount() > 0) {
//			topPanel.setDividerLocation(leftPanel.getHeight());
//		}
//		else {
//			topPanel.setDividerLocation(0);
//		}
	}


	private Polygon getPolygon(SubsheetVO vo) {
		if (vo.getPolygonITM30() != null && vo.getPolygonITM30().length() > 0) {
			String[] aux = vo.getPolygonITM30().split("\n");
			String[] point = null;
			int[] xx = new int[aux.length];
			int[] yy = new int[aux.length];
			for (int i = 0; i < aux.length; i++) {
				point = aux[i].split(",");
				xx[i] = Integer.parseInt(point[0]);
				yy[i] = Integer.parseInt(point[1]);
			}
			return new Polygon(xx, yy, aux.length);
		}
		else {
			return null;
		}
	}


	private void jbInit() throws Exception {
	  colItemType.setColumnName("progressiveHie02ITM01");
		colItemType.setEditableOnInsert(true);

		titledBorder1 = new TitledBorder("");
		titledBorder2 = new TitledBorder("");
		titledBorder3 = new TitledBorder("");
		buttonsPanel2.setLayout(flowLayout3);
		buttonsPanel3.setLayout(flowLayout4);
		flowLayout3.setAlignment(FlowLayout.LEFT);
		flowLayout4.setAlignment(FlowLayout.LEFT);

		gridSheets.setValueObjectClassName(
			"org.jallinone.items.spareparts.java.ItemSheetVO");
		gridSheets.setDeleteButton(deleteButton2);
		gridSheets.setFunctionId("SPARE_PART_CAT_DEF");
		gridSheets.setMaxNumberOfRowsOnInsert(1000);
		gridSheets.setAllowColumnsPermission(false);
		gridSheets.setAllowColumnsProfile(false);
		gridSheets.setMaxSortedColumns(1);
		gridSheets.setInsertButton(insertButton2);
		gridSheets.setReloadButton(reloadButton2);
		gridSheets.setSaveButton(saveButton2);

		docsPanel.getDocsGrid().setValueObjectClassName(
			"org.jallinone.items.spareparts.java.SheetAttachedDocVO");
		docsPanel.getDocsGrid().setFunctionId("SPARE_PART_CAT_DEF");
		docsPanel.getDocsGrid().setMaxNumberOfRowsOnInsert(1000);
		docsPanel.getDocsGrid().setAllowColumnsPermission(false);
		docsPanel.getDocsGrid().setAllowColumnsProfile(false);
		docsPanel.getDocsGrid().setMaxSortedColumns(1);

		gridSpareParts.setValueObjectClassName("org.jallinone.items.spareparts.java.SheetSparePartVO");
		gridSpareParts.setDeleteButton(deleteButton3);
		gridSpareParts.setFunctionId("SPARE_PART_CAT_DEF");
		gridSpareParts.setMaxNumberOfRowsOnInsert(1000);
		gridSpareParts.setAllowColumnsPermission(false);
		gridSpareParts.setAllowColumnsProfile(false);
		gridSpareParts.setMaxSortedColumns(1);
		gridSpareParts.setInsertButton(insertButton3);
		gridSpareParts.setReloadButton(reloadButton3);
		gridSpareParts.setSaveButton(saveButton3);

		titledBorder1.setTitleColor(Color.blue);
		titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("documents"));
		titledBorder2.setTitleColor(Color.blue);
		titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("sheets"));
		titledBorder3.setTitleColor(Color.blue);
		titledBorder3.setTitle(ClientSettings.getInstance().getResources().getResource("spare parts"));

		colSheetDescr.setColumnName("descriptionSYS10");
		colSheetDescr.setPreferredWidth(150);
		colSheetCode.setColumnName("sheetCodeITM25");
		colSheetCode.setEditableOnInsert(true);
		colSheetCode.setPreferredWidth(80);

		bottomPanel.setBorder(titledBorder1);
		bottomPanel.setLayout(borderLayout5);
		bottomPanel.setMaximumSize(new Dimension(300, 1024));
		rightPanel.setLayout(borderLayout1);
		split.setDividerSize(5);
		leftPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		leftPanel.setDividerSize(5);
		sparePartsPanel.setLayout(borderLayout3);
		sparePartsPanel.setBorder(titledBorder3);
		navigationPanel.setLayout(flowLayout2);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		labelNav.setLabel("path");
		sheetsPanel.setLayout(borderLayout4);
		colItemCode.setColumnName("itemCodeItm01ITM27");
		colItemCode.setHeaderColumnName("itemCodeItm01ITM28");
    colItemCode.setEditableOnInsert(true);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setPreferredWidth(150);
    sheetsPanel.setBorder(titledBorder2);
    buttonsPanel2.add(insertButton2, null);
		buttonsPanel2.add(saveButton2, null);
		buttonsPanel2.add(reloadButton2, null);
		buttonsPanel2.add(deleteButton2, null);
		bottomPanel.add(docsPanel, BorderLayout.CENTER);

		buttonsPanel3.add(insertButton3, null);
		buttonsPanel3.add(saveButton3, null);
		buttonsPanel3.add(reloadButton3, null);
		buttonsPanel3.add(deleteButton3, null);


		this.setLayout(new BorderLayout());
		this.add(split, BorderLayout.CENTER);
		this.add(navigationPanel, BorderLayout.NORTH);

		gridSheets.getColumnContainer().add(colSheetCode, null);
		gridSheets.getColumnContainer().add(colSheetDescr, null);

		if (!readOnly) {
			sheetsPanel.add(buttonsPanel2, BorderLayout.NORTH);
			sparePartsPanel.add(buttonsPanel3, BorderLayout.NORTH);
		}

		sheetsPanel.add(gridSheets, BorderLayout.CENTER);

		sparePartsPanel.add(gridSpareParts, BorderLayout.CENTER);
		gridSpareParts.getColumnContainer().add(colItemType, null);
    gridSpareParts.getColumnContainer().add(colItemCode, null);
		topPanel.add(sheetsPanel, JSplitPane.BOTTOM);

		topPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		topPanel.add(sparePartsPanel, JSplitPane.TOP);

		leftPanel.add(topPanel, JSplitPane.TOP);
		leftPanel.add(bottomPanel, JSplitPane.BOTTOM);
		split.add(rightPanel, JSplitPane.RIGHT);
		split.add(leftPanel, JSplitPane.LEFT);
		navigationPanel.add(labelNav, null);
		rightPanel.add(imgScrollPane, BorderLayout.CENTER);
		imgScrollPane.getViewport().add(imgPanel, null);
    gridSpareParts.getColumnContainer().add(colItemDescr, null);
		split.setDividerLocation(350);
		leftPanel.setDividerLocation(200);
		topPanel.setDividerLocation(0);
		topPanel.setDividerSize(0);
	}

	private void reloadContent() {

		// define additional columns visibilities...
		ItemSheetLevelVO vo = (ItemSheetLevelVO)levelsPerCompanyCode.get(parentVO.getCompanyCodeSys01ITM25()+"_"+parentVO.getLevITM25().add(new BigDecimal(1)));
		try {
			if (vo!=null) {
				Method m = null;
				Method md = null;
				int index;
				for(int i=0;i<10;i++) {

					m = ItemSheetLevelVO.class.getMethod("getSProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getSProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(vo,new Object[0])!=null) {
						gridSheets.setVisibleColumn("sProp"+i+"ITM25",true);
						index = gridSheets.getTable().getGrid().convertColumnIndexToView(gridSheets.getTable().getGrid().getColumnIndex("sProp"+i+"ITM25"));
						gridSheets.getTable().getGrid().getColumnModel().getColumn(index).setHeaderValue((String)md.invoke(vo,new Object[0]));
					}
					else
						gridSheets.setVisibleColumn("sProp"+i+"ITM25",false);

					m = ItemSheetLevelVO.class.getMethod("getDProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getDProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(vo,new Object[0])!=null) {
						gridSheets.setVisibleColumn("dProp"+i+"ITM25",true);
						index = gridSheets.getTable().getGrid().convertColumnIndexToView(gridSheets.getTable().getGrid().getColumnIndex("dProp"+i+"ITM25"));
						gridSheets.getTable().getGrid().getColumnModel().getColumn(index).setHeaderValue((String)md.invoke(vo,new Object[0]));
					}
					else
						gridSheets.setVisibleColumn("dProp"+i+"ITM25",false);

					m = ItemSheetLevelVO.class.getMethod("getNProp"+i+"ProgressiveSys10ITM29",new Class[0]);
					md = ItemSheetLevelVO.class.getMethod("getNProp"+i+"DescriptionSys10ITM29",new Class[0]);
					if (m.invoke(vo,new Object[0])!=null) {
						gridSheets.setVisibleColumn("nProp"+i+"ITM25",true);
						index = gridSheets.getTable().getGrid().convertColumnIndexToView(gridSheets.getTable().getGrid().getColumnIndex("nProp"+i+"ITM25"));
						gridSheets.getTable().getGrid().getColumnModel().getColumn(index).setHeaderValue((String)md.invoke(vo,new Object[0]));
					}
					else
						gridSheets.setVisibleColumn("nProp"+i+"ITM25",false);

				}
				topPanel.setDividerLocation(0);
			}
			else {
				// leaf found!
				topPanel.setDividerLocation(leftPanel.getHeight());


//				OptionPane.showMessageDialog(MDIFrame.getInstance(),
//																		 "Level description not found!",
//																		 "Error", JOptionPane.ERROR_MESSAGE);
//				return;
			}
	 }
		 catch (Throwable t) {
			 OptionPane.showMessageDialog(MDIFrame.getInstance(), t.getMessage(),
																		"Error", JOptionPane.ERROR_MESSAGE);
			 return;
		}



		gridSheets.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
		gridSheets.getOtherGridParams().put(ApplicationConsts.ID,parentVO.getSheetCodeITM25());

		docsPanel.getDocsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01, parentVO.getCompanyCodeSys01ITM25());
		docsPanel.getDocsGrid().getOtherGridParams().put(ApplicationConsts.ID,parentVO.getSheetCodeITM25());

		gridSpareParts.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01ITM25());
		gridSpareParts.getOtherGridParams().put(ApplicationConsts.ID,parentVO.getSheetCodeITM25());

		gridSheets.reloadData();
		docsPanel.getDocsGrid().reloadData();
		gridSpareParts.reloadData();

		Response res = ClientUtils.getData("loadItemSheetImage", parentVO);
		if (res.isError()) {
			OptionPane.showMessageDialog(MDIFrame.getInstance(), res.getErrorMessage(),
																	 "Error", JOptionPane.ERROR_MESSAGE);
			bytes = null;
		}
		else {
			ItemSheetVO tvo = (ItemSheetVO)((VOResponse)res).getVo();
			bytes = tvo.getImageITM25();
			imgPanel.setImage(bytes);
			imgScrollPane.getViewport().revalidate();
		}
	}

	public final void addLink(final ItemSheetVO parentVO) {

	  this.parentVO = parentVO;
		LinkButton linkButton = new LinkButton();
		linkButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int index = sheetVOs.indexOf(parentVO);
				while (sheetVOs.size() > index + 1) {
					navigationPanel.remove(sheetVOs.size());
					sheetVOs.remove(sheetVOs.size() - 1);
				}
				navigationPanel.revalidate();
				navigationPanel.repaint();

				ItemSheetsGridPanel.this.parentVO = (ItemSheetVO) sheetVOs.get(sheetVOs.size() - 1);

//				ItemSheetLevelVO vo = (ItemSheetLevelVO)levelsPerCompanyCode.get(parentVO.getCompanyCodeSys01ITM25()+"_"+parentVO.getLevITM25().add(new BigDecimal(1)));
//				if (vo!=null)
//					topPanel.setDividerLocation(0);
//				else
//					topPanel.setDividerLocation(leftPanel.getHeight());
//
				reloadContent();
			}

		});
		linkButton.setLabel(parentVO.getDescriptionSYS10());
		sheetVOs.add(parentVO);
		navigationPanel.add(linkButton, null);
		navigationPanel.revalidate();
		navigationPanel.repaint();

		reloadContent();

	}


	public final void clearData() {
		gridSheets.clearData();
		gridSpareParts.clearData();
		docsPanel.getDocsGrid().clearData();

		while (sheetVOs.size() > 0) {
			navigationPanel.remove(sheetVOs.size());
			sheetVOs.remove(sheetVOs.size() - 1);
		}
		navigationPanel.revalidate();
		navigationPanel.repaint();

		bytes = null;
		imgPanel.setImage(bytes);

	}



	public GridControl getGrid() {
		return gridSheets;
	}

	public ItemSheetVO getParentVO() {
		return parentVO;
	}

	public JMenuItem getMenuStart() {
		return menuStart;
	}

	public JMenuItem getMenuSave() {
		return menuSave;
	}

	public ArrayList getPoints() {
		return points;
	}
  public SheetImage getImgPanel() {
    return imgPanel;
  }
  public GridControl getItemSparePartsGrid() {
    return itemSparePartsGrid;
  }
  public void setCallbacks(SparePartsCatalogueCallbacks callbacks) {
    this.callbacks = callbacks;
  }
  public SparePartsCatalogueCallbacks getCallbacks() {
    return callbacks;
  }

	/**
	 * <p>Title: JAllInOne</p>
	 * <p>Description: Inner class used to show the optional image.</p>
	 * @author Mauro Carniel
	 * @version 1.0
	 */
	class SheetImage extends JPanel {

		/** image */
		private Image img = null;

		public void setImage(byte[] bytes) {
			if (bytes != null) {
				img = new ImageIcon(bytes).getImage();
				this.setPreferredSize(new Dimension(img.getWidth(this),img.getHeight(this)));
				imgScrollPane.getViewport().setPreferredSize(new Dimension(img.getWidth(this),img.getHeight(this)));
			}
			else {
				img = null;
			}
			repaint();
		}

		public final void paint(Graphics g) {
			super.paint(g);
			if (img != null) {
				g.drawImage(img, 0, 0, this);
			}
			try {
				if (!readOnly &&
						gridSheets.getVOListTableModel().getRowCount()>0 &&
						gridSheets.getSelectedRow()!=-1 &&
						gridSheets.getMode()==Consts.READONLY) {
					Polygon pol = (Polygon)pols.get(gridSheets.getSelectedRow());
					if (pol!=null) {
						g.setColor(Color.orange);
						g.drawPolygon(pol);
					}
				}
				if (!readOnly && points.size()>1 && gridSheets.getMode()==Consts.READONLY) {
					g.setColor(Color.red);
					String[] polStr = null;
					int x1,x2,y1,y2;
					polStr = points.get(0).toString().split(",");
					x1 = Integer.parseInt(polStr[0]);
					y1 = Integer.parseInt(polStr[1]);
					for (int i = 1; i < points.size(); i++) {
						polStr = points.get(i).toString().split(",");
						x2 = Integer.parseInt(polStr[0]);
						y2 = Integer.parseInt(polStr[1]);
						g.drawLine(x1,y1,x2,y2);
						x1 = x2;
						y1 = y2;
					}
				}
			} catch (Throwable t) {
				//t.printStackTrace();
			}
		}

	}

}
