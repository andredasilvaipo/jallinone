package org.jallinone.warehouse.tables.movements.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.warehouse.tables.movements.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.java.WarehousePK;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch warehouse movements from WAR02 table.</p>
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
public class LoadMovementsBean  implements LoadMovements {


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




  public LoadMovementsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public MovementVO getMovement() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadWarehouseMovements(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		GridParams gridParams, String serverLanguageId, String username) throws	Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


			WarehousePK pk = (WarehousePK)gridParams.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE);

      String sql =
          "select WAR04_WAREHOUSE_MOTIVES.QTY_SIGN,WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE,WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE,WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01,"+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01,WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01,WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01,"+
          "SYS10_LOC.DESCRIPTION,WAR02_WAREHOUSE_MOVEMENTS.MOVEMENT_DATE,WAR02_WAREHOUSE_MOVEMENTS.USERNAME,WAR02_WAREHOUSE_MOVEMENTS.NOTE,"+
          "WAR02_WAREHOUSE_MOVEMENTS.DELTA_QTY,WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04,SYS10_WAR04.DESCRIPTION,SYS10_ITM01.DESCRIPTION,"+
          "WAR01_WAREHOUSES.DESCRIPTION,"+
          "WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM06,WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM11,"+
          "WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM07,WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM12,"+
          "WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM08,WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM13,"+
          "WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM09,WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM14,"+
          "WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM10,WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM15 "+
          " from "+
          "WAR04_WAREHOUSE_MOTIVES,WAR02_WAREHOUSE_MOVEMENTS,SYS10_TRANSLATIONS SYS10_LOC,SYS10_TRANSLATIONS SYS10_WAR04,"+
          "SYS10_TRANSLATIONS SYS10_ITM01,WAR01_WAREHOUSES,ITM01_ITEMS where "+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04=WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE and "+
          "WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10=SYS10_WAR04.PROGRESSIVE and "+
          "SYS10_WAR04.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
          "SYS10_LOC.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
					"WAR01_WAREHOUSES.COMPANY_CODE_SYS01=? and " +
					"WAR01_WAREHOUSES.WAREHOUSE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("qtySignWAR04","WAR04_WAREHOUSE_MOTIVES.QTY_SIGN");
      attribute2dbField.put("itemTypeWAR04","WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE");
      attribute2dbField.put("progressiveWAR02","WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01WAR02","WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWar01WAR02","WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("itemCodeItm01WAR02","WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie01WAR02","WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
      attribute2dbField.put("movementDateWAR02","WAR02_WAREHOUSE_MOVEMENTS.MOVEMENT_DATE");
      attribute2dbField.put("usernameWAR02","WAR02_WAREHOUSE_MOVEMENTS.USERNAME");
      attribute2dbField.put("noteWAR02","WAR02_WAREHOUSE_MOVEMENTS.NOTE");
      attribute2dbField.put("deltaQtyWAR02","WAR02_WAREHOUSE_MOVEMENTS.DELTA_QTY");
      attribute2dbField.put("warehouseMotiveWar04WAR02","WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04");
      attribute2dbField.put("motiveDescriptionSYS10","SYS10_WAR04.DESCRIPTION");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_ITM01.DESCRIPTION");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("variantTypeItm06WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR02","WAR02_WAREHOUSE_MOVEMENTS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
			values.add(pk.getCompanyCodeSys01WAR01());
			values.add(pk.getWarehouseCodeWAR01());


      // read from WAR02 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MovementVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );


      if (answer.isError())
				throw new Exception(answer.getErrorMessage());


			List rows = ((VOListResponse)answer).getRows();
			String descr = null;
			MovementVO vo = null;
			for(int i=0;i<rows.size();i++) {
				vo = (MovementVO)rows.get(i);
				descr = vo.getItemDescriptionSYS10();

				// check supported variants for current item...
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11WAR02())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant1Descriptions,
						vo,
						vo.getVariantTypeItm06WAR02(),
						vo.getVariantCodeItm11WAR02(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12WAR02())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant2Descriptions,
						vo,
						vo.getVariantTypeItm07WAR02(),
						vo.getVariantCodeItm12WAR02(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13WAR02())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant3Descriptions,
						vo,
						vo.getVariantTypeItm08WAR02(),
						vo.getVariantCodeItm13WAR02(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14WAR02())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant4Descriptions,
						vo,
						vo.getVariantTypeItm09WAR02(),
						vo.getVariantCodeItm14WAR02(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15WAR02())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant5Descriptions,
						vo,
						vo.getVariantTypeItm10WAR02(),
						vo.getVariantCodeItm15WAR02(),
						serverLanguageId,
						username
					);
				}
				vo.setItemDescriptionSYS10(descr);

			} // end for on rows...


	    return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching warehouse movements list",ex);
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
				MovementVO vo,
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

