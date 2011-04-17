package org.jallinone.system.importdata.server;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.importdata.java.ImportDescriptorVO;
import org.jallinone.system.importdata.java.SelectableFieldVO;
import org.jallinone.system.languages.java.LanguageVO;
import org.jallinone.system.languages.server.LanguagesBean;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.send.java.GridParams;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to load ETL process fields defined in SYS24 table and create SelectableFieldVO objects.</p>
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
public class LoadSelectableFieldsBean  implements LoadSelectableFields {


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



  private LoadETLProcessFieldsBean bean;

  public void setBean(LoadETLProcessFieldsBean bean) {
    this.bean = bean;
  }

  private LanguagesBean langsBean;

  public void setLangsBean(LanguagesBean langsBean) {
    this.langsBean = langsBean;
  }



  public LoadSelectableFieldsBean() {}

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public SelectableFieldVO getSelectableFieldVO() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadSelectableFields(GridParams gridParams,Properties p,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...
      langsBean.setConn(conn); // use same transaction...
      ETLProcessVO processVO = (ETLProcessVO)gridParams.getOtherGridParams().get(ApplicationConsts.FILTER_VO);

      // read from SYS24 table...
      Response res = bean.loadETLProcessFields(processVO.getProgressiveSYS23(),username);

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
            selVO.setLabel(p.getProperty(impVO.getLabels()[i]));
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
            selVO.setLabel(p.getProperty(impVO.getLabels()[i]));
            selVO.setRequired(impVO.getRequiredFields().contains(impVO.
                getFields()[i]));
            rows.add(selVO);
          }
        } // end for

        // retrieve supported languages...
        res = langsBean.loadLanguages( serverLanguageId,username);
        if (res.isError())
          throw new Exception(res.getErrorMessage());
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
                p.getProperty((String)impVO.getProgressiveSys10Fields().get(field))+
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
                p.getProperty((String)impVO.getProgressiveSys10Fields().get(field))+
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
                  p.getProperty("hierarchy") +
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
                p.getProperty("hierarchy")+
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
        throw new Exception(res.getErrorMessage());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching ETL process fields",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
    
      try {
        bean.setConn(null);
        langsBean.setConn(null);
      } catch (Exception ex) {}
      
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

