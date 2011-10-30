package org.jallinone.purchases.pricelist.server;

import java.math.BigDecimal;

import org.jallinone.variants.java.VariantsMatrixVO;

public class VariantsPrice {

	private VariantsMatrixVO matrixVO;
	private Object[][] cells;
	private String priceListCode;
	private java.sql.Date startDate;
	private java.sql.Date endDate;
	private BigDecimal progressiveReg04;

	
	public VariantsPrice() {}


	public void setMatrixVO(VariantsMatrixVO matrixVO) {
		this.matrixVO = matrixVO;
	}


	public void setCells(Object[][] cells) {
		this.cells = cells;
	}


	public void setPriceListCode(String priceListCode) {
		this.priceListCode = priceListCode;
	}


	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}


	public VariantsMatrixVO getMatrixVO() {
		return matrixVO;
	}


	public Object[][] getCells() {
		return cells;
	}


	public String getPriceListCode() {
		return priceListCode;
	}


	public java.sql.Date getStartDate() {
		return startDate;
	}


	public java.sql.Date getEndDate() {
		return endDate;
	}


	public BigDecimal getProgressiveReg04() {
		return progressiveReg04;
	}


	public void setProgressiveReg04(BigDecimal progressiveReg04) {
		this.progressiveReg04 = progressiveReg04;
	}



	
	
}
