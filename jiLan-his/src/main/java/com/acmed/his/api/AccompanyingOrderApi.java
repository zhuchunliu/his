package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.InvitationDto;
import com.acmed.his.pojo.mo.AddAccompanyingOrderConfirmationModel;
import com.acmed.his.pojo.mo.AddAccompanyingOrderModel;
import com.acmed.his.pojo.mo.AppraiseAccompanyingOrderModel;
import com.acmed.his.pojo.vo.AccompanyingOrderPatientVo;
import com.acmed.his.pojo.vo.SuppliersOrderVo;
import com.acmed.his.service.*;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * AccompanyingOrderApi
 *
 * @author jimson
 * @date 2017/12/22
 */
@Api(tags = "就医北上广")
@RestController
@RequestMapping("accompanying")
public class AccompanyingOrderApi {
    @Autowired
    private AccompanyingOrderManager accompanyingOrderManager;

    @Autowired
    private AccompanyingOrderConfirmationManager accompanyingOrderConfirmationManager;

    @Autowired
    private AccompanyingPriceManager accompanyingPriceManager;

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private OrgManager orgManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private WxManager wxManager;

    @Autowired
    private AccompanyingInvitationManager accompanyingInvitationManager;

    @ApiOperation(value = "创建就医北上广订单")
    @PostMapping("createOrder")
    public ResponseResult create(@AccessToken AccessInfo info, @RequestBody AddAccompanyingOrderModel model){
        String patientId = info.getPatientId();
        Integer isAccompanying = model.getIsAccompanying();
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setCreateBy(patientId);
        // 查询是否有订单
        AccompanyingInvitation bypatientId = accompanyingInvitationManager.getBypatientId(patientId);
        if(bypatientId!=null){
            accompanyingOrder.setInvitationCode(bypatientId.getInvitationCode());
        }
        BeanUtils.copyProperties(model,accompanyingOrder);
        Dept deptDetail = deptManager.getDeptDetail(model.getDeptId());
        Integer orgCode = deptDetail.getOrgCode();
        String dept = deptDetail.getDept();
        Org orgDetail = orgManager.getOrgDetail(orgCode);
        accompanyingOrder.setOrgName(orgDetail.getOrgName());
        accompanyingOrder.setOrgCode(orgCode);
        accompanyingOrder.setDept(dept);
        accompanyingOrder.setStatus(1);
        Integer cityId = new Integer(orgDetail.getCity());
        accompanyingOrder.setCityId(cityId);
        accompanyingOrder.setDelFlag(0);
        accompanyingOrder.setCityName(baseInfoManager.getAreaById(cityId).getName());
        AccompanyingPrice accompanyingPrice = accompanyingPriceManager.getByOrgCode(orgCode);
        Integer level = model.getLevel();
        BigDecimal accompanyingPrice1 = accompanyingPrice.getAccompanyingPrice();
        accompanyingOrder.setAccompanyingPrice(accompanyingPrice1);
        if (level == 1){
            BigDecimal gradeOnePrice = accompanyingPrice.getGradeOnePrice();
            accompanyingOrder.setServiceCharge(gradeOnePrice);
            if (isAccompanying == 1){
                accompanyingOrder.setTotalBalance(gradeOnePrice.add(accompanyingPrice1));
            }else {
                accompanyingOrder.setTotalBalance(gradeOnePrice);
            }
        }
        else if (level == 2){
            BigDecimal gradeTwoPrice = accompanyingPrice.getGradeTwoPrice();
            accompanyingOrder.setServiceCharge(gradeTwoPrice);
            if (isAccompanying == 1){
                accompanyingOrder.setTotalBalance(gradeTwoPrice.add(accompanyingPrice1));
            }else {
                accompanyingOrder.setTotalBalance(gradeTwoPrice);
            }
        }
        else if (level == 3){
            BigDecimal gradeThreePrice = accompanyingPrice.getGradeThreePrice();
            accompanyingOrder.setServiceCharge(gradeThreePrice);
            if (isAccompanying == 1){
                accompanyingOrder.setTotalBalance(gradeThreePrice.add(accompanyingPrice1));
            }else {
                accompanyingOrder.setTotalBalance(gradeThreePrice);
            }
        }else {
            BigDecimal gradeFourPrice = accompanyingPrice.getGradeFourPrice();
            accompanyingOrder.setServiceCharge(gradeFourPrice);
            if (isAccompanying == 1){
                accompanyingOrder.setTotalBalance(gradeFourPrice.add(accompanyingPrice1));
            }else {
                accompanyingOrder.setTotalBalance(gradeFourPrice);
            }
        }
        AccompanyingOrder accompanyingOrder1 = accompanyingOrderManager.addAccompanyingOrder(accompanyingOrder);
        if (accompanyingOrder1 != null){
            return ResponseUtil.setSuccessResult(accompanyingOrder1.getOrderCode());
        }
        return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER_ERR,"创建订单失败");
    }

    @ApiOperation(value = "删除创建订单")
    @GetMapping("deleteOrder")
    public ResponseResult delete(@AccessToken AccessInfo info,@RequestParam("orderCode") String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setOrderCode(orderCode);
        // 删除标记
        accompanyingOrder.setDelFlag(1);
        accompanyingOrder.setModifyBy(info.getPatientId());
        accompanyingOrder.setModifyAt(LocalDateTime.now().toString());
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "添加价格")
    @GetMapping("addAccompanyingPrice")
    public ResponseResult addAccompanyingPrice(@RequestBody AccompanyingPrice accompanyingPrice){
        accompanyingPriceManager.save(accompanyingPrice);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取价格")
    @GetMapping("getAccompanyingPrice")
    public ResponseResult addAccompanyingPrice(@RequestParam("orgCode") Integer orgCode){
        return ResponseUtil.setSuccessResult(accompanyingPriceManager.getByOrgCode(orgCode));
    }


    @ApiOperation(value = "创建附属订单")
    @PostMapping("addAccompanyingOrderConfirmation")
    public ResponseResult addAccompanyingOrderConfirmation(@RequestBody AddAccompanyingOrderConfirmationModel model,@AccessToken AccessInfo info){
        addAccompanyingOrderConfirmationP(model,info);
        return ResponseUtil.setSuccessResult();
    }
    @Transactional
    public void addAccompanyingOrderConfirmationP(AddAccompanyingOrderConfirmationModel model,AccessInfo info){
        AccompanyingOrderConfirmation accompanyingOrderConfirmation = new AccompanyingOrderConfirmation();
        BeanUtils.copyProperties(model,accompanyingOrderConfirmation);
        accompanyingOrderConfirmation.setCreateBy(info.getUserId().toString());
        Dept deptDetail = deptManager.getDeptDetail(model.getDeptId());
        Integer orgCode = deptDetail.getOrgCode();
        Org orgDetail = orgManager.getOrgDetail(orgCode);
        accompanyingOrderConfirmation.setOrgName(orgDetail.getOrgName());
        Integer cityId = new Integer(orgDetail.getCity());
        accompanyingOrderConfirmation.setCityId(cityId);
        accompanyingOrderConfirmation.setCityName(baseInfoManager.getAreaById(cityId).getName());
        accompanyingOrderConfirmationManager.save(accompanyingOrderConfirmation);
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(4);
        accompanyingOrder.setOrderCode(model.getOrderCode());
        accompanyingOrderManager.update(accompanyingOrder);
    }


    @ApiOperation(value = "预约失败")
    @GetMapping("yuyueshibai")
    public ResponseResult yuyueshibai(@RequestParam("orderCode") String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(6);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "标记为已经处理")
    @GetMapping("handling")
    public ResponseResult handling(String orderCode, HttpServletRequest request){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(3);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "标记为用户未就诊")
    @GetMapping("doNotSeeDoc")
    public ResponseResult doNotSeeDoc(@RequestParam("orderCode") String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(9);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "标记为待评价")
    @GetMapping("evaluate")
    public ResponseResult evaluate(@RequestParam("orderCode") String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(10);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "标记为拒绝取消")
    @GetMapping("rejectCancel")
    public ResponseResult rejectCancel(@RequestParam("orderCode") String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(4);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "标记为同意取消")
    @GetMapping("agreedCancel")
    public ResponseResult agreedCancel(@RequestParam("orderCode") String orderCode) {
        return agreedCancelP(orderCode);

    }

    @Transactional
    public ResponseResult agreedCancelP(String orderCode){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(8);
        accompanyingOrder.setOrderCode(orderCode);
        int update = accompanyingOrderManager.update(accompanyingOrder);
        if (update == 1){
            BigDecimal totalBalance = accompanyingOrderManager.getByOrderCode(orderCode).getTotalBalance();
            String fee = totalBalance.multiply(new BigDecimal(100)).intValue()+"";
            Map<String, String> refund = null;
            try {
                refund = wxManager.refund(orderCode, fee, "退款", fee);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException(StatusCode.FAIL,"退款异常");
            }
            String returnCode = refund.get("return_code");
            if (StringUtils.equals("SUCCESS",returnCode)){
                // 退款成功
                //商户退款单号
                String outRefundNo = refund.get("out_refund_no");
                // 微信退款单号
                String refundId = refund.get("refund_id");
                AccompanyingOrder param = new AccompanyingOrder();
                param.setOrderCode(orderCode);
                param.setOtherReturnOrderCode(refundId);
                param.setReturnOrderCode(outRefundNo);
                accompanyingOrderManager.update(param);
                return ResponseUtil.setSuccessResult();
            }else {
                throw new BaseException(StatusCode.FAIL,"退款异常");
            }
        }
        throw new BaseException(StatusCode.FAIL,"退款异常");
    }


    /**
     * 患者查看列表
     * @param info
     * @return
     */
    @ApiOperation(value = "患者查看就医北上广列表")
    @GetMapping("patientOrderList")
    public ResponseResult<PageResult<AccompanyingOrderPatientVo>> patientOrderList(@AccessToken AccessInfo info,@RequestParam("pageSize")  Integer pageSize,@RequestParam("pageNum")  Integer pageNum){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setCreateBy(info.getPatientId());
        accompanyingOrder.setDelFlag(0);
        PageResult<AccompanyingOrder> pageResult = accompanyingOrderManager.selectByAccompanyingOrder(accompanyingOrder,"modifyat DESC",pageNum,pageSize);
        List<AccompanyingOrder> accompanyingOrders = pageResult.getData();
        List<AccompanyingOrderPatientVo> result = Lists.newArrayList();
        PageResult<AccompanyingOrderPatientVo> newResult = new PageResult<>();
        newResult.setPageSize(pageSize);
        newResult.setPageNum(pageNum);
        newResult.setTotal(pageResult.getTotal());
        if (accompanyingOrders.size() != 0){
            List<String> orderCodeList = Lists.newArrayList();
            for (AccompanyingOrder item :accompanyingOrders){
                orderCodeList.add(item.getOrderCode());
            }
            List<AccompanyingOrderConfirmation> accompanyingOrderConfirmations = accompanyingOrderConfirmationManager.getByOrderCodes(orderCodeList);
            for (AccompanyingOrder item :accompanyingOrders){
                String orderCode = item.getOrderCode();
                AccompanyingOrderPatientVo accompanyingOrderPatientVo = new AccompanyingOrderPatientVo();
                accompanyingOrderPatientVo.setStatus(item.getStatus());
                accompanyingOrderPatientVo.setOrderCode(orderCode);
                accompanyingOrderPatientVo.setRealName(item.getRealName());
                accompanyingOrderPatientVo.setStartTime(item.getStartTime());
                accompanyingOrderPatientVo.setEndTime(item.getEndTime());
                accompanyingOrderPatientVo.setPrice(NumberFormtUtil.toString2decimal(item.getTotalBalance()));
                accompanyingOrderPatientVo.setHopeHospitalName(item.getOrgName());
                accompanyingOrderPatientVo.setHopeDepartmentName(item.getDept());
                accompanyingOrderPatientVo.setHopeCityName(item.getCityName());
                accompanyingOrderPatientVo.setPoint(item.getPoint());
                accompanyingOrderPatientVo.setCardType(item.getCardType());
                accompanyingOrderPatientVo.setCardNo(item.getCardNo());
                accompanyingOrderPatientVo.setCreateTime(item.getCreateAt());
                accompanyingOrderPatientVo.setPayType(item.getPayType());
                accompanyingOrderPatientVo.setRemark(item.getRemark());
                accompanyingOrderPatientVo.setMobile(item.getMobile());
                if (accompanyingOrderConfirmations.size() != 0){
                    for (AccompanyingOrderConfirmation ittem : accompanyingOrderConfirmations){
                        accompanyingOrderPatientVo.setDoctorName(ittem.getDoctorName());
                        accompanyingOrderPatientVo.setRealCityName(ittem.getCityName());
                        accompanyingOrderPatientVo.setRealHospitalName(ittem.getOrgName());
                        accompanyingOrderPatientVo.setRealDepartmentName(ittem.getDept());
                        accompanyingOrderPatientVo.setTime(ittem.getTime());
                        accompanyingOrderPatientVo.setAccompanyingMobile(ittem.getMobile());
                        accompanyingOrderPatientVo.setAccompanyingName(ittem.getAccompanyingName());
                    }
                }
                result.add(accompanyingOrderPatientVo);
            }
        }
        newResult.setData(result);
        return ResponseUtil.setSuccessResult(newResult);
    }

    @ApiOperation(value = "管理员和渠道查看订单列表")
    @GetMapping("suppliersOrderList")
    public ResponseResult<PageResult<SuppliersOrderVo>> suppliersOrderList(@RequestParam("status")  Integer status,@RequestParam("type")  Integer type,@RequestParam("pageSize")  Integer pageSize,@RequestParam("pageNum")  Integer pageNum){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(status);
        accompanyingOrder.setDelFlag(0);
        String orderBy = "createat DESC";
        if (type == 1){
            orderBy = "starttime DESC";
        }
        PageResult<AccompanyingOrder> accompanyingOrderPageResult = accompanyingOrderManager.selectByAccompanyingOrder(accompanyingOrder,orderBy,pageNum,pageSize);
        List<AccompanyingOrder> accompanyingOrders = accompanyingOrderPageResult.getData();
        List<SuppliersOrderVo> result = Lists.newArrayList();
        PageResult<SuppliersOrderVo> res = new PageResult<>();
        if (accompanyingOrders.size() != 0){
            List<String> orderCodeList = new ArrayList<>();
            for (AccompanyingOrder item :accompanyingOrders){
                orderCodeList.add(item.getOrderCode());
            }
            List<AccompanyingOrderConfirmation> accompanyingOrderConfirmations = accompanyingOrderConfirmationManager.getByOrderCodes(orderCodeList);
            for (AccompanyingOrder item :accompanyingOrders){
                String orderCode = item.getOrderCode();
                SuppliersOrderVo suppliersOrderVo = new SuppliersOrderVo();
                suppliersOrderVo.setStatus(item.getStatus());
                suppliersOrderVo.setOrderCode(orderCode);
                suppliersOrderVo.setRealName(item.getRealName());
                suppliersOrderVo.setStartTime(item.getStartTime());
                suppliersOrderVo.setEndTime(item.getEndTime());
                suppliersOrderVo.setPrice(NumberFormtUtil.toString2decimal(item.getTotalBalance()));
                suppliersOrderVo.setHopeHospitalName(item.getOrgName());
                suppliersOrderVo.setHopeDepartmentName(item.getDept());
                suppliersOrderVo.setHopeCityName(item.getCityName());
                suppliersOrderVo.setPoint(item.getPoint());
                suppliersOrderVo.setCardType(item.getCardType());
                suppliersOrderVo.setCardNo(item.getCardNo());
                suppliersOrderVo.setCreateTime(item.getCreateAt());
                suppliersOrderVo.setPayType(item.getPayType());
                suppliersOrderVo.setRemark(item.getRemark());
                suppliersOrderVo.setMobile(item.getMobile());
                suppliersOrderVo.setCaseDetail(item.getCaseDetail());
                suppliersOrderVo.setCasePictures(item.getCasePictures());
                suppliersOrderVo.setIsAccompanying(item.getIsAccompanying());
                suppliersOrderVo.setLevel(item.getLevel());
                if (accompanyingOrderConfirmations.size() != 0){
                    for (AccompanyingOrderConfirmation ittem : accompanyingOrderConfirmations){
                        suppliersOrderVo.setDoctorName(ittem.getDoctorName());
                        suppliersOrderVo.setRealCityName(ittem.getCityName());
                        suppliersOrderVo.setRealHospitalName(ittem.getOrgName());
                        suppliersOrderVo.setRealDepartmentName(ittem.getDept());
                        suppliersOrderVo.setTime(ittem.getTime());
                        suppliersOrderVo.setAccompanyingMobile(ittem.getMobile());
                        suppliersOrderVo.setAccompanyingName(ittem.getAccompanyingName());
                        suppliersOrderVo.setServicePrice(NumberFormtUtil.toString2decimal(ittem.getServiceCharge()));
                        suppliersOrderVo.setAccompanyingPrice(NumberFormtUtil.toString2decimal(ittem.getAccompanyingPrice()));
                    }
                }
                result.add(suppliersOrderVo);
            }
        }
        res.setTotal(accompanyingOrderPageResult.getTotal());
        res.setPageNum(pageNum);
        res.setPageSize(pageSize);
        res.setData(result);
        return ResponseUtil.setSuccessResult(res);
    }

    @ApiOperation(value = "接受邀请")
    @GetMapping("accessInvitation")
    public ResponseResult accessInvitation(@AccessToken AccessInfo info,@RequestParam("invitationCode")String invitationCode){
        String patientId = info.getPatientId();
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setCreateBy(patientId);
        // 查询是否有订单
        List<AccompanyingOrder> accompanyingOrders = accompanyingOrderManager.selectByAccompanyingOrder(accompanyingOrder);
        if (accompanyingOrders.size() == 0){
            AccompanyingInvitation accompanyingInvitation = new AccompanyingInvitation();
            accompanyingInvitation.setPatientId(patientId);
            accompanyingInvitation.setInvitationCode(invitationCode);
            accompanyingInvitationManager.addAccompanyingInvitation(accompanyingInvitation);
        }
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "医生接受邀请")
    @GetMapping("doctorAccessInvitation")
    public ResponseResult doctorAccessInvitation(@AccessToken AccessInfo info,@RequestParam("invitationCode")String invitationCode){
        Integer userId = info.getUserId();
        AccompanyingInvitation accompanyingInvitation = new AccompanyingInvitation();
        accompanyingInvitation.setUserId(userId);
        accompanyingInvitation.setInvitationCode(invitationCode);
        accompanyingInvitationManager.addAccompanyingInvitation(accompanyingInvitation);
        return ResponseUtil.setSuccessResult();
    }





    /**
     * 取消预约
     */
    @ApiOperation(value = "取消预约")
    @GetMapping("cancelReservation")
    public ResponseResult cancelReservation(@RequestParam("orderCode") String orderCode,@AccessToken AccessInfo info){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(7);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrder.setModifyBy(info.getPatientId());
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }


    /**
     * 取消订单
     * @param orderCode
     * @return
     */
    @ApiOperation(value = "取消订单")
    @GetMapping("cancelOrder")
    public ResponseResult cancelOrder(@RequestParam("orderCode") String orderCode,@AccessToken AccessInfo info) throws Exception {
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(5);
        accompanyingOrder.setOrderCode(orderCode);
        accompanyingOrder.setModifyBy(info.getPatientId());
        return cancelOrderP(accompanyingOrder,orderCode);

    }
    @Transactional
    public ResponseResult cancelOrderP(AccompanyingOrder accompanyingOrder,String orderCode){
        int update = accompanyingOrderManager.update(accompanyingOrder);
        if (update == 1){
            BigDecimal totalBalance = accompanyingOrderManager.getByOrderCode(orderCode).getTotalBalance();
            String fee = totalBalance.multiply(new BigDecimal(100)).intValue()+"";
            Map<String, String> refund = null;
            try {
                refund = wxManager.refund(orderCode, fee, "退款", fee);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException(StatusCode.FAIL,"退款异常");
            }
            String returnCode = refund.get("return_code");
            String resultCode = refund.get("result_code");
            if (StringUtils.equals("SUCCESS",returnCode) && StringUtils.equals("SUCCESS",resultCode)){
                // 退款成功
                //商户退款单号
                String outRefundNo = refund.get("out_refund_no");
                // 微信退款单号
                String refundId = refund.get("refund_id");
                AccompanyingOrder param = new AccompanyingOrder();
                param.setOrderCode(orderCode);
                param.setOtherReturnOrderCode(refundId);
                param.setReturnOrderCode(outRefundNo);
                accompanyingOrderManager.update(param);
                return ResponseUtil.setSuccessResult();
            }else {
                throw new BaseException(StatusCode.FAIL,"退款异常");
            }
        }
        throw new BaseException(StatusCode.FAIL,"退款异常");
    }




    /**
     * 评价订单
     * @param appraiseAccompanyingOrderModel 参数
     * @return resultBody
     */
    @ApiOperation(value = "评价订单")
    @PostMapping("appraiseAccompanyingOrder")
    public ResponseResult appraiseAccompanyingOrder(@AccessToken AccessInfo info,@RequestBody AppraiseAccompanyingOrderModel appraiseAccompanyingOrderModel){
        AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
        accompanyingOrder.setStatus(10);
        accompanyingOrder.setOrderCode(appraiseAccompanyingOrderModel.getOrderCode());
        accompanyingOrder.setModifyBy(info.getPatientId());
        accompanyingOrder.setPoint(accompanyingOrder.getPoint());
        accompanyingOrderManager.update(accompanyingOrder);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "消息")
    @GetMapping("msg")
    public ResponseResult<Map<String,Long>> getMsg(){
        String orderBy = "createat DESC";
        AccompanyingOrder accompanyingOrder1 = new AccompanyingOrder();
        accompanyingOrder1.setStatus(2);
        AccompanyingOrder accompanyingOrder2 = new AccompanyingOrder();
        accompanyingOrder2.setStatus(7);
        Long total = accompanyingOrderManager.selectByAccompanyingOrder(accompanyingOrder1, orderBy, 1, 1).getTotal();
        Long total1 = accompanyingOrderManager.selectByAccompanyingOrder(accompanyingOrder2, orderBy, 1, 1).getTotal();
        Map<String,Long> map = new HashMap<>(2);
        map.put("quxiao",total1);
        map.put("daichuli",total);
        return ResponseUtil.setSuccessResult(map);
    }



    @ApiOperation(value = "预约成功列表")
    @GetMapping("getList")
    public ResponseResult<PageResult<InvitationDto>> getList(@RequestParam("pageNum")Integer pageNum, @RequestParam("pageSize")Integer pageSize, @AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(accompanyingInvitationManager.selectAccompanyingInvitationByUserId(info.getUserId(),pageNum,pageSize));
    }

    @ApiOperation(value = "设置价格")
    @PostMapping("setprice")
    public ResponseResult setprice(@RequestBody AccompanyingPrice accompanyingPrice){
        accompanyingPriceManager.save(accompanyingPrice);
        return ResponseUtil.setSuccessResult();
    }
}
