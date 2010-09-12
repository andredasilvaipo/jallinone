package org.jallinone.system.translations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.registers.color.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantTypeVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.variants.java.VariantVO;
import org.jallinone.system.languages.java.LanguageVO;
import org.jallinone.system.translations.java.TopicVO;
import org.openswing.swing.customvo.java.CustomValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch translations from SYS10.</p>
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
public class LoadTranslationsAction implements Action {


  public LoadTranslationsAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadTranslations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
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

      GridParams gridParams = (GridParams)inputPar;
      String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      TopicVO topic = (TopicVO)gridParams.getOtherGridParams().get(ApplicationConsts.TOPIC);
      java.util.List langs = topic.getLangs();


      String select = "select ";
      String from = " from "+topic.getTableName()+",";
      String where = " where ";

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("attributeNameN0","T0.PROGRESSIVE");

      LanguageVO vo = null;
      for(int i=0;i<langs.size();i++) {
        vo = (LanguageVO)langs.get(i);

        attribute2dbField.put("attributeNameS"+i,"T"+i+".DESCRIPTION");

        select += "T"+i+".DESCRIPTION,";
        from += "SYS10_TRANSLATIONS T"+i+",";
        where +=
            "not T"+i+".DESCRIPTION='"+ApplicationConsts.JOLLY+"' and "+
            "T"+i+".LANGUAGE_CODE='"+vo.getLanguageCodeSYS09()+"' and "+
            "T"+i+".PROGRESSIVE="+topic.getTableName()+"."+topic.getColumnName()+" and ";
        if (topic.isUseCompanyCode())
          where += topic.getTableName()+".COMPANY_CODE_SYS01='"+companyCodeSys01+"' and ";
        if (topic.isUseEnabled())
          where += topic.getTableName()+".ENABLED='Y' and ";
      }
      select = select.substring(0,select.length()-1); // remove last comma...
      from = from.substring(0,from.length()-1); // remove last comma...
      where = where.substring(0,where.length()-4); // remove last AND...

      String sql = select+",T0.PROGRESSIVE "+from+where+" order by T0.PROGRESSIVE ";


      // read from SYS10 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          new ArrayList(),
          attribute2dbField,
          CustomValueObject.class,
          "Y",
          "N",
          context,
          gridParams,
          30,
          true
      );

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


      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching translations",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
