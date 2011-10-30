package org.jallinone.commons.client;

import javax.swing.JPanel;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.MDIFrame;
import java.beans.Beans;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.jallinone.system.customizations.java.WindowCustomizationVO;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.lookup.client.LookupController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class that contains a set of customized column,
 * read from SYS12 table and based on the specified function code.</p>
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
public class CustomizedColumns {


  /**
   * Constructor.
   * @param progressiveSYS13 window identifier
   * @param grid grid control
   */
  public CustomizedColumns(BigDecimal progressiveSYS13,GridControl grid) {
    // retrieve customized input controls list...
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ArrayList cols = applet.getAuthorizations().getCustomizedWindows().getCustomizedFields(progressiveSYS13);
    if (cols.size() > 0) {
      // adding customized columns...
      WindowCustomizationVO inputControlInfo = null;
      TextColumn textColumn = null;
      DateColumn dateColumn = null;
      DecimalColumn decColumn = null;
      IntegerColumn intColumn = null;
      int w = 0;
      for (int i = 0; i < cols.size(); i++) {
        inputControlInfo = (WindowCustomizationVO) cols.get(i);
        if (inputControlInfo.getColumnTypeSYS12().equals("S")) {
          textColumn = new TextColumn();
          textColumn.setHeaderColumnName(inputControlInfo.getDescriptionSYS10());
          textColumn.setColumnName(inputControlInfo.getAttributeNameSYS12());
          textColumn.setMaxCharacters(inputControlInfo.getColumnSizeSYS12().intValue());
          textColumn.setColumnRequired(false);
          textColumn.setColumnFilterable(true);
          textColumn.setEditableOnEdit(true);
          textColumn.setEditableOnInsert(true);
          textColumn.setColumnSortable(true);
          grid.getColumnContainer().add(textColumn,null);
        }
        else if (inputControlInfo.getColumnTypeSYS12().equals("D")) {
          dateColumn = new DateColumn();
          dateColumn.setHeaderColumnName(inputControlInfo.getDescriptionSYS10());
          dateColumn.setColumnName(inputControlInfo.getAttributeNameSYS12());
          dateColumn.setColumnRequired(false);
          dateColumn.setColumnFilterable(true);
          dateColumn.setEditableOnEdit(true);
          dateColumn.setEditableOnInsert(true);
          dateColumn.setColumnSortable(true);
          grid.getColumnContainer().add(dateColumn,null);
        }
        else if (inputControlInfo.getColumnTypeSYS12().equals("N") &&
                 inputControlInfo.getColumnDecSYS12() != null &&
                 inputControlInfo.getColumnDecSYS12().intValue()>0) {
          decColumn = new DecimalColumn();
          decColumn.setHeaderColumnName(inputControlInfo.getDescriptionSYS10());
          decColumn.setColumnName(inputControlInfo.getAttributeNameSYS12());
          decColumn.setColumnRequired(false);
          decColumn.setColumnFilterable(true);
          decColumn.setEditableOnEdit(true);
          decColumn.setEditableOnInsert(true);
          decColumn.setColumnSortable(true);
          decColumn.setDecimals(inputControlInfo.getColumnDecSYS12().intValue());
          decColumn.setMaxValue(Math.pow(10d,inputControlInfo.getColumnSizeSYS12().doubleValue()) - 1);
          grid.getColumnContainer().add(decColumn,null);
        }
        else if (inputControlInfo.getColumnTypeSYS12().equals("N") &&
                 (inputControlInfo.getColumnDecSYS12() == null ||
                  inputControlInfo.getColumnDecSYS12().intValue()==0)) {
          intColumn = new IntegerColumn();
          intColumn.setHeaderColumnName(inputControlInfo.getDescriptionSYS10());
          intColumn.setColumnName(inputControlInfo.getAttributeNameSYS12());
          intColumn.setColumnRequired(false);
          intColumn.setColumnFilterable(true);
          intColumn.setEditableOnEdit(true);
          intColumn.setEditableOnInsert(true);
          intColumn.setColumnSortable(true);
          grid.getColumnContainer().add(intColumn,null);
        }
      }

      Container c = grid.getParent();
      while(c!=null && !(c instanceof InternalFrame))
        c = c.getParent();
      if (c!=null) {
        if (((InternalFrame)c).getSize().width<850)
          ((InternalFrame)c).setSize(
              Math.min(850,((InternalFrame)c).getSize().width+105*cols.size()),
              ((InternalFrame)c).getSize().height
          );
      }
    }
  }


  /**
   * Constructor, used for lookup grids.
   * @param progressiveSYS13 window identifier
   * @param lookup control
   */
  public CustomizedColumns(BigDecimal progressiveSYS13,LookupController controller) {
    // retrieve customized input controls list...
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ArrayList cols = applet.getAuthorizations().getCustomizedWindows().getCustomizedFields(progressiveSYS13);
    if (cols.size() > 0) {
      // adding customized columns...
      WindowCustomizationVO inputControlInfo = null;
      TextColumn textColumn = null;
      DateColumn dateColumn = null;
      DecimalColumn decColumn = null;
      IntegerColumn intColumn = null;
      int w = 0;
      for (int i = 0; i < cols.size(); i++) {
        inputControlInfo = (WindowCustomizationVO) cols.get(i);
        controller.setVisibleColumn(inputControlInfo.getAttributeNameSYS12(),true);
        controller.setHeaderColumnName(inputControlInfo.getAttributeNameSYS12(),inputControlInfo.getDescriptionSYS10());
        controller.setSortableColumn(inputControlInfo.getAttributeNameSYS12(),true);
      }
      controller.setFramePreferedSize(new Dimension(
          Math.min(850,controller.getFramePreferedSize().width+105*cols.size()),
          controller.getFramePreferedSize().height
      ));

    }
  }


}
