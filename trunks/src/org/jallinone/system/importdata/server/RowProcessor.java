package org.jallinone.system.importdata.server;

import java.io.File;
import org.jallinone.system.importdata.java.ImportDescriptorVO;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.util.List;
import java.math.BigDecimal;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Row processor, used to read a file to import, one row per time.</p>
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
public abstract class RowProcessor {

  /** collection of couples <Class,Constructor having a String argument> */
  private HashMap clazzCache = new HashMap();

  /** collection of couples <String date format pattern,SimpleDateFormat corresponding instance> */
  private HashMap simpleDateFormatCache = new HashMap();


  /**
   * Open file to process.
   */
  public abstract void openFile(InputStream in) throws Throwable;


  /**
   * Read the next row from file.
   * param indexes collection of couples <field name,Class type of the field">
   * @return null if no rows are available or the current row otherwise
   */
  public abstract Object[] getNextRow(List fields,ImportDescriptorVO fileDescrVO,HashMap indexes) throws Throwable;


  /**
   * Close file.
   */
  public abstract void closeFile() throws Throwable;


  /**
   * Convert
   * @param isString define if the "obj" to convert is a String type object (it avoids to perform a slow instanceof test)
   * @param obj object to convert; may be null
   * @param clazz target class type
   * @throws Throwable in case of conversion error
   * @return converted object
   */
  public final Object convertObj(boolean isString,String dateFormat,Object obj,Class clazz) throws Throwable {
    if (obj==null)
      return null;
    if (isString) {
      obj = obj.toString().trim();
      if (obj.toString().length()==0)
        return null;
      if (clazz.equals(String.class))
        // String -> String...
        return obj;

      if (java.util.Date.class.isAssignableFrom(clazz)) {
        // string -> Date/Timestamp...
        SimpleDateFormat sdf = (SimpleDateFormat)simpleDateFormatCache.get(dateFormat);
        if (sdf==null) {
          sdf = new SimpleDateFormat(dateFormat);
          simpleDateFormatCache.put(dateFormat,sdf);
        }
        Long time = new Long(sdf.parse(obj.toString()).getTime());
        Constructor c = (Constructor)clazzCache.get(clazz);
        if (c==null) {
          c = clazz.getConstructor(new Class[]{Long.TYPE});
          clazzCache.put(clazz,c);
        }
        return c.newInstance(new Object[]{time});
      } // end String -> Date conversion...
      else {
        // String -> other type...
        Constructor c = (Constructor)clazzCache.get(clazz);
        if (c==null) {
          c = clazz.getConstructor(new Class[]{String.class});
          clazzCache.put(clazz,c);
        }
        return c.newInstance(new Object[]{obj});
      }
    }
    else {
      // e.g. a java.util.Date to convert to java.sql.Date or a java.sql.Date to convert to java.sql.Timestamp
      if (obj instanceof java.util.Date) {
        Long time = new Long(((java.util.Date)obj).getTime());
        Constructor c = (Constructor)clazzCache.get(clazz);
        if (c==null) {
          c = clazz.getConstructor(new Class[]{Long.TYPE});
          clazzCache.put(clazz,c);
        }
        return c.newInstance(new Object[]{time});
      }
      else if (obj instanceof Number && Number.class.isAssignableFrom(clazz)) {
        if (obj.getClass().getName().equals(clazz.getName()))
          return obj;
        Number n = (Number)obj;
        if (n.longValue()==n.doubleValue()) {
          // int or long...
          Constructor c = (Constructor)clazzCache.get(clazz);
          if (c==null) {
            c = clazz.getConstructor(new Class[]{String.class});
            clazzCache.put(clazz,c);
          }
          return c.newInstance(new Object[]{n.toString()});
        }
        else {
          // a number having decimals...
          Constructor c = (Constructor)clazzCache.get(clazz);
          if (c==null) {
            c = clazz.getConstructor(new Class[]{Double.class});
            clazzCache.put(clazz,c);
          }
          return c.newInstance(new Object[]{new Double(n.doubleValue())});
        }
      }
      else if (obj instanceof Number && clazz.equals(String.class)) {
        Number n = (Number)obj;
        if (n.longValue()==n.doubleValue())
          return String.valueOf(n.longValue());
        return obj.toString();
      }
      throw new Exception("Cannot convert a "+obj.getClass().getName()+" to a type "+clazz.getName());
    }
  }


}
