package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.sql.Date;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import java.sql.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to confirm a sale document:
 * it change the document state and calculate the doc. sequence that returns to calling class.</p>
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
public class ConfirmSaleDocBean  implements ConfirmSaleDoc {


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


  private LoadSaleDocBean loadSaleDocBean;

  public void setLoadSaleDocBean(LoadSaleDocBean loadSaleDocBean) {
	  this.loadSaleDocBean = loadSaleDocBean;
  }

  private SaleDocsBean bean;

  public void setBean(SaleDocsBean bean) {
    this.bean = bean;
  }

  private LoadSaleDocRowsBean rowsBean;

  public void setRowsBean(LoadSaleDocRowsBean rowsBean) {
    this.rowsBean = rowsBean;
  }

  private InsertSaleItemBean insbean;

  public void setInsbean(InsertSaleItemBean insbean) {
	  this.insbean = insbean;
  }

	private LoadSaleDocRowBean loadSaleDocRowBean;

	public void setLoadSaleDocRowBean(LoadSaleDocRowBean loadSaleDocRowBean) {
		this.loadSaleDocRowBean = loadSaleDocRowBean;
	}



  public ConfirmSaleDocBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse confirmSaleDoc(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocPK pk, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...
      rowsBean.setConn(conn); // use same transaction...
      insbean.setConn(conn);
      loadSaleDocBean.setConn(conn);
			loadSaleDocRowBean.setConn(conn);


      // generate progressive for doc. sequence...
      pstmt = conn.prepareStatement(
        "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
      );
      pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(2,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(3,pk.getDocYearDOC01());
      ResultSet rset = pstmt.executeQuery();
      int docSequenceDOC01 = 1;
      if (rset.next())
        docSequenceDOC01 = rset.getInt(1)+1;
      rset.close();
      pstmt.close();


      // create delivery requests from sale orders/contracts...
      if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
          pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE)) {

        // retrieve doc header...
    	  DetailSaleDocVO originalVO = loadSaleDocBean.loadSaleDoc(
          pk,
          serverLanguageId,
          username,
          new ArrayList()
        );

        // retrieve doc rows...
        GridParams gridParams = new GridParams();
        gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
        Response res = rowsBean.loadSaleDocRows(
          variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
          gridParams,
          serverLanguageId,
          username

        );
        if (res.isError())
          throw new Exception(res.getErrorMessage());
        java.util.List rows = ((VOListResponse)res).getRows();

        // group rows per delivery date...
        GridSaleDocRowVO gridRowVO = null;
        HashMap map = new HashMap(); // collection of couples <delivery date,list of DetailSaleDocRowVO objects having that date>
        ArrayList dates = new ArrayList(); // list of delivery dates (to sort...)
        ArrayList aux = null;
        for(int i=0;i<rows.size();i++) {
          gridRowVO = (GridSaleDocRowVO)rows.get(i);

	         // skip rows to do not move in warehouse...
					 if (gridRowVO.getNoWarehouseMovITM01().booleanValue())
						 continue;

          aux = (ArrayList)map.get(gridRowVO.getDeliveryDateDOC02());
          if (aux==null) {
            aux = new ArrayList();
            map.put(gridRowVO.getDeliveryDateDOC02(),aux);
            dates.add(gridRowVO.getDeliveryDateDOC02());
          }
          aux.add(gridRowVO);
        }
        Date[] datesToSort = (Date[])dates.toArray(new Date[dates.size()]);
        Arrays.sort(datesToSort);

        // create delivery requests for each delivery date, ordered by date...
        DetailSaleDocRowVO rowVO = null;
        DetailSaleDocVO vo = null;
        for(int i=0;i<datesToSort.length;i++) {
          vo = (DetailSaleDocVO)originalVO.clone();
          vo.setDocTypeDoc01DOC01(vo.getDocTypeDOC01());
          vo.setDocYearDoc01DOC01(vo.getDocYearDOC01());
          vo.setDocNumberDoc01DOC01(vo.getDocNumberDOC01());
          vo.setDocSequenceDoc01DOC01(new BigDecimal(docSequenceDOC01));
          vo.setDocRefNumberDOC01(docSequenceDOC01+"/"+vo.getDocYearDOC01());
          vo.setDocTypeDOC01(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE);
          vo.setDeliveryDateDOC01(datesToSort[i]);
          vo.setDocStateDOC01(ApplicationConsts.CONFIRMED);

          // generate progressive for doc. sequence...
          pstmt = conn.prepareStatement(
            "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
          );
          pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
          pstmt.setString(2,vo.getDocTypeDOC01());
          pstmt.setBigDecimal(3,pk.getDocYearDOC01());
          rset = pstmt.executeQuery();
          int docSequence = 1;
          if (rset.next())
            docSequence = rset.getInt(1)+1;
          rset.close();
          pstmt.close();

          vo.setDocSequenceDOC01(new BigDecimal(docSequence));

          res = bean.insertSaleDoc(
            vo,
            serverLanguageId,
            username,
            vo.getCompanyCodeSys01DOC01(),
            new ArrayList()
          );
          if (res.isError())
            throw new Exception(res.getErrorMessage());


          // insert rows related to the current delivery date...
          aux = (ArrayList)map.get(datesToSort[i]);
          for(int k=0;k<aux.size();k++) {
            gridRowVO = (GridSaleDocRowVO)aux.get(k);

            // retrieve detail...
            res = loadSaleDocRowBean.loadSaleDocRow(
                variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
                new SaleDocRowPK(
                  gridRowVO.getCompanyCodeSys01DOC02(),
                  gridRowVO.getDocTypeDOC02(),
                  gridRowVO.getDocYearDOC02(),
                  gridRowVO.getDocNumberDOC02(),
                  gridRowVO.getItemCodeItm01DOC02(),
                  gridRowVO.getVariantTypeItm06DOC02(),
                  gridRowVO.getVariantCodeItm11DOC02(),
                  gridRowVO.getVariantTypeItm07DOC02(),
                  gridRowVO.getVariantCodeItm12DOC02(),
                  gridRowVO.getVariantTypeItm08DOC02(),
                  gridRowVO.getVariantCodeItm13DOC02(),
                  gridRowVO.getVariantTypeItm09DOC02(),
                  gridRowVO.getVariantCodeItm14DOC02(),
                  gridRowVO.getVariantTypeItm10DOC02(),
                  gridRowVO.getVariantCodeItm15DOC02()
                ),
                serverLanguageId,
                username

            );
            if (res.isError()) {
              throw new Exception(res.getErrorMessage());
            }
            rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
            rowVO.setDocTypeDOC02(vo.getDocTypeDOC01());
            rowVO.setDocNumberDOC02(vo.getDocNumberDOC01());

            insbean.insertSaleItem(
								variant1Descriptions,
								variant2Descriptions,
								variant3Descriptions,
								variant4Descriptions,
								variant5Descriptions,
                rowVO,
                serverLanguageId,
                username

            );
            if (res.isError()) {
              throw new Exception(res.getErrorMessage());
            }

          } // end for on rows for a specified deliv.date

        } // end for on delivery dates...

      } // end del.req. creation for sale orders/contracts...


      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.CONFIRMED);
      pstmt.setInt(2,docSequenceDOC01);
      pstmt.setString(3,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(4,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(5,pk.getDocYearDOC01());
      pstmt.setBigDecimal(6,pk.getDocNumberDOC01());
      pstmt.execute();

      return new VOResponse(new BigDecimal(docSequenceDOC01));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while confirming a sale order",ex);
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

      }
      catch (Exception exx) {}

      try {
        bean.setConn(null);
        rowsBean.setConn(null);
        insbean.setConn(null);
        loadSaleDocBean.setConn(null);
				loadSaleDocRowBean.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

