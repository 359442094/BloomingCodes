package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.model.BaseRequest;
import cn.blooming.bep.common.api.validate.rule.ValidateNotNull;
import io.swagger.annotations.ApiModelProperty;

public class QueryCardBinRequest extends BaseRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = -8167886572513256114L;

	/**
	 * 卡bin
	 */
	@ApiModelProperty(value = "卡bin", required = true)
	@ValidateNotNull
	private String cardbin;

	public String getCardbin() {
		return cardbin;
	}

	public void setCardbin(String cardbin) {
		this.cardbin = cardbin;
	}

	@Override
	public String toString() {
		return "QueryCardBinRequest [cardbin=" + cardbin + "]";
	}
	
}
