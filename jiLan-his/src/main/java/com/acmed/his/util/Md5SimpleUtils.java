package com.acmed.his.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5SimpleUtils {

    private static Logger logger = LoggerFactory.getLogger(Md5SimpleUtils.class);
    public static String md5One(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        md.update(s.getBytes());
        return byteArrayToHexString(md.digest());
    }

    public static String md5Remove(String s) {
        String result = md5One(s);
        result = result.substring(0, result.length() - 1);
        return result;
    }

    private static String[] HexCode = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result = result + byteToHexString(b[i]);
        }
        return result;
    }

    public static void main(String args[]) {
        try {

            System.out.println(Md5SimpleUtils.md5One("afb547a21c3f53291b23d39f25e13269"));
            //System.out.println(Md5SimpleUtils.md5Remove("123456"));


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
