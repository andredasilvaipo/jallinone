package org.jallinone.sales.documents.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used for delivery request rows grid.</p>
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
public class DeliveryRequestRowsController extends CompanyGridController {

  /** delivery request rows panel */
  private DeliveryRequestRowsGridPanel panel = null;


  public DeliveryRequestRowsController(DeliveryRequestRowsGridPanel panel) {
    this.panel = panel;
  }



  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (panel.getGrid().getVOListTableModel().getRowCount()>0) {
      doubleClick(0,panel.getGrid().getVOListTableModel().getObjectForRow(0));
    }
  }




}
