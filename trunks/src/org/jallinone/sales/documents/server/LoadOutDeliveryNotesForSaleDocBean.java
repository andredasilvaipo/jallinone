package org.jallinone.sales.documents.server;

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
import org.jallinone.sales.documents.invoices.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch out delivery notes from DOC08 table,
 * filtered by the specified sale document.</p>
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
public class LoadOutDeliveryNotesForSaleDocBean  implements LoadOutDeliveryNotesForSaleDoc {


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




  public LoadOutDeliveryNotesForSaleDocBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public OutDeliveryNotesVO getOutDeliveryNotes(DetailSaleDocVO vo) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadOutDeliveryNotesForSaleDoc(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
		ResultSet rset = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

			DetailSaleDocVO docVO = (DetailSaleDocVO)pars.getOtherGridParams().get(ApplicationConsts.SALE_DOC_VO); // invoice document...

      // retrieve deliv.note requests related to the specified sale doc..
			pstmt = conn.prepareStatement(
			  "select DOC_YEAR,DOC_NUMBER from DOC01_SELLING WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE_DOC01=? AND DOC_YEAR_DOC01=? AND DOC_NUMBER_DOC01=?"
			);
		  pstmt.setString(1,docVO.getCompanyCodeSys01DOC01()); // starting sale doc...
			pstmt.setString(2,docVO.getDocTypeDoc01DOC01());
			pstmt.setBigDecimal(3,docVO.getDocYearDoc01DOC01());
			pstmt.setBigDecimal(4,docVO.getDocNumberDoc01DOC01());
		  rset = pstmt.executeQuery();
			String where = "";
			while(rset.next()) {
				where +=
					" DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01="+rset.getInt(1)+" and "+
					" DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01="+rset.getInt(2)+" or ";
			}
			rset.close();
			pstmt.close();
			if (where.length()==0) {
				return new VOListResponse(new ArrayList(),false,0);
			}
			where = " and ("+where.substring(0,where.length()-3)+") ";


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
          "DOC08_DELIVERY_NOTES.DOC_NUMBER in ("+
					" select DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
          " from DOC10_OUT_DELIVERY_NOTE_ITEMS where "+
          " DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
          " DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01=?  "+ // deliv. req. note
					where;

      if (docVO.getDocNumberDOC01()==null)
        sql += " and DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY-DOC10_OUT_DELIVERY_NOTE_ITEMS.INVOICE_QTY>0)";
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
      values.add(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE);
      values.add(ApplicationConsts.CLOSED);
      values.add(docVO.getCompanyCodeSys01DOC01());
      values.add(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE);


      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          OutDeliveryNotesVO.class,
          "Y",
          "N",
          null,
          pars,
          true
      );

      if (res.isError())
        throw new Exception(res.getErrorMessage());

      // check if the invoice document has been already created and there exists delivery notes linked to it...
      if (docVO.getDocNumberDOC01()!=null) {
        pstmt = conn.prepareStatement(
            "select DOC08_DELIVERY_NOTES.DOC_NUMBER from DOC08_DELIVERY_NOTES where "+
            "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
            "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
            "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
            "DOC08_DELIVERY_NOTES.DOC_STATE=? and "+
            "(DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER) "+
            " in (select DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR,DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER "+
            " from DOC10_OUT_DELIVERY_NOTE_ITEMS,DOC02_SELLING_ITEMS where "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01=? and "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01=? and "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01=? and "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
            " DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
            " DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
            " DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
            " DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01=DOC02_SELLING_ITEMS.ITEM_CODE_ITM01)"
        );

        pstmt.setString(1,ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE);
        pstmt.setString(2,ApplicationConsts.CLOSED);
        pstmt.setString(3,docVO.getCompanyCodeSys01DOC01());
        pstmt.setString(4,docVO.getDocTypeDoc01DOC01());
        pstmt.setBigDecimal(5,docVO.getDocYearDoc01DOC01());
        pstmt.setBigDecimal(6,docVO.getDocNumberDoc01DOC01());
        pstmt.setString(7,docVO.getDocTypeDOC01());
        pstmt.setBigDecimal(8,docVO.getDocYearDOC01());
        pstmt.setBigDecimal(9,docVO.getDocNumberDOC01());

        HashSet docNumberDOC08s = new HashSet();
        rset = pstmt.executeQuery();
        while(rset.next())
          docNumberDOC08s.add(rset.getBigDecimal(1));
        rset.close();

        java.util.List rows = ((VOListResponse)res).getRows();
        OutDeliveryNotesVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (OutDeliveryNotesVO)rows.get(i);
          if (docNumberDOC08s.contains(vo.getDocNumberDOC08()))
            vo.setSelected(Boolean.TRUE);
        }
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching out delivery notes list, related to the specified sale document",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
				try {
						rset.close();
				}
				catch (Exception exx) {}
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

