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
import org.jallinone.production.billsofmaterial.java.ComponentVO;
import org.jallinone.items.java.DetailItemVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller for components grid panel.</p>
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
public class ComponentsController extends CompanyGridController {

  /** product panel */
  private ProductPanel panel = null;


  public ComponentsController(ProductPanel panel) {
    this.panel = panel;
  }


  /**
   * Validate min/max values/percentages and dates.
   */
  private Response validateComponent(ComponentVO vo) {
    return new VOResponse(new Boolean(true));
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    ComponentVO vo = (ComponentVO)valueObject;
    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();
    vo.setCompanyCodeSys01ITM03(itemVO.getCompanyCodeSys01ITM01());
    vo.setRevisionITM03(new BigDecimal(1));
    vo.setVersionITM03(new BigDecimal(1));
    vo.setSequenceITM03(new BigDecimal(panel.getComponentsGrid().getVOListTableModel().getRowCount()));
    vo.setStartDateITM03(new java.sql.Date(System.currentTimeMillis()));
    vo.setQtyITM03(new BigDecimal(1));
  }



  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ComponentVO vo = null;
    Response response = null;
    DetailItemVO itemVO = (DetailItemVO)panel.getFrame().getFormPanel().getVOModel().getValueObject();

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (ComponentVO)newValueObjects.get(i);
      response = validateComponent(vo);
      if (response.isError())
        return response;

      vo.setCompanyCodeSys01ITM03(itemVO.getCompanyCodeSys01ITM01());
      vo.setParentItemCodeItm01ITM03(itemVO.getItemCodeITM01());
    }

    response = ClientUtils.getData("insertComponents",newValueObjects);
    if (!response.isError()) {
      panel.getExplosionPanel().reloadTree();
    }
    return response;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response response = null;
    ComponentVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (ComponentVO)persistentObjects.get(i);
      response = validateComponent(vo);
      if (response.isError())
        return response;
    }

    response = ClientUtils.getData("updateComponents",new ArrayList[]{oldPersistentObjects,persistentObjects});
    if (!response.isError()) {
      panel.getExplosionPanel().reloadTree();
    }
    return response;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteComponents",persistentObjects);
    if (!response.isError()) {
      panel.getExplosionPanel().reloadTree();
    }
    return response;
  }



}
