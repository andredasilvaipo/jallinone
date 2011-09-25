package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.*;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.border.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sqltool.java.TableVO;
import org.jallinone.system.customizations.java.CustomFunctionVO;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import java.util.StringTokenizer;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.client.GridController;
import org.jallinone.system.customizations.java.CustomColumnVO;
import javax.swing.ImageIcon;
import java.awt.event.*;
import org.jallinone.sqltool.client.TablePanel;
import org.jallinone.sqltool.java.ColumnVO;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.form.model.client.ValueChangeListener;
import org.openswing.swing.form.model.client.ValueChangeEvent;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame that show a custom function.</p>
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
public class ShowCustomFunctionFrame extends InternalFrame {

  JSplitPane splitPane = new JSplitPane();
  TextAreaControl controlNote = new TextAreaControl();
  JPanel topPanel = new JPanel();
  JPanel filterPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** collection of <columnname,Boolean> used to enable grid data loading */
  private Hashtable whereParamsFilled = new Hashtable();

  /** grid panel */
  private TablePanel panel = null;


  public ShowCustomFunctionFrame(final String functionCode) {
    try {
      jbInit();
      new Thread() {
        public void run() {
          initContent(functionCode);
        }
      }.start();
      setSize(750,500);
      MDIFrame.add(this,true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  public ShowCustomFunctionFrame() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Load query info in another thread...
   */
  private void initContent(String functionCode) {
    try {
      // retrieve custom function info....
      Response res = ClientUtils.getData("loadCustomFunction",functionCode);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }
      CustomFunctionVO vo = (CustomFunctionVO)((VOResponse)res).getVo();
      setTitle(vo.getDescriptionSYS10());
      controlNote.setValue(vo.getNoteSYS16());

      // retrieve columns info...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.FUNCTION_CODE_SYS06,functionCode);
      res = ClientUtils.getData("loadCustomColumns",gridParams);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }
      java.util.List customCols = ((VOListResponse)res).getRows();

      // construct main tables list...
      StringTokenizer st = new StringTokenizer(vo.getMainTablesSYS16(),",");
      ArrayList tables = new ArrayList();
      while(st.hasMoreTokens()) {
        tables.add(st.nextToken());
      }

      // retrieve query info...
      TableVO tableVO = new TableVO(vo.getSql(),tables,true);
      res = ClientUtils.getData("getQueryInfo",tableVO);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }
      tableVO = (TableVO)((VOResponse)res).getVo();
      ColumnVO colVO = null;
      CustomColumnVO customColVO = null;
      boolean found;
      Hashtable whereParamColNames = new Hashtable(); // collection of <columnname,index in where> related to where params...
      boolean paramsRequired = false; // flag used to disable auto data loading...
      ArrayList paramsDefined = new ArrayList(); // input controls defined in filter panel; used to auto set filter panel height into the split pane...
      int count = 0;
      for(int i=0;i<customCols.size();i++) {
        customColVO = (CustomColumnVO)customCols.get(i);

        found = false;
        for(int j=0;j<tableVO.getColumns().size();j++) {
          colVO = (ColumnVO)tableVO.getColumns().get(j);
          if (customColVO.getColumnNameSYS22().equals(colVO.getColumnName())) {
            found = true;
            break;
          }
        }

        if (!found && customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_WHERE)) {
          // parameter in where clause...
          colVO = new ColumnVO(tableVO);
          colVO.setColumnName(customColVO.getColumnNameSYS22());
          colVO.setAttributeName(customColVO.getColumnNameSYS22());
          colVO.setColumnHeaderName(customColVO.getColumnNameSYS22());
          colVO.setColumnType(ApplicationConsts.TYPE_TEXT);
          colVO.setColumnSize(new BigDecimal(255));
          whereParamColNames.put(colVO.getAttributeName(),new Integer(count));
          whereParamsFilled.put(new Integer(count),new Boolean(false));
          count++;
          found = true;
        }

        if (!found)
          continue;

        if (customColVO.isIsParamRequiredSYS22())
          paramsRequired = true;
        if (customColVO.isIsParamSYS22()) {
          if (paramsDefined.size()==0) {
            // add filter panel title...
            TitledBorder titledBorder1 = new TitledBorder(ClientSettings.getInstance().getResources().getResource("filter conditions"));
            titledBorder1.setTitleColor(Color.blue);
            filterPanel.setBorder(titledBorder1);
          }

          // define label + input control for filter panel...
          LabelControl label = new LabelControl();
          label.setText(colVO.getColumnHeaderName());
          InputControl inputControl = null;
          if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_TEXT) ||
              customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_WHERE)) {
            inputControl = new TextControl();
            inputControl.setAttributeName(colVO.getAttributeName());
            if (colVO.getColumnSize().intValue()>10)
              ((TextControl)inputControl).setColumns(Math.min(30,colVO.getColumnSize().intValue()));
            ((TextControl)inputControl).setRequired(customColVO.isIsParamRequiredSYS22());
            ((TextControl)inputControl).setValue(customColVO.getDefaultValueTextSYS22());
            ((TextControl)inputControl).setMaxCharacters(colVO.getColumnSize().intValue());
          }
          else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_NUM)) {
            inputControl = new NumericControl();
            inputControl.setAttributeName(colVO.getAttributeName());
            if (colVO.getColumnSize().intValue()>10)
              ((NumericControl)inputControl).setColumns(Math.min(30,colVO.getColumnSize().intValue()));
            ((NumericControl)inputControl).setRequired(customColVO.isIsParamRequiredSYS22());
            ((NumericControl)inputControl).setValue(customColVO.getDefaultValueNumSYS22());
            ((NumericControl)inputControl).setMaxValue(Math.pow(10d,colVO.getColumnSize().doubleValue()));
            if (colVO.getColumnDec()!=null)
              ((NumericControl)inputControl).setDecimals(colVO.getColumnDec().intValue());
          }
          else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_DATE)) {
            inputControl = new DateControl();
            inputControl.setAttributeName(colVO.getAttributeName());
            ((DateControl)inputControl).setRequired(customColVO.isIsParamRequiredSYS22());
            ((DateControl)inputControl).setValue(customColVO.getDefaultValueNumSYS22());
            if (customColVO.getDefaultValueDateSYS22()!=null &&
                customColVO.getDefaultValueDateSYS22().equals(Boolean.TRUE))
              ((DateControl)inputControl).setValue(new java.sql.Date(System.currentTimeMillis()));
          }
          else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_ENUM) &&
                   customColVO.getConstraintValuesSYS22()!=null &&
                   customColVO.getConstraintValuesSYS22().length()>0) {
            inputControl = new ComboBoxControl();
            inputControl.setAttributeName(colVO.getAttributeName());
            ((ComboBoxControl)inputControl).setRequired(customColVO.isIsParamRequiredSYS22());
            ((ComboBoxControl)inputControl).setValue(customColVO.getDefaultValueNumSYS22());
            ArrayList domainValues = new ArrayList();
            StringTokenizer stt = new StringTokenizer(customColVO.getConstraintValuesSYS22(),",");
            String token = null;
            Domain domain = new Domain(colVO.getColumnName());
            while(stt.hasMoreTokens()) {
              token = stt.nextToken();
              domain.addDomainPair(token,token);
            }
            ((ComboBoxControl)inputControl).setDomain(domain);
          }

          // add label + input control to filter panel...
          filterPanel.add(label,       new GridBagConstraints(0, paramsDefined.size(), 1, 1, 0.0, 0.0
                  ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
          filterPanel.add((JComponent)inputControl,       new GridBagConstraints(1, paramsDefined.size(), 1, 1, 1.0, 0.0
                  ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

          paramsDefined.add(inputControl);
        }
        colVO.setColumnVisible(customColVO.isColumnVisibleSYS22());
        if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_PROG)) {
          colVO.setProgressive(true);
          colVO.setColumnRequired(false);
        }
        if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_TEXT))
          colVO.setDefaultValue(customColVO.getDefaultValueTextSYS22());
        else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_NUM))
          colVO.setDefaultValue(customColVO.getDefaultValueNumSYS22());
        else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_DATE) &&
                 customColVO.getDefaultValueDateSYS22()!=null &&
                 customColVO.getDefaultValueDateSYS22().equals(Boolean.TRUE))
          colVO.setDefaultValue(new java.sql.Date(System.currentTimeMillis()));
        else if (customColVO.getColumnTypeSYS22().equals(ApplicationConsts.TYPE_ENUM) &&
                 customColVO.getConstraintValuesSYS22()!=null &&
                 customColVO.getConstraintValuesSYS22().length()>0) {
          ArrayList domainValues = new ArrayList();
          StringTokenizer stt = new StringTokenizer(customColVO.getConstraintValuesSYS22(),",");
          while(stt.hasMoreTokens())
            domainValues.add(stt.nextToken());
          colVO.setColumnValues(domainValues);
        }
      }


      // create grid panel...
      panel = new TablePanel(tableVO,tableVO.getMainTables().size()==0,!paramsRequired && vo.getAutoLoadDataSYS16());
      splitPane.add(panel, JSplitPane.BOTTOM);
      if (paramsDefined.size()>0)
        splitPane.setDividerLocation((paramsDefined.size()>5?0:150)+Math.min(paramsDefined.size()*30,300));
      else if (vo.getNoteSYS16()!=null && vo.getNoteSYS16().trim().length()>0)
        splitPane.setDividerLocation(150);
      else
        splitPane.setDividerLocation(0);

      for(int i=0;i<paramsDefined.size();i++)
        ((InputControl)paramsDefined.get(i)).addValueChangedListener(new ControlChangedListener(
          (InputControl)paramsDefined.get(i),
          panel.getGrid(),
          (Integer)whereParamColNames.get( ((InputControl)paramsDefined.get(i)).getAttributeName() )
        ));

      // disable reload button until all required controls are filled...
      panel.getGrid().getReloadButton().setEnabled( whereParamColNames.size()==0 );

    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        ex.toString(),
        ClientSettings.getInstance().getResources().getResource("Error"),
        JOptionPane.WARNING_MESSAGE
      );
    }
  }



  private void jbInit() throws Exception {
    setTitle(ClientSettings.getInstance().getResources().getResource("custom function"));
    splitPane.setDividerLocation(0);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    controlNote.setEnabled(false);
    controlNote.setMaxCharacters(2000);
    this.getContentPane().add(splitPane,  BorderLayout.CENTER);
    topPanel.setLayout(borderLayout1);
    topPanel.add(filterPanel,BorderLayout.NORTH);
    topPanel.add(controlNote,BorderLayout.CENTER);
    splitPane.add(new JScrollPane(topPanel), JSplitPane.TOP);
    splitPane.add(new JPanel(), JSplitPane.BOTTOM);
    filterPanel.setLayout(gridBagLayout1);
  }


  /**
   * <p>Title: JAllInOne</p>
   * <p>Description: Value change listener used to add/remove filter conditions onto the grid.</p>
   * @version 1.0
   */
  class ControlChangedListener implements ValueChangeListener {

    /** input control used to filter data on grid */
    private InputControl control = null;

    /** grid to filter */
    private GridControl grid = null;

    /** define if the current control is related to a where param */
    private Integer whereParamIndex;


    public ControlChangedListener(InputControl control,GridControl grid,Integer whereParamIndex) {
      this.control = control;
      this.grid = grid;
      this.whereParamIndex = whereParamIndex;
    }


    public void valueChanged(ValueChangeEvent e) {
      Object value = control.getValue();

      if (whereParamIndex!=null) {
        // disable/enable reload button until all required controls are filled...
        whereParamsFilled.put(whereParamIndex,new Boolean(value!=null && !value.equals("")));
        boolean ok = true;
        Enumeration en = whereParamsFilled.keys();
        while(en.hasMoreElements())
          ok = ok && ((Boolean)whereParamsFilled.get(en.nextElement())).booleanValue();
        panel.getGrid().getReloadButton().setEnabled( ok );

        // set value in grid parameters...
        Vector values = (Vector)grid.getOtherGridParams().get(ApplicationConsts.WHERE_VALUES);
        if (values==null) {
          values = new Vector();
          grid.getOtherGridParams().put(ApplicationConsts.WHERE_VALUES,values);
          for(int i=0;i<=whereParamIndex.intValue();i++)
            values.add(null);
        }
        for(int i=values.size();i<=whereParamIndex.intValue();i++)
          values.add(null);
        values.set(whereParamIndex.intValue(),value);
      }
      else {
        if (value!=null)
          grid.getQuickFilterValues().put(
              control.getAttributeName(),
              new FilterWhereClause[] {
                new FilterWhereClause(control.getAttributeName(),"=",value),
                null
              }
          );
        else
          grid.getQuickFilterValues().remove(control.getAttributeName());
      }

    }

  }


}
