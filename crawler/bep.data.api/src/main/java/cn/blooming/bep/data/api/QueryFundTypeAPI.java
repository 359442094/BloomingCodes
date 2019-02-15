package cn.blooming.bep.data.api;

import cn.blooming.bep.data.api.model.QueryFundTypeRequest;
import cn.blooming.bep.data.api.model.QueryFundTypeResponse;
import com.bloosoming.rpcproxy.annotation.APIParam;
import com.bloosoming.rpcproxy.annotation.ExportAPI;
import com.bloosoming.rpcproxy.annotation.ExportAPIMethod;

@ExportAPI
public interface QueryFundTypeAPI {
    @ExportAPIMethod(httpMethod = ExportAPIMethod.HTTP_METHOD_POST)
    /**
     * 查询所有基金类型
     * @param request
     * @return
     */
    QueryFundTypeResponse process(@APIParam(name="request") QueryFundTypeRequest queryFundTypeRequest);
}
