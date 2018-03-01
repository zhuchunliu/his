package com.acmed.his.util;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;


public class UUIDUtil
{

  
  public static String generate()
  {
	    return String.format("%s%s",StringUtils.replace(UUID.randomUUID().toString(), "-", ""),
                RandomUtil.generateString(4));
  }

  public static String generate32() {
    return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
  }
  
  public static void main(String[] args)
  {
    System.out.println(generate()+"  "+generate().length());
  }
}