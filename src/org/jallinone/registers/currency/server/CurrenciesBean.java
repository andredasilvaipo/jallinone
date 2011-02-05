package org.jallinone.registers.currency.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage currencies.</p>
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
public class CurrenciesBean  implements Currencies {


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




  public CurrenciesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public CurrencyConvVO getCurrencyConv() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse loadCompanyCurrency(String companyCode,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG03_CURRENCIES.CURRENCY_CODE,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.ENABLED "+
          "from REG03_CURRENCIES,SYS01_COMPANIES where "+
          "REG03_CURRENCIES.ENABLED='Y' and "+
          "SYS01_COMPANIES.COMPANY_CODE=? and "+
          "SYS01_COMPANIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("currencyCodeREG03","REG03_CURRENCIES.CURRENCY_CODE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("enabledREG03","REG03_CURRENCIES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(companyCode); // company code...


      // read from REG03 table...
      Response answer =  CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CurrencyVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching currency settings",ex);
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
  public VOListResponse loadCurrencies(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
     if (this.conn==null) conn = getConn(); else conn = this.conn;

     String sql =
          "select REG03_CURRENCIES.CURRENCY_CODE,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.ENABLED from REG03_CURRENCIES where "+
          "REG03_CURRENCIES.ENABLED='Y'";

			if (gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03)!=null)
				sql += " and NOT REG03_CURRENCIES.CURRENCY_CODE='"+gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03)+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("currencyCodeREG03","REG03_CURRENCIES.CURRENCY_CODE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("enabledREG03","REG03_CURRENCIES.ENABLED");

      ArrayList values = new ArrayList();


      // read from REG03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CurrencyVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching currencies list",ex);
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
  public VOListResponse loadCurrencyConvs(GridParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String currCode = (String)pars.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03);

      String sql =
					"select REG06_CURRENCY_CONVS.CURRENCY_CODE_REG03,REG06_CURRENCY_CONVS.CURRENCY_CODE2_REG03,"+
					"REG06_CURRENCY_CONVS.VALUE,REG06_CURRENCY_CONVS.START_DATE "+
					"from REG06_CURRENCY_CONVS where "+
					"REG06_CURRENCY_CONVS.CURRENCY_CODE_REG03=? ";
			ArrayList params = new ArrayList();
			params.add(currCode);

			if (pars.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
				sql +=
					" and REG06_CURRENCY_CONVS.START_DATE<=? "+
					" order by REG06_CURRENCY_CONVS.START_DATE DESC ";
				params.add( new java.sql.Date(((java.util.Date)pars.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)).getTime()) );

				pars = new GridParams(); // remove other filtering ordering conditions...
			}
      CurrencyConvVO vo = null;
      ArrayList list = new ArrayList();
      pstmt = conn.prepareStatement(sql);
			for(int i=0;i<params.size();i++)
				pstmt.setObject(i+1,params.get(i));
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = new CurrencyConvVO();
        vo.setCurrencyCodeReg03REG06(rset.getString(1));
        vo.setCurrencyCode2Reg03REG06(rset.getString(2));
        vo.setValueREG06(rset.getBigDecimal(3));
				vo.setStartDateREG06(rset.getDate(4));
        list.add(vo);
      }
      rset.close();

      return new VOListResponse(list,false,list.size());

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching currency conversions list",ex);
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
  public VOListResponse validateCurrencyCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      String sql =
					"select REG03_CURRENCIES.CURRENCY_CODE,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.ENABLED "+
					"from REG03_CURRENCIES where "+
					"REG03_CURRENCIES.ENABLED='Y' and "+
					"REG03_CURRENCIES.CURRENCY_CODE='"+validationPars.getCode()+"'";

				if (validationPars.getLookupValidationParameters().get(ApplicationConsts.CURRENCY_CODE_REG03)!=null)
					sql += " and NOT REG03_CURRENCIES.CURRENCY_CODE='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.CURRENCY_CODE_REG03)+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("currencyCodeREG03","REG03_CURRENCIES.CURRENCY_CODE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("enabledREG03","REG03_CURRENCIES.ENABLED");

      ArrayList values = new ArrayList();

      GridParams gridParams = new GridParams();

      // read from REG03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CurrencyVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating currency code",ex);
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
  public VOListResponse insertCurrency(CurrencyVO vo,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setEnabledREG03("Y");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("currencyCodeREG03","CURRENCY_CODE");
      attribute2dbField.put("currencySymbolREG03","CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","DECIMAL_SYMBOL");
      attribute2dbField.put("decimalsREG03","DECIMALS");
      attribute2dbField.put("enabledREG03","ENABLED");

      // insert into REG03...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "REG03_CURRENCIES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );
/*
      // insert records in REG06...
      pstmt = conn.prepareStatement(
          "insert into REG06_CURRENCY_CONVS(CURRENCY_CODE_REG03,CURRENCY_CODE2_REG03,START_DATE) "+
          "select '"+vo.getCurrencyCodeREG03()+"',CURRENCY_CODE,? from REG03_CURRENCIES where CURRENCY_CODE <> '"+vo.getCurrencyCodeREG03()+"' and ENABLED='Y'"
      );
			pstmt.setDate(1,new java.sql.Date(System.currentTimeMillis()));
			pstmt.execute();
			pstmt.close();

      pstmt = conn.prepareStatement(
          "insert into REG06_CURRENCY_CONVS(CURRENCY_CODE2_REG03,CURRENCY_CODE_REG03,START_DATE) "+
          "select '"+vo.getCurrencyCodeREG03()+"',CURRENCY_CODE,? from REG03_CURRENCIES where CURRENCY_CODE <> '"+vo.getCurrencyCodeREG03()+"' and ENABLED='Y'"
      );
			pstmt.setDate(1,new java.sql.Date(System.currentTimeMillis()));
			pstmt.execute();
			pstmt.close();
*/
      ArrayList list = new ArrayList();
      list.add(vo);

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new currency", ex);
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
		public VOListResponse insertCurrencyConvs(ArrayList vos,String serverLanguageId,String username) throws Throwable {
			Connection conn = null;
			try {
				if (this.conn==null) conn = getConn(); else conn = this.conn;

				Map attribute2dbField = new HashMap();
				attribute2dbField.put("currencyCodeReg03REG06","CURRENCY_CODE_REG03");
				attribute2dbField.put("currencyCode2Reg03REG06","CURRENCY_CODE2_REG03");
				attribute2dbField.put("startDateREG06","START_DATE");
				attribute2dbField.put("valueREG06","VALUE");

				// insert into REG03...
				Response res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vos,
						"REG06_CURRENCY_CONVS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError())
					throw new Exception(res.getErrorMessage());


	      ArrayList inverseVOs = new ArrayList();
				CurrencyConvVO vo = null;
				String currCode = null;
	      for(int i=0;i<vos.size();i++) {
					vo = (CurrencyConvVO)((CurrencyConvVO)vos.get(i)).clone();
					currCode = vo.getCurrencyCode2Reg03REG06();
					vo.setCurrencyCode2Reg03REG06(vo.getCurrencyCodeReg03REG06());
					vo.setCurrencyCodeReg03REG06(currCode);
					vo.setValueREG06(new BigDecimal(1).divide( vo.getValueREG06(),5,BigDecimal.ROUND_HALF_UP ));
					inverseVOs.add(vo);
				}
				Response res2 = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						inverseVOs,
						"REG06_CURRENCY_CONVS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res2.isError())
					throw new Exception(res2.getErrorMessage());

				return new VOListResponse(vos,false,vos.size());
			}
			catch (Throwable ex) {
				Logger.error(username, this.getClass().getName(),
										 "executeCommand", "Error while inserting new currency conversions", ex);
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
  public VOListResponse updateCurrencies(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      CurrencyVO oldVO = null;
      CurrencyVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CurrencyVO)oldVOs.get(i);
        newVO = (CurrencyVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("currencyCodeREG03");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("currencyCodeREG03","CURRENCY_CODE");
        attr2dbFields.put("currencySymbolREG03","CURRENCY_SYMBOL");
        attr2dbFields.put("thousandSymbolREG03","THOUSAND_SYMBOL");
        attr2dbFields.put("decimalSymbolREG03","DECIMAL_SYMBOL");
        attr2dbFields.put("decimalsREG03","DECIMALS");
        attr2dbFields.put("enabledREG03","ENABLED");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG03_CURRENCIES",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing currencies",ex);
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
  public VOListResponse updateCurrencyConvs(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CurrencyConvVO oldVO = null;
      CurrencyConvVO newVO = null;
      Response res = null;
      BigDecimal conv = null;
      BigDecimal invConv = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CurrencyConvVO)oldVOs.get(i);
        newVO = (CurrencyConvVO)newVOs.get(i);
        conv = null;
        invConv = null;
        if (newVO.getValueREG06()!=null) {
          conv = newVO.getValueREG06().setScale(5,BigDecimal.ROUND_HALF_UP);
          invConv = new BigDecimal(1d/newVO.getValueREG06().doubleValue()).setScale(5,BigDecimal.ROUND_HALF_UP);
        }

				pstmt = conn.prepareStatement(
						"update REG06_CURRENCY_CONVS set VALUE="+
						(newVO.getValueREG06()==null?"null":conv.toString())+
						" where "+
						"CURRENCY_CODE_REG03='"+newVO.getCurrencyCodeReg03REG06()+"' and "+
						"CURRENCY_CODE2_REG03='"+newVO.getCurrencyCode2Reg03REG06()+"' and "+
						"START_DATE=? and "+
						"VALUE"+(oldVO.getValueREG06()==null?" is null":"="+oldVO.getValueREG06())
				);
				pstmt.setDate(1,newVO.getStartDateREG06());
				pstmt.execute();
				pstmt.close();

	      pstmt = conn.prepareStatement(
					"update REG06_CURRENCY_CONVS set VALUE="+
					(newVO.getValueREG06()==null?"null":invConv.toString())+
					" where "+
					"CURRENCY_CODE_REG03='"+newVO.getCurrencyCode2Reg03REG06()+"' and "+
					"CURRENCY_CODE2_REG03='"+newVO.getCurrencyCodeReg03REG06()+"' and "+
				  "START_DATE=? "
				);
				pstmt.setDate(1,newVO.getStartDateREG06());
				pstmt.execute();
				pstmt.close();

      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing currency conversions",ex);
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

      } catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOResponse deleteCurrencies(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;



      stmt = conn.createStatement();

      CurrencyVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG03...
        vo = (CurrencyVO)list.get(i);
        stmt.execute("update REG03_CURRENCIES set ENABLED='N' where CURRENCY_CODE='"+vo.getCurrencyCodeREG03()+"'");

        // delete records from REG06...
        stmt.execute(
            "delete from REG06_CURRENCY_CONVS where CURRENCY_CODE_REG03 = '"+vo.getCurrencyCodeREG03()+"' or CURRENCY_CODE2_REG03 = '"+vo.getCurrencyCodeREG03()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing currencies",ex);
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
		public VOResponse deleteCurrencyConvs(ArrayList list,String serverLanguageId,String username) throws Throwable {
			PreparedStatement pstmt = null;
			Connection conn = null;
			try {
				if (this.conn==null) conn = getConn(); else conn = this.conn;
				CurrencyConvVO vo = null;

				pstmt = conn.prepareStatement(
						"delete from REG06_CURRENCY_CONVS where CURRENCY_CODE_REG03 = ? and CURRENCY_CODE2_REG03 = ? and START_DATE=?"
				);

				for(int i=0;i<list.size();i++) {
					// delete records from REG06...
					vo = (CurrencyConvVO)list.get(i);
					pstmt.setString(1,vo.getCurrencyCodeReg03REG06());
					pstmt.setString(2,vo.getCurrencyCode2Reg03REG06());
					pstmt.setDate(3,vo.getStartDateREG06());
					pstmt.execute();
				}

				return new VOResponse(new Boolean(true));
			}
			catch (Throwable ex) {
				Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing currency conversions",ex);
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



}

