package cn.blooming.bep.data.api;

import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

import cn.blooming.bep.data.api.model.QueryFundHistoryRequest;
import cn.blooming.bep.data.api.model.QueryFundHistoryResponse;

@ExportAPI
public interface QueryFundRateHistoryAPI {

	@ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
	/**
    * 查询基金历史数据
    * @param request
    * @return
    */
	QueryFundHistoryResponse process(@APIParam(name="request") QueryFundHistoryRequest request);
    
	
}
