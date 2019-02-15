package cn.blooming.bep.data.api.model;

import java.util.List;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

public class QueryBankListResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8167886572513256114L;

	/**
	 * 银行列表
	 */
	@ApiModelProperty(value = "银行列表", required = true)
	private List<Bank> banks = null;

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
	
}
