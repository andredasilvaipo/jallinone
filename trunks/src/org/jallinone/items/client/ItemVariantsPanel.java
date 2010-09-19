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
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.items.java.ItemPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used inside the item detail frame to show variants.</p>
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
public class ItemVariantsPanel extends JPanel {


  private DetailItemVO itemVO = null;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  private ArrayList variantPanels = new ArrayList();


  public ItemVariantsPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
  }


  public DetailItemVO getItemVO() {
    return itemVO;
  }

  public void setContent(DetailItemVO itemVO,java.util.List variantsNames) {
    this.itemVO = itemVO;
    ItemPK pk = new ItemPK(itemVO.getCompanyCodeSys01ITM01(),itemVO.getItemCodeITM01());

    variantPanels.clear();
    this.removeAll();
//    this.revalidate();
//    this.repaint();

    VariantNameVO vo = null;
    ItemVariantPanel p = null;
    int posx = 0;
    int posy = 0;
    for(int i=0;i<variantsNames.size();i++) {
      vo = (VariantNameVO)variantsNames.get(i);

      if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY) && useVariant(itemVO,vo)) {
        p = new ItemVariantPanel();
        p.setVariant(vo);
        p.getVarGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM21());
        p.getVarGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);
        p.getVarGrid().getOtherGridParams().put(ApplicationConsts.TABLE_NAME,vo.getTableName());

        variantPanels.add(p);
        this.add(p,  new GridBagConstraints(posx, posy, 1, 1, 1.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        if (variantsNames.size()>2) {
          posx++;
          if (posx == 2) {
            posx = 0;
            posy++;
          }
        }
        else
          posy++;
      }

    }

//    this.revalidate();
//    this.repaint();

    for(int i=0;i<variantPanels.size();i++)
      ((ItemVariantPanel)variantPanels.get(i)).getVarGrid().reloadData();
  }


  private boolean useVariant(DetailItemVO itemVO,VariantNameVO vo) {
    if (vo.getTableName().equals("ITM11_VARIANTS_1"))
      return Boolean.TRUE.equals(itemVO.getUseVariant1ITM01());
    if (vo.getTableName().equals("ITM12_VARIANTS_2"))
      return Boolean.TRUE.equals(itemVO.getUseVariant2ITM01());
    if (vo.getTableName().equals("ITM13_VARIANTS_3"))
      return Boolean.TRUE.equals(itemVO.getUseVariant3ITM01());
    if (vo.getTableName().equals("ITM14_VARIANTS_4"))
      return Boolean.TRUE.equals(itemVO.getUseVariant4ITM01());
    if (vo.getTableName().equals("ITM15_VARIANTS_5"))
      return Boolean.TRUE.equals(itemVO.getUseVariant5ITM01());
    return false;
  }


  public void clearData() {
    variantPanels.clear();
    this.removeAll();
    this.revalidate();
    this.repaint();
  }


  public void setButtonsEnabled(boolean enabled) {
    for(int i=0;i<variantPanels.size();i++)
      ((ItemVariantsPanel)variantPanels.get(i)).setButtonsEnabled(enabled);
  }


}
