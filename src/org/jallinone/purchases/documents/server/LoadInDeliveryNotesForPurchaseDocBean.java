package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.hierarchies.java.*;
import org.jallinone.purchases.documents.invoices.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch in delivery notes from DOC08 table,
 * filtered by the specified purchase document.</p>
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
public class LoadInDeliveryNotesForPurchaseDocBean  implements LoadInDeliveryNotesForPurchaseDoc {


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




  public LoadInDeliveryNotesForPurchaseDocBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public InDeliveryNotesVO getInDeliveryNotes(DetailPurchaseDocVO vo) {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadInDeliveryNotesForPurchaseDoc(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
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
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.DESTINATION_CODE_REG18,DOC08_DELIVERY_NOTES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE "+
          " from DOC08_DELIVERY_NOTES where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
          "DOC08_DELIVERY_NOTES.DOC_STATE=? and "+
          "(DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER) "+
          " in (select DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
          " from DOC09_IN_DELIVERY_NOTE_ITEMS where "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06=? and "+
          " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06=? ";

      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)pars.getOtherGridParams().get(ApplicationConsts.PURCHASE_DOC_VO); // invoice document...
      if (docVO.getDocNumberDOC06()==null)
        sql += " and DOC09_IN_DELIVERY_NOTE_ITEMS.QTY-DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY>0)";
      else
        sql += ")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("destinationCodeReg18DOC08","DOC08_DELIVERY_NOTES.DESTINATION_CODE_REG18");
      attribute2dbField.put("descriptionDOC08","DOC08_DELIVERY_NOTES.DESCRIPTION");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");


      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
      values.add(ApplicationConsts.CLOSED);
      values.add(docVO.getCompanyCodeSys01DOC06());
      values.add(docVO.getDocTypeDoc06DOC06());
      values.add(docVO.getDocYearDoc06DOC06());
      values.add(docVO.getDocNumberDoc06DOC06());


      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          InDeliveryNotesVO.class,
          "Y",
          "N",
          null,
          pars,
          true
      );

      if (res.isError())
        throw new Exception(res.getErrorMessage());

      // check if the invoice document has been already created and there exists delivery notes linked to it...
      if (docVO.getDocNumberDOC06()!=null) {
        pstmt = conn.prepareStatement(
            "select DOC08_DELIVERY_NOTES.DOC_NUMBER from DOC08_DELIVERY_NOTES where "+
            "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
            "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
            "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
            "DOC08_DELIVERY_NOTES.DOC_STATE=? and "+
            "(DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER) "+
            " in (select DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
            " from DOC09_IN_DELIVERY_NOTE_ITEMS,DOC07_PURCHASE_ITEMS where "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC06=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01 and "+
            " DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
            " DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
            " DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01 and "+
            " DOC09_IN_DELIVERY_NOTE_ITEMS.QTY=DOC09_IN_DELIVERY_NOTE_ITEMS.INVOICE_QTY"+
            ")"
        );

        pstmt.setString(1,ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
        pstmt.setString(2,ApplicationConsts.CLOSED);
        pstmt.setString(3,docVO.getCompanyCodeSys01DOC06());
        pstmt.setString(4,docVO.getDocTypeDoc06DOC06());
        pstmt.setBigDecimal(5,docVO.getDocYearDoc06DOC06());
        pstmt.setBigDecimal(6,docVO.getDocNumberDoc06DOC06());
        pstmt.setString(7,docVO.getDocTypeDOC06());
        pstmt.setBigDecimal(8,docVO.getDocYearDOC06());
        pstmt.setBigDecimal(9,docVO.getDocNumberDOC06());

        HashSet docNumberDOC08s = new HashSet();
        ResultSet rset = pstmt.executeQuery();
        while(rset.next())
          docNumberDOC08s.add(rset.getBigDecimal(1));
        rset.close();

        java.util.List rows = ((VOListResponse)res).getRows();
        InDeliveryNotesVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (InDeliveryNotesVO)rows.get(i);
          if (docNumberDOC08s.contains(vo.getDocNumberDOC08()))
            vo.setSelected(Boolean.TRUE);
        }
      }

      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching in delivery notes list, related to the specified purchase document",ex);
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

