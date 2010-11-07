package org.jallinone.system.translations.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;

import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.languages.java.*;
import org.jallinone.system.translations.java.*;
import org.openswing.swing.customvo.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage translations from SYS10.</p>
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
public class TranslationsBean  implements Translations {


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




  public TranslationsBean() {}



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public CustomValueObject getCustomValueObject() {
	  throw new UnsupportedOperationException();
  }
  
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadTranslations(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      TopicVO topic = (TopicVO)gridParams.getOtherGridParams().get(ApplicationConsts.TOPIC);
      LanguageVO[] langs = topic.getLangs();

      String select = "select ";
      String from = " from ";
      if (topic.getTableName()!=null)
        from += topic.getTableName()+",";
      String where = " where ";

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("attributeNameN0","T0.PROGRESSIVE");

      LanguageVO vo = null;
      for(int i=0;i<langs.length;i++) {
        vo = (LanguageVO)langs[i];

        attribute2dbField.put("attributeNameS"+i,"T"+i+".DESCRIPTION");

        select += "T"+i+".DESCRIPTION,";
        from += "SYS10_TRANSLATIONS T"+i+",";
        where +=
            "not T"+i+".DESCRIPTION='"+ApplicationConsts.JOLLY+"' and "+
            "T"+i+".LANGUAGE_CODE='"+vo.getLanguageCodeSYS09()+"' and ";
        if (topic.getTableName()!=null)
          where += "T"+i+".PROGRESSIVE="+topic.getTableName()+"."+topic.getColumnName()+" and ";
        else if (langs.length>1 && i>0)
          where += "T0.PROGRESSIVE=T"+i+".PROGRESSIVE and ";
        if (topic.isUseCompanyCode())
          where += topic.getTableName()+".COMPANY_CODE_SYS01='"+companyCodeSys01+"' and ";
        if (topic.isUseEnabled())
          where += topic.getTableName()+".ENABLED='Y' and ";
      }
      select = select.substring(0,select.length()-1); // remove last comma...
      from = from.substring(0,from.length()-1); // remove last comma...
      where = where.substring(0,where.length()-4); // remove last AND...

      String sql = select+",T0.PROGRESSIVE "+from+where+" order by T0.PROGRESSIVE ";


      // read from SYS10 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          new ArrayList(),
          attribute2dbField,
          CustomValueObject.class,
          "Y",
          "N",
          null,
          gridParams,
          30,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching translations",ex);
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
  public VOListResponse updateTranslations(TopicVO topic,ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      LanguageVO[] langs = topic.getLangs();
      CustomValueObject oldVO = null;
      CustomValueObject newVO = null;
      Class clazz = CustomValueObject.class;
      Response res = null;
      LanguageVO vo = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CustomValueObject)oldVOs.get(i);
        newVO = (CustomValueObject)newVOs.get(i);

        // update SYS10 tables...
        for(int k=0;k<langs.length;k++) {
          vo = (LanguageVO)langs[k];
          TranslationUtils.updateTranslation(
            (String)clazz.getMethod("getAttributeNameS"+k,new Class[0]).invoke(oldVO,new Object[0]),
            (String)clazz.getMethod("getAttributeNameS"+k,new Class[0]).invoke(newVO,new Object[0]),
            newVO.getAttributeNameN0(),
            vo.getLanguageCodeSYS09(),
            conn
          );
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing variants",ex);
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




}

