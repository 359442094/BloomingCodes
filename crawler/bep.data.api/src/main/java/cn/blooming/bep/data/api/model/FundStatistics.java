package cn.blooming.bep.data.api.model;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 同类均值实体类
 * */
public class FundStatistics implements Serializable {
    private static final long serialVersionUID = 6043566785369303173L;
    /**
     * 基金类型
     * */
    @ApiModelProperty(value = "基金类型")
    private String fundType;
    /**
     * 日期
     * */
    @ApiModelProperty(value = "日期", required = true)
    private Date date;
    /**
     * 当天均值
     * */
    @ApiModelProperty(value = "当天同类均值", required = true)
    private BigDecimal average;
    /**
     * 前一日相比的增长率
     * */
    @ApiModelProperty(value = "前一日相比的增长率", required = true)
    private BigDecimal rate;
    /**
     * 更新时间
     * */
    @ApiModelProperty(value = "更新时间")
    private Date updated;
   /* *//**
     * 近一周均值
     * *//*
    @ApiModelProperty(value = "近一周均值")
    private BigDecimal average7days;
    *//**
     * 近一月均值
     * *//*
    @ApiModelProperty(value = "近一月均值")
    private BigDecimal average1month;
    *//**
     * 近三月均值
     * *//*
    @ApiModelProperty(value = "近三月均值")
    private BigDecimal average3month;
    *//**
     * 近半年均值
     * *//*
    private BigDecimal average6month;
    *//**
     * 近一年均值
     * *//*
    @ApiModelProperty(value = "近一年均值")
    private BigDecimal average1year;
    *//**
     * 近二年均值
     * *//*
    @ApiModelProperty(value = "近二年均值")
    private BigDecimal average2year;
    *//**
     * 近三年均值
     * *//*
    @ApiModelProperty(value = "近三年均值")
    private BigDecimal average3year;*/

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "FundStatistics{" +
                "fundType='" + fundType + '\'' +
                ", date=" + date +
                ", average=" + average +
                ", rate=" + rate +
                ", updated=" + updated +
                '}';
    }
}
