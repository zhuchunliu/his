package com.acmed.his.util;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;


public class UUIDUtil
{

  
  public static String generate()
  {
	  String str=UUID.randomUUID().toString();
	    str = StringUtils.replace(str, "-", "");
	    return str;
  }
  
  public static void main(String[] args)
  {
    System.out.println(generate());
  }
}