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

    @ApiModelProperty("诊断分类:字典中获取")
    private String category;

    @ApiModelProperty("模板")
    private List<DiagnosisDetail> list;

    public DiagnosisTplVo(DicItem dicItem, List<DiagnosisTpl> detailList) {
        this.categoryName = null == dicItem?null:dicItem.getDicItemName();
        this.category = dicItem.getDicItemCode();
        list = new ArrayList<>();
        for(DiagnosisTpl tpl :detailList){
            list.add(new DiagnosisDetail(tpl));
        }

    }

    public DiagnosisTplVo(DicItem dicItem) {
        this.categoryName = null == dicItem?null:dicItem.getDicItemName();
        this.category = dicItem.getDicItemCode();
    }

    @Data
    public class DiagnosisDetail {
        @ApiModelProperty("诊疗项目id")
        private Integer id;

        @ApiModelProperty("诊断")
        private String diagnosis;

        @ApiModelProperty("药品备注")
        private String memo;

        public DiagnosisDetail(DiagnosisTpl tpl) {
            this.id = tpl.getId();
            this.diagnosis = tpl.getDiagnosis();
            this.memo = tpl.getMemo();
        }
    }

}
