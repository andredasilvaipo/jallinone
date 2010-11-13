package org.jallinone.production.orders.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.server.LoadItemBean;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.production.billsofmaterial.server.BillOfMaterialsBean;
import org.jallinone.production.billsofmaterial.server.BillOfMaterialsUtil;
import org.jallinone.production.manufactures.java.ManufacturePhaseVO;
import org.jallinone.production.manufactures.java.ManufactureVO;
import org.jallinone.production.manufactures.server.ManufacturePhasesBean;
import org.jallinone.production.orders.java.DetailProdOrderVO;
import org.jallinone.production.orders.java.ProdOrderComponentVO;
import org.jallinone.production.orders.java.ProdOrderPK;
import org.jallinone.production.orders.java.ProdOrderProductVO;
import org.jallinone.production.orders.java.ProdOrderVO;
import org.jallinone.registers.currency.server.CurrenciesBean;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.warehouse.availability.java.ItemAvailabilityVO;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.server.ManualMovementsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage production orders
 * </p>
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
public class ProdOrdersBean  implements ProdOrders {


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



  private ProdOrderProductsBean rowsAction;

  public void setRowsAction(ProdOrderProductsBean rowsAction) {
	  this.rowsAction = rowsAction;
  }

  private AddMovementBean mov;

  public void setMov(AddMovementBean mov) {
	  this.mov = mov;
  }



  private CheckComponentsAvailabilityBean check;

  public void setCheck(CheckComponentsAvailabilityBean check) {
	  this.check = check;
  }

  private BillOfMaterialsBean bill;

  public void setBill(BillOfMaterialsBean bill) {
	  this.bill = bill;
  }

  private LoadItemBean item;

  public void setItem(LoadItemBean item) {
	  this.item = item;
  }

  private ManufacturePhasesBean ops;

  public void setOps(ManufacturePhasesBean ops) {
	  this.ops = ops;
  }

  private MeasuresBean conv;

  public void setConv(MeasuresBean conv) {
	  this.conv = conv;
  }


  private CurrenciesBean compCurr;

  public void setCompCurr(CurrenciesBean compCurr) {
	  this.compCurr = compCurr;
  }

  public ProdOrdersBean() {}




  /**
   * Business logic to execute.
   */
  public VOListResponse loadProdOrders(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01,DOC22_PRODUCTION_ORDER.DOC_STATE,DOC22_PRODUCTION_ORDER.DOC_YEAR,"+
          "DOC22_PRODUCTION_ORDER.DOC_NUMBER,DOC22_PRODUCTION_ORDER.DOC_DATE,DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01,"+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01,"+
          "DOC22_PRODUCTION_ORDER.DOC_SEQUENCE "+
          "from DOC22_PRODUCTION_ORDER where "+
          "DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC22_PRODUCTION_ORDER.DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC22_PRODUCTION_ORDER.DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC22_PRODUCTION_ORDER.DOC_NUMBER");
      attribute2dbField.put("docSequenceDOC22","DOC22_PRODUCTION_ORDER.DOC_SEQUENCE");
      attribute2dbField.put("docDateDOC22","DOC22_PRODUCTION_ORDER.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01");


      ArrayList values = new ArrayList();

      // read from DOC22 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ProdOrderVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching production orders list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




  /**
   * Business logic to execute.
   */
  public VOResponse updateProdOrder(DetailProdOrderVO oldVO,DetailProdOrderVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC_NUMBER");
      attribute2dbField.put("docDateDOC22","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DESCRIPTION2_WAR01");
      attribute2dbField.put("noteDOC22","NOTE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC22");
      pkAttributes.add("docYearDOC22");
      pkAttributes.add("docNumberDOC22");

      // update DOC22 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC22_PRODUCTION_ORDER",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing production order",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }

  }




	/**
	 * Business logic to execute.
	 */
	 public VOResponse confirmProdOrder(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             DetailProdOrderVO vo, String t1, String t2, String t3, String t4,
             String t5, String serverLanguageId, String username, String imagePath,
             ArrayList companiesList) throws Throwable {
		 PreparedStatement pstmt = null;
		 PreparedStatement pstmt2 = null;
		 ResultSet rset = null;
		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;
			 rowsAction.setConn(conn); // use same transaction...
			 mov.setConn(conn); // use same transaction...
			 check.setConn(conn); // use same transaction...
			 bill.setConn(conn); // use same transaction...
			 item.setConn(conn); // use same transaction...
			 ops.setConn(conn); // use same transaction...
			 conv.setConn(conn); // use same transaction...



			 // retrieve products defined in the specified production order...
			 GridParams gridParams = new GridParams();
			 gridParams.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,new ProdOrderPK(vo.getCompanyCodeSys01DOC22(),vo.getDocYearDOC22(),vo.getDocNumberDOC22()));
			 Response res = rowsAction.loadProdOrderProducts(gridParams,serverLanguageId,username);
			 if (res.isError())
				 throw new Exception(res.getErrorMessage());
			 ArrayList products = new ArrayList(((VOListResponse)res).getRows());
			 HashMap compAltCodes = new HashMap(); // collection of <component item code,HashSet of alternative component item codes>

			 // retrieve components availabilities required in the specified production order...
			 res = check.checkComponentsAvailability(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,compAltCodes,products,serverLanguageId,username,companiesList);
			 if (res.isError())
				 throw new Exception(res.getErrorMessage());
			 ArrayList components = new ArrayList(((VOListResponse)res).getRows());


			 // remove all components previously saved in DOC24 for the specified production order...
			 pstmt = conn.prepareStatement(
					 "delete from DOC24_PRODUCTION_COMPONENTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
			 );
			 pstmt.setString(1, vo.getCompanyCodeSys01DOC22());
			 pstmt.setBigDecimal(2, vo.getDocYearDOC22());
			 pstmt.setBigDecimal(3, vo.getDocNumberDOC22());
			 pstmt.execute();
			 pstmt.close();


			 // generate progressive for doc. sequence...
			 pstmt = conn.prepareStatement(
					 "select max(DOC_SEQUENCE) from DOC22_PRODUCTION_ORDER where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
			 );
			 pstmt.setString(1,vo.getCompanyCodeSys01DOC22());
			 pstmt.setBigDecimal(2,vo.getDocYearDOC22());
			 rset = pstmt.executeQuery();
			 int docSequenceDOC22 = 1;
			 if (rset.next())
				 docSequenceDOC22 = rset.getInt(1)+1;
			 vo.setDocSequenceDOC22(new BigDecimal(docSequenceDOC22));
			 rset.close();


			 // update order state...
			 pstmt = conn.prepareStatement("update DOC22_PRODUCTION_ORDER set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?");
			 pstmt.setString(1,ApplicationConsts.CONFIRMED);
			 pstmt.setInt(2,docSequenceDOC22);
			 pstmt.setString(3,vo.getCompanyCodeSys01DOC22());
			 pstmt.setBigDecimal(4,vo.getDocYearDOC22());
			 pstmt.setBigDecimal(5,vo.getDocNumberDOC22());
			 pstmt.execute();
			 pstmt.close();


			 // insert components in DOC24 for the specified production order...
			 pstmt2 = conn.prepareStatement(
					 "insert into DOC24_PRODUCTION_COMPONENTS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,QTY,PROGRESSIVE_HIE01) values(?,?,?,?,?,?)"
			 );
			 ProdOrderComponentVO compVO = null;
			 ProdOrderComponentVO auxCompVO = null;


			 // check components availability in the specified warehouse and remove components from it...
			 ItemAvailabilityVO availVO = null;
			 java.util.List list = null;
			 BigDecimal availability = new BigDecimal(0);
			 BigDecimal qty,delta;
			 WarehouseMovementVO movVO = null;
			 int i;
			 ArrayList serialNumbers = new ArrayList();
			 for(int j=0;j<components.size();j++) {
				 compVO = (ProdOrderComponentVO)components.get(j);
				 qty = compVO.getQtyDOC24();
				 list = compVO.getAvailabilities();
				 availability = new BigDecimal(0);
				 for(i=0;i<list.size();i++) {
					 availVO = (ItemAvailabilityVO)list.get(i);
					 availability = availability.add(availVO.getAvailableQtyWAR03());
				 }
				 if (availability.doubleValue()<qty.doubleValue()) {
					 return new VOResponse(
							 compVO.getItemCodeItm01DOC24()+" "+
							 t1+".\n"+
							 t2+": "+availability.doubleValue()+" "+
							 t3+": "+qty.doubleValue()
					 );
				 }
				 // the current component is available: it will be removed...
				 i=0;
				 while(qty.doubleValue()>0) {
					 availVO = (ItemAvailabilityVO)list.get(i);
					 if (qty.doubleValue()>availVO.getAvailableQtyWAR03().doubleValue()) {
						 delta = availVO.getAvailableQtyWAR03();
						 qty = qty.subtract(delta);
					 }
					 else {
						 delta = qty;
						 qty = new BigDecimal(0);
					 }

					 // insert record in DOC24...
					 compVO.setProgressiveHie01DOC24(availVO.getProgressiveHie01WAR03());
					 compVO.setLocationDescriptionSYS10(availVO.getLocationDescriptionSYS10());
					 pstmt2.setString(1, vo.getCompanyCodeSys01DOC22());
					 pstmt2.setBigDecimal(2, vo.getDocYearDOC22());
					 pstmt2.setBigDecimal(3, vo.getDocNumberDOC22());
					 pstmt2.setString(4, compVO.getItemCodeItm01DOC24());
					 pstmt2.setBigDecimal(5, delta);
					 pstmt2.setBigDecimal(6, compVO.getProgressiveHie01DOC24());
					 pstmt2.execute();

					 // create a warehouse movement...
					 movVO = new WarehouseMovementVO(
							 availVO.getProgressiveHie01WAR03(),
							 delta,
							 vo.getCompanyCodeSys01DOC22(),
							 vo.getWarehouseCodeWar01DOC22(),
							 compVO.getItemCodeItm01DOC24(),
							 ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_PRODUCTION,
							 ApplicationConsts.ITEM_GOOD,
							 t4+" "+vo.getDocSequenceDOC22()+"/"+vo.getDocYearDOC22(),
							 serialNumbers,

							 compVO.getVariantCodeItm11DOC24(),
							 compVO.getVariantCodeItm12DOC24(),
							 compVO.getVariantCodeItm13DOC24(),
							 compVO.getVariantCodeItm14DOC24(),
							 compVO.getVariantCodeItm15DOC24(),
							 compVO.getVariantTypeItm06DOC24(),
							 compVO.getVariantTypeItm07DOC24(),
							 compVO.getVariantTypeItm08DOC24(),
							 compVO.getVariantTypeItm09DOC24(),
							 compVO.getVariantTypeItm10DOC24()

					 );
					 res = mov.addWarehouseMovement(movVO,t5,serverLanguageId,username);
					 if (res.isError()) {
						 throw new Exception(res.getErrorMessage());
					 }

					 i++;
				 }

			 }
			 rset.close();
			 pstmt2.close();

			 Hashtable usedComponents = new Hashtable();
			 Hashtable usedComponentsVO = new Hashtable();
			 for(int j=0;j<components.size();j++) {
				 compVO = (ProdOrderComponentVO)components.get(j);
				 usedComponents.put(compVO.getItemCodeItm01DOC24(),compVO.getQtyDOC24());
				 usedComponentsVO.put(compVO.getItemCodeItm01DOC24(),compVO);
			 }
			 res = insertProductComponents(conn,vo,products,usedComponents,usedComponentsVO,compAltCodes,serverLanguageId,username,imagePath);
			 if (res.isError()) {
				 throw new Exception(res.getErrorMessage());
			 }

			 return new VOResponse(new BigDecimal(docSequenceDOC22));
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"executeCommand","Error while confirming a production order",ex);
			 try {
				 if (this.conn==null && conn!=null)
					 // rollback only local connection
					 conn.rollback();
			 }
			 catch (Exception ex3) {
			 }

			 throw new Exception(ex.getMessage());
		 }
		 finally {
			 try {
				 if (rset != null) {
					 rset.close();
				 }
			 }
			 catch (Exception ex4) {
			 }
			 try {
				 pstmt.close();
			 }
			 catch (Exception ex2) {
			 }
			 try {
				 pstmt2.close();
			 }
			 catch (Exception ex2) {
			 }
			 try {
				 if (this.conn==null && conn!=null) {
					 // close only local connection
					 conn.commit();
					 conn.close();
				 }

			 }
			 catch (Exception exx) {}
		 }
	 }


	 /**
	  * Insert records into DOC25/DOC26 tables.
	  */
	 private Response insertProductComponents(Connection conn,DetailProdOrderVO prodOrderVO,ArrayList products,Hashtable usedComponents,Hashtable usedComponentsVO,HashMap compAltCodes,String serverLanguageId,String username,String imagePath) throws Throwable {
		 PreparedStatement pstmt = null;
		 PreparedStatement pstmt2 = null;
		 try {
			 pstmt = conn.prepareStatement(
					 "insert into DOC25_PRODUCTION_PROD_COMPS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,PRODUCT_ITEM_CODE_ITM01,COMPONENT_ITEM_CODE_ITM01,QTY,SEQUENCE) values(?,?,?,?,?,?,?)"
			 );
			 pstmt2 = conn.prepareStatement(
					 "insert into DOC26_PRODUCTION_OPERATIONS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,PHASE_NUMBER,"+
					 "OPERATION_CODE,OPERATION_DESCRIPTION,VALUE,DURATION,MANUFACTURE_TYPE,COMPLETION_PERC,QTY,TASK_CODE,TASK_DESCRIPTION,"+
					 "MACHINERY_CODE,MACHINERY_DESCRIPTION,SUBST_OPERATION_CODE,SUBST_OPERATION_DESCRIPTION,NOTE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			 );

			 ProdOrderProductVO prodVO = null;
			 Response res = null;
			 TreeModel model = null;
			 DefaultMutableTreeNode root;
			 int sequence = 0;
			 HashSet subProductsAlreadyAdded = new HashSet();
			 for(int i=0;i<products.size();i++) {
				 prodVO = (ProdOrderProductVO)products.get(i);
				 // retrieve bill of materials for each product...
				 res = BillOfMaterialsUtil.getBillOfMaterials(conn,compCurr,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),prodVO.getItemCodeItm01DOC23()),serverLanguageId,username,new ArrayList());
				 if (res.isError())
					 throw new Exception(res.getErrorMessage());
				 model = (TreeModel)((VOResponse)res).getVo();
				 root = (DefaultMutableTreeNode)model.getRoot();

				 // expand nodes to retrieve sun-products and fill in DOC25/DOC26...
				 sequence = expandNode(sequence,prodOrderVO,root,usedComponents,usedComponentsVO,compAltCodes,subProductsAlreadyAdded,pstmt,pstmt2,serverLanguageId,username,imagePath);
			 }
			 return new VOResponse(Boolean.TRUE);
		 }
		 finally {
			 try {
				 pstmt.close();
			 }
			 catch (Exception ex) {
			 }
			 try {
				 pstmt2.close();
			 }
			 catch (Exception ex1) {
			 }

		 }
	 }


	 /**
	  * Expand the current (sub)product node.
	  */
	 private int expandNode(int sequence,DetailProdOrderVO prodOrderVO,DefaultMutableTreeNode node,Hashtable usedComponents,Hashtable usedComponentsVO,HashMap compAltCodes,HashSet subProductsAlreadyAdded,PreparedStatement pstmt,PreparedStatement pstmt2,String serverLanguageId,String username,String imagePath) throws Throwable {
		 DefaultMutableTreeNode childNode = null;
		 MaterialVO vo = null;
		 BigDecimal qty = null;
		 Iterator it = null;
		 BigDecimal delta = null;
		 String itemCode = null;
		 MaterialVO prodVO = (MaterialVO)node.getUserObject();

		 for(int i=0;i<node.getChildCount();i++) {
			 childNode = (DefaultMutableTreeNode)node.getChildAt(i);
			 if (!childNode.isLeaf()) {
				 // the component is a sub-product...
				 sequence = expandNode(sequence,prodOrderVO,childNode,usedComponents,usedComponentsVO,compAltCodes,subProductsAlreadyAdded,pstmt,pstmt2,serverLanguageId,username,imagePath);
			 }
		 }

		 BigDecimal altQty = null;
		 for(int i=0;i<node.getChildCount();i++) {
			 childNode = (DefaultMutableTreeNode)node.getChildAt(i);
			 vo = (MaterialVO)childNode.getUserObject();
			 if (childNode.isLeaf()) {
				 // component found: check if there must be used an alternative component instead of the current one...
				 qty = (BigDecimal)usedComponents.get(vo.getItemCodeItm01ITM03());
				 if (qty!=null && qty.doubleValue()>0) {
					 if (vo.getQtyITM03().doubleValue()<=qty.doubleValue())
						 delta = vo.getQtyITM03();
					 else {
						 delta = qty;
					 }
					 vo.setQtyITM03(vo.getQtyITM03().subtract(delta));
					 usedComponents.put(vo.getItemCodeItm01ITM03(),qty.subtract(delta));

					 pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
					 pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
					 pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
					 pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
					 pstmt.setString(5,vo.getItemCodeItm01ITM03());
					 pstmt.setBigDecimal(6,delta);
					 pstmt.setInt(7,sequence);
					 pstmt.execute();
					 sequence++;
				 }
				 if (vo.getQtyITM03().doubleValue()>0) {
					 it = ((HashSet)compAltCodes.get(vo.getItemCodeItm01ITM03())).iterator();
					 while(vo.getQtyITM03().doubleValue()>0 && it.hasNext()) {
						 itemCode = it.next().toString();
						 qty = (BigDecimal)usedComponents.get(itemCode);

						 altQty = conv.convertQty(
								 ((ProdOrderComponentVO)usedComponentsVO.get(itemCode)).getMinSellingQtyUmCodeReg02ITM01(),
								 vo.getMinSellingQtyUmCodeReg02ITM01(),
								 qty,
								 serverLanguageId,
								 username
						 );

						 if (qty!=null && qty.doubleValue()>0) {
							 if (vo.getQtyITM03().doubleValue()<=altQty.doubleValue()) {
								 delta = conv.convertQty(
										 vo.getMinSellingQtyUmCodeReg02ITM01(),
										 ((ProdOrderComponentVO)usedComponentsVO.get(itemCode)).getMinSellingQtyUmCodeReg02ITM01(),
										 vo.getQtyITM03(),
										 serverLanguageId,
										 username
								 );
								 usedComponents.put(itemCode,qty.subtract(delta));
								 vo.setQtyITM03(new BigDecimal(0));

							 }
							 else {
								 delta = qty;
								 usedComponents.put(itemCode,new BigDecimal(0));
								 vo.setQtyITM03(vo.getQtyITM03().subtract(altQty));
							 }

							 pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
							 pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
							 pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
							 pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
							 pstmt.setString(5,itemCode);
							 pstmt.setBigDecimal(6,delta);
							 pstmt.setInt(7,sequence);
							 pstmt.execute();
							 sequence++;
						 }
					 }
				 }
			 }
			 else {
				 // the component is a sub-product...
				 pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
				 pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
				 pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
				 pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
				 pstmt.setString(5,vo.getItemCodeItm01ITM03());
				 pstmt.setBigDecimal(6,vo.getQtyITM03());
				 pstmt.setInt(7,sequence);
				 pstmt.execute();
				 sequence++;
			 }
		 }

		 ItemPK pk = new ItemPK(prodVO.getCompanyCodeSys01ITM03(),prodVO.getItemCodeItm01ITM03());
		 BigDecimal prg = item.getProgressiveHie02ITM01(pk, username);

		 // retrieve manufacture code...
		 Response res = new VOResponse(item.loadItem(
			 pk,
			 prg,
			 serverLanguageId,
			 username,
			 imagePath,
			 new ArrayList()
		 ));
		 if (res.isError())
			 throw new Exception(res.getErrorMessage());
		 DetailItemVO itemVO = (DetailItemVO)((VOResponse)res).getVo();

		 // retrieve manufacture operations and insert them to DOC26...
		 ManufactureVO manVO = new ManufactureVO();
		 manVO.setCompanyCodeSys01PRO01(itemVO.getCompanyCodeSys01ITM01());
		 manVO.setManufactureCodePRO01(itemVO.getManufactureCodePro01ITM01());
		 GridParams gridParams = new GridParams();
		 gridParams.getOtherGridParams().put(ApplicationConsts.MANUFACTURE_VO,manVO);
		 res = ops.loadManufacturePhases(gridParams,serverLanguageId,username,new ArrayList());
		 if (res.isError())
			 throw new Exception(res.getErrorMessage());
		 java.util.List list = ((VOListResponse)res).getRows();
		 ManufacturePhaseVO phaseVO = null;

		 if (subProductsAlreadyAdded.contains(prodVO.getItemCodeItm01ITM03()))
			 return sequence;
		 subProductsAlreadyAdded.add(prodVO.getItemCodeItm01ITM03());

		 for(int i=0;i<list.size();i++) {
			 phaseVO = (ManufacturePhaseVO)list.get(i);
			 /*
"insert into DOC26_PRODUCTION_OPERATIONS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,PHASE_NUMBER,"+
"OPERATION_CODE,OPERATION_DESCRIPTION,VALUE,DURATION,MANUFACTURE_TYPE,COMPLETION_PERC,QTY,TASK_CODE,TASK_DESCRIPTION,"+
"MACHINERY_CODE,MACHINERY_DESCRIPTION,SUBST_OPERATION_CODE,SUBST_OPERATION_DESCRIPTION,NOTE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"

			  */
			 pstmt2.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
			 pstmt2.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
			 pstmt2.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
			 pstmt2.setString(4,prodVO.getItemCodeItm01ITM03());
			 pstmt2.setBigDecimal(5,phaseVO.getPhaseNumberPRO02());
			 pstmt2.setString(6,phaseVO.getOperationCodePro04PRO02());
			 pstmt2.setString(7,phaseVO.getDescriptionSYS10());
			 pstmt2.setBigDecimal(8,phaseVO.getValuePRO02());
			 pstmt2.setBigDecimal(9,phaseVO.getDurationPRO02());
			 pstmt2.setString(10,phaseVO.getManufactureTypePRO02());
			 pstmt2.setBigDecimal(11,phaseVO.getCompletionPercPRO02());
			 pstmt2.setBigDecimal(12,phaseVO.getQtyPRO02());
			 pstmt2.setString(13,phaseVO.getTaskCodeReg07PRO02());
			 pstmt2.setString(14,phaseVO.getTaskDescriptionSYS10());
			 pstmt2.setString(15,phaseVO.getMachineryCodePro03PRO02());
			 pstmt2.setString(16,phaseVO.getMachineryDescriptionSYS10());
			 pstmt2.setString(17,phaseVO.getSubstOperationCodePro04PRO02());
			 pstmt2.setString(18,phaseVO.getDescription2());
			 pstmt2.setString(19,phaseVO.getNotePRO02());
			 pstmt2.execute();

			 try {
				 rowsAction.setConn(null);
				 mov.setConn(null);
				 check.setConn(null);
				 bill.setConn(null);
				 item.setConn(null);
				 ops.setConn(null);
				 conv.setConn(null);
			 } catch (Exception ex) {}
		 }

		 return sequence;
	 }



  /**
   * Business logic to execute.
   */
  public VOResponse closeProdOrder(DetailProdOrderVO vo,String t1,String t2,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      rowsAction.setConn(conn); // use same transaction...
      mov.setConn(conn); // use same transaction...

      // update order state...
      pstmt = conn.prepareStatement("update DOC22_PRODUCTION_ORDER set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1, ApplicationConsts.CLOSED);
      pstmt.setString(2, vo.getCompanyCodeSys01DOC22());
      pstmt.setBigDecimal(3, vo.getDocYearDOC22());
      pstmt.setBigDecimal(4, vo.getDocNumberDOC22());
      pstmt.execute();
      pstmt.close();
      vo.setDocStateDOC22(ApplicationConsts.CLOSED);


      // retrieve products defined in the production order...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,new ProdOrderPK(vo.getCompanyCodeSys01DOC22(),vo.getDocYearDOC22(),vo.getDocNumberDOC22()));
      Response res = rowsAction.loadProdOrderProducts(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      java.util.List products = ((VOListResponse)res).getRows();


      // add created products to warehouse locations...
      ItemPK pk = null;
      ProdOrderProductVO prodVO = null;
      WarehouseMovementVO movVO = null;
      ArrayList serialNumbers = new ArrayList();
      for(int i=0;i<products.size();i++) {
        prodVO = (ProdOrderProductVO)products.get(i);

        // create a warehouse movement...
        movVO = new WarehouseMovementVO(
          prodVO.getProgressiveHie01DOC23(),
          prodVO.getQtyDOC23(),
          vo.getCompanyCodeSys01DOC22(),
          vo.getWarehouseCode2War01DOC22(),
          prodVO.getItemCodeItm01DOC23(),
          ApplicationConsts.WAREHOUSE_MOTIVE_LOAD_BY_PRODUCTION,
          ApplicationConsts.ITEM_GOOD,
          t1+" "+vo.getDocSequenceDOC22()+"/"+vo.getDocYearDOC22(),
          serialNumbers,

         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY

          /*
          prodVO.getVariantCodeItm11DOC23(),
          prodVO.getVariantCodeItm12DOC23(),
          prodVO.getVariantCodeItm13DOC23(),
          prodVO.getVariantCodeItm14DOC23(),
          prodVO.getVariantCodeItm15DOC23(),
          prodVO.getVariantTypeItm06DOC23(),
          prodVO.getVariantTypeItm07DOC23(),
          prodVO.getVariantTypeItm08DOC23(),
          prodVO.getVariantTypeItm09DOC23(),
          prodVO.getVariantTypeItm10DOC23()
          */
        );
        res = mov.addWarehouseMovement(movVO,t2,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing a production order",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

    	throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }

      try {
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}

      try {
        rowsAction.setConn(null);
        mov.setConn(null);
      } catch (Exception ex) {}
    }
  }




  /**
   * Business logic to execute.
   */
  public VOResponse loadProdOrder(ProdOrderPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01,DOC22_PRODUCTION_ORDER.DOC_STATE,DOC22_PRODUCTION_ORDER.DOC_YEAR,"+
          "DOC22_PRODUCTION_ORDER.DOC_NUMBER,DOC22_PRODUCTION_ORDER.DOC_DATE,DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01,"+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01,"+
          "DOC22_PRODUCTION_ORDER.NOTE,WAR01_WAREHOUSES.PROGRESSIVE_HIE02,DOC22_PRODUCTION_ORDER.DOC_SEQUENCE "+
          "from DOC22_PRODUCTION_ORDER,WAR01_WAREHOUSES where "+
          "DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01=? and "+
          "DOC22_PRODUCTION_ORDER.DOC_YEAR=? and "+
          "DOC22_PRODUCTION_ORDER.DOC_NUMBER=? and "+
          "DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC22_PRODUCTION_ORDER.DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC22_PRODUCTION_ORDER.DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC22_PRODUCTION_ORDER.DOC_NUMBER");
      attribute2dbField.put("docSequenceDOC22","DOC22_PRODUCTION_ORDER.DOC_SEQUENCE");
      attribute2dbField.put("docDateDOC22","DOC22_PRODUCTION_ORDER.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01");
      attribute2dbField.put("noteDOC22","DOC22_PRODUCTION_ORDER.NOTE");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());

      // read from DOC22 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          DetailProdOrderVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching production orders list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }





  /**
   * Business logic to execute.
   */
  public VOResponse insertProdOrder(DetailProdOrderVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCode = companiesList.get(0).toString();

      if (vo.getCompanyCodeSys01DOC22()==null)
        vo.setCompanyCodeSys01DOC22(companyCode);

      // generate internal progressive for doc. number...
      vo.setDocNumberDOC22(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC22(),"DOC22_PRODUCTION_ORDER","DOC_NUMBER",conn));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC_NUMBER");
      attribute2dbField.put("docDateDOC22","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DESCRIPTION2_WAR01");
      attribute2dbField.put("noteDOC22","NOTE");


      // insert into DOC22...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC22_PRODUCTION_ORDER",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );
      if (res.isError())
    	  throw new Exception(res.getErrorMessage());

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new production order",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

    	throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }

  }





  /**
   * Business logic to execute.
   */
  public VOResponse deleteProdOrders(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt23 = null;
    PreparedStatement pstmt24 = null;
    PreparedStatement pstmt25 = null;
    PreparedStatement pstmt26 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ProdOrderPK pk = null;

      pstmt = conn.prepareStatement(
          "delete from DOC22_PRODUCTION_ORDER where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt23 = conn.prepareStatement(
          "delete from DOC23_PRODUCTION_PRODUCTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt24 = conn.prepareStatement(
          "delete from DOC24_PRODUCTION_COMPONENTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt25 = conn.prepareStatement(
          "delete from DOC25_PRODUCTION_PROD_COMPS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt26 = conn.prepareStatement(
          "delete from DOC26_PRODUCTION_OPERATIONS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );

      for(int i=0;i<list.size();i++) {
        pk = (ProdOrderPK)list.get(i);

        // phisically delete the record in DOC23...
        pstmt23.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt23.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt23.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt23.execute();

        // phisically delete the record in DOC24...
        pstmt24.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt24.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt24.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt24.execute();

        // phisically delete the record in DOC25...
        pstmt25.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt25.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt25.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt25.execute();

        // phisically delete the record in DOC26...
        pstmt26.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt26.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt26.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt26.execute();

        // phisically delete the record in DOC22...
        pstmt.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt.execute();
      }


     return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing production orders",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt23.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt24.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt25.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt26.close();
      }
      catch (Exception ex2) {
      }
      try {
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}
    }

  }





}

