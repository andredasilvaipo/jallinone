package org.jallinone.production.orders.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.production.billsofmaterial.server.BillOfMaterialsBean;
import org.jallinone.production.billsofmaterial.server.BillOfMaterialsUtil;
import org.jallinone.production.orders.java.ProdOrderComponentVO;
import org.jallinone.production.orders.java.ProdOrderProductVO;
import org.jallinone.registers.currency.server.CurrenciesBean;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.warehouse.availability.java.ItemAvailabilityVO;
import org.jallinone.warehouse.availability.server.ItemAvailabilitiesBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to check if all components are available in the specified warehouse.</p>
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
public class CheckComponentsAvailabilityBean implements CheckComponentsAvailability {

	private BillOfMaterialsBean bean;
	private ItemAvailabilitiesBean avail;
	private MeasuresBean conv;


	public void setBean(BillOfMaterialsBean bean) {
		this.bean = bean;
	}

	public void setAvail(ItemAvailabilitiesBean avail) {
		this.avail = avail;
	}


	public void setConv(MeasuresBean conv) {
		this.conv = conv;
	}


	private CurrenciesBean compCurr;

	public void setCompCurr(CurrenciesBean compCurr) {
		this.compCurr = compCurr;
	}

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** external connection */
	private Connection conn = null;

	/**
	 * Set external connection.
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Create local connection
	 */
	public Connection getConn() throws Exception {
		Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
	}


	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	public ProdOrderProductVO getProdOrderProduct() {
		throw new UnsupportedOperationException();
	}


	public CheckComponentsAvailabilityBean() {
	}


	/**
	 * Check if components required by specified products are available.
	 * @param products list of ProdOrderProductVO objects
	 * @params compAltComps collection of <component item code,HashSet of alternative component item codes>; filled by this method (and given back by reference)
	 * @return VOListResponse of ProdOrderComponentVO objects
	 */
	public final VOListResponse checkComponentsAvailability(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             HashMap compAltComps, ArrayList products, String serverLanguageId,
             String username, ArrayList companiesList) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);
			avail.setConn(conn);
			conv.setConn(conn);

			if (products.size()==0) {
				return new VOListResponse(new ArrayList(),false,0);
			}


			// fill in comps hashtable with the collection of required components...
			ItemPK pk = null;
			ProdOrderProductVO prodVO = null;
			ArrayList components = null;
			MaterialVO compVO = null;
			Response res = null;
			ProdOrderComponentVO componentVO = null;
			Hashtable comps = new Hashtable(); // collection of <component item code,ProdOrderComponentVO object>
			for(int i=0;i<products.size();i++) {
				// retrieve bill of materials for each product...
				prodVO = (ProdOrderProductVO)products.get(i);
				pk = new ItemPK(prodVO.getCompanyCodeSys01DOC23(),prodVO.getItemCodeItm01DOC23());
				res = BillOfMaterialsUtil.getBillOfMaterials(conn,compCurr,pk,serverLanguageId,username,new ArrayList());
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}

				// extract components only (leaf nodes)...
				components = getComponents((DefaultMutableTreeNode) ((TreeModel)((VOResponse)res).getVo()).getRoot() );
				for(int j=0;j<components.size();j++) {
					compVO = (MaterialVO)components.get(j);
					componentVO = (ProdOrderComponentVO)comps.get(compVO.getItemCodeItm01ITM03());
					if (componentVO==null) {
						componentVO = new ProdOrderComponentVO();
						comps.put(compVO.getItemCodeItm01ITM03(),componentVO);
						componentVO.setAvailableQty(new BigDecimal(0));
						componentVO.setCompanyCodeSys01DOC24(compVO.getCompanyCodeSys01ITM03());
						componentVO.setDescriptionSYS10(compVO.getDescriptionSYS10());
						componentVO.setDocNumberDOC24(prodVO.getDocNumberDOC23());
						componentVO.setDocYearDOC24(prodVO.getDocYearDOC23());
						componentVO.setItemCodeItm01DOC24(compVO.getItemCodeItm01ITM03());
						componentVO.setMinSellingQtyUmCodeReg02ITM01(compVO.getMinSellingQtyUmCodeReg02ITM01());
						componentVO.setQtyDOC24(new BigDecimal(0));
					}
					componentVO.setQtyDOC24(componentVO.getQtyDOC24().add(compVO.getQtyITM03().multiply(prodVO.getQtyDOC23())));
				}
			}


			// check components availability in the specified warehouse...
			Enumeration en = comps.keys();
			GridParams gridParams = new GridParams();
			gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,prodVO.getCompanyCodeSys01DOC23());
			gridParams.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,prodVO.getWarehouseCodeWar01DOC22());
			gridParams.getOtherGridParams().put(ApplicationConsts.LOAD_ALL,Boolean.TRUE);
			ItemAvailabilityVO availVO = null;
			BigDecimal availability,altAvailability,delta;
			String itemCode = null;
			ArrayList list,availList;
			AltComponentVO altVO = null;
			ArrayList alternativeComps = new ArrayList();
			ArrayList compsToRemove = new ArrayList();
			ProdOrderComponentVO altComponentVO = null;
			HashSet altCodes = null; // list of alternative component item codes...
			BigDecimal altQty = null;
			while(en.hasMoreElements()) {
				itemCode = en.nextElement().toString();
				componentVO = (ProdOrderComponentVO)comps.get(itemCode);

				gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),itemCode));
				res = avail.loadItemAvailabilities(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username,companiesList);
				if (res.isError())
					throw new Exception(res.getErrorMessage());

				availList = new ArrayList(((VOListResponse)res).getRows());
				componentVO.setAvailabilities(availList);
				availability = new BigDecimal(0);
				for(int i=0;i<availList.size();i++) {
					availVO = (ItemAvailabilityVO)availList.get(i);
					availability = availability.add(availVO.getAvailableQtyWAR03());
				}
				componentVO.setAvailableQty(availability);

				if (componentVO.getQtyDOC24().doubleValue()>componentVO.getAvailableQty().doubleValue()) {
					// check if there exist some alternative component...
					res = bean.loadAltComponents(gridParams,serverLanguageId,username);
					if (res.isError())
						throw new Exception(res.getErrorMessage());

					list = new ArrayList(((VOListResponse)res).getRows());
					for(int i=0;i<list.size();i++) {
						altVO = (AltComponentVO)list.get(i);
						gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),altVO.getItemCodeItm01ITM04()));
						res = avail.loadItemAvailabilities(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username,companiesList);
						if (res.isError())
							throw new Exception(res.getErrorMessage());

						availList = new ArrayList(((VOListResponse)res).getRows());
						altAvailability = new BigDecimal(0);
						for(int j=0;j<availList.size();j++) {
							availVO = (ItemAvailabilityVO)availList.get(j);
							altAvailability = altAvailability.add(availVO.getAvailableQtyWAR03());
						}
						if (altAvailability.doubleValue()>0) {
							altComponentVO = new ProdOrderComponentVO();
							altComponentVO.setAvailabilities(availList);
							altComponentVO.setAvailableQty(altAvailability);
							altComponentVO.setCompanyCodeSys01DOC24(altVO.getCompanyCodeSys01ITM04());
							altComponentVO.setDescriptionSYS10(altVO.getDescriptionSYS10());
							altComponentVO.setDocNumberDOC24(prodVO.getDocNumberDOC23());
							altComponentVO.setDocYearDOC24(prodVO.getDocYearDOC23());
							altComponentVO.setItemCodeItm01DOC24(altVO.getItemCodeItm01ITM04());
							altComponentVO.setMinSellingQtyUmCodeReg02ITM01(altVO.getMinSellingQtyUmCodeReg02ITM01());
							altQty = conv.convertQty(
									altVO.getMinSellingQtyUmCodeReg02ITM01(),
									componentVO.getMinSellingQtyUmCodeReg02ITM01(),
									altAvailability,
									serverLanguageId,username
							);
							if (componentVO.getQtyDOC24().subtract(availability).doubleValue()>altQty.doubleValue()) {
								delta = altQty;
								altComponentVO.setQtyDOC24(altAvailability);
							}
							else {
								delta = componentVO.getQtyDOC24();
								altComponentVO.setQtyDOC24(
										conv.convertQty(
												componentVO.getMinSellingQtyUmCodeReg02ITM01(),
												altVO.getMinSellingQtyUmCodeReg02ITM01(),
												delta,
												serverLanguageId,username
										)
								);
							}
							componentVO.setQtyDOC24(componentVO.getQtyDOC24().subtract(delta));
							alternativeComps.add(altComponentVO);

							altCodes = (HashSet)compAltComps.get(itemCode);
							if (altCodes==null) {
								altCodes = new HashSet();
								compAltComps.put(itemCode,altCodes);
							}
							altCodes.add(altVO.getItemCodeItm01ITM04());

							if (componentVO.getQtyDOC24().doubleValue()==0) {
								compsToRemove.add(componentVO);
								break;
							}
							if (componentVO.getQtyDOC24().subtract(availability).doubleValue()==0)
								break;
						}
					}
				}
			}

			list = new ArrayList(comps.values());
			list.addAll(alternativeComps);
			list.removeAll(compsToRemove);
			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"checkComponentsAvailability","Error while retrieving components availability for the specified production order",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
	        try {
	            if (this.conn==null && conn!=null) {
	                // close only local connection
	                conn.commit();
	                conn.close();
	            }

	        }
	        catch (Exception exx) {}

	        bean.setConn(null);
			avail.setConn(null);
			conv.setConn(null);

		}
	}


	/**
	 * Expand the current node and return the list of leaves (MaterialVO objects).
	 */
	private ArrayList getComponents(DefaultMutableTreeNode node) {
		ArrayList list = new ArrayList();
		for(int i=0;i<node.getChildCount();i++)
			list.addAll(getComponents((DefaultMutableTreeNode)node.getChildAt(i)));
		if (node.isLeaf())
			list.add(node.getUserObject());
		return list;
	}



}
