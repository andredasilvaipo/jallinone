package org.jallinone.items.client;



import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.model.client.VOModel;
import java.awt.event.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.documents.java.GridDocumentVO;
import java.util.HashSet;
import org.jallinone.documents.java.DocumentTypeVO;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemAttachedDocVO;
import org.openswing.swing.table.client.GridController;
import javax.swing.border.*;
import java.util.Map;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.items.java.ItemVariantVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used inside the item detail frame to show a variant.</p>
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
public class ItemVariantPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  SaveButton saveButton5 = new SaveButton();
  EditButton editButton5 = new EditButton();
  ReloadButton reloadButton5 = new ReloadButton();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel varButtonsPanel1 = new JPanel();
  GridControl varGrid = new GridControl();
  TextColumn colVarCode = new TextColumn();
  TextColumn colVarDesc = new TextColumn();
  TextColumn colVarTypeCode = new TextColumn();
  TextColumn colVarTypeDesc = new TextColumn();
  CheckBoxColumn colVarSel = new CheckBoxColumn();
  JPanel varPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TitledBorder titledBorder1;


  public ItemVariantPanel() {
    try {
      jbInit();

      ServerGridDataLocator gridLocator = new ServerGridDataLocator();
      gridLocator.setServerMethodName("loadItemVariants");
      varGrid.setGridDataLocator(gridLocator);
      varGrid.setController(new GridController() {

        /**
         * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
         * @param rowNumbers row indexes related to the changed/new rows
         * @param oldPersistentObjects old value objects, previous the changes; it can contains null objects, in case of new inserted rows
         * @param persistentObjects value objects related to the changed/new rows
         * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
         */
        public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
          return ClientUtils.getData("updateItemVariants",new ArrayList[]{oldPersistentObjects,persistentObjects});
        }


				/**
				 * @param grid grid
				 * @param row selected row index
				 * @param attributeName attribute name that identifies the selected grid column
				 * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
				 */
				public boolean isCellEditable(GridControl grid,int row,String attributeName) {
					String currentSelVarType = null;
					ItemVariantVO vo = null;
					for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
						vo = (ItemVariantVO)grid.getVOListTableModel().getObjectForRow(i);
						if (Boolean.TRUE.equals(vo.getSelected()) && currentSelVarType==null)
							currentSelVarType = vo.getVariantType();
					}
					if (currentSelVarType!=null &&
							!((ItemVariantVO)grid.getVOListTableModel().getObjectForRow(row)).getVariantType().equals(currentSelVarType))
						return false;
					return grid.isFieldEditable(row,attributeName);
				}


      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    varPanel.setLayout(gridBagLayout3);

    this.setLayout(borderLayout1);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    varButtonsPanel1.setLayout(flowLayout5);
    varGrid.setAutoLoadData(false);
//    varGrid.setFunctionId("ITM01");
    varGrid.setEditButton(editButton5);
    varGrid.setReloadButton(reloadButton5);
    varGrid.setSaveButton(saveButton5);
    varGrid.setValueObjectClassName("org.jallinone.items.java.ItemVariantVO");
    varGrid.setVisibleStatusPanel(false);

    colVarTypeCode.setColumnName("variantType");
    colVarTypeCode.setPreferredWidth(70);
    colVarTypeCode.setEditableOnEdit(false);
    colVarTypeCode.setColumnFilterable(true);
    colVarTypeCode.setColumnSelectable(false);

    colVarTypeDesc.setColumnName("variantTypeDesc");
    colVarTypeDesc.setPreferredWidth(150);
    colVarTypeDesc.setEditableOnEdit(false);
    colVarTypeDesc.setColumnFilterable(false);
    colVarTypeDesc.setColumnSelectable(false);

    colVarCode.setColumnName("variantCode");
    colVarCode.setPreferredWidth(70);
    colVarCode.setEditableOnEdit(false);
    colVarCode.setColumnFilterable(false);
    colVarCode.setColumnSelectable(false);

    colVarDesc.setColumnName("variantDesc");
    colVarDesc.setPreferredWidth(150);
    colVarDesc.setEditableOnEdit(false);
    colVarDesc.setColumnFilterable(false);
    colVarDesc.setColumnSelectable(false);

    colVarSel.setColumnName("selected");
    colVarSel.setPreferredWidth(60);
    colVarSel.setEditableOnEdit(true);
    colVarSel.setColumnFilterable(false);
    colVarSel.setColumnSelectable(false);
    colVarSel.setShowDeSelectAllInPopupMenu(true);

    this.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    varPanel.add(varButtonsPanel1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    varButtonsPanel1.add(editButton5, null);
    varButtonsPanel1.add(saveButton5, null);
    varButtonsPanel1.add(reloadButton5, null);
    varPanel.add(varGrid,      new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(varPanel, BorderLayout.CENTER);

    varGrid.getColumnContainer().add(colVarTypeCode, null);
    varGrid.getColumnContainer().add(colVarTypeDesc, null);
    varGrid.getColumnContainer().add(colVarCode, null);
    varGrid.getColumnContainer().add(colVarDesc, null);
    varGrid.getColumnContainer().add(colVarSel, null);
  }


  public GridControl getVarGrid() {
    return varGrid;
  }


  public void setVariant(VariantNameVO vo) {
    titledBorder1.setTitle(vo.getDescriptionSYS10());
    if (!Boolean.TRUE.equals(vo.getUseVariantTypeITM21())) {
      colVarTypeCode.setColumnVisible(false);
      colVarTypeDesc.setColumnVisible(false);
    }
  }


  public void setButtonsEnabled(boolean enabled) {
    editButton5.setEnabled(enabled);
    reloadButton5.setEnabled(enabled);
  }


}
