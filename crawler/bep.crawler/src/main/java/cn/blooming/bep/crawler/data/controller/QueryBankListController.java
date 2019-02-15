package cn.blooming.bep.crawler.data.controller;

import java.util.LinkedList;
import java.util.List;

import cn.blooming.bep.crawler.model.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.blooming.bep.crawler.model.entity.Bank;
import cn.blooming.bep.crawler.model.entity.BankExample;
import cn.blooming.bep.data.api.QueryBankListAPI;
import cn.blooming.bep.data.api.model.QueryBankListRequest;
import cn.blooming.bep.data.api.model.QueryBankListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"查询银行列表数据"})
public class QueryBankListController implements QueryBankListAPI {

	private static final Logger log= LoggerFactory.getLogger(QueryBankListController.class);

	@Value("${bank.image.urlprefix}")
	private String bankLogoUrlPrefix = null;

	@Autowired
	private BankService bankService;
	
	@Override
	@ResponseBody
	@RequestMapping(path = "/bank/list", method = {RequestMethod.POST})
	@ApiOperation(value = "查询银行列表数据", notes = "查询银行列表数据")
	public QueryBankListResponse process(QueryBankListRequest request) {
		log.debug("查询银行列表参数:"+request);
		BankExample example = new BankExample();
		example.setOrderByClause("code asc");
		List<Bank> list = bankService.selectByExample(example);
		
		List<cn.blooming.bep.data.api.model.Bank> result = new LinkedList<>();
		if(list != null && list.size() > 0) {
			for(Bank bank : list) {
				cn.blooming.bep.data.api.model.Bank b = new cn.blooming.bep.data.api.model.Bank();
				
				b.setCode(bank.getCode());
				b.setName(bank.getName());
				b.setLogoUrl(bankLogoUrlPrefix + bank.getCode() + ".jpg");
				result.add(b);
			}
		}
		
		QueryBankListResponse rsp = new QueryBankListResponse();
		rsp.setBanks(result);
		log.debug("返回结果[QueryBankListResponse]:"+rsp);
		return rsp;
	}

}
