package cn.blooming.bep.crawler.data.controller;

import cn.blooming.bep.crawler.model.service.CardbinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cn.blooming.bep.common.api.exception.BEPServiceException;
import cn.blooming.bep.crawler.ErrorCode;
import cn.blooming.bep.crawler.model.entity.Cardbin;
import cn.blooming.bep.data.api.QueryCardBinAPI;
import cn.blooming.bep.data.api.model.QueryCardBinRequest;
import cn.blooming.bep.data.api.model.QueryCardBinResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"查询CardBin信息"})
public class QueryCardBinController implements QueryCardBinAPI {

	private static final Logger log= LoggerFactory.getLogger(QueryCardBinController.class);

	@Value("${bank.image.urlprefix}")
	private String bankLogoUrlPrefix = null;

	@Autowired
	private CardbinService cardbinService;
	
	@Override
	@ResponseBody
	@RequestMapping(path = "/card/queryCardbin", method = {RequestMethod.POST})
	@ApiOperation(value = "查询CardBin信息", notes = "查询CardBin信息")
	public QueryCardBinResponse process(QueryCardBinRequest request) {
		log.debug("查询CardBin参数:"+request);
		Cardbin card = cardbinService.selectByPrimaryKey(request.getCardbin());
		
		if(card == null) {
			throw new BEPServiceException(ErrorCode.CARDBIN_NOT_FOUND, "Cardbin不存在");
		}
		
		QueryCardBinResponse rsp = new QueryCardBinResponse();
		rsp.setBankCode(card.getBankCode());
		rsp.setBankName(card.getBankName());
		rsp.setCardbin(card.getCardbin());
		rsp.setType(card.getCardType());
		rsp.setBankLogoUrl(bankLogoUrlPrefix + card.getBankCode() + ".jpg");
		log.debug("返回结果[QueryCardBinResponse]:"+rsp);
		return rsp;
	}

}
