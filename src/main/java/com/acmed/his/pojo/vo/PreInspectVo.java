package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查处方
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreInspectVo {
    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<PreInspectVo.Inspect> inspectList;

    @ApiModelProperty("附加费")
    private List<PreInspectVo.Charge> chargeList;

    public PreInspectVo(Prescription prescription, List<com.acmed.his.model.Inspect> preInspectist, List<com.acmed.his.model.Charge> preChargeList) {
        this.id = prescription.getId();
        this.applyId = prescription.getApplyId();
        if(null != preInspectist && 0 != preInspectist.size()){
            inspectList = new ArrayList<>();

            preInspectist.forEach((obj)->{
                PreInspectVo.Inspect inspect = new PreInspectVo.Inspect();
                BeanUtils.copyProperties(obj,inspect);
                inspectList.add(inspect);
            });
        }

        if(null != preChargeList && 0 != preChargeList.size()){
            chargeList = new ArrayList<>();
            preChargeList.forEach((obj)->{
                PreInspectVo.Charge charge = new PreInspectVo.Charge();
                BeanUtils.copyProperties(obj,charge);
                chargeList.add(charge);
            });
        }
    }

    @Data
    public class Inspect{

        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

        @ApiModelProperty("病情摘要")
        private String summary;

        @ApiModelProperty("检查诊断")
        private String diagnosis;

    }

    @Data
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;

//        @ApiModelProperty("费用")
//        private Double fee;
    }
}
