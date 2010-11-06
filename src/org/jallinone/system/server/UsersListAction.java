package org.jallinone.system.server;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import org.openswing.swing.customvo.java.CustomValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve the list of connected users.</p>
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
public class UsersListAction implements Action {


  public UsersListAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "usersList";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
        Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
        //userSessionPars = new JAIOUserSessionParameters();
        Enumeration en = userSessions.keys();
        JAIOUserSessionParameters pars = null;
        ArrayList users = new ArrayList();
        while(en.hasMoreElements()) {
          pars = (JAIOUserSessionParameters)userSessions.get(en.nextElement());
          users.add(pars);
        }

        JAIOUserSessionParameters[] uList = (JAIOUserSessionParameters[])users.toArray(new JAIOUserSessionParameters[users.size()]);
        Arrays.sort(uList,new Comparator() {

          public boolean equals(Object obj) {
            if (obj==null || !(obj instanceof JAIOUserSessionParameters))
              return false;
            return this.equals(obj);
          }

          public int compare(Object o1, Object o2) {
            JAIOUserSessionParameters u1 = (JAIOUserSessionParameters)o1;
            JAIOUserSessionParameters u2 = (JAIOUserSessionParameters)o2;
            return u1.getUsername().compareTo(u2.getUsername());
          }

        });
        List usersList = new ArrayList();
        CustomValueObject vo = null;
        for(int i=0;i<uList.length;i++) {
          vo = new CustomValueObject();
          vo.setAttributeNameS0(uList[i].getUsername());
          vo.setAttributeNameS1(uList[i].getName_1());
          vo.setAttributeNameS2(uList[i].getName_2());
          usersList.add(vo);
        }

        return new VOListResponse(usersList,false,usersList.size());
    } catch (Exception ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    }

  }



}
