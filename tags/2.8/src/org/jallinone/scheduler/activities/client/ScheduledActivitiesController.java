package org.jallinone.scheduler.activities.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.scheduler.activities.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import java.awt.Color;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description:Grid Controller used for scheduled activities grid frame.</p>
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
public class ScheduledActivitiesController extends CompanyGridController {


  /** scheduled activities grid frame */
  private ScheduledActivitiesGridFrame gridFrame = null;

  /** flag used to view subject and manager info in activity detail */
  private boolean crm;


  public ScheduledActivitiesController(boolean crm) {
    this.crm = crm;
    gridFrame = new ScheduledActivitiesGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    new ScheduledActivityController(
        gridFrame.getGrid(),
        gridFrame,
        new ScheduledActivityPK(
          ((GridScheduledActivityVO)persistentObject).getCompanyCodeSys01SCH06(),
          ((GridScheduledActivityVO)persistentObject).getProgressiveSCH06()
        ),
        crm
    );
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (super.beforeInsertGrid(grid)) {
      new ScheduledActivityController(
        gridFrame.getGrid(),
        gridFrame,
        null,
        crm
      );
    }
    return false;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    GridScheduledActivityVO vo = null;
    ScheduledActivityPK pk = null;
    ArrayList pks = new ArrayList();
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (GridScheduledActivityVO)persistentObjects.get(i);
      pk = new ScheduledActivityPK(vo.getCompanyCodeSys01SCH06(),vo.getProgressiveSCH06());
      pks.add(pk);
    }
    Response response = ClientUtils.getData("deleteScheduledActivities",pks);
    return response;
  }


  /**
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributedName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributedName,Object value) {
    GridScheduledActivityVO vo = (GridScheduledActivityVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
    if (attributedName.equals("activityStateSCH06")) {
      Color color = null;
      if (vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) ||
          vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED))
        return super.getBackgroundColor(row,attributedName,value);
      else return new Color(241,143,137);
    }
    else if (attributedName.equals("prioritySCH06")) {
      Color color = null;
      if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGHEST))
        color = new Color(241,123,137);
      else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGH))
        color = new Color(248,176,181);
      else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_NORMAL))
        color = new Color(191,246,207);
      else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_LOW))
        color = new Color(191,226,207);
      else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_TRIVIAL))
        color = new Color(191,206,207);
      return color;
    }
    else
      return super.getBackgroundColor(row,attributedName,value);

  }


}
