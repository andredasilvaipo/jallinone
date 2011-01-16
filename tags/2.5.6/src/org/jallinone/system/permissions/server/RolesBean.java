package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.permissions.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage roles.</p>
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
public class RolesBean  implements Roles {


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




  public RolesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public UserRoleVO getUserRole() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public GridPermissionsPerRoleVO getGridPermissionsPerRole() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public RoleFunctionCompanyVO getRoleFunctionCompany() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public RoleFunctionVO getRoleFunction() {
	  throw new UnsupportedOperationException();
  }

  
  /**
   * Business logic to execute.
   */
  public VOListResponse loadRoles(String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
     if (this.conn==null) conn = getConn(); else conn = this.conn;

     stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select SYS04_ROLES.PROGRESSIVE,SYS04_ROLES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS14_USER_ROLES,SYS04_ROLES,SYS10_TRANSLATIONS where "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS14_USER_ROLES.USERNAME_SYS03='"+username+"' and "+
          "SYS04_ROLES.ENABLED='Y'"
      );
      RoleVO vo = null;
      ArrayList list = new ArrayList();
      while(rset.next()) {
        vo = new RoleVO();
        vo.setDescriptionSYS10(rset.getString(3));
        vo.setEnabledSYS04("Y");
        vo.setProgressiveSYS04(rset.getBigDecimal(1));
        vo.setProgressiveSys10SYS04(rset.getBigDecimal(2));
        list.add(vo);
      }

      rset.close();
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching user roles list",ex);
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
  public VOListResponse loadUserRoles(GridParams params,String langId,String username,HashMap userRoles) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve all (current) user roles...
      String roles = "";
      Iterator it = userRoles.keySet().iterator();
      while(it.hasNext()) {
        roles += it.next()+",";
      }
      if (roles.length()>0)
        roles = roles.substring(0,roles.length()-1);

      UserRoleVO vo = null;
      ArrayList list = new ArrayList();
      Hashtable rolesSet = new Hashtable();
      pstmt = conn.prepareStatement(
          "select SYS14_USER_ROLES.PROGRESSIVE_SYS04,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS04_ROLES where "+
          "SYS04_ROLES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS04_ROLES.PROGRESSIVE in ("+roles+") and "+
          "SYS14_USER_ROLES.USERNAME_SYS03=?"
      );
      pstmt.setString(1,langId);
      pstmt.setString(2,username);
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = new UserRoleVO();
        vo.setDescriptionSYS10(rset.getString(2));
        vo.setProgressiveSys04SYS14(rset.getBigDecimal(1));
        vo.setUsernameSys03SYS14((String)params.getOtherGridParams().get(ApplicationConsts.USERNAME_SYS03));
        vo.setSelected(Boolean.FALSE);
        list.add(vo);
        rolesSet.put(rset.getBigDecimal(1),new Integer(list.size()-1));
      }
      rset.close();
      pstmt.close();

      // retrieve roles associated to the specified user that are ALSO roles of the current user...
      pstmt = conn.prepareStatement(
          "select SYS14_USER_ROLES.PROGRESSIVE_SYS04,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS04_ROLES where "+
          "SYS04_ROLES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS04_ROLES.PROGRESSIVE in ("+roles+") and "+
          "SYS14_USER_ROLES.USERNAME_SYS03=?"
      );
      pstmt.setString(1,langId);
      pstmt.setString(2,(String)params.getOtherGridParams().get(ApplicationConsts.USERNAME_SYS03));
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (UserRoleVO)list.get(((Integer)rolesSet.get(rset.getBigDecimal(1))).intValue());
        vo.setSelected(Boolean.TRUE);
      }
      rset.close();

      return new VOListResponse(list,false,list.size());
    } catch (Exception ex1) {
      ex1.printStackTrace();
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }

    }

  }



  /**
   * Business logic to execute.
   */
  public VOListResponse updateGridPermissionsPerRole(String functionCodeSYS06,BigDecimal progressiveSYS04,ArrayList vos,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement("delete from SYS27_GRID_PERMISSIONS where FUNCTION_CODE_SYS06=? and PROGRESSIVE_SYS04=?");
      pstmt.setString(1,functionCodeSYS06);
      pstmt.setBigDecimal(2,progressiveSYS04);
      pstmt.execute();
      pstmt.close();
      pstmt = conn.prepareStatement("insert into SYS27_GRID_PERMISSIONS(COLS_POS,EDIT_COLS_IN_INS,EDIT_COLS_IN_EDIT,REQUIRED_COLS,COLS_VIS,FUNCTION_CODE_SYS06,PROGRESSIVE_SYS04) values(?,?,?,?,?,?,?)");
      GridPermissionsPerRoleVO vo = null;
      String colsPos = "";
      String editColsInIns = "";
      String editColsInEdit = "";
      String colsReq = "";
      String colsVis = "";
      for(int i=0;i<vos.size();i++) {
        vo = (GridPermissionsPerRoleVO)vos.get(i);
        colsPos += vo.getColumnName()+",";
        editColsInIns += (vo.isEditableInIns()?"true":"false")+",";
        editColsInEdit += (vo.isEditableInEdit()?"true":"false")+",";
        colsReq += (vo.isRequired()?"true":"false")+",";
        colsVis += (vo.isVisible()?"true":"false")+",";
      }
      colsPos = colsPos.substring(0,colsPos.length()-1);
      editColsInIns = editColsInIns.substring(0,editColsInIns.length()-1);
      editColsInEdit = editColsInEdit.substring(0,editColsInEdit.length()-1);
      colsReq = colsReq.substring(0,colsReq.length()-1);
      colsVis = colsVis.substring(0,colsVis.length()-1);

      pstmt.setString(1,colsPos);
      pstmt.setString(2,editColsInIns);
      pstmt.setString(3,editColsInEdit);
      pstmt.setString(4,colsReq);
      pstmt.setString(5,colsVis);
      pstmt.setString(6,functionCodeSYS06);
      pstmt.setBigDecimal(7,progressiveSYS04);
      pstmt.execute();
      
      return new VOListResponse(vos,false,vos.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating columns permission",ex);
      try {
        pstmt.close();
      }
      catch (Exception exx) {
      }
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
  public VOListResponse updateRoleFunctionCompanies(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      RoleFunctionCompanyVO oldVO = null;
      RoleFunctionCompanyVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (RoleFunctionCompanyVO)oldVOs.get(i);
        newVO = (RoleFunctionCompanyVO)newVOs.get(i);

        if (!oldVO.getCanView().booleanValue()) {
          // no record in SYS02 yet...
          if (newVO.getCanView().booleanValue()) {
            pstmt = conn.prepareStatement(
              "insert into SYS02_COMPANIES_ACCESS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01) "+
              "values(?,?,?,?,?,?)"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS02());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS02());
            pstmt.setString(3,newVO.getCanInsSYS02().booleanValue()?"Y":"N");
            pstmt.setString(4,newVO.getCanUpdSYS02().booleanValue()?"Y":"N");
            pstmt.setString(5,newVO.getCanDelSYS02().booleanValue()?"Y":"N");
            pstmt.setString(6,newVO.getCompanyCodeSys01SYS02());
            pstmt.execute();
          }
        }
        else {
          // record already exists in SYS02...
          if (newVO.getCanView().booleanValue()) {
            // record in SYS02 will be updated...
            pstmt = conn.prepareStatement(
              "update SYS02_COMPANIES_ACCESS set CAN_INS=?,CAN_UPD=?,CAN_DEL=? where "+
              "PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=? and COMPANY_CODE_SYS01=? "
            );
            pstmt.setString(1,newVO.getCanInsSYS02().booleanValue()?"Y":"N");
            pstmt.setString(2,newVO.getCanUpdSYS02().booleanValue()?"Y":"N");
            pstmt.setString(3,newVO.getCanDelSYS02().booleanValue()?"Y":"N");
            pstmt.setBigDecimal(4,newVO.getProgressiveSys04SYS02());
            pstmt.setString(5,newVO.getFunctionCodeSys06SYS02());
            pstmt.setString(6,newVO.getCompanyCodeSys01SYS02());
            pstmt.execute();
          }
          else {
            // delete record from SYS02...
            pstmt = conn.prepareStatement(
              "delete from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=? and COMPANY_CODE_SYS01=?"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS02());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS02());
            pstmt.setString(3,newVO.getCompanyCodeSys01SYS02());
            pstmt.execute();
          }
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating company-role-function settings",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
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
  public VOListResponse updateRoleFunctions(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      RoleFunctionVO oldVO = null;
      RoleFunctionVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (RoleFunctionVO)oldVOs.get(i);
        newVO = (RoleFunctionVO)newVOs.get(i);

        if (!oldVO.getCanView().booleanValue()) {
          // no record in SYS07 yet...
          if (newVO.getCanView().booleanValue()) {
            pstmt = conn.prepareStatement(
              "insert into SYS07_ROLE_FUNCTIONS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) values(?,?,?,?,?)"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
            pstmt.setString(3,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
            pstmt.setString(4,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
            pstmt.setString(5,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
            pstmt.execute();

            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // insert also records in SYS02...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "insert into SYS02_COMPANIES_ACCESS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01) "+
                "select ?,?,?,?,?,COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y'"
              );
              pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
              pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
              pstmt.setString(3,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
              pstmt.setString(4,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
              pstmt.setString(5,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
              pstmt.execute();
            }

          }
        }
        else {
          // record already exists in SYS07...
          if (newVO.getCanView().booleanValue()) {
            // record in SYS07 will be updated...
            pstmt = conn.prepareStatement(
              "update SYS07_ROLE_FUNCTIONS set CAN_INS=?,CAN_UPD=?,CAN_DEL=? where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
            );
            pstmt.setString(1,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
            pstmt.setString(2,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
            pstmt.setString(3,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
            pstmt.setBigDecimal(4,newVO.getProgressiveSys04SYS07());
            pstmt.setString(5,newVO.getFunctionCodeSys06SYS07());
            pstmt.execute();

            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // update records in SYS02 too...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "update SYS02_COMPANIES_ACCESS set CAN_INS=?,CAN_UPD=?,CAN_DEL=? where "+
                "PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=? and COMPANY_CODE_SYS01 in "+
                "(select COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y')"
              );
              pstmt.setString(1,newVO.getCanInsSYS07().booleanValue()?"Y":"N");
              pstmt.setString(2,newVO.getCanUpdSYS07().booleanValue()?"Y":"N");
              pstmt.setString(3,newVO.getCanDelSYS07().booleanValue()?"Y":"N");
              pstmt.setBigDecimal(4,newVO.getProgressiveSys04SYS07());
              pstmt.setString(5,newVO.getFunctionCodeSys06SYS07());
              pstmt.execute();
            }

          }
          else {
            if (newVO.getUseCompanyCodeSYS06().booleanValue()) {
              // delete records from SYS02...
              pstmt.close();
              pstmt = conn.prepareStatement(
                "delete from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
              );
              pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
              pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
              pstmt.execute();
            }

            // record will be deleted from SYS07...
            pstmt = conn.prepareStatement(
              "delete from SYS07_ROLE_FUNCTIONS where PROGRESSIVE_SYS04=? and FUNCTION_CODE_SYS06=?"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS07());
            pstmt.setString(2,newVO.getFunctionCodeSys06SYS07());
            pstmt.execute();
          }
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating role-functions settings",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
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
  public VOListResponse updateRoles(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      RoleVO oldVO = null;
      RoleVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (RoleVO)oldVOs.get(i);
        newVO = (RoleVO)newVOs.get(i);

        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10SYS04(),serverLanguageId,conn);
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing role descriptions",ex);
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
  public VOListResponse updateUserRoles(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      UserRoleVO oldVO = null;
      UserRoleVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (UserRoleVO)oldVOs.get(i);
        newVO = (UserRoleVO)newVOs.get(i);

        if (!oldVO.getSelected().booleanValue()) {
          // no record in SYS14 yet...
          if (newVO.getSelected().booleanValue()) {
            pstmt = conn.prepareStatement(
              "insert into SYS14_USER_ROLES(PROGRESSIVE_SYS04,USERNAME_SYS03) values(?,?)"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS14());
            pstmt.setString(2,newVO.getUsernameSys03SYS14());
            pstmt.execute();
          }
        }
        else {
          // record already exists in SYS14...
          if (!newVO.getSelected().booleanValue()) {
            // delete records from SYS14...
            pstmt = conn.prepareStatement(
              "delete from SYS14_USER_ROLES where PROGRESSIVE_SYS04=? and USERNAME_SYS03=?"
            );
            pstmt.setBigDecimal(1,newVO.getProgressiveSys04SYS14());
            pstmt.setString(2,newVO.getUsernameSys03SYS14());
            pstmt.execute();
          }
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating roles associated to the specified user",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
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
  public VOListResponse insertRoles(ArrayList list,String serverLanguageId,String username,String defCompanyCodeSys01SYS03) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      RoleVO vo = null;

      BigDecimal oldProgressiveSYS04 = null;
      BigDecimal progressiveSYS04 = null;
      BigDecimal progressiveSys10SYS04 = null;

      for(int i=0;i<list.size();i++) {
        vo = (RoleVO)list.get(i);

        // used for copy operation...
        oldProgressiveSYS04 = vo.getProgressiveSYS04();

        // generate new progressive for SYS04...
        progressiveSYS04 = CompanyProgressiveUtils.getInternalProgressive(defCompanyCodeSys01SYS03,"SYS04_ROLES","PROGRESSIVE",conn);
        vo.setProgressiveSYS04(progressiveSYS04);

        // insert record in SYS10...
        progressiveSys10SYS04 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,conn);
        vo.setProgressiveSys10SYS04(progressiveSys10SYS04);

        // insert record in SYS04...
        pstmt = conn.prepareStatement(
            "insert into SYS04_ROLES(PROGRESSIVE,PROGRESSIVE_SYS10,ENABLED) VALUES(?,?,'Y')"
        );
        pstmt.setBigDecimal(1,vo.getProgressiveSYS04());
        pstmt.setBigDecimal(2,vo.getProgressiveSys10SYS04());
        pstmt.execute();
        pstmt.close();

        // link the new role to the current user...
        pstmt = conn.prepareStatement(
            "insert into SYS14_USER_ROLES(USERNAME_SYS03,PROGRESSIVE_SYS04) VALUES(?,?)"
        );
        pstmt.setString(1,username);
        pstmt.setBigDecimal(2,progressiveSYS04);
        pstmt.execute();

        if (!username.toUpperCase().equals("ADMIN")) {
          pstmt.close();
          // link the new role to the ADMIN user...
          pstmt = conn.prepareStatement(
              "insert into SYS14_USER_ROLES(USERNAME_SYS03,PROGRESSIVE_SYS04) VALUES('ADMIN',?)"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.execute();
        }

        if (oldProgressiveSYS04!=null) {
          // duplicate all old progressive settings...
          pstmt.close();
          pstmt = conn.prepareStatement(
              "insert into SYS07_ROLE_FUNCTIONS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) "+
              "select ?,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL from SYS07_ROLE_FUNCTIONS where "+
              "PROGRESSIVE_SYS04=?"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.setBigDecimal(2,oldProgressiveSYS04);
          pstmt.execute();
          pstmt.close();

          pstmt = conn.prepareStatement(
              "insert into SYS02_COMPANIES_ACCESS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01) "+
              "select ?,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01 from SYS02_COMPANIES_ACCESS where "+
              "PROGRESSIVE_SYS04=?"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.setBigDecimal(2,oldProgressiveSYS04);
          pstmt.execute();
          pstmt.close();

        }
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting new roles", ex);
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
  public VOListResponse loadGridPermissionsPerRole(Properties p,GridParams gridParams,String langId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      BigDecimal progressiveSYS04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      String functionCodeSYS06 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06);

      String sql =
          "select SYS26_GRID_PERMISSIONS_DEFS.COLS_POS,SYS26_GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_INS,"+
          "SYS26_GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_EDIT,SYS26_GRID_PERMISSIONS_DEFS.REQUIRED_COLS,"+
          "SYS26_GRID_PERMISSIONS_DEFS.COLS_VIS,SYS26_GRID_PERMISSIONS_DEFS.COLS_NAME "+
          "from SYS26_GRID_PERMISSIONS_DEFS "+
          "where SYS26_GRID_PERMISSIONS_DEFS.FUNCTION_CODE_SYS06=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionCodeSYS06);
      rset = pstmt.executeQuery();
      ArrayList rows = new ArrayList();
      String[] colsPos = null;
      String[] editColsInIns = null;
      String[] editColsInEdit = null;
      String[] colsReq = null;
      String[] colsVis = null;
      String[] colsHeader = null;
      Hashtable cols = new Hashtable(); // collection of pairs: <attribute name,GridPermissionsPerRoleVO object>
      GridPermissionsPerRoleVO vo = null;
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");
        colsHeader = rset.getString(6).split(",");
        for(int i=0;i<colsPos.length;i++) {
          vo = new GridPermissionsPerRoleVO();
          vo.setColumnName(colsPos[i]);
          vo.setDescription(p.getProperty(colsHeader[i]));
          vo.setDefaultEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setDefaultEditableInIns(editColsInIns[i].equals("true"));
          vo.setDefaultRequired(colsReq[i].equals("true"));
          vo.setDefaultVisible(colsVis[i].equals("true"));
          vo.setEditableInEdit(vo.isDefaultEditableInEdit());
          vo.setEditableInIns(vo.isDefaultEditableInIns());
          vo.setRequired(vo.isDefaultRequired());
          vo.setVisible(vo.isDefaultVisible());
          rows.add(vo);
          cols.put(vo.getColumnName(),vo);
        }
      }
      if (rows.size()==0)
        return new VOListResponse(new ArrayList(),false,0);

      rset.close();
      pstmt.close();
      sql =
          "select SYS27_GRID_PERMISSIONS.COLS_POS,SYS27_GRID_PERMISSIONS.EDIT_COLS_IN_INS,"+
          "SYS27_GRID_PERMISSIONS.EDIT_COLS_IN_EDIT,SYS27_GRID_PERMISSIONS.REQUIRED_COLS,"+
          "SYS27_GRID_PERMISSIONS.COLS_VIS "+
          "from SYS27_GRID_PERMISSIONS "+
          "where SYS27_GRID_PERMISSIONS.FUNCTION_CODE_SYS06=? and SYS27_GRID_PERMISSIONS.PROGRESSIVE_SYS04=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionCodeSYS06);
      pstmt.setBigDecimal(2,progressiveSYS04);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");

        for(int i=0;i<colsPos.length;i++) {
          vo.setColumnName(colsPos[i]);
          vo = (GridPermissionsPerRoleVO)cols.get(vo.getColumnName());
          //vo.setDescription("");
          vo.setEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setEditableInIns(editColsInIns[i].equals("true"));
          vo.setRequired(colsReq[i].equals("true"));
          vo.setVisible(colsVis[i].equals("true"));
        }
      }
      VOListResponse answer = new VOListResponse(rows,false,rows.size());

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    } catch (Exception ex1) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while retrieving columns permission",ex1);
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        rset.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
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
  public VOListResponse loadRoleFunctionCompanies(GridParams params,String langId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      BigDecimal progressiveSYS04 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      String functionCodeSYS06 = (String)params.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06);

      // retrieve all companies...
      RoleFunctionCompanyVO vo = null;
      ArrayList list = new ArrayList();
      pstmt = conn.prepareStatement(
          "select COMPANY_CODE,NAME_1 from SYS01_COMPANIES,REG04_SUBJECTS where "+
          "SYS01_COMPANIES.COMPANY_CODE=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and SYS01_COMPANIES.ENABLED='Y'"
      );
      ResultSet rset = pstmt.executeQuery();
      Hashtable companies = new Hashtable();
      while(rset.next()) {
        vo = new RoleFunctionCompanyVO();
        vo.setCompanyCodeSys01SYS02(rset.getString(1));
        vo.setName_1REG04(rset.getString(2));
        vo.setCanDelSYS02(Boolean.FALSE);
        vo.setCanUpdSYS02(Boolean.FALSE);
        vo.setCanInsSYS02(Boolean.FALSE);
        vo.setCanView(Boolean.FALSE);
        vo.setProgressiveSys04SYS02(progressiveSYS04);
        vo.setFunctionCodeSys06SYS02(functionCodeSYS06);
        list.add(vo);
        companies.put(rset.getString(1),new Integer(list.size()-1));
      }
      rset.close();
      pstmt.close();


      // retrieve all USER companies, associated to the specified role-function...
      pstmt = conn.prepareStatement(
          "select SYS02_COMPANIES_ACCESS.COMPANY_CODE_SYS01,SYS02_COMPANIES_ACCESS.CAN_INS,SYS02_COMPANIES_ACCESS.CAN_UPD,SYS02_COMPANIES_ACCESS.CAN_DEL from "+
          "SYS02_COMPANIES_ACCESS where "+
          "SYS02_COMPANIES_ACCESS.PROGRESSIVE_SYS04=? and "+
          "SYS02_COMPANIES_ACCESS.FUNCTION_CODE_SYS06=?"
      );
      pstmt.setBigDecimal(1,progressiveSYS04);
      pstmt.setString(2,functionCodeSYS06);

      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (RoleFunctionCompanyVO)list.get(((Integer)companies.get(rset.getString(1))).intValue());
        vo.setFunctionCodeSys06SYS02(functionCodeSYS06);
        vo.setProgressiveSys04SYS02(progressiveSYS04);
        vo.setCanInsSYS02(Boolean.valueOf(rset.getString(2).equals("Y")));
        vo.setCanUpdSYS02(Boolean.valueOf(rset.getString(3).equals("Y")));
        vo.setCanDelSYS02(Boolean.valueOf(rset.getString(4).equals("Y")));
        vo.setCanView(Boolean.TRUE);
      }
      rset.close();

      Response answer = new VOListResponse(list,false,list.size());


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    } catch (Exception ex1) {
      ex1.printStackTrace();
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
    }

  }

  



  /**
   * Business logic to execute.
   */
  public VOListResponse loadRoleFunctions(GridParams params,String langId,String username,HashMap userRoles) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      BigDecimal progressiveSYS04 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      BigDecimal progressiveHIE01 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);

      // retrieve all subnodes of the specified node...
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
          "where ENABLED='Y' and PROGRESSIVE_HIE02=2 and PROGRESSIVE>=? "+
          "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      pstmt.setBigDecimal(1,progressiveHIE01);
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

      // retrieve all USER functions + functions associated to the specified role...
      String roles = "";
      Iterator it = userRoles.keySet().iterator();
      while(it.hasNext()) {
        roles += it.next()+",";
      }
      if (roles.length()>0)
        roles = roles.substring(0,roles.length()-1);

      RoleFunctionVO vo = null;
      ArrayList list = new ArrayList();
      HashSet functions = new HashSet();
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,SYS10_TRANSLATIONS.DESCRIPTION,SYS07_ROLE_FUNCTIONS.CAN_INS,SYS07_ROLE_FUNCTIONS.CAN_UPD,SYS07_ROLE_FUNCTIONS.CAN_DEL,SYS06_FUNCTIONS.USE_COMPANY_CODE,SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 "+
          "from SYS06_FUNCTIONS,SYS07_ROLE_FUNCTIONS,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS where "+
          "SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 in ("+roles+") and "+
          "SYS07_ROLE_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS06_FUNCTIONS.FUNCTION_CODE=SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06 and "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01 in ("+nodes+") and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10"
      );
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = new RoleFunctionVO();
        if (rset.getBigDecimal(7).equals(progressiveSYS04)) {
          vo.setCanDelSYS07(Boolean.valueOf(rset.getString(5).equals("Y")));
          vo.setCanUpdSYS07(Boolean.valueOf(rset.getString(4).equals("Y")));
          vo.setCanInsSYS07(Boolean.valueOf(rset.getString(3).equals("Y")));
          vo.setCanView(Boolean.TRUE);
          if (functions.contains(rset.getString(1))) {
            functions.remove(rset.getString(1));
            for(int i=0;i<list.size();i++) {
              if (((RoleFunctionVO)list.get(i)).getFunctionCodeSys06SYS07().equals(rset.getString(1))) {
                list.remove(i);
                break;
              }
            }
          }
        }
        else {
          vo.setCanDelSYS07(Boolean.FALSE);
          vo.setCanUpdSYS07(Boolean.FALSE);
          vo.setCanInsSYS07(Boolean.FALSE);
          vo.setCanView(Boolean.FALSE);
        }
        vo.setDescriptionSYS10(rset.getString(2));
        vo.setFunctionCodeSys06SYS07(rset.getString(1));
        vo.setProgressiveSys04SYS07(progressiveSYS04);
        vo.setUseCompanyCodeSYS06(Boolean.valueOf(rset.getString(6).equals("Y")));
        if (!functions.contains(rset.getString(1))) {
          functions.add(rset.getString(1));
          list.add(vo);
        }
      }
      rset.close();

      Response answer = new VOListResponse(list,false,list.size());


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    } catch (Exception ex1) {
      ex1.printStackTrace();
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
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
  public VOResponse deleteRole(RoleVO vo,String t1,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      // the role will be removed only if no user has that role (except the current user...)
      ResultSet rset = stmt.executeQuery("select USERNAME_SYS03 from SYS14_USER_ROLES where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());
      int count = 0;
      while(rset.next()) {
        if (!rset.getString(1).equals(username.toUpperCase()))
          count++;
      }
      rset.close();
      if (count>0) {
        throw new Exception(t1);
      }
      else {
        // phisically remove that role from all user associations (from SYS14 table...)
        stmt.execute("delete from SYS14_USER_ROLES where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // phisically remove companies-role relations from SYS02 table...
        stmt.execute("delete from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // phisically remove functions-role associations from SYS07 table...
        stmt.execute("delete from SYS07_ROLE_FUNCTIONS where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // logically delete the record in SYS09...
        stmt.execute("update SYS04_ROLES set ENABLED='N' where PROGRESSIVE="+vo.getProgressiveSYS04());
      }

      Response answer = new VOResponse(new Boolean(true));




      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing role",ex);
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
    }

  }



}

