package com.acmed.his.service;

import com.acmed.his.model.Org;
import com.acmed.his.model.User;
import com.acmed.his.pojo.wxmb.WxTplMsg;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * WxMsgManager
 *
 * @author jimson
 * @date 2018/5/25
 */
@Slf4j
@Service
public class WxMsgManager {

    @Autowired
    private WxManager wxManager;

    @Autowired
    private OrgManager orgManager;

    @Autowired
    private UserManager userManager;

    /**
     * 每日下午五点消息推送
     */
    @Scheduled(cron="0 0 17 * * ?")
    public void task5pm(){
        String time = LocalDate.now().toString();
        Org org = new Org();
        org.setRemoved("0");
        org.setIsRecommend("0");
        List<Org> list = orgManager.getList(org);
        List<Integer> uIdList = new ArrayList<>();
        if (list.size()!=0){
            for (Org item :list){
                uIdList.add(new Integer(item.getManager()));
            }
        }
        if (uIdList.size()!=0){
            List<User> listByIds = userManager.getListByIds(uIdList);
            for (User item : listByIds){
                if (StringUtils.isNotEmpty(item.getOpenid())){

                    WxTplMsg wxTplMsg = new WxTplMsg();
                    wxTplMsg.setTouser(item.getOpenid());
                    wxTplMsg.setTemplate_id("46GwUsQOFuJc3JPjOT0I_fqmCoVLwISPIDEWun__Eyc");
                    WxTplMsg.DataBean dataBean = new WxTplMsg.DataBean();
                    WxTplMsg.DataBean.FirstBean firstBean = new WxTplMsg.DataBean.FirstBean();
                    firstBean.setColor("#173177");
                    firstBean.setValue("您好，您的诊所今天已经采购云药房的药品，为保证药品及时送达，请尽快完成支付。详情如下");
                    WxTplMsg.DataBean.Keyword1Bean keyword1 = new WxTplMsg.DataBean.Keyword1Bean();
                    keyword1.setColor("#173177");
                    keyword1.setValue(item.getUserName());
                    WxTplMsg.DataBean.Keyword2Bean keyword2 = new WxTplMsg.DataBean.Keyword2Bean();
                    keyword2.setColor("#173177");
                    keyword2.setValue("药品费用");
                    WxTplMsg.DataBean.Keyword3Bean keyword3 = new WxTplMsg.DataBean.Keyword3Bean();
                    keyword3.setColor("#173177");
                    keyword3.setValue(time);
/*        WxTplMsg.DataBean.RemarkBean remarkBean = new WxTplMsg.DataBean.RemarkBean();
        remarkBean.setColor("#173177");
        remarkBean.setValue();*/
                    dataBean.setFirst(firstBean);
                    dataBean.setKeyword1(keyword1);
                    dataBean.setKeyword2(keyword2);
                    dataBean.setKeyword3(keyword3);
                    //dataBean.setRemark(remarkBean);
                    wxTplMsg.setData(dataBean);
                    try {
                        this.sendtplmsg(wxTplMsg);
                    }catch (Exception e){
                        log.error("userId:"+item.getId()+"发送模板消息异常");
                        log.error(e.toString());
                    }
                }
            }
        }
    }


    public void sendtplmsg(WxTplMsg wxTplMsg){
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",wxManager.getBaseAccessToken());
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSON(wxTplMsg).toString(), headers);
        String result = restTemplate.postForObject(url, formEntity, String.class);
    }
}
