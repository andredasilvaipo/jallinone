package org.jallinone.ordertracking.server;


import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.ordertracking.java.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.internationalization.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used by the order tracking frame.</p>
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
public class LoadOrderTrackingBean  implements LoadOrderTracking {


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




  public LoadOrderTrackingBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public DocumentTrackingVO getDocumentTracking() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public OrderTrackingFilterVO getOrderTracking() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadOrderTracking(GridParams gridParams,HashMap mapDOC06,HashMap mapDOC01,HashMap mapDOC08,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      Object filter = gridParams.getOtherGridParams().get(ApplicationConsts.PROPERTIES_FILTER);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSYS01","COMPANY_CODE_SYS01");
      attribute2dbField.put("docType","DOC_TYPE");
      attribute2dbField.put("docYear","DOC_YEAR");
      attribute2dbField.put("docNumber","DOC_NUMBER");
      attribute2dbField.put("docSequence","DOC_SEQUENCE");
      attribute2dbField.put("docState","DOC_STATE");
      attribute2dbField.put("docDate","DOC_DATE");
      attribute2dbField.put("customerSupplierCode","C_S_CODE");
      attribute2dbField.put("name_1","NAME_1");
      attribute2dbField.put("name_2","NAME_2");

      ArrayList values = new ArrayList();
      String sql = null;
      Response answer = null;


      if (filter instanceof DocumentTrackingVO) {
        DocumentTrackingVO vo = (DocumentTrackingVO)filter;

        // read sale/purchase docs...
        String tableName = getDocTableName(vo.getDocType());
        String custSuppTableName = getCustomerSupplierTableName(vo.getDocType());
        String custSuppColName = getCustomerSupplierColName(vo.getDocType());
        String parentDocPrefix = getParentDocPrefix(vo.getDocType());
        String delivNoteItemsTableName = getDelNoteItemsTableName(vo.getDocType());
        sql =
            "select "+
            tableName+".COMPANY_CODE_SYS01 AS COMPANY_CODE_SYS01,"+
            tableName+".DOC_TYPE AS DOC_TYPE,"+
            tableName+".DOC_YEAR AS DOC_YEAR,"+
            tableName+".DOC_NUMBER AS DOC_NUMBER,"+
            tableName+".DOC_SEQUENCE AS DOC_SEQUENCE,"+
            tableName+".DOC_STATE AS DOC_STATE,"+
            tableName+".DOC_DATE AS DOC_DATE,"+
            "REG04_SUBJECTS.NAME_1 AS NAME_1, "+
            "REG04_SUBJECTS.NAME_2 AS NAME_2, "+
            custSuppTableName+"."+custSuppColName+" AS C_S_CODE "+
            "from "+tableName+",REG04_SUBJECTS,"+custSuppTableName+" where "+
            tableName+".COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 AND "+
            tableName+".PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE AND "+
            tableName+".COMPANY_CODE_SYS01="+custSuppTableName+".COMPANY_CODE_SYS01 AND "+
            tableName+".PROGRESSIVE_REG04="+custSuppTableName+".PROGRESSIVE_REG04 AND "+
            tableName+".COMPANY_CODE_SYS01=? AND "+
            tableName+".DOC_TYPE"+parentDocPrefix+"=? AND "+
            tableName+".DOC_YEAR"+parentDocPrefix+"=? AND "+
            tableName+".DOC_NUMBER"+parentDocPrefix+"=? AND "+
            tableName+".ENABLED='Y' ";

        values.add(vo.getCompanyCodeSYS01());
        values.add(vo.getDocType());
        values.add(vo.getDocYear());
        values.add(vo.getDocNumber());

        answer = QueryUtil.getQuery(
             conn,
             new UserSessionParameters(username),
             sql,
             values,
             attribute2dbField,
             DocumentTrackingVO.class,
             "Y",
             "N",
             null,
             gridParams,
             true
        );

        if (!answer.isError() && delivNoteItemsTableName!=null) {
          List rows = ((VOListResponse)answer).getRows();
          DocumentTrackingVO docVO = null;
          for(int i=0;i<rows.size();i++) {
            docVO = (DocumentTrackingVO)rows.get(i);
            docVO.setDocTypeDescr(getDocTypeDescr(mapDOC06,mapDOC01,mapDOC08,tableName,docVO.getDocType()));
          }

          // read delivery note rows docs...
          sql =
              "SELECT DISTINCT DOC_TYPE,DOC_YEAR,DOC_NUMBER FROM "+delivNoteItemsTableName+" WHERE "+
              "COMPANY_CODE_SYS01=? AND "+
              "DOC_TYPE"+parentDocPrefix+"=? AND "+
              "DOC_YEAR"+parentDocPrefix+"=? AND "+
              "DOC_NUMBER"+parentDocPrefix+"=? ";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,vo.getCompanyCodeSYS01());
          pstmt.setString(2,vo.getDocType());
          pstmt.setBigDecimal(3,vo.getDocYear());
          pstmt.setBigDecimal(4,vo.getDocNumber());
          ResultSet rset = pstmt.executeQuery();

          // read delivery notes docs...
          String sql2 =
              "select "+
              "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 AS COMPANY_CODE_SYS01,"+
              "DOC08_DELIVERY_NOTES.DOC_TYPE AS DOC_TYPE,"+
              "DOC08_DELIVERY_NOTES.DOC_YEAR AS DOC_YEAR,"+
              "DOC08_DELIVERY_NOTES.DOC_NUMBER AS DOC_NUMBER,"+
              "DOC08_DELIVERY_NOTES.DOC_SEQUENCE AS DOC_SEQUENCE,"+
              "DOC08_DELIVERY_NOTES.DOC_STATE AS DOC_STATE,"+
              "DOC08_DELIVERY_NOTES.DOC_DATE AS DOC_DATE,"+
              "REG04_SUBJECTS.NAME_1 AS NAME_1, "+
              "REG04_SUBJECTS.NAME_2 AS NAME_2, "+
              custSuppTableName+"."+custSuppColName+" AS C_S_CODE "+
              "from DOC08_DELIVERY_NOTES,REG04_SUBJECTS,"+custSuppTableName+" where "+
              "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 AND "+
              "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE AND "+
              "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01="+custSuppTableName+".COMPANY_CODE_SYS01 AND "+
              "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04="+custSuppTableName+".PROGRESSIVE_REG04 AND "+
              "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=? AND "+
              "DOC08_DELIVERY_NOTES.ENABLED='Y' AND (";

          values.clear();
          values.add(vo.getCompanyCodeSYS01());

          boolean found = false;
          while(rset.next()) {
            found = true;
            sql2 +="DOC08_DELIVERY_NOTES.DOC_TYPE=? AND DOC08_DELIVERY_NOTES.DOC_YEAR=? AND DOC08_DELIVERY_NOTES.DOC_NUMBER=? OR ";
            values.add(rset.getString(1));
            values.add(rset.getBigDecimal(2));
            values.add(rset.getBigDecimal(3));
          }
          rset.close();
          sql2 = sql2.substring(0,sql2.length()-4)+")";

          if (found) {
            answer = QueryUtil.getQuery(
                 conn,
                 new UserSessionParameters(username),
                 sql2,
                 values,
                 attribute2dbField,
                 DocumentTrackingVO.class,
                 "Y",
                 "N",
                 null,
                 gridParams,
                 true
            );
            if (!answer.isError()) {
              List newrows = ((VOListResponse)answer).getRows();
              for(int i=0;i<newrows.size();i++) {
                docVO = (DocumentTrackingVO)newrows.get(i);
                docVO.setDocTypeDescr(getDocTypeDescr(mapDOC06,mapDOC01,mapDOC08,"DOC08_DELIVERY_NOTES",docVO.getDocType()));
              }

              ( (VOListResponse) answer).getRows().addAll(0, rows);
            }
          }
        }



      }
      else if(filter instanceof OrderTrackingFilterVO) {
        OrderTrackingFilterVO vo = (OrderTrackingFilterVO)filter;
        String tableName = getDocTableName(vo.getDocType());
        String custSuppTableName = getCustomerSupplierTableName(vo.getDocType());
        String custSuppColName = getCustomerSupplierColName(vo.getDocType());
        sql =
            "select "+
            tableName+".COMPANY_CODE_SYS01 AS COMPANY_CODE_SYS01,"+
            tableName+".DOC_TYPE AS DOC_TYPE,"+
            tableName+".DOC_YEAR AS DOC_YEAR,"+
            tableName+".DOC_NUMBER AS DOC_NUMBER,"+
            tableName+".DOC_SEQUENCE AS DOC_SEQUENCE,"+
            tableName+".DOC_STATE AS DOC_STATE,"+
            tableName+".DOC_DATE AS DOC_DATE,"+
            "REG04_SUBJECTS.NAME_1 AS NAME_1, "+
            "REG04_SUBJECTS.NAME_2 AS NAME_2, "+
            custSuppTableName+"."+custSuppColName+" AS C_S_CODE "+
            "from "+tableName+",REG04_SUBJECTS,"+custSuppTableName+" where "+
            tableName+".COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 AND "+
            tableName+".PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE AND "+
            tableName+".COMPANY_CODE_SYS01="+custSuppTableName+".COMPANY_CODE_SYS01 AND "+
            tableName+".PROGRESSIVE_REG04="+custSuppTableName+".PROGRESSIVE_REG04 AND "+
            tableName+".DOC_TYPE=? AND "+
            tableName+".ENABLED='Y' ";


        values.add(vo.getDocType());

        if (vo.getDocDate()!=null) {
          sql += " AND "+tableName+".DOC_DATE=?";
          values.add(vo.getDocDate());
        }
        if (vo.getDocYear()!=null) {
          sql += " AND "+tableName+".DOC_YEAR=?";
          values.add(vo.getDocYear());
        }
        if (vo.getDocState()!=null) {
          sql += " AND "+tableName+".DOC_STATE=?";
          values.add(vo.getDocState());
        }
        if (vo.getProgressiveREG04()!=null) {
          sql += " AND "+tableName+".PROGRESSIVE_REG04=?";
          values.add(vo.getProgressiveREG04());
        }

        answer = QueryUtil.getQuery(
             conn,
             new UserSessionParameters(username),
             sql,
             values,
             attribute2dbField,
             DocumentTrackingVO.class,
             "Y",
             "N",
             null,
             gridParams,
             50,
             true
        );
        if (!answer.isError()) {
          List rows = ((VOListResponse)answer).getRows();
          DocumentTrackingVO docVO = null;
          for(int i=0;i<rows.size();i++) {
            docVO = (DocumentTrackingVO)rows.get(i);
            docVO.setDocTypeDescr(getDocTypeDescr(mapDOC06,mapDOC01,mapDOC08,tableName,docVO.getDocType()));
          }
        }
      }

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while loading orders tracking rows",ex);
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


  private String getDocTableName(String docType) {
    if (docType.equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE) ||
        docType.equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE))
      return "DOC06_PURCHASE";
    else if (docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE) ||
             docType.equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
      return "DOC01_SELLING";
    else if (docType.equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
      return "DOC08_DELIVERY_NOTES";
    return null;
  }


  private String getDocTypeDescr(HashMap mapDOC06,HashMap mapDOC01,HashMap mapDOC08,String tableName,String docType) {
    if (tableName.equals("DOC06_PURCHASE")) {
    	return (String)mapDOC06.get(docType);
    }
    else if (tableName.equals("DOC01_SELLING")) {
    	return (String)mapDOC01.get(docType);
    }
    else if (tableName.equals("DOC08_DELIVERY_NOTES")) {
    	return (String)mapDOC08.get(docType);
    }
    return null;
  }


  private String getDelNoteItemsTableName(String docType) {
    if (docType.equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE) ||
        docType.equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE))
      return "DOC09_IN_DELIVERY_NOTE_ITEMS";
    else if (docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE) ||
             docType.equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
      return "DOC10_OUT_DELIVERY_NOTE_ITEMS";
    return null;
  }



  private String getParentDocPrefix(String docType) {
    if (docType.equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE) ||
        docType.equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE))
      return "_DOC06";
    else if (docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE) ||
             docType.equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
      return "_DOC01";
    else if (docType.equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE))
      return "_DOC06";
    else if (docType.equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
      return "_DOC01";
    return null;
  }


  private String getCustomerSupplierTableName(String docType) {
    if (docType.equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE) ||
        docType.equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE))
      return "PUR01_SUPPLIERS";
    else if (docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE) ||
             docType.equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
      return "SAL07_CUSTOMERS";
    else if (docType.equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE))
      return "PUR01_SUPPLIERS";
    else if (docType.equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
      return "SAL07_CUSTOMERS";
    return null;

    }


  private String getCustomerSupplierColName(String docType) {
    if (docType.equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE) ||
        docType.equals(ApplicationConsts.PURCHASE_GENERIC_INVOICE) ||
        docType.equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE))
      return "SUPPLIER_CODE";
    else if (docType.equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_GENERIC_INVOICE) ||
             docType.equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
             docType.equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE) ||
             docType.equals(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE))
      return "CUSTOMER_CODE";
    else if (docType.equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE))
      return "SUPPLIER_CODE";
    else if (docType.equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
      return "CUSTOMER_CODE";
    return null;
  }


}

