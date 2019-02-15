package cn.blooming.bep.data.api;

import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

import cn.blooming.bep.data.api.model.QueryFundDataRequest;
import cn.blooming.bep.data.api.model.QueryFundDataResponse;

@ExportAPI
public interface QueryFundDataAPI {

	@ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
	/**
    * 查询基金基本数据
    * @param request
    * @return
    */
	QueryFundDataResponse process(@APIParam(name="request") QueryFundDataRequest request);

}
