package org.jallinone.system.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jallinone.commons.server.JAIOBeanFactory;
import org.jallinone.system.gridmanager.server.JAIODbPermissionsDescriptor;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.system.java.ButtonCompanyAuthorization;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.system.java.CustomizedWindows;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.permissions.java.ButtonAuthorization;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.table.permissions.java.GridPermissions;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.jallinone.variants.server.VariantsBean;
import org.jallinone.variants.java.VariantVO;
import org.jallinone.variants.java.VariantDescriptionsVO;
import org.jallinone.variants.server.Variants;

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


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		try {
			String langId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
			String username = userSessionPars.getUsername();
			UserAuthorizations bean = (UserAuthorizations)JAIOBeanFactory.getInstance().getBean(UserAuthorizations.class);

			HashMap applicationPars = bean.getApplicationPars();

			ButtonCompanyAuthorizations companyBa = new ButtonCompanyAuthorizations();
			ButtonCompanyAuthorization[] companyBas = bean.getButtonCompanyAuthorizations(langId, username);
			for(int i=0;i<companyBas.length;i++)
				companyBa.addButtonAuthorization(
					companyBas[i].getFunctionId(),
					companyBas[i].getCompanyCode(),
					companyBas[i].isInsertEnabled(),
					companyBas[i].isEditEnabled(),
					companyBas[i].isDeleteEnabled()
				);

			ButtonsAuthorizations ba = new ButtonsAuthorizations();
			ButtonAuthorization[] bas = bean.getButtonsAuthorizations(langId, username);
			for(int i=0;i<bas.length;i++)
				ba.addButtonAuthorization(
					bas[i].getFunctionId(),
					bas[i].isInsertEnabled(),
					bas[i].isEditEnabled(),
					bas[i].isDeleteEnabled()
				);

		  String[] companies = bean.getCompanies();
			int companiesNr = companies.length;
			HashMap userRoles = bean.getUserRoles(langId, username);

			HashMap gridPermissions = getGridPermissions(username, userRoles);
			HashMap lastGridPermissionsDigests = bean.getLastGridPermissionsDigests();
			DefaultTreeModel model = getMenu(langId, username);
			CustomizedWindows cust = bean.getWindowCustomizations(langId);

			// store user roles in user session...
			((JAIOUserSessionParameters)userSessionPars).setUserRoles(userRoles);

			// store company authorizations in user session...
			((JAIOUserSessionParameters)userSessionPars).setCompanyBa(companyBa);

			// store the customized windows in user session...
			((JAIOUserSessionParameters)userSessionPars).setCustomizedWindows(cust);

			// store application parameters in user session...
			((JAIOUserSessionParameters)userSessionPars).setAppParams(applicationPars);

      // store variants descriptions...
      Variants vBean = (Variants)JAIOBeanFactory.getInstance().getBean(Variants.class);

      String tableName = "ITM11_VARIANTS_1";
      String variantTypeJoin = "VARIANT_TYPE_ITM06";
      java.util.List rows = vBean.loadAllVariants(tableName,variantTypeJoin,langId,username).getRows();
      VariantVO vo = null;
      VariantDescriptionsVO d = null;
      for(int i=0;i<rows.size();i++) {
        vo = (VariantVO)rows.get(i);
        d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(vo.getCompanyCodeSys01());
        if (d==null) {
          d = new VariantDescriptionsVO();
          ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(vo.getCompanyCodeSys01(),d);
        }
        d.getVariant1Descriptions().put(
          vo.getVariantType()+"_"+vo.getVariantCode(),
          vo.getDescriptionSys10()
        );
      }

      tableName = "ITM12_VARIANTS_2";
      variantTypeJoin = "VARIANT_TYPE_ITM07";
      rows = vBean.loadAllVariants(tableName,variantTypeJoin,langId,username).getRows();
      for(int i=0;i<rows.size();i++) {
        vo = (VariantVO)rows.get(i);
        d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(vo.getCompanyCodeSys01());
        if (d==null) {
          d = new VariantDescriptionsVO();
          ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(vo.getCompanyCodeSys01(),d);
        }
        d.getVariant2Descriptions().put(
          vo.getVariantType()+"_"+vo.getVariantCode(),
          vo.getDescriptionSys10()
        );
      }

      tableName = "ITM13_VARIANTS_3";
      variantTypeJoin = "VARIANT_TYPE_ITM08";
      rows = vBean.loadAllVariants(tableName,variantTypeJoin,langId,username).getRows();
      for(int i=0;i<rows.size();i++) {
        vo = (VariantVO)rows.get(i);
        d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(vo.getCompanyCodeSys01());
        if (d==null) {
          d = new VariantDescriptionsVO();
          ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(vo.getCompanyCodeSys01(),d);
        }
        d.getVariant3Descriptions().put(
          vo.getVariantType()+"_"+vo.getVariantCode(),
          vo.getDescriptionSys10()
        );
      }

      tableName = "ITM14_VARIANTS_4";
      variantTypeJoin = "VARIANT_TYPE_ITM09";
      rows = vBean.loadAllVariants(tableName,variantTypeJoin,langId,username).getRows();
      for(int i=0;i<rows.size();i++) {
        vo = (VariantVO)rows.get(i);
        d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(vo.getCompanyCodeSys01());
        if (d==null) {
          d = new VariantDescriptionsVO();
          ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(vo.getCompanyCodeSys01(),d);
        }
        d.getVariant4Descriptions().put(
          vo.getVariantType()+"_"+vo.getVariantCode(),
          vo.getDescriptionSys10()
        );
      }

      tableName = "ITM15_VARIANTS_5";
      variantTypeJoin = "VARIANT_TYPE_ITM10";
      rows = vBean.loadAllVariants(tableName,variantTypeJoin,langId,username).getRows();
      for(int i=0;i<rows.size();i++) {
        vo = (VariantVO)rows.get(i);
        d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(vo.getCompanyCodeSys01());
        if (d==null) {
          d = new VariantDescriptionsVO();
          ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(vo.getCompanyCodeSys01(),d);
        }
        d.getVariant5Descriptions().put(
          vo.getVariantType()+"_"+vo.getVariantCode(),
          vo.getDescriptionSys10()
        );
      }

	    // check if there exists an entry in ((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO()
			// for each supported company code...
			for(int i=0;i<companies.length;i++) {
				d = (VariantDescriptionsVO)((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().get(companies[i]);
				if (d==null) {
					d = new VariantDescriptionsVO();
					((JAIOUserSessionParameters)userSessionPars).getVariantDescriptionsVO().put(companies[i],d);
				}
			}

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
					((JAIOUserSessionParameters)userSessionPars).getCompanyCodeSys01SYS03(),
					lastGridPermissionsDigests,
					gridPermissions
			));
		}
		catch (Throwable ex) {
			Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
			return new ErrorResponse(ex.getMessage());
		}
	}



	  /**
	   * Business logic to execute.
	   */
	  private HashMap getGridPermissions(String username,HashMap userRoles) throws Throwable {
	    PreparedStatement pstmt = null;
	    Statement stmt = null;
	    Connection conn = null;
	    try {
	      conn = ConnectionManager.getConnection(null);
	      stmt = conn.createStatement(); // used for secondary query on SYS02...
	      ResultSet rset = null;

	      JAIODbPermissionsDescriptor dbPermissionsDescriptor = new JAIODbPermissionsDescriptor();
	      String sql =
	        "select "+
	        dbPermissionsDescriptor.getFunctionIdFieldNameInGridPermissionsTable()+","+
	        dbPermissionsDescriptor.getColumnsAttributeFieldNameInGridPermissionsTable()+","+
	        dbPermissionsDescriptor.getEditableColumnsInInsertFieldNameInGridPermissionsTable()+","+
	        dbPermissionsDescriptor.getEditableColumnsInEditFieldNameInGridPermissionsTable()+","+
	        dbPermissionsDescriptor.getColumnsMandatoryFieldNameInGridPermissionsTable()+","+
	        dbPermissionsDescriptor.getColumnsVisibilityFieldNameInGridPermissionsTable()+" "+
	        "from "+dbPermissionsDescriptor.getGridPermissionsTableName()+" where ";
	      String functionId = null;
	      for(int j=0;j<userRoles.size();j++) {
	        sql += " (";
	        for(int i=0;i<dbPermissionsDescriptor.getRoleIdFieldNamesInGridPermissionsTable().length;i++)
	          sql += dbPermissionsDescriptor.getRoleIdFieldNamesInGridPermissionsTable()[i]+"=? or ";
	        sql = sql.substring(0,sql.length()-3);
	        sql += ") and ";
	      }
	      sql = sql.substring(0,sql.length()-4);
	      sql += " order by "+dbPermissionsDescriptor.getFunctionIdFieldNameInGridPermissionsTable();
	      pstmt = conn.prepareStatement(sql);
	      Object roleId =null;
	      int count = 1;
	      Iterator it = userRoles.keySet().iterator();
	      while(it.hasNext()) {
	        roleId = it.next();
	        pstmt.setObject(count++, roleId);
	      }
	      rset = pstmt.executeQuery();

	      HashMap gridPermissions = new HashMap();
	      GridPermissions permissions = null;
	      String[] aux = null;
	      while(rset.next()) {
	        if (functionId==null || !functionId.equals(rset.getString(1))) {
	          functionId = rset.getString(1);
	          int num = rset.getString(2).split(",").length;
	          permissions = new GridPermissions(
	            functionId,
	            username,
	            new String[num],
	            new boolean[num],
	            new boolean[num],
	            new boolean[num],
	            new boolean[num]
	          );
	          gridPermissions.put(functionId,permissions);
	        }
	        aux = rset.getString(2).split(","); // columnAttributes
	        for(int i=0;i<aux.length;i++)
	          permissions.getColumnsAttribute()[i] = aux[i];

	        aux = rset.getString(3).split(","); // editableColumnsInInsertFieldName
	        for(int i=0;i<aux.length;i++)
	          permissions.getColumnsEditabilityInInsert()[i] = aux[i].equals("true");

	        aux = rset.getString(4).split(","); // editableColumnsInEdit
	        for(int i=0;i<aux.length;i++)
	          permissions.getColumnsEditabilityInEdit()[i] = aux[i].equals("true");

	        aux = rset.getString(5).split(","); // columnsMandatory
	        for(int i=0;i<aux.length;i++)
	          permissions.getColumnsMandatory()[i] = aux[i].equals("true");

	        aux = rset.getString(6).split(","); // columnsVisibility
	        for(int i=0;i<aux.length;i++)
	          permissions.getColumnsVisibility()[i] = aux[i].equals("true");
	      }
	      rset.close();
	      pstmt.close();

	      return gridPermissions;

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
	        stmt.close();
	      }
	      catch (Exception ex) {
	      }
	      try {
	    	  ConnectionManager.releaseConnection(conn, null);
	      } catch (Exception e) {
	      }
	    }
	  }




	  /**
	   * Business logic to execute.
	   */
	  private DefaultTreeModel getMenu(String langId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    Statement stmt = null;
	    Connection conn = null;
	    try {
	      conn = ConnectionManager.getConnection(null);
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
	          "where SYS14_USER_ROLES.USERNAME_SYS03='"+username+"' and "+
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

	        functionsPerNode = (ArrayList)functions.get(new Integer(rset.getInt(4)));
	        if (functionsPerNode==null) {
	          functionsPerNode = new ArrayList();
	          functions.put(new Integer(rset.getInt(4)),functionsPerNode);
	        }

	        if (!functionsAdded.contains(new Integer(rset.getInt(4))+"-"+f.getFunctionId())) {
	          functionsAdded.add(new Integer(rset.getInt(4))+"-"+f.getFunctionId());
	          functionsPerNode.add(f);
	        }
	      }
	      pstmt.close();

	      // retrieve the whole tree...
	      DefaultTreeModel model = null;
	      pstmt = conn.prepareStatement(
	          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV,SYS10_TRANSLATIONS.DESCRIPTION "+
	          "from HIE01_LEVELS,SYS10_TRANSLATIONS "+
	          "where HIE01_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
	          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and ENABLED='Y' and PROGRESSIVE_HIE02=2 "+
	          "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
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
	          currentNode = new OpenSwingTreeNode();
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

	      return model;
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
	    		stmt.close();
	    	}
	    	catch (Exception ex) {
	    	}
	    	try {
	    		ConnectionManager.releaseConnection(conn, null);
	    	} catch (Exception e) {
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

