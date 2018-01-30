package com.acmed.his.api;

import com.acmed.his.config.MyWXPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * CallBackApi
 * 回调api
 * @author jimson
 * @date 2017/12/6
 */
@Controller
@RequestMapping("callback")
@Api(tags = "回调",hidden = true)
@ApiIgnore
public class CallBackApi {
    @RequestMapping(value = "wxPay")
    public void wxPay(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String notifyData = "...."; // 支付结果通知的xml格式数据

        MyWXPayConfig config = new MyWXPayConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
        String respString = "FAIL";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            // TODO 进行处理。
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            respString = "SUCCESS";
        }
        else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            respString = "FAIL";
        }
        resp.getWriter().write(respString);
    }

    /**
     * 退款通知
     * @param req
     * @param resp
     * @throws Exception
     */
    @RequestMapping(value = "wxPayRefund")
    public void wxPayRefund(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // TODO  业务处理



        //SUCCESS/FAIL
        //SUCCESS表示商户接收通知成功并校验成功
        String returnCode = "";
        //返回信息，如非空，为错误原因
        //        参数格式校验错误
        String returnMsg = "";
        String result = "<xml> \n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
        resp.getWriter().write(result);
    }

}
