package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.dataenum.FundType;
import cn.blooming.bep.common.api.model.ClientBaseRequest;
import cn.blooming.bep.common.api.validate.rule.ValidateNotNull;
import io.swagger.annotations.ApiModelProperty;

public class SearchFundsRequest extends ClientBaseRequest {

	private static final long serialVersionUID = -8167886572513256114L;

	/**
	 * 基金类别 默认所有
	 * STOCK: 股票型
	 * MIX：混合型
	 * BOND: 债券型
	 * INDEX: 指数型
	 * ETF: ETF连接型 QDII: QDII LOF: LOF FOF: FOF 1
	 * SF:分级型/结构型
	 * GF:保本型
	 * MMF:货币型
	 * FOF:投资型
	 */
	@ApiModelProperty(value = "基金类型", required = false)
	private FundType type;
	
	/**
	 * 模糊查询字段
	 * */
	@ApiModelProperty(value = "模糊查询字段", required = false)
	private String search;

	/**
	 * 排序字段， 默认当前净值收益率排序
	 *      sinceStartup      业绩排行[自成立日起收益率]   rate_net_value
	 *      value: 	       估值排行[当前净值收益率]   rate_7days
	 *      year0:    今年来     rate_current_year
	 *      day0      日涨幅     rate_net_value
	 *      day7:     最近1周    rate_7days
	 * 		month1:   最近1个月收益率  rate_month
	 * 		month3 :  最近3个月收益率  rate_quarter
	 * 		month6 :  最近6个月收益率  rate_half
	 * 		year1:    最近1年收益率    rate_year
	 */
	@ApiModelProperty(value = "排序字段", required = false)
	private String sort;
	
	/**
	 * 排序类型， 默认降序
	 * desc: 降序
	 * asc: 升序
	 */
	@ApiModelProperty(value = "排序类型", required = false)
	private String sortOrder;
	
	@ApiModelProperty(value = "查询起始行", required = true)
    @ValidateNotNull
    private Integer page;
	
    @ApiModelProperty(value = "查询数据行数", required = true)
    @ValidateNotNull
    private Integer pageSize;

	public FundType getType() {
		return type;
	}

	public void setType(FundType type) {
		this.type = type;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "SearchFundsRequest{" +
				"type=" + type +
				", search='" + search + '\'' +
				", sort='" + sort + '\'' +
				", sortOrder='" + sortOrder + '\'' +
				", page=" + page +
				", pageSize=" + pageSize +
				'}';
	}
}
