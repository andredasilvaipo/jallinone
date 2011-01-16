package org.jallinone.system.languages.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.languages.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage languages.</p>
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
public class LanguagesBean  implements Languages {


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




  public LanguagesBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOListResponse loadLanguages(String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select LANGUAGE_CODE,DESCRIPTION,CLIENT_LANGUAGE_CODE from SYS09_LANGUAGES where ENABLED='Y'"
      );
      LanguageVO vo = null;
      ArrayList list = new ArrayList();
      while(rset.next()) {
        vo = new LanguageVO();
        vo.setLanguageCodeSYS09(rset.getString(1));
        vo.setDescriptionSYS09(rset.getString(2));
        vo.setClientLanguageCodeSYS09(rset.getString(3));
        list.add(vo);
      }

      rset.close();
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"loadLanguages","Error while fetching languages list",ex);
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
  public VOListResponse insertLanguages(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer("");
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;



      LanguageVO vo = null;
      BufferedReader br = null;
      ResultSet rset = null;

      pstmt = conn.prepareStatement(
          "insert into SYS09_LANGUAGES(LANGUAGE_CODE,DESCRIPTION,CLIENT_LANGUAGE_CODE,CREATE_DATE,ENABLED) VALUES(?,?,?,?,'Y')"
      );

   		 int index = -1;
		   StringBuffer unicode = new StringBuffer();
      for(int i=0;i<list.size();i++) {
        vo = (LanguageVO)list.get(i);

        // insert record in SYS01...
        pstmt.setString(1,vo.getLanguageCodeSYS09());
        pstmt.setString(2,vo.getDescriptionSYS09());
        pstmt.setString(3,vo.getClientLanguageCodeSYS09());
        pstmt.setDate(4,new java.sql.Date(System.currentTimeMillis()));
        pstmt.execute();

        // insert translations in SYS10...
        stmt = conn.createStatement();
        sql.delete(0,sql.length());

        String fileName = null;
        String aux = "";

        for(int k=1;k<=ApplicationConsts.DB_VERSION;k++) {
          if (k>1)
            aux = String.valueOf(k);

          fileName = "inssql"+aux+"_"+vo.getClientLanguageCodeSYS09()+".ini";
          InputStream in = null;
          try {
            in = this.getClass().getResourceAsStream("/" + fileName);
          }
          catch (Exception ex5) {
          }
          if (in==null)
            in = new FileInputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ") + fileName);

          br = new BufferedReader(new InputStreamReader(in));
          String line = null;
          while ( (line = br.readLine()) != null) {
            sql.append(' ').append(line);
            if (line.endsWith(";")) {
              if (sql.indexOf(":LANGUAGE_CODE") != -1) {
                sql = replace(sql, ":LANGUAGE_CODE","'" + vo.getLanguageCodeSYS09() + "'");
              }
              if (sql.toString().trim().length()>0) {

								// check for unicode chars...
								while((index=sql.indexOf("\\u"))!=-1) {
									for(int j=index+2;j<Math.min(sql.length(),index+2+6);j++)
										if (Character.isDigit(sql.charAt(j)) ||
											sql.charAt(j)=='A' ||
											sql.charAt(j)=='B' ||
											sql.charAt(j)=='C' ||
											sql.charAt(j)=='D' ||
											sql.charAt(j)=='E' ||
											sql.charAt(j)=='F')
											unicode.append(sql.charAt(j));
										else
											break;
									if (unicode.length()>0) {
										sql.delete(index, index+1+unicode.length());
										sql.setCharAt(index, new Character((char)Integer.valueOf(unicode.toString(),16).intValue()).charValue());
										unicode.delete(0, unicode.length());
									}
								}

                stmt.execute(sql.toString().substring(0,sql.length() - 1));
              }
              sql.delete(0, sql.length());
            }

          }
          br.close();
        }


        // insert all other translations...
        rset = stmt.executeQuery("select LANGUAGE_CODE from SYS09_LANGUAGES where ENABLED='Y' order by CREATE_DATE ASC");
        String oldLangCode = null;
        if (rset.next())
          oldLangCode = rset.getString(1);
        rset.close();
        if (oldLangCode!=null)
          stmt.execute(
            "insert into SYS10_TRANSLATIONS(PROGRESSIVE,DESCRIPTION,LANGUAGE_CODE) " +
            "select PROGRESSIVE,DESCRIPTION,'" + vo.getLanguageCodeSYS09() +
            "' from SYS10_TRANSLATIONS where LANGUAGE_CODE='"+oldLangCode+"' and not PROGRESSIVE in "+
            "(select PROGRESSIVE from SYS10_TRANSLATIONS where LANGUAGE_CODE='"+vo.getLanguageCodeSYS09()+"')"
          );
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new languages:\n"+sql.toString(), ex);
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
    }

  }



  /**
   * Replace the specified pattern with the new one.
   * @param b sql script
   * @param oldPattern pattern to replace
   * @param newPattern new pattern
   * @return sql script with substitutions
   */
  private StringBuffer replace(StringBuffer b,String oldPattern,String newPattern) {
    int i = -1;
    while((i=b.indexOf(oldPattern))!=-1) {
      b.replace(i,i+oldPattern.length(),newPattern);

      try {
      } catch (Exception ex) {}
    }
    return b;
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse validateLanguageCode(LookupValidationParams validationPars,String serverLanguageId,String username) throws Throwable {


    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;






      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select LANGUAGE_CODE,DESCRIPTION,CLIENT_LANGUAGE_CODE from SYS09_LANGUAGES where ENABLED='Y' and LANGUAGE_CODE='"+validationPars.getCode()+"'"
      );
      LanguageVO vo = null;
      ArrayList list = new ArrayList();
      while(rset.next()) {
        vo = new LanguageVO();
        vo.setLanguageCodeSYS09(rset.getString(1));
        vo.setDescriptionSYS09(rset.getString(2));
        vo.setClientLanguageCodeSYS09(rset.getString(3));
        list.add(vo);
      }

      rset.close();
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating language code",ex);
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
  public VOListResponse updateLanguages(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      LanguageVO oldVO = null;
      LanguageVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (LanguageVO)oldVOs.get(i);
        newVO = (LanguageVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("languageCodeSYS09");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("languageCodeSYS09","LANGUAGE_CODE");
        attr2dbFields.put("descriptionSYS09","DESCRIPTION");

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SYS09_LANGUAGES",
            attr2dbFields,
            "Y",
            "N",
            null,
            false
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing languages",ex);
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
  public VOResponse deleteLanguage(LanguageVO vo,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();


      // logically delete the record in SYS09...
      stmt.execute("update SYS09_LANGUAGES set ENABLED='N' where LANGUAGE_CODE='"+vo.getLanguageCodeSYS09()+"'");

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing language",ex);
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
      } catch (Exception ex) {}
    }

  }



}

