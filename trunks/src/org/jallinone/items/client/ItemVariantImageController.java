package org.jallinone.items.client;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.items.java.ItemVariantImageVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Form controller used to set an image for an item variants combination.</p>
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
public class ItemVariantImageController extends FormController {

	private ItemVariantImagesFrame frame = null;

	public ItemVariantImageController(ItemVariantImagesFrame frame) {
		this.frame = frame;
	}


	/**
	 * This method must be overridden by the subclass to retrieve data and return the valorized value object.
	 * If the method is not overridden, the current version will return a "demo" value object.
	 * @param valueObjectClass value object class
	 * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
	 */
	public Response loadData(Class valueObjectClass) {
		ItemVariantImageVO vo = (ItemVariantImageVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRow());

		GridParams gridParams = new GridParams();
		ItemPK pk = new ItemPK();
		pk.setCompanyCodeSys01ITM01(vo.getCompanyCodeSys01ITM33());
		pk.setItemCodeITM01(vo.getItemCodeItm01ITM33());
		gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);

		gridParams.getFilteredColumns().put("variantTypeItm06ITM33",new FilterWhereClause[]{new FilterWhereClause("variantTypeItm06ITM33",Consts.EQ,vo.getVariantTypeItm06ITM33()),null});
		gridParams.getFilteredColumns().put("variantTypeItm07ITM33",new FilterWhereClause[]{new FilterWhereClause("variantTypeItm07ITM33",Consts.EQ,vo.getVariantTypeItm07ITM33()),null});
		gridParams.getFilteredColumns().put("variantTypeItm08ITM33",new FilterWhereClause[]{new FilterWhereClause("variantTypeItm08ITM33",Consts.EQ,vo.getVariantTypeItm08ITM33()),null});
		gridParams.getFilteredColumns().put("variantTypeItm09ITM33",new FilterWhereClause[]{new FilterWhereClause("variantTypeItm09ITM33",Consts.EQ,vo.getVariantTypeItm09ITM33()),null});
		gridParams.getFilteredColumns().put("variantTypeItm10ITM33",new FilterWhereClause[]{new FilterWhereClause("variantTypeItm10ITM33",Consts.EQ,vo.getVariantTypeItm10ITM33()),null});
		gridParams.getFilteredColumns().put("variantCodeItm11ITM33",new FilterWhereClause[]{new FilterWhereClause("variantCodeItm11ITM33",Consts.EQ,vo.getVariantCodeItm11ITM33()),null});
		gridParams.getFilteredColumns().put("variantCodeItm12ITM33",new FilterWhereClause[]{new FilterWhereClause("variantCodeItm12ITM33",Consts.EQ,vo.getVariantCodeItm12ITM33()),null});
		gridParams.getFilteredColumns().put("variantCodeItm13ITM33",new FilterWhereClause[]{new FilterWhereClause("variantCodeItm13ITM33",Consts.EQ,vo.getVariantCodeItm13ITM33()),null});
		gridParams.getFilteredColumns().put("variantCodeItm14ITM33",new FilterWhereClause[]{new FilterWhereClause("variantCodeItm14ITM33",Consts.EQ,vo.getVariantCodeItm14ITM33()),null});
		gridParams.getFilteredColumns().put("variantCodeItm15ITM33",new FilterWhereClause[]{new FilterWhereClause("variantCodeItm15ITM33",Consts.EQ,vo.getVariantCodeItm15ITM33()),null});
		VOListResponse res = (VOListResponse)ClientUtils.getData("loadItemVariantImages",gridParams);
		if (res.isError())
			return res;
		return new VOResponse(res.getRows().get(0));
	}


	/**
	 * Method called by the Form panel to insert new data.
	 * @param newValueObject value object to save
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response insertRecord(ValueObject newPersistentObject) throws Exception {
		return ClientUtils.getData("insertItemVariantImage",newPersistentObject);
	}

	/**
	 * Method called by the Form panel to update existing data.
	 * @param oldPersistentObject original value object, previous to the changes
	 * @param persistentObject value object to save
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
		return ClientUtils.getData("updateItemVariantImage",new Object[]{oldPersistentObject,persistentObject});
	}

	/**
	 * Method called by the Form panel to delete existing data.
	 * @param persistentObject value object to delete
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecord(ValueObject persistentObject) throws Exception {
		return ClientUtils.getData("deleteItemVariantImage",persistentObject);
	}

	/**
	 * Callback method called by the Form panel when the Form is set to INSERT mode.
	 * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
	 * @param persistentObject new value object
	 */
	public void createPersistentObject(ValueObject persistentObject) throws Exception {
		ItemVariantImageVO vo = (ItemVariantImageVO)persistentObject;
		vo.setCompanyCodeSys01ITM33(frame.getVo().getCompanyCodeSys01());
		vo.setItemCodeItm01ITM33(frame.getVo().getItemCodeItm01());
		vo.setVariantTypeItm06ITM33(ApplicationConsts.JOLLY);
		vo.setVariantTypeItm07ITM33(ApplicationConsts.JOLLY);
		vo.setVariantTypeItm08ITM33(ApplicationConsts.JOLLY);
		vo.setVariantTypeItm09ITM33(ApplicationConsts.JOLLY);
		vo.setVariantTypeItm10ITM33(ApplicationConsts.JOLLY);
		vo.setVariantCodeItm11ITM33(ApplicationConsts.JOLLY);
		vo.setVariantCodeItm12ITM33(ApplicationConsts.JOLLY);
		vo.setVariantCodeItm13ITM33(ApplicationConsts.JOLLY);
		vo.setVariantCodeItm14ITM33(ApplicationConsts.JOLLY);
		vo.setVariantCodeItm15ITM33(ApplicationConsts.JOLLY);
	}


	/**
	 * Callback method called when the data loading is completed.
	 * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
	 */
	public void loadDataCompleted(boolean error) {

	}



}
