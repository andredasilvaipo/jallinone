package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;

import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.hierarchies.java.*;
import org.jallinone.purchases.items.java.*;
import org.jallinone.registers.measure.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.purchases.items.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage ABC classification.</p>
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
public class ABCServiceBean  implements ABCService {


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


  private DeleteABCBean bean;

  public void setBean(DeleteABCBean bean) {
	  this.bean = bean;
  }


  public ABCServiceBean() {
  }



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public ABCVO getABC() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadABC(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		GridParams gridParams, String serverLanguageId, String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ArrayList mask = (ArrayList)gridParams.getOtherGridParams().get(ApplicationConsts.FILTER_VO);
      BigDecimal reportId = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.REPORT_ID);

      if (mask.size()==0 || reportId==null)
        return new VOListResponse(new ArrayList(),false,0);


      // load from TMP03...
      String sql =
        "SELECT T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
        "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
        "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
        "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
        "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
        "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
        "T.DESCRIPTION,"+
        "T1.QTY,T2.QTY,"+
        "T1.UNSOLD_GRADE,T2.INVOICED_GRADE,ITM23.MIN_STOCK "+
        "FROM SYS10_TRANSLATIONS T,ITM01_ITEMS I,TMP03_ABC T1 LEFT OUTER JOIN ("+
        " SELECT QTY,INVOICED_GRADE,REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
        " VARIANT_TYPE_ITM06,"+
        " VARIANT_CODE_ITM11,"+
        " VARIANT_TYPE_ITM07,"+
        " VARIANT_CODE_ITM12,"+
        " VARIANT_TYPE_ITM08,"+
        " VARIANT_CODE_ITM13,"+
        " VARIANT_TYPE_ITM09,"+
        " VARIANT_CODE_ITM14,"+
        " VARIANT_TYPE_ITM10,"+
        " VARIANT_CODE_ITM15 "+
        " FROM TMP03_ABC WHERE TMP03_ABC.QTY_TYPE=? "+
        ") T2 ON "+
        "T1.REPORT_ID=T2.REPORT_ID AND "+
        "T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
        "T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
        "T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
        "T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
        "T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
        "T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
        "T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
        "T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
        "T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
        "T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
        "T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
        "T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 "+
        "LEFT OUTER JOIN ITM23_VARIANT_MIN_STOCKS ITM23 ON "+
        " T1.COMPANY_CODE_SYS01=ITM23.COMPANY_CODE_SYS01 AND "+
        " T1.ITEM_CODE_ITM01=ITM23.ITEM_CODE_ITM01 AND "+
        " T1.VARIANT_TYPE_ITM06=ITM23.VARIANT_TYPE_ITM06 AND "+
        " T1.VARIANT_CODE_ITM11=ITM23.VARIANT_CODE_ITM11 AND "+
        " T1.VARIANT_TYPE_ITM07=ITM23.VARIANT_TYPE_ITM07 AND "+
        " T1.VARIANT_CODE_ITM12=ITM23.VARIANT_CODE_ITM12 AND "+
        " T1.VARIANT_TYPE_ITM08=ITM23.VARIANT_TYPE_ITM08 AND "+
        " T1.VARIANT_CODE_ITM13=ITM23.VARIANT_CODE_ITM13 AND "+
        " T1.VARIANT_TYPE_ITM09=ITM23.VARIANT_TYPE_ITM09 AND "+
        " T1.VARIANT_CODE_ITM14=ITM23.VARIANT_CODE_ITM14 AND "+
        " T1.VARIANT_TYPE_ITM10=ITM23.VARIANT_TYPE_ITM10 AND "+
        " T1.VARIANT_CODE_ITM15=ITM23.VARIANT_CODE_ITM15 "+
        "WHERE T1.REPORT_ID=? AND "+
        "T1.QTY_TYPE=? AND "+
        "T1.COMPANY_CODE_SYS01=I.COMPANY_CODE_SYS01 AND "+
        "T1.ITEM_CODE_ITM01=I.ITEM_CODE AND "+
        "I.PROGRESSIVE_SYS10=T.PROGRESSIVE AND "+
        "T.LANGUAGE_CODE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01","T1.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCode","T1.ITEM_CODE_ITM01");
      attribute2dbField.put("itemDescription","T.DESCRIPTION");
//      attribute2dbField.put("minStockITM23","SYS23_VARIANT_MIN_STOCKS.MIN_STOCK");
      attribute2dbField.put("unsold","T1.QTY");
      attribute2dbField.put("sold","T2.QTY");
      attribute2dbField.put("unsoldGrade","T1.UNSOLD_GRADE");
      attribute2dbField.put("invoicedGrade","T2.INVOICED_GRADE");

      attribute2dbField.put("variantTypeITM06","T1.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeITM11","T1.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeITM07","T1.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeITM12","T1.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeITM08","T1.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeITM13","T1.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeITM09","T1.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeITM14","T1.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeITM10","T1.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeITM15","T1.VARIANT_CODE_ITM15");

      attribute2dbField.put("minStockITM23","ITM23.MIN_STOCK");

      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.ABC_TYPE_SOLD_QTY);
      values.add(reportId);
      values.add(ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      values.add(serverLanguageId);

      if (mask.size()<9) {
        sql += " AND (";

        String pattern = null;
        for(int i=0;i<mask.size();i++) {
          pattern = mask.get(i).toString();
          if (pattern.substring(1).equals("C"))
            sql += "T1.UNSOLD_GRADE=? AND (T2.INVOICED_GRADE=? OR T2.INVOICED_GRADE is null) OR ";
          else
            sql += "T1.UNSOLD_GRADE=? AND T2.INVOICED_GRADE=? OR ";

          values.add(pattern.substring(0,1));
          values.add(pattern.substring(1));
        }
        sql = sql.substring(0,sql.length()-3)+")";
      }

      // read from PUR02 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ABCVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (res.isError())
    	  throw new Exception(res.getErrorMessage());

			ABCVO vo = null;
			List rows = ((VOListResponse)res).getRows();
			String descr = null;
			for(int i=0;i<rows.size();i++) {
				vo = (ABCVO)rows.get(i);
				descr = vo.getItemDescription();

				// check supported variants for current item...
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM11())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant1Descriptions,
						vo,
						vo.getVariantTypeITM06(),
						vo.getVariantCodeITM11(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM12())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant2Descriptions,
						vo,
						vo.getVariantTypeITM07(),
						vo.getVariantCodeITM12(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM13())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant3Descriptions,
						vo,
						vo.getVariantTypeITM08(),
						vo.getVariantCodeITM13(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM14())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant4Descriptions,
						vo,
						vo.getVariantTypeITM09(),
						vo.getVariantCodeITM14(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM15())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant5Descriptions,
						vo,
						vo.getVariantTypeITM10(),
						vo.getVariantCodeITM15(),
						serverLanguageId,
						username
					);
				}
				vo.setItemDescription(descr);

			} // end for on rows...


   	  return (VOListResponse)res;
    }
    catch (Throwable ex) {
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while loading ABC classification",ex);
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
    }


  }





  /**
   * Business logic to execute.
   */
  public VOResponse createABC(CreateABCFilterVO filterVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);


      if (filterVO.getReportId()!=null)
        bean.deleteABC(filterVO.getReportId(),serverLanguageId,username);

      BigDecimal reportId = CompanyProgressiveUtils.getInternalProgressive(filterVO.getCompanyCode(),"TMP03_ABC","REPORT_ID",conn);
      filterVO.setReportId(reportId);


      // insert into TMP03 the good qtys...
      String sql =
        "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
        "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
        "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
        "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
        "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
        "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
        "QTY_TYPE,QTY) "+
        "SELECT "+reportId+",WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15,"+
        "'"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"',"+
        "SUM(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY) "+
        "FROM WAR03_ITEMS_AVAILABILITY,ITM01_ITEMS WHERE "+
        "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? AND "+
        "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? AND "+
        "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
        "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
        "ITM01_ITEMS.PROGRESSIVE_HIE02=? "+
        "GROUP BY WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
        "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setString(2,filterVO.getWarehouseCode());
      pstmt.setBigDecimal(3,filterVO.getProgressiveHie02ITM01());
      pstmt.execute();
      pstmt.close();


      // insert into TMP03 the sold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_SOLD_QTY+"',"+
       "SUM(DOC02_SELLING_ITEMS.VALUE) "+
       "FROM DOC02_SELLING_ITEMS,DOC01_SELLING WHERE "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
       "DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=? AND "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
       "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
       "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
       "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
       "DOC01_SELLING.WAREHOUSE_CODE_WAR01=? AND "+
       "DOC01_SELLING.CURRENCY_CODE_REG03=? AND "+
       "DOC01_SELLING.DOC_TYPE IN (?,?,?,?) AND "+
       "DOC01_SELLING.DOC_STATE=? AND "+
       "DOC01_SELLING.DOC_DATE>=? AND "+
       "DOC01_SELLING.DOC_DATE<=? "+
       "GROUP BY DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setBigDecimal(2,filterVO.getProgressiveHie02ITM01());
      pstmt.setString(3,filterVO.getWarehouseCode());
      pstmt.setString(4,filterVO.getCurrencyCodeREG03());
      pstmt.setString(5,ApplicationConsts.SALE_INVOICE_DOC_TYPE);
      pstmt.setString(6,ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE);
      pstmt.setString(7,ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
      pstmt.setString(8,ApplicationConsts.SALE_DESK_DOC_TYPE);
      pstmt.setString(9,ApplicationConsts.CLOSED); // ???
      pstmt.setDate(10,filterVO.getStartDate());
      pstmt.setDate(11,filterVO.getEndDate());
      int rows = pstmt.executeUpdate();
      pstmt.close();


      // insert into TMP03 the pawned qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"',"+
       "SUM(DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY) "+
       "FROM DOC02_SELLING_ITEMS,DOC01_SELLING WHERE "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
       "DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=? AND "+
       "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
       "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
       "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
       "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
       "DOC01_SELLING.WAREHOUSE_CODE_WAR01=? AND "+
       "DOC01_SELLING.CURRENCY_CODE_REG03=? AND "+
       "DOC01_SELLING.DOC_TYPE IN (?,?) AND "+
       "DOC01_SELLING.DOC_STATE=? AND "+
       "DOC01_SELLING.DOC_DATE>=? AND "+
       "DOC01_SELLING.DOC_DATE<=? "+
       "GROUP BY DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
       "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,filterVO.getCompanyCode());
      pstmt.setBigDecimal(2,filterVO.getProgressiveHie02ITM01());
      pstmt.setString(3,filterVO.getWarehouseCode());
      pstmt.setString(4,filterVO.getCurrencyCodeREG03());
      pstmt.setString(5,ApplicationConsts.SALE_ORDER_DOC_TYPE);
      pstmt.setString(6,ApplicationConsts.SALE_CONTRACT_DOC_TYPE);
      pstmt.setString(7,ApplicationConsts.CONFIRMED);
      pstmt.setDate(8,filterVO.getStartDate());
      pstmt.setDate(9,filterVO.getEndDate());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // CASE A: insert into TMP03 the unsold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
       "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
       "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
       "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
       "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
       "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"',"+
       "T1.QTY-T2.QTY "+
       "FROM TMP03_ABC T1,TMP03_ABC T2 WHERE "+
       "T1.REPORT_ID=? AND "+
       "T2.REPORT_ID=? AND "+
       "T1.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"' AND "+
       "T2.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"' AND "+
       "T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
       "T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
       "T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
       "T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
       "T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
       "T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
       "T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
       "T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
       "T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
       "T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
       "T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
       "T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 AND "+
       "T1.QTY-T2.QTY>0 ";

      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setBigDecimal(2,filterVO.getReportId());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // CASE B: insert into TMP03 the unsold qtys...
      sql =
       "INSERT INTO TMP03_ABC(REPORT_ID,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
       "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
       "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
       "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
       "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
       "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
       "QTY_TYPE,QTY) "+
       "SELECT "+reportId+",T1.COMPANY_CODE_SYS01,T1.ITEM_CODE_ITM01,"+
       "T1.VARIANT_TYPE_ITM06,T1.VARIANT_CODE_ITM11,"+
       "T1.VARIANT_TYPE_ITM07,T1.VARIANT_CODE_ITM12,"+
       "T1.VARIANT_TYPE_ITM08,T1.VARIANT_CODE_ITM13,"+
       "T1.VARIANT_TYPE_ITM09,T1.VARIANT_CODE_ITM14,"+
       "T1.VARIANT_TYPE_ITM10,T1.VARIANT_CODE_ITM15,"+
       "'"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"',"+
       "T1.QTY "+
       "FROM TMP03_ABC T1 WHERE "+
       "T1.REPORT_ID=? AND "+
       "T1.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_GOOD_QTY+"' AND "+
       "NOT EXISTS(SELECT * FROM TMP03_ABC T2 WHERE "+
       " T2.REPORT_ID=? AND "+
       " T2.QTY_TYPE='"+ApplicationConsts.ABC_TYPE_PAWNED_QTY+"' AND "+
       " T1.COMPANY_CODE_SYS01=T2.COMPANY_CODE_SYS01 AND "+
       " T1.ITEM_CODE_ITM01=T2.ITEM_CODE_ITM01 AND "+
       " T1.VARIANT_TYPE_ITM06=T2.VARIANT_TYPE_ITM06 AND "+
       " T1.VARIANT_CODE_ITM11=T2.VARIANT_CODE_ITM11 AND "+
       " T1.VARIANT_TYPE_ITM07=T2.VARIANT_TYPE_ITM07 AND "+
       " T1.VARIANT_CODE_ITM12=T2.VARIANT_CODE_ITM12 AND "+
       " T1.VARIANT_TYPE_ITM08=T2.VARIANT_TYPE_ITM08 AND "+
       " T1.VARIANT_CODE_ITM13=T2.VARIANT_CODE_ITM13 AND "+
       " T1.VARIANT_TYPE_ITM09=T2.VARIANT_TYPE_ITM09 AND "+
       " T1.VARIANT_CODE_ITM14=T2.VARIANT_CODE_ITM14 AND "+
       " T1.VARIANT_TYPE_ITM10=T2.VARIANT_TYPE_ITM10 AND "+
       " T1.VARIANT_CODE_ITM15=T2.VARIANT_CODE_ITM15 "+
       ")";

      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setBigDecimal(2,filterVO.getReportId());
      rows = pstmt.executeUpdate();
      pstmt.close();


      // retrieve total unsold...
      sql = "SELECT SUM(QTY) FROM TMP03_ABC WHERE REPORT_ID=? AND QTY_TYPE='"+ApplicationConsts.ABC_TYPE_UNSOLD_QTY+"'";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      rset = pstmt.executeQuery();
      BigDecimal totalUnsold = new BigDecimal(0);
      if (rset.next())
        totalUnsold = rset.getBigDecimal(1);
      rset.close();
      pstmt.close();


      // retrieve total invoiced...
      sql = "SELECT SUM(QTY) FROM TMP03_ABC WHERE REPORT_ID=? AND QTY_TYPE=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      rset = pstmt.executeQuery();
      BigDecimal totalSold = new BigDecimal(0);
      if (rset.next())
        totalSold = rset.getBigDecimal(1);
      rset.close();
      pstmt.close();


      // grade unsold items...
      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='A' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?>? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.setBigDecimal(3,totalUnsold);
      pstmt.setBigDecimal(4,filterVO.getPerc1Unsold().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='C' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?<? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.setBigDecimal(3,totalUnsold);
      pstmt.setBigDecimal(4,filterVO.getPerc2Unsold().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET UNSOLD_GRADE='B' WHERE REPORT_ID=? AND QTY_TYPE=? AND UNSOLD_GRADE IS NULL ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_UNSOLD_QTY);
      pstmt.execute();
      pstmt.close();


      // grade invoiced items...
      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='A' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?>? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.setBigDecimal(3,totalSold);
      pstmt.setBigDecimal(4,filterVO.getPerc1Invoiced().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='C' WHERE REPORT_ID=? AND QTY_TYPE=? AND QTY/?<? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.setBigDecimal(3,totalSold);
      pstmt.setBigDecimal(4,filterVO.getPerc2Invoiced().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP));
      pstmt.execute();
      pstmt.close();

      sql = "UPDATE TMP03_ABC SET INVOICED_GRADE='B' WHERE REPORT_ID=? AND QTY_TYPE=? AND INVOICED_GRADE IS NULL ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setBigDecimal(1,filterVO.getReportId());
      pstmt.setString(2,ApplicationConsts.ABC_TYPE_SOLD_QTY);
      pstmt.execute();
      pstmt.close();

      return new VOResponse(reportId);
    }
    catch (Throwable ex) {
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating ABC classification",ex);
    	throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        rset.close();
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



		private String getVariantCodeAndTypeDesc(
				HashMap variantDescriptions,
				ABCVO vo,
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

