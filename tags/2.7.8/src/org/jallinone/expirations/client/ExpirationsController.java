package org.jallinone.expirations.client;

import java.util.ArrayList;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.expirations.java.ExpirationVO;
import org.openswing.swing.message.receive.java.ErrorResponse;
import java.util.HashMap;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sale/purchase expirations grid frame.</p>
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
public class ExpirationsController extends GridController {

  /** grid frame */
  private ExpirationsGridFrame gridFrame = null;


  public ExpirationsController() {
    gridFrame = new ExpirationsGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
		ExpirationVO vo = null;
		for(int i=0;i<persistentObjects.size();i++) {
			vo = (ExpirationVO)persistentObjects.get(i);

			if (vo.getPayedValueDOC19()!=null &&
					vo.getPayedValueDOC19().equals(vo.getValueDOC19()) &&
					!Boolean.TRUE.equals(vo.getPayedDOC19())) {
					vo.setPayedDOC19(Boolean.TRUE);
			}

			if (Boolean.TRUE.equals(vo.getPayedDOC19()) &&
			    (vo.getValueDOC19()==null && vo.getPayedValueDOC19()!=null ||
				   vo.getValueDOC19()!=null && vo.getPayedValueDOC19()==null ||
       		 vo.getValueDOC19()!=null && !vo.getValueDOC19().equals(vo.getPayedValueDOC19())) &&
					(vo.getRoundingAccountCodeAcc02DOC19()==null || vo.getRoundingAccountCodeAcc02DOC19().equals(""))
			) {
				 return new ErrorResponse("you need to specify a rounding account code");
			}

		}
    return ClientUtils.getData("updateExpirations",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


		/**
		 * @param grid grid
		 * @param row selected row index
		 * @param attributeName attribute name that identifies the selected grid column
		 * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
		 */
		public boolean isCellEditable(GridControl grid,int row,String attributeName) {
			ExpirationVO vo = (ExpirationVO)grid.getVOListTableModel().getObjectForRow(row);

			if (attributeName.equals("alreadyPayedDOC19") &&
					vo.getAlreadyPayedDOC19()!=null && vo.getAlreadyPayedDOC19().doubleValue()!=0)
				return false;
			return grid.isFieldEditable(row,attributeName);
		}



		/**
		 * Callback method invoked each time a cell is edited: this method define if the new value is valid.
		 * This method is invoked ONLY if:
		 * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
		 * - the cell is editable
		 * Default behaviour: cell value is valid.
		 * @param rowNumber selected row index
		 * @param attributeName attribute name related to the column currently selected
		 * @param oldValue old cell value (before cell editing)
		 * @param newValue new cell value (just edited)
		 * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
		 */
		public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
			ExpirationVO vo = (ExpirationVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);

			if (attributeName.equals("payedDOC19") &&
					Boolean.TRUE.equals(newValue) &&
					vo.getPayedDateDOC19()==null)
				vo.setPayedDateDOC19(new java.sql.Date(System.currentTimeMillis()));

			if (attributeName.equals("payedDOC19") &&
					Boolean.TRUE.equals(newValue) &&
					vo.getPayedValueDOC19()==null)
				vo.setPayedValueDOC19(vo.getValueDOC19());

			if (attributeName.equals("payedDOC19") &&
					Boolean.TRUE.equals(newValue) &&
					vo.getRealPaymentTypeCodeReg11DOC19()==null) {
				vo.setRealPaymentTypeCodeReg11DOC19(vo.getPaymentTypeCodeReg11DOC19());
				vo.setRealPaymentDescriptionSYS10(vo.getPaymentDescriptionSYS10());
			}

			if (attributeName.equals("payedValueDOC19") &&
					newValue!=null &&
					!newValue.equals(vo.getValueDOC19()) &&
					vo.getRoundingAccountCodeAcc02DOC19()==null) {

				// retrieve default rounding account code...
				HashMap map = new HashMap();
				map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC19());
				if (vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
						vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
						vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
						vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
						vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
						vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE))
					map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
				else
					map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ROUNDING_COSTS_CODE);

				Response res = ClientUtils.getData("loadUserParam",map);
				if (!res.isError()) {
					String accCode = (String)((VOResponse)res).getVo();
					vo.setRoundingAccountCodeAcc02DOC19(accCode);
					gridFrame.getColRoundingAccCode().forceValidate();
				}
			}

			return true;
		}




		/**
		 * Callback method invoked each time the grid mode is changed.
		 * @param currentMode current grid mode
		 */
		public void modeChanged(int currentMode) {
			gridFrame.getButtonPayments().setEnabled(currentMode==Consts.READONLY);
		}


}
