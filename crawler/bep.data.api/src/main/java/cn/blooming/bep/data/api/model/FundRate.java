package cn.blooming.bep.data.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

public class FundRate implements Serializable {

	private static final long serialVersionUID = -8289332282839011619L;
	/**
	 * 日期
	 */
	@ApiModelProperty(value = "日期", required = true)
	private Date date;
	
	/**
	 * 单位净值
	 */
	@ApiModelProperty(value = "单位净值", required = true)
	private BigDecimal netValue;
	/**
	 * 累计净值
	 */
	@ApiModelProperty(value = "累计净值", required = true)
	private BigDecimal totalValue;
	
	/**
	 * 净值变动率
	 */
	@ApiModelProperty(value = "净值变动率", required = true)
	private BigDecimal rateNetValue;


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getNetValue() {
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getRateNetValue() {
		return rateNetValue;
	}

	public void setRateNetValue(BigDecimal rateNetValue) {
		this.rateNetValue = rateNetValue;
	}

	@Override
	public String toString() {
		return "FundRate{" +
				"date=" + date +
				", netValue=" + netValue +
				", totalValue=" + totalValue +
				", rateNetValue=" + rateNetValue +
				'}';
	}
}
