package com.tx.springboot.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.tx.springboot.pojo.Goods ;

/**
 * 将goods与miaosha_goods两张表绑定到一起
 */
public class GoodsVo extends Goods{
	private BigDecimal miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public void setMiaoshaPrice(BigDecimal miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
}
