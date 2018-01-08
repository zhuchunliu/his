package com.acmed.his.api;

import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.pojo.vo.DrugVo;
import com.acmed.his.service.DrugInspectReportManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Darren on 2018-01-04
 **/
@Api(tags = "药品管理")
@RequestMapping("/report")
@RestController
public class DrugInspectReportApi {

    @Autowired
    private DrugInspectReportManager reportManager;

    @ApiOperation(value = "药品销售统计")
    @PostMapping("/drug")
    public ResponseResult<PageResult<DrugVo>> getDrugList(@RequestBody(required = false) PageBase<DrugMo> pageBase,
                                                          @AccessToken AccessInfo info){


        return null;
    }

    @ApiOperation(value = "检查项目统计")
    @PostMapping("/inspect")
    public ResponseResult<PageResult<DrugVo>> getInspectList(@RequestBody(required = false) PageBase<DrugMo> pageBase,
                                                          @AccessToken AccessInfo info){
        return null;
    }


}
