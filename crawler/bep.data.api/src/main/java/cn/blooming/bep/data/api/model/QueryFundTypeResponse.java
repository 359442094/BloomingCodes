package cn.blooming.bep.data.api.model;

import cn.blooming.bep.common.api.model.BaseResponse;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class QueryFundTypeResponse extends BaseResponse {

    private static final long serialVersionUID = 3498088820279294862L;

    @ApiModelProperty(value = "基金类型集合",required = true)
    private List<String> types;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
