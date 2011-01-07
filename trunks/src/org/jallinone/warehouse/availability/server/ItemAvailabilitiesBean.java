package org.jallinone.warehouse.availability.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.availability.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.*;

import java.math.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage item availabilities and booked items, eventually for a specific warehouse.</p>
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
public class ItemAvailabilitiesBean implements ItemAvailabilities {


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




  public ItemAvailabilitiesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public BookedItemQtyVO getBookedItemQty(ItemPK pk) {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public ItemAvailabilityVO getItemAvailabilityVO(ItemPK pk) {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public OrderedItemQtyVO getOrderedItemQty(ItemPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadBookedItems(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      GridParams gridPars, String serverLanguageId, String username,
      ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }


      String sql =
          "select WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,sum(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY),WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 "+
          " from WAR03_ITEMS_AVAILABILITY,ITM01_ITEMS,SYS10_TRANSLATIONS,WAR01_WAREHOUSES "+
          " where "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("availableQtyWAR03","sum(WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY)");
      attribute2dbField.put("itemCodeItm01DOC02","WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("companyCodeSys01WAR03","WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWar01WAR03","WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("variantTypeItm06DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC02","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15");

      ArrayList pars = new ArrayList();
      pars.add( serverLanguageId );

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? ";
        pars.add( gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK)!=null) {
        ItemPK pk = (ItemPK)gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
        sql += " and WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? "+
               " and WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=? ";

        pars.add( pk.getCompanyCodeSys01ITM01() );
        pars.add( pk.getItemCodeITM01() );
      }

      sql += " group by WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
             "WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
             "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 ";

      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          pars,
          attribute2dbField,
          BookedItemQtyVO.class,
          "Y",
          "N",
          null,
          gridPars,
          50,
          true
      );

      if (res.isError())
        throw new Exception(res.getErrorMessage());

      // retrieve booked item quantities, for each item found...
      sql =
          "select sum(DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY) "+
          " from DOC02_SELLING_ITEMS,DOC01_SELLING where "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER and "+
          "DOC01_SELLING.DOC_STATE=? ";

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC01_SELLING.WAREHOUSE_CODE_WAR01=? ";
      }

      sql += " group by DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.ITEM_CODE_ITM01 ";

      BookedItemQtyVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      pstmt = conn.prepareStatement(sql);
      ResultSet rset = null;
      for(int i=0;i<list.size();i++) {
        vo = (BookedItemQtyVO)list.get(i);

        String descr = vo.getItemDescriptionSYS10();

        // check supported variants for current item...
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC02())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant1Descriptions,
            vo,
            vo.getVariantTypeItm06DOC02(),
            vo.getVariantCodeItm11DOC02(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC02())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant2Descriptions,
            vo,
            vo.getVariantTypeItm07DOC02(),
            vo.getVariantCodeItm12DOC02(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC02())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant3Descriptions,
            vo,
            vo.getVariantTypeItm08DOC02(),
            vo.getVariantCodeItm13DOC02(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC02())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant4Descriptions,
            vo,
            vo.getVariantTypeItm09DOC02(),
            vo.getVariantCodeItm14DOC02(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC02())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant5Descriptions,
            vo,
            vo.getVariantTypeItm10DOC02(),
            vo.getVariantCodeItm15DOC02(),
            serverLanguageId,
            username
          );
        }
        vo.setItemDescriptionSYS10(descr);



        pstmt.setString(1,vo.getCompanyCodeSys01WAR03());
        pstmt.setString(2,vo.getItemCodeItm01DOC02());
        pstmt.setString(3,ApplicationConsts.CONFIRMED);

        if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
          pstmt.setString(4,gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE).toString());
        }

        rset = pstmt.executeQuery();
        if (rset.next())
          vo.setBookQtyDOC02(rset.getBigDecimal(1));
        else
          vo.setBookQtyDOC02(new BigDecimal(0));
        rset.close();
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item availabilities and book items",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
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
  public VOListResponse loadItemAvailabilities(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      GridParams gridPars, String serverLanguageId, String username,
      ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01,WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01,SYS10_LOC.DESCRIPTION,"+
          "SYS10_ITM01.DESCRIPTION,WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY,WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY,WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01, "+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14,"+
          "WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10,WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 "+
          "from "+
          "WAR03_ITEMS_AVAILABILITY,SYS10_TRANSLATIONS SYS10_ITM01,SYS10_TRANSLATIONS SYS10_LOC,ITM01_ITEMS where "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? and "+
          "WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
          "SYS10_LOC.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR03","WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01WAR03","WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01");
      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
      attribute2dbField.put("descriptionSYS10","SYS10_ITM01.DESCRIPTION");
      attribute2dbField.put("availableQtyWAR03","WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY");
      attribute2dbField.put("damagedQtyWAR03","WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY");
      attribute2dbField.put("progressiveHie01WAR03","WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01");

      attribute2dbField.put("variantTypeItm06WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR03","WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15");


      ArrayList pars = new ArrayList();
      pars.add( serverLanguageId );
      pars.add( serverLanguageId );

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? ";

        pars.add( gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE) );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK)!=null) {
        ItemPK pk = (ItemPK)gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
        sql += " and WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? "+
               " and WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01=? ";

        pars.add( pk.getCompanyCodeSys01ITM01() );
        pars.add( pk.getItemCodeITM01() );
      }

      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01)!=null &&
          gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02)!=null) {

        // retrieve all subnodes of the specified node...
        pstmt = conn.prepareStatement(
            "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
            "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
            "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
        );

        BigDecimal progressiveHIE01 = (BigDecimal)gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
        BigDecimal progressiveHIE02 = (BigDecimal)gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

        pstmt.setBigDecimal(1,progressiveHIE02);
        pstmt.setBigDecimal(2,progressiveHIE01);
        ResultSet rset = pstmt.executeQuery();

        HashSet currentLevelNodes = new HashSet();
        HashSet newLevelNodes = new HashSet();
        String nodes = "";
        int currentLevel = -1;
        while(rset.next()) {
          if (currentLevel!=rset.getInt(3)) {
            // next level...
            currentLevel = rset.getInt(3);
            currentLevelNodes = newLevelNodes;
            newLevelNodes = new HashSet();
          }
          if (rset.getBigDecimal(1).equals(progressiveHIE01)) {
            newLevelNodes.add(rset.getBigDecimal(1));
            nodes += rset.getBigDecimal(1)+",";
          }
          else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
            newLevelNodes.add(rset.getBigDecimal(1));
            nodes += rset.getBigDecimal(1)+",";
          }
        }
        rset.close();
        pstmt.close();
        if (nodes.length()>0)
          nodes = nodes.substring(0,nodes.length()-1);

        sql += " and WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01 in ("+nodes+")";

      }

      Response answer = null;
      if (gridPars.getOtherGridParams().get(ApplicationConsts.LOAD_ALL)!=null &&
          ((Boolean)gridPars.getOtherGridParams().get(ApplicationConsts.LOAD_ALL)).booleanValue())
        answer = QueryUtil.getQuery(
            conn,
            new UserSessionParameters(username),
            sql,
            pars,
            attribute2dbField,
            ItemAvailabilityVO.class,
            "Y",
            "N",
            null,
            gridPars,
            true
        );
      else
        answer = QueryUtil.getQuery(
            conn,
            new UserSessionParameters(username),
            sql,
            pars,
            attribute2dbField,
            ItemAvailabilityVO.class,
            "Y",
            "N",
            null,
            gridPars,
            50,
            true
        );

      if (answer.isError())
        throw new Exception(answer.getErrorMessage());

      java.util.List rows = ((VOListResponse)answer).getRows();
      ItemAvailabilityVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (ItemAvailabilityVO)rows.get(i);
        String descr = vo.getDescriptionSYS10();

        // check supported variants for current item...
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11WAR03())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant1Descriptions,
            vo,
            vo.getVariantTypeItm06WAR03(),
            vo.getVariantCodeItm11WAR03(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12WAR03())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant2Descriptions,
            vo,
            vo.getVariantTypeItm07WAR03(),
            vo.getVariantCodeItm12WAR03(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13WAR03())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant3Descriptions,
            vo,
            vo.getVariantTypeItm08WAR03(),
            vo.getVariantCodeItm13WAR03(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14WAR03())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant4Descriptions,
            vo,
            vo.getVariantTypeItm09WAR03(),
            vo.getVariantCodeItm14WAR03(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15WAR03())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant5Descriptions,
            vo,
            vo.getVariantTypeItm10WAR03(),
            vo.getVariantCodeItm15WAR03(),
            serverLanguageId,
            username
          );
        }
        vo.setDescriptionSYS10(descr);

      }



     return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item availabilities",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
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
  public VOListResponse loadOrderedItems(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      GridParams gridPars, String serverLanguageId, String username) throws
      Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ItemPK pk = (ItemPK)gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
      if (pk==null)
        return new VOListResponse(new ArrayList(),false,0);

      String sql =
          "select DOC07_PURCHASE_ITEMS.DELIVERY_DATE,DOC07_PURCHASE_ITEMS.ORDER_QTY,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,DOC07_PURCHASE_ITEMS.DOC_YEAR,DOC06_PURCHASE.DOC_SEQUENCE,"+
          "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15, "+
          "SYS10_ITM01.DESCRIPTION "+
          "from DOC07_PURCHASE_ITEMS,DOC06_PURCHASE,ITM01_ITEMS,WAR01_WAREHOUSES,SYS10_TRANSLATIONS SYS10_ITM01 "+
          "where "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER and "+
          "DOC06_PURCHASE.DOC_STATE=? and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? and "+
          "DOC07_PURCHASE_ITEMS.ORDER_QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("deliveryDateDOC07","DOC07_PURCHASE_ITEMS.DELIVERY_DATE");
      attribute2dbField.put("orderQtyDOC07","DOC07_PURCHASE_ITEMS.ORDER_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("docYearDOC06","DOC07_PURCHASE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docSequenceDOC06","DOC06_PURCHASE.DOC_SEQUENCE");
      attribute2dbField.put("warehouseCodeWar01DOC06","DOC06_PURCHASE.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("variantTypeItm06DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15");
      attribute2dbField.put("descriptionSYS10","SYS10_ITM01.DESCRIPTION");

      ArrayList pars = new ArrayList();
      pars.add( ApplicationConsts.CONFIRMED );
      pars.add( pk.getCompanyCodeSys01ITM01() );
      pars.add( pk.getItemCodeITM01() );
      pars.add( serverLanguageId );

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC06_PURCHASE.WAREHOUSE_CODE_WAR01=? ";
        pars.add( gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE) );
      }

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          pars,
          attribute2dbField,
          OrderedItemQtyVO.class,
          "Y",
          "N",
          null,
          gridPars,
          50,
          true
      );

      if (answer.isError())
        throw new Exception(answer.getErrorMessage());

      java.util.List rows = ((VOListResponse)answer).getRows();
      OrderedItemQtyVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (OrderedItemQtyVO)rows.get(i);

        String descr = vo.getDescriptionSYS10();

        // check supported variants for current item...
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC07())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant1Descriptions,
            vo,
            vo.getVariantTypeItm06DOC07(),
            vo.getVariantCodeItm11DOC07(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC07())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant2Descriptions,
            vo,
            vo.getVariantTypeItm07DOC07(),
            vo.getVariantCodeItm12DOC07(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC07())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant3Descriptions,
            vo,
            vo.getVariantTypeItm08DOC07(),
            vo.getVariantCodeItm13DOC07(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC07())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant4Descriptions,
            vo,
            vo.getVariantTypeItm09DOC07(),
            vo.getVariantCodeItm14DOC07(),
            serverLanguageId,
            username
          );
        }
        if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC07())) {
          descr += " "+getVariantCodeAndTypeDesc(
            variant5Descriptions,
            vo,
            vo.getVariantTypeItm10DOC07(),
            vo.getVariantCodeItm15DOC07(),
            serverLanguageId,
            username
          );
        }
        vo.setDescriptionSYS10(descr);
      }

      return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching future item availabilities",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
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
      BookedItemQtyVO vo,
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


  private String getVariantCodeAndTypeDesc(
      HashMap variantDescriptions,
      ItemAvailabilityVO vo,
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


  private String getVariantCodeAndTypeDesc(
      HashMap variantDescriptions,
      OrderedItemQtyVO vo,
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




}

