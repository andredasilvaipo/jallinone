package org.jallinone.system.importdata.client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import org.jallinone.system.importdata.java.CSVUtils;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.usermodel.*;
import org.openswing.swing.client.OptionPane;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.jallinone.system.importdata.java.SelectableFieldVO;
import java.math.BigDecimal;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Preview panel, used in import data frame.</p>
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
public class PreviewPanel extends JPanel {

  private ArrayList row = new ArrayList();
  private String fileFormat = null;
  private ArrayList delimiters = new ArrayList();
  private GridControl grid = null;


  public PreviewPanel(GridControl grid) {
    this.grid = grid;

    addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==2 && "TXT".equals(fileFormat)) {
          int x1 = 0;
          if (e.getX()>=5)
            x1 = e.getX()-5;
          int x2 = getWidth();
          if (e.getX()+5<getWidth())
            x2 = e.getX()+5;

          boolean found = false;
          for(int i=x1;i<x2;i++) {
            found = found || delimiters.remove(new Integer(i));
          }
          if (!found)
            delimiters.add(new Integer(e.getX()));
          repaint();
        }
        else if (SwingUtilities.isRightMouseButton(e) &&
                 PreviewPanel.this.grid.getSelectedRow()!=-1) {
          SelectableFieldVO vo = (SelectableFieldVO)PreviewPanel.this.grid.getVOListTableModel().getObjectForRow(PreviewPanel.this.grid.getSelectedRow());
          int x1 = 0;
          int x2 = getWidth();
          int del = -1;
          int pos = -1;
          for(int i=0;i<delimiters.size();i++) {
            del = ((Integer)delimiters.get(i)).intValue();
            if (del<e.getX() && x1<del)
              x1 = del;
            else if (del>e.getX()&& del<x2)
              x2 = del;
          }
          if (x1!=-1 && x2!=-1) {
            vo.setSelected(true);

            if ("TXT".equals(fileFormat)) {
              vo.getField().setStartPosSYS24(new BigDecimal(x1/7+1));
              vo.getField().setEndPosSYS24(new BigDecimal(x2/7));
              if(PreviewPanel.this.grid.getSelectedRow()+1<PreviewPanel.this.grid.getVOListTableModel().getRowCount()) {
                PreviewPanel.this.grid.setRowSelectionInterval(PreviewPanel.this.grid.getSelectedRow()+1,PreviewPanel.this.grid.getSelectedRow()+1);
              }
            }
            else {
              if (x1==0) {
                vo.getField().setPosSYS24(new BigDecimal(1));
                if(PreviewPanel.this.grid.getSelectedRow()+1<PreviewPanel.this.grid.getVOListTableModel().getRowCount()) {
                  PreviewPanel.this.grid.setRowSelectionInterval(PreviewPanel.this.grid.getSelectedRow()+1,PreviewPanel.this.grid.getSelectedRow()+1);
                }
              }
              else
                for(int i=1;i<delimiters.size();i++) {
                  if (((Integer)delimiters.get(i-1)).intValue()==x1 &&
                      ((Integer)delimiters.get(i)).intValue()==x2) {
                    vo.getField().setPosSYS24(new BigDecimal(i+1));
                    if(PreviewPanel.this.grid.getSelectedRow()+1<PreviewPanel.this.grid.getVOListTableModel().getRowCount()) {
                      PreviewPanel.this.grid.setRowSelectionInterval(PreviewPanel.this.grid.getSelectedRow()+1,PreviewPanel.this.grid.getSelectedRow()+1);
                    }
                    break;
                  }
                }
            }

            PreviewPanel.this.grid.repaint();
          }
        }

      }

    });
  }


  public void paint(Graphics g) {
    super.paint(g);
    g.setFont(new Font("Courier",Font.PLAIN,12));
    if (fileFormat!=null && row.size()>0) {
      if ("TXT".equals(fileFormat)) {
        g.drawString(row.get(0).toString(),1,15);
      }
      else if ("CSV1".equals(fileFormat) || "CSV2".equals(fileFormat) || "XLS".equals(fileFormat)) {
        String line = null;
        int w = 1;
        boolean fillInDelim = delimiters.size()==0;
//        if (fillInDelim)
//          delimiters.add(new Integer(0));
        for(int j=0;j<row.size();j++) {
          line = row.get(j).toString();
          g.drawString(line, w, 15);
          w += g.getFontMetrics().stringWidth(line)+5;
          g.drawLine(w-2,0,w-2,20);
          if (fillInDelim) {
            delimiters.add(new Integer(w-2));
          }
        }
      }
    }

    int x = -1;
    for(int i=0;i<delimiters.size();i++) {
      x = ((Integer)delimiters.get(i)).intValue();
      g.drawLine(x,0,x,getHeight());
    }
  }


  public void setContent(ETLProcessVO vo) {
    row.clear();
    delimiters.clear();
    fileFormat = vo.getFileFormatSYS23();

    byte[] bytes = vo.getLocalFile();
    if (bytes==null || bytes.length==0) {
      Response res = ClientUtils.getData("getFolderContent",new Object[]{fileFormat,new File(vo.getFilenameSYS23())});
      if (!res.isError()) {
        bytes = (byte[])((VOResponse)res).getVo();
      }
      else
        OptionPane.showMessageDialog(MDIFrame.getInstance(),res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }
    if (bytes==null) {
      repaint();
      return;
    }


    BufferedReader br = null;
    try {
      if ("TXT".equals(fileFormat)) {
        br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
        row.add(br.readLine());
      }
      else if ("CSV1".equals(fileFormat) || "CSV2".equals(fileFormat)) {
        String delim = "CSV1".equals(vo.getFileFormatSYS23())?";":",";
        br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
        String line = null;
        line = br.readLine();
        delimiters.clear();

        boolean bContinue = true;
        int i = 0;
        int j = 0;
        String value = null;
        row.clear();
        while(bContinue){
          int ind = line.indexOf(delim,j);
          if(ind == -1){
            ind = line.length();
            bContinue = false;
            if (j>ind)
              continue;
          } else {
            // found separator: check if pattern has an odd number of " 'til the separator...
            if (CSVUtils.isOdd(line,j,ind)) {
              // e.g. "xyz;xyz";
              ind = CSVUtils.getRightEdge(line,ind); // ind = index of character next to the right delimiter "
            }

          }

          value = ind==line.length()?
                  line.substring(j) :
                  line.substring(j,ind);
          value = CSVUtils.decodePattern(value); // replace patterns "" with "
          j =  ind+1;

          row.add(value);
        }
      }
      else if ("XLS".equals(fileFormat)) {
        POIFSFileSystem poifs = new POIFSFileSystem(new ByteArrayInputStream(bytes));
        HSSFWorkbook wb = new HSSFWorkbook(poifs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFCell cell = null;
        Object value = null;
        HSSFRow hssfrow = sheet.getRow(0);
        delimiters.clear();
        for(short j=0;j<1000;j++){
          cell = hssfrow.getCell(j);
          if (cell!=null) {
            try {
              if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
                value = new Double(cell.getNumericCellValue());
              }
              else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
                value = cell.getStringCellValue();
              }
              else {
                value = cell.getDateCellValue();
              }
            }
            catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          else
            value = null;

          row.add(value);
        } // end for on cells...

        while(row.get(row.size()-1)==null)
          row.remove(row.size()-1);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        if (br!=null)
          br.close();
      }
      catch (Exception ex1) {
      }
    }
    repaint();
  }

}
