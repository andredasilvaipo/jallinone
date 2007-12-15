package org.jallinone.sqltool.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.carrier.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.sqltool.java.TableVO;
import org.jallinone.sqltool.java.ColumnVO;
import org.openswing.swing.client.GridControl;
import org.jallinone.sqltool.java.InsUpdDelTablesVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for query grid frame.</p>
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
public class TablesController extends GridController {

  /** tables info */
  private TableVO tableVO = null;

  /** grid control */
  private GridControl grid = null;


  public TablesController(TableVO tableVO,GridControl grid) {
    this.tableVO = tableVO;
    this.grid = grid;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    ColumnVO vo = null;
    for(int i=0;i<tableVO.getColumns().size();i++) {
      vo = (ColumnVO)tableVO.getColumns().get(i);
      grid.getVOListTableModel().setValueAt(vo.getDefaultValue(),grid.getSelectedRow()==-1?0:grid.getSelectedRow(),i);
    }
  }


/**
 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
 * @param rowNumbers row indexes related to the new rows to save
 * @param newValueObjects list of new value objects to save
 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
 */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    InsUpdDelTablesVO vo = new InsUpdDelTablesVO(tableVO);
    vo.addRowsToInsert(newValueObjects);
    return ClientUtils.getData("insertTables",vo);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    InsUpdDelTablesVO vo = new InsUpdDelTablesVO(tableVO);
    vo.addRowsToUpdate(oldPersistentObjects,persistentObjects);
    return ClientUtils.getData("updateTables",vo);
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    InsUpdDelTablesVO vo = new InsUpdDelTablesVO(tableVO);
    vo.addRowsToDelete(persistentObjects);
    return ClientUtils.getData("deleteTables",vo);
  }




}
