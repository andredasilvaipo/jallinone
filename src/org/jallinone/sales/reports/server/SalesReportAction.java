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
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.chart.imagemap.ImageMapUtilities;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.math.BigDecimal;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


import org.jallinone.commons.server.JAIOBeanFactory;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to generate a graph of sales.</p>
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
public class SalesReportAction implements Action {

  public SalesReportAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "salesReport";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  try {
			Object[] pars = (Object[])inputPar;
			Integer year = (Integer)pars[0];
			Integer month = (Integer)pars[1];
			String companyCode = (String)pars[2];
			String docType = (String)pars[3];
		  SaleReportVO[] rows = salesReport(year,month,companyCode,docType,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			Resources resources = factory.getResources(userSessionPars.getLanguageId());
			String title = resources.getResource("invoiced");
			if (docType.equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
				title = resources.getResource("desk selling");
			String y = resources.getResource("sellAmount");
			String x = resources.getResource("months");
			if (month!=null)
				x = resources.getResource("days");
			byte[] graph = salesGraph(rows,title,x,y);
		  return new VOResponse(graph);
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }


		/**
		 * Business logic to execute.
		 */
		public byte[] salesGraph(SaleReportVO[] rows,String title,String x,String y) throws Throwable {
			byte[] bytes = null;

			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
			SaleReportVO row = null;
			for(int i=0;i<rows.length;i++) {
				SimpleHistogramBin histogrambin = new SimpleHistogramBin(i, i+1, true, false);
				row = rows[i];
				categoryDataset.addValue(
					row.getValue()==null?0d:row.getValue().doubleValue(),
					"",
					row.getName()
				);
			}

		  JFreeChart chart = ChartFactory.createBarChart(
				 title, // Title
				 x,     // X-Axis label
				 y,     // Y-Axis label
				 categoryDataset,         // Dataset
				 PlotOrientation.VERTICAL,
				 false,
				 false,
				 false                     // Show legend
			);

		  	try {
				String tmpdir = System.getProperty("java.io.tmpdir");
				new File(tmpdir).mkdirs();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		  
			RenderedImage img = chart.createBufferedImage(Math.max(400,(rows.length==12?60:25)*rows.length),200);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			baos.close();
			return baos.toByteArray();
		}



  /**
   * Business logic to execute.
   */
  public SaleReportVO[] salesReport(Integer year,Integer month,String companyCode,String docType,String serverLanguageId,String username) throws Throwable {
	  Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
	  try {
		  conn = ConnectionManager.getConnection(null);
			SaleReportVO vo = null;
			SaleReportVO[] rows = null;
			Calendar cal = Calendar.getInstance();
			java.sql.Date startDate = null;
			java.sql.Date endDate = null;

			if (month==null) {
				// analysys of a whole year, month by month...
				rows = new SaleReportVO[12];
				String sql =
					"select sum(DOC01_SELLING.TOTAL) "+
					"from DOC01_SELLING where "+
					"DOC01_SELLING.COMPANY_CODE_SYS01=? and "+
					"DOC01_SELLING.DOC_TYPE=? and "+
					"DOC01_SELLING.DOC_STATE=? and "+
					"DOC01_SELLING.DOC_DATE>=? and DOC01_SELLING.DOC_DATE<? and "+
					"DOC01_SELLING.ENABLED='Y' ";
				pstmt = conn.prepareStatement(sql);

	      for(int i=0;i<=11;i++) {
					cal.set(cal.YEAR,year.intValue());
					cal.set(cal.MONTH,i);
					cal.set(cal.DAY_OF_MONTH,1);
					cal.set(cal.HOUR_OF_DAY,0);
					cal.set(cal.MINUTE,0);
					cal.set(cal.SECOND,0);
					cal.set(cal.MILLISECOND,0);
					startDate = new java.sql.Date(cal.getTimeInMillis());
					if (i!=11)
						cal.set(cal.MONTH,i+1);
					else {
						cal.set(cal.YEAR,year.intValue()+1);
						cal.set(cal.MONTH,0);
					}
					endDate = new java.sql.Date(cal.getTimeInMillis());
					pstmt.setString(1,companyCode);
					pstmt.setString(2,docType);
					pstmt.setString(3,ApplicationConsts.CLOSED);
					pstmt.setDate(4,startDate);
					pstmt.setDate(5,endDate);
					rset = pstmt.executeQuery();
					vo = new SaleReportVO();
					vo.setName(String.valueOf(i+1));
					if (rset.next()) {
						vo.setValue(rset.getBigDecimal(1));
					}
					rows[i] = vo;
					rset.close();
				}
				pstmt.close();

			}
			else {
				// analysys of a specific month...
				String sql =
					"select sum(DOC01_SELLING.TOTAL),DOC01_SELLING.DOC_DATE "+
					"from DOC01_SELLING where "+
					"DOC01_SELLING.COMPANY_CODE_SYS01=? and "+
					"DOC01_SELLING.DOC_TYPE=? and "+
					"DOC01_SELLING.DOC_STATE=? and "+
					"DOC01_SELLING.DOC_DATE>=? and DOC01_SELLING.DOC_DATE<? and "+
					"DOC01_SELLING.ENABLED='Y' "+
					"GROUP BY DOC01_SELLING.DOC_DATE "+
				  "ORDER BY DOC01_SELLING.DOC_DATE ";

				pstmt = conn.prepareStatement(sql);
				cal.set(cal.YEAR,year.intValue());
				cal.set(cal.MONTH,month.intValue());
				cal.set(cal.DAY_OF_MONTH,1);
				cal.set(cal.HOUR_OF_DAY,0);
				cal.set(cal.MINUTE,0);
				cal.set(cal.SECOND,0);
				cal.set(cal.MILLISECOND,0);
				startDate = new java.sql.Date(cal.getTimeInMillis());
				if (month.intValue()!=11)
					cal.set(cal.MONTH,month.intValue()+1);
				else {
					cal.set(cal.YEAR,year.intValue()+1);
					cal.set(cal.MONTH,0);
				}
				endDate = new java.sql.Date(cal.getTimeInMillis());
				pstmt.setString(1,companyCode);
				pstmt.setString(2,docType);
				pstmt.setString(3,ApplicationConsts.CLOSED);
				pstmt.setDate(4,startDate);
				pstmt.setDate(5,endDate);
				rset = pstmt.executeQuery();

				int nrOfDays = 31;
				if (month.intValue()==1)
					nrOfDays = year.intValue()%4==0?29:28;
				else	if (month.intValue()==10 || month.intValue()==3 || month.intValue()==5 || month.intValue()==8)
					nrOfDays = 30;

				rows = new SaleReportVO[nrOfDays];
				for(int i=1;i<=nrOfDays;i++) {
					vo = new SaleReportVO();
					vo.setName(String.valueOf(i));
					rows[i-1] = vo;
				}
				int day = -1;
				while(rset.next()) {
					cal.setTime(rset.getDate(2));
					day = cal.get(cal.DAY_OF_MONTH);
					rows[day-1].setValue(rset.getBigDecimal(1));
				}
				rset.close();
				pstmt.close();
			}

	    return rows;
	  }
	  catch (Throwable ex) {
		  Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching pivot table data",ex);
		  throw new Exception(ex.getMessage());
	  }
	  finally {
			try {
				pstmt.close();
			}
			catch (Exception ex1) {
			}
		  try {
			  ConnectionManager.releaseConnection(conn, null);
		  } catch (Exception e) {
		  }

	  }


  }



}

