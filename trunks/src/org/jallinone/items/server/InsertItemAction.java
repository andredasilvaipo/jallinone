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
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


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
    Statement stmt = null;
    PreparedStatement pstmt = null;
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

      if (vo.getBarCodeITM01()==null || vo.getBarCodeITM01().equals("")) {
        new BarCodeGeneratorImpl().calculateBarCode(conn,vo);
      }

      // check for barcode uniqueness...
      if (vo.getBarCodeITM01()!=null && !vo.getBarCodeITM01().trim().equals("")) {
        stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from ITM01_ITEMS where COMPANY_CODE_SYS01='"+companyCode+"' and BAR_CODE='"+vo.getBarCodeITM01()+"'");
        boolean barCodeFound = false;
        if (rset.next()) {
          barCodeFound = true;
        }
        rset.close();
        stmt.close();
        if (barCodeFound) {
          ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
          Resources res = factory.getResources(userSessionPars.getLanguageId());
          return new ErrorResponse(res.getResource("barcode already assigned to another item"));
        }
      }


      vo.setEnabledITM01("Y");
      if (vo.getCompanyCodeSys01ITM01()==null)
        vo.setCompanyCodeSys01ITM01(companyCode);
      if (vo.getUseVariant1ITM01()==null)
        vo.setUseVariant1ITM01(Boolean.FALSE);
      if (vo.getUseVariant2ITM01()==null)
        vo.setUseVariant2ITM01(Boolean.FALSE);
      if (vo.getUseVariant3ITM01()==null)
        vo.setUseVariant3ITM01(Boolean.FALSE);
      if (vo.getUseVariant4ITM01()==null)
        vo.setUseVariant4ITM01(Boolean.FALSE);
      if (vo.getUseVariant5ITM01()==null)
        vo.setUseVariant5ITM01(Boolean.FALSE);

      // generate progressive for item description...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ITM01(),conn);
      vo.setProgressiveSys10ITM01(progressiveSYS10);

      if (vo.getAddDescriptionSYS10()!=null) {
        // generate progressive for item additional description...
        BigDecimal addProgressiveSYS10 = TranslationUtils.insertTranslations(vo.getAddDescriptionSYS10(),vo.getCompanyCodeSys01ITM01(),conn);
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
      attribute2dbField.put("largeImageITM01","LARGE_IMAGE");
      attribute2dbField.put("smallImageITM01","SMALL_IMAGE");
      attribute2dbField.put("enabledITM01","ENABLED");
      attribute2dbField.put("serialNumberRequiredITM01","SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("versionITM01","VERSION");
      attribute2dbField.put("revisionITM01","REVISION");
      attribute2dbField.put("manufactureCodePro01ITM01","MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("startDateITM01","START_DATE");

      attribute2dbField.put("useVariant1ITM01","USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","USE_VARIANT_5");

      attribute2dbField.put("barCodeITM01","BAR_CODE");
      attribute2dbField.put("barcodeTypeITM01","BARCODE_TYPE");


      if (vo.getSmallImage()!=null) {
        BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01(),"ITM01_ITEMS","SMALL_IMG",conn);
        vo.setSmallImageITM01("SMALL_IMG"+imageProgressive);
        attribute2dbField.put("smallImageITM01",imageProgressive);

        // save image on file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
        }
        new File(appPath).mkdirs();
        FileOutputStream out = new FileOutputStream(appPath+vo.getSmallImageITM01());
        out.write(vo.getSmallImage());
        out.close();
      }

      if (vo.getLargeImage()!=null) {
        BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01(),"ITM01_ITEMS","LARGE_IMG",conn);
        vo.setLargeImageITM01("LARGE_IMG"+imageProgressive);
        attribute2dbField.put("largeImageITM01",imageProgressive);

        // save image on file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.IMAGE_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
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
          vo.getProgressiveHie01HIE02() // window identifier...
      );

      // insert product variants...
      stmt = conn.createStatement();
      if (!Boolean.TRUE.equals(vo.getUseVariant1ITM01())) {
        stmt.execute(
            "insert into ITM16_PRODUCT_VARIANTS_1(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11," +
            "VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) VALUES("+
            "'"+vo.getCompanyCodeSys01ITM01()+"','"+vo.getItemCodeITM01()+"','*','*',3412,3362,'Y')"
            );
      }
      if (!Boolean.TRUE.equals(vo.getUseVariant2ITM01())) {
        stmt.execute(
            "insert into ITM17_PRODUCT_VARIANTS_2(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12," +
            "VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) VALUES("+
            "'"+vo.getCompanyCodeSys01ITM01()+"','"+vo.getItemCodeITM01()+"','*','*',3422,3372,'Y')"
            );
      }
      if (!Boolean.TRUE.equals(vo.getUseVariant3ITM01())) {
        stmt.execute(
            "insert into ITM18_PRODUCT_VARIANTS_3(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13," +
            "VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) VALUES("+
            "'"+vo.getCompanyCodeSys01ITM01()+"','"+vo.getItemCodeITM01()+"','*','*',3432,3382,'Y')"
            );
      }
      if (!Boolean.TRUE.equals(vo.getUseVariant4ITM01())) {
        stmt.execute(
            "insert into ITM19_PRODUCT_VARIANTS_4(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14," +
            "VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) VALUES("+
            "'"+vo.getCompanyCodeSys01ITM01()+"','"+vo.getItemCodeITM01()+"','*','*',3442,3392,'Y')"
            );
      }
      if (!Boolean.TRUE.equals(vo.getUseVariant5ITM01())) {
        stmt.execute(
            "insert into ITM20_PRODUCT_VARIANTS_5(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15," +
            "VARIANT_PROGRESSIVE_SYS10,TYPE_VARIANT_PROGRESSIVE_SYS10,ENABLED) VALUES("+
            "'"+vo.getCompanyCodeSys01ITM01()+"','"+vo.getItemCodeITM01()+"','*','*',3452,3402,'Y')"
            );
      }


      if (!Boolean.TRUE.equals(vo.getUseVariant1ITM01()) &&
          !Boolean.TRUE.equals(vo.getUseVariant2ITM01()) &&
          !Boolean.TRUE.equals(vo.getUseVariant3ITM01()) &&
          !Boolean.TRUE.equals(vo.getUseVariant4ITM01()) &&
          !Boolean.TRUE.equals(vo.getUseVariant5ITM01())) {

        // retrieve the min stock for the item that does not have variants...
        String sql =
            "insert into ITM23_VARIANT_MIN_STOCKS("+
            "COMPANY_CODE_SYS01,ITEM_CODE_ITM01,  "+
            "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
            "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
            "MIN_STOCK) "+
            "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,vo.getCompanyCodeSys01());
        pstmt.setString(2,vo.getItemCodeITM01());
        pstmt.setString(3,ApplicationConsts.JOLLY);
        pstmt.setString(4,ApplicationConsts.JOLLY);
        pstmt.setString(5,ApplicationConsts.JOLLY);
        pstmt.setString(6,ApplicationConsts.JOLLY);
        pstmt.setString(7,ApplicationConsts.JOLLY);
        pstmt.setString(8,ApplicationConsts.JOLLY);
        pstmt.setString(9,ApplicationConsts.JOLLY);
        pstmt.setString(10,ApplicationConsts.JOLLY);
        pstmt.setString(11,ApplicationConsts.JOLLY);
        pstmt.setString(12,ApplicationConsts.JOLLY);

        pstmt.setBigDecimal(13,vo.getMinStockITM23());
        pstmt.execute();
        pstmt.close();
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
        stmt.close();
      }
      catch (Exception ex2) {
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
