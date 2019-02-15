package cn.blooming.bep.data.api.model;

import java.util.Date;

import cn.blooming.bep.common.api.model.BaseRequest;
import cn.blooming.bep.common.api.validate.rule.ValidateNotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

public class QueryFundHistoryRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8167886572513256114L;
	
	/**
	 * 基金代码
	 */
	@ApiModelProperty(value = "基金代码", required = true)
	@ValidateNotNull
	private String code;

	/**
	 * 开始日期
	 */
	@ApiModelProperty(value = "开始日期", required = true)
	@ValidateNotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start;
	
	/**
	 * 结束日期
	 */
	@ApiModelProperty(value = "结束日期", required = true)
	@ValidateNotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	
}
