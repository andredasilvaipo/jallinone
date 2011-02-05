package org.jallinone.registers.payments.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.registers.payments.java.PaymentInstalmentVO;
import org.jallinone.registers.payments.java.PaymentTypeVO;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage payments.</p>
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
public class PaymentsBean implements Payments {


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






  public PaymentsBean() {
  }



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PaymentVO getPayment() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PaymentInstalmentVO getPaymentInstalment() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PaymentTypeVO getPaymentType() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadPaymentInstalments(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
	  Connection conn = null;
	  try {
		  if (this.conn==null) conn = getConn(); else conn = this.conn;

			String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String paymentCodeREG10 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.PAYMENT_CODE_REG10);

      String sql =
          "select REG17_PAY_INSTALMENTS.COMPANY_CODE_SYS01,REG17_PAY_INSTALMENTS.PAYMENT_CODE_REG10,REG17_PAY_INSTALMENTS.RATE_NUMBER,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,REG17_PAY_INSTALMENTS.PERCENTAGE,REG17_PAY_INSTALMENTS.INSTALMENT_DAYS,"+
          "REG17_PAY_INSTALMENTS.PAYMENT_TYPE_CODE_REG11 "+
          " from REG17_PAY_INSTALMENTS,SYS10_TRANSLATIONS,REG11_PAY_TYPES where "+
					"REG17_PAY_INSTALMENTS.COMPANY_CODE_SYS01='"+companyCodeSys01+"' and "+
          "REG17_PAY_INSTALMENTS.PAYMENT_TYPE_CODE_REG11=REG11_PAY_TYPES.PAYMENT_TYPE_CODE and "+
          "REG11_PAY_TYPES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and REG17_PAY_INSTALMENTS.PAYMENT_CODE_REG10='"+paymentCodeREG10+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG17","REG17_PAY_INSTALMENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentCodeReg10REG17","REG17_PAY_INSTALMENTS.PAYMENT_CODE_REG10");
      attribute2dbField.put("rateNumberREG17","REG17_PAY_INSTALMENTS.RATE_NUMBER");
      attribute2dbField.put("paymentTypeDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("percentageREG17","REG17_PAY_INSTALMENTS.PERCENTAGE");
      attribute2dbField.put("instalmentDaysREG17","REG17_PAY_INSTALMENTS.INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeCodeReg11REG17","REG17_PAY_INSTALMENTS.PAYMENT_TYPE_CODE_REG11");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from REG17 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PaymentInstalmentVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching payment instalments list",ex);
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
  public VOListResponse loadPaymentTypes(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";
			if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
				companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
			}
			else {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
				companies = companies.substring(0,companies.length()-1);
			}

      String sql =
          "select REG11_PAY_TYPES.COMPANY_CODE_SYS01,REG11_PAY_TYPES.PAYMENT_TYPE_CODE,REG11_PAY_TYPES.PROGRESSIVE_SYS10,"+
					"SYS10.DESCRIPTION,REG11_PAY_TYPES.ENABLED,REG11_PAY_TYPES.ACCOUNT_CODE_ACC02,ACC02_SYS10.DESCRIPTION "+
					"from SYS10_TRANSLATIONS SYS10,REG11_PAY_TYPES "+
					"LEFT OUTER JOIN ("+
					"  SELECT ACC02_ACCOUNTS.COMPANY_CODE_SYS01,ACC02_ACCOUNTS.ACCOUNT_CODE,SYS10_TRANSLATIONS.DESCRIPTION "+
					"  FROM SYS10_TRANSLATIONS,ACC02_ACCOUNTS where "+
					"  ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"  SYS10_TRANSLATIONS.LANGUAGE_CODE=? "+
          ") ACC02_SYS10 ON "+
					"REG11_PAY_TYPES.COMPANY_CODE_SYS01=ACC02_SYS10.COMPANY_CODE_SYS01 and "+
					"REG11_PAY_TYPES.ACCOUNT_CODE_ACC02=ACC02_SYS10.ACCOUNT_CODE "+
					"where "+
					"REG11_PAY_TYPES.PROGRESSIVE_SYS10=SYS10.PROGRESSIVE and "+
          "REG11_PAY_TYPES.ENABLED='Y' and "+
					"SYS10.LANGUAGE_CODE=? and "+
					"REG11_PAY_TYPES.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG11","REG11_PAY_TYPES.COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentTypeCodeREG11","REG11_PAY_TYPES.PAYMENT_TYPE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG11","REG11_PAY_TYPES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG11","REG11_PAY_TYPES.ENABLED");
			attribute2dbField.put("accountCodeAcc02REG11","REG11_PAY_TYPES.ACCOUNT_CODE_ACC02");
			attribute2dbField.put("acc02DescriptionSYS10","ACC02_SYS10.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
			values.add(serverLanguageId);


      // read from REG11 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PaymentTypeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching payment types list",ex);
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
  public VOListResponse loadPayments(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
     if (this.conn==null) conn = getConn(); else conn = this.conn;

		 // retrieve companies list...
		 String companies = "";
		 if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
			 companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
		 }
		 else {
			 for(int i=0;i<companiesList.size();i++)
				 companies += "'"+companiesList.get(i).toString()+"',";
			 companies = companies.substring(0,companies.length()-1);
		 }


     String sql =
          "select REG10_PAY_MODES.COMPANY_CODE_SYS01,REG10_PAY_MODES.PAYMENT_CODE,REG10_PAY_MODES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "REG10_PAY_MODES.ENABLED,REG10_PAY_MODES.STEP,REG10_PAY_MODES.INSTALMENT_NUMBER,REG10_PAY_MODES.START_DAY,"+
          "REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11,REG10_PAY_MODES.FIRST_INSTALMENT_DAYS,SYS10_REG11.DESCRIPTION, "+
					"REG11_PAY_TYPES.ACCOUNT_CODE_ACC02 "+
          " from REG10_PAY_MODES,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS SYS10_REG11,REG11_PAY_TYPES where "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11=REG11_PAY_TYPES.PAYMENT_TYPE_CODE and "+
          "REG11_PAY_TYPES.PROGRESSIVE_SYS10=SYS10_REG11.PROGRESSIVE and "+
          "SYS10_REG11.LANGUAGE_CODE=? and "+
          "REG10_PAY_MODES.ENABLED='Y' and "+
					"REG10_PAY_MODES.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG10","REG10_PAY_MODES.COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentCodeREG10","REG10_PAY_MODES.PAYMENT_CODE");
      attribute2dbField.put("progressiveSys10REG10","REG10_PAY_MODES.PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabledREG10","REG10_PAY_MODES.ENABLED");
      attribute2dbField.put("stepREG10","REG10_PAY_MODES.STEP");
      attribute2dbField.put("instalmentNumberREG10","REG10_PAY_MODES.INSTALMENT_NUMBER");
      attribute2dbField.put("startDayREG10","REG10_PAY_MODES.START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11");
      attribute2dbField.put("firstInstalmentDaysREG10","REG10_PAY_MODES.FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionSYS10","SYS10_REG11.DESCRIPTION");
			attribute2dbField.put("accountCodeAcc02REG11","REG11_PAY_TYPES.ACCOUNT_CODE_ACC02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);


      // read from REG10 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PaymentVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching payments list",ex);
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
  public VOListResponse insertPayments(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      PaymentVO vo = null;
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeREG10","PAYMENT_CODE");
      attribute2dbField.put("progressiveSys10REG10","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG10","ENABLED");
      attribute2dbField.put("stepREG10","STEP");
      attribute2dbField.put("instalmentNumberREG10","INSTALMENT_NUMBER");
      attribute2dbField.put("startDayREG10","START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","PAYMENT_TYPE_CODE_REG11");
      attribute2dbField.put("firstInstalmentDaysREG10","FIRST_INSTALMENT_DAYS");
			attribute2dbField.put("companyCodeSys01REG10","COMPANY_CODE_SYS01");

      pstmt = conn.prepareStatement(
        "insert into REG17_PAY_INSTALMENTS(COMPANY_CODE_SYS01,PAYMENT_CODE_REG10,RATE_NUMBER,PAYMENT_TYPE_CODE_REG11,PERCENTAGE,INSTALMENT_DAYS) "+
        "values(?,?,?,?,?,?)"
      );

      int days;
      BigDecimal total;
      BigDecimal ratePerc = null;

      for(int j=0;j<list.size();j++) {
        vo = (PaymentVO)list.get(j);
        vo.setEnabledREG10("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01REG10(),conn);
        vo.setProgressiveSys10REG10(progressiveSYS10);

        // insert into REG10...
        Response res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG10_PAY_MODES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        // insert instalments into REG17...
        days = vo.getFirstInstalmentDaysREG10().intValue();
        total = new BigDecimal(0);
        ratePerc = null;
        for(int i=0;i<vo.getInstalmentNumberREG10().intValue();i++) {
					pstmt.setString(1,vo.getCompanyCodeSys01REG10());
          pstmt.setString(2,vo.getPaymentCodeREG10());
          pstmt.setInt(3,i+1);
          pstmt.setString(4,vo.getPaymentTypeCodeReg11REG10());
          if (i+1<vo.getInstalmentNumberREG10().intValue()) {
            ratePerc = new BigDecimal(100).divide(vo.getInstalmentNumberREG10(), 5,BigDecimal.ROUND_HALF_UP);
            total = total.add(ratePerc);
            pstmt.setBigDecimal(5,ratePerc);
          }
          else
            pstmt.setBigDecimal(5,new BigDecimal(100).subtract(total));
          pstmt.setInt(6,days);
          days += vo.getStepREG10().intValue();
          pstmt.execute();
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new payments", ex);
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
  public VOListResponse insertPaymentTypes(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      PaymentTypeVO vo = null;

      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG11","COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentTypeCodeREG11","PAYMENT_TYPE_CODE");
      attribute2dbField.put("progressiveSys10REG11","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG11","ENABLED");
      attribute2dbField.put("accountCodeAcc02REG11","ACCOUNT_CODE_ACC02");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (PaymentTypeVO)list.get(i);
        vo.setEnabledREG11("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01REG11(),conn);
        vo.setProgressiveSys10REG11(progressiveSYS10);

        // insert into REG11...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG11_PAY_TYPES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new payment types", ex);
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
  public VOListResponse updatePaymentInstalments(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      PaymentInstalmentVO oldVO = null;
      PaymentInstalmentVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01REG17");
      pkAttrs.add("paymentCodeReg10REG17");
      pkAttrs.add("rateNumberREG17");

      HashMap attr2dbFields = new HashMap();
			attr2dbFields.put("companyCodeSys01REG17","COMPANY_CODE_SYS01");
      attr2dbFields.put("paymentCodeReg10REG17","PAYMENT_CODE_REG10");
      attr2dbFields.put("rateNumberREG17","RATE_NUMBER");
      attr2dbFields.put("percentageREG17","PERCENTAGE");
      attr2dbFields.put("instalmentDaysREG17","INSTALMENT_DAYS");
      attr2dbFields.put("paymentTypeCodeReg11REG17","PAYMENT_TYPE_CODE_REG11");


      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (PaymentInstalmentVO)oldVOs.get(i);
        newVO = (PaymentInstalmentVO)newVOs.get(i);

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG17_PAY_INSTALMENTS",
            attr2dbFields,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating payment instalments",ex);
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
  public VOListResponse updatePayments(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      PaymentVO oldVO = null;
      PaymentVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01REG10");
      pkAttrs.add("paymentCodeREG10");

      HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG10","COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentCodeREG10","PAYMENT_CODE");
      attribute2dbField.put("startDayREG10","START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","PAYMENT_TYPE_CODE_REG11");

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (PaymentVO)oldVOs.get(i);
        newVO = (PaymentVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG10(),serverLanguageId,conn);

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG10_PAY_MODES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing payments",ex);
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
  public VOListResponse updatePaymentTypes(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      PaymentTypeVO oldVO = null;
      PaymentTypeVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01REG11");
      pkAttrs.add("paymentTypeCodeREG11");

      HashMap attr2dbFields = new HashMap();
			attr2dbFields.put("companyCodeSys01REG11","COMPANY_CODE_SYS01");
      attr2dbFields.put("paymentTypeCodeREG11","PAYMENT_TYPE_CODE");
      attr2dbFields.put("progressiveSys10REG11","PROGRESSIVE_SYS10");
      attr2dbFields.put("enabledREG11","ENABLED");
			attr2dbFields.put("accountCodeAcc02REG11","ACCOUNT_CODE_ACC02");

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (PaymentTypeVO)oldVOs.get(i);
        newVO = (PaymentTypeVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG11(),serverLanguageId,conn);

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG11_PAY_TYPES",
            attr2dbFields,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing payment types",ex);
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
  public VOListResponse validatePaymentTypeCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";

			// if client transmit a specific function code then use it to filter company code list by INSERT authorizations,
			// otherwise retrieve company code list from READ authorizations...
			if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
				companies = "'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"',";
			}
			else if (validationPars.getLookupValidationParameters().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT)!=null) {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
			} else {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
			}

			if (companies.length()>0)
				companies = companies.substring(0,companies.length()-1);

      String sql =
				"select REG11_PAY_TYPES.COMPANY_CODE_SYS01,REG11_PAY_TYPES.PAYMENT_TYPE_CODE,REG11_PAY_TYPES.PROGRESSIVE_SYS10,"+
				"SYS10.DESCRIPTION,REG11_PAY_TYPES.ENABLED,REG11_PAY_TYPES.ACCOUNT_CODE_ACC02,ACC02_SYS10.DESCRIPTION "+
				"from SYS10_TRANSLATIONS SYS10,REG11_PAY_TYPES "+
				"LEFT OUTER JOIN ("+
				"  SELECT ACC02_ACCOUNTS.COMPANY_CODE_SYS01,ACC02_ACCOUNTS.ACCOUNT_CODE,SYS10_TRANSLATIONS.DESCRIPTION "+
				"  FROM SYS10_TRANSLATIONS,ACC02_ACCOUNTS where "+
				"  ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"  SYS10_TRANSLATIONS.LANGUAGE_CODE=? "+
				") ACC02_SYS10 ON "+
				"REG11_PAY_TYPES.COMPANY_CODE_SYS01=ACC02_SYS10.COMPANY_CODE_SYS01 and "+
				"REG11_PAY_TYPES.ACCOUNT_CODE_ACC02=ACC02_SYS10.ACCOUNT_CODE "+
				"where "+
				"REG11_PAY_TYPES.PROGRESSIVE_SYS10=SYS10.PROGRESSIVE and "+
				"REG11_PAY_TYPES.ENABLED='Y' and "+
				"SYS10.LANGUAGE_CODE=? and "+
				"REG11_PAY_TYPES.COMPANY_CODE_SYS01 in ("+companies+") and "+
			  "REG11_PAY_TYPES.PAYMENT_TYPE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG11","REG11_PAY_TYPES.COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentTypeCodeREG11","REG11_PAY_TYPES.PAYMENT_TYPE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG11","REG11_PAY_TYPES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG11","REG11_PAY_TYPES.ENABLED");
			attribute2dbField.put("accountCodeAcc02REG11","REG11_PAY_TYPES.ACCOUNT_CODE_ACC02");
			attribute2dbField.put("acc02DescriptionSYS10","ACC02_SYS10.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
			values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from REG11 table...
      Response answer =  CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PaymentTypeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating payment type code",ex);
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
  public VOListResponse validatePaymentCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";

			// if client transmit a specific function code then use it to filter company code list by INSERT authorizations,
			// otherwise retrieve company code list from READ authorizations...
			if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
				companies = "'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"',";
			}
			else if (validationPars.getLookupValidationParameters().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT)!=null) {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
			} else {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
			}

			if (companies.length()>0)
				companies = companies.substring(0,companies.length()-1);

      String sql =
          "select REG10_PAY_MODES.COMPANY_CODE_SYS01,REG10_PAY_MODES.PAYMENT_CODE,REG10_PAY_MODES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "REG10_PAY_MODES.ENABLED,REG10_PAY_MODES.STEP,REG10_PAY_MODES.INSTALMENT_NUMBER,REG10_PAY_MODES.START_DAY,"+
          "REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11,REG10_PAY_MODES.FIRST_INSTALMENT_DAYS,SYS10_REG11.DESCRIPTION, "+
					"REG11_PAY_TYPES.ACCOUNT_CODE_ACC02 "+
          " from REG10_PAY_MODES,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS SYS10_REG11,REG11_PAY_TYPES where "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
					"REG10_PAY_MODES.COMPANY_CODE_SYS01=REG11_PAY_TYPES.COMPANY_CODE_SYS01 and "+
          "REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11=REG11_PAY_TYPES.PAYMENT_TYPE_CODE and "+
          "REG11_PAY_TYPES.PROGRESSIVE_SYS10=SYS10_REG11.PROGRESSIVE and "+
          "SYS10_REG11.LANGUAGE_CODE=? and "+
          "REG10_PAY_MODES.ENABLED='Y' and "+
					"REG10_PAY_MODES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG10_PAY_MODES.PAYMENT_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG10","REG10_PAY_MODES.COMPANY_CODE_SYS01");
      attribute2dbField.put("paymentCodeREG10","REG10_PAY_MODES.PAYMENT_CODE");
      attribute2dbField.put("progressiveSys10REG10","REG10_PAY_MODES.PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabledREG10","REG10_PAY_MODES.ENABLED");
      attribute2dbField.put("stepREG10","REG10_PAY_MODES.STEP");
      attribute2dbField.put("instalmentNumberREG10","REG10_PAY_MODES.INSTALMENT_NUMBER");
      attribute2dbField.put("startDayREG10","REG10_PAY_MODES.START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","REG10_PAY_MODES.PAYMENT_TYPE_CODE_REG11");
      attribute2dbField.put("firstInstalmentDaysREG10","REG10_PAY_MODES.FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionSYS10","SYS10_REG11.DESCRIPTION");
			attribute2dbField.put("accountCodeAcc02REG11","REG11_PAY_TYPES.ACCOUNT_CODE_ACC02");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from REG11 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PaymentVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating payment code",ex);
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
  public VOResponse deletePaymentTypes(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();

      PaymentTypeVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG11...
        vo = (PaymentTypeVO)list.get(i);
        stmt.execute("update REG11_PAY_TYPES set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG11()+"' and PAYMENT_TYPE_CODE='"+vo.getPaymentTypeCodeREG11()+"'");
      }

      Response answer = new VOResponse(new Boolean(true));


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing payment types",ex);
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
  public VOResponse deletePayments(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      PaymentVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG10...
        vo = (PaymentVO)list.get(i);
        stmt.execute("update REG10_PAY_MODES set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG10()+"' and PAYMENT_CODE='"+vo.getPaymentCodeREG10()+"'");
      }

      Response answer = new VOResponse(new Boolean(true));

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing payments",ex);
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



}

