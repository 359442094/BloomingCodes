package cn.blooming.bep.crawler.data.controller;
import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.model.service.ext.QueryFundService;
import cn.blooming.bep.data.api.QueryFundTypeAPI;
import cn.blooming.bep.data.api.model.QueryFundTypeRequest;
import cn.blooming.bep.data.api.model.QueryFundTypeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = {"查询基金类型列表"})
public class QueryFundTypeController implements QueryFundTypeAPI {

    private static final Logger log= LoggerFactory.getLogger(QueryFundTypeController.class);

    @Autowired
    private QueryFundService queryFundService;

    @ResponseBody
    @RequestMapping(path = "/fund/types",method = RequestMethod.POST)
    @ApiOperation(value = "查询基金类型列表",notes = "查询基金类型列表")
    @Override
    public QueryFundTypeResponse process(QueryFundTypeRequest queryFundTypeRequest) {
        queryFundTypeRequest.setVersion(ClientInfo.VERSION);
        log.debug("查询基金类型列表:"+queryFundTypeRequest);
        List<String> resultTypes=new ArrayList<>();
        List<String> types = queryFundService.getFundTypes();

        for (String type : types) {
            switch (type){
                case "stock":
                    resultTypes.add("股票型");
                    break;
                case "mix":
                    resultTypes.add("混合型");
                    break;
                case "fof":
                    resultTypes.add("混合-FOF");
                    break;
                case "bond":
                    resultTypes.add("债券型");
                    break;
                case "index":
                    resultTypes.add("股票指数");
                    break;
                case "etf":
                    resultTypes.add("联接基金");
                    break;
                case "mmf":
                    resultTypes.add("货币型");
                    break;
                case "qdii":
                    resultTypes.add("QDII");
                    break;
                case "gf":
                    resultTypes.add("保本型");
                    break;
                case "sf":
                    resultTypes.add("固定收益");
                    break;
                default:
                    resultTypes.add("未知类型");
                    break;
            }
        }

        QueryFundTypeResponse queryFundTypeResponse=new QueryFundTypeResponse();
        queryFundTypeResponse.setTypes(resultTypes);
        log.debug("返回结果[QueryFundTypeResponse]:"+queryFundTypeResponse);
        return queryFundTypeResponse;
    }
}
