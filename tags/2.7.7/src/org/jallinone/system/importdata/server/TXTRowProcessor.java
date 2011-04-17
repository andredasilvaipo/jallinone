package org.jallinone.system.importdata.server;

import java.io.*;
import org.jallinone.system.importdata.java.ImportDescriptorVO;
import java.util.ArrayList;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import java.util.HashMap;
import java.util.List;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Row processor, used to read a TXT file to import, one row per time.</p>
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
public class TXTRowProcessor extends RowProcessor {

  private BufferedReader br = null;


 /**
  * Open file to process.
  */
  public void openFile(InputStream in) throws Throwable {
    br = new BufferedReader(new InputStreamReader(in));
  }


  /**
   * Read the next row from file.
   * param indexes collection of couples <field name,Class type of the field">
   * @return null if no rows are available or the current row otherwise
   */
  public Object[] getNextRow(List fields,ImportDescriptorVO fileDescrVO,HashMap indexes) throws Throwable {
    String line = br.readLine();
    if (line==null)
      return null;

    ETLProcessFieldVO vo = null;
    Object[] values = new Object[fields.size()];
    for(int i=0;i<fields.size();i++) {
      vo = (ETLProcessFieldVO)fields.get(i);
      values[i] = convertObj(
        true,
        vo.getDateFormatSYS24(),
        line.substring(vo.getStartPosSYS24().intValue()-1,vo.getEndPosSYS24().intValue()),
        (Class)indexes.get(vo.getFieldNameSYS24())
      );
    }
    return values;
  }


  /**
   * Close file.
   */
  public void closeFile() throws Throwable {
    br.close();
  }


}
