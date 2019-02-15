package cn.blooming.bep.crawler.model.mapper;

import cn.blooming.bep.crawler.model.entity.Bank;
import cn.blooming.bep.crawler.model.entity.BankExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	long countByExample(BankExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int deleteByExample(BankExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int deleteByPrimaryKey(String code);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int insert(Bank record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int insertSelective(Bank record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	List<Bank> selectByExample(BankExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	Bank selectByPrimaryKey(String code);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByExampleSelective(@Param("record") Bank record, @Param("example") BankExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByExample(@Param("record") Bank record, @Param("example") BankExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByPrimaryKeySelective(Bank record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table bank
	 * @mbg.generated  Thu Jan 10 14:50:37 CST 2019
	 */
	int updateByPrimaryKey(Bank record);
}