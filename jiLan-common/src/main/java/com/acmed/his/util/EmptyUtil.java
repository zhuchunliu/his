package com.acmed.his.util;/**
 * Created by Eric on 2017-05-18.
 */

/**
 * 判断是否为空
 *
 * @Author Eric
 * @Version 2017-05-18 11:32
 **/

public class EmptyUtil {

    /**
     * 判断字符串是否为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input){
        if(input == null || "".equals(input)){
            return true;
        }
        return false;
    }


    public static boolean isEmpty(Object o)
    {
        if (o == null) {
            return true;
        }
        if ((o instanceof String))
        {
            String str = (String)o;
            if (str.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(Object o)
    {
        return !isEmpty(o);
    }

    public static boolean isNotBlank(Object o)
    {
        return !isBlank(o);
    }

    public static boolean isBlank(Object o)
    {
        if (o == null) {
            return true;
        }
        if ((o instanceof String))
        {
            String str = (String)o;
            return isBlank(str);
        }
        return false;
    }

    public static boolean isBlank(String str)
    {
        if ((str == null) || (str.length() == 0)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
