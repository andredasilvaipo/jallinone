package org.jallinone.system.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.message.receive.java.UserAuthorizationsResponse;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.system.java.CustomizedWindows;
import org.jallinone.system.customizations.java.WindowCustomizationVO;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve function authorizations and buttons authorizations,
 * according to the logged user.</p>
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
public class UserAuthorizationsAction implements Action {

  public UserAuthorizationsAction() {
  }




  /**
   * @return request name
   */
  public final String getRequestName() {
    return "getUserAuthorizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    ButtonsAuthorizations ba = new ButtonsAuthorizations();
    ButtonCompanyAuthorizations companyBa = new ButtonCompanyAuthorizations();

    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);
      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset2 = null;

      // retrieve functions...
      Hashtable functions = new Hashtable();
      ArrayList functionsPerNode = null;
      ApplicationFunction f = null;
      HashSet functionsAdded = new HashSet();
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,SYS06_FUNCTIONS.IMAGE_NAME,SYS06_FUNCTIONS.METHOD_NAME,SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS10_TRANSLATIONS.DESCRIPTION,SYS07_ROLE_FUNCTIONS.CAN_INS,SYS07_ROLE_FUNCTIONS.CAN_UPD,SYS07_ROLE_FUNCTIONS.CAN_DEL,SYS06_FUNCTIONS.USE_COMPANY_CODE,SYS04_ROLES.PROGRESSIVE "+
          "from SYS06_FUNCTIONS,SYS04_ROLES,SYS07_ROLE_FUNCTIONS,SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS "+
          "where SYS14_USER_ROLES.USERNAME_SYS03='"+userSessionPars.getUsername()+"' and "+
          "SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.PROGRESSIVE=SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 and "+
          "SYS07_ROLE_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10 order by "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS18_FUNCTION_LINKS.POS_ORDER"
      );
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        f = new ApplicationFunction(
          rset.getString(5),
          rset.getString(1),
          rset.getString(2),
          rset.getString(3)
        );

        if (rset.getString(9).equals("Y")) {
          // authorizations defined at company level...
          rset2 = stmt.executeQuery("select COMPANY_CODE_SYS01,CAN_INS,CAN_UPD,CAN_DEL from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04="+rset.getInt(10)+" and FUNCTION_CODE_SYS06='"+f.getFunctionId()+"'");
          while(rset2.next()) {
            companyBa.addButtonAuthorization(
              f.getFunctionId(),
              rset2.getString(1),
              rset2.getString(2).equals("Y"),
              rset2.getString(3).equals("Y"),
              rset2.getString(4).equals("Y")
            );
          }
          rset2.close();
        }

        functionsPerNode = (ArrayList)functions.get(new Integer(rset.getInt(4)));
        if (functionsPerNode==null) {
          functionsPerNode = new ArrayList();
          functions.put(new Integer(rset.getInt(4)),functionsPerNode);
        }

        if (!functionsAdded.contains(new Integer(rset.getInt(4))+"-"+f.getFunctionId())) {
          functionsAdded.add(new Integer(rset.getInt(4))+"-"+f.getFunctionId());
          functionsPerNode.add(f);
        }

        ba.addButtonAuthorization(
            f.getFunctionId(),
            rset.getString(6).equals("Y"),
            rset.getString(7).equals("Y"),
            rset.getString(8).equals("Y")
        );

      }
      pstmt.close();

      // retrieve user roles...
      Hashtable userRoles = new Hashtable();
      pstmt = conn.prepareStatement(
          "select SYS04_ROLES.PROGRESSIVE,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SYS04_ROLES,SYS14_USER_ROLES,SYS10_TRANSLATIONS "+
          "where SYS14_USER_ROLES.USERNAME_SYS03='"+userSessionPars.getUsername()+"' and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS04_ROLES.PROGRESSIVE_SYS10"
      );
      rset = pstmt.executeQuery();
      while(rset.next()) {
        userRoles.put(rset.getBigDecimal(1),rset.getString(2));
      }
      rset.close();
      pstmt.close();

      // retrieve the whole tree...
      DefaultTreeModel model = null;
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEVEL,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from HIE01_LEVELS,SYS10_TRANSLATIONS "+
          "where HIE01_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and ENABLED='Y' and PROGRESSIVE_HIE02=2 "+
          "order by LEVEL,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      rset = pstmt.executeQuery();
      Hashtable currentLevelNodes = new Hashtable();
      Hashtable newLevelNodes = new Hashtable();
      int currentLevel = -1;
      DefaultMutableTreeNode currentNode = null;
      DefaultMutableTreeNode parentNode = null;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new Hashtable();
        }

        if (currentLevel==0) {
          // prepare a tree model with the root node...
          currentNode = new DefaultMutableTreeNode();
          model = new DefaultTreeModel(currentNode);
        }
        else {
          currentNode = new ApplicationFunction(rset.getString(4),null);

          parentNode = (DefaultMutableTreeNode)currentLevelNodes.get(new Integer(rset.getInt(2)));
          parentNode.add(currentNode);
        }

        newLevelNodes.put(new Integer(rset.getInt(1)),currentNode);

        // add functions to the node...
        functionsPerNode = (ArrayList)functions.get(new Integer(rset.getInt(1)));
        if (functionsPerNode!=null)
          for(int i=0;i<functionsPerNode.size();i++)
            currentNode.add((DefaultMutableTreeNode)functionsPerNode.get(i));

      }

      // remove folders that have no children (except other folders)...
      hasChild((DefaultMutableTreeNode)model.getRoot());

      // count companies number...
      int companiesNr = 0;
      rset = stmt.executeQuery("select COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y'");
      while(rset.next())
        companiesNr++;
      rset.close();

      // retrieve windows customizations...
      CustomizedWindows cust = new CustomizedWindows();
      rset = stmt.executeQuery(
        "select SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_TYPE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_SIZE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_DEC,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10,SYS12_WINDOW_CUSTOMIZATIONS.ATTRIBUTE_NAME,SYS10_TRANSLATIONS.DESCRIPTION,SYS13_WINDOWS.TABLE_NAME from "+
        "SYS12_WINDOW_CUSTOMIZATIONS,SYS10_TRANSLATIONS,SYS13_WINDOWS where "+
        "SYS13_WINDOWS.PROGRESSIVE=SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13 and "+
        "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
        "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE "+
        "order by SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10"
      );
      WindowCustomizationVO vo = null;
      while(rset.next()) {
        vo = new WindowCustomizationVO();
        vo.setProgressiveSys13SYS12(rset.getBigDecimal(1));
        vo.setColumnNameSYS12(rset.getString(2));
        vo.setColumnTypeSYS12(rset.getString(3));
        vo.setColumnSizeSYS12(rset.getBigDecimal(4));
        vo.setColumnDecSYS12(rset.getBigDecimal(5));
        vo.setProgressiveSys10SYS12(rset.getBigDecimal(6));
        vo.setAttributeNameSYS12(rset.getString(7));
        vo.setDescriptionSYS10(rset.getString(8));
        vo.setTableNameSYS13(rset.getString(9));
        cust.addWindowCustomization(vo);
      }
      rset.close();

      // retrieve application parameters from SYS11...
      HashMap applicationPars = new HashMap();
      rset = stmt.executeQuery("select PARAM_CODE,VALUE from SYS11_APPLICATION_PARS");
      while(rset.next()) {
        applicationPars.put(rset.getString(1),rset.getString(2));
      }
      rset.close();

      // store user roles in user session...
      ((JAIOUserSessionParameters)userSessionPars).setUserRoles(userRoles);

      // store company authorizations in user session...
      ((JAIOUserSessionParameters)userSessionPars).setCompanyBa(companyBa);

      // store the customized windows in user session...
      ((JAIOUserSessionParameters)userSessionPars).setCustomizedWindows(cust);

      // store application parameters in user session...
      ((JAIOUserSessionParameters)userSessionPars).setAppParams(applicationPars);


      return new VOResponse(new ApplicationParametersVO(
          userSessionPars.getLanguageId(),
          model,
          ba,
          companyBa,
          companiesNr==1,
          cust,
          applicationPars,
          userRoles,
          ((JAIOUserSessionParameters)userSessionPars).getProgressiveReg04SYS03(),
          ((JAIOUserSessionParameters)userSessionPars).getName_1(),
          ((JAIOUserSessionParameters)userSessionPars).getName_2(),
          ((JAIOUserSessionParameters)userSessionPars).getEmployeeCode(),
          ((JAIOUserSessionParameters)userSessionPars).getCompanyCodeSys01SYS03()
      ));
    } catch (Exception ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }
  }


  /**
   * Remove folders that have no children (except other folders).
   * @param node current node
   */
  private boolean hasChild(DefaultMutableTreeNode node) {
    ArrayList toRemove = new ArrayList();
    try {
      ApplicationFunction f = null;
      if (node.getChildCount()==0 &&
          node instanceof ApplicationFunction &&
          ((ApplicationFunction)node).isFolder())
        return false;
      if (node.getChildCount()==0 &&
          node instanceof ApplicationFunction &&
          !((ApplicationFunction)node).isFolder())
        return true;

      boolean hasFunctionChild = false;
      for(int i=0;i<node.getChildCount();i++) {
        f = (ApplicationFunction)node.getChildAt(i);
        if(!f.isFolder())
          hasFunctionChild = true;
        if (!hasChild(f)) {
          toRemove.add(f);
        }
        else
          hasFunctionChild = true;
      }
      if (!hasFunctionChild)
        return false;
    }
    finally {
      for(int i=0;i<toRemove.size();i++)
        ((DefaultMutableTreeNode)((DefaultMutableTreeNode)toRemove.get(i)).getParent()).remove((DefaultMutableTreeNode)toRemove.get(i));

    }
    return true;
  }



}
