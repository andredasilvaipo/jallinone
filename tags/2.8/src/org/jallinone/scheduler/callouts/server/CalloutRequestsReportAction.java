package org.jallinone.scheduler.callouts.server;

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
import org.jallinone.scheduler.callouts.java.CallOutRequestReportVO;
import org.jallinone.scheduler.callouts.java.CallOutTypeVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to generate a graph of call-out requests.</p>
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
public class CalloutRequestsReportAction implements Action {

  public CalloutRequestsReportAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "calloutRequestsReport";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
	  try {
			Object[] pars = (Object[])inputPar;
			Integer year = (Integer)pars[0];
			Integer month = (Integer)pars[1];
			String companyCode = (String)pars[2];
			CallOutTypeVO callOutType = (CallOutTypeVO)pars[3];

			conn = ConnectionManager.getConnection(null);
			CallOutRequestReportVO vo = null;
			CallOutRequestReportVO[] openedRows = null;
			CallOutRequestReportVO[] closedRows = null;
			Calendar cal = Calendar.getInstance();
			java.sql.Date startDate = null;
			java.sql.Date endDate = null;

			if (month==null) {
				// analysys of a whole year, month by month...
				String sql =
					"select count(*) "+
					"from SCH03_CALL_OUT_REQUESTS,SCH10_CALL_OUTS where "+
					"SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH10_CALL_OUTS.COMPANY_CODE_SYS01 and "+
					"SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
					"SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
					"SCH10_CALL_OUTS.PROGRESSIVE_HIE02=? and "+
					"SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE=? and "+
					"SCH03_CALL_OUT_REQUESTS.REQUEST_DATE>=? and SCH03_CALL_OUT_REQUESTS.REQUEST_DATE<?  ";
				pstmt = conn.prepareStatement(sql);

				openedRows = new CallOutRequestReportVO[12];
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
					pstmt.setBigDecimal(2,callOutType.getProgressiveHie02SCH11());
					pstmt.setString(3,ApplicationConsts.OPENED);
					pstmt.setDate(4,startDate);
					pstmt.setDate(5,endDate);
					rset = pstmt.executeQuery();
					vo = new CallOutRequestReportVO();
					vo.setName(String.valueOf(i+1));
					if (rset.next()) {
						vo.setValue(rset.getBigDecimal(1));
					}
					openedRows[i] = vo;
					rset.close();
				}


				closedRows = new CallOutRequestReportVO[12];
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
					pstmt.setBigDecimal(2,callOutType.getProgressiveHie02SCH11());
					pstmt.setString(3,ApplicationConsts.CLOSED);
					pstmt.setDate(4,startDate);
					pstmt.setDate(5,endDate);
					rset = pstmt.executeQuery();
					vo = new CallOutRequestReportVO();
					vo.setName(String.valueOf(i+1));
					if (rset.next()) {
						vo.setValue(rset.getBigDecimal(1));
					}
					closedRows[i] = vo;
					rset.close();
				}




				pstmt.close();

			}
			else {
				// analysys of a specific month...
				String sql =
					"select COUNT(*),SCH03_CALL_OUT_REQUESTS.REQUEST_DATE "+
					"from SCH03_CALL_OUT_REQUESTS,SCH10_CALL_OUTS where "+
					"SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH10_CALL_OUTS.COMPANY_CODE_SYS01 and "+
					"SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
					"SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
					"SCH10_CALL_OUTS.PROGRESSIVE_HIE02=? and "+
					"SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE=? and "+
					"SCH03_CALL_OUT_REQUESTS.REQUEST_DATE>=? and SCH03_CALL_OUT_REQUESTS.REQUEST_DATE<? "+
					"GROUP BY SCH03_CALL_OUT_REQUESTS.REQUEST_DATE "+
					"ORDER BY SCH03_CALL_OUT_REQUESTS.REQUEST_DATE ";

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
				int nrOfDays = 31;
				if (month.intValue()==1)
					nrOfDays = year.intValue()%4==0?29:28;
				else	if (month.intValue()==10 || month.intValue()==3 || month.intValue()==5 || month.intValue()==8)
					nrOfDays = 30;




				pstmt.setString(1,companyCode);
				pstmt.setBigDecimal(2,callOutType.getProgressiveHie02SCH11());
				pstmt.setString(3,ApplicationConsts.OPENED);
				pstmt.setDate(4,startDate);
				pstmt.setDate(5,endDate);
				rset = pstmt.executeQuery();

				openedRows = new CallOutRequestReportVO[nrOfDays];
				for(int i=1;i<=nrOfDays;i++) {
					vo = new CallOutRequestReportVO();
					vo.setName(String.valueOf(i));
					openedRows[i-1] = vo;
				}
				int day = -1;
				while(rset.next()) {
					cal.setTime(rset.getDate(2));
					day = cal.get(cal.DAY_OF_MONTH);
					openedRows[day-1].setValue(rset.getBigDecimal(1));
				}
				rset.close();



				pstmt.setString(1,companyCode);
				pstmt.setBigDecimal(2,callOutType.getProgressiveHie02SCH11());
				pstmt.setString(3,ApplicationConsts.CLOSED);
				pstmt.setDate(4,startDate);
				pstmt.setDate(5,endDate);
				rset = pstmt.executeQuery();

				closedRows = new CallOutRequestReportVO[nrOfDays];
				for(int i=1;i<=nrOfDays;i++) {
					vo = new CallOutRequestReportVO();
					vo.setName(String.valueOf(i));
					closedRows[i-1] = vo;
				}
				day = -1;
				while(rset.next()) {
					cal.setTime(rset.getDate(2));
					day = cal.get(cal.DAY_OF_MONTH);
					closedRows[day-1].setValue(rset.getBigDecimal(1));
				}
				rset.close();

				pstmt.close();
			}


			ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
			Resources resources = factory.getResources(userSessionPars.getLanguageId());
			String title = callOutType.getDescriptionSYS10();
			String y = resources.getResource("nr");
			String x = resources.getResource("months");
			String opened = resources.getResource("opened");
			String closed = resources.getResource("closed");
			if (month!=null)
				x = resources.getResource("days");
			byte[] graph = calloutRequestsGraph(openedRows,closedRows,title,x,y,opened,closed);
		  return new VOResponse(graph);
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching data",ex);
			return new ErrorResponse(ex.getMessage());
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



		/**
		 * Business logic to execute.
		 */
		public byte[] calloutRequestsGraph(CallOutRequestReportVO[] openedRows,CallOutRequestReportVO[] closedRows,String title,String x,String y,String opened,String closed) throws Throwable {
			byte[] bytes = null;

			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
			CallOutRequestReportVO row = null;
			for(int i=0;i<openedRows.length;i++) {
				row = openedRows[i];
				categoryDataset.addValue(
					row.getValue()==null?0d:row.getValue().doubleValue(),
					opened,
					row.getName()
				);

				row = closedRows[i];
				categoryDataset.addValue(
					row.getValue()==null?0d:row.getValue().doubleValue(),
					closed,
					row.getName()
				);

			}

		  JFreeChart chart = ChartFactory.createBarChart(
				 title, // Title
				 x,     // X-Axis label
				 y,     // Y-Axis label
				 categoryDataset,         // Dataset
				 PlotOrientation.VERTICAL,
				 true,
				 false,
				 false                     // Show legend
			);

		  try {
				String tmpdir = System.getProperty("java.io.tmpdir");
				new File(tmpdir).mkdirs();
			} catch (Throwable e) {
				e.printStackTrace();
			}

			RenderedImage img = chart.createBufferedImage(Math.max(400,(openedRows.length==12?60:25)*openedRows.length),200);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			baos.close();
			return baos.toByteArray();
		}




}

