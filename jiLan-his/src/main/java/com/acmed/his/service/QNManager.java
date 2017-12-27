package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.vo.UpTokenAndKeyVo;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.acmed.his.util.WaterCodeUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * QNManager
 *
 * @author jimson
 * @date 2017/12/27
 */
@Service
public class QNManager {
    @Autowired
    private Environment environment;


    /**
     * 获取上传token
     * @param type 类型
     * @return DTOUPTokenAndKey
     */
    public UpTokenAndKeyVo getUpToken(Integer type){
        Auth auth = Auth.create(environment.getProperty("qiniu.accessKey"),environment.getProperty("qiniu.secretKey"));
        String key = null;
        switch (type){
            case 1 :
                key = "image/jibing/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//大病图片路径
                break;
            case 2 :
                key = "image/idcard/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//身份证图片路径
                break;
            case 3 :
                key = "image/practice/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//执业图片路径
                break;
            case 4 :
                key = "image/professional/qualification/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//执业资格图片路径
                break;
            case 5 :
                key = "image/user/headimg/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//头像
                break;
            case 6 :
                key = "image/prize/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//奖品
                break;
            case 7 :
                key = "image/medal/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//勋章
                break;
            case 8 :
                key = "image/alarm/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//医闹图片路径
                break;
            case 9 :
                key = "image/sudden/death/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//猝死图片路径
                break;
            case 10 :
                key = "image/accompanying/casedetail/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//病情详情图
                break;
            default:
                key = "image/other/" + System.currentTimeMillis() + "/" + WaterCodeUtil.getSixSmsCode();//其他图
        }
        String upToken = auth.uploadToken(environment.getProperty("qiniu.bucket"),key,10,null);
        UpTokenAndKeyVo dtoupTokenAndKey = new UpTokenAndKeyVo();
        //key   七牛云上的路径
        dtoupTokenAndKey.setKey(key);
        //上传的token
        dtoupTokenAndKey.setUpToken(upToken);
        return dtoupTokenAndKey;
    }


    /**
     * 上传图片
     * @param url 图片网络路径
     * @return ResultBody<DTOKey>
     */
    public ResponseResult<String> uploadByUrl(String url){
        String accessKey = environment.getProperty("qiniu.accessKey");
        String secretKey = environment.getProperty("qiniu.secretKey");
        String bucket = environment.getProperty("qiniu.bucket");
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        String key = "image/user/headimg/"+System.currentTimeMillis()+"/"+WaterCodeUtil.getSixSmsCode();
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //抓取网络资源到空间
        try {
            FetchRet fetchRet = bucketManager.fetch(url,bucket,key);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_QINIU_ERR,ex.response.toString());
        }
        return ResponseUtil.setSuccessResult(key);
    }


    /**
     * 获取上传token
     * @return token
     */
    private String getToken(){
        String accessKey = environment.getProperty("qiniu.accessKey");
        String secretKey = environment.getProperty("qiniu.secretKey");
        long expires = 60;//单位秒
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(environment.getProperty("qiniu.bucket"),null,expires,null);
        return upToken;
    }
}
