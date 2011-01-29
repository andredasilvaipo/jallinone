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
public class WarehouseUtilsBean implements WarehouseUtils {



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



	private MeasuresBean convBean;

	public void setConvBean(MeasuresBean convBean) {
		this.convBean = convBean;
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DeliveryNotePK getDeliveryNote() {
		throw new UnsupportedOperationException();
	}



	  /**
	   * Business logic to execute.
	   */
	  public VOListResponse loadInDeliveryNoteRows(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams pars, String serverLanguageId, String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      convBean.setConn(conn); // use same transaction...
	      DeliveryNotePK pk = (DeliveryNotePK)pars.getOtherGridParams().get(ApplicationConsts.DELIVERY_NOTE_PK);

	      String sql =
	          "select DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE,DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06,DOC09_IN_DELIVERY_NOTE_ITEMS.ROW_NUMBER,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01,PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE,SYS10_TRANSLATIONS.DESCRIPTION,DOC09_IN_DELIVERY_NOTE_ITEMS.QTY,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.SUPPLIER_QTY,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,REG02_ALIAS2.DECIMALS,PUR02_SUPPLIER_ITEMS.UM_CODE_REG02,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02,DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01,SYS10_LOC.DESCRIPTION,DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED, "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC06,DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY, "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06,DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07,DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08,DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09,DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14,"+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10,DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15 "+
	          " from DOC08_DELIVERY_NOTES,DOC09_IN_DELIVERY_NOTE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,PUR02_SUPPLIER_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2,SYS10_TRANSLATIONS SYS10_LOC where "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
	          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
	          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
	          "SYS10_LOC.LANGUAGE_CODE=? and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE=DOC08_DELIVERY_NOTES.DOC_TYPE and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR=DOC08_DELIVERY_NOTES.DOC_YEAR and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER=DOC08_DELIVERY_NOTES.DOC_NUMBER and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 and "+
	          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04 and "+
	          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01 and "+
	          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
	          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE=? and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR=? and "+
	          "DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER=? ";

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("progressiveDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE");
	      attribute2dbField.put("companyCodeSys01DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01");
	      attribute2dbField.put("docTypeDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE");
	      attribute2dbField.put("docYearDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR");
	      attribute2dbField.put("docNumberDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER");
	      attribute2dbField.put("docTypeDoc06DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06");
	      attribute2dbField.put("docYearDoc06DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06");
	      attribute2dbField.put("docNumberDoc06DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06");
	      attribute2dbField.put("rowNumberDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.ROW_NUMBER");
	      attribute2dbField.put("itemCodeItm01DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01");
	      attribute2dbField.put("supplierItemCodePur02DOC09","PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE");
	      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
	      attribute2dbField.put("qtyDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.QTY");
	      attribute2dbField.put("supplierQtyDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.SUPPLIER_QTY");
	      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
	      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
	      attribute2dbField.put("supplierQtyDecimalsREG02","REG02_ALIAS2.DECIMALS");
	      attribute2dbField.put("umCodeReg02PUR02","PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
	      attribute2dbField.put("progressiveHie02DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02");
	      attribute2dbField.put("progressiveHie01DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01");
	      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
	      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
	      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
	      attribute2dbField.put("docSequenceDoc06DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC06");
	      attribute2dbField.put("invoiceQtyDOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY");

	      attribute2dbField.put("variantTypeItm06DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06");
	      attribute2dbField.put("variantCodeItm11DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11");
	      attribute2dbField.put("variantTypeItm07DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07");
	      attribute2dbField.put("variantCodeItm12DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12");
	      attribute2dbField.put("variantTypeItm08DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08");
	      attribute2dbField.put("variantCodeItm13DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13");
	      attribute2dbField.put("variantTypeItm09DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09");
	      attribute2dbField.put("variantCodeItm14DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14");
	      attribute2dbField.put("variantTypeItm10DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10");
	      attribute2dbField.put("variantCodeItm15DOC09","DOC09_IN_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15");

	      ArrayList values = new ArrayList();
	      values.add(serverLanguageId);
	      values.add(serverLanguageId);
	      values.add(pk.getCompanyCodeSys01DOC08());
	      values.add(pk.getDocTypeDOC08());
	      values.add(pk.getDocYearDOC08());
	      values.add(pk.getDocNumberDOC08());

				int blockSize = 50;
				if (pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE)!=null)
					blockSize = Integer.parseInt(pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE).toString());

	      // read from DOC09 table...
	      Response res = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          GridInDeliveryNoteRowVO.class,
	          "Y",
	          "N",
	          null,
	          pars,
	          blockSize,
					 true
	     );
	     if (!res.isError()) {
	       ArrayList serialNums = null;
	       java.util.List rows = ((VOListResponse)res).getRows();
	       GridInDeliveryNoteRowVO vo = null;

	       pstmt = conn.prepareStatement(
	         "select SERIAL_NUMBER "+
	         "from DOC11_IN_SERIAL_NUMBERS "+
	         "where "+
	         "PROGRESSIVE_DOC09=? "
	        );

	        String descr = null;
	        for(int i=0;i<rows.size();i++) {
	          vo = (GridInDeliveryNoteRowVO)rows.get(i);

						descr = vo.getDescriptionSYS10();

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




	          // retrieve and set m.u. conversion factor...
	          vo.setValueREG05(convBean.getConversion(
	            vo.getUmCodeREG02(),
	            vo.getUmCodeReg02PUR02(),
	            serverLanguageId,
	            username
	          ));

	          // retrieve serial numbers...
	          serialNums = new ArrayList();
	          vo.setSerialNumbers(serialNums);
	          pstmt.setBigDecimal(1,vo.getProgressiveDOC09());

	          ResultSet rset = null;
	          try {
	            rset = pstmt.executeQuery();
	            while(rset.next()) {
	              serialNums.add(rset.getString(1));
	            }
	          }
	          catch (Exception ex3) {
	            throw ex3;
	          }
	          finally {
	            rset.close();
	         }


	        }
	     }

	     Response answer = res;
	     if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching in delivery note rows list",ex);
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
	         convBean.setConn(null);
	      } catch (Exception exx) {}
	    }

	  }





	  /**
	   * Delete and re-insert serial numbers in DOC11 table.
	   * It does not commit or rollback the connection.
	   */
	  public VOResponse reinsertInSerialNumbers(GridInDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

	      ArrayList serialNums = vo.getSerialNumbers();

	      // delete previous serial numbers from DOC11...
	      pstmt = conn.prepareStatement(
	       "delete from DOC11_IN_SERIAL_NUMBERS where PROGRESSIVE_DOC09=?  "
	      );

	      pstmt.setBigDecimal(1,vo.getProgressiveDOC09());
	      pstmt.execute();
	      pstmt.close();

	      pstmt = conn.prepareStatement(
	        "insert into DOC11_IN_SERIAL_NUMBERS(PROGRESSIVE_DOC09,SERIAL_NUMBER) values(?,?)"
	      );

	      for(int i=0;i<serialNums.size();i++) {
	         pstmt.setBigDecimal(1,vo.getProgressiveDOC09());
	         pstmt.setString(2,(String)serialNums.get(i));
	         pstmt.execute();
	      }

	      return  new VOResponse(Boolean.TRUE);
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"reinsertInSerialNumbers","Error while creating serial numbers in DOC11 table.",ex);
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
	  public VOListResponse loadOutDeliveryNoteRows(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams pars, String serverLanguageId, String username) throws Throwable {

	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

	      DeliveryNotePK pk = (DeliveryNotePK)pars.getOtherGridParams().get(ApplicationConsts.DELIVERY_NOTE_PK);

	      String sql =
	          "select DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE,DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.ROW_NUMBER,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY,"+
	          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01,SYS10_LOC.DESCRIPTION,DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED, "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC01,DOC10_OUT_DELIVERY_NOTE_ITEMS.INVOICE_QTY, "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14,"+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10,DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15 "+
	          " from DOC08_DELIVERY_NOTES,DOC10_OUT_DELIVERY_NOTE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,REG02_MEASURE_UNITS REG02_ALIAS1,SYS10_TRANSLATIONS SYS10_LOC where "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
	          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
	          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
	          "SYS10_LOC.LANGUAGE_CODE=? and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=DOC08_DELIVERY_NOTES.DOC_TYPE and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=DOC08_DELIVERY_NOTES.DOC_YEAR and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=DOC08_DELIVERY_NOTES.DOC_NUMBER and "+
	          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=? and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=? and "+
	          "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=? ";

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("progressiveDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE");
	      attribute2dbField.put("companyCodeSys01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01");
	      attribute2dbField.put("docTypeDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE");
	      attribute2dbField.put("docYearDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR");
	      attribute2dbField.put("docNumberDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER");
	      attribute2dbField.put("docTypeDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01");
	      attribute2dbField.put("docYearDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01");
	      attribute2dbField.put("docNumberDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01");
	      attribute2dbField.put("rowNumberDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.ROW_NUMBER");
	      attribute2dbField.put("itemCodeItm01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01");
	      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
	      attribute2dbField.put("qtyDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY");
	      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
	      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
	      attribute2dbField.put("progressiveHie02DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE02");
	      attribute2dbField.put("progressiveHie01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.PROGRESSIVE_HIE01");
	      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
	      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
	      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
	      attribute2dbField.put("docSequenceDoc01DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_SEQUENCE_DOC01");
	      attribute2dbField.put("invoiceQtyDOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.INVOICE_QTY");

	      attribute2dbField.put("variantTypeItm06DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM06");
	      attribute2dbField.put("variantCodeItm11DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM11");
	      attribute2dbField.put("variantTypeItm07DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM07");
	      attribute2dbField.put("variantCodeItm12DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM12");
	      attribute2dbField.put("variantTypeItm08DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM08");
	      attribute2dbField.put("variantCodeItm13DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM13");
	      attribute2dbField.put("variantTypeItm09DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM09");
	      attribute2dbField.put("variantCodeItm14DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM14");
	      attribute2dbField.put("variantTypeItm10DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_TYPE_ITM10");
	      attribute2dbField.put("variantCodeItm15DOC10","DOC10_OUT_DELIVERY_NOTE_ITEMS.VARIANT_CODE_ITM15");

	      ArrayList values = new ArrayList();
	      values.add(serverLanguageId);
	      values.add(serverLanguageId);
	      values.add(pk.getCompanyCodeSys01DOC08());
	      values.add(pk.getDocTypeDOC08());
	      values.add(pk.getDocYearDOC08());
	      values.add(pk.getDocNumberDOC08());

				int blockSize = 50;
				if (pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE)!=null)
					blockSize = Integer.parseInt(pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE).toString());

	      // read from DOC10 table...
	      Response res = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          GridOutDeliveryNoteRowVO.class,
	          "Y",
	          "N",
	          null,
	          pars,
						blockSize,
	          true
	     );



	     if (!res.isError()) {
	       ArrayList serialNums = null;
	       java.util.List rows = ((VOListResponse)res).getRows();
	       GridOutDeliveryNoteRowVO vo = null;

	       pstmt = conn.prepareStatement(
	         "select SERIAL_NUMBER from DOC12_OUT_SERIAL_NUMBERS where "+
	         "PROGRESSIVE_DOC10=? "
	        );
					String descr = null;
	        for(int i=0;i<rows.size();i++) {
	          vo = (GridOutDeliveryNoteRowVO)rows.get(i);

						descr = vo.getDescriptionSYS10();

						// check supported variants for current item...
						if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC10())) {
							descr += " "+getVariantCodeAndTypeDesc(
								variant1Descriptions,
								vo,
								vo.getVariantTypeItm06DOC10(),
								vo.getVariantCodeItm11DOC10(),
								serverLanguageId,
								username
							);
						}
						if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC10())) {
							descr += " "+getVariantCodeAndTypeDesc(
								variant2Descriptions,
								vo,
								vo.getVariantTypeItm07DOC10(),
								vo.getVariantCodeItm12DOC10(),
								serverLanguageId,
								username
							);
						}
						if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC10())) {
							descr += " "+getVariantCodeAndTypeDesc(
								variant3Descriptions,
								vo,
								vo.getVariantTypeItm08DOC10(),
								vo.getVariantCodeItm13DOC10(),
								serverLanguageId,
								username
							);
						}
						if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC10())) {
							descr += " "+getVariantCodeAndTypeDesc(
								variant4Descriptions,
								vo,
								vo.getVariantTypeItm09DOC10(),
								vo.getVariantCodeItm14DOC10(),
								serverLanguageId,
								username
							);
						}
						if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC10())) {
							descr += " "+getVariantCodeAndTypeDesc(
								variant5Descriptions,
								vo,
								vo.getVariantTypeItm10DOC10(),
								vo.getVariantCodeItm15DOC10(),
								serverLanguageId,
								username
							);
						}
						vo.setDescriptionSYS10(descr);


	          // retrieve serial numbers...
	          serialNums = new ArrayList();
	          vo.setSerialNumbers(serialNums);
	          pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
	          ResultSet rset = null;
	          try {
	            rset = pstmt.executeQuery();
	            while(rset.next()) {
	              serialNums.add(rset.getString(1));
	            }
	          }
	          catch (Exception ex3) {
	            throw ex3;
	          }
	          finally {
	            rset.close();
	         }


	        }
	     }

	     Response answer = res;
	     if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching out delivery note rows list",ex);
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
	    }
  	}




	  /**
	   * Delete and re-insert serial numbers in DOC11 table.
	   * It does not comit or rollback the connection.
	   */
	  public VOResponse reinsertOutSerialNumbers(GridOutDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable{
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      ArrayList serialNums = vo.getSerialNumbers();

	      // delete previous serial numbers from DOC11...
	      pstmt = conn.prepareStatement(
	       "delete from DOC12_OUT_SERIAL_NUMBERS where PROGRESSIVE_DOC10=?"
	      );

	      pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
	      pstmt.execute();
	      pstmt.close();

	      pstmt = conn.prepareStatement(
	        "insert into DOC12_OUT_SERIAL_NUMBERS(PROGRESSIVE_DOC10,SERIAL_NUMBER) values(?,?)"
	      );

	      for(int i=0;i<serialNums.size();i++) {
	         pstmt.setBigDecimal(1,vo.getProgressiveDOC10());
	         pstmt.setString(2,(String)serialNums.get(i));
	         pstmt.execute();
	      }

	      return new VOResponse(Boolean.TRUE);
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"reinsertOutSerialNumbers","Error while creating serial numbers in DOC12 table.",ex);
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





			private String getVariantCodeAndTypeDesc(
					HashMap variantDescriptions,
					GridOutDeliveryNoteRowVO vo,
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






}
