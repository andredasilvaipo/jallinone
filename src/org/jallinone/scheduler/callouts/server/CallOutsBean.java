package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;

import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage call-outs.</p>
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
public class CallOutsBean  implements CallOuts {


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




  public CallOutsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public HierarchyLevelVO getHierarchyLevel() {
	  throw new UnsupportedOperationException();	  
  }

  

  /**
   * Business logic to execute.
   */
  public VOResponse loadCallOut(CallOutPK pk,String serverLanguageId,String username,ArrayList customizedFields ) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","SCH10_CALL_OUTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","SCH10_CALL_OUTS.CALL_OUT_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("levelDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","SCH10_CALL_OUTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledSCH10","SCH10_CALL_OUTS.ENABLED");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH10");
      pkAttributes.add("callOutCodeSCH10");

      String baseSQL =
          "select "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01,SCH10_CALL_OUTS.CALL_OUT_CODE,A.DESCRIPTION,B.DESCRIPTION,"+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02,SCH10_CALL_OUTS.PROGRESSIVE_HIE01,SCH10_CALL_OUTS.PROGRESSIVE_SYS10 "+
          " from SCH10_CALL_OUTS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,HIE01_LEVELS "+
          " where "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=A.PROGRESSIVE and A.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH10_CALL_OUTS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01=? and "+
          "SCH10_CALL_OUTS.CALL_OUT_CODE=?";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());

      // read from SCH10 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          CallOutVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields 
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing call-out",ex);
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
  public VOListResponse loadCallOuts(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

      HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
      if (vo!=null) {
        progressiveHIE01 = vo.getProgressiveHIE01();
        progressiveHIE02 = vo.getProgressiveHie02HIE01();
      }

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select SCH10_CALL_OUTS.COMPANY_CODE_SYS01,SCH10_CALL_OUTS.CALL_OUT_CODE,A.DESCRIPTION,B.DESCRIPTION,"+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02,SCH10_CALL_OUTS.PROGRESSIVE_HIE01,SCH10_CALL_OUTS.PROGRESSIVE_SYS10"+
          " from SCH10_CALL_OUTS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,HIE01_LEVELS where "+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02=? and "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH10_CALL_OUTS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SCH10_CALL_OUTS.ENABLED='Y' ";

      if (rootProgressiveHIE01==null || !rootProgressiveHIE01.equals(progressiveHIE01)) {
        // retrieve all subnodes of the specified node...
        pstmt = conn.prepareStatement(
            "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
            "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
            "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
        );
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
        sql += " and SCH10_CALL_OUTS.PROGRESSIVE_HIE01 in ("+nodes+")";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","SCH10_CALL_OUTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","SCH10_CALL_OUTS.CALL_OUT_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","SCH10_CALL_OUTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("levelDescriptionSYS10","B.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(progressiveHIE02);
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      // read from SCH10 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CallOutVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching call-outs list",ex);
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
  public VOResponse updateCallOut(CallOutVO oldVO,CallOutVO newVO,String serverLanguageId,String username,ArrayList customizedFields ) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // update call-out description...
      TranslationUtils.updateTranslation(
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveSys10SCH10(),
          serverLanguageId,
          conn
      );

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","CALL_OUT_CODE");
      attribute2dbField.put("progressiveHie02SCH10","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","PROGRESSIVE_SYS10");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH10");
      pkAttributes.add("callOutCodeSCH10");

      // update SCH10 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "SCH10_CALL_OUTS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing call-out",ex);
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
  public VOListResponse validateCallOutCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);

      String sql =
          "select SCH10_CALL_OUTS.COMPANY_CODE_SYS01,SCH10_CALL_OUTS.CALL_OUT_CODE,A.DESCRIPTION,B.DESCRIPTION,"+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02,SCH10_CALL_OUTS.PROGRESSIVE_HIE01,SCH10_CALL_OUTS.PROGRESSIVE_SYS10 "+
          " from SCH10_CALL_OUTS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,HIE01_LEVELS where "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH10_CALL_OUTS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01 ='"+companyCodeSYS10+"' and "+
          "SCH10_CALL_OUTS.CALL_OUT_CODE ='"+pars.getCode()+"' and "+
          "SCH10_CALL_OUTS.ENABLED='Y' ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","SCH10_CALL_OUTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","SCH10_CALL_OUTS.CALL_OUT_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","SCH10_CALL_OUTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("levelDescriptionSYS10","B.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      if (pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE02)!=null) {
        sql += " and SCH10_CALL_OUTS.PROGRESSIVE_HIE02=?";
        values.add(pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE02));
      }

      // read from SCH10 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CallOutVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating call-out code",ex);
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
  public VOResponse insertCallOut(CallOutVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields ) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCode = companiesList.get(0).toString();

      vo.setEnabledSCH10("Y");
      if (vo.getCompanyCodeSys01SCH10()==null)
        vo.setCompanyCodeSys01SCH10(companyCode);

      // generate progressive for call-out description...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SCH10(),conn);
      vo.setProgressiveSys10SCH10(progressiveSYS10);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","CALL_OUT_CODE");
      attribute2dbField.put("progressiveHie02SCH10","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledSCH10","ENABLED");

      // insert into SCH10...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SCH10_CALL_OUTS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new call-out",ex);
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
  public VOResponse deleteCallOuts(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CallOutPK pk = null;
      stmt = conn.createStatement();

      for(int i=0;i<list.size();i++) {
        pk = (CallOutPK)list.get(i);

        // logically delete the record in SCH10...
        stmt.execute(
            "update SCH10_CALL_OUTS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH10()+"' and "+
            "CALL_OUT_CODE='"+pk.getCallOutCodeSCH10()+"'"
        );
      }


      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing call-outs",ex);
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
    	} catch (Exception ex) {}
    }

  }



}

