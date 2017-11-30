package com.acmed.his.pojo.vo;

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
public class DiagnosisTplVo {

    @ApiModelProperty("诊断分类")
    private String categoryName;

    @ApiModelProperty("模板")
    private List<Detail> list;

    public DiagnosisTplVo(DicItem dicItem, List<DiagnosisTpl> detailList) {
        this.categoryName = null == dicItem?null:dicItem.getDicItemName();
        list = new ArrayList<>();
        for(DiagnosisTpl tpl :detailList){
            list.add(new Detail(tpl));
        }

    }

    @Data
    public class Detail {
        @ApiModelProperty("诊疗项目id")
        private Integer id;

        @ApiModelProperty("诊断分类:字典中获取")
        private String category;

        @ApiModelProperty("诊断")
        private String diagnosis;

        @ApiModelProperty("药品备注")
        private String memo;

        public Detail(DiagnosisTpl tpl) {
            this.id = tpl.getId();
            this.category = tpl.getCategory();
            this.diagnosis = tpl.getDiagnosis();
            this.memo = tpl.getMemo();
        }
    }

}
