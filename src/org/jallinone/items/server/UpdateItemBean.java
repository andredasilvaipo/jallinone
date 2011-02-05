package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import java.math.*;

import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;
import org.jallinone.documents.server.FileUtils;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update an existing item.</p>
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
public class UpdateItemBean  implements UpdateItem {


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




  public UpdateItemBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse updateItem(DetailItemVO oldVO,DetailItemVO newVO,String t1,String serverLanguageId,String username,String imagePath,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      if (newVO.getBarCodeITM01()==null) {
        new BarCodeGeneratorImpl().calculateBarCode(conn,newVO);
      }

      // check for barcode uniqueness...
      if (newVO.getBarCodeITM01()!=null && !newVO.getBarCodeITM01().trim().equals("")) {
        stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from ITM01_ITEMS where COMPANY_CODE_SYS01='"+newVO.getCompanyCodeSys01()+"' and BAR_CODE='"+newVO.getBarCodeITM01()+"' and NOT ITEM_CODE='"+newVO.getItemCodeItm01()+"'");
        boolean barCodeFound = false;
        if (rset.next()) {
          barCodeFound = true;
        }
        rset.close();
        stmt.close();
        if (barCodeFound) {
          return new VOResponse(t1);
        }
      }

      // update item description...
      TranslationUtils.updateTranslation(
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveSys10ITM01(),
          serverLanguageId,
          conn
      );

      if (newVO.getAddDescriptionSYS10()!=null &&
          (newVO.getAddDescriptionSYS10().trim().length()>0 && oldVO.getAddDescriptionSYS10()==null ||
           newVO.getAddDescriptionSYS10().trim().length()>0 && oldVO.getAddDescriptionSYS10()!=null && oldVO.getAddDescriptionSYS10().trim().length()==0 ||
           oldVO.getAddDescriptionSYS10()!=null && oldVO.getAddDescriptionSYS10().trim().length()>0 && newVO.getAddDescriptionSYS10()==null ||
           oldVO.getAddDescriptionSYS10()!=null && oldVO.getAddDescriptionSYS10().trim().length()>0 && newVO.getAddDescriptionSYS10()!=null && newVO.getAddDescriptionSYS10().trim().length()==0 ||
           oldVO.getAddDescriptionSYS10()!=null && oldVO.getAddDescriptionSYS10().trim().length()>0 && oldVO.getAddDescriptionSYS10()!=null && !oldVO.getAddDescriptionSYS10().equals(newVO.getAddDescriptionSYS10()))) {
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
          BigDecimal addProgressiveSYS10 = TranslationUtils.insertTranslations(newVO.getAddDescriptionSYS10(),newVO.getCompanyCodeSys01ITM01(),conn);
          newVO.setAddProgressiveSys10ITM01(addProgressiveSYS10);
        }
      }


      if (oldVO.getSmallImage()!=null && newVO.getSmallImage()==null) {
        // remove image from file system...
        String appPath = imagePath;
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
        }
        new File(appPath+oldVO.getSmallImageITM01()).delete();
      }
      else if (newVO.getSmallImage()!=null) {
				// save image on file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}
				new File(appPath).mkdirs();

        if (oldVO.getSmallImage()==null) {
					String relativePath = FileUtils.getFilePath(appPath,"ITM01");
          BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(newVO.getCompanyCodeSys01(),"ITM01_ITEMS","SMALL_IMG",conn);
          newVO.setSmallImageITM01(relativePath+"SMALL_IMG"+imageProgressive);
					new File(appPath+relativePath).mkdirs();
        }
        else
          newVO.setSmallImageITM01(oldVO.getSmallImageITM01());

        File f = new File(appPath+newVO.getSmallImageITM01());
        f.delete();
        FileOutputStream out = new FileOutputStream(f);
        out.write(newVO.getSmallImage());
        out.close();
      }


      if (oldVO.getLargeImage()!=null && newVO.getLargeImage()==null) {
        // remove image from file system...
        String appPath = imagePath;
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
        }
        new File(appPath+oldVO.getLargeImageITM01()).delete();
      }
      else if (newVO.getLargeImage()!=null) {
				// save image on file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}
				new File(appPath).mkdirs();

        if (oldVO.getLargeImage()==null) {
					String relativePath = FileUtils.getFilePath(appPath,"ITM01");
          BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(newVO.getCompanyCodeSys01(),"ITM01_ITEMS","LARGE_IMG",conn);
          newVO.setLargeImageITM01(relativePath+"LARGE_IMG"+imageProgressive);
					new File(appPath+relativePath).mkdirs();
        }
        else
          newVO.setLargeImageITM01(oldVO.getLargeImageITM01());

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
      attribute2dbField.put("largeImageITM01","LARGE_IMAGE");
      attribute2dbField.put("smallImageITM01","SMALL_IMAGE");
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

			attribute2dbField.put("noWarehouseMovITM01","NO_WAREHOUSE_MOV");
			attribute2dbField.put("sheetCodeItm25ITM01","SHEET_CODE_ITM25");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01ITM01");
      pkAttributes.add("itemCodeITM01");

      // update ITM01 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "ITM01_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
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


      if (!Boolean.TRUE.equals(newVO.getUseVariant1ITM01()) &&
          !Boolean.TRUE.equals(newVO.getUseVariant1ITM01()) &&
          !Boolean.TRUE.equals(newVO.getUseVariant1ITM01()) &&
          !Boolean.TRUE.equals(newVO.getUseVariant1ITM01()) &&
          !Boolean.TRUE.equals(newVO.getUseVariant1ITM01())) {

        // retrieve the min stock for the item that does not have variants...
        String sql =
            "update ITM23_VARIANT_MIN_STOCKS set MIN_STOCK=? where "+
            "COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and   "+
            "VARIANT_TYPE_ITM06=? and VARIANT_TYPE_ITM07=? and VARIANT_TYPE_ITM08=? and VARIANT_TYPE_ITM09=? and VARIANT_TYPE_ITM10=? and "+
            "VARIANT_CODE_ITM11=? and VARIANT_CODE_ITM12=? and VARIANT_CODE_ITM13=? and VARIANT_CODE_ITM14=? and VARIANT_CODE_ITM15=? ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setBigDecimal(1,newVO.getMinStockITM23());
        pstmt.setString(2,newVO.getCompanyCodeSys01());
        pstmt.setString(3,newVO.getItemCodeITM01());
        pstmt.setString(4,ApplicationConsts.JOLLY);
        pstmt.setString(5,ApplicationConsts.JOLLY);
        pstmt.setString(6,ApplicationConsts.JOLLY);
        pstmt.setString(7,ApplicationConsts.JOLLY);
        pstmt.setString(8,ApplicationConsts.JOLLY);
        pstmt.setString(9,ApplicationConsts.JOLLY);
        pstmt.setString(10,ApplicationConsts.JOLLY);
        pstmt.setString(11,ApplicationConsts.JOLLY);
        pstmt.setString(12,ApplicationConsts.JOLLY);
        pstmt.setString(13,ApplicationConsts.JOLLY);
        pstmt.execute();
        pstmt.close();
      }


      Response answer = res;




      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing item",ex);
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

