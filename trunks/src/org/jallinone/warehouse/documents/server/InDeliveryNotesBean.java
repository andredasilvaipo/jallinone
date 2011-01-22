package org.jallinone.warehouse.documents.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO;
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import org.jallinone.warehouse.documents.java.InDeliveryNoteRowPK;
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
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.items.java.VariantBarcodeVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.items.server.ValidateVariantBarcodeBean;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.warehouse.server.WarehousesBean;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage in delivery notes.</p>
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
public class InDeliveryNotesBean  implements InDeliveryNotes {


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

	private AddMovementBean movBean;

	public void setMovBean(AddMovementBean movBean) {
		this.movBean = movBean;
	}


	private WarehouseUtilsBean bean;

	public void setBean(WarehouseUtilsBean bean) {
		this.bean = bean;
	}

	private MeasuresBean convBean;

	public void setConvBean(MeasuresBean convBean) {
		this.convBean = convBean;
	}



  public InDeliveryNotesBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOListResponse loadInDeliveryNotes(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      int blockSize = 50;
			if (pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE)!=null)
				blockSize = Integer.parseInt(pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE).toString());

      String sql =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,PUR01_SUPPLIERS.SUPPLIER_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2, "+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,PUR01_SUPPLIERS,REG04_SUBJECTS where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC08_DELIVERY_NOTES.DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC08_DELIVERY_NOTES.DOC_REF");
      attribute2dbField.put("warehouseDescriptionDOC08","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");

      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);

      // read from DOC08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridDeliveryNoteVO.class,
          "Y",
          "N",
          null,
          pars,
          blockSize,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching in delivery notes list",ex);
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
   * Update in qty in referred purchase orders when closing a delivery note.
   * It update warehouse available quantities too.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if qtys updating was correctly executed
   */
  public VOResponse updateInQuantities(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		DeliveryNotePK pk, String t1, String t2, String serverLanguageId,
		String username) throws Throwable {
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      movBean.setConn(conn);
      bean.setConn(conn);
//
//      // retrieve internationalization settings (Resources object)...
//      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
//      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // retrieve all in delivery note rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
      Response res = bean.loadInDeliveryNoteRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pars,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());

      ArrayList values = new ArrayList();
      String sql1 =
          "select QTY,IN_QTY,ORDER_QTY from DOC07_PURCHASE_ITEMS where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

				String sql2 =
          "update DOC07_PURCHASE_ITEMS set IN_QTY=?,ORDER_QTY=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and IN_QTY=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      pstmt1 = conn.prepareStatement(sql1);
      pstmt2 = conn.prepareStatement(sql2);


      // for each item row it will be updated the related purchase order row and warehouse available quantities...
      GridInDeliveryNoteRowVO vo = null;
      ResultSet rset1 = null;
      BigDecimal qtyDOC07 = null;
      BigDecimal inQtyDOC07 = null;
      BigDecimal orderQtyDOC07 = null;
      BigDecimal qtyToAdd = null;
      Response innerResponse = null;
      for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
        vo = (GridInDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
        pstmt1.setString(1,vo.getCompanyCodeSys01DOC09());
        pstmt1.setString(2,vo.getDocTypeDoc06DOC09());
        pstmt1.setBigDecimal(3,vo.getDocYearDoc06DOC09());
        pstmt1.setBigDecimal(4,vo.getDocNumberDoc06DOC09());
        pstmt1.setString(5,vo.getItemCodeItm01DOC09());

        pstmt1.setString(6,vo.getVariantTypeItm06DOC09());
        pstmt1.setString(7,vo.getVariantCodeItm11DOC09());
        pstmt1.setString(8,vo.getVariantTypeItm07DOC09());
        pstmt1.setString(9,vo.getVariantCodeItm12DOC09());
        pstmt1.setString(10,vo.getVariantTypeItm08DOC09());
        pstmt1.setString(11,vo.getVariantCodeItm13DOC09());
        pstmt1.setString(12,vo.getVariantTypeItm09DOC09());
        pstmt1.setString(13,vo.getVariantCodeItm14DOC09());
        pstmt1.setString(14,vo.getVariantTypeItm10DOC09());
        pstmt1.setString(15,vo.getVariantCodeItm15DOC09());

        rset1 = pstmt1.executeQuery();
        if(rset1.next()) {
          qtyDOC07 = rset1.getBigDecimal(1);
          inQtyDOC07 = rset1.getBigDecimal(2);
          orderQtyDOC07 = rset1.getBigDecimal(3);
          rset1.close();

          // update in qty in the purchase order row...
          if (vo.getSupplierQtyDOC09().doubleValue()<qtyDOC07.subtract(inQtyDOC07).doubleValue())
            qtyToAdd = vo.getSupplierQtyDOC09();
          else
            qtyToAdd = qtyDOC07.subtract(inQtyDOC07);

          // update order qty in the purchase order row...
          orderQtyDOC07 = orderQtyDOC07.subtract(vo.getQtyDOC09()).setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP);
          if (orderQtyDOC07.doubleValue()<0)
            orderQtyDOC07 = new BigDecimal(0);

          pstmt2.setBigDecimal(1,inQtyDOC07.add(qtyToAdd).setScale(vo.getSupplierQtyDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          pstmt2.setBigDecimal(2,orderQtyDOC07);
          pstmt2.setString(3,vo.getCompanyCodeSys01DOC09());
          pstmt2.setString(4,vo.getDocTypeDoc06DOC09());
          pstmt2.setBigDecimal(5,vo.getDocYearDoc06DOC09());
          pstmt2.setBigDecimal(6,vo.getDocNumberDoc06DOC09());
          pstmt2.setString(7,vo.getItemCodeItm01DOC09());
          pstmt2.setBigDecimal(8,inQtyDOC07);

          pstmt2.setString(9,vo.getVariantTypeItm06DOC09());
          pstmt2.setString(10,vo.getVariantCodeItm11DOC09());
          pstmt2.setString(11,vo.getVariantTypeItm07DOC09());
          pstmt2.setString(12,vo.getVariantCodeItm12DOC09());
          pstmt2.setString(13,vo.getVariantTypeItm08DOC09());
          pstmt2.setString(14,vo.getVariantCodeItm13DOC09());
          pstmt2.setString(15,vo.getVariantTypeItm09DOC09());
          pstmt2.setString(16,vo.getVariantCodeItm14DOC09());
          pstmt2.setString(17,vo.getVariantTypeItm10DOC09());
          pstmt2.setString(18,vo.getVariantCodeItm15DOC09());

          if (pstmt2.executeUpdate()==0)
         	  throw new Exception("Updating not performed: the record was previously updated.");
        }
        else
          rset1.close();

        // update warehouse available qty..
        WarehouseMovementVO movVO = new WarehouseMovementVO(
            vo.getProgressiveHie01DOC09(),
            vo.getQtyDOC09(),
            vo.getCompanyCodeSys01DOC09(),
            vo.getWarehouseCodeWar01DOC08(),
            vo.getItemCodeItm01DOC09(),
            ApplicationConsts.WAREHOUSE_MOTIVE_LOAD_BY_ORDER,
            ApplicationConsts.ITEM_GOOD,
            t1+" "+vo.getDocSequenceDoc06DOC09()+"/"+vo.getDocYearDoc06DOC09(),
            vo.getSerialNumbers(),

            vo.getVariantCodeItm11DOC09(),
            vo.getVariantCodeItm12DOC09(),
            vo.getVariantCodeItm13DOC09(),
            vo.getVariantCodeItm14DOC09(),
            vo.getVariantCodeItm15DOC09(),
            vo.getVariantTypeItm06DOC09(),
            vo.getVariantTypeItm07DOC09(),
            vo.getVariantTypeItm08DOC09(),
            vo.getVariantTypeItm09DOC09(),
            vo.getVariantTypeItm10DOC09()
        );
        innerResponse = movBean.addWarehouseMovement(movVO,t2,serverLanguageId,username);

        if (innerResponse.isError())
          throw new Exception(innerResponse.getErrorMessage());
      }


      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateInQuantities","Error while updating in quantites in purchase orders",ex);
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
        pstmt1.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
      }

      try {
    	  bean.setConn(null);
      } catch (Exception e) {
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
   * Business logic to execute.
   */
  public VOResponse updateInDeliveryNote(DetailDeliveryNoteVO oldVO,DetailDeliveryNoteVO newVO,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC_REF");

      attribute2dbField.put("addressDOC08","ADDRESS");
      attribute2dbField.put("cityDOC08","CITY");
      attribute2dbField.put("provinceDOC08","PROVINCE");
      attribute2dbField.put("countryDOC08","COUNTRY");
      attribute2dbField.put("zipDOC08","ZIP");
      attribute2dbField.put("noteDOC08","NOTE");
      attribute2dbField.put("enabledDOC08","ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","CARRIER_CODE_REG09");
      attribute2dbField.put("progressiveReg04DOC08","PROGRESSIVE_REG04");
      attribute2dbField.put("deliveryDateDOC08","DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","TRANSPORT_MOTIVE_CODE_REG20");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC08");
      pkAttributes.add("docTypeDOC08");
      pkAttributes.add("docYearDOC08");
      pkAttributes.add("docNumberDOC08");

      // update DOC08 table...
      Response res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC08_DELIVERY_NOTES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing in delivery note",ex);
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
  public VOListResponse updateInDeliveryNoteRows(ArrayList oldRows,ArrayList newRows,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

      GridInDeliveryNoteRowVO oldVO = null;
      GridInDeliveryNoteRowVO newVO = null;

      Response res = null;
      for(int i=0;i<newRows.size();i++) {
        oldVO = (GridInDeliveryNoteRowVO)oldRows.get(i);
        newVO = (GridInDeliveryNoteRowVO)newRows.get(i);

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC09","COMPANY_CODE_SYS01");
        attribute2dbField.put("docTypeDOC09","DOC_TYPE");
        attribute2dbField.put("docYearDOC09","DOC_YEAR");
        attribute2dbField.put("docNumberDOC09","DOC_NUMBER");
        attribute2dbField.put("docTypeDoc06DOC09","DOC_TYPE_DOC06");
        attribute2dbField.put("docYearDoc06DOC09","DOC_YEAR_DOC06");
        attribute2dbField.put("docNumberDoc06DOC09","DOC_NUMBER_DOC06");
        attribute2dbField.put("rowNumberDOC09","ROW_NUMBER");
        attribute2dbField.put("itemCodeItm01DOC09","ITEM_CODE_ITM01");
        attribute2dbField.put("qtyDOC09","QTY");
        attribute2dbField.put("supplierQtyDOC09","SUPPLIER_QTY");
        attribute2dbField.put("progressiveHie02DOC09","PROGRESSIVE_HIE02");
        attribute2dbField.put("progressiveHie01DOC09","PROGRESSIVE_HIE01");
        attribute2dbField.put("invoiceQtyDOC09","INVOICE_QTY");

        attribute2dbField.put("progressiveDOC09","PROGRESSIVE");
        attribute2dbField.put("variantTypeItm06DOC09","VARIANT_TYPE_ITM06");
        attribute2dbField.put("variantCodeItm11DOC09","VARIANT_CODE_ITM11");
        attribute2dbField.put("variantTypeItm07DOC09","VARIANT_TYPE_ITM07");
        attribute2dbField.put("variantCodeItm12DOC09","VARIANT_CODE_ITM12");
        attribute2dbField.put("variantTypeItm08DOC09","VARIANT_TYPE_ITM08");
        attribute2dbField.put("variantCodeItm13DOC09","VARIANT_CODE_ITM13");
        attribute2dbField.put("variantTypeItm09DOC09","VARIANT_TYPE_ITM09");
        attribute2dbField.put("variantCodeItm14DOC09","VARIANT_CODE_ITM14");
        attribute2dbField.put("variantTypeItm10DOC09","VARIANT_TYPE_ITM10");
        attribute2dbField.put("variantCodeItm15DOC09","VARIANT_CODE_ITM15");

        HashSet pkAttributes = new HashSet();
        pkAttributes.add("progressiveDOC09");

        // update DOC09 table...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttributes,
            oldVO,
            newVO,
            "DOC09_IN_DELIVERY_NOTE_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );

        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        // insert serial numbers...
        res = bean.reinsertInSerialNumbers(newVO,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }

      return new VOListResponse(newRows,false,newRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing purchase order row",ex);
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
    	  bean.setConn(null);
      } catch (Exception e) {
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
   * Business logic to execute.
   */
  public VOResponse insertInDeliveryNote(DetailDeliveryNoteVO vo,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCode = companiesList.get(0).toString();

      vo.setEnabledDOC08("Y");
      vo.setDescriptionDOC08(vo.getWarehouseDescriptionDOC08());

      if (vo.getCompanyCodeSys01DOC08()==null)
        vo.setCompanyCodeSys01DOC08(companyCode);

      // generate the internal progressive for the document...
      vo.setDocNumberDOC08( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC08(),"DOC08_DELIVERY_NOTES","DOC_NUMBER",conn) );

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC_REF");

      attribute2dbField.put("addressDOC08","ADDRESS");
      attribute2dbField.put("cityDOC08","CITY");
      attribute2dbField.put("provinceDOC08","PROVINCE");
      attribute2dbField.put("countryDOC08","COUNTRY");
      attribute2dbField.put("zipDOC08","ZIP");
      attribute2dbField.put("noteDOC08","NOTE");
      attribute2dbField.put("enabledDOC08","ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","CARRIER_CODE_REG09");
      attribute2dbField.put("progressiveReg04DOC08","PROGRESSIVE_REG04");
      attribute2dbField.put("deliveryDateDOC08","DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","TRANSPORT_MOTIVE_CODE_REG20");

      // insert into DOC08...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC08_DELIVERY_NOTES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new in delivery note",ex);
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
		public GridInDeliveryNoteRowVO validateCode(
			HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
			PurchaseDocPK pk,
			String warehouseCode,
			BigDecimal progressiveREG04,
			BigDecimal progressiveHie01DOC09,
			BigDecimal docSequenceDOC06,
			BigDecimal delivNoteDocNumber,
			String codeType,
			String code,
			String serverLanguageId, String username) throws Throwable {

			 PreparedStatement pstmt = null;
			 Connection conn = null;
			 try {
				 if (this.conn == null) conn = getConn(); else conn = this.conn;
				 convBean.setConn(conn);

       	 Response res = null;
				 java.util.List rows = null;
				 ArrayList values = new ArrayList();
				 boolean itemFound = false;

				 GridInDeliveryNoteRowVO vo = new GridInDeliveryNoteRowVO();
				 vo.setCompanyCodeSys01DOC09(pk.getCompanyCodeSys01DOC06());
				 vo.setDocNumberDoc06DOC09(pk.getDocNumberDOC06());
				 vo.setDocNumberDOC09(delivNoteDocNumber);
				 vo.setDocTypeDoc06DOC09(pk.getDocTypeDOC06());
				 vo.setDocTypeDOC09(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
				 vo.setDocYearDoc06DOC09(pk.getDocYearDOC06());
				 vo.setDocYearDOC09(new BigDecimal(new java.util.Date().getYear() + 1900));
				 vo.setSupplierQtyDOC09(new BigDecimal(1));
				 vo.setQtyDOC09(new BigDecimal(1));
				 vo.setInvoiceQtyDOC09(new BigDecimal(0));
				 vo.setSerialNumbers(new ArrayList());
				 vo.setWarehouseCodeWar01DOC08(warehouseCode);
				 vo.setProgressiveHie01DOC09(progressiveHie01DOC09); // warehouse location
				 vo.setDocSequenceDoc06DOC09(docSequenceDOC06);

	       if ("B".equals(codeType)) {
					 // validate variants barcode...
					 String sql =
							 "select "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01,ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
							 "ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10,"+
							 "ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15,"+
							 "ITM22_VARIANT_BARCODES.BAR_CODE,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
							 "from ITM22_VARIANT_BARCODES,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC07_PURCHASE_ITEMS "+
							 "where "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=? AND "+
							 "ITM22_VARIANT_BARCODES.BAR_CODE=? AND "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
							 "ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and ITM01_ITEMS.ENABLED='Y' AND "+
							 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
							 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
							 "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01 AND "+
							 "DOC07_PURCHASE_ITEMS.DOC_TYPE=? AND "+
							 "DOC07_PURCHASE_ITEMS.DOC_YEAR=? AND "+
							 "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? AND "+
							 "DOC07_PURCHASE_ITEMS.QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 AND "+
							 "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14 AND "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15  ";

					 Map attribute2dbField = new HashMap();
					 attribute2dbField.put("companyCodeSys01ITM22","ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01");
					 attribute2dbField.put("itemCodeItm01ITM22","ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01");
					 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
					 attribute2dbField.put("barCodeITM22","ITM22_VARIANT_BARCODES.BAR_CODE");
					 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
					 attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
					 attribute2dbField.put("variantTypeItm06ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06");
					 attribute2dbField.put("variantCodeItm11ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11");
					 attribute2dbField.put("variantTypeItm07ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07");
					 attribute2dbField.put("variantCodeItm12ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12");
					 attribute2dbField.put("variantTypeItm08ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08");
					 attribute2dbField.put("variantCodeItm13ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13");
					 attribute2dbField.put("variantTypeItm09ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09");
					 attribute2dbField.put("variantCodeItm14ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14");
					 attribute2dbField.put("variantTypeItm10ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10");
					 attribute2dbField.put("variantCodeItm15ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15");

	         values.clear();
					 values.add(pk.getCompanyCodeSys01DOC06());
					 values.add(code);
					 values.add(serverLanguageId);
					 values.add(pk.getDocTypeDOC06());
					 values.add(pk.getDocYearDOC06());
					 values.add(pk.getDocNumberDOC06());

					 // read from ITM01 table...
					 res = QueryUtil.getQuery(
							 conn,
							 new UserSessionParameters(username),
							 sql,
							 values,
							 attribute2dbField,
							 VariantBarcodeVO.class,
							 "Y",
							 "N",
							 null,
							 new GridParams(),
							 true
					 );
					 if (res.isError())
						 throw new Exception(res.getErrorMessage());

					 rows = ((VOListResponse)res).getRows();
					 if (rows.size() == 1) {
						 // found variants barcode: pre-fill code and qty in variants matrix...
						 VariantBarcodeVO barcodeVO = (VariantBarcodeVO)rows.get(0);

						 //vo.setInQtyDOC07();
						 //vo.setDecimalsREG02();
						 vo.setItemCodeItm01DOC09(barcodeVO.getItemCodeItm01ITM22());
						 vo.setDescriptionSYS10(barcodeVO.getDescriptionSYS10());
						 vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(barcodeVO.getSerialNumberRequiredITM01())));
						 vo.setProgressiveHie02DOC09(barcodeVO.getProgressiveHie02ITM01()); // item's LOB

						 //vo.setQtyDOC07();
						 //vo.setSupplierItemCodePur02DOC09();
						 //vo.setSupplierQtyDecimalsREG02();
						 //vo.setUmCodeREG02();
						 //vo.setUmCodeReg02PUR02();
						 //vo.setValueREG05();
						 vo.setVariantCodeItm11DOC09(barcodeVO.getVariantCodeItm11ITM22());
						 vo.setVariantCodeItm12DOC09(barcodeVO.getVariantCodeItm12ITM22());
						 vo.setVariantCodeItm13DOC09(barcodeVO.getVariantCodeItm13ITM22());
						 vo.setVariantCodeItm14DOC09(barcodeVO.getVariantCodeItm14ITM22());
						 vo.setVariantCodeItm15DOC09(barcodeVO.getVariantCodeItm15ITM22());
						 vo.setVariantTypeItm06DOC09(barcodeVO.getVariantTypeItm06ITM22());
						 vo.setVariantTypeItm07DOC09(barcodeVO.getVariantTypeItm07ITM22());
						 vo.setVariantTypeItm08DOC09(barcodeVO.getVariantTypeItm08ITM22());
						 vo.setVariantTypeItm09DOC09(barcodeVO.getVariantTypeItm09ITM22());
						 vo.setVariantTypeItm10DOC09(barcodeVO.getVariantTypeItm10ITM22());

						 itemFound = true;
					 }

	         if (!itemFound) {
						 // no barcode found in ITM22 (barcode for item variants)
						 // trying to find a barcode at item level...
						 sql =
								 "select "+
								 "ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,SYS10_TRANSLATIONS.DESCRIPTION,"+
								 "ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
								 "from ITM01_ITEMS,SYS10_TRANSLATIONS,DOC07_PURCHASE_ITEMS where "+
								 "ITM01_ITEMS.COMPANY_CODE_SYS01=? AND "+
								 "ITM01_ITEMS.BAR_CODE=? AND "+
								 "ITM01_ITEMS.ENABLED='Y' AND "+
								 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
								 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
								 "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND " +
								 "DOC07_PURCHASE_ITEMS.DOC_TYPE=? AND " +
								 "DOC07_PURCHASE_ITEMS.DOC_YEAR=? AND " +
								 "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? AND " +
								 "DOC07_PURCHASE_ITEMS.QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 AND "+
								 "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14='*' AND " +
								 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15='*' ";

						 attribute2dbField.clear();
						 attribute2dbField.put("companyCodeSys01ITM01","ITM01_ITEMS.COMPANY_CODE_SYS01");
						 attribute2dbField.put("itemCodeITM01","ITM01_ITEMS.ITEM_CODE");
						 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
						 attribute2dbField.put("barCodeITM01","ITM01_ITEMS.BAR_CODE");
						 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
						 attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

						 values.clear();
						 values.add(pk.getCompanyCodeSys01DOC06());
						 values.add(code);
						 values.add(serverLanguageId);
						 values.add(pk.getDocTypeDOC06());
						 values.add(pk.getDocYearDOC06());
						 values.add(pk.getDocNumberDOC06());

						 // read from ITM01 table...
						 res = QueryUtil.getQuery(
								 conn,
								 new UserSessionParameters(username),
								 sql,
								 values,
								 attribute2dbField,
								 GridItemVO.class,
								 "Y",
								 "N",
								 null,
								 new GridParams(),
								 true
						 );

						 if (res.isError())
							 throw new Exception(res.getErrorMessage());

						 rows = ( (VOListResponse) res).getRows();
						 if (rows.size() == 1) {
							 // found variants barcode: pre-fill code and qty in variants matrix...
							 GridItemVO itemVO = (GridItemVO)rows.get(0);

							 //vo.setInQtyDOC07();
							 //vo.setDecimalsREG02();
							 vo.setItemCodeItm01DOC09(itemVO.getItemCodeITM01());
							 vo.setDescriptionSYS10(itemVO.getDescriptionSYS10());
							 vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(itemVO.getSerialNumberRequiredITM01())));
							 vo.setProgressiveHie02DOC09(itemVO.getProgressiveHie02ITM01()); // item's LOB

							 //vo.setQtyDOC07();
							 //vo.setSupplierItemCodePur02DOC09();
							 //vo.setSupplierQtyDecimalsREG02();
							 //vo.setUmCodeREG02();
							 //vo.setUmCodeReg02PUR02();
							 //vo.setValueREG05();
							 vo.setVariantCodeItm11DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm12DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm13DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm14DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm15DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm06DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm07DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm08DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm09DOC09(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm10DOC09(ApplicationConsts.JOLLY);

							 itemFound = true;
						 }
	         }

           if (!itemFound) {
						 // no barcode found!
						 return null;
					 }
	       }
         else {
					 // check for item code WITHOUT VARIANTS in buying order...
					 String sql =
							 "select DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01,DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,"+
							 "SYS10_TRANSLATIONS.DESCRIPTION,"+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15, "+
							 "ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02 "+
							 "from DOC07_PURCHASE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
							 "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
							 "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
							 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
							 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
							 "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
							 "DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
							 "DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
							 "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
							 "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? and "+
  						 "DOC07_PURCHASE_ITEMS.QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 and "+
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14='*' AND " +
							 "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15='*' ";

					 Map attribute2dbField = new HashMap();
					 attribute2dbField.put("companyCodeSys01DOC07","DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01");
					 attribute2dbField.put("itemCodeItm01DOC07","DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01");
					 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
					 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
					 attribute2dbField.put("progressiveHie02DOC07","DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02");

					 attribute2dbField.put("variantTypeItm06DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06");
					 attribute2dbField.put("variantCodeItm11DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11");
					 attribute2dbField.put("variantTypeItm07DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07");
					 attribute2dbField.put("variantCodeItm12DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12");
					 attribute2dbField.put("variantTypeItm08DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08");
					 attribute2dbField.put("variantCodeItm13DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13");
					 attribute2dbField.put("variantTypeItm09DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09");
					 attribute2dbField.put("variantCodeItm14DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14");
					 attribute2dbField.put("variantTypeItm10DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10");
					 attribute2dbField.put("variantCodeItm15DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15");

					 values.clear();
					 values.add(serverLanguageId);
					 values.add(pk.getCompanyCodeSys01DOC06());
					 values.add(pk.getDocTypeDOC06());
					 values.add(pk.getDocYearDOC06());
					 values.add(pk.getDocNumberDOC06());
					 values.add(code);

					 // read from DOC07 table...
					 res = QueryUtil.getQuery(
							 conn,
							 new UserSessionParameters(username),
							 sql,
							 values,
							 attribute2dbField,
							 GridPurchaseDocRowVO.class,
							 "Y",
							 "N",
							 null,
							 new GridParams(),
							 true
					 );

					 if (res.isError())
						 throw new Exception(res.getErrorMessage());

					 rows = ( (VOListResponse) res).getRows();
					 if (rows.size() == 1) {
							GridPurchaseDocRowVO itemVO = (GridPurchaseDocRowVO)rows.get(0);

							//vo.setInQtyDOC07();
							//vo.setDecimalsREG02();
							vo.setItemCodeItm01DOC09(itemVO.getItemCodeItm01DOC07());
							vo.setDescriptionSYS10(itemVO.getDescriptionSYS10());
							vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(itemVO.getSerialNumberRequiredITM01())));
							vo.setProgressiveHie02DOC09(itemVO.getProgressiveHie02DOC07()); // item's LOB
							//vo.setQtyDOC07();
							//vo.setSupplierItemCodePur02DOC09();
							//vo.setSupplierQtyDecimalsREG02();
							//vo.setUmCodeREG02();
							//vo.setUmCodeReg02PUR02();
							//vo.setValueREG05();
							vo.setVariantCodeItm11DOC09(itemVO.getVariantCodeItm11DOC07());
							vo.setVariantCodeItm12DOC09(itemVO.getVariantCodeItm12DOC07());
							vo.setVariantCodeItm13DOC09(itemVO.getVariantCodeItm13DOC07());
							vo.setVariantCodeItm14DOC09(itemVO.getVariantCodeItm14DOC07());
							vo.setVariantCodeItm15DOC09(itemVO.getVariantCodeItm15DOC07());
							vo.setVariantTypeItm06DOC09(itemVO.getVariantTypeItm06DOC07());
							vo.setVariantTypeItm07DOC09(itemVO.getVariantTypeItm07DOC07());
							vo.setVariantTypeItm08DOC09(itemVO.getVariantTypeItm08DOC07());
							vo.setVariantTypeItm09DOC09(itemVO.getVariantTypeItm09DOC07());
							vo.setVariantTypeItm10DOC09(itemVO.getVariantTypeItm10DOC07());

							itemFound = true;
					 }

					 if (!itemFound) {
						 // no item found!
						 return null;
					 }
				 }

				 String descr = vo.getDescriptionSYS10();

				 // check supported variants for current item...
				 if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC09())) {
					 descr += " "+getVariantCodeAndTypeDesc(
						 variant1Descriptions,
						 vo,
						 vo.getVariantTypeItm06DOC09(),
						 vo.getVariantCodeItm11DOC09(),
						 serverLanguageId,
						 username
					 );
				 }
				 if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC09())) {
					 descr += " "+getVariantCodeAndTypeDesc(
						 variant2Descriptions,
						 vo,
						 vo.getVariantTypeItm07DOC09(),
						 vo.getVariantCodeItm12DOC09(),
						 serverLanguageId,
						 username
					 );
				 }
				 if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC09())) {
					 descr += " "+getVariantCodeAndTypeDesc(
						 variant3Descriptions,
						 vo,
						 vo.getVariantTypeItm08DOC09(),
						 vo.getVariantCodeItm13DOC09(),
						 serverLanguageId,
						 username
					 );
				 }
				 if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC09())) {
					 descr += " "+getVariantCodeAndTypeDesc(
						 variant4Descriptions,
						 vo,
						 vo.getVariantTypeItm09DOC09(),
						 vo.getVariantCodeItm14DOC09(),
						 serverLanguageId,
						 username
					 );
				 }
				 if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC09())) {
					 descr += " "+getVariantCodeAndTypeDesc(
						 variant5Descriptions,
						 vo,
						 vo.getVariantTypeItm10DOC09(),
						 vo.getVariantCodeItm15DOC09(),
						 serverLanguageId,
						 username
					 );
				 }
				 vo.setDescriptionSYS10(descr);


				 // retrieve qty conversion and decimals...
				 String sql =
									 "select "+
									 " REG02_ALIAS1.DECIMALS,"+ // supplier decs
									 " PUR02_SUPPLIER_ITEMS.UM_CODE_REG02, "+ // supplier m.u.
									 " REG02_ALIAS2.DECIMALS, "+ // sale decs
									 " ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+ // sale m.u.
									 "from PUR02_SUPPLIER_ITEMS,ITM01_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2 where "+
									 "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
									 "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
									 "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
									 "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 = ? and "+
									 "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=? and "+
									 "PUR02_SUPPLIER_ITEMS.ENABLED='Y' and "+
									 "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=? and "+
									 "ITM01_ITEMS.MIN_SELlING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE ";
				 pstmt =  conn.prepareStatement(sql);
				 pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
				 pstmt.setBigDecimal(2,progressiveREG04);
				 pstmt.setString(3,vo.getItemCodeItm01DOC09());
				 ResultSet rset = pstmt.executeQuery();
				 if (rset.next()) {
					 vo.setSupplierQtyDecimalsREG02(rset.getBigDecimal(1));
					 vo.setDecimalsREG02(rset.getBigDecimal(3));
					 vo.setUmCodeReg02PUR02(rset.getString(2));
					 vo.setValueREG05(convBean.getConversion(
						 rset.getString(4),
						 vo.getUmCodeReg02PUR02(),
						 serverLanguageId,
						 username
					 ));
				 }
				 rset.close();
				 pstmt.close();

				 return vo;
			 }
			 catch (Throwable ex) {
				 Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating code for an in delivery note row",ex);
				 throw new Exception(ex.getMessage());
			 }
			 finally {
				 try {
					 pstmt.close();
				 }
				 catch (Exception ex2) {
				 }

				 try {
					 convBean.setConn(null);
				 } catch (Exception ex) {}

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





	private String getVariantCodeAndTypeDesc(
			HashMap variantDescriptions,
			GridInDeliveryNoteRowVO vo,
			String varType,
			String varCode,
			String serverLanguageId,
			String username
	) throws Throwable {
		String varDescr = (String)variantDescriptions.get(varType+"_"+varCode);
		if (varDescr==null)
			varDescr = ApplicationConsts.JOLLY.equals(varCode)?"":varCode;
		return varDescr;
	}




  /**
   * Business logic to execute.
   */
  public VOResponse insertInDeliveryNoteRow(GridInDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);
      vo.setInvoiceQtyDOC09(new BigDecimal(0));

      // check if note in DOC08 must be updated with delivery note defined in DOC01/DOC06...

      String note = "";
      pstmt = conn.prepareStatement(
        "select NOTE FROM DOC06_PURCHASE WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? AND "+
        "NOT EXISTS(SELECT * FROM DOC09_IN_DELIVERY_NOTE_ITEMS WHERE "+
        "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? AND "+
        "DOC_TYPE_DOC06=? and DOC_YEAR_DOC06=? and DOC_NUMBER_DOC06=? )"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(2,vo.getDocTypeDoc06DOC09());
      pstmt.setBigDecimal(3,vo.getDocYearDoc06DOC09());
      pstmt.setBigDecimal(4,vo.getDocNumberDoc06DOC09());
      pstmt.setString(5,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(6,vo.getDocTypeDOC09());
      pstmt.setBigDecimal(7,vo.getDocYearDOC09());
      pstmt.setBigDecimal(8,vo.getDocNumberDOC09());
      pstmt.setString(9,vo.getDocTypeDoc06DOC09());
      pstmt.setBigDecimal(10,vo.getDocYearDoc06DOC09());
      pstmt.setBigDecimal(11,vo.getDocNumberDoc06DOC09());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        note = rset.getString(1);
      }
      rset.close();
      pstmt.close();
      if (note!=null && !note.equals("")) {
        // retrieve current note...
        pstmt = conn.prepareStatement(
          "SELECT NOTE FROM DOC08_DELIVERY_NOTES "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,vo.getCompanyCodeSys01DOC09());
        pstmt.setString(2,vo.getDocTypeDOC09());
        pstmt.setBigDecimal(3,vo.getDocYearDOC09());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC09());
        rset = pstmt.executeQuery();
        String currentNote = null;
        if (rset.next()) {
          currentNote = rset.getString(1);
        }
        rset.close();
        pstmt.close();
        if (currentNote==null)
          currentNote = note;
        else
          currentNote = currentNote+"\n"+note;

        // update note...
        pstmt = conn.prepareStatement(
          "UPDATE DOC08_DELIVERY_NOTES SET NOTE=? "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,currentNote);
        pstmt.setString(2,vo.getCompanyCodeSys01DOC09());
        pstmt.setString(3,vo.getDocTypeDOC09());
        pstmt.setBigDecimal(4,vo.getDocYearDOC09());
        pstmt.setBigDecimal(5,vo.getDocNumberDOC09());
        pstmt.execute();
        pstmt.close();

      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC09","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC09","DOC_TYPE");
      attribute2dbField.put("docYearDOC09","DOC_YEAR");
      attribute2dbField.put("docNumberDOC09","DOC_NUMBER");
      attribute2dbField.put("docTypeDoc06DOC09","DOC_TYPE_DOC06");
      attribute2dbField.put("docYearDoc06DOC09","DOC_YEAR_DOC06");
      attribute2dbField.put("docNumberDoc06DOC09","DOC_NUMBER_DOC06");
      attribute2dbField.put("rowNumberDOC09","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC09","ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC09","QTY");
      attribute2dbField.put("supplierQtyDOC09","SUPPLIER_QTY");
      attribute2dbField.put("progressiveHie02DOC09","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01DOC09","PROGRESSIVE_HIE01");
      attribute2dbField.put("docSequenceDoc06DOC09","DOC_SEQUENCE_DOC06");
      attribute2dbField.put("invoiceQtyDOC09","INVOICE_QTY");

      attribute2dbField.put("progressiveDOC09","PROGRESSIVE");
      attribute2dbField.put("variantTypeItm06DOC09","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC09","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC09","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC09","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC09","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC09","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC09","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC09","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC09","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC09","VARIANT_CODE_ITM15");

      vo.setProgressiveDOC09( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC09(),"DOC09_IN_DELIVERY_NOTE_ITEMS","PROGRESSIVE",conn) );
      vo.setRowNumberDOC09( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC09(),"DOC09_IN_DELIVERY_NOTE_ITEMS","ROW_NUMBER",conn) );
/*
      vo.setVariantCodeItm11DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm06DOC09(ApplicationConsts.JOLLY);

      vo.setVariantCodeItm12DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm13DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm14DOC09(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm15DOC09(ApplicationConsts.JOLLY);

      vo.setVariantTypeItm07DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm08DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm09DOC09(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm10DOC09(ApplicationConsts.JOLLY);
*/
      // insert into DOC09...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC09_IN_DELIVERY_NOTE_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // update delivery note state...
      pstmt = conn.prepareStatement("update DOC08_DELIVERY_NOTES set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC09());
      pstmt.setString(3,vo.getDocTypeDOC09());
      pstmt.setBigDecimal(4,vo.getDocYearDOC09());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC09());
      pstmt.execute();

      // insert serial numbers...
      if (vo.getSerialNumbers().size()>0) {
        res = bean.reinsertInSerialNumbers(vo,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new in delivery note row",ex);
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
    	  bean.setConn(null);
      } catch (Exception e) {
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
   * Business logic to execute.
   */
  public VOResponse loadInDeliveryNote(DeliveryNotePK pk,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC08_DELIVERY_NOTES.DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC08_DELIVERY_NOTES.DOC_REF");
      attribute2dbField.put("warehouseDescriptionDOC08","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("addressDOC08","DOC08_DELIVERY_NOTES.ADDRESS");
      attribute2dbField.put("cityDOC08","DOC08_DELIVERY_NOTES.CITY");
      attribute2dbField.put("provinceDOC08","DOC08_DELIVERY_NOTES.PROVINCE");
      attribute2dbField.put("countryDOC08","DOC08_DELIVERY_NOTES.COUNTRY");
      attribute2dbField.put("zipDOC08","DOC08_DELIVERY_NOTES.ZIP");
      attribute2dbField.put("noteDOC08","DOC08_DELIVERY_NOTES.NOTE");
      attribute2dbField.put("enabledDOC08","DOC08_DELIVERY_NOTES.ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09");
      attribute2dbField.put("carrierDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");
      attribute2dbField.put("deliveryDateDOC08","DOC08_DELIVERY_NOTES.DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20");
      attribute2dbField.put("transportMotiveDescriptionSYS10","SYS10_TRANSLATIONS_B.DESCRIPTION");


      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC08");
      pkAttributes.add("docTypeDOC08");
      pkAttributes.add("docYearDOC08");
      pkAttributes.add("docNumberDOC08");

      String baseSQL =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.ADDRESS,DOC08_DELIVERY_NOTES.CITY,DOC08_DELIVERY_NOTES.PROVINCE,DOC08_DELIVERY_NOTES.COUNTRY,DOC08_DELIVERY_NOTES.ZIP,"+
          "DOC08_DELIVERY_NOTES.NOTE,DOC08_DELIVERY_NOTES.ENABLED,DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,PUR01_SUPPLIERS.SUPPLIER_CODE,REG04_SUBJECTS.NAME_1,WAR01_WAREHOUSES.PROGRESSIVE_HIE02, "+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE,DOC08_DELIVERY_NOTES.DELIVERY_DATE,DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20,SYS10_TRANSLATIONS_B.DESCRIPTION "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,REG09_CARRIERS,SYS10_TRANSLATIONS,PUR01_SUPPLIERS,REG04_SUBJECTS,"+
          "REG20_TRANSPORT_MOTIVES,SYS10_TRANSLATIONS SYS10_TRANSLATIONS_B where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09=REG09_CARRIERS.CARRIER_CODE and "+
          "REG09_CARRIERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=? and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
          "DOC08_DELIVERY_NOTES.DOC_YEAR=? and "+
          "DOC08_DELIVERY_NOTES.DOC_NUMBER=? and "+
          "DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20=REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE and "+
          "REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS_B.PROGRESSIVE and SYS10_TRANSLATIONS_B.LANGUAGE_CODE=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC08());
      values.add(pk.getDocTypeDOC08());
      values.add(pk.getDocYearDOC08());
      values.add(pk.getDocNumberDOC08());
      values.add(serverLanguageId);

      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailDeliveryNoteVO.class,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing in delivery note",ex);
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
  public VOResponse deleteInDeliveryNoteRows(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      InDeliveryNoteRowPK rowPK = null;

      pstmt = conn.prepareStatement(
        "delete from DOC09_IN_DELIVERY_NOTE_ITEMS where PROGRESSIVE=?"
      );

      pstmt2 = conn.prepareStatement(
       "delete from DOC11_IN_SERIAL_NUMBERS where PROGRESSIVE_DOC09=? "
      );

      Response res = null;
      for(int i=0;i<list.size();i++) {
        rowPK = (InDeliveryNoteRowPK)list.get(i);

        // delete previous serial numbers from DOC11...
        pstmt2.setBigDecimal(1,rowPK.getProgressiveDOC09());
        pstmt2.execute();

        // phisically delete the record in DOC09...
        pstmt.setBigDecimal(1,rowPK.getProgressiveDOC09());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing in delivery note rows",ex);
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



}

