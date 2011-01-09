package org.jallinone.documents.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.documents.java.DocumentPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.documents.java.DetailDocumentVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for document form.</p>
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
public class DocumentController extends CompanyFormController {

  /** document frame */
  private DocumentFrame frame = null;

  /** parent frame */
  private DocumentsFrame parentFrame = null;

  /** document pk */
  private DocumentPK pk = null;

  /** used to link the document to the specified tree level (on insert) */
  private BigDecimal progressiveHie01DOC17 = null;

  /** used to link the document to the specified company code (on insert) */
  private String companyCodeSys01DOC16 = null;


  public DocumentController(DocumentsFrame parentFrame,DocumentPK pk,String companyCodeSys01DOC16,BigDecimal progressiveHie01DOC17) {
    this.parentFrame = parentFrame;
    this.pk = pk;
    this.progressiveHie01DOC17 = progressiveHie01DOC17;
    this.companyCodeSys01DOC16 = companyCodeSys01DOC16;
    this.frame = new DocumentFrame(this);
    MDIFrame.add(frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (pk!=null) {
      frame.getFormPanel().setMode(Consts.READONLY);
      frame.getFormPanel().executeReload();
    }
    else {
      frame.getFormPanel().insert();
    }
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);
    if (ok) {
      frame.getLinksgrid().clearData();
      frame.getVgrid().clearData();
      frame.getPropPanel().clearData();
      frame.getControlDocSize().setText("");
    }
    return ok;
  }


  /**
   * Callback method invoked on pressing INSERT button, after changing to insert mode.
   */
  public void afterInsertData(Form form) {
    frame.setButtonsEnabled(false);
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadDocument",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    DetailDocumentVO vo = (DetailDocumentVO)newPersistentObject;
    vo.setCompanyCodeSys01DOC14(companyCodeSys01DOC16);
    vo.setProgressiveHie01DOC17(progressiveHie01DOC17);
    if (vo.getDocument()==null) {
      return new ErrorResponse("you must attach a file");
    }

    Response res = ClientUtils.getData("insertDocument",vo);
    if (!res.isError()) {
      vo = (DetailDocumentVO)((VOResponse)res).getVo();
      vo.setDocument(null);
      frame.getControlDocSize().setText("");
      pk = new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14());
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }

      frame.getLinksgrid().getOtherGridParams().put(
          ApplicationConsts.DOCUMENT_PK,
          pk
      );
      frame.getLinksgrid().reloadData();

      frame.getVgrid().getOtherGridParams().put(
          ApplicationConsts.DOCUMENT_PK,
          pk
      );
      frame.getVgrid().reloadData();

      frame.getPropPanel().reloadData(frame);

      frame.setButtonsEnabled(true);

    }
    return res;
  }

  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    DetailDocumentVO vo = (DetailDocumentVO)persistentObject;
    Response res = ClientUtils.getData("updateDocument",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      vo = (DetailDocumentVO)((VOResponse)res).getVo();
      vo.setDocument(null);
      frame.getVgrid().reloadData();
      frame.getPropPanel().reloadData(frame);
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
    }
    return res;
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ArrayList pks = new ArrayList();
    DetailDocumentVO vo = (DetailDocumentVO)persistentObject;
    DocumentPK pk = new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14());
    pks.add(pk);
    Response res = ClientUtils.getData("deleteDocuments",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
      frame.getLinksgrid().clearData();
      frame.getVgrid().clearData();
      frame.getPropPanel().clearData();
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject PersistentObject) throws Exception { }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }


}
