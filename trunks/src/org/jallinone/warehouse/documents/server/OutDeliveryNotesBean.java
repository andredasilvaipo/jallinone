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
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.LoadSaleDocBean;
import org.jallinone.sales.documents.server.SaleDocsBean;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridDeliveryNoteVO;
import org.jallinone.warehouse.documents.java.GridOutDeliveryNoteRowVO;
import org.jallinone.warehouse.documents.java.OutDeliveryNoteRowPK;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.movements.server.AddMovementBean;
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
 * * <p>Description: Bean used to manage out delivery notes.</p>
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
public class OutDeliveryNotesBean  implements OutDeliveryNotes {


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


  private AddMovementBean movBean;

  public void setMovBean(AddMovementBean movBean) {
	  this.movBean = movBean;
  }

  private LoadSaleDocBean loadSaleDocBean;


  public void setLoadSaleDocBean(LoadSaleDocBean loadSaleDocBean) {
	  this.loadSaleDocBean = loadSaleDocBean;
  }

  private WarehouseUtilsBean bean;

  public void setBean(WarehouseUtilsBean bean) {
	  this.bean = bean;
  }


  public OutDeliveryNotesBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse insertOutDeliveryNote(DetailDeliveryNoteVO vo,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCode = companiesList.get(0).toString();

      vo.setEnabledDOC08("Y");
      vo.setDescriptionDOC08(vo.getWarehouseDescriptionDOC08());

      if (vo.getCompanyCodeSys01DOC08()==null)
        vo.setCompanyCodeSys01DOC08(companyCode);

      // generate the internal progressive for the document...
      vo.setDocNumberDOC08( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC08(),"DOC08_DELIVERY_NOTES","DOC_NUMBER",conn) );

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC_REF");

      attribute2dbField.put("addressDOC08","ADDRESS");
      attribute2dbField.put("cityDOC08","CITY");
      attribute2dbField.put("provinceDOC08","PROVINCE");
      attribute2dbField.put("countryDOC08","COUNTRY");
      attribute2dbField.put("zipDOC08","ZIP");
      attribute2dbField.put("noteDOC08","NOTE");
      attribute2dbField.put("enabledDOC08","ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","CARRIER_CODE_REG09");
      attribute2dbField.put("progressiveReg04DOC08","PROGRESSIVE_REG04");
      attribute2dbField.put("deliveryDateDOC08","DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","TRANSPORT_MOTIVE_CODE_REG20");

      // insert into DOC08...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC08_DELIVERY_NOTES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;




      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new out delivery note",ex);
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
  public VOResponse insertOutDeliveryNoteRow(GridOutDeliveryNoteRowVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);
      vo.setInvoiceQtyDOC10(new BigDecimal(0));

      // check if note in DOC08 must be updated with delivery note defined in DOC01/DOC06...
      String note = "";
      pstmt = conn.prepareStatement(
        "select DELIVERY_NOTE FROM DOC01_SELLING WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? AND "+
        "NOT EXISTS(SELECT * FROM DOC10_OUT_DELIVERY_NOTE_ITEMS WHERE "+
        "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? AND "+
        "DOC_TYPE_DOC01=? and DOC_YEAR_DOC01=? and DOC_NUMBER_DOC01=? )"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC10());
      pstmt.setString(2,vo.getDocTypeDoc01DOC10());
      pstmt.setBigDecimal(3,vo.getDocYearDoc01DOC10());
      pstmt.setBigDecimal(4,vo.getDocNumberDoc01DOC10());
      pstmt.setString(5,vo.getCompanyCodeSys01DOC10());
      pstmt.setString(6,vo.getDocTypeDOC10());
      pstmt.setBigDecimal(7,vo.getDocYearDOC10());
      pstmt.setBigDecimal(8,vo.getDocNumberDOC10());
      pstmt.setString(9,vo.getDocTypeDoc01DOC10());
      pstmt.setString(10,vo.getDocTypeDoc01DOC10());
      pstmt.setBigDecimal(11,vo.getDocNumberDoc01DOC10());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        note = rset.getString(1);
      }
      rset.close();
      pstmt.close();
      if (note!=null && !note.equals("")) {
        // retrieve current note...
        pstmt = conn.prepareStatement(
          "SELECT NOTE FROM DOC08_DELIVERY_NOTES "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,vo.getCompanyCodeSys01DOC10());
        pstmt.setString(2,vo.getDocTypeDOC10());
        pstmt.setBigDecimal(3,vo.getDocYearDOC10());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC10());
        rset = pstmt.executeQuery();
        String currentNote = null;
        if (rset.next()) {
          currentNote = rset.getString(1);
        }
        rset.close();
        pstmt.close();
        if (currentNote==null)
          currentNote = note;
        else
          currentNote = currentNote+"\n"+note;

        // update note...
        pstmt = conn.prepareStatement(
          "UPDATE DOC08_DELIVERY_NOTES SET NOTE=? "+
          "WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE=? AND DOC_YEAR=? AND DOC_NUMBER=? "
        );
        pstmt.setString(1,currentNote);
        pstmt.setString(2,vo.getCompanyCodeSys01DOC10());
        pstmt.setString(3,vo.getDocTypeDOC10());
        pstmt.setBigDecimal(4,vo.getDocYearDOC10());
        pstmt.setBigDecimal(5,vo.getDocNumberDOC10());
        pstmt.execute();
        pstmt.close();

      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC10","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC10","DOC_TYPE");
      attribute2dbField.put("docYearDOC10","DOC_YEAR");
      attribute2dbField.put("docNumberDOC10","DOC_NUMBER");
      attribute2dbField.put("docTypeDoc01DOC10","DOC_TYPE_DOC01");
      attribute2dbField.put("docYearDoc01DOC10","DOC_YEAR_DOC01");
      attribute2dbField.put("docNumberDoc01DOC10","DOC_NUMBER_DOC01");
      attribute2dbField.put("rowNumberDOC10","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC10","ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC10","QTY");
      attribute2dbField.put("progressiveHie02DOC10","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01DOC10","PROGRESSIVE_HIE01");
      attribute2dbField.put("docSequenceDoc01DOC10","DOC_SEQUENCE_DOC01");
      attribute2dbField.put("invoiceQtyDOC10","INVOICE_QTY");

      attribute2dbField.put("progressiveDOC10","PROGRESSIVE");
      attribute2dbField.put("variantTypeItm06DOC10","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC10","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC10","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC10","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC10","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC10","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC10","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC10","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC10","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC10","VARIANT_CODE_ITM15");

      vo.setRowNumberDOC10( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC10(),"DOC10_IN_DELIVERY_NOTE_ITEMS","ROW_NUMBER",conn) );
      vo.setProgressiveDOC10( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC10(),"DOC10_IN_DELIVERY_NOTE_ITEMS","PROGRESSIVE",conn) );
/*
      vo.setVariantCodeItm11DOC10(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm06DOC10(ApplicationConsts.JOLLY);

      vo.setVariantCodeItm12DOC10(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm13DOC10(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm14DOC10(ApplicationConsts.JOLLY);
      vo.setVariantCodeItm15DOC10(ApplicationConsts.JOLLY);

      vo.setVariantTypeItm07DOC10(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm08DOC10(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm09DOC10(ApplicationConsts.JOLLY);
      vo.setVariantTypeItm10DOC10(ApplicationConsts.JOLLY);
*/

      // insert into DOC10...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC10_OUT_DELIVERY_NOTE_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // update delivery note state...
      pstmt = conn.prepareStatement("update DOC08_DELIVERY_NOTES set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC10());
      pstmt.setString(3,vo.getDocTypeDOC10());
      pstmt.setBigDecimal(4,vo.getDocYearDOC10());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC10());
      pstmt.execute();

      // insert serial numbers...
      if (vo.getSerialNumbers().size()>0) {
        res = bean.reinsertOutSerialNumbers(vo,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new out delivery note row",ex);
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
  public VOResponse loadOutDeliveryNote(DeliveryNotePK pk,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC08_DELIVERY_NOTES.DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC08_DELIVERY_NOTES.DOC_REF");
      attribute2dbField.put("warehouseDescriptionDOC08","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("addressDOC08","DOC08_DELIVERY_NOTES.ADDRESS");
      attribute2dbField.put("cityDOC08","DOC08_DELIVERY_NOTES.CITY");
      attribute2dbField.put("provinceDOC08","DOC08_DELIVERY_NOTES.PROVINCE");
      attribute2dbField.put("countryDOC08","DOC08_DELIVERY_NOTES.COUNTRY");
      attribute2dbField.put("zipDOC08","DOC08_DELIVERY_NOTES.ZIP");
      attribute2dbField.put("noteDOC08","DOC08_DELIVERY_NOTES.NOTE");
      attribute2dbField.put("enabledDOC08","DOC08_DELIVERY_NOTES.ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09");
      attribute2dbField.put("carrierDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");
      attribute2dbField.put("deliveryDateDOC08","DOC08_DELIVERY_NOTES.DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20");
      attribute2dbField.put("transportMotiveDescriptionSYS10","SYS10_TRANSLATIONS_B.DESCRIPTION");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC08");
      pkAttributes.add("docTypeDOC08");
      pkAttributes.add("docYearDOC08");
      pkAttributes.add("docNumberDOC08");

      String baseSQL =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.ADDRESS,DOC08_DELIVERY_NOTES.CITY,DOC08_DELIVERY_NOTES.PROVINCE,DOC08_DELIVERY_NOTES.COUNTRY,DOC08_DELIVERY_NOTES.ZIP,"+
          "DOC08_DELIVERY_NOTES.NOTE,DOC08_DELIVERY_NOTES.ENABLED,DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,DOC08_DELIVERY_NOTES.DOC_SEQUENCE, "+
          "DOC08_DELIVERY_NOTES.DELIVERY_DATE,DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20,SYS10_TRANSLATIONS_B.DESCRIPTION "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,REG09_CARRIERS,SYS10_TRANSLATIONS,SAL07_CUSTOMERS,REG04_SUBJECTS,"+
          "REG20_TRANSPORT_MOTIVES,SYS10_TRANSLATIONS SYS10_TRANSLATIONS_B where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09=REG09_CARRIERS.CARRIER_CODE and "+
          "REG09_CARRIERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=? and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
          "DOC08_DELIVERY_NOTES.DOC_YEAR=? and "+
          "DOC08_DELIVERY_NOTES.DOC_NUMBER=? and "+
          "DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20=REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE and "+
          "REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS_B.PROGRESSIVE and SYS10_TRANSLATIONS_B.LANGUAGE_CODE=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC08());
      values.add(pk.getDocTypeDOC08());
      values.add(pk.getDocYearDOC08());
      values.add(pk.getDocNumberDOC08());
      values.add(serverLanguageId);

      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailDeliveryNoteVO.class,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing out delivery note",ex);
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
  public VOListResponse loadOutDeliveryNotes(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2, "+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,SAL07_CUSTOMERS,REG04_SUBJECTS where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC08_DELIVERY_NOTES.DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC08_DELIVERY_NOTES.DOC_REF");
      attribute2dbField.put("warehouseDescriptionDOC08","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");

      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE);

      // read from DOC08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridDeliveryNoteVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching out delivery notes list",ex);
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
  public VOResponse updateOutDeliveryNote(DetailDeliveryNoteVO oldVO,DetailDeliveryNoteVO newVO,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC_REF");

      attribute2dbField.put("addressDOC08","ADDRESS");
      attribute2dbField.put("cityDOC08","CITY");
      attribute2dbField.put("provinceDOC08","PROVINCE");
      attribute2dbField.put("countryDOC08","COUNTRY");
      attribute2dbField.put("zipDOC08","ZIP");
      attribute2dbField.put("noteDOC08","NOTE");
      attribute2dbField.put("enabledDOC08","ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","CARRIER_CODE_REG09");
      attribute2dbField.put("progressiveReg04DOC08","PROGRESSIVE_REG04");
      attribute2dbField.put("deliveryDateDOC08","DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","TRANSPORT_MOTIVE_CODE_REG20");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC08");
      pkAttributes.add("docTypeDOC08");
      pkAttributes.add("docYearDOC08");
      pkAttributes.add("docNumberDOC08");

      // update DOC08 table...
      Response res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC08_DELIVERY_NOTES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing out delivery note",ex);
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
  public VOListResponse updateOutDeliveryNoteRows(ArrayList oldRows,ArrayList newRows,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

      GridOutDeliveryNoteRowVO oldVO = null;
      GridOutDeliveryNoteRowVO newVO = null;

      Response res = null;
      for(int i=0;i<newRows.size();i++) {
        oldVO = (GridOutDeliveryNoteRowVO)oldRows.get(i);
        newVO = (GridOutDeliveryNoteRowVO)newRows.get(i);

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC10","COMPANY_CODE_SYS01");
        attribute2dbField.put("docTypeDOC10","DOC_TYPE");
        attribute2dbField.put("docYearDOC10","DOC_YEAR");
        attribute2dbField.put("docNumberDOC10","DOC_NUMBER");
        attribute2dbField.put("docTypeDoc01DOC10","DOC_TYPE_DOC01");
        attribute2dbField.put("docYearDoc01DOC10","DOC_YEAR_DOC01");
        attribute2dbField.put("docNumberDoc01DOC10","DOC_NUMBER_DOC01");
        attribute2dbField.put("rowNumberDOC10","ROW_NUMBER");
        attribute2dbField.put("itemCodeItm01DOC10","ITEM_CODE_ITM01");
        attribute2dbField.put("qtyDOC10","QTY");
        attribute2dbField.put("progressiveHie02DOC10","PROGRESSIVE_HIE02");
        attribute2dbField.put("progressiveHie01DOC10","PROGRESSIVE_HIE01");
        attribute2dbField.put("invoiceQtyDOC10","INVOICE_QTY");

        attribute2dbField.put("progressiveDOC10","PROGRESSIVE");
        attribute2dbField.put("variantTypeItm06DOC10","VARIANT_TYPE_ITM06");
        attribute2dbField.put("variantCodeItm11DOC10","VARIANT_CODE_ITM11");
        attribute2dbField.put("variantTypeItm07DOC10","VARIANT_TYPE_ITM07");
        attribute2dbField.put("variantCodeItm12DOC10","VARIANT_CODE_ITM12");
        attribute2dbField.put("variantTypeItm08DOC10","VARIANT_TYPE_ITM08");
        attribute2dbField.put("variantCodeItm13DOC10","VARIANT_CODE_ITM13");
        attribute2dbField.put("variantTypeItm09DOC10","VARIANT_TYPE_ITM09");
        attribute2dbField.put("variantCodeItm14DOC10","VARIANT_CODE_ITM14");
        attribute2dbField.put("variantTypeItm10DOC10","VARIANT_TYPE_ITM10");
        attribute2dbField.put("variantCodeItm15DOC10","VARIANT_CODE_ITM15");

        HashSet pkAttributes = new HashSet();
        pkAttributes.add("progressiveDOC10");

        // update DOC10 table...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttributes,
            oldVO,
            newVO,
            "DOC10_OUT_DELIVERY_NOTE_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );

        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        // insert serial numbers...
        res = bean.reinsertOutSerialNumbers(newVO,serverLanguageId,username);
        if (res.isError())
      	  throw new Exception(res.getErrorMessage());

      }

      return new VOListResponse(newRows,false,newRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing out delivery note row",ex);
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
  public VOResponse deleteOutDeliveryNoteRows(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      OutDeliveryNoteRowPK rowPK = null;

      pstmt = conn.prepareStatement(
          "delete from DOC10_OUT_DELIVERY_NOTE_ITEMS where PROGRESSIVE=?"
      );

      pstmt2 = conn.prepareStatement(
       "delete from DOC12_OUT_SERIAL_NUMBERS where PROGRESSIVE_DOC10=?"
      );

      Response res = null;
      for(int i=0;i<list.size();i++) {
        rowPK = (OutDeliveryNoteRowPK)list.get(i);

        // delete previous serial numbers from DOC11...
        pstmt2.setBigDecimal(1,rowPK.getProgressiveDOC10());
        pstmt2.execute();
        pstmt2.close();

        // phisically delete the record in DOC10...
        pstmt.setBigDecimal(1,rowPK.getProgressiveDOC10());
        pstmt.execute();
      }

      Response answer =  new VOResponse(new Boolean(true));
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing out delivery note rows",ex);
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
        pstmt2.close();
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
   * Update out qty in referred sale documents when closing an out delivery note.
   * It update warehouse available quantities too.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if qtys updating was correctly executed
   */
  public VOResponse updateOutQtysPurchaseOrder(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		DeliveryNotePK pk, String t1, String t2, String serverLanguageId,
		String username) throws Throwable {
	// String t1 = resources.getResource("unload items from sale document");
	// String t2 = res.getResource("the warehouse motive specified is not defined");

    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      movBean.setConn(conn);
      bean.setConn(conn);

      // retrieve all in delivery note rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
      Response res = bean.loadOutDeliveryNoteRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pars,serverLanguageId,username);
      if (res.isError())
    	  throw new Exception(res.getErrorMessage());

      ArrayList values = new ArrayList();
      String sql1 =
          "select QTY,OUT_QTY from DOC02_SELLING_ITEMS where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      String sql2 =
          "update DOC02_SELLING_ITEMS set OUT_QTY=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and OUT_QTY=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      pstmt1 = conn.prepareStatement(sql1);
      pstmt2 = conn.prepareStatement(sql2);

      // for each item row it will be updated the related purchase order row and warehouse available quantities...
      GridOutDeliveryNoteRowVO vo = null;
      ResultSet rset1 = null;
      BigDecimal qtyDOC02 = null;
      BigDecimal outQtyDOC02 = null;
      BigDecimal qtyToAdd = null;
      Response innerResponse = null;
      for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
        vo = (GridOutDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);
        pstmt1.setString(1,vo.getCompanyCodeSys01DOC10());
        pstmt1.setString(2,vo.getDocTypeDoc01DOC10());
        pstmt1.setBigDecimal(3,vo.getDocYearDoc01DOC10());
        pstmt1.setBigDecimal(4,vo.getDocNumberDoc01DOC10());
        pstmt1.setString(5,vo.getItemCodeItm01DOC10());

        pstmt1.setString(6,vo.getVariantTypeItm06DOC10());
        pstmt1.setString(7,vo.getVariantCodeItm11DOC10());
        pstmt1.setString(8,vo.getVariantTypeItm07DOC10());
        pstmt1.setString(9,vo.getVariantCodeItm12DOC10());
        pstmt1.setString(10,vo.getVariantTypeItm08DOC10());
        pstmt1.setString(11,vo.getVariantCodeItm13DOC10());
        pstmt1.setString(12,vo.getVariantTypeItm09DOC10());
        pstmt1.setString(13,vo.getVariantCodeItm14DOC10());
        pstmt1.setString(14,vo.getVariantTypeItm10DOC10());
        pstmt1.setString(15,vo.getVariantCodeItm15DOC10());

        rset1 = pstmt1.executeQuery();
        if(rset1.next()) {
          qtyDOC02 = rset1.getBigDecimal(1);
          outQtyDOC02 = rset1.getBigDecimal(2);
          rset1.close();
/*
          // update out qty in the sale document row...
          if (vo.getSupplierQtyDOC10().doubleValue()<qtyDOC02.subtract(outQtyDOC02).doubleValue())
            qtyToAdd = vo.getSupplierQtyDOC10();
          else
            qtyToAdd = qtyDOC02.subtract(outQtyDOC02);
          pstmt2.setBigDecimal(1,outQtyDOC02.add(qtyToAdd).setScale(vo.getSupplierQtyDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          pstmt2.setString(2,vo.getCompanyCodeSys01DOC10());
          pstmt2.setString(3,vo.getDocTypeDoc01DOC10());
          pstmt2.setBigDecimal(4,vo.getDocYearDoc01DOC10());
          pstmt2.setBigDecimal(5,vo.getDocNumberDoc01DOC10());
          pstmt2.setString(6,vo.getItemCodeItm01DOC10());
          pstmt2.setBigDecimal(7,outQtyDOC02);
 */

          if (pstmt2.executeUpdate()==0)
        	  throw new Exception("Updating not performed: the record was previously updated.");
        }
        else
          rset1.close();

        // update warehouse available qty..
        WarehouseMovementVO movVO = new WarehouseMovementVO(
            vo.getProgressiveHie01DOC10(),
            vo.getQtyDOC10(),
            vo.getCompanyCodeSys01DOC10(),
            vo.getWarehouseCodeWar01DOC08(),
            vo.getItemCodeItm01DOC10(),
            ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_ORDER,
            ApplicationConsts.ITEM_GOOD,
            t1+" "+vo.getDocNumberDoc01DOC10()+"/"+vo.getDocYearDoc01DOC10(),
            vo.getSerialNumbers(),

            vo.getVariantCodeItm11DOC10(),
            vo.getVariantCodeItm12DOC10(),
            vo.getVariantCodeItm13DOC10(),
            vo.getVariantCodeItm14DOC10(),
            vo.getVariantCodeItm15DOC10(),
            vo.getVariantTypeItm06DOC10(),
            vo.getVariantTypeItm07DOC10(),
            vo.getVariantTypeItm08DOC10(),
            vo.getVariantTypeItm09DOC10(),
            vo.getVariantTypeItm10DOC10()

        );
        innerResponse = movBean.addWarehouseMovement(movVO,t2,serverLanguageId,username);

        if (innerResponse.isError())
          throw new Exception(innerResponse.getErrorMessage());
      }


      return  new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateOutQuantities","Error while updating out quantites in sale documents",ex);
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
        pstmt1.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
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
      try {
          movBean.setConn(null);
      } catch (Exception ex) {}
    }

  }



  /**
   * Update out qty in referred delivery requests/sale documents when closing an out delivery note.
   * It update warehouse available quantities too.
   * No commit/rollback is executed.
   * @return ErrorResponse in case of errors, new VOResponse(Boolean.TRUE) if qtys updating was correctly executed
   */
  public VOResponse updateOutQtysSaleDoc(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		DeliveryNotePK pk, String t1, String t2, String serverLanguageId,
		String username) throws Throwable {
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      loadSaleDocBean.setConn(conn);
      movBean.setConn(conn);
      bean.setConn(conn);

      // retrieve all out delivery note rows...
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
      Response res = bean.loadOutDeliveryNoteRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pars,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());

      ArrayList values = new ArrayList();
      String sql1 =
          "select QTY,OUT_QTY from DOC02_SELLING_ITEMS where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      String sql2 =
          "update DOC02_SELLING_ITEMS set OUT_QTY=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and OUT_QTY=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? ";

      String sql3 =
          "update DOC01_SELLING set DOC_STATE=? where "+
          "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and "+
          " EXISTS(SELECT * FROM DOC02_SELLING_ITEMS WHERE "+
          " COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? "+
          " GROUP BY COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER "+
          " HAVING SUM(QTY-OUT_QTY)=0 )";

      pstmt1 = conn.prepareStatement(sql1);
      pstmt2 = conn.prepareStatement(sql2);
      pstmt3 = conn.prepareStatement(sql3);

      // for each item row it will be updated the related sale document row and warehouse available quantities...
      GridOutDeliveryNoteRowVO vo = null;
      ResultSet rset1 = null;
      BigDecimal qtyDOC02 = null;
      BigDecimal outQtyDOC02 = null;
      BigDecimal qtyToAdd = null;
      Response innerResponse = null;
      Response saleDocRes = null;
      DetailSaleDocVO saleDocVO = null;
      for(int i=0;i<((VOListResponse)res).getRows().size();i++) {
        vo = (GridOutDeliveryNoteRowVO)((VOListResponse)res).getRows().get(i);

        // update delivery request...
        pstmt1.setString(1,vo.getCompanyCodeSys01DOC10());
        pstmt1.setString(2,vo.getDocTypeDoc01DOC10());
        pstmt1.setBigDecimal(3,vo.getDocYearDoc01DOC10());
        pstmt1.setBigDecimal(4,vo.getDocNumberDoc01DOC10());
        pstmt1.setString(5,vo.getItemCodeItm01DOC10());

        pstmt1.setString(6,vo.getVariantTypeItm06DOC10());
        pstmt1.setString(7,vo.getVariantCodeItm11DOC10());
        pstmt1.setString(8,vo.getVariantTypeItm07DOC10());
        pstmt1.setString(9,vo.getVariantCodeItm12DOC10());
        pstmt1.setString(10,vo.getVariantTypeItm08DOC10());
        pstmt1.setString(11,vo.getVariantCodeItm13DOC10());
        pstmt1.setString(12,vo.getVariantTypeItm09DOC10());
        pstmt1.setString(13,vo.getVariantCodeItm14DOC10());
        pstmt1.setString(14,vo.getVariantTypeItm10DOC10());
        pstmt1.setString(15,vo.getVariantCodeItm15DOC10());

        rset1 = pstmt1.executeQuery();
        if(rset1.next()) {
          qtyDOC02 = rset1.getBigDecimal(1);
          outQtyDOC02 = rset1.getBigDecimal(2);
          rset1.close();

          // update out qty in the sale document row...
          if (vo.getQtyDOC10().doubleValue()<qtyDOC02.subtract(outQtyDOC02).doubleValue())
            qtyToAdd = vo.getQtyDOC10();
          else
            qtyToAdd = qtyDOC02.subtract(outQtyDOC02);
          pstmt2.setBigDecimal(1,outQtyDOC02.add(qtyToAdd).setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          pstmt2.setString(2,vo.getCompanyCodeSys01DOC10());
          pstmt2.setString(3,vo.getDocTypeDoc01DOC10());
          pstmt2.setBigDecimal(4,vo.getDocYearDoc01DOC10());
          pstmt2.setBigDecimal(5,vo.getDocNumberDoc01DOC10());
          pstmt2.setString(6,vo.getItemCodeItm01DOC10());
          pstmt2.setBigDecimal(7,outQtyDOC02);

          pstmt2.setString(8,vo.getVariantTypeItm06DOC10());
          pstmt2.setString(9,vo.getVariantCodeItm11DOC10());
          pstmt2.setString(10,vo.getVariantTypeItm07DOC10());
          pstmt2.setString(11,vo.getVariantCodeItm12DOC10());
          pstmt2.setString(12,vo.getVariantTypeItm08DOC10());
          pstmt2.setString(13,vo.getVariantCodeItm13DOC10());
          pstmt2.setString(14,vo.getVariantTypeItm09DOC10());
          pstmt2.setString(15,vo.getVariantCodeItm14DOC10());
          pstmt2.setString(16,vo.getVariantTypeItm10DOC10());
          pstmt2.setString(17,vo.getVariantCodeItm15DOC10());

          if (pstmt2.executeUpdate()==0)
            throw new Exception("Updating not performed: the record was previously updated.");
        }
        else
          rset1.close();

        pstmt3.setString(1,ApplicationConsts.CLOSED);
        pstmt3.setString(2,vo.getCompanyCodeSys01DOC10());
        pstmt3.setString(3,vo.getDocTypeDoc01DOC10());
        pstmt3.setBigDecimal(4,vo.getDocYearDoc01DOC10());
        pstmt3.setBigDecimal(5,vo.getDocNumberDoc01DOC10());
        pstmt3.setString(6,vo.getCompanyCodeSys01DOC10());
        pstmt3.setString(7,vo.getDocTypeDoc01DOC10());
        pstmt3.setBigDecimal(8,vo.getDocYearDoc01DOC10());
        pstmt3.setBigDecimal(9,vo.getDocNumberDoc01DOC10());
        int processedRows = pstmt3.executeUpdate();


        // update sale document...
        if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE)) {
       	  SaleDocPK saleDocPK = new SaleDocPK(vo.getCompanyCodeSys01DOC10(),vo.getDocTypeDoc01DOC10(),vo.getDocYearDoc01DOC10(),vo.getDocNumberDoc01DOC10());

       	  saleDocVO = loadSaleDocBean.loadSaleDoc(
    		  saleDocPK,
    		  serverLanguageId,
    		  username,
    		  new ArrayList()
          );

          pstmt1.setString(1,saleDocVO.getCompanyCodeSys01DOC01());
          pstmt1.setString(2,saleDocVO.getDocTypeDoc01DOC01());
          pstmt1.setBigDecimal(3,saleDocVO.getDocYearDoc01DOC01());
          pstmt1.setBigDecimal(4,saleDocVO.getDocNumberDoc01DOC01());
          pstmt1.setString(5,vo.getItemCodeItm01DOC10());

          pstmt1.setString(6,vo.getVariantTypeItm06DOC10());
          pstmt1.setString(7,vo.getVariantCodeItm11DOC10());
          pstmt1.setString(8,vo.getVariantTypeItm07DOC10());
          pstmt1.setString(9,vo.getVariantCodeItm12DOC10());
          pstmt1.setString(10,vo.getVariantTypeItm08DOC10());
          pstmt1.setString(11,vo.getVariantCodeItm13DOC10());
          pstmt1.setString(12,vo.getVariantTypeItm09DOC10());
          pstmt1.setString(13,vo.getVariantCodeItm14DOC10());
          pstmt1.setString(14,vo.getVariantTypeItm10DOC10());
          pstmt1.setString(15,vo.getVariantCodeItm15DOC10());

          rset1 = pstmt1.executeQuery();
          if(rset1.next()) {
            qtyDOC02 = rset1.getBigDecimal(1);
            outQtyDOC02 = rset1.getBigDecimal(2);
            rset1.close();

            // update out qty in the sale document row...
            if (vo.getQtyDOC10().doubleValue()<qtyDOC02.subtract(outQtyDOC02).doubleValue())
              qtyToAdd = vo.getQtyDOC10();
            else
              qtyToAdd = qtyDOC02.subtract(outQtyDOC02);
            pstmt2.setBigDecimal(1,outQtyDOC02.add(qtyToAdd).setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
            pstmt2.setString(2,saleDocVO.getCompanyCodeSys01DOC01());
            pstmt2.setString(3,saleDocVO.getDocTypeDoc01DOC01());
            pstmt2.setBigDecimal(4,saleDocVO.getDocYearDoc01DOC01());
            pstmt2.setBigDecimal(5,saleDocVO.getDocNumberDoc01DOC01());
            pstmt2.setString(6,vo.getItemCodeItm01DOC10());
            pstmt2.setBigDecimal(7,outQtyDOC02);

            pstmt2.setString(8,vo.getVariantTypeItm06DOC10());
            pstmt2.setString(9,vo.getVariantCodeItm11DOC10());
            pstmt2.setString(10,vo.getVariantTypeItm07DOC10());
            pstmt2.setString(11,vo.getVariantCodeItm12DOC10());
            pstmt2.setString(12,vo.getVariantTypeItm08DOC10());
            pstmt2.setString(13,vo.getVariantCodeItm13DOC10());
            pstmt2.setString(14,vo.getVariantTypeItm09DOC10());
            pstmt2.setString(15,vo.getVariantCodeItm14DOC10());
            pstmt2.setString(16,vo.getVariantTypeItm10DOC10());
            pstmt2.setString(17,vo.getVariantCodeItm15DOC10());

            if (pstmt2.executeUpdate()==0)
            	throw new Exception("Updating not performed: the record was previously updated.");
          }
          else
            rset1.close();

        } // end if on deliv.req.doc.



        String motive = null;
        if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE))
          motive = ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_ORDER;
        else if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE))
          motive = ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_CONTRACT;
        else if (vo.getDocTypeDoc01DOC10().equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
          motive = ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_DELIV_REQ;

        // update warehouse available qty..
        WarehouseMovementVO movVO = new WarehouseMovementVO(
            vo.getProgressiveHie01DOC10(),
            vo.getQtyDOC10(),
            vo.getCompanyCodeSys01DOC10(),
            vo.getWarehouseCodeWar01DOC08(),
            vo.getItemCodeItm01DOC10(),
            motive,
            ApplicationConsts.ITEM_GOOD,
            t1+" "+vo.getDocSequenceDoc01DOC10()+"/"+vo.getDocYearDoc01DOC10(),
            vo.getSerialNumbers(),
            vo.getVariantCodeItm11DOC10(),
            vo.getVariantCodeItm12DOC10(),
            vo.getVariantCodeItm13DOC10(),
            vo.getVariantCodeItm14DOC10(),
            vo.getVariantCodeItm15DOC10(),
            vo.getVariantTypeItm06DOC10(),
            vo.getVariantTypeItm07DOC10(),
            vo.getVariantTypeItm08DOC10(),
            vo.getVariantTypeItm09DOC10(),
            vo.getVariantTypeItm10DOC10()

        );
        innerResponse = movBean.addWarehouseMovement(movVO,t2,serverLanguageId,username);

        if (innerResponse.isError())
            throw new Exception(innerResponse.getErrorMessage());
      }


      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateOutQuantities","Error while updating out quantites in sale documents",ex);
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
        pstmt1.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt3.close();
      }
      catch (Exception ex1) {
      }
      try {
          loadSaleDocBean.setConn(null);
          movBean.setConn(null);
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


}

