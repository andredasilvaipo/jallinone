package org.jallinone.reports.utils.server;

import java.util.*;
import java.text.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Singleton used inside Jasper Report to correctly format a decimal value.</p>
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
public class ReportUtils {


  /**
   * Format the specified number according to the specified number of decimals.
   * @param number number to format
   * @param decimals specified number of decimals
   * @return
   */
  public static final String formatNumber(BigDecimal number,BigDecimal decimals) {
    if (number==null)
      return null;
    try {
      number = number.setScale(decimals.intValue(),BigDecimal.ROUND_HALF_UP);
      String mask = "#,##0";
      if (decimals.intValue()>0)
        mask += ".";
      for(int i=0;i<decimals.intValue();i++)
        mask += "0";
      DecimalFormat df = new DecimalFormat(mask);
      return df.format(number);
    }
    catch (Throwable ex) {
      return number.toString();
    }
  }


}
