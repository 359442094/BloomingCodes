package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class QueryFundDataResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8167886572513256114L;
	
	/**
	 * 基金数据对象
	 */
	@ApiModelProperty(value = "基金数据对象", required = true)
	private Fund fund;

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

}
