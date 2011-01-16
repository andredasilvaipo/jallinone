package org.jallinone.subjects.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.subjects.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage subjects.</p>
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
public class SubjectsBean  implements Subjects {


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




  private PeopleBean peopleBean;

  public void setPeopleBean(PeopleBean peopleBean) {
    this.peopleBean = peopleBean;
  }

  private OrganizationBean orgBean;

  public void setOrgBean(OrganizationBean orgBean) {
    this.orgBean = orgBean;
  }



  public SubjectsBean() {
  }

  

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ReferenceVO getReferenceVO(SubjectPK pk) {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public SubjectHierarchyLevelVO getSubjectHierarchyLevel(SubjectVO vo) {
	  throw new UnsupportedOperationException();
  }

  

  /**
   * Business logic to execute.
   */
  public VOResponse insertOrganization(OrganizationVO inputPar,String t1,String t2,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      
      peopleBean.setConn(conn); // use same transaction...
      orgBean.setConn(conn); // use same transaction...
      orgBean.insert(true,(OrganizationVO)inputPar,t2,serverLanguageId,username);
      return new VOResponse(inputPar);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new subject", ex);
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
    	try {
    		peopleBean.setConn(null);
    		orgBean.setConn(null);
    	} catch (Exception ex) {}
    }

  }



  /**
   * Business logic to execute.
   */
  public VOResponse insertPeople(PeopleVO inputPar,String t1,String t2,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn); // use same transaction...
      orgBean.setConn(conn); // use same transaction...
      peopleBean.insert(true,(PeopleVO)inputPar,t1,serverLanguageId,username);
      return new VOResponse(inputPar);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new subject", ex);
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
    	try {
    		peopleBean.setConn(null);
    		orgBean.setConn(null);
    	} catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOResponse insertSubjectHierarchy(SubjectHierarchyVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // insert record in SYS10...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01REG08(),conn);
      vo.setProgressiveSys10REG08(progressiveSYS10);

      // insert into HIE02...
      vo.setProgressiveHie02REG08( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01REG08(),"HIE02_HIERARCHIES","PROGRESSIVE",conn) );
      pstmt = conn.prepareStatement("insert into HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) values(?,'"+vo.getCompanyCodeSys01REG08()+"','Y')");
      pstmt.setBigDecimal(1,vo.getProgressiveHie02REG08());
      pstmt.execute();
      pstmt.close();

      // insert into HIE01...
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01REG08(),conn);
      pstmt = conn.prepareStatement("insert into HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) values(?,?,0,'Y')");
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie02REG08());
      pstmt.execute();
      pstmt.close();
      pstmt = conn.prepareStatement("update HIE02_HIERARCHIES set PROGRESSIVE_HIE01=? where PROGRESSIVE=?");
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie02REG08());
      pstmt.execute();

      // insert into REG08...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG08","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSys10REG08","PROGRESSIVE_SYS10");
      attribute2dbField.put("subjectTypeREG08","SUBJECT_TYPE");
      attribute2dbField.put("progressiveHie02REG08","PROGRESSIVE_HIE02");

      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "REG08_SUBJECT_HIERARCHIES",
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
                   "executeCommand", "Error while inserting a new SubjectHierarchy", ex);
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
  public VOResponse insertSubjectsLinks(HierarSubjectsVO hsVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // insert record in REG16...
      pstmt = conn.prepareStatement("insert into REG16_SUBJECTS_LINKS(COMPANY_CODE_SYS01,PROGRESSIVE_REG04,PROGRESSIVE_HIE01,PROGRESSIVE_HIE02) values(?,?,?,?)");
      Subject vo = null;
      for(int i=0;i<hsVO.getSubjects().size();i++) {
        vo = (Subject)hsVO.getSubjects().get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01REG04());
        pstmt.setBigDecimal(2,vo.getProgressiveREG04());
        pstmt.setBigDecimal(3,hsVO.getProgressiveHie01REG16());
        pstmt.setBigDecimal(4,hsVO.getProgressiveHie02REG16());
        try {
          pstmt.execute();
        }
        catch (SQLException ex4) {
          Logger.warn(username, this.getClass().getName(),
                       "executeCommand", "Error while inserting a subjects link:\n"+ex4.getMessage());
        }
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting subjects links", ex);
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
  public VOListResponse loadHierarSubjects(GridParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);
      String companyCodeSYS01 = (String)pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String subjectType = (String)pars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);
      Boolean loadOnlyCurrentLevel = (Boolean)pars.getOtherGridParams().get(ApplicationConsts.LOAD_ONLY_CURRENT_LEVEL);

      String subjectTypes = null;
      if (subjectType.equals(ApplicationConsts.SUBJECT_EMPLOYEE) ||
          subjectType.equals(ApplicationConsts.SUBJECT_SUPPLIER))
        subjectTypes = "'"+subjectType+"'";
      else if (subjectType.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
          subjectType.equals(ApplicationConsts.SUBJECT_PEOPLE_CONTACT))
        subjectTypes = "'"+ApplicationConsts.SUBJECT_PEOPLE_CONTACT+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT+"'";
      else if (subjectType.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ||
          subjectType.equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER))
        subjectTypes = "'"+ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER+"'";


      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS,REG16_SUBJECTS_LINKS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ("+subjectTypes+") and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=REG16_SUBJECTS_LINKS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=REG16_SUBJECTS_LINKS.PROGRESSIVE_REG04 and "+
          "REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE02=? and "+
          "REG04_SUBJECTS.ENABLED='Y'";


      if (!rootProgressiveHIE01.equals(progressiveHIE01)) {
        // retrieve all subnodes of the specified node...

        if (loadOnlyCurrentLevel!=null && loadOnlyCurrentLevel.booleanValue())
          pstmt = conn.prepareStatement(
              "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
              "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE=? "+
              "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
          );
        else
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
        sql += " and PROGRESSIVE_HIE01 in ("+nodes+")";
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");

      ArrayList values = new ArrayList();
      values.add(companyCodeSYS01);
      values.add(progressiveHIE02);

      // read from REG04,REG16 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SubjectVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching subjects list",ex);
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
  public VOListResponse loadReferences(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);

      String sql =
          "select REG15_REFERENCES.COMPANY_CODE_SYS01,REG15_REFERENCES.PROGRESSIVE,REG15_REFERENCES.PROGRESSIVE_REG04,REG15_REFERENCES.REFERENCE_TYPE,REG15_REFERENCES.VALUE from REG15_REFERENCES where "+
          "REG15_REFERENCES.COMPANY_CODE_SYS01=? and REG15_REFERENCES.PROGRESSIVE_REG04=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG15","REG15_REFERENCES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG15","REG15_REFERENCES.PROGRESSIVE");
      attribute2dbField.put("progressiveReg04REG15","REG15_REFERENCES.PROGRESSIVE_REG04");
      attribute2dbField.put("referenceTypeREG15","REG15_REFERENCES.REFERENCE_TYPE");
      attribute2dbField.put("valueREG15","REG15_REFERENCES.VALUE");

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());


      // read from REG15 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ReferenceVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching references list",ex);
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
  public VOListResponse loadSubjectHierarchies(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String subjectTypeREG08 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);


      String sql =
          "select REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01,REG08_SUBJECT_HIERARCHIES.SUBJECT_TYPE,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION from REG08_SUBJECT_HIERARCHIES,SYS10_TRANSLATIONS where "+
          "REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01 in ("+companies+") and SUBJECT_TYPE='"+subjectTypeREG08+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG08","REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveHie02REG08","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSys10REG08","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("subjectTypeREG08","REG08_SUBJECT_HIERARCHIES.SUBJECT_TYPE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from REG08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SubjectHierarchyVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching subject hierarchies list",ex);
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
  public VOListResponse loadSubjectHierarchyLevels(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);
      String subjectType = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);


      String sql =
          "select REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION from REG08_SUBJECT_HIERARCHIES,SYS10_TRANSLATIONS where "+
          "REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01=? and SUBJECT_TYPE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG16","REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveHie02REG16","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02");
      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(subjectType);

      // read from REG08 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SubjectHierarchyLevelVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (res.isError())
        throw new Exception(res.getErrorMessage());

     java.util.List rows = ((VOListResponse)res).getRows();
      Hashtable hash = new Hashtable();
      SubjectHierarchyLevelVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (SubjectHierarchyLevelVO)rows.get(i);
        vo.setProgressiveReg04REG16(pk.getProgressiveREG04());
        hash.put(
            vo.getProgressiveHie02REG16(),
            vo
        );
      }

      sql =
          "select SYS10_TRANSLATIONS.DESCRIPTION,REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE02,REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE01 "+
          "from REG16_SUBJECTS_LINKS,SYS10_TRANSLATIONS "+
          "where "+
          "REG16_SUBJECTS_LINKS.PROGRESSIVE_HIE01=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG16_SUBJECTS_LINKS.COMPANY_CODE_SYS01=? and REG16_SUBJECTS_LINKS.PROGRESSIVE_REG04=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,serverLanguageId);
      pstmt.setString(2,pk.getCompanyCodeSys01REG04());
      pstmt.setBigDecimal(3,pk.getProgressiveREG04());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (SubjectHierarchyLevelVO)hash.get(rset.getBigDecimal(2));
        if (vo!=null) {
            vo.setLevelDescriptionSYS10(rset.getString(1));
            vo.setProgressiveHie01REG16(rset.getBigDecimal(3));
        }
      }
      rset.close();

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching subject hierarchy levels list",ex);
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
  public VOListResponse loadSubjectPerName(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
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

      String subjectTypeREG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE); // mandatory
      String name_1REG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.NAME_1); // optional
      String name_2REG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.NAME_2); // optional
      BigDecimal progressiveREG04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04); // optional



      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("addressREG04","REG04_SUBJECTS.ADDRESS");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("zipREG04","REG04_SUBJECTS.ZIP");
      attribute2dbField.put("sexREG04","REG04_SUBJECTS.SEX");
      attribute2dbField.put("maritalStatusREG04","REG04_SUBJECTS.MARITAL_STATUS");
      attribute2dbField.put("nationalityREG04","REG04_SUBJECTS.NATIONALITY");
      attribute2dbField.put("birthdayREG04","REG04_SUBJECTS.BIRTHDAY");
      attribute2dbField.put("birthplaceREG04","REG04_SUBJECTS.BIRTHPLACE");
      attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
      attribute2dbField.put("mobileNumberREG04","REG04_SUBJECTS.MOBILE_NUMBER");
      attribute2dbField.put("faxNumberREG04","REG04_SUBJECTS.FAX_NUMBER");
      attribute2dbField.put("emailAddressREG04","REG04_SUBJECTS.EMAIL_ADDRESS");
      attribute2dbField.put("webSiteREG04","REG04_SUBJECTS.WEB_SITE");
      attribute2dbField.put("lawfulSiteREG04","REG04_SUBJECTS.LAWFUL_SITE");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01REG04");
      pkAttributes.add("progressiveREG04");

      String baseSQL = null;
      if (subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION) ||
          subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
          subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG04_SUBJECTS.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ('"+ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER+"')";
      }
      else {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,"+
          "REG04_SUBJECTS.SEX,REG04_SUBJECTS.MARITAL_STATUS,REG04_SUBJECTS.NATIONALITY,REG04_SUBJECTS.BIRTHDAY,REG04_SUBJECTS.BIRTHPLACE,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.MOBILE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG04_SUBJECTS.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ('"+ApplicationConsts.SUBJECT_PEOPLE_CONTACT+"','"+ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER+"')";
      }


      ArrayList values = new ArrayList();

      if (name_1REG04!=null && !name_1REG04.equals("")) {
        baseSQL += " and REG04_SUBJECTS.NAME_1=? ";
        values.add(name_1REG04);
      }
      if (name_2REG04!=null && !name_2REG04.equals("")) {
        baseSQL += " and REG04_SUBJECTS.NAME_2=? ";
        values.add(name_2REG04);
      }
      if (progressiveREG04!=null) {
        baseSQL += " and REG04_SUBJECTS.PROGRESSIVE=? ";
        values.add(progressiveREG04);
      }


      // read from REG04 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION) ||
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ?
            OrganizationVO.class:
            PeopleVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching a subject per name",ex);
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
  public VOListResponse updateReferences(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      ReferenceVO oldVO = null;
      ReferenceVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ReferenceVO)oldVOs.get(i);
        newVO = (ReferenceVO)newVOs.get(i);


        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01REG15");
        pkAttrs.add("progressiveReg04REG15");
        pkAttrs.add("progressiveREG15");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("companyCodeSys01REG15","COMPANY_CODE_SYS01");
        attr2dbFields.put("progressiveReg04REG15","PROGRESSIVE_REG04");
        attr2dbFields.put("progressiveREG15","PROGRESSIVE");
        attr2dbFields.put("referenceTypeREG15","REFERENCE_TYPE");
        attr2dbFields.put("valueREG15","VALUE");


        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG15_REFERENCES",
            attr2dbFields,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing references",ex);
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
  public void updateOrganization(OrganizationVO oldVO,OrganizationVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn); // use same transaction...
      orgBean.setConn(conn); // use same transaction...
      orgBean.update((OrganizationVO)oldVO,(OrganizationVO)newVO,t2,serverLanguageId,username);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while updating a subject", ex);
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
        peopleBean.setConn(null);
        orgBean.setConn(null);
      } catch (Exception ex) {}

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
  public void updatePeople(PeopleVO oldVO,PeopleVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn); // use same transaction...
      orgBean.setConn(conn); // use same transaction...
      peopleBean.update((PeopleVO)oldVO,(PeopleVO)newVO,t1,serverLanguageId,username);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while updating a subject", ex);
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
        peopleBean.setConn(null);
        orgBean.setConn(null);
      } catch (Exception ex) {}
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
  public VOListResponse updateSubjectHierarchies(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      SubjectHierarchyVO oldVO = null;
      SubjectHierarchyVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SubjectHierarchyVO)oldVOs.get(i);
        newVO = (SubjectHierarchyVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG08(),serverLanguageId,conn);

      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing subject hierarchies",ex);
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
  public VOListResponse updateSubjectHierarchyLevels(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      SubjectHierarchyLevelVO oldVO = null;
      SubjectHierarchyLevelVO newVO = null;
      Response res = null;
      stmt = conn.createStatement();

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SubjectHierarchyLevelVO)oldVOs.get(i);
        newVO = (SubjectHierarchyLevelVO)newVOs.get(i);

        // update/insert REG16 table...
        if (oldVO.getProgressiveHie01REG16()==null) {
          // insert...
          stmt.execute(
            "insert into REG16_SUBJECTS_LINKS(COMPANY_CODE_SYS01,PROGRESSIVE_REG04,PROGRESSIVE_HIE01,PROGRESSIVE_HIE02) "+
            "values('"+newVO.getCompanyCodeSys01REG16()+"',"+newVO.getProgressiveReg04REG16()+","+newVO.getProgressiveHie01REG16()+","+newVO.getProgressiveHie02REG16()+")"
          );
        }
        else {
          // update...
          stmt.execute(
            "update REG16_SUBJECTS_LINKS set PROGRESSIVE_HIE01="+newVO.getProgressiveHie01REG16()+" where "+
           "COMPANY_CODE_SYS01='"+newVO.getCompanyCodeSys01REG16()+"' and "+
           "PROGRESSIVE_REG04="+newVO.getProgressiveReg04REG16()+" and "+
           "PROGRESSIVE_HIE02="+newVO.getProgressiveHie02REG16()+" and "+
           "PROGRESSIVE_HIE01="+oldVO.getProgressiveHie01REG16()
          );
        }

      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing subject hierarchy levels",ex);
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
  public VOListResponse insertReferences(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ReferenceVO vo = null;


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG15","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG15","PROGRESSIVE");
      attribute2dbField.put("progressiveReg04REG15","PROGRESSIVE_REG04");
      attribute2dbField.put("referenceTypeREG15","REFERENCE_TYPE");
      attribute2dbField.put("valueREG15","VALUE");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (ReferenceVO)list.get(i);
        vo.setProgressiveREG15( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01REG15(),"REG15_REFERENCES","PROGRESSIVE",conn) );

        // insert into REG15...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG15_REFERENCES",
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
                   "executeCommand", "Error while inserting new references", ex);
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
  public VOResponse deleteSubjectHierarchy(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
     stmt = conn.createStatement();

      SubjectHierarchyVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (SubjectHierarchyVO)list.get(i);

        // phisically delete the record in SYS10...
        TranslationUtils.deleteTranslations(vo.getProgressiveSys10REG08(),conn);

        // phisically delete records in REG16...
        stmt.execute(
            "delete REG16_SUBJECT_LINKS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG08()+"' and "+
            "PROGRESSIVE_HIE01 in (SELECT PROGRESSIVE from HIE01_LEVELS where PROGRESSIVE_HIE02="+vo.getProgressiveHie02REG08()+")"
        );

        // phisically delete the record in REG08...
        stmt.execute(
            "delete REG08_SUBJECT_HIERARCHIES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG08()+"' and "+
            "SUBJECT_TYPE='"+vo.getSubjectTypeREG08()+"' and PROGRESSIVE_SYS10="+vo.getProgressiveSys10REG08()
        );

        // phisically delete records in HIE01...
        stmt.execute(
            "update HIE02_HIERARCHIES set PROGRESSIVE_HIE01=null where PROGRESSIVE="+vo.getProgressiveHie02REG08()
        );
        stmt.execute(
            "delete HIE01_LEVELS where PROGRESSIVE_HIE02="+vo.getProgressiveHie02REG08()
        );

        // phisically delete record in HIE02...
        stmt.execute(
            "delete HIE02_HIERARCHIES where PROGRESSIVE="+vo.getProgressiveHie02REG08()
        );

      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing subject hierarchy",ex);
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
  public VOResponse deleteSubjectsLinks(HierarSubjectsVO hsVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // delete records from REG16...
      pstmt = conn.prepareStatement("delete from REG16_SUBJECTS_LINKS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=? and PROGRESSIVE_HIE01=?");
      Subject vo = null;
      for(int i=0;i<hsVO.getSubjects().size();i++) {
        vo = (Subject)hsVO.getSubjects().get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01REG04());
        pstmt.setBigDecimal(2,vo.getProgressiveREG04());
        pstmt.setBigDecimal(3,hsVO.getProgressiveHie01REG16());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while deleting subjects links", ex);
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
  public VOResponse deleteReferences(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();

      ReferenceVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in REG15...
        vo = (ReferenceVO)list.get(i);
        stmt.execute(
            "delete from REG15_REFERENCES where "+
            "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG15()+"' and "+
            "PROGRESSIVE_REG04="+vo.getProgressiveReg04REG15()+" and "+
            "PROGRESSIVE="+vo.getProgressiveREG15()
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing references",ex);
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

