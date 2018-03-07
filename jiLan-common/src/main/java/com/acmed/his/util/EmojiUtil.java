package com.acmed.his.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jimson_yu on 2017/6/16.
 */
public class EmojiUtil {

            /**
    将emoji标签转换成utf8字符集保存进数据库
    @param str 需要转换的字符串
     @return
             */
    public static String emojiConvert(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            try {
                matcher.appendReplacement(sb,"[[" + URLEncoder.encode(matcher.group(1),"UTF-8") + "]]");
            } catch(UnsupportedEncodingException e) {
                //logger.error("emojiConvert error"+ e.getMessage());
                return str;
            }
        }
        matcher.appendTail(sb);
        //logger.debug("emojiConvert " + str + " to " + sb.toString() + ", len：" + sb.length());
        return sb.toString();
    }
      /**
    @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
    @param str
    转换后的字符串
    @return 转换前的字符串
     */
    public static String emojiRecovery(String str) {
        String patternString = "\\[\\[(.*?)\\]\\]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            try {
                matcher.appendReplacement(sb,
                        URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                //logger.error("emojiRecovery error"+ e.getMessage());
                return "";
            }
        }
        matcher.appendTail(sb);
        //logger.debug("emojiRecovery " + str + " to " + sb.toString());
        return sb.toString();
    }










/*

    *//**
     * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
     * @param str
     *            待转换字符串
     * @return 转换后字符串
     * @throws UnsupportedEncodingException
     *             exception
     *//*
    public static String emojiConvert1(String str)
            throws UnsupportedEncodingException {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            try {
                matcher.appendReplacement(
                        sb,
                        "[["
                                + URLEncoder.encode(matcher.group(1),
                                "UTF-8") + "]]");
            } catch(UnsupportedEncodingException e) {
                //LOG.error("emojiConvert error", e);
                throw e;
            }
        }
        matcher.appendTail(sb);
        //LOG.debug("emojiConvert " + str + " to " + sb.toString()
                //+ ", len：" + sb.length());
        return sb.toString();
    }

    *//**
     * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
     * @param str
     *            转换后的字符串
     * @return 转换前的字符串
     * @throws UnsupportedEncodingException
     *             exception
     *//*
    public static String emojiRecovery2(String str)
            throws UnsupportedEncodingException {
        String patternString = "\\[\\[(.*?)\\]\\]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            try {
                matcher.appendReplacement(sb,
                        URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                //LOG.error("emojiRecovery error", e);
                throw e;
            }
        }
        matcher.appendTail(sb);
        //LOG.debug("emojiRecovery " + str + " to " + sb.toString());
        return sb.toString();
    }*/
}
