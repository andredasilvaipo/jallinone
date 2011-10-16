package org.jallinone.sqltool.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import org.jallinone.sqltool.java.TableVO;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.GridDataLocator;
import java.util.Map;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: JPanel that contains SQL entities list.</p>
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
public class EntitiesListPanel extends JPanel implements ListSelectionListener,GridDataLocator {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  JSplitPane splitPane = new JSplitPane();
  JTabbedPane entitiesTabbedPane = new JTabbedPane() {

    public void setSelectedIndex(int index) {
      super.setSelectedIndex(index);
      entitiesTabbedPane_componentShown();
    }

  };
  JTabbedPane detailTabbedPane = new JTabbedPane();
  JScrollPane tablesScrollPane = new JScrollPane();
  JScrollPane viewsScrollPane = new JScrollPane();
  JScrollPane sinScrollPane = new JScrollPane();
  JList tablesList = new JList();
  JList viewsList = new JList();
  JList sinList = new JList();
  JPanel structurePanel = new JPanel();
  JPanel dataPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();

  private boolean tablesAlreadyLoaded = false;
  private boolean viewsAlreadyLoaded = false;
  private boolean sinsAlreadyLoaded = false;
  GridControl grid = new GridControl();
  TextColumn colName = new TextColumn();
  TextColumn colType = new TextColumn();
  CheckBoxColumn colPk = new CheckBoxColumn();
  CheckBoxColumn colObl = new CheckBoxColumn();

  private TablePanel tablePanel = null;


  public EntitiesListPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    tablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    viewsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sinList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    structurePanel.setLayout(borderLayout2);
    dataPanel.setLayout(borderLayout3);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    reloadButton.addActionListener(new EntitiesListPanel_reloadButton_actionAdapter(this));
    grid.setAutoLoadData(false);
    grid.setFunctionId("SQL");
    grid.setMaxSortedColumns(0);
    grid.setValueObjectClassName("org.jallinone.sqltool.java.ColumnVO");
    colName.setColumnName("columnNameWithoutTable");
    colName.setPreferredWidth(200);
    colType.setColumnName("columnType");
    colType.setPreferredWidth(150);
    colPk.setColumnName("primaryKey");
    colPk.setPreferredWidth(50);
    colObl.setColumnName("columnRequired");
    colObl.setPreferredWidth(50);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(splitPane,  BorderLayout.CENTER);
    splitPane.add(entitiesTabbedPane, JSplitPane.LEFT);
    splitPane.add(detailTabbedPane, JSplitPane.RIGHT);
    entitiesTabbedPane.add(tablesScrollPane,  "tables");
    entitiesTabbedPane.add(viewsScrollPane,   "views");
    entitiesTabbedPane.add(sinScrollPane,     "synonyms");
    tablesScrollPane.getViewport().add(tablesList, null);
    viewsScrollPane.getViewport().add(viewsList, null);
    sinScrollPane.getViewport().add(sinList, null);
    detailTabbedPane.add(structurePanel,  "structure");
    structurePanel.add(grid, BorderLayout.CENTER);
    detailTabbedPane.add(dataPanel,   "data");
    buttonsPanel.add(reloadButton, null);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colType, null);
    grid.getColumnContainer().add(colPk, null);
    grid.getColumnContainer().add(colObl, null);
    splitPane.setDividerLocation(200);

    entitiesTabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("tables"));
    entitiesTabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("views"));
    entitiesTabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("synonyms"));

    detailTabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("structure"));
    detailTabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("data"));

    tablesList.addListSelectionListener(this);
    viewsList.addListSelectionListener(this);
    sinList.addListSelectionListener(this);

    grid.setController(new GridController());
    grid.setGridDataLocator(this);

  }


  void reloadButton_actionPerformed(ActionEvent e) {
    if (entitiesTabbedPane.getSelectedIndex()==0) {
      new LoadEntities("TABLE",tablesList);
    }
    else if (entitiesTabbedPane.getSelectedIndex()==1) {
      new LoadEntities("VIEW",viewsList);
    }
    else if (entitiesTabbedPane.getSelectedIndex()==2) {
      new LoadEntities("SYNONYM",sinList);
    }
  }


  private void entitiesTabbedPane_componentShown() {
    if (entitiesTabbedPane.getSelectedIndex()==0 && !tablesAlreadyLoaded) {
      tablesAlreadyLoaded = true;
      new LoadEntities("TABLE",tablesList);
    }
    else if (entitiesTabbedPane.getSelectedIndex()==1 && !viewsAlreadyLoaded) {
      viewsAlreadyLoaded = true;
      new LoadEntities("VIEW",viewsList);
    }
    else if (entitiesTabbedPane.getSelectedIndex()==2 && !sinsAlreadyLoaded) {
      sinsAlreadyLoaded = true;
      new LoadEntities("SYNONYM",sinList);
    }
  }


  /**
   * <p>Description: Inner class used to load entities in another thread.</p>
   * @version 1.0
   */
  class LoadEntities extends Thread {

    private String entityType = null;
    private JList entitiesList = null;


    public LoadEntities(String entityType,JList entitiesList) {
      this.entityType = entityType;
      this.entitiesList = entitiesList;
      start();
    }


    public void run() {
      try {
        Response res = ClientUtils.getData("loadEntities", entityType);
        if (res.isError())
          JOptionPane.showMessageDialog(
            MDIFrame.getInstance(),
            ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
          );
        else {
          // list loaded...
          DefaultListModel model = new DefaultListModel();
          java.util.List list = ((VOListResponse)res).getRows();
          for(int i=0;i<list.size();i++)
            model.addElement(list.get(i));
          entitiesList.setModel(model);
          entitiesList.revalidate();
          entitiesList.repaint();
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ex.getMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
        );
      }
    }


  }


  /**
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
  public void valueChanged(ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      if (entitiesTabbedPane.getSelectedIndex()==0 && tablesList.getSelectedValue()!=null) {
        new LoadEntity(tablesList.getSelectedValue().toString());
      }
      else if (entitiesTabbedPane.getSelectedIndex()==1 && viewsList.getSelectedValue()!=null) {
        new LoadEntity(viewsList.getSelectedValue().toString());
      }
      else if (entitiesTabbedPane.getSelectedIndex()==2 && sinList.getSelectedValue()!=null) {
        new LoadEntity(sinList.getSelectedValue().toString());
      }
    }
  }


  /**
   * <p>Description: Inner class used to load entity structure and data in another thread.</p>
   * @version 1.0
   */
  class LoadEntity extends Thread {

    private String entity = null;


    public LoadEntity(String entity) {
      this.entity = entity;
      start();
    }


    public void run() {
      try {
        // set data content...
        dataPanel.removeAll();
        ArrayList mainTables = new ArrayList();
        mainTables.add(entity);
        tablePanel = new TablePanel("select * from "+entity,mainTables,false,false,true);
        dataPanel.add(tablePanel,BorderLayout.CENTER);
        dataPanel.revalidate();
        dataPanel.repaint();

        // set structure content...
        if (tablePanel.getTableVO()!=null)
          grid.reloadData();
      }
      catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ex.getMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
        );
      }
    }


  }


  /**
   * Method invoked by the grid to load a block or rows.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType v.o. type
   * @param otherGridParams other grid parameters
   * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
   */
  public Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType,
      Map otherGridParams) {
    TableVO tableVO = tablePanel.getTableVO();
    ArrayList rows = tableVO.getColumns();
    return new VOListResponse(rows,false,rows.size());
  }




}

class EntitiesListPanel_reloadButton_actionAdapter implements java.awt.event.ActionListener {
  EntitiesListPanel adaptee;

  EntitiesListPanel_reloadButton_actionAdapter(EntitiesListPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.reloadButton_actionPerformed(e);
  }
}


