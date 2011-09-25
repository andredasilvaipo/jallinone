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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for document links grid frame.</p>
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
public class DocumentLinksController extends CompanyGridController {

  /** detail frame */
  private DocumentFrame frame = null;


  public DocumentLinksController(DocumentFrame frame) {
    this.frame = frame;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertDocumentLinks",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    if (frame.getLinksgrid().getVOListTableModel().getRowCount()==persistentObjects.size()) {
      return new ErrorResponse("you are not allowed to delete all document links");
    }

    return ClientUtils.getData("deleteDocumentLinks",persistentObjects);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    DocumentLinkVO vo = (DocumentLinkVO)valueObject;
    DetailDocumentVO docVO = (DetailDocumentVO)frame.getFormPanel().getVOModel().getValueObject();
    vo.setCompanyCodeSys01DOC17(docVO.getCompanyCodeSys01DOC14());
    vo.setProgressiveDoc14DOC17(docVO.getProgressiveDOC14());
  }



}
