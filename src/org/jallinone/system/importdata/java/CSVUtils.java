package org.jallinone.system.importdata.java;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Utility methods related to CSV file reading.</p>
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
public class CSVUtils {



   /**
    * @return check if the pattern has an odd number of " 'til the separator
    * Examples:
    * """x -> true
    * "x   -> true
    * ""x  -> false
    * """"x -> false
    * x" -> false
    */
   public static boolean isOdd(String pattern,int index,int end) {
     int odd = 0;
     for(int i=index;i<end;i++)
       if (pattern.charAt(i)=='\"')
         odd++;
     return odd%2==1;
   }


   /**
    * @param index of the separator
    * @return index next to the " character, which is the right delimiter of the field
    * Examples:
    * "xyz;xyz";
    * "xyz"";xyz;xyz";
    * "xyz"";xyz;xyz"
    */
   public static int getRightEdge(String pattern,int index) {
     int odd = 0;
     for(int i=index;i<pattern.length();i++)
       if (pattern.charAt(i)=='\"')
         odd++;
       else {
         if (odd%2==0)
           odd = 0;
         else
           return i;
       }
     return pattern.length();
   }


   /**
    * Find occurrences of "" and replace them with "
    * Remove left and right delimiters, if exist.
    */
   public static String decodePattern(String pattern) {
     if (pattern.length()==0)
       return pattern;
     StringBuffer aux = new StringBuffer("");
     for(int i=0;i<pattern.length();i++)
       if (pattern.charAt(i)=='\"' && i+1<pattern.length() && pattern.charAt(i+1)=='\"') {
         aux.append( "\"" );
         i++;
       }
       else
         aux.append( String.valueOf(pattern.charAt(i)) );
     if (aux.charAt(0)=='\"' && aux.charAt(aux.length()-1)=='\"')
       return aux.substring(1,aux.length()-1);
     return aux.toString();
   }

}
