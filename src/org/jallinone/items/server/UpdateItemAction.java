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
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing item.</p>
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
public class UpdateItemAction implements Action {


  public UpdateItemAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateItem";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    Statement stmt = null;
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


      DetailItemVO oldVO = (DetailItemVO)((ValueObject[])inputPar)[0];
      DetailItemVO newVO = (DetailItemVO)((ValueObject[])inputPar)[1];


      // update item description...
      TranslationUtils.updateTranslation(
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveSys10ITM01(),
          serverLanguageId,
          conn
      );

      if (newVO.getAddDescriptionSYS10()!=null) {
        // update item additional description...
        if (newVO.getAddProgressiveSys10ITM01()!=null)
          TranslationUtils.updateTranslation(
              oldVO.getAddDescriptionSYS10(),
              newVO.getAddDescriptionSYS10(),
              newVO.getAddProgressiveSys10ITM01(),
              serverLanguageId,
              conn
          );
        else {
          BigDecimal addProgressiveSYS10 = TranslationUtils.insertTranslations(newVO.getAddDescriptionSYS10(),conn);
          newVO.setAddProgressiveSys10ITM01(addProgressiveSYS10);
        }
      }


      if (oldVO.getSmallImage()!=null && newVO.getSmallImage()==null) {
        // remove image from file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath()+appPath;
        }
        new File(appPath+oldVO.getSmallImageITM01()).delete();
      }
      else if (newVO.getSmallImage()!=null) {
        if (oldVO.getSmallImage()==null) {
          BigDecimal imageProgressive = ProgressiveUtils.getInternalProgressive("ITM01_ITEMS","SMALL_IMG",conn);
          newVO.setSmallImageITM01("SMALL_IMG"+imageProgressive);
        }
        else
          newVO.setSmallImageITM01(oldVO.getSmallImageITM01());

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
        File f = new File(appPath+newVO.getSmallImageITM01());
        f.delete();
        FileOutputStream out = new FileOutputStream(f);
        out.write(newVO.getSmallImage());
        out.close();
      }


      if (oldVO.getLargeImage()!=null && newVO.getLargeImage()==null) {
        // remove image from file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath()+appPath;
        }
        new File(appPath+oldVO.getLargeImageITM01()).delete();
      }
      else if (newVO.getLargeImage()!=null) {
        if (oldVO.getLargeImage()==null) {
          BigDecimal imageProgressive = ProgressiveUtils.getInternalProgressive("ITM01_ITEMS","LARGE_IMG",conn);
          newVO.setLargeImageITM01("LARGE_IMG"+imageProgressive);
        }
        else
          newVO.setLargeImageITM01(oldVO.getLargeImageITM01());

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
        File f = new File(appPath+newVO.getLargeImageITM01());
        f.delete();
        FileOutputStream out = new FileOutputStream(f);
        out.write(newVO.getLargeImage());
        out.close();
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
      attribute2dbField.put("serialNumberRequiredITM01","SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("versionITM01","VERSION");
      attribute2dbField.put("revisionITM01","REVISION");
      attribute2dbField.put("manufactureCodePro01ITM01","MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("startDateITM01","START_DATE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01ITM01");
      pkAttributes.add("itemCodeITM01");

      // update ITM01 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          userSessionPars,
          pkAttributes,
          oldVO,
          newVO,
          "ITM01_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(262) // window identifier...
      );

      if (res.isError()) {
        conn.rollback();
        return res;
      }

      if (newVO.getManufactureCodePro01ITM01()==null || newVO.getManufactureCodePro01ITM01().equals("")) {
        // phisically delete the components records in ITM03...
        stmt = conn.createStatement();
        stmt.execute(
            "delete from ITM03_COMPONENTS where "+
            "COMPANY_CODE_SYS01='"+newVO.getCompanyCodeSys01ITM01()+"' and "+
            "PARENT_ITEM_CODE_ITM01='"+newVO.getItemCodeITM01()+"'"
        );
        stmt.close();
      }


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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an existing item",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
