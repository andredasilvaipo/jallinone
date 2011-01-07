package org.jallinone.contacts.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.contacts.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.Subject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for people contacts defined inside an organization contact.</p>
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
public class PeopleContactsController extends CompanyGridController {


  /** parent organization detail frame */
  private ContactDetailFrame detailFrame = null;


  public PeopleContactsController(ContactDetailFrame detailFrame) {
    this.detailFrame = detailFrame;
  }



  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    Subject sub = (Subject)detailFrame.getCurrentForm().getVOModel().getValueObject();

    new ContactController(
        detailFrame.getController().getGridFrame(),
        new SubjectVO(
          ((GridContactVO)persistentObject).getCompanyCodeSys01REG04(),
          ((GridContactVO)persistentObject).getProgressiveREG04(),
          ((GridContactVO)persistentObject).getName_1REG04(),
          ((GridContactVO)persistentObject).getName_2REG04(),
          ((GridContactVO)persistentObject).getSubjectTypeREG04()
        ),
        new SubjectPK(
          sub.getCompanyCodeSys01REG04(),
          sub.getProgressiveREG04()
        )
    );
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    Subject sub = (Subject)detailFrame.getCurrentForm().getVOModel().getValueObject();

    if (super.beforeInsertGrid(grid)) {
      new ContactController(
          detailFrame.getController().getGridFrame(),
          null,
          new SubjectPK(
            sub.getCompanyCodeSys01REG04(),
            sub.getProgressiveREG04()
          )
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
    GridContactVO vo = (GridContactVO)persistentObjects.get(0);
    return ClientUtils.getData("deleteContact",new SubjectPK(vo.getCompanyCodeSys01REG04(),vo.getProgressiveREG04()));
  }




}
