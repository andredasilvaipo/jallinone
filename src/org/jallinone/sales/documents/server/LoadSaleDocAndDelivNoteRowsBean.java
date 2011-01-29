package org.jallinone.sales.documents.server;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch sale document rows from DOC02 table and let null in quantity + war. position defined in a delivery note row.</p>
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
public class LoadSaleDocAndDelivNoteRowsBean  implements LoadSaleDocAndDelivNoteRows {


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



  public LoadSaleDocAndDelivNoteRowsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridOutDeliveryNoteRowVO getGridOutDeliveryNoteRow(SaleDocPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadSaleDocAndDelivNoteRows(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		GridParams pars, String serverLanguageId, String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      convBean.setConn(conn); // use same transaction...
      SaleDocPK pk = (SaleDocPK)pars.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);

      String sql =
          "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,"+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,"+
          "DOC02_SELLING_ITEMS.QTY,SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.OUT_QTY,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS1.DECIMALS,DOC01_SELLING.DOC_SEQUENCE, "+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 "+
          " from DOC01_SELLING,REG02_MEASURE_UNITS REG02_ALIAS1,DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC01_SELLING.DOC_STATE=? and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
          "DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY>0 and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
					"ITM01_ITEMS.NO_WAREHOUSE_MOV='N'";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC10","DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDoc01DOC10","DOC02_SELLING_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDoc01DOC10","DOC02_SELLING_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDoc01DOC10","DOC02_SELLING_ITEMS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC10","DOC02_SELLING_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC02","DOC02_SELLING_ITEMS.QTY");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
      attribute2dbField.put("umCodeREG02","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("customerQtyDecimalsREG02","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("progressiveHie02DOC10","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("docSequenceDoc01DOC10","DOC01_SELLING.DOC_SEQUENCE");

      attribute2dbField.put("variantTypeItm06DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC10","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC10","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC01());
      values.add(ApplicationConsts.CONFIRMED);
      values.add(pk.getDocYearDOC01());
      values.add(pk.getDocNumberDOC01());

      // read from DOC02 table...
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
          true
      );

      if (!res.isError()) {
        GridOutDeliveryNoteRowVO vo = null;
        for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
          vo = (GridOutDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
          vo.setQtyDOC10(vo.getQtyDOC02().subtract(vo.getOutQtyDOC02()));
        }
      }

      Response answer = res;
      if (answer.isError())
				throw new Exception(answer.getErrorMessage());

	    java.util.List rows = ((VOListResponse)answer).getRows();
			String descr = null;
			GridOutDeliveryNoteRowVO vo = null;
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
			}

	    return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sale document + delivery note rows proposal list",ex);
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
    	try {
    		convBean.setConn(null);
    	} catch (Exception ex) {}
    }

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

