package org.jallinone.system.importdata.client;

import java.util.*;

import org.jallinone.variants.java.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.jallinone.system.importdata.java.SelectableFieldVO;
import org.openswing.swing.client.GridControl;
import java.awt.Color;
import org.jallinone.system.importdata.java.ETLProcessVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for ETL process fields grid frame.</p>
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
public class ETLProcessFieldsController extends GridController {

  private ETLProcessFrame frame = null;


  public ETLProcessFieldsController(ETLProcessFrame frame) {
    this.frame = frame;
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value is valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    SelectableFieldVO vo = (SelectableFieldVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
    ETLProcessVO processVO = (ETLProcessVO)frame.getMainPanel().getVOModel().getValueObject();
    if ("TXT".equals(processVO.getFileFormatSYS23()) &&
        attributeName.equals("field.startPosSYS24") &&
        newValue!=null &&
        vo.getField().getEndPosSYS24()!=null &&
        ((Number)newValue).intValue()>vo.getField().getEndPosSYS24().intValue())
      return false;
    if ("TXT".equals(processVO.getFileFormatSYS23()) &&
        attributeName.equals("field.endPosSYS24") &&
        newValue!=null &&
        vo.getField().getStartPosSYS24()!=null &&
        ((Number)newValue).intValue()<vo.getField().getStartPosSYS24().intValue())
      return false;
    if (attributeName.equals("selected") && Boolean.FALSE.equals(newValue)) {
      vo.getField().setStartPosSYS24(null);
      vo.getField().setEndPosSYS24(null);
      vo.getField().setPosSYS24(null);
    }
    return true;
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    SelectableFieldVO vo = (SelectableFieldVO)grid.getVOListTableModel().getObjectForRow(row);
    ETLProcessVO processVO = (ETLProcessVO)frame.getMainPanel().getVOModel().getValueObject();
    if (attributeName.equals("selected"))
      return true;
    if (vo.isSelected()) {
      if (("XLS".equals(processVO.getFileFormatSYS23()) ||
           "CSV1".equals(processVO.getFileFormatSYS23()) ||
           "CSV2".equals(processVO.getFileFormatSYS23())) &&
          attributeName.equals("field.posSYS24"))
        return true;
      if ("TXT".equals(processVO.getFileFormatSYS23()) &&
          (attributeName.equals("field.startPosSYS24") ||
           attributeName.equals("field.endPosSYS24")))
        return true;
      if (("TXT".equals(processVO.getFileFormatSYS23()) ||
           "CSV1".equals(processVO.getFileFormatSYS23()) ||
           "CSV2".equals(processVO.getFileFormatSYS23())) &&
          attributeName.equals("field.dateFormatSYS24"))
        return true;
    }
    return false;
  }


  /**
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributeName,Object value) {
    SelectableFieldVO vo = (SelectableFieldVO)frame.getGrid().getVOListTableModel().getObjectForRow(row);
    ETLProcessVO processVO = (ETLProcessVO)frame.getMainPanel().getVOModel().getValueObject();
    if (vo.isRequired()) {
      Color c = new Color(200,100,100);
      if ("XLS".equals(processVO.getFileFormatSYS23()) && attributeName.equals("field.posSYS24") && value==null)
        return c;
      else if ("CSV1".equals(processVO.getFileFormatSYS23()) && attributeName.equals("field.posSYS24") && value==null)
        return c;
      else if ("CSV2".equals(processVO.getFileFormatSYS23()) && attributeName.equals("field.posSYS24") && value==null)
        return c;
      else if ("TXT".equals(processVO.getFileFormatSYS23()) && attributeName.equals("field.startPosSYS24") && value==null)
        return c;
      else if ("TXT".equals(processVO.getFileFormatSYS23()) && attributeName.equals("field.endPosSYS24") && value==null)
        return c;
    }
    return ClientSettings.GRID_CELL_BACKGROUND;
  }


}
