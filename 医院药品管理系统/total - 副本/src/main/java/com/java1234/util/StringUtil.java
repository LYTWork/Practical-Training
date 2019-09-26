package com.java1234.util;

/*
 * 字符串工具类
 * @author java1234 AT
 *
 */
public class StringUtil {

	/*
	 * 判断是否是空
	 */
	public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
	}
	
	/*
	 * 判断是否不是空
	 */
	public static boolean isNotEmpty(String str){
        return (str != null) && !"".equals(str.trim());
	}
	

	/*
	 * 生成四位编号 controller
	 */
	public static String formatCode(String code){
		try {
			int length = code.length();
			Integer num = Integer.valueOf(code.substring(length-4,length))+1;//最后四位数自增一
			String codenum = num.toString();
			int codelength = codenum.length();
			for (int i = 4; i > codelength; i--) {
				codenum = "0"+codenum;
			}
			return codenum;
		} catch (NumberFormatException e) {
			return "0100";
		}
	}

}
