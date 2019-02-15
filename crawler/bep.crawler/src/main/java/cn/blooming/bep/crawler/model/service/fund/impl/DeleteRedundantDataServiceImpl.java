package cn.blooming.bep.crawler.model.service.fund.impl;

import cn.blooming.bep.crawler.ClientInfo;
import cn.blooming.bep.crawler.model.entity.*;
import cn.blooming.bep.crawler.model.service.FundHistoryService;
import cn.blooming.bep.crawler.model.service.FundHouseService;
import cn.blooming.bep.crawler.model.service.FundService;
import cn.blooming.bep.crawler.model.service.fund.DeleteRedundantDataService;
import cn.blooming.bep.fund.api.QueryAllBankFundAPI;
import cn.blooming.bep.fund.model.QueryAllBankFundRequest;
import cn.blooming.bep.fund.model.QueryAllBankFundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteRedundantDataServiceImpl implements DeleteRedundantDataService {

    private static final Logger logger= LoggerFactory.getLogger(DeleteRedundantDataServiceImpl.class);

    @Autowired
    private QueryAllBankFundAPI queryAllBankFundAPI;

    @Autowired
    private FundService fundService;

    @Autowired
    private FundHouseService fundHouseService;

    @Autowired
    private FundHistoryService fundHistoryService;

    @Override
    public void delete() {
        //开始删除多余数据
        deleteRedundantData();
    }

    /**
     * 删除多余数据
     * */
    private void deleteRedundantData(){
        //开始删除多余数据
        //获取正在售卖基金产品列表
        QueryAllBankFundRequest request=new QueryAllBankFundRequest();
        request.setClientId(ClientInfo.CLIENT_ID);
        request.setStaffUsername(ClientInfo.STAFF_USERNAME);
        request.setVersion(ClientInfo.VERSION);
        QueryAllBankFundResponse response = queryAllBankFundAPI.process(request);
        //获取需要过滤的数据列表
        String[] codesArray = response.getFundCodes();
        //转换成List方便下步操作
        List<String> filterCodes = arrayConvertToList(codesArray);

        //删除数据顺序不可颠倒
        //1.删除基金历史净值
        deleteFundHistory(filterCodes);
        //2.删除基金信息
        deleteFund(filterCodes);
        //3.删除基金公司
        //获取需要跳过删除的基金公司fundHouseCode
        List<String> fundHouseCodes = getFundHouseCodes(filterCodes);

        deleteFundHouse(fundHouseCodes);
    }
    /**
     * 获取需要删除的基金公司code
     * */
    private List<String> getFundHouseCodes(List<String> filterCodes){
        logger.debug("正在获取多余基金公司数据开始");
        List<String> fundHouseCodes=new ArrayList<>();
        //过滤非售卖产品的基金公司
        FundExample fundExample=new FundExample();
        fundExample.createCriteria().andCodeIn(filterCodes);
        List<Fund> funds = fundService.selectByExample(fundExample);
        for (Fund fund : funds) {
            logger.debug("--需要跳过的基金公司代码["+fund.getFundHouseCode()+"]");
            fundHouseCodes.add(fund.getFundHouseCode());
        }
        return fundHouseCodes;
    }

    /**
     * 删除多余基金公司
     * */
    private void deleteFundHouse(List<String> fundHouseCodes){
        logger.debug("----------删除多余基金公司开始----------");
        //删除成功历史净值数据条数
        int SuccessfulCount = 0;
        //删除失败历史净值数据条数
        int FailureCount = 0;
        //应删除数据条数
        FundHouseExample fundHouseExample=new FundHouseExample();
        //查询非过滤codes集合中的基金公司数据
        fundHouseExample.createCriteria().andCodeNotIn(fundHouseCodes);
        List<FundHouse> fundHouses = fundHouseService.selectByExample(fundHouseExample);

        int deleteCount = fundHouses.size();
        for (FundHouse fundHouse : fundHouses) {
            logger.debug("正在删除["+fundHouse.getName()+"]公司数据");
            try{
                int row = fundHouseService.deleteByFundHouseCode(fundHouse.getCode());
                if(row>0){
                    SuccessfulCount+=row;
                    logger.debug("删除["+fundHouse.getName()+"]公司数据成功");
                }else{
                    FailureCount++;
                    logger.debug("删除["+fundHouse.getName()+"]公司数据失败");
                }
            }catch (Exception e){
                FailureCount++;
                logger.debug("删除["+fundHouse.getName()+"]公司数据失败"+e.getMessage());
            }
        }
        logger.debug("----------删除多余基金公司结束----------");
        logger.debug("多余基金公司删除结束:应删除["+deleteCount+"],实际删除成功["+SuccessfulCount+"]条,删除失败["+FailureCount+"]条。");
    }
    /**
     * 删除基金多余数据
     * */
    private void deleteFund(List<String> filterCodes){
        logger.debug("----------删除基金多余产品开始----------");
        //删除成功历史净值数据条数
        int SuccessfulCount = 0;
        //删除失败历史净值数据条数
        int FailureCount = 0;
        //应删除条数
        FundExample fundExample=new FundExample();
        fundExample.createCriteria().andCodeNotIn(filterCodes);
        List<Fund> funds = fundService.selectByExample(fundExample);
        int deleteCount=funds.size();

        for (Fund fund : funds) {
            logger.debug("正在删除["+fund.getName()+"]基金产品数据");
            try{
                int row = fundService.deleteByFundCode(fund.getCode());
                if(row>0){
                    SuccessfulCount+=row;
                    logger.debug("删除["+fund.getName()+"]基金成功");
                }else{
                    FailureCount++;
                    logger.debug("删除["+fund.getName()+"]基金失败");
                }
            }catch (Exception e){
                FailureCount++;
                logger.debug("删除["+fund.getName()+"]旗下所有基金失败"+e);
            }
        }
        logger.debug("----------删除基金多余产品结束----------");
        logger.debug("基金产品数据删除结束:应删除["+deleteCount+"],实际删除成功["+SuccessfulCount+"]条,删除失败["+FailureCount+"]条。");
    }
    /**
     * 删除基金多余历史净值
     * */
    private void deleteFundHistory(List<String> filterCodes){
        logger.debug("----------删除基金多余历史净值开始----------");
        //删除成功历史净值数据条数
        int SuccessfulCount = 0;
        //删除失败历史净值数据条数
        int FailureCount = 0;
        FundHistoryExample fundHistoryExample=new FundHistoryExample();
        fundHistoryExample.createCriteria().andCodeNotIn(filterCodes);
        List<FundHistory> fundHistoryList = fundHistoryService.selectByExample(fundHistoryExample);
        //应删除条数
        int deleteCount=fundHistoryList.size();

        for (FundHistory fundHistory : fundHistoryList) {
            logger.debug("正在删除["+fundHistory.getCode()+"]的历史净值数据");
            try{
                int row = fundHistoryService.deleteByFundCode(fundHistory.getCode());
                if(row>0){
                    SuccessfulCount+=row;
                    logger.debug("删除["+fundHistory.getCode()+"]历史净值成功");
                }else{
                    FailureCount++;
                    logger.debug("删除["+fundHistory.getCode()+"]历史净值失败");
                }
            }catch (Exception e){
                logger.debug("删除["+fundHistory.getCode()+"]历史净值失败"+e.getMessage());
            }
        }
        logger.debug("----------删除基金多余历史净值结束----------");
        logger.debug("历史净值数据删除结束:应删除["+deleteCount+"],实际删除成功["+SuccessfulCount+"]条,删除失败["+FailureCount+"]条。");
    }

    /**
     * 数组转换成集合
     * */
    private List<String> arrayConvertToList(String [] codesArray){
        List<String> codes=new ArrayList<>();
        for (String code : codesArray) {
            codes.add(code);
        }
        return codes;
    }

}
