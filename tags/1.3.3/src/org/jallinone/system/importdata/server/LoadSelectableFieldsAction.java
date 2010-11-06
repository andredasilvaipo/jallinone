package org.jallinone.system.importdata.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import java.math.BigDecimal;
import java.util.Iterator;
import org.jallinone.system.languages.server.LoadLanguagesBean;
import org.jallinone.system.languages.java.LanguageVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load ETL process fields defined in SYS24 table and create SelectableFieldVO objects.</p>
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
public class LoadSelectableFieldsAction implements Action {

  private LoadETLProcessFieldsBean bean = new LoadETLProcessFieldsBean();
  private LoadLanguagesBean langsBean = new LoadLanguagesBean();


  public LoadSelectableFieldsAction() {}


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSelectableFields";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));

      GridParams gridParams = (GridParams)inputPar;
      ETLProcessVO processVO = (ETLProcessVO)gridParams.getOtherGridParams().get(ApplicationConsts.FILTER_VO);

      // read from SYS24 table...
      Response res = bean.loadETLProcessFields(conn,processVO.getProgressiveSYS23(),userSessionPars);

      if (!res.isError()) {
        java.util.List vos = ( (VOListResponse) res).getRows();
        HashMap map = new HashMap();
        ETLProcessFieldVO vo = null;
        for (int i = 0; i < vos.size(); i++) {
          vo = (ETLProcessFieldVO) vos.get(i);
          map.put(new FieldEntry(vo.getLanguageCodeSYS24(),vo.getFieldNameSYS24()), vo);
        }

        ArrayList rows = new ArrayList();
        SelectableFieldVO selVO = null;
        ImportDescriptorVO impVO = (ImportDescriptorVO) Class.forName(processVO.getClassNameSYS23()).newInstance();
        FieldEntry pk = null;
        for (int i = 0; i < impVO.getFields().length; i++) {
          pk = new FieldEntry(ApplicationConsts.JOLLY, impVO.getFields()[i]);
          if (map.containsKey(pk)) {
            vo = (ETLProcessFieldVO) map.get(pk);

            selVO = new SelectableFieldVO();
            selVO.setField(vo);
            selVO.setSelected(true);
            selVO.setLabel(resources.getResource(impVO.getLabels()[i]));
            selVO.setRequired(impVO.getRequiredFields().contains(impVO.getFields()[i]));
            rows.add(selVO);
          }
          else {
            vo = new ETLProcessFieldVO();
            vo.setFieldNameSYS24(impVO.getFields()[i]);
            vo.setLanguageCodeSYS24(ApplicationConsts.JOLLY);
            vo.setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

            selVO = new SelectableFieldVO();
            selVO.setField(vo);
            selVO.setSelected(false);
            selVO.setLabel(resources.getResource(impVO.getLabels()[i]));
            selVO.setRequired(impVO.getRequiredFields().contains(impVO.
                getFields()[i]));
            rows.add(selVO);
          }
        } // end for

        // retrieve supported languages...
        res = langsBean.loadLanguages(conn, userSessionPars);
        if (res.isError())
          return res;
        java.util.List langsVO = ( (VOListResponse) res).getRows();

        // for each progressive SYS10 x language...
        Iterator it = impVO.getProgressiveSys10Fields().keySet().iterator();
        String field = null;
        LanguageVO langVO = null;
        while(it.hasNext()) {
          field = it.next().toString();
          for(int i=0;i<langsVO.size();i++) {
            langVO = (LanguageVO)langsVO.get(i);
            pk = new FieldEntry(langVO.getLanguageCodeSYS09(),field);
            if (map.containsKey(pk)) {
              vo = (ETLProcessFieldVO)map.get(new FieldEntry(langVO.getLanguageCodeSYS09(),field));

              selVO = new SelectableFieldVO();
              selVO.setField(vo);
              selVO.setSelected(true);
              selVO.setLabel(
                resources.getResource((String)impVO.getProgressiveSys10Fields().get(field))+
                " "+
                langVO.getDescriptionSYS09()
              );
              selVO.setRequired(impVO.getRequiredFields().contains(field));
              rows.add(selVO);
            }
            else {
              vo = new ETLProcessFieldVO();
              vo.setFieldNameSYS24(field);
              vo.setLanguageCodeSYS24(langVO.getLanguageCodeSYS09());
              vo.setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

              selVO = new SelectableFieldVO();
              selVO.setField(vo);
              selVO.setSelected(false);
              selVO.setLabel(
                resources.getResource((String)impVO.getProgressiveSys10Fields().get(field))+
                " "+
                langVO.getDescriptionSYS09()
              );
              selVO.setRequired(impVO.getRequiredFields().contains(field));
              rows.add(selVO);
            }
          }

        } // end while on descriptions...

        // if a hierarchy level has been defined, create an object for each supported language...
        if (processVO.getProgressiveHIE02()!=null) {
          field = impVO.getHierarchyField();

          for(int i=0;i<langsVO.size();i++) {
            langVO = (LanguageVO)langsVO.get(i);
            pk = new FieldEntry(langVO.getLanguageCodeSYS09(),field);

            if (map.containsKey(pk)) {
              vo = (ETLProcessFieldVO)map.get(pk);

              selVO = new SelectableFieldVO();
              selVO.setField(vo);
              selVO.setSelected(true);
              selVO.setLabel(
                  resources.getResource("hierarchy") +
                  " " +
                  langVO.getDescriptionSYS09()
                  );
              selVO.setRequired(impVO.getRequiredFields().contains(field));
              rows.add(selVO);
            }
            else {

              vo = new ETLProcessFieldVO();
              vo.setFieldNameSYS24(field);
              vo.setLanguageCodeSYS24(langVO.getLanguageCodeSYS09());
              vo.setProgressiveSys23SYS24(processVO.getProgressiveSYS23());

              selVO = new SelectableFieldVO();
              selVO.setField(vo);
              selVO.setSelected(false);
              selVO.setLabel(
                resources.getResource("hierarchy")+
                " "+
                langVO.getDescriptionSYS09()
              );
              selVO.setRequired(impVO.getRequiredFields().contains(field));
              rows.add(selVO);
            }
          }
        }


        return new VOListResponse(rows,false,rows.size());
      }
      else
        return res;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching ETL process fields",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
