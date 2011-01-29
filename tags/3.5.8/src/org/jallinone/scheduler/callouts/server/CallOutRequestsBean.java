package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage call-out requests.</p>
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
public class CallOutRequestsBean  implements CallOutRequests {


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




  private ScheduledActivitiesBean delAct;

  public void setDelAct(ScheduledActivitiesBean delAct) {
    this.delAct = delAct;
  }



  public CallOutRequestsBean() {
  }




  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridCallOutRequestVO getGridCallOutRequest(CallOutPK pk) {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOResponse insertCallOutRequest(DetailCallOutRequestVO vo,String serverLanguageId,String username,ArrayList customizedFields ) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      vo.setProgressiveSCH03(CompanyProgressiveUtils.getConsecutiveProgressive(
          vo.getCompanyCodeSys01SCH03(),
          "SCH03_CALL_OUT_REQUESTS_YEAR="+vo.getRequestYearSCH03(),
          "PROGRESSIVE",
          conn
      ));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutStateSCH03","CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","USERNAME_SYS03");
      attribute2dbField.put("prioritySCH03","PRIORITY");
      attribute2dbField.put("requestDateSCH03","REQUEST_DATE");
      attribute2dbField.put("noteSCH03","NOTE");
      attribute2dbField.put("docTypeDoc01SCH03","DOC_TYPE_DOC01");
      attribute2dbField.put("docNumberDoc01SCH03","DOC_NUMBER_DOC01");
      attribute2dbField.put("docYearDoc01SCH03","DOC_YEAR_DOC01");
      attribute2dbField.put("progressiveReg04SCH03","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH03","PROGRESSIVE_SCH06");
      attribute2dbField.put("subjectTypeReg04SCH03","SUBJECT_TYPE_REG04");
			attribute2dbField.put("itemCodeItm01SCH03","ITEM_CODE_ITM01");

      // insert into SCH03...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SCH03_CALL_OUT_REQUESTS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new call-out request",ex);
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
  public VOResponse loadCallOutRequest(CallOutRequestPK pk,String serverLanguageId,String username,ArrayList customizedFields ) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH03");
      pkAttributes.add("requestYearSCH03");
      pkAttributes.add("progressiveSCH03");

      String sql =
          "select SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01,SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE,"+
          "SCH03_CALL_OUT_REQUESTS.DESCRIPTION,SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE,SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03,SCH03_CALL_OUT_REQUESTS.SUBJECT_TYPE_REG04,"+
          "SCH03_CALL_OUT_REQUESTS.PRIORITY,SCH03_CALL_OUT_REQUESTS.REQUEST_DATE,SCH10_CALL_OUTS.PROGRESSIVE_HIE02,"+
          "SCH03_CALL_OUT_REQUESTS.NOTE,SCH03_CALL_OUT_REQUESTS.DOC_TYPE_DOC01,SCH03_CALL_OUT_REQUESTS.DOC_NUMBER_DOC01,"+
          "SCH03_CALL_OUT_REQUESTS.DOC_YEAR_DOC01,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06, "+
					"ITM.PROGRESSIVE_HIE02,SCH03_CALL_OUT_REQUESTS.ITEM_CODE_ITM01,ITM.DESCRIPTION "+
          "from SCH10_CALL_OUTS,SYS10_TRANSLATIONS,SCH03_CALL_OUT_REQUESTS "+
					"LEFT OUTER JOIN ("+
					" SELECT ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
					" FROM ITM01_ITEMS,SYS10_TRANSLATIONS WHERE "+
					" ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
					" SYS10_TRANSLATIONS.LANGUAGE_CODE=? "+
					") ITM ON "+
					" SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=ITM.COMPANY_CODE_SYS01 AND "+
					" SCH03_CALL_OUT_REQUESTS.ITEM_CODE_ITM01=ITM.ITEM_CODE "+
					"where "+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
          "SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR=? and "+
          "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","SCH03_CALL_OUT_REQUESTS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutStateSCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03");
      attribute2dbField.put("prioritySCH03","SCH03_CALL_OUT_REQUESTS.PRIORITY");
      attribute2dbField.put("requestDateSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_DATE");
      attribute2dbField.put("noteSCH03","SCH03_CALL_OUT_REQUESTS.NOTE");
      attribute2dbField.put("docTypeDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_TYPE_DOC01");
      attribute2dbField.put("docNumberDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_NUMBER_DOC01");
      attribute2dbField.put("docYearDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_YEAR_DOC01");
      attribute2dbField.put("progressiveReg04SCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06");
      attribute2dbField.put("subjectTypeReg04SCH03","SCH03_CALL_OUT_REQUESTS.SUBJECT_TYPE_REG04");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");

			attribute2dbField.put("progressiveHie02ITM01","ITM.PROGRESSIVE_HIE02");
			attribute2dbField.put("itemCodeItm01SCH03","SCH03_CALL_OUT_REQUESTS.ITEM_CODE_ITM01");
			attribute2dbField.put("descriptionSYS10","ITM.DESCRIPTION");

      ArrayList values = new ArrayList();
			values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH03());
      values.add(pk.getRequestYearSCH03());
      values.add(pk.getProgressiveSCH03());

      // read from SCH03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          DetailCallOutRequestVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing call-out request",ex);
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
  public VOListResponse loadCallOutRequests(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01,SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE,"+
          "SCH03_CALL_OUT_REQUESTS.DESCRIPTION,SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE,SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03,"+
          "REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH03_CALL_OUT_REQUESTS.PRIORITY,SCH03_CALL_OUT_REQUESTS.REQUEST_DATE "+
          " from SCH03_CALL_OUT_REQUESTS,SCH10_CALL_OUTS,SYS10_TRANSLATIONS,REG04_SUBJECTS where "+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","SCH03_CALL_OUT_REQUESTS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutStateSCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("prioritySCH03","SCH03_CALL_OUT_REQUESTS.PRIORITY");
      attribute2dbField.put("requestDateSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_DATE");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SCH03 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridCallOutRequestVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching call-out requests list",ex);
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
   * Update call-out request.
   */
  public VOResponse updateCallOutRequest(DetailCallOutRequestVO oldVO,DetailCallOutRequestVO newVO,String serverLanguageId,String username,ArrayList customizedFields ) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutStateSCH03","CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","USERNAME_SYS03");
      attribute2dbField.put("prioritySCH03","PRIORITY");
      attribute2dbField.put("requestDateSCH03","REQUEST_DATE");
      attribute2dbField.put("noteSCH03","NOTE");
      attribute2dbField.put("docTypeDoc01SCH03","DOC_TYPE_DOC01");
      attribute2dbField.put("docNumberDoc01SCH03","DOC_NUMBER_DOC01");
      attribute2dbField.put("docYearDoc01SCH03","DOC_YEAR_DOC01");
      attribute2dbField.put("progressiveReg04SCH03","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH03","PROGRESSIVE_SCH06");
      attribute2dbField.put("subjectTypeReg04SCH03","SUBJECT_TYPE_REG04");
			attribute2dbField.put("itemCodeItm01SCH03","ITEM_CODE_ITM01");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH03");
      pkAttributes.add("requestYearSCH03");
      pkAttributes.add("progressiveSCH03");

      // update SCH03 table...
      Response answer = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "SCH03_CALL_OUT_REQUESTS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateCallOutRequest","Error while updating an existing call-out request",ex);
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
  public VOResponse deleteCallOutRequests(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      delAct.setConn(conn); // use same transaction...
      CallOutRequestPK pk = null;

      Response res = null;
      DetailCallOutRequestVO vo = null;
      stmt = conn.createStatement();
      ArrayList pks = new ArrayList();
      for(int i=0;i<list.size();i++) {
        pk = (CallOutRequestPK)list.get(i);

        res = loadCallOutRequest(pk,serverLanguageId,username,new ArrayList());
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        vo = (DetailCallOutRequestVO)((VOResponse)res).getVo();

        if (vo.getProgressiveSch06SCH03()!=null) {
          // phisically delete the record in SCH07, SCH08, SCH09, SCH06...
          pks.clear();
          pks.add(new ScheduledActivityPK(vo.getCompanyCodeSys01SCH03(),vo.getProgressiveSch06SCH03()));
          res = delAct.deleteActivities(pks,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }

        // phisically delete the record in SCH03...
        stmt.execute(
            "delete from SCH03_CALL_OUT_REQUESTS where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH03()+"' and "+
            "REQUEST_YEAR="+pk.getRequestYearSCH03()+" and "+
            "PROGRESSIVE="+pk.getProgressiveSCH03()
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing call-out requests",ex);
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
    	try {
    		delAct.setConn(null);
    	} catch (Exception ex) {}
    }

  }



}

