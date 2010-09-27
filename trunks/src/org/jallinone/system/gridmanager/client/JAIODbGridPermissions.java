package org.jallinone.system.gridmanager.client;

import org.openswing.swing.table.permissions.java.GridPermissionsManager;
import java.util.ArrayList;
import org.openswing.swing.table.permissions.java.GridPermissions;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.commons.client.ClientApplet;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.ApplicationClientFacade;
import java.util.Iterator;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid permissions manager: it manages the fetching of grid permissions.
 * This implementation is based on database tables: it stores and retrieves user roles from a table and after that
 * the permissions from a second table filtered by user roles and grid identifier.
 * </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
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
public class JAIODbGridPermissions extends GridPermissionsManager {

  public JAIODbGridPermissions() {}


  /**
   * @return list of role identifiers associated to the specified user (Object[] values)
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public ArrayList getUserRoles() throws Throwable {
    ClientApplet applet = ( (ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
    Iterator it = applet.getAuthorizations().getUserRoles().keySet().iterator();
    ArrayList userRoles = new ArrayList();
    while(it.hasNext())
      userRoles.add(new Object[]{it.next()});
    return userRoles;
//    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
//      "getUserRoles"
//    });
//    if (res.isError())
//      throw new Exception(res.getErrorMessage());
//    else
//      return (ArrayList)((VOResponse)res).getVo();
  }


  /**
   * @param functionId identifier (functionId) associated to the grid
   * @param userRoles list of role identifiers associated to the specified user
   * @param columnAttributes list of attribute names, that identify columns
   * @param columnsVisibility define which columns are visible
   * @param columnEditableInInsert define which columns are editable on insert; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnEditableInEdit define which columns are editable on edit; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnsMandatory define which columns are required on insert/edit mode; used to correctly define GridPermissions content: a column will be marked as required if currently not required but NOT the inverse
   * @return GridPermissions object, built starting from user roles for the specified grid identifier
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public GridPermissions getUserGridPermissions(String functionId,ArrayList userRoles,String[] columnAttributes,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable{
    ClientApplet applet = ( (ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
    GridPermissions serverPermissions = (GridPermissions)applet.getAuthorizations().getGridPermissions().get(functionId);

    GridPermissions permissions = new GridPermissions(
      functionId,
      getUsername(),
      (String[])columnAttributes.clone(),
      (boolean[])columnsVisibility.clone(),
      (boolean[])columnEditableInInsert.clone(),
      (boolean[])columnsEditableInEdit.clone(),
      (boolean[])columnsMandatory.clone()
    );
    if (serverPermissions==null)
      return permissions;

    boolean[] aux = serverPermissions.getColumnsEditabilityInInsert(); // editableColumnsInInsertFieldName
    for(int i=0;i<aux.length;i++)
      permissions.getColumnsEditabilityInInsert()[i] = permissions.getColumnsEditabilityInInsert()[i] && aux[i];

    aux = serverPermissions.getColumnsEditabilityInEdit(); // editableColumnsInEdit
    for(int i=0;i<aux.length;i++)
      permissions.getColumnsEditabilityInEdit()[i] = permissions.getColumnsEditabilityInEdit()[i] && aux[i];

    aux = serverPermissions.getColumnsMandatory(); // columnsMandatory
    for(int i=0;i<aux.length;i++)
      permissions.getColumnsMandatory()[i] = permissions.getColumnsMandatory()[i] || aux[i];

    aux = serverPermissions.getColumnsVisibility(); // columnsVisibility
    for(int i=0;i<aux.length;i++)
      permissions.getColumnsVisibility()[i] = aux[i];

    return permissions;

//    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
//        "getUserGridPermissions",
//        functionId,
//        userRoles,
//        columnAttributes,
//        columnsVisibility,
//        columnEditableInInsert,
//        columnsEditbleInEdit,
//        columnsMandatory
//    });
//    if (res.isError())
//      throw new Exception(res.getErrorMessage());
//    else
//      return (GridPermissions)((VOResponse)res).getVo();
  }


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid permissions will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no digest has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public String getLastGridDigest(String functionId) throws Throwable {
    ClientApplet applet = ( (ApplicationClientFacade)MDIFrame.getInstance().getClientFacade()).getMainClass();
    return (String)applet.getAuthorizations().getLastGridPermissionsDigests().get(functionId);


//    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
//        "getLastGridDigest",
//        functionId
//    });
//    if (res.isError())
//      throw new Exception(res.getErrorMessage());
//    else
//      return (String)((VOResponse)res).getVo();
  }


  /**
   * Store in grid permissions defaults table.
   * @param functionId identifier (functionId) associated to the grid
   * @param columnAttributes list of attribute names, that identify columns
   * @param headerColumnNames list of keys for columns, that will be translated
   * @param columnsVisibility define which columns are visible
   * @param columnEditableInInsert define which columns are editable on insert; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnEditableInEdit define which columns are editable on edit; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnsMandatory define which columns are required on insert/edit mode; used to correctly define GridPermissions content: a column will be marked as required if currently not required but NOT the inverse
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridPermissionsDefaults(String functionId,String[] columnAttributes,String[] headerColumnNames,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable {
    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
        "storeGridPermissionsDefaults",
        functionId,
        columnAttributes,
        headerColumnNames,
        columnsVisibility,
        columnEditableInInsert,
        columnsEditableInEdit,
        columnsMandatory
    });
    if (res.isError())
      throw new Exception(res.getErrorMessage());
  }


  /**
   * Store the "grid digest", i.e. a value that globally identify the current grid configuration.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridDigest(String functionId,String gridDigest) throws Throwable {
    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
        "storeGridDigest",
        functionId,
        gridDigest
    });
    if (res.isError())
      throw new Exception(res.getErrorMessage());
  }


  /**
   * Delete all grid permissions for the specified grid identifier.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void deleteAllGridPermissionsPerFunctionId(String functionId) throws Throwable {
    Response res = ClientUtils.getData("dbGridPermissions",new Object[]{
        "deleteAllGridPermissionsPerFunctionId",
        functionId
    });
    if (res.isError())
      throw new Exception(res.getErrorMessage());
  }


}
