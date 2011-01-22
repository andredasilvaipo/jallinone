package org.jallinone.system.translations.client;

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
import java.util.HashMap;
import org.jallinone.variants.java.VariantTypeVO;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.languages.java.LanguageVO;
import org.jallinone.system.translations.java.TopicVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class manages translations.</p>
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
public class TranslationsGridFrame extends InternalFrame {

  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl companyLabel = new LabelControl();
  LabelControl labelTopic= new LabelControl();
  ComboBoxControl controlTopic = new ComboBoxControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();
  JPanel voidPanel = new JPanel();
  JPanel buttonsPanel = null;
  GridControl grid = null;
  LanguageVO[] langs = null;


  public TranslationsGridFrame(String topic,BigDecimal progressiveSYS10) {
    try {
      init(topic);
      jbInit();

      Response res = ClientUtils.getData("loadLanguages",null);
      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        langs = (LanguageVO[])list.toArray(new LanguageVO[list.size()]);
      }
      else
        return;
      int w = 60+150*langs.length;
      if (w>630)
        w = 630;
      int h = 350;
      if (progressiveSYS10!=null)
        h = 100;

      setSize(w,h);
      setMinimumSize(new Dimension(w,h));
      MDIFrame.add(this);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init(String topic) {
    controlCompaniesCombo.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        Object companyCodeSys01 = controlCompaniesCombo.getValue();
        if (companyCodeSys01==null)
          companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();

        setupTopics(companyCodeSys01);
      }

    });


    Domain d = new Domain("DOMAIN_SYS10");
    controlTopic.setDomain(d);
//    if (topic==null)
//      controlTopic.setSelectedIndex(0);
//    else
//      controlTopic.setValue(topic);


    controlTopic.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        createGrid();
      }

    });


  }


  private void setupTopics(Object companyCodeSys01) {
    Domain d = new Domain("DOMAIN_SYS10");
    d.addDomainPair(new TopicVO(langs,"ITM01_ITEMS","PROGRESSIVE_SYS10",true,true),"item descriptions");
    d.addDomainPair(new TopicVO(langs,"ITM01_ITEMS","ADD_PROGRESSIVE_SYS10",true,true),"item additional descriptions");
    d.addDomainPair(new TopicVO(langs,"HIE02_HIERARCHIES","PROGRESSIVE_HIE01",false,true),"hierarchies names");
    d.addDomainPair(new TopicVO(langs,"HIE01_LEVELS","PROGRESSIVE",false,true),"hierarchy levels names");
    d.addDomainPair(new TopicVO(langs,"ITM21_VARIANTS","PROGRESSIVE_SYS10",true,false),"variant names");

    if (companyCodeSys01!=null) {
      Response res = ClientUtils.getData("loadVariantsNames",companyCodeSys01);
      if (!res.isError()) {
         java.util.List variantsNames = ((VOListResponse)res).getRows();
         VariantNameVO vo = null;
         for(int i=0;i<variantsNames.size();i++) {
           vo = (VariantNameVO)variantsNames.get(i);
           if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
             d.addDomainPair(new TopicVO(langs,"ITM06_VARIANT_TYPES_1","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant type")+" "+vo.getDescriptionSYS10());
             d.addDomainPair(new TopicVO(langs,"ITM11_VARIANTS_1","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant")+" "+vo.getDescriptionSYS10());
           }
           else if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
             d.addDomainPair(new TopicVO(langs,"ITM07_VARIANT_TYPES_2","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant type")+" "+vo.getDescriptionSYS10());
             d.addDomainPair(new TopicVO(langs,"ITM12_VARIANTS_2","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant")+" "+vo.getDescriptionSYS10());
           }
           else if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
             d.addDomainPair(new TopicVO(langs,"ITM08_VARIANT_TYPES_3","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant type")+" "+vo.getDescriptionSYS10());
             d.addDomainPair(new TopicVO(langs,"ITM13_VARIANTS_3","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant")+" "+vo.getDescriptionSYS10());
           }
           else if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
             d.addDomainPair(new TopicVO(langs,"ITM09_VARIANT_TYPES_4","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant type")+" "+vo.getDescriptionSYS10());
             d.addDomainPair(new TopicVO(langs,"ITM14_VARIANTS_4","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant")+" "+vo.getDescriptionSYS10());
           }
           else if (!vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
             d.addDomainPair(new TopicVO(langs,"ITM10_VARIANT_TYPES_5","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant type")+" "+vo.getDescriptionSYS10());
             d.addDomainPair(new TopicVO(langs,"ITM15_VARIANTS_5","PROGRESSIVE_SYS10",true,true),ClientSettings.getInstance().getResources().getResource("variant")+" "+vo.getDescriptionSYS10());
           }
         }
      }
    }


    d.addDomainPair(new TopicVO(langs,"REG01_VATS","PROGRESSIVE_SYS10",false,true),"vat descriptions");
    d.addDomainPair(new TopicVO(langs,"REG07_TASKS","PROGRESSIVE_SYS10",true,true),"task descriptions");
    d.addDomainPair(new TopicVO(langs,"REG10_PAY_MODES","PROGRESSIVE_SYS10",false,true),"payment descriptions");
    d.addDomainPair(new TopicVO(langs,"REG11_PAY_TYPES","PROGRESSIVE_SYS10",false,true),"payment type descriptions");
    d.addDomainPair(new TopicVO(langs,"REG19_AGENT_TYPES","PROGRESSIVE_SYS10",false,true),"agent types");
    d.addDomainPair(new TopicVO(langs,"REG20_TRANSPORT_MOTIVES","PROGRESSIVE_SYS10",false,true),"transport motives");
    d.addDomainPair(new TopicVO(langs,"SAL01_PRICELISTS","PROGRESSIVE_SYS10",true,false),"pricelist descriptions");
    d.addDomainPair(new TopicVO(langs,"SAL03_DISCOUNTS","PROGRESSIVE_SYS10",true,false),"discount descriptions");
    d.addDomainPair(new TopicVO(langs,"SAL06_CHARGES","PROGRESSIVE_SYS10",true,true),"charge descriptions");
    d.addDomainPair(new TopicVO(langs,"SAL09_ACTIVITIES","PROGRESSIVE_SYS10",true,false),"activity descriptions");
    d.addDomainPair(new TopicVO(langs,"SYS04_ROLES","PROGRESSIVE_SYS10",false,true),"role descriptions");
    d.addDomainPair(new TopicVO(langs,"SYS06_FUNCTIONS","PROGRESSIVE_SYS10",false,false),"function descriptions");
    d.addDomainPair(new TopicVO(langs,"ACC01_LEDGER","PROGRESSIVE_SYS10",true,true),"ledger descriptions");
    d.addDomainPair(new TopicVO(langs,"ACC02_ACCOUNTS","PROGRESSIVE_SYS10",true,true),"account descriptions");
    d.addDomainPair(new TopicVO(langs,"ACC03_ACCOUNTING_MOTIVES","PROGRESSIVE_SYS10",false,true),"account motives");
    d.addDomainPair(new TopicVO(langs,"ACC04_VAT_REGISTERS","PROGRESSIVE_SYS10",true,true),"vat registers");
    d.addDomainPair(new TopicVO(langs,"DOC20_DOC_PROPERTIES","PROGRESSIVE_SYS10",true,false),"document properties");


    controlTopic.setDomain(d);
    controlTopic.setSelectedIndex(0);
  }



  private void createGrid() {
    if (controlCompaniesCombo.getValue()==null)
      return;

    if (grid!=null) {
      this.getContentPane().remove(grid);
      topPanel.remove(buttonsPanel);
    }

    buttonsPanel = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    ReloadButton reloadButton = new ReloadButton();
    grid = new GridControl();
    grid.setFunctionId("SYS10");

    TopicVO topicVO = (TopicVO)controlTopic.getValue();
    if (topicVO==null) {
      topicVO = new TopicVO(langs,null,null,false,false);
    }

    grid.getOtherGridParams().put(ApplicationConsts.TOPIC,topicVO);
    grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());

    EditButton editButton = new EditButton();
    SaveButton saveButton = new SaveButton();

    LanguageVO vo = null;
    for(int i=0;i<langs.length;i++) {
      vo = (LanguageVO)langs[i];
      TextColumn col = new TextColumn();
      col.setColumnName("attributeNameS"+i);
      col.setHeaderColumnName(vo.getDescriptionSYS09());
      col.setEditableOnEdit(true);
      col.setColumnRequired(true);
      col.setPreferredWidth(150);
      col.setMaxCharacters(255);
      col.setColumnFilterable(true);
      col.setColumnSortable(false);
      grid.getColumnContainer().add(col);
    }

    ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
    ExportButton exportButton = new ExportButton();
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SYS10");
    grid.setAllowColumnsProfile(false);
    grid.setMaxSortedColumns(3);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);

    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);

    grid.setController(new GridController() {

      /**
       * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
       * @param rowNumbers row indexes related to the changed rows
       * @param oldPersistentObjects old value objects, previous the changes
       * @param persistentObjects value objects relatied to the changed rows
       * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
       */
      public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
        return ClientUtils.getData("updateTranslations",new Object[]{controlTopic.getValue(),oldPersistentObjects,persistentObjects});
      }

    });
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadTranslations");

    topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().repaint();
    topPanel.repaint();
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("translations"));

    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout2);
    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");
    labelTopic.setLabel("topic");
    this.getContentPane().add(topPanel, BorderLayout.NORTH);

    controlCompaniesCombo.setLinkLabel(companyLabel);
    controlCompaniesCombo.setFunctionCode("SYS10");

    topPanel.add(filterPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(companyLabel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCompaniesCombo,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelTopic,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlTopic,      new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(voidPanel,   new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


  }



}
