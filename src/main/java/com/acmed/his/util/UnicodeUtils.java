package com.acmed.his.util;


/**
 * 
* @ClassName: UnicodeUtils 
* @Description: unicode转码
* @author Eric
* @date 2016年4月6日 上午9:56:11 
*
 */
public class UnicodeUtils {
	
	/**
	 * 
	* @Title: encode 
	* @Description: Unicode对转码，对英文数字符号保留
	* @Author ： Eric
	* @Date：2016年4月6日 上午9:57:05
	* @param @param bytes
	* @param @return
	* @param @throws Exception
	* @return String
	* @throws
	 */
    public static String encode(String str) throws Exception {
		String result="";  
        for (int i = 0; i < str.length(); i++){
            int chr1 = str.charAt(i);  
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else if(chr1>=65280&&chr1<=65519){//汉字范围 \uFF00-\uFFEF (全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else if(chr1>=11904&&chr1<=42191){//汉字范围 \u2E80-\uA4CF (标点符号、特殊字符)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else if(chr1>=8526&&chr1<=8591){//罗马字符
                result+="\\u" + Integer.toHexString(chr1);  
            }else if(chr1>=8704&&chr1<=8905){//符号
                result+="\\u" + Integer.toHexString(chr1);  
            }else if(chr1==8198){//ios中文下输入英文引起的空格符号
                result+="\\u" + Integer.toHexString(chr1);  
            }else{
                result+=str.charAt(i);  
            }  
        }  
        return result;
    }  
}