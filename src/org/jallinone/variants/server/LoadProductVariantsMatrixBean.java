package org.jallinone.variants.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.purchases.documents.java.SupplierPriceItemVO;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.items.java.ItemPK;
import java.util.ArrayList;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.items.server.LoadItemVariantsAction;
import org.jallinone.items.java.ItemVariantVO;
import java.math.BigDecimal;
import org.jallinone.variants.java.VariantsItemDescriptor;
import org.jallinone.items.server.LoadItemVariantsBean;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean class used to load variants matrix for the specified item code.</p>
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
public class LoadProductVariantsMatrixBean {

  private LoadItemVariantsBean bean = new LoadItemVariantsBean();

  private HashMap productVariants = new HashMap();
  private HashMap variantTypes = new HashMap();
  private HashMap variantTypeJoins = new HashMap();
  private HashMap variantCodeJoins = new HashMap();


  public LoadProductVariantsMatrixBean() {
    productVariants.put("ITM11_VARIANTS_1","ITM16_PRODUCT_VARIANTS_1");
    productVariants.put("ITM12_VARIANTS_2","ITM17_PRODUCT_VARIANTS_2");
    productVariants.put("ITM13_VARIANTS_3","ITM18_PRODUCT_VARIANTS_3");
    productVariants.put("ITM14_VARIANTS_4","ITM19_PRODUCT_VARIANTS_4");
    productVariants.put("ITM15_VARIANTS_5","ITM20_PRODUCT_VARIANTS_5");

    variantTypes.put("ITM11_VARIANTS_1","ITM06_VARIANT_TYPES_1");
    variantTypes.put("ITM12_VARIANTS_2","ITM07_VARIANT_TYPES_2");
    variantTypes.put("ITM13_VARIANTS_3","ITM08_VARIANT_TYPES_3");
    variantTypes.put("ITM14_VARIANTS_4","ITM09_VARIANT_TYPES_4");
    variantTypes.put("ITM15_VARIANTS_5","ITM10_VARIANT_TYPES_5");

    variantTypeJoins.put("ITM11_VARIANTS_1","VARIANT_TYPE_ITM06");
    variantTypeJoins.put("ITM12_VARIANTS_2","VARIANT_TYPE_ITM07");
    variantTypeJoins.put("ITM13_VARIANTS_3","VARIANT_TYPE_ITM08");
    variantTypeJoins.put("ITM14_VARIANTS_4","VARIANT_TYPE_ITM09");
    variantTypeJoins.put("ITM15_VARIANTS_5","VARIANT_TYPE_ITM10");

    variantCodeJoins.put("ITM11_VARIANTS_1","VARIANT_CODE_ITM11");
    variantCodeJoins.put("ITM12_VARIANTS_2","VARIANT_CODE_ITM12");
    variantCodeJoins.put("ITM13_VARIANTS_3","VARIANT_CODE_ITM13");
    variantCodeJoins.put("ITM14_VARIANTS_4","VARIANT_CODE_ITM14");
    variantCodeJoins.put("ITM15_VARIANTS_5","VARIANT_CODE_ITM15");
  }


  /**
   * Business logic to execute.
   */
  public final Response getVariantsMatrix(
    Connection conn,
    VariantsItemDescriptor itemVO,
    UserSessionParameters userSessionPars,
    HttpServletRequest request,
    HttpServletResponse response,
    HttpSession userSession,
    ServletContext context
  ) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    try {

      // create VariantsMatrixVO v.o.
      VariantsMatrixVO matrixVO = new VariantsMatrixVO();
      matrixVO.setDecimals(itemVO.getDecimalsREG02().intValue());
      matrixVO.setItemPK(new ItemPK(itemVO.getCompanyCodeSys01(),itemVO.getItemCodeItm01()));

      // retrieve variants descriptors...
      String sql =
          "select ITM21_VARIANTS.COMPANY_CODE_SYS01,ITM21_VARIANTS.TABLE_NAME,ITM21_VARIANTS.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,ITM21_VARIANTS.USE_VARIANT_TYPE "+
          "from ITM21_VARIANTS,SYS10_TRANSLATIONS where "+
          "ITM21_VARIANTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "ITM21_VARIANTS.COMPANY_CODE_SYS01=?";
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM21","ITM21_VARIANTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("tableName","ITM21_VARIANTS.TABLE_NAME");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10ITM21","ITM21_VARIANTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("useVariantTypeITM21","ITM21_VARIANTS.USE_VARIANT_TYPE");
      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(itemVO.getCompanyCodeSys01());
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          VariantNameVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true
      );
      if (answer.isError())
        return answer;
      List variants = ((VOListResponse)answer).getRows();

      // define managedVariants...
      ArrayList managedVariants = new ArrayList();
      if (Boolean.TRUE.equals(itemVO.getUseVariant1ITM01()))
        managedVariants.add(variants.get(0));
      if (Boolean.TRUE.equals(itemVO.getUseVariant2ITM01()))
        managedVariants.add(variants.get(1));
      if (Boolean.TRUE.equals(itemVO.getUseVariant3ITM01()))
        managedVariants.add(variants.get(2));
      if (Boolean.TRUE.equals(itemVO.getUseVariant4ITM01()))
        managedVariants.add(variants.get(3));
      if (Boolean.TRUE.equals(itemVO.getUseVariant5ITM01()))
        managedVariants.add(variants.get(4));
      matrixVO.setManagedVariants(managedVariants);

      // define rows and cols...
      ArrayList rows = new ArrayList();
      ArrayList cols = new ArrayList();
      matrixVO.setRowDescriptors(rows);
      matrixVO.setColumnDescriptors(cols);

      // retrieve product variants, for each managed variant...
      GridParams gridParams = new GridParams();
      VariantNameVO varVO = null;
      ArrayList[] tmp = new ArrayList[managedVariants.size()-1];
      for(int i=0;i<managedVariants.size();i++) {
        varVO = (VariantNameVO)managedVariants.get(i);
        gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,matrixVO.getItemPK());
        gridParams.getOtherGridParams().put(ApplicationConsts.TABLE_NAME,varVO.getTableName());
        //gridParams.getOtherGridParams().put(ApplicationConsts.VARIANT_TYPE,null); //???
        answer = bean.getItemVariants(conn,gridParams,userSessionPars,request,response,userSession,context);
        if (answer.isError())
          return answer;
        ArrayList vos = new ArrayList(((VOListResponse)answer).getRows());

        if (i==0) {
          ItemVariantVO variantVO = null;
          for(int k=0;k<vos.size();k++) {
            variantVO = (ItemVariantVO)vos.get(k);
            if (!Boolean.TRUE.equals(variantVO.getSelected()))
                continue;

            VariantsMatrixRowVO rowVO = new VariantsMatrixRowVO();
            rowVO.setRowDescription(variantVO.getVariantCode());
            rowVO.setVariantCodeITM11(variantVO.getVariantCode());
            rowVO.setVariantTypeITM06(variantVO.getVariantType());
            rows.add(rowVO);
          }
        }
        else {
          tmp[i-1] = vos;
        }
      }

      // create combinations of "tmp"...
      if (managedVariants.size()>1)
        createCombinations(managedVariants,tmp,cols,0,null);

      return new VOResponse(matrixVO);
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"getVariantsMatrix","Error while fetching the variants matrix",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }


  private void createCombinations(ArrayList managedVariants,ArrayList[] tmp,ArrayList cols,int depth,VariantsMatrixColumnVO colVO) throws Exception {
    ItemVariantVO vo = null;
    VariantsMatrixColumnVO col2VO = null;
    for(int i=0;i<tmp[depth].size();i++) {
      vo = (ItemVariantVO)tmp[depth].get(i);
      if (!Boolean.TRUE.equals(vo.getSelected()))
          continue;

      if (depth < tmp.length - 1) {
        if (colVO == null) {
          col2VO = new VariantsMatrixColumnVO();
          setVariantTypeAndCodeDescription(col2VO,vo);
        }
        else {
          col2VO = (VariantsMatrixColumnVO)colVO.clone();
          setVariantTypeAndCodeDescription(col2VO,vo);
        }
        setVariantTypeAndCode(managedVariants,col2VO,vo,depth);
        createCombinations(managedVariants, tmp, cols, depth + 1, (VariantsMatrixColumnVO)col2VO.clone());
      }
      else {
        if (colVO == null) {
          col2VO = new VariantsMatrixColumnVO();
          setVariantTypeAndCodeDescription(col2VO,vo);
        }
        else {
          col2VO = (VariantsMatrixColumnVO)colVO.clone();
          setVariantTypeAndCodeDescription(col2VO,vo);
        }
        setVariantTypeAndCode(managedVariants,col2VO,vo,depth);

        cols.add(col2VO);
      }
    }
  }


  private void setVariantTypeAndCodeDescription(VariantsMatrixColumnVO colVO,ItemVariantVO vo) {
    colVO.setColumnDescription(
      (colVO.getColumnDescription()==null?"":(colVO.getColumnDescription()+" / "))+
      (ApplicationConsts.JOLLY.equals(vo.getVariantType())?"":(vo.getVariantType()+" "))+
      (ApplicationConsts.JOLLY.equals(vo.getVariantCode())?"":vo.getVariantCode())
    );
  }


  private void setVariantTypeAndCode(ArrayList managedVariants,VariantsMatrixColumnVO colVO,ItemVariantVO vo,int depth) {
    VariantNameVO varVO = (VariantNameVO)managedVariants.get(depth+1);
    if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
      colVO.setVariantCodeITM12(vo.getVariantCode());
      colVO.setVariantTypeITM07(vo.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
      colVO.setVariantCodeITM13(vo.getVariantCode());
      colVO.setVariantTypeITM08(vo.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
      colVO.setVariantCodeITM14(vo.getVariantCode());
      colVO.setVariantTypeITM09(vo.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
      colVO.setVariantCodeITM15(vo.getVariantCode());
      colVO.setVariantTypeITM10(vo.getVariantType());
    }

  }



}


