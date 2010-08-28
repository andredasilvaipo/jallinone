package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.*;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.border.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sqltool.java.TableVO;
import org.jallinone.system.customizations.java.CustomFunctionVO;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import java.util.StringTokenizer;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.client.GridController;
import org.jallinone.system.customizations.java.CustomColumnVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller for custom columns grid.</p>
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
public class CustomColumnsController extends GridController {

  /** custom columns grid */
  private GridControl grid = null;

  public CustomColumnsController(GridControl grid) {
    this.grid = grid;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateCustomColumns",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    CustomColumnVO colVO = (CustomColumnVO)grid.getVOListTableModel().getObjectForRow(row);

    if (attributeName.equals("columnTypeSYS22") &&
        colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_DATE))
      // column type combo is not editable if the column is a date...
      return false;

    if (colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_WHERE))
      // all row is not editable if the column is a where param...
      return false;

    if (attributeName.equals("defaultValueTextSYS22") &&
        !colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_TEXT))
      // default value text is not editable if column type is NOT text...
      return false;

    if (attributeName.equals("defaultValueNumSYS22") &&
        !colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_NUM))
      // default value num is not editable if column type is NOT num...
      return false;

    if (attributeName.equals("defaultValueDateSYS22") &&
        !colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_DATE))
      // default value date is not editable if column type is NOT date...
      return false;

    if (attributeName.equals("constraintValuesSYS22") &&
        !colVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_ENUM))
      // enumeration cell is not editable if column type is NOT enum...
      return false;

    return super.isCellEditable(grid,row,attributeName);
  }

  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
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
    CustomColumnVO colVO = (CustomColumnVO)grid.getVOListTableModel().getObjectForRow(rowNumber);

    if (attributeName.equals("columnNameSYS22") &&
        newValue!=null &&
        newValue.toString().length()>0 &&
        !Character.isLetter(newValue.toString().charAt(0)))
      return false;

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_TEXT) &&
        !newValue.equals(ApplicationConsts.TYPE_ENUM))
      // column type is NOT valid if it has been changed from text to a value different from enum
      return false;

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_ENUM) &&
        !newValue.equals(ApplicationConsts.TYPE_TEXT))
      // column type is NOT valid if it has been changed from enum to a value different from text
      return false;

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_NUM) &&
        !newValue.equals(ApplicationConsts.TYPE_PROG))
      // column type is NOT valid if it has been changed from num to a value different from prog
      return false;

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_PROG) &&
        !newValue.equals(ApplicationConsts.TYPE_NUM))
      // column type is NOT valid if it has been changed from prog to a value different from num
      return false;

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_NUM) &&
        newValue.equals(ApplicationConsts.TYPE_PROG))
      // column type has been changed from num to prog: default value num must be reset...
      colVO.setDefaultValueNumSYS22(null);

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_TEXT) &&
        newValue.equals(ApplicationConsts.TYPE_ENUM))
      // column type has been changed from text to enum: default value text must be reset...
      colVO.setDefaultValueTextSYS22(null);

    if (attributeName.equals("columnTypeSYS22") &&
        oldValue.equals(ApplicationConsts.TYPE_ENUM) &&
        newValue.equals(ApplicationConsts.TYPE_TEXT))
      // column type has been changed from enum to text: enumeration must be reset...
      colVO.setConstraintValuesSYS22(null);

    if (attributeName.equals("constraintValuesSYS22") && newValue!=null) {
      // enumeration cell has been changed: parse it and check its correctness...
      try {
        StringTokenizer st = new StringTokenizer(newValue.toString(), ",");
        if (st.countTokens()==0)
          return false;
        String token = null;
        while (st.hasMoreTokens()) {
          token = st.nextToken();
          token = token.trim();
          if (token.startsWith("'"))
            return false;
          if (token.endsWith("'"))
            return false;
        }
      }
      catch (Exception ex) {
        return false;
      }
    }

    return true;
  }


}
