package org.jallinone.items.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.CompanyTranslationUtils;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;


import javax.sql.DataSource;
import org.jallinone.items.java.BrandVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.items.java.ItemVariantImageVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.documents.server.FileUtils;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage item variant images.</p>
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
public class ItemVariantImagesBean implements ItemVariantImages {


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


  public ItemVariantImagesBean() {}



  /**
   * Business logic to execute.
   */
  public VOListResponse loadItemVariantImages(
			HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
			String serverLanguageId,
	    GridParams gridParams,String imagePath,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

      String sql =
				"SELECT "+
				"ITM33_VARIANT_IMAGES.COMPANY_CODE_SYS01,ITM33_VARIANT_IMAGES.ITEM_CODE_ITM01,ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM06,"+
				"ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM11,ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM07,ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM12,"+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM08,ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM13,ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM09,"+
				"ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM14,ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM10,ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM15,"+
				"ITM33_VARIANT_IMAGES.SMALL_IMAGE,ITM33_VARIANT_IMAGES.LARGE_IMAGE "+
        "from ITM33_VARIANT_IMAGES where "+
				"ITM33_VARIANT_IMAGES.COMPANY_CODE_SYS01=? and "+
				"ITM33_VARIANT_IMAGES.ITEM_CODE_ITM01=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM33","ITM33_VARIANT_IMAGES.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01ITM33","ITM33_VARIANT_IMAGES.ITEM_CODE_ITM01");
      attribute2dbField.put("variantTypeItm06ITM33","ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11ITM33","ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM11");
			attribute2dbField.put("variantTypeItm07ITM33","ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantCodeItm12ITM33","ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM12");
			attribute2dbField.put("variantTypeItm08ITM33","ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantCodeItm13ITM33","ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM13");
			attribute2dbField.put("variantTypeItm09ITM33","ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantCodeItm14ITM33","ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM14");
			attribute2dbField.put("variantTypeItm10ITM33","ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm15ITM33","ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM15");
			attribute2dbField.put("smallImageITM33","ITM33_VARIANT_IMAGES.SMALL_IMAGE");
			attribute2dbField.put("largeImageITM33","ITM33_VARIANT_IMAGES.LARGE_IMAGE");


      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01ITM01());
			values.add(pk.getItemCodeITM01());

      // read from ITM33 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ItemVariantImageVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError())
				throw new Exception(answer.getErrorMessage());
			else {
				VOListResponse res = (VOListResponse)answer;

				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}

				List rows = ((VOListResponse)res).getRows();
				String descr = null;
				ItemVariantImageVO vo = null;
				for(int i=0;i<rows.size();i++) {
					vo = (ItemVariantImageVO)rows.get(i);
					descr = "";

					// check supported variants for current item...
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11ITM33())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant1Descriptions,
							vo,
							vo.getVariantTypeItm06ITM33(),
							vo.getVariantCodeItm11ITM33(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12ITM33())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant2Descriptions,
							vo,
							vo.getVariantTypeItm07ITM33(),
							vo.getVariantCodeItm12ITM33(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13ITM33())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant3Descriptions,
							vo,
							vo.getVariantTypeItm08ITM33(),
							vo.getVariantCodeItm13ITM33(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14ITM33())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant4Descriptions,
							vo,
							vo.getVariantTypeItm09ITM33(),
							vo.getVariantCodeItm14ITM33(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15ITM33())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant5Descriptions,
							vo,
							vo.getVariantTypeItm10ITM33(),
							vo.getVariantCodeItm15ITM33(),
							serverLanguageId,
							username
						);
					}
					vo.setVariantsDescription(descr);

					if (vo.getSmallImageITM33()!=null) {
						// read image from file system...
						String path = appPath+"ITM01/SMALL_IMG/"+vo.getCompanyCodeSys01ITM33()+"/"+vo.getItemCodeItm01ITM33()+"/";
						String fileName = vo.getSmallImageITM33();
						 File f = new File(path+fileName);
						 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
						 byte[] aux = null;
						 byte[] img = new byte[0];
						 byte[] buf = new byte[10000];
						 int len = 0;
						 while((len=bis.read(buf))>0) {
							 aux = new byte[img.length+len];
							 System.arraycopy(img,0,aux,0,img.length);
							 System.arraycopy(buf,0,aux,img.length,len);
							 img = aux;
						 }
						 bis.close();
						 vo.setSmallImage(img);
					}


					if (vo.getLargeImageITM33()!=null) {
						// read image from file system...
						String path = appPath+"ITM01/LARGE_IMG/"+vo.getCompanyCodeSys01ITM33()+"/"+vo.getItemCodeItm01ITM33()+"/";
						String fileName = vo.getLargeImageITM33();
						 File f = new File(path+fileName);
						 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
						 byte[] aux = null;
						 byte[] img = new byte[0];
						 byte[] buf = new byte[10000];
						 int len = 0;
						 while((len=bis.read(buf))>0) {
							 aux = new byte[img.length+len];
							 System.arraycopy(img,0,aux,0,img.length);
							 System.arraycopy(buf,0,aux,img.length,len);
							 img = aux;
						 }
						 bis.close();
						 vo.setLargeImage(img);
					}

				} // end for on rows...

				return res;
			}
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching the list of images for item variants",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
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



	private String getVariantCodeAndTypeDesc(
			HashMap variantDescriptions,
			ItemVariantImageVO vo,
			String varType,
			String varCode,
			String serverLanguageId,
			String username
	) throws Throwable {
		String varDescr = (String)variantDescriptions.get(varType+"_"+varCode);
		if (varDescr==null)
			varDescr = ApplicationConsts.JOLLY.equals(varCode)?"":varCode;
		return varDescr;
	}



  /**
   * Business logic to execute.
   */
  public VOResponse updateItemVariantImage(ItemVariantImageVO oldVO,ItemVariantImageVO newVO,String imagePath,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      Response res = null;


			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01ITM33");
			pkAttrs.add("itemCodeItm01ITM33");
			pkAttrs.add("variantTypeItm06ITM33");
			pkAttrs.add("variantCodeItm11ITM33");
			pkAttrs.add("variantTypeItm07ITM33");
			pkAttrs.add("variantCodeItm12ITM33");
			pkAttrs.add("variantTypeItm08ITM33");
			pkAttrs.add("variantCodeItm13ITM33");
			pkAttrs.add("variantTypeItm09ITM33");
			pkAttrs.add("variantCodeItm14ITM33");
			pkAttrs.add("variantTypeItm10ITM33");
			pkAttrs.add("variantCodeItm15ITM33");

			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM33","COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM33","ITEM_CODE_ITM01");
			attribute2dbField.put("variantTypeItm06ITM33","VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantCodeItm11ITM33","VARIANT_CODE_ITM11");
			attribute2dbField.put("variantTypeItm07ITM33","VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantCodeItm12ITM33","VARIANT_CODE_ITM12");
			attribute2dbField.put("variantTypeItm08ITM33","VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantCodeItm13ITM33","VARIANT_CODE_ITM13");
			attribute2dbField.put("variantTypeItm09ITM33","VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantCodeItm14ITM33","VARIANT_CODE_ITM14");
			attribute2dbField.put("variantTypeItm10ITM33","VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm15ITM33","VARIANT_CODE_ITM15");

			String appPath = imagePath;
			appPath = appPath.replace('\\','/');
			if (!appPath.endsWith("/"))
				appPath += "/";
			if (!new File(appPath).isAbsolute()) {
				// relative path (to "WEB-INF/classes/" folder)
				appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
			}

			String path = appPath+"ITM01/SMALL_IMG/"+oldVO.getCompanyCodeSys01ITM33()+"/"+oldVO.getItemCodeItm01ITM33()+"/";
			if (oldVO.getSmallImage()!=null && newVO.getSmallImage()==null) {
				// remove image from file system...
				String fileName =
					 oldVO.getVariantTypeItm06ITM33()+"_"+oldVO.getVariantCodeItm11ITM33()+
					 oldVO.getVariantTypeItm07ITM33()+"_"+oldVO.getVariantCodeItm12ITM33()+
					 oldVO.getVariantTypeItm08ITM33()+"_"+oldVO.getVariantCodeItm13ITM33()+
					 oldVO.getVariantTypeItm09ITM33()+"_"+oldVO.getVariantCodeItm14ITM33()+
					 oldVO.getVariantTypeItm10ITM33()+"_"+oldVO.getVariantCodeItm15ITM33();
				 fileName = fileName.replace('*','_');
				 new File(path+fileName).delete();
			}
			else if (newVO.getSmallImage()!=null) {
				String fileName =
					newVO.getVariantTypeItm06ITM33()+"_"+newVO.getVariantCodeItm11ITM33()+
					newVO.getVariantTypeItm07ITM33()+"_"+newVO.getVariantCodeItm12ITM33()+
					newVO.getVariantTypeItm08ITM33()+"_"+newVO.getVariantCodeItm13ITM33()+
					newVO.getVariantTypeItm09ITM33()+"_"+newVO.getVariantCodeItm14ITM33()+
					newVO.getVariantTypeItm10ITM33()+"_"+newVO.getVariantCodeItm15ITM33();
				fileName = fileName.replace('*','_');

				newVO.setSmallImageITM33(fileName);
				attribute2dbField.put("smallImageITM33","SMALL_IMAGE");

				new File(path).mkdirs();
				FileOutputStream out = new FileOutputStream(path+newVO.getSmallImageITM33());
				out.write(newVO.getSmallImage());
				out.close();
			}




			path = appPath+"ITM01/LARGE_IMG/"+oldVO.getCompanyCodeSys01ITM33()+"/"+oldVO.getItemCodeItm01ITM33()+"/";
			if (oldVO.getLargeImage()!=null && newVO.getLargeImage()==null) {
				// remove image from file system...
				String fileName =
					oldVO.getVariantTypeItm06ITM33()+"_"+oldVO.getVariantCodeItm11ITM33()+
					oldVO.getVariantTypeItm07ITM33()+"_"+oldVO.getVariantCodeItm12ITM33()+
					oldVO.getVariantTypeItm08ITM33()+"_"+oldVO.getVariantCodeItm13ITM33()+
					oldVO.getVariantTypeItm09ITM33()+"_"+oldVO.getVariantCodeItm14ITM33()+
					oldVO.getVariantTypeItm10ITM33()+"_"+oldVO.getVariantCodeItm15ITM33();
				fileName = fileName.replace('*','_');
				 new File(path+fileName).delete();
			}
			else if (newVO.getLargeImage()!=null) {
				String fileName =
					newVO.getVariantTypeItm06ITM33()+"_"+newVO.getVariantCodeItm11ITM33()+
					newVO.getVariantTypeItm07ITM33()+"_"+newVO.getVariantCodeItm12ITM33()+
					newVO.getVariantTypeItm08ITM33()+"_"+newVO.getVariantCodeItm13ITM33()+
					newVO.getVariantTypeItm09ITM33()+"_"+newVO.getVariantCodeItm14ITM33()+
					newVO.getVariantTypeItm10ITM33()+"_"+newVO.getVariantCodeItm15ITM33();
				fileName = fileName.replace('*','_');

				newVO.setLargeImageITM33(fileName);
				attribute2dbField.put("largeImageITM33","LARGE_IMAGE");

				new File(path).mkdirs();
				FileOutputStream out = new FileOutputStream(path+newVO.getLargeImageITM33());
				out.write(newVO.getLargeImage());
				out.close();
			}





			res = org.jallinone.commons.server.QueryUtilExtension.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttrs,
          oldVO,
          newVO,
          "ITM33_VARIANT_IMAGES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing image for item variant",ex);
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
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }
  }


  /**
   * Business logic to execute.
   */
  public VOResponse insertItemVariantImage(ItemVariantImageVO vo,String imagePath,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM33","COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM33","ITEM_CODE_ITM01");
			attribute2dbField.put("variantTypeItm06ITM33","VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantCodeItm11ITM33","VARIANT_CODE_ITM11");
			attribute2dbField.put("variantTypeItm07ITM33","VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantCodeItm12ITM33","VARIANT_CODE_ITM12");
			attribute2dbField.put("variantTypeItm08ITM33","VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantCodeItm13ITM33","VARIANT_CODE_ITM13");
			attribute2dbField.put("variantTypeItm09ITM33","VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantCodeItm14ITM33","VARIANT_CODE_ITM14");
			attribute2dbField.put("variantTypeItm10ITM33","VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm15ITM33","VARIANT_CODE_ITM15");

      Response res = null;
			if (vo.getSmallImage()!=null) {
				// save image on file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}

       String path = appPath+"ITM01/SMALL_IMG/"+vo.getCompanyCodeSys01ITM33()+"/"+vo.getItemCodeItm01ITM33()+"/";
				String fileName =
					vo.getVariantTypeItm06ITM33()+"_"+vo.getVariantCodeItm11ITM33()+
					vo.getVariantTypeItm07ITM33()+"_"+vo.getVariantCodeItm12ITM33()+
					vo.getVariantTypeItm08ITM33()+"_"+vo.getVariantCodeItm13ITM33()+
					vo.getVariantTypeItm09ITM33()+"_"+vo.getVariantCodeItm14ITM33()+
					vo.getVariantTypeItm10ITM33()+"_"+vo.getVariantCodeItm15ITM33();
				fileName = fileName.replace('*','_');

				vo.setSmallImageITM33(fileName);
				attribute2dbField.put("smallImageITM33","SMALL_IMAGE");

				new File(path).mkdirs();
				FileOutputStream out = new FileOutputStream(path+vo.getSmallImageITM33());
				out.write(vo.getSmallImage());
				out.close();
			}

			if (vo.getLargeImage()!=null) {
				// save image on file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}

			 String path = appPath+"ITM01/LARGE_IMG/"+vo.getCompanyCodeSys01ITM33()+"/"+vo.getItemCodeItm01ITM33()+"/";
				String fileName =
					vo.getVariantTypeItm06ITM33()+"_"+vo.getVariantCodeItm11ITM33()+
					vo.getVariantTypeItm07ITM33()+"_"+vo.getVariantCodeItm12ITM33()+
					vo.getVariantTypeItm08ITM33()+"_"+vo.getVariantCodeItm13ITM33()+
					vo.getVariantTypeItm09ITM33()+"_"+vo.getVariantCodeItm14ITM33()+
					vo.getVariantTypeItm10ITM33()+"_"+vo.getVariantCodeItm15ITM33();
				fileName = fileName.replace('*','_');

				vo.setLargeImageITM33(fileName);
				attribute2dbField.put("largeImageITM33","LARGE_IMAGE");

				new File(path).mkdirs();
				FileOutputStream out = new FileOutputStream(path+vo.getLargeImageITM33());
				out.write(vo.getLargeImage());
				out.close();
			}


      // insert into ITM33...
      res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "ITM33_VARIANT_IMAGES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError()) {
				throw new Exception(res.getErrorMessage());
      }


      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new ItemVariantImage", ex);
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
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




  /**
   * Business logic to execute.
   */
  public VOResponse deleteItemVariantImage(ItemVariantImageVO vo,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement(
				"delete from ITM33_VARIANT_IMAGES where "+
				"ITM33_VARIANT_IMAGES.COMPANY_CODE_SYS01=? and ITM33_VARIANT_IMAGES.ITEM_CODE_ITM01=? and "+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM06=? and ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM11=? and "+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM07=? and ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM12=? and "+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM08=? and ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM13=? and "+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM09=? and ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM14=? and "+
				"ITM33_VARIANT_IMAGES.VARIANT_TYPE_ITM10=? and ITM33_VARIANT_IMAGES.VARIANT_CODE_ITM15=?"
			);

      // phisically delete the record in ITM33...
			pstmt.setString(1,vo.getCompanyCodeSys01ITM33());
			pstmt.setString(2,vo.getItemCodeItm01ITM33());
			pstmt.setString(3,vo.getVariantTypeItm06ITM33());
			pstmt.setString(4,vo.getVariantCodeItm11ITM33());
			pstmt.setString(5,vo.getVariantTypeItm07ITM33());
			pstmt.setString(6,vo.getVariantCodeItm12ITM33());
			pstmt.setString(7,vo.getVariantTypeItm08ITM33());
			pstmt.setString(8,vo.getVariantCodeItm13ITM33());
			pstmt.setString(9,vo.getVariantTypeItm09ITM33());
			pstmt.setString(10,vo.getVariantCodeItm14ITM33());
			pstmt.setString(11,vo.getVariantTypeItm10ITM33());
			pstmt.setString(12,vo.getVariantCodeItm15ITM33());
			pstmt.execute();
			pstmt.close();

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing image for variants",ex);
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
    }

  }

}

