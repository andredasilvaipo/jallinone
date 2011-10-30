package org.jallinone.documents.client;

import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.documents.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for document versions grid frame.</p>
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
public class DocumentVersionsController extends CompanyGridController {

  /** detail frame */
  private DocumentFrame frame = null;


  public DocumentVersionsController(DocumentFrame frame) {
    this.frame = frame;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    if (frame.getVgrid().getVOListTableModel().getRowCount()==persistentObjects.size()) {
      return new ErrorResponse("you are not allowed to delete all document versions");
    }
    return ClientUtils.getData("deleteDocumentVersions",persistentObjects);
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    Response res = ClientUtils.getData("loadDocumentVersion",persistentObject);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(frame),
          ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      String docId = (String)((VOResponse)res).getVo();
      try {
        ClientUtils.showDocument(docId);
      }
      catch (Exception ex) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+ex.toString(),
            ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }



}
