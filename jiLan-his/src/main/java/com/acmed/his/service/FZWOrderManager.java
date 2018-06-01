package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.FZWOrderMapper;
import com.acmed.his.dao.FZWServicePackageMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.fzw.FZWOrder;
import com.acmed.his.model.fzw.FZWServicePackage;
import com.acmed.his.pojo.mo.FZWOrderMo;
import com.acmed.his.util.ResponseUtil;
import com.acmed.his.util.UUIDUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FZWOrderManager
 *
 * @author jimson
 * @date 2018/5/18
 */
@Slf4j
@Service
@Transactional
public class FZWOrderManager {
    @Autowired
    private FZWOrderMapper fzwOrderMapper;

    @Autowired
    private FZWServicePackageMapper fzwServicePackageMapper;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private WxManager wxManager;

    /**
     * 获取所有服务包
     * @return
     */
    public List<FZWServicePackage> allFZWServicePackage(){
        return fzwServicePackageMapper.selectAll();
    }

    /**
     * 创建订单
     * @param fzwOrder
     */
    public FZWOrder createFZWOrder(FZWOrder fzwOrder){
        fzwOrder.setId(UUIDUtil.generate32());
        fzwOrder.setCreateAt(LocalDateTime.now().toString());
        fzwOrder.setOrderNum(commonManager.getFormatVal( "fzwOrderCode", "000000000"));
        fzwOrder.setStatus(1);
        fzwOrder.setIsSend(0);
        FZWServicePackage fzwServicePackage = fzwServicePackageMapper.selectByPrimaryKey(fzwOrder.getFZWServicePackageId());
        if (fzwServicePackage==null){
            throw new BaseException(StatusCode.ERROR_PARAM,"服务包不存在");
        }
        fzwOrder.setFZWServicePackageStr(fzwServicePackage.getName());
        fzwOrder.setPrice(fzwServicePackage.getPrice());
        fzwOrder.setModifyAt(null);
        fzwOrder.setModifyBy(null);
        fzwOrderMapper.insert(fzwOrder);
        return fzwOrder;
    }

    public void patientUpdateFzwOrder(String patientId,FZWOrderMo fzwOrderMo){
        FZWOrder fzwOrder = fzwOrderMapper.selectByPrimaryKey(fzwOrderMo.getId());
        if (fzwOrder==null){
            throw new BaseException(StatusCode.ERROR_PARAM,"订单不存在");
        }
        if(!fzwOrder.getCreateBy().equals(patientId)){
            // 非本人订单
            throw new BaseException(StatusCode.ERROR_PARAM,"订单不存在");
        }
        if (fzwOrder.getStatus()!=1){
            throw new BaseException(StatusCode.ERROR_PARAM,"该订单不支持修改");
        }
        FZWServicePackage fzwServicePackage = fzwServicePackageMapper.selectByPrimaryKey(fzwOrderMo.getFZWServicePackageId());
        if (fzwServicePackage==null){
            throw new BaseException(StatusCode.ERROR_PARAM,"服务包不存在");
        }
        fzwOrder.setFZWServicePackageStr(fzwServicePackage.getName());
        fzwOrder.setFZWServicePackageId(fzwOrderMo.getFZWServicePackageId());
        fzwOrder.setPrice(fzwServicePackage.getPrice());
        fzwOrder.setPrice(fzwServicePackage.getPrice());
        fzwOrder.setName(fzwOrderMo.getName());
        fzwOrder.setSex(fzwOrderMo.getSex());
        fzwOrder.setEmail(fzwOrderMo.getEmail());
        fzwOrder.setExamDate(fzwOrderMo.getExamDate());
        fzwOrder.setMobile(fzwOrderMo.getMobile());
        fzwOrder.setDoctor(fzwOrderMo.getDoctor());
        fzwOrder.setHospital(fzwOrderMo.getHospital());
        fzwOrder.setRemark(fzwOrderMo.getRemark());
        fzwOrder.setModifyBy(patientId);
        fzwOrder.setModifyAt(LocalDateTime.now().toString());
        fzwOrderMapper.updateByPrimaryKey(fzwOrder);
    }

    public int update(String id,Integer status,String feeType,String returnOrderCode,String otherReturnOrderCode,String otherOrderCode,String modifyBy,Integer isSend){
        FZWOrder fzwOrder = new FZWOrder();
        fzwOrder.setId(id);
        fzwOrder.setStatus(status);
        fzwOrder.setFeeType(feeType);
        fzwOrder.setReturnOrderCode(returnOrderCode);
        fzwOrder.setOtherReturnOrderCode(otherReturnOrderCode);
        fzwOrder.setOtherOrderCode(otherOrderCode);
        fzwOrder.setModifyBy(modifyBy);
        fzwOrder.setIsSend(isSend);
        fzwOrder.setModifyAt(LocalDateTime.now().toString());
        return fzwOrderMapper.updateByPrimaryKeySelective(fzwOrder);
    }

    public List<FZWOrder> getByCrateByAndStatus(String createBy,Integer status){
        Example example = new Example(FZWOrder.class);
        example.setOrderByClause("createAt desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("createBy",createBy);
        if (status!=null){
            criteria.andEqualTo("status",status);
        }
        return fzwOrderMapper.selectByExample(example);
    }
    public FZWOrder getById(String id){
        return fzwOrderMapper.selectByPrimaryKey(id);
    }

    public boolean sendOrderToFzw(FZWOrder fzwOrder){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "d25c4647aa76adc0549c515720754936");

        Map<String,String> hmap = new HashMap<>();
        hmap.put("yuyue","预约");
        hmap.put("name",fzwOrder.getName());
        hmap.put("date","预约");
        hmap.put("sex",fzwOrder.getSex());
        hmap.put("email",fzwOrder.getEmail());
        hmap.put("user-id",fzwOrder.getExamDate());
        hmap.put("tel",fzwOrder.getMobile());
        hmap.put("s_province","省");
        hmap.put("s_city","市");
        hmap.put("s_county","区");
        hmap.put("detail-add","详细地址");
        hmap.put("title",fzwOrder.getDoctor());
        hmap.put("company","暂无最近体检机构");
        hmap.put("sel1",fzwOrder.getFZWServicePackageStr());
        hmap.put("sel2",fzwOrder.getHospital());
        hmap.put("z_content",fzwOrder.getRemark());
        hmap.put("order_num",fzwOrder.getId());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONUtils.toJSONString(hmap), headers);
        JSONObject json = restTemplate.postForObject("http://www.4000663363.com/index.php/consultation/personala", formEntity, JSONObject.class);
        System.err.println(json);
        System.err.println(json.get("success"));


        if (StringUtils.equals("true",json.get("success").toString())){
            return true;
        }else {
            log.error("订单支付成功，发送fzw失败，订单号{}",fzwOrder.getId());
            return false;
        }
    }

    public List<FZWOrder> getBy(FZWOrder fzwOrder){
        return this.fzwOrderMapper.select(fzwOrder);
    }

    public boolean refund(String id,String modifyBy){
        FZWOrder fzwOrder = this.fzwOrderMapper.selectByPrimaryKey(id);
        Map<String, String> refund = null;
        try {
            refund = wxManager.refund(fzwOrder.getId(), fzwOrder.getPrice().toString(), "名医直通取消订单", fzwOrder.getPrice().toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fzw退款失败{}",refund);
            throw new BaseException(StatusCode.FAIL,"微信退款失败");
        }
        String returnCode = refund.get("return_code");
        String resultCode = refund.get("result_code");
        if (StringUtils.equals("SUCCESS",returnCode) && StringUtils.equals("SUCCESS",resultCode)){
            // 退款成功
            //商户退款单号
            String outRefundNo = refund.get("out_refund_no");
            // 微信退款单号
            String refundId = refund.get("refund_id");
            int update = this.update(id,4,null,outRefundNo,refundId,null,modifyBy,null);
            if (update!=0){
                return true;
            }else {
                return false;
            }
        }
        log.error("fzw退款失败{}",refund);
        throw new BaseException(StatusCode.FAIL,"微信退款失败");


    }


}
