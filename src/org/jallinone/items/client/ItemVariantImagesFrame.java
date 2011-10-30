package org.jallinone.items.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.items.java.DetailItemVO;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemVariantVO;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.variants.java.VariantNameVO;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.Form;
import java.util.HashSet;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used to manage images related to combinations of variants.</p>
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
public class ItemVariantImagesFrame extends InternalFrame {

  Form form = new Form();
  JPanel buttonsPanel = new JPanel();
  JPanel detPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  GridControl grid = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  DeleteButton deleteButton1 = new DeleteButton();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  TextColumn colVarDescr = new TextColumn();
  ImageColumn colImgSmall = new ImageColumn();
  ImageColumn colImgLarge = new ImageColumn();
	private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
	ItemVariantImagesController gridController = new ItemVariantImagesController(this);
	private DetailItemVO vo = null;
	private java.util.List v1 = null;
	private java.util.List v2 = null;
	private java.util.List v3 = null;
	private java.util.List v4 = null;
	private java.util.List v5 = null;
  BorderLayout borderLayout2 = new BorderLayout();
  TextColumn colVar1 = new TextColumn();
  TextColumn colVar2 = new TextColumn();
  TextColumn colVar5 = new TextColumn();
  TextColumn colVar4 = new TextColumn();
  TextColumn colVar3 = new TextColumn();
	LabelControl labelSmall = new LabelControl();
	LabelControl labelLarge = new LabelControl();
	ImageControl controlSmall = new ImageControl();
	ImageControl controlLarge = new ImageControl();
	int pos = 2;
	ItemVariantImageController formController = new ItemVariantImageController(this);
  JSplitPane split = new JSplitPane();
	int varsNr = 0;


	public ItemVariantImagesFrame(ItemFrame frame,DetailItemVO vo) {
		super();
		this.vo = vo;
    try {
			form.setLayout(gridBagLayout2);
			init();

			jbInit();

			setTitle(ClientSettings.getInstance().getResources().getResource("images per variant"));
			grid.setGridDataLocator(gridDataLocator);
			gridDataLocator.setServerMethodName("loadItemVariantImages");
			grid.setController(gridController);
	    form.setFormController(formController);

     	HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01ITM33");
			pkAttrs.add("itemCodeItm01ITM33");
			pkAttrs.add("variantTypeItm06ITM33");
			pkAttrs.add("variantCodeItm11ITM33");
			pkAttrs.add("variantTypeItm07ITM33");
			pkAttrs.add("variantCodeItm12ITM33");
			pkAttrs.add("variantTypeItm08ITM33");
			pkAttrs.add("variantCodeItm13ITM33");
			pkAttrs.add("variantTypeItm09ITM33");
			pkAttrs.add("variantCodeItm14ITM33");
			pkAttrs.add("variantTypeItm10ITM33");
			pkAttrs.add("variantCodeItm15ITM33");
			form.linkGrid(grid,pkAttrs,true);

			setSize(950,550);

	    frame.pushFrame(this);
			this.setParentFrame(frame);
			form.setMode(Consts.READONLY);

			MDIFrame.add(this,true);

			split.setDividerLocation(this.getHeight()/2);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
	}


	private void init() {
		ItemPK pk = new ItemPK();
		pk.setCompanyCodeSys01ITM01(vo.getCompanyCodeSys01());
		pk.setItemCodeITM01(vo.getItemCodeItm01());
	  grid.getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);

		Response res = ClientUtils.getData("loadVariantsNames", vo.getCompanyCodeSys01());
		if (!res.isError()) {
			java.util.List variantsNames = ( (VOListResponse) res).getRows();
			VariantNameVO vvo = null;

			for (int i = 0; i < variantsNames.size(); i++) {
				vvo = (VariantNameVO) variantsNames.get(i);

	      if (i==0 && vo.getUseVariant1ITM01().booleanValue()) {
					v1 = getComboValues("ITM11_VARIANTS_1",pk);
					colVar1.setVisible(true);
					colVar1.setHeaderColumnName(vvo.getDescriptionSYS10());
					pos++;
				  initCombo(pos,vvo.getDescriptionSYS10(),"variantTypeItm06ITM33","variantCodeItm11ITM33",v1);
				}
				if (i==1 && vo.getUseVariant2ITM01().booleanValue()) {
					v2 = getComboValues("ITM12_VARIANTS_2",pk);
					colVar2.setVisible(true);
					colVar2.setHeaderColumnName(vvo.getDescriptionSYS10());
					pos++;
				  initCombo(pos,vvo.getDescriptionSYS10(),"variantTypeItm07ITM33","variantCodeItm12ITM33",v2);
				}
				if (i==2 && vo.getUseVariant3ITM01().booleanValue()) {
					v3 = getComboValues("ITM13_VARIANTS_3",pk);
					colVar3.setVisible(true);
					colVar3.setHeaderColumnName(vvo.getDescriptionSYS10());
					pos++;
					initCombo(pos,vvo.getDescriptionSYS10(),"variantTypeItm08ITM33","variantCodeItm13ITM33",v3);
				}
				if (i==3 && vo.getUseVariant4ITM01().booleanValue()) {
					v4 = getComboValues("ITM14_VARIANTS_4",pk);
					colVar4.setVisible(true);
					colVar4.setHeaderColumnName(vvo.getDescriptionSYS10());
					pos++;
				  initCombo(pos,vvo.getDescriptionSYS10(),"variantTypeItm09ITM33","variantCodeItm14ITM33",v4);
				}
				if (i==4 && vo.getUseVariant5ITM01().booleanValue()) {
					v5 = getComboValues("ITM25_VARIANTS_5",pk);
					colVar5.setVisible(true);
					colVar5.setHeaderColumnName(vvo.getDescriptionSYS10());
					pos++;
					initCombo(pos,vvo.getDescriptionSYS10(),"variantTypeItm10ITM33","variantCodeItm15ITM33",v5);
				}
			} // end for
		}
	}


	/**
	 * Retrieve combo items related to an item variant...
	 */
	private java.util.List getComboValues(String tableName,ItemPK pk) {
		try {
			GridParams gridParams = new GridParams();
			gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);
			gridParams.getOtherGridParams().put(ApplicationConsts.TABLE_NAME,tableName);
			VOListResponse res = (VOListResponse)ClientUtils.getData("loadItemVariants", gridParams);
			return res.getRows();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList();
		}
	}


	/**
	 * Fill in the combo boxes related to an item variant...
	 */
	private void initCombo(int pos,final String varName,String typeVariantAttrName,String codeVariantAttrName,final java.util.List vos) {
		try {
			LabelControl label = new LabelControl();
			final ComboBoxControl comboBoxTypes = new ComboBoxControl();
			final ComboBoxControl comboBoxCodes = new ComboBoxControl();

			ItemVariantVO vo = null;
			Domain d = new Domain("T_"+varName);
			ArrayList types = new ArrayList();
			for(int i=0;i<vos.size();i++) {
				vo = (ItemVariantVO)vos.get(i);
				if (!vo.getVariantType().equals(ApplicationConsts.JOLLY) &&
						!types.contains(vo.getVariantType())) {
					types.add(vo.getVariantType());
					d.addDomainPair(
						vo.getVariantType(),
						vo.getVariantTypeDesc()
					);
				}
			}
			comboBoxTypes.setDomain(d);
			comboBoxTypes.setAttributeName(typeVariantAttrName);

			d = new Domain(varName);
			for(int i=0;i<vos.size();i++) {
				vo = (ItemVariantVO)vos.get(i);

	      if (types.size()==0 ||
						types.get(0).equals(vo.getVariantType()))
					d.addDomainPair(
						vo.getVariantCode(),
						vo.getVariantDesc()
					);
			}
			comboBoxCodes.setDomain(d);
			comboBoxCodes.setAttributeName(codeVariantAttrName);


			label.setText(varName);
			form.add(label,
											new GridBagConstraints(0, pos, 1, 1, 0.0, 0.0
																						 , GridBagConstraints.WEST,
																						 GridBagConstraints.NONE,
																						 new Insets(5, 5, 5, 5), 0, 0));
			int x = 1;
			if (types.size()>0) {
				form.add(comboBoxTypes,
												new GridBagConstraints(x, pos, 1, 1, 1.0, 0.0
																							 , GridBagConstraints.WEST,
																							 GridBagConstraints.NONE,
																							 new Insets(5, 5, 5, 5), 0, 0));
				x++;
				comboBoxTypes.setRequired(true);
			}
			comboBoxCodes.setRequired(true);

			form.add(comboBoxCodes,
											new GridBagConstraints(x, pos, 1, 1, 1.0, 0.0
																						 , GridBagConstraints.WEST,
																						 GridBagConstraints.NONE,
																						 new Insets(5, 5, 5, 5), 0, 0));

      if (types.size()>0) {
				comboBoxTypes.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange()==e.SELECTED) {
							Domain d = new Domain(varName);
							ItemVariantVO vo = null;
							for(int i=0;i<vos.size();i++) {
								vo = (ItemVariantVO)vos.get(i);

								if (comboBoxTypes.getValue().equals(vo.getVariantType()))
									d.addDomainPair(
										vo.getVariantCode(),
										vo.getVariantDesc()
									);
							}
							comboBoxCodes.setDomain(d);

						} // end item selected
					}

				});
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


  private void jbInit() throws Exception {
    form.setDeleteButton(deleteButton1);
    form.setEditButton(editButton1);
    form.setInsertButton(insertButton1);
    form.setReloadButton(reloadButton1);
    form.setSaveButton(saveButton1);
    form.setVOClassName("org.jallinone.items.java.ItemVariantImageVO");
    labelSmall.setLabel("smallImage");
		labelLarge.setLabel("largeImage");
		controlSmall.setAttributeName("smallImage");
		controlLarge.setAttributeName("largeImage");

		colVar1.setColumnSortable(true);
    colVar1.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVar1.setSortingOrder(1);
    colVar2.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVar2.setSortingOrder(2);
    colVar3.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVar3.setSortingOrder(3);
    colVar4.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVar4.setSortingOrder(4);
    colVar5.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVar5.setSortingOrder(5);
    grid.setDeleteButton(null);
    grid.setEditButton(null);
    grid.setInsertButton(null);
    grid.setReloadButton(null);
    grid.setSaveButton(null);
    split.setOrientation(JSplitPane.VERTICAL_SPLIT);

	  int h = pos;
		pos = 0;

    form.add(labelSmall,
										new GridBagConstraints(0, pos, 1, 1, 0.0, 0.0
																					 , GridBagConstraints.WEST,
																					 GridBagConstraints.NONE,
																					 new Insets(5, 5, 5, 5), 0, 0));
		form.add(labelLarge,
										 new GridBagConstraints(2, pos, 1, 1, 0.0, 0.0
																						, GridBagConstraints.WEST,
																						GridBagConstraints.NONE,
																						new Insets(5, 5, 5, 5), 0, 0));
		pos++;
		form.add(controlSmall,
										new GridBagConstraints(0, pos, 2, 1, 1.0, 0.0
																					 , GridBagConstraints.NORTHWEST,
																					 GridBagConstraints.NONE,
																					 new Insets(5, 5, 5, 5), 200, 200));
		form.add(controlLarge,
										 new GridBagConstraints(2, pos, 1, 1+h, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    detPanel.add(buttonsPanel, BorderLayout.NORTH);
		form.setPreferredSize(new Dimension(750,400));
    grid.setRowHeight(200);
//    grid.setRowHeightFixed(false);
    grid.setValueObjectClassName("org.jallinone.items.java.ItemVariantImageVO");
    this.getContentPane().setLayout(borderLayout2);
    detPanel.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    deleteButton1.setText("deleteButton1");
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    colImgLarge.setColumnName("largeImage");
    colImgLarge.setPreferredWidth(500);
    colVarDescr.setColumnName("variantsDescription");
		colVarDescr.setHeaderColumnName("variants");
		colImgSmall.setColumnName("smallImage");
    colImgSmall.setPreferredWidth(150);
		colVarDescr.setPreferredWidth(200);
    colVar1.setColumnName("variantCodeItm11ITM33");
    colVar1.setColumnFilterable(true);
    colVar1.setColumnSelectable(false);
    colVar1.setColumnVisible(false);
    colVar1.setPreferredWidth(80);
    colVar2.setColumnName("variantCodeItm12ITM33");
    colVar2.setColumnFilterable(true);
    colVar2.setColumnSelectable(false);
    colVar2.setColumnVisible(false);
    colVar2.setPreferredWidth(80);
    colVar3.setColumnName("variantCodeItm13ITM33");
    colVar3.setColumnFilterable(true);
    colVar3.setColumnSelectable(false);
    colVar3.setColumnVisible(false);
    colVar3.setPreferredWidth(80);
    colVar4.setColumnName("variantCodeItm14ITM33");
    colVar4.setColumnFilterable(true);
    colVar4.setColumnSelectable(false);
    colVar4.setColumnVisible(false);
    colVar4.setPreferredWidth(80);
    colVar5.setColumnName("variantCodeItm15ITM33");
    colVar5.setColumnFilterable(true);
    colVar5.setColumnSelectable(false);
    colVar5.setColumnVisible(false);
    colVar5.setPreferredWidth(80);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    split.add(grid, JSplitPane.TOP);
    this.getContentPane().add(split, BorderLayout.NORTH);
    split.add(detPanel, JSplitPane.BOTTOM);
    grid.getColumnContainer().add(colVar1, null);
    grid.getColumnContainer().add(colVar2, null);
    grid.getColumnContainer().add(colVar3, null);
    grid.getColumnContainer().add(colVar4, null);
    grid.getColumnContainer().add(colVar5, null);
    grid.getColumnContainer().add(colVarDescr, null);
    grid.getColumnContainer().add(colImgSmall, null);
    grid.getColumnContainer().add(colImgLarge, null);
    detPanel.add(form, BorderLayout.CENTER);
    split.setDividerLocation(300);
  }


  public GridControl getGrid() {
    return grid;
  }


  public DetailItemVO getVo() {
    return vo;
  }
  public java.util.List getV5() {
    return v5;
  }
  public java.util.List getV4() {
    return v4;
  }
  public java.util.List getV3() {
    return v3;
  }
  public java.util.List getV2() {
    return v2;
  }
  public java.util.List getV1() {
    return v1;
  }
  public Form getForm() {
    return form;
  }


}
