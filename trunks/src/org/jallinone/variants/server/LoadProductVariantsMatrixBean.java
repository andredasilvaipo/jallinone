package org.jallinone.variants.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.sales.documents.java.PriceItemVO;
import org.jallinone.sales.pricelist.java.PriceVO;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.purchases.documents.java.SupplierPriceItemVO;
import org.jallinone.purchases.pricelist.java.SupplierPriceVO;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.GridItemVO;
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
public class LoadProductVariantsMatrixBean implements LoadProductVariantsMatrix {

  private LoadItemVariantsBean bean;

  public void setBean(LoadItemVariantsBean bean) {
	  this.bean = bean;
  }

  private HashMap productVariants = new HashMap();
  private HashMap variantTypes = new HashMap();
  private HashMap variantTypeJoins = new HashMap();
  private HashMap variantCodeJoins = new HashMap();


  private DataSource dataSource;

  public void setDataSource(DataSource dataSource) {
	  this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;

  /**
   * Set external connection.
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {

    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }


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
  public VariantsMatrixVO getDetailItemVariantsMatrix(
    DetailItemVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }


  /**
   * Business logic to execute.
   */
  public VariantsMatrixVO getGridItemVariantsMatrix(
    GridItemVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }


  /**
   * Business logic to execute.
   */
  public VariantsMatrixVO getSupplierPriceItemVariantsMatrix(
    SupplierPriceItemVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }


  /**
   * Business logic to execute.
   */
  public VariantsMatrixVO getSupplierPriceVariantsMatrix(
    SupplierPriceVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }


  /**
   * Business logic to execute.
   */
  public VariantsMatrixVO getPriceVariantsMatrix(
    PriceVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }


  /**
   * Business logic to execute.
   */
  public VariantsMatrixVO getPriceItemVariantsMatrix(
    PriceItemVO itemVO,
    String serverLanguageId,String username
  ) throws Throwable {
	  return getVM(itemVO,serverLanguageId,username);
  }



  /**
   * Business logic to execute.
   */
  private VariantsMatrixVO getVM(
    VariantsItemDescriptor itemVO,
    String serverLanguageId,String username
  ) throws Throwable {

    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);

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
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantNameVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );
      if (answer.isError())
        throw new Exception(answer.getErrorMessage());
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
      matrixVO.setManagedVariants((VariantNameVO[])managedVariants.toArray(new VariantNameVO[managedVariants.size()]));

      // define rows and cols...
      ArrayList rows = new ArrayList();
      ArrayList cols = new ArrayList();

      // retrieve product variants, for each managed variant...
      GridParams gridParams = new GridParams();
      VariantNameVO varVO = null;
      ArrayList[] tmp = new ArrayList[managedVariants.size()-1];
      boolean[] tmpSameVarType = new boolean[managedVariants.size()-1];
      boolean sameVarType = true;
      ItemVariantVO variantVO = null;
      String currVarType = null;
      for(int i=0;i<managedVariants.size();i++) {
        varVO = (VariantNameVO)managedVariants.get(i);
        gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,matrixVO.getItemPK());
        gridParams.getOtherGridParams().put(ApplicationConsts.TABLE_NAME,varVO.getTableName());
        //gridParams.getOtherGridParams().put(ApplicationConsts.VARIANT_TYPE,null); //???
        answer = bean.loadItemVariants(gridParams,serverLanguageId,username);
        if (answer.isError())
        	throw new Exception(answer.getErrorMessage());
        // list of all variant's elements (not only the ones associated to the current item
        // ("selected" attribute establishes which ones are associated to the current item...)
        ArrayList vos = new ArrayList(((VOListResponse)answer).getRows());

        sameVarType = true;
        currVarType = null;
        for(int k=0;k<vos.size();k++) {
          variantVO = (ItemVariantVO)vos.get(k);
          if (!Boolean.TRUE.equals(variantVO.getSelected()))
              continue;
          if (currVarType!=null && !currVarType.equals(variantVO.getVariantType())) {
            sameVarType = false;
            break;
          }
          else
            currVarType = variantVO.getVariantType();
        }

        if (i==0) {
          for(int k=0;k<vos.size();k++) {
            variantVO = (ItemVariantVO)vos.get(k);
            if (!Boolean.TRUE.equals(variantVO.getSelected()))
                continue;

            VariantsMatrixRowVO rowVO = new VariantsMatrixRowVO();
            setVariantDescription(variantVO,rowVO,sameVarType);
            setVariantTypeAndCode(variantVO,rowVO);
            rows.add(rowVO);
          }
        }
        else {
          tmpSameVarType[i-1] = sameVarType;
          tmp[i-1] = vos; // es. i=1 (color),tmp[0]=[red,black,orange...]
                          // es. i=2 (drop), tmp[1]=[short,medium,...]
        }
      }

      // create combinations of "tmp"...
      if (managedVariants.size()>1)
        createCombinations(managedVariants,tmp,tmpSameVarType,cols,0,null);

      matrixVO.setRowDescriptors((VariantsMatrixRowVO[])rows.toArray(new VariantsMatrixRowVO[rows.size()]));
      matrixVO.setColumnDescriptors((VariantsMatrixColumnVO[])cols.toArray(new VariantsMatrixColumnVO[cols.size()]));

      return matrixVO;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"getVariantsMatrix","Error while fetching the variants matrix",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }
  }


  /**
   * Create combinations of all variants elements defined within "tmp"; e.g. combinations of colors, drops, heights...
   * @param managedVariants ArrayList
   * @param tmp array of all variants (except the first one) and for each variant, the list of variants' elements (red, black, orange...)
   * @param cols list of all columns of the matrix (created recursivelly by this method)
   * @param depth current dept
   * @param colVO parent column to "duplicate" for each current variant's elements
   */
   private void createCombinations(ArrayList managedVariants,ArrayList[] tmp,boolean[] tmpSameVarType,ArrayList cols,int depth,VariantsMatrixColumnVO colVO) throws Exception {
     ItemVariantVO vo = null;
     VariantsMatrixColumnVO col2VO = null;
     for(int i=0;i<tmp[depth].size();i++) {
    	 vo = (ItemVariantVO)tmp[depth].get(i); // colors: red, black, orange...
    	 if (!Boolean.TRUE.equals(vo.getSelected()))
    		 // skip variant's element, if not associated to the current item...
    		 continue;

    	 if (depth < tmp.length - 1) {
    		 if (colVO == null) {
    			 col2VO = new VariantsMatrixColumnVO();
    			 setVariantDescription(col2VO,vo,tmpSameVarType[depth]);
    		 }
    		 else {
    			 col2VO = (VariantsMatrixColumnVO)colVO.clone();
    			 setVariantDescription(col2VO,vo,tmpSameVarType[depth]);
    		 }
    		 setVariantTypeAndCode(managedVariants,col2VO,vo,depth);
    		 createCombinations(managedVariants, tmp, tmpSameVarType, cols, depth + 1, (VariantsMatrixColumnVO)col2VO.clone());
    	 }
    	 else {
    		 if (colVO == null) {
    			 col2VO = new VariantsMatrixColumnVO();
    			 setVariantDescription(col2VO,vo,tmpSameVarType[depth]);
    		 }
    		 else {
    			 col2VO = (VariantsMatrixColumnVO)colVO.clone();
    			 setVariantDescription(col2VO,vo,tmpSameVarType[depth]);
    		 }
    		 setVariantTypeAndCode(managedVariants,col2VO,vo,depth);

    		 cols.add(col2VO);
    	 }
     }
   }


   private void setVariantDescription(VariantsMatrixColumnVO colVO,ItemVariantVO vo,boolean sameVarType) {
	   colVO.setColumnDescription(
			   (colVO.getColumnDescription()==null?"":(colVO.getColumnDescription()+" / "))+
                           (sameVarType?
                            "":
 			    (ApplicationConsts.JOLLY.equals(vo.getVariantType())?"":(vo.getVariantTypeDesc()+" "))
                           )+
			   (ApplicationConsts.JOLLY.equals(vo.getVariantCode())?"":vo.getVariantDesc())
	   );
  }


  private void setVariantTypeAndCode(ArrayList managedVariants,VariantsMatrixColumnVO colVO,ItemVariantVO vo,int depth) {
    VariantNameVO varVO = (VariantNameVO)managedVariants.get(depth+1);
    if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
      colVO.setVariantCodeITM11(vo.getVariantCode());
      colVO.setVariantTypeITM06(vo.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
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


  private void setVariantDescription(ItemVariantVO varVO,VariantsMatrixRowVO vo,boolean sameVarType) {
    vo.setRowDescription(
      (vo.getRowDescription()==null?"":(vo.getRowDescription()+" / "))+
      (sameVarType?
       "":
       (ApplicationConsts.JOLLY.equals(varVO.getVariantType())?"":(varVO.getVariantTypeDesc()+" "))
      )+
      (ApplicationConsts.JOLLY.equals(varVO.getVariantCode())?"":varVO.getVariantDesc())
    );
  }


  private void setVariantTypeAndCode(ItemVariantVO varVO,VariantsMatrixRowVO vo) {
    if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
      vo.setVariantCodeITM11(varVO.getVariantCode());
      vo.setVariantTypeITM06(varVO.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
      vo.setVariantCodeITM12(varVO.getVariantCode());
      vo.setVariantTypeITM07(varVO.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
      vo.setVariantCodeITM13(varVO.getVariantCode());
      vo.setVariantTypeITM08(varVO.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
      vo.setVariantCodeITM14(varVO.getVariantCode());
      vo.setVariantTypeITM09(varVO.getVariantType());
    }
    else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
      vo.setVariantCodeITM15(varVO.getVariantCode());
      vo.setVariantTypeITM10(varVO.getVariantType());
    }

  }


}


