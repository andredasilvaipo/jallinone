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
import org.jallinone.expirations.java.PaymentVO;
import org.jallinone.expirations.java.PaymentDistributionVO;
import org.openswing.swing.message.receive.java.ValueObject;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for payment distributions.</p>
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
public class PaymentDistributionsController extends GridController {

	private PaymentsGridFrame frame = null;


	public PaymentDistributionsController(PaymentsGridFrame frame) {
		this.frame = frame;
	}



	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		PaymentVO payVO = (PaymentVO)frame.getPayForm().getVOModel().getValueObject();
		PaymentDistributionVO vo = (PaymentDistributionVO)valueObject;
		vo.setCompanyCodeSys01DOC28(payVO.getCompanyCodeSys01DOC27());
		vo.setPayedDOC28(Boolean.FALSE);
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
		PaymentDistributionVO vo = (PaymentDistributionVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
		if (attributeName.equals("payedDOC28") &&
				Boolean.TRUE.equals(newValue) &&
				vo.getPaymentValueDOC28()==null
		) {
				vo.setPaymentValueDOC28(vo.getValueDOC19().subtract(vo.getAlreadyPayedDOC19()));
    }
		else if (attributeName.equals("paymentValueDOC28")) {
			// check for total amount...
			PaymentVO payVO = (PaymentVO)frame.getPayForm().getVOModel().getValueObject();
			BigDecimal total = new BigDecimal(0);
			if (newValue!=null)
				total = total.add((BigDecimal)newValue);
			for(int i=0;i<frame.getGrid().getVOListTableModel().getRowCount();i++) {
				vo = (PaymentDistributionVO)frame.getGrid().getVOListTableModel().getObjectForRow(i);
				if (i!=rowNumber &&
						vo.getPaymentValueDOC28()!=null)
					total = total.add(vo.getPaymentValueDOC28());
			}
			if (payVO.getPaymentValueDOC27()==null || payVO.getPaymentValueDOC27().subtract(total).doubleValue()<0)
				return false;

	    vo = (PaymentDistributionVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
	    if (newValue!=null && ((BigDecimal)newValue).equals(vo.getValueDOC19().subtract(vo.getAlreadyPayedDOC19())))
				vo.setPayedDOC28(Boolean.TRUE);

		}
		return true;
	}



}
