package cn.blooming.bep.crawler;

public class ErrorCode {

	/**
	 * cardbin不存在
	 */
	public static final String CARDBIN_NOT_FOUND = "DA0001";

	/**
	 * 截取失败
	 */
	public static final String INTERCEPTION_ERROR = "DA0002";

	/**
	 * 历史净值总记录数为空
	 * */
	public static final String FUNDHISTORY_TOTAL_NOT_FOUND = "DA0003";

	/**
	 * Data JSON 为空
	 * */
	public static final String FUNDHISTORY_JSON_NOT_FOUND = "DA0004";

	/**
	 * 基金历史参数为空
	 * */
	public static final String FUNDHISTORY_PARAMETER_NOT_FOUND = "DA0005";

	/**
	 * SearchFundsController中用户输入类型未能识别
	 * */
	public static final String USER_INPUT_TYPE_NOT_RECOGNIZED = "DA0006";

}
