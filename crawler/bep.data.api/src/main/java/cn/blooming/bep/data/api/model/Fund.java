package cn.blooming.bep.data.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 基金数据类
 * */
public class Fund implements Serializable {

    private static final long serialVersionUID = -4766267391984891023L;
    /**
     *  基金代码
     * */
    @ApiModelProperty(value = "基金代码", required = true)
    private String code;
    /**
     * 基金名称
     */
    @ApiModelProperty(value = "基金名称", required = true)
    private String name;
    /**
     * 基金类别
     * stock: 股票型
     * mix：混合型
     * bond: 债券型
     * index: 指数型
     * etf: ETF连接型 QDII: QDII LOF: LOF FOF: FOF 1
     * sf:分级型/结构型
     * gf:保本型
     * mmf:货币型
     */
    @ApiModelProperty(value = "基金类别", required = true)
    private String type;
    /**
     * 基金公司代码
     */
    @ApiModelProperty(value = "基金公司代码", required = true)
    private String fundHouseCode;
    /**
     * 今年来
     * */
    private BigDecimal rateCurrentYear;
    /**
     * 最近一周
     * */
    private BigDecimal rate7days;
    /**
     * 单位净值
     */
    @ApiModelProperty(value = "单位净值", required = true)
    private BigDecimal netValue;
    /**
     * 征税估定价值
     */
    @ApiModelProperty(value = "征税估定价值", required = true)
    private BigDecimal rateNetValue;
    /**
     * 累计净值
     */
    @ApiModelProperty(value = "累计净值", required = true)
    private BigDecimal totalValue;
    /**
     * 更新日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期", required = true)
    private Date updatedTime;
    /**
     * 上一月收益率
     */
    @ApiModelProperty(value = "上一月收益率", required = true)
    private BigDecimal rateMonth;
    /**
     * 上一季度收益率
     */
    @ApiModelProperty(value = "上一季度收益率", required = true)
    private BigDecimal rateQuarter;
    /**
     * 半年收益率
     */
    @ApiModelProperty(value = "半年收益率", required = true)
    private BigDecimal rateHalf;
    /**
     * 上一年度收益率
     */
    @ApiModelProperty(value = "上一年度收益率", required = true)
    private BigDecimal rateYear;
    /**
     * 三年收益率
     */
    @ApiModelProperty(value = "三年收益率", required = true)
    private BigDecimal rateThreeYear;
    /**
     * 五年收益率
     */
    @ApiModelProperty(value = "五年收益率", required = true)
    private BigDecimal rateFiveYear;
    /**
     * 自成立日起的收益率
     */
    @ApiModelProperty(value = "自成立日起的收益率", required = true)
    private BigDecimal rateStart;
    /**
     * 成立日
     */
    @ApiModelProperty(value = "成立日", required = true)
    private Date startDate;
    /**
     * 风险等级 R1 谨慎型 R2 稳健型 R3 平衡型 R4 进取型 R5 激进型 1
     */
    @ApiModelProperty(value = "风险等级", required = true)
    private String riskLevel;
    /**
     * 基金规模 (单位万)
     */
    @ApiModelProperty(value = "基金规模", required = true)
    private BigDecimal scale;
    /**
     * 基金评级
     */
    @ApiModelProperty(value = "基金评级", required = true)
    private String level;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFundHouseCode() {
        return fundHouseCode;
    }

    public void setFundHouseCode(String fundHouseCode) {
        this.fundHouseCode = fundHouseCode;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public BigDecimal getRateNetValue() {
        return rateNetValue;
    }

    public void setRateNetValue(BigDecimal rateNetValue) {
        this.rateNetValue = rateNetValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BigDecimal getRateMonth() {
        return rateMonth;
    }

    public void setRateMonth(BigDecimal rateMonth) {
        this.rateMonth = rateMonth;
    }

    public BigDecimal getRateQuarter() {
        return rateQuarter;
    }

    public void setRateQuarter(BigDecimal rateQuarter) {
        this.rateQuarter = rateQuarter;
    }

    public BigDecimal getRateHalf() {
        return rateHalf;
    }

    public void setRateHalf(BigDecimal rateHalf) {
        this.rateHalf = rateHalf;
    }

    public BigDecimal getRateYear() {
        return rateYear;
    }

    public void setRateYear(BigDecimal rateYear) {
        this.rateYear = rateYear;
    }

    public BigDecimal getRateThreeYear() {
        return rateThreeYear;
    }

    public void setRateThreeYear(BigDecimal rateThreeYear) {
        this.rateThreeYear = rateThreeYear;
    }

    public BigDecimal getRateFiveYear() {
        return rateFiveYear;
    }

    public void setRateFiveYear(BigDecimal rateFiveYear) {
        this.rateFiveYear = rateFiveYear;
    }

    public BigDecimal getRateStart() {
        return rateStart;
    }

    public void setRateStart(BigDecimal rateStart) {
        this.rateStart = rateStart;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getRateCurrentYear() {
        return rateCurrentYear;
    }

    public void setRateCurrentYear(BigDecimal rateCurrentYear) {
        this.rateCurrentYear = rateCurrentYear;
    }

    public BigDecimal getRate7days() {
        return rate7days;
    }

    public void setRate7days(BigDecimal rate7days) {
        this.rate7days = rate7days;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", fundHouseCode='" + fundHouseCode + '\'' +
                ", rateCurrentYear=" + rateCurrentYear +
                ", rate7days=" + rate7days +
                ", netValue=" + netValue +
                ", rateNetValue=" + rateNetValue +
                ", totalValue=" + totalValue +
                ", updatedTime=" + updatedTime +
                ", rateMonth=" + rateMonth +
                ", rateQuarter=" + rateQuarter +
                ", rateHalf=" + rateHalf +
                ", rateYear=" + rateYear +
                ", rateThreeYear=" + rateThreeYear +
                ", rateFiveYear=" + rateFiveYear +
                ", rateStart=" + rateStart +
                ", startDate=" + startDate +
                ", riskLevel='" + riskLevel + '\'' +
                ", scale=" + scale +
                ", level='" + level + '\'' +
                '}';
    }
}
