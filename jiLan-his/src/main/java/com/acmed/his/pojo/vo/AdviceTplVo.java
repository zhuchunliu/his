package com.acmed.his.pojo.vo;

import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.model.DicItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class AdviceTplVo {

    @ApiModelProperty("诊断分类")
    private String categoryName;

    @ApiModelProperty("医嘱分类:字典中获取")
    private String category;

    private List<AdviceDetail> list;

    public AdviceTplVo(DicItem dicItem, List<AdviceTpl> detailList) {
        this.categoryName = null == dicItem?null:dicItem.getDicItemName();
        this.category = dicItem.getDicItemCode();
        list = new ArrayList<>();
        for(AdviceTpl tpl :detailList){
            list.add(new AdviceDetail(tpl));
        }
    }

    public AdviceTplVo(DicItem dicItem) {
        this.categoryName = dicItem.getDicItemName();
        this.category = dicItem.getDicItemCode();
    }

    @Data
    public class AdviceDetail {
        public AdviceDetail(AdviceTpl tpl){
            this.id = tpl.getId();
            this.advice = tpl.getAdvice();
            this.memo = tpl.getMemo();
        }

        @ApiModelProperty("医嘱模板id")
        private Integer id;

        @ApiModelProperty("医嘱")
        private String advice;

        @ApiModelProperty("备注")
        private String memo;


    }

}
