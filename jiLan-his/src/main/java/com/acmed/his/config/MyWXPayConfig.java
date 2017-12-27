package com.acmed.his.config;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.*;


/**
 * MyWXPayConfig
 * 微信配置文件
 * @author jimson
 * @date 2017/12/6
 */
public class MyWXPayConfig implements WXPayConfig {
    private byte[] certData;
    private static MyWXPayConfig INSTANCE;

    public MyWXPayConfig() throws Exception {
        String certPath = "src/main/resources/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static MyWXPayConfig getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (MyWXPayConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyWXPayConfig();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 微信号id
     * @return
     */
    @Override
    public String getAppID() {
        return "wx3d71d42f9956d861";
    }
    /**
     * 商户号
     * @return
     */
    @Override
    public String getMchID() {
        return "1492994072";
    }
    /**
     * 支付秘钥
     * @return
     */
    @Override
    public String getKey() {
        return "d165120bf9b94a47882228eadb7af40d";
    }
    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }
    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }
    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
