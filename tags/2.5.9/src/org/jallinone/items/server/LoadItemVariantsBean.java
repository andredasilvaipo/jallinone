package org.jallinone.items.server;


import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.hierarchies.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to fetch item's variants and types, for the specified variant name.</p>
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
public class LoadItemVariantsBean implements LoadItemVariants {

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



	public LoadItemVariantsBean() {
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
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public ItemVariantVO getItemVariant(ItemPK pk) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public VariantNameVO getVariantName() {
		throw new UnsupportedOperationException();  
	}

	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadItemVariants(GridParams pars,String serverLanguageId,String username)  throws Throwable{
		
		PreparedStatement pstmt = null;

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
	
			String tableName = (String)pars.getOtherGridParams().get(ApplicationConsts.TABLE_NAME);
			ItemPK pk = (ItemPK)pars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
			String productVariant = (String)productVariants.get(tableName);
			String variantType = (String)variantTypes.get(tableName);
			String variantTypeJoin = (String)variantTypeJoins.get(tableName);
			String variantCodeJoin = (String)variantCodeJoins.get(tableName);

			String sql =
				"select "+tableName+"."+variantTypeJoin+","+tableName+".VARIANT_CODE,A.DESCRIPTION,B.DESCRIPTION, "+
				tableName+".PROGRESSIVE_SYS10,"+variantType+".PROGRESSIVE_SYS10 "+
				"from "+tableName+","+variantType+",SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B "+
				"where "+
				tableName+".COMPANY_CODE_SYS01=? and "+
				tableName+".COMPANY_CODE_SYS01="+variantType+".COMPANY_CODE_SYS01 and "+
				tableName+"."+variantTypeJoin+"="+variantType+".VARIANT_TYPE and "+
				tableName+".PROGRESSIVE_SYS10=A.PROGRESSIVE and A.LANGUAGE_CODE=? and "+
				variantType+".PROGRESSIVE_SYS10=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
				tableName+".ENABLED='Y' and "+
				variantType+".ENABLED='Y' and "+//and not "+tableName+"."+variantTypeJoin+"=? and "+
				"not "+tableName+".VARIANT_CODE=? "+
				"order by "+tableName+"."+variantTypeJoin+","+tableName+".CODE_ORDER";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("variantType",tableName+"."+variantTypeJoin);
			attribute2dbField.put("variantCode",tableName+".VARIANT_CODE");
			attribute2dbField.put("variantDesc","A.DESCRIPTION");
			attribute2dbField.put("variantTypeDesc","B.DESCRIPTION");
			attribute2dbField.put("variantProgressiveSys10",tableName+".PROGRESSIVE_SYS10");
			attribute2dbField.put("variantTypeProgressiveSys10",variantType+".PROGRESSIVE_SYS10");

			ArrayList values = new ArrayList();
			values.add(pk.getCompanyCodeSys01ITM01());
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			//values.add(ApplicationConsts.JOLLY);
			values.add(ApplicationConsts.JOLLY);

			// read from ITMxxx table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					ItemVariantVO.class,
					"Y",
					"N",
					null,
					pars,
					50,
					true
			);

			if (!answer.isError()) {
				java.util.List vos = ((VOListResponse)answer).getRows();
				HashMap map = new HashMap();
				ItemVariantVO vo = null;
				for(int i=0;i<vos.size();i++) {
					vo = (ItemVariantVO)vos.get(i);
					vo.setCompanyCodeSys01(pk.getCompanyCodeSys01ITM01());
					vo.setItemCodeItm01(pk.getItemCodeITM01());
					vo.setTableName(tableName);
					map.put(vo.getVariantType()+"."+vo.getVariantCode(),vo);
				}

				pstmt = conn.prepareStatement(
						"select "+productVariant+"."+variantTypeJoin+","+productVariant+"."+variantCodeJoin+" "+
						"from "+productVariant+" "+
						"where "+
						productVariant+".COMPANY_CODE_SYS01=? and "+
						productVariant+".ITEM_CODE_ITM01=? and "+
						productVariant+".ENABLED='Y' "
				);
				pstmt.setString(1,pk.getCompanyCodeSys01ITM01());
				pstmt.setString(2,pk.getItemCodeITM01());
				ResultSet rset = pstmt.executeQuery();

				while(rset.next()) {
					vo = (ItemVariantVO)map.get(rset.getString(1)+"."+rset.getString(2));
					if (vo!=null)
						vo.setSelected(Boolean.TRUE);
				}
				rset.close();
				pstmt.close();

			}

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"getItemVariants","Error while fetching item variants list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
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



}

