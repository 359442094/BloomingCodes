package cn.blooming.bep.data.api;

import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

import cn.blooming.bep.data.api.model.QueryCardBinRequest;
import cn.blooming.bep.data.api.model.QueryCardBinResponse;

@ExportAPI
public interface QueryCardBinAPI {

	@ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
	/**
    * 查询基金基本数据
    * @param request
    * @return
    */
	QueryCardBinResponse process(@APIParam(name="request") QueryCardBinRequest request);
    
	
}
