package org.jallinone.system.importdata.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.hierarchies.server.HierarchiesBean;
import org.jallinone.hierarchies.server.HierarchyUtil;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.jallinone.system.importdata.java.ImportDescriptorVO;
import org.jallinone.system.languages.java.LanguageVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.UserSessionParameters;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Bean used to import data in a specific table.</p>
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
public class ImportDataBean implements ImportData {


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



  private HierarchiesBean bean;

  public void setBean(HierarchiesBean bean) {
    this.bean = bean;
  }


  public ImportDataBean() {
  }


  public VOResponse importData(
      ETLProcessVO processVO,
      List fieldsVO,
      List langsVO,
      String username,
      String defCompanyCodeSys01SYS03
  ) throws Throwable {
    Connection conn = null;
    PreparedStatement[] insPstmt = null;
    PreparedStatement[] updPstmt = null;
    PreparedStatement[] selPstmt = null;
    PreparedStatement insSYS10Pstmt = null;
    PreparedStatement updSYS10Pstmt = null;
    ResultSet rset = null;
    RowProcessor proc = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);
      
      long time = System.currentTimeMillis();

      ImportDescriptorVO impVO = (ImportDescriptorVO)Class.forName(processVO.getClassNameSYS23()).newInstance();

      LinkedHashMap fieldsToAdd = new LinkedHashMap();
      if (impVO.getFieldsToCopy()!=null && impVO.getFieldsToCopy().size()>0) {
        Iterator it = impVO.getFieldsToCopy().keySet().iterator();
        String fieldName = null;
        while(it.hasNext()) {
          fieldName = (String)impVO.getFieldsToCopy().get(it.next());
          fieldsToAdd.put(fieldName,"");
        }
      }


      HashMap[] treeIndexes = new HashMap[langsVO.size()];
      if (processVO.getProgressiveHIE02()!=null) {
        // retrieve the whole hierarchy currently defined in the database, for each language...
        for(int i=0;i<langsVO.size();i++) {
          LanguageVO vo = (LanguageVO)langsVO.get(i);
          DefaultTreeModel model = HierarchyUtil.loadHierarchy(processVO.getProgressiveHIE02(),vo.getLanguageCodeSYS09(),username);
          treeIndexes[i] = new HashMap();
          indexingTree((DefaultMutableTreeNode)model.getRoot(),treeIndexes[i],processVO.getLevelsSepSYS23(),"");
        }
      }


      // fixed fields, to add as insert values and as where values in update clause...
      ArrayList[] params = new ArrayList[impVO.getTableNames().length];
      String tableName = null;
      int hierarchyLevelIndex = -1;
      for(int k=0;k<impVO.getTableNames().length;k++) {
        tableName = impVO.getTableNames()[k];
        params[k] = new ArrayList();
        if (impVO.isSupportsCompanyCode()) {
          params[k].add(processVO.getCompanyCodeSys01SYS23());
        }
        if (impVO.getHierarchyField()!=null &&
            getFieldName(impVO.getHierarchyField(),tableName) != null) {
          params[k].add(null);
          hierarchyLevelIndex = params[k].size()-1;
        }
        if (impVO.getSubTypeField()!=null &&
            getFieldName(impVO.getSubTypeField(),tableName) != null) {
          params[k].add(processVO.getSubTypeValueSYS23());
        }
        if (impVO.getSubTypeField2()!=null &&
            getFieldName(impVO.getSubTypeField2(),tableName) != null) {
          params[k].add(processVO.getSubTypeValue2SYS23());
        }
      }


      insPstmt = new PreparedStatement[impVO.getTableNames().length];
      updPstmt = new PreparedStatement[impVO.getTableNames().length];
      selPstmt = new PreparedStatement[impVO.getTableNames().length];

      // prepare SQL for insert ops...
      String fieldName = null;
      String insSQL = null;
      ETLProcessFieldVO vo = null;
      HashSet alreadyAdded = new HashSet();
      int count = 0;
      for(int k=0;k<impVO.getTableNames().length;k++) {
        alreadyAdded.clear();
        tableName = impVO.getTableNames()[k];
        insSQL = "INSERT INTO " + tableName + "(";
        count = 0;
        if (impVO.isSupportsCompanyCode()) {
          insSQL += "COMPANY_CODE_SYS01,";
          count++;
        }
        if (impVO.getHierarchyField()!=null &&
            getFieldName(impVO.getHierarchyField(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getHierarchyField(),tableName))) {
          insSQL += getFieldName(impVO.getHierarchyField(),tableName)+",";
          alreadyAdded.add(getFieldName(impVO.getHierarchyField(),tableName));
          count++;
        }
        if (impVO.getSubTypeField()!=null &&
            getFieldName(impVO.getSubTypeField(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getSubTypeField(),tableName))) {
          insSQL += getFieldName(impVO.getSubTypeField(),tableName) + ",";
          alreadyAdded.add(getFieldName(impVO.getSubTypeField(),tableName));
          count++;
        }
        if (impVO.getSubTypeField2()!=null &&
            getFieldName(impVO.getSubTypeField2(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getSubTypeField2(),tableName))) {
          insSQL += getFieldName(impVO.getSubTypeField2(),tableName) + ",";
          alreadyAdded.add(getFieldName(impVO.getSubTypeField2(),tableName));
          count++;
        }
        if (impVO.getProgressiveFieldName()!=null &&
            getFieldName(impVO.getProgressiveFieldName(),tableName)!=null &&
            !alreadyAdded.contains(getFieldName(impVO.getProgressiveFieldName(),tableName))) {
          insSQL += getFieldName(impVO.getProgressiveFieldName(),tableName) + ",";
          alreadyAdded.add(getFieldName(impVO.getProgressiveFieldName(),tableName));
          count++;
        }
        Iterator it = fieldsToAdd.keySet().iterator();
        while(it.hasNext()) {
           fieldName = it.next().toString();
           if (getFieldName(fieldName,tableName)!=null &&
               !alreadyAdded.contains(getFieldName(fieldName,tableName))) {
             insSQL += getFieldName(fieldName, tableName) + ",";
             alreadyAdded.add(getFieldName(fieldName,tableName));
             count++;
           }
        }
        for (int i = 0; i < fieldsVO.size(); i++) {
          vo = (ETLProcessFieldVO) fieldsVO.get(i);
          if (getFieldName(vo.getFieldNameSYS24(),tableName)!=null &&
              !alreadyAdded.contains(getFieldName(vo.getFieldNameSYS24(),tableName))) {
            insSQL += getFieldName(vo.getFieldNameSYS24(), tableName) + ",";
            alreadyAdded.add(getFieldName(vo.getFieldNameSYS24(),tableName));
            count++;
          }
        }
        it = impVO.getDefaultFields().keySet().iterator();
        while (it.hasNext()) {
          fieldName = it.next().toString();
          if (getFieldName(fieldName,tableName)!=null) {
            insSQL += getFieldName(fieldName, tableName) + ",";
            count++;
          }
        }
        insSQL = insSQL.substring(0, insSQL.length() - 1);
        insSQL += ") VALUES(";
        for(int i=0;i<count;i++)
          insSQL += "?,";
        insSQL = insSQL.substring(0, insSQL.length() - 1);
        insSQL += ")";
        insPstmt[k] = conn.prepareStatement(insSQL);

      } // end for on tables...


      // prepare SQL for update ops...
      String updSQL = null;
      Iterator it = null;
      for(int k=0;k<impVO.getTableNames().length;k++) {
        tableName = impVO.getTableNames()[k];
        updSQL = "UPDATE " + tableName + " SET ";
        alreadyAdded.clear();
        for (int i = 0; i < fieldsVO.size(); i++) {
          vo = (ETLProcessFieldVO) fieldsVO.get(i);
          if (getFieldName(vo.getFieldNameSYS24(),tableName)!=null &&
              !alreadyAdded.contains(getFieldName(vo.getFieldNameSYS24(), tableName))) {
            updSQL += getFieldName(vo.getFieldNameSYS24(), tableName) + "=?,";
            alreadyAdded.add(getFieldName(vo.getFieldNameSYS24(), tableName));
          }
        }
        if (impVO.getHierarchyField()!=null &&
            getFieldName(impVO.getHierarchyField(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getHierarchyField(),tableName))) {
          updSQL += getFieldName(impVO.getHierarchyField(),tableName)+"=?,";
          alreadyAdded.add(getFieldName(impVO.getHierarchyField(),tableName));
        }
        updSQL = updSQL.substring(0, updSQL.length() - 1);
        updSQL += " WHERE ";
        if (impVO.isSupportsCompanyCode()) {
          updSQL += "COMPANY_CODE_SYS01=? and ";
        }
//        if (getFieldName(impVO.getHierarchyField(),tableName) != null &&
//            !alreadyAdded.contains(getFieldName(impVO.getHierarchyField(),tableName))) {
//          updSQL += getFieldName(impVO.getHierarchyField(),tableName)+"=? and ";
//          alreadyAdded.add(getFieldName(impVO.getHierarchyField(),tableName));
//        }
        if (getFieldName(impVO.getSubTypeField(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getSubTypeField(),tableName))) {
          updSQL += getFieldName(impVO.getSubTypeField(),tableName) + "=? and ";
          alreadyAdded.add(getFieldName(impVO.getSubTypeField(),tableName));
        }
        if (getFieldName(impVO.getSubTypeField2(),tableName) != null &&
            !alreadyAdded.contains(getFieldName(impVO.getSubTypeField2(),tableName))) {
          updSQL += getFieldName(impVO.getSubTypeField2(),tableName) + "=? and ";
          alreadyAdded.add(getFieldName(impVO.getSubTypeField2(),tableName));
        }
        it = impVO.getPkFields().iterator();
        while (it.hasNext()) {
          fieldName = it.next().toString();
          if (getFieldName(fieldName,tableName)!=null)
            updSQL += getFieldName(fieldName,tableName)+"=? and ";
        }
        updSQL = updSQL.substring(0, updSQL.length() - 4);
        updPstmt[k] = conn.prepareStatement(updSQL);

      } // end for on tables...



      // prepare SQL for insert description ops...
      String insSYS10 = "INSERT INTO SYS10_TRANSLATIONS(PROGRESSIVE,LANGUAGE_CODE,DESCRIPTION,COMPANY_CODE_SYS01) VALUES(?,?,?,?)";
      insSYS10Pstmt = conn.prepareStatement(insSYS10);


      // prepare SQL for update description ops...
      String updSYS10 = "UPDATE SYS10_TRANSLATIONS SET DESCRIPTION=? WHERE PROGRESSIVE=? and LANGUAGE_CODE=?";
      updSYS10Pstmt = conn.prepareStatement(updSYS10);


      // prepare SQL for select ops...
      String selSQL = null;
      for(int k=0;k<impVO.getTableNames().length;k++) {
        alreadyAdded.clear();
        tableName = impVO.getTableNames()[k];
        selSQL = "SELECT ";
        it = impVO.getProgressiveSys10Fields().keySet().iterator();
        while (it.hasNext()) {
          fieldName = it.next().toString();
          if (getFieldName(fieldName,tableName)!=null &&
              !alreadyAdded.contains(getFieldName(fieldName,tableName))) {
            selSQL += getFieldName(fieldName, tableName) + ",";
            alreadyAdded.add(getFieldName(fieldName,tableName));
          }
        }
        if (selSQL.length()>7) {
          selSQL = selSQL.substring(0,selSQL.length()-1);
        }
        else {
          selSQL += "*";
        }
        selSQL += " FROM "+tableName+" WHERE ";
        if (impVO.isSupportsCompanyCode()) {
          selSQL += "COMPANY_CODE_SYS01=? and ";
        }
//        if (getFieldName(impVO.getHierarchyField(),tableName) != null) {
//          selSQL += getFieldName(impVO.getHierarchyField(),tableName)+"=? and ";
//        }
        if (getFieldName(impVO.getSubTypeField(),tableName) != null) {
          selSQL += getFieldName(impVO.getSubTypeField(),tableName) + "=? and ";
        }
        if (getFieldName(impVO.getSubTypeField2(),tableName) != null) {
          selSQL += getFieldName(impVO.getSubTypeField2(),tableName) + "=? and ";
        }
        it = impVO.getPkFields().iterator();
        while (it.hasNext()) {
          fieldName = it.next().toString();
          if (getFieldName(fieldName,tableName)!=null)
            selSQL += "("+getFieldName(fieldName,tableName)+"=? or "+getFieldName(fieldName,tableName)+" is null) and ";
        }
        selSQL = selSQL.substring(0, selSQL.length() - 4);
        selPstmt[k] = conn.prepareStatement(selSQL);

      } // end for on tables...


      // indexing fields as <FieldEntry,Integer index>
      HashMap fields = new HashMap();
      int pos = 0;
      for(int i=0;i<fieldsVO.size();i++) {
        vo = (ETLProcessFieldVO)fieldsVO.get(i);
        fields.put(
          new FieldEntry(vo.getLanguageCodeSYS24(),vo.getFieldNameSYS24()),
          new Integer(pos++)
        );
      }
      LanguageVO langVO = null;
      if (processVO.getProgressiveHIE02()!=null) {
        for(int i=0;i<langsVO.size();i++) {
          langVO = (LanguageVO) langsVO.get(i);
          fields.put(
            new FieldEntry(vo.getLanguageCodeSYS24(),impVO.getHierarchyField()),
            new Integer(pos++)
          );
        }
      }

      // indexing fields as <field name,Class type>
      HashMap classes = new HashMap();
      for(int i=0;i<impVO.getFields().length;i++) {
        classes.put(
          impVO.getFields()[i],
          impVO.getFieldsType()[i]
        );
      }
      it = impVO.getProgressiveSys10Fields().keySet().iterator();
      while(it.hasNext()) {
        fieldName = it.next().toString();
        for(int i=0;i<langsVO.size();i++) {
          langVO = (LanguageVO) langsVO.get(i);
          classes.put(
            fieldName,
            String.class//BigDecimal.class
          );
        }
      }
      if (processVO.getProgressiveHIE02()!=null) {
        for(int i=0;i<langsVO.size();i++) {
          langVO = (LanguageVO) langsVO.get(i);
          classes.put(
            impVO.getHierarchyField(),
            String.class//BigDecimal.class
          );
        }
      }


      if (processVO.getFileFormatSYS23().equals(ApplicationConsts.FILE_FORMAT_TXT))
        proc = new TXTRowProcessor();
      else if (processVO.getFileFormatSYS23().equals(ApplicationConsts.FILE_FORMAT_CSV1))
        proc = new CSVRowProcessor(";");
      else if (processVO.getFileFormatSYS23().equals(ApplicationConsts.FILE_FORMAT_CSV2))
        proc = new CSVRowProcessor(",");
      else if (processVO.getFileFormatSYS23().equals(ApplicationConsts.FILE_FORMAT_XLS))
        proc = new XLSRowProcessor();

      InputStream in = null;
      if (processVO.getLocalFile() != null &&
          processVO.getLocalFile().length > 0)
        in = new ByteArrayInputStream(processVO.getLocalFile());
      else {
        File f = new File(processVO.getFilenameSYS23());
        if (!f.exists())
          throw new Exception("File non exists");
        in = new BufferedInputStream(new FileInputStream(f));
      }
      proc.openFile(in);

      Object[] row = null;
      pos = 1;
      String progFieldName = null;
      BigDecimal progressiveSYS10 = null;
      long rows = 0;
      long rowsInserted = 0;
      long rowsUpdated = 0;
      FieldEntry pk = null;
      String[] levelsDesc = null;
      BigDecimal progressiveHIE01 = null;
      int index = -1;
      Object value = null;
      BigDecimal progressive = null;
      Integer indexInt = null;
      long notYetCommited = 0;
      while((row=proc.getNextRow(fieldsVO,impVO,classes))!=null) {
        rows++;

        for(int k=0;k<impVO.getTableNames().length;k++) {
          tableName = impVO.getTableNames()[k];


          // check if record exists...
          pos = 1;
          if (impVO.isSupportsCompanyCode()) {
            selPstmt[k].setObject(pos++,processVO.getCompanyCodeSys01SYS23());
          }
          if (getFieldName(impVO.getSubTypeField(),tableName) != null) {
            selPstmt[k].setObject(pos++,processVO.getSubTypeValueSYS23());
          }
          if (getFieldName(impVO.getSubTypeField2(),tableName) != null) {
            selPstmt[k].setObject(pos++,processVO.getSubTypeValue2SYS23());
          }

//          for(int i=0;i<params[k].size();i++)
//            selPstmt[k].setObject(pos++,params[k].get(i));
          it = impVO.getPkFields().iterator();
          while (it.hasNext()) {
            fieldName = it.next().toString();
            if (getFieldName(fieldName,tableName)!=null)
              selPstmt[k].setObject(pos++,row[((Integer)fields.get(new FieldEntry(ApplicationConsts.JOLLY,fieldName))).intValue()]);
          }
          rset = selPstmt[k].executeQuery();

          if (rset.next()) {
            // record must be updated...
            if (impVO.getProgressiveSys10Fields().size()>0) {
              // record already exists: fetch progressives...
              it = impVO.getProgressiveSys10Fields().keySet().iterator();
              pos = 1;
              while(it.hasNext()) {
                progFieldName = it.next().toString();
                progressiveSYS10 = rset.getBigDecimal(pos++);

                // update record in SYS10...
                for(int i=0;i<langsVO.size();i++) {
                  langVO = (LanguageVO)langsVO.get(i);
                  pk = new FieldEntry(langVO.getLanguageCodeSYS09(),progFieldName);
                  indexInt = (Integer)fields.get(pk);
                  if (indexInt==null)
                    // in case of not mandatory progressiveSYS10 (e.g. additional description...)
                    // which has not been mapped in SYS24...
                    continue;
                  index = indexInt.intValue();
                  updSYS10Pstmt.setObject(1,row[index]);
                  updSYS10Pstmt.setBigDecimal(2,progressiveSYS10);
                  updSYS10Pstmt.setString(3,langVO.getLanguageCodeSYS09());
                  updSYS10Pstmt.execute();
                  row[index] = progressiveSYS10;
                }
              }
            }

            rset.close();


            if (processVO.getProgressiveHIE02()!=null) {
              // retrieve hierarchy level...
              langVO = (LanguageVO)langsVO.get(0);
              pk = new FieldEntry(langVO.getLanguageCodeSYS09(),impVO.getHierarchyField());
              levelsDesc = new String[langsVO.size()];
              levelsDesc[0] = (String)row[((Integer)fields.get(pk)).intValue()];
              progressiveHIE01 = (BigDecimal)treeIndexes[0].get(levelsDesc[0]);
              if (progressiveHIE01==null) {
                // the levels do not exist yet: must be inserted...

                for(int i=0;i<langsVO.size();i++) {
                  langVO = (LanguageVO)langsVO.get(i);
                  pk = new FieldEntry(langVO.getLanguageCodeSYS09(),impVO.getHierarchyField());
                  levelsDesc[i] = (String)row[((Integer)fields.get(pk)).intValue()];
                }
                progressiveHIE01 = insertLevels(
                		conn,
                		langVO.getLanguageCodeSYS09(),
                		username,
                		langsVO,
                		levelsDesc,
                		processVO.getLevelsSepSYS23(),
                		treeIndexes,
                		processVO.getProgressiveHIE02(),
                		processVO.getCompanyCodeSys01SYS23() // ???defCompanyCodeSys01SYS03
                );
              }

              for(int i=0;i<langsVO.size();i++) {
                langVO = (LanguageVO)langsVO.get(i);
                pk = new FieldEntry(langVO.getLanguageCodeSYS09(),impVO.getHierarchyField());
                row[((Integer)fields.get(pk)).intValue()] = progressiveHIE01;
              }
              params[k].set(hierarchyLevelIndex,progressiveHIE01);
            }


            // update main record...
            pos = 1;
//            for(int i=0;i<row.length;i++)
//              updPstmt[k].setObject(pos++,row[i]);

            alreadyAdded.clear();
            for (int i = 0; i < fieldsVO.size(); i++) {
              vo = (ETLProcessFieldVO) fieldsVO.get(i);
              if (getFieldName(vo.getFieldNameSYS24(),tableName)!=null &&
                  !alreadyAdded.contains(getFieldName(vo.getFieldNameSYS24(), tableName))) {
                updPstmt[k].setObject(pos++,row[i]);
                alreadyAdded.add(getFieldName(vo.getFieldNameSYS24(), tableName));
              }
            }
            if (impVO.getHierarchyField()!=null &&
                getFieldName(impVO.getHierarchyField(),tableName) != null &&
                !alreadyAdded.contains(getFieldName(impVO.getHierarchyField(),tableName))) {
              updPstmt[k].setObject(pos++,row[((Integer)fields.get(new FieldEntry(ApplicationConsts.JOLLY,impVO.getHierarchyField()))).intValue()]);
              alreadyAdded.add(getFieldName(impVO.getHierarchyField(),tableName));
            }

            if (impVO.isSupportsCompanyCode()) {
              updPstmt[k].setObject(pos++,processVO.getCompanyCodeSys01SYS23());
            }
            if (getFieldName(impVO.getSubTypeField(),tableName) != null) {
              updPstmt[k].setObject(pos++,processVO.getSubTypeValueSYS23());
            }
            if (getFieldName(impVO.getSubTypeField2(),tableName) != null) {
              updPstmt[k].setObject(pos++,processVO.getSubTypeValue2SYS23());
            }

//            for(int i=0;i<params[k].size();i++)
//              updPstmt[k].setObject(pos++,params[k].get(i));

            it = impVO.getPkFields().iterator();
            while (it.hasNext()) {
              fieldName = it.next().toString();
              if (getFieldName(fieldName,tableName)!=null)
                updPstmt[k].setObject(pos++,row[((Integer)fields.get(new FieldEntry(ApplicationConsts.JOLLY,fieldName))).intValue()]);
            }
            updPstmt[k].execute();

            rowsUpdated++;
            notYetCommited++;
          }
          else {
            // record does not exist yet...
            rset.close();

            if (impVO.getProgressiveSys10Fields().size()>0) {
              // create progressives and insert records in SYS10...
              it = impVO.getProgressiveSys10Fields().keySet().iterator();
              pos = 1;

              while(it.hasNext()) {
                progFieldName = it.next().toString();

                progressiveSYS10 = CompanyProgressiveUtils.getInternalProgressive(
                		processVO.getCompanyCodeSys01SYS23(), // ??? defCompanyCodeSys01SYS03,
                		"SYS10_TRANSLATIONS",
                		"PROGRESSIVE",
                		conn
                );

                // insert record in SYS10...
                for(int i=0;i<langsVO.size();i++) {
                  langVO = (LanguageVO)langsVO.get(i);
                  pk = new FieldEntry(langVO.getLanguageCodeSYS09(),progFieldName);
                  indexInt = (Integer)fields.get(pk);
                  if (indexInt==null)
                    // in case of not mandatory progressiveSYS10 (e.g. additional description...)
                    // which has not been mapped in SYS24...
                    continue;
                  index = indexInt.intValue();
                  insSYS10Pstmt.setBigDecimal(1,progressiveSYS10);
                  insSYS10Pstmt.setString(2,langVO.getLanguageCodeSYS09());
                  insSYS10Pstmt.setObject(3,row[index]);
                  insSYS10Pstmt.setString(4,processVO.getCompanyCodeSys01SYS23());
                  insSYS10Pstmt.execute();
                  row[index] = progressiveSYS10;
                }
              }
            }


            if (processVO.getProgressiveHIE02()!=null) {
              // retrieve hierarchy level...
              langVO = (LanguageVO)langsVO.get(0);
              pk = new FieldEntry(langVO.getLanguageCodeSYS09(),impVO.getHierarchyField());
              levelsDesc = new String[langsVO.size()];
              levelsDesc[0] = (String)row[((Integer)fields.get(pk)).intValue()];
              progressiveHIE01 = (BigDecimal)treeIndexes[0].get(levelsDesc[0]);
              if (progressiveHIE01==null) {
                // the levels do not exist yet: must be inserted...

                for(int i=0;i<langsVO.size();i++) {
                  langVO = (LanguageVO)langsVO.get(i);
                  pk = new FieldEntry(langVO.getLanguageCodeSYS09(),impVO.getHierarchyField());
                  levelsDesc[i] = (String)row[((Integer)fields.get(pk)).intValue()];
                }
                progressiveHIE01 = insertLevels(
                		conn,
                		langVO.getLanguageCodeSYS09(),
                		username,
                		langsVO,
                		levelsDesc,
                		processVO.getLevelsSepSYS23(),
                		treeIndexes,
                		processVO.getProgressiveHIE02(),
                		processVO.getCompanyCodeSys01SYS23() // ???defCompanyCodeSys01SYS03
                );
              }
              params[k].set(hierarchyLevelIndex,progressiveHIE01);
            }


            // insert main record...
            pos = 1;
            alreadyAdded.clear();

            if (impVO.getHierarchyField()!=null &&
                getFieldName(impVO.getHierarchyField(),tableName) != null &&
                !alreadyAdded.contains(getFieldName(impVO.getHierarchyField(),tableName))) {
              alreadyAdded.add(getFieldName(impVO.getHierarchyField(),tableName));
            }
            if (impVO.getSubTypeField()!=null &&
                getFieldName(impVO.getSubTypeField(),tableName) != null &&
                !alreadyAdded.contains(getFieldName(impVO.getSubTypeField(),tableName))) {
              alreadyAdded.add(getFieldName(impVO.getSubTypeField(),tableName));
            }
            if (impVO.getSubTypeField2()!=null &&
                getFieldName(impVO.getSubTypeField2(),tableName) != null &&
                !alreadyAdded.contains(getFieldName(impVO.getSubTypeField2(),tableName))) {
              alreadyAdded.add(getFieldName(impVO.getSubTypeField2(),tableName));
            }
            if (impVO.getProgressiveFieldName()!=null &&
                getFieldName(impVO.getProgressiveFieldName(),tableName)!=null &&
                !alreadyAdded.contains(getFieldName(impVO.getProgressiveFieldName(),tableName))) {
              alreadyAdded.add(getFieldName(impVO.getProgressiveFieldName(),tableName));
            }

            for(int i=0;i<params[k].size();i++)
              insPstmt[k].setObject(pos++,params[k].get(i));

            // check for progressive to calculate...
            if (impVO.getProgressiveFieldName()!=null &&
                getFieldName(impVO.getProgressiveFieldName(),tableName)!=null) {
              progressive = CompanyProgressiveUtils.getInternalProgressive(
            	 processVO.getCompanyCodeSys01SYS23(), // ???defCompanyCodeSys01SYS03,
                 tableName,
                 getFieldName(impVO.getProgressiveFieldName(),tableName),
                 conn
              );
              insPstmt[k].setBigDecimal(pos++,progressive);
            }

            it = fieldsToAdd.keySet().iterator();
            while(it.hasNext()) {
               fieldName = it.next().toString();
               if (getFieldName(fieldName,tableName)!=null &&
                   !alreadyAdded.contains(getFieldName(fieldName,tableName))) {
                 insPstmt[k].setObject(pos++,fieldsToAdd.get(fieldName));
                 alreadyAdded.add(getFieldName(fieldName,tableName));
               }
            }

            for (int i = 0; i < fieldsVO.size(); i++) {
              vo = (ETLProcessFieldVO) fieldsVO.get(i);
              if (getFieldName(vo.getFieldNameSYS24(),tableName)!=null &&
                  !alreadyAdded.contains(getFieldName(vo.getFieldNameSYS24(), tableName))) {
                insPstmt[k].setObject(pos++,row[i]);
                alreadyAdded.add(getFieldName(vo.getFieldNameSYS24(), tableName));
              }
            }

//            for(int i=0;i<row.length;i++)
//              insPstmt[k].setObject(pos++,row[i]);
            it = impVO.getDefaultFields().keySet().iterator();
            while (it.hasNext()) {
              fieldName = it.next().toString();
              if (getFieldName(fieldName,tableName)!=null)
                insPstmt[k].setObject(pos++,impVO.getDefaultFields().get(fieldName));
            }
            insPstmt[k].execute();

            rowsInserted++;
            notYetCommited++;
          }



          if (impVO.getFieldsToCopy().size()>0) {
            // copy values related to table just processed to other fields...
            it = impVO.getFieldsToCopy().keySet().iterator();
            while (it.hasNext()) {
              fieldName = it.next().toString(); // e.g. REG04_SUBJECTS.PROGRESSIVE
              if (getFieldName(fieldName, tableName) != null &&
                  impVO.getFieldsToCopy().get(fieldName)!=null) { // e.g. SAL07_CUSTOMERS.PROGRESSIVE_REG04
                if (fields.get(new FieldEntry(ApplicationConsts.JOLLY,fieldName))!=null)
                  value = row[((Integer)fields.get(new FieldEntry(ApplicationConsts.JOLLY,fieldName))).intValue()];
                else if (fieldName.equals(impVO.getProgressiveFieldName())) // e.g. SAL07_CUSTOMERS.PROGRESSIVE_REG04
                  value = progressive;
                fieldsToAdd.put(impVO.getFieldsToCopy().get(fieldName),value);
                //row[((Integer)fields.get(new FieldEntry(ApplicationConsts.JOLLY,(String)impVO.getFieldsToCopy().get(fieldName)))).intValue()] = value;
              }
            }
          }

        } // end for on tables...

        if (notYetCommited>1000) {
          notYetCommited = 0;
          conn.commit();
        }

      } // end while


      proc.closeFile();

      long secs = (System.currentTimeMillis()-time)/1000L;
      String msg = "Data import '"+processVO.getDescriptionSYS23()+"' completed in ";
      if (secs/60L>0)
        msg = (secs/60L)+" minutes";
      else
        msg += secs+" seconds";
      msg += "\nRows read: "+rows+" - rows inserted: "+rowsInserted+" - rows updated: "+rowsUpdated;

      Logger.info(
        username,
        this.getClass().getName(),
        "importData",
        msg
      );

      return new VOResponse(
        String.valueOf(secs/1000L)+"\n"+
        String.valueOf(rows) + "\n" +
        String.valueOf(rowsInserted) + "\n" +
        String.valueOf(rowsUpdated) + "\n"
      );
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"importData",ex.getMessage(),ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        if (proc!=null)
          proc.closeFile();
      }
      catch (Throwable ex2) {
      }
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        for(int i=0;i<insPstmt.length;i++)
          if (insPstmt[i]!=null)
            insPstmt[i].close();
      }
      catch (Exception ex1) {
      }
      try {
        for(int i=0;i<updPstmt.length;i++)
          if (updPstmt[i]!=null)
            updPstmt[i].close();
      }
      catch (Exception ex1) {
      }
      try {
        for(int i=0;i<selPstmt.length;i++)
          if (selPstmt[i]!=null)
            selPstmt[i].close();
      }
      catch (Exception ex1) {
      }
      try {
        insSYS10Pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        updSYS10Pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
		bean.setConn(null);
      } catch (Exception e) {
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
   * Index tree nodes, using as key the combination of levels descriptions.
   * @param node DefaultMutableTreeNode
   * @param map collection of pairs <leveldescr1 levelsSeparator leveldescr2 levelsSeparator ...,progressiveHIE01>
   */
  private void indexingTree(DefaultMutableTreeNode node,HashMap map,String levelsSeparator,String parentDesc) {
    HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
    DefaultMutableTreeNode childNode = null;

    map.put(parentDesc+vo.getDescriptionSYS10(),vo.getProgressiveHIE01());

    for(int i=0;i<node.getChildCount();i++) {
      childNode = (DefaultMutableTreeNode)node.getChildAt(i);
      vo = (HierarchyLevelVO)node.getUserObject();
      indexingTree(childNode,map,levelsSeparator,parentDesc+vo.getDescriptionSYS10()+levelsSeparator);
    }
  }


  /**
   * Insert levels into the hierarchy, for each supported language.
   */
  private BigDecimal insertLevels(Connection conn,String serverLanguageId,String username,List langsVO,String[] levelsDesc,String levelsSeparator,HashMap[] treeIndexes,BigDecimal progressiveHIE02,String defCompanyCodeSys01SYS03) throws Throwable {
    BigDecimal progressiveHIE01 = null;
    BigDecimal parentProgressiveHIE01 = null;
    LanguageVO langVO = null;

    String sep = levelsSeparator;
    if (sep.equals("|"))
      sep = "\\|";

    String[][] splittedLevelsDesc = new String[langsVO.size()][];
    for(int i=0;i<langsVO.size();i++)
      splittedLevelsDesc[i] = levelsDesc[i].split(sep);
    String aux[] = new String[langsVO.size()];
    for(int i=0;i<langsVO.size();i++)
      aux[i] = "";
    Response res = null;
    HierarchyLevelVO vo = null;

    langVO = (LanguageVO)langsVO.get(0);
    for(int j=0;j<splittedLevelsDesc[0].length;j++) {
      for(int i=0;i<langsVO.size();i++) {
        if (j > 0)
          aux[i] += levelsSeparator;
        aux[i] += splittedLevelsDesc[i][j];
      }
      progressiveHIE01 = (BigDecimal)treeIndexes[0].get(aux[0]);
      if (progressiveHIE01==null) {
        // level to insert, for each language...
        vo = new HierarchyLevelVO();
        vo.setDescriptionSYS10(splittedLevelsDesc[0][j]);
        vo.setLevelHIE01(new BigDecimal(j));
        vo.setProgressiveHie02HIE01(progressiveHIE02);
        vo.setProgressiveHie01HIE01(parentProgressiveHIE01);
        res = bean.insertLevel(vo,serverLanguageId,username,defCompanyCodeSys01SYS03);
        if (!res.isError()) {
          vo = (HierarchyLevelVO)((VOResponse)res).getVo();
          progressiveHIE01 = vo.getProgressiveHIE01();
          treeIndexes[0].put(aux[0],progressiveHIE01);

          for(int i=0;i<langsVO.size();i++) {
            langVO = (LanguageVO)langsVO.get(i);
            TranslationUtils.updateTranslation(splittedLevelsDesc[0][j],splittedLevelsDesc[i][j],progressiveHIE01,langVO.getLanguageCodeSYS09(),conn);
            treeIndexes[i].put(aux[i],progressiveHIE01);
          }
        }
        else
          throw new Exception(res.getErrorMessage());

      }
      parentProgressiveHIE01 = progressiveHIE01;
    }

    return progressiveHIE01;
  }


  private String getFieldName(String tableAndFieldName,String tableName) {
    if (tableAndFieldName==null)
      return null;
    int index = tableAndFieldName.indexOf(tableName+".");
    if (index==-1)
      return null;
    else
      return tableAndFieldName.substring(tableName.length()+1);
  }


}
