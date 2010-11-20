package org.jallinone.system.companies.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.system.companies.java.CompanyVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage companies.</p>
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
public class CompaniesBean  implements Companies {


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


  private OrganizationBean bean;

  public void setBean(OrganizationBean bean) {
	  this.bean = bean;
  }




  public CompaniesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public CompanyVO getCompany() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse loadCompany(String companyCode,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,REG04_SUBJECTS.WEB_SITE,"+
          "REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE,SYS01_COMPANIES.CURRENCY_CODE_REG03,REG04_SUBJECTS.PROGRESSIVE "+
          "from REG04_SUBJECTS,SYS01_COMPANIES where "+
          "COMPANY_CODE_SYS01='"+companyCode+"' and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SYS01_COMPANIES.COMPANY_CODE and "+
          "SYS01_COMPANIES.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M'"
      );
      OrganizationVO vo = new OrganizationVO();
      if(rset.next()) {
        vo.setCompanyCodeSys01REG04(companyCode);
        vo.setSubjectTypeREG04(rset.getString(1));
        vo.setName_1REG04(rset.getString(2));
        vo.setName_2REG04(rset.getString(3));
        vo.setAddressREG04(rset.getString(4));

        vo.setCityREG04(rset.getString(5));
        vo.setZipREG04(rset.getString(6));
        vo.setProvinceREG04(rset.getString(7));
        vo.setCountryREG04(rset.getString(8));
        vo.setTaxCodeREG04(rset.getString(9));
        vo.setPhoneNumberREG04(rset.getString(10));
        vo.setFaxNumberREG04(rset.getString(11));
        vo.setEmailAddressREG04(rset.getString(12));
        vo.setWebSiteREG04(rset.getString(13));
        vo.setLawfulSiteREG04(rset.getString(14));
        vo.setNoteREG04(rset.getString(15));
        vo.setCurrencyCodeReg03(rset.getString(16));
        vo.setProgressiveREG04(rset.getBigDecimal(17));
        rset.close();

      }
      else {
        rset.close();
        throw new Exception("Record not found.");
      }
      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching company detail",ex);
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
  public VOResponse updateCompany(OrganizationVO oldVO,OrganizationVO newVO,String t1,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

      Response res = bean.update(oldVO,newVO,t1,serverLanguageId,username);
      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing company",ex);
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
  public VOResponse insertCompany(OrganizationVO vo,String t1,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);
      stmt = conn.createStatement();


      // insert record in SYS01...
      pstmt = conn.prepareStatement(
          "insert into SYS01_COMPANIES(COMPANY_CODE,CURRENCY_CODE_REG03,CREATION_DATE,ENABLED) "+
          "VALUES('"+vo.getCompanyCodeSys01REG04()+"','"+vo.getCurrencyCodeReg03()+"',?,'Y')"
      );
      pstmt.setTimestamp(1,new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.execute();
      pstmt.close();

      vo.setProgressiveREG04(new BigDecimal(2));
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_MY_COMPANY);
      bean.insert(false,vo,t1,serverLanguageId,username);

      // add grants of the new company code to ADMIN user...
      stmt.execute(
          "INSERT INTO SYS02_COMPANIES_ACCESS(COMPANY_CODE_SYS01,PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) "+
          "SELECT '"+vo.getCompanyCodeSys01REG04()+"',2,FUNCTION_CODE_SYS06,'Y','Y','Y' FROM SYS07_ROLE_FUNCTIONS,SYS06_FUNCTIONS WHERE FUNCTION_CODE_SYS06=FUNCTION_CODE AND USE_COMPANY_CODE='Y' and SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04=2"
      );
      pstmt.close();

      // insert company description...
      pstmt = conn.prepareStatement(
        "INSERT INTO REG04_SUBJECTS(COMPANY_CODE_SYS01,PROGRESSIVE,NAME_1,SUBJECT_TYPE,ENABLED) "+
        "VALUES(?,2,?,?,'Y')"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getName_1REG04());
      pstmt.setString(3,ApplicationConsts.SUBJECT_MY_COMPANY);
      pstmt.execute();
      pstmt.close();

      // retrieve the first company code defined, that will be used to clone data defined per company...
      pstmt = conn.prepareStatement(
        "SELECT COMPANY_CODE FROM SYS01_COMPANIES WHERE ENABLED='Y' ORDER BY CREATION_DATE ASC"
      );
      ResultSet rset = pstmt.executeQuery();
      rset.next();
      String companyCode = rset.getString(1);
      rset.close();
      pstmt.close();


      // insert company report customizations...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS15_REPORT_CUSTOMIZATIONS(COMPANY_CODE_SYS01,FUNCTION_CODE_SYS06,REPORT_NAME) "+
        "SELECT ?,FUNCTION_CODE_SYS06,REPORT_NAME FROM SYS15_REPORT_CUSTOMIZATIONS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


      // insert company user parameters...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) "+
        "SELECT ?,USERNAME_SYS03,PARAM_CODE,VALUE FROM SYS19_USER_PARAMS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


      // insert company user parameters...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) "+
        "SELECT DISTINCT ?,PARAM_CODE,VALUE FROM SYS21_COMPANY_PARAMS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


			// insert initial value for progressives...
			pstmt = conn.prepareStatement("SELECT COUNT(*) FROM SYS01_COMPANIES");
			rset = pstmt.executeQuery();
			rset.next();
			int companies = rset.getInt(1);
			rset.close();
			pstmt.close();
			pstmt = conn.prepareStatement(
				"UPDATE SYS21_COMPANY_PARAMS SET VALUE=? WHERE COMPANY_CODE_SYS01=? AND PARAM_CODE=?"
			);
 		  pstmt.setString(1,String.valueOf(companies+2)); // progressive 2 is locked for db initialization...
			pstmt.setString(2,vo.getCompanyCodeSys01REG04());
			pstmt.setString(3,ApplicationConsts.INITIAL_VALUE);
			int rows = pstmt.executeUpdate();
			pstmt.close();


      // insert company ledger...
      cloneRecordsAndDescriptions(
          conn,
          "LEDGER_CODE,PROGRESSIVE_SYS10,ENABLED,ACCOUNT_TYPE",
          "ACC01_LEDGER",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company char of accounts...
      cloneRecordsAndDescriptions(
          conn,
          "ACCOUNT_CODE,LEDGER_CODE_ACC01,PROGRESSIVE_SYS10,ENABLED,ACCOUNT_TYPE,CAN_DEL",
          "ACC02_ACCOUNTS",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company vat registers...
      cloneRecordsAndDescriptions(
          conn,
          "REGISTER_CODE,PROGRESSIVE_SYS10,REGISTER_TYPE,READ_ONLY,ACCOUNT_CODE_ACC02,ENABLED",
          "ACC04_VAT_REGISTERS",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company variants definitions...
      cloneRecordsAndDescriptions(
          conn,
          "TABLE_NAME,PROGRESSIVE_SYS10,USE_VARIANT_TYPE",
          "ITM21_VARIANTS",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company variant types...
      cloneRecordsAndDescriptions(
          conn,
          "VARIANT_TYPE,PROGRESSIVE_SYS10,ENABLED",
              "ITM06_VARIANT_TYPES_1",
              companyCode,
              vo.getCompanyCodeSys01REG04()
          );
      cloneRecordsAndDescriptions(
          conn,
          "VARIANT_TYPE,PROGRESSIVE_SYS10,ENABLED",
          "ITM07_VARIANT_TYPES_2",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
          conn,
          "VARIANT_TYPE,PROGRESSIVE_SYS10,ENABLED",
          "ITM08_VARIANT_TYPES_3",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
          conn,
          "VARIANT_TYPE,PROGRESSIVE_SYS10,ENABLED",
          "ITM09_VARIANT_TYPES_4",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
          conn,
          "VARIANT_TYPE,PROGRESSIVE_SYS10,ENABLED",
          "ITM10_VARIANT_TYPES_5",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company variant types...
      cloneRecordsAndDescriptions(
           conn,
           "VARIANT_CODE,VARIANT_TYPE_ITM06,PROGRESSIVE_SYS10,ENABLED",
           "ITM11_VARIANTS_1",
           companyCode,
           vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
           conn,
           "VARIANT_CODE,VARIANT_TYPE_ITM07,PROGRESSIVE_SYS10,ENABLED",
           "ITM12_VARIANTS_2",
           companyCode,
           vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
           conn,
           "VARIANT_CODE,VARIANT_TYPE_ITM08,PROGRESSIVE_SYS10,ENABLED",
           "ITM13_VARIANTS_3",
           companyCode,
           vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
           conn,
           "VARIANT_CODE,VARIANT_TYPE_ITM09,PROGRESSIVE_SYS10,ENABLED",
           "ITM14_VARIANTS_4",
           companyCode,
           vo.getCompanyCodeSys01REG04()
      );
      cloneRecordsAndDescriptions(
           conn,
           "VARIANT_CODE,VARIANT_TYPE_ITM10,PROGRESSIVE_SYS10,ENABLED",
           "ITM15_VARIANTS_5",
           companyCode,
           vo.getCompanyCodeSys01REG04()
      );

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new company",ex);
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
      catch (Exception ex2) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
			try {
				bean.setConn(null);
			}
			catch (Exception ex1) {
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
   * Clone records of "tableName" table having "oldCompanyCode" company code to "newCompanyCode" and
   * clone records in SYS10, too.
   */
  private void cloneRecordsAndDescriptions(Connection conn,String selectFields,String tableName,String oldCompanyCode,String newCompanyCode) throws Exception {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;
    try {
      int index = 0;
      int count = 0;
      StringTokenizer st = new StringTokenizer(selectFields,",");
      while(st.hasMoreTokens()) {
        if (st.nextToken().equals("PROGRESSIVE_SYS10"))
          index = count;
        count++;
      }
      String aux = "";
      for(int i=1;i<count;i++)
        aux += "?,";
      aux +="?";

      pstmt = conn.prepareStatement(
        "select "+selectFields+" from "+tableName+" where COMPANY_CODE_SYS01='"+oldCompanyCode+"'"
      );
      pstmt2 = conn.prepareStatement(
        "insert into "+tableName+"(COMPANY_CODE_SYS01,"+selectFields+") values('"+newCompanyCode+"',"+aux+")"
      );
      pstmt3 = conn.prepareStatement(
        "insert into SYS10_TRANSLATIONS(PROGRESSIVE,LANGUAGE_CODE,DESCRIPTION) "+
        "select ?,LANGUAGE_CODE,DESCRIPTION from SYS10_TRANSLATIONS where PROGRESSIVE=?"
      );

      ResultSet rset = pstmt.executeQuery();
      Object oldProgressive = null;
      BigDecimal newProgressive = null;
      while(rset.next()) {
        for(int i=1;i<=count;i++)
          if (i-1!=index)
            pstmt2.setObject(i,rset.getObject(i));
          else {
            oldProgressive = rset.getObject(i);
            newProgressive = CompanyProgressiveUtils.getInternalProgressive(newCompanyCode,"SYS10_TRANSLATIONS","PROGRESSIVE",conn);
            pstmt2.setObject(i,newProgressive);
          }
        pstmt2.execute();

        pstmt3.setBigDecimal(1,newProgressive);
        pstmt3.setObject(2,oldProgressive);
        pstmt3.execute();

      }
      rset.close();

    }
    catch (Exception ex) {
        throw ex;
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt3.close();
      }
      catch (Exception ex3) {
      }

    }
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadCompanies(String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select SYS01_COMPANIES.COMPANY_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.PROGRESSIVE from SYS01_COMPANIES,REG04_SUBJECTS where "+
          "SYS01_COMPANIES.COMPANY_CODE=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SYS01_COMPANIES.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M'"
      );
      CompanyVO vo = null;
      ArrayList list = new ArrayList();
      while(rset.next()) {
        vo = new CompanyVO();
        vo.setCompanyCodeSYS01(rset.getString(1));
        vo.setName_1REG04(rset.getString(2));
        vo.setProgressiveREG04(rset.getBigDecimal(3));
        list.add(vo);
      }

      rset.close();
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching companies list",ex);
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
  public VOResponse deleteCompany(SubjectPK pk,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();


      // logically delete the record in SYS01...
      stmt.execute("update SYS01_COMPANIES set ENABLED='N' where COMPANY_CODE='"+pk.getCompanyCodeSys01REG04()+"'");

      // phisically delete records in SYS02, linked to this company code...
      stmt.execute("delete from SYS02_COMPANIES_ACCESS where COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01REG04()+"'");

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing company",ex);
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
      catch (Exception ex2) {
      }
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



}

