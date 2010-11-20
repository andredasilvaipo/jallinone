package org.jallinone.sqltool.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to execute insert/update/delete operations, based on TableVO infos.</p>
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
public class InsUpdDelTablesVO extends ValueObjectImpl {

  /** table info */
  private TableVO tableVO = null;

  /** list of RowVO objects to insert */
  private ArrayList insertRows = new ArrayList();

  /** list of RowVO objects updated (old values) */
  private ArrayList oldUpdatedRows = new ArrayList();

  /** list of RowVO objects updated (new values) */
  private ArrayList newUpdatedRows = new ArrayList();

  /** list of RowVO objects to delete */
  private ArrayList rowsToDelete = new ArrayList();

  
  public InsUpdDelTablesVO() {}
  

  public InsUpdDelTablesVO(TableVO tableVO) {
    this.tableVO = tableVO;
  }


  public void addRowsToInsert(ArrayList insertRows) {
    this.insertRows = insertRows;
  }


  public void addRowsToUpdate(ArrayList oldUpdatedRows,ArrayList newUpdatedRows) {
    this.oldUpdatedRows = oldUpdatedRows;
    this.newUpdatedRows = newUpdatedRows;
  }


  public void addRowsToDelete(ArrayList rowsToDelete) {
    this.rowsToDelete = rowsToDelete;
  }


  public TableVO getTableVO() {
    return tableVO;
  }
  public ArrayList getOldUpdatedRows() {
    return oldUpdatedRows;
  }

  public ArrayList getNewUpdatedRows() {
    return newUpdatedRows;
  }
  public ArrayList getInsertRows() {
    return insertRows;
  }
  public ArrayList getRowsToDelete() {
    return rowsToDelete;
  }


  public void setTableVO(TableVO tableVO) {
	  this.tableVO = tableVO;
  }


  public void setInsertRows(ArrayList insertRows) {
	  this.insertRows = insertRows;
  }


  public void setOldUpdatedRows(ArrayList oldUpdatedRows) {
	  this.oldUpdatedRows = oldUpdatedRows;
  }


  public void setNewUpdatedRows(ArrayList newUpdatedRows) {
	  this.newUpdatedRows = newUpdatedRows;
  }


  public void setRowsToDelete(ArrayList rowsToDelete) {
	  this.rowsToDelete = rowsToDelete;
  }


}
