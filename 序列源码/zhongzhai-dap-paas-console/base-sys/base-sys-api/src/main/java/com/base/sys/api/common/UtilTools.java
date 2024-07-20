package com.base.sys.api.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UtilTools {
	private UtilTools(){

		//
	}
	  public static  String getNum(){
		 String numStr = "" ;
		 String trandStr = String.valueOf((Math.random() * 9 + 1) * 1000000);
		 String dataStr = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
		 numStr = trandStr.substring(0, 4);
		 numStr = numStr + dataStr ;
		 return numStr ;
		}
	  public static String getUUID32(){
		  return UUID.randomUUID().toString().replace("-", "").toUpperCase() ;
	  }
}
