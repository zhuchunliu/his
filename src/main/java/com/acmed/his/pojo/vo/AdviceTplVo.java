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

    private List<Detail> list;

    public AdviceTplVo(DicItem dicItem, List<AdviceTpl> detailList) {
        this.categoryName = null == dicItem?null:dicItem.getDicItemName();
        list = new ArrayList<>();
        for(AdviceTpl tpl :detailList){
            list.add(new Detail(tpl));
        }
    }

    @Data
    public class Detail {
        public Detail(AdviceTpl tpl){
            this.id = tpl.getId();
            this.category = tpl.getCategory();
            this.advice = tpl.getAdvice();
        }

        @ApiModelProperty("医嘱模板id")
        private Integer id;

        @ApiModelProperty("医嘱")
        private String advice;

        @ApiModelProperty("医嘱分类:字典中获取")
        private String category;
    }

}
