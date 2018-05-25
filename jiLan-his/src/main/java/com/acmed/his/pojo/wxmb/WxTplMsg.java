package com.acmed.his.pojo.wxmb;

import lombok.Data;

/**
 * WxTplMsg
 *
 * @author jimson
 * @date 2018/5/25
 */
@Data
public class WxTplMsg {

    /**
     * touser : OPENID
     * template_id : ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY
     * url : http://weixin.qq.com/download
     * miniprogram : {"appid":"xiaochengxuappid12345","pagepath":"index?foo=bar"}
     * data : {"first":{"value":"恭喜你购买成功！","color":"#173177"},"keyword1":{"value":"巧克力","color":"#173177"},"keyword2":{"value":"39.8元","color":"#173177"},"keyword3":{"value":"2014年9月22日","color":"#173177"},"remark":{"value":"欢迎再次购买！","color":"#173177"}}
     */

    private String touser;
    private String template_id;
    private String url;
    private MiniprogramBean miniprogram;
    private DataBean data;
    @Data
    public static class MiniprogramBean {
        /**
         * appid : xiaochengxuappid12345
         * pagepath : index?foo=bar
         */

        private String appid;
        private String pagepath;

    }
    @Data
    public static class DataBean {
        /**
         * first : {"value":"恭喜你购买成功！","color":"#173177"}
         * keyword1 : {"value":"巧克力","color":"#173177"}
         * keyword2 : {"value":"39.8元","color":"#173177"}
         * keyword3 : {"value":"2014年9月22日","color":"#173177"}
         * remark : {"value":"欢迎再次购买！","color":"#173177"}
         */

        private FirstBean first;
        private Keyword1Bean keyword1;
        private Keyword2Bean keyword2;
        private Keyword3Bean keyword3;
        private RemarkBean remark;

        @Data
        public static class FirstBean {
            /**
             * value : 恭喜你购买成功！
             * color : #173177
             */

            private String value;
            private String color;
        }

        @Data
        public static class Keyword1Bean {
            /**
             * value : 巧克力
             * color : #173177
             */

            private String value;
            private String color;
        }

        @Data
        public static class Keyword2Bean {
            /**
             * value : 39.8元
             * color : #173177
             */

            private String value;
            private String color;
        }

        @Data
        public static class Keyword3Bean {
            /**
             * value : 2014年9月22日
             * color : #173177
             */

            private String value;
            private String color;

        }

        @Data
        public static class RemarkBean {
            /**
             * value : 欢迎再次购买！
             * color : #173177
             */

            private String value;
            private String color;
        }
    }
}
