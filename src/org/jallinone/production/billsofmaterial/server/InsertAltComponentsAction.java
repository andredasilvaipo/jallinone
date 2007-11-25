package org.jallinone.production.billsofmaterial.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new alternative components in ITM04 table.</p>
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
public class InsertAltComponentsAction implements Action {


  public InsertAltComponentsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertAltComponents";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
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

     ArrayList list = (ArrayList)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM04","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01ITM04","ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveITM04","PROGRESSIVE");

      // check if there already exist the specified components in some group (in some PROGRESSIVE...)
      String items = "";
      AltComponentVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (AltComponentVO) list.get(i);
        items += "'"+vo.getItemCodeItm01ITM04()+"',";
      }
      if (items.length()>0)
        items = items.substring(0,items.length()-1);
      pstmt = conn.prepareStatement(
        "select PROGRESSIVE,ITEM_CODE_ITM01 from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01 in ("+items+")"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
      rset = pstmt.executeQuery();
      ArrayList progressives = new ArrayList();
      HashSet itms = new HashSet();
      while(rset.next()) {
        progressives.add(rset.getBigDecimal(1));
        itms.add(rset.getString(2));
      }
      rset.close();
      pstmt.close();

      // if progressives size = 0 then add all components to the current item group
      // if progressives size = 1 then add all components to the fetched group (fetched PROGRESSIVE)
      // if progressives size > 1 then join all groups found and add all components to the unique group just created
      BigDecimal progressiveITM04 = null;
      Response res = null;
      if (progressives.size()==0) {
        vo = (AltComponentVO) list.get(0);
        if (vo.getProgressiveITM04()==null) {

          // maybe there exists ONE record for the current item...
          pstmt = conn.prepareStatement(
            "select PROGRESSIVE from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=?"
          );
          pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
          pstmt.setString(2,vo.getCurrentItemCodeItm01ITM04());
          rset = pstmt.executeQuery();
          if (rset.next())
            progressiveITM04 = rset.getBigDecimal(1);
          rset.close();
          pstmt.close();
          if (progressiveITM04==null) {
            // this is the first alternative component defined for this item: create a new progressive...
            progressiveITM04 = ProgressiveUtils.getInternalProgressive("ITM04_ALTERNATIVE_ITEMS", "PROGRESSIVE", conn);
            // insert current item into ITM04...
            AltComponentVO currVO = new AltComponentVO();
            currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
            currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
            currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
            currVO.setProgressiveITM04(progressiveITM04);
            res = QueryUtil.insertTable(
                conn,
                userSessionPars,
                currVO,
                "ITM04_ALTERNATIVE_ITEMS",
                attribute2dbField,
                "Y",
                "N",
                context,
                true
            );
            if (res.isError()) {
              conn.rollback();
              return res;
            }
          }
        }
        else
          progressiveITM04 = vo.getProgressiveITM04();
      }
      else if (progressives.size()==1) {
        // add all components to the fetched group (fetched PROGRESSIVE)
        progressiveITM04 = (BigDecimal)progressives.get(0);
        // insert current item into ITM04...
        AltComponentVO currVO = new AltComponentVO();
        currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
        currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
        currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
        currVO.setProgressiveITM04(progressiveITM04);
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            currVO,
            "ITM04_ALTERNATIVE_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }
      else if (progressives.size()>1) {
        // join all groups found and add all components to the unique group just created
        String progrs = "";
        for(int i=0;i<progressives.size();i++)
          progrs += progressives.get(i)+",";
        progrs = progrs.substring(0,progrs.length()-1);
        pstmt = conn.prepareStatement(
          "select ITEM_CODE_ITM01 from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and PROGRESSIVE in ("+progrs+")"
        );
        pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
        rset = pstmt.executeQuery();
        itms = new HashSet();
        while(rset.next())
          if (!rset.getString(1).equals(vo.getCurrentItemCodeItm01ITM04()))
            itms.add(rset.getString(1));
        rset.close();
        pstmt.close();

        // delete all items of all retrieved groups...
        pstmt = conn.prepareStatement(
          "delete from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and PROGRESSIVE in ("+progrs+")"
        );
        pstmt.execute();

        // insert all items in a single new group...
        progressiveITM04 = ProgressiveUtils.getInternalProgressive("ITM04_ALTERNATIVE_ITEMS", "PROGRESSIVE", conn);
        // insert current item into ITM04...
        AltComponentVO currVO = new AltComponentVO();
        currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
        currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
        currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
        currVO.setProgressiveITM04(progressiveITM04);
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            currVO,
            "ITM04_ALTERNATIVE_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

        Iterator it = itms.iterator();
        while(it.hasNext()) {
          currVO = new AltComponentVO();
          currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
          currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
          currVO.setItemCodeItm01ITM04(it.next().toString());
          currVO.setProgressiveITM04(progressiveITM04);
          res = QueryUtil.insertTable(
              conn,
              userSessionPars,
              currVO,
              "ITM04_ALTERNATIVE_ITEMS",
              attribute2dbField,
              "Y",
              "N",
              context,
              true
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }

      }


      // add alternative components to the calculated progressive...
      for(int i=0;i<list.size();i++) {
        vo = (AltComponentVO)list.get(i);
        vo.setProgressiveITM04(progressiveITM04);
        if (progressives.size()!=1 || !itms.contains(vo.getItemCodeItm01ITM04())) {
          // insert into ITM04...
          res = QueryUtil.insertTable(
              conn,
              userSessionPars,
              vo,
              "ITM04_ALTERNATIVE_ITEMS",
              attribute2dbField,
              "Y",
              "N",
              context,
              true
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
      }

      Response answer = new VOListResponse(list,false,list.size());

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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting new alternative components", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex4) {
      }
      try {
        pstmt.close();
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

