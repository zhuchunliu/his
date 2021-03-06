package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.DicItem;
import com.acmed.his.pojo.mo.DicItemMo;
import com.acmed.his.pojo.mo.DicItemRemoveMo;
import com.acmed.his.pojo.mo.DicItemUpMo;
import com.acmed.his.pojo.vo.DicDetailVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BaseInfoApi
 *
 * @author jimson
 * @date 2017/11/21
 */
@Api(tags = "字典维护")
@RestController
@RequestMapping("dicItem")
public class DicItemApi {
    @Autowired
    private BaseInfoManager baseInfoManager;

    @ApiOperation(value = "添加字典")
    @PostMapping("/add")
    public ResponseResult addDicItem(@RequestBody DicItemMo dicItem,@AccessToken AccessInfo info){
        DicItem dicItem1 = new DicItem();
        BeanUtils.copyProperties(dicItem,dicItem1);
        return ResponseUtil.setSuccessResult(ImmutableMap.of("id",baseInfoManager.addDicItem(dicItem1,info.getUser())));
    }
    @ApiOperation(value = "查询所有字典")
    @GetMapping("/list")
    public ResponseResult<List<DicDetailVo>> allDicItem(){
        List<DicDetailVo> allDicItems = baseInfoManager.getAllDicItems();
        return ResponseUtil.setSuccessResult(allDicItems);
    }

    @ApiOperation(value = "根据字典类型查找字典列表")
    @GetMapping("/dicTypeCode")
    public ResponseResult<List<DicItem>> getDicItemsByDicTypeCode(@ApiParam("字典类型")@RequestParam("dicTypeCode") String dicTypeCode,
                                                                  @AccessToken AccessInfo info){
        List<DicItem> dicItemsByDicTypeCode = baseInfoManager.getDicItemsByDicTypeCode(dicTypeCode,info.getUser());
        return ResponseUtil.setSuccessResult(dicItemsByDicTypeCode);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/removed")
    public ResponseResult removed(@RequestBody DicItemRemoveMo dicItemRemoveMo){
        DicItem dicItem = baseInfoManager.getDicItem(dicItemRemoveMo.getDicTypeCode(), dicItemRemoveMo.getDicItemCode());
        if (dicItem != null){
            dicItem.setDicItemCode(null);
            dicItem.setDicTypeCode(null);
            dicItem.setRemoved("1");
            baseInfoManager.updateDicItem(dicItem);
        }
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody DicItemUpMo dicItemUpMo,@AccessToken AccessInfo info){
        DicItem dicItem = baseInfoManager.getDicItem(dicItemUpMo.getDicTypeCode(), dicItemUpMo.getDicItemCode());
        if (dicItem != null){
            dicItem.setDicItemCode(null);
            dicItem.setRemoved(null);
            dicItem.setDicItemName(dicItemUpMo.getDicItemName());
            dicItem.setStartTime(dicItemUpMo.getStartTime());
            dicItem.setEndTime(dicItemUpMo.getEndTime());
            dicItem.setOrgCode(info.getUser().getOrgCode());
            int i = baseInfoManager.updateDicItem(dicItem);
            if (i==0){
                return ResponseUtil.setErrorMeg(StatusCode.FAIL,"已存在相同的字典名");
            }else {
                return ResponseUtil.setSuccessResult();
            }
        }else {
            // 不存在
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"参数错误");
        }

    }
}
