package cn.blooming.bep.crawler.model.mapper;

import cn.blooming.bep.crawler.model.entity.FundHistory;
import cn.blooming.bep.crawler.model.entity.FundHistoryExample;
import cn.blooming.bep.crawler.model.entity.FundHistoryKey;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FundHistoryMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	long countByExample(FundHistoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int deleteByExample(FundHistoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int deleteByPrimaryKey(FundHistoryKey key);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int insert(FundHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int insertSelective(FundHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	List<FundHistory> selectByExample(FundHistoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	FundHistory selectByPrimaryKey(FundHistoryKey key);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByExampleSelective(@Param("record") FundHistory record, @Param("example") FundHistoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByExample(@Param("record") FundHistory record, @Param("example") FundHistoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByPrimaryKeySelective(FundHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table fund_history
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByPrimaryKey(FundHistory record);
}