package org.jallinone.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new item in ITM01 table.</p>
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
public class InsertItemAction implements Action {


  public InsertItemAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertItem";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("ITM01").get(0).toString();

      DetailItemVO vo = (DetailItemVO)inputPar;
      vo.setEnabledITM01("Y");
      if (vo.getCompanyCodeSys01ITM01()==null)
        vo.setCompanyCodeSys01ITM01(companyCode);

      // generate progressive for item description...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
      vo.setProgressiveSys10ITM01(progressiveSYS10);

      if (vo.getAddDescriptionSYS10()!=null) {
        // generate progressive for item additional description...
        BigDecimal addProgressiveSYS10 = TranslationUtils.insertTranslations(vo.getAddDescriptionSYS10(),conn);
        vo.setAddProgressiveSys10ITM01(addProgressiveSYS10);
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM01","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeITM01","ITEM_CODE");
      attribute2dbField.put("progressiveHie02ITM01","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01ITM01","PROGRESSIVE_HIE01");
      attribute2dbField.put("addProgressiveSys10ITM01","ADD_PROGRESSIVE_SYS10");
      attribute2dbField.put("progressiveSys10ITM01","PROGRESSIVE_SYS10");
      attribute2dbField.put("minSellingQtyITM01","MIN_SELLING_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("vatCodeReg01ITM01","VAT_CODE_REG01");
      attribute2dbField.put("grossWeightITM01","GROSS_WEIGHT");
      attribute2dbField.put("grossWeightUmCodeReg02ITM01","GROSS_WEIGHT_UM_CODE_REG02");
      attribute2dbField.put("netWeightITM01","NET_WEIGHT");
      attribute2dbField.put("netWeightUmCodeReg02ITM01","NET_WEIGHT_UM_CODE_REG02");
      attribute2dbField.put("widthITM01","WIDTH");
      attribute2dbField.put("widthUmCodeReg02ITM01","WIDTH_UM_CODE_REG02");
      attribute2dbField.put("heightITM01","HEIGHT");
      attribute2dbField.put("heightUmCodeReg02ITM01","HEIGHT_UM_CODE_REG02");
      attribute2dbField.put("noteITM01","NOTE");
      attribute2dbField.put("colorCodeReg13ITM01","COLOR_CODE_REG13");
      attribute2dbField.put("sizeCodeReg14ITM01","SIZE_CODE_REG14");
      attribute2dbField.put("largeImageITM01","LARGE_IMAGE");
      attribute2dbField.put("smallImageITM01","SMALL_IMAGE");
      attribute2dbField.put("enabledITM01","ENABLED");
      attribute2dbField.put("serialNumberRequiredITM01","SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("versionITM01","VERSION");
      attribute2dbField.put("revisionITM01","REVISION");
      attribute2dbField.put("manufactureCodePro01ITM01","MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("startDateITM01","START_DATE");


      if (vo.getSmallImage()!=null) {
        BigDecimal imageProgressive = ProgressiveUtils.getInternalProgressive("ITM01_ITEMS","SMALL_IMG",conn);
        vo.setSmallImageITM01("SMALL_IMG"+imageProgressive);
        attribute2dbField.put("smallImageITM01",imageProgressive);

        // save image on file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath()+appPath;
        }
        new File(appPath).mkdirs();
        FileOutputStream out = new FileOutputStream(appPath+vo.getSmallImageITM01());
        out.write(vo.getSmallImage());
        out.close();
      }

      if (vo.getLargeImage()!=null) {
        BigDecimal imageProgressive = ProgressiveUtils.getInternalProgressive("ITM01_ITEMS","LARGE_IMG",conn);
        vo.setLargeImageITM01("LARGE_IMG"+imageProgressive);
        attribute2dbField.put("largeImageITM01",imageProgressive);

        // save image on file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath()+appPath;
        }
        new File(appPath).mkdirs();
        FileOutputStream out = new FileOutputStream(appPath+vo.getLargeImageITM01());
        out.write(vo.getLargeImage());
        out.close();
      }

      // insert into ITM01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "ITM01_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(262) // window identifier...
      );

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new item",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
