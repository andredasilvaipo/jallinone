package org.jallinone.expirations.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import javax.sql.DataSource;

import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.accounting.movements.server.AccountingMovementsBean;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.expirations.java.ExpirationVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.server.ParamsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update existing sale/purchase expirations.</p>
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
public class UpdateExpirationsBean  implements UpdateExpirations {


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



  private InsertJournalItemBean insJornalItemAction;

  public void setInsJornalItemAction(InsertJournalItemBean insJornalItemAction) {
    this.insJornalItemAction = insJornalItemAction;
  }

  private ParamsBean userParamAction;

  public void setUserParamAction(ParamsBean userParamAction) {
    this.userParamAction = userParamAction;
  }



  public UpdateExpirationsBean() {
  }




  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public ExpirationVO getExpiration() {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse updateExpirations(String t1,String t2,ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      insJornalItemAction.setConn(conn); // use same transaction...
      userParamAction.setConn(conn); // use same transaction...


      ExpirationVO oldVO = null;
      ExpirationVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01DOC19");
      pkAttrs.add("progressiveDOC19");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveDOC19","PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01DOC19","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC19","DOC_TYPE");
      attribute2dbField.put("docNumberDOC19","DOC_NUMBER");
      attribute2dbField.put("docYearDOC19","DOC_YEAR");
      attribute2dbField.put("docSequenceDOC19","DOC_SEQUENCE");
      attribute2dbField.put("name_1DOC19","NAME_1");
      attribute2dbField.put("name_2DOC19","NAME_2");
      attribute2dbField.put("descriptionDOC19","DESCRIPTION");
      attribute2dbField.put("valueDOC19","VALUE");
      attribute2dbField.put("payedDOC19","PAYED");
      attribute2dbField.put("docDateDOC19","DOC_DATE");
      attribute2dbField.put("expirationDateDOC19","EXPIRATION_DATE");
      attribute2dbField.put("progressiveReg04DOC19","PROGRESSIVE_REG04");
      attribute2dbField.put("customerSupplierCodeDOC19","CUSTOMER_SUPPLIER_CODE");

			attribute2dbField.put("payedDateDOC19","PAYED_DATE");
			attribute2dbField.put("payedValueDOC19","PAYED_VALUE");
			attribute2dbField.put("realPaymentTypeCodeReg11DOC19","REAL_PAYMENT_TYPE_CODE_REG11");
			attribute2dbField.put("paymentTypeCodeReg11DOC19","PAYMENT_TYPE_CODE_REG11");

			attribute2dbField.put("roundingAccountCodeAcc02DOC19","ROUNDING_ACCOUNT_CODE_ACC02");
			attribute2dbField.put("realAccountCodeAcc02DOC19","REAL_ACCOUNT_CODE_ACC02");

      JournalHeaderVO jhVO = null;
      HashMap map = new HashMap();
      String bankAccountCode = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ExpirationVO)oldVOs.get(i);
        newVO = (ExpirationVO)newVOs.get(i);

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "DOC19_EXPIRATIONS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        map.put(ApplicationConsts.COMPANY_CODE_SYS01,newVO.getCompanyCodeSys01DOC19());
        map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.BANK_ACCOUNT);
        res = userParamAction.loadUserParam(map,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        bankAccountCode = ((VOResponse)res).getVo().toString();

        // generate an accounting item if the row has been payed...
        if (!oldVO.getPayedDOC19().booleanValue() && newVO.getPayedDOC19().booleanValue()) {

          jhVO = new JournalHeaderVO();
          jhVO.setCompanyCodeSys01ACC05(newVO.getCompanyCodeSys01DOC19());
          String creditDebitAccountCode = null;
          String accountCodeTypeACC06 = null;
          if (newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
							newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
            jhVO.setDescriptionACC05(
                newVO.getDescriptionDOC19()+" - "+
                t1+" "+newVO.getName_1DOC19()+" "+(newVO.getName_2DOC19()==null?"":newVO.getName_2DOC19())
            );

            // determine account codes defined for the current customer...
            pstmt = conn.prepareStatement(
              "select CREDIT_ACCOUNT_CODE_ACC02 from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
            );
            pstmt.setString(1,newVO.getCompanyCodeSys01DOC19());
            pstmt.setBigDecimal(2,newVO.getProgressiveReg04DOC19());
            ResultSet rset = pstmt.executeQuery();
            if (!rset.next()) {
              rset.close();
              throw new Exception("customer not found");
            }
            creditDebitAccountCode = rset.getString(1);
            rset.close();
            pstmt.close();

            accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_CUSTOMER;
          }
          else {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
            jhVO.setDescriptionACC05(
                newVO.getDescriptionDOC19()+" - "+
                t2+" "+newVO.getName_1DOC19()+" "+(newVO.getName_2DOC19()==null?"":newVO.getName_2DOC19())
            );

            // determine account codes defined for the current supplier...
            pstmt = conn.prepareStatement(
              "select DEBIT_ACCOUNT_CODE_ACC02 from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
            );
            pstmt.setString(1,newVO.getCompanyCodeSys01DOC19());
            pstmt.setBigDecimal(2,newVO.getProgressiveReg04DOC19());
            ResultSet rset = pstmt.executeQuery();
            if (!rset.next()) {
              rset.close();
              throw new Exception("supplier not found");
            }
            creditDebitAccountCode = rset.getString(1);
            rset.close();
            pstmt.close();

            accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_SUPPLIER;
          }

          jhVO.setItemDateACC05(newVO.getPayedDateDOC19());
					Calendar cal = Calendar.getInstance();
					cal.setTime(newVO.getPayedDateDOC19());
					jhVO.setItemYearACC05(new BigDecimal(cal.get(Calendar.YEAR)));

          JournalRowVO jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(creditDebitAccountCode);
          jrVO.setAccountCodeACC06(newVO.getCustomerSupplierCodeDOC19());
          jrVO.setAccountCodeTypeACC06(accountCodeTypeACC06);
          jrVO.setCreditAmountACC06(newVO.getValueDOC19());
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06( newVO.getRealAccountCodeAcc02DOC19()==null?bankAccountCode:newVO.getRealAccountCodeAcc02DOC19() );
          jrVO.setAccountCodeACC06( newVO.getRealAccountCodeAcc02DOC19()==null?bankAccountCode:newVO.getRealAccountCodeAcc02DOC19() );
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
          jrVO.setDebitAmountACC06(newVO.getPayedValueDOC19());
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);


					// check if there is a rounding on payment...
					if (newVO.getPayedValueDOC19()!=null && !newVO.getPayedValueDOC19().equals(newVO.getValueDOC19())) {

						// another accounting movement must be performed, related to rounding...
						jrVO = new JournalRowVO();
						jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
						jrVO.setAccountCodeAcc02ACC06( newVO.getRoundingAccountCodeAcc02DOC19() );
						jrVO.setAccountCodeACC06( newVO.getRoundingAccountCodeAcc02DOC19() );
						jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
						jrVO.setDebitAmountACC06(newVO.getValueDOC19().subtract(newVO.getPayedValueDOC19()));
						jrVO.setDescriptionACC06("");
						jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
						jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
						jhVO.addJournalRow(jrVO);
					}

          res = insJornalItemAction.insertJournalItem(jhVO,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }


        }

      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing expirations",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex1) {
      }

      try {
        insJornalItemAction.setConn(null);
        userParamAction.setConn(null);
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


	public VOResponse payImmediately(String companyCode,String docType,BigDecimal docYear,BigDecimal docNumber,BigDecimal docSequence,String t1,String t2,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			insJornalItemAction.setConn(conn); // use same transaction...
			userParamAction.setConn(conn); // use same transaction...

			pstmt = conn.prepareStatement(
			  "UPDATE DOC19_EXPIRATIONS SET PAYED_VALUE=VALUE,PAYED_DATE=EXPIRATION_DATE,PAYED='Y',REAL_PAYMENT_TYPE_CODE_REG11=PAYMENT_TYPE_CODE_REG11 WHERE "+
				"COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
			);
		  pstmt.setString(1,companyCode);
			pstmt.setString(2,docType);
			pstmt.setBigDecimal(3,docYear);
			pstmt.setBigDecimal(4,docNumber);
			int rows = pstmt.executeUpdate();
			if (rows!=1)
				throw new Exception("invalid number of payments");
			pstmt.close();

			pstmt = conn.prepareStatement(
				"SELECT DESCRIPTION,NAME_1,NAME_2,PROGRESSIVE_REG04,VALUE,CUSTOMER_SUPPLIER_CODE,ACCOUNT_CODE_ACC02 "+
				"FROM DOC19_EXPIRATIONS WHERE "+
				"COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=?"
			);
			pstmt.setString(1,companyCode);
			pstmt.setString(2,docType);
			pstmt.setBigDecimal(3,docYear);
			pstmt.setBigDecimal(4,docNumber);
			rset = pstmt.executeQuery();
			rset.next();
			String descriptionDOC19 = rset.getString(1);
			String name_1DOC19 = rset.getString(2);
			String name_2DOC19 = rset.getString(3);
			BigDecimal progressiveReg04DOC19 = rset.getBigDecimal(4);
			BigDecimal valueDOC19 = rset.getBigDecimal(5);
			String customerSupplierCodeDOC19 = rset.getString(6);
			String accountCode = rset.getString(7);
			rset.close();
			pstmt.close();


			if (accountCode==null) {
				HashMap map = new HashMap();
				map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
				map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.BANK_ACCOUNT);
				Response res = userParamAction.loadUserParam(map,serverLanguageId,username);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
				accountCode = ((VOResponse)res).getVo().toString();
			}

			// generate an accounting item, since the row has been payed...
			JournalHeaderVO jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			String creditDebitAccountCode = null;
			String accountCodeTypeACC06 = null;
			if (docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
					docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
					docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
					docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
					docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
					docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
				jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
				jhVO.setDescriptionACC05(
						descriptionDOC19+" - "+
						t1+" "+name_1DOC19+" "+(name_2DOC19==null?"":name_2DOC19)
				);

				// determine account codes defined for the current customer...
				pstmt = conn.prepareStatement(
					"select CREDIT_ACCOUNT_CODE_ACC02 from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
				);
				pstmt.setString(1,companyCode);
				pstmt.setBigDecimal(2,progressiveReg04DOC19);
				rset = pstmt.executeQuery();
				if (!rset.next()) {
					rset.close();
					throw new Exception("customer not found");
				}
				creditDebitAccountCode = rset.getString(1);
				rset.close();
				pstmt.close();

				accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_CUSTOMER;
			}
			else {
				jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
				jhVO.setDescriptionACC05(
						descriptionDOC19+" - "+
						t2+" "+name_1DOC19+" "+(name_2DOC19==null?"":name_2DOC19)
				);

				// determine account codes defined for the current supplier...
				pstmt = conn.prepareStatement(
					"select DEBIT_ACCOUNT_CODE_ACC02 from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
				);
				pstmt.setString(1,companyCode);
				pstmt.setBigDecimal(2,progressiveReg04DOC19);
				rset = pstmt.executeQuery();
				if (!rset.next()) {
					rset.close();
					throw new Exception("supplier not found");
				}
				creditDebitAccountCode = rset.getString(1);
				rset.close();
				pstmt.close();

				accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_SUPPLIER;
			}
			jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
			jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

			JournalRowVO jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(creditDebitAccountCode);
			jrVO.setAccountCodeACC06(customerSupplierCodeDOC19);
			jrVO.setAccountCodeTypeACC06(accountCodeTypeACC06);
			jrVO.setCreditAmountACC06(valueDOC19);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(accountCode);
			jrVO.setAccountCodeACC06(accountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setDebitAmountACC06(valueDOC19);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);


			Response res = insJornalItemAction.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			return new VOResponse(Boolean.TRUE);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing expiration",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}      throw new Exception(ex.getMessage());
		}
		finally {
			try {
				if (rset!=null)
					rset.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt!=null)
					pstmt.close();
			}
			catch (Exception ex1) {
			}

			try {
				insJornalItemAction.setConn(null);
				userParamAction.setConn(null);
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



}

