package org.jallinone.variants.java;


import java.io.Serializable;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Utility class used to generate a variants combination,
 * starting from
 * - a VariantMatrixRowVO + VariantMatrixVO or
 * - a VariantMatrixRowVO + VariantMatrixColumnVO + VariantMatrixVO
 * </p>
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
public class VariantsMatrixUtils {

  /**
   * Fill in the "obj" argument (having methods setVariantTypeXXX,setVariantCodeXXX)
   * according to the specified rowVO and colVO, based on the specified matrixVO
   * @param obj object (PK or value object) whose variants related attributes must be filled
   * @param attrSuffix suffix to append to setter methods of "obj"
   * @param matrixVO variants descriptor for a specified item
   * @param rowVO first variant to use (always defined)
   * @param colVO additional variants to use (may be null, if one only variant is used for the current item)
   */
  public static void setVariantTypesAndCodes(Serializable obj,String attrSuffix,VariantsMatrixVO matrixVO,VariantsMatrixRowVO rowVO,VariantsMatrixColumnVO colVO) throws Throwable {
    if (colVO==null) {
      // rowVO contains all needed info...
      obj.getClass().getMethod("setVariantTypeItm06"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM06()});
      obj.getClass().getMethod("setVariantTypeItm07"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM07()});
      obj.getClass().getMethod("setVariantTypeItm08"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM08()});
      obj.getClass().getMethod("setVariantTypeItm09"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM09()});
      obj.getClass().getMethod("setVariantTypeItm10"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM10()});

      obj.getClass().getMethod("setVariantCodeItm11"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM11()});
      obj.getClass().getMethod("setVariantCodeItm12"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM12()});
      obj.getClass().getMethod("setVariantCodeItm13"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM13()});
      obj.getClass().getMethod("setVariantCodeItm14"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM14()});
      obj.getClass().getMethod("setVariantCodeItm15"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM15()});
    }
    else {
      // rowVO contains only one couple variantType/variantCode useful; colVO contains the other 4 couples variantType/variantCode to use...
      VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
      // set JOLLY for all attributes, as default value, except for the real attribute to set;
      // other attributes will be overridden using VariantsMatrixColumnVO object;
      // no all other attributes will be overridden, so it is important to set the default...
      obj.getClass().getMethod("setVariantTypeItm06"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM06()});
      obj.getClass().getMethod("setVariantCodeItm11"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM11()});
      obj.getClass().getMethod("setVariantTypeItm07"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM07()});
      obj.getClass().getMethod("setVariantCodeItm12"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM12()});
      obj.getClass().getMethod("setVariantTypeItm08"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM08()});
      obj.getClass().getMethod("setVariantCodeItm13"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM13()});
      obj.getClass().getMethod("setVariantTypeItm09"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM09()});
      obj.getClass().getMethod("setVariantCodeItm14"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM14()});
      obj.getClass().getMethod("setVariantTypeItm10"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantTypeITM10()});
      obj.getClass().getMethod("setVariantCodeItm15"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{rowVO.getVariantCodeITM15()});

      for(int i=1;i<matrixVO.getManagedVariants().length;i++) {
        varVO = (VariantNameVO)matrixVO.getManagedVariants()[i];
        if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
          obj.getClass().getMethod("setVariantTypeItm06"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantTypeITM06()});
          obj.getClass().getMethod("setVariantCodeItm11"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantCodeITM11()});
        }
        else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
          obj.getClass().getMethod("setVariantTypeItm07"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantTypeITM07()});
          obj.getClass().getMethod("setVariantCodeItm12"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantCodeITM12()});
        }
        else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
          obj.getClass().getMethod("setVariantTypeItm08"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantTypeITM08()});
          obj.getClass().getMethod("setVariantCodeItm13"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantCodeITM13()});
        }
        else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
          obj.getClass().getMethod("setVariantTypeItm09"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantTypeITM09()});
          obj.getClass().getMethod("setVariantCodeItm14"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantCodeITM14()});
        }
        else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
          obj.getClass().getMethod("setVariantTypeItm10"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantTypeITM10()});
          obj.getClass().getMethod("setVariantCodeItm15"+attrSuffix,new Class[]{String.class}).invoke(obj,new Object[]{colVO.getVariantCodeITM15()});
        }
      }
    }
  }


  /**
   * @param matrixVO variants descriptor for a specified item
   * @param rowVO first variant to use (always defined)
   * @return variantType value for the "real" attribute managed by the VariantsMatrixRowVO object passed as argument
   */
  public static String getVariantType(VariantsMatrixVO matrixVO,VariantsMatrixRowVO rowVO) throws Exception {
    VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
    if (varVO.getTableName().equals("ITM11_VARIANTS_1"))
      return rowVO.getVariantTypeITM06();
    if (varVO.getTableName().equals("ITM12_VARIANTS_2"))
      return rowVO.getVariantTypeITM07();
    if (varVO.getTableName().equals("ITM13_VARIANTS_3"))
      return rowVO.getVariantTypeITM08();
    if (varVO.getTableName().equals("ITM14_VARIANTS_4"))
      return rowVO.getVariantTypeITM09();
    if (varVO.getTableName().equals("ITM15_VARIANTS_5"))
      return rowVO.getVariantTypeITM10();
    else
      throw new Exception("Invalid table name: '"+varVO.getTableName()+"'");
  }


  /**
   * @param matrixVO variants descriptor for a specified item
   * @param rowVO first variant to use (always defined)
   * @return variantCode value for the "real" attribute managed by the VariantsMatrixRowVO object passed as argument
   */
  public static String getVariantCode(VariantsMatrixVO matrixVO,VariantsMatrixRowVO rowVO) throws Exception {
    VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
    if (varVO.getTableName().equals("ITM11_VARIANTS_1"))
      return rowVO.getVariantCodeITM11();
    if (varVO.getTableName().equals("ITM12_VARIANTS_2"))
      return rowVO.getVariantCodeITM12();
    if (varVO.getTableName().equals("ITM13_VARIANTS_3"))
      return rowVO.getVariantCodeITM13();
    if (varVO.getTableName().equals("ITM14_VARIANTS_4"))
      return rowVO.getVariantCodeITM14();
    if (varVO.getTableName().equals("ITM15_VARIANTS_5"))
      return rowVO.getVariantCodeITM15();
    else
      throw new Exception("Invalid table name: '"+varVO.getTableName()+"'");
  }


//  /**
//   * Set the row variant type/code to the VariantsMatrixColumnVO object.
//   * @param matrixVO variants descriptor for a specified item
//   * @param rowVO first variant to use (always defined)
//   * @param colVO column where the row variant type/code must be copied
//   */
//  public static void setVariantTypeAndCode(VariantsMatrixVO matrixVO,VariantsMatrixRowVO rowVO,VariantsMatrixColumnVO colVO) throws Exception {
//    VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
//    if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
//      colVO.setVariantTypeITM06(rowVO.getVariantTypeITM06());
//      colVO.setVariantCodeITM11(rowVO.getVariantCodeITM11());
//    }
//    else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
//      colVO.setVariantTypeITM07(rowVO.getVariantTypeITM07());
//      colVO.setVariantCodeITM12(rowVO.getVariantCodeITM12());
//    }
//    else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
//      colVO.setVariantTypeITM08(rowVO.getVariantTypeITM08());
//      colVO.setVariantCodeITM13(rowVO.getVariantCodeITM13());
//    }
//    else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
//      colVO.setVariantTypeITM09(rowVO.getVariantTypeITM09());
//      colVO.setVariantCodeITM14(rowVO.getVariantCodeITM14());
//    }
//    else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
//      colVO.setVariantTypeITM10(rowVO.getVariantTypeITM10());
//      colVO.setVariantCodeITM15(rowVO.getVariantCodeITM15());
//    }
//  }


}
