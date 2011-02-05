package org.jallinone.sales.agents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.sales.agents.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage agents and agent types.</p>
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
public class AgentsBean  implements Agents {


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




  public AgentsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public AgentVO getAgent() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public AgentTypeVO getAgentType() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadAgents(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select SAL10_AGENTS.COMPANY_CODE_SYS01,SAL10_AGENTS.AGENT_CODE,SAL10_AGENTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,SAL10_AGENTS.PERCENTAGE,SAL10_AGENTS.PROGRESSIVE_REG04,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from SAL10_AGENTS,SYS10_TRANSLATIONS,REG04_SUBJECTS where "+
          "SAL10_AGENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL10_AGENTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL10_AGENTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SAL10_AGENTS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL10","SAL10_AGENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("percentageSAL10","SAL10_AGENTS.PERCENTAGE");
      attribute2dbField.put("progressiveReg04SAL10","SAL10_AGENTS.PROGRESSIVE_REG04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("agentCodeSAL10","SAL10_AGENTS.AGENT_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL10","SAL10_AGENTS.PROGRESSIVE_SYS10");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SAL10,REG04,SYS10 tables...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          AgentVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching agents list",ex);
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
  public VOListResponse loadAgentTypes(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG19_AGENT_TYPES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,REG19_AGENT_TYPES.ENABLED from REG19_AGENT_TYPES,SYS10_TRANSLATIONS where "+
          "REG19_AGENT_TYPES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG19_AGENT_TYPES.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG19","REG19_AGENT_TYPES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG19","REG19_AGENT_TYPES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from REG19 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          AgentTypeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching agent types list",ex);
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
  public VOListResponse updateAgents(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      AgentVO oldVO = null;
      AgentVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (AgentVO)oldVOs.get(i);
        newVO = (AgentVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01SAL10");
        pkAttrs.add("progressiveReg04SAL10");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01SAL10","COMPANY_CODE_SYS01");
        attribute2dbField.put("percentageSAL10","PERCENTAGE");
        attribute2dbField.put("progressiveReg04SAL10","PROGRESSIVE_REG04");
        attribute2dbField.put("agentCodeSAL10","AGENT_CODE");
        attribute2dbField.put("progressiveSys10SAL10","PROGRESSIVE_SYS10");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SAL10_AGENTS",
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
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing agents",ex);
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
  public VOListResponse updateAgentTypes(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      AgentTypeVO oldVO = null;
      AgentTypeVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (AgentTypeVO)oldVOs.get(i);
        newVO = (AgentTypeVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG19(),serverLanguageId,conn);
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing agent types",ex);
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
  public VOListResponse validateAgentCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SAL10_AGENTS.COMPANY_CODE_SYS01,SAL10_AGENTS.AGENT_CODE,SAL10_AGENTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,SAL10_AGENTS.PERCENTAGE,SAL10_AGENTS.PROGRESSIVE_REG04,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from SAL10_AGENTS,SYS10_TRANSLATIONS,REG04_SUBJECTS where "+
          "SAL10_AGENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL10_AGENTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL10_AGENTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SAL10_AGENTS.COMPANY_CODE_SYS01=? and "+
          "SAL10_AGENTS.AGENT_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL10","SAL10_AGENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("percentageSAL10","SAL10_AGENTS.PERCENTAGE");
      attribute2dbField.put("progressiveReg04SAL10","SAL10_AGENTS.PROGRESSIVE_REG04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("agentCodeSAL10","SAL10_AGENTS.AGENT_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL10","SAL10_AGENTS.PROGRESSIVE_SYS10");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01));

      GridParams gridParams = new GridParams();

      // read from SAL10 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          AgentVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating agent code",ex);
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
  public VOListResponse insertAgents(ArrayList list,String t1,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      AgentVO vo = null;

      pstmt = conn.prepareStatement("select AGENT_CODE from SAL10_AGENTS where COMPANY_CODE_SYS01=? and AGENT_CODE=?");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL10","COMPANY_CODE_SYS01");
      attribute2dbField.put("percentageSAL10","PERCENTAGE");
      attribute2dbField.put("progressiveReg04SAL10","PROGRESSIVE_REG04");
      attribute2dbField.put("agentCodeSAL10","AGENT_CODE");
      attribute2dbField.put("progressiveSys10SAL10","PROGRESSIVE_SYS10");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (AgentVO)list.get(i);

        // check if there exists an agent with the same code...
        pstmt.setString(1,vo.getCompanyCodeSys01SAL10());
        pstmt.setString(2,vo.getAgentCodeSAL10());
        ResultSet rset = pstmt.executeQuery();
        if (rset.next()) {
          rset.close();
          return new VOListResponse(t1);
        }
        rset.close();

        // insert into SAL10...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SAL10_AGENTS",
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
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new agents", ex);
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
  public VOListResponse insertAgentTypes(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      AgentTypeVO vo = null;

      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveSys10REG19","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG19","ENABLED");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (AgentTypeVO)list.get(i);
        vo.setEnabledREG19("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);
        vo.setProgressiveSys10REG19(progressiveSYS10);

        // insert into REG19...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG19_AGENT_TYPES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new agent types", ex);
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
  public VOResponse deleteAgents(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      AgentVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SAL10...
        vo = (AgentVO)list.get(i);
        stmt.execute("delete from SAL10_AGENTS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL10()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04SAL10());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing agents",ex);
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
  public VOResponse deleteAgentTypes(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      AgentTypeVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG19...
        vo = (AgentTypeVO)list.get(i);
        stmt.execute("update REG19_AGENT_TYPES set ENABLED='N' where PROGRESSIVE_SYS10="+vo.getProgressiveSys10REG19());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing agent types",ex);
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




}

