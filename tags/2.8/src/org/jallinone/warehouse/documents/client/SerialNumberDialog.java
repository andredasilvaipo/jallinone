package org.jallinone.warehouse.documents.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.event.*;
import org.openswing.swing.client.GenericButton;
import org.jallinone.variants.java.VariantsMatrixVO;
import java.math.BigDecimal;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Input dialog used to prompt user for serial numbers (and bar code optionally) for the specified item.</p>
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
public class SerialNumberDialog extends JDialog {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  JScrollPane scrollPane = new JScrollPane();
  GenericButton closeButton = new GenericButton(new ImageIcon(ClientUtils.getImage("sizes.gif")));
  DefaultTableModel model = new DefaultTableModel(new String[]{
    ClientSettings.getInstance().getResources().getResource("item"),
    ClientSettings.getInstance().getResources().getResource("serial number")
  },0) {

    public boolean isCellEditable(int row, int column) {
      if (column==0)
        return false;
      return true;
    }

  };
  JTable serialNumsGrid = new JTable(model);

  private ArrayList serialNums = null;
  private Object[][] cells = null;
  private VariantsMatrixVO matrixVO = null;


  public SerialNumberDialog(Object[][] cells,VariantsMatrixVO matrixVO,ArrayList serialNums,String item) {
    super(MDIFrame.getInstance(),ClientSettings.getInstance().getResources().getResource("serial numbers from item")+" "+item,true);
    this.cells = cells;
    this.matrixVO = matrixVO;
    this.serialNums = serialNums;
    try {
      jbInit();
      init();

      int w = 350;
      if (cells!=null && matrixVO!=null)
        w += 200;
      setSize(w,200);

      ClientUtils.centerDialog(MDIFrame.getInstance(),this);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Fill in the table.
   */
  private void init() {
    if (cells!=null && matrixVO!=null) {
      serialNumsGrid.getColumnModel().getColumn(0).setPreferredWidth(200);

      Object[] row = null;
      BigDecimal qty = null;
      int pos = 0;
      for(int i=0;i<cells.length;i++) {
        row = cells[i];
        for(int j=0;j<row.length;j++)
          if (cells[i][j]!=null) {
            qty = (BigDecimal)cells[i][j];
            for(int k=0;k<qty.intValue();k++) {
              model.addRow(new Object[] {
                 ((VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i]).getRowDescription()+" "+((VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[j]).getColumnDescription(),
                 serialNums.get(pos)
              });
              pos++;
            }
          }
      }

    }
    else {
      serialNumsGrid.getColumnModel().removeColumn(serialNumsGrid.getColumnModel().getColumn(0));

      for(int i=0;i<serialNums.size();i++) {
        model.addRow(new Object[]{"",serialNums.get(i)});
      }
    }
  }


  private void jbInit() throws Exception {
    serialNumsGrid.setRowHeight(ClientSettings.CELL_HEIGHT);
    serialNumsGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    serialNumsGrid.setSurrendersFocusOnKeystroke(true);
    serialNumsGrid.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
    serialNumsGrid.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
    serialNumsGrid.setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
    serialNumsGrid.setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);

    this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    this.setModal(true);
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    closeButton.setPreferredSize(new Dimension(100, 32));
    closeButton.setText(ClientSettings.getInstance().getResources().getResource("close"));
    closeButton.addActionListener(new SerialNumberDialog_closeButton_actionAdapter(this));
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    buttonsPanel.add(closeButton, null);
    scrollPane.getViewport().add(serialNumsGrid, null);
  }


  void closeButton_actionPerformed(ActionEvent e) {
    if (serialNumsGrid.getCellEditor()!=null)
      serialNumsGrid.getCellEditor().stopCellEditing();

    for(int i=0;i<serialNums.size();i++) {
      serialNums.set(i,model.getValueAt(i,1));
    }
    for(int i=0;i<serialNums.size();i++) {
      if (serialNums.get(i)==null) {
        JOptionPane.showMessageDialog(
            MDIFrame.getInstance(),
            ClientSettings.getInstance().getResources().getResource("you must define a serial number for each row"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.WARNING_MESSAGE
        );
        return;
      }
    }
    setVisible(false);
    dispose();
  }


}

class SerialNumberDialog_closeButton_actionAdapter implements java.awt.event.ActionListener {
  SerialNumberDialog adaptee;

  SerialNumberDialog_closeButton_actionAdapter(SerialNumberDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.closeButton_actionPerformed(e);
  }
}
