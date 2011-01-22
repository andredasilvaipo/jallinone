package org.jallinone.system.translations.server;

import java.sql.*;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Utility class used insert, update and delete record in SYS10 table.</p>
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
public class TranslationUtils {


//  /**
//   * Insert a record in SYS10 table for each language defined in SYS09.
//   * @param description description to insert
//   * @param conn database connection
//   * @return new progressive relative to SYS10
//   */
//  public static final BigDecimal insertTranslations(String description,Connection conn) throws Exception {
//    return insertTranslations(description,null,conn);
//  }

  /**
   * Insert a record in SYS10 table for each language defined in SYS09.
   * @param description description to insert
   * @param companyCodeSys01 (optional) company code of the table having the PROGRESSIVE_SYS10 field
   * @param conn database connection
   * @return new progressive relative to SYS10
   */
  public static final BigDecimal insertTranslations(String description,String companyCodeSys01,Connection conn) throws Exception {
    BigDecimal progressive = CompanyProgressiveUtils.getInternalProgressive(companyCodeSys01,"SYS10_TRANSLATIONS","PROGRESSIVE",conn);
    PreparedStatement pstmt = null;
    try {
			pstmt = conn.prepareStatement(
				"insert into SYS10_TRANSLATIONS(PROGRESSIVE,LANGUAGE_CODE,DESCRIPTION,COMPANY_CODE_SYS01) "+
				"select "+progressive+",LANGUAGE_CODE,?,? FROM SYS09_LANGUAGES where ENABLED='Y'"
			);
		  pstmt.setString(1,description);
			pstmt.setString(2,companyCodeSys01);
			pstmt.execute();
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
    }
    return progressive;
  }


  /**
   * Update a record in SYS10 table for for the specified language.
   * @param oldDescription old description value
   * @param description description to insert
   * @param progressive progressive that identifies the record in SYS10
   * @param languageCode server language identifier
   * @param conn database connection
   */
  public static final void updateTranslation(String oldDescription,String description,BigDecimal progressive,String languageCode,Connection conn) throws Exception {
		PreparedStatement pstmt = null;
    try {
			String sql =
				"update SYS10_TRANSLATIONS set DESCRIPTION=? where "+
		  	"PROGRESSIVE="+progressive+" and LANGUAGE_CODE=? and DESCRIPTION=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,description);
			pstmt.setString(2,languageCode);
			pstmt.setString(3,oldDescription);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows==0) {
			Logger.error("NONAME","org.jallinone.system.translations.server.TranslationUtils","updateTranslation",sql+"\n\nUpdate not allowed: description already updated by another process",null);
        throw new Exception("Update not allowed: description already updated by another process");
      }
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * Delete a record in SYS10 table for each language defined in SYS09.
   * @param progressive progressive that identifies the record in SYS10
   * @param conn database connection
   */
  public static final void deleteTranslations(BigDecimal progressive,Connection conn) throws Exception {
		PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(
        "delete from SYS10_TRANSLATIONS where PROGRESSIVE="+progressive
			);
			pstmt.execute();
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
    }
  }


}
