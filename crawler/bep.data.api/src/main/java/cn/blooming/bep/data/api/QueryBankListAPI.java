package cn.blooming.bep.data.api;

import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

import cn.blooming.bep.data.api.model.QueryBankListRequest;
import cn.blooming.bep.data.api.model.QueryBankListResponse;

@ExportAPI
public interface QueryBankListAPI {

	@ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
	/**
    * 查询银行列表数据
    * @param request
    * @return
    */
	QueryBankListResponse process(@APIParam(name="request") QueryBankListRequest request);
}
