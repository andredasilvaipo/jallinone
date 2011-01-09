package org.jallinone.items.server;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.jallinone.commons.java.*;
import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.events.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to prepare barcode labels related to the specified set of items
 * and fill in a temporary table (TMP02_BARCODES) with that data.</p>
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
public class CreateBarcodeLabelsDataBean  implements CreateBarcodeLabelsData {


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



  private LoadItemBean bean;

  public void setBean(LoadItemBean bean) {
    this.bean = bean;
  }



  public CreateBarcodeLabelsDataBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOResponse createBarcodeLabelsData(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		ItemToPrintVO[] rows,String serverLanguageId,String username,String imagePath) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rset2 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...

      ItemToPrintVO rowVO = (ItemToPrintVO)rows[0];
      BigDecimal reportId = CompanyProgressiveUtils.getInternalProgressive(rowVO.getCompanyCodeSys01(),"TMP02_BARCODES","REPORT_ID",conn);
      pstmt = conn.prepareStatement(
        "insert into TMP02_BARCODES(REPORT_ID,PROGRESSIVE,BAR_CODE,BARCODE_TYPE,DESCRIPTION) values(?,?,?,?,?)"
      );
      pstmt2 = conn.prepareStatement(
        "select BAR_CODE from ITM22_VARIANT_BARCODES where "+
        "COMPANY_CODE_SYS01=? AND "+
        "ITEM_CODE_ITM01=? AND "+
        "VARIANT_TYPE_ITM06=? AND "+
        "VARIANT_CODE_ITM11=? AND "+
        "VARIANT_TYPE_ITM07=? AND "+
        "VARIANT_CODE_ITM12=? AND "+
        "VARIANT_TYPE_ITM08=? AND "+
        "VARIANT_CODE_ITM13=? AND "+
        "VARIANT_TYPE_ITM09=? AND "+
        "VARIANT_CODE_ITM14=? AND "+
        "VARIANT_TYPE_ITM10=? AND "+
        "VARIANT_CODE_ITM15=? "
      );

      DetailItemVO itemVO = null;
      String barcode = null;
      String barcodeType = null;
      long progressive = 0;
      Response res = null;
      String aux = null;
      ItemPK pk = null;
      BigDecimal progressiveHie02ITM01 = null;
      for(int i=0;i<rows.length;i++) {
        rowVO = rows[i];

        pk = new ItemPK(rowVO.getCompanyCodeSys01(),rowVO.getItemCodeItm01());
		progressiveHie02ITM01 = bean.getProgressiveHie02ITM01(pk, username);
        res = new VOResponse(bean.loadItem(
        		pk,
        		progressiveHie02ITM01,
        		serverLanguageId,
        		username,
        		imagePath,
        		new ArrayList()
        ));
        if (res.isError())
          throw new Exception(res.getErrorMessage());
        itemVO = (DetailItemVO)((VOResponse)res).getVo();
        barcodeType = itemVO.getBarcodeTypeITM01();

        barcode = itemVO.getBarCodeITM01();

        if (barcode==null || barcode.equals("")) {
          // barcode not defined at item level...
          pstmt2.setString(1,rowVO.getCompanyCodeSys01());
          pstmt2.setString(2,rowVO.getItemCodeItm01());
          pstmt2.setString(3,rowVO.getVariantTypeItm06());
          pstmt2.setString(4,rowVO.getVariantCodeItm11());
          pstmt2.setString(5,rowVO.getVariantTypeItm07());
          pstmt2.setString(6,rowVO.getVariantCodeItm12());
          pstmt2.setString(7,rowVO.getVariantTypeItm08());
          pstmt2.setString(8,rowVO.getVariantCodeItm13());
          pstmt2.setString(9,rowVO.getVariantTypeItm09());
          pstmt2.setString(10,rowVO.getVariantCodeItm14());
          pstmt2.setString(11,rowVO.getVariantTypeItm10());
          pstmt2.setString(12,rowVO.getVariantCodeItm15());
          rset2 = pstmt2.executeQuery();
          if (rset2.next()) {
            barcode = rset2.getString(1);
          }
          rset2.close();
        }

        if (barcode!=null && !barcode.equals("")) {
          pstmt.setBigDecimal(1,reportId);
          pstmt.setLong(2,progressive);
          pstmt.setString(3,pad(barcode,barcodeType));
          pstmt.setString(4,barcodeType);
/*
          aux = itemVO.getDescriptionSYS10();
          if (rowVO.getVariantTypeItm06()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantTypeItm06()))
            aux += " "+rowVO.getVariantTypeItm06();
          if (rowVO.getVariantCodeItm11()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm11()))
            aux += " "+rowVO.getVariantCodeItm11();

          if (rowVO.getVariantTypeItm07()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantTypeItm07()))
            aux += " "+rowVO.getVariantTypeItm07();
          if (rowVO.getVariantCodeItm12()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm12()))
            aux += " "+rowVO.getVariantCodeItm12();

          if (rowVO.getVariantTypeItm08()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantTypeItm08()))
            aux += " "+rowVO.getVariantTypeItm08();
          if (rowVO.getVariantCodeItm13()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm13()))
            aux += " "+rowVO.getVariantCodeItm13();

          if (rowVO.getVariantTypeItm09()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantTypeItm09()))
            aux += " "+rowVO.getVariantTypeItm09();
          if (rowVO.getVariantCodeItm14()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm14()))
            aux += " "+rowVO.getVariantCodeItm14();

          if (rowVO.getVariantTypeItm10()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantTypeItm10()))
            aux += " "+rowVO.getVariantTypeItm10();
          if (rowVO.getVariantCodeItm15()!=null && !ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm15()))
            aux += " "+rowVO.getVariantCodeItm15();
*/



					aux = itemVO.getDescriptionSYS10();

					// check supported variants for current item...
					if (!ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm11())) {
						aux += " "+getVariantCodeAndTypeDesc(
							variant1Descriptions,
							rowVO,
							rowVO.getVariantTypeItm06(),
							rowVO.getVariantCodeItm11(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm12())) {
						aux += " "+getVariantCodeAndTypeDesc(
							variant2Descriptions,
							rowVO,
							rowVO.getVariantTypeItm07(),
							rowVO.getVariantCodeItm12(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm13())) {
						aux += " "+getVariantCodeAndTypeDesc(
							variant3Descriptions,
							rowVO,
							rowVO.getVariantTypeItm08(),
							rowVO.getVariantCodeItm13(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm14())) {
						aux += " "+getVariantCodeAndTypeDesc(
							variant4Descriptions,
							rowVO,
							rowVO.getVariantTypeItm09(),
							rowVO.getVariantCodeItm14(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(rowVO.getVariantCodeItm15())) {
						aux += " "+getVariantCodeAndTypeDesc(
							variant5Descriptions,
							rowVO,
							rowVO.getVariantTypeItm10(),
							rowVO.getVariantCodeItm15(),
							serverLanguageId,
							username
						);
					}
					rowVO.setDescriptionSYS10(aux);



          pstmt.setString(5,aux);

          for(int j=0;j<rowVO.getQty().intValue();j++) {
            pstmt.setLong(2,progressive);
            pstmt.execute();
            progressive++;
          }
        }
      }


      return new VOResponse(reportId);
    } catch (Exception ex1) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching barcodes and fill in TMP02 table",ex1);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	throw new Exception(ex1.getMessage());
    } finally {
    	try {
    		pstmt.close();
    	}
    	catch (Exception ex) {
    	}
    	try {
    		rset2.close();
    	}
    	catch (Exception ex) {
    	}
    	try {
    		pstmt2.close();
    	}
    	catch (Exception ex) {
    	}
    	try {
    		bean.setConn(null);
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


  private String pad(String code,String barcodeType) {
    if (barcodeType.equals(ApplicationConsts.BAR_CODE_EAN13)) {
      int len = code.length();
      for(int i=len;i<13;i++)
        code = "0"+code;
      return code;
    }
    else
      return code;
  }



		private String getVariantCodeAndTypeDesc(
				HashMap variantDescriptions,
				ItemToPrintVO vo,
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

