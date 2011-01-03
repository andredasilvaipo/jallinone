package org.jallinone.production.billsofmaterial.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller for alternative components grid panel.
 * Alternative components definition is allowed only for items that are not products.</p>
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
public class AltComponentsController extends CompanyGridController {

  /** product panel */
  private ProductPanel panel = null;


  public AltComponentsController(ProductPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    AltComponentVO vo = (AltComponentVO)valueObject;
    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();
    vo.setCompanyCodeSys01ITM04(itemVO.getCompanyCodeSys01ITM01());
  }


  /**
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    // reload grid because the current item could be included in an existing group that includes many other components...
    grid.reloadData();
  }


  /**
   * Callback method invoked on pressing INSERT button.
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (!super.beforeInsertGrid(grid))
      return false;

    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();
//    if (itemVO.getManufactureCodePro01ITM01()!=null)
//      return false;
    return true;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    // check if there exist alternative components previously defined...
    AltComponentVO vo = null;
    if (panel.getAltCompsGrid().getVOListTableModel().getRowCount()>newValueObjects.size()) {
      AltComponentVO prevVO = (AltComponentVO)panel.getAltCompsGrid().getVOListTableModel().getObjectForRow(newValueObjects.size());
      for(int i=0;i<newValueObjects.size();i++) {
        vo = (AltComponentVO)newValueObjects.get(i);
        vo.setProgressiveITM04(prevVO.getProgressiveITM04());
      }
    }
    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (AltComponentVO)newValueObjects.get(i);
      vo.setCurrentItemCodeItm01ITM04(itemVO.getItemCodeITM01());
    }

    return ClientUtils.getData("insertAltComponents",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteAltComponents",persistentObjects);
  }



}
