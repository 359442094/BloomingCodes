package cn.blooming.bep.data.api;

import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

import cn.blooming.bep.data.api.model.SearchFundsRequest;
import cn.blooming.bep.data.api.model.SearchFundsResponse;

@ExportAPI
public interface SearchFundsAPI {

	@ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
	/**
    * 查询银行列表数据
    * @param request
    * @return
    */
	SearchFundsResponse process(@APIParam(name="request") SearchFundsRequest request) throws Exception;
}
