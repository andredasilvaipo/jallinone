package org.jallinone.sales.reports.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.pivottable.tablemodelreaders.server.*;
import org.openswing.swing.pivottable.server.*;
import org.openswing.swing.util.dataconverters.java.*;
import org.jallinone.sales.reports.java.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.pivottable.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fill in the sales pivot table.</p>
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
public class SalesPivotAction implements Action {

  public SalesPivotAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "salesPivot";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  SalesPivotVO vo = (SalesPivotVO)inputPar;
	  try {
		  Response answer = salesPivot(vo,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }
  
  

  /**
   * Business logic to execute.
   */
  public VOResponse salesPivot(SalesPivotVO vo,String serverLanguageId,String username) throws Throwable {
	  Connection conn = null;
	  try {
		  conn = ConnectionManager.getConnection(null);

		  String select =
			  " select DOC01_SELLING.DOC_YEAR,DOC01_SELLING.TOTAL,DOC01_SELLING.DOC_DATE,DOC01_SELLING.WAREHOUSE_CODE_WAR01 ";
		  String from =
			  " from DOC01_SELLING ";
		  String where =
			  " where DOC01_SELLING.ENABLED='Y' ";

		  ArrayList values = new ArrayList();
		  HashMap attributesMap = new HashMap();
		  attributesMap.put("itemTypeITM01","ITEM_TYPE");
		  attributesMap.put("itemCodeItm01DOC02","ITEM_CODE_ITM01");
		  attributesMap.put("docYearDOC01","DOC_YEAR");
		  attributesMap.put("qtyDOC02","QTY");
		  attributesMap.put("totalDOC01","TOTAL");
		  attributesMap.put("docDateDOC01","DOC_DATE");
		  attributesMap.put("customerCodeSAL07","CUSTOMER_CODE");
		  attributesMap.put("warehouseCodeWar01DOC01","WAREHOUSE_CODE_WAR01");

		  if (vo.getCompanyCode()!=null && !vo.getCompanyCode().equals("")) {
			  where += " and DOC01_SELLING.COMPANY_CODE_SYS01=? ";
			  values.add(vo.getCompanyCode());
		  }
		  if (vo.getDocType()!=null && !vo.getDocType().equals("")) {
			  where += " and DOC01_SELLING.DOC_TYPE=? ";
			  values.add(vo.getDocType());
		  }
		  if (vo.getDocState()!=null && !vo.getDocState().equals("")) {
			  where += " and DOC01_SELLING.DOC_STATE=? ";
			  values.add(vo.getDocState());
		  }
		  if (vo.getDocYear()!=null && !vo.getDocYear().equals("")) {
			  where += " and DOC01_SELLING.DOC_YEAR=? ";
			  values.add(vo.getDocYear());
		  }


		  boolean customerAdded = false;
		  if (vo.getPivotPars().getDataFields().indexOf("customerCodeSAL07")!=-1) {
			  customerAdded = true;
			  from += ",REG04_SUBJECTS,SAL07_CUSTOMERS ";
			  where +=
				  " and "+
				  "DOC01_SELLING.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
				  "DOC01_SELLING.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
				  "DOC01_SELLING.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
				  "DOC01_SELLING.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 ";
		  }
		  boolean itemAdded = false;
		  boolean innerItemAdded = false;
		  boolean rowFieldsContainsItemCode = false;
		  for(int i=0;i<vo.getPivotPars().getRowFields().size();i++)
			  if (((RowField)vo.getPivotPars().getRowFields().get(i)).getColumnName().equals("itemCodeItm01DOC02")) {
				  rowFieldsContainsItemCode = true;
				  break;
			  }
		  if (rowFieldsContainsItemCode) {
			  select += ",DOC02_SELLING_ITEMS.ITEM_CODE_ITM01 ";
			  from +=
				  ",DOC02_SELLING_ITEMS ";
			  where +=
				  " and "+
				  " DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
				  " DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
				  " DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
				  " DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER ";
			  innerItemAdded = true;
		  }


		  boolean dataFieldsContainsQty = false;
		  for(int i=0;i<vo.getPivotPars().getDataFields().size();i++)
			  if (((DataField)vo.getPivotPars().getDataFields().get(i)).getColumnName().equals("qtyDOC02")) {
				  dataFieldsContainsQty = true;
				  break;
			  }
		  if (dataFieldsContainsQty) {
			  select += ",DOC02_SELLING_ITEMS.QTY ";
			  if (!itemAdded && !innerItemAdded) {
				  from +=
					  ",DOC02_SELLING_ITEMS ";
				  where +=
					  " and "+
					  " DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
					  " DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
					  " DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
					  " DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER ";

				  innerItemAdded = true;
			  }
		  }
		  if (vo.getCustomerCode()!=null && !vo.getCustomerCode().equals("")) {
			  if (!customerAdded) {
				  from += ",REG04_SUBJECTS,SAL07_CUSTOMERS ";
				  where +=
					  " and "+
					  "DOC01_SELLING.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
					  "DOC01_SELLING.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
					  "DOC01_SELLING.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
					  "DOC01_SELLING.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 ";
			  }
			  where +=
				  " and SAL07_CUSTOMERS.CUSTOMER_CODE=? ";
			  values.add(vo.getCustomerCode());
			  customerAdded = true;
		  }
		  if (vo.getAmount()!=null && !vo.getAmount().equals("")) {
			  where += " and DOC01_SELLING.TOTAL>=? ";
			  values.add(vo.getAmount());
		  }
		  if (vo.getProgressiveHie02()!=null && !vo.getProgressiveHie02().equals("")) {
			  if (!itemAdded && !innerItemAdded) {
				  where +=
					  " and EXISTS(SELECT * FROM DOC02_SELLING_ITEMS "+
					  " where "+
					  " DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
					  " DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
					  " DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
					  " DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER and "+
					  " DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=?  ";
			  }
			  else
				  where +=
					  " and DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02=? ";
			  values.add(vo.getProgressiveHie02());
			  itemAdded = true;
		  }
		  if (vo.getItemCode()!=null && !vo.getItemCode().equals("")) {
			  if (!itemAdded && !innerItemAdded) {
				  where +=
					  " and EXISTS(SELECT * FROM DOC02_SELLING_ITEMS "+
					  " where "+
					  " DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
					  " DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
					  " DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
					  " DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER and "+
					  " DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? ";
			  }
			  else
				  where +=
					  " and DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? ";
			  values.add(vo.getItemCode());
			  itemAdded = true;
		  }

		  if (itemAdded && !innerItemAdded)
			  where += ")";

		  StringBuffer msg = new StringBuffer();
		  msg.append("Query to execute:\n").append(select+from+where);
		  if (values.size()>0) {
			  msg.append("\nParameters:\n");
			  for(int i=0;i<values.size();i++)
				  msg.append("PARAM").append(i+1).append(": '").append(values.get(i)).append("'\n");
		  }

		  Logger.debug(username,this.getClass().getName(),"executeCommand",msg.toString());


		  SQLReader reader = new SQLReader(
				  select+from+where,
				  values,
				  conn,
				  attributesMap
		  );
		  //          new String[]{
		  //            "orderDate",
		  //            "category",
		  //            "subCategory",
		  //            "country",
		  //            "region",
		  //            "agent",
		  //            "item",
		  //            "sellQty",
		  //            "sellAmount"
		  //          },
		  //          new DataConverter[]{
		  //            new StringToDateConverter(new SimpleDateFormat("dd/MM/yyyy")),
		  //            new DataConverter(),
		  //            new DataConverter(),
		  //            new DataConverter(),
		  //            new DataConverter(),
		  //            new DataConverter(),
		  //            new DataConverter(),
		  //            new StringToDoubleConverter(),
		  //            new StringToDoubleConverter()
		  //          }
		  //      );


		  PivotTableEngine engine = new PivotTableEngine(reader);
		  Response res = engine.getPivotTableModel(vo.getPivotPars());
		  if (res.isError())
			  throw new Exception(res.getErrorMessage());
		  return (VOResponse)res;
	  }
	  catch (Throwable ex) {
		  Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching pivot table data",ex);
		  throw new Exception(ex.getMessage());
	  }
	  finally {
		  try {
			  ConnectionManager.releaseConnection(conn, null);
		  } catch (Exception e) {
		  }

	  }


  }

  
  
}

