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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch items types from ITM02 table.</p>
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
public class LoadVariantBarcodesAction implements Action {


  public LoadVariantBarcodesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadVariantBarcodes";
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
            colVO.getVariantTypeITM07(),
            colVO.getVariantTypeITM08(),
            colVO.getVariantTypeITM09(),
            colVO.getVariantTypeITM10(),
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
          "VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "BAR_CODE from ITM22_VARIANT_BARCODES where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? AND ";

      if (matrixVO.getColumnDescriptors().size()==0) {
        if (containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
          sql +=
              "VARIANT_TYPE_ITM06=? AND "+
              "VARIANT_CODE_ITM11=? ";
        }
        else if (containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
          sql +=
              "VARIANT_TYPE_ITM07=? AND "+
              "VARIANT_CODE_ITM12=? ";
        }
        else if (containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
          sql +=
              "VARIANT_TYPE_ITM08=? AND "+
              "VARIANT_CODE_ITM13=? ";
        }
        else if (containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
          sql +=
              "VARIANT_TYPE_ITM09=? AND "+
              "VARIANT_CODE_ITM14=? ";
        }
        else if (containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
          sql +=
              "VARIANT_TYPE_ITM10=? AND "+
              "VARIANT_CODE_ITM15=? ";
        }
      }
      else
        sql +=
            "VARIANT_TYPE_ITM06=? AND "+
            "VARIANT_CODE_ITM11=? ";
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
        pstmt.setString(3,rowVO.getVariantTypeITM06());
        pstmt.setString(4,rowVO.getVariantCodeITM11());

        vo = new CustomValueObject();
        vo.setAttributeNameS0(rowVO.getRowDescription());
        rows.add(vo);

        rset = pstmt.executeQuery();
        while(rset.next()) {
          colPK = new ColPK(
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8)
          );

          if (matrixVO.getColumnDescriptors().size()==0) {
            try {
              CustomValueObject.class.getMethod("setAttributeNameS1",new Class[] {String.class}).invoke(vo, new Object[] {rset.getString(9)});
            }
            catch (Throwable ex) {
              ex.printStackTrace();
            }
          }
          else {
            pos = (Integer)cols.get(colPK);
            if (pos!=null) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameS" +pos,new Class[] {String.class}).invoke(vo, new Object[] {rset.getString(9)});
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching barcodes",ex);
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

    private String variantTypeITM07;
    private String variantTypeITM08;
    private String variantTypeITM09;
    private String variantTypeITM10;

    private String variantCodeITM12;
    private String variantCodeITM13;
    private String variantCodeITM14;
    private String variantCodeITM15;


    public ColPK(String variantTypeITM07,String variantTypeITM08,String variantTypeITM09,String variantTypeITM10,
                 String variantCodeITM12,String variantCodeITM13,String variantCodeITM14,String variantCodeITM15) {
      this.variantTypeITM07 = variantTypeITM07==null?ApplicationConsts.JOLLY:variantTypeITM07;
      this.variantTypeITM08 = variantTypeITM08==null?ApplicationConsts.JOLLY:variantTypeITM08;
      this.variantTypeITM09 = variantTypeITM09==null?ApplicationConsts.JOLLY:variantTypeITM09;
      this.variantTypeITM10 = variantTypeITM10==null?ApplicationConsts.JOLLY:variantTypeITM10;
      this.variantCodeITM12 = variantCodeITM12==null?ApplicationConsts.JOLLY:variantCodeITM12;
      this.variantCodeITM13 = variantCodeITM13==null?ApplicationConsts.JOLLY:variantCodeITM13;
      this.variantCodeITM14 = variantCodeITM14==null?ApplicationConsts.JOLLY:variantCodeITM14;
      this.variantCodeITM15 = variantCodeITM15==null?ApplicationConsts.JOLLY:variantCodeITM15;
    }


    public String getVariantCodeITM12() {
      return variantCodeITM12;
    }
    public String getVariantCodeITM13() {
      return variantCodeITM13;
    }
    public String getVariantCodeITM14() {
      return variantCodeITM14;
    }
    public String getVariantCodeITM15() {
      return variantCodeITM15;
    }
    public String getVariantTypeITM07() {
      return variantTypeITM07;
    }
    public String getVariantTypeITM08() {
      return variantTypeITM08;
    }
    public String getVariantTypeITM09() {
      return variantTypeITM09;
    }
    public String getVariantTypeITM10() {
      return variantTypeITM10;
    }

    public boolean equals(Object o) {
      if (o==null ||
        !(o instanceof ColPK))
        return false;
      ColPK vo = (ColPK)o;
      return
          (this.variantTypeITM07).equals(vo.variantTypeITM07) &&
          (this.variantTypeITM08).equals(vo.variantTypeITM08) &&
          (this.variantTypeITM09).equals(vo.variantTypeITM09) &&
          (this.variantTypeITM10).equals(vo.variantTypeITM10) &&
          (this.variantCodeITM12).equals(vo.variantCodeITM12) &&
          (this.variantCodeITM13).equals(vo.variantCodeITM13) &&
          (this.variantCodeITM14).equals(vo.variantCodeITM14) &&
          (this.variantCodeITM15).equals(vo.variantCodeITM15);
    }


    public int hashCode() {
      return
         (variantTypeITM07+
          variantTypeITM08+
          variantTypeITM09+
          variantTypeITM10+
          variantCodeITM12+
          variantCodeITM13+
          variantCodeITM14+
          variantCodeITM15).hashCode();
    }

  } // end inner class


}
