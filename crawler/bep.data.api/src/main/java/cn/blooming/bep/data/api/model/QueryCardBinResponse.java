package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

public class QueryCardBinResponse extends BaseResponse {

	/**
	 *
	 */
	private static final long serialVersionUID = -8167886572513256114L;

	/**
	 * 卡bin
	 */
	@ApiModelProperty(value = "卡bin", required = true)
	private String cardbin;
	
	/**
	 * 银行代码
	 */
	@ApiModelProperty(value = "银行代码", required = true)
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	@ApiModelProperty(value = "银行名称", required = true)
	private String bankName;
	
	/**
	 * 卡类型
	 */
	@ApiModelProperty(value = "卡类型", required = true)
	private String type;

	/**
	 * 银行logo地址
	 */
	@ApiModelProperty(value = "银行logo地址", required = true)
	private String bankLogoUrl;

	public String getCardbin() {
		return cardbin;
	}

	public void setCardbin(String cardbin) {
		this.cardbin = cardbin;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBankLogoUrl() {
		return bankLogoUrl;
	}

	public void setBankLogoUrl(String bankLogoUrl) {
		this.bankLogoUrl = bankLogoUrl;
	}

	@Override
	public String toString() {
		return "QueryCardBinResponse [cardbin=" + cardbin + ", bankCode=" + bankCode + ", bankName=" + bankName
				+ ", type=" + type + "]";
	}

	
}
