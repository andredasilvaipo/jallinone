package org.jallinone.variants.server;



import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.purchases.documents.java.SupplierPriceItemVO;
import org.jallinone.purchases.pricelist.java.SupplierPriceVO;
import org.jallinone.sales.documents.java.PriceItemVO;
import org.jallinone.sales.pricelist.java.PriceVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.variants.java.VariantsItemDescriptor;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.openswing.swing.message.receive.java.Response;


public interface LoadProductVariantsMatrix {

	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getDetailItemVariantsMatrix(
	    DetailItemVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;

	  
	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getGridItemVariantsMatrix(
	    GridItemVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;
	  
	  
	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getSupplierPriceItemVariantsMatrix(
	    SupplierPriceItemVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;
	  
	  
	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getSupplierPriceVariantsMatrix(
	    SupplierPriceVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;
	  	  
	  
	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getPriceVariantsMatrix(
	    PriceVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;
	  
	  
	  /**
	   * Business logic to execute.
	   */
	  public VariantsMatrixVO getPriceItemVariantsMatrix(
	    PriceItemVO itemVO,
	    String serverLanguageId,String username
	  ) throws Throwable;
	
}
