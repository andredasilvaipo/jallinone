package org.jallinone.warehouse.movements.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import java.math.BigDecimal;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import java.util.ArrayList;
import org.jallinone.warehouse.documents.client.SerialNumberDialog;
import org.jallinone.variants.java.VariantsMatrixVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for employee detail frame.</p>
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
public class ManualMovementController extends CompanyFormController {


  /** detail frame */
  private ManualMovementDetailFrame detailFrame = null;


  public ManualMovementController() {
    detailFrame = new ManualMovementDetailFrame(this);
    MDIFrame.add(detailFrame);

  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {

    if (detailFrame.isSerialNumbersRequired() &&
        !promptSerialNumbers(detailFrame.getVariantsPanel().getCells(),detailFrame.getVariantsPanel().getVariantsMatrixVO(),(MovementVO)newPersistentObject)) {
      return new ErrorResponse("insert not allowed until serial numbers are not defined");
    }

    Response res = null;

    if (detailFrame.getVariantsPanel().getVariantsMatrixVO()==null) {
      // no variants...
      res = ClientUtils.getData("insertManualMovement",newPersistentObject);
    }
    else {
      // the item has variants...
      res = ClientUtils.getData(
        "insertManualMovements",
        new Object[]{
          newPersistentObject,
          detailFrame.getVariantsPanel().getVariantsMatrixVO(),
          detailFrame.getVariantsPanel().getCells()
        }
      );
    }

    return res;
    //return ClientUtils.getData("insertManualMovement",newPersistentObject);
  }


  /**
   * Show an input dialog to insert serial numbers.
   */
  private boolean promptSerialNumbers(Object[][] cells,VariantsMatrixVO matrixVO,MovementVO vo) {
    // define serial numbers and bar codes list to the right size...
    ArrayList list = new ArrayList(vo.getDeltaQtyWAR02().intValue());
    for(int i=0;i<vo.getDeltaQtyWAR02().intValue();i++) {
      list.add(null);
    }
    vo.setSerialNumbers(list);

    // show input dialog...
    SerialNumberDialog d = new SerialNumberDialog(
        cells,
        matrixVO,
        vo.getSerialNumbers(),
        vo.getItemCodeItm01WAR02()+" - "+vo.getItemDescriptionSYS10()
    );

    return true;
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    detailFrame.getManualMovForm().insert();

    JOptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        ClientSettings.getInstance().getResources().getResource("manual movement correctly inserted"),
        ClientSettings.getInstance().getResources().getResource("manual movement"),
        JOptionPane.INFORMATION_MESSAGE
    );

    detailFrame.initControls();
  }



}
