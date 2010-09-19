package org.jallinone.system.importdata.server;

import java.io.*;
import org.jallinone.system.importdata.java.ImportDescriptorVO;
import java.util.ArrayList;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import java.util.HashMap;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.usermodel.*;
import java.util.List;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Row processor, used to read a xls (Excel) file to import, one row per time.</p>
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
public class XLSRowProcessor extends RowProcessor {

  private InputStream in = null;
  private HSSFSheet sheet = null;
  private int rowNum = 0;


 /**
  * Open file to process.
  */
  public void openFile(InputStream in) throws Throwable {
    this.in = in;
    POIFSFileSystem poifs = new POIFSFileSystem(in);
    HSSFWorkbook wb = new HSSFWorkbook(poifs);
    sheet = wb.getSheetAt(0);
  }


  /**
   * Read the next row from file.
   * param indexes collection of couples <field name,Class type of the field">
   * @return null if no rows are available or the current row otherwise
   */
  public Object[] getNextRow(List fields,ImportDescriptorVO fileDescrVO,HashMap indexes) throws Throwable {
    HSSFRow row = sheet.getRow(rowNum++);
    int attempts = 1;
    while(attempts<10 && row==null) {
      attempts++;
      sheet.getRow(rowNum++);
    }
    if (attempts>=10)
      return null;


    ETLProcessFieldVO vo = null;
    Object[] values = new Object[fields.size()];
    HSSFCell cell = null;
    Class clazz = null;
    Object value = null;
    for(int i=0;i<fields.size();i++) {
      vo = (ETLProcessFieldVO)fields.get(i);
      clazz = (Class)indexes.get(vo.getFieldNameSYS24());
      cell = row.getCell((short)(vo.getPosSYS24().shortValue()-1));
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

      values[i] = convertObj(
        cell.getCellType() == cell.CELL_TYPE_STRING,
        vo.getDateFormatSYS24(),
        value,
        clazz
      );
    }
    return values;
  }


  /**
   * Close file.
   */
  public void closeFile() throws Throwable {
    in.close();
  }


}
