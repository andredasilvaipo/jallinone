package org.jallinone.sqltool.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import java.sql.Types;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import org.jallinone.sqltool.java.TableVO;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.sqltool.java.ColumnVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.domains.java.Domain;
import java.util.Iterator;
import org.jallinone.sqltool.java.ForeignKeyVO;
import java.util.Hashtable;
import java.util.HashSet;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This JPanel contains a grid controls related to the specified SQL.</p>
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
public class TablePanel extends JPanel {

  JPanel buttonsPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  CopyButton copyButton = new CopyButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ExportButton exportButton = new ExportButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** table info */
  private TableVO tableVO = null;


  public TablePanel(String sql,ArrayList mainTables,boolean convertColumnHeaders,boolean disableGrid,boolean autoLoadData) {
    try {
      grid.setAutoLoadData(autoLoadData);
      tableVO = new TableVO(sql,mainTables,convertColumnHeaders);
      Response res = ClientUtils.getData("getQueryInfo",tableVO);
      if (res.isError())
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
        );
      else {
        tableVO = (TableVO)((VOResponse)res).getVo();
        init(disableGrid);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        ClientSettings.getInstance().getResources().getResource(e.getMessage()),
        ClientSettings.getInstance().getResources().getResource("Error"),
        JOptionPane.ERROR_MESSAGE
      );
    }

  }


  public TablePanel(TableVO tableVO,boolean disableGrid,boolean autoLoadData) {
    this.tableVO = tableVO;
    grid.setAutoLoadData(autoLoadData);
    init(disableGrid);
  }


  /**
   * Define grid content.
   * @param disableGrid define if grid is readonly or could be modified
   */
  private void init(boolean disableGrid) {
    try {
      // retrieve columns that must be defined as lookups...
      Iterator it = tableVO.getForeingKeys().values().iterator();
      ForeignKeyVO fk = null;
      String colName = null;
      Hashtable fks = new Hashtable(); // collection of pairs < fktable.fkcolumn , ForeignKeyVO >
      HashSet colsToDisable = new HashSet(); // collection of fktable.fkcolumn that must be disabled in grid
      while(it.hasNext()) {
        fk = (ForeignKeyVO)it.next();
        colName = fk.getFkTableName()+"."+fk.getFkFieldNames().get(fk.getFkFieldNames().size()-1); // fktable.fkcolumn
        fks.put(colName,fk);
        for(int i=0;i<fk.getFkFieldNames().size()-1;i++)
          colsToDisable.add(fk.getFkTableName()+"."+fk.getFkFieldNames().get(i)); // fktable.fkcolumn
      }

      // create table content...
      jbInit();

      ArrayList cols = tableVO.getColumns();
      grid.setMaxSortedColumns(cols.size());
      ColumnVO vo = null;
      int t;
      Column col = null;
      for(int i=0;i<cols.size();i++) {
        vo = (ColumnVO)cols.get(i);
        t = vo.getColumnSqlType();

        if (vo.getColumnValues()!=null && vo.getColumnValues().size()>0) {
          // column is a combo box...
          col = new ComboColumn();
          Domain domain = new Domain(vo.getColumnName());
          for(int j=0;j<vo.getColumnValues().size();j++)
            domain.addDomainPair(vo.getColumnValues().get(j),vo.getColumnValues().get(j).toString());
          ((ComboColumn)col).setDomain(domain);
        }
        else if (fks.containsKey(vo.getColumnName()) &&
                 t!=Types.DATE && t!=Types.TIMESTAMP && t!=Types.TIME) {
          // lookup...
          col = new CodLookupColumn();
          ((CodLookupColumn)col).setMaxCharacters(vo.getColumnSize().intValue());
          ((CodLookupColumn)col).setAllowOnlyNumbers(t!=Types.VARCHAR && t!=Types.CHAR);
          if (t==Types.CHAR)
            ((CodLookupColumn)col).setCodePadding(true);
          fk = (ForeignKeyVO)fks.get(vo.getColumnName());
          ((CodLookupColumn)col).setEnableCodBox(fk.getFkFieldNames().size()==1);

          // retrieve primary key table infos...
          String sql = "select * from "+fk.getPkTableName();
          TableVO auxVO = new TableVO(sql,new ArrayList(),false);
          Response res = ClientUtils.getData("getQueryInfo",auxVO);
          if (res.isError())
            JOptionPane.showMessageDialog(
              MDIFrame.getInstance(),
              ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.ERROR_MESSAGE
            );
          else {
            auxVO = (TableVO)((VOResponse)res).getVo();
            setupLookup(auxVO,(CodLookupColumn)col,fk);
          }
        }
        else if (t==Types.DATE || t==Types.TIMESTAMP || t==Types.TIME) {
          col = new DateColumn();
        }
        else if (t==Types.BIGINT || t==Types.INTEGER || t==Types.NUMERIC || t==Types.SMALLINT && vo.getColumnDec().equals(new BigDecimal(0))) {
          col = new IntegerColumn();
          ((IntegerColumn)col).setMaxValue((int)Math.pow(10d,vo.getColumnSize().doubleValue()));
        }
        else if (t==Types.BIGINT || t==Types.DECIMAL || t==Types.DOUBLE || t==Types.FLOAT || t==Types.INTEGER || t==Types.NUMERIC || t==Types.REAL || t==Types.SMALLINT) {
          col = new DecimalColumn();
          ((DecimalColumn)col).setMaxValue(Math.pow(10d,vo.getColumnSize().doubleValue()));
          ((DecimalColumn)col).setDecimals(vo.getColumnDec().intValue());
        }
        else {
          col = new TextColumn();
          ((TextColumn)col).setMaxCharacters(vo.getColumnSize().intValue());
          if (t==Types.CHAR)
            ((TextColumn)col).setRpadding(true);
        }

        col.setColumnName(vo.getAttributeName());
        col.setHeaderColumnName(vo.getColumnHeaderName());
        col.setColumnRequired(vo.getColumnRequired() && vo.getColumnVisible());
        col.setColumnFilterable(true);
        col.setColumnDuplicable(!vo.getProgressive());
        col.setColumnSortable(true);
        col.setColumnVisible(vo.getColumnVisible());
        col.setEditableOnInsert(!colsToDisable.contains(vo.getColumnName()) && !vo.getProgressive());
        col.setEditableOnEdit(!colsToDisable.contains(vo.getColumnName()) && !vo.getProgressive() && !vo.getPrimaryKey());
        col.setPreferredWidth(Math.max(vo.getColumnSize().intValue(),col.getHeaderColumnName().length()*9));

        grid.getColumnContainer().add(col);

        // set grid controller/data locator...
        grid.setController(new TablesController(tableVO,grid));
        grid.setGridDataLocator(gridDataLocator);
        gridDataLocator.setServerMethodName("executeQuery");
        grid.getOtherGridParams().put(ApplicationConsts.QUERY_INFO,tableVO);
      }


      if (disableGrid) {
        buttonsPanel.add(reloadButton, null);
        buttonsPanel.add(exportButton, null);
        buttonsPanel.add(navigatorBar, null);
      }
      else {
        buttonsPanel.add(insertButton, null);
        buttonsPanel.add(copyButton, null);
        buttonsPanel.add(editButton, null);
        buttonsPanel.add(saveButton, null);
        buttonsPanel.add(reloadButton, null);
        buttonsPanel.add(deleteButton, null);
        buttonsPanel.add(exportButton, null);
        buttonsPanel.add(navigatorBar, null);
      }

    }
    catch(Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        ClientSettings.getInstance().getResources().getResource(e.getMessage()),
        ClientSettings.getInstance().getResources().getResource("Error"),
        JOptionPane.ERROR_MESSAGE
      );
    }
  }


  /**
   * @return table info
   */
  public final TableVO getTableVO() {
    return tableVO;
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.sqltool.java.RowVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SQL");
    grid.setInsertButton(insertButton);
    grid.setCopyButton(copyButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    this.setLayout(borderLayout1);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
  }


  public GridControl getGrid() {
    return grid;
  }


  /**
   * Setup a lookup controller.
   */
  private void setupLookup(final TableVO auxVO,CodLookupColumn col,final ForeignKeyVO fk) {
    LookupController lookupController = new LookupController();
    final LookupServerDataLocator lookupDataLocator = new LookupServerDataLocator();
    lookupDataLocator.setGridMethodName("executeQuery");
    lookupDataLocator.setValidationMethodName("executeValidateQuery");

    col.setLookupController(lookupController);
    lookupController.setAllColumnVisible(false);
    lookupController.setLookupDataLocator(lookupDataLocator);
    lookupController.setFrameTitle(fk.getPkTableName());
    lookupController.setLookupValueObjectClassName("org.jallinone.sqltool.java.RowVO");
    ColumnVO vo = null;
    int colWidth = 0;
    int width = 0;
    for(int j=0;j<auxVO.getColumns().size();j++) {
      vo = (ColumnVO)auxVO.getColumns().get(j);
      lookupController.setVisibleColumn(vo.getAttributeName(),true);
      lookupController.setHeaderColumnName(vo.getAttributeName(),vo.getColumnHeaderName());
      lookupController.setSortableColumn(vo.getAttributeName(),true);
      lookupController.setFilterableColumn(vo.getAttributeName(),true);
      colWidth = Math.max(vo.getColumnSize().intValue(),vo.getColumnHeaderName().length()*9);
      lookupController.setPreferredWidthColumn(vo.getAttributeName(),colWidth);
      width += colWidth;
      for(int i=0;i<fk.getFkFieldNames().size();i++)
        if (vo.getColumnName().equals(fk.getPkTableName()+"."+fk.getPkFieldNames().get(i)))
          lookupController.addLookup2ParentLink(
            vo.getAttributeName(),
            tableVO.getAttributeName(fk.getFkTableName()+"."+fk.getFkFieldNames().get(i))
          );

    }

    lookupController.setFramePreferedSize(new Dimension(Math.min(750,width+30),500));
    lookupController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

      public void beforeLookupAction(ValueObject parentVO) {
        lookupDataLocator.getLookupFrameParams().put(ApplicationConsts.QUERY_INFO,auxVO);
        lookupDataLocator.getLookupValidationParameters().put(ApplicationConsts.QUERY_INFO,auxVO);
        lookupDataLocator.getLookupValidationParameters().put(ApplicationConsts.CODE,fk.getPkFieldNames().get(fk.getPkFieldNames().size()-1));
      }

      public void forceValidate() {}

    });

  }

}
