package org.jallinone.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantNameVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch min stocks defined for item's variants from ITM23 table.</p>
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
public class LoadVariantMinStocksAction implements Action {


  public LoadVariantMinStocksAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadVariantMinStocks";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));

      // indexing cols...
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)inputPar;;
      HashMap cols = new HashMap();
      VariantsMatrixColumnVO colVO = null;
      for(int i=0;i<matrixVO.getColumnDescriptors().size();i++) {
        colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(i);
        cols.put(
          new ColPK(
            colVO.getVariantTypeITM06(),
            colVO.getVariantTypeITM07(),
            colVO.getVariantTypeITM08(),
            colVO.getVariantTypeITM09(),
            colVO.getVariantTypeITM10(),
            colVO.getVariantCodeITM11(),
            colVO.getVariantCodeITM12(),
            colVO.getVariantCodeITM13(),
            colVO.getVariantCodeITM14(),
            colVO.getVariantCodeITM15()
          ),
          new Integer(i+1)
        );
      }

      String sql =
          "select "+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "MIN_STOCK "+
          "from ITM23_VARIANT_MIN_STOCKS where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? AND ";

      VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants().get(0);
      if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
        sql +=
            "VARIANT_TYPE_ITM06=? AND "+
            "VARIANT_CODE_ITM11=? ";
      }
      else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
        sql +=
            "VARIANT_TYPE_ITM07=? AND "+
            "VARIANT_CODE_ITM12=? ";
      }
      else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
        sql +=
            "VARIANT_TYPE_ITM08=? AND "+
            "VARIANT_CODE_ITM13=? ";
      }
      else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
        sql +=
            "VARIANT_TYPE_ITM09=? AND "+
            "VARIANT_CODE_ITM14=? ";
      }
      else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
        sql +=
            "VARIANT_TYPE_ITM10=? AND "+
            "VARIANT_CODE_ITM15=? ";
      }

      pstmt = conn.prepareStatement(sql);

      ArrayList rows = new ArrayList();
      CustomValueObject vo = null;
      VariantsMatrixRowVO rowVO = null;
      ColPK colPK = null;
      Integer pos = null;
      for(int i=0;i<matrixVO.getRowDescriptors().size();i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

        pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
        pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());
        pstmt.setString(3,VariantsMatrixUtils.getVariantType(matrixVO,rowVO));
        pstmt.setString(4,VariantsMatrixUtils.getVariantCode(matrixVO,rowVO));

        vo = new CustomValueObject();
        vo.setAttributeNameS0(rowVO.getRowDescription());
        rows.add(vo);

        rset = pstmt.executeQuery();
        while(rset.next()) {
          colPK = new ColPK(
            varVO.getTableName().equals("ITM11_VARIANTS_1")?ApplicationConsts.JOLLY:rset.getString(1),
            varVO.getTableName().equals("ITM12_VARIANTS_2")?ApplicationConsts.JOLLY:rset.getString(2),
            varVO.getTableName().equals("ITM13_VARIANTS_3")?ApplicationConsts.JOLLY:rset.getString(3),
            varVO.getTableName().equals("ITM14_VARIANTS_4")?ApplicationConsts.JOLLY:rset.getString(4),
            varVO.getTableName().equals("ITM15_VARIANTS_5")?ApplicationConsts.JOLLY:rset.getString(5),
            varVO.getTableName().equals("ITM11_VARIANTS_1")?ApplicationConsts.JOLLY:rset.getString(6),
            varVO.getTableName().equals("ITM12_VARIANTS_2")?ApplicationConsts.JOLLY:rset.getString(7),
            varVO.getTableName().equals("ITM13_VARIANTS_3")?ApplicationConsts.JOLLY:rset.getString(8),
            varVO.getTableName().equals("ITM14_VARIANTS_4")?ApplicationConsts.JOLLY:rset.getString(9),
            varVO.getTableName().equals("ITM15_VARIANTS_5")?ApplicationConsts.JOLLY:rset.getString(10)
          );

          if (matrixVO.getColumnDescriptors().size()==0) {
            try {
              CustomValueObject.class.getMethod("setAttributeNameN0",new Class[] {BigDecimal.class}).invoke(vo, new Object[] {rset.getBigDecimal(11)});
            }
            catch (Throwable ex) {
              ex.printStackTrace();
            }
          }
          else {
            pos = (Integer)cols.get(colPK);
            if (pos!=null) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" +pos,new Class[] {BigDecimal.class}).invoke(vo, new Object[] {rset.getBigDecimal(11)});
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
            }
            else
              Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Variants not found: "+colPK,null);
          }

        }
        rset.close();
      }


      VOListResponse res = new VOListResponse(rows,false,rows.size());

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        res
      ));
      return res;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching min stocks",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().size();i++)
      if (((VariantNameVO)vo.getManagedVariants().get(i)).getTableName().equals(tableName))
        return true;
    return false;
  }


  class ColPK {

    private String variantTypeItm06;
    private String variantTypeItm07;
    private String variantTypeItm08;
    private String variantTypeItm09;
    private String variantTypeItm10;

    private String variantCodeItm11;
    private String variantCodeItm12;
    private String variantCodeItm13;
    private String variantCodeItm14;
    private String variantCodeItm15;


    public ColPK(String variantTypeItm06,String variantTypeItm07,String variantTypeItm08,String variantTypeItm09,String variantTypeItm10,
                 String variantCodeItm11,String variantCodeItm12,String variantCodeItm13,String variantCodeItm14,String variantCodeItm15) {
      this.variantTypeItm06 = variantTypeItm06==null?ApplicationConsts.JOLLY:variantTypeItm06;
      this.variantTypeItm07 = variantTypeItm07==null?ApplicationConsts.JOLLY:variantTypeItm07;
      this.variantTypeItm08 = variantTypeItm08==null?ApplicationConsts.JOLLY:variantTypeItm08;
      this.variantTypeItm09 = variantTypeItm09==null?ApplicationConsts.JOLLY:variantTypeItm09;
      this.variantTypeItm10 = variantTypeItm10==null?ApplicationConsts.JOLLY:variantTypeItm10;
      this.variantCodeItm11 = variantCodeItm11==null?ApplicationConsts.JOLLY:variantCodeItm11;
      this.variantCodeItm12 = variantCodeItm12==null?ApplicationConsts.JOLLY:variantCodeItm12;
      this.variantCodeItm13 = variantCodeItm13==null?ApplicationConsts.JOLLY:variantCodeItm13;
      this.variantCodeItm14 = variantCodeItm14==null?ApplicationConsts.JOLLY:variantCodeItm14;
      this.variantCodeItm15 = variantCodeItm15==null?ApplicationConsts.JOLLY:variantCodeItm15;
    }


    public String getVariantCodeItm11() {
      return variantCodeItm11;
    }
    public String getVariantCodeItm12() {
      return variantCodeItm12;
    }
    public String getVariantCodeItm13() {
      return variantCodeItm13;
    }
    public String getVariantCodeItm14() {
      return variantCodeItm14;
    }
    public String getVariantCodeItm15() {
      return variantCodeItm15;
    }
    public String getVariantTypeItm06() {
      return variantTypeItm06;
    }
    public String getVariantTypeItm07() {
      return variantTypeItm07;
    }
    public String getVariantTypeItm08() {
      return variantTypeItm08;
    }
    public String getVariantTypeItm09() {
      return variantTypeItm09;
    }
    public String getVariantTypeItm10() {
      return variantTypeItm10;
    }

    public boolean equals(Object o) {
      if (o==null ||
        !(o instanceof ColPK))
        return false;
      ColPK vo = (ColPK)o;
      return
          (this.variantTypeItm06).equals(vo.variantTypeItm06) &&
          (this.variantTypeItm07).equals(vo.variantTypeItm07) &&
          (this.variantTypeItm08).equals(vo.variantTypeItm08) &&
          (this.variantTypeItm09).equals(vo.variantTypeItm09) &&
          (this.variantTypeItm10).equals(vo.variantTypeItm10) &&
          (this.variantCodeItm12).equals(vo.variantCodeItm12) &&
          (this.variantCodeItm13).equals(vo.variantCodeItm13) &&
          (this.variantCodeItm14).equals(vo.variantCodeItm14) &&
          (this.variantCodeItm15).equals(vo.variantCodeItm15);
    }


    public int hashCode() {
      return
         (variantTypeItm06+
          variantTypeItm07+
          variantTypeItm08+
          variantTypeItm09+
          variantTypeItm10+
          variantCodeItm11+
          variantCodeItm12+
          variantCodeItm13+
          variantCodeItm14+
          variantCodeItm15).hashCode();
    }

  } // end inner class


}
