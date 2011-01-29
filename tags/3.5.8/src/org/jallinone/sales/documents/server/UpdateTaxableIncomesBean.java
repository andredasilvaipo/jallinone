package org.jallinone.sales.documents.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.jallinone.accounting.movements.java.TaxableIncomeVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.registers.vat.server.VatsBean;
import org.jallinone.sales.documents.activities.java.SaleDocActivityVO;
import org.jallinone.sales.documents.activities.server.SaleDocActivitiesBean;
import org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO;
import org.jallinone.sales.documents.headercharges.server.SaleDocChargesBean;
import org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO;
import org.jallinone.sales.documents.headerdiscounts.server.SaleDocDiscountsBean;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to update all taxable incomes for all items and activities and charges (value charges),
 * for the specified sale document (order, contract, retail sale, invoice).
 * After updating all taxable incomes, also document totals are updated.</p>
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
public class UpdateTaxableIncomesBean implements UpdateTaxableIncomes {


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



  private VatsBean vatBean;

  public void setVatBean(VatsBean vatBean) {
    this.vatBean = vatBean;
  }

  private LoadSaleDocBean docBean;

  public void setDocBean(LoadSaleDocBean docBean) {
    this.docBean = docBean;
  }

  private LoadSaleDocRowsBean rowsBean;

  public void setRowsBean(LoadSaleDocRowsBean rowsBean) {
    this.rowsBean = rowsBean;
  }


  private LoadSaleDocRowBean bean;

  public void setBean(LoadSaleDocRowBean bean) {
	  this.bean = bean;
  }

  private SaleItemTotalDiscountBean itemDiscountBean;

  public void setItemDiscountBean(SaleItemTotalDiscountBean itemDiscountBean) {
    this.itemDiscountBean = itemDiscountBean;
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public TaxableIncomeVO getTaxableIncome() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Recalculate items/activities/value charges totals and document totals.
   * No commit or rollback are executed. No connection is created or released.
   * @return list of TaxableIncomeVO objects, grouped per item/act/charge and per vat code
   */
  public VOListResponse updateTaxableIncomes(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocPK pk, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vatBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...
      rowsBean.setConn(conn); // use same transaction...
      itemDiscountBean.setConn(conn); // use same transaction...
      bean.setConn(conn);

      long time = System.currentTimeMillis();
      BigDecimal totalTaxableIncome = new BigDecimal(0);

      // retrieve document header value object...
      DetailSaleDocVO vo = docBean.loadSaleDoc(pk,serverLanguageId,username,new ArrayList());

      // check if there exists a vat code defined at customer level...
      BigDecimal customerVatValue = null;
      String customerVatCode = null;
      String customerVatDescription = null;
      if (vo.getCustomerVatCodeReg01DOC01()!=null && !vo.getCustomerVatCodeReg01DOC01().equals("")) {
        // retrieve vat value and deductible percentage...
        Response res = vatBean.validateVatCode(
            new LookupValidationParams(vo.getCustomerVatCodeReg01DOC01(),new HashMap()),
            serverLanguageId,
            username,
            new ArrayList()
        );
        if (!res.isError()) {
          VatVO vatVO = (VatVO)((VOListResponse)res).getRows().get(0);
          customerVatValue = new BigDecimal(vatVO.getValueREG01().doubleValue()*(1d-vatVO.getDeductibleREG01().doubleValue()/100d));
          customerVatCode = vatVO.getVatCodeREG01();
          customerVatDescription = vatVO.getDescriptionSYS10();
        }
      }

      // retrieve all item rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
      Response rowsResponse = rowsBean.loadSaleDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pars,serverLanguageId,username);
      if (rowsResponse.isError())
    	  throw new Exception(rowsResponse.getErrorMessage());
      java.util.List itemRows = ((VOListResponse)rowsResponse).getRows();

      // for each item, calculate item discounts...
      GridSaleDocRowVO itemVO = null;
      Response rowResponse = null;
      DetailSaleDocRowVO detailItemVO = null;
      Response itemTotDiscResponse = null;
      ArrayList detailItemRows = new ArrayList();
      double vatPerc;
      for(int i=0;i<itemRows.size();i++) {
        itemVO = (GridSaleDocRowVO)itemRows.get(i);
        // retrieve item detail...
        rowResponse = bean.loadSaleDocRow(
          variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
          new SaleDocRowPK(
            pk.getCompanyCodeSys01DOC01(),
            pk.getDocTypeDOC01(),
            pk.getDocYearDOC01(),
            pk.getDocNumberDOC01(),
            itemVO.getItemCodeItm01DOC02(),
            itemVO.getVariantTypeItm06DOC02(),
            itemVO.getVariantCodeItm11DOC02(),
            itemVO.getVariantTypeItm07DOC02(),
            itemVO.getVariantCodeItm12DOC02(),
            itemVO.getVariantTypeItm08DOC02(),
            itemVO.getVariantCodeItm13DOC02(),
            itemVO.getVariantTypeItm09DOC02(),
            itemVO.getVariantCodeItm14DOC02(),
            itemVO.getVariantTypeItm10DOC02(),
            itemVO.getVariantCodeItm15DOC02()
          ),
          serverLanguageId,
          username

        );
        if (rowResponse.isError())
        	throw new Exception(rowsResponse.getErrorMessage());
        detailItemVO = (DetailSaleDocRowVO)((VOResponse)rowResponse).getVo();
        detailItemRows.add(detailItemVO);

        // calculate item discount...
        detailItemVO.setTaxableIncomeDOC02(detailItemVO.getQtyDOC02().multiply(detailItemVO.getValueSal02DOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        itemTotDiscResponse = itemDiscountBean.getSaleItemTotalDiscount(detailItemVO,serverLanguageId,username);
        if (itemTotDiscResponse.isError())
        	throw new Exception(itemTotDiscResponse.getErrorMessage());

        // apply total discount to item detail...
        detailItemVO.setTotalDiscountDOC02( detailItemVO.getTotalDiscountDOC02().setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP) );

        // apply total discount to taxable income...
        detailItemVO.setTaxableIncomeDOC02(detailItemVO.getTaxableIncomeDOC02().subtract(detailItemVO.getTotalDiscountDOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

        // calculate row vat...
        vatPerc = detailItemVO.getValueReg01DOC02().doubleValue()*(1d-detailItemVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
        detailItemVO.setVatValueDOC02(detailItemVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

        // calculate row total...
        detailItemVO.setValueDOC02(detailItemVO.getTaxableIncomeDOC02().add(detailItemVO.getVatValueDOC02()));

        // apply taxable income to total taxable income...
        totalTaxableIncome = totalTaxableIncome.add(detailItemVO.getTaxableIncomeDOC02());
      }

      // retrieve all activities rows...
      rowsResponse = rowsBean.loadSaleDocActivities(pars,serverLanguageId,username);
      if (rowsResponse.isError())
        throw new Exception(rowsResponse.getErrorMessage());
      java.util.List actsRows = ((VOListResponse)rowsResponse).getRows();
      SaleDocActivityVO actVO = null;
      for(int i=0;i<actsRows.size();i++) {
        actVO = (SaleDocActivityVO)actsRows.get(i);
        actVO.setTaxableIncomeDOC13(
          actVO.getValueDOC13().setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
        );

        // apply taxable income to total taxable income...
        totalTaxableIncome = totalTaxableIncome.add(actVO.getTaxableIncomeDOC13());
      }

      // retrieve all charges rows...
      rowsResponse = rowsBean.loadSaleDocCharges(pars,serverLanguageId,username);
      if (rowsResponse.isError())
    	  throw new Exception(rowsResponse.getErrorMessage());
      java.util.List chargesRows = ((VOListResponse)rowsResponse).getRows();
      SaleDocChargeVO chargeVO = null;
      for(int i=0;i<chargesRows.size();i++) {
        chargeVO = (SaleDocChargeVO)chargesRows.get(i);
        if (chargeVO.getValueDOC03()!=null) {
          chargeVO.setTaxableIncomeDOC03(
            chargeVO.getValueDOC03().setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
          );

          // apply taxable income to total taxable income...
          totalTaxableIncome = totalTaxableIncome.add(chargeVO.getTaxableIncomeDOC03());
        }
      }

      // fill in coeff...
      Hashtable coeff = new Hashtable(); // collection of <item/act/valuecharge,taxableincome/totaltaxableincome>
      for(int i=0;i<detailItemRows.size();i++) {
        detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
        coeff.put(detailItemVO,detailItemVO.getTaxableIncomeDOC02().divide(totalTaxableIncome,BigDecimal.ROUND_HALF_UP));
      }

      for(int i=0;i<actsRows.size();i++) {
        actVO = (SaleDocActivityVO)actsRows.get(i);
        coeff.put(actVO,actVO.getTaxableIncomeDOC13().divide(totalTaxableIncome,BigDecimal.ROUND_HALF_UP));
      }

      for(int i=0;i<chargesRows.size();i++) {
        chargeVO = (SaleDocChargeVO)chargesRows.get(i);
        if (chargeVO.getValueDOC03()!=null) {
          coeff.put(chargeVO,chargeVO.getTaxableIncomeDOC03().divide(totalTaxableIncome,BigDecimal.ROUND_HALF_UP));
        }
      }

      // apply % charges to all taxable incomes...
      SaleDocChargeVO percVO = null;
      for(int k=0;k<chargesRows.size();k++) {
        percVO = (SaleDocChargeVO)chargesRows.get(k);
        if (percVO.getPercDOC03()!=null) {
          for(int i=0;i<detailItemRows.size();i++) {
            detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
            detailItemVO.setTaxableIncomeDOC02(
              detailItemVO.getTaxableIncomeDOC02().add(detailItemVO.getTaxableIncomeDOC02().multiply(percVO.getPercDOC03().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<actsRows.size();i++) {
            actVO = (SaleDocActivityVO)actsRows.get(i);
            actVO.setTaxableIncomeDOC13(
              actVO.getTaxableIncomeDOC13().add(actVO.getTaxableIncomeDOC13().multiply(percVO.getPercDOC03().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<chargesRows.size();i++) {
            chargeVO = (SaleDocChargeVO)chargesRows.get(i);
            if (chargeVO.getValueDOC03()!=null) {
              chargeVO.setTaxableIncomeDOC03(
                chargeVO.getTaxableIncomeDOC03().add(chargeVO.getTaxableIncomeDOC03().multiply(percVO.getPercDOC03().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
                setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
              );
            }
          }

        }
      }

      // +MC 7/1/2011 (as in SaleDocTotalsBean.getSaleDocTotals)
			// apply discount (eventually) defined at row level...
			if (vo.getDiscountValueDOC01()!=null) {

				for(int i=0;i<detailItemRows.size();i++) {
					detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
					detailItemVO.setTaxableIncomeDOC02(
						detailItemVO.getTaxableIncomeDOC02().subtract(vo.getDiscountValueDOC01().multiply((BigDecimal)coeff.get(detailItemVO))).
						setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
					);
				}

				for(int i=0;i<actsRows.size();i++) {
					actVO = (SaleDocActivityVO)actsRows.get(i);
					actVO.setTaxableIncomeDOC13(
						actVO.getTaxableIncomeDOC13().subtract(vo.getDiscountValueDOC01().multiply((BigDecimal)coeff.get(actVO))).
						setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
					);
				}

				for(int i=0;i<chargesRows.size();i++) {
					chargeVO = (SaleDocChargeVO)chargesRows.get(i);
					if (chargeVO.getValueDOC03()!=null) {
						chargeVO.setTaxableIncomeDOC03(
							chargeVO.getTaxableIncomeDOC03().subtract(vo.getDiscountValueDOC01().multiply((BigDecimal)coeff.get(chargeVO))).
							setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
						);
					}
				}

			}
			else if (vo.getDiscountPercDOC01()!=null) {
				for(int i=0;i<detailItemRows.size();i++) {
					detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
					detailItemVO.setTaxableIncomeDOC02(
						detailItemVO.getTaxableIncomeDOC02().subtract(detailItemVO.getTaxableIncomeDOC02().multiply(vo.getDiscountPercDOC01().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
						setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
					);
				}

				for(int i=0;i<actsRows.size();i++) {
					actVO = (SaleDocActivityVO)actsRows.get(i);
					actVO.setTaxableIncomeDOC13(
						actVO.getTaxableIncomeDOC13().subtract(actVO.getTaxableIncomeDOC13().multiply(vo.getDiscountPercDOC01().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
						setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
					);
				}

				for(int i=0;i<chargesRows.size();i++) {
					chargeVO = (SaleDocChargeVO)chargesRows.get(i);
					if (chargeVO.getValueDOC03()!=null) {
						chargeVO.setTaxableIncomeDOC03(
							chargeVO.getTaxableIncomeDOC03().subtract(chargeVO.getTaxableIncomeDOC03().multiply(vo.getDiscountPercDOC01().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
							setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
						);
					}
				}

			}

      // retrieve header discounts..
      rowsResponse = rowsBean.loadSaleDocDiscounts(pars,serverLanguageId,username);
      if (rowsResponse.isError())
        throw new Exception(rowsResponse.getErrorMessage());
      java.util.List discounts = ((VOListResponse)rowsResponse).getRows();

      // apply header discounts to all taxable incomes...
      SaleDocDiscountVO discVO = null;
      for(int k=0;k<discounts.size();k++) {
        discVO = (SaleDocDiscountVO)discounts.get(k);
        if (discVO.getPercDOC05()!=null) {
          // the current discount is % discount...
          for(int i=0;i<detailItemRows.size();i++) {
            detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
            detailItemVO.setTaxableIncomeDOC02(
              detailItemVO.getTaxableIncomeDOC02().subtract(detailItemVO.getTaxableIncomeDOC02().multiply(discVO.getPercDOC05().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<actsRows.size();i++) {
            actVO = (SaleDocActivityVO)actsRows.get(i);
            actVO.setTaxableIncomeDOC13(
              actVO.getTaxableIncomeDOC13().subtract(actVO.getTaxableIncomeDOC13().multiply(discVO.getPercDOC05().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<chargesRows.size();i++) {
            chargeVO = (SaleDocChargeVO)chargesRows.get(i);
            if (chargeVO.getValueDOC03()!=null) {
              chargeVO.setTaxableIncomeDOC03(
                chargeVO.getTaxableIncomeDOC03().subtract(chargeVO.getTaxableIncomeDOC03().multiply(discVO.getPercDOC05().divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP))).
                setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
              );
            }
          }

        }
        else if (discVO.getValueDOC05()!=null) {
          // the current discount is a value discount...
          for(int i=0;i<detailItemRows.size();i++) {
            detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);
            detailItemVO.setTaxableIncomeDOC02(
              detailItemVO.getTaxableIncomeDOC02().subtract(discVO.getValueDOC05().multiply((BigDecimal)coeff.get(detailItemVO))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<actsRows.size();i++) {
            actVO = (SaleDocActivityVO)actsRows.get(i);
            actVO.setTaxableIncomeDOC13(
              actVO.getTaxableIncomeDOC13().subtract(discVO.getValueDOC05().multiply((BigDecimal)coeff.get(actVO))).
              setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
            );
          }

          for(int i=0;i<chargesRows.size();i++) {
            chargeVO = (SaleDocChargeVO)chargesRows.get(i);
            if (chargeVO.getValueDOC03()!=null) {
              chargeVO.setTaxableIncomeDOC03(
                chargeVO.getTaxableIncomeDOC03().subtract(discVO.getValueDOC05().multiply((BigDecimal)coeff.get(chargeVO))).
                setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
              );
            }
          }

        }

      }

      // update vat values, item rows and calculate totals...
      Hashtable itemsTaxableIncomeRows = new Hashtable(); // collection of pairs <vatcode,TaxableIncomeVO>
      totalTaxableIncome = new BigDecimal(0);
      BigDecimal totalVat = new BigDecimal(0);
      String vatCode = null;
      String vatDescr = null;
      TaxableIncomeVO tVO = null;
      pstmt = conn.prepareStatement(
        "update DOC02_SELLING_ITEMS set TAXABLE_INCOME=?,VAT_VALUE=?,VALUE=?,TOTAL_DISCOUNT=? "+
        "where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
        "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
        "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
        "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
        "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
        "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
      );
      for(int i=0;i<detailItemRows.size();i++) {
        detailItemVO = (DetailSaleDocRowVO)detailItemRows.get(i);

        if (customerVatValue==null) {
          vatCode = detailItemVO.getVatCodeItm01DOC02();
          vatDescr = detailItemVO.getVatDescriptionDOC02();
          vatPerc = detailItemVO.getValueReg01DOC02().doubleValue()*(1d-detailItemVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
        }
        else {
          vatCode = customerVatCode;
          vatDescr = customerVatDescription;
          vatPerc = customerVatValue.doubleValue()/100d;
        }

        detailItemVO.setVatValueDOC02(detailItemVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        detailItemVO.setValueDOC02(detailItemVO.getTaxableIncomeDOC02().add(detailItemVO.getVatValueDOC02()));

        totalTaxableIncome = totalTaxableIncome.add(detailItemVO.getTaxableIncomeDOC02());
        totalVat = totalVat.add(detailItemVO.getVatValueDOC02());

        tVO = (TaxableIncomeVO)itemsTaxableIncomeRows.get(vatCode);
        if (tVO==null) {
          tVO = new TaxableIncomeVO();
          tVO.setRowType(tVO.ITEM_ROW_TYPE);
          tVO.setVatCode(vatCode);
          tVO.setVatDescription(vatDescr);
          tVO.setVatValue(new BigDecimal(0));
          tVO.setTaxableIncome(new BigDecimal(0));
          itemsTaxableIncomeRows.put(vatCode,tVO);
        }
        tVO.setTaxableIncome(tVO.getTaxableIncome().add(detailItemVO.getTaxableIncomeDOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        tVO.setVatValue(tVO.getVatValue().add(detailItemVO.getVatValueDOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

        pstmt.setBigDecimal(1,detailItemVO.getTaxableIncomeDOC02());
        pstmt.setBigDecimal(2,detailItemVO.getVatValueDOC02());
        pstmt.setBigDecimal(3,detailItemVO.getValueDOC02());
        pstmt.setBigDecimal(4,detailItemVO.getTotalDiscountDOC02());
        pstmt.setString(5,detailItemVO.getCompanyCodeSys01DOC02());
        pstmt.setString(6,detailItemVO.getDocTypeDOC02());
        pstmt.setBigDecimal(7,detailItemVO.getDocYearDOC02());
        pstmt.setBigDecimal(8,detailItemVO.getDocNumberDOC02());
        pstmt.setString(9,detailItemVO.getItemCodeItm01DOC02());

        pstmt.setString(10,detailItemVO.getVariantTypeItm06DOC02());
        pstmt.setString(11,detailItemVO.getVariantCodeItm11DOC02());
        pstmt.setString(12,detailItemVO.getVariantTypeItm07DOC02());
        pstmt.setString(13,detailItemVO.getVariantCodeItm12DOC02());
        pstmt.setString(14,detailItemVO.getVariantTypeItm08DOC02());
        pstmt.setString(15,detailItemVO.getVariantCodeItm13DOC02());
        pstmt.setString(16,detailItemVO.getVariantTypeItm09DOC02());
        pstmt.setString(17,detailItemVO.getVariantCodeItm14DOC02());
        pstmt.setString(18,detailItemVO.getVariantTypeItm10DOC02());
        pstmt.setString(19,detailItemVO.getVariantCodeItm15DOC02());

        pstmt.execute();
      }
      pstmt.close();

      // update vat values, activities rows and calculate totals...
      Hashtable actsTaxableIncomeRows = new Hashtable(); // collection of pairs <vatcode,TaxableIncomeVO>
      pstmt = conn.prepareStatement("update DOC13_SELLING_ACTIVITIES set TAXABLE_INCOME=?,VAT_VALUE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ACTIVITY_CODE_SAL09=?");
      for(int i=0;i<actsRows.size();i++) {
        actVO = (SaleDocActivityVO)actsRows.get(i);

        if (customerVatValue==null) {
          vatCode = actVO.getVatCodeSal09DOC13();
          vatDescr = actVO.getVatDescriptionDOC13();
          vatPerc = actVO.getValueReg01DOC13().doubleValue()*(1d-actVO.getVatDeductibleDOC13().doubleValue()/100d)/100;
        }
        else {
          vatCode = customerVatCode;
          vatDescr = customerVatDescription;
          vatPerc = customerVatValue.doubleValue()/100d;
        }

        actVO.setVatValueDOC13(actVO.getTaxableIncomeDOC13().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

        totalTaxableIncome = totalTaxableIncome.add(actVO.getTaxableIncomeDOC13());
        totalVat = totalVat.add(actVO.getVatValueDOC13());

        tVO = (TaxableIncomeVO)actsTaxableIncomeRows.get(vatCode);
        if (tVO==null) {
          tVO = new TaxableIncomeVO();
          tVO.setRowType(tVO.ACTIVITY_ROW_TYPE);
          tVO.setVatCode(vatCode);
          tVO.setVatDescription(vatDescr);
          tVO.setVatValue(new BigDecimal(0));
          tVO.setTaxableIncome(new BigDecimal(0));
          actsTaxableIncomeRows.put(vatCode,tVO);
        }
        tVO.setTaxableIncome(tVO.getTaxableIncome().add(actVO.getTaxableIncomeDOC13()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        tVO.setVatValue(tVO.getVatValue().add(actVO.getVatValueDOC13()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

        pstmt.setBigDecimal(1,actVO.getTaxableIncomeDOC13());
        pstmt.setBigDecimal(2,actVO.getVatValueDOC13());
        pstmt.setString(3,actVO.getCompanyCodeSys01DOC13());
        pstmt.setString(4,actVO.getDocTypeDOC13());
        pstmt.setBigDecimal(5,actVO.getDocYearDOC13());
        pstmt.setBigDecimal(6,actVO.getDocNumberDOC13());
        pstmt.setString(7,actVO.getActivityCodeSal09DOC13());
        pstmt.execute();
      }
      pstmt.close();

      // update vat values, value charges rows and calculate totals...
      Hashtable chargesTaxableIncomeRows = new Hashtable(); // collection of pairs <vatcode,TaxableIncomeVO>
      pstmt = conn.prepareStatement("update DOC03_SELLING_CHARGES set TAXABLE_INCOME=?,VAT_VALUE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and CHARGE_CODE_SAL06=?");
      for(int i=0;i<chargesRows.size();i++) {
        chargeVO = (SaleDocChargeVO)chargesRows.get(i);
        if (chargeVO.getValueDOC03()!=null) {
          if (customerVatValue==null) {
            vatCode = chargeVO.getVatCodeSal06DOC03();
            vatDescr = chargeVO.getVatDescriptionDOC03();
            vatPerc = chargeVO.getValueReg01DOC03().doubleValue()*(1d-chargeVO.getVatDeductibleDOC03().doubleValue()/100d)/100;
          }
          else {
            vatCode = customerVatCode;
            vatDescr = customerVatDescription;
            vatPerc = customerVatValue.doubleValue()/100d;
          }

          chargeVO.setVatValueDOC03(chargeVO.getTaxableIncomeDOC03().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          totalTaxableIncome = totalTaxableIncome.add(chargeVO.getTaxableIncomeDOC03());
          totalVat = totalVat.add(chargeVO.getVatValueDOC03());

          tVO = (TaxableIncomeVO)chargesTaxableIncomeRows.get(vatCode);
          if (tVO==null) {
            tVO = new TaxableIncomeVO();
            tVO.setRowType(tVO.CHARGE_ROW_TYPE);
            tVO.setVatCode(vatCode);
            tVO.setVatDescription(vatDescr);
            tVO.setVatValue(new BigDecimal(0));
            tVO.setTaxableIncome(new BigDecimal(0));
            chargesTaxableIncomeRows.put(vatCode,tVO);
          }
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(chargeVO.getTaxableIncomeDOC03()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          tVO.setVatValue(tVO.getVatValue().add(chargeVO.getVatValueDOC03()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          pstmt.setBigDecimal(1,chargeVO.getTaxableIncomeDOC03());
          pstmt.setBigDecimal(2,chargeVO.getVatValueDOC03());
          pstmt.setString(3,chargeVO.getCompanyCodeSys01DOC03());
          pstmt.setString(4,chargeVO.getDocTypeDOC03());
          pstmt.setBigDecimal(5,chargeVO.getDocYearDOC03());
          pstmt.setBigDecimal(6,chargeVO.getDocNumberDOC03());
          pstmt.setString(7,chargeVO.getChargeCodeSal06DOC03());
          pstmt.execute();
        }
      }
      pstmt.close();


      // update document totals..
      pstmt = conn.prepareStatement(
        "update DOC01_SELLING set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? "+
        "where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and "+
        "TAXABLE_INCOME=? and TOTAL_VAT=? and TOTAL=?"
      );

      BigDecimal total =
        totalTaxableIncome.
        add(totalVat).
        subtract(vo.getAllowanceDOC01()).
        subtract(vo.getDepositDOC01()).
        setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP);

      pstmt.setBigDecimal(1,totalTaxableIncome);
      pstmt.setBigDecimal(2,totalVat);
      pstmt.setBigDecimal(3,total);
      pstmt.setString(4,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(5,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(6,vo.getDocYearDOC01());
      pstmt.setBigDecimal(7,vo.getDocNumberDOC01());
      pstmt.setBigDecimal(8,vo.getTaxableIncomeDOC01());
      pstmt.setBigDecimal(9,vo.getTotalVatDOC01());
      pstmt.setBigDecimal(10,vo.getTotalDOC01());
      int updatedRows = pstmt.executeUpdate();
      if (updatedRows==0)
        return new VOListResponse("record already updated");


//      vo.setTaxableIncomeDOC01(totalTaxableIncome);
//      vo.setTotalVatDOC01(totalVat);
//      vo.setTotalDOC01(total);

      Logger.debug(username,this.getClass().getName(),"updateTaxableIncomes","Updating taxable incomes... total time: "+(System.currentTimeMillis()-time)+" ms");

      // return taxable income rows...
      ArrayList taxableIncomeRows = new ArrayList();
      Enumeration en = itemsTaxableIncomeRows.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( itemsTaxableIncomeRows.get(en.nextElement()) );
      }
      en = actsTaxableIncomeRows.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( actsTaxableIncomeRows.get(en.nextElement()) );
      }
      en = chargesTaxableIncomeRows.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( chargesTaxableIncomeRows.get(en.nextElement()) );
      }

      return new VOListResponse(taxableIncomeRows,false,taxableIncomeRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateTaxableIncomes","Error while updating taxable incomes and document totals:\n"+ex.getMessage(), ex);
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
        vatBean.setConn(null);
        docBean.setConn(null);
        rowsBean.setConn(null);
        itemDiscountBean.setConn(null);
        bean.setConn(null);
      } catch (Exception ex) {}
    }

  }



}

