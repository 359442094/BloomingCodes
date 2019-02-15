package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.model.BaseRequest;
import cn.blooming.bep.common.api.validate.rule.ValidateNotNull;
import io.swagger.annotations.ApiModelProperty;

public class QueryFundDataRequest extends BaseRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = -8167886572513256114L;
	
	/**
	 * 基金代码
	 */
	@ApiModelProperty(value = "基金代码", required = true)
	//@ValidateNotNull
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "QueryFundDataRequest [code=" + code + "]";
	}
	
}
