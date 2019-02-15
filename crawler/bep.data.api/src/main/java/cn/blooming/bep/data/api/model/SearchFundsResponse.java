package cn.blooming.bep.data.api.model;

import java.util.List;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

public class SearchFundsResponse extends BaseResponse {

	/**
	 *
	 */
	private static final long serialVersionUID = -8167886572513256114L;
	
	/**
	 * 基金列表
	 */
	@ApiModelProperty(value = "基金列表", required = true)
	private List<Fund> funds;
	
	@ApiModelProperty(value = "总记录数")
    private Long totalEntries;
	
    @ApiModelProperty(value = "本次查询数量")
    private Integer pageSize;
    @ApiModelProperty(value = "查询总页数")
	private Integer pages;
    @ApiModelProperty(value = "查询起始行")
    private Integer currentPage;

	@ApiModelProperty(value = "查询到的关键词")
	private List<String> keys;

	public List<Fund> getFunds() {
		return funds;
	}

	public void setFunds(List<Fund> funds) {
		this.funds = funds;
	}

	public Long getTotalEntries() {
		return totalEntries;
	}

	public void setTotalEntries(Long totalEntries) {
		this.totalEntries = totalEntries;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "SearchFundsResponse{" +
				"funds=" + funds +
				", totalEntries=" + totalEntries +
				", pageSize=" + pageSize +
				", pages=" + pages +
				", currentPage=" + currentPage +
				", keys=" + keys +
				'}';
	}
}
