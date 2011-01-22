package org.jallinone.commons.client;

import org.openswing.swing.table.client.GridController;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.table.client.Grid;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import java.util.ArrayList;
import org.openswing.swing.table.model.client.VOListAdapter;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.table.model.client.VOListTableModel;
import java.lang.reflect.Method;
import java.lang.reflect.*;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used with editable grids that support more companies.</p>
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
public class CompanyGridController extends GridController {


  public CompanyGridController() {
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    ClientApplet applet = ((ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
    ArrayList companyCodes = applet.getAuthorizations().getCompanyBa().getCompaniesList(grid.getFunctionId());
    for(int i=0;i<companyCodes.size();i++)
      if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
        grid.getFunctionId(),
        companyCodes.get(i).toString()
      ))
        return true;

    JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(grid),
        ClientSettings.getInstance().getResources().getResource("You are not allowed to insert data."),
        ClientSettings.getInstance().getResources().getResource("Attention"),
        JOptionPane.WARNING_MESSAGE
    );
    return false;
  }


  /**
   * Callback method invoked on pressing COPY button.
   * @return <code>true</code> allows to go to INSERT mode (by copying data), <code>false</code> the mode change is interrupted
   */
  public boolean beforeCopyGrid(GridControl grid) {
    return beforeInsertGrid(grid);
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    if (grid.getMode()==Consts.EDIT) {
      if (attributeName.startsWith("companyCode"))
        return false;

      if (!grid.isFieldEditable(row,attributeName))
        return false;

      ClientApplet applet = ((ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
      Method[] m = grid.getVOListTableModel().getValueObjectType().getMethods();
      for(int i=0;i<m.length;i++)
        if (m[i].getName().startsWith("getCompanyCode")) {
          try {
            String companyCode = m[i].invoke(grid.getVOListTableModel().getObjectForRow(row),new Object[0]).toString();
            return applet.getAuthorizations().getCompanyBa().isEditEnabled(grid.getFunctionId(),companyCode);
          }
          catch (Throwable ex) {
            return false;
          }
        }
      return false;
    }
    else
      return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    ClientApplet applet = ((ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();

    Method[] m = grid.getVOListTableModel().getValueObjectType().getMethods();
    for(int i=0;i<m.length;i++)
      if (m[i].getName().startsWith("getCompanyCode")) {
        for(int j=0;j<grid.getSelectedRows().length;j++)
          try {
            if (!applet.getAuthorizations().getCompanyBa().isDeleteEnabled(
                grid.getFunctionId(), // function code
                m[i].invoke(grid.getVOListTableModel().getObjectForRow(grid.getSelectedRows()[j]),new Object[0]).toString() // companyCode
            )) {
              JOptionPane.showMessageDialog(
                  ClientUtils.getParentFrame(grid),
                  ClientSettings.getInstance().getResources().getResource("You are not allowed to delete the selected row."),
                  ClientSettings.getInstance().getResources().getResource("Attention"),
                  JOptionPane.WARNING_MESSAGE
              );
              return false;
            }
          }
          catch (Throwable ex) {
            return false;
          }
        return true;
      }
    return false;
  }



}