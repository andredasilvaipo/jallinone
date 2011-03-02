package org.jallinone.warehouse.movements.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;

import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.warehouse.movements.java.*;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to manage warehouse movements in WAR02 and consequently in WAR03.</p>
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
public class ManualMovementsBean implements ManualMovements {


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

  private AddMovementBean bean;

  public void setBean(AddMovementBean bean) {
	  this.bean = bean;
  }



  public ManualMovementsBean() {
  }


  /**
   * Business logic to execute.
   */
  public VOResponse insertManualMovement(MovementVO vo,String t1,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

			if (vo.getVariantCodeItm11WAR02()==null)
				vo.setVariantCodeItm11WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm12WAR02()==null)
				vo.setVariantCodeItm12WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm13WAR02()==null)
				vo.setVariantCodeItm13WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm14WAR02()==null)
				vo.setVariantCodeItm14WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm15WAR02()==null)
				vo.setVariantCodeItm15WAR02(ApplicationConsts.JOLLY);

			if (vo.getVariantTypeItm06WAR02()==null)
				vo.setVariantTypeItm06WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm07WAR02()==null)
				vo.setVariantTypeItm07WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm08WAR02()==null)
				vo.setVariantTypeItm08WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm09WAR02()==null)
				vo.setVariantTypeItm09WAR02(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm10WAR02()==null)
				vo.setVariantTypeItm10WAR02(ApplicationConsts.JOLLY);

      WarehouseMovementVO movVO = new WarehouseMovementVO(
          vo.getProgressiveHie01WAR02(),
          vo.getDeltaQtyWAR02(),
          vo.getCompanyCodeSys01WAR02(),
          vo.getWarehouseCodeWar01WAR02(),
          vo.getItemCodeItm01WAR02(),
          vo.getWarehouseMotiveWar04WAR02(),
          vo.getItemTypeWAR04(),
          vo.getNoteWAR02(),
          vo.getSerialNumbers(),

          vo.getVariantCodeItm11WAR02(),
          vo.getVariantCodeItm12WAR02(),
          vo.getVariantCodeItm13WAR02(),
          vo.getVariantCodeItm14WAR02(),
          vo.getVariantCodeItm15WAR02(),
          vo.getVariantTypeItm06WAR02(),
          vo.getVariantTypeItm07WAR02(),
          vo.getVariantTypeItm08WAR02(),
          vo.getVariantTypeItm09WAR02(),
          vo.getVariantTypeItm10WAR02()

      );

      Response res = bean.addWarehouseMovement(movVO,t1,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new manual warehouse movement",ex);
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
    	  bean.setConn(null);
      } catch (Exception e) {
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




  /**
   * Business logic to execute.
   */
  public VOResponse insertManualMovements(MovementVO voTemplate,VariantsMatrixVO matrixVO,Object[][] cells,String t1,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      MovementVO vo = null;
      Response res = null;
      int pos = 0;
      ArrayList sn = new ArrayList();

      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];


        if (matrixVO.getColumnDescriptors().length==0) {

          if (cells[i][0]!=null) {
            vo = (MovementVO)voTemplate.clone();

            if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
              // e.g. color but not no size...
              vo.setVariantCodeItm11WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm06WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
              vo.setVariantCodeItm12WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm07WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm12WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm07WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
              vo.setVariantCodeItm13WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm08WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm13WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm08WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
              vo.setVariantCodeItm14WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm09WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm14WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm09WAR02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
              vo.setVariantCodeItm15WAR02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm10WAR02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm15WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm10WAR02(rowVO.getVariantTypeITM06());
            }


            //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());
/*
            vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());

            vo.setVariantCodeItm12WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm13WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm14WAR02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm15WAR02(ApplicationConsts.JOLLY);

            vo.setVariantTypeItm07WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm08WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm09WAR02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm10WAR02(ApplicationConsts.JOLLY);
*/
            try {
				vo.setDeltaQtyWAR02((BigDecimal)cells[i][0]);
			} catch (Exception e) {
				continue;
			}
            sn.clear();
            if (vo.getDeltaQtyWAR02().intValue()>0) {
              sn.addAll(voTemplate.getSerialNumbers().subList(pos,pos+vo.getDeltaQtyWAR02().intValue()));
            }
            vo.setSerialNumbers(sn);
            pos += vo.getDeltaQtyWAR02().intValue();
          }

          WarehouseMovementVO movVO = new WarehouseMovementVO(
              vo.getProgressiveHie01WAR02(),
              vo.getDeltaQtyWAR02(),
              vo.getCompanyCodeSys01WAR02(),
              vo.getWarehouseCodeWar01WAR02(),
              vo.getItemCodeItm01WAR02(),
              vo.getWarehouseMotiveWar04WAR02(),
              vo.getItemTypeWAR04(),
              vo.getNoteWAR02(),
              vo.getSerialNumbers(),

              vo.getVariantCodeItm11WAR02(),
              vo.getVariantCodeItm12WAR02(),
              vo.getVariantCodeItm13WAR02(),
              vo.getVariantCodeItm14WAR02(),
              vo.getVariantCodeItm15WAR02(),
              vo.getVariantTypeItm06WAR02(),
              vo.getVariantTypeItm07WAR02(),
              vo.getVariantTypeItm08WAR02(),
              vo.getVariantTypeItm09WAR02(),
              vo.getVariantTypeItm10WAR02()

          );

          res = bean.addWarehouseMovement(movVO,t1,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().length;k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[k];
            if (cells[i][k]!=null) {
              vo = (MovementVO)voTemplate.clone();

              try {
            	  vo.setDeltaQtyWAR02((BigDecimal)cells[i][k]);
              } catch (Exception e) {
            	  continue;
              }
              sn.clear();
              if (vo.getDeltaQtyWAR02().intValue()>0) {
                sn.addAll(voTemplate.getSerialNumbers().subList(pos,pos+vo.getDeltaQtyWAR02().intValue()));
              }
              vo.setSerialNumbers(sn);
              pos += vo.getDeltaQtyWAR02().intValue();

              //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

              vo.setVariantCodeItm11WAR02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06WAR02(rowVO.getVariantTypeITM06());

              vo.setVariantCodeItm12WAR02(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13WAR02(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14WAR02(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15WAR02(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              vo.setVariantTypeItm07WAR02(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08WAR02(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09WAR02(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10WAR02(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              WarehouseMovementVO movVO = new WarehouseMovementVO(
                  vo.getProgressiveHie01WAR02(),
                  vo.getDeltaQtyWAR02(),
                  vo.getCompanyCodeSys01WAR02(),
                  vo.getWarehouseCodeWar01WAR02(),
                  vo.getItemCodeItm01WAR02(),
                  vo.getWarehouseMotiveWar04WAR02(),
                  vo.getItemTypeWAR04(),
                  vo.getNoteWAR02(),
                  vo.getSerialNumbers(),

                  vo.getVariantCodeItm11WAR02(),
                  vo.getVariantCodeItm12WAR02(),
                  vo.getVariantCodeItm13WAR02(),
                  vo.getVariantCodeItm14WAR02(),
                  vo.getVariantCodeItm15WAR02(),
                  vo.getVariantTypeItm06WAR02(),
                  vo.getVariantTypeItm07WAR02(),
                  vo.getVariantTypeItm08WAR02(),
                  vo.getVariantTypeItm09WAR02(),
                  vo.getVariantTypeItm10WAR02()

              );

              res = bean.addWarehouseMovement(movVO,t1,serverLanguageId,username);
              if (res.isError())
                throw new Exception(res.getErrorMessage());


            } // end if not null
          } // end inner for
      } // end outer for

      return new VOResponse(voTemplate);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new manual warehouse movement",ex);
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
    	  bean.setConn(null);
      } catch (Exception e) {
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


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }




}

