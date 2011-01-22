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
import org.jallinone.items.java.VariantBarcodeVO;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;

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
      pstmt.setBigDecimal(10,vo.getDocYearDoc01DOC10());
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

			int blockSize = 50;
			if (pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE)!=null)
				blockSize = Integer.parseInt(pars.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE).toString());

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
          blockSize,
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
          " HAVING SUM(QTY-OUT_QTY)=0 )"; // used to close delivery note requests documents

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

			  // close delivery note requests document...
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



		/**
		 * Business logic to execute.
		 */
		public GridOutDeliveryNoteRowVO validateCode(
			HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
			SaleDocPK pk,
			String warehouseCode,
			BigDecimal progressiveREG04,
			BigDecimal progressiveHie01DOC10,
			BigDecimal docSequenceDOC01,
			BigDecimal delivNoteDocNumber,
			String codeType,
			String code,
			String serverLanguageId, String username) throws Throwable {

			 PreparedStatement pstmt = null;
			 Connection conn = null;
			 try {
				 if (this.conn == null) conn = getConn(); else conn = this.conn;

					Response res = null;
				 java.util.List rows = null;
				 ArrayList values = new ArrayList();
				 boolean itemFound = false;

				 GridOutDeliveryNoteRowVO vo = new GridOutDeliveryNoteRowVO();
				 vo.setCompanyCodeSys01DOC10(pk.getCompanyCodeSys01DOC01());
				 vo.setDocNumberDoc01DOC10(pk.getDocNumberDOC01());
				 vo.setDocNumberDOC10(delivNoteDocNumber);
				 vo.setDocTypeDoc01DOC10(pk.getDocTypeDOC01());
				 vo.setDocTypeDOC10(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE);
				 vo.setDocYearDoc01DOC10(pk.getDocYearDOC01());
				 vo.setDocYearDOC10(new BigDecimal(new java.util.Date().getYear() + 1900));
				 vo.setQtyDOC10(new BigDecimal(1));
				 vo.setInvoiceQtyDOC10(new BigDecimal(0));
				 vo.setSerialNumbers(new ArrayList());
				 vo.setWarehouseCodeWar01DOC08(warehouseCode);
				 vo.setProgressiveHie01DOC10(progressiveHie01DOC10); // warehouse location
				 vo.setDocSequenceDoc01DOC10(docSequenceDOC01);

				 if ("B".equals(codeType)) {
					 // validate variants barcode...
					 String sql =
							 "select "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01,ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
							 "ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10,"+
							 "ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15,"+
							 "ITM22_VARIANT_BARCODES.BAR_CODE,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
							 "from ITM22_VARIANT_BARCODES,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC02_SELLING_ITEMS "+
							 "where "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=? AND "+
							 "ITM22_VARIANT_BARCODES.BAR_CODE=? AND "+
							 "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
							 "ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and ITM01_ITEMS.ENABLED='Y' AND "+
							 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
							 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
							 "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01 AND "+
							 "DOC02_SELLING_ITEMS.DOC_TYPE=? AND "+
							 "DOC02_SELLING_ITEMS.DOC_YEAR=? AND "+
							 "DOC02_SELLING_ITEMS.DOC_NUMBER=? AND "+
							 "DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY>0 AND "+
							 "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10=ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15=ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15  ";

					 Map attribute2dbField = new HashMap();
					 attribute2dbField.put("companyCodeSys01ITM22","ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01");
					 attribute2dbField.put("itemCodeItm01ITM22","ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01");
					 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
					 attribute2dbField.put("barCodeITM22","ITM22_VARIANT_BARCODES.BAR_CODE");
					 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
					 attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

					 attribute2dbField.put("variantTypeItm06ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06");
					 attribute2dbField.put("variantCodeItm11ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11");
					 attribute2dbField.put("variantTypeItm07ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07");
					 attribute2dbField.put("variantCodeItm12ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12");
					 attribute2dbField.put("variantTypeItm08ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08");
					 attribute2dbField.put("variantCodeItm13ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13");
					 attribute2dbField.put("variantTypeItm09ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09");
					 attribute2dbField.put("variantCodeItm14ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14");
					 attribute2dbField.put("variantTypeItm10ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10");
					 attribute2dbField.put("variantCodeItm15ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15");

					 values.clear();
					 values.add(pk.getCompanyCodeSys01DOC01());
					 values.add(code);
					 values.add(serverLanguageId);
					 values.add(pk.getDocTypeDOC01());
					 values.add(pk.getDocYearDOC01());
					 values.add(pk.getDocNumberDOC01());

					 // read from ITM01 table...
					 res = QueryUtil.getQuery(
							 conn,
							 new UserSessionParameters(username),
							 sql,
							 values,
							 attribute2dbField,
							 VariantBarcodeVO.class,
							 "Y",
							 "N",
							 null,
							 new GridParams(),
							 true
					 );
					 if (res.isError())
						 throw new Exception(res.getErrorMessage());

					 rows = ((VOListResponse)res).getRows();
					 if (rows.size() == 1) {
						 // found variants barcode: pre-fill code and qty in variants matrix...
						 VariantBarcodeVO barcodeVO = (VariantBarcodeVO)rows.get(0);

						 //vo.setInQtyDOC02();
						 //vo.setDecimalsREG02();
						 vo.setItemCodeItm01DOC10(barcodeVO.getItemCodeItm01ITM22());
						 vo.setDescriptionSYS10(barcodeVO.getDescriptionSYS10());
						 vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(barcodeVO.getSerialNumberRequiredITM01())));
						 vo.setProgressiveHie02DOC10(barcodeVO.getProgressiveHie02ITM01()); // item's LOB

						 //vo.setQtyDOC02();
						 //vo.setSupplierItemCodePur02DOC10();
						 //vo.setSupplierQtyDecimalsREG02();
						 //vo.setUmCodeREG02();
						 //vo.setUmCodeReg02PUR02();
						 //vo.setValueREG05();
						 vo.setVariantCodeItm11DOC10(barcodeVO.getVariantCodeItm11ITM22());
						 vo.setVariantCodeItm12DOC10(barcodeVO.getVariantCodeItm12ITM22());
						 vo.setVariantCodeItm13DOC10(barcodeVO.getVariantCodeItm13ITM22());
						 vo.setVariantCodeItm14DOC10(barcodeVO.getVariantCodeItm14ITM22());
						 vo.setVariantCodeItm15DOC10(barcodeVO.getVariantCodeItm15ITM22());
						 vo.setVariantTypeItm06DOC10(barcodeVO.getVariantTypeItm06ITM22());
						 vo.setVariantTypeItm07DOC10(barcodeVO.getVariantTypeItm07ITM22());
						 vo.setVariantTypeItm08DOC10(barcodeVO.getVariantTypeItm08ITM22());
						 vo.setVariantTypeItm09DOC10(barcodeVO.getVariantTypeItm09ITM22());
						 vo.setVariantTypeItm10DOC10(barcodeVO.getVariantTypeItm10ITM22());

						 itemFound = true;
					 }

					 if (!itemFound) {
						 // no barcode found in ITM22 (barcode for item variants)
						 // trying to find a barcode at item level...
						 sql =
								 "select "+
								 "ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,SYS10_TRANSLATIONS.DESCRIPTION,"+
								 "ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
								 "from ITM01_ITEMS,SYS10_TRANSLATIONS,DOC02_SELLING_ITEMS where "+
								 "ITM01_ITEMS.COMPANY_CODE_SYS01=? AND "+
								 "ITM01_ITEMS.BAR_CODE=? AND "+
								 "ITM01_ITEMS.ENABLED='Y' AND "+
								 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
								 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
								 "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND " +
								 "DOC02_SELLING_ITEMS.DOC_TYPE=? AND " +
								 "DOC02_SELLING_ITEMS.DOC_YEAR=? AND " +
								 "DOC02_SELLING_ITEMS.DOC_NUMBER=? AND " +
								 "DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY>0 AND "+
								 "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14='*' AND " +
								 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15='*' ";

						 attribute2dbField.clear();
						 attribute2dbField.put("companyCodeSys01ITM01","ITM01_ITEMS.COMPANY_CODE_SYS01");
						 attribute2dbField.put("itemCodeITM01","ITM01_ITEMS.ITEM_CODE");
						 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
						 attribute2dbField.put("barCodeITM01","ITM01_ITEMS.BAR_CODE");
						 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
						 attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

						 values.clear();
						 values.add(pk.getCompanyCodeSys01DOC01());
						 values.add(code);
						 values.add(serverLanguageId);
						 values.add(pk.getDocTypeDOC01());
						 values.add(pk.getDocYearDOC01());
						 values.add(pk.getDocNumberDOC01());

						 // read from ITM01 table...
						 res = QueryUtil.getQuery(
								 conn,
								 new UserSessionParameters(username),
								 sql,
								 values,
								 attribute2dbField,
								 GridItemVO.class,
								 "Y",
								 "N",
								 null,
								 new GridParams(),
								 true
						 );

						 if (res.isError())
							 throw new Exception(res.getErrorMessage());

						 rows = ( (VOListResponse) res).getRows();
						 if (rows.size() == 1) {
							 // found variants barcode: pre-fill code and qty in variants matrix...
							 GridItemVO itemVO = (GridItemVO)rows.get(0);

							 //vo.setInQtyDOC02();
							 //vo.setDecimalsREG02();
							 vo.setItemCodeItm01DOC10(itemVO.getItemCodeITM01());
							 vo.setDescriptionSYS10(itemVO.getDescriptionSYS10());
							 vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(itemVO.getSerialNumberRequiredITM01())));
							 vo.setProgressiveHie02DOC10(itemVO.getProgressiveHie02ITM01()); // item's LOB

							 //vo.setQtyDOC02();
							 //vo.setSupplierItemCodePur02DOC10();
							 //vo.setSupplierQtyDecimalsREG02();
							 //vo.setUmCodeREG02();
							 //vo.setUmCodeReg02PUR02();
							 //vo.setValueREG05();
							 vo.setVariantCodeItm11DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm12DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm13DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm14DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantCodeItm15DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm06DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm07DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm08DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm09DOC10(ApplicationConsts.JOLLY);
							 vo.setVariantTypeItm10DOC10(ApplicationConsts.JOLLY);

							 itemFound = true;
						 }
					 }

					 if (!itemFound) {
						 // no barcode found!
						 return null;
					 }
				 }
/*
				 else if ("S".equals(codeType)) {
					 // search for a serial number!
					 String sql =
							 "select WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01,WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER,"+
							 "WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01,WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01,"+
							 "SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
							 "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11,"+
							 "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12,"+
							 "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13,"+
							 "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14,"+
							 "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15 "+
							 "from WAR05_STORED_SERIAL_NUMBERS,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC02_SELLING_ITEMS "+
							 "where "+
							 "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=? and "+
							 "WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER=? and "+
							 "ITM01_ITEMS.COMPANY_CODE_SYS01=? AND "+
							 "ITM01_ITEMS.ITEM_CODE=WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01 AND "+
							 "ITM01_ITEMS.ENABLED='Y' AND "+
							 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
							 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
							 "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND " +
							 "DOC02_SELLING_ITEMS.DOC_TYPE=? AND " +
							 "DOC02_SELLING_ITEMS.DOC_YEAR=? AND " +
							 "DOC02_SELLING_ITEMS.DOC_NUMBER=? AND " +
							 "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06=WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07=WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08=WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09=WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10=WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11=WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12=WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13=WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14=WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14 AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15=WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15  ";

						values.clear();
						values.add(pk.getCompanyCodeSys01DOC01());
					 values.add(code);
					 values.add(pk.getCompanyCodeSys01DOC01());
					 values.add(serverLanguageId);
					 values.add(pk.getDocTypeDOC01());
					 values.add(pk.getDocYearDOC01());
					 values.add(pk.getDocNumberDOC01());

					 Map attribute2dbField = new HashMap();
					 attribute2dbField.put("companyCodeSys01WAR05","WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01");
					 attribute2dbField.put("serialNumberWAR05","WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER");
					 attribute2dbField.put("itemCodeItm01WAR05","WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01");
					 attribute2dbField.put("progressiveHie01WAR05","WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01");
					 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
					 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
					 attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

					 attribute2dbField.put("variantTypeItm06WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06");
					 attribute2dbField.put("variantCodeItm11WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11");
					 attribute2dbField.put("variantTypeItm07WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07");
					 attribute2dbField.put("variantCodeItm12WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12");
					 attribute2dbField.put("variantTypeItm08WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08");
					 attribute2dbField.put("variantCodeItm13WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13");
					 attribute2dbField.put("variantTypeItm09WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09");
					 attribute2dbField.put("variantCodeItm14WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14");
					 attribute2dbField.put("variantTypeItm10WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10");
					 attribute2dbField.put("variantCodeItm15WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15");

					 res = QueryUtil.getQuery(
							 conn,
							 new UserSessionParameters(username),
							 sql,
							 values,
							 attribute2dbField,
							 StoredSerialNumberVO.class,
							 "Y",
							 "N",
							 null,
							 new GridParams(),
							 true
					 );

					 if (res.isError())
						 throw new Exception(res.getErrorMessage());

					 rows = ( (VOListResponse) res).getRows();
					 if (rows.size() == 1) {
							StoredSerialNumberVO sVO = (StoredSerialNumberVO)rows.get(0);

							//vo.setInQtyDOC02();
							//vo.setDecimalsREG02();
							vo.setItemCodeItm01DOC10(sVO.getItemCodeItm01WAR05());
							vo.setDescriptionSYS10(sVO.getDescriptionSYS10());
							vo.setSerialNumberRequiredITM01(new Boolean(true));
							vo.setProgressiveHie02DOC10(sVO.getProgressiveHie02ITM01()); // item's LOB

							//vo.setQtyDOC02();
							//vo.setSupplierItemCodePur02DOC10();
							//vo.setSupplierQtyDecimalsREG02();
							//vo.setUmCodeREG02();
							//vo.setUmCodeReg02PUR02();
							//vo.setValueREG05();
							vo.setVariantCodeItm11DOC10(sVO.getVariantCodeItm11WAR05());
							vo.setVariantCodeItm12DOC10(sVO.getVariantCodeItm12WAR05());
							vo.setVariantCodeItm13DOC10(sVO.getVariantCodeItm13WAR05());
							vo.setVariantCodeItm14DOC10(sVO.getVariantCodeItm14WAR05());
							vo.setVariantCodeItm15DOC10(sVO.getVariantCodeItm15WAR05());
							vo.setVariantTypeItm06DOC10(sVO.getVariantTypeItm06WAR05());
							vo.setVariantTypeItm07DOC10(sVO.getVariantTypeItm07WAR05());
							vo.setVariantTypeItm08DOC10(sVO.getVariantTypeItm08WAR05());
							vo.setVariantTypeItm09DOC10(sVO.getVariantTypeItm09WAR05());
							vo.setVariantTypeItm10DOC10(sVO.getVariantTypeItm10WAR05());
							vo.setSerialNumberRequiredITM01(Boolean.TRUE);
							vo.getSerialNumbers().add(code);

							itemFound = true;
					 }

					 if (!itemFound) {
						 // no serial number found!
						 return null;
					 }
				 }
*/
				 else {
					 // check for item code WITHOUT VARIANTS in sale doc...
					 String sql =
							 "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
							 "SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02,"+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15, "+
							 "ITM01_ITEMS.NO_WAREHOUSE_MOV "+
							 "from DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
							 "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
							 "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
							 "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
							 "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
							 "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
							 "DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
							 "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
							 "DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
							 "DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY>0 AND "+
							 "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? AND "+
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14='*' AND " +
							 "DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15='*' ";

					 Map attribute2dbField = new HashMap();
					 attribute2dbField.put("companyCodeSys01DOC02","DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01");
					 attribute2dbField.put("itemCodeItm01DOC02","DOC02_SELLING_ITEMS.ITEM_CODE_ITM01");
					 attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
					 attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
					 attribute2dbField.put("progressiveHie02DOC02","DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02");

					 attribute2dbField.put("variantTypeItm06DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06");
					 attribute2dbField.put("variantCodeItm11DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11");
					 attribute2dbField.put("variantTypeItm07DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07");
					 attribute2dbField.put("variantCodeItm12DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12");
					 attribute2dbField.put("variantTypeItm08DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08");
					 attribute2dbField.put("variantCodeItm13DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13");
					 attribute2dbField.put("variantTypeItm09DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09");
					 attribute2dbField.put("variantCodeItm14DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14");
					 attribute2dbField.put("variantTypeItm10DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10");
					 attribute2dbField.put("variantCodeItm15DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15");

					 attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");

					 values.clear();
					 values.add(serverLanguageId);
					 values.add(pk.getCompanyCodeSys01DOC01());
					 values.add(pk.getDocTypeDOC01());
					 values.add(pk.getDocYearDOC01());
					 values.add(pk.getDocNumberDOC01());
					 values.add(code);

					 // read from DOC02 table...
					 res = QueryUtil.getQuery(
							 conn,
							 new UserSessionParameters(username),
							 sql,
							 values,
							 attribute2dbField,
							 GridSaleDocRowVO.class,
							 "Y",
							 "N",
							 null,
							 new GridParams(),
							 true
					 );

					 if (res.isError())
						 throw new Exception(res.getErrorMessage());

					 rows = ( (VOListResponse) res).getRows();
					 if (rows.size() == 1) {
							GridSaleDocRowVO itemVO = (GridSaleDocRowVO)rows.get(0);

							//vo.setInQtyDOC07();
							//vo.setDecimalsREG02();
							vo.setItemCodeItm01DOC10(itemVO.getItemCodeItm01DOC02());
							vo.setDescriptionSYS10(itemVO.getDescriptionSYS10());
							vo.setSerialNumberRequiredITM01(new Boolean("Y".equals(itemVO.getSerialNumberRequiredITM01())));
							vo.setProgressiveHie02DOC10(itemVO.getProgressiveHie02DOC02()); // item's LOB
							//vo.setQtyDOC02();
							//vo.setSupplierItemCodePur02DOC10();
							//vo.setSupplierQtyDecimalsREG02();
							//vo.setUmCodeREG02();
							//vo.setUmCodeReg02PUR02();
							//vo.setValueREG05();
							vo.setVariantCodeItm11DOC10(itemVO.getVariantCodeItm11DOC02());
							vo.setVariantCodeItm12DOC10(itemVO.getVariantCodeItm12DOC02());
							vo.setVariantCodeItm13DOC10(itemVO.getVariantCodeItm13DOC02());
							vo.setVariantCodeItm14DOC10(itemVO.getVariantCodeItm14DOC02());
							vo.setVariantCodeItm15DOC10(itemVO.getVariantCodeItm15DOC02());
							vo.setVariantTypeItm06DOC10(itemVO.getVariantTypeItm06DOC02());
							vo.setVariantTypeItm07DOC10(itemVO.getVariantTypeItm07DOC02());
							vo.setVariantTypeItm08DOC10(itemVO.getVariantTypeItm08DOC02());
							vo.setVariantTypeItm09DOC10(itemVO.getVariantTypeItm09DOC02());
							vo.setVariantTypeItm10DOC10(itemVO.getVariantTypeItm10DOC02());

							itemFound = true;
					 }

					 if (!itemFound) {
						 // no item found!
						 return null;
					 }
				 }

				 String descr = vo.getDescriptionSYS10();

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

				 return vo;
			 }
			 catch (Throwable ex) {
				 Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating code for an in delivery note row",ex);
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

