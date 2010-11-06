package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.subjects.java.PeopleVO;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to insert/update/delete people in/from REG04 table.</p>
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
public class PeopleBean {


  public PeopleBean() {
  }


  /**
   * Check if there exist a person with the same first nane + last name
   */
  public final void checkPeopleExist(Connection conn,PeopleVO vo,UserSessionParameters userSessionPars,ServletContext context) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

      String sql = "select NAME_1,NAME_2 from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and UPPER(NAME_1)=? and UPPER(NAME_2)=? and ENABLED='Y' ";
      if (vo.getProgressiveREG04()!=null)
        sql += " and not PROGRESSIVE="+vo.getProgressiveREG04();

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getName_1REG04()==null?null:vo.getName_1REG04().toUpperCase());
      pstmt.setString(3,vo.getName_2REG04()==null?null:vo.getName_2REG04().toUpperCase());
      rset = pstmt.executeQuery();
      if (rset.next())
        throw new Exception(factory.getResources(serverLanguageId).getResource("there is already another people with the same first and last name."));
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }


  /**
   * (Optionally) generate progressive and insert record.
   */
  public final void insert(Connection conn,boolean generateProgressive,PeopleVO vo,UserSessionParameters userSessionPars,ServletContext context) throws Throwable {
    if (vo.getProgressiveREG04()==null)
      checkPeopleExist(conn,vo,userSessionPars,context);

    PreparedStatement pstmt = null;
    try {
      // check if there already exists a progressive (a contact...)
      if (vo.getProgressiveREG04()!=null) {
        // update subject type in REG04...
        pstmt = conn.prepareStatement(
          "update REG04_SUBJECTS set SUBJECT_TYPE=? where COMPANY_CODE_SYS01=? and PROGRESSIVE=? "
        );
        pstmt.setString(1,vo.getSubjectTypeREG04());
        pstmt.setString(2,vo.getCompanyCodeSys01REG04());
        pstmt.setBigDecimal(3,vo.getProgressiveREG04());
        pstmt.execute();
        return;
      }

      if (generateProgressive) {
        vo.setProgressiveREG04( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01REG04(),"REG04_SUBJECTS","PROGRESSIVE",conn) );
      }

      // insert record in REG04...
      pstmt = conn.prepareStatement(
          "insert into REG04_SUBJECTS(COMPANY_CODE_SYS01,NAME_1,NAME_2,ADDRESS,CITY,ZIP,PROVINCE,COUNTRY,TAX_CODE,PHONE_NUMBER,"+
          "FAX_NUMBER,EMAIL_ADDRESS,WEB_SITE,MOBILE_NUMBER,NOTE,ENABLED,SUBJECT_TYPE,PROGRESSIVE,SEX,MARITAL_STATUS,BIRTHDAY,"+
          "BIRTHPLACE,NATIONALITY,COMPANY_CODE_SYS01_REG04,PROGRESSIVE_REG04) VALUES("+
          "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?,?,?,?,?,?)"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getName_1REG04());
      pstmt.setString(3,vo.getName_2REG04());
      pstmt.setString(4,vo.getAddressREG04());
      pstmt.setString(5,vo.getCityREG04());
      pstmt.setString(6,vo.getZipREG04());
      pstmt.setString(7,vo.getProvinceREG04());
      pstmt.setString(8,vo.getCountryREG04());
      pstmt.setString(9,vo.getTaxCodeREG04());
      pstmt.setString(10,vo.getPhoneNumberREG04());
      pstmt.setString(11,vo.getFaxNumberREG04());
      pstmt.setString(12,vo.getEmailAddressREG04());
      pstmt.setString(13,vo.getWebSiteREG04());
      pstmt.setString(14,vo.getMobileNumberREG04());
      pstmt.setString(15,vo.getNoteREG04());
      pstmt.setString(16,vo.getSubjectTypeREG04());
      pstmt.setBigDecimal(17,vo.getProgressiveREG04());

      pstmt.setString(18,vo.getSexREG04());
      pstmt.setString(19,vo.getMaritalStatusREG04());
      pstmt.setDate(20,vo.getBirthdayREG04());
      pstmt.setString(21,vo.getBirthplaceREG04());
      pstmt.setString(22,vo.getNationalityREG04());
      pstmt.setString(23,vo.getCompanyCodeSys01Reg04REG04());
      pstmt.setBigDecimal(24,vo.getProgressiveReg04REG04());

      pstmt.execute();
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }


  /**
   * Update record.
   */
  public final Response update(Connection conn,PeopleVO oldVO,PeopleVO newVO,UserSessionParameters userSessionPars,ServletContext context) throws Throwable  {
    checkPeopleExist(conn,newVO,userSessionPars,context);

    HashSet pkAttrs = new HashSet();
    pkAttrs.add("companyCodeSys01REG04");
    pkAttrs.add("progressiveREG04");

    HashMap attr2dbFields = new HashMap();
    attr2dbFields.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
    attr2dbFields.put("progressiveREG04","PROGRESSIVE");
    attr2dbFields.put("name_1REG04","NAME_1");
    attr2dbFields.put("name_2REG04","NAME_2");
    attr2dbFields.put("addressREG04","ADDRESS");
    attr2dbFields.put("cityREG04","CITY");
    attr2dbFields.put("zipREG04","ZIP");
    attr2dbFields.put("provinceREG04","PROVINCE");
    attr2dbFields.put("countryREG04","COUNTRY");
    attr2dbFields.put("taxCodeREG04","TAX_CODE");
    attr2dbFields.put("phoneNumberREG04","PHONE_NUMBER");
    attr2dbFields.put("faxNumberREG04","FAX_NUMBER");
    attr2dbFields.put("emailAddressREG04","EMAIL_ADDRESS");
    attr2dbFields.put("webSiteREG04","WEB_SITE");
    attr2dbFields.put("mobileNumberREG04","MOBILE_NUMBER");
    attr2dbFields.put("noteREG04","NOTE");
    attr2dbFields.put("sexREG04","SEX");
    attr2dbFields.put("maritalStatusREG04","MARITAL_STATUS");
    attr2dbFields.put("birthdayREG04","BIRTHDAY");
    attr2dbFields.put("birthplaceREG04","BIRTHPLACE");
    attr2dbFields.put("nationalityREG04","NATIONALITY");
    attr2dbFields.put("companyCodeSys01Reg04REG04","COMPANY_CODE_SYS01_REG04");
    attr2dbFields.put("progressiveReg04REG04","PROGRESSIVE_REG04");

    return new QueryUtil().updateTable(
        conn,
        userSessionPars,
        pkAttrs,
        oldVO,
        newVO,
        "REG04_SUBJECTS",
        attr2dbFields,
        "Y",
        "N",
        context,
        true
    );

  }





}
