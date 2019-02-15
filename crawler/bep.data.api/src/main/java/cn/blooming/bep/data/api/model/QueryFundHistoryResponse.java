package cn.blooming.bep.data.api.model;

import java.util.List;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

public class QueryFundHistoryResponse extends BaseResponse {

	/**
	 *
	 */
	private static final long serialVersionUID = -8167886572513256114L;
	
	/**
	 * 基金历史数据
	 */
	@ApiModelProperty(value = "基金历史数据", required = true)
	private List<FundRate> rates;


	/**
	 * 基金同类均值
	 * */
	@ApiModelProperty(value = "基金同类均值", required = true)
	private List<FundStatistics> fundStatistics;



	public List<FundRate> getRates() {
		return rates;
	}

	public void setRates(List<FundRate> rates) {
		this.rates = rates;
	}

	public List<FundStatistics> getFundStatistics() {
		return fundStatistics;
	}

	public void setFundStatistics(List<FundStatistics> fundStatistics) {
		this.fundStatistics = fundStatistics;
	}

	@Override
	public String toString() {
		return "QueryFundHistoryResponse{" +
				"rates=" + rates +
				", fundStatistics=" + fundStatistics +
				'}';
	}
}
