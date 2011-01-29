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
import org.openswing.swing.form.client.FormController;
import org.jallinone.expirations.java.PaymentVO;
import org.openswing.swing.message.receive.java.ValueObject;
import java.math.BigDecimal;
import org.jallinone.expirations.java.PaymentDistributionVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the form controller for a payment.</p>
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
public class PaymentController extends FormController {

	private PaymentsGridFrame frame = null;


	public PaymentController(PaymentsGridFrame frame) {
		this.frame = frame;
	}


	public Response loadData(Class valueObjectClass) {
		return new VOResponse(frame.getPayGrid().getVOListTableModel().getObjectForRow(frame.getPayGrid().getSelectedRow()));
	}


	/**
	 * Callback method called when the data loading is completed.
	 * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
	 */
	public void loadDataCompleted(boolean error) {
		if (!error) {
			PaymentVO vo = (PaymentVO)frame.getPayForm().getVOModel().getValueObject();

			frame.getControlValue().setCurrencySymbol(vo.getCurrencySymbolREG03());
			frame.getControlValue().setDecimals(vo.getDecimalsREG03().intValue());

			frame.getGrid().reloadData();
		}
		else
			frame.getGrid().clearData();
	}


	/**
	 * Callback method called when the Form mode is changed.
	 * @param currentMode current Form mode
	 */
	public void modeChanged(int currentMode) {
		if (currentMode==Consts.INSERT) {
			frame.getGrid().clearData();
		}
	}


	/**
	 * Callback method called by the Form panel when the Form is set to INSERT mode.
	 * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
	 * @param persistentObject new value object
	 */
	public void createPersistentObject(ValueObject PersistentObject) throws Exception {
		PaymentVO payVO = (PaymentVO)frame.getPayForm().getVOModel().getValueObject();
		payVO.setCompanyCodeSys01DOC27((String)frame.getPayGrid().getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
		payVO.setPaymentDateDOC27(new java.sql.Date(System.currentTimeMillis()));
		payVO.setProgressiveReg04DOC27((BigDecimal)frame.getPayGrid().getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));

  	if (frame.getControlCodCustomer().getValue()!=null || !frame.getControlCodCustomer().getValue().equals(""))
			payVO.setCustomerSupplierCodeDOC27((String)frame.getControlCodCustomer().getValue());
		else
			payVO.setCustomerSupplierCodeDOC27((String)frame.getControlCodSupplier().getValue());

	}


	/**
	 * Method called by the Form panel to insert new data.
	 * @param newValueObject value object to save
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response insertRecord(ValueObject newPersistentObject) throws Exception {
		// retrieve payed expirations...
		ArrayList exp = new ArrayList();
		for(int i=0;i<frame.getGrid().getVOListTableModel().getRowCount();i++)
			exp.add( frame.getGrid().getVOListTableModel().getObjectForRow(i) );
		if (exp.size()==0)
			return new ErrorResponse("you need to specify at least one expiration");
		PaymentVO payVO = (PaymentVO)frame.getPayForm().getVOModel().getValueObject();

	// check total amount...
		BigDecimal total = new BigDecimal(0);
		PaymentDistributionVO vo = null;
		for(int i=0;i<exp.size();i++) {
			vo = (PaymentDistributionVO)exp.get(i);
			if (vo.getPaymentValueDOC28()==null)
				return new ErrorResponse("expirations must have a payment value");

			total = total.add(vo.getPaymentValueDOC28());
		}
		if ( payVO.getPaymentValueDOC27().subtract(total).doubleValue()!=0)
			return new ErrorResponse("payments specified for expirations are not equal to the total payment value");

		Response res = ClientUtils.getData("insertPayment",new Object[]{
		  newPersistentObject,
		  exp
		});
		if (!res.isError()) {
			frame.getParentGrid().reloadCurrentBlockOfData();
			frame.getGrid().setMode(Consts.READONLY);
			frame.getPayGrid().reloadCurrentBlockOfData();
		}
		return res;
	}


}
