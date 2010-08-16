package org.jallinone.items.server;


import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.items.java.*;
import org.openswing.swing.server.QueryUtil;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.CustomizedWindows;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update item's variants and types.</p>
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
public class UpdateItemVariantsAction implements Action {


  private HashMap productVariants = new HashMap();
  private HashMap variantTypes = new HashMap();
  private HashMap variantTypeJoins = new HashMap();
  private HashMap variantCodeJoins = new HashMap();


  public UpdateItemVariantsAction() {
    productVariants.put("ITM11_VARIANTS_1","ITM16_PRODUCT_VARIANTS_1");
    productVariants.put("ITM12_VARIANTS_2","ITM17_PRODUCT_VARIANTS_2");
    productVariants.put("ITM13_VARIANTS_3","ITM18_PRODUCT_VARIANTS_3");
    productVariants.put("ITM14_VARIANTS_4","ITM19_PRODUCT_VARIANTS_4");
    productVariants.put("ITM15_VARIANTS_5","ITM20_PRODUCT_VARIANTS_5");

    variantTypes.put("ITM11_VARIANTS_1","ITM06_VARIANT_TYPES_1");
    variantTypes.put("ITM12_VARIANTS_2","ITM07_VARIANT_TYPES_2");
    variantTypes.put("ITM13_VARIANTS_3","ITM08_VARIANT_TYPES_3");
    variantTypes.put("ITM14_VARIANTS_4","ITM09_VARIANT_TYPES_4");
    variantTypes.put("ITM15_VARIANTS_5","ITM10_VARIANT_TYPES_5");

    variantTypeJoins.put("ITM11_VARIANTS_1","VARIANT_TYPE_ITM06");
    variantTypeJoins.put("ITM12_VARIANTS_2","VARIANT_TYPE_ITM07");
    variantTypeJoins.put("ITM13_VARIANTS_3","VARIANT_TYPE_ITM08");
    variantTypeJoins.put("ITM14_VARIANTS_4","VARIANT_TYPE_ITM09");
    variantTypeJoins.put("ITM15_VARIANTS_5","VARIANT_TYPE_ITM10");

    variantCodeJoins.put("ITM11_VARIANTS_1","VARIANT_CODE_ITM11");
    variantCodeJoins.put("ITM12_VARIANTS_2","VARIANT_CODE_ITM12");
    variantCodeJoins.put("ITM13_VARIANTS_3","VARIANT_CODE_ITM13");
    variantCodeJoins.put("ITM14_VARIANTS_4","VARIANT_CODE_ITM14");
    variantCodeJoins.put("ITM15_VARIANTS_5","VARIANT_CODE_ITM15");
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateItemVariants";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmtIns = null;
    PreparedStatement pstmtUpd = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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


      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      ItemVariantVO oldVO = null;
      ItemVariantVO newVO = null;

      HashSet pk = new HashSet();
      pk.add("companyCodeSys01ITM02");
      pk.add("progressiveHie02ITM02");

      if (newVOs.size()>0) {
        String tableName = ((ItemVariantVO)newVOs.get(0)).getTableName();
        String variantTypeJoin = (String)variantTypeJoins.get(tableName);
        String variantCodeJoin = (String)variantCodeJoins.get(tableName);
        String productVariant = (String)productVariants.get(tableName);
        String variantType = (String)variantTypes.get(tableName);

        pstmtIns = conn.prepareStatement(
            "insert into " + productVariant + "(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+variantTypeJoin+","+variantCodeJoin+",VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) "+
            "values(?,?,?,?,?,?,?)"
        );

        pstmtUpd = conn.prepareStatement(
            "update " + productVariant + " set ENABLED=? " +
            "where COMPANY_CODE_SYS01=? and " +
            "ITEM_CODE_ITM01=? and " +
            variantTypeJoin + "=? and " +
            variantCodeJoin + "=? "
        );


        for(int i=0;i<oldVOs.size();i++) {
          oldVO = (ItemVariantVO)oldVOs.get(i);
          newVO = (ItemVariantVO)newVOs.get(i);

          if (!Boolean.TRUE.equals(oldVO.getSelected()) && Boolean.TRUE.equals(newVO.getSelected())) {

            pstmtUpd.setString(1,"Y");
            pstmtUpd.setString(2,newVO.getCompanyCodeSys01());
            pstmtUpd.setString(3,newVO.getItemCodeItm01());
            pstmtUpd.setString(4,newVO.getVariantType());
            pstmtUpd.setString(5,newVO.getVariantCode());
            int res = pstmtUpd.executeUpdate();
            if (res==0) {
              pstmtIns.setString(1,newVO.getCompanyCodeSys01());
              pstmtIns.setString(2,newVO.getItemCodeItm01());
              pstmtIns.setString(3,newVO.getVariantType());
              pstmtIns.setString(4,newVO.getVariantCode());
              pstmtIns.setBigDecimal(5,newVO.getVariantProgressiveSys10());
              pstmtIns.setBigDecimal(6,newVO.getVariantTypeProgressiveSys10());
              pstmtIns.setString(7,"Y");
              pstmtIns.executeUpdate();
            }

          }
          else if (Boolean.TRUE.equals(oldVO.getSelected()) && !Boolean.TRUE.equals(newVO.getSelected())) {

            pstmtUpd.setString(1,"N");
            pstmtUpd.setString(2,newVO.getCompanyCodeSys01());
            pstmtUpd.setString(3,newVO.getItemCodeItm01());
            pstmtUpd.setString(4,newVO.getVariantType());
            pstmtUpd.setString(5,newVO.getVariantCode());
            pstmtUpd.executeUpdate();
          }

        } // end for

      } // end if on newVOs.size()>0


      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing item's variants and types",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmtIns.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmtUpd.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
