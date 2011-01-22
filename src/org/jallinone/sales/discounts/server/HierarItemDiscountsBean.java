package org.jallinone.sales.discounts.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.java.*;
import org.jallinone.system.server.*;
import java.math.*;

import org.openswing.swing.message.send.java.*;
import org.jallinone.subjects.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.sales.discounts.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage hierarchy item discounts in SAL03/SAL05 tables.</p>
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
public class HierarItemDiscountsBean  implements HierarItemDiscounts {


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

  private DiscountBean discountBean;
  
  public void setDiscountBean(DiscountBean discountBean) {
	  this.discountBean = discountBean;
  }


  public HierarItemDiscountsBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public DiscountVO getDiscount() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public HierarItemDiscountVO getHierarItemDiscount() {
	  throw new UnsupportedOperationException();	
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadHierarItemDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);

      BigDecimal progressiveHIE01 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

      // retrieve COMPANY_CODE from progressiveHIE02...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select COMPANY_CODE_SYS01 from ITM02_ITEM_TYPES where PROGRESSIVE_HIE02="+progressiveHIE02
      );
      String companyCodeSYS01 = null;
      if(rset.next())
        companyCodeSYS01 = rset.getString(1);
      else {
        rset.close();
        return new VOListResponse("Item hierarchy not found.");
      }
      rset.close();

      // retrieve discounts...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL05_ITEM_HIERAR_DISCOUNTS where "+
          "COMPANY_CODE_SYS01='"+companyCodeSYS01+"' and PROGRESSIVE_HIE01="+progressiveHIE01
      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = discountBean.getDiscountsList(
          companyCodeSYS01,
          discountCodes,
          serverLanguageId,
          gridParams,
          username,
          HierarItemDiscountVO.class
      );
      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          ((HierarItemDiscountVO)rows.get(i)).setProgressiveHie01SAL05(progressiveHIE01);
        }

      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching hierarchy item discounts list",ex);
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
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}    
      try {
    	  discountBean.setConn(null);
      } catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOListResponse updateHierarItemDiscounts(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);

      DiscountVO oldVO = null;
      DiscountVO newVO = null;

      Response res = null;
      for(int i=0;i<newVOs.size();i++) {
        oldVO = (DiscountVO)oldVOs.get(i);
        newVO = (DiscountVO)newVOs.get(i);
        res = discountBean.updateDiscount(oldVO,newVO,serverLanguageId,username);
        if (res.isError()) {
          try {
          }
          catch (Exception ex4) {
          }
          throw new Exception(res.getErrorMessage());
        }

      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating hierarchy item discounts",ex);
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
    	  discountBean.setConn(null);
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



  /**
   * Business logic to execute.
   */
  public VOListResponse insertHierarItemDiscounts(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);

      HierarItemDiscountVO vo = null;
      ResultSet rset = null;
      stmt = conn.createStatement();
      for(int i=0;i<list.size();i++) {
        vo = (HierarItemDiscountVO)list.get(i);
        vo.setDiscountTypeSAL03(ApplicationConsts.DISCOUNT_CUSTOMER);

        // retrieve COMPANY_CODE from progressiveHIE01...
        rset = stmt.executeQuery(
            "select COMPANY_CODE_SYS01 from ITM02_ITEM_TYPES where PROGRESSIVE_HIE02 in "+
            "(select PROGRESSIVE_HIE02 from HIE01_LEVELS where PROGRESSIVE="+vo.getProgressiveHie01SAL05()+")"
        );
        if(rset.next())
          vo.setCompanyCodeSys01SAL03(rset.getString(1));
        else {
          rset.close();
          return new VOListResponse("Item hierarchy not found.");
        }
        rset.close();

        discountBean.insertDiscount(vo);

        stmt.execute(
            "insert into SAL05_ITEM_HIERAR_DISCOUNTS(COMPANY_CODE_SYS01,PROGRESSIVE_HIE01,DISCOUNT_CODE_SAL03) "+
            "values('"+vo.getCompanyCodeSys01SAL03()+"',"+vo.getProgressiveHie01SAL05()+",'"+vo.getDiscountCodeSAL03()+"')"
        );
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting hierarchy item discounts",ex);
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
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}    
      try {
    	  discountBean.setConn(null);   	  
      } catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOResponse deleteHierarItemDiscounts(ArrayList vos,String serverLanguageId,String username) throws Throwable {
    
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);

      HierarItemDiscountVO vo = null;
      stmt = conn.createStatement();


      for(int i=0;i<vos.size();i++) {
        vo = (HierarItemDiscountVO)vos.get(i);

        stmt.execute(
            "delete from SAL05_ITEM_HIERAR_DISCOUNTS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL03()+"' and "+
            "PROGRESSIVE_HIE01="+vo.getProgressiveHie01SAL05()+" and DISCOUNT_CODE_SAL03='"+vo.getDiscountCodeSAL03()+"'"
        );

        discountBean.deleteDiscount(vo);

      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting hierarchy item discounts",ex);
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
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}    
      try {
    	  discountBean.setConn(null);
      } catch (Exception ex) {}
    }

  }



}

