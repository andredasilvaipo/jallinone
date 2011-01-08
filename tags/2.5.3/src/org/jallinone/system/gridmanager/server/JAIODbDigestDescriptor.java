package org.jallinone.system.gridmanager.server;

import java.util.*;
import org.openswing.swing.table.permissions.database.server.DbDigestDescriptor;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Descriptor of the table related to profile digest storing, where the primary key is based on the "functionId" attribute of the GridProfile.
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
public class JAIODbDigestDescriptor implements DbDigestDescriptor {

  private static final String PROFILE_DIGESTS = "SYS25_FUNCTIONS_DIGESTS";
  private static final String FUNCTION_ID = "FUNCTION_CODE_SYS06";
  private static final String DIGEST = "DIGEST";
  private static final Hashtable EMPTY = new Hashtable();


  /**
   * @return name of the profile digest table.
   */
  public String getDigestTableName() {
    return PROFILE_DIGESTS;
  }


  /**
   * @return database field name related to the "digest", that identify all grid columns
   */
  public String getDigestFieldName() {
    return DIGEST;
  }


  /**
   * @return database field name related to the "functionId" attribute of GridProfile
   */
  public String getFunctionIdFieldName() {
    return FUNCTION_ID;
  }


  /**
   * Callback method used to fill  in the insert SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridDigest" method. For instance a CREATE_USER field or a CREATE_DATE field.
   */
  public Hashtable storeGridDigestOnInsert() {
    return EMPTY;
  }


  /**
   * Callback method used to fill  in the SET part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridDigest" method. For instance an UPDATE_USER field or an UPDATE_DATE field.
   */
  public Hashtable storeGridDigestOnSetUpdate() {
    return EMPTY;
  }


  /**
   * Callback method used to fill  in the WHERE part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridDigest" method.
   */
  public Hashtable storeGridDigestOnWhereUpdate() {
    return EMPTY;
  }


}
